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

import fr.limsi.rorqual.core.view.MainApplicationAdapter;

/**
 * Created by christophe on 30/06/15.
 */
public class ModelGraph {

    ModelContainer root;
    Camera camera;

    public ModelContainer getRoot() {
        return root;
    }

    public ModelGraph() {
        root = new ModelContainer();
        root.setSelectable(false);
        root.root = this;
    }

    public void draw(ModelBatch modelBatch, Environment environment, ModelContainer.Type type){
        synchronized (this) {
            root.draw(modelBatch, environment, type);
        }
    }

    public ModelContainer getObject(int screenX, int screenY) {
        ModelContainer hit = hit(screenX, screenY);
        return hit;
    }

    public ModelContainer hit(int screenX, int screenY) {
        if (camera == null)
            return null;
        Ray ray = camera.getPickRay(screenX, screenY);
        return root.hit(ray);
    }

    public void setCamera(Camera c) {
        camera = c;
    }

    public Camera getCamera() {
        return camera;
    }

    HashMap<Object, ModelContainer> objects_map = new HashMap<Object, ModelContainer>();

    public ModelContainer getFromUserObject(Object o) {
        if (objects_map.containsKey(o))
            return objects_map.get(o);
        return null;
    }

    public void add(ModelContainer m) {
        if (m.getUserData() != null)
            objects_map.put(m.getUserData(), m);
    }
    public void remove(ModelContainer m) {
        if (m.getUserData() != null && objects_map.containsKey(m.getUserData()))
            objects_map.remove(m.getUserData());
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
        synchronized (this) {
            root.act();
        }
    }

}
