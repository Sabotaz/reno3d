package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import fr.limsi.rorqual.core.ui.Layout;
import fr.limsi.rorqual.core.ui.MainUiControleur;
import fr.limsi.rorqual.core.view.CameraUpdater;
import fr.limsi.rorqual.core.view.GyrometerCameraUpdater;
import fr.limsi.rorqual.core.view.OrthographicCameraUpdater;
import fr.limsi.rorqual.core.view.PerspectiveCameraUpdater;

/**
 * Created by christophe on 06/08/15.
 */
// Singleton
// Moteur de gestion des différentes caméras
public class CameraEngine implements GestureDetector.GestureListener {

    private Cameras curent_camera = Cameras.ORTHOGRAPHIC;

    private enum Cameras {
        PERSPECTIVE,
        ORTHOGRAPHIC,
        GYROMETER, // use of Android Gyro
        ;

        private CameraUpdater updater; // the current one

        static { // the different updaters
            ORTHOGRAPHIC.updater = new OrthographicCameraUpdater();
            PERSPECTIVE.updater = new PerspectiveCameraUpdater();
            GYROMETER.updater = new GyrometerCameraUpdater();
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
                curent_camera = Cameras.GYROMETER;
                break;
            case ORTHOGRAPHIC:
                curent_camera = Cameras.PERSPECTIVE;
                break;
            case GYROMETER:
                curent_camera = Cameras.ORTHOGRAPHIC;
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
            c.getCameraUpdater().updateViewport(height, width);
        }
    }

    // GESTURES

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return getCurrentCameraUpdater().touchDown(x, y, pointer, button);
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return getCurrentCameraUpdater().tap(x, y, count, button);
    }

    @Override
    public boolean longPress(float x, float y) {
        return getCurrentCameraUpdater().longPress(x, y);
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return getCurrentCameraUpdater().fling(velocityX, velocityY, button);
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return getCurrentCameraUpdater().pan(x, y, deltaX, deltaY);
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return getCurrentCameraUpdater().panStop(x, y, pointer, button);
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return getCurrentCameraUpdater().zoom(initialDistance, distance);
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return getCurrentCameraUpdater().pinch(initialPointer1, initialPointer2, pointer1, pointer2);
    }

}
