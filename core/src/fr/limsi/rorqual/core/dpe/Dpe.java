package fr.limsi.rorqual.core.dpe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import fr.limsi.rorqual.core.dpe.enums.chauffageproperties.*;
import fr.limsi.rorqual.core.dpe.enums.generalproperties.*;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.*;
import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.*;
import fr.limsi.rorqual.core.dpe.enums.ecsproperties.*;
import fr.limsi.rorqual.core.event.*;
import fr.limsi.rorqual.core.model.*;
import fr.limsi.rorqual.core.model.Mur;
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
    private HashMap<Mur, HashMap<EventType, Object>> walls_properties = new HashMap<Mur, HashMap<EventType, Object>>();
    private HashMap<Slab, HashMap<EventType, Object>> slabs_properties = new HashMap<Slab, HashMap<EventType, Object>>();
    private HashMap<Fenetre, HashMap<EventType, Object>> windows_properties = new HashMap<Fenetre, HashMap<EventType, Object>>();
    private HashMap<Porte, HashMap<EventType, Object>> doors_properties = new HashMap<Porte, HashMap<EventType, Object>>();
    private HashMap<EventType,Object> general_properties = new HashMap<EventType,Object>();
    private HashMap<EventType,Object> chauffage_properties = new HashMap<EventType,Object>();
    private HashMap<EventType,Object> ecs_properties = new HashMap<EventType,Object>();

    /*** Attributs liés à l'interface graphique de libGDX ***/
    private Skin skin;
    private BitmapFont fontBlack, fontWhite;
    private TextButton.TextButtonStyle textButtonStyle;

    /*** Attributs liés au calcul du DPE ***/

    // 0.Variables générales hors model IFC
    private double SH;
    private double nbHabitant;
    private double nbJoursAbsenceParAn;
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
    public Dpe () {
        fontBlack = new BitmapFont(Gdx.files.internal("data/font/black.fnt"));
        fontWhite = new BitmapFont(Gdx.files.internal("data/font/white.fnt"));
        skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));
        textButtonStyle = new TextButton.TextButtonStyle(skin.getDrawable("default-round"),skin.getDrawable("default-round-down"),null,fontBlack);
        /*ifcModel = IfcHolder.getInstance().getIfcModel();
        ifcHelper = new IfcHelper(ifcModel);
        SH = ifcHelper.calculSurfaceHabitable();
        PER = ifcHelper.calculPerimetreBatiment();*/
        EventManager.getInstance().addListener(Channel.DPE, this);
        SH=0;

    }

    /*---------------------------------Calculateur DPE-------------------------------------------*/

    public void calc_BV() {
        BV = GV*(1-F);
    }

    public void calc_GV() {
        GV = DP_murExt + DP_murLnc + DP_murAh + DP_murVer + DP_toiTer + DP_toiCp + DP_toiCa + DP_planVs + DP_planTp + DP_planSs + DP_planAh + DP_fen + DP_pfen + DP_fenVer + DP_pfenVer + DP_portExt + DP_portLnc + DP_portVer + PT + DR;
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

                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        EventRequest eventRequest = (EventRequest) items.get("eventRequest");
                        Layout layout = (Layout) items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeBatimentEnum typeBatiment = (TypeBatimentEnum) items.get("lastValue");
                            if (typeBatiment == TypeBatimentEnum.MAISON) {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("maison"), true);
                            } else {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("maison"), false);
                            }
                            general_properties.put(DpeEvent.TYPE_BATIMENT, typeBatiment);

                        } else if (eventRequest == EventRequest.GET_STATE) {

                            TypeBatimentEnum type = (TypeBatimentEnum) general_properties.get(DpeEvent.TYPE_BATIMENT);

                            HashMap<String, Object> currentItems = new HashMap<String, Object>();
                            currentItems.put("lastValue", type);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_BATIMENT, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);

                            // wait for layout to be  populated

                            while (!layout.isInitialised()) {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException ie) {

                                }
                            }
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("maison"), false);
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
                            System.out.println("SH = " + SH);

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

                    case SURFACE_HABITABLE: {
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        EventRequest eventRequest = (EventRequest) items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            SH = (double) items.get("lastValue");
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue", SH);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.SURFACE_HABITABLE, currentItems);
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
                            System.out.println(departement);
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

                    case ABONNEMENT_ELECTRIQUE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeAbonnementElectriqueEnum typeAbonnementElectrique = (TypeAbonnementElectriqueEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.ABONNEMENT_ELECTRIQUE, typeAbonnementElectrique);
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

                    case EQUIPEMENT_ECLAIRAGE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeEquipementEclairageEnum equipementEclairage = (TypeEquipementEclairageEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.EQUIPEMENT_ECLAIRAGE, equipementEclairage);
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
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeEquipementElectromenagerEnum equipementElectromenager = (TypeEquipementElectromenagerEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.EQUIPEMENT_ELECTROMENAGER, equipementElectromenager);
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
                            nbHabitant = (double) items.get("lastValue");
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
                            nbJoursAbsenceParAn = (double) items.get("lastValue");
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            HashMap<String, Object> currentItems = new HashMap<String, Object>();
                            currentItems.put("lastValue", nbJoursAbsenceParAn);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.NOMBRE_JOURS_ABSENCE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case SOURCE_CHAUFFAGE :{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        Layout layout = (Layout) items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeDeSourceEnum typeSource = (TypeDeSourceEnum) items.get("lastValue");
                            if (typeSource == TypeDeSourceEnum.CHAUFFAGE_UNIQUE) {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), false);
                            } else if (typeSource == TypeDeSourceEnum.CHAUFFAGE_AVEC_POIL_OU_INSERT_BOIS){
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), false);
                            } else if (typeSource == TypeDeSourceEnum.CHAUDIERE_GAZ_OU_FIOUL_AVEC_CHAUDIERE_BOIS){
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), false);
                            } else if (typeSource == TypeDeSourceEnum.CHAUDIERE_AVEC_PAC){
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), false);
                            } else if (typeSource == TypeDeSourceEnum.CHAUDIERE_AVEC_PAC_ET_INSERT_BOIS){
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), true);
                            }
                            chauffage_properties.put(DpeEvent.SOURCE_CHAUFFAGE, typeSource);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeDeSourceEnum type = (TypeDeSourceEnum) chauffage_properties.get(DpeEvent.SOURCE_CHAUFFAGE);

                            HashMap<String, Object> currentItems = new HashMap<String, Object>();
                            currentItems.put("lastValue", type);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.SOURCE_CHAUFFAGE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);

                            // wait for layout to be  populated

                            while (!layout.isInitialised()) {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException ie) {

                                }
                            }
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), false);
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), false);
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), false);
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), false);
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), false);
                        }
                        break;
                    }

                    case TYPE_CHAUFFAGE :{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeChauffageEnum typeChauffage = (TypeChauffageEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.TYPE_CHAUFFAGE, typeChauffage);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeChauffageEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.TYPE_CHAUFFAGE)){
                                type = (TypeChauffageEnum) chauffage_properties.get(DpeEvent.TYPE_CHAUFFAGE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_CHAUFFAGE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_CHAUFFAGE_SANS_POIL: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeChauffageSansPoilEnum typeChauffage = (TypeChauffageSansPoilEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.TYPE_CHAUFFAGE_SANS_POIL, typeChauffage);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeChauffageSansPoilEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.TYPE_CHAUFFAGE_SANS_POIL)){
                                type = (TypeChauffageSansPoilEnum) chauffage_properties.get(DpeEvent.TYPE_CHAUFFAGE_SANS_POIL);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_CHAUFFAGE_SANS_POIL, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_CHAUDIERE_CHAUFFAGE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeChaudiereEnum typeChaudiere = (TypeChaudiereEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.TYPE_CHAUDIERE_CHAUFFAGE, typeChaudiere);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeChaudiereEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.TYPE_CHAUDIERE_CHAUFFAGE)){
                                type = (TypeChaudiereEnum) chauffage_properties.get(DpeEvent.TYPE_CHAUDIERE_CHAUFFAGE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_CHAUDIERE_CHAUFFAGE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_CHAUDIERE_GAZ_FIOUL:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeChaudiereGazFioulEnum typeChaudiere = (TypeChaudiereGazFioulEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.TYPE_CHAUDIERE_GAZ_FIOUL, typeChaudiere);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeChaudiereGazFioulEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.TYPE_CHAUDIERE_GAZ_FIOUL)){
                                type = (TypeChaudiereGazFioulEnum) chauffage_properties.get(DpeEvent.TYPE_CHAUDIERE_GAZ_FIOUL);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_CHAUDIERE_GAZ_FIOUL, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_CHAUDIERE_BOIS:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeChaudiereBoisEnum typeChaudiere = (TypeChaudiereBoisEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.TYPE_CHAUDIERE_BOIS, typeChaudiere);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeChaudiereBoisEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.TYPE_CHAUDIERE_BOIS)){
                                type = (TypeChaudiereBoisEnum) chauffage_properties.get(DpeEvent.TYPE_CHAUDIERE_BOIS);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_CHAUDIERE_BOIS, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_CHAUDIERE_ECS:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeChaudiereEnum typeChaudiere = (TypeChaudiereEnum) items.get("lastValue");
                            ecs_properties.put(DpeEvent.TYPE_CHAUDIERE_ECS, typeChaudiere);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeChaudiereEnum type = null;
                            if (ecs_properties.containsKey(DpeEvent.TYPE_CHAUDIERE_ECS)){
                                type = (TypeChaudiereEnum) ecs_properties.get(DpeEvent.TYPE_CHAUDIERE_ECS);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_CHAUDIERE_ECS, currentItems);
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
                            FrequenceUtilisationPoilEnum frequenceUtilisationPoil = (FrequenceUtilisationPoilEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.FREQUENCE_UTILISATION_POIL_OU_INSERT, frequenceUtilisationPoil);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            FrequenceUtilisationPoilEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.FREQUENCE_UTILISATION_POIL_OU_INSERT)){
                                type = (FrequenceUtilisationPoilEnum) chauffage_properties.get(DpeEvent.FREQUENCE_UTILISATION_POIL_OU_INSERT);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.FREQUENCE_UTILISATION_POIL_OU_INSERT, currentItems);
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
                            if (typeEquipementEcs == TypeEquipementEcsEnum.BALLON_ELECTRIQUE) {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_ballon_electrique"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffe_eau"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_accumulateur"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chaudiere"), false);
                            } else if (typeEquipementEcs == TypeEquipementEcsEnum.CHAUFFE_EAU){
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_ballon_electrique"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffe_eau"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_accumulateur"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chaudiere"), false);
                            } else if (typeEquipementEcs == TypeEquipementEcsEnum.ACCUMULATEUR){
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_ballon_electrique"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffe_eau"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_accumulateur"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chaudiere"), false);
                            } else if (typeEquipementEcs == TypeEquipementEcsEnum.CHAUDIERE){
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_ballon_electrique"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffe_eau"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_accumulateur"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chaudiere"), true);
                            }
                            ecs_properties.put(DpeEvent.TYPE_EQUIPEMENT_ECS, typeEquipementEcs);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {

                            TypeEquipementEcsEnum type = (TypeEquipementEcsEnum) ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS);

                            HashMap<String, Object> currentItems = new HashMap<String, Object>();
                            currentItems.put("lastValue", type);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_EQUIPEMENT_ECS, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);

                            // wait for layout to be  populated

                            while (!layout.isInitialised()) {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException ie) {

                                }
                            }
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_ballon_electrique"), false);
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffe_eau"), false);
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_accumulateur"), false);
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chaudiere"), false);
                        }
                        break;
                    }

                    case TYPE_BALLON_ELECTRIQUE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeBallonElectriqueEnum typeBallonElectrique= (TypeBallonElectriqueEnum) items.get("lastValue");
                            ecs_properties.put(DpeEvent.TYPE_BALLON_ELECTRIQUE, typeBallonElectrique);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeBallonElectriqueEnum type = null;
                            if (ecs_properties.containsKey(DpeEvent.TYPE_BALLON_ELECTRIQUE)){
                                type = (TypeBallonElectriqueEnum) ecs_properties.get(DpeEvent.TYPE_BALLON_ELECTRIQUE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_BALLON_ELECTRIQUE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_CHAUFFE_EAU:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeChauffeEauEnum typeChauffeEau= (TypeChauffeEauEnum) items.get("lastValue");
                            ecs_properties.put(DpeEvent.TYPE_CHAUFFE_EAU, typeChauffeEau);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeChauffeEauEnum type = null;
                            if (ecs_properties.containsKey(DpeEvent.TYPE_CHAUFFE_EAU)){
                                type = (TypeChauffeEauEnum) ecs_properties.get(DpeEvent.TYPE_CHAUFFE_EAU);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_CHAUFFE_EAU, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_ACCUMULATEUR:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeAccumulateurEnum typeAccumulateur= (TypeAccumulateurEnum) items.get("lastValue");
                            ecs_properties.put(DpeEvent.TYPE_ACCUMULATEUR, typeAccumulateur);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeAccumulateurEnum type = null;
                            if (ecs_properties.containsKey(DpeEvent.TYPE_ACCUMULATEUR)){
                                type = (TypeAccumulateurEnum) ecs_properties.get(DpeEvent.TYPE_ACCUMULATEUR);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_ACCUMULATEUR, currentItems);
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
                            Layout layout = (Layout)items.get("layout");
                            if (!walls_properties.containsKey(mur))
                                walls_properties.put(mur, new HashMap<EventType, Object>());
                            walls_properties.get(mur).put(event, typeMur);
                            if (!typeMur.equals(TypeMurEnum.MUR_INTERIEUR)) {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("date_isolation"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_isolation"), true);
                            } else {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("date_isolation"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_isolation"), false);
                            }
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            TypeMurEnum type = null;
                            if (walls_properties.containsKey(mur))
                                if (walls_properties.get(mur).containsKey(DpeEvent.TYPE_MUR))
                                    type = (TypeMurEnum) walls_properties.get(mur).get(DpeEvent.TYPE_MUR);
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
                            Layout layout = (Layout) items.get("layout");
                            if (!walls_properties.containsKey(mur))
                                walls_properties.put(mur, new HashMap<EventType, Object>());
                            walls_properties.get(mur).put(event, dateIsolationMur);
                            if (dateIsolationMur.equals(DateIsolationMurEnum.JAMAIS) || dateIsolationMur.equals(DateIsolationMurEnum.INCONNUE)) {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_isolation"), false);
                            } else {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_isolation"), true);
                            }
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            DateIsolationMurEnum type = null;
                            if (walls_properties.containsKey(mur))
                                if (walls_properties.get(mur).containsKey(DpeEvent.DATE_ISOLATION_MUR))
                                    type = (DateIsolationMurEnum) walls_properties.get(mur).get(DpeEvent.DATE_ISOLATION_MUR);
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
                            if (!walls_properties.containsKey(mur))
                                walls_properties.put(mur, new HashMap<EventType, Object>());
                            walls_properties.get(mur).put(event, typeIsolationMur);
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            TypeIsolationMurEnum type = null;
                            if (walls_properties.containsKey(mur))
                                if (walls_properties.get(mur).containsKey(DpeEvent.TYPE_ISOLATION_MUR))
                                    type = (TypeIsolationMurEnum) walls_properties.get(mur).get(DpeEvent.TYPE_ISOLATION_MUR);
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
                            OrientationMurEnum orientationMur = (OrientationMurEnum) items.get("lastValue");
                            if (!walls_properties.containsKey(mur))
                                walls_properties.put(mur, new HashMap<EventType, Object>());
                            walls_properties.get(mur).put(event, orientationMur);
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            OrientationMurEnum type = null;
                            if (walls_properties.containsKey(mur))
                                if (walls_properties.get(mur).containsKey(DpeEvent.ORIENTATION_MUR))
                                    type = (OrientationMurEnum) walls_properties.get(mur).get(DpeEvent.ORIENTATION_MUR);
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", mur);
                            Event e2 = new Event(DpeEvent.ORIENTATION_MUR, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_FENETRE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        Fenetre fenetre = (Fenetre)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeFenetreEnum typeFenetre = (TypeFenetreEnum)items.get("lastValue");
                            if (!windows_properties.containsKey(fenetre)){
                                windows_properties.put(fenetre, new HashMap<EventType, Object>());
                            }
                            windows_properties.get(fenetre).put(event, typeFenetre);

                        } else if (eventRequest == EventRequest.GET_STATE) {
                            TypeFenetreEnum type = null;
                            if (windows_properties.containsKey(fenetre))
                                if (windows_properties.get(fenetre).containsKey(DpeEvent.TYPE_FENETRE))
                                    type = (TypeFenetreEnum)windows_properties.get(fenetre).get(DpeEvent.TYPE_FENETRE);
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
                        if (items.get("userObject") instanceof IfcWindow){
                            Fenetre fenetre = (Fenetre)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMateriauMenuiserieEnum typeMateriauMenuiserie = (TypeMateriauMenuiserieEnum)items.get("lastValue");
                                if (!windows_properties.containsKey(fenetre))
                                    windows_properties.put(fenetre, new HashMap<EventType, Object>());
                                windows_properties.get(fenetre).put(event, typeMateriauMenuiserie);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMateriauMenuiserieEnum type = null;
                                if (windows_properties.containsKey(fenetre))
                                    if (windows_properties.get(fenetre).containsKey(DpeEvent.TYPE_MATERIAU_MENUISERIE))
                                        type = (TypeMateriauMenuiserieEnum) windows_properties.get(fenetre).get(DpeEvent.TYPE_MATERIAU_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", fenetre);
                                Event e2 = new Event(DpeEvent.TYPE_MATERIAU_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }
                        }
                        else if (items.get("userObject") instanceof IfcDoor){
                            Porte porte = (Porte)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMateriauMenuiserieEnum typeMateriauMenuiserie = (TypeMateriauMenuiserieEnum)items.get("lastValue");
                                if (!doors_properties.containsKey(porte))
                                    doors_properties.put(porte, new HashMap<EventType, Object>());
                                doors_properties.get(porte).put(event, typeMateriauMenuiserie);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMateriauMenuiserieEnum type = null;
                                if (doors_properties.containsKey(porte))
                                    if (doors_properties.get(porte).containsKey(DpeEvent.TYPE_MATERIAU_MENUISERIE))
                                        type = (TypeMateriauMenuiserieEnum) doors_properties.get(porte).get(DpeEvent.TYPE_MATERIAU_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", porte);
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
                            Fenetre fenetre = (Fenetre)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeVitrageEnum TypeVitrage = (TypeVitrageEnum)items.get("lastValue");
                                if (!windows_properties.containsKey(fenetre))
                                    windows_properties.put(fenetre, new HashMap<EventType, Object>());
                                windows_properties.get(fenetre).put(event, TypeVitrage);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeVitrageEnum type = null;
                                if (windows_properties.containsKey(fenetre))
                                    if (windows_properties.get(fenetre).containsKey(DpeEvent.TYPE_VITRAGE_MENUISERIE))
                                        type = (TypeVitrageEnum) windows_properties.get(fenetre).get(DpeEvent.TYPE_VITRAGE_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", fenetre);
                                Event e2 = new Event(DpeEvent.TYPE_VITRAGE_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }

                        }
                        else if (items.get("userObject") instanceof IfcDoor){
                            Porte porte = (Porte)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeVitrageEnum TypeVitrage = (TypeVitrageEnum)items.get("lastValue");
                                if (!doors_properties.containsKey(porte))
                                    doors_properties.put(porte, new HashMap<EventType, Object>());
                                doors_properties.get(porte).put(event, TypeVitrage);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeVitrageEnum type = null;
                                if (doors_properties.containsKey(porte))
                                    if (doors_properties.get(porte).containsKey(DpeEvent.TYPE_VITRAGE_MENUISERIE))
                                        type = (TypeVitrageEnum) doors_properties.get(porte).get(DpeEvent.TYPE_VITRAGE_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", porte);
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
                            Fenetre fenetre = (Fenetre)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeFermetureEnum typeFermeture = (TypeFermetureEnum)items.get("lastValue");
                                if (!windows_properties.containsKey(fenetre))
                                    windows_properties.put(fenetre, new HashMap<EventType, Object>());
                                windows_properties.get(fenetre).put(event, typeFermeture);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeFermetureEnum type = null;
                                if (windows_properties.containsKey(fenetre))
                                    if (windows_properties.get(fenetre).containsKey(DpeEvent.TYPE_FERMETURE_MENUISERIE))
                                        type = (TypeFermetureEnum) windows_properties.get(fenetre).get(DpeEvent.TYPE_FERMETURE_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", fenetre);
                                Event e2 = new Event(DpeEvent.TYPE_FERMETURE_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }

                        }
                        else if (items.get("userObject") instanceof IfcDoor){
                            Porte porte = (Porte)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeFermetureEnum typeFermeture = (TypeFermetureEnum)items.get("lastValue");
                                if (!doors_properties.containsKey(porte))
                                    doors_properties.put(porte, new HashMap<EventType, Object>());
                                doors_properties.get(porte).put(event, typeFermeture);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeFermetureEnum type = null;
                                if (doors_properties.containsKey(porte))
                                    if (doors_properties.get(porte).containsKey(DpeEvent.TYPE_FERMETURE_MENUISERIE))
                                        type = (TypeFermetureEnum) doors_properties.get(porte).get(DpeEvent.TYPE_FERMETURE_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", porte);
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
                            Fenetre fenetre = (Fenetre)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMasqueEnum masque = (TypeMasqueEnum)items.get("lastValue");
                                if (!windows_properties.containsKey(fenetre))
                                    windows_properties.put(fenetre, new HashMap<EventType, Object>());
                                windows_properties.get(fenetre).put(event, masque);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMasqueEnum type = null;
                                if (windows_properties.containsKey(fenetre))
                                    if (windows_properties.get(fenetre).containsKey(DpeEvent.MASQUE_PROCHE_MENUISERIE))
                                        type = (TypeMasqueEnum) windows_properties.get(fenetre).get(DpeEvent.MASQUE_PROCHE_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", fenetre);
                                Event e2 = new Event(DpeEvent.MASQUE_PROCHE_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }

                        }
                        else if (items.get("userObject") instanceof IfcDoor){
                            Porte porte = (Porte)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMasqueEnum masque = (TypeMasqueEnum)items.get("lastValue");
                                if (!doors_properties.containsKey(porte))
                                    doors_properties.put(porte, new HashMap<EventType, Object>());
                                doors_properties.get(porte).put(event, masque);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMasqueEnum type = null;
                                if (doors_properties.containsKey(porte))
                                    if (doors_properties.get(porte).containsKey(DpeEvent.MASQUE_PROCHE_MENUISERIE))
                                        type = (TypeMasqueEnum) doors_properties.get(porte).get(DpeEvent.MASQUE_PROCHE_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", porte);
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
                            Fenetre fenetre = (Fenetre)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMasqueEnum masque = (TypeMasqueEnum)items.get("lastValue");
                                if (!windows_properties.containsKey(fenetre))
                                    windows_properties.put(fenetre, new HashMap<EventType, Object>());
                                windows_properties.get(fenetre).put(event, masque);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMasqueEnum type = null;
                                if (windows_properties.containsKey(fenetre))
                                    if (windows_properties.get(fenetre).containsKey(DpeEvent.MASQUE_LOINTAIN_MENUISERIE))
                                        type = (TypeMasqueEnum) windows_properties.get(fenetre).get(DpeEvent.MASQUE_LOINTAIN_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", fenetre);
                                Event e2 = new Event(DpeEvent.MASQUE_LOINTAIN_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }

                        }
                        else if (items.get("userObject") instanceof IfcDoor){
                            Porte porte = (Porte)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMasqueEnum masque = (TypeMasqueEnum)items.get("lastValue");
                                if (!doors_properties.containsKey(porte))
                                    doors_properties.put(porte, new HashMap<EventType, Object>());
                                doors_properties.get(porte).put(event, masque);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMasqueEnum type = null;
                                if (doors_properties.containsKey(porte))
                                    if (doors_properties.get(porte).containsKey(DpeEvent.MASQUE_LOINTAIN_MENUISERIE))
                                        type = (TypeMasqueEnum) doors_properties.get(porte).get(DpeEvent.MASQUE_LOINTAIN_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", porte);
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
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeDoorEnum typeDoor = (TypeDoorEnum)items.get("lastValue");
                            Layout layout = (Layout)items.get("layout");
                            if (!doors_properties.containsKey(porte))
                                doors_properties.put(porte, new HashMap<EventType, Object>());
                            doors_properties.get(porte).put(event, typeDoor);
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
                            if (doors_properties.containsKey(porte))
                                if (doors_properties.get(porte).containsKey(DpeEvent.TYPE_PORTE))
                                    type = (TypeDoorEnum) doors_properties.get(porte).get(DpeEvent.TYPE_PORTE);
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", porte);
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
