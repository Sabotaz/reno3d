package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by ricordeau on 20/07/15.
 */
public class Mur {

    private Vector3 A = new Vector3();
    private Vector3 B = new Vector3();

    private float height;
    private float width;
    private float depth;

    private static float DEFAULT_DEPTH = 0.2f;
    private static float DEFAULT_HEIGHT = 2.8f;

    public Mur(Vector3 a, Vector3 b) {
        this(a, b, DEFAULT_DEPTH, DEFAULT_HEIGHT);
    }

    public Mur(Vector3 a, Vector3 b, float d) {
        this(a,b,d, DEFAULT_HEIGHT);
    }

    public Mur(Vector3 a, Vector3 b, float d, float h) {
        this.A = new Vector3(a);
        this.B = new Vector3(b);
        this.height = h;
        this.depth = d;
        this.width = b.cpy().sub(a).len();
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    private void setWidth(float width) { // PRIVATE ! changed programatically
        this.width = width;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public Vector3 getA() {
        return A;
    }

    public void setA(Vector3 a) {
        A = a;
    }

    public Vector3 getB() {
        return B;
    }

    public void setB(Vector3 b) {
        B = b;
    }
}
