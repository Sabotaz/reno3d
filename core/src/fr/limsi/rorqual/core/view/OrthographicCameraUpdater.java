package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by christophe on 15/07/15.
 */
public class OrthographicCameraUpdater extends CameraUpdater {

    OrthographicCamera camera;

    public OrthographicCameraUpdater() {

        OrthographicCamera orthographicCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        orthographicCamera.viewportHeight = Gdx.graphics.getHeight();
        orthographicCamera.viewportWidth = Gdx.graphics.getWidth();
        orthographicCamera.zoom = 1f/100;
        orthographicCamera.position.set(0.f, 0, 10f);
        orthographicCamera.lookAt(0f, 0f, 0f);
        orthographicCamera.up.set(0, 1, 0);
        orthographicCamera.update();

        camera = orthographicCamera;
    }

    public Camera getCamera() {
        return camera;
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


    public void act() {
    }

}
