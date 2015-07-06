package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.concurrent.Semaphore;

import fr.limsi.rorqual.core.dpe.TypeDoorEnum;
import fr.limsi.rorqual.core.dpe.TypeFenetreEnum;
import fr.limsi.rorqual.core.dpe.TypeMenuiserieFenetreEnum;
import fr.limsi.rorqual.core.dpe.TypeVitrageEnum;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventListener;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.event.EventType;
import fr.limsi.rorqual.core.utils.AssetManager;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDoor;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSlab;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWindow;

/**
 * Created by christophe on 03/06/15.
 */
public class DpeUi implements EventListener {

    private Stage stage;
    private AssetManager assets;

    public DpeUi(Stage stage2d) {
        EventManager.getInstance().addListener(Channel.DPE, this);
        stage = stage2d;
        assets = AssetManager.getInstance();
    }
    Semaphore s = new Semaphore(1,true);

    public void notify(Channel c, Event e) {
        try {
            EventType eventType = e.getEventType();
            if (c == Channel.DPE)
                if (eventType instanceof DpeEvent) {
                    DpeEvent event = (DpeEvent) eventType;
                    Object o = e.getUserObject();
                    Skin skin = (Skin)assets.get("uiskin");

                    switch (event) {
                        case START_DPE: {
                            s.acquire();
                            Texture textureStartDpe = (Texture)assets.get("textureStartDpe");
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
                            Texture textureTypeBatiment1 = (Texture)assets.get("textureTypeBatiment1");
                            Texture textureTypeBatiment2 = (Texture)assets.get("textureTypeBatiment2");
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
                            Texture textureNbNiveau1 = (Texture)assets.get("textureNbNiveau1");
                            Texture textureNbNiveau2 = (Texture)assets.get("textureNbNiveau2");
                            Texture textureNbNiveau3 = (Texture)assets.get("textureNbNiveau3");
                            Texture textureNbNiveau4 = (Texture)assets.get("textureNbNiveau4");
                            Texture textureNbNiveau5 = (Texture)assets.get("textureNbNiveau5");
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
                            Texture textureForme1 = (Texture)assets.get("textureForme1");
                            Texture textureForme2 = (Texture)assets.get("textureForme2");
                            Texture textureForme3 = (Texture)assets.get("textureForme3");
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
                            Texture textureMit1 = (Texture)assets.get("textureMit1");
                            Texture textureMit2 = (Texture)assets.get("textureMit2");
                            Texture textureMit3 = (Texture)assets.get("textureMit3");
                            Texture textureMit4 = (Texture)assets.get("textureMit4");
                            Texture textureMit5 = (Texture)assets.get("textureMit5");
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
                            Texture texturePosAppt1 = (Texture)assets.get("texturePosAppt1");
                            Texture texturePosAppt2 = (Texture)assets.get("texturePosAppt2");
                            Texture texturePosAppt3 = (Texture)assets.get("texturePosAppt3");
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
                            Dialog dialog = new Dialog(" Annee de construction du batiment ", skin, "dialog") {
                                protected void result(Object object) {

                                }
                            }.show(stage);

                            TextButton textButton1 = new TextButton("<1975",skin);
                            TextButton textButton2 = new TextButton("1975-1977",skin);
                            TextButton textButton3 = new TextButton("1978-1982",skin);
                            TextButton textButton4 = new TextButton("1983-1988",skin);
                            TextButton textButton5 = new TextButton("1989-2000",skin);
                            TextButton textButton6 = new TextButton("2001-2005",skin);
                            TextButton textButton7 = new TextButton("2006-2012",skin);
                            TextButton textButton8 = new TextButton(">2012",skin);
                            float largeur = textButton1.getWidth()+textButton2.getWidth()+textButton3.getWidth()+textButton4.getWidth()+45;
                            float hauteur = textButton1.getHeight()+textButton5.getHeight()+25;

                            dialog.setSize(largeur, hauteur);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            dialog.addActor(textButton1);
                            dialog.addActor(textButton2);
                            dialog.addActor(textButton3);
                            dialog.addActor(textButton4);
                            dialog.addActor(textButton5);
                            dialog.addActor(textButton6);
                            dialog.addActor(textButton7);
                            dialog.addActor(textButton8);

                            textButton1.setPosition(4, textButton5.getHeight() + 4);
                            textButton2.setPosition(textButton1.getX()+textButton1.getWidth()+10,textButton5.getHeight()+4);
                            textButton3.setPosition(textButton2.getX()+textButton2.getWidth()+10,textButton5.getHeight()+4);
                            textButton4.setPosition(textButton3.getX()+textButton3.getWidth()+10,textButton5.getHeight()+4);
                            textButton5.setPosition(4,4);
                            textButton6.setPosition(textButton5.getX()+textButton5.getWidth()+10,4);
                            textButton7.setPosition(textButton6.getX()+textButton6.getWidth()+10,4);
                            textButton8.setPosition(textButton7.getX()+textButton7.getWidth()+10,4);

                            textButton1.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 1970;
                                    DpeEvent responseType = DpeEvent.ANNEE_CONSTRUCTION_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });
                            textButton2.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 1976;
                                    DpeEvent responseType = DpeEvent.ANNEE_CONSTRUCTION_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });
                            textButton3.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 1980;
                                    DpeEvent responseType = DpeEvent.ANNEE_CONSTRUCTION_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });
                            textButton4.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 1985;
                                    DpeEvent responseType = DpeEvent.ANNEE_CONSTRUCTION_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });
                            textButton5.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 1995;
                                    DpeEvent responseType = DpeEvent.ANNEE_CONSTRUCTION_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });
                            textButton6.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 2002;
                                    DpeEvent responseType = DpeEvent.ANNEE_CONSTRUCTION_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });
                            textButton7.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 2008;
                                    DpeEvent responseType = DpeEvent.ANNEE_CONSTRUCTION_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });
                            textButton8.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 2014;
                                    DpeEvent responseType = DpeEvent.ANNEE_CONSTRUCTION_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            });
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
                            Dialog dialog = new Dialog(" Qu'est-ce qu'il y a derriere ce mur ? " , skin, "dialog") {
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
                                    Object[] items = new Object[2];
                                    items[0]=wall;
                                    items[1]=derriere;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            }.button("Exterieur", 1).button("Local non chauffe", 2).button("Autre habitation", 3).button("Veranda", 4).show(stage);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            /*Popup popup = new Popup(skin, MainApplicationAdapter.getFromUserObject(wall));
                            popup.setTitle(" Qu'est-ce qu'il y a derriere ce mur ? ");
                            popup.getButtonTable().addActor(new TextButton("Exterieur", skin));
                            popup.getButtonTable().addActor(new TextButton("Local non chauffe", skin));
                            popup.getButtonTable().addActor(new TextButton("Autre habitation", skin));
                            popup.getButtonTable().addActor(new TextButton("Veranda", skin));
                            stage.addActor(popup);*/
                            break;
                        }

                        case ISOLATION_MUR: {
                            s.acquire();
                            final Object wall = e.getUserObject();
                            Dialog dialog = new Dialog(" Ce mur est-il isole ? ", skin, "dialog") {
                                protected void result (Object object) {
                                    String isole="";
                                    if(object.equals(1)){
                                        isole="non";
                                    }
                                    else if (object.equals(2)) {
                                        isole="oui";
                                    }
                                    else if (object.equals(3)) {
                                        isole="inconnue";
                                    }
                                    DpeEvent responseType = DpeEvent.ISOLATION_MUR_RESPONSE;
                                    Object[] items = new Object[2];
                                    items[0]=wall;
                                    items[1]=isole;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            }.button("Non", 1).button("Oui", 2).button("Inconnue", 3).show(stage);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            break;
                        }

                        case DATE_ISOLATION_MUR:{
                            s.acquire();
                            final Object wall = e.getUserObject();
                            final Dialog dialog = new Dialog(" En quelle annee ce mur a-t'il ete isole ? ", skin, "dialog") {
                                protected void result(Object object) {

                                }
                            }.show(stage);

                            TextButton textButton1 = new TextButton("A la construction",skin);
                            TextButton textButton2 = new TextButton("Inconnue",skin);
                            TextButton textButton3 = new TextButton("<1983",skin);
                            TextButton textButton4 = new TextButton("1983-1988",skin);
                            TextButton textButton5 = new TextButton("1989-2000",skin);
                            TextButton textButton6 = new TextButton("2001-2005",skin);
                            TextButton textButton7 = new TextButton("2006-2012",skin);
                            TextButton textButton8 = new TextButton(">2012",skin);
                            float largeur = textButton1.getWidth()+textButton2.getWidth()+textButton3.getWidth()+textButton4.getWidth()+45;
                            float hauteur = textButton1.getHeight()+textButton5.getHeight()+25;

                            dialog.setSize(largeur, hauteur);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            dialog.addActor(textButton1);
                            dialog.addActor(textButton2);
                            dialog.addActor(textButton3);
                            dialog.addActor(textButton4);
                            dialog.addActor(textButton5);
                            dialog.addActor(textButton6);
                            dialog.addActor(textButton7);
                            dialog.addActor(textButton8);

                            textButton1.setPosition(4, textButton5.getHeight() + 4);
                            textButton2.setPosition(textButton1.getX() + textButton1.getWidth() + 10, textButton5.getHeight() + 4);
                            textButton3.setPosition(textButton2.getX() + textButton2.getWidth() + 10, textButton5.getHeight() + 4);
                            textButton4.setPosition(textButton3.getX() + textButton3.getWidth() + 10, textButton5.getHeight() + 4);
                            textButton5.setPosition(24, 4);
                            textButton6.setPosition(textButton5.getX() + textButton5.getWidth() + 10, 4);
                            textButton7.setPosition(textButton6.getX() + textButton6.getWidth() + 10, 4);
                            textButton8.setPosition(textButton7.getX() + textButton7.getWidth() + 10, 4);

                            textButton1.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = -1;
                                    Object[] items = new Object[2];
                                    items[0] = wall;
                                    Object[] r = {true, reponse};
                                    items[1] = r;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton2.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = -2;
                                    Object[] items = new Object[2];
                                    items[0] = wall;
                                    Object[] r = {false, reponse};
                                    items[1] = r;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton3.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 1980;
                                    Object[] items = new Object[2];
                                    items[0] = wall;
                                    Object[] r = {true, reponse};
                                    items[1] = r;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton4.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 1985;
                                    Object[] items = new Object[2];
                                    items[0] = wall;
                                    Object[] r = {true, reponse};
                                    items[1] = r;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton5.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 1995;
                                    Object[] items = new Object[2];
                                    items[0] = wall;
                                    Object[] r = {true, reponse};
                                    items[1] = r;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton6.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 2002;
                                    Object[] items = new Object[2];
                                    items[0] = wall;
                                    Object[] r = {true, reponse};
                                    items[1] = r;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton7.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 2008;
                                    Object[] items = new Object[2];
                                    items[0] = wall;
                                    Object[] r = {true, reponse};
                                    items[1] = r;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton8.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 2014;
                                    Object[] items = new Object[2];
                                    items[0] = wall;
                                    Object[] r = {true, reponse};
                                    items[1] = r;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            break;
                        }

                        case DERRIERE_SLAB : {
                            s.acquire();
                            final IfcSlab slab = (IfcSlab)e.getUserObject();
                            Dialog dialog = new Dialog(" Qu'est-ce qu'il y a sous ce plancher ? " , skin, "dialog") {
                                protected void result(Object object) {
                                    String derriere = "";
                                    if (object.equals(1)) {
                                        derriere = "ah";
                                    } else if (object.equals(2)) {
                                        derriere = "ss";
                                    } else if (object.equals(3)) {
                                        derriere = "vs";
                                    } else if (object.equals(4)) {
                                        derriere = "tp";
                                    }
                                    DpeEvent responseType = DpeEvent.DERRIERE_SLAB_RESPONSE;
                                    Object[] items = new Object[2];
                                    items[0]=slab;
                                    items[1]=derriere;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            }.button("Autre habitation", 1).button("Sous-sol", 2).button("Vide-Sanitaire", 3).button("Terre plein", 4).show(stage);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            break;
                        }

                        case ISOLATION_SLAB: {
                            s.acquire();
                            final Object slab = e.getUserObject();
                            Dialog dialog = new Dialog(" Ce plancher est-il isole ? ", skin, "dialog") {
                                protected void result (Object object) {
                                    String isole="";
                                    if(object.equals(1)){
                                        isole="non";
                                    }
                                    else if (object.equals(2)) {
                                        isole="oui";
                                    }
                                    else if (object.equals(3)) {
                                        isole="inconnue";
                                    }
                                    DpeEvent responseType = DpeEvent.ISOLATION_SLAB_RESPONSE;
                                    Object[] items = new Object[2];
                                    items[0]=slab;
                                    items[1]=isole;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            }.button("Non", 1).button("Oui", 2).button("Inconnue", 3).show(stage);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            break;
                        }

                        case DATE_ISOLATION_SLAB:{
                            s.acquire();
                            final Object slab = e.getUserObject();
                            final Dialog dialog = new Dialog(" En quelle annee ce plancher a-t'il ete isole ? ", skin, "dialog") {
                                protected void result(Object object) {

                                }
                            }.show(stage);

                            TextButton textButton1 = new TextButton("A la construction",skin);
                            TextButton textButton2 = new TextButton("Inconnue",skin);
                            TextButton textButton3 = new TextButton("<1983",skin);
                            TextButton textButton4 = new TextButton("1983-1988",skin);
                            TextButton textButton5 = new TextButton("1989-2000",skin);
                            TextButton textButton6 = new TextButton("2001-2005",skin);
                            TextButton textButton7 = new TextButton("2006-2012",skin);
                            TextButton textButton8 = new TextButton(">2012",skin);
                            float largeur = textButton1.getWidth()+textButton2.getWidth()+textButton3.getWidth()+textButton4.getWidth()+45;
                            float hauteur = textButton1.getHeight()+textButton5.getHeight()+25;

                            dialog.setSize(largeur, hauteur);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            dialog.addActor(textButton1);
                            dialog.addActor(textButton2);
                            dialog.addActor(textButton3);
                            dialog.addActor(textButton4);
                            dialog.addActor(textButton5);
                            dialog.addActor(textButton6);
                            dialog.addActor(textButton7);
                            dialog.addActor(textButton8);

                            textButton1.setPosition(4, textButton5.getHeight() + 4);
                            textButton2.setPosition(textButton1.getX() + textButton1.getWidth() + 10, textButton5.getHeight() + 4);
                            textButton3.setPosition(textButton2.getX() + textButton2.getWidth() + 10, textButton5.getHeight() + 4);
                            textButton4.setPosition(textButton3.getX() + textButton3.getWidth() + 10, textButton5.getHeight() + 4);
                            textButton5.setPosition(24, 4);
                            textButton6.setPosition(textButton5.getX() + textButton5.getWidth() + 10, 4);
                            textButton7.setPosition(textButton6.getX() + textButton6.getWidth() + 10, 4);
                            textButton8.setPosition(textButton7.getX() + textButton7.getWidth() + 10, 4);

                            textButton1.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = -1;
                                    Object[] items = new Object[2];
                                    items[0] = slab;
                                    Object[] r = {true, reponse};
                                    items[1] = r;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_SLAB_RESPONSE;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton2.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = -2;
                                    Object[] items = new Object[2];
                                    items[0] = slab;
                                    Object[] r = {false, reponse};
                                    items[1] = r;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_SLAB_RESPONSE;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton3.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 1980;
                                    Object[] items = new Object[2];
                                    items[0] = slab;
                                    Object[] r = {true, reponse};
                                    items[1] = r;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_SLAB_RESPONSE;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton4.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 1985;
                                    Object[] items = new Object[2];
                                    items[0] = slab;
                                    Object[] r = {true, reponse};
                                    items[1] = r;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_SLAB_RESPONSE;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton5.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 1995;
                                    Object[] items = new Object[2];
                                    items[0] = slab;
                                    Object[] r = {true, reponse};
                                    items[1] = r;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_SLAB_RESPONSE;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton6.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 2002;
                                    Object[] items = new Object[2];
                                    items[0] = slab;
                                    Object[] r = {true, reponse};
                                    items[1] = r;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_SLAB_RESPONSE;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton7.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 2008;
                                    Object[] items = new Object[2];
                                    items[0] = slab;
                                    Object[] r = {true, reponse};
                                    items[1] = r;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_SLAB_RESPONSE;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton8.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 2014;
                                    Object[] items = new Object[2];
                                    items[0] = slab;
                                    Object[] r = {true, reponse};
                                    items[1] = r;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_SLAB_RESPONSE;
                                    Event response = new Event(responseType, items);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            break;
                        }
                        case TYPE_FENETRE: {
                            s.acquire();
                            final IfcWindow window = (IfcWindow)e.getUserObject();
                            Texture windowBattante = (Texture) AssetManager.getInstance().get("textureWindowTypeBattante");
                            Texture windowCoulissante = (Texture) AssetManager.getInstance().get("textureWindowTypeCoulissante");
                            Image image1 = new Image(windowBattante);
                            Image image2 = new Image(windowCoulissante);
                            final Dialog dialog = new Dialog(" Type de fenetre ", skin, "dialog") {
                                protected void result (Object object) {

                                }
                            }.show(stage);
                            ImageButton imageButton1 = new ImageButton(image1.getDrawable());
                            imageButton1.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = window;
                                    item[1] = TypeFenetreEnum.BATTANTE;
                                    DpeEvent responseType = DpeEvent.TYPE_FENETRE_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            ImageButton imageButton2 = new ImageButton(image2.getDrawable());
                            imageButton2.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = window;
                                    item[1] = TypeFenetreEnum.COULISSANTE;
                                    DpeEvent responseType = DpeEvent.TYPE_FENETRE_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            dialog.getContentTable().add(imageButton1).pad(10);
                            dialog.getContentTable().add(imageButton2).pad(10);
                            dialog.setSize(image1.getWidth() + image2.getWidth() + 60, Math.max(image1.getHeight(), image2.getHeight()) + 60);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            break;
                        }
                        case TYPE_MENUISERIE_FENETRE:{
                            s.acquire();
                            final IfcWindow window = (IfcWindow)e.getUserObject();
                            Texture materiauBois = (Texture) AssetManager.getInstance().get("textureWindowMateriauBois");
                            Texture materiauMetallique = (Texture) AssetManager.getInstance().get("textureWindowMateriauMetallique");
                            Texture materiauPvc = (Texture) AssetManager.getInstance().get("textureWindowMateriauPvc");
                            Image image1 = new Image(materiauBois);
                            Image image2 = new Image(materiauMetallique);
                            Image image3 = new Image(materiauPvc);
                            final Dialog dialog = new Dialog(" Type de fenetre ", skin, "dialog") {
                                protected void result (Object object) {

                                }
                            }.show(stage);
                            ImageButton imageButton1 = new ImageButton(image1.getDrawable());
                            imageButton1.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = window;
                                    item[1] = TypeMenuiserieFenetreEnum.BOIS;
                                    DpeEvent responseType = DpeEvent.TYPE_MENUISERIE_FENETRE_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            ImageButton imageButton2 = new ImageButton(image2.getDrawable());
                            imageButton2.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = window;
                                    item[1] = TypeMenuiserieFenetreEnum.METALLIQUE;
                                    DpeEvent responseType = DpeEvent.TYPE_MENUISERIE_FENETRE_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            ImageButton imageButton3 = new ImageButton(image3.getDrawable());
                            imageButton3.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = window;
                                    item[1] = TypeMenuiserieFenetreEnum.PVC;
                                    DpeEvent responseType = DpeEvent.TYPE_MENUISERIE_FENETRE_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            dialog.getContentTable().add(imageButton1).pad(10);
                            dialog.getContentTable().add(imageButton2).pad(10);
                            dialog.getContentTable().add(imageButton3).pad(10);
                            dialog.setSize(image1.getWidth() + image2.getWidth() + image3.getWidth() + 80, image1.getHeight() + 60);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            break;
                        }
                        case TYPE_VITRAGE_FENETRE: {
                            s.acquire();
                            final IfcWindow window = (IfcWindow)e.getUserObject();
                            final Dialog dialog = new Dialog(" Type de fenetre ", skin, "dialog") {
                                protected void result (Object object) {

                                }
                            }.show(stage);
                            TextButton textButton1 = new TextButton("Simple vitrage",skin);
                            textButton1.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = window;
                                    item[1] = TypeVitrageEnum.SIMPLE_VITRAGE;
                                    DpeEvent responseType = DpeEvent.TYPE_VITRAGE_FENETRE_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton2 = new TextButton("Survitrage",skin);
                            textButton2.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = window;
                                    item[1] = TypeVitrageEnum.SURVITRAGE;
                                    DpeEvent responseType = DpeEvent.TYPE_VITRAGE_FENETRE_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton3 = new TextButton("Double vitrage < 1990",skin);
                            textButton3.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = window;
                                    item[1] = TypeVitrageEnum.DOUBLE_VITRAGE_INF_1990;
                                    DpeEvent responseType = DpeEvent.TYPE_VITRAGE_FENETRE_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton4 = new TextButton("1990 < Double vitrage < 2001",skin);
                            textButton4.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = window;
                                    item[1] = TypeVitrageEnum.DOUBLE_VITRAGE_SUP_1990_INF_2001;
                                    DpeEvent responseType = DpeEvent.TYPE_VITRAGE_FENETRE_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton5 = new TextButton("Double vitrage > 2001",skin);
                            textButton5.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = window;
                                    item[1] = TypeVitrageEnum.DOUBLE_VITRAGE_SUP_2001;
                                    DpeEvent responseType = DpeEvent.TYPE_VITRAGE_FENETRE_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton6 = new TextButton("Triple Vitrage",skin);
                            textButton6.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = window;
                                    item[1] = TypeVitrageEnum.TRIPLE_VITRAGE;
                                    DpeEvent responseType = DpeEvent.TYPE_VITRAGE_FENETRE_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            dialog.getContentTable().add(textButton1).pad(2);
                            dialog.getContentTable().add(textButton2).pad(2);
                            dialog.getContentTable().add(textButton3).pad(2);
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton4).pad(2);
                            dialog.getContentTable().add(textButton5).pad(2);
                            dialog.getContentTable().add(textButton6).pad(2);
                            dialog.setSize(textButton3.getWidth() * 3 + 120, textButton1.getHeight() * 2 + 60);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            break;
                        }
                        case TYPE_DOOR: {
                            s.acquire();
                            final IfcDoor door = (IfcDoor)e.getUserObject();
                            final Dialog dialog = new Dialog(" Type de porte ", skin, "dialog") {
                                protected void result (Object object) {

                                        }
                            }.show(stage);
                            TextButton textButton1 = new TextButton("Porte opaque pleine",skin);
                            textButton1.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = door;
                                    item[1] = TypeDoorEnum.PORTE_OPAQUE_PLEINE;
                                    DpeEvent responseType = DpeEvent.TYPE_DOOR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton2 = new TextButton("Porte avec moins de 30% de simple vitrage",skin);
                            textButton2.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = door;
                                    item[1] = TypeDoorEnum.PORTE_AVEC_MOIS_DE_30_POURCENT_DE_SIMPLE_VITRAGE;
                                    DpeEvent responseType = DpeEvent.TYPE_DOOR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton3 = new TextButton("Porte avec 30-60% de simple vitrage",skin);
                            textButton3.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = door;
                                    item[1] = TypeDoorEnum.PORTE_AVEC_30_60_POURCENT_DE_SIMPLE_VITRAGE;
                                    DpeEvent responseType = DpeEvent.TYPE_DOOR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton4 = new TextButton("Porte avec double vitrage",skin);
                            textButton4.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = door;
                                    item[1] = TypeDoorEnum.PORTE_AVEC_DOUBLE_VITRAGE;
                                    DpeEvent responseType = DpeEvent.TYPE_DOOR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton5 = new TextButton("Porte opaque pleine isolee",skin);
                            textButton5.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = door;
                                    item[1] = TypeDoorEnum.PORTE_OPAQUE_PLEINE_ISOLEE;
                                    DpeEvent responseType = DpeEvent.TYPE_DOOR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton6 = new TextButton("Porte precedee d'un SAS",skin);
                            textButton6.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = door;
                                    item[1] = TypeDoorEnum.PORTE_PRECEDE_DUN_SAS;
                                    DpeEvent responseType = DpeEvent.TYPE_DOOR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            dialog.getContentTable().add(textButton1).pad(2);
                            dialog.getContentTable().add(textButton2).pad(2);
                            dialog.getContentTable().add(textButton3).pad(2);
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton4).pad(2);
                            dialog.getContentTable().add(textButton5).pad(2);
                            dialog.getContentTable().add(textButton6).pad(2);
                            dialog.setSize(textButton3.getWidth() * 3 + 120, textButton1.getHeight() * 2 + 60);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            break;
                        }
                    }
                }
        } catch (InterruptedException ie) {

        }
    }

}
