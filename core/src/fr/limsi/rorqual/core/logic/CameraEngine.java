package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;

import fr.limsi.rorqual.core.view.CameraUpdater;
import fr.limsi.rorqual.core.view.OrthographicCameraUpdater;
import fr.limsi.rorqual.core.view.PerspectiveCameraUpdater;

/**
 * Created by christophe on 06/08/15.
 */
public class CameraEngine implements InputProcessor {

    private Cameras curent_camera = Cameras.PERSPECTIVE;

    private enum Cameras {
        PERSPECTIVE,
        ORTHOGRAPHIC,
        ;

        private CameraUpdater updater;

        static {
            OrthographicCamera orthographicCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            orthographicCamera.viewportHeight = Gdx.graphics.getHeight();
            orthographicCamera.viewportWidth = Gdx.graphics.getWidth();
            orthographicCamera.zoom = 1f/100;
            orthographicCamera.position.set(0.f, 0, 10f);
            orthographicCamera.lookAt(0f, 0f, 0f);
            orthographicCamera.up.set(0, 1, 0);
            orthographicCamera.update();
            ORTHOGRAPHIC.updater = new OrthographicCameraUpdater(orthographicCamera);

            PerspectiveCamera perspectiveCamera = new PerspectiveCamera(30f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            perspectiveCamera.viewportHeight = Gdx.graphics.getHeight();
            perspectiveCamera.viewportWidth = Gdx.graphics.getWidth();
            perspectiveCamera.position.set(0, -20, 1.65f);
            perspectiveCamera.near = 1f;
            perspectiveCamera.far = 10000f;
            //perspectiveCamera.lookAt(0, 0, 0);
            perspectiveCamera.direction.set(0,1,0);
            perspectiveCamera.up.set(0, 0, 1);
            perspectiveCamera.update();
            PERSPECTIVE.updater = new PerspectiveCameraUpdater(perspectiveCamera);
        }

        public CameraUpdater getCameraUpdater() {
            return updater;
        }

        public Camera getCamera() {
            return updater.getCamera();
        }
    }

    private CameraEngine() {}

    /** Holder */
    private static class CameraEngineHolder
    {
        /** Instance unique non préinitialisée */
        private final static CameraEngine INSTANCE = new CameraEngine();
    }

    public static synchronized CameraEngine getInstance() {
        return CameraEngineHolder.INSTANCE;
    }

    public void switchCamera() {
        switch (curent_camera) {
            case PERSPECTIVE:
                curent_camera = Cameras.ORTHOGRAPHIC;
                break;
            case ORTHOGRAPHIC:
                curent_camera = Cameras.PERSPECTIVE;
                break;
        }
    }

    public Camera getCurrentCamera() {
        return curent_camera.getCamera();
    }

    public CameraUpdater getCurrentCameraUpdater() {
        return curent_camera.getCameraUpdater();
    }

    public void updateViewport(int height, int width) {
        for (Cameras c : Cameras.values()) {
            c.getCamera().viewportHeight = height;
            c.getCamera().viewportWidth = width;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return getCurrentCameraUpdater().keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return getCurrentCameraUpdater().keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return getCurrentCameraUpdater().keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return getCurrentCameraUpdater().touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return getCurrentCameraUpdater().touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return getCurrentCameraUpdater().touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return getCurrentCameraUpdater().mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return getCurrentCameraUpdater().scrolled(amount);
    }

}
