package fr.limsi.rorqual.core.utils.scene3d;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.math.collision.Ray;

import javax.jws.WebParam;

/**
 * Created by christophe on 30/06/15.
 */
public class ModelGraph implements InputProcessor {

    ModelContainer root;
    ModelBatch modelBatch;
    Camera camera;
    Environment environment;

    public ModelContainer getRoot() {
        return root;
    }

    public ModelGraph(Camera camera, Environment environment, ShaderProvider shaderProvider) {
        root = new ModelContainer();
        modelBatch = new ModelBatch(shaderProvider);
        this.camera = camera;
        this.environment = environment;
    }

    public void draw(){
        camera.update();
        modelBatch.begin(camera);
        root.draw(modelBatch, environment);
        modelBatch.end();
    }

    public ModelContainer getObject(int screenX, int screenY) {
        return hit(screenX, screenY);
    }

    public ModelContainer hit(int screenX, int screenY) {
        Ray ray = camera.getPickRay(screenX, screenY);
        return root.hit(ray);
    }

    public void setCamera(Camera c) {
        camera = c;
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

    public ModelContainer getFromUserObject(Object o) {
        return null;
    }

}
