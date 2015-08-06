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
            ORTHOGRAPHIC.updater = new OrthographicCameraUpdater();
            PERSPECTIVE.updater = new PerspectiveCameraUpdater();
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
