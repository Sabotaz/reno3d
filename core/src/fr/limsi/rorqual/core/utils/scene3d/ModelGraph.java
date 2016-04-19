package fr.limsi.rorqual.core.utils.scene3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.math.collision.Ray;

import java.util.HashMap;

import javax.jws.WebParam;

import fr.limsi.rorqual.core.logic.CameraEngine;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;

/**
 * Created by christophe on 30/06/15.
 */
public class ModelGraph {

    ModelContainer root;

    public ModelContainer getRoot() {
        return root;
    }

    public ModelGraph() {
        root = new ModelContainer();
        root.setSelectable(false);
        root.root = this;
    }

    public void draw(ModelBatch modelBatch, Environment environment, ModelContainer.Type type){
        synchronized (this.getRoot()) {
            root.draw(modelBatch, environment, type);
        }
    }

    public ModelContainer getObject(int screenX, int screenY) {
        ModelContainer hit = hit(screenX, screenY);
        return hit;
    }

    public ModelContainer hit(int screenX, int screenY) {
        Ray ray = CameraEngine.getInstance().getCurrentCamera().getPickRay(screenX, screenY);
        return root.hit(ray);
    }

    private int count(ModelContainer current) {
        int i = 1;
        for (ModelContainer child : current.getChildren())
            i += count(child);
        return i;
    }

    public int count() {
        synchronized (this.getRoot()) {
            return count(this.getRoot());
        }
    }

    public void act() {
        synchronized (this.getRoot()) {
            root.act();
        }
    }

}
