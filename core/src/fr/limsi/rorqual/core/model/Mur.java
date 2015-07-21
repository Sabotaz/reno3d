package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Date;

import fr.limsi.rorqual.core.dpe.enums.wallproperties.DateIsolationMurEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.OrientationMurEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.TypeIsolationMurEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.TypeMurEnum;

import java.util.ArrayList;

import fr.limsi.rorqual.core.model.utils.Coin;
import fr.limsi.rorqual.core.model.utils.MyVector2;

/**
 * Created by ricordeau on 20/07/15.
 */
public class Mur extends ActableModel {

    private Vector3 A = new Vector3();
    private Vector3 B = new Vector3();
    private float height;
    private float width;
    private float depth;
    private TypeMurEnum typeMurEnum;
    private TypeIsolationMurEnum typeIsolationMur;
    private DateIsolationMurEnum dateIsolationMur;
    private OrientationMurEnum orientationMur;

    private static float DEFAULT_DEPTH = 0.2f;
    private static float DEFAULT_HEIGHT = 2.8f;

    private ArrayList<Coin> coins = new ArrayList<Coin>();

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
        this.typeMurEnum = TypeMurEnum.INCONNUE;
        this.typeIsolationMur = TypeIsolationMurEnum.NON_ISOLE;
        this.dateIsolationMur = DateIsolationMurEnum.JAMAIS;
        this.orientationMur = OrientationMurEnum.INCONNUE;
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

    public TypeMurEnum getTypeMurEnum() {
        return typeMurEnum;
    }

    public void setTypeMurEnum(TypeMurEnum typeMurEnum) {
        this.typeMurEnum = typeMurEnum;
    }

    public TypeIsolationMurEnum getTypeIsolationMurEnum() {
        return typeIsolationMur;
    }

    public void setTypeIsolationMurEnum(TypeIsolationMurEnum typeIsolationMurEnum) {
        this.typeIsolationMur = typeIsolationMurEnum;
    }

    public DateIsolationMurEnum getDateIsolationMurEnum() {
        return dateIsolationMur;
    }

    public void setDateIsolationMurEnum(DateIsolationMurEnum dateIsolationMurEnum) {
        this.dateIsolationMur = dateIsolationMurEnum;
    }

    public ArrayList<Vector3> getAnchors(Vector3 pt, float depth) {
        ArrayList<Vector3> anchors = new ArrayList<Vector3>();
        anchors.add(A);
        anchors.add(B);

        Vector3 x_dir = B.cpy().sub(A).setLength(depth/2);
        Vector3 y_dir = x_dir.cpy().crs(Vector3.Z).setLength(this.depth/2);

        // px : +x_dir      mx : -x_dir
        // py : +y_dir      my : -y_dir
        Vector3 a_px_py = A.cpy().add(x_dir).add(y_dir);
        Vector3 a_mx_py = A.cpy().sub(x_dir).add(y_dir);
        Vector3 a_px_my = A.cpy().add(x_dir).sub(y_dir);
        Vector3 a_mx_my = A.cpy().sub(x_dir).sub(y_dir);
        anchors.add(a_px_py);
        anchors.add(a_mx_py);
        anchors.add(a_px_my);
        anchors.add(a_mx_my);

        Vector3 b_px_py = B.cpy().add(x_dir).add(y_dir);
        Vector3 b_mx_py = B.cpy().sub(x_dir).add(y_dir);
        Vector3 b_px_my = B.cpy().add(x_dir).sub(y_dir);
        Vector3 b_mx_my = B.cpy().sub(x_dir).sub(y_dir);
        anchors.add(b_px_py);
        anchors.add(b_mx_py);
        anchors.add(b_px_my);
        anchors.add(b_mx_my);

        Vector2 intersection = new Vector2();
        Vector2 pt1 = new MyVector2(pt);
        Vector2 pt2 = new MyVector2(pt.cpy().add(y_dir));
        // intersect +y_dir
        Vector2 coin1 = new MyVector2(A.cpy().add(y_dir));
        Vector2 coin2 = new MyVector2(A.cpy().sub(y_dir));
        if (Intersector.intersectLines(pt1, pt2, coin1, coin2, intersection))
            anchors.add(new Vector3(intersection.x, intersection.y, 0));
        // intersect -y_dir
        coin1 = new MyVector2(B.cpy().add(y_dir));
        coin2 = new MyVector2(B.cpy().sub(y_dir));
        if (Intersector.intersectLines(pt2, pt2, coin1, coin2, intersection))
            anchors.add(new Vector3(intersection.x, intersection.y, 0));

        return anchors;
    }

    public void act() {

    }
}
