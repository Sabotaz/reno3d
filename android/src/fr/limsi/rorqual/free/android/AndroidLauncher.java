package fr.limsi.rorqual.free.android;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.util.HashMap;

import fr.limsi.rorqual.free.R;
import fr.limsi.rorqual.android.sensors.GyroscopeListener;
import fr.limsi.rorqual.core.utils.analytics.Action;
import fr.limsi.rorqual.core.utils.analytics.ActionResolver;
import fr.limsi.rorqual.core.utils.analytics.Category;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;


public class AndroidLauncher extends AndroidApplication implements ActionResolver {

    private SensorManager sensorManager;
    private GyroscopeListener gyroscopeListener;

    private final static String PROPERTY_ID = "UA-73647708-1";

    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     *
     * A single tracker is usually enough for most purposes. In case you do need
     * multiple trackers, storing them all in Application object helps ensure
     * that they are created only once per application instance.
     */
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg:
        // roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a
        // company.
    }

    private Tracker tracker;
    private Tracker globalTracker;

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);

            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics
                    .newTracker(R.xml.app_tracker)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics
                    .newTracker(R.xml.global_tracker) : analytics
                    .newTracker(R.xml.ecommerce_tracker);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }

    @Override
    public void setTrackerScreenName(String path) {
        // Set screen name.
        // Where path is a String representing the screen name.
        tracker.setScreenName(path);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void sendTrackerEvent(Category category, Action action) {
        // Set screen name.
        // Where path is a String representing the screen name.
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category.name())
                .setAction(action.name())
                .build());
    }

    @Override
    public void sendTrackerEvent(Category category, Action action, String label) {
        // Set screen name.
        // Where path is a String representing the screen name.
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category.name())
                .setAction(action.name())
                .setLabel(label)
                .build());
    }

    @Override
    public void sendEmail(String subject) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"3drenodev@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT   , "");
        try {
            startActivity(Intent.createChooser(i, "Envoyer un mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AndroidLauncher.this, "Aucun client mail n'est installé.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        config.r = config.g = config.b = 8;

        MainApplicationAdapter application = new MainApplicationAdapter(this);

        initialize(application, config);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscopeListener = new GyroscopeListener();

        initListeners();

        //gyroscopeListener.startTask();
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

            application.setVersionName(pInfo.versionName);
            application.setVersionCode(pInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {

            application.setVersionName("1.0.x");
            application.setVersionCode(1);
            e.printStackTrace();
        }

        tracker = this.getTracker(TrackerName.APP_TRACKER);
        globalTracker = this.getTracker(TrackerName.GLOBAL_TRACKER);

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
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    //when this Activity resumes
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
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
        super.onStop();
    }

}
