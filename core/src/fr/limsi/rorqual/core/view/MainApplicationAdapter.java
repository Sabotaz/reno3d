package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;

import fr.limsi.rorqual.core.utils.DefaultMutableTreeNode;

import fr.limsi.rorqual.core.model.IfcHolder;
import fr.limsi.rorqual.core.utils.SceneGraphMaker;
import scene3d.Stage3d;

public class MainApplicationAdapter extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

    ShapeRenderer shape;
    Stage3d stage;

    @Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("data/gdx/badlogic.jpg");
        shape = new ShapeRenderer();

        DefaultMutableTreeNode spatialStructureTreeNode = IfcHolder.getInstance().getSpatialStructureTreeNode();
        //print(spatialStructureTreeNode, 0);

        stage = SceneGraphMaker.makeSceneGraph(spatialStructureTreeNode);
        OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1f/50;
        stage.setCamera(camera);
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

        stage.getCamera().position.set(0.f, 0, 0f);
        stage.getCamera().lookAt(0f, 0f, -1f);
        stage.getCamera().up.set(0,1,0);

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
}
