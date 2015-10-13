package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.HashMap;

import fr.limsi.rorqual.core.event.ButtonValue;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventListener;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.event.EventRequest;
import fr.limsi.rorqual.core.event.UiEvent;
import fr.limsi.rorqual.core.logic.CameraEngine;
import fr.limsi.rorqual.core.logic.Logic;
import fr.limsi.rorqual.core.model.Batiment;
import fr.limsi.rorqual.core.model.IfcExporter;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.utils.AssetManager;

/**
 * Created by christophe on 28/07/15.
 */
// Controleur pour l'UI de l'écran principal (boutons + gestion de l'affichage des fenêtres)
public class MainUiControleur implements EventListener {

    Actor tb = null;
    Stage stage = null;

    private MainUiControleur() {
        EventManager.getInstance().addListener(Channel.UI, this);
    }

    /** Holder */
    private static class MainUiControleurHolder
    {
        /** Instance unique non préinitialisée */
        private final static MainUiControleur INSTANCE = new MainUiControleur();
    }

    public static synchronized MainUiControleur getInstance() {
        return MainUiControleurHolder.INSTANCE;
    }

    public void setStage(Stage s) {
        stage = s;
    }

    public Stage getStage() {
        return stage;
    }

    public void removeTb() {
        if (tb != null)
            synchronized (stage) {
                tb.remove();
            }
    }

    public void addTb(Actor actor) {
        removeTb();
        tb = actor;
        if (tb != null) {
            synchronized (stage) {
                if (tb instanceof TabWindow){
                    tb.setPosition(((TabWindow) tb).getPrefWidth()/2, Gdx.graphics.getHeight() - ((TabWindow) tb).getPrefHeight()/2-100);
                }else{
                    tb.setPosition(((TabWindow) tb).getPrefWidth()/2, Gdx.graphics.getHeight() - 100);
                }
                stage.addActor(tb);
            }
        }
    }

    @Override
    public void notify(Channel c, Event e) throws InterruptedException {
        if (c == Channel.UI) {
            if (e.getEventType() == UiEvent.BUTTON_CLICKED) {

                HashMap<String,Object> items = (HashMap<String,Object>) e.getUserObject();
                if (items.get("eventRequest") == EventRequest.GET_STATE) {
                    // FIXME: disable this case (no state to get)
                    HashMap<String,Object> response = new HashMap<String,Object>();
                    response.put("lastValue",null);
                    response.put("userObject",null);
                    response.put("eventRequest", EventRequest.CURRENT_STATE);
                    Event e2 = new Event(UiEvent.BUTTON_CLICKED, response);
                    EventManager.getInstance().put(Channel.UI, e2);

                } else if (items.get("eventRequest") == EventRequest.UPDATE_STATE) {
                    ButtonValue lastValue = (ButtonValue) items.get("lastValue");
                    Button button = (Button) items.get("button");
                    Layout layout = (Layout) items.get("layout");
                    switch (lastValue) {
                        case EXIT:
                            Gdx.app.exit();
                            break;
                        case SWITCH_2D_3D:
                            CameraEngine.getInstance().switchCamera();
                            ((Button)layout.getFromId("camera_button")).getStyle().up = (Drawable)StyleFactory.getDrawable(CameraEngine.getInstance().getCurrentCameraUpdater().iconeName);
                            ModelHolder.getInstance().getBatiment().setCamera(CameraEngine.getInstance().getCurrentCamera());
                            break;
                        case MOVE:
                            if (button.isChecked()) {
                                Logic.getInstance().move();
                            }
                            else
                                Logic.getInstance().stop();
                            break;
                        case DELETE:
                            if (button.isChecked()) {
                                Logic.getInstance().delete();
                            }
                            else
                                Logic.getInstance().stop();
                            break;
                        case ROTATE_D:
                            if (button.isChecked()) {
                                Logic.getInstance().rotate_d();
                            }
                            else
                                Logic.getInstance().stop();
                            break;
                        case ROTATE_G:
                            if (button.isChecked()) {
                                Logic.getInstance().rotate_g();
                            }
                            else
                                Logic.getInstance().stop();
                            break;
                        case MUR:
                            if (button.isChecked()) {
                                Logic.getInstance().startWall();
                                removeTb();
                            }
                            else
                                Logic.getInstance().stop();
                            break;
                        case PIECE:
                            if (button.isChecked()) {
                                Logic.getInstance().startPiece();
                                removeTb();
                            }
                            else
                                Logic.getInstance().stop();
                            break;
                        case FENETRE:
                            if (button.isChecked()) {
                                Logic.getInstance().startFenetre();
                                removeTb();
                            }
                            else
                                Logic.getInstance().stop();
                            break;
                        case PORTE:
                            if (button.isChecked()) {
                                removeTb();
                                Logic.getInstance().startPorte();
                            }
                            else
                                Logic.getInstance().stop();
                            break;
                        case DPE:
                            if (button.isChecked())
                                addTb(DpeUi.getPropertyWindow(DpeEvent.INFOS_GENERALES));
                            else
                                removeTb();
                            break;
                        case CHAUFFAGE:
                            if (button.isChecked())
                                addTb(DpeUi.getPropertyWindow(DpeEvent.INFOS_CHAUFFAGE));
                            else
                                removeTb();
                            break;
                        case MENUISERIE:
                            removeTb();
                            if (button.isChecked())
                                addTb(ModelLibrary.getInstance().getTabWindow());
                            else
                                Logic.getInstance().stop();
                            break;
                        case ETAGE_PLUS:
                            ModelHolder.getInstance().getBatiment().etageSuperieur();
                            CameraEngine.getInstance().getCurrentCameraUpdater().reset();
                            ((TextButton)layout.getFromId("currentEtage")).setText("" + ModelHolder.getInstance().getBatiment().getCurrentEtage().getNumber());
                            break;
                        case ETAGE_MINUS:
                            ModelHolder.getInstance().getBatiment().etageInferieur();
                            CameraEngine.getInstance().getCurrentCameraUpdater().reset();
                            ((TextButton)layout.getFromId("currentEtage")).setText("" + ModelHolder.getInstance().getBatiment().getCurrentEtage().getNumber());
                            break;
                        case EXPORT_IFC:
                            IfcExporter.getInstance().realiseExportIfc();
                            break;
                        default:
                            System.out.println(lastValue);
                    }
                }
            }
        }
    }
}
