package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeFenetre;
import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeFermetureEnum;
import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeMasqueEnum;
import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeMateriauMenuiserieEnum;
import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeVitrageEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.TypeMurEnum;

/**
 * Created by ricordeau on 20/07/15.
 */
public class Fenetre extends Ouverture {

    static float DEFAULT_Y = 1.f;
    static float DEFAULT_WIDTH = 0.6f;
    static float DEFAULT_HEIGHT = 0.75f;
    private double coefficientDeTransmissionThermique=4;
    private double deperdition;
    public TypeFenetre typeFenetre;
    public TypeMateriauMenuiserieEnum typeMateriau;
    private TypeVitrageEnum typeVitrage;
    private TypeFermetureEnum typeFermeture;
    private TypeMasqueEnum masqueProche, masqueLointain;
    private double fts=0.64,fe1=1,fe2=1,c1=1.2,bas=1,sse=0;

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
        this.typeVitrage=TypeVitrageEnum.SIMPLE_VITRAGE;
        this.typeFermeture=TypeFermetureEnum.SANS_FEMETURE;
        this.masqueProche=TypeMasqueEnum.ABSENCE_MASQUE_PROCHE;
        this.masqueLointain=TypeMasqueEnum.ABSENCE_MASQUE_LOINTAIN;
    }

    public double getCoefficientDeTransmissionThermique() {
        return coefficientDeTransmissionThermique;
    }
    public void setCoefficientDeTransmissionThermique(double coefficientDeTransmissionThermique) {
        this.coefficientDeTransmissionThermique = coefficientDeTransmissionThermique;
    }
    public double getDeperdition() {
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

    public void setDeperdition(double deperdition) {
        this.deperdition = deperdition;
    }

    public TypeVitrageEnum getTypeVitrage() {
        return typeVitrage;
    }

    public void setTypeVitrage(TypeVitrageEnum typeVitrage) {
        this.typeVitrage = typeVitrage;
    }

    public TypeFermetureEnum getTypeFermeture() {
        return typeFermeture;
    }

    public void setTypeFermeture(TypeFermetureEnum typeFermeture) {
        this.typeFermeture = typeFermeture;
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
    }
    public void actualiseCoeffTransmissionThermiqueFenetre(){
        double u=5;
        switch(this.typeMateriau){
            case METALLIQUE:
                switch (this.typeFenetre){
                    case FENETRE_SIMPLE_VENTAIL_BATTANTE:
                    case FENETRE_DOUBLE_VENTAIL_BATTANTE:
                        switch (this.typeVitrage){
                            case SIMPLE_VITRAGE:
                                u=4.95;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case SURVITRAGE:
                                u=4;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_INF_1990:
                                u=3.7;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                                u=3.6;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_SUP_2001:
                                u=2.25;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case TRIPLE_VITRAGE:
                                u=1.88;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                        }
                        break;
                    case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                        switch (typeVitrage){
                            case SIMPLE_VITRAGE:
                                u=4.63;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case SURVITRAGE:
                                u=3.46;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_INF_1990:
                                u=3.46;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                                u=3.36;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_SUP_2001:
                                u=2.18;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case TRIPLE_VITRAGE:
                                u=1.65;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                        }
                        break;
                    case FENETRE_DE_TOIT:
                    case LUCARNE:
                        switch (typeVitrage){
                            case SIMPLE_VITRAGE:
                                u=4.95;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case SURVITRAGE:
                                u=4.38;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_INF_1990:
                                u=4.01;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                                u=3.92;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_SUP_2001:
                                u=3.30;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case TRIPLE_VITRAGE:
                                u=3.15;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
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
                                u=3.90;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case SURVITRAGE:
                                u=2.75;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_INF_1990:
                                u=2.45;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                                u=2.35;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_SUP_2001:
                                u=1.70;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case TRIPLE_VITRAGE:
                                u=1.24;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                        }
                        break;
                    case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                        switch (typeVitrage){
                            case SIMPLE_VITRAGE:
                                u=4.25;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case SURVITRAGE:
                                u=3.00;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_INF_1990:
                                u=2.62;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                                u=2.52;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_SUP_2001:
                                u=1.85;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case TRIPLE_VITRAGE:
                                u=1.39;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                        }
                        break;
                    case FENETRE_DE_TOIT:
                    case LUCARNE:
                        switch (typeVitrage){
                            case SIMPLE_VITRAGE:
                                u=3.90;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case SURVITRAGE:
                                u=2.92;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_INF_1990:
                                u=2.70;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                                u=2.70;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_SUP_2001:
                                u=2.01;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case TRIPLE_VITRAGE:
                                u=1.39;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
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
                                u=4.2;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case SURVITRAGE:
                                u=2.9;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_INF_1990:
                                u=2.7;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                                u=2.55;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_SUP_2001:
                                u=1.75;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case TRIPLE_VITRAGE:
                                u=1.24;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                        }
                        break;
                    case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                        switch (typeVitrage){
                            case SIMPLE_VITRAGE:
                                u=4.2;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case SURVITRAGE:
                                u=2.9;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_INF_1990:
                                u=2.7;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                                u=2.55;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_SUP_2001:
                                u=1.75;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case TRIPLE_VITRAGE:
                                u=1.24;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                        }
                        break;
                    case FENETRE_DE_TOIT:
                    case LUCARNE:
                        switch (typeVitrage){
                            case SIMPLE_VITRAGE:
                                u=4.2;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case SURVITRAGE:
                                u=3.15;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_INF_1990:
                                u=2.96;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                                u=2.9;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case DOUBLE_VITRAGE_SUP_2001:
                                u=2.04;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                            case TRIPLE_VITRAGE:
                                u=1.46;
                                u=actualiseCoeffTransmissionThermiqueAvecFermeture(u);
                                break;
                        }
                        break;
                }
                break;
        }
        this.actualiseDeperdition();
    }
    public double actualiseCoeffTransmissionThermiqueAvecFermeture(double uDevantEtreActualise){
        double nouveauU=uDevantEtreActualise;
        switch(typeFermeture){
            case JALOUSIE_ACCORDEON:
            case VOLET_BATTANT_AVEC_AJOURES_FIXES:
                if (uDevantEtreActualise<1.3){nouveauU=1.1;}
                else if (uDevantEtreActualise<1.4){nouveauU=1.2;}
                else if (uDevantEtreActualise<1.5){nouveauU=1.3;}
                else if (uDevantEtreActualise<1.6){nouveauU=1.4;}
                else if (uDevantEtreActualise<1.7){nouveauU=1.5;}
                else if (uDevantEtreActualise<1.8){nouveauU=1.6;}
                else if (uDevantEtreActualise<1.9){nouveauU=1.7;}
                else if (uDevantEtreActualise<2.0){nouveauU=1.8;}
                else if (uDevantEtreActualise<2.1){nouveauU=1.9;}
                else if (uDevantEtreActualise<2.2){nouveauU=1.9;}
                else if (uDevantEtreActualise<2.3){nouveauU=2.0;}
                else if (uDevantEtreActualise<2.4){nouveauU=2.1;}
                else if (uDevantEtreActualise<2.5){nouveauU=2.2;}
                else if (uDevantEtreActualise<2.6){nouveauU=2.3;}
                else if (uDevantEtreActualise<2.7){nouveauU=2.4;}
                else if (uDevantEtreActualise<2.8){nouveauU=2.5;}
                else if (uDevantEtreActualise<2.9){nouveauU=2.5;}
                else if (uDevantEtreActualise<3.0){nouveauU=2.6;}
                else if (uDevantEtreActualise<3.2){nouveauU=2.7;}
                else if (uDevantEtreActualise<3.4){nouveauU=2.9;}
                else if (uDevantEtreActualise<3.6){nouveauU=3.0;}
                else if (uDevantEtreActualise<3.8){nouveauU=3.2;}
                else if (uDevantEtreActualise<4.0){nouveauU=3.4;}
                else if (uDevantEtreActualise<4.2){nouveauU=3.5;}
                else if (uDevantEtreActualise<4.4){nouveauU=3.7;}
                else if (uDevantEtreActualise<4.6){nouveauU=3.8;}
                else if (uDevantEtreActualise<4.8){nouveauU=4.0;}
                else if (uDevantEtreActualise<5.0){nouveauU=4.1;}
                else if (uDevantEtreActualise<5.2){nouveauU=4.3;}
                else if (uDevantEtreActualise<5.4){nouveauU=4.4;}
                else if (uDevantEtreActualise<5.6){nouveauU=4.6;}
                else if (uDevantEtreActualise<5.8){nouveauU=4.7;}
                else if (uDevantEtreActualise<6.0){nouveauU=4.9;}
                else if (uDevantEtreActualise<6.2){nouveauU=5.0;}
                else if (uDevantEtreActualise>=6.2){nouveauU=5.2;}
                break;
            case VOLET_ROULANT_EN_METAL:
            case FERMETURE_SANS_AJOURES:
                if (uDevantEtreActualise<1.3){nouveauU=1.1;}
                else if (uDevantEtreActualise<1.4){nouveauU=1.2;}
                else if (uDevantEtreActualise<1.5){nouveauU=1.3;}
                else if (uDevantEtreActualise<1.6){nouveauU=1.4;}
                else if (uDevantEtreActualise<1.7){nouveauU=1.5;}
                else if (uDevantEtreActualise<1.8){nouveauU=1.5;}
                else if (uDevantEtreActualise<1.9){nouveauU=1.6;}
                else if (uDevantEtreActualise<2.0){nouveauU=1.7;}
                else if (uDevantEtreActualise<2.1){nouveauU=1.8;}
                else if (uDevantEtreActualise<2.2){nouveauU=1.9;}
                else if (uDevantEtreActualise<2.3){nouveauU=1.9;}
                else if (uDevantEtreActualise<2.4){nouveauU=2.0;}
                else if (uDevantEtreActualise<2.5){nouveauU=2.1;}
                else if (uDevantEtreActualise<2.6){nouveauU=2.2;}
                else if (uDevantEtreActualise<2.7){nouveauU=2.3;}
                else if (uDevantEtreActualise<2.8){nouveauU=2.3;}
                else if (uDevantEtreActualise<2.9){nouveauU=2.4;}
                else if (uDevantEtreActualise<3.0){nouveauU=2.5;}
                else if (uDevantEtreActualise<3.2){nouveauU=2.6;}
                else if (uDevantEtreActualise<3.4){nouveauU=2.7;}
                else if (uDevantEtreActualise<3.6){nouveauU=2.9;}
                else if (uDevantEtreActualise<3.8){nouveauU=3.0;}
                else if (uDevantEtreActualise<4.0){nouveauU=3.1;}
                else if (uDevantEtreActualise<4.2){nouveauU=3.3;}
                else if (uDevantEtreActualise<4.4){nouveauU=3.4;}
                else if (uDevantEtreActualise<4.6){nouveauU=3.6;}
                else if (uDevantEtreActualise<4.8){nouveauU=3.7;}
                else if (uDevantEtreActualise<5.0){nouveauU=3.8;}
                else if (uDevantEtreActualise<5.2){nouveauU=4.0;}
                else if (uDevantEtreActualise<5.4){nouveauU=4.1;}
                else if (uDevantEtreActualise<5.6){nouveauU=4.2;}
                else if (uDevantEtreActualise<5.8){nouveauU=4.4;}
                else if (uDevantEtreActualise<6.0){nouveauU=4.5;}
                else if (uDevantEtreActualise<6.2){nouveauU=4.6;}
                else if (uDevantEtreActualise>=6.2){nouveauU=4.8;}
                break;
            case VOLET_BATTANT_BOIS:
            case VOLET_ROULANT_PVC:
                if (uDevantEtreActualise<1.3){nouveauU=1.1;}
                else if (uDevantEtreActualise<1.4){nouveauU=1.2;}
                else if (uDevantEtreActualise<1.5){nouveauU=1.3;}
                else if (uDevantEtreActualise<1.6){nouveauU=1.3;}
                else if (uDevantEtreActualise<1.7){nouveauU=1.4;}
                else if (uDevantEtreActualise<1.8){nouveauU=1.5;}
                else if (uDevantEtreActualise<1.9){nouveauU=1.6;}
                else if (uDevantEtreActualise<2.0){nouveauU=1.6;}
                else if (uDevantEtreActualise<2.1){nouveauU=1.7;}
                else if (uDevantEtreActualise<2.2){nouveauU=1.8;}
                else if (uDevantEtreActualise<2.3){nouveauU=1.9;}
                else if (uDevantEtreActualise<2.4){nouveauU=2.0;}
                else if (uDevantEtreActualise<2.5){nouveauU=2.0;}
                else if (uDevantEtreActualise<2.6){nouveauU=2.1;}
                else if (uDevantEtreActualise<2.7){nouveauU=2.2;}
                else if (uDevantEtreActualise<2.8){nouveauU=2.2;}
                else if (uDevantEtreActualise<2.9){nouveauU=2.3;}
                else if (uDevantEtreActualise<3.0){nouveauU=2.4;}
                else if (uDevantEtreActualise<3.2){nouveauU=2.5;}
                else if (uDevantEtreActualise<3.4){nouveauU=2.6;}
                else if (uDevantEtreActualise<3.6){nouveauU=2.7;}
                else if (uDevantEtreActualise<3.8){nouveauU=2.9;}
                else if (uDevantEtreActualise<4.0){nouveauU=3.0;}
                else if (uDevantEtreActualise<4.2){nouveauU=3.1;}
                else if (uDevantEtreActualise<4.4){nouveauU=3.3;}
                else if (uDevantEtreActualise<4.6){nouveauU=3.4;}
                else if (uDevantEtreActualise<4.8){nouveauU=3.5;}
                else if (uDevantEtreActualise<5.0){nouveauU=3.7;}
                else if (uDevantEtreActualise<5.2){nouveauU=3.8;}
                else if (uDevantEtreActualise<5.4){nouveauU=3.9;}
                else if (uDevantEtreActualise<5.6){nouveauU=4.0;}
                else if (uDevantEtreActualise<5.8){nouveauU=4.2;}
                else if (uDevantEtreActualise<6.0){nouveauU=4.3;}
                else if (uDevantEtreActualise<6.2){nouveauU=4.4;}
                else if (uDevantEtreActualise>=6.2){nouveauU=4.5;}
                break;
            case SANS_FEMETURE:
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
                    this.bas=0.22;
                    break;
                case EST:
                case OUEST:
                    this.bas=0.31;
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
                        this.c1=1.1;
                        break;
                    case OUEST:
                        this.c1=0.57;
                        break;
                    case EST:
                        this.c1=0.57;
                        break;
                    case NORD:
                        this.c1=0.2;
                        break;
                }
                break;
            case FENETRE_DE_TOIT:
                switch (this.getMur().getOrientationMur()){
                    case SUD:
                        this.c1=1.2;
                        break;
                    case OUEST:
                        this.c1=0.75;
                        break;
                    case EST:
                        this.c1=0.75;
                        break;
                    case NORD:
                        this.c1=0.32;
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
                        this.fts=0.52;
                        break;
                    case DOUBLE_VITRAGE_INF_1990:
                    case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                        this.fts=0.47;
                        break;
                    case DOUBLE_VITRAGE_SUP_2001:
                        this.fts=0.4;
                        break;
                    case TRIPLE_VITRAGE:
                        this.fts=0.41;
                        break;
                }
                break;
            case PVC:
                switch(this.typeVitrage){
                    case SIMPLE_VITRAGE:
                    case SURVITRAGE:
                        switch (this.typeFenetre){
                            case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                                this.fts=0.54;
                                break;
                            default:
                                this.fts=0.49;
                                break;
                        }
                        break;
                    case DOUBLE_VITRAGE_INF_1990:
                    case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                        switch (this.typeFenetre){
                            case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                                this.fts=0.48;
                                break;
                            default:
                                this.fts=0.44;
                                break;
                        }
                        break;
                    case DOUBLE_VITRAGE_SUP_2001:
                        switch (this.typeFenetre){
                            case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                                this.fts=0.41;
                                break;
                            default:
                                this.fts=0.38;
                                break;
                        }
                        break;
                    case TRIPLE_VITRAGE:
                        switch (this.typeFenetre){
                            case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                                this.fts=0.43;
                                break;
                            default:
                                this.fts=0.39;
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
                                this.fts=0.59;
                                break;
                            default:
                                this.fts=0.54;
                                break;
                        }
                        break;
                    case DOUBLE_VITRAGE_INF_1990:
                    case DOUBLE_VITRAGE_SUP_1990_INF_2001:
                        switch (this.typeFenetre){
                            case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                                this.fts=0.53;
                                break;
                            default:
                                this.fts=0.485;
                                break;
                        }
                        break;
                    case DOUBLE_VITRAGE_SUP_2001:
                        switch (this.typeFenetre){
                            case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                                this.fts=0.46;
                                break;
                            default:
                                this.fts=0.42;
                                break;
                        }
                        break;
                    case TRIPLE_VITRAGE:
                        switch (this.typeFenetre){
                            case FENETRE_DOUBLE_VENTAIL_COULISSANTE:
                                this.fts=0.47;
                                break;
                            default:
                                this.fts=0.43;
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
            this.fe1=0.55;
        }
        actualiseSse();
    }
    public void actualiseFe2(){
        if (this.masqueLointain.equals(TypeMasqueEnum.ABSENCE_MASQUE_LOINTAIN)){
            this.fe2=1;
        }else if(this.masqueLointain.equals(TypeMasqueEnum.PRESENCE_MASQUE_LOINTAIN_PARTIEL)){
            switch(this.getMur().getOrientationMur()){
                case SUD:
                    this.fe2=0.3;
                    break;
                case EST:
                case OUEST:
                    this.fe2=0.4;
                    break;
                case NORD:
                    this.fe2=1;
                    break;
            }
        }else if(this.masqueLointain.equals(TypeMasqueEnum.PRESENCE_MASQUE_LOINTAIN_TOTAL)){
            switch(this.getMur().getOrientationMur()){
                case SUD:
                    this.fe2=0.1;
                    break;
                case EST:
                case OUEST:
                    this.fe2=0.2;
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

    @Override
    protected void makeModel() {
        BoundingBox b = new BoundingBox();
        this.calculateBoundingBox(b);
        float w = this.getWidth() / b.getWidth();
        float h = this.getMur().getDepth() / b.getHeight();
        float d = this.getHeight() / b.getDepth();
        Vector3 dmin = b.getMin(new Vector3()).scl(-1);
        model_transform.idt().scale(w, h, d).translate(dmin);
    }
}
