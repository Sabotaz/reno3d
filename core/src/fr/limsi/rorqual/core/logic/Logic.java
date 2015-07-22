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
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
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

    public void stop() {
        currentState = State.NONE;
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

    Vector3 start = new Vector3();
    Vector3 end = new Vector3();
    ModelContainer wall;
    boolean making_wall = false;
    Mur mur;
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

            start = new Vector3(intersection);
            end = new Vector3(intersection);

            mur = new Mur(start, end);

            wall = new ModelContainer(mur) {
                public void act() {
                    super.act();
                    mur.setA(start);
                    mur.setB(end);
                    mur.act();
                }
            };
            //ModelHolder.getInstance().getBatiment().getCurrentEtage().addMur(mur);
            ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().add(wall);
            //modelGraph.getRoot().add(wall);

            return true;
        } else
            return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (currentState == State.WALL && making_wall) {
            ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().remove(wall);

            if (!start.equals(end)) {
                Mur copy_mur = new Mur(mur);
                ModelHolder.getInstance().getBatiment().getCurrentEtage().addMur(copy_mur);
            }
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
