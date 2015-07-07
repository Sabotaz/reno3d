package fr.limsi.rorqual.core.dpe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.lang.reflect.Type;
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
    private IfcHelper ifcHelper;
    private HashMap<IfcWallStandardCase, HashMap<EventType, Object>> walls_properties = new HashMap<>();
    private HashMap<IfcSlab, HashMap<EventType, Object>> slabs_properties = new HashMap<>();
    private HashMap<IfcWindow, HashMap<EventType, Object>> windows_properties = new HashMap<>();
    private HashMap<IfcDoor, HashMap<EventType, Object>> doors_properties = new HashMap<>();
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
        if (!walls_properties.get(wall).containsKey(DpeEvent.TYPE_MUR_RESPONSE))
            return;
        TypeMurEnum typeMur = (TypeMurEnum) walls_properties.get(wall).get(DpeEvent.TYPE_MUR_RESPONSE);

        if (!walls_properties.get(wall).containsKey(DpeEvent.DATE_ISOLATION_MUR_RESPONSE))
            return;
        DateIsolationMurEnum dateIsolationMur = (DateIsolationMurEnum) walls_properties.get(wall).get(DpeEvent.DATE_ISOLATION_MUR_RESPONSE);

        TypeIsolationMurEnum typeIsolationMur = TypeIsolationMurEnum.UNKNOWN;
        if (walls_properties.get(wall).containsKey(DpeEvent.TYPE_ISOLATION_MUR_RESPONSE)){
            typeIsolationMur = (TypeIsolationMurEnum) walls_properties.get(wall).get(DpeEvent.TYPE_ISOLATION_MUR_RESPONSE);
        }
        actualiseDP_wall(wall, typeMur, dateIsolationMur, typeIsolationMur);
    }

    public void tryActualiseWindowDP(IfcWindow window) {
        if (!windows_properties.get(window).containsKey(DpeEvent.TYPE_FENETRE_RESPONSE))
            return;
        TypeFenetreEnum typeFenetre = (TypeFenetreEnum) windows_properties.get(window).get(DpeEvent.TYPE_FENETRE_RESPONSE);

        if (!windows_properties.get(window).containsKey(DpeEvent.TYPE_MENUISERIE_FENETRE_RESPONSE))
            return;
        TypeMenuiserieFenetreEnum typeMenuiserieFenetre = (TypeMenuiserieFenetreEnum) windows_properties.get(window).get(DpeEvent.TYPE_MENUISERIE_FENETRE_RESPONSE);

        if (!windows_properties.get(window).containsKey(DpeEvent.TYPE_VITRAGE_FENETRE_RESPONSE))
            return;
        TypeVitrageEnum typeVitrageFenetre = (TypeVitrageEnum) windows_properties.get(window).get(DpeEvent.TYPE_VITRAGE_FENETRE_RESPONSE);

        actualiseDP_window(window, typeFenetre, typeMenuiserieFenetre, typeVitrageFenetre);
    }

    public void actualiseDP_wall(IfcWallStandardCase wall, TypeMurEnum typeMur, DateIsolationMurEnum dateIsolationMur, TypeIsolationMurEnum typeIsolationMur){
        double uMur=0,sMur=ifcHelper.getWallSurface(wall);
        if (dateIsolationMur.equals(DateIsolationMurEnum.JAMAIS)){
            uMur=2.5;
        }
        else if (dateIsolationMur.equals(DateIsolationMurEnum.UNKNOWN) || dateIsolationMur.equals(DateIsolationMurEnum.A_LA_CONSTRUCTION) || dateIsolationMur.equals(DateIsolationMurEnum.EN_RENOVATION_DATE_INCONNUE)){
            if (anneeConstruction<1975){
                if (dateIsolationMur.equals(DateIsolationMurEnum.A_LA_CONSTRUCTION) || dateIsolationMur.equals(DateIsolationMurEnum.EN_RENOVATION_DATE_INCONNUE)){
                    uMur=0.8;
                }else{
                    uMur=2.5;
                }
                if (typeIsolationMur.equals(TypeIsolationMurEnum.UNKNOWN)){
                    ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.NON_ISOLE);
                }
            }
            if (anneeConstruction>=1975 && anneeConstruction<=1977){
                uMur=1;
                if (typeIsolationMur.equals(TypeIsolationMurEnum.UNKNOWN)){
                    ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
                }
            }
            if (anneeConstruction>=1978 && anneeConstruction<=1982){
                if (typeEnergieConstruction.equals(typeEnergieConstruction.ELECTRIQUE)){
                    uMur=0.8;
                }else{
                    uMur=1;
                }
                if (typeIsolationMur.equals(TypeIsolationMurEnum.UNKNOWN)){
                    ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
                }
            }
            if (anneeConstruction>=1983 && anneeConstruction<=1988){
                if (typeEnergieConstruction.equals(typeEnergieConstruction.ELECTRIQUE)){
                    uMur=0.7;
                }else{
                    uMur=0.8;
                }
                if (typeIsolationMur.equals(TypeIsolationMurEnum.UNKNOWN)){
                    ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
                }
            }
            if (anneeConstruction>=1989 && anneeConstruction<=2000){
                if (typeEnergieConstruction.equals(typeEnergieConstruction.ELECTRIQUE)){
                    uMur=0.45;
                }else{
                    uMur=0.5;
                }
                if (typeIsolationMur.equals(TypeIsolationMurEnum.UNKNOWN)){
                    ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
                }
            }
            if (anneeConstruction>=2001 && anneeConstruction<=2005){
                uMur=0.4;
                if (typeIsolationMur.equals(TypeIsolationMurEnum.UNKNOWN)){
                    ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
                }
            }
            if (anneeConstruction>=2006 && anneeConstruction<=2012){
                uMur=0.35;
                if (typeIsolationMur.equals(TypeIsolationMurEnum.UNKNOWN)){
                    ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITE);
                }
            }
            if (anneeConstruction>2012){
                uMur=0.2;
                if (typeIsolationMur.equals(TypeIsolationMurEnum.UNKNOWN)){
                    ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITE);
                }
            }
        }
        else if (dateIsolationMur.equals(DateIsolationMurEnum.EN_RENOVATION_AVANT_1983)){
            uMur=0.82;
            if (typeIsolationMur.equals(TypeIsolationMurEnum.UNKNOWN)){
                ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
            }
        }
        else if (dateIsolationMur.equals(DateIsolationMurEnum.EN_RENOVATION_ENTRE_1983_ET_1988)){
            uMur=0.75;
            if (typeIsolationMur.equals(TypeIsolationMurEnum.UNKNOWN)){
                ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
            }
        }
        else if (dateIsolationMur.equals(DateIsolationMurEnum.EN_RENOVATION_ENTRE_1989_ET_2000)){
            uMur=0.48;
            if (typeIsolationMur.equals(TypeIsolationMurEnum.UNKNOWN)){
                ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
            }
        }
        else if (dateIsolationMur.equals(DateIsolationMurEnum.EN_RENOVATION_ENTRE_2001_ET_2005)){
            uMur=0.42;
            if (typeIsolationMur.equals(TypeIsolationMurEnum.UNKNOWN)){
                ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
            }
        }
        else if (dateIsolationMur.equals(DateIsolationMurEnum.EN_RENOVATION_ENTRE_2006_ET_2012)){
            uMur=0.36;
            if (typeIsolationMur.equals(TypeIsolationMurEnum.UNKNOWN)){
                ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
            }
        }
        else if (dateIsolationMur.equals(DateIsolationMurEnum.EN_RENOVATION_APRES_2012)){
            uMur=0.24;
            if (typeIsolationMur.equals(TypeIsolationMurEnum.UNKNOWN)){
                ifcHelper.addPropertyTypeIsolationWall(wall,TypeIsolationMurEnum.ITI);
            }
        }

        if (typeMur.equals(TypeMurEnum.MUR_DONNANT_SUR_EXTERIEUR)){
            DP_murExt += sMur*uMur;
            System.out.println("Actualise DP Mur ext -> U="+uMur+"  S="+sMur+"  DPmurExt="+DP_murExt);
        }
        if (typeMur.equals(TypeMurEnum.MUR_DONNANT_SUR_UNE_AUTRE_HABITATION)){
            DP_murAh += sMur*uMur*0.2;
            System.out.println("Actualise DP Mur ah -> U="+uMur+"  S="+sMur+" b=0.2"+"  DPmurAh="+DP_murAh);
        }
        if (typeMur.equals(TypeMurEnum.MUR_DONNANT_SUR_UN_LOCAL_NON_CHAUFFE)){
            double b_lnc=0;
            if (dateIsolationMur.equals(DateIsolationMurEnum.UNKNOWN) || dateIsolationMur.equals(DateIsolationMurEnum.JAMAIS)){
                b_lnc=0.95;
            }else{
                b_lnc=0.85;
            }
            DP_murLnc += sMur*uMur*b_lnc;
            System.out.println("Actualise DP Mur lnc -> U="+uMur+"  S="+sMur+" b="+b_lnc+"  DPmurLnc="+DP_murLnc);
        }
        if (typeMur.equals(TypeMurEnum.MUR_DONNANT_SUR_UNE_VERANDA)){
            double b_ver=0.6;
            // TODO -> quand on connaitra l'orientation du mur, on pourra affiner le calcul de b_ver
            DP_murVer += sMur*uMur*b_ver;
            System.out.println("Actualise DP Mur ver -> U="+uMur+"  S="+sMur+" b="+b_ver+"  DPmurVer="+DP_murVer);
        }
    }

    public void actualiseDP_window(IfcWindow window,TypeFenetreEnum typeFenetre,TypeMenuiserieFenetreEnum typeMenuiserie,TypeVitrageEnum typeVitrage){
        double uFen=0,sFen=ifcHelper.getWindowSurface(window);
        if (typeMenuiserie.equals(TypeMenuiserieFenetreEnum.METALLIQUE)){
            if (typeFenetre.equals(TypeFenetreEnum.BATTANTE)){
                if (typeVitrage.equals(TypeVitrageEnum.SIMPLE_VITRAGE)){
                    uFen=4.95;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.SURVITRAGE)){
                    uFen=4;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_INF_1990)){
                    uFen=3.7;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_1990_INF_2001)){
                    uFen=3.6;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_2001)){
                    uFen=2.25;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.TRIPLE_VITRAGE)){
                    uFen=1.88;
                }
            }

            if (typeFenetre.equals(TypeFenetreEnum.COULISSANTE)){
                if (typeVitrage.equals(TypeVitrageEnum.SIMPLE_VITRAGE)){
                    uFen=4.63;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.SURVITRAGE)){
                    uFen=3.46;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_INF_1990)){
                    uFen=3.46;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_1990_INF_2001)){
                    uFen=3.36;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.DOUBLE_VITRAGE_SUP_2001)){
                    uFen=2.18;
                }
                else if (typeVitrage.equals(TypeVitrageEnum.TRIPLE_VITRAGE)){
                    uFen=1.65;
                }
            }
        }
        else if (typeMenuiserie.equals(TypeMenuiserieFenetreEnum.PVC)){
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
        else if (typeMenuiserie.equals(TypeMenuiserieFenetreEnum.BOIS)){
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
        DP_fen += sFen*uFen;
        System.out.println("Actualise DP Fen -> U="+uFen+"  S="+sFen+"  DPfen="+DP_fen);
    }

    public void actualiseDP_door(IfcDoor door, TypeDoorEnum typeDoor){
        double sDoor=ifcHelper.getDoorSurface(door),uDoor=0;
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
        DP_portExt += sDoor*uDoor;
        System.out.println("Actualise DP Door -> U="+uDoor+"  S="+sDoor+"  DPdoor="+DP_portExt);
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
        notifierPortes();
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
        slabs_properties.put(slab, new HashMap<EventType, Object>());
        DpeEvent eventType = DpeEvent.DERRIERE_SLAB;
        Event event = new Event(eventType, slab);
        EventManager.getInstance().put(Channel.DPE, event);

        eventType = DpeEvent.ISOLATION_SLAB;
        event = new Event(eventType, slab);
        EventManager.getInstance().put(Channel.DPE, event);
    }

    /*** Demande à l'utilisateur des informations permettant de déterminer les déperditions au niveau des windows ***/
    public void demanderWindow(IfcWindow window){
        windows_properties.put(window, new HashMap<EventType, Object>());
        DpeEvent eventType = DpeEvent.TYPE_FENETRE;
        Event event = new Event(eventType, window);
        EventManager.getInstance().put(Channel.DPE, event);

        eventType = DpeEvent.TYPE_MENUISERIE_FENETRE;
        event = new Event(eventType, window);
        EventManager.getInstance().put(Channel.DPE, event);

        eventType = DpeEvent.TYPE_VITRAGE_FENETRE;
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

                    case TYPE_MUR_RESPONSE: {
                        Object[] items = (Object[]) o;
                        IfcWallStandardCase wall = (IfcWallStandardCase)items[0];
                        TypeMurEnum typeMur = (TypeMurEnum)items[1];
                        ifcHelper.addPropertyTypeWall(wall, typeMur);
                        walls_properties.get(items[0]).put(event, typeMur);
                        if (!typeMur.equals(TypeMurEnum.MUR_INTERIEUR)){
                            eventType = DpeEvent.DATE_ISOLATION_MUR;
                            Event event2 = new Event(eventType, wall);
                            EventManager.getInstance().put(Channel.DPE, event2);
                        }
                        break;
                    }

                    case DATE_ISOLATION_MUR_RESPONSE: {
                        Object[] items = (Object[]) o;
                        IfcWallStandardCase wall = (IfcWallStandardCase)items[0];
                        DateIsolationMurEnum dateIsolationMur = (DateIsolationMurEnum)items[1];
                        ifcHelper.addPropertyDateIsolationWall(wall, dateIsolationMur);
                        walls_properties.get(items[0]).put(event, dateIsolationMur);
                        if (!dateIsolationMur.equals(DateIsolationMurEnum.JAMAIS) && !dateIsolationMur.equals(DateIsolationMurEnum.UNKNOWN)){
                            eventType = DpeEvent.TYPE_ISOLATION_MUR;
                            Event event2 = new Event(eventType, wall);
                            EventManager.getInstance().put(Channel.DPE, event2);
                        } else {
                            tryActualiseWallDP(wall);
                        }
                        break;
                    }

                    case TYPE_ISOLATION_MUR_RESPONSE: {
                        Object[] items = (Object[]) o;
                        IfcWallStandardCase wall = (IfcWallStandardCase)items[0];
                        TypeIsolationMurEnum typeIsolationMur = (TypeIsolationMurEnum)items[1];
                        ifcHelper.addPropertyTypeIsolationWall(wall, typeIsolationMur);
                        walls_properties.get(items[0]).put(event, typeIsolationMur);
                        tryActualiseWallDP(wall);
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

                    case TYPE_MENUISERIE_FENETRE_RESPONSE: {
                        Object[] items = (Object[]) o;
                        IfcWindow window = (IfcWindow)items[0];
                        TypeMenuiserieFenetreEnum typeMenuiserieFenetre = (TypeMenuiserieFenetreEnum)items[1];
                        ifcHelper.addPropertyTypeMenuiserieWindow(window, typeMenuiserieFenetre);
                        windows_properties.get(items[0]).put(event, typeMenuiserieFenetre);
                        break;
                    }

                    case TYPE_VITRAGE_FENETRE_RESPONSE: {
                        Object[] items = (Object[]) o;
                        IfcWindow window = (IfcWindow)items[0];
                        TypeVitrageEnum typeVitrage = (TypeVitrageEnum)items[1];
                        ifcHelper.addPropertyTypeVitrageWindow(window, typeVitrage);
                        windows_properties.get(items[0]).put(event, typeVitrage);
                        tryActualiseWindowDP(window);
                        break;
                    }

                    case TYPE_DOOR_RESPONSE:{
                        Object[] items = (Object[]) o;
                        IfcDoor door = (IfcDoor)items[0];
                        TypeDoorEnum typedoor = (TypeDoorEnum)items[1];
                        ifcHelper.addPropertyTypeDoor(door, typedoor);
                        doors_properties.get(items[0]).put(event, typedoor);
                        actualiseDP_door(door,typedoor);
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
                        System.out.println("######################### COUCOU !!! #########################");
                        //calc_PT();
                        log();
                    }
                }
            }
        }
    }
}
