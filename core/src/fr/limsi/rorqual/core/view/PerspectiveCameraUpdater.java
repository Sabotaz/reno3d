package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import fr.limsi.rorqual.core.model.Etage;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Slab;

/**
 * Created by christophe on 15/07/15.
 */
// caméra utilisant une projection perspective, et utilisant les gestures
public class PerspectiveCameraUpdater extends CameraUpdater {

    PerspectiveCamera camera;

    public PerspectiveCameraUpdater() {
        iconeName = "drawable:3D";

        PerspectiveCamera perspectiveCamera = new PerspectiveCamera(50f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) {
            @Override
            public void update (boolean updateFrustum) {
                float aspect = viewportWidth / viewportHeight;
                projection.setToProjection(Math.abs(near), Math.abs(far), fieldOfView, aspect);
                // DON'T update the view !!
                //view.setToLookAt(position, tmp.set(position).add(direction), up);
                combined.set(projection);
                Matrix4.mul(combined.val, view.val);

                if (updateFrustum) {
                    invProjectionView.set(combined);
                    Matrix4.inv(invProjectionView.val);
                    frustum.update(invProjectionView);
                }
            }
        };
        perspectiveCamera.viewportHeight = Gdx.graphics.getHeight();
        perspectiveCamera.viewportWidth = Gdx.graphics.getWidth();
        perspectiveCamera.near = 0.1f;
        perspectiveCamera.far = 1000f;
        perspectiveCamera.position.set(pos);
        pos.set(0, 0, 30f);

        camera = perspectiveCamera;

        update();
    }

    protected void setCamera() {}

    public Camera getCamera() {
        return camera;
    }

    @Override
    public void updateViewport(int height, int width) {
        camera.viewportHeight = height;
        camera.viewportWidth = width;
    }

    int last_screenX = -1;
    int last_screenY = -1;

    protected Matrix4 user_rotation = new Matrix4();

    public void act() {
        update();
    }

    @Override
    public void reset() {
        Etage etage = ModelHolder.getInstance().getBatiment().getCurrentEtage();
        float elevation = etage.getElevation();
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
        user_rotation.idt().rotate(1, 0, 0, -90);
        user_rotation.rotate(1, 0, 0, 50);
        user_rotation.rotate(0, 0, 1, 90);
        yaw = pitch = roll = 0;
        lastEuler.setFromEulerAngles(yaw, pitch, roll);
        update();


        yaw = 0;
        pitch = 45;
        roll = -45;

        float py = (float)(dist * Math.cos(pitch)*Math.sin(roll));
        float px = (float)(dist * Math.sin(pitch)*Math.sin(roll));
        float pz = (float)(dist * Math.cos(roll));

        user_rotation.idt().rotate(1, 0, 0, -45);
        user_rotation.rotate(0, 0, 1, 45);
        pos.set(center_x + px, center_y + py, center_z + pz);
        update();
    }

    private float center_x = 0;
    private float center_y = 5;
    private float center_z = 1.6f;

    protected void update() {
        /* set look at */
        roll = roll < -90 ? -90 : (roll > -20 ? -20 : roll);

        float py = (float)(dist * Math.cos(pitch * Math.PI / 180)*Math.sin(roll*Math.PI / 180));
        float px = (float)(dist * Math.sin(pitch*Math.PI / 180)*Math.sin(roll*Math.PI / 180));
        float pz = (float)(dist * Math.cos(roll*Math.PI / 180));

        pos.set(center_x + px, center_y + py, center_z + pz);

        Vector3 dir = new Vector3(px, py, pz);
        Vector3 up = new Vector3(0,0,1);//dir.cpy().crs(new Vector3(0,0,1)).crs(dir);
        Vector3 vz = dir.cpy().nor();
        Vector3 vx = up.cpy().crs(vz);
        Vector3 vy = vz.cpy().crs(vx);

        user_rotation.set(vx, vy, vz, Vector3.Zero);

        Matrix4 translation = new Matrix4().translate(new Vector3().sub(pos));
        Matrix4 mx = new Matrix4();
        mx.mul(user_rotation);
        mx.mul(translation);

        camera.view.set(mx);
        camera.position.set(pos);
        camera.update();
    }

    boolean translating = false;

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        translating = button == 1;
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

    float yaw = 0;
    float pitch = 0;
    float roll = 0;
    Matrix4 lastEuler = new Matrix4();

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (last_screenX == -1 || last_screenY == -1) {
            last_screenX = (int) x;
            last_screenY = (int) y;
        }
        if (translating) {
            float py = (float)(Math.cos(pitch * Math.PI / 180)*Math.sin(roll*Math.PI / 180));
            float px = (float)(Math.sin(pitch * Math.PI / 180)*Math.sin(roll*Math.PI / 180));

            Vector2 dir = new Vector2(px, py).nor().scl(-deltaY/50);
            Vector2 dir2 = new Vector2(px, py).rotate90(1).nor().scl(-deltaX/50);

            center_x += dir.x + dir2.x;
            center_y += dir.y + dir2.y;
        } else {
            pitch += deltaX/7.5;
            roll += deltaY/10;
        }

        last_screenX = (int) x;
        last_screenY = (int) y;
        update();
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

        avancer(zoomLast/distance);

        // update last
        zoomLast = distance;
        update();

        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    float ZOOM_RATIO = 0.02f;

    private float dist = 10;
    private void avancer(float amount) {

        dist *= amount;

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
        float diff = 1 + amount/15.f;

        avancer(diff);

        // update last
        update();

        return true;

    }
}
