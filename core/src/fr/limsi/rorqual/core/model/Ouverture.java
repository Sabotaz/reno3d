package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector2;

import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeMateriauMenuiserieEnum;
import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeVitrageEnum;

/**
 * Created by ricordeau on 20/07/15.
 */
public class Ouverture {

    // Attributs
    protected Mur mur;
    protected Vector2 position;
    protected float width;
    protected float height;
    protected float surface;
    protected Model model;
    protected TypeMateriauMenuiserieEnum typeMateriauMenuiserie;
    protected TypeVitrageEnum typeVitrage;
    protected float coefficientDeTransmissionThermique;
    protected float deperdition;

    // Constructeur
    public Ouverture(Mur mur, Vector2 position, float width, float height, Model model){
        this.mur=mur;
        this.position=position;
        this.width=width;
        this.height=height;
        this.surface=width*height;
        this.model = model;
        this.typeMateriauMenuiserie = TypeMateriauMenuiserieEnum.INCONNUE;
        this.typeVitrage = TypeVitrageEnum.INCONNUE;
        this.coefficientDeTransmissionThermique=2;
        this.deperdition=surface*coefficientDeTransmissionThermique;
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
    public Model getModel() {
        return model;
    }
    public void setModel(Model model) {
        this.model = model;
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
}
