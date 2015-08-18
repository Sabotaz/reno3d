package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
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
        perspectiveCamera.position.set(0, -20, 1.65f);
        perspectiveCamera.near = 1f;
        perspectiveCamera.far = 10000f;
        //perspectiveCamera.lookAt(0, 0, 0);
        perspectiveCamera.direction.set(0,1,0);
        perspectiveCamera.up.set(0, 0, 1);
        perspectiveCamera.update();

        camera = perspectiveCamera;
    }

    protected void setCamera() {}

    private enum Sense {
        GAUCHE,
        DROITE,
        HAUT,
        BAS,
        AVANCER,
        RECULER,
        NONE;
    }

    private Sense camera_mov = Sense.NONE;

    private boolean ctrl = false;

    public Camera getCamera() {
        return camera;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                camera_mov = Sense.GAUCHE;
                return true;
            case Input.Keys.RIGHT:
                camera_mov = Sense.DROITE;
                return true;
            case Input.Keys.UP:
                camera_mov = Sense.AVANCER;
                return true;
            case Input.Keys.DOWN:
                camera_mov = Sense.RECULER;
                return true;
            case Input.Keys.PLUS:
                camera_mov = Sense.HAUT;
                return true;
            case Input.Keys.MINUS:
                camera_mov = Sense.BAS;
                return true;
            case Input.Keys.CONTROL_LEFT:
            case Input.Keys.CONTROL_RIGHT:
                ctrl = true;
                return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            //NinePatch patch = atlas.createPatch("wall");
            case Input.Keys.LEFT:
            case Input.Keys.RIGHT:
            case Input.Keys.UP:
            case Input.Keys.DOWN:
            case Input.Keys.PLUS:
            case Input.Keys.MINUS:
                camera_mov = Sense.NONE;
                return true;
            case Input.Keys.CONTROL_LEFT:
            case Input.Keys.CONTROL_RIGHT:
                ctrl = false;
                return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    boolean dragged = false;
    int last_screenX;
    int last_screenY;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        last_screenX = screenX;
        last_screenY = screenY;
        dragged = false;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        dragged = true;
        int diffX = screenX - last_screenX;
        int diffY = screenY - last_screenY;

        Vector3 before = camera.unproject(new Vector3(last_screenX, last_screenY,1)).sub(pos).nor();
        Vector3 after = camera.unproject(new Vector3(screenX, screenY,1)).sub(pos).nor();
        if (!before.isCollinear(after))
            user_rotation.rotate(after.cpy().crs(before), -(float)(Math.acos(after.dot(before)) * 180. / Math.PI));

        last_screenX = screenX;
        last_screenY = screenY;
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        camera.position.scl(1+amount/10f);
        camera.update();
        //pc.fieldOfView = pc.fieldOfView * (1+amount/10f);
        return true;
    }

    protected Vector3 pos = new Vector3(0,0,1.65f);
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

}
