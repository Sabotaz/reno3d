package fr.limsi.rorqual.core.dpe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.limsi.rorqual.core.dpe.enums.chauffageproperties.*;
import fr.limsi.rorqual.core.dpe.enums.generalproperties.*;
import fr.limsi.rorqual.core.dpe.enums.slabproperties.DateIsolationSlab;
import fr.limsi.rorqual.core.dpe.enums.slabproperties.MitoyennetePlafond;
import fr.limsi.rorqual.core.dpe.enums.slabproperties.MitoyennetePlancher;
import fr.limsi.rorqual.core.dpe.enums.slabproperties.TypeIsolationSlab;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.*;
import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.*;
import fr.limsi.rorqual.core.dpe.enums.ecsproperties.*;
import fr.limsi.rorqual.core.event.*;
import fr.limsi.rorqual.core.model.*;
import fr.limsi.rorqual.core.ui.Layout;
import fr.limsi.rorqual.core.ui.TabWindow;

@XStreamAlias("dpe")
public class Dpe implements EventListener {

    private HashMap<EventType,Object> general_properties = new HashMap<EventType,Object>();
    private HashMap<EventType,Object> chauffage_properties = new HashMap<EventType,Object>();

    /*** Attributs liés au calcul du DPE ***/

    // 0.Variables générales
    private float sh;
    private float sdepTot;
    private float sdepMurs;
    private float sdepToits;
    private float sdepFen;
    private float sdepPorte;
    private float sdepPorteFenetre;
    private float perimetreBatiment;
    private float scoreDpe = 700;
    private DateConstructionBatimentEnum dateConstructionBatiment = DateConstructionBatimentEnum.AVANT_1975; // Initialisation défavorable
    private TypeEnergieConstructionEnum typeEnergieConstruction = TypeEnergieConstructionEnum.AUTRE; // Initialisation défavorable
    private DepartementBatimentEnum departementBatiment = DepartementBatimentEnum.AIN; // Choix par défaut
    private TypeBatimentEnum typeBatiment = TypeBatimentEnum.MAISON;
    private CategorieLogementEnum categorieLogement = CategorieLogementEnum.T1_F1;
    private TypeAbonnementElectriqueEnum typeAbonnementElectrique = TypeAbonnementElectriqueEnum.AUTRE;

    public void actualiseSH(){
        float tampon=0;
        for (Slab s: ModelHolder.getInstance().getBatiment().getSlabs()){
            tampon+=s.getSurface();
        }
        this.sh=tampon;
        this.actualiseQ4pa();
        this.actualiseAi();
        this.actualiseG();
        this.actualisePr();
        this.actualiseCch();
        this.actualiseConsommationEclairage();
        this.actualiseConsommationCuisson();
        this.actualiseScoreDpe();
    }
    public void actualisePerimetreBatiment(){
        float tampon=0;
        for (Mur m: ModelHolder.getInstance().getBatiment().getMurs()){
            if (m.getEtage() != null && m.getEtage().getNumber()==0 && m.getTypeMur() != TypeMurEnum.MUR_INTERIEUR){
                tampon+=m.getWidth();
            }
        }
        perimetreBatiment=tampon;
        this.actualiseDpPlancherFuncPerimetre();
    }
    public void actualiseSdepMurs(){
        float tampon = 0;
        for (Mur m : ModelHolder.getInstance().getBatiment().getMurs()) {
            if(m.getDeperdition()!=0) {
                tampon += m.getSurface();
            }
        }
        sdepMurs = tampon;
        this.actualiseSdep();
    }
    public void actualiseSdepToits(){
        float tampon = 0;
        for (Slab s : ModelHolder.getInstance().getBatiment().getSlabs()) {
            if(s.getDeperditionPlafond() !=0) {
                tampon += s.getSurface();
            }
        }
        sdepToits = tampon;
        this.actualiseSdep();
    }
    public void actualiseSdepFen(){
        float tampon = 0;
        for (Fenetre f : fenetreList) {
            if(f.getDeperdition() !=0) {
                tampon += f.getSurface();
            }
        }
        sdepFen = tampon;
        this.actualiseSdep();
    }
    public void actualiseSdepPorte(){
        float tampon = 0;
        for (Porte p : porteList) {
            if(p.getDeperdition() !=0) {
                tampon += p.getSurface();
            }
        }
        sdepPorte = tampon;
        this.actualiseSdep();
    }
    public void actualiseSdepPorteFenetre(){
        float tampon = 0;
        for (PorteFenetre pf : porteFenetreList) {
            if(pf.getDeperdition() !=0) {
                tampon += pf.getSurface();
            }
        }
        sdepPorteFenetre = tampon;
        this.actualiseSdep();
    }
    public void actualiseSdep(){
        sdepTot = sdepMurs + sdepToits + sdepFen + sdepPorte + sdepPorteFenetre;
        this.actualiseQ4paEnv();
    }

    // 1.Expression du besoin de chauffage
    private float bv;
    private float gv;
    private float lastGv;
    public void actualiseBV(){
        bv=gv*(1-f);
        this.actualiseBch();
    }
    public void actualiseGV(){
        gv=dpMur+dpToit+dpPlancher+dpFenetre+dpPorte+dpPorteFenetre+renouvellementAir;
        if (gv != lastGv){
            this.actualiseBV();
            this.actualiseG();
            this.actualiseCch();
            this.actualiseX();
        }
        lastGv=gv;
    }

    // 2.Calcul des déperditions de l'enveloppe GV
    private float dpMur;
    private float dpToit;
    private float dpPlancher;
    private float dpPorte;
    private float dpFenetre;
    private float dpPorteFenetre;

    public void actualiseDpMur(){
        float tampon=0;
        for (Mur m : ModelHolder.getInstance().getBatiment().getMurs()) {
            tampon += m.getDeperdition();
        }
        dpMur=tampon;
        this.actualiseGV();
    }
    public void actualiseDpToit(){
        float tampon=0;
        for (Slab s : ModelHolder.getInstance().getBatiment().getSlabs()) {
            tampon += s.getDeperditionPlafond();
        }
        dpToit=tampon;
        this.actualiseGV();
    }
    public void actualiseDpPlancher(){
        float tampon=0;
        for (Slab s : ModelHolder.getInstance().getBatiment().getSlabs()) {
            tampon += s.getDeperditionPlancher();
        }
        dpPlancher=tampon;
        this.actualiseGV();
    }
    public void actualiseDpPlancherFuncPerimetre(){
        for (Slab s : ModelHolder.getInstance().getBatiment().getSlabs()) {
            this.actualiseCoeffDeperditionThermique(s,false,true);
        }
        this.actualiseDpPlancher();
    }
    public void actualiseDpPorte(){
        float tampon=0;
        for (Porte p : porteList) {
            tampon += p.getDeperdition();
        }
        dpPorte=tampon;
        this.actualiseSdepPorte();
        this.actualiseGV();
    }
    public void actualiseDpFenetre(){
        float tampon=0;
        for (Fenetre f : fenetreList) {
            tampon += f.getDeperdition();
        }
        dpFenetre=tampon;
        this.actualiseSdepFen();
        this.actualiseGV();
    }
    public void actualiseDpPorteFenetre(){
        float tampon=0;
        for (PorteFenetre pf : porteFenetreList) {
            tampon += pf.getDeperdition();
        }
        dpPorteFenetre=tampon;
        this.actualiseSdepPorteFenetre();
        this.actualiseGV();
    }
    ///*** Murs ***///
    public void actualiseCoeffDeperditionThermique(Mur mur){
        float u=0;
        switch (mur.getDateIsolationMurEnum()){
            case JAMAIS:
                u=2;
                break;
            case INCONNUE:
                u=this.getUmurFoncAnneeConstruction(mur);
                break;
            case A_LA_CONSTRUCTION:
                if(dateConstructionBatiment.equals(DateConstructionBatimentEnum.AVANT_1975)){
                    u=0.8f;
                    mur.setTypeIsolationMurEnum(TypeIsolationMurEnum.ITI);
                }else{
                    u=this.getUmurFoncAnneeConstruction(mur);
                }
                break;
            case EN_RENOVATION_AVANT_1983:
                u=0.82f;
                mur.setTypeIsolationMurEnum(TypeIsolationMurEnum.ITI);
                break;
            case EN_RENOVATION_ENTRE_1983_ET_1988:
                u=0.75f;
                mur.setTypeIsolationMurEnum(TypeIsolationMurEnum.ITI);
                break;
            case EN_RENOVATION_ENTRE_1989_ET_2000:
                u=0.48f;
                mur.setTypeIsolationMurEnum(TypeIsolationMurEnum.ITI);
                break;
            case EN_RENOVATION_ENTRE_2001_ET_2005:
                u=0.42f;
                mur.setTypeIsolationMurEnum(TypeIsolationMurEnum.ITI);
                break;
            case EN_RENOVATION_ENTRE_2006_ET_2012:
                u=0.36f;
                mur.setTypeIsolationMurEnum(TypeIsolationMurEnum.ITI);
                break;
            case EN_RENOVATION_APRES_2012:
                u=0.24f;
                mur.setTypeIsolationMurEnum(TypeIsolationMurEnum.ITI);
                break;
        }
        mur.setCoeffTransmissionThermique(u);
        mur.actualiseDeperdition();
    }
    public float getUmurFoncAnneeConstruction(Mur mur){
        float uMur=2.5f;
        switch (dateConstructionBatiment){
            case AVANT_1975:
                uMur=2;
                mur.setTypeIsolationMurEnum(TypeIsolationMurEnum.NON_ISOLE);
                break;
            case ENTRE_1975_ET_1977:
                uMur=1;
                break;
            case ENTRE_1978_ET_1982:
                if (typeEnergieConstruction.equals(TypeEnergieConstructionEnum.ELECTRIQUE)){
                    uMur=0.8f;
                }else if (typeEnergieConstruction.equals(TypeEnergieConstructionEnum.AUTRE)){
                    uMur=1;
                }
                mur.setTypeIsolationMurEnum(TypeIsolationMurEnum.ITI);
                break;
            case ENTRE_1983_ET_1988:
                if (typeEnergieConstruction.equals(TypeEnergieConstructionEnum.ELECTRIQUE)){
                    uMur=0.7f;
                }else if (typeEnergieConstruction.equals(TypeEnergieConstructionEnum.AUTRE)){
                    uMur=0.8f;
                }
                mur.setTypeIsolationMurEnum(TypeIsolationMurEnum.ITI);
                break;
            case ENTRE_1989_ET_2000:
                if (typeEnergieConstruction.equals(TypeEnergieConstructionEnum.ELECTRIQUE)){
                    uMur=0.45f;
                }else if (typeEnergieConstruction.equals(TypeEnergieConstructionEnum.AUTRE)){
                    uMur=0.5f;
                }
                mur.setTypeIsolationMurEnum(TypeIsolationMurEnum.ITI);
                break;
            case ENTRE_2001_ET_2005:
                uMur=0.4f;
                mur.setTypeIsolationMurEnum(TypeIsolationMurEnum.ITI);
                break;
            case ENTRE_2006_ET_2012:
                uMur=0.35f;
                mur.setTypeIsolationMurEnum(TypeIsolationMurEnum.ITE);
                break;
            case APRES_2012:
                uMur=0.2f;
                mur.setTypeIsolationMurEnum(TypeIsolationMurEnum.ITE);
                break;
        }
        return uMur;
    }
    ///*** Slabs ***///
    public void actualiseCoeffDeperditionThermique(Slab slab,boolean actualisePlafond,boolean actualisePlancher){
        if(actualisePlancher){
            float uPlancher=2.5f;
            if (!slab.getMitoyennetePlancher().equals(MitoyennetePlancher.TERRE_PLEIN)){
                switch (slab.getDateIsolationPlancher()){
                    case JAMAIS:
                        uPlancher=2;
                        break;
                    case INCONNUE:
                        uPlancher=this.getUplancherFoncAnneeConstruction(slab);
                        break;
                    case A_LA_CONSTRUCTION:
                        if(dateConstructionBatiment.equals(DateConstructionBatimentEnum.AVANT_1975)){
                            uPlancher=0.8f;
                        }else{
                            uPlancher=this.getUplancherFoncAnneeConstruction(slab);
                        }
                        break;
                    case EN_RENOVATION_AVANT_1983:
                        uPlancher=0.85f;
                        slab.setTypeIsolationPlancher(TypeIsolationSlab.ITE);
                        break;
                    case EN_RENOVATION_ENTRE_1983_ET_1988:
                        uPlancher=0.6f;
                        slab.setTypeIsolationPlancher(TypeIsolationSlab.ITE);
                        break;
                    case EN_RENOVATION_ENTRE_1989_ET_2000:
                        uPlancher=0.55f;
                        slab.setTypeIsolationPlancher(TypeIsolationSlab.ITE);
                        break;
                    case EN_RENOVATION_ENTRE_2001_ET_2005:
                        uPlancher=0.3f;
                        slab.setTypeIsolationPlancher(TypeIsolationSlab.ITE);
                        break;
                    case EN_RENOVATION_ENTRE_2006_ET_2012:
                        uPlancher=0.27f;
                        slab.setTypeIsolationPlancher(TypeIsolationSlab.ITE);
                        break;
                    case EN_RENOVATION_APRES_2012:
                        uPlancher=0.24f;
                        slab.setTypeIsolationPlancher(TypeIsolationSlab.ITE);
                        break;
                }
            }else{
                int tampon = (int)Math.round(2 * slab.getSurface() / perimetreBatiment);
                if (tampon<3){
                    uPlancher=0.25f;
                }else if (tampon>20){
                    uPlancher=0.1f;
                }else{
                    switch (tampon){
                        case 3:
                            uPlancher=0.25f;
                            break;
                        case 4:
                            uPlancher=0.23f;
                            break;
                        case 5:
                            uPlancher=0.21f;
                            break;
                        case 6:
                            uPlancher=0.19f;
                            break;
                        case 7:
                            uPlancher=0.18f;
                            break;
                        case 8:
                            uPlancher=0.17f;
                            break;
                        case 9:
                            uPlancher=0.16f;
                            break;
                        case 10:
                            uPlancher=0.15f;
                            break;
                        case 11:
                            uPlancher=0.15f;
                            break;
                        case 12:
                            uPlancher=0.14f;
                            break;
                        case 13:
                            uPlancher=0.13f;
                            break;
                        case 14:
                            uPlancher=0.12f;
                            break;
                        case 15:
                            uPlancher=0.12f;
                            break;
                        case 16:
                            uPlancher=0.11f;
                            break;
                        case 17:
                            uPlancher=0.11f;
                            break;
                        case 18:
                            uPlancher=0.11f;
                            break;
                        case 19:
                            uPlancher=0.1f;
                            break;
                        case 20:
                            uPlancher=0.1f;
                            break;
                    }
                }
            }
            slab.setuPlancher(uPlancher);
            slab.actualiseDeperditionPlancher();
        }
        if(actualisePlafond){
            float uPlafond=2.5f;
            switch (slab.getDateIsolationPlafond()){
                case JAMAIS:
                    uPlafond=2;
                    break;
                case INCONNUE:
                    uPlafond=this.getUplafondFoncAnneeConstruction(slab);
                    break;
                case A_LA_CONSTRUCTION:
                    if(dateConstructionBatiment.equals(DateConstructionBatimentEnum.AVANT_1975)){
                        uPlafond=0.5f;
                    }else{
                        uPlafond=this.getUplafondFoncAnneeConstruction(slab);
                    }
                    break;
                case EN_RENOVATION_AVANT_1983:
                    switch (slab.getMitoyennetePlafond()){
                        case COMBLE_PERDU:
                            uPlafond=0.4f;
                            break;
                        case COMBLE_AMMENAGEE:
                            uPlafond=0.61f;
                            break;
                        case TERRASSE:
                            uPlafond=0.7f;
                            break;
                        case AUTRE_HABITATION:
                            uPlafond=0;
                            break;
                    }
                    break;
                case EN_RENOVATION_ENTRE_1983_ET_1988:
                    switch (slab.getMitoyennetePlafond()){
                        case COMBLE_PERDU:
                            uPlafond=0.3f;
                            break;
                        case COMBLE_AMMENAGEE:
                            uPlafond=0.4f;
                            break;
                        case TERRASSE:
                            uPlafond=0.55f;
                            break;
                        case AUTRE_HABITATION:
                            uPlafond=0;
                            break;
                    }
                    break;
                case EN_RENOVATION_ENTRE_1989_ET_2000:
                    switch (slab.getMitoyennetePlafond()){
                        case COMBLE_PERDU:
                            uPlafond=0.25f;
                            break;
                        case COMBLE_AMMENAGEE:
                            uPlafond=0.3f;
                            break;
                        case TERRASSE:
                            uPlafond=0.4f;
                            break;
                        case AUTRE_HABITATION:
                            uPlafond=0;
                            break;
                    }
                    break;
                case EN_RENOVATION_ENTRE_2001_ET_2005:
                    switch (slab.getMitoyennetePlafond()){
                        case COMBLE_PERDU:
                            uPlafond=0.23f;
                            break;
                        case COMBLE_AMMENAGEE:
                            uPlafond=0.25f;
                            break;
                        case TERRASSE:
                            uPlafond=0.3f;
                            break;
                        case AUTRE_HABITATION:
                            uPlafond=0;
                            break;
                    }
                    break;
                case EN_RENOVATION_ENTRE_2006_ET_2012:
                    switch (slab.getMitoyennetePlafond()){
                        case COMBLE_PERDU:
                            uPlafond=0.2f;
                            break;
                        case COMBLE_AMMENAGEE:
                            uPlafond=0.2f;
                            break;
                        case TERRASSE:
                            uPlafond=0.27f;
                            break;
                        case AUTRE_HABITATION:
                            uPlafond=0;
                            break;
                    }
                    break;
                case EN_RENOVATION_APRES_2012:
                    switch (slab.getMitoyennetePlafond()){
                        case COMBLE_PERDU:
                            uPlafond=0.12f;
                            break;
                        case COMBLE_AMMENAGEE:
                            uPlafond=0.18f;
                            break;
                        case TERRASSE:
                            uPlafond=0.15f;
                            break;
                        case AUTRE_HABITATION:
                            uPlafond=0;
                            break;
                    }
                    break;
            }
            slab.setuPlafond(uPlafond);
            slab.actualiseDeperditionPlafond();
        }
    }
    public float getUplafondFoncAnneeConstruction(Slab slab){
        float uPlafond=2.5f;
        boolean isComble;
        if (slab.getMitoyennetePlafond().equals(MitoyennetePlafond.TERRASSE)){
            isComble=false;
        }else{
            isComble=true;
        }
        switch (dateConstructionBatiment){
            case AVANT_1975:
                uPlafond=2;
                break;
            case ENTRE_1975_ET_1977:
                if(isComble){
                    uPlafond=0.5f;
                }else{
                    uPlafond=0.75f;
                }
                break;
            case ENTRE_1978_ET_1982:
                if(isComble){
                    if (typeEnergieConstruction.equals(TypeEnergieConstructionEnum.ELECTRIQUE)){
                        uPlafond=0.4f;
                    }else if (typeEnergieConstruction.equals(TypeEnergieConstructionEnum.AUTRE)){
                        uPlafond=0.5f;
                    }
                }else{
                    if (typeEnergieConstruction.equals(TypeEnergieConstructionEnum.ELECTRIQUE)){
                        uPlafond=0.7f;
                    }else if (typeEnergieConstruction.equals(TypeEnergieConstructionEnum.AUTRE)){
                        uPlafond=0.75f;
                    }
                }
                break;
            case ENTRE_1983_ET_1988:
                if(isComble){
                    uPlafond=0.3f;
                }else{
                    if (typeEnergieConstruction.equals(TypeEnergieConstructionEnum.ELECTRIQUE)){
                        uPlafond=0.4f;
                    }else if (typeEnergieConstruction.equals(TypeEnergieConstructionEnum.AUTRE)){
                        uPlafond=0.55f;
                    }
                }
                break;
            case ENTRE_1989_ET_2000:
                if(isComble){
                    uPlafond=0.25f;
                }else{
                    if (typeEnergieConstruction.equals(TypeEnergieConstructionEnum.ELECTRIQUE)){
                        uPlafond=0.35f;
                    }else if (typeEnergieConstruction.equals(TypeEnergieConstructionEnum.AUTRE)){
                        uPlafond=0.4f;
                    }
                }
                break;
            case ENTRE_2001_ET_2005:
                if(isComble){
                    uPlafond=0.23f;
                }else{
                    uPlafond=0.3f;
                }
                break;
            case ENTRE_2006_ET_2012:
                if(isComble){
                    uPlafond=0.2f;
                }else{
                    uPlafond=0.27f;
                }
                break;
            case APRES_2012:
                uPlafond=0.12f;
                break;
        }
        return uPlafond;
    }
    public float getUplancherFoncAnneeConstruction(Slab slab){
        float uPlancher=2.5f;
        switch (dateConstructionBatiment){
            case AVANT_1975:
                uPlancher=2;
                slab.setTypeIsolationPlancher(TypeIsolationSlab.NON_ISOLE);
                break;
            case ENTRE_1975_ET_1977:
                uPlancher=0.9f;
                break;
            case ENTRE_1978_ET_1982:
                if(typeEnergieConstruction.equals(TypeEnergieConstructionEnum.ELECTRIQUE)){
                    uPlancher=0.8f;
                }else{
                    uPlancher=0.9f;
                }
                slab.setTypeIsolationPlancher(TypeIsolationSlab.ITI);
                break;
            case ENTRE_1983_ET_1988:
                if(typeEnergieConstruction.equals(TypeEnergieConstructionEnum.ELECTRIQUE)){
                    uPlancher=0.55f;
                }else{
                    uPlancher=0.7f;
                }
                slab.setTypeIsolationPlancher(TypeIsolationSlab.ITI);
                break;
            case ENTRE_1989_ET_2000:
                if(typeEnergieConstruction.equals(TypeEnergieConstructionEnum.ELECTRIQUE)){
                    uPlancher=0.55f;
                }else{
                    uPlancher=0.6f;
                }
                slab.setTypeIsolationPlancher(TypeIsolationSlab.ITI);
                break;
            case ENTRE_2001_ET_2005:
                uPlancher=0.3f;
                slab.setTypeIsolationPlancher(TypeIsolationSlab.ITI);
                break;
            case ENTRE_2006_ET_2012:
                uPlancher=0.27f;
                slab.setTypeIsolationPlancher(TypeIsolationSlab.ITI);
                break;
            case APRES_2012:
                uPlancher=0.22f;
                slab.setTypeIsolationPlancher(TypeIsolationSlab.ITI);
                break;
        }
        return uPlancher;
    }
    public void gestionSuperpositionCreationSlab(Slab slabDuDessous, Slab slabDuDessus){
        slabDuDessous.setMitoyennetePlafond(MitoyennetePlafond.AUTRE_ETAGE_DU_LOGEMENT);
        slabDuDessus.setMitoyennetePlancher(MitoyennetePlancher.AUTRE_ETAGE_DU_LOGEMENT);
        actualiseCoeffDeperditionThermique(slabDuDessous, true, false);
        actualiseCoeffDeperditionThermique(slabDuDessus, false, true);
    }
    public void analyseSuperpositionSlab(Slab slabNouveau){
        int numberEtage = slabNouveau.getEtage().getNumber();
        Intersector.MinimumTranslationVector translationVector = new Intersector.MinimumTranslationVector();
        try{
            ArrayList<Slab> slabsEtageInf = ModelHolder.getInstance().getBatiment().getEtage(numberEtage-1).getSlabs();
            for (Slab slabEtageInf : slabsEtageInf){
                if (Intersector.overlapConvexPolygons(slabNouveau.getPolygon(),slabEtageInf.getPolygon(),translationVector)
                        && translationVector.depth > 0){
                    gestionSuperpositionCreationSlab(slabEtageInf,slabNouveau);
                }
            }
        }catch (ArrayIndexOutOfBoundsException e){}
        try {
            ModelHolder.getInstance().getBatiment().getEtage(numberEtage+1);
            ArrayList<Slab> slabsEtageSup = ModelHolder.getInstance().getBatiment().getEtage(numberEtage+1).getSlabs();
            for (Slab slabEtageSup : slabsEtageSup){
                if (Intersector.overlapConvexPolygons(slabNouveau.getPolygon(),slabEtageSup.getPolygon(),translationVector)
                        && translationVector.depth > 0){
                    gestionSuperpositionCreationSlab(slabNouveau, slabEtageSup);
                }
            }
        }catch(IndexOutOfBoundsException e){}
    }
    ///*** Fenêtres ***///
    @XStreamOmitField
    private List<Fenetre> fenetreList=new ArrayList<Fenetre>();
    public void actualiseNbFenetreSvEtDv(){
        nbFenetreSV=0;
        nbFenetreDV=0;
        for (Fenetre f : fenetreList){
            switch (f.getTypeVitrage()){
                case SIMPLE_VITRAGE:
                case SURVITRAGE :
                case INCONNUE :
                    nbFenetreSV ++;
                    break;
                case DOUBLE_VITRAGE_INF_1990 :
                case DOUBLE_VITRAGE_SUP_1990_INF_2001 :
                case DOUBLE_VITRAGE_SUP_2001 :
                case TRIPLE_VITRAGE :
                    nbFenetreDV ++;
                    break;
            }
        }
        this.actualiseQ4paConv();
    }
    ///*** Portes-fenêtres ***///
    @XStreamOmitField
    private List<PorteFenetre> porteFenetreList=new ArrayList<PorteFenetre>();
    ///*** Portes ***///
    @XStreamOmitField
    private List<Porte> porteList=new ArrayList<Porte>();

    public void setFenetreList(List<Fenetre> fenetres) {
        fenetreList = fenetres;
    }

    public void setPorteList(List<Porte> portes) {
        porteList = portes;
    }

    public void setPorteFenetreList(List<PorteFenetre> portefenetres) {
        porteFenetreList = portefenetres;
    }

    // 2.4. Calcul des ponts thermiques TODO : finir ça !

    // 2.5. Calcul des déperditions par renouvellement d'air
    private float renouvellementAir, hVent=0.7293f,hPerm,qVarep=2.145f,qVinf,q4pa,q4paEnv,q4paConv=2,smea=4,nbFenetreSV,nbFenetreDV;
    private TypeVentilationEnum typeVentilation=TypeVentilationEnum.INCONNUE; // Initialisation logique
    private TemperatureInterieurEnum tInt=TemperatureInterieurEnum.ENTRE_22_ET_23; // Initialisation défavorable
    public void actualiseRenouvellementAir(){
        renouvellementAir=hVent+hPerm;
        this.actualiseGV();
    }
    public void actualiseHvent(){
        hVent=0.34f*qVarep;
        this.actualiseRenouvellementAir();
    }
    public void actualiseHperm(){
        hPerm=0.34f*qVinf;
        this.actualiseRenouvellementAir();
    }
    public void actualiseqVinf(){
        float val = 0.7f*(tInt.getTemperatureInterieure()-6.58f);
        qVinf=0.0146f*q4pa*(float)Math.pow(val,0.667);
        this.actualiseHperm();
    }
    public void actualiseQ4pa(){
        q4pa=q4paEnv+0.45f*smea*sh;
        this.actualiseqVinf();
    }
    public void actualiseQ4paEnv(){
        q4paEnv=q4paConv*sdepTot;
        this.actualiseQ4pa();
    }
    public void actualiseQ4paConv(){
        if(nbFenetreSV>=nbFenetreDV){
            q4paConv=2.0f;
        }else{
            q4paConv=1.7f;
        }
        this.actualiseQ4paEnv();
    }
    public void actualiseSmeaAndQvarep(){
        switch (typeVentilation){
            case INCONNUE:
                switch(dateConstructionBatiment){
                    case AVANT_1975:
                        smea=4;
                        qVarep=2.145f;
                        break;
                    case ENTRE_1975_ET_1977:
                    case ENTRE_1978_ET_1982:
                        smea=2;
                        qVarep=1.8975f;
                        break;
                    case ENTRE_1983_ET_1988:
                    case ENTRE_1989_ET_2000:
                    case ENTRE_2001_ET_2005:
                        smea=2;
                        qVarep=1.65f;
                        break;
                    case ENTRE_2006_ET_2012:
                    case APRES_2012:
                        smea=1.5f;
                        qVarep=1.0725f;
                        break;
                }
                break;
            case NATURELLE:
                smea=4;
                qVarep=2.145f;
                break;
            case VMC_AUTO_REGLABLE_AVANT_1982:
                smea=2;
                qVarep=1.8975f;
                break;
            case VMC_AUTO_REGLABLE_APRES_1982:
                smea=2;
                qVarep=1.65f;
                break;
            case VMC_HYGRO:
                smea=1.5f;
                qVarep=1.0725f;
                break;
            case VMC_DOUBLE_FLUX:
                smea=0;
                qVarep=1.65f;
                break;
        }
        this.actualiseQ4pa();
        this.actualiseHvent();
    }

    // 2.6.Calcul de f : on cherche à minimiser x donc à minimiser aS et aI et à maximiser GV et DHcor
    private float f;
    private float x;
    private float dhCor=64800;
    private float aI=0;
    private float aS=0;
    private float sse=0; // Il n'y a aucune baie, la sse est nulle initialement
    public void actualiseF(){
        float a=x-(float)Math.pow(x,3.6);
        float b=1-(float)Math.pow(x,3.6);
        f=a/b;
        this.actualiseBV();
    }
    public void actualiseX(){
        x=(aI+aS)/(gv*dhCor);
        this.actualiseRrp();
        this.actualiseF();
    }
    public void actualisedhCor(){
        dhCor=departementBatiment.getDhref()+getKdh()*departementBatiment.getNref();
        this.actualiseBch();
        this.actualiseX();
    }
    public void actualiseAi(){
        aI=4.17f*sh*departementBatiment.getNref();
        this.actualiseX();
    }
    public int getKdh(){
        switch(tInt){
            case ENTRE_16_ET_17:
                return -1;
            case ENTRE_18_ET_19:
                return 0;
            case ENTRE_20_ET_21:
                return 1;
            case ENTRE_22_ET_23:
                return 2;
        }
        return -555;
    }
    public void actualiseAs(){
        aS=1000*departementBatiment.getE()*sse;
        this.actualiseX();
    }

    // 2.7.Détermination de la surface sud équivalente
    public void actualiseSse(){
        float tampon = 0;
        for (Fenetre f:fenetreList){
            if (f.getMur().getTypeMur().equals(TypeMurEnum.MUR_DONNANT_SUR_EXTERIEUR))
                tampon += f.getSurfaceSudEquivalente();
        }
        for (PorteFenetre pf:porteFenetreList){
            if (pf.getMur().getTypeMur().equals(TypeMurEnum.MUR_DONNANT_SUR_EXTERIEUR))
                tampon += pf.getSurfaceSudEquivalente();
        }
        this.sse=tampon;
        this.actualiseAs();
    }

    // 3.Traitement de l'intermittence
    private float intermittence = 1;
    private float i0 = 0.93f;
    private float g;
    public void actualiseIntermittence(){
        intermittence=i0/(1+0.1f*(g-1));
        this.actualiseBch();
    }
    public void actualiseG(){
        g=gv/(2.5f*sh);
        this.actualiseIntermittence();
    }
    public void actualiseI0(){
        Chauffage chauffagePrincipal = new Chauffage();
        switch (installationChauffage){
            case CHAUFFAGE_UNIQUE :
                chauffagePrincipal= chauffageUnique;
                break;
            case CHAUFFAGE_AVEC_POIL_OU_INSERT_BOIS:
                chauffagePrincipal= chauffageAvecPoil;
                break;
            case CHAUDIERE_GAZ_OU_FIOUL_AVEC_CHAUDIERE_BOIS:
                chauffagePrincipal= chaudiereGaz;
                break;
            case CHAUDIERE_AVEC_PAC:
                chauffagePrincipal= chaudiereAvecPac;
                break;
            case CHAUDIERE_AVEC_PAC_ET_INSERT_BOIS:
                chauffagePrincipal= chaudiereAvecPacEtPoil;
                break;
        }
        if (typeBatiment.equals(TypeBatimentEnum.MAISON)){ // Maison
            if(chauffagePrincipal.getType().equals(Chauffage.Type.DIVISE)){ // le chauffage est de type divisé
                if (programmationSysteme.equals(ProgrammationSystemeEnum.POSSIBLE)){ // Systeme programmable
                    i0=0.84f;
                }else{ // Systeme non programmable
                    i0 = 0.86f;
                }
            }else{ // le chauffage est de type central
                if (presenceThermostat.equals(PresenceThermostatEnum.AUCUN_DES_DEUX)){ // Sans régulation par pièce
                    if(programmationSysteme.equals(ProgrammationSystemeEnum.POSSIBLE)) { // Systeme programmable
                        i0 = 0.9f;
                    }else{ // Systeme non programmable
                        if (typeEmetteurDeChaleur.equals(Chauffage.Emission.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                            i0 = 0.91f;
                        }else if (typeEmetteurDeChaleur.equals(Chauffage.Emission.RADIATEUR)){ // Radiateur
                            i0 = 0.93f;
                        }else{ // Plancher chauffant ou système mixte
                            i0 = 0.94f;
                        }
                    }
                }else{ // Avec régulation pièce par pièce
                    if(programmationSysteme.equals(ProgrammationSystemeEnum.POSSIBLE)) { // Systeme programmable
                        i0 = 0.87f;
                    }else{ // Systeme non programmable
                        if (typeEmetteurDeChaleur.equals(Chauffage.Emission.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                            i0 = 0.88f;
                        }else if (typeEmetteurDeChaleur.equals(Chauffage.Emission.RADIATEUR)){ // Radiateur
                            i0 = 0.9f;
                        }else{ // Plancher chauffant ou système mixte
                            i0 = 0.92f;
                        }
                    }
                }
            }
        }else{ //Appartement
            if(chauffagePrincipal.getType().equals(Chauffage.Type.DIVISE)){ // le chauffage est de type divisé
                if (programmationSysteme.equals(ProgrammationSystemeEnum.POSSIBLE)){ // Systeme programmable
                    if (typeEmetteurDeChaleur.equals(Chauffage.Emission.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                        i0 = 0.88f;
                    }else if (typeEmetteurDeChaleur.equals(Chauffage.Emission.RADIATEUR)){ // Radiateur
                        i0 = 0.88f;
                    }else{ // Plancher chauffant ou système mixte
                        i0 = 0.93f;
                    }
                }else{ // Systeme non programmable
                    if (typeEmetteurDeChaleur.equals(Chauffage.Emission.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                        i0 = 0.9f;
                    }else if (typeEmetteurDeChaleur.equals(Chauffage.Emission.RADIATEUR)){ // Radiateur
                        i0 = 0.9f;
                    }else{ // Plancher chauffant ou système mixte
                        i0 = 0.95f;
                    }
                }
            }else{ // le chauffage est de type central
                if (presenceThermostat.equals(PresenceThermostatEnum.AUCUN_DES_DEUX)){ // Sans régulation par pièce
                    if(programmationSysteme.equals(ProgrammationSystemeEnum.POSSIBLE)) { // Systeme programmable
                        if (typeEmetteurDeChaleur.equals(Chauffage.Emission.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                            i0 = 0.93f;
                        }else if (typeEmetteurDeChaleur.equals(Chauffage.Emission.RADIATEUR)){ // Radiateur
                            i0 = 0.94f;
                        }else{ // Plancher chauffant ou système mixte
                            i0 = 0.95f;
                        }
                    }else{ // Systeme non programmable
                        if (typeEmetteurDeChaleur.equals(Chauffage.Emission.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                            i0 = 0.91f;
                        }else if (typeEmetteurDeChaleur.equals(Chauffage.Emission.RADIATEUR)){ // Radiateur
                            i0 = 0.93f;
                        }else{ // Plancher chauffant ou système mixte
                            i0 = 0.95f;
                        }
                    }
                }else{ // Avec régulation pièce par pièce
                    if(programmationSysteme.equals(ProgrammationSystemeEnum.POSSIBLE)) { // Systeme programmable
                        if (typeEmetteurDeChaleur.equals(Chauffage.Emission.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                            i0 = 0.89f;
                        }else if (typeEmetteurDeChaleur.equals(Chauffage.Emission.RADIATEUR)){ // Radiateur
                            i0 = 0.91f;
                        }else{ // Plancher chauffant ou système mixte
                            i0 = 0.93f;
                        }
                    }else{ // Systeme non programmable
                        if (typeEmetteurDeChaleur.equals(Chauffage.Emission.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                            i0 = 0.95f;
                        }else if (typeEmetteurDeChaleur.equals(Chauffage.Emission.RADIATEUR)){ // Radiateur
                            i0 = 0.96f;
                        }else{ // Plancher chauffant ou système mixte
                            i0 = 0.97f;
                        }
                    }
                }
            }
        }
        this.actualiseIntermittence();
    }

    // 4.Calcul du besoin et des consommations
    private InstallationChauffageEnum installationChauffage = InstallationChauffageEnum.CHAUFFAGE_UNIQUE;
    private FrequenceUtilisationPoilEnum frequenceUtilisationPoeleAvecChauffage = FrequenceUtilisationPoilEnum.TRES_PEU;
    private FrequenceUtilisationPoilEnum frequenceUtilisationPoeleAvecChauffageEtPac = FrequenceUtilisationPoilEnum.TRES_PEU;
    private PresenceInstallationSolaireEnum presenceInstallationSolaire = PresenceInstallationSolaireEnum.INSTALLATION_SOLAIRE_ABSENTE;
    private float cch;
    private float bch;
    private float pr=0;
    private float prs1=3.6f;
    private float prs2=3.7f;
    private float rrp;
    private float fCh=1;
    public void actualiseBch(){
        bch=((bv*dhCor/1000)-pr*rrp)*intermittence;
        this.actualiseCch();
    }
    public void actualisePr(){
        pr = sh*(prs1+prs2);
        this.actualiseBch();
    }
    public void actualiseRrp(){
        rrp=(1-3.6f*(float)Math.pow(x,2.6f)+2.6f*(float)Math.pow(x,3.6))/(float)Math.pow((1-(float)Math.pow(x,3.6)),2);
        this.actualiseBch();
    }
    public void actualisePrs1(){
        switch (installationChauffage){
            case CHAUFFAGE_UNIQUE:
                prs1=chauffageUnique.getPrs1();
                break;
            case CHAUFFAGE_AVEC_POIL_OU_INSERT_BOIS:
                prs1=chauffageAvecPoil.getPrs1();
                break;
            case CHAUDIERE_GAZ_OU_FIOUL_AVEC_CHAUDIERE_BOIS:
                prs1=chaudiereGaz.getPrs1();
                break;
            case CHAUDIERE_AVEC_PAC:
                prs1=chaudiereAvecPac.getPrs1();
                break;
            case CHAUDIERE_AVEC_PAC_ET_INSERT_BOIS:
                prs1=chaudiereAvecPacEtPoil.getPrs1();
                break;
        }
        this.actualisePr();
    }
    public void actualisePrs2(){
        switch (installationEcs){
            case CHAUFFE_EAU_THERMODYNAMIQUE_SUR_AIR_EXTRAIT:
            case CHAUFFE_EAU_THERMODYNAMIQUE_SUR_AIR_EXTERIEUR:
            case CHAUFFE_EAU_GAZ_INF_1991:
            case CHAUFFE_EAU_GAZ_ENTRE_1991_2002:
            case CHAUFFE_EAU_GAZ_SUP_2003:
                this.prs2=2.1f;
                break;
            case CHAUDIERE:
                this.prs2=1.05f;
                break;
            case BALLON_ELECTRIQUE_HORIZONTAL_INF_15ANS:
            case BALLON_ELECTRIQUE_HORIZONTAL_SUP_15ANS:
            case BALLON_ELECTRIQUE_VERTICAL_INF_15ANS:
            case BALLON_ELECTRIQUE_VERTICAL_SUP_15ANS:
            case ACCUMULATEUR_GAZ_CLASSIQUE_INF_1991:
            case ACCUMULATEUR_GAZ_CLASSIQUE_ENTRE_1991_2002:
            case ACCUMULATEUR_GAZ_CLASSIQUE_SUP_2003:
            case ACCUMULATEUR_GAZ_CONDENSATION:
                if (localEquipementEcs.getBoolean()){
                    this.prs2=3.7f;
                }else{
                    this.prs2=1.05f;
                }
                break;
        }
        this.actualisePr();
    }
    public void actualiseCch(){
        switch(this.installationChauffage){
            case CHAUFFAGE_UNIQUE:
                if(this.rendementHasTobeCalculated(chauffageUnique))
                    this.actualiseRendementGenerationChauffage(chauffageUnique);
                float ich=chauffageUnique.getIch();
                this.cch=this.bch*ich*fCh;
//                System.out.println("-- Chauffage unique --");
//                System.out.println("cch = " + cch);
//                System.out.println("bch = " + bch);
//                System.out.println("ich = " + ich);
//                System.out.println("-- ********* --");
                break;
            case CHAUFFAGE_AVEC_POIL_OU_INSERT_BOIS:
                if(this.rendementHasTobeCalculated(chauffageAvecPoil))
                    this.actualiseRendementGenerationChauffage(chauffageAvecPoil);
                float k1 = frequenceUtilisationPoeleAvecChauffage.getFrequence();
                float ichChauffage = chauffageAvecPoil.getIch();
                float ichPoele = poeleAvecChauffage.getIch();
                this.cch=(k1*bch*ichPoele+(1-k1)*bch*ichChauffage)*fCh;
//                System.out.println("-- Chaudiere avec poele --");
//                System.out.println("cch = " + cch);
//                System.out.println("bch = " + bch);
//                System.out.println("ichChauffage = " + ichChauffage);
//                System.out.println("ichPoele = " + ichPoele);
//                System.out.println("-- ********* --");
                break;
            case CHAUDIERE_GAZ_OU_FIOUL_AVEC_CHAUDIERE_BOIS:
                this.actualiseRendementGenerationChauffage(chaudiereGaz);
                this.actualiseRendementGenerationChauffage(chaudiereBois);
                float ichChaudiereGazFioul = chaudiereGaz.getIch();
                float ichChaudiereBois = chaudiereBois.getIch();
                this.cch=(0.75f*bch*ichChaudiereBois+0.25f*bch*ichChaudiereGazFioul)*fCh;
//                System.out.println("-- Chaudiere Gaz + chaudiere bois --");
//                System.out.println("cch = " + cch);
//                System.out.println("bch = " + bch);
//                System.out.println("ichChaudiereGazFioul = " + ichChaudiereGazFioul);
//                System.out.println("ichChaudiereBois = " + ichChaudiereBois);
//                System.out.println("-- ********* --");
                break;
            case CHAUDIERE_AVEC_PAC:
                if(this.rendementHasTobeCalculated(chaudiereAvecPac))
                    this.actualiseRendementGenerationChauffage(chaudiereAvecPac);
                float ichChaudiere = chaudiereAvecPac.getIch();
                float ichPac = pacAvecChaudiere.getIch();
                this.cch=(0.8f*bch*ichPac+0.2f*bch*ichChaudiere)*fCh;
//                System.out.println("-- Chaudiere avec pac --");
//                System.out.println("cch = " + cch);
//                System.out.println("bch = " + bch);
//                System.out.println("ichChaudiere = " + ichChaudiere);
//                System.out.println("ichPac = " + ichPac);
//                System.out.println("-- ********* --");
                break;
            case CHAUDIERE_AVEC_PAC_ET_INSERT_BOIS:
                if(this.rendementHasTobeCalculated(chaudiereAvecPacEtPoil))
                    this.actualiseRendementGenerationChauffage(chaudiereAvecPacEtPoil);
                float k2 = frequenceUtilisationPoeleAvecChauffageEtPac.getFrequence();
                float ichChaudiere2 = chaudiereAvecPacEtPoil.getIch();
                float ichPac2 = pacAvecChaudiereEtPoele.getIch();
                float ichPoele2 = poeleAvecChaudiereEtPac.getIch();
                this.cch=((1-k2)*(0.8f*bch*ichPac2)+(1-k2)*(0.2f*bch*ichChaudiere2)+k2*bch*ichPoele2)*fCh;
//                System.out.println("-- Chaudiere avec pac et poele --");
//                System.out.println("cch = " + cch);
//                System.out.println("bch = " + bch);
//                System.out.println("ichChaudiere2 = " + ichChaudiere2);
//                System.out.println("ichPac2 = " + ichPac2);
//                System.out.println("ichPoele2 = " + ichPoele2);
//                System.out.println("-- ********* --");
                break;
        }
        this.actualiseScoreDpe();
    }
    public void actualiseFacteurSolaire(){
        if(presenceInstallationSolaire==PresenceInstallationSolaireEnum.INSTALLATION_SOLAIRE_PRESENTE){
            fCh=departementBatiment.getFch();
            fEcs=departementBatiment.getfEcs();
        }else{
            fCh=1;
            fEcs=0;
        }
        this.actualiseCch();
        this.actualiseCecs();
    }

    // 5.Rendements des installations
    private Chauffage.Generateur generateurChauffageUnique = Chauffage.Generateur.CHAUDIERE_ELECTRIQUE;
    private Chauffage.Generateur generateurChauffageAvecPoil = Chauffage.Generateur.CHAUDIERE_ELECTRIQUE;
    private Chauffage.Generateur generateurChaudiereAvecPac = Chauffage.Generateur.CHAUDIERE_ELECTRIQUE;
    private Chauffage.Generateur generateurChaudiereAvecPacEtPoil = Chauffage.Generateur.CHAUDIERE_ELECTRIQUE;
    private Chauffage.Generateur generateurChaudiereGaz = Chauffage.Generateur.CHAUDIERE_GAZ_CLASSIQUE_AVANT_1981;
    private Chauffage.Generateur generateurChaudiereBois = Chauffage.Generateur.CHAUDIERE_BOIS_PLUS_DE_15_ANS;
    private Chauffage.Generateur generateurPacAvecChaudiere = Chauffage.Generateur.POMPE_A_CHALEUR_AIR_AIR;
    private Chauffage.Generateur generateurPacAvecChaudiereEtPoele = Chauffage.Generateur.POMPE_A_CHALEUR_AIR_AIR;
    private Chauffage.Generateur generateurPoeleAvecChauffage = Chauffage.Generateur.POIL_OU_INSERT_BOIS_AVANT_2001;
    private Chauffage.Generateur generateurPoeleAvecChaudiereEtPac = Chauffage.Generateur.POIL_OU_INSERT_BOIS_AVANT_2001;
    private Chauffage chauffageUnique = new Chauffage(generateurChauffageUnique,true,false,Chauffage.Emission.RADIATEUR);
    private Chauffage chauffageAvecPoil = new Chauffage(generateurChauffageAvecPoil,true,false,Chauffage.Emission.RADIATEUR);
    private Chauffage chaudiereAvecPac = new Chauffage(generateurChaudiereAvecPac,true,false,Chauffage.Emission.RADIATEUR);
    private Chauffage chaudiereAvecPacEtPoil = new Chauffage(generateurChaudiereAvecPacEtPoil,true,false,Chauffage.Emission.RADIATEUR);
    private Chauffage chaudiereGaz = new Chauffage(generateurChaudiereGaz,true,false,Chauffage.Emission.RADIATEUR);
    private Chauffage chaudiereBois = new Chauffage(generateurChaudiereBois,true,false,Chauffage.Emission.RADIATEUR);
    private Chauffage pacAvecChaudiere = new Chauffage(generateurPacAvecChaudiere,true,false,Chauffage.Emission.RADIATEUR);
    private Chauffage pacAvecChaudiereEtPoele = new Chauffage(generateurPacAvecChaudiereEtPoele,true,false,Chauffage.Emission.RADIATEUR);
    private Chauffage poeleAvecChauffage = new Chauffage(generateurPoeleAvecChauffage,true,false,Chauffage.Emission.RADIATEUR);
    private Chauffage poeleAvecChaudiereEtPac = new Chauffage(generateurPoeleAvecChaudiereEtPac,true,false,Chauffage.Emission.RADIATEUR);
    private Chauffage.Emission typeEmetteurDeChaleur = Chauffage.Emission.RADIATEUR;
    public void actualiseEmetteurAllGenerateurs(){
        chauffageUnique.setEmission(typeEmetteurDeChaleur);
        chauffageAvecPoil.setEmission(typeEmetteurDeChaleur);
        poeleAvecChauffage.setEmission(typeEmetteurDeChaleur);
        chaudiereAvecPac.setEmission(typeEmetteurDeChaleur);
        pacAvecChaudiere.setEmission(typeEmetteurDeChaleur);
        chaudiereGaz.setEmission(typeEmetteurDeChaleur);
        chaudiereBois.setEmission(typeEmetteurDeChaleur);
        chaudiereAvecPacEtPoil.setEmission(typeEmetteurDeChaleur);
        pacAvecChaudiereEtPoele.setEmission(typeEmetteurDeChaleur);
        poeleAvecChaudiereEtPac.setEmission(typeEmetteurDeChaleur);
        chaudiereEcs.setEmission(typeEmetteurDeChaleur);
    }
    public void actualiseLocalAllGenerateurs(){
        boolean tampon;
        if (localEquipementEcs.equals(LocalEquipementEcsEnum.SITUE_DANS_LOCAL_CHAUFFE)){
            tampon=true;
        }else{
            tampon=false;
        }
        chauffageUnique.setGenerateurDansVolumeChauffe(tampon);
        chauffageAvecPoil.setGenerateurDansVolumeChauffe(tampon);
        poeleAvecChauffage.setGenerateurDansVolumeChauffe(tampon);
        chaudiereAvecPac.setGenerateurDansVolumeChauffe(tampon);
        pacAvecChaudiere.setGenerateurDansVolumeChauffe(tampon);
        chaudiereGaz.setGenerateurDansVolumeChauffe(tampon);
        chaudiereBois.setGenerateurDansVolumeChauffe(tampon);
        chaudiereAvecPacEtPoil.setGenerateurDansVolumeChauffe(tampon);
        pacAvecChaudiereEtPoele.setGenerateurDansVolumeChauffe(tampon);
        poeleAvecChaudiereEtPac.setGenerateurDansVolumeChauffe(tampon);
        chaudiereEcs.setGenerateurDansVolumeChauffe(tampon);
        this.actualisePrs1();
    }
    public void actualiseRobinetAllGenerateurs(){
        boolean presence=true;
        if (presenceRobinetThermostatique.equals(PresenceRobinetEnum.ABSENCE_ROBINET_THERMOSTATIQUE)){
            presence=false;
        }
        chauffageUnique.setPresenceRobinetThermostatique(presence);
        chauffageAvecPoil.setPresenceRobinetThermostatique(presence);
        poeleAvecChauffage.setPresenceRobinetThermostatique(presence);
        chaudiereAvecPac.setPresenceRobinetThermostatique(presence);
        pacAvecChaudiere.setPresenceRobinetThermostatique(presence);
        chaudiereGaz.setPresenceRobinetThermostatique(presence);
        chaudiereBois.setPresenceRobinetThermostatique(presence);
        chaudiereAvecPacEtPoil.setPresenceRobinetThermostatique(presence);
        pacAvecChaudiereEtPoele.setPresenceRobinetThermostatique(presence);
        poeleAvecChaudiereEtPac.setPresenceRobinetThermostatique(presence);
        chaudiereEcs.setPresenceRobinetThermostatique(presence);
    }
    public void actualiseRendementsChauffage(Chauffage chauffage){
        boolean situeDansLocalChauffe,presenceRobinet;
        if (localEquipementEcs.equals(LocalEquipementEcsEnum.SITUE_DANS_LOCAL_CHAUFFE)){
            situeDansLocalChauffe=true;
        }else{
            situeDansLocalChauffe=false;
        }
        if (presenceRobinetThermostatique.equals(PresenceRobinetEnum.PRESENCE_ROBINET_THERMOSTATIQUE)){
            presenceRobinet=true;
        }else{
            presenceRobinet=false;
        }
        chauffage.setEmission(typeEmetteurDeChaleur);
        chauffage.setGenerateurDansVolumeChauffe(situeDansLocalChauffe);
        chauffage.setPresenceRobinetThermostatique(presenceRobinet);
    }
    public boolean rendementHasTobeCalculated(Chauffage chauffage){
        boolean answer=false;
        String nameGenerateur = chauffage.getGenerateur().toString();
        if (nameGenerateur.startsWith("Chaudière")) {
            if (!chauffage.getGenerateur().equals(Chauffage.Generateur.CHAUDIERE_ELECTRIQUE)){
                answer = true;
            }
        }
        if (chauffage.getGenerateur().equals(Chauffage.Generateur.RADIATEUR_GAZ_AVANT_2006)
        || chauffage.getGenerateur().equals(Chauffage.Generateur.RADIATEUR_GAZ_APRES_2006)){
            answer = true;
        }
        return answer;
    }
    public void manageLayoutFuncInstallationChauffage(Layout layout){
        switch (installationChauffage){
            case CHAUFFAGE_UNIQUE:
                String nameGenerateur = chauffageUnique.getGenerateur().toString();
                if (nameGenerateur.startsWith("Chaudière")) {
                    if (installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                            && generateurChauffageUnique != Chauffage.Generateur.CHAUDIERE_ELECTRIQUE) {
                        chaudiereAssureChauffageEtEcs = true;
                        chaudiereEcs = chauffageUnique;
                        ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                        ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                    }else if (installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                            && generateurChauffageUnique == Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                        chaudiereAssureChauffageEtEcs=false;
                        installationEcs=TypeEquipementEcsEnum.BALLON_ELECTRIQUE_HORIZONTAL_SUP_15ANS;
                        ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                        ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                    }else if (! installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                            && generateurChauffageUnique != Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                        chaudiereAssureChauffageEtEcs=true;
                        chaudiereEcs=chauffageUnique;
                        ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                        ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                    }else if (! installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                            && generateurChauffageUnique == Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                        chaudiereAssureChauffageEtEcs=false;
                        ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                        ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                    }
                }else{
                    chaudiereAssureChauffageEtEcs=false;
                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                    if(installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)){
                        installationEcs=TypeEquipementEcsEnum.BALLON_ELECTRIQUE_HORIZONTAL_SUP_15ANS;
                    }
                }
                this.actualisePrs1();
                this.actualiseRendementsChauffage(chauffageUnique);
                this.actualiseI0();
                this.actualiseCch();
                this.actualiseRendementEcs();
                break;
            case CHAUFFAGE_AVEC_POIL_OU_INSERT_BOIS:
                nameGenerateur = chauffageAvecPoil.getGenerateur().toString();
                if (nameGenerateur.startsWith("Chaudière")){ // Si le générateur est une chaudière
                    if (installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                            && generateurChauffageAvecPoil != Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                        chaudiereAssureChauffageEtEcs=true;
                        chaudiereEcs=chauffageAvecPoil;
                        ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                        ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                    }else if (installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                            && generateurChauffageAvecPoil == Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                        chaudiereAssureChauffageEtEcs=false;
                        installationEcs=TypeEquipementEcsEnum.BALLON_ELECTRIQUE_HORIZONTAL_SUP_15ANS;
                        ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                        ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                    }else if (! installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                            && generateurChauffageAvecPoil != Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                        chaudiereAssureChauffageEtEcs=true;
                        chaudiereEcs=chauffageAvecPoil;
                        ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                        ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                    }else if (! installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                            && generateurChauffageAvecPoil == Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                        chaudiereAssureChauffageEtEcs=false;
                        ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                        ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                    }
                }else{
                    chaudiereAssureChauffageEtEcs=false;
                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                    if(installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)){
                        installationEcs=TypeEquipementEcsEnum.BALLON_ELECTRIQUE_HORIZONTAL_SUP_15ANS;
                    }
                }
                this.actualisePrs1();
                this.actualiseI0();
                this.actualiseCch();
                this.actualiseRendementEcs();
                break;
            case CHAUDIERE_GAZ_OU_FIOUL_AVEC_CHAUDIERE_BOIS:
                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                if (installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)){
                    chaudiereAssureChauffageEtEcs=true;
                    chaudiereEcs=chaudiereGaz;
                }else if (! installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)){
                    chaudiereAssureChauffageEtEcs=false;
                }
                break;
            case CHAUDIERE_AVEC_PAC:
                if (installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                        && generateurChaudiereAvecPac != Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                    chaudiereAssureChauffageEtEcs=true;
                    chaudiereEcs=chaudiereAvecPac;
                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                }else if (installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                        && generateurChaudiereAvecPac == Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                    chaudiereAssureChauffageEtEcs=false;
                    installationEcs=TypeEquipementEcsEnum.BALLON_ELECTRIQUE_HORIZONTAL_SUP_15ANS;
                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                }else if (! installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                        && generateurChaudiereAvecPac != Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                    chaudiereAssureChauffageEtEcs=true;
                    chaudiereEcs=chaudiereAvecPac;
                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                }else if (! installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                        && generateurChaudiereAvecPac == Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                    chaudiereAssureChauffageEtEcs=false;
                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                }
                this.actualisePrs1();
                this.actualiseRendementsChauffage(chaudiereAvecPac);
                this.actualiseI0();
                this.actualiseCch();
                this.actualiseRendementEcs();
                break;
            case CHAUDIERE_AVEC_PAC_ET_INSERT_BOIS:
                if (installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                        && generateurChaudiereAvecPacEtPoil != Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                    chaudiereAssureChauffageEtEcs=true;
                    chaudiereEcs=chaudiereAvecPacEtPoil;
                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                }else if (installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                        && generateurChaudiereAvecPacEtPoil == Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                    chaudiereAssureChauffageEtEcs=false;
                    installationEcs=TypeEquipementEcsEnum.BALLON_ELECTRIQUE_HORIZONTAL_SUP_15ANS;
                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                }else if (! installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                        && generateurChaudiereAvecPacEtPoil != Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                    chaudiereAssureChauffageEtEcs=true;
                    chaudiereEcs=chaudiereAvecPacEtPoil;
                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                }else if (! installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                        && generateurChaudiereAvecPacEtPoil == Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                    chaudiereAssureChauffageEtEcs=false;
                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                }
                this.actualisePrs1();
                this.actualiseRendementsChauffage(chaudiereAvecPacEtPoil);
                this.actualiseI0();
                this.actualiseCch();
                this.actualiseRendementEcs();
                break;
        }
    }

    // 6.Rendement de génération des chaudières
    private PresenceThermostatEnum presenceThermostat = PresenceThermostatEnum.AUCUN_DES_DEUX;
    private PresenceRobinetEnum presenceRobinetThermostatique = PresenceRobinetEnum.ABSENCE_ROBINET_THERMOSTATIQUE;
    private ProgrammationSystemeEnum programmationSysteme = ProgrammationSystemeEnum.IMPOSSIBLE;
    private boolean chaudiereAssureChauffageEtEcs=false;
    private int tabTauxDeCharge[] = new int[10];
    private float tabCoeffPondX[] = new float[10];
    public void actualiseRendementGenerationChauffage(Chauffage chauffage){
        float tInt=this.tInt.getTemperatureInterieure();
        float tExtBase=departementBatiment.getTempExtBase();
        float pch;
        float pecs=2.844923077f; // Cas défavorable
        float pDim;
        float pn=0;
        float rr=chauffage.getRr();
        float rd=chauffage.getRd();
        float re=chauffage.getRe();
        float rg;
        float cdimRef;
        float tabTchxFinal[] = new float [10];
        float tabQpx[] = new float [10];
        float tfonc100 = this.getTfonc100();
        float tfonc30;
        float qp0,qp15,qp30,qp50,qp100;
        float pmfou=0;
        float pmcons=0;
        boolean haveRegulation=presenceThermostat.getBoolean();

        pch=1.2f*gv*(tInt-tExtBase)/(1000*rr*rd*re);
        pDim=pch;

        if (chauffage.getGenerateur().equals(Chauffage.Generateur.RADIATEUR_GAZ_AVANT_2006)
                || chauffage.getGenerateur().equals(Chauffage.Generateur.RADIATEUR_GAZ_APRES_2006)){
            int n = (int) sh/12;
            pn=pDim/n;
            chauffage.setPn(pn);
        }else{
            if(chaudiereAssureChauffageEtEcs){
                if(declenchementChaudiere.getBoolean())
                    pecs=21;

                pDim=Math.max(pch,pecs);
            }
            if (pDim<=5){
                pn=5;
                chauffage.setPn(pn);
            }else if (pDim>5 && pDim<=10){
                pn=10;
                chauffage.setPn(pn);
            }else if (pDim>10 && pDim<=13){
                pn=13;
                chauffage.setPn(pn);
            }else if (pDim>13 && pDim<=18){
                pn=18;
                chauffage.setPn(pn);
            }else if (pDim>18 && pDim<=24){
                pn=24;
                chauffage.setPn(pn);
            }else if (pDim>24 && pDim<=28){
                pn=28;
                chauffage.setPn(pn);
            }else if (pDim>28 && pDim<=32){
                pn=32;
                chauffage.setPn(pn);
            }else if (pDim>32 && pDim<=40){
                pn=40;
                chauffage.setPn(pn);
            }else if (pDim>40){
                pn = (((int)(pDim / 5)) + 1)*5;
                chauffage.setPn(pn);
            }
        }

        cdimRef=1000*pn/gv*(tInt-tExtBase);
        for(int i=0;i<9;i++){
            tabTchxFinal[i] = tabTauxDeCharge[i]/cdimRef;
        }
        tabTchxFinal[9]=tabTauxDeCharge[9];

        for (int i=0;i<9;i++){
            pmfou += pn*tabTchxFinal[i]*tabCoeffPondX[i];
        }

        float rpint = chauffage.getRpint();
        float rpn = chauffage.getRpn();
        float pveil = chauffage.getPuissanceVeilleuse();
        qp0=chauffage.getQp0();

        switch(chauffage.getGenerateur()){
            case CHAUDIERE_GAZ_BASSE_TEMPERATURE_AVANT_2001:
            case CHAUDIERE_GAZ_BASSE_TEMPERATURE_APRES_2001:
            case CHAUDIERE_FIOUL_BASSE_TEMPERATURE:
                tfonc30=this.getTfonc30ChaudiereBasseTemperature();
                if(haveRegulation){
                    qp30=(0.3f*pn*(100-(rpint+0.1f*(40-tfonc30))))/(rpint+0.1f*(40-tfonc30));
                }else{
                    qp30=(0.3f*pn*(100-(rpint+0.1f*(40-tfonc100))))/(rpint+0.1f*(40-tfonc100));
                }
                qp15=qp30/2;
                qp100=(pn*(100-(rpn+0.1f*(70-tfonc100))))/(rpn+0.1f*(70-tfonc100));
                for (int i=0;i<9;i++){
                    if(i<2){ // Entre 0 et 15% de charge
                        tabQpx[i] = ((qp15-0.15f*qp0)*tabTchxFinal[i]/0.15f)+0.15f*qp0;
                    }else if (i==2){ // Entre 15 et 30% de charge
                        tabQpx[i] = ((qp30-qp15)/0.15f)*tabTchxFinal[i]+qp15-((qp30-qp15)/0.15f)*0.15f;
                    }else if (i>2){ // Entre 30 et 100% de charge
                        tabQpx[i] = ((qp100-qp30)/0.7f)*tabTchxFinal[i]+qp30-((qp100-qp30)/0.7f)*0.3f;
                    }
                }
                break;

            case CHAUDIERE_GAZ_CONDENSATION_AVANT_1986:
            case CHAUDIERE_GAZ_CONDENSATION_ENTRE_1986_ET_2001:
            case CHAUDIERE_GAZ_CONDENSATION_APRES_2001:
            case CHAUDIERE_FIOUL_CONDENSATION:
                tfonc30=this.getTfonc30ChaudiereCondensation();
                if(haveRegulation){
                    qp30=(0.3f*pn*(100-(rpint+0.2f*(33-tfonc30))))/(rpint+0.2f*(33-tfonc30));
                }else{
                    qp30=(0.3f*pn*(100-(rpint+0.2f*(33-tfonc100))))/(rpint+0.2f*(33-tfonc100));
                }
                qp15=qp30/2;
                qp100=(pn*(100-(rpn+0.1f*(70-tfonc100))))/(rpn+0.1f*(70-tfonc100));
                for (int i=0;i<9;i++){
                    if(i<2){ // Entre 0 et 15% de charge
                        tabQpx[i] = ((qp15-0.15f*qp0)*tabTchxFinal[i]/0.15f)+0.15f*qp0;
                    }else if (i==2){ // Entre 15 et 30% de charge
                        tabQpx[i] = ((qp30-qp15)/0.15f)*tabTchxFinal[i]+qp15-((qp30-qp15)/0.15f)*0.15f;
                    }else if (i>2){ // Entre 30 et 100% de charge
                        tabQpx[i] = ((qp100-qp30)/0.7f)*tabTchxFinal[i]+qp30-((qp100-qp30)/0.7f)*0.3f;
                    }
                }
                break;

            case CHAUDIERE_GAZ_CLASSIQUE_AVANT_1981:
            case CHAUDIERE_GAZ_CLASSIQUE_ENTRE_1981_ET_1986:
            case CHAUDIERE_GAZ_CLASSIQUE_ENTRE_1986_ET_1991:
            case CHAUDIERE_FIOUL_CLASSIQUE_AVANT_1970:
            case CHAUDIERE_FIOUL_CLASSIQUE_ENTRE_1970_ET_1976:
            case CHAUDIERE_FIOUL_CLASSIQUE_ENTRE_1976_ET_1981:
            case CHAUDIERE_FIOUL_CLASSIQUE_ENTRE_1981_ET_1991:
                tfonc30=this.getTfonc30ChaudiereStandardAvant1990();
                if(haveRegulation){
                    qp30=(0.3f*pn*(100-(rpint+0.1f*(50-tfonc30))))/(rpint+0.1f*(50-tfonc30));
                }else{
                    qp30=(0.3f*pn*(100-(rpint+0.1f*(50-tfonc100))))/(rpint+0.1f*(50-tfonc100));
                }
                qp100=(pn*(100-(rpn+0.1f*(70-tfonc100))))/(rpn+0.1f*(70-tfonc100));
                for (int i=0;i<9;i++){
                    if(i<=2){ // Entre 0 et 30% de charge
                        tabQpx[i] = ((qp30-0.15f*qp0)*tabTchxFinal[i]/0.3f)+0.15f*qp0;
                    }else if (i>2){ // Entre 30 et 100% de charge
                        tabQpx[i] = ((qp100-qp30)/0.7f)*tabTchxFinal[i]+qp30-((qp100-qp30)/0.7f)*0.3f;
                    }
                }
                break;

            case CHAUDIERE_GAZ_STANDARD_ENTRE_1991_ET_2001:
            case CHAUDIERE_GAZ_STANDARD_APRES_2001:
            case CHAUDIERE_FIOUL_STANDARD_APRES_1991:
                tfonc30=this.getTfonc30ChaudiereStandardApres1991();
                if(haveRegulation){
                    qp30=(0.3f*pn*(100-(rpint+0.1f*(50-tfonc30))))/(rpint+0.1f*(50-tfonc30));
                }else{
                    qp30=(0.3f*pn*(100-(rpint+0.1f*(50-tfonc100))))/(rpint+0.1f*(50-tfonc100));
                }
                qp100=(pn*(100-(rpn+0.1f*(70-tfonc100))))/(rpn+0.1f*(70-tfonc100));
                for (int i=0;i<9;i++){
                    if(i<=2){ // Entre 0 et 30% de charge
                        tabQpx[i] = ((qp30-0.15f*qp0)*tabTchxFinal[i]/0.3f)+0.15f*qp0;
                    }else if (i>2){ // Entre 30 et 100% de charge
                        tabQpx[i] = ((qp100-qp30)/0.7f)*tabTchxFinal[i]+qp30-((qp100-qp30)/0.7f)*0.3f;
                    }
                }
                break;

            case CHAUDIERE_BOIS_PLUS_DE_15_ANS:
            case CHAUDIERE_BOIS_MOINS_DE_15_ANS:
                qp50=0.5f*pn*(100-rpint)/rpint;
                qp100=pn*(100-rpn)/rpn;
                for (int i=0;i<9;i++){
                    if(i<=4){ // Entre 0 et 50% de charge
                        tabQpx[i] = ((qp50-qp0)/0.5f)*tabTchxFinal[i]+qp0;
                    }else if (i>4){ // Entre 50 et 100% de charge
                        tabQpx[i] = ((qp100-qp50)/0.5f)*tabTchxFinal[i]+2*qp50-qp100;
                    }
                }
                break;

            case RADIATEUR_GAZ_AVANT_2006:
            case RADIATEUR_GAZ_APRES_2006:
                for (int i=0;i<9;i++){
                    tabQpx[i]=((100-rpn)/rpn)*pn*tabTchxFinal[i];
                }
                break;
        }
        for (int i=0;i<9;i++){
            pmcons += pn*tabTchxFinal[i]*tabCoeffPondX[i]*((pn*tabTchxFinal[i]+tabQpx[i])/(pn*tabTchxFinal[i]));
        }
        rg = pmfou/(pmcons+0.3f*0.15f*qp0+pveil);
        chauffage.setRg(rg);
    }
    public float getTfonc100(){
        float tFonc100 = 80; // Cas défavorable
        switch(dateConstructionBatiment){
            case AVANT_1975:
            case ENTRE_1975_ET_1977:
            case ENTRE_1978_ET_1982:
                if(typeEmetteurDeChaleur.equals(Chauffage.Emission.PLANCHER_CHAUFFANT)){
                    tFonc100 = 60;
                }else{
                    tFonc100 = 80;
                }
                break;
            default:
                if(typeEmetteurDeChaleur.equals(Chauffage.Emission.PLANCHER_CHAUFFANT)){
                    tFonc100 = 35;
                }else{
                    tFonc100 = 65;
                }
                break;
        }
        return tFonc100;
    }
    public float getTfonc30ChaudiereCondensation(){
        float tFonc30 = 38; // Cas défavorable
        switch(dateConstructionBatiment){
            case AVANT_1975:
            case ENTRE_1975_ET_1977:
            case ENTRE_1978_ET_1982:
                if(typeEmetteurDeChaleur.equals(Chauffage.Emission.PLANCHER_CHAUFFANT)){
                    tFonc30 = 32;
                }else{
                    tFonc30 = 38;
                }
                break;
            default:
                if(typeEmetteurDeChaleur.equals(Chauffage.Emission.PLANCHER_CHAUFFANT)){
                    tFonc30 = 24.5f;
                }else{
                    tFonc30 = 34;
                }
                break;
        }
    return tFonc30;
    }
    public float getTfonc30ChaudiereBasseTemperature(){
        float tFonc30 = 48.5f; // Cas défavorable

        switch(dateConstructionBatiment){
            case AVANT_1975:
            case ENTRE_1975_ET_1977:
            case ENTRE_1978_ET_1982:
                if(typeEmetteurDeChaleur.equals(Chauffage.Emission.PLANCHER_CHAUFFANT)){
                    tFonc30 = 42.5f;
                }else{
                    tFonc30 = 48.5f;
                }
                break;
            default:
                if(typeEmetteurDeChaleur.equals(Chauffage.Emission.PLANCHER_CHAUFFANT)){
                    tFonc30 = 35;
                }else{
                    tFonc30 = 44;
                }
                break;
        }
        return tFonc30;
    }
    public float getTfonc30ChaudiereStandardAvant1990(){
        float tFonc30 = 59; // Cas défavorable
        switch(dateConstructionBatiment){
            case AVANT_1975:
            case ENTRE_1975_ET_1977:
            case ENTRE_1978_ET_1982:
                if(typeEmetteurDeChaleur.equals(Chauffage.Emission.PLANCHER_CHAUFFANT)){
                    tFonc30 = 53;
                }else{
                    tFonc30 = 59;
                }
                break;
            default:
                if(typeEmetteurDeChaleur.equals(Chauffage.Emission.PLANCHER_CHAUFFANT)){
                    tFonc30 = 50;
                }else{
                    tFonc30 = 54.5f;
                }
                break;
        }
        return tFonc30;
    }
    public float getTfonc30ChaudiereStandardApres1991(){
        float tFonc30 = 55.5f; // Cas défavorable
        switch(dateConstructionBatiment){
            case AVANT_1975:
            case ENTRE_1975_ET_1977:
            case ENTRE_1978_ET_1982:
                if(typeEmetteurDeChaleur.equals(Chauffage.Emission.PLANCHER_CHAUFFANT)){
                    tFonc30 = 49.5f;
                }else{
                    tFonc30 = 55.5f;
                }
                break;
            default:
                if(typeEmetteurDeChaleur.equals(Chauffage.Emission.PLANCHER_CHAUFFANT)){
                    tFonc30 = 45;
                }else{
                    tFonc30 = 51.5f;
                }
                break;
        }
        return tFonc30;
    }

    // 7.Expression du besoin de la consommation d'ECS
    private TypeEquipementEcsEnum installationEcs = TypeEquipementEcsEnum.BALLON_ELECTRIQUE_HORIZONTAL_SUP_15ANS;
    private Chauffage chaudiereEcs = new Chauffage(Chauffage.Generateur.CHAUDIERE_ELECTRIQUE,true,false,Chauffage.Emission.RADIATEUR);
    private LocalEquipementEcsEnum localEquipementEcs = LocalEquipementEcsEnum.SITUE_DANS_LOCAL_CHAUFFE;
    private DeclenchementChaudiereEnum declenchementChaudiere = DeclenchementChaudiereEnum.NON_DECLENCHEMENT_OUVERTURE_ROBINET_EAU_CHAUDE;
    private UsageEauChaudeEnum usageEauChaude = UsageEauChaudeEnum.DOUCHES;
    private float bEcs=3688;
    private float becs=55;
    private float nbJoursAbsenceParAn=0;
    private float nbHabitant=4;
    private float cEcs=5000;
    private float iEcs=5.45f;
    private float fEcs=0;
    private float tfr=10.5f;
    public void actualiseBecs(){
        bEcs=(1.1627f*(365-nbJoursAbsenceParAn)*nbHabitant*becs*(50-tfr))/1000;
        this.actualiseRendementEcs();
        this.actualiseCecs();
    }
    public void actualiseTfr(){
        switch (departementBatiment.getZoneHiver()){
            case 1:
                tfr=10.5f;
                break;
            case 2:
                tfr=12;
                break;
            case 3:
                tfr=14.5f;
                break;
            default:
                tfr=10.5f;
                break;
        }
        this.actualiseBecs();
    }
    public void actualisebecs(){
        switch (usageEauChaude){
            case DOUCHES:
                becs=40;
                break;
            case BAINS:
                becs=55;
                break;
            default:
                becs=55;
                break;
        }
        this.actualiseBecs();
    }
    public void actualiseCecs(){
        cEcs=bEcs*iEcs*(1-fEcs);
        this.actualiseScoreDpe();
    }

    // 8.Rendements de l'installation d'ECS
    public void actualiseRendementEcs(){
        float cr,qgw,rs,rd,rendement,rpn,qp0,pveil,cop,pecs,vs,cef;
        vs = this.getVs();
        cef = this.getCef();
        switch (installationEcs){
            case BALLON_ELECTRIQUE_HORIZONTAL_INF_15ANS:
                rd=getRdBallonElectrique();
                cr=(939+10.4f*vs)/(45*vs);
                qgw=0.344f*vs*cr*(55-13);
                rendement = 1.08f/(1+(qgw*rd/bEcs));
                iEcs=1/rendement;
                break;

            case BALLON_ELECTRIQUE_HORIZONTAL_SUP_15ANS:
                rd=getRdBallonElectrique();
                cr=(939+10.4f*vs)/(45*vs);
                qgw=0.344f*vs*cr*(55-13);
                rendement = 1/(1+(qgw*rd/bEcs));
                iEcs=1/rendement;
                break;

            case BALLON_ELECTRIQUE_VERTICAL_INF_15ANS:
                rd=getRdBallonElectrique();
                cr=(224+66.3f*(float)Math.pow(vs,2/3))/(45*vs);
                qgw=0.344f*vs*cr*(55-13);
                rendement = 1.08f/(1+(qgw*rd/bEcs));
                iEcs=1/rendement;
                break;

            case BALLON_ELECTRIQUE_VERTICAL_SUP_15ANS:
                rd=getRdBallonElectrique();
                cr=(224+66.3f*(float)Math.pow(vs,2/3))/(45*vs);
                qgw=0.344f*vs*cr*(55-13);
                rendement = 1/(1+(qgw*rd/bEcs));
                iEcs=1/rendement;
                break;

            case CHAUFFE_EAU_GAZ_INF_1991:
                iEcs=(1/73)+1720*(3/bEcs)+6536*(0.13f/bEcs);
                break;
            case CHAUFFE_EAU_GAZ_ENTRE_1991_2002:
                iEcs=(1/84)+1720*(1/bEcs)+6536*(0.1f/bEcs);
                break;
            case CHAUFFE_EAU_GAZ_SUP_2003:
                iEcs=(1/84)+1720*(1/bEcs);
                break;

            case CHAUDIERE:
                qp0=chaudiereEcs.getQp0();
                rpn=chaudiereEcs.getRpn();
                pveil=chaudiereEcs.getPuissanceVeilleuse();
                iEcs = (1/rpn)+1720*(qp0/bEcs)+6536*(0.5f*pveil/bEcs);
                break;

            case ACCUMULATEUR_GAZ_CLASSIQUE_INF_1991 :
                qgw=11*(float)Math.pow(vs,2/3)+0.015f*10;
                iEcs=(1/83)+((8256*0.12f+qgw)/bEcs)+(6536*0.15f/bEcs);
                break;
            case ACCUMULATEUR_GAZ_CLASSIQUE_ENTRE_1991_2002 :
                qgw=11*(float)Math.pow(vs,2/3)+0.015f*10;
                iEcs=(1/83)+((8256*0.1f+qgw)/bEcs)+(6536*0.15f/bEcs);
                break;
            case ACCUMULATEUR_GAZ_CLASSIQUE_SUP_2003 :
                qgw=11*(float)Math.pow(vs,2/3)+0.015f*10;
                iEcs=(1/83)+((8256*0.12f+qgw)/bEcs);
                break;
            case ACCUMULATEUR_GAZ_CONDENSATION :
                qgw=11*(float)Math.pow(vs,2/3)+0.015f*10;
                iEcs=(1/98)+((8256*0.12f+qgw)/bEcs);
                break;

            case CHAUFFE_EAU_THERMODYNAMIQUE_SUR_AIR_EXTRAIT:
                cop=2.4f;
                rd=getRdBallonThermodynamique();
                cr=(224+66.3f*(float)Math.pow(vs,2/3))/(45*vs);
                if(vs<=150){
                    pecs = 5-1.751f*(vs-20)/65;
                }else{
                    pecs = (7.14f*vs+428)/1000;
                }
                iEcs=(3/(1+2*cop))+rd*(11.9f*cr*vs*(cef-0.0576f*(bEcs/(pecs*1000*cop*rd))))/bEcs;
                break;
            case CHAUFFE_EAU_THERMODYNAMIQUE_SUR_AIR_EXTERIEUR:
                cop=2.1f;
                rd=getRdBallonThermodynamique();
                cr=(224+66.3f*(float)Math.pow(vs,2/3))/(45*vs);
                if(vs<=150){
                    pecs = 5-1.751f*(vs-20)/65;
                }else{
                    pecs = (7.14f*vs+428)/1000;
                }
                iEcs=(3/(1+2*cop))+rd*(11.9f*cr*vs*(cef-0.0576f*(bEcs/(pecs*1000*cop*rd))))/bEcs;
                break;

        }
        this.actualiseCecs();
    }
    public float getRdBallonElectrique(){
        if(localEquipementEcs.getBoolean()){
            return 0.85f;
        }else{
            return 0.8f;
        }
    }
    public float getRdBallonThermodynamique(){
        if(localEquipementEcs.getBoolean()){
            return 0.9f;
        }else{
            return 0.85f;
        }
    }
    public float getVs(){
        float vs=300;
        switch (categorieLogement){
            case T1_F1:
                vs=100;
                break;
            case T2_F2:
                vs=150;
                break;
            case T3_F3:
                vs=200;
                break;
            case T4_F4:
                vs=250;
                break;
            default:
                vs=300;
                break;
        }
        return vs;
    }
    public float getCef(){
        float cef=1.1f;
        if(typeAbonnementElectrique == TypeAbonnementElectriqueEnum.DOUBLE_TARIF){
            if(localEquipementEcs == LocalEquipementEcsEnum.SITUE_DANS_LOCAL_CHAUFFE){
                cef=0.6f;
            }else{ //LNC ou pas d'info
                cef=0.75f;
            }
        }else{ //Simple ou tarif ou pas d'info
            if(localEquipementEcs == LocalEquipementEcsEnum.SITUE_DANS_LOCAL_CHAUFFE){
                cef=0.9f;
            }else{ //LNC ou pas d'info
                cef=1.1f;
            }
        }
        return cef;
    }

    // 9.Consommation de climatisation
    private PresenceClimatisationLogementEnum presenceClimatisation = PresenceClimatisationLogementEnum.NON;
    private float cClimatisation;
    private float rClimatisation=4;
    private float sClimatisation; // Pas de climatisation au départ
    public void actualiseConsommationClimatisation(){
        switch(departementBatiment.getZoneEte()){
            case Ea:
                if (sClimatisation<150){rClimatisation=2;}
                else{rClimatisation=4;}
                break;
            case Eb:
                if (sClimatisation<150){rClimatisation=3;}
                else{rClimatisation=5;}
                break;
            case Ec:
                if (sClimatisation<150){rClimatisation=4;}
                else{rClimatisation=6;}
                break;
            case Ed:
                if (sClimatisation<150){rClimatisation=5;}
                else{rClimatisation=7;}
                break;
        }
        if (presenceClimatisation.equals(PresenceClimatisationLogementEnum.NON)){
            cClimatisation=0;
        }else{
            cClimatisation = rClimatisation * sClimatisation;
        }
        this.actualiseScoreDpe();
    }

    // 10.Concommation des usages spécifiques
    private TypeEquipementEclairageEnum equipementEclairage = TypeEquipementEclairageEnum.MAJORITAIREMENT_AMPOULE_A_INCANDESCENCE;
    private TypeEquipementCuissonEnum equipementCuisson = TypeEquipementCuissonEnum.FEUX_GAZ_ET_FOUR_GAZ;
    private ArrayList<TypeEquipementElectromenagerEnum> listEquipementElectromenager = new ArrayList<TypeEquipementElectromenagerEnum>();
    private float cElectromenager;
    private float cEclairageSurfacique;
    private float cEclairage;
    private float cCuisson;
    public void actualiseConsommationEclairage(){
        cEclairageSurfacique=equipementEclairage.getConsommationEclairage();
        cEclairage=cEclairageSurfacique * sh;
        this.actualiseScoreDpe();
    }
    public void actualiseConsommationElectromenager(){
        cElectromenager=0;
        for (TypeEquipementElectromenagerEnum actualEquipement : listEquipementElectromenager){
            cElectromenager+=actualEquipement.getConsommation();
        }
        this.actualiseScoreDpe();
    }
    public void actualiseConsommationCuisson(){
        cCuisson = equipementCuisson.getConsommation();
        this.actualiseScoreDpe();
    }

    /*** Constructeur en singleton ***/
    private static class DpeHolder
    {
        /** Instance unique non préinitialisée */
        private static Dpe INSTANCE = new Dpe();
    }

    public static synchronized void loadDpe(Dpe dpe) {
        DpeHolder.INSTANCE = dpe;
        dpe.init();
    }

    public static synchronized Dpe getInstance() {
        return DpeHolder.INSTANCE;
    }
    private Dpe () {
        init();
    }

    private boolean isFake = false;

    public Dpe (Object ... fake) {
        isFake = true;
        init();
    }

    private void init() {
        EventManager.getInstance().addListener(Channel.DPE, this);
        tabCoeffPondX[0]=0.1f;
        tabCoeffPondX[1]=0.25f;
        tabCoeffPondX[2]=0.2f;
        tabCoeffPondX[3]=0.15f;
        tabCoeffPondX[4]=0.1f;
        tabCoeffPondX[5]=0.1f;
        tabCoeffPondX[6]=0.05f;
        tabCoeffPondX[7]=0.025f;
        tabCoeffPondX[8]=0.025f;
        tabCoeffPondX[9]=0;
        int tampon=-5;
        for(int i=0;i<tabTauxDeCharge.length;i++){
            tampon+=10;
            tabTauxDeCharge[i]=tampon;
        }
    }

    /*---------------------------------Calculateur DPE-------------------------------------------*/

    public void actualiseScoreDpe(){
        if (sh != 0){
            scoreDpe = (cElectromenager+cEclairage+cCuisson+cClimatisation+cEcs+cch)/sh;
        }else{
            scoreDpe = 700;
        }
    }
    public float getScoreDpe(){
        return Math.round(this.scoreDpe);
    }

    public void notify(Channel c, Event e) throws InterruptedException {

        EventType eventType = e.getEventType();
        if (c == Channel.DPE || (isFake && c == Channel.FAKE_DPE)) {
            if (eventType instanceof DpeEvent) {
                DpeEvent event = (DpeEvent) eventType;
                Object o = e.getUserObject();
                switch (event) {
                    case TYPE_BATIMENT: {
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        EventRequest eventRequest = (EventRequest) items.get("eventRequest");
                        Layout layout = (Layout) items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            typeBatiment = (TypeBatimentEnum) items.get("lastValue");
                            this.actualiseI0();

                        } else if (eventRequest == EventRequest.GET_STATE) {
                            TypeBatimentEnum type = typeBatiment;
                            HashMap<String, Object> currentItems = new HashMap<String, Object>();
                            currentItems.put("lastValue", type);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_BATIMENT, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case CATEGORIE_BATIMENT: {
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        EventRequest eventRequest = (EventRequest) items.get("eventRequest");
                        Layout layout = (Layout) items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            categorieLogement = (CategorieLogementEnum) items.get("lastValue");
                            this.actualiseRendementEcs();

                        } else if (eventRequest == EventRequest.GET_STATE) {
                            CategorieLogementEnum type = categorieLogement;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue", type);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CATEGORIE_BATIMENT, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

//                    case FORME_MAISON: {
//                        HashMap<String,Object> items = (HashMap<String,Object>) o;
//                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
//                        if (eventRequest == EventRequest.UPDATE_STATE) {
//                            FormeMaisonEnum formeMaison = (FormeMaisonEnum) items.get("lastValue");
//                            if (formeMaison.equals(FormeMaisonEnum.CARRE)){FOR = 4.12;}
//                            else if (formeMaison.equals(FormeMaisonEnum.ALLONGE)){FOR = 4.81;}
//                            else if (formeMaison.equals(FormeMaisonEnum.DEVELOPPE)){FOR = 5.71;}
//                            general_properties.put(DpeEvent.FORME_MAISON, formeMaison);
//                        }
//                        else if (eventRequest == EventRequest.GET_STATE) {
//                            FormeMaisonEnum type = null;
//                            if (general_properties.containsKey(DpeEvent.FORME_MAISON)){
//                                type = (FormeMaisonEnum) general_properties.get(DpeEvent.FORME_MAISON);
//                            }
//                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
//                            currentItems.put("lastValue",type);
//                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
//                            Event e2 = new Event(DpeEvent.FORME_MAISON, currentItems);
//                            if (!isFake)
//                                EventManager.getInstance().put(Channel.DPE, e2);
//                        }
//                        break;
//                    }

//                    case MITOYENNETE_MAISON: {
//                        HashMap<String,Object> items = (HashMap<String,Object>) o;
//                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
//                        if (eventRequest == EventRequest.UPDATE_STATE) {
//                            MitoyenneteMaisonEnum mitoyenneteMaison = (MitoyenneteMaisonEnum) items.get("lastValue");
//                            if (mitoyenneteMaison.equals(MitoyenneteMaisonEnum.NON_ACCOLE)){MIT = 1;}
//                            else if (mitoyenneteMaison.equals(MitoyenneteMaisonEnum.ACCOLE_PETIT_COTE)){MIT = 0.8;}
//                            else if (mitoyenneteMaison.equals(MitoyenneteMaisonEnum.ACCOLE_GRAND_OU_DEUX_PETITS_COTES)){MIT = 0.7;}
//                            else if (mitoyenneteMaison.equals(MitoyenneteMaisonEnum.ACCOLE_GRAND_ET_UN_PETIT_COTE)){MIT = 0.5;}
//                            else if (mitoyenneteMaison.equals(MitoyenneteMaisonEnum.ACCOLE_DEUX_GRANDS_COTES)){MIT = 0.35;}
//                            general_properties.put(DpeEvent.MITOYENNETE_MAISON, mitoyenneteMaison);
//                        }
//                        else if (eventRequest == EventRequest.GET_STATE) {
//                            MitoyenneteMaisonEnum type = null;
//                            if (general_properties.containsKey(DpeEvent.MITOYENNETE_MAISON)){
//                                type = (MitoyenneteMaisonEnum) general_properties.get(DpeEvent.MITOYENNETE_MAISON);
//                            }
//                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
//                            currentItems.put("lastValue",type);
//                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
//                            Event e2 = new Event(DpeEvent.MITOYENNETE_MAISON, currentItems);
//                            if (!isFake)
//                                EventManager.getInstance().put(Channel.DPE, e2);
//                        }
//                        break;
//                    }

                    case DEPARTEMENT_BATIMENT:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            departementBatiment = (DepartementBatimentEnum) items.get("lastValue");
                            this.actualiseAs();
                            this.actualiseAi();
                            this.actualisedhCor();
                            this.actualiseConsommationClimatisation();
                            this.actualiseCch();
                            this.actualiseTfr();
                            this.actualiseFacteurSolaire();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            DepartementBatimentEnum type = this.departementBatiment;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.DEPARTEMENT_BATIMENT, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case ANNEE_CONSTRUCTION: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            dateConstructionBatiment = (DateConstructionBatimentEnum) items.get("lastValue");
                            // Actualisation des murs
                            for(Mur m:ModelHolder.getInstance().getBatiment().getMurs()){
                                actualiseCoeffDeperditionThermique(m);
                            }
                            // Actualisation des slabs
                            for(Slab s:ModelHolder.getInstance().getBatiment().getSlabs()){
                                actualiseCoeffDeperditionThermique(s,true,true);
                            }
                            this.actualiseSmeaAndQvarep();
                            this.actualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            DateConstructionBatimentEnum type = this.dateConstructionBatiment;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.ANNEE_CONSTRUCTION, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case ENERGIE_CONSTRUCTION: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            typeEnergieConstruction = (TypeEnergieConstructionEnum) items.get("lastValue");
                            // Actualisation des murs
                            for(Mur m:ModelHolder.getInstance().getBatiment().getMurs()){
                                actualiseCoeffDeperditionThermique(m);
                            }
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeEnergieConstructionEnum type = TypeEnergieConstructionEnum.AUTRE;
                            if (general_properties.containsKey(DpeEvent.ENERGIE_CONSTRUCTION)){
                                type = (TypeEnergieConstructionEnum) general_properties.get(DpeEvent.ENERGIE_CONSTRUCTION);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.ENERGIE_CONSTRUCTION, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case ABONNEMENT_ELECTRIQUE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            typeAbonnementElectrique = (TypeAbonnementElectriqueEnum) items.get("lastValue");
                            this.actualiseRendementEcs();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeAbonnementElectriqueEnum type = typeAbonnementElectrique;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.ABONNEMENT_ELECTRIQUE, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case CLIMATISATION_LOGEMENT: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            presenceClimatisation = (PresenceClimatisationLogementEnum) items.get("lastValue");
                            this.actualiseConsommationClimatisation();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            PresenceClimatisationLogementEnum type = presenceClimatisation;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CLIMATISATION_LOGEMENT, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case SURFACE_CLIMATISATION: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            this.sClimatisation = (float) items.get("lastValue");
                            general_properties.put(DpeEvent.SURFACE_CLIMATISATION, sClimatisation);
                            this.actualiseConsommationClimatisation();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            PresenceClimatisationLogementEnum type = null;
                            if (general_properties.containsKey(DpeEvent.SURFACE_CLIMATISATION)){
                                type = (PresenceClimatisationLogementEnum) general_properties.get(DpeEvent.SURFACE_CLIMATISATION);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.SURFACE_CLIMATISATION, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case EQUIPEMENT_ECLAIRAGE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            equipementEclairage = (TypeEquipementEclairageEnum) items.get("lastValue");
                            this.actualiseConsommationEclairage();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeEquipementEclairageEnum type = equipementEclairage;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.EQUIPEMENT_ECLAIRAGE, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case EQUIPEMENT_ELECTROMENAGER: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeEquipementElectromenagerEnum equipementElectromenager = (TypeEquipementElectromenagerEnum) items.get("lastValue");
                            if (listEquipementElectromenager.contains(equipementElectromenager)){
                                listEquipementElectromenager.remove(equipementElectromenager);
                            }else{
                                listEquipementElectromenager.add(equipementElectromenager);
                            }
                            this.actualiseConsommationElectromenager();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeEquipementElectromenagerEnum type = null;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.EQUIPEMENT_ELECTROMENAGER, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case EQUIPEMENT_CUISSON: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            equipementCuisson = (TypeEquipementCuissonEnum) items.get("lastValue");
                            this.actualiseConsommationCuisson();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeEquipementCuissonEnum type = equipementCuisson;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.EQUIPEMENT_CUISSON, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_VENTILATION: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            typeVentilation = (TypeVentilationEnum) items.get("lastValue");
                            this.actualiseSmeaAndQvarep();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeVentilationEnum type = typeVentilation;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_VENTILATION, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case NOMBRE_PERSONNES_DANS_LOGEMENT:{
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        EventRequest eventRequest = (EventRequest) items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            nbHabitant = (float) items.get("lastValue");
                            this.actualiseBecs();
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            HashMap<String, Object> currentItems = new HashMap<String, Object>();
                            currentItems.put("lastValue", nbHabitant);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.NOMBRE_PERSONNES_DANS_LOGEMENT, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case NOMBRE_JOURS_ABSENCE:{
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        EventRequest eventRequest = (EventRequest) items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            nbJoursAbsenceParAn = (float) items.get("lastValue");
                            this.actualiseBecs();
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            HashMap<String, Object> currentItems = new HashMap<String, Object>();
                            currentItems.put("lastValue", nbJoursAbsenceParAn);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.NOMBRE_JOURS_ABSENCE, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case INSTALLATION_CHAUFFAGE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        Layout layout = (Layout) items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            installationChauffage = (InstallationChauffageEnum) items.get("lastValue");
                            if (installationChauffage == InstallationChauffageEnum.CHAUFFAGE_UNIQUE) {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), false);
                            } else if (installationChauffage == InstallationChauffageEnum.CHAUFFAGE_AVEC_POIL_OU_INSERT_BOIS){
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), false);
                            } else if (installationChauffage == InstallationChauffageEnum.CHAUDIERE_GAZ_OU_FIOUL_AVEC_CHAUDIERE_BOIS){
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                            } else if (installationChauffage == InstallationChauffageEnum.CHAUDIERE_AVEC_PAC){
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                            } else if (installationChauffage == InstallationChauffageEnum.CHAUDIERE_AVEC_PAC_ET_INSERT_BOIS){
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                            }
                            this.manageLayoutFuncInstallationChauffage(layout);
                            this.actualisePrs1();
                            this.actualiseI0();
                            this.actualiseCch();
                            this.actualiseRendementEcs();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            InstallationChauffageEnum type = installationChauffage;
                            HashMap<String, Object> currentItems = new HashMap<String, Object>();
                            currentItems.put("lastValue", type);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.INSTALLATION_CHAUFFAGE, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);

                            // wait for layout to be  populated

                            while (!layout.isInitialised()) {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException ie) {

                                }
                            }
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), true);
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), false);
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), false);
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), false);
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), false);
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                            ((TabWindow) layout.getFromId("tab_window")).setPosition((((TabWindow) layout.getFromId("tab_window"))).getPrefWidth()/2, Gdx.graphics.getHeight() - ((TabWindow) layout.getFromId("tab_window")).getPrefHeight()/2);
                        }
                        break;
                    }

                    case CHAUFFAGE_UNIQUE :{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        Layout layout = (Layout) items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            generateurChauffageUnique = (Chauffage.Generateur) items.get("lastValue");
                            chauffageUnique=new Chauffage(generateurChauffageUnique,localEquipementEcs.getBoolean(),
                                    presenceRobinetThermostatique.getBoolean(),typeEmetteurDeChaleur);

                            String nameGenerateur = chauffageUnique.getGenerateur().toString();
                            if (nameGenerateur.startsWith("Chaudière")){ // Si le générateur est une chaudière mais pas une chaudière électrique
                                if (installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                                        && generateurChauffageUnique != Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                                    chaudiereAssureChauffageEtEcs=true;
                                    chaudiereEcs=chauffageUnique;
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                                }else if (installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                                        && generateurChauffageUnique == Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                                    chaudiereAssureChauffageEtEcs=false;
                                    installationEcs=TypeEquipementEcsEnum.BALLON_ELECTRIQUE_HORIZONTAL_SUP_15ANS;
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                                }else if (! installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                                        && generateurChauffageUnique != Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                                    chaudiereAssureChauffageEtEcs=true;
                                    chaudiereEcs=chauffageUnique;
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                                }else if (! installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                                        && generateurChauffageUnique == Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                                    chaudiereAssureChauffageEtEcs=false;
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                                }
                            }else{
                                chaudiereAssureChauffageEtEcs=false;
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                                if(installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)){
                                    installationEcs=TypeEquipementEcsEnum.BALLON_ELECTRIQUE_HORIZONTAL_SUP_15ANS;
                                }
                            }
                            this.actualisePrs1();
                            this.actualiseRendementsChauffage(chauffageUnique);
                            this.actualiseI0();
                            this.actualiseCch();
                            this.actualiseRendementEcs();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage.Generateur type = generateurChauffageUnique;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CHAUFFAGE_UNIQUE, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case CHAUFFAGE_SANS_POIL: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        Layout layout = (Layout) items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            generateurChauffageAvecPoil = (Chauffage.Generateur) items.get("lastValue");
                            chauffageAvecPoil = new Chauffage(generateurChauffageAvecPoil,localEquipementEcs.getBoolean(),
                                    presenceRobinetThermostatique.getBoolean(),typeEmetteurDeChaleur);

                            String nameGenerateur = chauffageAvecPoil.getGenerateur().toString();
                            if (nameGenerateur.startsWith("Chaudière")){ // Si le générateur est une chaudière
                                if (installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                                        && generateurChauffageAvecPoil != Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                                    chaudiereAssureChauffageEtEcs=true;
                                    chaudiereEcs=chauffageAvecPoil;
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                                }else if (installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                                        && generateurChauffageAvecPoil == Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                                    chaudiereAssureChauffageEtEcs=false;
                                    installationEcs=TypeEquipementEcsEnum.BALLON_ELECTRIQUE_HORIZONTAL_SUP_15ANS;
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                                }else if (! installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                                        && generateurChauffageAvecPoil != Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                                    chaudiereAssureChauffageEtEcs=true;
                                    chaudiereEcs=chauffageAvecPoil;
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                                }else if (! installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                                        && generateurChauffageAvecPoil == Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                                    chaudiereAssureChauffageEtEcs=false;
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                                }
                            }else{
                                chaudiereAssureChauffageEtEcs=false;
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                                if(installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)){
                                    installationEcs=TypeEquipementEcsEnum.BALLON_ELECTRIQUE_HORIZONTAL_SUP_15ANS;
                                }
                            }
                            this.actualisePrs1();
                            this.actualiseI0();
                            this.actualiseCch();
                            this.actualiseRendementEcs();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage.Generateur type = generateurChauffageAvecPoil;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CHAUFFAGE_SANS_POIL, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case POELE_OU_INSERT_AVEC_CHAUFFAGE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            generateurPoeleAvecChauffage = (Chauffage.Generateur) items.get("lastValue");
                            poeleAvecChauffage = new Chauffage(generateurPoeleAvecChauffage,localEquipementEcs.getBoolean(),
                                    presenceRobinetThermostatique.getBoolean(),typeEmetteurDeChaleur);
                            this.actualiseRendementsChauffage(poeleAvecChauffage);
                            this.actualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage.Generateur type = generateurPoeleAvecChauffage;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUFFAGE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            frequenceUtilisationPoeleAvecChauffage = (FrequenceUtilisationPoilEnum) items.get("lastValue");
                            this.actualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            FrequenceUtilisationPoilEnum type = frequenceUtilisationPoeleAvecChauffage;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUFFAGE, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            frequenceUtilisationPoeleAvecChauffageEtPac = (FrequenceUtilisationPoilEnum) items.get("lastValue");
                            this.actualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            FrequenceUtilisationPoilEnum type = frequenceUtilisationPoeleAvecChauffageEtPac;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case CHAUDIERE_GAZ_FIOUL:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            generateurChaudiereGaz = (Chauffage.Generateur) items.get("lastValue");
                            chaudiereGaz = new Chauffage(generateurChaudiereGaz,localEquipementEcs.getBoolean(),
                                    presenceRobinetThermostatique.getBoolean(),typeEmetteurDeChaleur);

                            if (installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)){
                                chaudiereAssureChauffageEtEcs=true;
                                chaudiereEcs=chaudiereGaz;
                            }else{
                                chaudiereAssureChauffageEtEcs=false;
                            }
                            this.actualisePrs1();
                            this.actualiseRendementsChauffage(chaudiereGaz);
                            this.actualiseI0();
                            this.actualiseCch();
                            this.actualiseRendementEcs();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage.Generateur type = generateurChaudiereGaz;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CHAUDIERE_GAZ_FIOUL, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case CHAUDIERE_BOIS:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            generateurChaudiereBois = (Chauffage.Generateur) items.get("lastValue");
                            chaudiereBois = new Chauffage(generateurChaudiereBois,localEquipementEcs.getBoolean(),
                                    presenceRobinetThermostatique.getBoolean(),typeEmetteurDeChaleur);
                            this.actualiseRendementsChauffage(chaudiereBois);
                            this.actualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage.Generateur type = generateurChaudiereBois;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CHAUDIERE_BOIS, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case CHAUDIERE_AVEC_PAC:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        Layout layout = (Layout)items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            generateurChaudiereAvecPac = (Chauffage.Generateur) items.get("lastValue");
                            chaudiereAvecPac = new Chauffage(generateurChaudiereAvecPac,localEquipementEcs.getBoolean(),
                                    presenceRobinetThermostatique.getBoolean(),typeEmetteurDeChaleur);

                            if (installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                                    && generateurChaudiereAvecPac != Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                                chaudiereAssureChauffageEtEcs=true;
                                chaudiereEcs=chaudiereAvecPac;
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                            }else if (installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                                    && generateurChaudiereAvecPac == Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                                chaudiereAssureChauffageEtEcs=false;
                                installationEcs=TypeEquipementEcsEnum.BALLON_ELECTRIQUE_HORIZONTAL_SUP_15ANS;
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                            }else if (! installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                                    && generateurChaudiereAvecPac != Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                                chaudiereAssureChauffageEtEcs=true;
                                chaudiereEcs=chaudiereAvecPac;
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                            }else if (! installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                                    && generateurChaudiereAvecPac == Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                                chaudiereAssureChauffageEtEcs=false;
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                            }
                            this.actualisePrs1();
                            this.actualiseRendementsChauffage(chaudiereAvecPac);
                            this.actualiseI0();
                            this.actualiseCch();
                            this.actualiseRendementEcs();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage.Generateur type = generateurChaudiereAvecPac;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CHAUDIERE_AVEC_PAC, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case POMPE_A_CHALEUR_AVEC_CHAUDIERE :{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            generateurPacAvecChaudiere = (Chauffage.Generateur) items.get("lastValue");
                            pacAvecChaudiere = new Chauffage(generateurPacAvecChaudiere,localEquipementEcs.getBoolean(),
                                    presenceRobinetThermostatique.getBoolean(),typeEmetteurDeChaleur);
                            this.actualiseRendementsChauffage(pacAvecChaudiere);
                            this.actualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage.Generateur type = generateurPacAvecChaudiere;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case CHAUDIERE_AVEC_PAC_ET_POELE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        Layout layout = (Layout)items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            generateurChaudiereAvecPacEtPoil = (Chauffage.Generateur) items.get("lastValue");
                            chaudiereAvecPacEtPoil = new Chauffage(generateurChaudiereAvecPacEtPoil,localEquipementEcs.getBoolean(),
                                    presenceRobinetThermostatique.getBoolean(),typeEmetteurDeChaleur);

                            if (installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                                    && generateurChaudiereAvecPacEtPoil != Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                                chaudiereAssureChauffageEtEcs=true;
                                chaudiereEcs=chaudiereAvecPacEtPoil;
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                            }else if (installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                                    && generateurChaudiereAvecPacEtPoil == Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                                chaudiereAssureChauffageEtEcs=false;
                                installationEcs=TypeEquipementEcsEnum.BALLON_ELECTRIQUE_HORIZONTAL_SUP_15ANS;
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                            }else if (! installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                                    && generateurChaudiereAvecPacEtPoil != Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                                chaudiereAssureChauffageEtEcs=true;
                                chaudiereEcs=chaudiereAvecPacEtPoil;
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                            }else if (! installationEcs.equals(TypeEquipementEcsEnum.CHAUDIERE)
                                    && generateurChaudiereAvecPacEtPoil == Chauffage.Generateur.CHAUDIERE_ELECTRIQUE){
                                chaudiereAssureChauffageEtEcs=false;
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                            }
                            this.actualisePrs1();
                            this.actualiseRendementsChauffage(chaudiereAvecPacEtPoil);
                            this.actualiseI0();
                            this.actualiseCch();
                            this.actualiseRendementEcs();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage.Generateur type = generateurChaudiereAvecPacEtPoil;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE :{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            generateurPacAvecChaudiereEtPoele = (Chauffage.Generateur) items.get("lastValue");
                            pacAvecChaudiereEtPoele = new Chauffage(generateurPacAvecChaudiereEtPoele,localEquipementEcs.getBoolean(),
                                    presenceRobinetThermostatique.getBoolean(),typeEmetteurDeChaleur);
                            this.actualiseRendementsChauffage(pacAvecChaudiereEtPoele);
                            this.actualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage.Generateur type = generateurPacAvecChaudiereEtPoele;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            generateurPoeleAvecChaudiereEtPac = (Chauffage.Generateur) items.get("lastValue");
                            poeleAvecChaudiereEtPac = new Chauffage(generateurPoeleAvecChaudiereEtPac,localEquipementEcs.getBoolean(),
                                    presenceRobinetThermostatique.getBoolean(),typeEmetteurDeChaleur);
                            this.actualiseRendementsChauffage(poeleAvecChaudiereEtPac);
                            this.actualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage.Generateur type = generateurPoeleAvecChaudiereEtPac;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TEMPERATURE_INTERIEUR:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            tInt = (TemperatureInterieurEnum) items.get("lastValue");
                            this.actualiseqVinf();
                            this.actualisedhCor();
                            this.actualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TemperatureInterieurEnum type = tInt;
                            if (chauffage_properties.containsKey(DpeEvent.TEMPERATURE_INTERIEUR)){
                                type = (TemperatureInterieurEnum) chauffage_properties.get(DpeEvent.TEMPERATURE_INTERIEUR);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TEMPERATURE_INTERIEUR, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_EMETTEUR_DE_CHALEUR:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            typeEmetteurDeChaleur = (Chauffage.Emission) items.get("lastValue");
                            this.actualiseEmetteurAllGenerateurs();
                            this.actualiseI0();
                            this.actualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage.Emission type = typeEmetteurDeChaleur;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            presenceThermostat = (PresenceThermostatEnum) items.get("lastValue");
                            this.actualiseI0();
                            this.actualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            PresenceThermostatEnum type = presenceThermostat;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case PRESENCE_ROBINET_THERMOSTATIQUE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            presenceRobinetThermostatique = (PresenceRobinetEnum) items.get("lastValue");
                            this.actualiseRobinetAllGenerateurs();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            PresenceRobinetEnum type = presenceRobinetThermostatique;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.PRESENCE_ROBINET_THERMOSTATIQUE, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case SYSTEME_PROGRAMMABLE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            programmationSysteme = (ProgrammationSystemeEnum) items.get("lastValue");
                            this.actualiseI0();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            ProgrammationSystemeEnum type = programmationSysteme;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.SYSTEME_PROGRAMMABLE, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_EQUIPEMENT_ECS:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            installationEcs = (TypeEquipementEcsEnum) items.get("lastValue");
                            if(installationEcs == TypeEquipementEcsEnum.CHAUDIERE){
                                chaudiereAssureChauffageEtEcs=true;
                                switch(installationChauffage){
                                    case CHAUFFAGE_UNIQUE:
                                        chaudiereEcs=chauffageUnique;
                                        break;
                                    case CHAUFFAGE_AVEC_POIL_OU_INSERT_BOIS:
                                        chaudiereEcs=chauffageAvecPoil;
                                        break;
                                    case CHAUDIERE_GAZ_OU_FIOUL_AVEC_CHAUDIERE_BOIS:
                                        chaudiereEcs=chaudiereGaz;
                                        break;
                                    case CHAUDIERE_AVEC_PAC:
                                        chaudiereEcs=chaudiereAvecPac;
                                        break;
                                    case CHAUDIERE_AVEC_PAC_ET_INSERT_BOIS:
                                        chaudiereEcs=chaudiereAvecPacEtPoil;
                                        break;
                                }
                            }else{
                                chaudiereAssureChauffageEtEcs=false;
                            }
                            this.actualisePrs2();
                            this.actualiseRendementEcs();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeEquipementEcsEnum type = installationEcs;
                            HashMap<String, Object> currentItems = new HashMap<String, Object>();
                            currentItems.put("lastValue", type);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_EQUIPEMENT_ECS, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case PRESENCE_INSTALLATION_SOLAIRE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            presenceInstallationSolaire = (PresenceInstallationSolaireEnum) items.get("lastValue");
                            this.actualiseFacteurSolaire();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            PresenceInstallationSolaireEnum type = presenceInstallationSolaire;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.PRESENCE_INSTALLATION_SOLAIRE, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case LOCAL_EQUIPEMENT_ECS:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            localEquipementEcs= (LocalEquipementEcsEnum) items.get("lastValue");
                            this.actualiseLocalAllGenerateurs();
                            this.actualisePrs2();
                            this.actualiseRendementEcs();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            LocalEquipementEcsEnum type = localEquipementEcs;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.LOCAL_EQUIPEMENT_ECS, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case USAGE_EAU_CHAUDE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            usageEauChaude = (UsageEauChaudeEnum) items.get("lastValue");
                            this.actualisebecs();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            UsageEauChaudeEnum type = usageEauChaude;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.USAGE_EAU_CHAUDE, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case DECLENCHEMENT_CHAUDIERE_ROBINET:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            declenchementChaudiere= (DeclenchementChaudiereEnum) items.get("lastValue");
                            this.actualiseCch();
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            DeclenchementChaudiereEnum type = declenchementChaudiere;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.DECLENCHEMENT_CHAUDIERE_ROBINET, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_MUR: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        Mur mur = (Mur)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeMurEnum typeMur = (TypeMurEnum)items.get("lastValue");
                            mur.setTypeMur(typeMur);
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            TypeMurEnum type = mur.getTypeMur();
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", mur);
                            Event e2 = new Event(DpeEvent.TYPE_MUR, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case DATE_ISOLATION_MUR: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        Mur mur = (Mur)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            DateIsolationMurEnum dateIsolationMur = (DateIsolationMurEnum)items.get("lastValue");
                            mur.setDateIsolationMurEnum(dateIsolationMur);
                            actualiseCoeffDeperditionThermique(mur);
                            ArrayList<Ouverture> ouverturesMur = mur.getOuvertures();
                            if (!ouverturesMur.isEmpty()){
                                for(Ouverture actualOuverture : ouverturesMur){
                                    if (actualOuverture instanceof Fenetre){
                                        ((Fenetre) actualOuverture).actualiseDeperdition();
                                    }else if (actualOuverture instanceof PorteFenetre){
                                        ((PorteFenetre) actualOuverture).actualiseDeperdition();
                                    }else if (actualOuverture instanceof Porte){
                                        ((Porte) actualOuverture).actualiseDeperdition();
                                    }
                                }
                            }
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            DateIsolationMurEnum type = mur.getDateIsolationMurEnum();
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", mur);
                            Event e2 = new Event(DpeEvent.DATE_ISOLATION_MUR, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_ISOLATION_MUR: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        Mur mur = (Mur)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeIsolationMurEnum typeIsolationMur = (TypeIsolationMurEnum) items.get("lastValue");
                            mur.setTypeIsolationMurEnum(typeIsolationMur);
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            TypeIsolationMurEnum type = mur.getTypeIsolationMurEnum();
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", mur);
                            Event e2 = new Event(DpeEvent.TYPE_ISOLATION_MUR, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case ORIENTATION_MUR: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        Mur mur = (Mur)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            OrientationEnum orientationMur = (OrientationEnum) items.get("lastValue");
                            mur.setOrientationMur(orientationMur);
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            OrientationEnum type = mur.getOrientationMur();
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", mur);
                            Event e2 = new Event(DpeEvent.ORIENTATION_MUR, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case MITOYENNETE_PLANCHER: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        Slab plancher = (Slab)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            MitoyennetePlancher mitoyennetePlancher = (MitoyennetePlancher)items.get("lastValue");
                            plancher.setMitoyennetePlancher(mitoyennetePlancher);
                            actualiseCoeffDeperditionThermique(plancher, false, true);
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            MitoyennetePlancher type = plancher.getMitoyennetePlancher();
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", plancher);
                            Event e2 = new Event(DpeEvent.MITOYENNETE_PLANCHER, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case MITOYENNETE_PLAFOND: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        Slab plafond = (Slab)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            MitoyennetePlafond mitoyennetePlafond = (MitoyennetePlafond)items.get("lastValue");
                            plafond.setMitoyennetePlafond(mitoyennetePlafond);
                            actualiseCoeffDeperditionThermique(plafond, true, false);
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            MitoyennetePlafond type = plafond.getMitoyennetePlafond();
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", plafond);
                            Event e2 = new Event(DpeEvent.MITOYENNETE_PLAFOND, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case DATE_ISOLATION_PLANCHER: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        Slab plancher = (Slab)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            DateIsolationSlab dateIsolationPlancher = (DateIsolationSlab)items.get("lastValue");
                            plancher.setDateIsolationPlancher(dateIsolationPlancher);
                            actualiseCoeffDeperditionThermique(plancher,false,true);
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            DateIsolationSlab type = plancher.getDateIsolationPlancher();
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", plancher);
                            Event e2 = new Event(DpeEvent.DATE_ISOLATION_PLANCHER, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case DATE_ISOLATION_PLAFOND: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        Slab plancher = (Slab)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            DateIsolationSlab dateIsolationPlafond = (DateIsolationSlab)items.get("lastValue");
                            plancher.setDateIsolationPlafond(dateIsolationPlafond);
                            actualiseCoeffDeperditionThermique(plancher,true,false);
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            DateIsolationSlab type = plancher.getDateIsolationPlafond();
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", plancher);
                            Event e2 = new Event(DpeEvent.DATE_ISOLATION_PLAFOND, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_FENETRE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        Fenetre fenetre = (Fenetre)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeFenetre typeMenuiserie = (TypeFenetre)items.get("lastValue");

                        } else if (eventRequest == EventRequest.GET_STATE) {
                            TypeFenetre type = fenetre.getTypeFenetre();
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", fenetre);
                            Event e2 = new Event(DpeEvent.TYPE_FENETRE, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_MATERIAU_MENUISERIE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        Fenetre fenetre = (Fenetre)items.get("userObject");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeMateriauMenuiserieEnum typeMateriauMenuiserie = (TypeMateriauMenuiserieEnum)items.get("lastValue");
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            TypeMateriauMenuiserieEnum type = fenetre.getTypeMateriau();
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", fenetre);
                            Event e2 = new Event(DpeEvent.TYPE_MATERIAU_MENUISERIE, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_VITRAGE_MENUISERIE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if(items.get("userObject") instanceof Fenetre){
                            Fenetre fenetre = (Fenetre)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeVitrageEnum typeVitrage = (TypeVitrageEnum)items.get("lastValue");
                                fenetre.setTypeVitrage(typeVitrage);
                                this.actualiseNbFenetreSvEtDv();
                                this.actualiseSse();
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeVitrageEnum type = fenetre.getTypeVitrage();
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", fenetre);
                                Event e2 = new Event(DpeEvent.TYPE_VITRAGE_MENUISERIE, currentItems);
                                if (!isFake)
                                    EventManager.getInstance().put(Channel.DPE, e2);
                            }
                        }else if (items.get("userObject") instanceof PorteFenetre){
                            PorteFenetre porteFenetre = (PorteFenetre)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeVitrageEnum typeVitrage = (TypeVitrageEnum)items.get("lastValue");
                                porteFenetre.setTypeVitrage(typeVitrage);
                                this.actualiseSse();
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeVitrageEnum type = porteFenetre.getTypeVitrage();
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", porteFenetre);
                                Event e2 = new Event(DpeEvent.TYPE_VITRAGE_MENUISERIE, currentItems);
                                if (!isFake)
                                    EventManager.getInstance().put(Channel.DPE, e2);
                            }
                        }
                        break;
                    }

                    case TYPE_FERMETURE_MENUISERIE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (items.get("userObject") instanceof Fenetre){
                            Fenetre fenetre = (Fenetre)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeFermetureEnum typeFermeture = (TypeFermetureEnum)items.get("lastValue");
                                fenetre.setTypeFermeture(typeFermeture);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeFermetureEnum type = fenetre.getTypeFermeture();
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", fenetre);
                                Event e2 = new Event(DpeEvent.TYPE_FERMETURE_MENUISERIE, currentItems);
                                if (!isFake)
                                    EventManager.getInstance().put(Channel.DPE, e2);
                            }
                        }else if (items.get("userObject") instanceof PorteFenetre){
                            PorteFenetre porteFenetre = (PorteFenetre)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeFermetureEnum typeFermeture = (TypeFermetureEnum)items.get("lastValue");
                                porteFenetre.setTypeFermeture(typeFermeture);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeFermetureEnum type = porteFenetre.getTypeFermeture();
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", porteFenetre);
                                Event e2 = new Event(DpeEvent.TYPE_FERMETURE_MENUISERIE, currentItems);
                                if (!isFake)
                                    EventManager.getInstance().put(Channel.DPE, e2);
                            }
                        }
                        break;
                    }

                    case MASQUE_PROCHE_MENUISERIE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if(items.get("userObject") instanceof Fenetre){
                            Fenetre fenetre = (Fenetre)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMasqueEnum masque = (TypeMasqueEnum)items.get("lastValue");
                                fenetre.setMasqueProche(masque);
                                this.actualiseSse();
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMasqueEnum type = fenetre.getMasqueProche();
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", fenetre);
                                Event e2 = new Event(DpeEvent.MASQUE_PROCHE_MENUISERIE, currentItems);
                                if (!isFake)
                                    EventManager.getInstance().put(Channel.DPE, e2);
                            }
                        }else if(items.get("userObject") instanceof PorteFenetre){
                            PorteFenetre porteFenetre = (PorteFenetre)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMasqueEnum masque = (TypeMasqueEnum)items.get("lastValue");
                                porteFenetre.setMasqueProche(masque);
                                this.actualiseSse();
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMasqueEnum type = porteFenetre.getMasqueProche();
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", porteFenetre);
                                Event e2 = new Event(DpeEvent.MASQUE_PROCHE_MENUISERIE, currentItems);
                                if (!isFake)
                                    EventManager.getInstance().put(Channel.DPE, e2);
                            }
                        }
                        break;
                    }

                    case MASQUE_LOINTAIN_MENUISERIE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (items.get("userObject") instanceof Fenetre){
                            Fenetre fenetre = (Fenetre)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMasqueEnum masque = (TypeMasqueEnum)items.get("lastValue");
                                fenetre.setMasqueLointain(masque);
                                this.actualiseSse();
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMasqueEnum type = fenetre.getMasqueLointain();
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", fenetre);
                                Event e2 = new Event(DpeEvent.MASQUE_LOINTAIN_MENUISERIE, currentItems);
                                if (!isFake)
                                    EventManager.getInstance().put(Channel.DPE, e2);
                            }
                        }else if (items.get("userObject") instanceof PorteFenetre){
                            PorteFenetre porteFenetre = (PorteFenetre)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMasqueEnum masque = (TypeMasqueEnum)items.get("lastValue");
                                porteFenetre.setMasqueLointain(masque);
                                this.actualiseSse();
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMasqueEnum type = porteFenetre.getMasqueLointain();
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", porteFenetre);
                                Event e2 = new Event(DpeEvent.MASQUE_LOINTAIN_MENUISERIE, currentItems);
                                EventManager.getInstance().put (Channel.DPE, e2);
                            }
                        }
                        break;
                    }

                    case TYPE_PORTE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        Porte porte = (Porte)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        Layout layout = (Layout)items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypePorte typeDoor = (TypePorte)items.get("lastValue");
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            TypePorte type = porte.getTypePorte();
                            HashMap<String, Object> currentItems = new HashMap<String, Object>();
                            currentItems.put("lastValue", type);
                            currentItems.put("userObject", porte);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_PORTE, currentItems);
                            if (!isFake)
                                EventManager.getInstance().put(Channel.DPE, e2);

                            // wait for layout to be  populated

                            while (!layout.isInitialised()) {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException ie) {

                                }
                            }
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("vitrage_et_fermeture"), false);
                        }
                        break;
                    }
                    case MUR_AJOUTE: {
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        Mur mur = (Mur) items.get("userObject");
                        this.actualiseCoeffDeperditionThermique(mur);
                        this.actualisePerimetreBatiment();
                        this.actualiseSdepMurs();
                        break;
                    }
                    case SIZE_MUR_CHANGED:{
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        Mur mur = (Mur) items.get("userObject");
                        mur.actualiseDeperdition();
                        this.actualisePerimetreBatiment();
                        this.actualiseSdepMurs();
                        break;
                    }
                    case MUR_REMOVED:{
                        this.actualiseDpMur();
                        this.actualisePerimetreBatiment();
                        this.actualiseSdepMurs();
                        break;
                    }
                    case DEPERDITION_MURS_CHANGED : {
                        this.actualiseDpMur();
                        this.actualiseSdepMurs();
                        break;
                    }
                    case MITOYENNETE_MUR_CHANGEE: {
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        Mur mur = (Mur) items.get("userObject");
                        this.actualiseCoeffDeperditionThermique(mur);
                        ArrayList<Ouverture> ouverturesMur = mur.getOuvertures();
                        if (!ouverturesMur.isEmpty()){
                            for(Ouverture actualOuverture : ouverturesMur){
                                if (actualOuverture instanceof Fenetre){
                                    ((Fenetre) actualOuverture).actualiseDeperdition();
                                }else if (actualOuverture instanceof PorteFenetre){
                                    ((PorteFenetre) actualOuverture).actualiseDeperdition();
                                }else if (actualOuverture instanceof Porte){
                                    ((Porte) actualOuverture).actualiseDeperdition();
                                }
                            }
                        }
                        break;
                    }
                    case SLAB_AJOUTE: {
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        Slab slab = (Slab) items.get("userObject");
                        slab.actualiseSurface();
                        this.actualiseCoeffDeperditionThermique(slab,true,true);
                        this.actualiseSH();
                        this.actualiseSdepToits();
                        this.analyseSuperpositionSlab(slab);
                        break;
                    }
                    case SLAB_REMOVED:{
                        this.actualiseDpPlancher();
                        this.actualiseDpToit();
                        this.actualiseSH();
                        this.actualiseSdepToits();
                        break;
                    }
                    case FENETRE_AJOUTEE: {
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        Fenetre fenetre = (Fenetre) items.get("userObject");
                        fenetreList.add(fenetre);
                        this.actualiseNbFenetreSvEtDv();
                        fenetre.actualiseCoeffTransmissionThermiqueFenetre();
                        fenetre.actualiseBas();
                        fenetre.actualiseC1();
                        fenetre.actualiseFts();
                        fenetre.getMur().actualiseSurface();
                        this.actualiseSse();
                        break;
                    }
                    case PORTE_FENETRE_AJOUTEE: {
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        PorteFenetre porteFenetre = (PorteFenetre) items.get("userObject");
                        porteFenetreList.add(porteFenetre);
                        porteFenetre.actualiseCoeffTransmissionThermique();
                        porteFenetre.actualiseBas();
                        porteFenetre.actualiseC1();
                        porteFenetre.actualiseFts();
                        porteFenetre.getMur().actualiseSurface();
                        this.actualiseSse();
                        break;
                    }
                    case PORTE_AJOUTE: {
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        Porte porte = (Porte) items.get("userObject");
                        porteList.add(porte);
                        porte.actualiseCoefficientDeTransmissionThermique();
                        porte.getMur().actualiseSurface();
                        break;
                    }
                    case ORIENTATION_GLOBALE_CHANGEE: {
                        //*** Chaine de traitement relative aux fenetres ***//
                        if (!fenetreList.isEmpty()) {
                            for (Fenetre actualFenetre : fenetreList) {
                                actualFenetre.actualiseDeperdition();
                                actualFenetre.actualiseBas();
                                actualFenetre.actualiseC1();
                                actualFenetre.actualiseFe2();
                            }
                        }
                        //*** Chaine de traitement relative aux porteFenetre ***//
                        if (!porteFenetreList.isEmpty()) {
                            for (PorteFenetre actualPorteFenetre : porteFenetreList) {
                                actualPorteFenetre.actualiseDeperdition();
                                actualPorteFenetre.actualiseBas();
                                actualPorteFenetre.actualiseC1();
                                actualPorteFenetre.actualiseFe2();
                            }
                        }
                        this.actualiseSse();
                        break;
                    }
                    case DEPERDITION_TOITS_CHANGED : {
                        this.actualiseDpToit();
                        this.actualiseSdepToits();
                        break;
                    }
                    case DEPERDITION_PLANCHERS_CHANGED : {
                        this.actualiseDpPlancher();
                        break;
                    }
                    case DEPERDITION_FENETRES_CHANGED : {
                        this.actualiseDpFenetre();
                        break;
                    }
                    case DEPERDITION_PORTES_CHANGED : {
                        this.actualiseDpPorte();
                        break;
                    }
                    case DEPERDITION_PORTES_FENETRES_CHANGED : {
                        this.actualiseDpPorteFenetre();
                        break;
                    }
                    case OUVERTURE_REMOVED:{
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        Ouverture ouverture = (Ouverture) items.get("userObject");
                        if (ouverture instanceof Fenetre){
                            synchronized (fenetreList) {
                                this.fenetreList.remove(ouverture);
                                this.actualiseNbFenetreSvEtDv();
                                this.actualiseDpFenetre();
                            }
                        }else if (ouverture instanceof Porte){
                            synchronized (porteList) {
                                this.porteList.remove(ouverture);
                                this.actualiseDpPorte();
                            }
                        }else if (ouverture instanceof PorteFenetre){
                                synchronized (porteFenetreList) {
                                    this.porteFenetreList.remove(ouverture);
                                    this.actualiseDpPorteFenetre();
                                }
                        }
                        break;
                    }
                    case SURFACE_CHANGED:{
                        this.actualiseSdepMurs();
                        this.actualiseSdepToits();
                        this.actualiseDpMur();
                        this.actualiseDpPlancher();
                        this.actualiseDpToit();
                        this.actualiseSH();
                        break;
                    }
                }
            }
        }
    }
}
