package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;
import java.util.concurrent.Semaphore;

import fr.limsi.rorqual.core.dpe.DpeEvent;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventListener;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.event.EventType;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSlab;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWindow;

/**
 * Created by christophe on 03/06/15.
 */
public class DpeUi implements EventListener {

    private Skin skin;
    private Stage stage;
    private Texture textureStartDpe;
    private Texture textureTypeBatiment1 = new Texture(Gdx.files.internal("data/img/dpe/TypeBatiment/maison.png"));
    private Texture textureTypeBatiment2 = new Texture(Gdx.files.internal("data/img/dpe/TypeBatiment/appt.png"));
    private Texture textureNbNiveau1 = new Texture(Gdx.files.internal("data/img/dpe/NbNiveaux/plainPied.png"));
    private Texture textureNbNiveau2 = new Texture(Gdx.files.internal("data/img/dpe/NbNiveaux/plainPiedCa.png"));
    private Texture textureNbNiveau3 = new Texture(Gdx.files.internal("data/img/dpe/NbNiveaux/r+1.png"));
    private Texture textureNbNiveau4 = new Texture(Gdx.files.internal("data/img/dpe/NbNiveaux/r+1Ca.png"));
    private Texture textureNbNiveau5 = new Texture(Gdx.files.internal("data/img/dpe/NbNiveaux/r+2.png"));
    private Texture textureForme1 = new Texture(Gdx.files.internal("data/img/dpe/FormeMaison/carre.png"));
    private Texture textureForme2 = new Texture(Gdx.files.internal("data/img/dpe/FormeMaison/allongee.png"));
    private Texture textureForme3 = new Texture(Gdx.files.internal("data/img/dpe/FormeMaison/developpee.png"));
    private Texture textureMit1 = new Texture(Gdx.files.internal("data/img/dpe/Mitoyennete/independante.png"));
    private Texture textureMit2 = new Texture(Gdx.files.internal("data/img/dpe/Mitoyennete/accoleePetitCote.png"));
    private Texture textureMit3 = new Texture(Gdx.files.internal("data/img/dpe/Mitoyennete/accoleeUnGrandOuDeuxPetits.png"));
    private Texture textureMit4 = new Texture(Gdx.files.internal("data/img/dpe/Mitoyennete/accoleeUnGrandEtUnPetit.png"));
    private Texture textureMit5 = new Texture(Gdx.files.internal("data/img/dpe/Mitoyennete/accoleeDeuxGrandsCotes.png"));
    private Texture texturePosAppt1 = new Texture(Gdx.files.internal("data/img/dpe/PositionAppartement/1erEtage.png"));
    private Texture texturePosAppt2 = new Texture(Gdx.files.internal("data/img/dpe/PositionAppartement/etageInt.png"));
    private Texture texturePosAppt3 = new Texture(Gdx.files.internal("data/img/dpe/PositionAppartement/dernierEtage.png"));

    public DpeUi(Stage stage2d) {
        EventManager.getInstance().addListener(Channel.DPE, this);
        skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));
        stage = stage2d;
        textureStartDpe = new Texture(Gdx.files.internal("data/img/dpe/StartDpe.png"));
    }
    Semaphore s = new Semaphore(1,true);

    public void notify(Channel c, Event e) {
        try {
            EventType eventType = e.getEventType();
            if (c == Channel.DPE)
                if (eventType instanceof DpeEvent) {
                    DpeEvent event = (DpeEvent) eventType;
                    Object o = e.getUserObject();

                    switch (event) {
                        case START_DPE: {
                            s.acquire();
                            Image image = new Image(textureStartDpe);
                            int largeurImage = textureStartDpe.getWidth() + 10;
                            int hauteurImage = textureStartDpe.getHeight() + 20;
                            Dialog dialog = new Dialog(" Objectif du DPE ", skin, "dialog") {
                                protected void result(Object object) {
                                    DpeEvent responseType = DpeEvent.START_DPE_RESPONSE;
                                    Event response = new Event(responseType, (boolean) object);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            }.button("Annuler", false).button("Commencer le Dpe", true).show(stage);
                            dialog.setSize(largeurImage, hauteurImage + dialog.getHeight());
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            image.setPosition((dialog.getWidth() - image.getWidth()) / 2, 40);
                            dialog.addActor(image);
                            break;
                        }

                        case TYPE_BATIMENT: {
                            s.acquire();
                            Image image1 = new Image(textureTypeBatiment1);
                            Image image2 = new Image(textureTypeBatiment2);
                            int largeur2images = textureTypeBatiment1.getWidth() * 2 + 10;
                            int hauteurImages = textureTypeBatiment2.getHeight() + 20;

                            Dialog dialog = new Dialog(" Type de batiment ", skin, "dialog") {
                                protected void result(Object object) {

                                }
                            }.show(stage);

                            ImageButton imageButton1 = new ImageButton(image1.getDrawable());
                            imageButton1.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    String reponse = "maison";
                                    DpeEvent responseType = DpeEvent.TYPE_BATIMENT_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });

                            ImageButton imageButton2 = new ImageButton(image2.getDrawable());
                            imageButton2.setPosition(imageButton1.getWidth() + 10, 0);
                            imageButton2.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    String reponse = "appartement";
                                    DpeEvent responseType = DpeEvent.TYPE_BATIMENT_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });

                            dialog.setSize(largeur2images, hauteurImages);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            dialog.addActor(imageButton1);
                            dialog.addActor(imageButton2);
                            break;
                        }

                        case NB_NIVEAUX: {
                            s.acquire();
                            Image image1 = new Image(textureNbNiveau1);
                            Image image2 = new Image(textureNbNiveau2);
                            Image image3 = new Image(textureNbNiveau3);
                            Image image4 = new Image(textureNbNiveau4);
                            Image image5 = new Image(textureNbNiveau5);
                            int largeurImages = textureNbNiveau1.getWidth() + textureNbNiveau2.getWidth() + textureNbNiveau3.getWidth() + textureNbNiveau4.getWidth() + textureNbNiveau5.getWidth() + 45;
                            int hauteurImages = textureNbNiveau1.getHeight() + 20;

                            Dialog dialog = new Dialog(" Nombre de niveaux ", skin, "dialog") {
                                protected void result(Object object) {

                                }
                            }.show(stage);

                            ImageButton imageButton1 = new ImageButton(image1.getDrawable());
                            imageButton1.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 1;
                                    DpeEvent responseType = DpeEvent.NB_NIVEAUX_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });

                            ImageButton imageButton2 = new ImageButton(image2.getDrawable());
                            imageButton2.setPosition(imageButton1.getWidth() + 10, 0);
                            imageButton2.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 1.5;
                                    DpeEvent responseType = DpeEvent.NB_NIVEAUX_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });

                            ImageButton imageButton3 = new ImageButton(image3.getDrawable());
                            imageButton3.setPosition(imageButton2.getX() + imageButton2.getWidth() + 10, 0);
                            imageButton3.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 2;
                                    DpeEvent responseType = DpeEvent.NB_NIVEAUX_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });

                            ImageButton imageButton4 = new ImageButton(image4.getDrawable());
                            imageButton4.setPosition(imageButton3.getX() + imageButton3.getWidth() + 10, 0);
                            imageButton4.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 2.5;
                                    DpeEvent responseType = DpeEvent.NB_NIVEAUX_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });

                            ImageButton imageButton5 = new ImageButton(image5.getDrawable());
                            imageButton5.setPosition(imageButton4.getX() + imageButton4.getWidth() + 10, 0);
                            imageButton5.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 3;
                                    DpeEvent responseType = DpeEvent.NB_NIVEAUX_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });

                            dialog.setSize(largeurImages, hauteurImages);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            dialog.addActor(imageButton1);
                            dialog.addActor(imageButton2);
                            dialog.addActor(imageButton3);
                            dialog.addActor(imageButton4);
                            dialog.addActor(imageButton5);
                            break;
                        }

                        case FORME: {
                            s.acquire();
                            Image image1 = new Image(textureForme1);
                            Image image2 = new Image(textureForme2);
                            Image image3 = new Image(textureForme3);
                            int largeur3images = textureForme1.getWidth() + textureForme2.getWidth() + textureForme3.getWidth() + 25;
                            int hauteurImages = textureForme1.getHeight() + 20;
                            Dialog dialog = new Dialog(" Forme de la maison ", skin, "dialog") {
                                protected void result(Object object) {

                                }
                            }.show(stage);

                            ImageButton imageButton1 = new ImageButton(image1.getDrawable());
                            imageButton1.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 4.12;
                                    DpeEvent responseType = DpeEvent.FORME_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });

                            ImageButton imageButton2 = new ImageButton(image2.getDrawable());
                            imageButton2.setPosition(imageButton1.getWidth() + 10, 0);
                            imageButton2.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 4.81;
                                    DpeEvent responseType = DpeEvent.FORME_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });

                            ImageButton imageButton3 = new ImageButton(image3.getDrawable());
                            imageButton3.setPosition(imageButton2.getX() + imageButton2.getWidth() + 10, 0);
                            imageButton3.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 5.71;
                                    DpeEvent responseType = DpeEvent.FORME_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });

                            dialog.setSize(largeur3images, hauteurImages);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            dialog.addActor(imageButton1);
                            dialog.addActor(imageButton2);
                            dialog.addActor(imageButton3);
                            break;
                        }

                        case MITOYENNETE: {
                            s.acquire();
                            Image image1 = new Image(textureMit1);
                            Image image2 = new Image(textureMit2);
                            Image image3 = new Image(textureMit3);
                            Image image4 = new Image(textureMit4);
                            Image image5 = new Image(textureMit5);
                            int largeurImages = textureMit1.getWidth() + textureMit2.getWidth() + textureMit3.getWidth() + textureMit4.getWidth() + textureMit5.getWidth() + 45;
                            int hauteurImages = textureMit1.getHeight() + 20;

                            Dialog dialog = new Dialog(" Mitoyennete avec un autre logement ", skin, "dialog") {
                                protected void result(Object object) {

                                }
                            }.show(stage);

                            ImageButton imageButton1 = new ImageButton(image1.getDrawable());
                            imageButton1.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 1;
                                    DpeEvent responseType = DpeEvent.MITOYENNETE_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });

                            ImageButton imageButton2 = new ImageButton(image2.getDrawable());
                            imageButton2.setPosition(imageButton1.getWidth() + 10, 0);
                            imageButton2.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 0.8;
                                    DpeEvent responseType = DpeEvent.MITOYENNETE_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });

                            ImageButton imageButton3 = new ImageButton(image3.getDrawable());
                            imageButton3.setPosition(imageButton2.getX() + imageButton2.getWidth() + 10, 0);
                            imageButton3.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 0.7;
                                    DpeEvent responseType = DpeEvent.MITOYENNETE_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });

                            ImageButton imageButton4 = new ImageButton(image4.getDrawable());
                            imageButton4.setPosition(imageButton3.getX() + imageButton3.getWidth() + 10, 0);
                            imageButton4.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 0.5;
                                    DpeEvent responseType = DpeEvent.MITOYENNETE_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });

                            ImageButton imageButton5 = new ImageButton(image5.getDrawable());
                            imageButton5.setPosition(imageButton4.getX() + imageButton4.getWidth() + 10, 0);
                            imageButton5.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 0.35;
                                    DpeEvent responseType = DpeEvent.MITOYENNETE_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });

                            dialog.setSize(largeurImages, hauteurImages);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            dialog.addActor(imageButton1);
                            dialog.addActor(imageButton2);
                            dialog.addActor(imageButton3);
                            dialog.addActor(imageButton4);
                            dialog.addActor(imageButton5);
                            break;
                        }
                        case POSITION_APPARTEMENT: {
                            s.acquire();
                            Image image1 = new Image(texturePosAppt1);
                            Image image2 = new Image(texturePosAppt2);
                            Image image3 = new Image(texturePosAppt3);
                            int largeur3images = texturePosAppt1.getWidth() + texturePosAppt2.getWidth() + texturePosAppt3.getWidth() + 25;
                            int hauteurImages = texturePosAppt1.getHeight() + 20;

                            final Dialog dialog = new Dialog(" Position de l'appartement ", skin, "dialog") {
                                protected void result(Object object) {

                                }
                            }.show(stage);

                            ImageButton imageButton1 = new ImageButton(image1.getDrawable());
                            imageButton1.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    String reponse = "bas";
                                    DpeEvent responseType = DpeEvent.POSITION_APPARTEMENT_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });

                            ImageButton imageButton2 = new ImageButton(image2.getDrawable());
                            imageButton2.setPosition(imageButton1.getWidth() + 10, 0);
                            imageButton2.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    String reponse = "int";
                                    DpeEvent responseType = DpeEvent.POSITION_APPARTEMENT_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });

                            ImageButton imageButton3 = new ImageButton(image3.getDrawable());
                            imageButton3.setPosition(imageButton2.getX() + imageButton2.getWidth() + 10, 0);
                            imageButton3.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    String reponse = "haut";
                                    DpeEvent responseType = DpeEvent.POSITION_APPARTEMENT_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });

                            dialog.setSize(largeur3images, hauteurImages);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            dialog.addActor(imageButton1);
                            dialog.addActor(imageButton2);
                            dialog.addActor(imageButton3);
                            break;
                        }

                        case ANNEE_CONSTRUCTION: {
                            s.acquire();
                            String initialText = (String) o;
                            final TextField textField = new TextField("", skin);
                            textField.setMessageText(initialText);
                            Dialog dialog = new Dialog(" Annee de construction du batiment ", skin, "dialog") {
                                protected void result(Object object) {
                                    String text = textField.getText();
                                    DpeEvent responseType = DpeEvent.ANNEE_CONSTRUCTION_RESPONSE;
                                    Event response = new Event(responseType, text);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            }.button("Valider").show(stage);
                            textField.setPosition((dialog.getWidth() - textField.getWidth()) / 2, 40);
                            dialog.setSize(dialog.getWidth() + 20, textField.getHeight() + dialog.getHeight() + 20);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            dialog.addActor(textField);
                            break;
                        }

                        case ENERGIE_CONSTRUCTION: {
                            s.acquire();
                            Dialog dialog = new Dialog(" Type d'energie a la construction ", skin, "dialog") {
                                protected void result (Object object) {
                                    String reponse;
                                    if ((int)object==1){
                                        reponse = "elec";
                                    }else{
                                        reponse = "autre";
                                    }
                                    DpeEvent responseType = DpeEvent.ENERGIE_CONSTRUCTION_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            }.button("Electrique", 1).button("Autre",2).show(stage);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            break;
                        }


                        case DERRIERE_MUR: {
                            s.acquire();
                            final IfcWallStandardCase wall = (IfcWallStandardCase)e.getUserObject();
                            Dialog dialog = new Dialog(" Qu'est-ce qu'il y a derriere ce mur : " , skin, "dialog") {
                                protected void result(Object object) {
                                    String derriere = "";
                                    if (object.equals(1)) {
                                        derriere = "ext";
                                    } else if (object.equals(2)) {
                                        derriere = "lnc";
                                    } else if (object.equals(3)) {
                                        derriere = "ah";
                                    } else if (object.equals(4)) {
                                        derriere = "ver";
                                    }
                                    DpeEvent responseType = DpeEvent.DERRIERE_MUR_RESPONSE;
                                    Object[]items = new Object[2];
                                    items[0]=wall;
                                    items[1]=derriere;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            }.button("Exterieur", 1).button("Local non chauffe", 2).button("Autre habitation", 3).button("Veranda", 4).show(stage);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            break;
                        }
                    }
                }
        } catch (InterruptedException ie) {

        }
    }

}
