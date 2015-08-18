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

    private GyroscopeValues gyroscopeValues;

    public GyrometerCameraUpdater() {
        super();
    }

    protected void init() {
        gyroscopeValues = GyroscopeValues.getInstance();
    }

    protected void setCamera() {
        if (gyroscopeValues.hasGyro()) {
            PerspectiveCamera perspectiveCamera = new PerspectiveCamera(30f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) {
                @Override
                public void update(boolean updateFrustum) {
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
            perspectiveCamera.near = .1f;
            perspectiveCamera.far = 10000f;
            //perspectiveCamera.lookAt(0, 0, 0);
            perspectiveCamera.direction.set(0, 1, 0);
            perspectiveCamera.up.set(0, 0, 1);
            // update view here
            perspectiveCamera.view.setToLookAt(perspectiveCamera.position, new Vector3().set(perspectiveCamera.position).add(perspectiveCamera.direction), perspectiveCamera.up);
            perspectiveCamera.update();

            camera = perspectiveCamera;
        }
        else super.setCamera();
    }

    protected void update() {
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
