package fr.limsi.rorqual.core.utils;

import com.badlogic.gdx.math.Matrix4;

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
    private boolean hasGyro = false;
    private Matrix4 matrix = new Matrix4();

    public Matrix4 getMatrix() {
        return matrix;
    }

    public void update(float[] mx) {
        matrix.set(new float[]{
                mx[0], mx[1], mx[2], 0,
                mx[3], mx[4], mx[5], 0,
                mx[6], mx[7], mx[8], 0,
                0, 0, 0, 1
        });
    }

    public boolean hasGyro() {
        return hasGyro;
    }

    public void hasGyro(boolean hasGyro) {
        this.hasGyro = hasGyro;
    }
}
