package fr.limsi.rorqual.core.dpe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

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
    TypeBatimentEnum typeBatiment;
    PositionAppartementEnum positionAppartement;
    TypeEnergieConstructionEnum typeEnergieConstruction;
    TypeVentilationEnum typeVentilation;
    TypeVmcEnum typeVmc;
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
        typeBatiment=TypeBatimentEnum.INCONNUE;
        positionAppartement=PositionAppartementEnum.INCONNUE;
        typeEnergieConstruction=TypeEnergieConstructionEnum.INCONNUE;
        typeVentilation=TypeVentilationEnum.INCONNUE;
        typeVmc=TypeVmcEnum.INCONNUE;
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


        actualiseDP_wall(wall, typeMur, dateIsolationMur, typeIsolationMur);
    }

    public void tryActualiseWindowDP(IfcWindow window) {
        if (!windows_properties.get(window).containsKey(DpeEvent.TYPE_FENETRE_RESPONSE))
            return;
        TypeFenetreEnum typeFenetre = (TypeFenetreEnum) windows_properties.get(window).get(DpeEvent.TYPE_FENETRE_RESPONSE);

        if (!windows_properties.get(window).containsKey(DpeEvent.TYPE_MATERIAU_MENUISERIE_RESPONSE))
            return;
        TypeMateriauMenuiserieEnum typeMenuiserieFenetre = (TypeMateriauMenuiserieEnum) windows_properties.get(window).get(DpeEvent.TYPE_MATERIAU_MENUISERIE_RESPONSE);

        if (!windows_properties.get(window).containsKey(DpeEvent.TYPE_VITRAGE_MENUISERIE_RESPONSE))
            return;
        TypeVitrageEnum typeVitrageFenetre = (TypeVitrageEnum) windows_properties.get(window).get(DpeEvent.TYPE_VITRAGE_MENUISERIE_RESPONSE);

        // On signal au model que le calcul thermique vient d'être effectuer sur window
        Object o[] = {window, DpeState.KNOWN};
        Event e = new Event(DpeEvent.DPE_STATE_CHANGED, o);
        EventManager.getInstance().put(Channel.DPE, e);

        actualiseDP_window(window, typeFenetre, typeMenuiserieFenetre, typeVitrageFenetre);
    }

    public void tryActualiseDoorDP(IfcDoor door) {
        if (!doors_properties.get(door).containsKey(DpeEvent.TYPE_DOOR_RESPONSE))
            return;
        TypeDoorEnum typeDoor = (TypeDoorEnum) doors_properties.get(door).get(DpeEvent.TYPE_DOOR_RESPONSE);

        TypeMateriauMenuiserieEnum typeMenuiserieDoor = TypeMateriauMenuiserieEnum.INCONNUE;
        if (doors_properties.get(door).containsKey(DpeEvent.TYPE_MATERIAU_MENUISERIE_RESPONSE)){
            typeMenuiserieDoor = (TypeMateriauMenuiserieEnum)doors_properties.get(door).get(DpeEvent.TYPE_MATERIAU_MENUISERIE_RESPONSE);
        }

        TypeVitrageEnum typeVitrage = TypeVitrageEnum.INCONNUE;
        if (doors_properties.get(door).containsKey(DpeEvent.TYPE_VITRAGE_MENUISERIE_RESPONSE)){
            typeVitrage = (TypeVitrageEnum)doors_properties.get(door).get(DpeEvent.TYPE_VITRAGE_MENUISERIE_RESPONSE);
        }

        // On signal au model que le calcul thermique vient d'être effectuer sur wall
        Object o[] = {door, DpeState.KNOWN};
        Event e = new Event(DpeEvent.DPE_STATE_CHANGED, o);
        EventManager.getInstance().put(Channel.DPE, e);

        actualiseDP_door(door, typeDoor, typeMenuiserieDoor, typeVitrage);
    }

    public void actualiseDP_wall(IfcWallStandardCase wall, TypeMurEnum typeMur, DateIsolationMurEnum dateIsolationMur, TypeIsolationMurEnum typeIsolationMur){
        double uMur=0,sMur=ifcHelper.getWallSurface(wall);
        if (dateIsolationMur.equals(DateIsolationMurEnum.JAMAIS)){
            uMur=2.5;
        }
        else if (dateIsolationMur.equals(DateIsolationMurEnum.INCONNUE) || dateIsolationMur.equals(DateIsolationMurEnum.A_LA_CONSTRUCTION) || dateIsolationMur.equals(DateIsolationMurEnum.EN_RENOVATION_DATE_INCONNUE)){
            if (anneeConstruction<1975){
                if (dateIsolationMur.equals(DateIsolationMurEnum.A_LA_CONSTRUCTION) || dateIsolationMur.equals(DateIsolationMurEnum.EN_RENOVATION_DATE_INCONNUE)){
                    uMur=0.8;
                }else{
                    uMur=2.5;
                }
                if (typeIsolationMur.equals(TypeIsolationMurEnum.INCONNUE)){
                    ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.NON_ISOLE);
                }
            }
            if (anneeConstruction>=1975 && anneeConstruction<=1977){
                uMur=1;
                if (typeIsolationMur.equals(TypeIsolationMurEnum.INCONNUE)){
                    ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
                }
            }
            if (anneeConstruction>=1978 && anneeConstruction<=1982){
                if (typeEnergieConstruction.equals(TypeEnergieConstructionEnum.ELECTRIQUE)){
                    uMur=0.8;
                }else{
                    uMur=1;
                }
                if (typeIsolationMur.equals(TypeIsolationMurEnum.INCONNUE)){
                    ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
                }
            }
            if (anneeConstruction>=1983 && anneeConstruction<=1988){
                if (typeEnergieConstruction.equals(TypeEnergieConstructionEnum.ELECTRIQUE)){
                    uMur=0.7;
                }else{
                    uMur=0.8;
                }
                if (typeIsolationMur.equals(TypeIsolationMurEnum.INCONNUE)){
                    ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
                }
            }
            if (anneeConstruction>=1989 && anneeConstruction<=2000){
                if (typeEnergieConstruction.equals(TypeEnergieConstructionEnum.ELECTRIQUE)){
                    uMur=0.45;
                }else{
                    uMur=0.5;
                }
                if (typeIsolationMur.equals(TypeIsolationMurEnum.INCONNUE)){
                    ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
                }
            }
            if (anneeConstruction>=2001 && anneeConstruction<=2005){
                uMur=0.4;
                if (typeIsolationMur.equals(TypeIsolationMurEnum.INCONNUE)){
                    ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
                }
            }
            if (anneeConstruction>=2006 && anneeConstruction<=2012){
                uMur=0.35;
                if (typeIsolationMur.equals(TypeIsolationMurEnum.INCONNUE)){
                    ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITE);
                }
            }
            if (anneeConstruction>2012){
                uMur=0.2;
                if (typeIsolationMur.equals(TypeIsolationMurEnum.INCONNUE)){
                    ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITE);
                }
            }
        }
        else if (dateIsolationMur.equals(DateIsolationMurEnum.EN_RENOVATION_AVANT_1983)){
            uMur=0.82;
            if (typeIsolationMur.equals(TypeIsolationMurEnum.INCONNUE)){
                ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
            }
        }
        else if (dateIsolationMur.equals(DateIsolationMurEnum.EN_RENOVATION_ENTRE_1983_ET_1988)){
            uMur=0.75;
            if (typeIsolationMur.equals(TypeIsolationMurEnum.INCONNUE)){
                ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
            }
        }
        else if (dateIsolationMur.equals(DateIsolationMurEnum.EN_RENOVATION_ENTRE_1989_ET_2000)){
            uMur=0.48;
            if (typeIsolationMur.equals(TypeIsolationMurEnum.INCONNUE)){
                ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
            }
        }
        else if (dateIsolationMur.equals(DateIsolationMurEnum.EN_RENOVATION_ENTRE_2001_ET_2005)){
            uMur=0.42;
            if (typeIsolationMur.equals(TypeIsolationMurEnum.INCONNUE)){
                ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
            }
        }
        else if (dateIsolationMur.equals(DateIsolationMurEnum.EN_RENOVATION_ENTRE_2006_ET_2012)){
            uMur=0.36;
            if (typeIsolationMur.equals(TypeIsolationMurEnum.INCONNUE)){
                ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
            }
        }
        else if (dateIsolationMur.equals(DateIsolationMurEnum.EN_RENOVATION_APRES_2012)){
            uMur=0.24;
            if (typeIsolationMur.equals(TypeIsolationMurEnum.INCONNUE)){
                ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
            }
        }

        if (typeMur.equals(TypeMurEnum.MUR_DONNANT_SUR_EXTERIEUR)){
            DP_murExt += sMur*uMur;
            System.out.println("Actualise DP Mur ext -> U="+uMur+"  S="+sMur+"  DPmurExt="+DP_murExt);
        }
        else if (typeMur.equals(TypeMurEnum.MUR_DONNANT_SUR_UNE_AUTRE_HABITATION)){
            DP_murAh += sMur*uMur*0.2;
            System.out.println("Actualise DP Mur ah -> U="+uMur+"  S="+sMur+" b=0.2"+"  DPmurAh="+DP_murAh);
        }
        else if (typeMur.equals(TypeMurEnum.MUR_DONNANT_SUR_UN_LOCAL_NON_CHAUFFE)){
            double b_lnc=0;
            if (dateIsolationMur.equals(DateIsolationMurEnum.INCONNUE) || dateIsolationMur.equals(DateIsolationMurEnum.JAMAIS)){
                b_lnc=0.95;
            }else{
                b_lnc=0.85;
            }
            DP_murLnc += sMur*uMur*b_lnc;
            System.out.println("Actualise DP Mur lnc -> U="+uMur+"  S="+sMur+" b="+b_lnc+"  DPmurLnc="+DP_murLnc);
        }
        else if (typeMur.equals(TypeMurEnum.MUR_DONNANT_SUR_UNE_VERANDA_NON_CHAUFFE)){
            double b_ver=0.6;
            // TODO -> quand on connaitra l'orientation du mur, on pourra affiner le calcul de b_ver
            DP_murVer += sMur*uMur*b_ver;
            System.out.println("Actualise DP Mur ver -> U="+uMur+"  S="+sMur+" b="+b_ver+"  DPmurVer="+DP_murVer);
        }
        else if (typeMur.equals(TypeMurEnum.MUR_DONNANT_SUR_UNE_VERANDA_CHAUFFE)){
            DP_murVer += sMur*uMur;
            System.out.println("Actualise DP Mur ver -> U="+uMur+"  S="+sMur+"  DPmurVer="+DP_murVer);
        }
        ifcHelper.addPropertyTransmittanceThermiqueWall(wall, String.valueOf(uMur));
    }

    public void actualiseDP_window(IfcWindow window,TypeFenetreEnum typeFenetre,TypeMateriauMenuiserieEnum typeMenuiserie,TypeVitrageEnum typeVitrage){
        double uFen=0,sFen=ifcHelper.getWindowSurface(window);
        if (typeMenuiserie.equals(TypeMateriauMenuiserieEnum.METALLIQUE)){
            if (typeFenetre.equals(TypeFenetreEnum.BATTANTE)){
                if (typeVitrage.equals(TypeVitrageEnum.SIMPLE_VITRAGE)){
                    uFen=4.95;
                    ifcHelper.addPropertyTypeMenuiserie(window,TypeMateriauMenuiserieEnum.METALLIQUE_AVANT_2001);
                }
                else if (typeVitrage.equals(TypeVitrageEnum.SURVITRAGE)){
                    uFen=4;
                    ifcHelper.addPropertyTypeMenuiserie(window,TypeMateriauMenuiserieEnum.METALLIQUE_AVANT_2001);
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_INF_1990)){
                    uFen=3.7;
                    ifcHelper.addPropertyTypeMenuiserie(window,TypeMateriauMenuiserieEnum.METALLIQUE_AVANT_2001);
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_1990_INF_2001)){
                    uFen=3.6;
                    ifcHelper.addPropertyTypeMenuiserie(window,TypeMateriauMenuiserieEnum.METALLIQUE_AVANT_2001);
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_2001)){
                    uFen=2.25;
                    ifcHelper.addPropertyTypeMenuiserie(window,TypeMateriauMenuiserieEnum.METALLIQUE_APRES_2001);
                }
                else if (typeVitrage.equals(TypeVitrageEnum.TRIPLE_VITRAGE)){
                    uFen=1.88;
                    ifcHelper.addPropertyTypeMenuiserie(window,TypeMateriauMenuiserieEnum.METALLIQUE_APRES_2001);
                }
            }

            if (typeFenetre.equals(TypeFenetreEnum.COULISSANTE)){
                if (typeVitrage.equals(TypeVitrageEnum.SIMPLE_VITRAGE)){
                    uFen=4.63;
                    ifcHelper.addPropertyTypeMenuiserie(window,TypeMateriauMenuiserieEnum.METALLIQUE_AVANT_2001);
                }
                else if (typeVitrage.equals(TypeVitrageEnum.SURVITRAGE)){
                    uFen=3.46;
                    ifcHelper.addPropertyTypeMenuiserie(window,TypeMateriauMenuiserieEnum.METALLIQUE_AVANT_2001);
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_INF_1990)){
                    uFen=3.46;
                    ifcHelper.addPropertyTypeMenuiserie(window,TypeMateriauMenuiserieEnum.METALLIQUE_AVANT_2001);
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_1990_INF_2001)){
                    uFen=3.36;
                    ifcHelper.addPropertyTypeMenuiserie(window,TypeMateriauMenuiserieEnum.METALLIQUE_AVANT_2001);
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_2001)){
                    uFen=2.18;
                    ifcHelper.addPropertyTypeMenuiserie(window,TypeMateriauMenuiserieEnum.METALLIQUE_APRES_2001);
                }
                else if (typeVitrage.equals(TypeVitrageEnum.TRIPLE_VITRAGE)){
                    uFen=1.65;
                    ifcHelper.addPropertyTypeMenuiserie(window,TypeMateriauMenuiserieEnum.METALLIQUE_APRES_2001);
                }
            }
        }
        else if (typeMenuiserie.equals(TypeMateriauMenuiserieEnum.PVC)){
            if (typeFenetre.equals(TypeFenetreEnum.BATTANTE)){
                if (typeVitrage.equals(TypeVitrageEnum.SIMPLE_VITRAGE)){
                    uFen=3.9;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.SURVITRAGE)){
                    uFen=2.75;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_INF_1990)){
                    uFen=2.45;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_1990_INF_2001)){
                    uFen=2.35;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_2001)){
                    uFen=1.7;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.TRIPLE_VITRAGE)){
                    uFen=1.24;
                }
            }

            if (typeFenetre.equals(TypeFenetreEnum.COULISSANTE)){
                if (typeVitrage.equals(TypeVitrageEnum.SIMPLE_VITRAGE)){
                    uFen=4.25;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.SURVITRAGE)){
                    uFen=3;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_INF_1990)){
                    uFen=2.62;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_1990_INF_2001)){
                    uFen=2.52;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_2001)){
                    uFen=1.85;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.TRIPLE_VITRAGE)){
                    uFen=1.39;
                }
            }
        }
        else if (typeMenuiserie.equals(TypeMateriauMenuiserieEnum.BOIS)){
            if (typeFenetre.equals(TypeFenetreEnum.BATTANTE)){
                if (typeVitrage.equals(TypeVitrageEnum.SIMPLE_VITRAGE)){
                    uFen=4.2;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.SURVITRAGE)){
                    uFen=2.9;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_INF_1990)){
                    uFen=2.7;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_1990_INF_2001)){
                    uFen=2.55;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_2001)){
                    uFen=1.75;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.TRIPLE_VITRAGE)){
                    uFen=1.24;
                }
            }

            if (typeFenetre.equals(TypeFenetreEnum.COULISSANTE)){
                if (typeVitrage.equals(TypeVitrageEnum.SIMPLE_VITRAGE)){
                    uFen=4.2;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.SURVITRAGE)){
                    uFen=2.9;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_INF_1990)){
                    uFen=2.7;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_1990_INF_2001)){
                    uFen=2.55;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_2001)){
                    uFen=1.75;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.TRIPLE_VITRAGE)){
                    uFen=1.24;
                }
            }
        }


        IfcWallStandardCase wallRelToWindow = ifcHelper.getWallRelToWindow(window);
        if(ifcHelper.getPropertiesWall(wallRelToWindow,WallPropertiesEnum.TYPE_DE_MUR).equals(TypeMurEnum.MUR_DONNANT_SUR_UNE_VERANDA_NON_CHAUFFE.toString())){
            double b_ver=0.6;
            // TODO considérer l'orientation du mur pour mieux fixer la valeur de b_ver
            DP_fenVer += b_ver*sFen*uFen;
            System.out.println("Actualise DP_fenVer -> U="+uFen+"  S="+sFen+" b="+b_ver+"  DPmurLnc="+DP_fenVer);
        }

        DP_fen += sFen*uFen;
        System.out.println("Actualise DP Fen -> U="+uFen+"  S="+sFen+"  DPfen="+DP_fen);
        ifcHelper.addPropertyTransmittanceThermiqueWindow(window, String.valueOf(uFen));
    }

    public void actualiseDP_door(IfcDoor door, TypeDoorEnum typeDoor, TypeMateriauMenuiserieEnum typeMateriauMenuiserieDoor, TypeVitrageEnum typeVitrage){
        double sDoor=ifcHelper.getDoorSurface(door),uDoor=0;
        IfcWallStandardCase wallRelToDoor = ifcHelper.getWallRelToDoor(door);

        if (typeDoor.equals(TypeDoorEnum.PORTE_FENETRE_BATTANTE)){
            if (typeMateriauMenuiserieDoor.equals(TypeMateriauMenuiserieEnum.METALLIQUE)){
                if (typeVitrage.equals(TypeVitrageEnum.SIMPLE_VITRAGE)){
                    uDoor=4.87;
                    ifcHelper.addPropertyTypeMenuiserie(door, TypeMateriauMenuiserieEnum.METALLIQUE_AVANT_2001);
                }
                if (typeVitrage.equals(TypeVitrageEnum.SURVITRAGE)){
                    uDoor=3.62;
                    ifcHelper.addPropertyTypeMenuiserie(door, TypeMateriauMenuiserieEnum.METALLIQUE_AVANT_2001);
                }
                if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_INF_1990)){
                    uDoor=3.54;
                    ifcHelper.addPropertyTypeMenuiserie(door, TypeMateriauMenuiserieEnum.METALLIQUE_AVANT_2001);
                }
                if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_1990_INF_2001)){
                    uDoor=3.44;
                    ifcHelper.addPropertyTypeMenuiserie(door, TypeMateriauMenuiserieEnum.METALLIQUE_AVANT_2001);
                }
                if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_2001)){
                    uDoor=2.18;
                    ifcHelper.addPropertyTypeMenuiserie(door, TypeMateriauMenuiserieEnum.METALLIQUE_APRES_2001);
                }
                if (typeVitrage.equals(TypeVitrageEnum.TRIPLE_VITRAGE)){
                    uDoor=1.73;
                    ifcHelper.addPropertyTypeMenuiserie(door, TypeMateriauMenuiserieEnum.METALLIQUE_APRES_2001);
                }
            }
            if (typeMateriauMenuiserieDoor.equals(TypeMateriauMenuiserieEnum.PVC)){
                if (typeVitrage.equals(TypeVitrageEnum.SIMPLE_VITRAGE)){
                    uDoor=3.99;
                }
                if (typeVitrage.equals(TypeVitrageEnum.SURVITRAGE)){
                    uDoor=2.83;
                }
                if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_INF_1990)){
                    uDoor=2.45;
                }
                if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_1990_INF_2001)){
                    uDoor=2.35;
                }
                if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_2001)){
                    uDoor=1.70;
                }
                if (typeVitrage.equals(TypeVitrageEnum.TRIPLE_VITRAGE)){
                    uDoor=1.24;
                }
            }
            if (typeMateriauMenuiserieDoor.equals(TypeMateriauMenuiserieEnum.BOIS)){
                if (typeVitrage.equals(TypeVitrageEnum.SIMPLE_VITRAGE)){
                    uDoor=4.29;
                }
                if (typeVitrage.equals(TypeVitrageEnum.SURVITRAGE)){
                    uDoor=2.98;
                }
                if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_INF_1990)){
                    uDoor=2.7;
                }
                if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_1990_INF_2001)){
                    uDoor=2.55;
                }
                if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_2001)){
                    uDoor=1.75;
                }
                if (typeVitrage.equals(TypeVitrageEnum.TRIPLE_VITRAGE)){
                    uDoor=1.17;
                }
            }
            if(ifcHelper.getPropertiesWall(wallRelToDoor,WallPropertiesEnum.TYPE_DE_MUR).equals(TypeMurEnum.MUR_DONNANT_SUR_UNE_VERANDA_NON_CHAUFFE.toString())){
                double b_ver=0.6;
                // TODO considérer l'orientation du mur pour mieux fixer la valeur de b_ver
                DP_pfenVer += b_ver*sDoor*uDoor;
                System.out.println("Actualise DP_pfenVer -> U="+uDoor+"  S="+sDoor+" b="+b_ver+"  DP_pfenVer="+DP_pfenVer);
            }else{
                DP_pfen += sDoor*uDoor;
                System.out.println("Actualise DP_pfen -> U="+uDoor+"  S="+sDoor+"  DP_pfen="+DP_pfen);
            }
        }
        else if (typeDoor.equals(TypeDoorEnum.PORTE_FENETRE_COULISSANTE)){
            if (typeMateriauMenuiserieDoor.equals(TypeMateriauMenuiserieEnum.METALLIQUE)){
                if (typeVitrage.equals(TypeVitrageEnum.SIMPLE_VITRAGE)){
                    uDoor=4.79;
                    ifcHelper.addPropertyTypeMenuiserie(door, TypeMateriauMenuiserieEnum.METALLIQUE_AVANT_2001);
                }
                if (typeVitrage.equals(TypeVitrageEnum.SURVITRAGE)){
                    uDoor=3.31;
                    ifcHelper.addPropertyTypeMenuiserie(door, TypeMateriauMenuiserieEnum.METALLIQUE_AVANT_2001);
                }
                if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_INF_1990)){
                    uDoor=3.31;
                    ifcHelper.addPropertyTypeMenuiserie(door, TypeMateriauMenuiserieEnum.METALLIQUE_AVANT_2001);
                }
                if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_1990_INF_2001)){
                    uDoor=3.12;
                    ifcHelper.addPropertyTypeMenuiserie(door, TypeMateriauMenuiserieEnum.METALLIQUE_AVANT_2001);
                }
                if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_2001)){
                    uDoor=2.10;
                    ifcHelper.addPropertyTypeMenuiserie(door, TypeMateriauMenuiserieEnum.METALLIQUE_APRES_2001);
                }
                if (typeVitrage.equals(TypeVitrageEnum.TRIPLE_VITRAGE)){
                    uDoor=1.58;
                    ifcHelper.addPropertyTypeMenuiserie(door, TypeMateriauMenuiserieEnum.METALLIQUE_APRES_2001);
                }
            }
            if (typeMateriauMenuiserieDoor.equals(TypeMateriauMenuiserieEnum.PVC)){
                if (typeVitrage.equals(TypeVitrageEnum.SIMPLE_VITRAGE)){
                    uDoor=4.34;
                }
                if (typeVitrage.equals(TypeVitrageEnum.SURVITRAGE)){
                    uDoor=3;
                }
                if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_INF_1990)){
                    uDoor=2.7;
                }
                if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_1990_INF_2001)){
                    uDoor=2.61;
                }
                if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_2001)){
                    uDoor=1.85;
                }
                if (typeVitrage.equals(TypeVitrageEnum.TRIPLE_VITRAGE)){
                    uDoor=1.31;
                }
            }
            if (typeMateriauMenuiserieDoor.equals(TypeMateriauMenuiserieEnum.BOIS)){
                if (typeVitrage.equals(TypeVitrageEnum.SIMPLE_VITRAGE)){
                    uDoor=4.29;
                }
                if (typeVitrage.equals(TypeVitrageEnum.SURVITRAGE)){
                    uDoor=2.98;
                }
                if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_INF_1990)){
                    uDoor=2.7;
                }
                if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_1990_INF_2001)){
                    uDoor=2.55;
                }
                if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_2001)){
                    uDoor=1.75;
                }
                if (typeVitrage.equals(TypeVitrageEnum.TRIPLE_VITRAGE)){
                    uDoor=1.17;
                }
            }
            if(ifcHelper.getPropertiesWall(wallRelToDoor,WallPropertiesEnum.TYPE_DE_MUR).equals(TypeMurEnum.MUR_DONNANT_SUR_UNE_VERANDA_NON_CHAUFFE.toString())){
                double b_ver=0.6;
                // TODO considérer l'orientation du mur pour mieux fixer la valeur de b_ver
                DP_pfenVer += b_ver*sDoor*uDoor;
                System.out.println("Actualise DP_pfenVer -> U="+uDoor+"  S="+sDoor+" b="+b_ver+"  DP_pfenVer="+DP_pfenVer);
            }else{
                DP_pfen += sDoor*uDoor;
                System.out.println("Actualise DP_pfen -> U="+uDoor+"  S="+sDoor+"  DP_pfen="+DP_pfen);
            }
        }
        else { //pas une porte-fenetre
            if (typeDoor.equals(TypeDoorEnum.PORTE_OPAQUE_PLEINE)){
                uDoor=3.5;
            }
            else if (typeDoor.equals(TypeDoorEnum.PORTE_AVEC_MOIS_DE_30_POURCENT_DE_SIMPLE_VITRAGE)){
                uDoor=4;
            }
            else if (typeDoor.equals(TypeDoorEnum.PORTE_AVEC_30_60_POURCENT_DE_SIMPLE_VITRAGE)){
                uDoor=4.5;
            }
            else if (typeDoor.equals(TypeDoorEnum.PORTE_AVEC_DOUBLE_VITRAGE)){
                uDoor=3.3;
            }
            else if (typeDoor.equals(TypeDoorEnum.PORTE_OPAQUE_PLEINE_ISOLEE)){
                uDoor=2;
            }
            else if (typeDoor.equals(TypeDoorEnum.PORTE_PRECEDE_DUN_SAS)){
                uDoor=1.5;
            }

            if(ifcHelper.getPropertiesWall(wallRelToDoor,WallPropertiesEnum.TYPE_DE_MUR).equals(TypeMurEnum.MUR_DONNANT_SUR_EXTERIEUR.toString())){
                DP_portExt += 2*uDoor;
                System.out.println("Actualise DP_portExt -> U="+uDoor+"  S="+sDoor+"  DPdoor="+DP_portExt);
            }
            else if(ifcHelper.getPropertiesWall(wallRelToDoor,WallPropertiesEnum.TYPE_DE_MUR).equals(TypeMurEnum.MUR_DONNANT_SUR_UN_LOCAL_NON_CHAUFFE.toString())){
                double b_lnc;
                if (ifcHelper.getPropertiesWall(wallRelToDoor,WallPropertiesEnum.DATE_ISOLATION_MUR).equals(DateIsolationMurEnum.JAMAIS)
                        || ifcHelper.getPropertiesWall(wallRelToDoor,WallPropertiesEnum.DATE_ISOLATION_MUR).equals(DateIsolationMurEnum.INCONNUE)){
                    b_lnc=0.95;
                }else{
                    b_lnc=0.85;
                }
                DP_portLnc += b_lnc*2*uDoor;
                System.out.println("Actualise DP_portLnc -> U="+uDoor+"  S="+sDoor+" b="+b_lnc+"  DPmurLnc="+DP_portLnc);
            }
            else if(ifcHelper.getPropertiesWall(wallRelToDoor,WallPropertiesEnum.TYPE_DE_MUR).equals(TypeMurEnum.MUR_DONNANT_SUR_UNE_VERANDA_NON_CHAUFFE.toString())){
                double b_ver=0.6;
                // TODO considérer l'orientation du mur pour mieux fixer la valeur de b_ver
                DP_portVer += b_ver*2*uDoor;
                System.out.println("Actualise DP_portVer -> U="+uDoor+"  S="+sDoor+" b="+b_ver+"  DPmurLnc="+DP_portVer);
            }
            else if(ifcHelper.getPropertiesWall(wallRelToDoor,WallPropertiesEnum.TYPE_DE_MUR).equals(TypeMurEnum.MUR_DONNANT_SUR_UNE_VERANDA_CHAUFFE.toString())){
                DP_portVer += 2*uDoor;
                System.out.println("Actualise DP_portVer -> U="+uDoor+"  S="+sDoor+"  DPmurLnc="+DP_portVer);
            }
        }

        ifcHelper.addPropertyTransmittanceThermiqueDoor(door, String.valueOf(uDoor));
    }

    public void calc_DR() {
        double Sdep=0,Q4PaConv=0,Q4PaEnv=0,Smea=0,Q4Pa=0;
    }

    /*---------------------------------------IHM------------------------------------------------*/

    /*** Explique ce qu'est le DPE et demande de continuer ou non ***/
    public void startDPE() {
        DpeEvent eventType = DpeEvent.START_DPE;
        Event event = new Event(eventType);
        EventManager.getInstance().put(Channel.DPE, event);
        notifierMurs();
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
        Iterator<IfcWallStandardCase> it = wallCollection.iterator();
        while (it.hasNext()) {
            wall = it.next();
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

    /*** Demande à l'utilisateur des informations permettant de déterminer les déperditions au niveau des murs ***/
    public void demanderMur(IfcWallStandardCase wall) {
        walls_properties.put(wall, new HashMap<EventType, Object>());
        DpeEvent eventType = DpeEvent.TYPE_MUR;
        Event event = new Event(eventType, wall);
        EventManager.getInstance().put(Channel.DPE, event);
    }

    /*** Demande à l'utilisateur des informations permettant de déterminer les déperditions au niveau des slabs ***/
    public void demanderSlab(IfcSlab slab){
//        slabs_properties.put(slab, new HashMap<EventType, Object>());
//        DpeEvent eventType = DpeEvent.DERRIERE_SLAB;
//        Event event = new Event(eventType, slab);
//        EventManager.getInstance().put(Channel.DPE, event);
    }

    /*** Demande à l'utilisateur des informations permettant de déterminer les déperditions au niveau des windows ***/
    public void demanderWindow(IfcWindow window){
        windows_properties.put(window, new HashMap<EventType, Object>());
        DpeEvent eventType = DpeEvent.TYPE_FENETRE;
        Event event = new Event(eventType, window);
        EventManager.getInstance().put(Channel.DPE, event);

        eventType = DpeEvent.TYPE_MATERIAU_MENUISERIE;
        event = new Event(eventType, window);
        EventManager.getInstance().put(Channel.DPE, event);

        eventType = DpeEvent.TYPE_VITRAGE_MENUISERIE;
        event = new Event(eventType, window);
        EventManager.getInstance().put(Channel.DPE, event);
    }

    /*** Demande à l'utilisateur des informations permettant de déterminer les déperditions au niveau des portes ***/
    public void demanderPorte(IfcDoor door){
        doors_properties.put(door, new HashMap<EventType, Object>());
        DpeEvent eventType = DpeEvent.TYPE_DOOR;
        Event event = new Event(eventType, door);
        EventManager.getInstance().put(Channel.DPE, event);
    }

    /*** Log les valeurs du DPE dans la console ***/
    public void logValeursDpe(){
        System.out.println("**************************** LOG DPE ***********************************");
        System.out.println("typeBatiment = "+typeBatiment.toString());
        System.out.println("positionAppartement = "+positionAppartement.toString());
        System.out.println("typeEnergieConstruction = "+typeEnergieConstruction.toString());
        System.out.println("typeVentilation = "+typeVentilation.toString());
        System.out.println("typeVmc = "+typeVmc.toString());
        System.out.println("anneeConstruction = "+anneeConstruction);
        System.out.println("SH = "+SH);
        System.out.println("NIV = "+NIV);
        System.out.println("MIT = "+MIT);
        System.out.println("MIT2 = "+MIT2);
        System.out.println("FOR = "+FOR);
        System.out.println("Per = "+Per);
        System.out.println("PER = "+PER);
        System.out.println("C_niv = "+C_niv);
        System.out.println("configuration_Appartement = "+configuration_Appartement);
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

                    case START_DPE_RESPONSE: {
                        boolean response = (boolean) o;
                        if (response) {
                            this.demandeTypeBatiment();
                        }
                        break;
                    }

                    case TYPE_BATIMENT_RESPONSE: {
                        typeBatiment = (TypeBatimentEnum) o;
                        if (typeBatiment.equals(TypeBatimentEnum.MAISON)) {
                            this.demandeNbNiveau();
                        } else {
                            this.demandePositionAppartement();
                        }
                        break;
                    }

                    case NB_NIVEAUX_RESPONSE: {
                        NIV = (double) o;
                        this.demandeForme();
                        break;
                    }

                    case FORME_RESPONSE: {
                        FOR = (double) o;
                        this.demandeMitoyennete();
                        break;
                    }

                    case MITOYENNETE_RESPONSE: {
                        MIT = (double) o;
                        this.calc_MIT2();
                        this.demandeAnneeConstruction();
                        break;
                    }

                    case POSITION_APPARTEMENT_RESPONSE:{
                        positionAppartement = (PositionAppartementEnum)o;
                        this.demandeAnneeConstruction();
                        break;
                    }

                    case ANNEE_CONSTRUCTION_RESPONSE: {
                        anneeConstruction = (double) o;
                        this.demandeTypeEnergieConstruction();
                        break;
                    }

                    case ENERGIE_CONSTRUCTION_RESPONSE: {
                        typeEnergieConstruction = (TypeEnergieConstructionEnum)o;
                        break;
                    }

                    case TYPE_MUR: {
                        Object[] items = (Object[]) o;
                        IfcWallStandardCase wall = (IfcWallStandardCase)items[0];
                        if (items[1] == EventRequest.UPDATE_STATE) {
                            TypeMurEnum typeMur = (TypeMurEnum) items[2];
                            Layout layout = (Layout) items[3];
                            ifcHelper.addPropertyTypeWall(wall, typeMur);
                            if (!walls_properties.containsKey(wall))
                                walls_properties.put(wall, new HashMap<EventType, Object>());
                            walls_properties.get(wall).put(event, typeMur);
                            if (!typeMur.equals(TypeMurEnum.MUR_INTERIEUR)) {/*
                            eventType = DpeEvent.DATE_ISOLATION_MUR;
                            Event event2 = new Event(eventType, wall);
                            EventManager.getInstance().put(Channel.DPE, event2);*/
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("date_isolation"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_isolation"), true);
                            } else {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("date_isolation"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_isolation"), false);
                                // On signal au model que le calcul thermique vient d'être effectuer sur wall
                                Object o2[] = {wall, DpeState.KNOWN};
                                Event e2 = new Event(DpeEvent.DPE_STATE_CHANGED, o2);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }
                            tryActualiseWallDP(wall);
                        } else if (items[1] == EventRequest.GET_STATE) {
                            Object type = null;
                            if (walls_properties.containsKey(wall))
                                if (walls_properties.get(wall).containsKey(DpeEvent.TYPE_MUR))
                                    type = walls_properties.get(wall).get(DpeEvent.TYPE_MUR);
                            Object o2[] = {wall, EventRequest.CURRENT_STATE, type};
                            Event e2 = new Event(DpeEvent.TYPE_MUR, o2);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case DATE_ISOLATION_MUR: {
                        Object[] items = (Object[]) o;
                        IfcWallStandardCase wall = (IfcWallStandardCase)items[0];
                        if (items[1] == EventRequest.UPDATE_STATE) {
                            DateIsolationMurEnum dateIsolationMur = (DateIsolationMurEnum) items[2];
                            Layout layout = (Layout) items[3];
                            ifcHelper.addPropertyDateIsolationWall(wall, dateIsolationMur);
                            if (!walls_properties.containsKey(wall))
                                walls_properties.put(wall, new HashMap<EventType, Object>());
                            walls_properties.get(wall).put(event, dateIsolationMur);
                            if (dateIsolationMur.equals(DateIsolationMurEnum.JAMAIS) || dateIsolationMur.equals(DateIsolationMurEnum.INCONNUE)) {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_isolation"), false);
                            } else {
                                /*eventType = DpeEvent.TYPE_ISOLATION_MUR;
                                Event event2 = new Event(eventType, wall);
                                EventManager.getInstance().put(Channel.DPE, event2);*/
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_isolation"), true);
                            }
                            tryActualiseWallDP(wall);
                        } else if (items[1] == EventRequest.GET_STATE) {
                            DateIsolationMurEnum type = null;
                            if (walls_properties.containsKey(wall))
                                if (walls_properties.get(wall).containsKey(DpeEvent.DATE_ISOLATION_MUR))
                                    type = (DateIsolationMurEnum) walls_properties.get(wall).get(DpeEvent.DATE_ISOLATION_MUR);
                            Object o2[] = {wall, EventRequest.CURRENT_STATE, type};
                            Event e2 = new Event(DpeEvent.DATE_ISOLATION_MUR, o2);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_ISOLATION_MUR: {
                        Object[] items = (Object[]) o;
                        IfcWallStandardCase wall = (IfcWallStandardCase)items[0];
                        if (items[1] == EventRequest.UPDATE_STATE) {
                            TypeIsolationMurEnum typeIsolationMur = (TypeIsolationMurEnum) items[2];
                            ifcHelper.addPropertyTypeIsolationWall(wall, typeIsolationMur);
                            if (!walls_properties.containsKey(wall))
                                walls_properties.put(wall, new HashMap<EventType, Object>());
                            walls_properties.get(wall).put(event, typeIsolationMur);
                            tryActualiseWallDP(wall);
                        } else if (items[1] == EventRequest.GET_STATE) {
                            TypeIsolationMurEnum type = null;
                            if (walls_properties.containsKey(wall))
                                if (walls_properties.get(wall).containsKey(DpeEvent.TYPE_ISOLATION_MUR))
                                    type = (TypeIsolationMurEnum) walls_properties.get(wall).get(DpeEvent.TYPE_ISOLATION_MUR);
                            Object o2[] = {wall, EventRequest.CURRENT_STATE, type};
                            Event e2 = new Event(DpeEvent.TYPE_ISOLATION_MUR, o2);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }
                    case TYPE_FENETRE_RESPONSE: {
                        Object[] items = (Object[]) o;
                        IfcWindow window = (IfcWindow)items[0];
                        TypeFenetreEnum typeFenetre = (TypeFenetreEnum)items[1];
                        ifcHelper.addPropertyTypeWindow(window, typeFenetre);
                        windows_properties.get(items[0]).put(event, typeFenetre);
                        break;
                    }

                    case TYPE_MATERIAU_MENUISERIE_RESPONSE: {
                        Object[] items = (Object[]) o;
                        if (items[0] instanceof IfcWindow){
                            IfcWindow window = (IfcWindow)items[0];
                            TypeMateriauMenuiserieEnum typeMenuiserieFenetre = (TypeMateriauMenuiserieEnum)items[1];
                            ifcHelper.addPropertyTypeMenuiserie(window, typeMenuiserieFenetre);
                            windows_properties.get(items[0]).put(event, typeMenuiserieFenetre);
                        }
                        if (items[0] instanceof IfcDoor){
                            IfcDoor door = (IfcDoor)items[0];
                            TypeMateriauMenuiserieEnum typeMenuiserieDoor = (TypeMateriauMenuiserieEnum)items[1];
                            ifcHelper.addPropertyTypeMenuiserie(door, typeMenuiserieDoor);
                            doors_properties.get(items[0]).put(event, typeMenuiserieDoor);
                            eventType = DpeEvent.TYPE_VITRAGE_MENUISERIE;
                            Event event2 = new Event(eventType, door);
                            EventManager.getInstance().put(Channel.DPE, event2);
                        }
                        break;
                    }

                    case TYPE_VITRAGE_MENUISERIE_RESPONSE: {
                        Object[] items = (Object[]) o;
                        if (items[0] instanceof IfcWindow){
                            IfcWindow window = (IfcWindow)items[0];
                            TypeVitrageEnum typeVitrage = (TypeVitrageEnum)items[1];
                            ifcHelper.addPropertyTypeVitrageMenuiserie(window, typeVitrage);
                            windows_properties.get(items[0]).put(event, typeVitrage);
                            tryActualiseWindowDP(window);
                        }
                        if (items[0] instanceof IfcDoor){
                            IfcDoor door = (IfcDoor)items[0];
                            TypeVitrageEnum typeVitrage = (TypeVitrageEnum)items[1];
                            ifcHelper.addPropertyTypeVitrageMenuiserie(door, typeVitrage);
                            doors_properties.get(items[0]).put(event, typeVitrage);
                            tryActualiseDoorDP(door);
                        }
                        break;
                    }

                    case TYPE_DOOR_RESPONSE:{
                        Object[] items = (Object[]) o;
                        IfcDoor door = (IfcDoor)items[0];
                        TypeDoorEnum typeDoor = (TypeDoorEnum)items[1];
                        ifcHelper.addPropertyTypeDoor(door, typeDoor);
                        doors_properties.get(items[0]).put(event, typeDoor);
                        if (typeDoor.equals(TypeDoorEnum.PORTE_FENETRE_BATTANTE) || typeDoor.equals(TypeDoorEnum.PORTE_FENETRE_COULISSANTE)){
                            eventType = DpeEvent.TYPE_MATERIAU_MENUISERIE;
                            Event event2 = new Event(eventType, door);
                            EventManager.getInstance().put(Channel.DPE, event2);
                        }else{
                            tryActualiseDoorDP(door);
                        }
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
                        if (o instanceof IfcWindow) {
                            IfcWindow window = (IfcWindow) o;
                            demanderWindow(window);
                        }
                        if (o instanceof IfcDoor) {
                            IfcDoor door = (IfcDoor) o;
                            demanderPorte(door);
                        }
                        break;
                    }
                    case DPE_STATE_NO_MORE_UNKNOWN:{
                        //logValeursDpe();
                    }
                    case DPE_STATE_NO_MORE_WALL_UNKNOWN:{
                        notifierFenetres();
                        notifierPortes();
                        ifcHelper.saveIfcModel();
                    }
                }
            }
        }
    }
}
