package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.concurrent.Semaphore;

import fr.limsi.rorqual.core.dpe.PositionAppartementEnum;
import fr.limsi.rorqual.core.dpe.TypeBatimentEnum;
import fr.limsi.rorqual.core.dpe.TypeDoorEnum;
import fr.limsi.rorqual.core.dpe.TypeEnergieConstructionEnum;
import fr.limsi.rorqual.core.dpe.TypeFenetreEnum;
import fr.limsi.rorqual.core.dpe.TypeMateriauMenuiserieEnum;
import fr.limsi.rorqual.core.dpe.TypeVitrageEnum;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventListener;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.event.EventType;
import fr.limsi.rorqual.core.utils.AssetManager;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDoor;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProduct;
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

    public Actor getPropertyWindow(Object o) {
        if (o instanceof IfcWallStandardCase) {
            Actor a = Layout.fromJson("data/ui/layout/wallProperties.json").getRoot();
            a.setUserObject(o);
            return a;
        }
        return null;
    }

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
                            int hauteurImage = textureStartDpe.getHeight();
                            final Dialog dialog = new Dialog(" Objectif du DPE ", skin, "dialog") {
                                protected void result(Object object) {
                                    DpeEvent responseType = DpeEvent.START_DPE_RESPONSE;
                                    Event response = new Event(responseType, (boolean) object);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            }.button("Annuler", false).button("Commencer le Dpe", true).show(stage);
                            dialog.getContentTable().add(image).pad(2);
                            dialog.setSize(largeurImage, hauteurImage + dialog.getHeight());
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            break;
                        }

                        case TYPE_BATIMENT: {
                            s.acquire();
                            Texture textureTypeBatiment1 = (Texture)assets.get("textureTypeBatiment1");
                            Texture textureTypeBatiment2 = (Texture)assets.get("textureTypeBatiment2");
                            Image image1 = new Image(textureTypeBatiment1);
                            Image image2 = new Image(textureTypeBatiment2);
                            final Dialog dialog = new Dialog(" Type de batiment ", skin, "dialog") {
                                protected void result(Object object) {

                                }
                            }.show(stage);

                            ImageButton imageButton1 = new ImageButton(image1.getDrawable());
                            imageButton1.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    TypeBatimentEnum typeBatiment = TypeBatimentEnum.MAISON;
                                    DpeEvent responseType = DpeEvent.TYPE_BATIMENT_RESPONSE;
                                    Event response = new Event(responseType, typeBatiment);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });

                            ImageButton imageButton2 = new ImageButton(image2.getDrawable());
                            imageButton2.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    TypeBatimentEnum typeBatiment = TypeBatimentEnum.APPARTEMENT;
                                    DpeEvent responseType = DpeEvent.TYPE_BATIMENT_RESPONSE;
                                    Event response = new Event(responseType, typeBatiment);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            dialog.getContentTable().add(imageButton1).pad(2);
                            dialog.getContentTable().add(imageButton2).pad(2);
                            dialog.setSize(imageButton1.getWidth() + imageButton2.getWidth() + 40, imageButton1.getHeight() + 34);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
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

                            final Dialog dialog = new Dialog(" Nombre de niveaux ", skin, "dialog") {
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
                                    dialog.remove();
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
                                    dialog.remove();
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
                                    dialog.remove();
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
                                    dialog.remove();
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
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            dialog.getContentTable().add(imageButton1).pad(2);
                            dialog.getContentTable().add(imageButton2).pad(2);
                            dialog.getContentTable().add(imageButton3).pad(2);
                            dialog.getContentTable().add(imageButton4).pad(2);
                            dialog.getContentTable().add(imageButton5).pad(2);
                            dialog.setSize(largeurImages + 20, hauteurImages + 20);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
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
                            final Dialog dialog = new Dialog(" Forme de la maison ", skin, "dialog") {
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
                                    dialog.remove();
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
                                    dialog.remove();
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
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            dialog.getContentTable().add(imageButton1).pad(2);
                            dialog.getContentTable().add(imageButton2).pad(2);
                            dialog.getContentTable().add(imageButton3).pad(2);
                            dialog.setSize(largeur3images + 20, hauteurImages + 20);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
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

                            final Dialog dialog = new Dialog(" Mitoyennete avec un autre logement ", skin, "dialog") {
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
                                    dialog.remove();
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
                                    dialog.remove();
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
                                    dialog.remove();
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
                                    dialog.remove();
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
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            dialog.getContentTable().add(imageButton1).pad(2);
                            dialog.getContentTable().add(imageButton2).pad(2);
                            dialog.getContentTable().add(imageButton3).pad(2);
                            dialog.getContentTable().add(imageButton4).pad(2);
                            dialog.getContentTable().add(imageButton5).pad(2);
                            dialog.setSize(largeurImages + 20, hauteurImages + 20);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
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
                                    PositionAppartementEnum positionAppartement = PositionAppartementEnum.PREMIER_ETAGE;
                                    DpeEvent responseType = DpeEvent.POSITION_APPARTEMENT_RESPONSE;
                                    Event response = new Event(responseType, positionAppartement);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });

                            ImageButton imageButton2 = new ImageButton(image2.getDrawable());
                            imageButton2.setPosition(imageButton1.getWidth() + 10, 0);
                            imageButton2.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    PositionAppartementEnum positionAppartement = PositionAppartementEnum.ETAGE_INTERMEDIAIRE;
                                    DpeEvent responseType = DpeEvent.POSITION_APPARTEMENT_RESPONSE;
                                    Event response = new Event(responseType, positionAppartement);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });

                            ImageButton imageButton3 = new ImageButton(image3.getDrawable());
                            imageButton3.setPosition(imageButton2.getX() + imageButton2.getWidth() + 10, 0);
                            imageButton3.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    PositionAppartementEnum positionAppartement = PositionAppartementEnum.DERNIER_ETAGE;
                                    DpeEvent responseType = DpeEvent.POSITION_APPARTEMENT_RESPONSE;
                                    Event response = new Event(responseType, positionAppartement);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            dialog.getContentTable().add(imageButton1).pad(2);
                            dialog.getContentTable().add(imageButton2).pad(2);
                            dialog.getContentTable().add(imageButton3).pad(2);
                            dialog.setSize(largeur3images + 20, hauteurImages + 20);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            break;
                        }

                        case ANNEE_CONSTRUCTION: {
                            s.acquire();
                            final Dialog dialog = new Dialog(" Annee de construction du batiment ", skin, "dialog") {
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

                            textButton1.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 1970;
                                    DpeEvent responseType = DpeEvent.ANNEE_CONSTRUCTION_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton2.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 1976;
                                    DpeEvent responseType = DpeEvent.ANNEE_CONSTRUCTION_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton3.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 1980;
                                    DpeEvent responseType = DpeEvent.ANNEE_CONSTRUCTION_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton4.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 1985;
                                    DpeEvent responseType = DpeEvent.ANNEE_CONSTRUCTION_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton5.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 1995;
                                    DpeEvent responseType = DpeEvent.ANNEE_CONSTRUCTION_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton6.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 2002;
                                    DpeEvent responseType = DpeEvent.ANNEE_CONSTRUCTION_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton7.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 2008;
                                    DpeEvent responseType = DpeEvent.ANNEE_CONSTRUCTION_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            textButton8.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    double reponse = 2014;
                                    DpeEvent responseType = DpeEvent.ANNEE_CONSTRUCTION_RESPONSE;
                                    Event response = new Event(responseType, reponse);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            dialog.getContentTable().add(textButton1).pad(2);
                            dialog.getContentTable().add(textButton2).pad(2);
                            dialog.getContentTable().add(textButton3).pad(2);
                            dialog.getContentTable().add(textButton4).pad(2);
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton5).pad(2);
                            dialog.getContentTable().add(textButton6).pad(2);
                            dialog.getContentTable().add(textButton7).pad(2);
                            dialog.getContentTable().add(textButton8).pad(2);
                            dialog.setSize(largeur + 40, hauteur + 20);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            break;
                        }

                        case ENERGIE_CONSTRUCTION: {
                            s.acquire();
                            Dialog dialog = new Dialog(" Type d'energie a la construction ", skin, "dialog") {
                                protected void result (Object object) {
                                    TypeEnergieConstructionEnum typeEnergieConstruction;
                                    if ((int)object==1){
                                        typeEnergieConstruction=TypeEnergieConstructionEnum.ELECTRIQUE;
                                    }else{
                                        typeEnergieConstruction=TypeEnergieConstructionEnum.AUTRE;
                                    }
                                    DpeEvent responseType = DpeEvent.ENERGIE_CONSTRUCTION_RESPONSE;
                                    Event response = new Event(responseType, typeEnergieConstruction);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    s.release();
                                }
                            }.button("Electrique", 1).button("Autre",2).show(stage);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            break;
                        }

                        case TYPE_MUR: {/*
                            s.acquire();
                            final IfcWallStandardCase wall = (IfcWallStandardCase)e.getUserObject();
                            final Dialog dialog = new Dialog(" Type de mur " , skin, "dialog") {
                                protected void result(Object object) {

                                }
                            }.show(stage);
                            TextButton textButton1 = new TextButton("Mur donnant sur un local non chauffe",skin);
                            textButton1.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = wall;
                                    item[1] = TypeMurEnum.MUR_DONNANT_SUR_UN_LOCAL_NON_CHAUFFE;
                                    DpeEvent responseType = DpeEvent.TYPE_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton2 = new TextButton("Mur donnant sur l'exterieur",skin);
                            textButton2.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = wall;
                                    item[1] = TypeMurEnum.MUR_DONNANT_SUR_EXTERIEUR;
                                    DpeEvent responseType = DpeEvent.TYPE_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton3 = new TextButton("Mur donnant sur une autre habitation",skin);
                            textButton3.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = wall;
                                    item[1] = TypeMurEnum.MUR_DONNANT_SUR_UNE_AUTRE_HABITATION;
                                    DpeEvent responseType = DpeEvent.TYPE_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton4 = new TextButton("Mur donnant sur une veranda chauffee",skin);
                            textButton4.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = wall;
                                    item[1] = TypeMurEnum.MUR_DONNANT_SUR_UNE_VERANDA_CHAUFFE;
                                    DpeEvent responseType = DpeEvent.TYPE_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton5 = new TextButton("Mur donnant sur une veranda non chauffee",skin);
                            textButton5.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = wall;
                                    item[1] = TypeMurEnum.MUR_DONNANT_SUR_UNE_VERANDA_NON_CHAUFFE;
                                    DpeEvent responseType = DpeEvent.TYPE_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton6 = new TextButton("Mur interieur",skin);
                            textButton6.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = wall;
                                    item[1] = TypeMurEnum.MUR_INTERIEUR;
                                    DpeEvent responseType = DpeEvent.TYPE_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            dialog.getContentTable().add(textButton1).pad(2).padTop(10).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton2).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton3).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton4).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton5).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton6).pad(2).left();
                            dialog.setSize(textButton5.getWidth() + 20, textButton1.getHeight() * 6 + 92);
                            dialog.setPosition(10, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));*/
                            break;
                        }

                        case DATE_ISOLATION_MUR: {/*
                            s.acquire();
                            final IfcWallStandardCase wall = (IfcWallStandardCase)e.getUserObject();
                            final Dialog dialog = new Dialog(" Date de l'isolation du mur " , skin, "dialog") {
                                protected void result(Object object) {

                                }
                            }.show(stage);
                            TextButton textButton1 = new TextButton("Jamais",skin);
                            textButton1.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = wall;
                                    item[1] = DateIsolationMurEnum.JAMAIS;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton2 = new TextButton("A la construction",skin);
                            textButton2.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = wall;
                                    item[1] = DateIsolationMurEnum.A_LA_CONSTRUCTION;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton3 = new TextButton("En renovation (date inconnue)",skin);
                            textButton3.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = wall;
                                    item[1] = DateIsolationMurEnum.EN_RENOVATION_DATE_INCONNUE;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton4 = new TextButton("En renovation avant 1983",skin);
                            textButton4.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = wall;
                                    item[1] = DateIsolationMurEnum.EN_RENOVATION_AVANT_1983;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton5 = new TextButton("En renovation entre 1983 et 1988",skin);
                            textButton5.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = wall;
                                    item[1] = DateIsolationMurEnum.EN_RENOVATION_ENTRE_1983_ET_1988;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton6 = new TextButton("En renovation entre 1989 et 2000",skin);
                            textButton6.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = wall;
                                    item[1] = DateIsolationMurEnum.EN_RENOVATION_ENTRE_1989_ET_2000;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton7 = new TextButton("En renovation entre 2001 et 2005",skin);
                            textButton7.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = wall;
                                    item[1] = DateIsolationMurEnum.EN_RENOVATION_ENTRE_2001_ET_2005;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton8 = new TextButton("En renovation entre 2006 et 2012",skin);
                            textButton8.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = wall;
                                    item[1] = DateIsolationMurEnum.EN_RENOVATION_ENTRE_2006_ET_2012;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton9 = new TextButton("En renovation apres 2012",skin);
                            textButton9.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = wall;
                                    item[1] = DateIsolationMurEnum.EN_RENOVATION_APRES_2012;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton10 = new TextButton("Inconnue",skin);
                            textButton10.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = wall;
                                    item[1] = DateIsolationMurEnum.INCONNUE;
                                    DpeEvent responseType = DpeEvent.DATE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            dialog.getContentTable().add(textButton1).pad(2).padTop(10).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton2).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton3).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton4).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton5).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton6).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton7).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton8).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton9).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton10).pad(2).left();
                            dialog.setSize(textButton5.getWidth() + 20, textButton1.getHeight() * 10 + 120);
                            dialog.setPosition(10, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));*/
                            break;
                        }

                        case TYPE_ISOLATION_MUR: {/*

                            s.acquire();
                            final IfcWallStandardCase wall = (IfcWallStandardCase)e.getUserObject();
                            final Dialog dialog = new Dialog(" Type d'isolation thermique " , skin, "dialog") {
                                protected void result(Object object) {

                                }
                            }.show(stage);
                            TextButton textButton1 = new TextButton("Interieure (ITI)",skin);
                            textButton1.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = wall;
                                    item[1] = TypeIsolationMurEnum.ITI;
                                    DpeEvent responseType = DpeEvent.TYPE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton2 = new TextButton("Exterieure (ITE)",skin);
                            textButton2.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = wall;
                                    item[1] = TypeIsolationMurEnum.ITE;
                                    DpeEvent responseType = DpeEvent.TYPE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton3 = new TextButton("Repartie (ITR)",skin);
                            textButton3.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = wall;
                                    item[1] = TypeIsolationMurEnum.ITR;
                                    DpeEvent responseType = DpeEvent.TYPE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton4 = new TextButton("Interieure + Exterieure (ITI+ITE)",skin);
                            textButton4.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = wall;
                                    item[1] = TypeIsolationMurEnum.ITI_PLUS_ITE;
                                    DpeEvent responseType = DpeEvent.TYPE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton5 = new TextButton("Interieure + Repartie (ITI+ITR)",skin);
                            textButton5.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = wall;
                                    item[1] = TypeIsolationMurEnum.ITI_PLUS_ITR;
                                    DpeEvent responseType = DpeEvent.TYPE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton6 = new TextButton("Exterieure + Repartie (ITE+ITR)",skin);
                            textButton6.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = wall;
                                    item[1] = TypeIsolationMurEnum.ITE_PLUS_ITR;
                                    DpeEvent responseType = DpeEvent.TYPE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton7 = new TextButton("Inconnue",skin);
                            textButton7.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = wall;
                                    item[1] = TypeIsolationMurEnum.INCONNUE;
                                    DpeEvent responseType = DpeEvent.TYPE_ISOLATION_MUR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            dialog.getContentTable().add(textButton1).pad(2).padTop(10).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton2).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton3).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton4).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton5).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton6).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton7).pad(2).left();
                            dialog.setSize(textButton4.getWidth() + 20, textButton1.getHeight() * 7 + 100);
                            dialog.setPosition(10, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));*/
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
                            dialog.setSize(image1.getWidth() + image2.getWidth() + 60, Math.max(image1.getHeight(), image2.getHeight()) + 55);
                            dialog.setPosition(10, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            break;
                        }

                        case TYPE_MATERIAU_MENUISERIE:{
                            s.acquire();
                            final IfcProduct product;
                            if (e.getUserObject() instanceof IfcWindow){
                                product = (IfcWindow)e.getUserObject();
                            }
                            else if (e.getUserObject() instanceof IfcDoor){
                                product = (IfcDoor)e.getUserObject();
                            } else {
                                product=null;
                            }
                            final Dialog dialog = new Dialog("Type de materiau", skin, "dialog") {
                                protected void result (Object object) {

                                }
                            }.show(stage);
                            TextButton button1 = new TextButton("Bois",skin);
                            button1.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = product;
                                    item[1] = TypeMateriauMenuiserieEnum.BOIS;
                                    DpeEvent responseType = DpeEvent.TYPE_MATERIAU_MENUISERIE_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton button2 = new TextButton("Metal",skin);
                            button2.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item [] = new Object[2];
                                    item[0] = product;
                                    item[1] = TypeMateriauMenuiserieEnum.METALLIQUE;
                                    DpeEvent responseType = DpeEvent.TYPE_MATERIAU_MENUISERIE_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton button3 = new TextButton("PVC",skin);
                            button3.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = product;
                                    item[1] = TypeMateriauMenuiserieEnum.PVC;
                                    DpeEvent responseType = DpeEvent.TYPE_MATERIAU_MENUISERIE_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            dialog.getContentTable().left().padLeft(20);
                            dialog.getContentTable().add(button1).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(button2).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(button3).pad(2).left();
                            dialog.setSize(button2.getWidth() + 100, button1.getHeight() * 3 + 60);
                            dialog.setPosition(10, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            break;
                        }
                        case TYPE_VITRAGE_MENUISERIE: {
                            s.acquire();
                            final IfcProduct product;
                            if (e.getUserObject() instanceof IfcWindow){
                                product = (IfcWindow)e.getUserObject();
                            }
                            else if (e.getUserObject() instanceof IfcDoor){
                                product = (IfcDoor)e.getUserObject();
                            } else {
                                product=null;
                            }
                            final Dialog dialog = new Dialog(" Type de vitrage ", skin, "dialog") {
                                protected void result (Object object) {

                                }
                            }.show(stage);
                            TextButton textButton1 = new TextButton("Simple vitrage",skin);
                            textButton1.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = product;
                                    item[1] = TypeVitrageEnum.SIMPLE_VITRAGE;
                                    DpeEvent responseType = DpeEvent.TYPE_VITRAGE_MENUISERIE_RESPONSE;
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
                                    item[0] = product;
                                    item[1] = TypeVitrageEnum.SURVITRAGE;
                                    DpeEvent responseType = DpeEvent.TYPE_VITRAGE_MENUISERIE_RESPONSE;
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
                                    item[0] = product;
                                    item[1] = TypeVitrageEnum.DOUBLE_VITRAGE_INF_1990;
                                    DpeEvent responseType = DpeEvent.TYPE_VITRAGE_MENUISERIE_RESPONSE;
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
                                    item[0] = product;
                                    item[1] = TypeVitrageEnum.DOUBLE_VITRAGE_SUP_1990_INF_2001;
                                    DpeEvent responseType = DpeEvent.TYPE_VITRAGE_MENUISERIE_RESPONSE;
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
                                    item[0] = product;
                                    item[1] = TypeVitrageEnum.DOUBLE_VITRAGE_SUP_2001;
                                    DpeEvent responseType = DpeEvent.TYPE_VITRAGE_MENUISERIE_RESPONSE;
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
                                    item[0] = product;
                                    item[1] = TypeVitrageEnum.TRIPLE_VITRAGE;
                                    DpeEvent responseType = DpeEvent.TYPE_VITRAGE_MENUISERIE_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            dialog.getContentTable().add(textButton1).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton2).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton3).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton4).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton5).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton6).pad(2).left();
                            dialog.setSize(textButton4.getWidth() + 40, textButton1.getHeight() * 6 + 92);
                            dialog.setPosition(10, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
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
                            TextButton textButton7 = new TextButton("Porte-fenetre battante",skin);
                            textButton7.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = door;
                                    item[1] = TypeDoorEnum.PORTE_FENETRE_BATTANTE;
                                    DpeEvent responseType = DpeEvent.TYPE_DOOR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            TextButton textButton8 = new TextButton("Porte-fenetre coulissante",skin);
                            textButton8.addListener(new ClickListener() {
                                public void clicked(InputEvent event, float x, float y) {
                                    Object item[] = new Object[2];
                                    item[0] = door;
                                    item[1] = TypeDoorEnum.PORTE_FENETRE_COULISSANTE;
                                    DpeEvent responseType = DpeEvent.TYPE_DOOR_RESPONSE;
                                    Event response = new Event(responseType, item);
                                    EventManager.getInstance().put(Channel.DPE, response);
                                    dialog.remove();
                                    s.release();
                                }
                            });
                            dialog.getContentTable().add(textButton1).pad(2).padTop(10).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton2).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton3).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton4).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton5).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton6).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton7).pad(2).left();
                            dialog.getContentTable().row();
                            dialog.getContentTable().add(textButton8).pad(2).left();
                            dialog.setSize(textButton2.getWidth() + 25, textButton1.getHeight() * 8 + 100);
                            dialog.setPosition(10, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                            break;
                        }
                    }
                }
        } catch (InterruptedException ie) {

        }
    }

}
