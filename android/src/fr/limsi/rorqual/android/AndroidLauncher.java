package fr.limsi.rorqual.android;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaRouter;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.surfaceview.FixedResolutionStrategy;
import com.badlogic.gdx.backends.android.surfaceview.ResolutionStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import fr.limsi.rorqual.android.sensors.GyroscopeListener;
import fr.limsi.rorqual.core.model.IfcHelper;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;


public class AndroidLauncher extends AndroidApplication {
    private SensorManager sensorManager;
    private GyroscopeListener gyroscopeListener;
    @Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        config.r = config.g = config.b = 8;

        //config.resolutionStrategy = new FixedResolutionStrategy(1280,800);

        MainApplicationAdapter application = new MainApplicationAdapter();

        initialize(application, config);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscopeListener = new GyroscopeListener();

        MediaRouter mediaRouter = (MediaRouter) getSystemService(MEDIA_ROUTER_SERVICE);
        for (int i = 0; i < mediaRouter.getRouteCount(); i++) {
            System.out.println(mediaRouter.getRouteAt(i));
        }


        initListeners();

        //gyroscopeListener.startTask();

	}

    private void initListeners() {
        /*register the sensor listener to listen to the gyroscope sensor, use the
        callbacks defined in this class, and gather the sensor information as quick
        as possible*/

        sensorManager.registerListener(gyroscopeListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);

        sensorManager.registerListener(gyroscopeListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_FASTEST);

        sensorManager.registerListener(gyroscopeListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_FASTEST);

    }

    //when this Activity starts
    @Override
    protected void onResume()
    {
        super.onResume();
    }

    //When this Activity isn't visible anymore
    @Override
    protected void onStop()
    {
        //unregister the sensor listener
        sensorManager.unregisterListener(gyroscopeListener);
        super.onStop();
    }

}
