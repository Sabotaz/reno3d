package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import fr.limsi.rorqual.core.utils.GyroscopeValues;

/**
 * Created by christophe on 15/07/15.
 */
public class GyrometerCameraUpdater extends PerspectiveCameraUpdater {


    protected void update() {
        GyroscopeValues gyroscopeValues = GyroscopeValues.getInstance();

        if (gyroscopeValues.hasGyro()) {
            Matrix4 remap = new Matrix4();
            remap.rotate(0, 0, 1, 90);
            Matrix4 translation = new Matrix4().translate(new Vector3().sub(pos));
            Matrix4 rotation = gyroscopeValues.getMatrix();
            Matrix4 mx = new Matrix4();
            mx.mul(remap);
            mx.mul(rotation);
            mx.mul(user_rotation);
            mx.mul(translation);

            camera.view.set(mx);
            camera.update();
        } else {
            super.update();
        }
    }

    public void act() {
        update();
    }

}
