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

/**
 * Created by christophe on 15/07/15.
 */
public class PerspectiveCameraUpdater extends CameraUpdater {

    PerspectiveCamera camera;

    public PerspectiveCameraUpdater() {

        PerspectiveCamera perspectiveCamera = new PerspectiveCamera(30f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) {
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
        perspectiveCamera.near = 1f;
        perspectiveCamera.far = 10000f;
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

    protected Vector3 pos = new Vector3();
    protected Matrix4 user_rotation = new Matrix4();

    public void act() {
        update();
    }

    protected void update() {
        Matrix4 translation = new Matrix4().translate(new Vector3().sub(pos));
        Matrix4 mx = new Matrix4();
        mx.mul(user_rotation);
        mx.mul(translation);

        camera.view.set(mx);
        camera.update();
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

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (last_screenX == -1 || last_screenY == -1) {
            last_screenX = (int) x;
            last_screenY = (int) y;
        }

        Vector3 before = camera.unproject(new Vector3(last_screenX, last_screenY, 1)).sub(pos).nor();
        Vector3 after = camera.unproject(new Vector3(x, y, 1)).sub(pos).nor();
        if (!before.isCollinear(after))
            user_rotation.rotate(after.cpy().crs(before), -(float) (Math.acos(after.dot(before)) * 180. / Math.PI));

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

    float ZOOM_RATIO = 0.05f;

    private void avancer(float amount) {

        Quaternion q = camera.view.cpy().inv().getRotation(new Quaternion());

        Vector3 dir = Vector3.Z.cpy().mul(q);

        dir.nor();
        System.out.println(dir);
        dir.scl(amount * ZOOM_RATIO);

        pos.add(dir);

    }

}
