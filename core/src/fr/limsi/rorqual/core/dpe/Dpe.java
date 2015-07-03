package fr.limsi.rorqual.core.dpe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

import fr.limsi.rorqual.core.event.*;
import fr.limsi.rorqual.core.model.IfcHelper;
import fr.limsi.rorqual.core.model.IfcHolder;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDoor;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSlab;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWindow;
import ifc2x3javatoolbox.ifcmodel.IfcModel;

/**
 * Created by ricordeau on 20/05/15.
 */
public class Dpe implements EventListener {

    boolean over=false;
    /*** Attributs liés au model IFC***/
    private IfcModel ifcModel;
    private HashMap<IfcWallStandardCase, HashMap<EventType, Object>> walls_properties = new HashMap<IfcWallStandardCase, HashMap<EventType, Object>>();
    private HashMap<IfcSlab, HashMap<EventType, Object>> slabs_properties = new HashMap<IfcSlab, HashMap<EventType, Object>>();
    private Collection<IfcWallStandardCase> wallStandardCaseCollection;
    private Collection<IfcSlab> slabCollection;
    private Collection<IfcWindow> windowCollection;
    private Collection<IfcDoor> doorCollection;

    /*** Attributs liés à l'interface graphique de libGDX ***/
    private Skin skin;
    private BitmapFont fontBlack, fontWhite;
    private Stage stage;
    private TextButton.TextButtonStyle textButtonStyle;

    /*** Attributs liés au calcul du DPE ***/

    // 0.Variables générales
    private enum typeBatiment{
        MAISON,
        APPARTEMENT;
    }
    private enum positionAppartement{
        PREMIER_ETAGE,
        ETAGE_INTERMEDIAIRE,
        DERNIER_ETAGE;
    }
    private enum typeEnergieConstruction{
        ELECTRIQUE,
        AUTRE;
    }
    private enum deperditionsPlancherBasAppartement{
        EXT,
        LNC,
        AH,
        TP;
    }
    private enum deperditionsPlancherHautAppartement{
        EXT,
        LNC,
        AH,
        TOITURE;
    }
    private enum typeVentilation{
        NATURELLE,
        MECANIQUE,
        INCONNUE
    }
    private enum typeVMC{
        VMC_AUTOREGLABLE_AVANT_1982,
        VMC_AUTOREGLABLE_APRES_1982,
        VMC_HYGRO_B,
        VMC_DOUBLE_FLUX,
        INCONNUE;
    }
    private typeBatiment typeBatiment;
    private positionAppartement positionAppartement;
    private typeEnergieConstruction typeEnergieConstruction;
//    private type
    private double anneeConstruction;
    private double SH;
    private double NIV;
    private double MIT;
    private double MIT2;
    private double FOR;
    private double Per;
    private double PER;
    private double C_niv;
    private String configuration_Appartement;
    private List<deperditionsPlancherBasAppartement> deperditionsPlancherBasAppartementList;
    private List<deperditionsPlancherHautAppartement> deperditionsPlancherHautAppartementList;

    // 1.Expression du besoin de chauffage
    private double BV;
    private double GV;
    private double F;

    // 2.Calcul des déperditions de l'enveloppe GV
    private double DP_murExt;
    private double DP_murLnc;
    private double DP_murAh;
    private double DP_murVer;
    private double DP_toiTer;
    private double DP_toiCp;
    private double DP_toiCa;
    private double DP_planVs;
    private double DP_planTp;
    private double DP_planSs;
    private double DP_planAh;
    private double DP_fen;
    private double DP_pfen;
    private double DP_fenVer;
    private double DP_pfenVer;
    private double DP_portExt;
    private double DP_portLnc;
    private double DP_portVer;
    private double PT;
    private double DR;
    private double Tint;
    private double Sdep;

    /*** Constructeur ***/
    public Dpe (Stage stageMenu) {
        fontBlack = new BitmapFont(Gdx.files.internal("data/font/black.fnt"));
        fontWhite = new BitmapFont(Gdx.files.internal("data/font/white.fnt"));
        skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));
        textButtonStyle = new TextButton.TextButtonStyle(skin.getDrawable("default-round"),skin.getDrawable("default-round-down"),null,fontBlack);
        ifcModel = IfcHolder.getInstance().getIfcModel();
        stage = stageMenu;
        wallStandardCaseCollection = ifcModel.getCollection(IfcWallStandardCase.class);
        slabCollection = ifcModel.getCollection(IfcSlab.class);
        windowCollection = ifcModel.getCollection(IfcWindow.class);
        doorCollection = ifcModel.getCollection(IfcDoor.class);
        SH = IfcHelper.calculSurfaceHabitable(ifcModel);
        PER = IfcHelper.calculPerimetreBatiment(ifcModel);
        deperditionsPlancherBasAppartementList = new ArrayList<>();
        deperditionsPlancherHautAppartementList = new ArrayList<>();
        EventManager.getInstance().addListener(Channel.DPE, this);
    }

    /*---------------------------------Calculateur DPE-------------------------------------------*/

    public void calc_BV() {
        BV = GV*(1-F);
    }

    public void calc_GV() {
        GV = DP_murExt + DP_murLnc + DP_murAh + DP_murVer + DP_toiTer + DP_toiCp + DP_toiCa + DP_planVs + DP_planTp + DP_planSs + DP_planAh + DP_fen + DP_pfen + DP_fenVer + DP_pfenVer + DP_portExt + DP_portLnc + DP_portVer + PT + DR;
    }

    public void calc_Sdep(){
        Sdep=0;
        for (IfcWindow actualWindow : windowCollection){
            Sdep += IfcHelper.getWindowSurface(ifcModel,actualWindow);
        }
    }

    public void calc_MIT2(){
        if (FOR == 4.12){ // Forme carré ou rectangulaire de la maison
            if (MIT == 1){MIT2=1;}
            else if (MIT == 0.8){MIT2=0.8;}
            else if (MIT == 0.7){MIT2=0.675;}
            else if (MIT == 0.5){MIT2=0.5;}
            else if (MIT == 0.35){MIT2=0.35;}
        }
        else if (FOR == 4.81){ // Forme allongé de la maison
            if (MIT == 1){MIT2=1;}
            else if (MIT == 0.8){MIT2=0.9;}
            else if (MIT == 0.7){MIT2=0.725;}
            else if (MIT == 0.5){MIT2=0.55;}
            else if (MIT == 0.35){MIT2=0.4;}
        }
        else if (FOR == 5.71){ // Forme développé de la maison
            if (MIT == 1){MIT2=1;}
            else if (MIT == 0.8){MIT2=0.9;}
            else if (MIT == 0.7){MIT2=0.75;}
            else if (MIT == 0.5){MIT2=0.7;}
            else if (MIT == 0.35){MIT2=0.55;}
        }
    }

    public void calc_C_niv(){
        if (NIV==1){C_niv=0;}
        else if (NIV==1.5){C_niv=1;}
        else if (NIV==2){C_niv=1;}
        else if (NIV==2.5){C_niv=2;}
        else if (NIV==3){C_niv=2;}
    }

    public double calc_lRfm(){
        double lRfm=0;
        if (SH<90){
            if (NIV==1){
                if (FOR==4.12){lRfm=2;}
                else if (FOR==4.81){lRfm=4;}
                else if (FOR==5.71){lRfm=6;}
            }
            else if (NIV==1.5){
                if (FOR==4.12){lRfm=2;}
                else if (FOR==4.81){lRfm=4;}
                else if (FOR==5.71){lRfm=6;}
            }
            else if (NIV==2){
                lRfm=0;
            }
            else if (NIV>=2.5){
                lRfm=0;
            }
        }
        else if (SH>90 && SH<160){
            if (NIV==1){
                if (FOR==4.12){lRfm=2;}
                else if (FOR==4.81){lRfm=4;}
                else if (FOR==5.71){lRfm=6;}
            }
            else if (NIV==1.5){
                if (FOR==4.12){lRfm=2;}
                else if (FOR==4.81){lRfm=4;}
                else if (FOR==5.71){lRfm=6;}
            }
            else if (NIV==2){
                if (FOR==4.12){lRfm=4;}
                else if (FOR==4.81){lRfm=8;}
                else if (FOR==5.71){lRfm=12;}
            }
            else if (NIV>=2.5){
                lRfm=0;
            }
        }
        else if (SH>160){
            if (NIV==1){
                if (FOR==4.12){lRfm=2;}
                else if (FOR==4.81){lRfm=4;}
                else if (FOR==5.71){lRfm=6;}
            }
            else if (NIV==1.5){
                if (FOR==4.12){lRfm=2;}
                else if (FOR==4.81){lRfm=4;}
                else if (FOR==5.71){lRfm=6;}
            }
            else if (NIV==2){
                if (FOR==4.12){lRfm=4;}
                else if (FOR==4.81){lRfm=8;}
                else if (FOR==5.71){lRfm=12;}
            }
            else if (NIV>=2.5){
                if (FOR==4.12){lRfm=4;}
                else if (FOR==4.81){lRfm=8;}
                else if (FOR==5.71){lRfm=12;}
            }
        }
        return lRfm;
    }

    public double calc_lMen(){
        double lMen=0,largeurFenetre=0,hauteurFenetre=0,largeurPorte=0,hauteurPorte=0;

        for (IfcWindow actualWindow : windowCollection){
            largeurFenetre=IfcHelper.getWindowWidth(ifcModel,actualWindow);
            hauteurFenetre=IfcHelper.getWindowHeight(ifcModel,actualWindow);
            lMen += (2*largeurFenetre+2*hauteurFenetre);
        }

        for (IfcDoor actualDoor : doorCollection){
            largeurPorte=IfcHelper.getDoorWidth(ifcModel,actualDoor);
            hauteurPorte=IfcHelper.getDoorHeight(ifcModel,actualDoor);
            lMen += (2*largeurPorte+2*hauteurPorte);
        }
        return lMen;
    }

    /*** Regarde l'ensemble des murs extérieurs et retourne l'isolation la plus répandue ***/
    public String calc_type_iso_mur_exterieur() {
        int nbWallNonIsole=0, nbWallITI=0, nbWallITE=0, nbWallITR=0;
        String isolationType="";
        for(IfcWallStandardCase actualWall : wallStandardCaseCollection){
            if (IfcHelper.getPropertyTypeWall(actualWall).equals("ext")){
                isolationType = IfcHelper.getPropertyTypeIsolation(actualWall);
                switch(isolationType){
                    case "sans" :
                        nbWallNonIsole++;
                        break;
                    case "ITI" :
                        nbWallITI++;
                        break;
                    case "ITE" :
                        nbWallITE++;
                        break;
                    case "ITR" :
                        nbWallITR++;
                        break;
                }
            }
        }
        if (nbWallNonIsole >= nbWallITI && nbWallNonIsole >= nbWallITE && nbWallNonIsole >= nbWallITR){isolationType = "sans";}
        else if (nbWallITI > nbWallNonIsole && nbWallITI >= nbWallITE && nbWallITI >= nbWallITR){isolationType="ITI";}
        else if (nbWallITE > nbWallNonIsole && nbWallITE > nbWallITI && nbWallITE >= nbWallITR){isolationType="ITE";}
        else if (nbWallITR > nbWallNonIsole && nbWallITR >= nbWallITI && nbWallITR > nbWallITE){isolationType="ITR";}
        return isolationType;
    }

    /*** Regarde l'ensemble des planchers bas et retourne l'isolation la plus répandue ***/
    public String calc_type_iso_planchers_bas() {
        int nbSlabNonIsole=0, nbSlabITI=0, nbSlabITE=0, nbSlabITR=0;
        String isolationType="";
        List<IfcSlab> slabList = IfcHelper.getSlabsRelToFirstStage(ifcModel);
        for(IfcSlab actualSlab : slabList){
            isolationType = IfcHelper.getPropertyTypeIsolation(actualSlab);
            switch(isolationType){
                case "sans" :
                    nbSlabNonIsole++;
                    break;
                case "ITI" :
                    nbSlabITI++;
                    break;
                case "ITE" :
                    nbSlabITE++;
                    break;
                case "ITR" :
                    nbSlabITR++;
                    break;
            }
        }
        if (nbSlabNonIsole >= nbSlabITI && nbSlabNonIsole >= nbSlabITE && nbSlabNonIsole >= nbSlabITR){isolationType = "sans";}
        else if (nbSlabITI > nbSlabNonIsole && nbSlabITI >= nbSlabITE && nbSlabITI >= nbSlabITR){isolationType="ITI";}
        else if (nbSlabITE > nbSlabNonIsole && nbSlabITE > nbSlabITI && nbSlabITE >= nbSlabITR){isolationType="ITE";}
        else if (nbSlabITR > nbSlabNonIsole && nbSlabITR >= nbSlabITI && nbSlabITR > nbSlabITE){isolationType="ITR";}
        return isolationType;
    }

    public double getUmurInconnu(IfcWallStandardCase wall){
        double u=-1;
        if (anneeConstruction<1975){
            u = 2.5;
            IfcHelper.addPropertyTypeIsolation(ifcModel, wall, "sans");
        }
        else if (anneeConstruction>=1975 && anneeConstruction<=1977){
            u = 1;
            IfcHelper.addPropertyTypeIsolation(ifcModel,wall,"ITI");
        }
        else if (anneeConstruction>=1978 && anneeConstruction<=1982){
            if(typeEnergieConstruction.equals(typeEnergieConstruction.ELECTRIQUE)){
                u=0.8;
                IfcHelper.addPropertyTypeIsolation(ifcModel,wall,"ITI");
            }
            else{
                u=1;
                IfcHelper.addPropertyTypeIsolation(ifcModel,wall,"ITI");
            }
        }
        else if (anneeConstruction>=1983 && anneeConstruction<=1988){
            if(typeEnergieConstruction.equals(typeEnergieConstruction.ELECTRIQUE)){
                u=0.7;
                IfcHelper.addPropertyTypeIsolation(ifcModel,wall,"ITI");
            }
            else{
                u=0.8;
                IfcHelper.addPropertyTypeIsolation(ifcModel,wall,"ITI");
            }
        }
        else if (anneeConstruction>=1989 && anneeConstruction<=2000){
            if(typeEnergieConstruction.equals(typeEnergieConstruction.ELECTRIQUE)){
                u=0.45;
                IfcHelper.addPropertyTypeIsolation(ifcModel,wall,"ITI");
            }
            else{
                u=0.5;
                IfcHelper.addPropertyTypeIsolation(ifcModel,wall,"ITI");
            }
        }
        else if (anneeConstruction>=2001 && anneeConstruction<=2005){
            u = 0.4;
            IfcHelper.addPropertyTypeIsolation(ifcModel,wall,"ITI");
        }
        else if (anneeConstruction>=2006 && anneeConstruction<=2012){
            u = 0.35;
            IfcHelper.addPropertyTypeIsolation(ifcModel,wall,"ITE");
        }
        else if (anneeConstruction>2012){
            u = 0.2;
            IfcHelper.addPropertyTypeIsolation(ifcModel,wall,"ITE");
        }
        return u;
    }

    public double getUslabInconnu(IfcSlab slab){
        double u=-1;
        if (anneeConstruction<1975){
            u = 2;
            IfcHelper.addPropertyTypeIsolation(ifcModel, slab, "sans");
        }
        else if (anneeConstruction>=1975 && anneeConstruction<=1977){
            u = 0.9;
            IfcHelper.addPropertyTypeIsolation(ifcModel,slab,"ITI");
        }
        else if (anneeConstruction>=1978 && anneeConstruction<=1982){
            if(typeEnergieConstruction.equals(typeEnergieConstruction.ELECTRIQUE)){
                u=0.8;
                IfcHelper.addPropertyTypeIsolation(ifcModel,slab,"ITI");
            }
            else{
                u=0.9;
                IfcHelper.addPropertyTypeIsolation(ifcModel,slab,"ITI");
            }
        }
        else if (anneeConstruction>=1983 && anneeConstruction<=1988){
            if(typeEnergieConstruction.equals(typeEnergieConstruction.ELECTRIQUE)){
                u=0.55;
                IfcHelper.addPropertyTypeIsolation(ifcModel,slab,"ITI");
            }
            else{
                u=0.7;
                IfcHelper.addPropertyTypeIsolation(ifcModel,slab,"ITI");
            }
        }
        else if (anneeConstruction>=1989 && anneeConstruction<=2000){
            if(typeEnergieConstruction.equals(typeEnergieConstruction.ELECTRIQUE)){
                u=0.55;
                IfcHelper.addPropertyTypeIsolation(ifcModel,slab,"ITI");
            }
            else{
                u=0.6;
                IfcHelper.addPropertyTypeIsolation(ifcModel,slab,"ITI");
            }
        }
        else if (anneeConstruction>=2001 && anneeConstruction<=2005){
            u = 0.3;
            IfcHelper.addPropertyTypeIsolation(ifcModel,slab,"ITI");
        }
        else if (anneeConstruction>=2006 && anneeConstruction<=2012){
            u = 0.27;
            IfcHelper.addPropertyTypeIsolation(ifcModel,slab,"ITI");
        }
        else if (anneeConstruction>2012){
            u = 0.22;
            IfcHelper.addPropertyTypeIsolation(ifcModel,slab,"ITI");
        }
        return u;
    }

    public double getUmur(boolean isAnneeIsolationConnue,double anneeIsolation, IfcWallStandardCase wall){
        double u=-1;
        if (isAnneeIsolationConnue && anneeIsolation!=anneeConstruction){
            if (anneeIsolation<1983){
                u = 0.82;
                IfcHelper.addPropertyTypeIsolation(ifcModel,wall,"ITI");
            }
            else if (anneeIsolation>=1983 && anneeIsolation<=1988){
                u = 0.75;
                IfcHelper.addPropertyTypeIsolation(ifcModel,wall,"ITI");
            }
            else if (anneeIsolation>=1989 && anneeIsolation<=2000){
                u = 0.48;
                IfcHelper.addPropertyTypeIsolation(ifcModel,wall,"ITI");
            }
            else if (anneeIsolation>=2001 && anneeIsolation<=2005){
                u = 0.42;
                IfcHelper.addPropertyTypeIsolation(ifcModel,wall,"ITI");
            }
            else if (anneeIsolation>=2006 && anneeIsolation<=2012){
                u = 0.36;
                IfcHelper.addPropertyTypeIsolation(ifcModel,wall,"ITI");
            }
            else if (anneeIsolation>2012){
                u = 0.24;
                IfcHelper.addPropertyTypeIsolation(ifcModel,wall,"ITI");
            }
        }
        else if (isAnneeIsolationConnue && anneeIsolation==anneeConstruction){
            if (anneeConstruction<1974){
                u=0.8;
                IfcHelper.addPropertyTypeIsolation(ifcModel,wall,"ITI");
            }else{
                u=this.getUmurInconnu(wall);
            }
        }
        else if(!isAnneeIsolationConnue){
            u=this.getUmurInconnu(wall);
        }
        return u;
    }

    public double getUslab(boolean isAnneeIsolationConnue,double anneeIsolation, IfcSlab slab){
        double u=-1;
        if (isAnneeIsolationConnue && anneeIsolation!=anneeConstruction){
            if (anneeIsolation<1983){
                u = 0.85;
                IfcHelper.addPropertyTypeIsolation(ifcModel,slab,"ITE");
            }
            else if (anneeIsolation>=1983 && anneeIsolation<=1988){
                u = 0.6;
                IfcHelper.addPropertyTypeIsolation(ifcModel,slab,"ITE");
            }
            else if (anneeIsolation>=1989 && anneeIsolation<=2000){
                u = 0.55;
                IfcHelper.addPropertyTypeIsolation(ifcModel,slab,"ITE");
            }
            else if (anneeIsolation>=2001 && anneeIsolation<=2005){
                u = 0.3;
                IfcHelper.addPropertyTypeIsolation(ifcModel,slab,"ITE");
            }
            else if (anneeIsolation>=2006 && anneeIsolation<=2012){
                u = 0.27;
                IfcHelper.addPropertyTypeIsolation(ifcModel,slab,"ITE");
            }
            else if (anneeIsolation>2012){
                u = 0.24;
                IfcHelper.addPropertyTypeIsolation(ifcModel,slab,"ITE");
            }
        }
        else if (isAnneeIsolationConnue && anneeIsolation==anneeConstruction){
            if (anneeConstruction<1974){
                u=0.8;
                IfcHelper.addPropertyTypeIsolation(ifcModel,slab,"ITI");
            }else{
                u=this.getUslabInconnu(slab);
            }
        }
        else if(!isAnneeIsolationConnue){
            u=this.getUslabInconnu(slab);
        }
        return u;
    }

    public void tryActualiseMurDP(IfcWallStandardCase wall) {
        if (!walls_properties.get(wall).containsKey(DpeEvent.DERRIERE_MUR_RESPONSE))
            return;
        String derriere = (String)walls_properties.get(wall).get(DpeEvent.DERRIERE_MUR_RESPONSE);

        if (!walls_properties.get(wall).containsKey(DpeEvent.ISOLATION_MUR_RESPONSE))
            return;
        String isolation = (String)walls_properties.get(wall).get(DpeEvent.ISOLATION_MUR_RESPONSE);

        if (isolation=="oui") {
            if (!walls_properties.get(wall).containsKey(DpeEvent.DATE_ISOLATION_MUR_RESPONSE))
                return;
            Object item[] = (Object[]) walls_properties.get(wall).get(DpeEvent.DATE_ISOLATION_MUR_RESPONSE);
            actualiseDP_mur(wall, derriere, isolation, (boolean) item[0], (double) item[1]);
            Object res[] = {wall, DpeState.KNOWN};
            Event stateChange = new Event(DpeEvent.DPE_STATE_CHANGED, res);
            EventManager.getInstance().put(Channel.DPE, stateChange);
        } else {
            actualiseDP_mur(wall, derriere, isolation, false, 0);
            Object res[] = {wall, DpeState.KNOWN};
            Event stateChange = new Event(DpeEvent.DPE_STATE_CHANGED, res);
            EventManager.getInstance().put(Channel.DPE, stateChange);
        }
    }

    public void tryActualiseSlabDP(IfcSlab slab) {
        if (!slabs_properties.get(slab).containsKey(DpeEvent.DERRIERE_SLAB_RESPONSE))
            return;
        String derriere = (String)slabs_properties.get(slab).get(DpeEvent.DERRIERE_SLAB_RESPONSE);

        if (!slabs_properties.get(slab).containsKey(DpeEvent.ISOLATION_SLAB_RESPONSE))
            return;
        String isolation = (String)slabs_properties.get(slab).get(DpeEvent.ISOLATION_SLAB_RESPONSE);

        if (isolation=="oui") {
            if (!slabs_properties.get(slab).containsKey(DpeEvent.DATE_ISOLATION_SLAB_RESPONSE))
                return;
            Object item[] = (Object[]) slabs_properties.get(slab).get(DpeEvent.DATE_ISOLATION_SLAB_RESPONSE);
            actualiseDP_slab(slab, derriere, isolation, (boolean) item[0], (double) item[1]);
            Object res[] = {slab, DpeState.KNOWN};
            Event stateChange = new Event(DpeEvent.DPE_STATE_CHANGED, res);
            EventManager.getInstance().put(Channel.DPE, stateChange);
        } else {
            actualiseDP_slab(slab, derriere, isolation, false, 0);
            Object res[] = {slab, DpeState.KNOWN};
            Event stateChange = new Event(DpeEvent.DPE_STATE_CHANGED, res);
            EventManager.getInstance().put(Channel.DPE, stateChange);
        }
    }

    public void actualiseDP_mur(IfcWallStandardCase wall, String derriere,String isole,boolean isAnneeIsolationConnue,double anneeIsolation){
        double surface=IfcHelper.getWallSurface(wall);
        double u=0,b=0;
        switch (isole){
            case "oui":
                u=getUmur(isAnneeIsolationConnue,anneeIsolation,wall);
                break;
            case "non":
                u=2.5;
                break;
            case "inconnue":
                u=getUmurInconnu(wall);
                break;
        }
        switch (derriere){
            case "ext":
                DP_murExt += surface*u;
                break;
            case "lnc":
                if (isole=="non"){b=0.95;}
                else {b=0.85;}
                DP_murLnc += surface*u*b;
                break;
            case "ah":
                DP_murAh += surface*u*0.2;
                break;
            case "ver":
                DP_murVer += surface*u*0.6;
                break;
        }
        System.out.println("Aire = "+surface);
        System.out.println("U = " + u);
    }

    public void actualiseDP_slab(IfcSlab slab, String derriere,String isole,boolean isAnneeIsolationConnue,double anneeIsolation){
        double surface = IfcHelper.getSlabSurface(slab);
        double u=0,b=0;
        if (!derriere.equals("tp")){
            switch (isole){
                case "oui":
                    u=getUslab(isAnneeIsolationConnue, anneeIsolation, slab);
                    break;
                case "non":
                    u=2.5;
                    break;
                case "inconnue":
                    u=getUslabInconnu(slab);
                    break;
            }
            switch (derriere){
                case "vs":
                    DP_planVs += surface*u*0.8;
                    break;
                case "ss":
                    if (isole=="non"){b=0.95;}
                    else {b=0.85;}
                    DP_planSs += surface*u*b;
                    break;
                case "ah":
                    DP_planAh += surface*u*0.2;
                    break;
            }
        }else{ // Uplan = terre plein
            if (typeBatiment.equals(typeBatiment.MAISON)){
                Per = FOR*MIT2*Math.sqrt((SH/NIV));
            }else{
                Per = 2*(SH/PER+PER);
            }
            double variableTest = Math.round(2*surface/Per);
            if(variableTest<=3){u = 0.25;}
            else if(variableTest==4){u = 0.23;}
            else if(variableTest==5){u = 0.21;}
            else if(variableTest==6){u = 0.19;}
            else if(variableTest==7){u = 0.18;}
            else if(variableTest==8){u = 0.17;}
            else if(variableTest==9){u = 0.16;}
            else if(variableTest==10){u = 0.15;}
            else if(variableTest==11){u = 0.15;}
            else if(variableTest==12){u = 0.14;}
            else if(variableTest==13){u = 0.13;}
            else if(variableTest==14){u = 0.12;}
            else if(variableTest==15){u = 0.12;}
            else if(variableTest==16){u = 0.11;}
            else if(variableTest==17){u = 0.11;}
            else if(variableTest==18){u = 0.11;}
            else if(variableTest==19){u = 0.1;}
            else if(variableTest>=20){u = 0.1;}
            DP_planTp += surface*u;
        }
        System.out.println("Aire = "+surface);
        System.out.println("U = " + u);
    }

    public void calc_PT() {
        if (typeBatiment == typeBatiment.MAISON) {
            double kPbM = 0, lPbM = 0, kPhM = 0.54, lPhM = 0, kPiM = 0, lPiM = 0, kRfM = 0, lRfM = 0, kMen = 0, lMen = 0;
            calc_C_niv();
            lPbM = FOR * MIT2 * Math.sqrt(SH / NIV);
            lPhM = FOR * MIT2 * Math.sqrt(SH / NIV);
            lPiM = C_niv * FOR * MIT * Math.sqrt(SH / NIV);
            lRfM = calc_lRfm();
            lMen = calc_lMen();
            String typeIsolationMur = calc_type_iso_mur_exterieur();
            String typeIsolationSlab = calc_type_iso_planchers_bas();
            switch (typeIsolationMur){
                case "sans" :
                    kPiM=0.86;
                    kRfM=0.73;
                    kMen=0.38;
                    switch (typeIsolationSlab){
                        case "sans":
                            kPbM=0.39;
                            break;
                        case "ITI":
                            kPbM=0.47;
                            break;
                        case "ITE":
                            kPbM=0.8;
                            break;
                    }
                    break;
                case "ITI" :
                    kPiM=0.92;
                    kRfM=0.82;
                    kMen=0;
                    switch (typeIsolationSlab){
                        case "sans":
                            kPbM=0.31;
                            break;
                        case "ITI":
                            kPbM=0.08;
                            break;
                        case "ITE":
                            kPbM=0.71;
                            break;
                    }
                    break;
                case "ITE" :
                    kPiM=0.13;
                    kRfM=0.13;
                    kMen=0.25;
                    switch (typeIsolationSlab){
                        case "sans":
                            kPbM=0.49;
                            break;
                        case "ITI":
                            kPbM=0.48;
                            break;
                        case "ITE":
                            kPbM=0.64;
                            break;
                    }
                    break;
                case "ITR" :
                    kPiM=0.24;
                    kRfM=0.2;
                    kMen=0.2;
                    switch (typeIsolationSlab){
                        case "sans":
                            kPbM=0.35;
                            break;
                        case "ITI":
                            kPbM=0.1;
                            break;
                        case "ITE":
                            kPbM=0.45;
                            break;
                    }
                    break;
            }
            PT = kPbM * lPbM + kPhM * lPhM + kPiM * lPiM + kRfM * lRfM + kMen * lMen;
            System.out.println("kPbm = "+kPbM);
            System.out.println("lPbm = "+lPbM);
            System.out.println("kPhm = "+kPhM);
            System.out.println("lPhm = "+lPhM);
            System.out.println("kPim = "+kPiM);
            System.out.println("lPim = "+lPiM);
            System.out.println("kRfM = "+kRfM);
            System.out.println("lRfm = "+lRfM);
            System.out.println("kMen = "+kMen);
            System.out.println("lMen = "+lMen);
        }else{ // type de batiment = appartement
            double lPbeMe=0,kPbeMe=0,lPbiMe=0,kPbiMe=0,lTpMe=0,kTpMe=0,lPibMe=0,kPibMe=0,lPihMe=0,kPihMe=0,lTteMe=0,kTteMe=0,lTtiMe=0,kTtiMe=0,lTcMe=0,kTcMe=0,lRfMe=0,kRfMe=0;

            switch (configuration_Appartement){
                case "1":
                    lPbeMe=0;lPbiMe=0;lTpMe=0;lPibMe=1;lPihMe=0;lTteMe=0;lTtiMe=0;lTcMe=1;lRfMe=0.4;
                    break;
                case "2":
                    lPbeMe=0;lPbiMe=0;lTpMe=0;lPibMe=1;lPihMe=0;lTteMe=0.5;lTtiMe=0;lTcMe=0.5;lRfMe=0.4;
                    break;
                case "3":
                    lPbeMe=0;lPbiMe=0;lTpMe=0;lPibMe=1;lPihMe=0;lTteMe=1;lTtiMe=0;lTcMe=0;lRfMe=0.4;
                    break;
                case "4":
                    lPbeMe=0;lPbiMe=0;lTpMe=0;lPibMe=1;lPihMe=0;lTteMe=1;lTtiMe=1;lTcMe=0;lRfMe=0.4;
                    break;
                case "5":
                    lPbeMe=0;lPbiMe=0;lTpMe=0;lPibMe=1;lPihMe=0;lTteMe=1;lTtiMe=1;lTcMe=0;lRfMe=0.4;
                    break;
                case "6":
                    lPbeMe=1;lPbiMe=1;lTpMe=0;lPibMe=0;lPihMe=0;lTteMe=1;lTtiMe=1;lTcMe=0;lRfMe=0.4;
                    break;
                case "7":
                    lPbeMe=0;lPbiMe=0;lTpMe=0;lPibMe=1;lPihMe=1;lTteMe=0;lTtiMe=0;lTcMe=0;lRfMe=0.4;
                    break;
                case "8":
                    lPbeMe=1;lPbiMe=1;lTpMe=0;lPibMe=0;lPihMe=1;lTteMe=0;lTtiMe=0;lTcMe=0;lRfMe=0.4;
                    break;
                case "9":
                    lPbeMe=1;lPbiMe=1;lTpMe=0;lPibMe=0;lPihMe=1;lTteMe=0;lTtiMe=0;lTcMe=0;lRfMe=0.4;
                    break;
                case "10":
                    lPbeMe=1;lPbiMe=0;lTpMe=0;lPibMe=0;lPihMe=1;lTteMe=0;lTtiMe=0;lTcMe=0;lRfMe=0.4;
                    break;
                case "10b":
                    lPbeMe=0;lPbiMe=0;lTpMe=1;lPibMe=0;lPihMe=1;lTteMe=0;lTtiMe=0;lTcMe=0;lRfMe=0.4;
                    break;
                case "11":
                    lPbeMe=1;lPbiMe=1;lTpMe=0;lPibMe=0;lPihMe=1;lTteMe=0;lTtiMe=0;lTcMe=0;lRfMe=0.4;
                    break;
                case "11b":
                    lPbeMe=1;lPbiMe=0;lTpMe=1;lPibMe=0;lPihMe=1;lTteMe=0;lTtiMe=0;lTcMe=0;lRfMe=0.4;
                    break;
                case "12":
                    lPbeMe=1;lPbiMe=1;lTpMe=0;lPibMe=0;lPihMe=0;lTteMe=1;lTtiMe=1;lTcMe=0;lRfMe=0.4;
                    break;
                case "13":
                    lPbeMe=1;lPbiMe=1;lTpMe=0;lPibMe=0;lPihMe=0;lTteMe=1;lTtiMe=1;lTcMe=0;lRfMe=0.4;
                    break;
                case "14":
                    lPbeMe=1;lPbiMe=1;lTpMe=0;lPibMe=0;lPihMe=0;lTteMe=1;lTtiMe=1;lTcMe=0;lRfMe=0.4;
                    break;
            }
            if (anneeConstruction<=1982){kTpMe=2;}
            else{kTpMe=1.45;}
            String isolationMur = this.calc_type_iso_mur_exterieur();
            switch (isolationMur) {
                case "ITI":
                    kPbeMe=0.55;kPbiMe=0.4;kPibMe=0.6;kPihMe=0.6;kTteMe=0.5;kTtiMe=0.5;kTcMe=0;kRfMe=0.5;
                    break;
                case "ITE":
                    kPbeMe=0.8;kPbiMe=0.1;kPibMe=0.1;kPihMe=0.1;kTteMe=0.8;kTtiMe=0.1;kTcMe=0.5;kRfMe=0.1;
                    break;
                default:
                    kPbeMe=0.8;kPbiMe=0.1;kPibMe=0.1;kPihMe=0.1;kTteMe=0.8;kTtiMe=0.1;kTcMe=0.5;kRfMe=0.1;
                    break;
            }
            PT = PER*(lPbeMe*kPbeMe + lPbiMe*kPbiMe + lTpMe*kTpMe + lPibMe*kPibMe + lPihMe*kPihMe + lTteMe*kTteMe + lTtiMe*kTtiMe + lTcMe*kTcMe + lRfMe*kRfMe);
            System.out.println("lPbeMe = "+lPbeMe);
            System.out.println("kPbeMe = "+kPbeMe);
            System.out.println("lPbiMe = "+lPbiMe);
            System.out.println("kPbiMe = "+kPbiMe);
            System.out.println("lTpMe = "+lTpMe);
            System.out.println("kTpMe = "+kTpMe);
            System.out.println("lPibMe = "+lPibMe);
            System.out.println("kPibMe = "+kPibMe);
            System.out.println("lPihMe = "+lPihMe);
            System.out.println("kPihMe = "+kPihMe);
            System.out.println("lTteMe = "+lTteMe);
            System.out.println("kTteMe = "+kTteMe);
            System.out.println("lTtiMe = "+lTtiMe);
            System.out.println("kTtiMe = "+kTtiMe);
            System.out.println("lTcMe = "+lTcMe);
            System.out.println("kTcMe = "+kTcMe);
            System.out.println("lRfMe = "+lRfMe);
            System.out.println("kRfMe = "+kRfMe);
        }
    }

    public void calc_DR() {
        double Sdep=0,Q4PaConv=0,Q4PaEnv=0,Smea=0,Q4Pa=0;
    }

    /*---------------------------------------IHM------------------------------------------------*/

    /*** Explique ce qu'est le DPE et demande de continuer ou non ***/
    public void startDPE() {
        notifierMurs();
        notifierPlanchers();
        notifierFenetres();
        DpeEvent eventType = DpeEvent.START_DPE;
        Event event = new Event(eventType);
        EventManager.getInstance().put(Channel.DPE, event);
    }

    /*** Demande le type de batiment ***/
    public void demandeTypeBatiment() {
        DpeEvent eventType = DpeEvent.TYPE_BATIMENT;
        Event event = new Event(eventType);
        EventManager.getInstance().put(Channel.DPE, event);
    }

    /*** Demande le nombre de niveaux ***/
    public void demandeNbNiveau(){
        DpeEvent eventType = DpeEvent.NB_NIVEAUX;
        Event event = new Event(eventType);
        EventManager.getInstance().put(Channel.DPE, event);
    }

    /*** Demande la forme du batiment ***/
    public void demandeForme(){
        DpeEvent eventType = DpeEvent.FORME;
        Event event = new Event(eventType);
        EventManager.getInstance().put(Channel.DPE, event);
    }

    /*** Demande la mitoyenneté d'une maison ***/
    public void demandeMitoyennete(){
        DpeEvent eventType = DpeEvent.MITOYENNETE;
        Event event = new Event(eventType);
        EventManager.getInstance().put(Channel.DPE, event);
    }

    /*** Demande la la position d'un appartement ***/
    public void demandePositionAppartement() {
        DpeEvent eventType = DpeEvent.POSITION_APPARTEMENT;
        Event event = new Event(eventType);
        EventManager.getInstance().put(Channel.DPE, event);
    }

    /*** Demande l'année de construction d'une maison ou d'un appartement ***/
    public void demandeAnneeConstruction() {
        DpeEvent eventType = DpeEvent.ANNEE_CONSTRUCTION;
        Event event = new Event(eventType);
        EventManager.getInstance().put(Channel.DPE, event);
    }

    /*** Demande le type d'énergie à la construction ***/
    public void demandeTypeEnergieConstruction() {
        DpeEvent eventType = DpeEvent.ENERGIE_CONSTRUCTION;
        Event event = new Event(eventType);
        EventManager.getInstance().put(Channel.DPE, event);
    }

    /*** Notifie le statut des planchers ***/
    public void notifierPlanchers() {
        IfcSlab slab;
        Iterator<IfcSlab> it = slabCollection.iterator();
        while (it.hasNext()) {
            slab = it.next();
            Object o[] = {slab, DpeState.UNKNOWN};
            Event e = new Event(DpeEvent.DPE_STATE_CHANGED, o);
            EventManager.getInstance().put(Channel.DPE, e);
        }
    }

    /*** Notifie le statut des murs ***/
    public void notifierMurs() {
        IfcWallStandardCase wall;
        Iterator<IfcWallStandardCase> it = wallStandardCaseCollection.iterator();
        while (it.hasNext()) {
            wall = it.next();
            if(IfcHelper.getPropertyTypeWall(wall).equals("ext")){
                Object o[] = {wall, DpeState.UNKNOWN};
                Event e = new Event(DpeEvent.DPE_STATE_CHANGED, o);
                EventManager.getInstance().put(Channel.DPE, e);
            }
        }
    }

    /*** Notifie le statut des fenetres ***/
    public void notifierFenetres() {
        IfcWindow window;
        Iterator<IfcWindow> it = windowCollection.iterator();
        while (it.hasNext()) {
            window = it.next();
            Object o[] = {window, DpeState.UNKNOWN};
            Event e = new Event(DpeEvent.DPE_STATE_CHANGED, o);
            EventManager.getInstance().put(Channel.DPE, e);
        }
    }

    /*** Demande à l'utilisateur des informations permettant de déterminer les déperditions au niveau des murs ***/
    public void demanderMur(IfcWallStandardCase wall) {
        walls_properties.put(wall, new HashMap<EventType, Object>());
        DpeEvent eventType = DpeEvent.DERRIERE_MUR;
        Event event = new Event(eventType, wall);
        EventManager.getInstance().put(Channel.DPE, event);

        eventType = DpeEvent.ISOLATION_MUR;
        event = new Event(eventType, wall);
        EventManager.getInstance().put(Channel.DPE, event);
    }

    /*** Demande à l'utilisateur des informations permettant de déterminer les déperditions au niveau des slabs ***/
    public void demanderSlab(IfcSlab slab){
        slabs_properties.put(slab, new HashMap<EventType, Object>());
        DpeEvent eventType = DpeEvent.DERRIERE_SLAB;
        Event event = new Event(eventType, slab);
        EventManager.getInstance().put(Channel.DPE, event);

        eventType = DpeEvent.ISOLATION_SLAB;
        event = new Event(eventType, slab);
        EventManager.getInstance().put(Channel.DPE, event);
    }

    public void log(){
        System.out.println("************************************************************************************");
        System.out.println("Type d'energie a la construction : " + typeEnergieConstruction);
        System.out.println("Type de batiment : " + typeBatiment);
        System.out.println("Annee de construction : " + anneeConstruction);
        if(typeBatiment==typeBatiment.APPARTEMENT){
            System.out.println("PositionAppt : " + positionAppartement);
        }
        else{
            System.out.println("NIV : " + NIV);
            System.out.println("MIT : " + MIT);
            System.out.println("MIT2 : " + MIT2);
            System.out.println("FOR : " + FOR);
        }
        System.out.println("Surface habitable : " + SH);
        System.out.println("DP mur ext : " + DP_murExt);
        System.out.println("DP mur ah : " + DP_murAh);
        System.out.println("DP mur lnc : " + DP_murLnc);
        System.out.println("DP mur ver : " + DP_murVer);
        System.out.println("DP plancher ah : " + DP_planAh);
        System.out.println("DP plancher ss : " + DP_planSs);
        System.out.println("DP plancher tp : " + DP_planTp);
        System.out.println("DP plancher vs : " + DP_planVs);
        System.out.println("DP plancher vs : " + DP_fen);
        System.out.println("PT : "+PT);
        System.out.println("************************************************************************************");
    }

    public void notify(Channel c, Event e) throws InterruptedException {

        EventType eventType = e.getEventType();
        if (c == Channel.DPE) {
            if (eventType instanceof DpeEvent) {
                DpeEvent event = (DpeEvent) eventType;
                Object o = e.getUserObject();
                switch (event) {
                    case START_DPE_RESPONSE: {
                        boolean response = (boolean) o;
                        if (response) {
                            this.demandeTypeBatiment();
                        }
                        break;
                    }
                    case TYPE_BATIMENT_RESPONSE: {
                        String reponse = (String) o;
                        stage.getActors().pop();
                        if (reponse == "maison") {
                            typeBatiment = typeBatiment.MAISON;
                            this.demandeNbNiveau();
                        } else {
                            typeBatiment = typeBatiment.APPARTEMENT;
                            demandePositionAppartement();
                        }
                        break;
                    }
                    case NB_NIVEAUX_RESPONSE: {
                        NIV = (double) o;
                        stage.getActors().pop();
                        this.demandeForme();
                        break;
                    }

                    case FORME_RESPONSE: {
                        FOR = (double) o;
                        stage.getActors().pop();
                        this.demandeMitoyennete();
                        break;
                    }

                    case MITOYENNETE_RESPONSE: {
                        MIT = (double) o;
                        this.calc_MIT2();
                        stage.getActors().pop();
                        this.demandeAnneeConstruction();
                        break;
                    }

                    case POSITION_APPARTEMENT_RESPONSE:{
                        String reponse = (String) o;
                        if (reponse.equals("bas")){
                            positionAppartement=positionAppartement.PREMIER_ETAGE;
                            configuration_Appartement="10";
                        } else if (reponse.equals("int")){
                            positionAppartement=positionAppartement.ETAGE_INTERMEDIAIRE;
                            configuration_Appartement="7";
                        } else if (reponse.equals("haut")){
                            positionAppartement=positionAppartement.DERNIER_ETAGE;
                            configuration_Appartement="2";
                        }
                        stage.getActors().pop();
                        this.demandeAnneeConstruction();
                        break;
                    }

                    case ANNEE_CONSTRUCTION_RESPONSE: {
                        anneeConstruction = (double) o;
                        stage.getActors().pop();
                        this.demandeTypeEnergieConstruction();
                        break;
                    }

                    case ENERGIE_CONSTRUCTION_RESPONSE: {
                        String reponse = (String) o;
                        if (reponse.equals("elec")){
                            typeEnergieConstruction=typeEnergieConstruction.ELECTRIQUE;
                        }else{
                            typeEnergieConstruction=typeEnergieConstruction.AUTRE;
                        }
                        break;
                    }

                    case ISOLATION_MUR_RESPONSE: {
                        Object[] items = (Object[]) o;
                        IfcWallStandardCase wall = (IfcWallStandardCase)items[0];
                        String isole = (String)items[1];
                        if (isole=="oui"){
                            DpeEvent et = DpeEvent.DATE_ISOLATION_MUR;
                            Event ev = new Event(et, wall);
                            EventManager.getInstance().put(Channel.DPE, ev);
                        }
                    }
                    case DERRIERE_MUR_RESPONSE:
                    case DATE_ISOLATION_MUR_RESPONSE:
                    {
                        Object[] items = (Object[]) o;
                        walls_properties.get(items[0]).put(event, items[1]);
                        tryActualiseMurDP((IfcWallStandardCase)items[0]);
                        break;
                    }

                    case ISOLATION_SLAB_RESPONSE: {
                        Object[] items = (Object[]) o;
                        IfcSlab slab = (IfcSlab)items[0];
                        String isole = (String)items[1];
                        if (isole=="oui"){
                            DpeEvent et = DpeEvent.DATE_ISOLATION_SLAB;
                            Event ev = new Event(et, slab);
                            EventManager.getInstance().put(Channel.DPE, ev);
                        }
                    }
                    case DERRIERE_SLAB_RESPONSE:
                    case DATE_ISOLATION_SLAB_RESPONSE:
                    {
                        Object[] items = (Object[]) o;
                        slabs_properties.get(items[0]).put(event, items[1]);

                        tryActualiseSlabDP((IfcSlab) items[0]);

                        break;
                    }
                    case DPE_REQUEST: {
                        System.out.println(o.getClass());
                        if (o instanceof IfcWallStandardCase) {
                            IfcWallStandardCase wall = (IfcWallStandardCase) o;
                            demanderMur(wall);
                        }
                        if (o instanceof IfcSlab) {
                            IfcSlab slab = (IfcSlab) o;
                            demanderSlab(slab);
                        }
                        break;
                    }
                    case DPE_STATE_NO_MORE_UNKNOWN:{
                        System.out.println("######################### COUCOU !!! #########################");
                        calc_PT();
                        log();
                    }
                }
            }
        }
    }
}
