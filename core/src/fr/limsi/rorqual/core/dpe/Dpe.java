package fr.limsi.rorqual.core.dpe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;

import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.event.EventType;
import fr.limsi.rorqual.core.model.IfcHelper;
import fr.limsi.rorqual.core.model.IfcHolder;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSlab;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWindow;
import ifc2x3javatoolbox.ifc2x3tc1.LIST;
import ifc2x3javatoolbox.ifcmodel.IfcModel;

/**
 * Created by ricordeau on 20/05/15.
 */
public class Dpe implements EventListener {

    /*** Attributs liés au model IFC***/
    private IfcModel _ifcModel;
    private Collection<IfcWallStandardCase> _wallStandardCaseCollection;
    private Collection<IfcSlab> _slabCollection;
    private Collection<IfcWindow> _windowCollection;

    /*** Attributs liés à l'interface graphique de libGDX ***/
    private Skin _skin;
    private BitmapFont _fontBlack, _fontWhite;
    private Stage _stage;
    private TextButton.TextButtonStyle _textButtonStyle;

    /*** Attention horreur : variables tampons ***/
    private IfcWallStandardCase _wall;
    private IfcSlab _slab;
    private IfcWindow _window;
    private String _derriere;
    private String _typeMenuiserie;
    private String _materiauMenuiserie;
    private String _typeVitrage;
    private boolean _isole;
    private double _anneeIsolation;
    private double _S;
    private double _U;
    private double _b;

    /*** Attributs liés au calcul du DPE ***/

    // 0.Variables générales
    private String _typeBatiment; // "Maison" ou "Appartement"
    private String _positionAppartement; // "PremierEtage" ou "EtageIntermediaire" ou "DernierEtage"
    private String _typeEnergieConstruction; // "Electrique" ou "Autre"
    private boolean _presenceLNC;
    private double _anneeConstruction;
    private double _SH;
    private double _NIV;
    private double _MIT;
    private double _MIT2;
    private double _FOR;
    private double _Per;
    private double _PER;

    // 1.Expression du besoin de chauffage
    private double _BV;
    private double _GV;
    private double _F;

    // 2.Calcul des déperditions de l'enveloppe GV
    private double _DP_murExt;
    private double _DP_murLnc;
    private double _DP_murAh;
    private double _DP_murVer;
    private double _DP_toiTer;
    private double _DP_toiCp;
    private double _DP_toiCa;
    private double _DP_planVs;
    private double _DP_planTp;
    private double _DP_planSs;
    private double _DP_planAh;
    private double _DP_fen;
    private double _DP_pfen;
    private double _DP_fenVer;
    private double _DP_pfenVer;
    private double _DP_portExt;
    private double _DP_portLnc;
    private double _DP_portVer;
    private double _PT;
    private double _DR;

    /*** Autres constructeurs ***/
    public Dpe (Stage stage) {
        _fontBlack = new BitmapFont(Gdx.files.internal("data/font/black.fnt"));
        _fontWhite = new BitmapFont(Gdx.files.internal("data/font/white.fnt"));
        _skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));
        _textButtonStyle = new TextButton.TextButtonStyle(_skin.getDrawable("default-round"),_skin.getDrawable("default-round-down"),null,_fontBlack);
        _ifcModel = IfcHolder.getInstance().getIfcModel();
        _stage = stage;
        _wallStandardCaseCollection = _ifcModel.getCollection(IfcWallStandardCase.class);
        _slabCollection = _ifcModel.getCollection(IfcSlab.class);
        _windowCollection = _ifcModel.getCollection(IfcWindow.class);
        _SH = IfcHelper.calculSurfaceHabitable(_ifcModel);
    }

    /*----------------------------------Getter & Setter-----------------------------------------*/
    public void set_anneeConstruction(double annee){
        _anneeConstruction=annee;
    }

    /*------------------------------------Calculateur-------------------------------------------*/
    public void calc_BV() {
        _BV = _GV*(1-_F);
    }

    public void calc_GV() {
        _GV = _DP_murExt + _DP_murLnc + _DP_murAh + _DP_murVer + _DP_toiTer + _DP_toiCp + _DP_toiCa + _DP_planVs + _DP_planTp + _DP_planSs + _DP_planAh + _DP_fen + _DP_pfen + _DP_fenVer + _DP_pfenVer + _DP_portExt + _DP_portLnc + _DP_portVer + _PT + _DR;
    }

    public void calc_MIT2(){
        if (_FOR == 4.12){ // Forme carré ou rectangulaire de la maison
            if (_MIT == 1){_MIT2=1;}
            else if (_MIT == 0.8){_MIT2=0.8;}
            else if (_MIT == 0.7){_MIT2=0.675;}
            else if (_MIT == 0.5){_MIT2=0.5;}
            else if (_MIT == 0.35){_MIT2=0.35;}
        }
        else if (_FOR == 4.81){ // Forme allongé de la maison
            if (_MIT == 1){_MIT2=1;}
            else if (_MIT == 0.8){_MIT2=0.9;}
            else if (_MIT == 0.7){_MIT2=0.725;}
            else if (_MIT == 0.5){_MIT2=0.55;}
            else if (_MIT == 0.35){_MIT2=0.4;}
        }
        else if (_FOR == 5.71){ // Forme développé de la maison
            if (_MIT == 1){_MIT2=1;}
            else if (_MIT == 0.8){_MIT2=0.9;}
            else if (_MIT == 0.7){_MIT2=0.75;}
            else if (_MIT == 0.5){_MIT2=0.7;}
            else if (_MIT == 0.35){_MIT2=0.55;}
        }
    }

    public void calc_DP_Mur(){
        _S = IfcHelper.getWallSurface(_wall);
        _b=1;
        switch (_derriere){
            case "ext":
                _DP_murExt+=_S*_U;
                break;
            case "lnc":
                _DP_murLnc+=_S*_U*_b;
                break;
            case "ah":
                _DP_murAh+=_S*_U*0.2;
                break;
            case "ver":
                _DP_murVer+=_S*_U*_b;
                break;
        }
    }

    public void calc_DP_Mur(IfcWallStandardCase wall, String derriere){
        _S = IfcHelper.getWallSurface(wall);
        _b=1;
        switch (derriere){
            case "ext":
                _DP_murExt+=_S*_U;
                break;
            case "lnc":
                _DP_murLnc+=_S*_U*_b;
                break;
            case "ah":
                _DP_murAh+=_S*_U*0.2;
                break;
            case "ver":
                _DP_murVer+=_S*_U*_b;
                break;
        }
    }

    public void calc_DP_Plancher(){
        _S = IfcHelper.getSlabSurface(_slab);
        _b=1;
        switch (_derriere){
            case "vs":
                _DP_planVs+=_S*_U*0.8;
                break;
            case "ss":
                _DP_planSs+=_S*_U*_b;
                break;
            case "tp":
                _DP_planTp+=_S*_U;
                break;
            case "ah":
                _DP_planAh+=_S*_U*_b*0.2;
                break;
        }
    }

    public void calc_DP_Fenetre(){
        _S = IfcHelper.getWindowSurface(_ifcModel,_window);
        _DP_fen += _S*_U;
    }

    public void calcUmurInconnu(){
        if (_anneeConstruction<1975){
            _U = 2.5;
        }
        else if (_anneeConstruction>=1975 && _anneeConstruction<=1977){
            _U = 1;
        }
        else if (_anneeConstruction>=1978 && _anneeConstruction<=1982){
            if(_typeEnergieConstruction.equals("Electrique")){
                _U=0.8;
            }
            else{
                _U=1;
            }
        }
        else if (_anneeConstruction>=1983 && _anneeConstruction<=1988){
            if(_typeEnergieConstruction.equals("Electrique")){
                _U=0.7;
            }
            else{
                _U=0.8;
            }
        }
        else if (_anneeConstruction>=1989 && _anneeConstruction<=2000){
            if(_typeEnergieConstruction.equals("Electrique")){
                _U=0.45;
            }
            else{
                _U=0.5;
            }
        }
        else if (_anneeConstruction>=2001 && _anneeConstruction<=2005){
            _U = 0.4;
        }
        else if (_anneeConstruction>=2006 && _anneeConstruction<=2012){
            _U = 0.35;
        }
        else if (_anneeConstruction>2012){
            _U = 0.2;
        }
    }

    public void calcUplancherInconnu(){
        if (_anneeConstruction<1975){
            _U = 2;
        }
        else if (_anneeConstruction>=1975 && _anneeConstruction<=1977){
            _U = 0.9;
        }
        else if (_anneeConstruction>=1978 && _anneeConstruction<=1982){
            if(_typeEnergieConstruction.equals("Electrique")){
                _U=0.8;
            }
            else{
                _U=0.9;
            }
        }
        else if (_anneeConstruction>=1983 && _anneeConstruction<=1988){
            if(_typeEnergieConstruction.equals("Electrique")){
                _U=0.55;
            }
            else{
                _U=0.7;
            }
        }
        else if (_anneeConstruction>=1989 && _anneeConstruction<=2000){
            if(_typeEnergieConstruction.equals("Electrique")){
                _U=0.55;
            }
            else{
                _U=0.6;
            }
        }
        else if (_anneeConstruction>=2001 && _anneeConstruction<=2005){
            _U = 0.3;
        }
        else if (_anneeConstruction>=2006 && _anneeConstruction<=2012){
            _U = 0.27;
        }
        else if (_anneeConstruction>2012){
            _U = 0.22;
        }
    }

    public void calcUmurRenovation(){
        if (_anneeIsolation<1983){
            _U = 0.82;
        }
        else if (_anneeIsolation>=1983 && _anneeIsolation<=1988){
            _U = 0.75;
        }
        else if (_anneeIsolation>=1989 && _anneeIsolation<=2000){
            _U = 0.48;
        }
        else if (_anneeIsolation>=2001 && _anneeIsolation<=2005){
            _U = 0.42;
        }
        else if (_anneeIsolation>=2006 && _anneeIsolation<=2012){
            _U = 0.36;
        }
        else if (_anneeIsolation>2012){
            _U = 0.24;
        }
    }

    public void calcUplancherRenovation(){
        if (_anneeIsolation<1983){
            _U = 0.85;
        }
        else if (_anneeIsolation>=1983 && _anneeIsolation<=1988){
            _U = 0.6;
        }
        else if (_anneeIsolation>=1989 && _anneeIsolation<=2000){
            _U = 0.55;
        }
        else if (_anneeIsolation>=2001 && _anneeIsolation<=2005){
            _U = 0.3;
        }
        else if (_anneeIsolation>=2006 && _anneeIsolation<=2012){
            _U = 0.27;
        }
        else if (_anneeIsolation>2012){
            _U = 0.24;
        }
    }

    public void calcUplanTp(){
        double surfacePlancher = IfcHelper.getSlabSurface(_slab);
        double variableTest;

        if (_typeBatiment == "Maison"){
            _Per = _FOR*_MIT2*Math.sqrt(_SH/_NIV);
        }
        else{ // Type de batiment = appartement
            _Per = 2*((_SH/_PER)+_PER);
        }

            variableTest=Math.round(2*surfacePlancher/_Per);
            if(variableTest<=3){_U = 0.25;}
            else if(variableTest==4){_U = 0.23;}
            else if(variableTest==5){_U = 0.21;}
            else if(variableTest==6){_U = 0.19;}
            else if(variableTest==7){_U = 0.18;}
            else if(variableTest==8){_U = 0.17;}
            else if(variableTest==9){_U = 0.16;}
            else if(variableTest==10){_U = 0.15;}
            else if(variableTest==11){_U = 0.15;}
            else if(variableTest==12){_U = 0.14;}
            else if(variableTest==13){_U = 0.13;}
            else if(variableTest==14){_U = 0.12;}
            else if(variableTest==15){_U = 0.12;}
            else if(variableTest==16){_U = 0.11;}
            else if(variableTest==17){_U = 0.11;}
            else if(variableTest==18){_U = 0.11;}
            else if(variableTest==19){_U = 0.1;}
            else if(variableTest>=20){_U = 0.1;}

            calc_DP_Plancher();
            transitionSlab();
    }

    public void calcUfenetre(){
        switch (_materiauMenuiserie){

            case "bois":
                switch (_typeMenuiserie){

                    case "battante":
                        switch (_typeVitrage){
                            case "simple":
                                _U = 4.2;
                                break;
                            case "survitrage":
                                _U = 2.9;
                                break;
                            case "double90":
                                _U = 2.7;
                                break;
                            case "double90-01":
                                _U = 2.55;
                                break;
                            case "double01":
                                _U = 1.75;
                                break;
                            case "triple":
                                _U = 1.24;
                                break;
                        }
                        break;

                    case "coulissante":
                        switch (_typeVitrage){
                            case "simple":
                                _U = 4.2;
                                break;
                            case "survitrage":
                                _U = 2.9;
                                break;
                            case "double90":
                                _U = 2.7;
                                break;
                            case "double90-01":
                                _U = 2.55;
                                break;
                            case "double01":
                                _U = 1.75;
                                break;
                            case "triple":
                                _U = 1.24;
                                break;
                        }
                        break;

                    case "toit":
                        switch (_typeVitrage){
                            case "simple":
                                _U = 4.2;
                                break;
                            case "survitrage":
                                _U = 3.15;
                                break;
                            case "double90":
                                _U = 2.96;
                                break;
                            case "double90-01":
                                _U = 2.9;
                                break;
                            case "double01":
                                _U = 2.04;
                                break;
                            case "triple":
                                _U = 1.46;
                                break;
                        }
                        break;
                }
                break;

            case "pvc":
                switch (_typeMenuiserie){

                    case "battante":
                        switch (_typeVitrage){
                            case "simple":
                                _U = 3.90;
                                break;
                            case "survitrage":
                                _U = 2.75;
                                break;
                            case "double90":
                                _U = 2.45;
                                break;
                            case "double90-01":
                                _U = 2.35;
                                break;
                            case "double01":
                                _U = 1.70;
                                break;
                            case "triple":
                                _U = 1.24;
                                break;
                        }
                        break;

                    case "coulissante":
                        switch (_typeVitrage){
                            case "simple":
                                _U = 4.25;
                                break;
                            case "survitrage":
                                _U = 3;
                                break;
                            case "double90":
                                _U = 2.62;
                                break;
                            case "double90-01":
                                _U = 2.52;
                                break;
                            case "double01":
                                _U = 1.85;
                                break;
                            case "triple":
                                _U = 1.39;
                                break;
                        }
                        break;

                    case "toit":
                        switch (_typeVitrage){
                            case "simple":
                                _U = 3.90;
                                break;
                            case "survitrage":
                                _U = 2.92;
                                break;
                            case "double90":
                                _U = 2.70;
                                break;
                            case "double90-01":
                                _U = 2.70;
                                break;
                            case "double01":
                                _U = 2.01;
                                break;
                            case "triple":
                                _U = 1.39;
                                break;
                        }
                        break;
                }
                break;

            case "met<2001":
                switch (_typeMenuiserie){

                    case "battante":
                        switch (_typeVitrage){
                            case "simple":
                                _U = 4.95;
                                break;
                            case "survitrage":
                                _U = 4;
                                break;
                            case "double90":
                                _U = 3.70;
                                break;
                            case "double90-01":
                                _U = 3.60;
                                break;
                        }
                        break;

                    case "coulissante":
                        switch (_typeVitrage){
                            case "simple":
                                _U = 4.63;
                                break;
                            case "survitrage":
                                _U = 3.46;
                                break;
                            case "double90":
                                _U = 3.46;
                                break;
                            case "double90-01":
                                _U = 3.36;
                                break;
                        }
                        break;

                    case "toit":
                        switch (_typeVitrage){
                            case "simple":
                                _U = 4.95;
                                break;
                            case "survitrage":
                                _U = 4.38;
                                break;
                            case "double90":
                                _U = 4.01;
                                break;
                            case "double90-01":
                                _U = 3.92;
                                break;
                        }
                        break;
                }
                break;

            case "met>=2001":
                switch (_typeMenuiserie){
                    case "battante":
                        switch (_typeVitrage){
                            case "double01":
                                _U = 2.25;
                                break;
                            case "triple":
                                _U = 1.88;
                                break;
                        }
                        break;
                    case "coulissante":
                        switch (_typeVitrage){
                            case "double01":
                                _U = 2.18;
                                break;
                            case "triple":
                                _U = 1.65;
                                break;
                        }
                        break;
                    case "toit":
                        switch (_typeVitrage){
                            case "double01":
                                _U = 3.3;
                                break;
                            case "triple":
                                _U = 3.15;
                                break;
                        }
                        break;
                }
                break;
        }
    }

    /*--------------------------------Lecteur/Writter .IFC---------------------------------------*/

    /*** On mémorise le premier mur contenu dans le model ***/
    public void takeFirstWall(){
        Iterator<IfcWallStandardCase> it = _wallStandardCaseCollection.iterator();
        _wall=it.next();
        MainApplicationAdapter.select(_wall);
    }

    /*** On mémorise le premier slab contenu dans le model ***/
    public void takeFirstSlab(){
        Iterator<IfcSlab> it = _slabCollection.iterator();
        _slab = it.next();
        MainApplicationAdapter.select(_slab);
    }

    /*** On mémorise la première window contenue dans le model ***/
    public void takeFirstWindow(){
        Iterator<IfcWindow> it = _windowCollection.iterator();
        _window = it.next();
        MainApplicationAdapter.select(_window);
    }

    /*** On vérifie si on est au dernier mur du model ***/
    public boolean isLastWall(){
        int compteur=0;
        int nbMurs=_wallStandardCaseCollection.size();
        boolean isNotEqual=true;
        Iterator<IfcWallStandardCase> it = _wallStandardCaseCollection.iterator();
        while(isNotEqual){
            compteur += 1;
            if(it.next().equals(_wall)){
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

    /*** On vérifie si on est au dernier plancher du model ***/
    public boolean isLastSlab(){
        int compteur=0;
        int nbSlabs=_slabCollection.size();
        boolean isNotEqual=true;
        Iterator<IfcSlab> it = _slabCollection.iterator();
        while(isNotEqual){
            compteur += 1;
            if(it.next().equals(_slab)){
                isNotEqual=false;
            }
        }
        if (compteur<nbSlabs && !isNotEqual){
            return false;
        }
        else{
            return true;
        }
    }

    /*** On vérifie si on est à la dernière window du model ***/
    public boolean isLastWindow(){
        int compteur=0;
        int nbWindows=_windowCollection.size();
        boolean isNotEqual=true;
        Iterator<IfcWindow> it = _windowCollection.iterator();
        while(isNotEqual){
            compteur += 1;
            if(it.next().equals(_window)){
                isNotEqual=false;
            }
        }
        if (compteur<nbWindows && !isNotEqual){
            return false;
        }
        else{
            return true;
        }
    }

    /*** On prend le mur suivant du model ***/
    public void takeNextWall(){
        Iterator<IfcWallStandardCase> it = _wallStandardCaseCollection.iterator();
        for(int i=0;i<_wallStandardCaseCollection.size()-1;i++){
            if(it.next().equals(_wall)){
                _wall=it.next();
            }
        }
    }

    /*** On prend le slab suivant du model ***/
    public void takeNextSlab(){
        Iterator<IfcSlab> it = _slabCollection.iterator();
        for(int i=0;i<_slabCollection.size()-1;i++){
            if(it.next().equals(_slab)){
                _slab=it.next();
            }
        }
    }

    /*** On prend la window suivante du model ***/
    public void takeNextWindow(){
        Iterator<IfcWindow> it = _windowCollection.iterator();
        for(int i=0;i<_windowCollection.size()-1;i++){
            if(it.next().equals(_window)){
                _window=it.next();
            }
        }
    }

    /*---------------------------- Bloc de transition logique ----------------------------------*/

    /*** On regarde s'il reste des murs à traiter, sinon on traite les éléments suivants ***/
    public void transitionMur(){
        if (isLastWall()){
            takeFirstSlab();
            MainApplicationAdapter.select(_slab);
            demandeSousPlancher();
        }
        else{
            takeNextWall();
            MainApplicationAdapter.select(_wall);
            demandeDerriereMur();
        }
    }

    /*** On regarde s'il reste des slabs à traiter, sinon on traite les éléments suivants ***/
    public void transitionSlab(){
        if (isLastSlab()){
            takeFirstWindow();
            MainApplicationAdapter.select(_window);
            demandeMateriauFenetre();
        }
        else{
            takeNextSlab();
            MainApplicationAdapter.select(_slab);
            demandeSousPlancher();
        }
    }

    /*** On regarde s'il reste des windows à traiter, sinon on traite les éléments suivants ***/
    public void transitionWindow(){
        if (isLastWindow()){
            // TODO : continuer à partir de là ...
        }
        else{
            takeNextWindow();
            MainApplicationAdapter.select(_window);
            demandeMateriauFenetre();
        }
    }

    /*---------------------------------------IHM------------------------------------------------*/

    /*** Ajout du message initial et du bouton startDpe ***/
    public void startDPE() {

        Texture texture = new Texture(Gdx.files.internal("data/img/dpe/StartDpe.png"));
        Image image = new Image (texture);
        int largeurImage = texture.getWidth()+10;
        int hauteurImage = texture.getHeight()+20;

        //////////////////////////////////////////////////////////////////////////////////
        Iterator<IfcWallStandardCase> it = _wallStandardCaseCollection.iterator();
        for(int i=0;i<_wallStandardCaseCollection.size();i++){
            System.out.println(it.next().getName().getDecodedValue());
        }
        Iterator<IfcSlab> it2 = _slabCollection.iterator();
        for(int i=0;i<_slabCollection.size();i++){
            System.out.println(it2.next().getStepLine());
        }
        Iterator<IfcWindow> it3 = _windowCollection.iterator();
        for(int i=0;i<_windowCollection.size();i++){
            System.out.println(it3.next().getStepLine());
        }
        //////////////////////////////////////////////////////////////////////////////////

        final Dialog dialog = new Dialog(" Objectif du DPE ", _skin, "dialog") {
            protected void result (Object object) {
                if(object.equals(true)){
                    demandeTypeBatiment();
                }
                else{
                    Array<Actor> actorArray = _stage.getActors();
                    for (Actor actualActor : actorArray){
                        if (actualActor instanceof TextButton){
                            if (actualActor.getName().equals("DPE")){
                                actualActor.setVisible(true);
                            }
                        }
                    }
                }
            }
        }.button("Annuler", false).button("Commencer le Dpe", true).show(_stage);

        dialog.setSize(largeurImage, hauteurImage + dialog.getHeight());
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
        image.setPosition((dialog.getWidth() - image.getWidth()) / 2, 40);
        dialog.addActor(image);
    }

    /*** Demande le type de batiment ***/
    public void demandeTypeBatiment(){

        Texture texture1 = new Texture(Gdx.files.internal("data/img/dpe/TypeBatiment/maison.png"));
        Texture texture2 = new Texture(Gdx.files.internal("data/img/dpe/TypeBatiment/appt.png"));
        Image image1 = new Image(texture1);
        Image image2 = new Image(texture2);
        int largeur2images = texture1.getWidth()*2+10;
        int hauteurImages = texture1.getHeight()+20;

        final Dialog dialog = new Dialog(" Type de batiment ", _skin, "dialog") {
            protected void result (Object object) {

            }
        }.show(_stage);

        ImageButton imageButton1 = new ImageButton(image1.getDrawable());
        imageButton1.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _typeBatiment = "Maison";
                dialog.remove();
                demandeNbNiveau();
            }
        });

        ImageButton imageButton2 = new ImageButton(image2.getDrawable());
        imageButton2.setPosition(imageButton1.getWidth() + 10, 0);
        imageButton2.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _typeBatiment = "Appartement";
                dialog.remove();
                demandePositionAppartement();
            }
        });

        dialog.setSize(largeur2images, hauteurImages);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
        dialog.addActor(imageButton1);
        dialog.addActor(imageButton2);
    }

    /*** Demande le nombre de niveaux ***/
    public void demandeNbNiveau(){
        Texture texture1 = new Texture(Gdx.files.internal("data/img/dpe/NbNiveaux/plainPied.png"));
        Texture texture2 = new Texture(Gdx.files.internal("data/img/dpe/NbNiveaux/plainPiedCa.png"));
        Texture texture3 = new Texture(Gdx.files.internal("data/img/dpe/NbNiveaux/r+1.png"));
        Texture texture4 = new Texture(Gdx.files.internal("data/img/dpe/NbNiveaux/r+1Ca.png"));
        Texture texture5 = new Texture(Gdx.files.internal("data/img/dpe/NbNiveaux/r+2.png"));
        Image image1 = new Image(texture1);
        Image image2 = new Image(texture2);
        Image image3 = new Image(texture3);
        Image image4 = new Image(texture4);
        Image image5 = new Image(texture5);
        int largeurImages = texture1.getWidth() + texture2.getWidth() + texture3.getWidth() + texture4.getWidth() + texture5.getWidth() + 45;
        int hauteurImages = texture1.getHeight() + 20;

        final Dialog dialog = new Dialog(" Nombre de niveaux ", _skin, "dialog") {
            protected void result(Object object) {

            }
        }.show(_stage);

        ImageButton imageButton1 = new ImageButton(image1.getDrawable());
        imageButton1.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _NIV = 1;
                dialog.remove();
                demandeForme();
            }
        });

        ImageButton imageButton2 = new ImageButton(image2.getDrawable());
        imageButton2.setPosition(imageButton1.getWidth() + 10, 0);
        imageButton2.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _NIV = 1.5;
                dialog.remove();
                demandeForme();
            }
        });

        ImageButton imageButton3 = new ImageButton(image3.getDrawable());
        imageButton3.setPosition(imageButton2.getX() + imageButton2.getWidth() + 10, 0);
        imageButton3.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _NIV = 2;
                dialog.remove();
                demandeForme();
            }
        });

        ImageButton imageButton4 = new ImageButton(image4.getDrawable());
        imageButton4.setPosition(imageButton3.getX() + imageButton3.getWidth() + 10, 0);
        imageButton4.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _NIV = 2.5;
                dialog.remove();
                demandeForme();
            }
        });

        ImageButton imageButton5 = new ImageButton(image5.getDrawable());
        imageButton5.setPosition(imageButton4.getX() + imageButton4.getWidth() + 10, 0);
        imageButton5.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _NIV = 3;
                dialog.remove();
                demandeForme();
            }
        });

        dialog.setSize(largeurImages, hauteurImages);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
        dialog.addActor(imageButton1);
        dialog.addActor(imageButton2);
        dialog.addActor(imageButton3);
        dialog.addActor(imageButton4);
        dialog.addActor(imageButton5);
    }

    /*** Demande le nombre de niveaux d'une maison ***/
    public void demandeForme(){
        Texture texture1 = new Texture(Gdx.files.internal("data/img/dpe/FormeMaison/carre.png"));
        Texture texture2 = new Texture(Gdx.files.internal("data/img/dpe/FormeMaison/allongee.png"));
        Texture texture3 = new Texture(Gdx.files.internal("data/img/dpe/FormeMaison/developpee.png"));
        Image image1 = new Image(texture1);
        Image image2 = new Image(texture2);
        Image image3 = new Image(texture3);
        int largeur3images = texture1.getWidth() + texture2.getWidth() + texture3.getWidth() + 25;
        int hauteurImages = texture1.getHeight() + 20;

        final Dialog dialog = new Dialog(" Forme de la maison ", _skin, "dialog") {
            protected void result(Object object) {

            }
        }.show(_stage);

        ImageButton imageButton1 = new ImageButton(image1.getDrawable());
        imageButton1.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _FOR = 4.12;
                dialog.remove();
                demandeMitoyennete();
            }
        });

        ImageButton imageButton2 = new ImageButton(image2.getDrawable());
        imageButton2.setPosition(imageButton1.getWidth() + 10, 0);
        imageButton2.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _FOR = 4.81;
                dialog.remove();
                demandeMitoyennete();
            }
        });

        ImageButton imageButton3 = new ImageButton(image3.getDrawable());
        imageButton3.setPosition(imageButton2.getX() + imageButton2.getWidth() + 10, 0);
        imageButton3.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _FOR = 5.71;
                dialog.remove();
                demandeMitoyennete();
            }
        });

        dialog.setSize(largeur3images, hauteurImages);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
        dialog.addActor(imageButton1);
        dialog.addActor(imageButton2);
        dialog.addActor(imageButton3);
    }

    /*** Demande la mitoyenneté d'une maison ***/
    public void demandeMitoyennete(){
        Texture texture1 = new Texture(Gdx.files.internal("data/img/dpe/Mitoyennete/independante.png"));
        Texture texture2 = new Texture(Gdx.files.internal("data/img/dpe/Mitoyennete/accoleePetitCote.png"));
        Texture texture3 = new Texture(Gdx.files.internal("data/img/dpe/Mitoyennete/accoleeUnGrandOuDeuxPetits.png"));
        Texture texture4 = new Texture(Gdx.files.internal("data/img/dpe/Mitoyennete/accoleeUnGrandEtUnPetit.png"));
        Texture texture5 = new Texture(Gdx.files.internal("data/img/dpe/Mitoyennete/accoleeDeuxGrandsCotes.png"));
        Image image1 = new Image(texture1);
        Image image2 = new Image(texture2);
        Image image3 = new Image(texture3);
        Image image4 = new Image(texture4);
        Image image5 = new Image(texture5);
        int largeurImages = texture1.getWidth() + texture2.getWidth() + texture3.getWidth() + texture4.getWidth() + texture5.getWidth() + 45;
        int hauteurImages = texture1.getHeight() + 20;

        final Dialog dialog = new Dialog(" Mitoyennete avec un autre logement ", _skin, "dialog") {
            protected void result(Object object) {

            }
        }.show(_stage);

        ImageButton imageButton1 = new ImageButton(image1.getDrawable());
        imageButton1.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _MIT = 1;
                calc_MIT2();
                dialog.remove();
                demandeAnneeConstruction("ex : 1998");
            }
        });

        ImageButton imageButton2 = new ImageButton(image2.getDrawable());
        imageButton2.setPosition(imageButton1.getWidth() + 10, 0);
        imageButton2.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _MIT = 0.8;
                calc_MIT2();
                dialog.remove();
                demandeAnneeConstruction("ex : 1998");
            }
        });

        ImageButton imageButton3 = new ImageButton(image3.getDrawable());
        imageButton3.setPosition(imageButton2.getX() + imageButton2.getWidth() + 10, 0);
        imageButton3.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _MIT = 0.7;
                calc_MIT2();
                dialog.remove();
                demandeAnneeConstruction("ex : 1998");
            }
        });

        ImageButton imageButton4 = new ImageButton(image4.getDrawable());
        imageButton4.setPosition(imageButton3.getX() + imageButton3.getWidth() + 10, 0);
        imageButton4.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _MIT = 0.5;
                calc_MIT2();
                dialog.remove();
                demandeAnneeConstruction("ex : 1998");
            }
        });

        ImageButton imageButton5 = new ImageButton(image5.getDrawable());
        imageButton5.setPosition(imageButton4.getX() + imageButton4.getWidth() + 10, 0);
        imageButton5.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _MIT = 0.35;
                calc_MIT2();
                dialog.remove();
                demandeAnneeConstruction("ex : 1998");
            }
        });

        dialog.setSize(largeurImages, hauteurImages);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
        dialog.addActor(imageButton1);
        dialog.addActor(imageButton2);
        dialog.addActor(imageButton3);
        dialog.addActor(imageButton4);
        dialog.addActor(imageButton5);
    }

    /*** Demande Année de contruction ***/
    public void demandeAnneeConstruction(String initialText){
        final TextField textField = new TextField("",_skin);

;       Dialog dialog = new Dialog(" Annee de construction du batiment ", _skin, "dialog") {
            protected void result (Object object) {
                String reponse = textField.getText();
                try{
                    double annee = Double.parseDouble(reponse);
                    if (annee < 1800 || annee > 2015){
                        demandeAnneeConstruction("Saisie invalide");
                    }
                    else {
                        _anneeConstruction = annee;
                        demandeTypeEnergieConstruction();
                    }
                }
                catch (NumberFormatException e){
                    demandeAnneeConstruction("Saisie invalide");
                }
            }
        }.button("Valider", true).show(_stage);

        textField.setPosition((dialog.getWidth() - textField.getWidth()) / 2, 40);
        textField.setMessageText(initialText);
        dialog.setSize(dialog.getWidth(), textField.getHeight() + dialog.getHeight() + 20);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
        dialog.addActor(textField);
    }

    /*** Demande position de l'appartement ***/
    public void demandePositionAppartement() {
        Texture texture1 = new Texture(Gdx.files.internal("data/img/dpe/PositionAppartement/1erEtage.png"));
        Texture texture2 = new Texture(Gdx.files.internal("data/img/dpe/PositionAppartement/etageInt.png"));
        Texture texture3 = new Texture(Gdx.files.internal("data/img/dpe/PositionAppartement/dernierEtage.png"));
        Image image1 = new Image(texture1);
        Image image2 = new Image(texture2);
        Image image3 = new Image(texture3);
        int largeur3images = texture1.getWidth() + texture2.getWidth() + texture3.getWidth() + 25;
        int hauteurImages = texture1.getHeight() + 20;

        final Dialog dialog = new Dialog(" Position de l'appartement ", _skin, "dialog") {
            protected void result(Object object) {

            }
        }.show(_stage);

        ImageButton imageButton1 = new ImageButton(image1.getDrawable());
        imageButton1.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _positionAppartement = "PremierEtage";
                dialog.remove();
                demandeAnneeConstruction("ex : 1998");
            }
        });

        ImageButton imageButton2 = new ImageButton(image2.getDrawable());
        imageButton2.setPosition(imageButton1.getWidth() + 10, 0);
        imageButton2.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _positionAppartement = "EtageIntermediaire";
                dialog.remove();
                demandeAnneeConstruction("ex : 1998");
            }
        });

        ImageButton imageButton3 = new ImageButton(image3.getDrawable());
        imageButton3.setPosition(imageButton2.getX() + imageButton2.getWidth() + 10, 0);
        imageButton3.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _positionAppartement = "dernierEtage";
                dialog.remove();
                demandeAnneeConstruction("ex : 1998");
            }
        });

        dialog.setSize(largeur3images, hauteurImages);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
        dialog.addActor(imageButton1);
        dialog.addActor(imageButton2);
        dialog.addActor(imageButton3);
    }

    /*** Demande le type d'énergie à la construction ***/
    public void demandeTypeEnergieConstruction(){

        Dialog dialog = new Dialog(" Type d'energie a la construction ", _skin, "dialog") {
            protected void result (Object object) {
                if(object.equals(1)){
                    _typeEnergieConstruction = "Electrique";
                }
                else
                {
                    _typeEnergieConstruction = "Autre";
                }
                if (!_wallStandardCaseCollection.isEmpty()){
                    takeFirstWall();

                    while (!isLastWall()){
                        demandeDerriereMur2(_wall);
                        takeNextWall();
                    }
                    //takeFirstSlab();
                    //demandeSousPlancher();
//                    takeFirstWindow();
//                    demandeMateriauFenetre();
                    demandePresenceLNC();
                }
            }
        }.button("Electrique", 1).button("Autre",2).show(_stage);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
    }

    public void demandeDerriereMur2(IfcWallStandardCase wall) {
        DpeEvent eventType = DpeEvent.DERRIERE_MUR;
        Event event = new Event(eventType, wall);

        EventManager.getInstance().put(Channel.DPE, event);
    }

    /*** Demande si le logement contient un LNC ***/
    public void demandePresenceLNC(){
        Dialog dialog = new Dialog(" Présence d'une local non chauffe ? ", _skin, "dialog") {
            protected void result (Object object) {
                if(object.equals(1)){
                    _presenceLNC = true;
                }
                else
                {
                    _presenceLNC = false;
                }
                takeFirstWall();
                demandeDerriereMur();
            }
        }.button("Oui", 1).button("Non", 2).show(_stage);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
    }

    /*** Demande ce qui se trouve derriere un mur ***/
    public void demandeDerriereMur(){

        /*** Qu'est-ce qu'il y a derrière ? ***/
        String name = _wall.getName().getDecodedValue();
        if(IfcHelper.getPropertyTypeWall(_wall).equals("ext")){
            Dialog dialog = new Dialog(" Qu'est-ce qu'il y a derriere le mur : "+name, _skin, "dialog") {
                protected void result (Object object) {
                    if(object.equals(1)){
                        _derriere="ext";
                    }
                    else if (object.equals(2)) {
                        _derriere="lnc";
                    }
                    else if (object.equals(3)) {
                        _derriere="ah";
                    }
                    else if (object.equals(4)) {
                        _derriere="ver";
                    }
                    demandeIsolationMur();
                }
            }.button("Exterieur", 1).button("Local non chauffe", 2).button("Autre habitation", 3).button("Veranda", 4).show(_stage);
            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
        }
        else if (IfcHelper.getPropertyTypeWall(_wall).equals("int")) {
            if (_presenceLNC){
                Dialog dialog = new Dialog(" Local non chauffe derriere ce mur ? : "+name, _skin, "dialog") {
                    protected void result (Object object) {
                        if(object.equals(1)){
                            _derriere="lnc";
                            demandeIsolationMur();
                        }
                        else if (object.equals(2)) {
                            transitionMur();
                        }
                    }
                }.button("oui", 1).button("non", 2).show(_stage);
                dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
            }else{
                transitionMur();
            }

        }
    }

    /*** Demande si le mur est isolé ***/
    public void demandeIsolationMur(){

        Dialog dialog = new Dialog(" Isolation ? : ", _skin, "dialog") {
            protected void result (Object object) {
                if(object.equals(1)){
                    _isole=false;
                    _U = 2.5;
                    calc_DP_Mur();
                    suite();
                    transitionMur();
                }
                else if (object.equals(2)) {
                    _isole=true;
                    demandeAnneeIsolationMurConnue();
                }
                else if (object.equals(3)) {
                    _isole=false; // On considere le pire des cas
                    calcUmurInconnu();
                    calc_DP_Mur();
                    suite();
                    transitionMur();
                }
            }
        }.button("Non", 1).button("Oui", 2).button("Inconnue", 3).show(_stage);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
    }

    /*** Demande si l'année d'isolation est connue ou non ***/
    public void demandeAnneeIsolationMurConnue(){
        Dialog dialog = new Dialog(" Annee d'isolation ? : ", _skin, "dialog") {
            protected void result (Object object) {
                if(object.equals(1)){
                    demandeAnneeIsolationMur("");
                }
                else if (object.equals(2)) {
                    if(_anneeConstruction<1974){
                        _U = 0.8;
                    }
                    else{
                        calcUmurInconnu();
                    }
                    calc_DP_Mur();
                    suite();
                    transitionMur();
                }
            }
        }.button("Connue", 1).button("Inconnue",2).show(_stage);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
    }

    /*** Demande l'année d'isolation ***/
    public void demandeAnneeIsolationMur(String initialText){
        final TextField textField = new TextField("",_skin);

        ;       Dialog dialog = new Dialog("      Annee d'isolation      ", _skin, "dialog") {
            protected void result (Object object) {
                String reponse = textField.getText();
                try{
                    _anneeIsolation = Double.parseDouble(reponse);
                    if (_anneeIsolation < 1800 || _anneeIsolation > 2015){
                        demandeAnneeIsolationMur("Saisie invalide");
                    }
                    else if (_anneeIsolation == _anneeConstruction){
                        calcUmurInconnu();
                        calc_DP_Mur();
                        suite();
                        transitionMur();
                    }
                    else{
                        calcUmurRenovation();
                        calc_DP_Mur();
                        suite();
                        transitionMur();
                    }
                }
                catch (NumberFormatException e){
                    demandeAnneeIsolationMur("Saisie invalide");
                }
            }
        }.button("Valider", true).show(_stage);

        textField.setPosition((dialog.getWidth() - textField.getWidth()) / 2, 40);
        textField.setMessageText(initialText);
        dialog.setSize(dialog.getWidth(), textField.getHeight() + dialog.getHeight() + 20);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
        dialog.addActor(textField);
    }

    /*** Demande ce qui se trouve sous un plancher ***/
    public void demandeSousPlancher(){

        /*** Qu'est-ce qu'il y a dessous ? ***/
        Dialog dialog = new Dialog(" Qu'est-ce qu'il y a sous ce plancher : ", _skin, "dialog") {
            protected void result (Object object) {
                if(object.equals(1)){
                    _derriere="vs";
                    demandeIsolationPlancher();
                }
                else if (object.equals(2)) {
                    _derriere="ss";
                    demandeIsolationPlancher();
                }
                else if (object.equals(3)) {
                    _derriere="tp";
                    calcUplanTp();
                }
                else if (object.equals(4)) {
                    _derriere="ah";
                    demandeIsolationPlancher();
                }
            }
        }.button("Vide-sanitaire", 1).button("Sous-sol", 2).button("Terre plein", 3).button("Autre habitation", 4).show(_stage);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
    }

    /*** Demande si le mur est isolé ***/
    public void demandeIsolationPlancher(){

        Dialog dialog = new Dialog(" Isolation ? : ", _skin, "dialog") {
            protected void result (Object object) {
                if(object.equals(1)){
                    _isole=false;
                    _U = 2.5;
                    calc_DP_Plancher();
                    suite();
                    transitionSlab();
                }
                else if (object.equals(2)) {
                    _isole=true;
                    demandeAnneeIsolationPlancherConnue();
                }
                else if (object.equals(3)) {
                    _isole=false; // On considere le pire des cas
                    calcUplancherInconnu();
                    calc_DP_Plancher();
                    suite();
                    transitionSlab();
                }
            }
        }.button("Non", 1).button("Oui", 2).button("Inconnue", 3).show(_stage);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
    }

    /*** Demande si l'année d'isolation est connue ou non ***/
    public void demandeAnneeIsolationPlancherConnue(){
        Dialog dialog = new Dialog(" Annee d'isolation ? : ", _skin, "dialog") {
            protected void result (Object object) {
                if(object.equals(1)){
                    demandeAnneeIsolationPlancher("");
                }
                else if (object.equals(2)) {
                    if(_anneeConstruction<1974){
                        _U = 0.8;
                    }
                    else{
                        calcUplancherInconnu();
                    }
                    calc_DP_Plancher();
                    suite();
                    transitionSlab();
                }
            }
        }.button("Connue", 1).button("Inconnue",2).show(_stage);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
    }

    /*** Demande l'année d'isolation ***/
    public void demandeAnneeIsolationPlancher(String initialText){
        final TextField textField = new TextField("",_skin);

        ;       Dialog dialog = new Dialog("      Annee d'isolation      ", _skin, "dialog") {
            protected void result (Object object) {
                String reponse = textField.getText();
                try{
                    _anneeIsolation = Double.parseDouble(reponse);
                    if (_anneeIsolation < 1800 || _anneeIsolation > 2015){
                        demandeAnneeIsolationPlancher("Saisie invalide");
                    }
                    else if (_anneeIsolation == _anneeConstruction){
                        calcUplancherInconnu();
                        calc_DP_Plancher();
                        suite();
                        transitionSlab();
                    }
                    else {
                        calcUplancherRenovation();
                        calc_DP_Plancher();
                        suite();
                        transitionSlab();
                    }
                }
                catch (NumberFormatException e){
                    demandeAnneeIsolationMur("Saisie invalide");
                }
            }
        }.button("Valider", true).show(_stage);

        textField.setPosition((dialog.getWidth() - textField.getWidth()) / 2, 40);
        textField.setMessageText(initialText);
        dialog.setSize(dialog.getWidth(), textField.getHeight() + dialog.getHeight() + 20);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
        dialog.addActor(textField);
    }

    /*** Demande le materiau constituant une fenêtre ***/
    public void demandeMateriauFenetre(){
        Texture texture1 = new Texture(Gdx.files.internal("data/img/dpe/Fenetre/Materiaux/bois.png"));
        Texture texture2 = new Texture(Gdx.files.internal("data/img/dpe/Fenetre/Materiaux/metallique.png"));
        Texture texture3 = new Texture(Gdx.files.internal("data/img/dpe/Fenetre/Materiaux/pvc.png"));
        Image image1 = new Image(texture1);
        Image image2 = new Image(texture2);
        Image image3 = new Image(texture3);
        int largeur3images = texture1.getWidth() + texture2.getWidth() + texture3.getWidth() + 25;
        int hauteurImages = texture1.getHeight() + 20;

        final Dialog dialog = new Dialog(" Materiau constituant la fenetre ", _skin, "dialog") {
            protected void result(Object object) {

            }
        }.show(_stage);

        ImageButton imageButton1 = new ImageButton(image1.getDrawable());
        imageButton1.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _materiauMenuiserie = "bois";
                dialog.remove();
                demandeTypeFenetre();
            }
        });

        ImageButton imageButton2 = new ImageButton(image2.getDrawable());
        imageButton2.setPosition(imageButton1.getWidth() + 10, 0);
        imageButton2.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                dialog.remove();
                demandeAgeMenuiserieMetallique();
            }
        });

        ImageButton imageButton3 = new ImageButton(image3.getDrawable());
        imageButton3.setPosition(imageButton2.getX() + imageButton2.getWidth() + 10, 0);
        imageButton3.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _materiauMenuiserie = "pvc";
                dialog.remove();
                demandeTypeFenetre();
            }
        });

        dialog.setSize(largeur3images, hauteurImages);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
        dialog.addActor(imageButton1);
        dialog.addActor(imageButton2);
        dialog.addActor(imageButton3);
    }

    /*** Demande l'age d'une menuiserie metallique ***/
    public void demandeAgeMenuiserieMetallique(){
        Dialog dialog = new Dialog(" Age de la menuiserie ? : ", _skin, "dialog") {
            protected void result (Object object) {
                if(object.equals(1)){
                    _materiauMenuiserie = "met<2001";
                    demandeTypeFenetre();
                }
                else if (object.equals(2)) {
                    _materiauMenuiserie = "met>=2001";
                    demandeTypeFenetre();
                }
            }
        }.button("inferieure a 2001", 1).button("superieure ou egal a 2001",2).show(_stage);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
    }

    /*** Demande le type d'une fenetre ***/
    public void demandeTypeFenetre(){
        Texture texture1 = new Texture(Gdx.files.internal("data/img/dpe/Fenetre/Type/battante.png"));
        Texture texture2 = new Texture(Gdx.files.internal("data/img/dpe/Fenetre/Type/coulissante.png"));
        Image image1 = new Image(texture1);
        Image image2 = new Image(texture2);
        int largeur2images = texture1.getWidth()+texture2.getWidth()+10;
        int hauteurImages = texture1.getHeight()+20;

        final Dialog dialog = new Dialog(" Type de fenetre ", _skin, "dialog") {
            protected void result (Object object) {

            }
        }.show(_stage);

        ImageButton imageButton1 = new ImageButton(image1.getDrawable());
        imageButton1.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _typeMenuiserie = "battante";
                dialog.remove();
                if (_materiauMenuiserie.equals("bois")||(_materiauMenuiserie.equals("pvc"))) {
                    demandeTypeVitrage();
                }
                else if (_materiauMenuiserie.equals("met<2001")) {
                    demandeTypeVitrageMetInf01();
                }
                else if (_materiauMenuiserie.equals("met>=2001")) {
                    demandeTypeVitrageMetSup01();
                }
            }
        });

        ImageButton imageButton2 = new ImageButton(image2.getDrawable());
        imageButton2.setPosition(imageButton1.getWidth() + 10, 0);
        imageButton2.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _typeMenuiserie = "coulissante";
                dialog.remove();
                if (_materiauMenuiserie.equals("bois")||(_materiauMenuiserie.equals("pvc"))) {
                    demandeTypeVitrage();
                }
                else if (_materiauMenuiserie.equals("met<2001")) {
                    demandeTypeVitrageMetInf01();
                }
                else if (_materiauMenuiserie.equals("met>=2001")) {
                    demandeTypeVitrageMetSup01();
                }
            }
        });

        dialog.setSize(largeur2images, hauteurImages);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
        dialog.addActor(imageButton1);
        dialog.addActor(imageButton2);
    }

    /*** Demande le type de vitrage d'une fenetre PVC ou bois ***/
    public void demandeTypeVitrage(){
        Dialog dialog = new Dialog(" Quel est le type de vitrage : ", _skin, "dialog") {
            protected void result (Object object) {
                if(object.equals(1)){
                    _typeVitrage = "simple";
                }
                else if (object.equals(2)) {
                    _typeVitrage = "survitrage";
                }
                else if (object.equals(3)) {
                    _typeVitrage = "double90";
                }
                else if (object.equals(4)) {
                    _typeVitrage = "double90-01";
                }
                else if (object.equals(5)) {
                    _typeVitrage = "double01";
                }
                calcUfenetre();
                calc_DP_Fenetre();
                suite();
                transitionWindow();
            }
        }.button("Simple vitrage", 1).button("Survitrage", 2).button("Double vitrage <1990", 3).button("Double vitrage 1990<...<2001", 4).button("Double vitrage >2001", 5).show(_stage);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
    }

    /*** Demande le type de vitrage d'une fenetre mettallique >2001 ***/
    public void demandeTypeVitrageMetSup01(){
        Dialog dialog = new Dialog(" Quel est le type de vitrage : ", _skin, "dialog") {
            protected void result (Object object) {
                if(object.equals(1)){
                    _typeVitrage = "double01";
                }
                else if (object.equals(2)) {
                    _typeVitrage = "triple";
                }
                calcUfenetre();
                calc_DP_Fenetre();
                suite();
                transitionWindow();
            }
        }.button("Double vitrage >2001", 1).button("Triple vitrage", 2).show(_stage);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
    }

    /*** Demande le type de vitrage d'une fenetre mettallique <2001***/
    public void demandeTypeVitrageMetInf01(){
        Dialog dialog = new Dialog(" Quel est le type de vitrage : ", _skin, "dialog") {
            protected void result (Object object) {
                if(object.equals(1)){
                    _typeVitrage = "simple";
                }
                else if (object.equals(2)) {
                    _typeVitrage = "survitrage";
                }
                else if (object.equals(3)) {
                    _typeVitrage = "double90";
                }
                else if (object.equals(4)) {
                    _typeVitrage = "double90-01";
                }
                calcUfenetre();
                calc_DP_Fenetre();
                suite();
                transitionWindow();
            }
        }.button("Simple vitrage", 1).button("Survitrage", 2).button("Double vitrage <1990", 3).button("Double vitrage 1990<...<2001", 4).show(_stage);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
    }


    public void suite(){
        System.out.println("************************************************************************************");
        System.out.println("Type d'energie a la construction : " + _typeEnergieConstruction);
        System.out.println("Type de batiment : " + _typeBatiment);
        System.out.println("Annee de construction : " + _anneeConstruction);
        System.out.println("NIV : " + _NIV);
        System.out.println("MIT : " + _MIT);
        System.out.println("FOR : " + _FOR);
        System.out.println("Surface habitable : " + _SH);
        System.out.println("DP mur ext : " + _DP_murExt);
        System.out.println("DP mur ah : " + _DP_murAh);
        System.out.println("DP mur lnc : " + _DP_murLnc);
        System.out.println("DP mur ver : " + _DP_murVer);
        System.out.println("DP plancher ah : " + _DP_planAh);
        System.out.println("DP plancher ss : " + _DP_planSs);
        System.out.println("DP plancher tp : " + _DP_planTp);
        System.out.println("DP plancher vs : " + _DP_planVs);
        System.out.println("Surface tampon : " + _S);
        System.out.println("U tampon : " + _U);
        System.out.println("Annee d'isolation : " + _anneeIsolation);
        System.out.println("************************************************************************************");
    }

    public void notify(Channel c, Event e) {

        EventType eventType = e.getEventType();
        if (c == Channel.DPE)
            if (eventType instanceof DpeEvent) {
                DpeEvent event = (DpeEvent) eventType;
                Object o = e.getUserObject();
                switch (event) {
                    case DERRIERE_MUR_RESPONSE:
                        Object items[] = (Object[]) o;
                        IfcWallStandardCase wall = (IfcWallStandardCase) items[0];
                        String derriere = (String) items[1];
                        calc_DP_Mur(wall, derriere);
                }
            }
    }


}
