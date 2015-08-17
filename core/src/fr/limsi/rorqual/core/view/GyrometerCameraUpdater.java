package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import fr.limsi.rorqual.core.utils.GyroscopeValues;

/**
 * Created by christophe on 15/07/15.
 */
public class GyrometerCameraUpdater extends PerspectiveCameraUpdater {

    private boolean haveCapabilities;
    private GyroscopeValues gyroscopeValues;

    public GyrometerCameraUpdater() {
        super();
        gyroscopeValues = GyroscopeValues.getInstance();
    }

    public void act() {
        if (gyroscopeValues.hasGyro()) {
            Matrix4 mx = new Matrix4();
            float x = (float)Math.toDegrees(gyroscopeValues.getX());
            float y = (float)Math.toDegrees(gyroscopeValues.getY());
            float z = (float)Math.toDegrees(gyroscopeValues.getZ());
            // x = -z
            // z = x
            // y = -y
            mx.setFromEulerAngles(x, -z, -y);
            Vector3 dir = Vector3.Y.cpy().mul(mx);
            Vector3 up = Vector3.Z.cpy().mul(mx);

            camera.direction.set(dir);
            camera.up.set(up);
            camera.update();
        }
        else
            super.act();
    }

}
