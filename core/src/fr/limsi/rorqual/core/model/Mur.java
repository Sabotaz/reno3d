package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.math.Vector3;

import java.util.Date;

import fr.limsi.rorqual.core.dpe.enums.wallproperties.DateIsolationMurEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.TypeIsolationMurEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.TypeMurEnum;

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
    private TypeIsolationMurEnum typeIsolationMurEnum;
    private DateIsolationMurEnum dateIsolationMurEnum;

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
        this.typeMurEnum = TypeMurEnum.INCONNUE;
        this.typeIsolationMurEnum = TypeIsolationMurEnum.NON_ISOLE;
        this.dateIsolationMurEnum = DateIsolationMurEnum.JAMAIS;
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
        return typeIsolationMurEnum;
    }

    public void setTypeIsolationMurEnum(TypeIsolationMurEnum typeIsolationMurEnum) {
        this.typeIsolationMurEnum = typeIsolationMurEnum;
    }

    public DateIsolationMurEnum getDateIsolationMurEnum() {
        return dateIsolationMurEnum;
    }

    public void setDateIsolationMurEnum(DateIsolationMurEnum dateIsolationMurEnum) {
        this.dateIsolationMurEnum = dateIsolationMurEnum;
    }

    public void anchorAto(Mur other) {
        anchorTo(A, other);
    }

    public void anchorBto(Mur other) {
        anchorTo(B, other);
    }

    private void anchorTo(Vector3 v, Mur other) {

    }

    public void act() {

    }
}
