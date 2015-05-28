package fr.limsi.rorqual.core.dpe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import ifc2x3javatoolbox.ifcmodel.IfcModel;

/**
 * Created by ricordeau on 20/05/15.
 */
public class Dpe {

    /*** Attributs liés au model IFC***/
    private IfcModel _ifcModel;

    /*** Attributs liés à l'interface graphique de libGDX ***/
    private TextureAtlas _atlas;
    private Skin _skin;
    private BitmapFont _fontBlack, _fontWhite;
    private Stage _stage;

    /*** Attributs liés au calcul du DPE ***/

    // 0.Variables générales
    private String _typeBatiment; // "Maison" ou "Appartement"
    private double _SH;
    private double _NIV;
    private double _MIT;
    private double _MIT2;
    private double _FOR;

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

    private double _S_murExt;
    private double _S_murLnc;
    private double _S_murAh;
    private double _S_murVer;
    private double _S_toiTer;
    private double _S_toiCp;
    private double _S_toiCa;
    private double _S_planVs;
    private double _S_planTp;
    private double _S_planSs;
    private double _S_planAh;
    private double _S_fen;
    private double _S_pfen;
    private double _S_fenVer;
    private double _S_pfenVer;

    private double _U_murExt;
    private double _U_murLnc;
    private double _U_murAh;
    private double _U_murVer;
    private double _U_toiTer;
    private double _U_toiCp;
    private double _U_toiCa;
    private double _U_planVs;
    private double _U_planTp;
    private double _U_planSs;
    private double _U_planAh;
    private double _U_fen;
    private double _U_pfen;
    private double _U_fenVer;
    private double _U_pfenVer;
    private double _U_portExt;
    private double _U_portLnc;
    private double _U_portVer;

    private double _b_lnc;
    private double _b_cp;
    private double _b_ss;
    private double _b_ver;


    /*** Constructeur par défaut ***/
    public Dpe () {}

    /*** Autres constructeurs ***/
    public Dpe (IfcModel ifcModel, Stage stage) {
        _atlas = new TextureAtlas(Gdx.files.internal("data/ui/button.pack"));
        _fontBlack = new BitmapFont(Gdx.files.internal("data/font/black.fnt"));
        _fontWhite = new BitmapFont(Gdx.files.internal("data/font/white.fnt"));
        _skin = new Skin(_atlas);
        _ifcModel = ifcModel;
        _stage = stage;

    }

    /*----------------------------------Getter & Setter-----------------------------------------*/
    public void set_b_lnc(double b_lnc){_b_lnc=b_lnc;}
    public void set_b_cp(double b_cp){_b_cp=b_cp;}
    public void set_b_ss(double b_ss){_b_ss=b_ss;}
    public void set_b_ver(double b_ver){_b_ver=b_ver;}


    /*------------------------------------Calculateur-------------------------------------------*/
    public void calc_BV() {
        _BV = _GV*(1-_F);
    }

    public void calc_GV() {
        _GV = _DP_murExt + _DP_murLnc + _DP_murAh + _DP_murVer + _DP_toiTer + _DP_toiCp + _DP_toiCa + _DP_planVs + _DP_planTp + _DP_planSs + _DP_planAh + _DP_fen + _DP_pfen + _DP_fenVer + _DP_pfenVer + _DP_portExt + _DP_portLnc + _DP_portVer + _PT + _DR;
    }

    public void calc_DP_murExt() {
        _DP_murExt = _S_murExt*_U_murExt;
    }

    public void calc_DP_murLnc() {
        _DP_murLnc = _b_lnc*_S_murLnc*_U_murLnc;
    }

    public void calc_DP_murAh(){
        _DP_murAh = 0.2*_S_murAh*_U_murAh;
    }

    public void calc_DP_murVer(){
        _DP_murVer = _b_ver*_S_murVer*_U_murVer;
    }

    public void calc_DP_toiTer(){
        _DP_toiTer = _S_toiTer*_U_toiTer;
    }

    public void calc_DP_toiCp(){
        _DP_toiCp = _b_cp*_S_toiCp*_U_toiCp;
    }

    public void calc_DP_toiCa(){
        _DP_toiCa = _S_toiCa*_U_toiCa;
    }

    public void calc_DP_planVs(){
        _DP_planVs = 0.8*_S_planVs*_U_planVs;
    }

    public void calc_DP_planTp(){
        _DP_planTp = _S_planTp*_U_planTp;
    }

    public void calc_DP_planSs(){
        _DP_planSs = _b_ss*_S_planSs*_U_planSs;
    }

    public void calc_DP_planAh(){
        _DP_planAh = _S_planAh*_U_planAh;
    }

    public void calc_DP_fen(){
        _DP_fen = _S_fen*_U_fen;
    }

    public void calc_DP_pfen(){
        _DP_pfen = _S_pfen*_U_pfen;
    }

    public void calc_DP_fenVer(){
        _DP_fenVer = _b_ver*_S_fenVer*_U_fenVer;
    }

    public void calc_DP_pfenVer(){
        _DP_pfenVer = _b_ver*_S_pfenVer*_U_pfenVer;
    }

    public void calc_DP_portExt(){
        _DP_portExt = 2*_U_portExt;
    }

    public void calc_DP_portLnc(){
        _DP_portLnc = _b_lnc*2*_U_portLnc;
    }

    public void calc_DP_portVer(){
        _DP_portVer = _b_ver*2*_U_portVer;
    }

    public void calc_b_lnc_Maison(boolean isole, boolean inconnu){
        if (inconnu){ // Il manque une information : on considère le pire des cas
            _b_lnc=0.95;
        }
        else{
            if(isole){
                _b_lnc=0.95;
            }
            else{
                _b_lnc=0.85;
            }
        }
    }

    public void calc_b_lnc_Appartement(boolean isole, boolean rezDeChaussee, boolean presenceSAS, boolean circulationCentrale, boolean inconnu){
        if (inconnu){ // Il manque une information : on considère le pire des cas
            _b_lnc=0.8;
        }
        else{
            if(rezDeChaussee){
                if(presenceSAS){ // Appartement au rez-de-chaussée + pas de SAS entre la porte d'accès à la circulation et l'extérieur
                    if(isole){
                        _b_lnc=0.5;
                    }
                    else{
                        _b_lnc=0.3;
                    }
                }
                else // Appartement au rez-de-chaussée + Présence d'un SAS entre la porte d'accès à la circulation et l'extérieur
                {
                    _b_lnc=0.8;
                }
            }
            else{
                if(circulationCentrale){ //Appt pas au rez-de-chaussée + positionnement ventrale de la circulation
                    if(presenceSAS){ //Appt pas au rez-de-chaussée + positionnement ventrale de la circulation + presence SAS
                        if(isole){ //Appt pas au rez-de-chaussée + positionnement ventrale de la circulation + presence SAS + mur isolé
                            _b_lnc=0.25;
                        }
                        else { //Appt pas au rez-de-chaussée + positionnement ventrale de la circulation + presence SAS + mur non-isolé
                            _b_lnc=0.1;
                        }
                    }
                    else { //Appt pas au rez-de-chaussée + positionnement ventrale de la circulation + pas de SAS
                        if(isole){ //Appt pas au rez-de-chaussée + positionnement ventrale de la circulation + pas de SAS + mur isolé
                            _b_lnc=0.45;
                        }
                        else { //Appt pas au rez-de-chaussée + positionnement ventrale de la circulation + pas de SAS + mur non-isolé
                            _b_lnc=0.25;
                        }
                    }
                }
                else{ //Appt pas au rez-de-chaussée + positionnement non ventrale de la circulation
                    if(presenceSAS){ //Appt pas au rez-de-chaussée + positionnement non ventrale de la circulation + presence SAS
                        if(isole){ //Appt pas au rez-de-chaussée + positionnement non ventrale de la circulation + presence SAS + mur isolé
                            _b_lnc=0.5;
                        }
                        else { //Appt pas au rez-de-chaussée + positionnement non ventrale de la circulation + presence SAS + mur non-isolé
                            _b_lnc=0.3;
                        }
                    }
                    else { //Appt pas au rez-de-chaussée + positionnement non ventrale de la circulation + pas de SAS
                        if(isole){ //Appt pas au rez-de-chaussée + positionnement non ventrale de la circulation + pas de SAS + mur isolé
                            _b_lnc=0.6;
                        }
                        else { //Appt pas au rez-de-chaussée + positionnement non ventrale de la circulation + pas de SAS + mur non-isolé
                            _b_lnc=0.35;
                        }
                    }
                }
            }
        }
    }

    public void calc_b_ss(boolean isole){
        if(isole){
            _b_ss=0.95;
        }
        else{
            _b_ss=0.85;
        }
    }

    public void calc_b_cp(boolean planchersPlusRampantsIsole, boolean seulRampantsIsoles){
        if(planchersPlusRampantsIsole && !seulRampantsIsoles){
            _b_cp=0.95;
        }
        else if (!planchersPlusRampantsIsole && !seulRampantsIsoles){
            _b_cp=0.9;
        }
        else if (seulRampantsIsoles){
            _b_cp=0.85;
        }
    }

    public void calc_U_murs(boolean isole){

    }

    /*------------------------------------IHM---------------------------------------------------*/
    /*** Ajout du message initial et du bouton startDpe ***/
    public void startDPE() {
        Texture texture = new Texture(Gdx.files.internal("data/img/dpe/StartDpe.png"));
        Image image = new Image (texture);
        image.setPosition(((Gdx.graphics.getWidth() - texture.getWidth()) / 2), ((Gdx.graphics.getHeight() - texture.getHeight()) / 2));
        _stage.addActor(image);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(_skin.getDrawable("buttonUp"),_skin.getDrawable("buttonDown"),null,_fontBlack);
        TextButton textButton = new TextButton("Commencer le DPE", textButtonStyle);
        textButton.setPosition(((Gdx.graphics.getWidth() - textButton.getWidth()) / 2), image.getY());
        textButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Array<Actor> actorArray = _stage.getActors();
                actorArray.pop();
                actorArray.pop();
                choixTypeBatiment();
            }
        });
        _stage.addActor(textButton);
    }

    /*** Choix entre appartement et maison ***/
    public void choixTypeBatiment(){

        Texture texture1 = new Texture(Gdx.files.internal("data/img/dpe/TypeBatiment/maison.png"));
        Texture texture2 = new Texture(Gdx.files.internal("data/img/dpe/TypeBatiment/appt.png"));
        Image image1 = new Image(texture1);
        Image image2 = new Image(texture2);
        int largeur2images = texture1.getWidth()*2+20;

        ImageButton imageButton1 = new ImageButton(image1.getDrawable());
        imageButton1.setPosition((Gdx.graphics.getWidth() - largeur2images) / 2, (Gdx.graphics.getHeight() - texture1.getHeight() - 10));
        imageButton1.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _typeBatiment="Maison";
                Array<Actor> actorArray = _stage.getActors();
                actorArray.pop();
                actorArray.pop();
                System.out.println("DPmurExt = " + _DP_murExt);
                System.out.println("DPmurAh = " + _DP_murAh);
            }
        });
        _stage.addActor(imageButton1);

        ImageButton imageButton2 = new ImageButton(image2.getDrawable());
        imageButton2.setPosition(imageButton1.getX() + texture1.getWidth() + 10, (Gdx.graphics.getHeight() - texture2.getHeight() - 10));
        imageButton2.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _typeBatiment="Appartement";
                Array<Actor> actorArray = _stage.getActors();
                actorArray.pop();
                actorArray.pop();
            }
        });
        _stage.addActor(imageButton2);
    }


}
