package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by christophe on 15/07/15.
 */
public class OrthographicCameraUpdater extends CameraUpdater {

    OrthographicCamera camera;

    private final float VIEWPORT_WIDTH = 30;

    public OrthographicCameraUpdater() {

        float ratio = Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
        OrthographicCamera orthographicCamera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_WIDTH*ratio);
        orthographicCamera.zoom = 1f;
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
    public void updateViewport(int height, int width) {
        //
        camera.viewportWidth = VIEWPORT_WIDTH;
        camera.viewportHeight = VIEWPORT_WIDTH * height/width;
        camera.update();
    }

    public void act() {
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    int last_screenX = -1;
    int last_screenY = -1;

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (last_screenX == -1 || last_screenY == -1) {
            last_screenX = (int) x;
            last_screenY = (int) y;
        }

        Vector3 before = camera.unproject(new Vector3(last_screenX, last_screenY, 1));
        Vector3 after = camera.unproject(new Vector3(x, y, 1));

        Vector3 diff = before.cpy().sub(after);

        camera.position.add(diff);

        camera.update();

        System.out.println(camera.position);

        last_screenX = (int) x;
        last_screenY = (int) y;
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        last_screenX = -1;
        last_screenY = -1;
        return true;
    }

    float zoomStart = 0;
    float zoomLast = 0;

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if (zoomStart != initialDistance) { // new zoom !
            zoomStart = initialDistance;
            zoomLast = zoomStart;
        }
        // diff
        float diff = distance - zoomLast;

        avancer(diff);

        // update last
        zoomLast = distance;

        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    float ZOOM_RATIO = 0.001f;

    private void avancer(float amount) {

        camera.zoom -= amount * ZOOM_RATIO;
        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 10);
        System.out.println(camera.zoom);
        camera.update();
    }
}
