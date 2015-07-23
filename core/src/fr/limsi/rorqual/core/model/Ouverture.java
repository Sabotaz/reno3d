package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Extrude;
import eu.mihosoft.vrl.v3d.Vector3d;
import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeMateriauMenuiserieEnum;
import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeVitrageEnum;
import fr.limsi.rorqual.core.utils.CSGUtils;

/**
 * Created by ricordeau on 20/07/15.
 */
public abstract class Ouverture extends ActableModel {

    // Attributs
    protected Mur mur;
    protected Vector2 position;
    protected float width;
    protected float height;
    protected float surface;
    protected TypeMateriauMenuiserieEnum typeMateriauMenuiserie = TypeMateriauMenuiserieEnum.INCONNUE;
    protected TypeVitrageEnum typeVitrage = TypeVitrageEnum.INCONNUE;
    protected float coefficientDeTransmissionThermique = 2;
    protected float deperdition = surface*coefficientDeTransmissionThermique;

    // Constructeur
    public Ouverture(Mur mur, Vector2 position, float width, float height){
        this.mur=mur;
        mur.addOuverture(this);
        this.position=position;
        this.width=width;
        this.height=height;
        this.surface=width*height;
    }

    // Getter & Setter
    public Mur getMur() {
        return mur;
    }
    public void setMur(Mur mur) {
        this.mur = mur;
    }
    public Vector2 getPosition() {
        return position;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
    }
    public float getWidth() {
        return width;
    }
    public void setWidth(float width) {
        this.width = width;
    }
    public float getHeight() {
        return height;
    }
    public void setHeight(float height) {
        this.height = height;
    }
    public float getSurface() {
        return surface;
    }
    public void setSurface(float surface) {
        this.surface = surface;
    }
    public TypeMateriauMenuiserieEnum getTypeMateriauMenuiserie() {
        return typeMateriauMenuiserie;
    }
    public void setTypeMateriauMenuiserie(TypeMateriauMenuiserieEnum typeMateriauMenuiserie) {
        this.typeMateriauMenuiserie = typeMateriauMenuiserie;
    }
    public TypeVitrageEnum getTypeVitrage() {
        return typeVitrage;
    }
    public void setTypeVitrage(TypeVitrageEnum typeVitrage) {
        this.typeVitrage = typeVitrage;
    }
    public float getCoefficientDeTransmissionThermique() {
        return coefficientDeTransmissionThermique;
    }
    public void setCoefficientDeTransmissionThermique(float coefficientDeTransmissionThermique) {
        this.coefficientDeTransmissionThermique = coefficientDeTransmissionThermique;
    }
    public float getDeperdition() {
        return deperdition;
    }
    public void setDeperdition(float deperdition) {
        this.deperdition = deperdition;
    }

    public CSG getCSG() {

        Vector3 A = mur.getA();
        Vector3 B = mur.getB();

        Vector3 z_shape = Vector3.Z.cpy().scl(this.height);

        Vector3 x_dir = B.cpy().sub(A);
        Vector3 openingA = A.cpy().add(x_dir.cpy().setLength(this.position.x)).add(z_shape.cpy().setLength(this.position.y));
        Vector3 openingB = openingA.cpy().add(x_dir.cpy().setLength(this.width));

        Vector3 y_dir = x_dir.cpy().crs(Vector3.Z).setLength(mur.getDepth()/2);

        Vector3d dir = CSGUtils.castVector(z_shape);

        List<Vector3d> face = new ArrayList<Vector3d>();
        face.add(CSGUtils.castVector(openingA.cpy().add(y_dir)));
        face.add(CSGUtils.castVector(openingA.cpy().sub(y_dir)));
        face.add(CSGUtils.castVector(openingB.cpy().add(y_dir)));
        face.add(CSGUtils.castVector(openingB.cpy().sub(y_dir)));

        CSG csg = Extrude.points(dir, face);

        return csg;
    }

    public void act() {

    }
}
