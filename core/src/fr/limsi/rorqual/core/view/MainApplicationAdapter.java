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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;

import fr.limsi.rorqual.core.utils.DefaultMutableTreeNode;

import fr.limsi.rorqual.core.model.IfcHolder;
import fr.limsi.rorqual.core.utils.SceneGraphMaker;
import scene3d.Stage3d;

public class MainApplicationAdapter extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture img;

    ShapeRenderer shape;
    Stage3d stage;

    Camera[] cameras = new Camera[2];

    int ncam = 0;

    @Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("data/gdx/badlogic.jpg");
        shape = new ShapeRenderer();

        DefaultMutableTreeNode spatialStructureTreeNode = IfcHolder.getInstance().getSpatialStructureTreeNode();
        //print(spatialStructureTreeNode, 0);

        stage = SceneGraphMaker.makeSceneGraph(spatialStructureTreeNode);
        OrthographicCamera camera1 = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera1.zoom = 1f/10;

        camera1.position.set(0.f, 0, 10f);
        camera1.lookAt(0f, 0f, 0f);
        camera1.up.set(0,1,0);
        camera1.update();
        stage.setCamera(camera1);
        cameras[0] = camera1;

        PerspectiveCamera camera2 = new PerspectiveCamera(30f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera2.position.set(-5, -5, 5);
        camera2.near = 0.001f;
        camera2.far = 1000f;
        camera2.lookAt(0, 0, 0);
        camera2.up.set(0,0,1);
        camera2.update();
        cameras[1] = camera2;

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

		Gdx.gl.glClearColor(0.12f, 0.38f, 0.55f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

        shape.begin(ShapeRenderer.ShapeType.Line);

        int grid_size = 10;

        for (int i = 0; i < Gdx.graphics.getHeight()/grid_size; i++) {
            if (i % 5 == 0)
                shape.setColor(new Color(1, 1, 1, 0.15f));
            else
                shape.setColor(new Color(1, 1, 1, 0.05f));
            shape.line(0, i*grid_size, Gdx.graphics.getWidth(), i*grid_size);
        }
        for (int i = 0; i < Gdx.graphics.getWidth()/grid_size; i++) {
            if (i % 5 == 0)
                shape.setColor(new Color(1, 1, 1, 0.15f));
            else
                shape.setColor(new Color(1, 1, 1, 0.05f));
            shape.line(i*grid_size, 0, i*grid_size, Gdx.graphics.getHeight());
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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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
}
