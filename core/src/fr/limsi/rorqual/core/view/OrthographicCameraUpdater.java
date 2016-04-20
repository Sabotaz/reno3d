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
import com.badlogic.gdx.math.collision.BoundingBox;

import fr.limsi.rorqual.core.model.Batiment;
import fr.limsi.rorqual.core.model.Etage;
import fr.limsi.rorqual.core.model.Fenetre;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Ouverture;
import fr.limsi.rorqual.core.model.Slab;

/**
 * Created by christophe on 15/07/15.
 */
// Caméra utilisant une projection ortho, et utilisant les gestures
public class OrthographicCameraUpdater extends CameraUpdater {

    OrthographicCamera camera;

    private final float VIEWPORT_WIDTH = 30;

    public OrthographicCameraUpdater() {
        iconeName = "drawable:2D";

        float ratio = Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
        OrthographicCamera orthographicCamera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_WIDTH*ratio);
        orthographicCamera.zoom = 0.5f;
        orthographicCamera.position.set(0.f, 0.f, 10f);
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

    @Override
    public void reset() {
        BoundingBox b = new BoundingBox();
        Etage etage = ModelHolder.getInstance().getBatiment().getCurrentEtage();
        float center_x = 0;
        float center_y = 0;
        if (!etage.getMurs().isEmpty()) {
            float min_x = Float.MAX_VALUE;
            float min_y = Float.MAX_VALUE;
            float max_x = -Float.MAX_VALUE;
            float max_y = -Float.MAX_VALUE;
            for (Mur m : etage.getMurs()) {
                Vector2 v = m.getA().getPosition();
                if (v.x < min_x) min_x = v.x;
                if (v.y < min_y) min_y = v.y;
                if (v.x > max_x) max_x = v.x;
                if (v.y > max_y) max_y = v.y;
                v = m.getB().getPosition();
                if (v.x < min_x) min_x = v.x;
                if (v.y < min_y) min_y = v.y;
                if (v.x > max_x) max_x = v.x;
                if (v.y > max_y) max_y = v.y;
            }
            center_x = min_x + (max_x - min_x)/2;
            center_y = min_y + (max_y - min_y)/2;
        } else {

        }
        camera.position.set(center_x, center_y, 50f);
        camera.zoom = 0.5f;
        camera.update();
    }

    @Override
    public Vector3 getPosition() {
        return camera.position;
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
        camera.update();

        return true;
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

        // diff
        float diff = 1 + amount;

        avancer(diff);

        // update last
        camera.update();

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
        camera.update();
    }
}
