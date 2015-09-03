package fr.limsi.rorqual.core.dpe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.limsi.rorqual.core.dpe.enums.DpeChange;
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

public class Dpe implements EventListener {

    private HashMap<EventType,Object> general_properties = new HashMap<EventType,Object>();
    private HashMap<EventType,Object> chauffage_properties = new HashMap<EventType,Object>();
    private HashMap<EventType,Object> ecs_properties = new HashMap<EventType,Object>();

    /*** Attributs liés au calcul du DPE ***/

    // 0.Variables générales
    private float sh = 0;
    private float per;
    private float scoreDpe = 700;
    private DateConstructionBatimentEnum dateConstructionBatiment = DateConstructionBatimentEnum.AVANT_1975; // Initialisation défavorable
    private TypeEnergieConstructionEnum typeEnergieConstruction = TypeEnergieConstructionEnum.AUTRE; // Initialisation défavorable
    private DepartementBatimentEnum departementBatiment = DepartementBatimentEnum.AIN; // Choix par défaut
    public void actualiseSH(){
        float tampon=0;
        for (Slab s:slabList){
            tampon+=s.getSurface();
        }
        this.sh=tampon;
        this.actualiseAi();
    }

    // 1.Expression du besoin de chauffage
    private float bv=100;
    private float gv; //TODO : trouver le GV défavorable en faisant plusieurs simulations ...

    // 2.Calcul des déperditions de l'enveloppe GV
    private float dpMur;
    private float dpToit;
    private float dpPlancher;
    private float dpPorte;
    private float dpFenetre;
    public void actualiseGV(float val){
        this.gv += val;
    } // TODO : mettre en place les changements qui découlent de cette actualisation ...
    ///*** Murs ***///
    private List<Mur> murList=new ArrayList<Mur>();
    private HashMap<Mur,HashMap<DpeChange,Boolean>> murChangeSaveList = new HashMap<Mur,HashMap<DpeChange,Boolean>>();
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
    private List<Slab> slabList=new ArrayList<Slab>();
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
                int tampon = (int)Math.round(2 * slab.getSurface() / per);
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
    ///*** Fenêtres ***///
    private List<Fenetre> fenetreList=new ArrayList<Fenetre>();
    private HashMap<Fenetre,HashMap<DpeChange,Boolean>> fenetreChangeSaveList = new HashMap<Fenetre,HashMap<DpeChange,Boolean>>();
    ///*** Portes-fenêtres ***///
    private List<PorteFenetre> porteFenetreList=new ArrayList<PorteFenetre>();
    private HashMap<PorteFenetre,HashMap<DpeChange,Boolean>> porteFenetreChangeSaveList = new HashMap<PorteFenetre,HashMap<DpeChange,Boolean>>();
    ///*** Portes ***///
    private List<Porte> porteList=new ArrayList<Porte>();
    private HashMap<Porte,HashMap<DpeChange,Boolean>> porteChangeSaveList = new HashMap<Porte,HashMap<DpeChange,Boolean>>();

    // 2.4. Calcul des ponts thermiques

    // 2.5. Calcul des déperditions par renouvellement d'air
    private float renouvellementAir, hVent,hPerm,qVarep=2.145f,qVinf,q4pa,q4paEnv,q4paConv=2,smea=4,sDep,nbFenetreSV,nbFenetreDV;
    private TypeVentilationEnum typeVentilation=TypeVentilationEnum.INCONNUE; // Initialisation logique
    private TemperatureInterieurEnum tInt=TemperatureInterieurEnum.ENTRE_22_ET_23; // Initialisation défavorable
    public void actualiseRenouvellementAir(){
        renouvellementAir=hVent+hPerm;
    }
    public void actualiseHvent(){
        hVent=0.34f*qVarep;
    }
    public void actualiseHperm(){
        hPerm=0.34f*qVinf;
    }
    public void actualiseqVinf(){
        float val = 0.7f*(tInt.getTemperatureInterieure()-6.58f);
        qVinf=0.0146f*q4pa*(float)Math.pow(val,0.667);
    }
    public void actualiseQ4pa(){
        q4pa=q4paEnv+0.45f*smea*sh;
    }
    public void actualiseQ4paEnv(){
        q4paEnv=q4paConv*sDep;
    }
    public void actualiseQ4paConv(){
        if(nbFenetreSV>=nbFenetreDV){
            q4paConv=2.0f;
        }else{
            q4paConv=1.7f;
        }
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
    }
    public void actualiseX(){
        x=(aI+aS)/(gv*dhCor);
    }
    public void actualisedhCor(){
        dhCor=departementBatiment.getDhref()+getKdh()*departementBatiment.getNref();
    }
    public void actualiseAi(){
        aI=4.17f*sh*departementBatiment.getNref();
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
    private float i0 = 1; // Cas le plus défavorable (cf partie 3)
    private float g = 72; // (9000/2.5*50)
    public void actualiseIntermittence(){
        intermittence=i0/(1+0.1f*(g-1));
        this.actualiseBch();
    }
    public void actualiseG(){
        g=gv/(2.5f* sh);
        this.actualiseIntermittence();
    } // TODO : prendre en compte le changement sur gv
    public void actualiseI0(Chauffage chauffagePrincipal){ // On besoin du chauffage en paramètre afin de déterminer le type (divisé ou central)
        if (general_properties.get(DpeEvent.TYPE_BATIMENT).equals(TypeBatimentEnum.MAISON)){ // Maison
            if(chauffagePrincipal.getType().equals(Chauffage.Type.DIVISE)){ // le chauffage est de type divisé
                if (chauffage_properties.get(DpeEvent.SYSTEME_PROGRAMMABLE).equals(ProgrammationSystemeEnum.POSSIBLE)){ // Systeme programmable
                    i0=0.84f;
                }else{ // Systeme non programmable
                    i0 = 0.86f;
                }
            }else{ // le chauffage est de type central
                if (chauffage_properties.get(DpeEvent.PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR).equals(PresenceThermostatEnum.AUCUN_DES_DEUX)){ // Sans régulation par pièce
                    if(chauffage_properties.get(DpeEvent.SYSTEME_PROGRAMMABLE).equals(ProgrammationSystemeEnum.POSSIBLE)) { // Systeme programmable
                        i0 = 0.9f;
                    }else{ // Systeme non programmable
                        if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                            i0 = 0.91f;
                        }else if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.RADIATEUR)){ // Radiateur
                            i0 = 0.93f;
                        }else{ // Plancher chauffant ou système mixte
                            i0 = 0.94f;
                        }
                    }
                }else{ // Avec régulation pièce par pièce
                    if(chauffage_properties.get(DpeEvent.SYSTEME_PROGRAMMABLE).equals(ProgrammationSystemeEnum.POSSIBLE)) { // Systeme programmable
                        i0 = 0.87f;
                    }else{ // Systeme non programmable
                        if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                            i0 = 0.88f;
                        }else if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.RADIATEUR)){ // Radiateur
                            i0 = 0.9f;
                        }else{ // Plancher chauffant ou système mixte
                            i0 = 0.92f;
                        }
                    }
                }
            }
        }else{ //Appartement
            if(chauffagePrincipal.getType().equals(Chauffage.Type.DIVISE)){ // le chauffage est de type divisé
                if (chauffage_properties.get(DpeEvent.SYSTEME_PROGRAMMABLE).equals(ProgrammationSystemeEnum.POSSIBLE)){ // Systeme programmable
                    if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                        i0 = 0.88f;
                    }else if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.RADIATEUR)){ // Radiateur
                        i0 = 0.88f;
                    }else{ // Plancher chauffant ou système mixte
                        i0 = 0.93f;
                    }
                }else{ // Systeme non programmable
                    if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                        i0 = 0.9f;
                    }else if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.RADIATEUR)){ // Radiateur
                        i0 = 0.9f;
                    }else{ // Plancher chauffant ou système mixte
                        i0 = 0.95f;
                    }
                }
            }else{ // le chauffage est de type central
                if (chauffage_properties.get(DpeEvent.PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR).equals(PresenceThermostatEnum.AUCUN_DES_DEUX)){ // Sans régulation par pièce
                    if(chauffage_properties.get(DpeEvent.SYSTEME_PROGRAMMABLE).equals(ProgrammationSystemeEnum.POSSIBLE)) { // Systeme programmable
                        if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                            i0 = 0.93f;
                        }else if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.RADIATEUR)){ // Radiateur
                            i0 = 0.94f;
                        }else{ // Plancher chauffant ou système mixte
                            i0 = 0.95f;
                        }
                    }else{ // Systeme non programmable
                        if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                            i0 = 0.91f;
                        }else if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.RADIATEUR)){ // Radiateur
                            i0 = 0.93f;
                        }else{ // Plancher chauffant ou système mixte
                            i0 = 0.95f;
                        }
                    }
                }else{ // Avec régulation pièce par pièce
                    if(chauffage_properties.get(DpeEvent.SYSTEME_PROGRAMMABLE).equals(ProgrammationSystemeEnum.POSSIBLE)) { // Systeme programmable
                        if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                            i0 = 0.89f;
                        }else if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.RADIATEUR)){ // Radiateur
                            i0 = 0.91f;
                        }else{ // Plancher chauffant ou système mixte
                            i0 = 0.93f;
                        }
                    }else{ // Systeme non programmable
                        if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                            i0 = 0.95f;
                        }else if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.RADIATEUR)){ // Radiateur
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
    public void tryActualiseI0(){
        if (chauffage_properties.containsKey(DpeEvent.INSTALLATION_CHAUFFAGE)) {
            if (general_properties.containsKey(DpeEvent.TYPE_BATIMENT)) {
                if (chauffage_properties.containsKey(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR)) {
                    if (chauffage_properties.containsKey(DpeEvent.PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR)) {
                        if (chauffage_properties.containsKey(DpeEvent.SYSTEME_PROGRAMMABLE)) {
                            if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUFFAGE_UNIQUE)
                                    && chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_UNIQUE)) {
                                this.actualiseI0((Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_UNIQUE));
                            } else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUFFAGE_AVEC_POIL_OU_INSERT_BOIS)
                                    && chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_SANS_POIL)) {
                                this.actualiseI0((Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_SANS_POIL));
                            } else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_GAZ_OU_FIOUL_AVEC_CHAUDIERE_BOIS)
                                    && chauffage_properties.containsKey(DpeEvent.CHAUDIERE_GAZ_FIOUL)) {
                                this.actualiseI0((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_GAZ_FIOUL));
                            } else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_AVEC_PAC)
                                    && chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC)){
                                this.actualiseI0((Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_UNIQUE));
                            } else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_AVEC_PAC_ET_INSERT_BOIS)
                                    && chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE)){
                                this.actualiseI0((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE));
                            }
                        }
                    }
                }
            }
        }
    }

    // 4.Calcul du besoin et des consommations
    private float cch;
    private float bch;
    private float pr=0;
    private float prs1=0; // Cas défavorable
    private float prs2=1.05f; // Cas défavorable
    private float rrp;
    public void actualiseBch(){
        bch=((bv*dhCor/1000)-pr*rrp)*intermittence; // TODO : prendre en compte le changement sur bv,dhcor
        tryActualiseCch();
    }
    public void actualisePr(){
        pr = sh*(prs1+prs2);
        this.actualiseBch();
    }
    public void actualiseRrp(){
        rrp=(1-3.6f*(float)Math.pow(x,2.6f)+2.6f*(float)Math.pow(x,1.6))/(float)Math.pow((1-(float)Math.pow(x,3.6)),2); // TODO : prendre en compte le changement sur x
        this.actualiseBch();
    }
    public void actualisePrs1(){
        if (ecs_properties.containsKey(DpeEvent.LOCAL_EQUIPEMENT_ECS)){
            if (ecs_properties.containsKey(DpeEvent.TYPE_EQUIPEMENT_ECS)){
                if ((ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS).equals(TypeEquipementEcsEnum.CHAUDIERE)  // L'équipement est une chaudière mixte et le dictionnaire comporte une chaudière mixte
                        && ecs_properties.containsKey(DpeEvent.CHAUDIERE_ECS))){
                    Chauffage chaudiereMixte = (Chauffage)ecs_properties.get(DpeEvent.CHAUDIERE_ECS);
                    prs1=chaudiereMixte.getPrs1();
                }else{ // L'équipement n'est pas une chaudière
                    prs1=0;
                }
                this.actualisePr();
            }
        }
    }
    public void actualisePrs2(){
        if (ecs_properties.containsKey(DpeEvent.TYPE_EQUIPEMENT_ECS)){
            if(ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS).equals(TypeEquipementEcsEnum.CHAUFFE_EAU_THERMODYNAMIQUE_SUR_AIR_EXTRAIT) // Chauffe-eau
                    || ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS).equals(TypeEquipementEcsEnum.CHAUFFE_EAU_THERMODYNAMIQUE_SUR_AIR_EXTERIEUR)
                    || ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS).equals(TypeEquipementEcsEnum.CHAUFFE_EAU_GAZ_INF_1991)
                    || ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS).equals(TypeEquipementEcsEnum.CHAUFFE_EAU_GAZ_ENTRE_1991_2002)
                    || ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS).equals(TypeEquipementEcsEnum.CHAUFFE_EAU_GAZ_SUP_2003)){
                prs2=2.1f;
                this.actualisePr();
            }else if (ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS).equals(TypeEquipementEcsEnum.CHAUDIERE)){ // Chaudière
                prs2=1.05f;
                this.actualisePr();
            }
            else { // Accumulateur ou ballon électrique
                if (ecs_properties.containsKey(DpeEvent.LOCAL_EQUIPEMENT_ECS)){
                    if (ecs_properties.get(DpeEvent.LOCAL_EQUIPEMENT_ECS).equals(LocalEquipementEcsEnum.SITUE_DANS_LOCAL_CHAUFFE)){
                        prs2=3.7f;
                    }else{
                        prs2=1.05f;
                    }
                    this.actualisePr();
                }
            }
        }
    }
    public void actualiseCch(){
        if(chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUFFAGE_UNIQUE)){
            Chauffage chauffage = (Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_UNIQUE);
            float ich=chauffage.getIch();
            this.cch=this.bch*ich;
        }else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUFFAGE_AVEC_POIL_OU_INSERT_BOIS)){
            Chauffage chauffage = (Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_SANS_POIL);
            Chauffage poele = (Chauffage)chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE);
            float k = ((FrequenceUtilisationPoilEnum)chauffage_properties.get(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUFFAGE)).getFrequence();
            float ichChauffage = chauffage.getIch();
            float ichPoele = poele.getIch();
            this.cch=k*bch*ichPoele+(1-k)*bch*ichChauffage;
        }else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_GAZ_OU_FIOUL_AVEC_CHAUDIERE_BOIS)){
            Chauffage chaudiereGazFioul = (Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_GAZ_FIOUL);
            Chauffage chaudiereBois = (Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_BOIS);
            float ichChaudiereGazFioul = chaudiereGazFioul.getIch();
            float ichChaudiereBois = chaudiereBois.getIch();
            this.cch=0.75f*bch*ichChaudiereBois+0.25f*bch*ichChaudiereGazFioul;
        }else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_AVEC_PAC)){
            Chauffage chaudiere = (Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC);
            Chauffage pac = (Chauffage)chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE);
            float ichChaudiere = chaudiere.getIch();
            float ichPac = pac.getIch();
            this.cch=0.8f*bch*ichPac+0.2f*bch*ichChaudiere;
        }else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_AVEC_PAC_ET_INSERT_BOIS)){
            Chauffage chaudiere = (Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE);
            Chauffage pac = (Chauffage)chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE);
            Chauffage poele = (Chauffage)chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC);
            float k = (float)chauffage_properties.get(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC);
            float ichChaudiere = chaudiere.getIch();
            float ichPac = pac.getIch();
            float ichPoele = poele.getIch();
            this.cch=(1-k)*(0.8f*bch*ichPac)+(1-k)*(0.2f*bch*ichChaudiere)+k*bch*ichPoele;
        }
        this.actualiseScoreDpe();
    }
    public void tryActualiseCch(){ // On s'assure qu'il y ait au moins les systèmes de présent
        if(chauffage_properties.containsKey(DpeEvent.INSTALLATION_CHAUFFAGE)){
            if(chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUFFAGE_UNIQUE)){
                if(chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_UNIQUE)){
                    this.actualiseCch();
                }
            }else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUFFAGE_AVEC_POIL_OU_INSERT_BOIS)){
                if(chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_SANS_POIL) && chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE)
                        && chauffage_properties.containsKey(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUFFAGE) ){
                    this.actualiseCch();
                }
            }else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_GAZ_OU_FIOUL_AVEC_CHAUDIERE_BOIS)){
                if(chauffage_properties.containsKey(DpeEvent.CHAUDIERE_GAZ_FIOUL) && chauffage_properties.containsKey(DpeEvent.CHAUDIERE_BOIS)){
                    this.actualiseCch();
                }
            }else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_AVEC_PAC)){
                if(chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC) && chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE)){
                    this.actualiseCch();
                }
            }else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_AVEC_PAC_ET_INSERT_BOIS)){
                if(chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE) && chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE)
                        && chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC) && chauffage_properties.containsKey(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC)){
                    this.actualiseCch();
                }
            }
        }
    }

    // 5.Rendements des installations
    public void actualiseEmetteurAllGenerateurs(Chauffage.Emission typeEmission){
        if (chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_UNIQUE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_UNIQUE)).setEmission(typeEmission);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_SANS_POIL)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_SANS_POIL)).setEmission(typeEmission);
        }
        if (chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE)).setEmission(typeEmission);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_GAZ_FIOUL)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_GAZ_FIOUL)).setEmission(typeEmission);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_BOIS)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_BOIS)).setEmission(typeEmission);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC)).setEmission(typeEmission);
        }
        if (chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE)).setEmission(typeEmission);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE)).setEmission(typeEmission);
        }
        if (chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE)).setEmission(typeEmission);
        }
        if (chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC)).setEmission(typeEmission);
        }
        if (ecs_properties.containsKey(DpeEvent.CHAUDIERE_ECS)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_ECS)).setEmission(typeEmission);
        }
    }
    public void actualiseLocalAllGenerateurs(LocalEquipementEcsEnum localEquipementEcs){
        boolean tampon;
        if (localEquipementEcs.equals(LocalEquipementEcsEnum.SITUE_DANS_LOCAL_CHAUFFE)){
            tampon=true;
        }else{
            tampon=false;
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_UNIQUE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_UNIQUE)).setGenerateurDansVolumeChauffe(tampon);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_SANS_POIL)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_SANS_POIL)).setGenerateurDansVolumeChauffe(tampon);
        }
        if (chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE)).setGenerateurDansVolumeChauffe(tampon);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_GAZ_FIOUL)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_GAZ_FIOUL)).setGenerateurDansVolumeChauffe(tampon);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_BOIS)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_BOIS)).setGenerateurDansVolumeChauffe(tampon);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC)).setGenerateurDansVolumeChauffe(tampon);
        }
        if (chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE)).setGenerateurDansVolumeChauffe(tampon);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE)).setGenerateurDansVolumeChauffe(tampon);
        }
        if (chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE)).setGenerateurDansVolumeChauffe(tampon);
        }
        if (chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC)).setGenerateurDansVolumeChauffe(tampon);
        }
        if (ecs_properties.containsKey(DpeEvent.CHAUDIERE_ECS)){
            ((Chauffage)ecs_properties.get(DpeEvent.CHAUDIERE_ECS)).setGenerateurDansVolumeChauffe(tampon);
        }
    }
    public void actualiseRobinetAllGenerateurs(PresenceRobinetEnum presenceRobinet){
        boolean presence=true;
        if (presenceRobinet.equals(PresenceRobinetEnum.ABSENCE_ROBINET_THERMOSTATIQUE)){
            presence=false;
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_UNIQUE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_UNIQUE)).setPresenceRobinetThermostatique(presence);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_SANS_POIL)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_SANS_POIL)).setPresenceRobinetThermostatique(presence);
        }
        if (chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE)).setPresenceRobinetThermostatique(presence);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_GAZ_FIOUL)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_GAZ_FIOUL)).setPresenceRobinetThermostatique(presence);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_BOIS)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_BOIS)).setPresenceRobinetThermostatique(presence);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC)).setPresenceRobinetThermostatique(presence);
        }
        if (chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE)).setPresenceRobinetThermostatique(presence);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE)).setPresenceRobinetThermostatique(presence);
        }
        if (chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE)).setPresenceRobinetThermostatique(presence);
        }
        if (chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC)).setPresenceRobinetThermostatique(presence);
        }
        if (ecs_properties.containsKey(DpeEvent.CHAUDIERE_ECS)){
            ((Chauffage)ecs_properties.get(DpeEvent.CHAUDIERE_ECS)).setPresenceRobinetThermostatique(presence);
        }
    }
    public void actualiseRendementsChauffage(Chauffage chauffage){
        boolean situeDansLocalChauffe=false; // Initialisation défavorable
        Chauffage.Emission typeEmission = Chauffage.Emission.RADIATEUR; // Initialisation défavorable
        boolean presenceRobinetThermostatique=true; // Initialisation défavorable
        if (ecs_properties.containsKey(DpeEvent.LOCAL_EQUIPEMENT_ECS)){
            if(ecs_properties.get(DpeEvent.LOCAL_EQUIPEMENT_ECS).equals(LocalEquipementEcsEnum.SITUE_DANS_LOCAL_CHAUFFE)) {
                situeDansLocalChauffe = true;
            }
        }
        if (ecs_properties.containsKey(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR)){
            typeEmission=(Chauffage.Emission)ecs_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR);
        }
        if (ecs_properties.containsKey(DpeEvent.PRESENCE_ROBINET_THERMOSTATIQUE)){
            if (ecs_properties.get(DpeEvent.PRESENCE_ROBINET_THERMOSTATIQUE).equals(PresenceRobinetEnum.ABSENCE_ROBINET_THERMOSTATIQUE)){
                presenceRobinetThermostatique=false;
            }
        }
        chauffage.setEmission(typeEmission);
        chauffage.setGenerateurDansVolumeChauffe(situeDansLocalChauffe);
        chauffage.setPresenceRobinetThermostatique(presenceRobinetThermostatique);
    }

    // 6.Rendement de génération des chaudières
    private int tabTauxDeCharge[] = new int[10];
    private float tabCoeffPondX[] = new float[10];
    public void tryActualiseIch(){
        boolean actualisationCch = false;
        if (chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_UNIQUE)){
            this.actualiseRendementGeneration((Chauffage) chauffage_properties.get(DpeEvent.CHAUFFAGE_UNIQUE));
            actualisationCch=true;
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_SANS_POIL)){
            this.actualiseRendementGeneration((Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_SANS_POIL));
            actualisationCch=true;
        }
        if (chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE)){
            this.actualiseRendementGeneration((Chauffage)chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE));
            actualisationCch=true;
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_GAZ_FIOUL)){
            this.actualiseRendementGeneration((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_GAZ_FIOUL));
            actualisationCch=true;
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_BOIS)){
            this.actualiseRendementGeneration((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_BOIS));
            actualisationCch=true;
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC)){
            this.actualiseRendementGeneration((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC));
            actualisationCch=true;
        }
        if (chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE)){
            this.actualiseRendementGeneration((Chauffage)chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE));
            actualisationCch=true;
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE)){
            this.actualiseRendementGeneration((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE));
            actualisationCch=true;
        }
        if (chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE)){
            this.actualiseRendementGeneration((Chauffage)chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE));
            actualisationCch=true;
        }
        if (chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC)){
            this.actualiseRendementGeneration((Chauffage)chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC));
            actualisationCch=true;
        }
        if (ecs_properties.containsKey(DpeEvent.CHAUDIERE_ECS)){
            this.actualiseRendementGeneration((Chauffage)ecs_properties.get(DpeEvent.CHAUDIERE_ECS));
            actualisationCch=true;
        }
        if(actualisationCch){
            this.tryActualiseCch();
        }
    } // TODO : prendre en compte les changements sur GV
    public void actualiseRendementGeneration(Chauffage chauffage){
        float tInt=23; // Cas défavorable
        float tExtBase=-15; // Cas défavorable
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
        boolean haveRegulation=false;

        if (general_properties.get(DpeEvent.PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR)==PresenceThermostatEnum.PRESENCE_THERMOSTAT_OU_SONDE){
            haveRegulation=true;
        }

        if (general_properties.containsKey(DpeEvent.DEPARTEMENT_BATIMENT)){
            tExtBase=((DepartementBatimentEnum)general_properties.get(DpeEvent.DEPARTEMENT_BATIMENT)).getTempExtBase();
        }
        if(chauffage_properties.containsKey(DpeEvent.TEMPERATURE_INTERIEUR)){
            tInt=((TemperatureInterieurEnum)chauffage_properties.get(DpeEvent.TEMPERATURE_INTERIEUR)).getTemperatureInterieure();
        }
        pch=1.2f*gv*(tInt-tExtBase)/(1000*rr*rd*re);
        pDim=pch;

        if (chauffage.getGenerateur().equals(Chauffage.Generateur.RADIATEUR_GAZ_AVANT_2006)
                || chauffage.getGenerateur().equals(Chauffage.Generateur.RADIATEUR_GAZ_APRES_2006)){
            int n = (int) sh/12;
            pn=pDim/n;
            chauffage.setPn(pn);
        }else{
            if(chauffage.getGenereEgalementEcs()){
                if(ecs_properties.containsKey(DpeEvent.DECLENCHEMENT_CHAUDIERE_ROBINET)){
                    if(ecs_properties.get(DpeEvent.DECLENCHEMENT_CHAUDIERE_ROBINET).equals(DeclenchementChaudiereEnum.DECLENCHEMENT_OUVERTURE_ROBINET_EAU_CHAUDE)){
                        pecs=21;
                    }
                }
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
        if (general_properties.containsKey(DpeEvent.ANNEE_CONSTRUCTION)){
            if(chauffage_properties.containsKey(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR)){
                switch((DateConstructionBatimentEnum)general_properties.get(DpeEvent.ANNEE_CONSTRUCTION)){
                    case AVANT_1975:
                    case ENTRE_1975_ET_1977:
                    case ENTRE_1978_ET_1982:
                        if(chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.PLANCHER_CHAUFFANT)){
                            tFonc100 = 60;
                        }else{
                            tFonc100 = 80;
                        }
                        break;
                    default:
                        if(chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.PLANCHER_CHAUFFANT)){
                            tFonc100 = 35;
                        }else{
                            tFonc100 = 65;
                        }
                        break;
                }
            }
        }
        return tFonc100;
    }
    public float getTfonc30ChaudiereCondensation(){
        float tFonc30 = 38; // Cas défavorable
        if (general_properties.containsKey(DpeEvent.ANNEE_CONSTRUCTION)){
            if(chauffage_properties.containsKey(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR)){
                switch((DateConstructionBatimentEnum)general_properties.get(DpeEvent.ANNEE_CONSTRUCTION)){
                    case AVANT_1975:
                    case ENTRE_1975_ET_1977:
                    case ENTRE_1978_ET_1982:
                        if(chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.PLANCHER_CHAUFFANT)){
                            tFonc30 = 32;
                        }else{
                            tFonc30 = 38;
                        }
                        break;
                    default:
                        if(chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.PLANCHER_CHAUFFANT)){
                            tFonc30 = 24.5f;
                        }else{
                            tFonc30 = 34;
                        }
                        break;
                }
            }
        }
        return tFonc30;
    }
    public float getTfonc30ChaudiereBasseTemperature(){
        float tFonc30 = 48.5f; // Cas défavorable
        if (general_properties.containsKey(DpeEvent.ANNEE_CONSTRUCTION)){
            if(chauffage_properties.containsKey(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR)){
                switch((DateConstructionBatimentEnum)general_properties.get(DpeEvent.ANNEE_CONSTRUCTION)){
                    case AVANT_1975:
                    case ENTRE_1975_ET_1977:
                    case ENTRE_1978_ET_1982:
                        if(chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.PLANCHER_CHAUFFANT)){
                            tFonc30 = 42.5f;
                        }else{
                            tFonc30 = 48.5f;
                        }
                        break;
                    default:
                        if(chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.PLANCHER_CHAUFFANT)){
                            tFonc30 = 35;
                        }else{
                            tFonc30 = 44;
                        }
                        break;
                }
            }
        }
        return tFonc30;
    }
    public float getTfonc30ChaudiereStandardAvant1990(){
        float tFonc30 = 59; // Cas défavorable
        if (general_properties.containsKey(DpeEvent.ANNEE_CONSTRUCTION)){
            if(chauffage_properties.containsKey(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR)){
                switch((DateConstructionBatimentEnum)general_properties.get(DpeEvent.ANNEE_CONSTRUCTION)){
                    case AVANT_1975:
                    case ENTRE_1975_ET_1977:
                    case ENTRE_1978_ET_1982:
                        if(chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.PLANCHER_CHAUFFANT)){
                            tFonc30 = 53;
                        }else{
                            tFonc30 = 59;
                        }
                        break;
                    default:
                        if(chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.PLANCHER_CHAUFFANT)){
                            tFonc30 = 50;
                        }else{
                            tFonc30 = 54.5f;
                        }
                        break;
                }
            }
        }
        return tFonc30;
    }
    public float getTfonc30ChaudiereStandardApres1991(){
        float tFonc30 = 55.5f; // Cas défavorable
        if (general_properties.containsKey(DpeEvent.ANNEE_CONSTRUCTION)){
            if(chauffage_properties.containsKey(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR)){
                switch((DateConstructionBatimentEnum)general_properties.get(DpeEvent.ANNEE_CONSTRUCTION)){
                    case AVANT_1975:
                    case ENTRE_1975_ET_1977:
                    case ENTRE_1978_ET_1982:
                        if(chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.PLANCHER_CHAUFFANT)){
                            tFonc30 = 49.5f;
                        }else{
                            tFonc30 = 55.5f;
                        }
                        break;
                    default:
                        if(chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.PLANCHER_CHAUFFANT)){
                            tFonc30 = 45;
                        }else{
                            tFonc30 = 51.5f;
                        }
                        break;
                }
            }
        }
        return tFonc30;
    }

    // 7.Expression du besoin de la consommation d'ECS
    private float bEcs=3688;
    private float becs=55;
    private float nbJoursAbsenceParAn=0;
    private float nbHabitant=4;
    private float cEcs=5000;
    private float iEcs=5.45f;
    private float fEcs; // TODO : prendre en compte les installations solaires
    private float tfr=10.5f;
    public void actualiseBecs(){
        bEcs=(1.1627f*(365-nbJoursAbsenceParAn)*nbHabitant*becs*(50-tfr))/1000;
        this.tryActualiseRendementEquipementEcs();
        this.actualiseCecs();
    }
    public void actualiseTfr(){
        if(general_properties.containsKey(DpeEvent.DEPARTEMENT_BATIMENT)){
            int zone = ((DepartementBatimentEnum)general_properties.get(DpeEvent.DEPARTEMENT_BATIMENT)).getZoneHiver();
            switch (zone){
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
        }
        this.actualiseBecs();
    }
    public void actualisebecs(UsageEauChaudeEnum usageEcs){
        switch (usageEcs){
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
        cEcs=bEcs*iEcs;
        this.actualiseScoreDpe();
    }

    // 8.Rendements de l'installation d'ECS
    public void tryActualiseRendementEquipementEcs(){
        TypeEquipementEcsEnum vieilEquipementEcs = TypeEquipementEcsEnum.CHAUFFE_EAU_GAZ_ENTRE_1991_2002; // On prend un vieil équipement de base
        if (ecs_properties.containsKey(DpeEvent.TYPE_EQUIPEMENT_ECS)){
            this.actualiseRendementEcs((TypeEquipementEcsEnum)ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS));
        }else{
            this.actualiseRendementEcs(vieilEquipementEcs);
        }
    }
    public void actualiseRendementEcs(TypeEquipementEcsEnum equipementEcs){ // TODO-> prendre en compte les dépendances
        float cr,qgw,rs,rd,rendement,rpn,qp0,pveil,cop,pecs,vs,cef;
        vs = this.getVs();
        cef = this.getCef();
        switch (equipementEcs){
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
                if(ecs_properties.containsKey(DpeEvent.CHAUDIERE_ECS)){
                    Chauffage chaudiere = (Chauffage)ecs_properties.get(DpeEvent.CHAUDIERE_ECS);
                    qp0=chaudiere.getQp0();
                    rpn=chaudiere.getRpn();
                    pveil=chaudiere.getPuissanceVeilleuse();
                    iEcs = (1/rpn)+1720*(qp0/bEcs)+6536*(0.5f*pveil/bEcs);
                }
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
        if(ecs_properties.get(DpeEvent.LOCAL_EQUIPEMENT_ECS)==LocalEquipementEcsEnum.SITUE_DANS_LOCAL_CHAUFFE){
            return 0.85f;
        }else{
            return 0.8f;
        }
    }
    public float getRdBallonThermodynamique(){
        if(ecs_properties.get(DpeEvent.LOCAL_EQUIPEMENT_ECS)==LocalEquipementEcsEnum.SITUE_DANS_LOCAL_CHAUFFE){
            return 0.9f;
        }else{
            return 0.85f;
        }
    }
    public float getVs(){
        float vs=300;
        if(general_properties.containsKey(DpeEvent.CATEGORIE_BATIMENT)){
            switch ((CategorieLogementEnum)general_properties.get(DpeEvent.CATEGORIE_BATIMENT)){
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
        }
        return vs;
    }
    public float getCef(){
        float cef=1.1f;
        if(general_properties.get(DpeEvent.ABONNEMENT_ELECTRIQUE) == TypeAbonnementElectriqueEnum.DOUBLE_TARIF){
            if(ecs_properties.get(DpeEvent.LOCAL_EQUIPEMENT_ECS) == LocalEquipementEcsEnum.SITUE_DANS_LOCAL_CHAUFFE){
                cef=0.6f;
            }else{ //LNC ou pas d'info
                cef=0.75f;
            }
        }else{ //Simple ou tarif ou pas d'info
            if(ecs_properties.get(DpeEvent.LOCAL_EQUIPEMENT_ECS) == LocalEquipementEcsEnum.SITUE_DANS_LOCAL_CHAUFFE){
                cef=0.9f;
            }else{ //LNC ou pas d'info
                cef=1.1f;
            }
        }
        return cef;
    }

    // 9.Consommation de climatisation
    private float cClimatisation=700; // Cas le plus défavorable (7*100)
    private float rClimatisation=7; // Cas le plus défavorable (voir 9)
    private float sClimatisation=100; // On considère que l'on climatise une grande surface
    public void actualiseResistanceClim(){
        if (general_properties.containsKey(DpeEvent.DEPARTEMENT_BATIMENT)){
            DepartementBatimentEnum.ZoneEte zoneEte = ((DepartementBatimentEnum) general_properties.get(DpeEvent.DEPARTEMENT_BATIMENT)).getZoneEte();
            switch(zoneEte){
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
        }else{
            rClimatisation=7;
        }
        actualiseConsommationClimatisation();
    }
    public void actualiseConsommationClimatisation(){
        if (general_properties.containsKey(DpeEvent.CLIMATISATION_LOGEMENT)){
            if ((general_properties.get(DpeEvent.CLIMATISATION_LOGEMENT)).equals(PresenceClimatisationLogementEnum.NON)){
                cClimatisation=0;
            }else{
                cClimatisation = rClimatisation * sClimatisation;
            }
        }else{
            cClimatisation=700;
        }
        this.actualiseScoreDpe();
    }

    // 10.Concommation des usages spécifiques
    private float cElectromenager=1906; // Comme si on possède tous les éléments électroménager (voir 10.3)
    private float cEclairageSurfacique = 3.7f; // Consommation max annuel des lampes (voir 10.2)
    private float cEclairage = 185; // sh max * cEclairageSurfacique max = 50*3.7
    private float cCuisson = 1660; // Conso cuisson max (cf 10.1)
    public void actualiseConsommationEclairageSurfacique() {
        if (general_properties.containsKey(DpeEvent.EQUIPEMENT_ECLAIRAGE)){
            TypeEquipementEclairageEnum equipementEclairage = (TypeEquipementEclairageEnum)general_properties.get(DpeEvent.EQUIPEMENT_ECLAIRAGE);
            cEclairageSurfacique=equipementEclairage.getConsommationEclairage();
        }else{
            cEclairageSurfacique=3.7f;
        }
        this.actualiseConsommationEclairage();
    }
    public void actualiseConsommationEclairage(){
        cEclairage=cEclairageSurfacique * sh;
        this.actualiseScoreDpe();
    }
    public void actualiseConsommationElectromenager(){
        if(general_properties.containsKey(DpeEvent.EQUIPEMENT_ELECTROMENAGER)){
            cElectromenager=0;
            ArrayList<TypeEquipementElectromenagerEnum> listEquipement = (ArrayList<TypeEquipementElectromenagerEnum>)general_properties.get(DpeEvent.EQUIPEMENT_ELECTROMENAGER);
            for (TypeEquipementElectromenagerEnum actualEquipement : listEquipement){
                cElectromenager+=actualEquipement.getConsommation();
            }
        }
        this.actualiseScoreDpe();
    }
    public void actualiseConsommationCuisson(){
        if (general_properties.containsKey(DpeEvent.EQUIPEMENT_CUISSON)){
            TypeEquipementCuissonEnum equipementCuisson = (TypeEquipementCuissonEnum)general_properties.get(DpeEvent.EQUIPEMENT_CUISSON);
            cCuisson = equipementCuisson.getConsommation();
        }else{
            cCuisson=1660;
        }
        this.actualiseScoreDpe();
    }

    /*** Constructeur en singleton ***/
    private static class DpeHolder
    {
        /** Instance unique non préinitialisée */
        private final static Dpe INSTANCE = new Dpe();
    }

    public static synchronized Dpe getInstance() {
        return DpeHolder.INSTANCE;
    }
    private Dpe () {
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
        scoreDpe = (cElectromenager+cEclairage+cCuisson+cClimatisation+cEcs+cch)/sh;
    }

    public float getScoreDpe(){
        return Math.round(this.scoreDpe);
    }


    public void notify(Channel c, Event e) throws InterruptedException {

        EventType eventType = e.getEventType();
        if (c == Channel.DPE) {
            if (eventType instanceof DpeEvent) {
                DpeEvent event = (DpeEvent) eventType;
                Object o = e.getUserObject();
                switch (event) {
                    case TYPE_BATIMENT: {
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        EventRequest eventRequest = (EventRequest) items.get("eventRequest");
                        Layout layout = (Layout) items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeBatimentEnum typeBatiment = (TypeBatimentEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.TYPE_BATIMENT, typeBatiment);
                            this.tryActualiseI0();

                        } else if (eventRequest == EventRequest.GET_STATE) {
                            TypeBatimentEnum type = (TypeBatimentEnum) general_properties.get(DpeEvent.TYPE_BATIMENT);
                            HashMap<String, Object> currentItems = new HashMap<String, Object>();
                            currentItems.put("lastValue", type);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_BATIMENT, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case CATEGORIE_BATIMENT: {
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        EventRequest eventRequest = (EventRequest) items.get("eventRequest");
                        Layout layout = (Layout) items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            CategorieLogementEnum categorieLogement = (CategorieLogementEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.CATEGORIE_BATIMENT, categorieLogement);
                            this.tryActualiseRendementEquipementEcs();

                        } else if (eventRequest == EventRequest.GET_STATE) {
                            CategorieLogementEnum type = null;
                            if (general_properties.containsKey(DpeEvent.CATEGORIE_BATIMENT)){
                                type = (CategorieLogementEnum) general_properties.get(DpeEvent.CATEGORIE_BATIMENT);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue", type);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CATEGORIE_BATIMENT, currentItems);
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
//                            EventManager.getInstance().put(Channel.DPE, e2);
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
//                            EventManager.getInstance().put(Channel.DPE, e2);
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
                            this.actualiseResistanceClim();
                            this.tryActualiseIch();
                            this.actualiseTfr();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            DepartementBatimentEnum type = this.departementBatiment;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.DEPARTEMENT_BATIMENT, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case POSITION_APPARTEMENT:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            PositionAppartementEnum positionAppartement = (PositionAppartementEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.POSITION_APPARTEMENT, positionAppartement);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            PositionAppartementEnum type = null;
                            if (general_properties.containsKey(DpeEvent.POSITION_APPARTEMENT)){
                                type = (PositionAppartementEnum) general_properties.get(DpeEvent.POSITION_APPARTEMENT);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.POSITION_APPARTEMENT, currentItems);
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
                            for(Mur m:murList){
                                actualiseCoeffDeperditionThermique(m);
                            }
                            this.tryActualiseIch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            DateConstructionBatimentEnum type = this.dateConstructionBatiment;
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.ANNEE_CONSTRUCTION, currentItems);
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
                            for(Mur m:murList){
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
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case ABONNEMENT_ELECTRIQUE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeAbonnementElectriqueEnum typeAbonnementElectrique = (TypeAbonnementElectriqueEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.ABONNEMENT_ELECTRIQUE, typeAbonnementElectrique);
                            this.tryActualiseRendementEquipementEcs();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeAbonnementElectriqueEnum type = null;
                            if (general_properties.containsKey(DpeEvent.ABONNEMENT_ELECTRIQUE)){
                                type = (TypeAbonnementElectriqueEnum) general_properties.get(DpeEvent.ABONNEMENT_ELECTRIQUE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.ABONNEMENT_ELECTRIQUE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case CLIMATISATION_LOGEMENT: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            PresenceClimatisationLogementEnum presenceClimatisationLogement = (PresenceClimatisationLogementEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.CLIMATISATION_LOGEMENT, presenceClimatisationLogement);
                            this.actualiseConsommationClimatisation();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            PresenceClimatisationLogementEnum type = null;
                            if (general_properties.containsKey(DpeEvent.CLIMATISATION_LOGEMENT)){
                                type = (PresenceClimatisationLogementEnum) general_properties.get(DpeEvent.CLIMATISATION_LOGEMENT);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CLIMATISATION_LOGEMENT, currentItems);
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
                            this.actualiseResistanceClim();
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
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case EQUIPEMENT_ECLAIRAGE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeEquipementEclairageEnum equipementEclairage = (TypeEquipementEclairageEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.EQUIPEMENT_ECLAIRAGE, equipementEclairage);
                            this.actualiseConsommationEclairageSurfacique();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeEquipementEclairageEnum type = null;
                            if (general_properties.containsKey(DpeEvent.EQUIPEMENT_ECLAIRAGE)){
                                type = (TypeEquipementEclairageEnum) general_properties.get(DpeEvent.EQUIPEMENT_ECLAIRAGE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.EQUIPEMENT_ECLAIRAGE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case EQUIPEMENT_ELECTROMENAGER: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        ArrayList<TypeEquipementElectromenagerEnum> listAppareils = new ArrayList<TypeEquipementElectromenagerEnum>();
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeEquipementElectromenagerEnum equipementElectromenager = (TypeEquipementElectromenagerEnum) items.get("lastValue");
                            if (general_properties.containsKey(DpeEvent.EQUIPEMENT_ELECTROMENAGER)){
                                listAppareils = (ArrayList) general_properties.get(DpeEvent.EQUIPEMENT_ELECTROMENAGER);
                                if (!listAppareils.contains(equipementElectromenager)){
                                    listAppareils.add(equipementElectromenager);
                                }else{
                                    listAppareils.remove(equipementElectromenager);
                                }
                            }else{
                                listAppareils.add(equipementElectromenager);
                            }
                            general_properties.put(DpeEvent.EQUIPEMENT_ELECTROMENAGER, listAppareils);
                            this.actualiseConsommationElectromenager();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeEquipementElectromenagerEnum type = null;
                            if (general_properties.containsKey(DpeEvent.EQUIPEMENT_ELECTROMENAGER)){
                                type = (TypeEquipementElectromenagerEnum) general_properties.get(DpeEvent.EQUIPEMENT_ELECTROMENAGER);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.EQUIPEMENT_ELECTROMENAGER, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case EQUIPEMENT_CUISSON: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeEquipementCuissonEnum equipementCuisson = (TypeEquipementCuissonEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.EQUIPEMENT_CUISSON, equipementCuisson);
                            this.actualiseConsommationCuisson();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeEquipementCuissonEnum type = null;
                            if (general_properties.containsKey(DpeEvent.EQUIPEMENT_CUISSON)){
                                type = (TypeEquipementCuissonEnum) general_properties.get(DpeEvent.EQUIPEMENT_CUISSON);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.EQUIPEMENT_CUISSON, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_VENTILATION: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeVentilationEnum typeVentilation = (TypeVentilationEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.TYPE_VENTILATION, typeVentilation);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeVentilationEnum type = null;
                            if (general_properties.containsKey(DpeEvent.TYPE_VENTILATION)){
                                type = (TypeVentilationEnum) general_properties.get(DpeEvent.TYPE_VENTILATION);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_VENTILATION, currentItems);
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
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case INSTALLATION_CHAUFFAGE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        Layout layout = (Layout) items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            InstallationChauffageEnum typeSource = (InstallationChauffageEnum) items.get("lastValue");
                            if (typeSource == InstallationChauffageEnum.CHAUFFAGE_UNIQUE) {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), false);
                            } else if (typeSource == InstallationChauffageEnum.CHAUFFAGE_AVEC_POIL_OU_INSERT_BOIS){
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), false);
                            } else if (typeSource == InstallationChauffageEnum.CHAUDIERE_GAZ_OU_FIOUL_AVEC_CHAUDIERE_BOIS){
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                            } else if (typeSource == InstallationChauffageEnum.CHAUDIERE_AVEC_PAC){
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                            } else if (typeSource == InstallationChauffageEnum.CHAUDIERE_AVEC_PAC_ET_INSERT_BOIS){
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                            }
                            chauffage_properties.put(DpeEvent.INSTALLATION_CHAUFFAGE, typeSource);
                            this.tryActualiseI0();
                            this.tryActualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            InstallationChauffageEnum type = (InstallationChauffageEnum) chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE);

                            HashMap<String, Object> currentItems = new HashMap<String, Object>();
                            currentItems.put("lastValue", type);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.INSTALLATION_CHAUFFAGE, currentItems);
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
                        }
                        break;
                    }

                    case CHAUFFAGE_UNIQUE :{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        Layout layout = (Layout) items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage chauffage = new Chauffage((Chauffage.Generateur) items.get("lastValue"));
                            chauffage_properties.put(DpeEvent.CHAUFFAGE_UNIQUE, chauffage);

                            String nameGenerateur = chauffage.getGenerateur().toString();
                            if (nameGenerateur.startsWith("Chaudière")){ // Si le générateur est une chaudière
                                this.actualiseRendementGeneration(chauffage);
                                if (ecs_properties.containsKey(DpeEvent.TYPE_EQUIPEMENT_ECS)){
                                    if (ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS).equals(TypeEquipementEcsEnum.CHAUDIERE)) {
                                        chauffage.setGenereEgalementEcs(true);
                                        ecs_properties.put(DpeEvent.CHAUDIERE_ECS, chauffage);
                                        this.actualisePrs1();
                                    }else{
                                        chauffage.setGenereEgalementEcs(false);
                                    }
                                }
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                            }else{
                                if (ecs_properties.containsKey(DpeEvent.CHAUDIERE_ECS)){
                                    ecs_properties.remove(DpeEvent.CHAUDIERE_ECS);
                                }
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                            }
                            if(chauffage.getGenerateur().equals(Chauffage.Generateur.RADIATEUR_GAZ_AVANT_2006) || chauffage.getGenerateur().equals(Chauffage.Generateur.RADIATEUR_GAZ_APRES_2006)){
                                this.actualiseRendementGeneration(chauffage);
                            }
                            this.actualiseRendementsChauffage(chauffage);
                            this.tryActualiseI0();
                            this.tryActualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage type = null;
                            if (chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_UNIQUE)){
                                type = (Chauffage) chauffage_properties.get(DpeEvent.CHAUFFAGE_UNIQUE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CHAUFFAGE_UNIQUE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case CHAUFFAGE_SANS_POIL: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        Layout layout = (Layout) items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage chauffage = new Chauffage((Chauffage.Generateur) items.get("lastValue"));
                            chauffage_properties.put(DpeEvent.CHAUFFAGE_SANS_POIL, chauffage);

                            String nameGenerateur = chauffage.getGenerateur().toString();
                            if (nameGenerateur.startsWith("Chaudière")){ // Si le générateur est une chaudière
                                this.actualiseRendementGeneration(chauffage);
                                if (ecs_properties.containsKey(DpeEvent.TYPE_EQUIPEMENT_ECS)){
                                    if (ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS).equals(TypeEquipementEcsEnum.CHAUDIERE)){
                                        chauffage.setGenereEgalementEcs(true);
                                        ecs_properties.put(DpeEvent.CHAUDIERE_ECS, chauffage);
                                        this.actualisePrs1();
                                    }else{
                                        chauffage.setGenereEgalementEcs(false);
                                    }
                                }
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                            }else{
                                if (ecs_properties.containsKey(DpeEvent.CHAUDIERE_ECS)){
                                    ecs_properties.remove(DpeEvent.CHAUDIERE_ECS);
                                }
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                            }
                            if(chauffage.getGenerateur().equals(Chauffage.Generateur.RADIATEUR_GAZ_AVANT_2006) || chauffage.getGenerateur().equals(Chauffage.Generateur.RADIATEUR_GAZ_APRES_2006)){
                                this.actualiseRendementGeneration(chauffage);
                            }
                            this.tryActualiseI0();
                            this.tryActualiseIch();
                            this.tryActualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage type = null;
                            if (chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_SANS_POIL)){
                                type = (Chauffage) chauffage_properties.get(DpeEvent.CHAUFFAGE_SANS_POIL);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CHAUFFAGE_SANS_POIL, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case POELE_OU_INSERT_AVEC_CHAUFFAGE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage chauffage = new Chauffage((Chauffage.Generateur) items.get("lastValue"));
                            chauffage_properties.put(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE, chauffage);
                            this.actualiseRendementsChauffage(chauffage);
                            this.tryActualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage type = null;
                            if (chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE)){
                                type = (Chauffage) chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUFFAGE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            FrequenceUtilisationPoilEnum frequenceUtilisationPoil = (FrequenceUtilisationPoilEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUFFAGE, frequenceUtilisationPoil);
                            this.tryActualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            FrequenceUtilisationPoilEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUFFAGE)){
                                type = (FrequenceUtilisationPoilEnum) chauffage_properties.get(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUFFAGE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUFFAGE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case CHAUDIERE_GAZ_FIOUL:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage chaudiere = new Chauffage((Chauffage.Generateur) items.get("lastValue"));
                            chauffage_properties.put(DpeEvent.CHAUDIERE_GAZ_FIOUL, chaudiere);

                            if (ecs_properties.containsKey(DpeEvent.TYPE_EQUIPEMENT_ECS)){
                                if (ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS).equals(TypeEquipementEcsEnum.CHAUDIERE)){
                                    chaudiere.setGenereEgalementEcs(true);
                                    ecs_properties.put(DpeEvent.CHAUDIERE_ECS, chaudiere);
                                    this.actualisePrs1();
                                }else{
                                    chaudiere.setGenereEgalementEcs(false);
                                }
                            }

                            this.actualiseRendementsChauffage(chaudiere);
                            this.actualiseRendementGeneration(chaudiere);
                            this.tryActualiseI0();
                            this.tryActualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage type = null;
                            if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_GAZ_FIOUL)){
                                type = (Chauffage) chauffage_properties.get(DpeEvent.CHAUDIERE_GAZ_FIOUL);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CHAUDIERE_GAZ_FIOUL, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case CHAUDIERE_BOIS:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage chaudiere = new Chauffage((Chauffage.Generateur) items.get("lastValue"));
                            chauffage_properties.put(DpeEvent.CHAUDIERE_BOIS, chaudiere);
                            this.actualiseRendementsChauffage(chaudiere);
                            this.actualiseRendementGeneration(chaudiere);
                            this.tryActualiseCch();

                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage type = null;
                            if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_BOIS)){
                                type = (Chauffage) chauffage_properties.get(DpeEvent.CHAUDIERE_BOIS);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CHAUDIERE_BOIS, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case CHAUDIERE_AVEC_PAC:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage chaudiere = new Chauffage((Chauffage.Generateur) items.get("lastValue"));
                            chauffage_properties.put(DpeEvent.CHAUDIERE_AVEC_PAC, chaudiere);
                            this.actualiseRendementsChauffage(chaudiere);
                            this.actualiseRendementGeneration(chaudiere);
                            this.tryActualiseI0();
                            this.tryActualiseCch();

                            if (ecs_properties.containsKey(DpeEvent.TYPE_EQUIPEMENT_ECS)){
                                if (ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS).equals(TypeEquipementEcsEnum.CHAUDIERE)){
                                    chaudiere.setGenereEgalementEcs(true);
                                    ecs_properties.put(DpeEvent.CHAUDIERE_ECS, chaudiere);
                                    this.actualisePrs1();
                                }else{
                                    chaudiere.setGenereEgalementEcs(false);
                                }
                            }
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage type = null;
                            if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC)){
                                type = (Chauffage) chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CHAUDIERE_AVEC_PAC, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case POMPE_A_CHALEUR_AVEC_CHAUDIERE :{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage pac = new Chauffage((Chauffage.Generateur) items.get("lastValue"));
                            chauffage_properties.put(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE, pac);
                            this.actualiseRendementsChauffage(pac);
                            this.tryActualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage type = null;
                            if (chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE)){
                                type = (Chauffage) chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case CHAUDIERE_AVEC_PAC_ET_POELE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage chaudiere = new Chauffage((Chauffage.Generateur) items.get("lastValue"));
                            chauffage_properties.put(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE, chaudiere);

                            if (ecs_properties.containsKey(DpeEvent.TYPE_EQUIPEMENT_ECS)){
                                if (ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS).equals(TypeEquipementEcsEnum.CHAUDIERE)){
                                    chaudiere.setGenereEgalementEcs(true);
                                    ecs_properties.put(DpeEvent.CHAUDIERE_ECS, chaudiere);
                                    this.actualisePrs1();
                                }else{
                                    chaudiere.setGenereEgalementEcs(false);
                                }
                            }
                            this.actualiseRendementsChauffage(chaudiere);
                            this.actualiseRendementGeneration(chaudiere);
                            this.tryActualiseI0();
                            this.tryActualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage type = null;
                            if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE)){
                                type = (Chauffage) chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE :{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage pac = new Chauffage((Chauffage.Generateur) items.get("lastValue"));
                            chauffage_properties.put(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE, pac);
                            this.actualiseRendementsChauffage(pac);
                            this.tryActualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage type = null;
                            if (chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE)){
                                type = (Chauffage) chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage poele = new Chauffage((Chauffage.Generateur) items.get("lastValue"));
                            chauffage_properties.put(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC, poele);
                            this.actualiseRendementsChauffage(poele);
                            this.tryActualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage type = null;
                            if (chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC)){
                                type = (Chauffage) chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            FrequenceUtilisationPoilEnum frequenceUtilisationPoil = (FrequenceUtilisationPoilEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC, frequenceUtilisationPoil);
                            this.tryActualiseCch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            FrequenceUtilisationPoilEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC)){
                                type = (FrequenceUtilisationPoilEnum) chauffage_properties.get(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TEMPERATURE_INTERIEUR:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            tInt = (TemperatureInterieurEnum) items.get("lastValue");
                            this.actualisedhCor();
                            this.tryActualiseIch();
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
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_EMETTEUR_DE_CHALEUR:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage.Emission typeEmission = (Chauffage.Emission) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR, typeEmission);
                            this.actualiseEmetteurAllGenerateurs(typeEmission);
                            this.tryActualiseI0();
                            this.tryActualiseIch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage.Emission type = null;
                            if (chauffage_properties.containsKey(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR)){
                                type = (Chauffage.Emission) chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            PresenceThermostatEnum presenceThermostat = (PresenceThermostatEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR, presenceThermostat);
                            this.tryActualiseI0();
                            this.tryActualiseIch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            PresenceThermostatEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR)){
                                type = (PresenceThermostatEnum) chauffage_properties.get(DpeEvent.PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case PRESENCE_ROBINET_THERMOSTATIQUE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            PresenceRobinetEnum presenceRobinet = (PresenceRobinetEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.PRESENCE_ROBINET_THERMOSTATIQUE, presenceRobinet);
                            this.actualiseRobinetAllGenerateurs(presenceRobinet);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            PresenceRobinetEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.PRESENCE_ROBINET_THERMOSTATIQUE)){
                                type = (PresenceRobinetEnum) chauffage_properties.get(DpeEvent.PRESENCE_ROBINET_THERMOSTATIQUE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.PRESENCE_ROBINET_THERMOSTATIQUE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case SYSTEME_PROGRAMMABLE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            ProgrammationSystemeEnum programmationSysteme = (ProgrammationSystemeEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.SYSTEME_PROGRAMMABLE, programmationSysteme);
                            this.tryActualiseI0();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            ProgrammationSystemeEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.SYSTEME_PROGRAMMABLE)){
                                type = (ProgrammationSystemeEnum) chauffage_properties.get(DpeEvent.SYSTEME_PROGRAMMABLE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.SYSTEME_PROGRAMMABLE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_EQUIPEMENT_ECS:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        Layout layout = (Layout) items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeEquipementEcsEnum typeEquipementEcs = (TypeEquipementEcsEnum) items.get("lastValue");
                            if (typeEquipementEcs == TypeEquipementEcsEnum.CHAUDIERE) {
                                if (chauffage_properties.containsKey(DpeEvent.INSTALLATION_CHAUFFAGE)) {
                                    if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUFFAGE_UNIQUE)
                                            && chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_UNIQUE)) {
                                        Chauffage chauffage = (Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_UNIQUE);
                                        chauffage.setGenereEgalementEcs(true);
                                        ecs_properties.put(DpeEvent.CHAUDIERE_ECS, chauffage);
                                        this.actualiseRendementsChauffage(chauffage);
                                    } else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUFFAGE_AVEC_POIL_OU_INSERT_BOIS)
                                            && chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_SANS_POIL)) {
                                        Chauffage chauffage = (Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_SANS_POIL);
                                        chauffage.setGenereEgalementEcs(true);
                                        ecs_properties.put(DpeEvent.CHAUDIERE_ECS, chauffage);
                                        this.actualiseRendementsChauffage(chauffage);
                                    } else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_GAZ_OU_FIOUL_AVEC_CHAUDIERE_BOIS)
                                            && chauffage_properties.containsKey(DpeEvent.CHAUDIERE_GAZ_FIOUL)) {
                                        Chauffage chauffage = (Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_GAZ_FIOUL);
                                        chauffage.setGenereEgalementEcs(true);
                                        ecs_properties.put(DpeEvent.CHAUDIERE_ECS, chauffage);
                                        this.actualiseRendementsChauffage(chauffage);
                                    } else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_AVEC_PAC)
                                            && chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC)){
                                        Chauffage chauffage = (Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC);
                                        chauffage.setGenereEgalementEcs(true);
                                        ecs_properties.put(DpeEvent.CHAUDIERE_ECS, chauffage);
                                        this.actualiseRendementsChauffage(chauffage);
                                    } else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_AVEC_PAC_ET_INSERT_BOIS)
                                            && chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE)){
                                        Chauffage chauffage = (Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE);
                                        chauffage.setGenereEgalementEcs(true);
                                        ecs_properties.put(DpeEvent.CHAUDIERE_ECS, chauffage);
                                        this.actualiseRendementsChauffage(chauffage);
                                    }
                                }
                            }
                            ecs_properties.put(DpeEvent.TYPE_EQUIPEMENT_ECS, typeEquipementEcs);
                            this.actualisePrs1();
                            this.actualisePrs2();
                            this.actualiseRendementEcs(typeEquipementEcs);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {

                            TypeEquipementEcsEnum type = (TypeEquipementEcsEnum) ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS);

                            HashMap<String, Object> currentItems = new HashMap<String, Object>();
                            currentItems.put("lastValue", type);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_EQUIPEMENT_ECS, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case PRESENCE_INSTALLATION_SOLAIRE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            PresenceInstallationSolaireEnum isPresent= (PresenceInstallationSolaireEnum) items.get("lastValue");
                            ecs_properties.put(DpeEvent.PRESENCE_INSTALLATION_SOLAIRE, isPresent);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            PresenceInstallationSolaireEnum type = null;
                            if (ecs_properties.containsKey(DpeEvent.NOMBRE_PERSONNES_DANS_LOGEMENT)){
                                type = (PresenceInstallationSolaireEnum) ecs_properties.get(DpeEvent.PRESENCE_INSTALLATION_SOLAIRE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.PRESENCE_INSTALLATION_SOLAIRE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case LOCAL_EQUIPEMENT_ECS:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            LocalEquipementEcsEnum localEquipementEcs= (LocalEquipementEcsEnum) items.get("lastValue");
                            ecs_properties.put(DpeEvent.LOCAL_EQUIPEMENT_ECS, localEquipementEcs);
                            this.actualiseLocalAllGenerateurs(localEquipementEcs);
                            this.actualisePrs1();
                            this.actualisePrs2();
                            this.tryActualiseRendementEquipementEcs();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            LocalEquipementEcsEnum type = null;
                            if (ecs_properties.containsKey(DpeEvent.LOCAL_EQUIPEMENT_ECS)){
                                type = (LocalEquipementEcsEnum) ecs_properties.get(DpeEvent.LOCAL_EQUIPEMENT_ECS);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.LOCAL_EQUIPEMENT_ECS, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case USAGE_EAU_CHAUDE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            UsageEauChaudeEnum usageEauChaude= (UsageEauChaudeEnum) items.get("lastValue");
                            ecs_properties.put(DpeEvent.USAGE_EAU_CHAUDE, usageEauChaude);
                            this.actualisebecs(usageEauChaude);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            UsageEauChaudeEnum type = null;
                            if (ecs_properties.containsKey(DpeEvent.USAGE_EAU_CHAUDE)){
                                type = (UsageEauChaudeEnum) ecs_properties.get(DpeEvent.USAGE_EAU_CHAUDE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.USAGE_EAU_CHAUDE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case DECLENCHEMENT_CHAUDIERE_ROBINET:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            DeclenchementChaudiereEnum declenchementChaudiere= (DeclenchementChaudiereEnum) items.get("lastValue");
                            ecs_properties.put(DpeEvent.DECLENCHEMENT_CHAUDIERE_ROBINET, declenchementChaudiere);
                            this.tryActualiseIch();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            DeclenchementChaudiereEnum type = null;
                            if (ecs_properties.containsKey(DpeEvent.DECLENCHEMENT_CHAUDIERE_ROBINET)){
                                type = (DeclenchementChaudiereEnum) ecs_properties.get(DpeEvent.DECLENCHEMENT_CHAUDIERE_ROBINET);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.DECLENCHEMENT_CHAUDIERE_ROBINET, currentItems);
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
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            MitoyennetePlancher type = plancher.getMitoyennetePlancher();
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", plancher);
                            Event e2 = new Event(DpeEvent.MITOYENNETE_PLANCHER, currentItems);
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
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            MitoyennetePlafond type = plafond.getMitoyennetePlafond();
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", plafond);
                            Event e2 = new Event(DpeEvent.MITOYENNETE_PLAFOND, currentItems);
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
                            actualiseCoeffDeperditionThermique(plancher,false,true);
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            DateIsolationSlab type = plancher.getDateIsolationPlafond();
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", plancher);
                            Event e2 = new Event(DpeEvent.DATE_ISOLATION_PLANCHER, currentItems);
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
                                this.actualiseSse();
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeVitrageEnum type = fenetre.getTypeVitrage();
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", fenetre);
                                Event e2 = new Event(DpeEvent.TYPE_VITRAGE_MENUISERIE, currentItems);
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
                                EventManager.getInstance().put(Channel.DPE, e2);
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
                        if (!murList.contains(mur)){
                            murList.add(mur);
                            this.actualiseCoeffDeperditionThermique(mur);
                        }
                        break;
                    }
                    case SLAB_AJOUTE: {
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        Slab plancher = (Slab) items.get("userObject");
                        plancher.actualiseSurface();
                        slabList.add(plancher);
                        this.actualiseSH();
                        break;
                    }
                    case FENETRE_AJOUTEE: {
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        Fenetre fenetre = (Fenetre) items.get("userObject");
                        fenetreList.add(fenetre);
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
                        System.out.println(murList.size());
                        for(Mur m : murList){
                            System.out.println(m.toString());
                        }
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        Porte porte = (Porte) items.get("userObject");
                        porteList.add(porte);
                        porte.actualiseCoefficientDeTransmissionThermique();
                        porte.getMur().actualiseSurface();
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
                    case SIZE_MUR_CHANGED:{
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        Mur mur = (Mur) items.get("userObject");
                        mur.actualiseDeperdition();
                    }
                    case MUR_REMOVED:{
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        Mur mur = (Mur) items.get("userObject");
                        if (murList.contains(mur)){
                            murList.remove(mur);
                        }
                    }
                }
            }
        }
    }
}
