package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by christophe on 15/07/15.
 */
public class GyrometerCameraUpdater extends PerspectiveCameraUpdater {

    private boolean haveCapabilities;

    public GyrometerCameraUpdater() {
        super();
        haveCapabilities = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
    }

    public void act() {
        if (haveCapabilities) {
            Matrix4 mx = new Matrix4();
            Gdx.input.getRotationMatrix(mx.val);
            Vector3 dir = Vector3.X.cpy().mul(mx);
            camera.direction.set(dir);
            camera.update();
        }
        else
            super.act();
    }

}
