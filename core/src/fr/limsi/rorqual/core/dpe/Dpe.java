package fr.limsi.rorqual.core.dpe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventListener;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.event.EventType;
import fr.limsi.rorqual.core.model.IfcHelper;
import fr.limsi.rorqual.core.model.IfcHolder;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;
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
    private Collection<IfcWallStandardCase> wallStandardCaseCollection;
    private Collection<IfcSlab> slabCollection;
    private Collection<IfcWindow> windowCollection;

    /*** Attributs liés à l'interface graphique de libGDX ***/
    private Skin skin;
    private BitmapFont fontBlack, fontWhite;
    private Stage stage;
    private TextButton.TextButtonStyle textButtonStyle;

    /*** Attributs liés au calcul du DPE ***/

    // 0.Variables générales
    private enum typeBatimentEnum{
        MAISON,
        APPARTEMENT;
    }
    private enum positionAppartementEnum{
        PREMIER_ETAGE,
        ETAGE_INTERMEDIAIRE,
        DERNIER_ETAGE;
    }
    private enum typeEnergieConstructionEnum{
        ELECTRIQUE,
        AUTRE;
    }
    private typeBatimentEnum typeBatiment;
    private positionAppartementEnum positionAppartement;
    private typeEnergieConstructionEnum typeEnergieConstruction;
    private double anneeConstruction;
    private double SH;
    private double NIV;
    private double MIT;
    private double MIT2;
    private double FOR;
    private double Per;
    private double PER;
    private Semaphore semaphore;

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
        SH = IfcHelper.calculSurfaceHabitable(ifcModel);
        EventManager.getInstance().addListener(Channel.DPE, this);
    }

    /*---------------------------------Calculateur DPE-------------------------------------------*/



    /*-------------------------------Reader/Writter .IFC-----------------------------------------*/

    /*** On mémorise le premier mur contenu dans le model ***/
    public IfcWallStandardCase getFirstWall(){
        Iterator<IfcWallStandardCase> it = wallStandardCaseCollection.iterator();
        IfcWallStandardCase wall=it.next();
        MainApplicationAdapter.select(wall);
        return (wall);
    }

    /*** On vérifie si on est au dernier mur du model ***/
    public boolean isLastWall(IfcWallStandardCase wall){
        int compteur=0;
        int nbMurs=wallStandardCaseCollection.size();
        boolean isNotEqual=true;
        Iterator<IfcWallStandardCase> it = wallStandardCaseCollection.iterator();
        while(isNotEqual){
            compteur += 1;
            if(it.next().equals(wall)){
                isNotEqual=false;
            }
        }
        if (compteur<nbMurs && !isNotEqual){
            return false;
        }
        else{
            return true;
        }
    }

    /*** On prend le mur suivant du model ***/
    public IfcWallStandardCase getNextWall(IfcWallStandardCase actualWall){
        IfcWallStandardCase nextWall=null;
        Iterator<IfcWallStandardCase> it = wallStandardCaseCollection.iterator();
        for(int i=0;i<wallStandardCaseCollection.size()-1;i++){
            if(it.next().equals(actualWall)){
                nextWall=it.next();
                MainApplicationAdapter.select(nextWall);
            }
        }
        return (nextWall);
    }

    /*---------------------------------Blocs logiques--------------------------------------------*/


    /*---------------------------------------IHM------------------------------------------------*/

    /*** Explique ce qu'est le DPE et demande de continuer ou non ***/
    public void startDPE() {
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
    public void demandeAnneeConstruction(String initialText) {
        DpeEvent eventType = DpeEvent.ANNEE_CONSTRUCTION;
        Event event = new Event(eventType,initialText);
        EventManager.getInstance().put(Channel.DPE, event);
    }

    /*** Demande le type d'énergie à la construction ***/
    public void demandeTypeEnergieConstruction() {
        DpeEvent eventType = DpeEvent.ENERGIE_CONSTRUCTION;
        Event event = new Event(eventType);
        EventManager.getInstance().put(Channel.DPE, event);
    }

    /*** Demande ce qui se trouve derrière un mur ***/
    public void demandeDerriereMur(IfcWallStandardCase wall) {
        DpeEvent eventType = DpeEvent.DERRIERE_MUR;
        Event event = new Event(eventType,wall);
        EventManager.getInstance().put(Channel.DPE, event);
    }

    public void log(){
        System.out.println("************************************************************************************");
        System.out.println("Type d'energie a la construction : " + typeEnergieConstruction);
        System.out.println("Type de batiment : " + typeBatiment);
        System.out.println("Annee de construction : " + anneeConstruction);
        System.out.println("NIV : " + NIV);
        System.out.println("MIT : " + MIT);
        System.out.println("FOR : " + FOR);
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
                            typeBatiment = typeBatimentEnum.MAISON;
                            this.demandeNbNiveau();
                        } else {
                            typeBatiment = typeBatimentEnum.APPARTEMENT;
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
                        stage.getActors().pop();
                        this.demandeAnneeConstruction("ex: 1998");
                        break;
                    }

                    case POSITION_APPARTEMENT_RESPONSE:{
                        String reponse = (String) o;
                        if (reponse.equals("bas")){
                            positionAppartement=positionAppartementEnum.PREMIER_ETAGE;
                        } else if (reponse.equals("int")){
                            positionAppartement=positionAppartementEnum.ETAGE_INTERMEDIAIRE;
                        } else if (reponse.equals("haut")){
                            positionAppartement=positionAppartementEnum.DERNIER_ETAGE;
                        }
                        stage.getActors().pop();
                        this.demandeAnneeConstruction("ex: 1998");
                        break;
                    }

                    case ANNEE_CONSTRUCTION_RESPONSE: {
                        String reponse = (String) o;
                        try{
                            double annee = Double.parseDouble(reponse);
                            if (annee < 1700){
                                this.demandeAnneeConstruction("Annee saisie < 1700");
                            }
                            else if (annee > 2015){
                                this.demandeAnneeConstruction("Annee saisie > 2015");
                            }
                            else {
                                anneeConstruction = annee;
                                this.demandeTypeEnergieConstruction();
                            }
                        }
                        catch (NumberFormatException ev){
                            this.demandeAnneeConstruction("Saisie invalide");
                        }
                        break;
                    }

                    case ENERGIE_CONSTRUCTION_RESPONSE: {
                        String reponse = (String) o;
                        if (reponse.equals("elec")){
                            typeEnergieConstruction=typeEnergieConstructionEnum.ELECTRIQUE;
                        }else{
                            typeEnergieConstruction=typeEnergieConstructionEnum.AUTRE;
                        }
                        if (!wallStandardCaseCollection.isEmpty()) {
                            IfcWallStandardCase wall = getFirstWall();
                            demandeDerriereMur(wall);
                        }else{
                            // TODO il n'y a pas de murs dans le model, on fait quoi ???
                        }
                        break;
                    }

                    case DERRIERE_MUR_RESPONSE:{
                        Object items[] = (Object[]) o;
                        IfcWallStandardCase wall = (IfcWallStandardCase)items[0];
                        String derriere = (String)items[1];
                        if (!isLastWall(wall)){
                            wall=getNextWall(wall);
                            demandeDerriereMur(wall);
                        }
                    }
                }
            }
        }
    }
}
