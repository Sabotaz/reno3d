package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.UBJsonReader;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypePorte;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.TypeMurEnum;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.utils.AssetManager;

/**
 * Created by ricordeau on 20/07/15.
 */
// classe modélisant une porte (thermique)
public class Porte extends Ouverture{

    @XStreamOmitField
    public static float DEFAULT_Y = 0.0f;
    @XStreamOmitField
    public static float DEFAULT_WIDTH = 1.0f;
    @XStreamOmitField
    public static float DEFAULT_HEIGHT = 2.15f;
    @XStreamAlias("coefficientTransmissionThermique")
    private double coefficientDeTransmissionThermique;
    @XStreamAlias("deperdition")
    private double deperdition;
    @XStreamAlias("typePorte")
    public TypePorte typePorte;

    // Attributs
    public Porte() {
        this(null, DEFAULT_WIDTH);
    }

    // Constructeur
    public Porte(Mur mur, float x) {
        // x est le milieu ?
        this(mur, x - DEFAULT_WIDTH / 2, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Porte(Mur mur, float x, float y, float width, float height) {
        super(mur, new Vector2(x, y), width, height);
    }

    // Getter & Setter
    public double getCoefficientDeTransmissionThermique() {
        return coefficientDeTransmissionThermique;
    }
    public void setCoefficientDeTransmissionThermique(double coefficientDeTransmissionThermique) {
        this.coefficientDeTransmissionThermique = coefficientDeTransmissionThermique;
    }
    public double getDeperdition() {
        return deperdition;
    }
    public void setDeperdition(double deperdition) {
        this.deperdition = deperdition;
    }

    public TypePorte getTypePorte(){
        return this.typePorte;
    }

    public void actualiseCoefficientDeTransmissionThermique() {
        double u=4.5;
        double deperdition=0;
        if (this.getMur().getTypeMur().equals(TypeMurEnum.MUR_INTERIEUR)){
            this.deperdition=0;
            return;
        }else{
            switch (this.typePorte) {
                case PORTE_OPAQUE_PLEINE:
                    u=3.5;
                    deperdition = 3.5*this.surface;
                    break;
                case PORTE_AVEC_MOINS_DE_30_POURCENT_DE_SIMPLE_VITRAGE:
                    u=4;
                    deperdition = 4*this.surface;
                    break;
                case PORTE_AVEC_30_60_POURCENT_DE_SIMPLE_VITRAGE:
                    u=4.5;
                    deperdition = 4.5*this.surface;
                    break;
                case PORTE_AVEC_DOUBLE_VITRAGE:
                    u=3.3;
                    deperdition = 3.3*this.surface;
                    break;
                case PORTE_OPAQUE_PLEINE_ISOLEE:
                    u=2;
                    deperdition = 2*this.surface;
                    break;
                case PORTE_PRECEDEE_D_UN_SAS:
                    u=1.5;
                    deperdition = 1.5*this.surface;
                    break;
            }
            this.coefficientDeTransmissionThermique=u;
            this.actualiseDeperdition();
        }
    }
    public void actualiseDeperdition(){
        double u=this.coefficientDeTransmissionThermique;
        double deperdition=0;
        if (!this.getMur().getTypeMur().equals(TypeMurEnum.MUR_INTERIEUR)){
            switch (this.getMur().getTypeMur()){
                case MUR_DONNANT_SUR_UNE_VERANDA_NON_CHAUFFE:
                    switch (this.getMur().getOrientationMur()){
                        case INCONNUE:
                        case NORD:
                            switch(this.getMur().getDateIsolationMurEnum()){
                                case INCONNUE:
                                case JAMAIS:
                                    // Non isolé
                                    deperdition=0.95*this.surface*u;
                                    break;
                                default:
                                    // isolé
                                    deperdition=0.85*this.surface*u;
                                    break;
                            }
                            break;
                        case EST:
                        case OUEST:
                            switch(this.getMur().getDateIsolationMurEnum()){
                                case INCONNUE:
                                case JAMAIS:
                                    // Non isolé
                                    deperdition= 0.63*this.surface*u;
                                    break;
                                default:
                                    // isolé
                                    deperdition= 0.6*this.surface*u;
                                    break;
                            }
                            break;
                        case SUD:
                            switch(this.getMur().getDateIsolationMurEnum()){
                                case INCONNUE:
                                case JAMAIS:
                                    // Non isolé
                                    deperdition= 0.6*this.surface*u;
                                    break;
                                default:
                                    // isolé
                                    deperdition= 0.55*this.surface*u;
                                    break;
                            }
                            break;
                    }
                    break;
                case MUR_DONNANT_SUR_UN_LOCAL_NON_CHAUFFE:
                    switch (this.getMur().getDateIsolationMurEnum()) {
                        case INCONNUE:
                        case JAMAIS:
                            // Non isolé
                            deperdition= 0.95*this.surface*u;
                            break;
                        default:
                            // isolé
                            deperdition= 0.85*this.surface*u;
                            break;
                    }
                    break;
                case MUR_DONNANT_SUR_UNE_AUTRE_HABITATION:
                case MUR_DONNANT_SUR_EXTERIEUR:
                    deperdition= this.surface*u;
                    break;
                case MUR_INTERIEUR:
                case MUR_DONNANT_SUR_UNE_VERANDA_CHAUFFE:
                    break;
            }
        }
        this.deperdition=deperdition;
        Event e = new Event(DpeEvent.DEPERDITION_PORTES_CHANGED, null);
        EventManager.getInstance().put(Channel.DPE, e);
    }

    @Override
    public String toString(){
        return "Porte ->  s="+surface+" u="+coefficientDeTransmissionThermique+ " dp="+deperdition;
    }

    @Override
    public void copy(Ouverture other) {
        super.copy(other);
        if (other instanceof Porte) {
            Porte porte = (Porte) other;
            coefficientDeTransmissionThermique = porte.coefficientDeTransmissionThermique;
            deperdition = porte.deperdition;
            typePorte = porte.typePorte;
        }
    }

}
