package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

import java.io.File;

import fr.limsi.rorqual.core.model.IfcHelper;
import fr.limsi.rorqual.core.model.IfcHolder;
import fr.limsi.rorqual.core.utils.DefaultMutableTreeNode;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;
import fr.limsi.rorqual.core.view.shaders.ShaderAttribute;
import ifc2x3javatoolbox.helpers.IfcSpatialStructure;
import ifc2x3javatoolbox.ifcmodel.IfcModel;

/**
 * Created by christophe on 20/03/15.
 */
public class Logic implements InputProcessor {

    private enum State {
        NONE,
        WALL,
        ;
    }

    private Camera camera = null;

    private State currentState = State.NONE;

    private ModelGraph modelGraph;

    private Logic() {}

    /** Holder */
    private static class LogicHolder
    {
        /** Instance unique non préinitialisée */
        private final static Logic INSTANCE = new Logic();
    }

    public static synchronized Logic getInstance() {
        return LogicHolder.INSTANCE;
    }

    public void setCamera(Camera c) {
        camera = c;
    }

    public void startWall() {
        currentState = State.WALL;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    final Vector3 start = new Vector3();
    final Vector3 end = new Vector3();
    ModelContainer wall;
    boolean making_wall = false;
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (currentState == State.WALL) {
            Ray ray = camera.getPickRay(screenX, screenY);
            Plane plane = new Plane(Vector3.Z.cpy(), Vector3.Zero.cpy()); /// floor

            Vector3 intersection = new Vector3();
            boolean intersect = Intersector.intersectRayPlane(ray, plane, intersection);
            if (!intersect) {
                making_wall = false;
                return false;
            } else {
                making_wall = true;
            }

            start.set(intersection);
            end.set(intersection);
            Vector3 dir = end.cpy().sub(start);

            float y = 0.2f;
            float z = 2.8f;
            float x = dir.len();
            float[] vertices = new float[]{
                    0,0,0,
                    0,0,z,
                    0,y,z,
                    0,y,0,
                    x,y,0,
                    x,0,0,
                    x,0,z,
                    x,y,z
            };
            short[] indices = new short[]{
                    0,1,2, 2,3,0,
                    0,3,4, 4,5,0,
                    0,5,6, 6,1,0,

                    1,6,7, 7,2,1,
                    7,4,3, 3,2,7,
                    4,7,6, 6,5,4
            };

            Mesh mesh = new Mesh(true, 8, 6*2*3,
                    new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));
            mesh.setVertices(vertices);
            mesh.setIndices(indices);

            Material material = new Material();
            material.set(ColorAttribute.createDiffuse(Color.TEAL));

            ModelBuilder modelBuilder = new ModelBuilder();
            modelBuilder.begin();
            MeshPartBuilder meshBuilder;
            meshBuilder = modelBuilder.part("part1", GL20.GL_TRIANGLES, mesh.getVertexAttributes(), material);
            meshBuilder.addMesh(mesh);

            Model model = modelBuilder.end();

            wall = new ModelContainer(new ModelInstance(model)) {
                public void act() {
                    super.act();
                    Mesh mesh = super.model.nodes.get(0).parts.get(0).meshPart.mesh;

                    float y = 0.2f;
                    float z = 2.8f;
                    float x = Logic.this.end.cpy().sub(Logic.this.start).len();
                    float[] vertices = new float[]{
                            0,0,0,
                            0,0,z,
                            0,y,z,
                            0,y,0,
                            x,y,0,
                            x,0,0,
                            x,0,z,
                            x,y,z
                    };
                    mesh.setVertices(vertices);

                    if (!start.equals(end)) {
                        Matrix4 mx = new Matrix4().idt();

                        float angle = new Vector2(end.x-start.x, end.y-start.y).angle();

                        mx.translate(start).rotate(0, 0, 1, angle);

                        this.transform = mx;
                    }
                }
            };

            modelGraph.getRoot().add(wall);

            return true;
        } else
            return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (currentState == State.WALL && making_wall) {
            modelGraph.getRoot().remove(wall);
            if (!start.equals(end))
                IfcHolder.getInstance().getHelper().addWall(start, end, 0.2);
            return true;
        } else
            return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (currentState == State.WALL && making_wall) {
            Ray ray = camera.getPickRay(screenX, screenY);
            Plane plane = new Plane(Vector3.Z.cpy(), Vector3.Zero.cpy()); /// floor

            Vector3 intersection = new Vector3();
            boolean intersect = Intersector.intersectRayPlane(ray, plane, intersection);
            if (intersect)
                end.set(intersection);
            else
                end.set(start);

            return true;
        } else
            return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void setModelGraph(ModelGraph modelGraph) {
        this.modelGraph = modelGraph;
    }
}