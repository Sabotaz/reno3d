package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Collection;

import fr.limsi.rorqual.core.utils.DefaultMutableTreeNode;

import fr.limsi.rorqual.core.model.IfcHolder;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRoot;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWall;
import ifc2x3javatoolbox.ifcmodel.IfcModel;

public class MainApplicationAdapter extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;


    @Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("data/gdx/badlogic.jpg");

        DefaultMutableTreeNode spatialStructureTreeNode = IfcHolder.getInstance().getSpatialStructureTreeNode();
        print(spatialStructureTreeNode, 0);
	}

    public void print(DefaultMutableTreeNode treeNode, int tab) {

        for (int i = 0; i < tab; i++)
            System.out.print(" ");
        if (treeNode.getUserObject() instanceof IfcWall) {
            System.out.println("IfcWall: " + ((IfcWall) treeNode.getUserObject()).getName() + " (" + ((IfcWall) treeNode.getUserObject()).getGlobalId() + ")");
        } else if (treeNode.getUserObject() instanceof IfcRoot) {
            System.out.println("IfcRoot: " + ((IfcRoot) treeNode.getUserObject()).getName() + " (" + ((IfcRoot) treeNode.getUserObject()).getGlobalId() + ") [" + treeNode.getUserObject().getClass().getName() + "]");
        } else {
            System.out.println(treeNode.getUserObject() + " [" + treeNode.getUserObject().getClass().getName() + "]");
        }

        for (int i = 0; i < treeNode.getChildCount(); i++) {
            DefaultMutableTreeNode currentTreeNode = (DefaultMutableTreeNode)treeNode.getChildAt(i);
            print(currentTreeNode, tab+1);
        }

    }

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
}
