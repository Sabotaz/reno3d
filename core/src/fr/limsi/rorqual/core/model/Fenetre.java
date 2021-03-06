package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeFenetre;
import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeFermetureEnum;
import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeMasqueEnum;
import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeMateriauMenuiserieEnum;
import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeVitrageEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.TypeMurEnum;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventManager;

/**
 * Created by ricordeau on 20/07/15.
 */
// Classe modélisant une fenêtre (thermique)
public class Fenetre extends Ouverture {

    @XStreamOmitField
    public static float DEFAULT_Y = 1.f;
    @XStreamOmitField
    public static float DEFAULT_WIDTH = 0.6f;
    @XStreamOmitField
    public static float DEFAULT_HEIGHT = 0.75f;
    @XStreamAlias("coefficientTransmissionThermique")
    private float coefficientDeTransmissionThermique=4;
    @XStreamAlias("deperdition")
    private float deperdition;
    @XStreamAlias("typeFenetre")
    public TypeFenetre typeFenetre;
    @XStreamAlias("typeMateriau")
    public TypeMateriauMenuiserieEnum typeMateriau;
    @XStreamAlias("typeVitrage")
    private TypeVitrageEnum typeVitrage=TypeVitrageEnum.SIMPLE_VITRAGE;
    @XStreamAlias("typeFermeture")
    private TypeFermetureEnum typeFermeture=TypeFermetureEnum.SANS_FERMETURE;
    @XStreamAlias("masqueProche")
    private TypeMasqueEnum masqueProche=TypeMasqueEnum.ABSENCE_MASQUE_PROCHE;
    @XStreamAlias("masqueLointain")
    private TypeMasqueEnum masqueLointain=TypeMasqueEnum.ABSENCE_MASQUE_LOINTAIN;
    @XStreamOmitField
    private float fts=0.64f;
    @XStreamOmitField
    private float fe1=1;
    @XStreamOmitField
    private float fe2=1;
    @XStreamOmitField
    private float c1=1.2f;
    @XStreamOmitField
    private float bas=1;
    @XStreamOmitField
    private float sse=0;

    // Constructeur
    public Fenetre() {
        this(null, DEFAULT_WIDTH);
    }
    public Fenetre(Mur mur, float x) {
        // x est le milieu ?
        this(mur, x - DEFAULT_WIDTH / 2, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    public Fenetre(Mur mur, float x, float y, float width, float height) {
        super(mur, new Vector2(x, y), width, height);
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
    public TypeFenetre getTypeFenetre() {
        return typeFenetre;
    }

    public void setTypeFenetre(TypeFenetre typeFenetre) {
        this.typeFenetre = typeFenetre;
    }

    public TypeMateriauMenuiserieEnum getTypeMateriau() {
        return typeMateriau;
    }

    public void setTypeMateriau(TypeMateriauMenuiserieEnum typeMateriau) {
        this.typeMateriau = typeMateriau;
    }

    public void setDeperdition(float deperdition) {
        this.deperdition = deperdition;
    }

    public TypeVitrageEnum getTypeVitrage() {
        return typeVitrage;
    }

    public void setTypeVitrage(TypeVitrageEnum typeVitrage) {
        this.typeVitrage = typeVitrage;
        this.actualiseCoeffTransmissionThermiqueFenetre();
        this.actualiseFts();
    }

    public TypeFermetureEnum getTypeFermeture() {
        return typeFermeture;
    }

    public void setTypeFermeture(TypeFermetureEnum typeFermeture) {
        this.typeFermeture = typeFermeture;
        this.actualiseCoeffTransmissionThermiqueFenetre();
    }

    public void setMasqueProche(TypeMasqueEnum masqueProche) {
        this.masqueProche = masqueProche;
        this.actualiseFe1();
    }

    public void setMasqueLointain(TypeMasqueEnum masqueLointain) {
        this.masqueLointain = masqueLointain;
        this.actualiseFe2();
    }

    public void actualiseDeperdition(){
        float u=this.coefficientDeTransmissionThermique;
        float deperdition=0;
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
                                    deperdition=0.95f*this.surface*u;
                                    break;
                                default:
                                    // isolé
                                    deperdition=0.85f*this.surface*u;
                                    break;
                            }
                            break;
                        case EST:
                        case OUEST:
                            switch(this.getMur().getDateIsolationMurEnum()){
                                case INCONNUE:
                                case JAMAIS:
                                    // Non isolé
                                    deperdition= 0.63f*this.surface*u;
                                    break;
                                default:
                                    // isolé
                                    deperdition= 0.6f*this.surface*u;
                                    break;
                            }
                            break;
                        case SUD:
                            switch(this.getMur().getDateIsolationMurEnum()){
                                case INCONNUE:
                                case JAMAIS:
                                    // Non isolé
                                    deperdition= 0.6f*this.surface*u;
                                    break;
                                default:
                                    // isolé
                                    deperdition= 0.55f*this.surface*u;
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
                            deperdition= 0.95f*this.surface*u;
                            break;
                        default:
                            // isolé
                            deperdition= 0.85f*this.surface*u;
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
        Event e = new Event(DpeEvent.DEPERDITION_FENETRES_CHANGED, null);
        EventManager.getInstance().put(Channel.DPE, e);
    }
    public void actualiseCoeffTransmissionThermiqueFenetre(){
        float u=5f;
        switch(this.typeMateriau){
            case METALLIQUE:
                switch (this.typeFenetre){
                    case FENETRE_SIMPLE_VENTAIL_BATTANTE:
                    case FENETRE_DOUBLE_VENTAIL_BATTANTE:
                        switch (this.typeVitrage){
                            case SIMPLE_VITRAGE:
                                u=4.95f;
                                break;
                            case SURVITRAGE:
                                u=4;
                                break;
                            case DOUBLE_VITRAGE_INF_1990:
                                u=3.7f;
                                break;
                            case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                                u=3.6f;
                                break;
                            case DOUBLE_VITRAGE_SUP_2001:
                                u=2.25f;
                                break;
                            case TRIPLE_VITRAGE:
                                u=1.88f;
                                break;
                        }
                        break;
                    case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                        switch (typeVitrage){
                            case SIMPLE_VITRAGE:
                                u=4.63f;
                                break;
                            case SURVITRAGE:
                                u=3.46f;
                                break;
                            case DOUBLE_VITRAGE_INF_1990:
                                u=3.46f;
                                break;
                            case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                                u=3.36f;
                                break;
                            case DOUBLE_VITRAGE_SUP_2001:
                                u=2.18f;
                                break;
                            case TRIPLE_VITRAGE:
                                u=1.65f;
                                break;
                        }
                        break;
                    case FENETRE_DE_TOIT:
                    case LUCARNE:
                        switch (typeVitrage){
                            case SIMPLE_VITRAGE:
                                u=4.95f;
                                break;
                            case SURVITRAGE:
                                u=4.38f;
                                break;
                            case DOUBLE_VITRAGE_INF_1990:
                                u=4.01f;
                                break;
                            case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                                u=3.92f;
                                break;
                            case DOUBLE_VITRAGE_SUP_2001:
                                u=3.30f;
                                break;
                            case TRIPLE_VITRAGE:
                                u=3.15f;
                                break;
                        }
                        break;
                }
                break;
            case PVC:
                switch (typeFenetre){
                    case FENETRE_DOUBLE_VENTAIL_BATTANTE:
                    case FENETRE_SIMPLE_VENTAIL_BATTANTE:
                        switch (typeVitrage){
                            case SIMPLE_VITRAGE:
                                u=3.90f;
                                break;
                            case SURVITRAGE:
                                u=2.75f;
                                break;
                            case DOUBLE_VITRAGE_INF_1990:
                                u=2.45f;
                                break;
                            case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                                u=2.35f;
                                break;
                            case DOUBLE_VITRAGE_SUP_2001:
                                u=1.70f;
                                break;
                            case TRIPLE_VITRAGE:
                                u=1.24f;
                                break;
                        }
                        break;
                    case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                        switch (typeVitrage){
                            case SIMPLE_VITRAGE:
                                u=4.25f;
                                break;
                            case SURVITRAGE:
                                u=3.00f;
                                break;
                            case DOUBLE_VITRAGE_INF_1990:
                                u=2.62f;
                                break;
                            case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                                u=2.52f;
                                break;
                            case DOUBLE_VITRAGE_SUP_2001:
                                u=1.85f;
                                break;
                            case TRIPLE_VITRAGE:
                                u=1.39f;
                                break;
                        }
                        break;
                    case FENETRE_DE_TOIT:
                    case LUCARNE:
                        switch (typeVitrage){
                            case SIMPLE_VITRAGE:
                                u=3.90f;
                                break;
                            case SURVITRAGE:
                                u=2.92f;
                                break;
                            case DOUBLE_VITRAGE_INF_1990:
                                u=2.70f;
                                break;
                            case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                                u=2.70f;
                                break;
                            case DOUBLE_VITRAGE_SUP_2001:
                                u=2.01f;
                                break;
                            case TRIPLE_VITRAGE:
                                u=1.39f;
                                break;
                        }
                        break;
                }
                break;
            case BOIS:
                switch (typeFenetre){
                    case FENETRE_SIMPLE_VENTAIL_BATTANTE:
                    case FENETRE_DOUBLE_VENTAIL_BATTANTE:
                        switch (typeVitrage){
                            case SIMPLE_VITRAGE:
                                u=4.2f;
                                break;
                            case SURVITRAGE:
                                u=2.9f;
                                break;
                            case DOUBLE_VITRAGE_INF_1990:
                                u=2.7f;
                                break;
                            case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                                u=2.55f;
                                break;
                            case DOUBLE_VITRAGE_SUP_2001:
                                u=1.75f;
                                break;
                            case TRIPLE_VITRAGE:
                                u=1.24f;
                                break;
                        }
                        break;
                    case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                        switch (typeVitrage){
                            case SIMPLE_VITRAGE:
                                u=4.2f;
                                break;
                            case SURVITRAGE:
                                u=2.9f;
                                break;
                            case DOUBLE_VITRAGE_INF_1990:
                                u=2.7f;
                                break;
                            case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                                u=2.55f;
                                break;
                            case DOUBLE_VITRAGE_SUP_2001:
                                u=1.75f;
                                break;
                            case TRIPLE_VITRAGE:
                                u=1.24f;
                                break;
                        }
                        break;
                    case FENETRE_DE_TOIT:
                    case LUCARNE:
                        switch (typeVitrage){
                            case SIMPLE_VITRAGE:
                                u=4.2f;
                                break;
                            case SURVITRAGE:
                                u=3.15f;
                                break;
                            case DOUBLE_VITRAGE_INF_1990:
                                u=2.96f;
                                break;
                            case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                                u=2.9f;
                                break;
                            case DOUBLE_VITRAGE_SUP_2001:
                                u=2.04f;
                                break;
                            case TRIPLE_VITRAGE:
                                u=1.46f;
                                break;
                        }
                        break;
                }
                break;
        }
        if (u != 5f) // INCONNU
            u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);

        this.coefficientDeTransmissionThermique=u;
        this.actualiseDeperdition();
    }
    public float actualiseCoeffTransmissionThermiqueAvecFermeture(float uDevantEtreActualise){
        float nouveauU=uDevantEtreActualise;
        float uMapping[][] = {
                {1.3f, 1.1f, 1.1f, 1.1f},
                {1.4f, 1.2f, 1.2f, 1.2f},
                {1.5f, 1.3f, 1.3f, 1.3f},
                {1.6f, 1.4f, 1.4f, 1.3f},
                {1.7f, 1.5f, 1.5f, 1.4f},
                {1.8f, 1.6f, 1.5f, 1.5f},
                {1.9f, 1.7f, 1.6f, 1.6f},
                {2.0f, 1.8f, 1.7f, 1.6f},
                {2.1f, 1.9f, 1.8f, 1.7f},
                {2.2f, 1.9f, 1.9f, 1.8f},
                {2.3f, 2.0f, 1.9f, 1.9f},
                {2.4f, 2.1f, 2.0f, 2.0f},
                {2.5f, 2.2f, 2.1f, 2.0f},
                {2.6f, 2.3f, 2.2f, 2.1f},
                {2.7f, 2.4f, 2.3f, 2.2f},
                {2.8f, 2.5f, 2.3f, 2.2f},
                {2.9f, 2.5f, 2.4f, 2.3f},
                {3.0f, 2.6f, 2.5f, 2.4f},
                {3.2f, 2.7f, 2.6f, 2.5f},
                {3.4f, 2.9f, 2.7f, 2.6f},
                {3.6f, 3.0f, 2.9f, 2.7f},
                {3.8f, 3.2f, 3.0f, 2.9f},
                {4.0f, 3.4f, 3.1f, 3.0f},
                {4.2f, 3.5f, 3.3f, 3.1f},
                {4.4f, 3.7f, 3.4f, 3.3f},
                {4.6f, 3.8f, 3.6f, 3.4f},
                {4.8f, 4.0f, 3.7f, 3.5f},
                {5.0f, 4.1f, 3.8f, 3.7f},
                {5.2f, 4.3f, 4.0f, 3.8f},
                {5.4f, 4.4f, 4.1f, 3.9f},
                {5.6f, 4.6f, 4.2f, 4.0f},
                {5.8f, 4.7f, 4.4f, 4.2f},
                {6.0f, 4.9f, 4.5f, 4.3f},
                {6.2f, 5.0f, 4.6f, 4.4f},
                {Float.MAX_VALUE, 5.2f, 4.8f, 4.5f}
        };
        switch(typeFermeture){
            case JALOUSIE_ACCORDEON:
            case VOLET_BATTANT_AVEC_AJOURES_FIXES:
                for (float[] us : uMapping) nouveauU = uDevantEtreActualise >= us[0] ? us[1] : nouveauU;
                break;
            case VOLET_ROULANT_EN_METAL:
            case FERMETURE_SANS_AJOURES:
                for (float[] us : uMapping) nouveauU = uDevantEtreActualise >= us[0] ? us[2] : nouveauU;
                break;
            case VOLET_BATTANT_BOIS:
            case VOLET_ROULANT_PVC:
                for (float[] us : uMapping) nouveauU = uDevantEtreActualise >= us[0] ? us[3] : nouveauU;
                break;
            case SANS_FERMETURE:
                break;
        }
        return nouveauU;
    }
    public void actualiseBas(){
        if(this.getMur().getTypeMur().equals(TypeMurEnum.MUR_DONNANT_SUR_UN_LOCAL_NON_CHAUFFE)){
            this.bas=0;
        }else{
            switch(this.getMur().getOrientationMur()){
                case NORD:
                    this.bas=1;
                    break;
                case SUD:
                    this.bas=0.22f;
                    break;
                case EST:
                case OUEST:
                    this.bas=0.31f;
                    break;
            }
        }
        actualiseSse();
    }
    public void actualiseC1(){
        switch (this.typeFenetre){
            case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
            case FENETRE_SIMPLE_VENTAIL_BATTANTE:
            case FENETRE_DOUBLE_VENTAIL_BATTANTE:
            case LUCARNE:
                switch (this.getMur().getOrientationMur()){
                    case SUD:
                        this.c1=1.1f;
                        break;
                    case OUEST:
                        this.c1=0.57f;
                        break;
                    case EST:
                        this.c1=0.57f;
                        break;
                    case NORD:
                        this.c1=0.2f;
                        break;
                }
                break;
            case FENETRE_DE_TOIT:
                switch (this.getMur().getOrientationMur()){
                    case SUD:
                        this.c1=1.2f;
                        break;
                    case OUEST:
                        this.c1=0.75f;
                        break;
                    case EST:
                        this.c1=0.75f;
                        break;
                    case NORD:
                        this.c1=0.32f;
                        break;
                }
                break;
        }
        actualiseSse();
    }
    public void actualiseFts(){
        switch (this.typeMateriau){
            case BOIS:
                switch(this.typeVitrage){
                    case SIMPLE_VITRAGE:
                    case SURVITRAGE:
                        this.fts=0.52f;
                        break;
                    case DOUBLE_VITRAGE_INF_1990:
                    case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                        this.fts=0.47f;
                        break;
                    case DOUBLE_VITRAGE_SUP_2001:
                        this.fts=0.4f;
                        break;
                    case TRIPLE_VITRAGE:
                        this.fts=0.41f;
                        break;
                }
                break;
            case PVC:
                switch(this.typeVitrage){
                    case SIMPLE_VITRAGE:
                    case SURVITRAGE:
                        switch (this.typeFenetre){
                            case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                                this.fts=0.54f;
                                break;
                            default:
                                this.fts=0.49f;
                                break;
                        }
                        break;
                    case DOUBLE_VITRAGE_INF_1990:
                    case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                        switch (this.typeFenetre){
                            case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                                this.fts=0.48f;
                                break;
                            default:
                                this.fts=0.44f;
                                break;
                        }
                        break;
                    case DOUBLE_VITRAGE_SUP_2001:
                        switch (this.typeFenetre){
                            case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                                this.fts=0.41f;
                                break;
                            default:
                                this.fts=0.38f;
                                break;
                        }
                        break;
                    case TRIPLE_VITRAGE:
                        switch (this.typeFenetre){
                            case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                                this.fts=0.43f;
                                break;
                            default:
                                this.fts=0.39f;
                                break;
                        }
                        break;
                }
                break;
            case METALLIQUE:
                switch(this.typeVitrage){
                    case SIMPLE_VITRAGE:
                    case SURVITRAGE:
                        switch (this.typeFenetre){
                            case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                                this.fts=0.59f;
                                break;
                            default:
                                this.fts=0.54f;
                                break;
                        }
                        break;
                    case DOUBLE_VITRAGE_INF_1990:
                    case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                        switch (this.typeFenetre){
                            case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                                this.fts=0.53f;
                                break;
                            default:
                                this.fts=0.485f;
                                break;
                        }
                        break;
                    case DOUBLE_VITRAGE_SUP_2001:
                        switch (this.typeFenetre){
                            case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                                this.fts=0.46f;
                                break;
                            default:
                                this.fts=0.42f;
                                break;
                        }
                        break;
                    case TRIPLE_VITRAGE:
                        switch (this.typeFenetre){
                            case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                                this.fts=0.47f;
                                break;
                            default:
                                this.fts=0.43f;
                                break;
                        }
                        break;
                }
                break;
        }
        actualiseSse();
    }
    public void actualiseFe1(){
        if (this.masqueProche.equals(TypeMasqueEnum.ABSENCE_MASQUE_PROCHE)){
            this.fe1=1;
        }else if(this.masqueProche.equals(TypeMasqueEnum.PRESENCE_MASQUE_PROCHE)){
            this.fe1=0.55f;
        }
        actualiseSse();
    }
    public void actualiseFe2(){
        if (this.masqueLointain.equals(TypeMasqueEnum.ABSENCE_MASQUE_LOINTAIN)){
            this.fe2=1;
        }else if(this.masqueLointain.equals(TypeMasqueEnum.PRESENCE_MASQUE_LOINTAIN_PARTIEL)){
            switch(this.getMur().getOrientationMur()){
                case SUD:
                    this.fe2=0.3f;
                    break;
                case EST:
                case OUEST:
                    this.fe2=0.4f;
                    break;
                case NORD:
                    this.fe2=1;
                    break;
            }
        }else if(this.masqueLointain.equals(TypeMasqueEnum.PRESENCE_MASQUE_LOINTAIN_TOTAL)){
            switch(this.getMur().getOrientationMur()){
                case SUD:
                    this.fe2=0.1f;
                    break;
                case EST:
                case OUEST:
                    this.fe2=0.2f;
                    break;
                case NORD:
                    this.fe2=1;
                    break;
            }
        }
        actualiseSse();
    }

    public void actualiseSse(){
        sse=surface*fts*fe1*fe2*c1*bas;
    }

    public float getSurfaceSudEquivalente(){
        return this.sse;
    }

    public TypeMasqueEnum getMasqueProche(){
        return this.masqueProche;
    }

    public TypeMasqueEnum getMasqueLointain(){
        return this.masqueLointain;
    }

    @Override
    public String toString(){
        return "Fenetre ->  s="+surface+" u="+coefficientDeTransmissionThermique+ " dp="+deperdition;
    }

    @Override
    public void copy(Ouverture other) {
        super.copy(other);
        if (other instanceof Fenetre) {
            Fenetre fenetre = (Fenetre) other;
            coefficientDeTransmissionThermique = fenetre.coefficientDeTransmissionThermique;
            deperdition = fenetre.deperdition;
            typeFenetre = fenetre.typeFenetre;
            typeMateriau = fenetre.typeMateriau;
            typeVitrage = fenetre.typeVitrage;
            typeFermeture = fenetre.typeFermeture;
            masqueProche = fenetre.masqueProche;
            masqueLointain = fenetre.masqueLointain;
        }
    }
}
