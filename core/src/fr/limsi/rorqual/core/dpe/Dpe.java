package fr.limsi.rorqual.core.dpe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import fr.limsi.rorqual.core.dpe.enums.DpePropertiesEnum;
import fr.limsi.rorqual.core.dpe.enums.chauffageproperties.*;
import fr.limsi.rorqual.core.dpe.enums.generalproperties.*;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.*;
import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.*;
import fr.limsi.rorqual.core.dpe.enums.slabproperties.*;
import fr.limsi.rorqual.core.event.*;
import fr.limsi.rorqual.core.model.IfcHelper;
import fr.limsi.rorqual.core.model.IfcHolder;
import fr.limsi.rorqual.core.ui.Layout;
import fr.limsi.rorqual.core.ui.TabWindow;
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
    private IfcHelper ifcHelper;
    private HashMap<IfcWallStandardCase, HashMap<EventType, Object>> walls_properties = new HashMap<IfcWallStandardCase, HashMap<EventType, Object>>();
    private HashMap<IfcSlab, HashMap<EventType, Object>> slabs_properties = new HashMap<IfcSlab, HashMap<EventType, Object>>();
    private HashMap<IfcWindow, HashMap<EventType, Object>> windows_properties = new HashMap<IfcWindow, HashMap<EventType, Object>>();
    private HashMap<IfcDoor, HashMap<EventType, Object>> doors_properties = new HashMap<IfcDoor, HashMap<EventType, Object>>();
    private HashMap<EventType,Object> general_properties = new HashMap<EventType,Object>();
    private HashMap<EventType,Object> chauffage_properties = new HashMap<EventType,Object>();
    private Collection<IfcWallStandardCase> wallCollection;
    private Collection<IfcSlab> slabCollection;
    private Collection<IfcWindow> windowCollection;
    private Collection<IfcDoor> doorCollection;

    /*** Attributs liés à l'interface graphique de libGDX ***/
    private Skin skin;
    private BitmapFont fontBlack, fontWhite;
    private Stage stage;
    private TextButton.TextButtonStyle textButtonStyle;

    /*** Attributs liés au calcul du DPE ***/

    // 0.Variables générales hors model IFC
    private double SH;
    private double NIV;
    private double MIT;
    private double MIT2;
    private double FOR;
    private double Per;
    private double PER;
    private double C_niv;

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
        ifcHelper = new IfcHelper(ifcModel);
        stage = stageMenu;
        wallCollection = ifcModel.getCollection(IfcWallStandardCase.class);
        slabCollection = ifcModel.getCollection(IfcSlab.class);
        windowCollection = ifcModel.getCollection(IfcWindow.class);
        doorCollection = ifcModel.getCollection(IfcDoor.class);
        SH = ifcHelper.calculSurfaceHabitable();
        PER = ifcHelper.calculPerimetreBatiment();
        EventManager.getInstance().addListener(Channel.DPE, this);
//
//        general_properties.put(DpeEvent.TYPE_BATIMENT,TypeBatimentEnum.MAISON);
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
            Sdep += ifcHelper.getWindowSurface(actualWindow);
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
            largeurFenetre=ifcHelper.getWindowWidth(actualWindow);
            hauteurFenetre=ifcHelper.getWindowHeight(actualWindow);
            lMen += (2*largeurFenetre+2*hauteurFenetre);
        }

        for (IfcDoor actualDoor : doorCollection){
            largeurPorte=ifcHelper.getDoorWidth(actualDoor);
            hauteurPorte=ifcHelper.getDoorHeight(actualDoor);
            lMen += (2*largeurPorte+2*hauteurPorte);
        }
        return lMen;
    }

    public void tryActualiseWallDP(IfcWallStandardCase wall) {

        // si pas de type de mur
        if (!walls_properties.get(wall).containsKey(DpeEvent.TYPE_MUR))
            return;

        TypeMurEnum typeMur = (TypeMurEnum) walls_properties.get(wall).get(DpeEvent.TYPE_MUR);

        // si mur connu mais pas de date d'isolation
        if (typeMur != TypeMurEnum.INCONNUE && !walls_properties.get(wall).containsKey(DpeEvent.DATE_ISOLATION_MUR))
            return;

        DateIsolationMurEnum dateIsolationMur = (DateIsolationMurEnum) walls_properties.get(wall).get(DpeEvent.DATE_ISOLATION_MUR);

        // si date connue mais pas de type d'isolation
        TypeIsolationMurEnum typeIsolationMur;

        if (dateIsolationMur == DateIsolationMurEnum.INCONNUE || dateIsolationMur == DateIsolationMurEnum.JAMAIS)
            typeIsolationMur = TypeIsolationMurEnum.INCONNUE;
        else if (!walls_properties.get(wall).containsKey(DpeEvent.TYPE_ISOLATION_MUR))
            return;
        else
            typeIsolationMur = (TypeIsolationMurEnum) walls_properties.get(wall).get(DpeEvent.TYPE_ISOLATION_MUR);

        // On signal au model que le calcul thermique vient d'être effectuer sur wall
        Object o[] = {wall, DpeState.KNOWN};
        Event e = new Event(DpeEvent.DPE_STATE_CHANGED, o);
        EventManager.getInstance().put(Channel.DPE, e);

        //actualiseDP_wall(wall, typeMur, dateIsolationMur, typeIsolationMur);
    }

    public void calc_DR() {
        double Sdep=0,Q4PaConv=0,Q4PaEnv=0,Smea=0,Q4Pa=0;
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
        Iterator<IfcWallStandardCase> it = wallCollection.iterator();
        while (it.hasNext()) {
            wall = it.next();
            // TODO : ne pas notifier les murs intérieurs
            Object o[] = {wall, DpeState.UNKNOWN};
            Event e = new Event(DpeEvent.DPE_STATE_CHANGED, o);
            EventManager.getInstance().put(Channel.DPE, e);
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

    /*** Notifie le statut des doors ***/
    public void notifierPortes() {
        IfcDoor door;
        Iterator<IfcDoor> it = doorCollection.iterator();
        while (it.hasNext()) {
            door = it.next();
            //TODO : ne prendre en considération que les doors reliées aux murs extérieurs
            Object o[] = {door, DpeState.UNKNOWN};
            Event e = new Event(DpeEvent.DPE_STATE_CHANGED, o);
            EventManager.getInstance().put(Channel.DPE, e);
        }
    }

    /*** Log les valeurs du DPE dans la console ***/
    public void logValeursDpe(){
        System.out.println("**************************** LOG DPE ***********************************");
//        System.out.println("typeBatiment = "+typeBatiment.toString());
//        System.out.println("positionAppartement = "+positionAppartement.toString());
//        System.out.println("typeEnergieConstruction = "+typeEnergieConstruction.toString());
//        System.out.println("typeVentilation = "+typeVentilation.toString());
//        System.out.println("typeVmc = "+typeVmc.toString());
//        System.out.println("anneeConstruction = "+anneeConstruction);
        System.out.println("SH = "+SH);
        System.out.println("NIV = "+NIV);
        System.out.println("MIT = "+MIT);
        System.out.println("MIT2 = "+MIT2);
        System.out.println("FOR = "+FOR);
        System.out.println("Per = "+Per);
        System.out.println("PER = "+PER);
        System.out.println("C_niv = "+C_niv);
//        System.out.println("configuration_Appartement = "+configuration_Appartement);
        System.out.println("BV = "+BV);
        System.out.println("GV = "+GV);
        System.out.println("F = "+F);
        System.out.println("DP_murExt = "+DP_murExt);
        System.out.println("DP_murLnc = "+DP_murLnc);
        System.out.println("DP_murAh = "+DP_murAh);
        System.out.println("DP_murVer = "+DP_murVer);
        System.out.println("DP_toiTer = "+DP_toiTer);
        System.out.println("DP_toiCp = "+DP_toiCp);
        System.out.println("DP_toiCa = "+DP_toiCa);
        System.out.println("DP_planVs = "+DP_planVs);
        System.out.println("DP_planTp = "+DP_planTp);
        System.out.println("DP_planSs = "+DP_planSs);
        System.out.println("DP_planAh = "+DP_planAh);
        System.out.println("DP_fen = "+DP_fen);
        System.out.println("DP_pfen = "+DP_pfen);
        System.out.println("DP_fenVer = "+DP_fenVer);
        System.out.println("DP_pfenVer = "+DP_pfenVer);
        System.out.println("DP_portExt = "+DP_portExt);
        System.out.println("DP_portLnc = "+DP_portLnc);
        System.out.println("DP_portVer = "+DP_portVer);
        System.out.println("PT = "+PT);
        System.out.println("DR = "+DR);
        System.out.println("Tint = "+Tint);
        System.out.println("Sdep = "+Sdep);
        System.out.println("*************************** FIN LOG DPE ********************************");
    }

    public void notify(Channel c, Event e) throws InterruptedException {

        EventType eventType = e.getEventType();
        if (c == Channel.DPE) {
            if (eventType instanceof DpeEvent) {
                DpeEvent event = (DpeEvent) eventType;
                Object o = e.getUserObject();
                switch (event) {
                    case TYPE_BATIMENT: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        Layout layout = (Layout)items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeBatimentEnum typeBatiment = (TypeBatimentEnum) items.get("lastValue");
                            if (typeBatiment.equals(TypeBatimentEnum.MAISON)) {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("forme_maison"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("mitoyennete_maison"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("position_appartement"), false);
                            } else {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("forme_maison"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("mitoyennete_maison"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("position_appartement"), true);
                            }
                            general_properties.put(DpeEvent.TYPE_BATIMENT, typeBatiment);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeBatimentEnum type = TypeBatimentEnum.APPARTEMENT;
                            while (!layout.getIsInitialised()){
                                try {
                                    layout = (Layout)items.get("layout");
                                    Thread.sleep(10);
                                } catch (InterruptedException ie) {

                                }
                            }
                            if (general_properties.containsKey(DpeEvent.TYPE_BATIMENT)) {
                                type = (TypeBatimentEnum) general_properties.get(DpeEvent.TYPE_BATIMENT);
                                if (type.equals(TypeBatimentEnum.MAISON)) {
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("forme_maison"), true);
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("mitoyennete_maison"), true);
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("position_appartement"), false);
                                } else {
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("forme_maison"), false);
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("mitoyennete_maison"), false);
                                    ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("position_appartement"), true);
                                }
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_BATIMENT, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case FORME_MAISON: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            FormeMaisonEnum formeMaison = (FormeMaisonEnum) items.get("lastValue");
                            if (formeMaison.equals(FormeMaisonEnum.CARRE)){FOR = 4.12;}
                            else if (formeMaison.equals(FormeMaisonEnum.ALLONGE)){FOR = 4.81;}
                            else if (formeMaison.equals(FormeMaisonEnum.DEVELOPPE)){FOR = 5.71;}
                            general_properties.put(DpeEvent.FORME_MAISON, formeMaison);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            FormeMaisonEnum type = null;
                            if (general_properties.containsKey(DpeEvent.FORME_MAISON)){
                                type = (FormeMaisonEnum) general_properties.get(DpeEvent.FORME_MAISON);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.FORME_MAISON, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case MITOYENNETE_MAISON: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            MitoyenneteMaisonEnum mitoyenneteMaison = (MitoyenneteMaisonEnum) items.get("lastValue");
                            if (mitoyenneteMaison.equals(MitoyenneteMaisonEnum.NON_ACCOLE)){MIT = 1;}
                            else if (mitoyenneteMaison.equals(MitoyenneteMaisonEnum.ACCOLE_PETIT_COTE)){MIT = 0.8;}
                            else if (mitoyenneteMaison.equals(MitoyenneteMaisonEnum.ACCOLE_GRAND_OU_DEUX_PETITS_COTES)){MIT = 0.7;}
                            else if (mitoyenneteMaison.equals(MitoyenneteMaisonEnum.ACCOLE_GRAND_ET_UN_PETIT_COTE)){MIT = 0.5;}
                            else if (mitoyenneteMaison.equals(MitoyenneteMaisonEnum.ACCOLE_DEUX_GRANDS_COTES)){MIT = 0.35;}
                            general_properties.put(DpeEvent.MITOYENNETE_MAISON, mitoyenneteMaison);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            MitoyenneteMaisonEnum type = null;
                            if (general_properties.containsKey(DpeEvent.MITOYENNETE_MAISON)){
                                type = (MitoyenneteMaisonEnum) general_properties.get(DpeEvent.MITOYENNETE_MAISON);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.MITOYENNETE_MAISON, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case DEPARTEMENT_BATIMENT:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            DepartementBatimentEnum departement = (DepartementBatimentEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.DEPARTEMENT_BATIMENT, departement);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            DepartementBatimentEnum type = null;
                            if (general_properties.containsKey(DpeEvent.DEPARTEMENT_BATIMENT)){
                                type = (DepartementBatimentEnum) general_properties.get(DpeEvent.DEPARTEMENT_BATIMENT);
                            }
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
                            DateConstructionBatimentEnum dateConstructionBatiment = (DateConstructionBatimentEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.ANNEE_CONSTRUCTION, dateConstructionBatiment);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            DateConstructionBatimentEnum type = null;
                            if (general_properties.containsKey(DpeEvent.ANNEE_CONSTRUCTION)){
                                type = (DateConstructionBatimentEnum) general_properties.get(DpeEvent.ANNEE_CONSTRUCTION);
                            }
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
                            TypeEnergieConstructionEnum typeEnergieConstruction = (TypeEnergieConstructionEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.ENERGIE_CONSTRUCTION, typeEnergieConstruction);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeEnergieConstructionEnum type = null;
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

                    case TYPE_CHAUFFAGE_PRINCIPAL :{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeChauffageEnum typeChauffage = (TypeChauffageEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.TYPE_CHAUFFAGE_PRINCIPAL, typeChauffage);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeChauffageEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.TYPE_CHAUFFAGE_PRINCIPAL)){
                                type = (TypeChauffageEnum) chauffage_properties.get(DpeEvent.TYPE_CHAUFFAGE_PRINCIPAL);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_CHAUFFAGE_PRINCIPAL, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_CHAUFFAGE_SECONDAIRE :{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeChauffageEnum typeChauffage = (TypeChauffageEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.TYPE_CHAUFFAGE_SECONDAIRE, typeChauffage);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeChauffageEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.TYPE_CHAUFFAGE_SECONDAIRE)){
                                type = (TypeChauffageEnum) chauffage_properties.get(DpeEvent.TYPE_CHAUFFAGE_SECONDAIRE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_CHAUFFAGE_SECONDAIRE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_POMPE_A_CHALEUR :{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypePompeChaleurEnum typePompeChaleur = (TypePompeChaleurEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.TYPE_POMPE_A_CHALEUR, typePompeChaleur);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypePompeChaleurEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.TYPE_POMPE_A_CHALEUR)){
                                type = (TypePompeChaleurEnum) chauffage_properties.get(DpeEvent.TYPE_POMPE_A_CHALEUR);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_POMPE_A_CHALEUR, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_POIL_OU_INSERT:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypePoilOuInsertEnum typePoilOuInsert = (TypePoilOuInsertEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.TYPE_POIL_OU_INSERT, typePoilOuInsert);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypePoilOuInsertEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.TYPE_POIL_OU_INSERT)){
                                type = (TypePoilOuInsertEnum) chauffage_properties.get(DpeEvent.TYPE_POIL_OU_INSERT);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_POIL_OU_INSERT, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case DATE_CHAUDIERE_GAZ_CLASSIQUE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            DateChaudiereGazClassiqueEnum dateChaudiere = (DateChaudiereGazClassiqueEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.DATE_CHAUDIERE_GAZ_CLASSIQUE, dateChaudiere);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            DateChaudiereGazClassiqueEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.DATE_CHAUDIERE_GAZ_CLASSIQUE)){
                                type = (DateChaudiereGazClassiqueEnum) chauffage_properties.get(DpeEvent.DATE_CHAUDIERE_GAZ_CLASSIQUE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.DATE_CHAUDIERE_GAZ_CLASSIQUE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case DATE_CHAUDIERE_GAZ_CONDENSATION:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            DateChaudiereGazCondensationEnum dateChaudiere = (DateChaudiereGazCondensationEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.DATE_CHAUDIERE_GAZ_CONDENSATION, dateChaudiere);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            DateChaudiereGazCondensationEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.DATE_CHAUDIERE_GAZ_CONDENSATION)){
                                type = (DateChaudiereGazCondensationEnum) chauffage_properties.get(DpeEvent.DATE_CHAUDIERE_GAZ_CONDENSATION);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.DATE_CHAUDIERE_GAZ_CONDENSATION, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case DATE_CHAUDIERE_GAZ_BASSE_TEMPERATURE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            DateChaudiereGazBasseTemperatureEnum dateChaudiere = (DateChaudiereGazBasseTemperatureEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.DATE_CHAUDIERE_GAZ_BASSE_TEMPERATURE, dateChaudiere);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            DateChaudiereGazBasseTemperatureEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.DATE_CHAUDIERE_GAZ_BASSE_TEMPERATURE)){
                                type = (DateChaudiereGazBasseTemperatureEnum) chauffage_properties.get(DpeEvent.DATE_CHAUDIERE_GAZ_BASSE_TEMPERATURE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.DATE_CHAUDIERE_GAZ_BASSE_TEMPERATURE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case DATE_CHAUDIERE_FIOUL:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            DateChaudiereFioulEnum dateChaudiere = (DateChaudiereFioulEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.DATE_CHAUDIERE_FIOUL, dateChaudiere);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            DateChaudiereFioulEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.DATE_CHAUDIERE_FIOUL)){
                                type = (DateChaudiereFioulEnum) chauffage_properties.get(DpeEvent.DATE_CHAUDIERE_FIOUL);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.DATE_CHAUDIERE_FIOUL, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case AGE_CHAUDIERE_BOIS:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            AgeChaudiereBoisEnum dateChaudiere = (AgeChaudiereBoisEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.AGE_CHAUDIERE_BOIS, dateChaudiere);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            AgeChaudiereBoisEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.AGE_CHAUDIERE_BOIS)){
                                type = (AgeChaudiereBoisEnum) chauffage_properties.get(DpeEvent.AGE_CHAUDIERE_BOIS);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.AGE_CHAUDIERE_BOIS, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case AGE_CHAUFFAGE_ELECTRIQUE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            AgeChauffageElectriqueEnum dateChaudiere = (AgeChauffageElectriqueEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.AGE_CHAUFFAGE_ELECTRIQUE, dateChaudiere);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            AgeChauffageElectriqueEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.AGE_CHAUFFAGE_ELECTRIQUE)){
                                type = (AgeChauffageElectriqueEnum) chauffage_properties.get(DpeEvent.AGE_CHAUFFAGE_ELECTRIQUE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.AGE_CHAUFFAGE_ELECTRIQUE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TEMPERATURE_INTERIEUR:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TemperatureInterieurEnum temperatureInterieur = (TemperatureInterieurEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.TEMPERATURE_INTERIEUR, temperatureInterieur);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TemperatureInterieurEnum type = null;
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
                            TypeEmetteurEnum typeEmetteur = (TypeEmetteurEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR, typeEmetteur);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeEmetteurEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR)){
                                type = (TypeEmetteurEnum) chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR);
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

                    case FREQUENCE_UTILISATION_POIL_OU_INSERT:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            ProgrammationSystemeEnum programmationSysteme = (ProgrammationSystemeEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.FREQUENCE_UTILISATION_POIL_OU_INSERT, programmationSysteme);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            ProgrammationSystemeEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.FREQUENCE_UTILISATION_POIL_OU_INSERT)){
                                type = (ProgrammationSystemeEnum) chauffage_properties.get(DpeEvent.FREQUENCE_UTILISATION_POIL_OU_INSERT);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.FREQUENCE_UTILISATION_POIL_OU_INSERT, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_MUR: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        IfcWallStandardCase wall = (IfcWallStandardCase)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeMurEnum typeMur = (TypeMurEnum)items.get("lastValue");
                            Layout layout = (Layout)items.get("layout");
                            if (!walls_properties.containsKey(wall))
                                walls_properties.put(wall, new HashMap<EventType, Object>());
                            walls_properties.get(wall).put(event, typeMur);
                            if (!typeMur.equals(TypeMurEnum.MUR_INTERIEUR)) {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("date_isolation"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_isolation"), true);
                            } else {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("date_isolation"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_isolation"), false);
                            }
                            tryActualiseWallDP(wall);
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            TypeMurEnum type = null;
                            if (walls_properties.containsKey(wall))
                                if (walls_properties.get(wall).containsKey(DpeEvent.TYPE_MUR))
                                    type = (TypeMurEnum) walls_properties.get(wall).get(DpeEvent.TYPE_MUR);
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", wall);
                            Event e2 = new Event(DpeEvent.TYPE_MUR, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case DATE_ISOLATION_MUR: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        IfcWallStandardCase wall = (IfcWallStandardCase)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            DateIsolationMurEnum dateIsolationMur = (DateIsolationMurEnum)items.get("lastValue");
                            Layout layout = (Layout) items.get("layout");
                            if (!walls_properties.containsKey(wall))
                                walls_properties.put(wall, new HashMap<EventType, Object>());
                            walls_properties.get(wall).put(event, dateIsolationMur);
                            if (dateIsolationMur.equals(DateIsolationMurEnum.JAMAIS) || dateIsolationMur.equals(DateIsolationMurEnum.INCONNUE)) {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_isolation"), false);
                            } else {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_isolation"), true);
                            }
                            tryActualiseWallDP(wall);
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            DateIsolationMurEnum type = null;
                            if (walls_properties.containsKey(wall))
                                if (walls_properties.get(wall).containsKey(DpeEvent.DATE_ISOLATION_MUR))
                                    type = (DateIsolationMurEnum) walls_properties.get(wall).get(DpeEvent.DATE_ISOLATION_MUR);
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", wall);
                            Event e2 = new Event(DpeEvent.DATE_ISOLATION_MUR, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_ISOLATION_MUR: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        IfcWallStandardCase wall = (IfcWallStandardCase)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeIsolationMurEnum typeIsolationMur = (TypeIsolationMurEnum) items.get("lastValue");
                            if (!walls_properties.containsKey(wall))
                                walls_properties.put(wall, new HashMap<EventType, Object>());
                            walls_properties.get(wall).put(event, typeIsolationMur);
                            tryActualiseWallDP(wall);
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            TypeIsolationMurEnum type = null;
                            if (walls_properties.containsKey(wall))
                                if (walls_properties.get(wall).containsKey(DpeEvent.TYPE_ISOLATION_MUR))
                                    type = (TypeIsolationMurEnum) walls_properties.get(wall).get(DpeEvent.TYPE_ISOLATION_MUR);
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", wall);
                            Event e2 = new Event(DpeEvent.TYPE_ISOLATION_MUR, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case ORIENTATION_MUR: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        IfcWallStandardCase wall = (IfcWallStandardCase)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            OrientationMurEnum orientationMur = (OrientationMurEnum) items.get("lastValue");
                            if (!walls_properties.containsKey(wall))
                                walls_properties.put(wall, new HashMap<EventType, Object>());
                            walls_properties.get(wall).put(event, orientationMur);
                            tryActualiseWallDP(wall);
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            OrientationMurEnum type = null;
                            if (walls_properties.containsKey(wall))
                                if (walls_properties.get(wall).containsKey(DpeEvent.ORIENTATION_MUR))
                                    type = (OrientationMurEnum) walls_properties.get(wall).get(DpeEvent.ORIENTATION_MUR);
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", wall);
                            Event e2 = new Event(DpeEvent.ORIENTATION_MUR, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_FENETRE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        IfcWindow window = (IfcWindow)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeFenetreEnum typeFenetre = (TypeFenetreEnum)items.get("lastValue");
                            if (!windows_properties.containsKey(window)){
                                windows_properties.put(window, new HashMap<EventType, Object>());
                            }
                            windows_properties.get(window).put(event, typeFenetre);

                        } else if (eventRequest == EventRequest.GET_STATE) {
                            TypeFenetreEnum type = null;
                            if (windows_properties.containsKey(window))
                                if (windows_properties.get(window).containsKey(DpeEvent.TYPE_FENETRE))
                                    type = (TypeFenetreEnum)windows_properties.get(window).get(DpeEvent.TYPE_FENETRE);
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", window);
                            Event e2 = new Event(DpeEvent.TYPE_FENETRE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_MATERIAU_MENUISERIE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (items.get("userObject") instanceof IfcWindow){
                            IfcWindow window = (IfcWindow)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMateriauMenuiserieEnum typeMateriauMenuiserie = (TypeMateriauMenuiserieEnum)items.get("lastValue");
                                if (!windows_properties.containsKey(window))
                                    windows_properties.put(window, new HashMap<EventType, Object>());
                                windows_properties.get(window).put(event, typeMateriauMenuiserie);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMateriauMenuiserieEnum type = null;
                                if (windows_properties.containsKey(window))
                                    if (windows_properties.get(window).containsKey(DpeEvent.TYPE_MATERIAU_MENUISERIE))
                                        type = (TypeMateriauMenuiserieEnum) windows_properties.get(window).get(DpeEvent.TYPE_MATERIAU_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", window);
                                Event e2 = new Event(DpeEvent.TYPE_MATERIAU_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }
                        }
                        else if (items.get("userObject") instanceof IfcDoor){
                            IfcDoor door = (IfcDoor)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMateriauMenuiserieEnum typeMateriauMenuiserie = (TypeMateriauMenuiserieEnum)items.get("lastValue");
                                if (!doors_properties.containsKey(door))
                                    doors_properties.put(door, new HashMap<EventType, Object>());
                                doors_properties.get(door).put(event, typeMateriauMenuiserie);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMateriauMenuiserieEnum type = null;
                                if (doors_properties.containsKey(door))
                                    if (doors_properties.get(door).containsKey(DpeEvent.TYPE_MATERIAU_MENUISERIE))
                                        type = (TypeMateriauMenuiserieEnum) doors_properties.get(door).get(DpeEvent.TYPE_MATERIAU_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", door);
                                Event e2 = new Event(DpeEvent.TYPE_MATERIAU_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }
                        }
                        break;
                    }

                    case TYPE_VITRAGE_MENUISERIE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (items.get("userObject") instanceof IfcWindow){
                            IfcWindow window = (IfcWindow)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeVitrageEnum TypeVitrage = (TypeVitrageEnum)items.get("lastValue");
                                if (!windows_properties.containsKey(window))
                                    windows_properties.put(window, new HashMap<EventType, Object>());
                                windows_properties.get(window).put(event, TypeVitrage);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeVitrageEnum type = null;
                                if (windows_properties.containsKey(window))
                                    if (windows_properties.get(window).containsKey(DpeEvent.TYPE_VITRAGE_MENUISERIE))
                                        type = (TypeVitrageEnum) windows_properties.get(window).get(DpeEvent.TYPE_VITRAGE_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", window);
                                Event e2 = new Event(DpeEvent.TYPE_VITRAGE_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }

                        }
                        else if (items.get("userObject") instanceof IfcDoor){
                            IfcDoor door = (IfcDoor)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeVitrageEnum TypeVitrage = (TypeVitrageEnum)items.get("lastValue");
                                if (!doors_properties.containsKey(door))
                                    doors_properties.put(door, new HashMap<EventType, Object>());
                                doors_properties.get(door).put(event, TypeVitrage);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeVitrageEnum type = null;
                                if (doors_properties.containsKey(door))
                                    if (doors_properties.get(door).containsKey(DpeEvent.TYPE_VITRAGE_MENUISERIE))
                                        type = (TypeVitrageEnum) doors_properties.get(door).get(DpeEvent.TYPE_VITRAGE_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", door);
                                Event e2 = new Event(DpeEvent.TYPE_VITRAGE_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }
                        }
                        break;
                    }

                    case TYPE_FERMETURE_MENUISERIE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (items.get("userObject") instanceof IfcWindow){
                            IfcWindow window = (IfcWindow)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeFermetureEnum typeFermeture = (TypeFermetureEnum)items.get("lastValue");
                                if (!windows_properties.containsKey(window))
                                    windows_properties.put(window, new HashMap<EventType, Object>());
                                windows_properties.get(window).put(event, typeFermeture);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeFermetureEnum type = null;
                                if (windows_properties.containsKey(window))
                                    if (windows_properties.get(window).containsKey(DpeEvent.TYPE_FERMETURE_MENUISERIE))
                                        type = (TypeFermetureEnum) windows_properties.get(window).get(DpeEvent.TYPE_FERMETURE_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", window);
                                Event e2 = new Event(DpeEvent.TYPE_FERMETURE_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }

                        }
                        else if (items.get("userObject") instanceof IfcDoor){
                            IfcDoor door = (IfcDoor)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeFermetureEnum typeFermeture = (TypeFermetureEnum)items.get("lastValue");
                                if (!doors_properties.containsKey(door))
                                    doors_properties.put(door, new HashMap<EventType, Object>());
                                doors_properties.get(door).put(event, typeFermeture);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeFermetureEnum type = null;
                                if (doors_properties.containsKey(door))
                                    if (doors_properties.get(door).containsKey(DpeEvent.TYPE_FERMETURE_MENUISERIE))
                                        type = (TypeFermetureEnum) doors_properties.get(door).get(DpeEvent.TYPE_FERMETURE_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", door);
                                Event e2 = new Event(DpeEvent.TYPE_FERMETURE_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }
                        }
                        break;
                    }

                    case MASQUE_PROCHE_MENUISERIE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (items.get("userObject") instanceof IfcWindow){
                            IfcWindow window = (IfcWindow)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMasqueEnum masque = (TypeMasqueEnum)items.get("lastValue");
                                if (!windows_properties.containsKey(window))
                                    windows_properties.put(window, new HashMap<EventType, Object>());
                                windows_properties.get(window).put(event, masque);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMasqueEnum type = null;
                                if (windows_properties.containsKey(window))
                                    if (windows_properties.get(window).containsKey(DpeEvent.MASQUE_PROCHE_MENUISERIE))
                                        type = (TypeMasqueEnum) windows_properties.get(window).get(DpeEvent.MASQUE_PROCHE_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", window);
                                Event e2 = new Event(DpeEvent.MASQUE_PROCHE_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }

                        }
                        else if (items.get("userObject") instanceof IfcDoor){
                            IfcDoor door = (IfcDoor)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMasqueEnum masque = (TypeMasqueEnum)items.get("lastValue");
                                if (!doors_properties.containsKey(door))
                                    doors_properties.put(door, new HashMap<EventType, Object>());
                                doors_properties.get(door).put(event, masque);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMasqueEnum type = null;
                                if (doors_properties.containsKey(door))
                                    if (doors_properties.get(door).containsKey(DpeEvent.MASQUE_PROCHE_MENUISERIE))
                                        type = (TypeMasqueEnum) doors_properties.get(door).get(DpeEvent.MASQUE_PROCHE_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", door);
                                Event e2 = new Event(DpeEvent.MASQUE_PROCHE_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }
                        }
                        break;
                    }

                    case MASQUE_LOINTAIN_MENUISERIE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (items.get("userObject") instanceof IfcWindow){
                            IfcWindow window = (IfcWindow)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMasqueEnum masque = (TypeMasqueEnum)items.get("lastValue");
                                if (!windows_properties.containsKey(window))
                                    windows_properties.put(window, new HashMap<EventType, Object>());
                                windows_properties.get(window).put(event, masque);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMasqueEnum type = null;
                                if (windows_properties.containsKey(window))
                                    if (windows_properties.get(window).containsKey(DpeEvent.MASQUE_LOINTAIN_MENUISERIE))
                                        type = (TypeMasqueEnum) windows_properties.get(window).get(DpeEvent.MASQUE_LOINTAIN_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", window);
                                Event e2 = new Event(DpeEvent.MASQUE_LOINTAIN_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }

                        }
                        else if (items.get("userObject") instanceof IfcDoor){
                            IfcDoor door = (IfcDoor)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMasqueEnum masque = (TypeMasqueEnum)items.get("lastValue");
                                if (!doors_properties.containsKey(door))
                                    doors_properties.put(door, new HashMap<EventType, Object>());
                                doors_properties.get(door).put(event, masque);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMasqueEnum type = null;
                                if (doors_properties.containsKey(door))
                                    if (doors_properties.get(door).containsKey(DpeEvent.MASQUE_LOINTAIN_MENUISERIE))
                                        type = (TypeMasqueEnum) doors_properties.get(door).get(DpeEvent.MASQUE_LOINTAIN_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", door);
                                Event e2 = new Event(DpeEvent.MASQUE_LOINTAIN_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }
                        }
                        break;
                    }

                    case TYPE_PORTE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        IfcDoor door = (IfcDoor)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeDoorEnum typeDoor = (TypeDoorEnum)items.get("lastValue");
                            Layout layout = (Layout)items.get("layout");
                            if (!doors_properties.containsKey(door))
                                doors_properties.put(door, new HashMap<EventType, Object>());
                            doors_properties.get(door).put(event, typeDoor);
                            if (typeDoor.equals(TypeDoorEnum.PORTE_FENETRE_COULISSANTE)||typeDoor.equals(TypeDoorEnum.PORTE_FENETRE_BATTANTE)) {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("materiau_porte"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("vitrage_porte"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("fermeture_porte"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("masque_proche_porte"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("masque_lointain_porte"), true);
                            } else {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("materiau_porte"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("vitrage_porte"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("fermeture_porte"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("masque_proche_porte"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("masque_lointain_porte"), false);
                            }
                            //tryActualiseWallDP(wall);
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            TypeDoorEnum type = null;
                            if (doors_properties.containsKey(door))
                                if (doors_properties.get(door).containsKey(DpeEvent.TYPE_PORTE))
                                    type = (TypeDoorEnum) doors_properties.get(door).get(DpeEvent.TYPE_PORTE);
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", door);
                            Event e2 = new Event(DpeEvent.TYPE_PORTE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

//                    case DPE_STATE_NO_MORE_UNKNOWN:{
//                        //logValeursDpe();
//                    }
//                    case DPE_STATE_NO_MORE_WALL_UNKNOWN:{
//                        notifierFenetres();
//                        notifierPortes();
//                        ifcHelper.saveIfcModel();
//                    }
                }
            }
        }
    }
}
