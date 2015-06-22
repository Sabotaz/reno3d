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
import fr.limsi.rorqual.core.event.DpeEvent;
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

    public double getUmurInconnu(){
        double u=-1;
        if (anneeConstruction<1975){
            u = 2.5;
        }
        else if (anneeConstruction>=1975 && anneeConstruction<=1977){
            u = 1;
        }
        else if (anneeConstruction>=1978 && anneeConstruction<=1982){
            if(typeEnergieConstruction.equals(typeEnergieConstructionEnum.ELECTRIQUE)){
                u=0.8;
            }
            else{
                u=1;
            }
        }
        else if (anneeConstruction>=1983 && anneeConstruction<=1988){
            if(typeEnergieConstruction.equals(typeEnergieConstructionEnum.ELECTRIQUE)){
                u=0.7;
            }
            else{
                u=0.8;
            }
        }
        else if (anneeConstruction>=1989 && anneeConstruction<=2000){
            if(typeEnergieConstruction.equals(typeEnergieConstructionEnum.ELECTRIQUE)){
                u=0.45;
            }
            else{
                u=0.5;
            }
        }
        else if (anneeConstruction>=2001 && anneeConstruction<=2005){
            u = 0.4;
        }
        else if (anneeConstruction>=2006 && anneeConstruction<=2012){
            u = 0.35;
        }
        else if (anneeConstruction>2012){
            u = 0.2;
        }
        return u;
    }

    public double getUmur(boolean isAnneeIsolationConnue,double anneeIsolation){
        double u=-1;
        if (isAnneeIsolationConnue && anneeIsolation!=anneeConstruction){
            if (anneeIsolation<1983){
                u = 0.82;
            }
            else if (anneeIsolation>=1983 && anneeIsolation<=1988){
                u = 0.75;
            }
            else if (anneeIsolation>=1989 && anneeIsolation<=2000){
                u = 0.48;
            }
            else if (anneeIsolation>=2001 && anneeIsolation<=2005){
                u = 0.42;
            }
            else if (anneeIsolation>=2006 && anneeIsolation<=2012){
                u = 0.36;
            }
            else if (anneeIsolation>2012){
                u = 0.24;
            }
        }
        else if (isAnneeIsolationConnue && anneeIsolation==anneeConstruction){
            if (anneeConstruction<1974){
                u=0.8;
            }else{
                u=this.getUmurInconnu();
            }
        }
        else if(!isAnneeIsolationConnue){
            u=this.getUmurInconnu();
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

    public void actualiseDP_mur(IfcWallStandardCase wall, String derriere,String isole,boolean isAnneeIsolationConnue,double anneeIsolation){
        double surface=IfcHelper.getWallSurface(wall);
        double u=0,b=0;
        switch (isole){
            case "oui":
                u=getUmur(isAnneeIsolationConnue,anneeIsolation);
                break;
            case "non":
                u=2.5;
                break;
            case "inconnue":
                u=getUmurInconnu();
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
        log();
    }

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

    public void transitionMur(IfcWallStandardCase wall){
        if (isLastWall(wall)){
//            takeFirstSlab();
//            MainApplicationAdapter.select(_slab);
//            demandeSousPlancher();
        }
        else{
            wall = getNextWall(wall);
            MainApplicationAdapter.select(wall);
            demandeDerriereMur(wall);
        }
    }

    /*---------------------------------------IHM------------------------------------------------*/

    /*** Explique ce qu'est le DPE et demande de continuer ou non ***/
    public void startDPE() {
        notifierMurs();
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

    /*** Notifie le statut des murs ***/
    public void notifierMurs() {
        IfcWallStandardCase wall;
        Iterator<IfcWallStandardCase> it = wallStandardCaseCollection.iterator();
        while (it.hasNext()) {
            wall = it.next();
            Object o[] = {wall, DpeState.UNKNOWN};
            Event e = new Event(DpeEvent.DPE_STATE_CHANGED, o);
            EventManager.getInstance().put(Channel.DPE, e);
        }
    }

    public void demanderMur(IfcWallStandardCase wall) {
        walls_properties.put(wall, new HashMap<EventType, Object>());
        DpeEvent eventType = DpeEvent.DERRIERE_MUR;
        Event event = new Event(eventType, wall);
        EventManager.getInstance().put(Channel.DPE, event);

        eventType = DpeEvent.ISOLATION_MUR;
        event = new Event(eventType, wall);
        EventManager.getInstance().put(Channel.DPE, event);
    }

    /*** Demande ce qui se trouve derrière un mur ***/
    public void demandeDerriereMur(IfcWallStandardCase wall) {
        if (IfcHelper.getPropertyTypeWall(wall)=="ext"){
            DpeEvent eventType = DpeEvent.DERRIERE_MUR;
            Event event = new Event(eventType,wall);
            EventManager.getInstance().put(Channel.DPE, event);
        }else{
            transitionMur(wall);
        }
    }

    /*** Demande isolation mur ***/
    public void demandeIsolationMur(Object[] tab) {
        DpeEvent eventType = DpeEvent.ISOLATION_MUR;
        Event event = new Event(eventType,tab);
        EventManager.getInstance().put(Channel.DPE, event);
    }

    public void demandeDateIsolation(Object[] tab) {
        DpeEvent eventType = DpeEvent.DATE_ISOLATION_MUR;
        Event event = new Event(eventType,tab);
        EventManager.getInstance().put(Channel.DPE, event);
    }

    public void log(){
        System.out.println("************************************************************************************");
        System.out.println("Type d'energie a la construction : " + typeEnergieConstruction);
        System.out.println("Type de batiment : " + typeBatiment);
        System.out.println("Annee de construction : " + anneeConstruction);
        if(typeBatiment==typeBatimentEnum.APPARTEMENT){
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
        System.out.println("************************************************************************************");
    }

    HashMap<IfcWallStandardCase, HashMap<EventType, Object>> walls_properties = new HashMap<IfcWallStandardCase, HashMap<EventType, Object>>();

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
                        this.calc_MIT2();
                        stage.getActors().pop();
                        this.demandeAnneeConstruction();
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
                            typeEnergieConstruction=typeEnergieConstructionEnum.ELECTRIQUE;
                        }else{
                            typeEnergieConstruction=typeEnergieConstructionEnum.AUTRE;
                        }
                        if (!wallStandardCaseCollection.isEmpty()) {
                            //IfcWallStandardCase wall = getFirstWall();
                            //this.demandeDerriereMur(wall);
                        }else{
                            // TODO il n'y a pas de murs dans le model : on fait quoi ???
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

                    case DPE_REQUEST: {
                        System.out.println(o.getClass());
                        if (o instanceof IfcWallStandardCase) {
                            IfcWallStandardCase wall = (IfcWallStandardCase) o;
                            demanderMur(wall);
                        }
                        break;
                    }

                }
            }
        }
    }
}
