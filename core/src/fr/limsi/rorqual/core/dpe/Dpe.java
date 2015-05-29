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

import java.util.Collection;

import fr.limsi.rorqual.core.model.IfcHelper;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifcmodel.IfcModel;

/**
 * Created by ricordeau on 20/05/15.
 */
public class Dpe {

    /*** Attributs liés au model IFC***/
    private IfcModel _ifcModel;

    /*** Attributs liés à l'interface graphique de libGDX ***/
    private Skin _skin;
    private BitmapFont _fontBlack, _fontWhite;
    private Stage _stage;
    private TextButton.TextButtonStyle _textButtonStyle;

    /*** Attributs liés au calcul du DPE ***/

    // 0.Variables générales
    private String _typeBatiment; // "Maison" ou "Appartement"
    private String _positionAppartement; // "PremierEtage" ou "EtageIntermediaire" ou "DernierEtage"
    private String _typeEnergieConstruction; // "Electrique" ou "Autre"
    private double _anneeConstruction;
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

    /*** Constructeur par défaut ***/
    public Dpe () {}

    /*** Autres constructeurs ***/
    public Dpe (Stage stage) {
        _fontBlack = new BitmapFont(Gdx.files.internal("data/font/black.fnt"));
        _fontWhite = new BitmapFont(Gdx.files.internal("data/font/white.fnt"));
        _skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));
        _textButtonStyle = new TextButton.TextButtonStyle(_skin.getDrawable("default-round"),_skin.getDrawable("default-round-down"),null,_fontBlack);
        _ifcModel = IfcHelper.loadIfcModel("data/ifc/coucou.ifc");
        _stage = stage;
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

    public void calculDeperditionsMurs(){
        Collection<IfcWallStandardCase> collectionWall = _ifcModel.getCollection(IfcWallStandardCase.class);
        for (IfcWallStandardCase actualWall : collectionWall){
            demandeDerriereMur(actualWall);
        }
    }

    /*---------------------------------------IHM-------------------------------------------------*/
    /*** Ajout du message initial et du bouton startDpe ***/
    public void startDPE() {

        Texture texture = new Texture(Gdx.files.internal("data/img/dpe/StartDpe.png"));
        Image image = new Image (texture);
        int largeurImage = texture.getWidth()+10;
        int hauteurImage = texture.getHeight()+20;

        final Dialog dialog = new Dialog(" Objectif du DPE ", _skin, "dialog") {
            protected void result (Object object) {
                if(object.equals(true)){
                    choixTypeBatiment();
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
    public void choixTypeBatiment(){

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
                demandeAnneeConstruction("ex : 1998");
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
                calculDeperditionsMurs();
            }
        }.button("Electrique",1).button("Autre",2).show(_stage);
        dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));

    }

    /*** Demande ce qui se trouve derriere un mur ***/
    public void demandeDerriereMur(IfcWallStandardCase wall){
        // TODO Fonction qui prend un mur en paramètre, qui positionne la caméra en face de celui-ci et qui sélectionne l'élément


    }


    public void suite(){
        System.out.println("Type d'energie a la construction : " + _typeEnergieConstruction);
        System.out.println("Type de batiment : " + _typeBatiment);
        System.out.println("Annee de construction : " + _anneeConstruction);
        System.out.println("Surface habitable : " + _SH);
    }
}
