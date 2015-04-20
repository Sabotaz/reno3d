package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import fr.limsi.rorqual.core.utils.DefaultMutableTreeNode;

import fr.limsi.rorqual.core.model.IfcHolder;
import fr.limsi.rorqual.core.utils.SceneGraphMaker;
import scene3d.Actor3d;
import scene3d.Stage3d;

public class MainApplicationAdapter extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture img;

    ShapeRenderer shape;
    Stage3d stage;

    Camera[] cameras = new Camera[2];

    int ncam = 1;

    Environment environnement;
    ShaderProgram shader;

    @Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("data/gdx/badlogic.jpg");
        shape = new ShapeRenderer();

        DefaultMutableTreeNode spatialStructureTreeNode = IfcHolder.getInstance().getSpatialStructureTreeNode();
        //print(spatialStructureTreeNode, 0);

        // lights
        environnement = new Environment();
        environnement.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environnement.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        OrthographicCamera camera1 = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera1.zoom = 1f/10;

        camera1.position.set(0.f, 0, 10f);
        camera1.lookAt(0f, 0f, 0f);
        camera1.up.set(0,1,0);
        camera1.update();
        cameras[0] = camera1;

        PerspectiveCamera camera2 = new PerspectiveCamera(30f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera2.position.set(0, -50, 50);
        camera2.near = 0.001f;
        camera2.far = 1000f;
        camera2.lookAt(0, 0, 0);
        camera2.up.set(0,0,1);
        camera2.update();
        cameras[1] = camera2;

        ShaderProvider shaderProvider = new DefaultShaderProvider() {
            @Override
            protected Shader createShader(Renderable renderable) {
                if (true) return new TestShader(renderable);
                return super.createShader(renderable);
            }
        };

        Camera baseCamera = cameras[ncam%cameras.length];

        stage = new Stage3d(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), baseCamera, environnement, shaderProvider);
        SceneGraphMaker.makeSceneGraph(spatialStructureTreeNode, stage);

        Gdx.input.setInputProcessor(this);
        //stage.getRoot().print();

	}

    public void print(DefaultMutableTreeNode treeNode, int tab) {

        System.out.print(new String(new char[tab]).replace('\0', ' '));

        System.out.println(treeNode.getUserObject() + " [" + treeNode.getUserObject().getClass().getName() + "]");

        for (int i = 0; i < treeNode.getChildCount(); i++) {
            DefaultMutableTreeNode currentTreeNode = (DefaultMutableTreeNode)treeNode.getChildAt(i);
            print(currentTreeNode, tab + 1);
        }
    }

	@Override
	public void render () {

        //shader.setUniformMatrix("u_projectionViewMatrix", cameras[ncam%cameras.length].combined);

		Gdx.gl.glClearColor(0.12f, 0.38f, 0.55f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setProjectionMatrix(cameras[ncam%cameras.length].combined);
        int grid_size = 1;
        int grid_div = 10;

        for (int i = -100; i < 100; i+=grid_size) {
            if (i % grid_div == 0)
                shape.setColor(new Color(1, 1, 1, 0.15f));
            else
                shape.setColor(new Color(1, 1, 1, 0.05f));
            shape.line(-100, i, 0, 100, i, 0);
        }
        for (int i = -100; i < 100; i+=grid_size) {
            if (i % grid_div == 0)
                shape.setColor(new Color(1, 1, 1, 0.15f));
            else
                shape.setColor(new Color(1, 1, 1, 0.05f));
            shape.line(i, -100, 0, i, 100, 0);
        }
        shape.end();
        /*
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();*/
        Gdx.gl.glDisable(Gdx.gl.GL_BLEND);
        stage.act();
        stage.draw();

	}

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.C:
                ncam++;
                stage.setCamera(cameras[ncam%cameras.length]);
                return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    private int last_screenX = 0;
    private int last_screenY = 0;
    private boolean dragged = false;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        last_screenX = screenX;
        last_screenY = screenY;
        dragged = false;
        return false;
    }

    Actor3d selected = null;

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (!dragged) {
            if (selected != null)
                selected.setColor(Color.WHITE);
            selected = stage.getObject(screenX, screenY);
            if (selected != null) {
                System.out.println("TOUCH: " + selected.userData);
                selected.setColor(Color.YELLOW);
            }
            return selected != null;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        dragged = true;
        int diffX = screenX - last_screenX;
        int diffY = screenY - last_screenY;
        Camera camera = cameras[ncam%cameras.length];
        if (camera instanceof OrthographicCamera) {
            OrthographicCamera oc = (OrthographicCamera) camera;
            oc.translate(-diffX*oc.zoom, diffY*oc.zoom, 0);
        } else {
            Vector3 before = camera.unproject(new Vector3(last_screenX, last_screenY,1)).sub(camera.position).nor();
            Vector3 after = camera.unproject(new Vector3(screenX, screenY,1)).sub(camera.position).nor();

            if (!before.isCollinear(after))
                camera.rotate(after.cpy().crs(before), (float)(Math.acos(after.dot(before)) * 180. / Math.PI));
            camera.up.set(0,0,1);
        }
        last_screenX = screenX;
        last_screenY = screenY;
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {

        Camera camera = cameras[ncam%cameras.length];
        if (camera instanceof OrthographicCamera) {
            OrthographicCamera oc = (OrthographicCamera) camera;
            oc.zoom = oc.zoom * (1+amount/10f);
        } else if (camera instanceof PerspectiveCamera) {
            PerspectiveCamera pc = (PerspectiveCamera) camera;
            pc.fieldOfView = pc.fieldOfView * (1+amount/10f);
        }
        return true;
    }
}
