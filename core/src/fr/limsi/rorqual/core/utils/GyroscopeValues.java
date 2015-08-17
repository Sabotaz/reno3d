package fr.limsi.rorqual.core.utils;

/**
 * Created by christophe on 17/08/15.
 */
public class GyroscopeValues {

    private GyroscopeValues() {}

    /** Holder */
    private static class GyroscopeValuesHolder
    {
        /** Instance unique non préinitialisée */
        private final static GyroscopeValues INSTANCE = new GyroscopeValues();
    }

    public static synchronized GyroscopeValues getInstance() {
        return GyroscopeValuesHolder.INSTANCE;
    }

    private float x = 0;
    private float y = 0;
    private float z = 0;
    private long timestamp = 0;
    private boolean hasGyro = false;
    private static final float NS2S = 1.0f / 1000000000.0f;

    public float getX() {
        return x;
    }

    public void update(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean hasGyro() {
        return hasGyro;
    }

    public void hasGyro(boolean hasGyro) {
        this.hasGyro = hasGyro;
    }
}
