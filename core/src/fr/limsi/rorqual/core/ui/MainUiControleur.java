package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.ArrayList;
import java.util.HashMap;

import fr.limsi.rorqual.core.dpe.enums.generalproperties.DateConstructionBatimentEnum;
import fr.limsi.rorqual.core.event.ButtonValue;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventListener;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.event.EventRequest;
import fr.limsi.rorqual.core.event.UiEvent;
import fr.limsi.rorqual.core.logic.Calculateur;
import fr.limsi.rorqual.core.logic.CameraEngine;
import fr.limsi.rorqual.core.logic.Deleter;
import fr.limsi.rorqual.core.logic.Logic;
import fr.limsi.rorqual.core.model.Batiment;
import fr.limsi.rorqual.core.model.IfcExporter;
import fr.limsi.rorqual.core.model.IfcImporter;
import fr.limsi.rorqual.core.model.MaterialTypeEnum;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Objet;
import fr.limsi.rorqual.core.model.Slab;
import fr.limsi.rorqual.core.utils.AssetManager;
import fr.limsi.rorqual.core.utils.scene3d.models.Cote;
import fr.limsi.rorqual.core.utils.scene3d.models.Cote2D;
import fr.limsi.rorqual.core.utils.scene3d.models.SurfaceCote;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;

/**
 * Created by christophe on 28/07/15.
 */
// Controleur pour l'UI de l'écran principal (boutons + gestion de l'affichage des fenêtres)
public class MainUiControleur implements EventListener {

    Actor tb = null;
    Stage stage = null;
    Layout mainLayout = null;

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

    public void setMainLayout(Layout layout) {
        this.mainLayout = layout;
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
                }else if(tb instanceof Window) {
                    tb.setPosition(Gdx.graphics.getWidth() - ((Window) tb).getPrefWidth() - 5, Gdx.graphics.getHeight() - ((Window) tb).getPrefHeight()-100);
                } else {
                    tb.setPosition(tb.getHeight()/2, Gdx.graphics.getHeight() - 100);
                }
                stage.addActor(tb);
            }
        }
    }

    private void uncheckGeneralButtons() {
        ((Button)(((Table)mainLayout.getFromId("general_buttons")).getChildren().first())).getButtonGroup().uncheckAll();
    }

    private void uncheckControlButtons() {
        ((Button)(((Table)mainLayout.getFromId("control_buttons")).getChildren().first())).getButtonGroup().uncheckAll();
    }

    private void uncheckCalculButtons() {
        ((Button)(((Table)mainLayout.getFromId("calcul_buttons")).getChildren().first())).getButtonGroup().uncheckAll();
    }

    ArrayList<Cote> cotes = new ArrayList<Cote>();
    SurfaceCote surface;

    @Override
    public synchronized void notify(Channel c, Event e) throws InterruptedException {
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
                            Logic.getInstance().stop();
                            uncheckGeneralButtons();
                            uncheckControlButtons();
                            uncheckCalculButtons();
                            removeTb();
                            CameraEngine.getInstance().switchCamera();
                            ((Button)layout.getFromId("camera_button")).getStyle().up = (Drawable)StyleFactory.getDrawable(CameraEngine.getInstance().getCurrentCameraUpdater().iconeName);
                            ModelHolder.getInstance().getBatiment().setCamera(CameraEngine.getInstance().getCurrentCamera());
                            break;
                        case MOVE:
                            uncheckGeneralButtons();
                            uncheckCalculButtons();
                            removeTb();
                            if (button.isChecked()) {
                                Logic.getInstance().move();
                            }
                            else
                                Logic.getInstance().stop();
                            break;
                        case DELETE:
                            uncheckGeneralButtons();
                            uncheckCalculButtons();
                            removeTb();
                            if (button.isChecked()) {
                                if (MainApplicationAdapter.getSelected() != null)
                                    Logic.getInstance().delete(MainApplicationAdapter.getSelected(), button);
                                else
                                    Logic.getInstance().delete();
                            }
                            else
                                Logic.getInstance().stop();
                            break;
                        case ROTATE_D:
                            uncheckGeneralButtons();
                            uncheckCalculButtons();
                            removeTb();
                            Logic.getInstance().stop();
                            Logic.getInstance().rotate_d(MainApplicationAdapter.getSelected(), button);
                            break;
                        case ROTATE_G:
                            uncheckGeneralButtons();
                            uncheckCalculButtons();
                            removeTb();
                            Logic.getInstance().stop();
                            Logic.getInstance().rotate_g(MainApplicationAdapter.getSelected(), button);
                            break;
                        case MUR:
                            uncheckControlButtons();
                            uncheckCalculButtons();
                            if (button.isChecked()) {
                                Logic.getInstance().startWall();
                                removeTb();
                            }
                            else
                                Logic.getInstance().stop();
                            break;
                        case PIECE:
                            uncheckControlButtons();
                            uncheckCalculButtons();
                            if (button.isChecked()) {
                                Logic.getInstance().startPiece();
                                removeTb();
                            }
                            else
                                Logic.getInstance().stop();
                            break;
                        case DPE:
                            uncheckControlButtons();
                            uncheckGeneralButtons();
                            if (button.isChecked())
                                addTb(DpeUi.getPropertyWindow(DpeEvent.INFOS_GENERALES));
                            else
                                removeTb();
                            break;
                        case CHAUFFAGE:
                            uncheckControlButtons();
                            uncheckGeneralButtons();
                            if (button.isChecked())
                                addTb(DpeUi.getPropertyWindow(DpeEvent.INFOS_CHAUFFAGE));
                            else
                                removeTb();
                            break;
                        case MENUISERIE:
                            uncheckControlButtons();
                            uncheckCalculButtons();
                            removeTb();
                            if (button.isChecked())
                                addTb(ModelLibrary.getInstance().getTabWindow());
                            else
                                Logic.getInstance().stop();
                            break;
                        case ETAGE_PLUS:
                            Logic.getInstance().stop();
                            uncheckGeneralButtons();
                            uncheckControlButtons();
                            removeTb();
                            ModelHolder.getInstance().getBatiment().etageSuperieur();
                            CameraEngine.getInstance().getCurrentCameraUpdater().reset();
                            ((TextButton)layout.getFromId("currentEtage")).setText("" + ModelHolder.getInstance().getBatiment().getCurrentEtage().getNumber());
                            break;
                        case ETAGE_MINUS:
                            Logic.getInstance().stop();
                            uncheckGeneralButtons();
                            uncheckControlButtons();
                            removeTb();
                            ModelHolder.getInstance().getBatiment().etageInferieur();
                            CameraEngine.getInstance().getCurrentCameraUpdater().reset();
                            ((TextButton)layout.getFromId("currentEtage")).setText("" + ModelHolder.getInstance().getBatiment().getCurrentEtage().getNumber());
                            break;
                        case EXPORT_IFC:
                            IfcExporter.getInstance().realiseExportIfc();
                            break;
                        case NEW_FILE:
                            Logic.getInstance().stop();
                            uncheckGeneralButtons();
                            uncheckControlButtons();
                            removeTb();

                            Deleter.deleteBatiment();
                            ((TextButton)layout.getFromId("currentEtage")).setText("" + ModelHolder.getInstance().getBatiment().getCurrentEtage().getNumber());
                            CameraEngine.getInstance().reset();

                            ((Button)layout.getFromId("camera_button")).getStyle().up = (Drawable)StyleFactory.getDrawable(CameraEngine.getInstance().getCurrentCameraUpdater().iconeName);
                            ModelHolder.getInstance().getBatiment().setCamera(CameraEngine.getInstance().getCurrentCamera());
                            break;
                        case CALCUL_SURFACE:
                            Logic.getInstance().stop();
                            uncheckGeneralButtons();
                            uncheckControlButtons();
                            Calculateur.getInstance().actualiseCalculs();
                            if (button.isChecked())
                                addTb(Calculateur.getInstance().getWindow());
                            else
                                removeTb();
                            break;
                        case VISIBILITY_TOIT:
                            Logic.getInstance().stop();
                            uncheckGeneralButtons();
                            uncheckControlButtons();
                            removeTb();

                            ModelHolder.getInstance().getBatiment().setPlafondsVisibles(button.isChecked());
                            break;
                        case IMPORT_IFC:
                            Logic.getInstance().stop();
                            uncheckGeneralButtons();
                            uncheckControlButtons();
                            removeTb();
//                            Deleter.deleteBatiment();
//                            IfcImporter.getInstance().realiseImportIfc();
                            CameraEngine.getInstance().reset();

                            ((Button)layout.getFromId("camera_button")).getStyle().up = (Drawable)StyleFactory.getDrawable(CameraEngine.getInstance().getCurrentCameraUpdater().iconeName);
                            ModelHolder.getInstance().getBatiment().setCamera(CameraEngine.getInstance().getCurrentCamera());
                            break;
                        default:
                            System.out.println(lastValue);
                    }
                }
            } else if (e.getEventType() == UiEvent.ITEM_SELECTED) {
                if (e.getUserObject() instanceof Objet) {
                    mainLayout.getFromId("Rotate_G").setVisible(true);
                    mainLayout.getFromId("Rotate_D").setVisible(true);
                } else {
                    mainLayout.getFromId("Rotate_G").setVisible(false);
                    mainLayout.getFromId("Rotate_D").setVisible(false);
                }
                if (e.getUserObject() instanceof Slab) {
                    Slab slab = ((Slab) e.getUserObject());
                    surface = new SurfaceCote(slab);
                    slab.add(surface);
                    for (Mur m : slab.getMurs()) {
                        Cote cote = new Cote(m);
                        cotes.add(cote);
                        m.add(cote);
                    }
                } else if (e.getUserObject() instanceof Mur) {
                    Mur mur = ((Mur) e.getUserObject());
                    Cote cote = new Cote(mur);
                    cotes.add(cote);
                    mur.add(cote);
                }
            } else if (e.getEventType() == UiEvent.ITEM_DESELECTED) {
                mainLayout.getFromId("Rotate_G").setVisible(false);
                mainLayout.getFromId("Rotate_D").setVisible(false);
                //removeTb();
                if (e.getUserObject() instanceof Slab) {
                    surface.getParent().remove(surface);

                    for (Cote cote : cotes) {
                        cote.getParent().remove(cote);
                    }
                    cotes.clear();

                } else if (e.getUserObject() instanceof Mur) {
                    for (Cote cote : cotes) {
                        cote.getParent().remove(cote);
                    }
                    cotes.clear();
                }
            }

            // textures

            else if (e.getEventType() == UiEvent.TEXTURE1_PICKED) {
                HashMap<String,Object> items = (HashMap<String,Object>) e.getUserObject();
                EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                if (eventRequest == EventRequest.UPDATE_STATE) {
                    if (items.get("userObject") instanceof Slab) {
                        Slab slab = (Slab)items.get("userObject");
                        slab.setPlancherMaterialType((MaterialTypeEnum) items.get("lastValue"));
                    } else if (items.get("userObject") instanceof Mur) {
                        Mur mur = (Mur)items.get("userObject");
                        mur.setExteriorMaterialType((MaterialTypeEnum)items.get("lastValue"));
                    }
                }
                else if (eventRequest == EventRequest.GET_STATE) {
                    HashMap<String,Object> currentItems = new HashMap<String,Object>();

                    if (items.get("userObject") instanceof Slab) {
                        Slab slab = (Slab)items.get("userObject");
                        currentItems.put("lastValue", slab.getPlancherMaterialType());
                    } else if (items.get("userObject") instanceof Mur) {
                        Mur mur = (Mur)items.get("userObject");
                        currentItems.put("lastValue", mur.getExteriorMaterialType());
                    }

                    currentItems.put("userObject", items.get("userObject"));
                    currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                    Event e2 = new Event(UiEvent.TEXTURE1_PICKED, currentItems);
                    EventManager.getInstance().put(Channel.UI, e2);
                }

            } else if (e.getEventType() == UiEvent.TEXTURE2_PICKED) {
                HashMap<String,Object> items = (HashMap<String,Object>) e.getUserObject();
                EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                if (eventRequest == EventRequest.UPDATE_STATE) {
                    if (items.get("userObject") instanceof Slab) {
                        Slab slab = (Slab)items.get("userObject");
                        slab.setPlafondMaterialType((MaterialTypeEnum) items.get("lastValue"));
                    } else if (items.get("userObject") instanceof Mur) {
                        Mur mur = (Mur)items.get("userObject");
                        mur.setInteriorMaterialType1((MaterialTypeEnum) items.get("lastValue"));
                    }
                }
                else if (eventRequest == EventRequest.GET_STATE) {
                    HashMap<String,Object> currentItems = new HashMap<String,Object>();

                    if (items.get("userObject") instanceof Slab) {
                        Slab slab = (Slab)items.get("userObject");
                        currentItems.put("lastValue", slab.getPlafondMaterialType());
                    } else if (items.get("userObject") instanceof Mur) {
                        Mur mur = (Mur)items.get("userObject");
                        currentItems.put("lastValue", mur.getInteriorMaterialType1());
                    }

                    currentItems.put("userObject", items.get("userObject"));
                    currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                    Event e2 = new Event(UiEvent.TEXTURE2_PICKED, currentItems);
                    EventManager.getInstance().put(Channel.UI, e2);
                }

            } else if (e.getEventType() == UiEvent.TEXTURE3_PICKED) {
                HashMap<String,Object> items = (HashMap<String,Object>) e.getUserObject();
                EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                if (eventRequest == EventRequest.UPDATE_STATE) {
                    if (items.get("userObject") instanceof Mur) {
                        Mur mur = (Mur)items.get("userObject");
                        mur.setInteriorMaterialType2((MaterialTypeEnum) items.get("lastValue"));
                    }
                }
                else if (eventRequest == EventRequest.GET_STATE) {
                    HashMap<String,Object> currentItems = new HashMap<String,Object>();

                    if (items.get("userObject") instanceof Mur) {
                        Mur mur = (Mur)items.get("userObject");
                        currentItems.put("lastValue", mur.getInteriorMaterialType2());
                    }

                    currentItems.put("userObject", items.get("userObject"));
                    currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                    Event e2 = new Event(UiEvent.TEXTURE3_PICKED, currentItems);
                    EventManager.getInstance().put(Channel.UI, e2);
                }
            }
        }
    }
}
