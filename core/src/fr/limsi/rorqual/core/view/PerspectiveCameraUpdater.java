package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by christophe on 15/07/15.
 */
public class PerspectiveCameraUpdater extends CameraUpdater {

    PerspectiveCamera camera;

    public PerspectiveCameraUpdater() {

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

        camera = perspectiveCamera;
    }

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

        Vector3 before = camera.unproject(new Vector3(last_screenX, last_screenY,1)).sub(camera.position).nor();
        Vector3 after = camera.unproject(new Vector3(screenX, screenY,1)).sub(camera.position).nor();
        if (!before.isCollinear(after))
            camera.rotate(after.cpy().crs(before), (float)(Math.acos(after.dot(before)) * 180. / Math.PI));
        camera.up.set(0,0,1);

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


    public void act() {
        Vector3 dir = camera.direction.cpy();

        Vector3 xy_dir = camera.direction.cpy();
        xy_dir.z = 0;
        xy_dir.nor();

        Vector3 pos = camera.position.cpy();
        Vector3 up = Vector3.Z.cpy();
        Vector3 y_dir = xy_dir.cpy().crs(up);

        float height = pos.z;

        Vector3 disp = new Vector3();
        Vector3 center = new Vector3();

        float angle = 0;

        boolean deported = ctrl;


        if (deported)
            switch (camera_mov) {
                case DROITE:
                    center = pos.cpy().add(xy_dir.cpy().scl(20));
                    angle = 2;
                    break;
                case GAUCHE:
                    center = pos.cpy().add(xy_dir.cpy().scl(20));
                    angle = -2;
                    break;
                case HAUT:
                    disp = up.cpy().scl(.1f);
                    break;
                case BAS:
                    if (pos.z > 1)
                        disp = up.cpy().scl(-.1f);
                    break;
                case AVANCER:
                    disp = xy_dir.cpy().scl(.2f);
                    break;
                case RECULER:
                    disp = xy_dir.cpy().scl(-.2f);
                    break;
            }
        else
            switch (camera_mov) {
                case DROITE:
                    center = pos.cpy();
                    angle = -1.5f;
                    //disp = y_dir.cpy().scl(.2f);
                    break;
                case GAUCHE:
                    center = pos.cpy();
                    angle = 1.5f;
                    //disp = y_dir.cpy().scl(-.2f);
                    break;
                case HAUT:
                    disp = up.cpy().scl(.1f);
                    break;
                case BAS:
                    if (pos.z > 1)
                        disp = up.cpy().scl(-.1f);
                    break;
                case AVANCER:
                    disp = xy_dir.cpy().scl(.2f);
                    break;
                case RECULER:
                    disp = xy_dir.cpy().scl(-.2f);
                    break;
            }

        pos.add(disp);

        camera.direction.set(dir);
        camera.position.set(pos);
        camera.up.set(up);
        camera.rotateAround(center, up, angle);
        camera.update();
    }

}
