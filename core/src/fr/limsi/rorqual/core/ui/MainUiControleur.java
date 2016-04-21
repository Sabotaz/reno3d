package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

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
import fr.limsi.rorqual.core.model.Fenetre;
import fr.limsi.rorqual.core.model.Ouverture;
import fr.limsi.rorqual.core.model.PorteFenetre;
import fr.limsi.rorqual.core.utils.Timeit;
import fr.limsi.rorqual.core.utils.analytics.ActionResolver;
import fr.limsi.rorqual.core.utils.analytics.Category;
import fr.limsi.rorqual.core.utils.analytics.HitMaker;
import fr.limsi.rorqual.core.utils.ifc.IfcExporter;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Objet;
import fr.limsi.rorqual.core.model.Slab;
import fr.limsi.rorqual.core.utils.AssetManager;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.models.Cote;
import fr.limsi.rorqual.core.utils.scene3d.models.SurfaceCote;
import fr.limsi.rorqual.core.utils.serialization.Deserializer;
import fr.limsi.rorqual.core.utils.serialization.Serializer;
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

        I18NBundle config_file = I18NBundle.createBundle(Gdx.files.getFileHandle("data/misc/config", Files.FileType.Internal), Locale.FRENCH);
        int version = MainApplicationAdapter.version;
        if (version == 2  || version == 4)
            ((CircularJauge)mainLayout.getFromId("dpe_jauge")).setForeground((Texture) AssetManager.getInstance().get("bar2"));

        //addTb(getIntro(),"intro");
    }

    Timeit timeit;
    String tabName;

    public void setCash(int cash) {
        ((Label)mainLayout.getFromId("label_cash")).setText("Total: " + cash + " euros");
    }

    public void setTotal(int total) {
        ((Label)mainLayout.getFromId("label_total")).setText("Travaux: " + total + " euros");
    }

    public void setRestant(int restant) {
        ((Label)mainLayout.getFromId("label_restant")).setText("Restant: " + restant + " euros");
    }

    public void setScore(int score) {
        ((Label)mainLayout.getFromId("label_score")).setText("Base: " + score + " kWh");
    }

    public void setEstimation(int estim) {
        ((Label)mainLayout.getFromId("label_estim")).setText("Actuel: " + estim + " kWh");
    }


    public void removeTb() {
        if (tb != null)
            synchronized (stage) {
                tb.remove();
                if (timeit != null) {
                    timeit.stop();
                    MainApplicationAdapter.LOG("CLOSE_TAB", "" + timeit.value());
                    MainApplicationAdapter.getActionResolver().sendTiming(Category.UI, timeit.value(), tabName);
                }
                timeit = null;
            }
    }

    public void addTb(Actor actor, String tabName) {
        this.tabName = tabName;
        addTb(actor);
    }

    public void addTb(Actor actor) {
        removeTb();
        tb = actor;
        if (tb != null) {
            timeit = new Timeit().start();
            MainApplicationAdapter.LOG("OPEN_TAB", tabName);
            synchronized (stage) {
                if (tb instanceof TabWindow){
                    tb.setPosition(((TabWindow) tb).getPrefWidth()/2, Gdx.graphics.getHeight() - ((TabWindow) tb).getPrefHeight()/2-100);
                }else if(tb instanceof Window) {
                    tb.setPosition((Gdx.graphics.getWidth() - ((Window) tb).getPrefWidth())/2, (Gdx.graphics.getHeight() - ((Window) tb).getPrefHeight())/2);
                } else {
                    tb.setPosition(tb.getHeight()/2, Gdx.graphics.getHeight() - 100);
                }
                stage.addActor(tb);
            }
        }
    }

    public void addTb(Actor actor, float x, float y) {
        removeTb();
        tb = actor;
        if (tb != null) {
            timeit = new Timeit().start();
            MainApplicationAdapter.LOG("OPEN_TAB", tabName);
            synchronized (stage) {
                tb.setPosition(x, y);
                stage.addActor(tb);
            }
        }
    }

    public void uncheckAll() {
        Logic.getInstance().stop();
        uncheckGeneralButtons();
        uncheckControlButtons();
        uncheckCalculButtons();
        uncheckInfoButtons();
        uncheckCameraButtons();
        uncheckSaveButtons();
    }

    private void uncheckNonGeneralButtons() {
        Logic.getInstance().stop();
        uncheckControlButtons();
        uncheckCalculButtons();
        uncheckInfoButtons();
        uncheckCameraButtons();
        uncheckSaveButtons();
    }

    private void uncheckNonCalculButtons() {
        Logic.getInstance().stop();
        uncheckGeneralButtons();
        uncheckControlButtons();
        uncheckInfoButtons();
        uncheckCameraButtons();
        uncheckSaveButtons();
    }

    private void uncheckNonInfoButtons() {
        Logic.getInstance().stop();
        uncheckGeneralButtons();
        uncheckControlButtons();
        uncheckCalculButtons();
        uncheckCameraButtons();
        uncheckSaveButtons();
    }

    private void uncheckNonControlButtons() {
        Logic.getInstance().stop();
        uncheckGeneralButtons();
        uncheckCalculButtons();
        uncheckInfoButtons();
        uncheckCameraButtons();
        uncheckSaveButtons();
    }

    private void uncheckNonCameraButtons() {
        Logic.getInstance().stop();
        uncheckGeneralButtons();
        uncheckControlButtons();
        uncheckCalculButtons();
        uncheckInfoButtons();
        uncheckSaveButtons();
    }

    private void uncheckNonSaveButtons() {
        Logic.getInstance().stop();
        uncheckGeneralButtons();
        uncheckControlButtons();
        uncheckCalculButtons();
        uncheckInfoButtons();
        uncheckCameraButtons();
    }

    private void uncheckGeneralButtons() {
        if (mainLayout.getFromId("general_buttons") != null)
            ((Button)(((Table)mainLayout.getFromId("general_buttons")).getChildren().first())).getButtonGroup().uncheckAll();
    }

    private void uncheckControlButtons() {
        if (mainLayout.getFromId("control_buttons") != null)
            ((Button)(((Table)mainLayout.getFromId("control_buttons")).getChildren().first())).getButtonGroup().uncheckAll();
    }

    private void uncheckCalculButtons() {
        if (mainLayout.getFromId("calcul_buttons") != null)
            ((Button)(((Table)mainLayout.getFromId("calcul_buttons")).getChildren().first())).getButtonGroup().uncheckAll();
    }

    private void uncheckInfoButtons() {
        if (mainLayout.getFromId("info_buttons") != null)
            ((Button)(((Table)mainLayout.getFromId("info_buttons")).getChildren().first())).getButtonGroup().uncheckAll();
    }

    private void uncheckCameraButtons() {
        if (mainLayout.getFromId("etage_buttons") != null)
            ((Button)(((Table)mainLayout.getFromId("etage_buttons")).getChildren().first())).getButtonGroup().uncheckAll();
    }

    private void uncheckSaveButtons() {
        if (mainLayout.getFromId("save_buttons") != null)
            ((Button)(((Table)mainLayout.getFromId("save_buttons")).getChildren().first())).getButtonGroup().uncheckAll();
    }

    ArrayList<Cote> cotes = new ArrayList<Cote>();
    SurfaceCote surface;

    @Override
    public synchronized void notify(Channel c, Event e) throws InterruptedException {
        if (c == Channel.UI) {
            if (e.getEventType() == UiEvent.BUTTON_CLICKED) {

                HashMap<String,Object> items = (HashMap<String,Object>) e.getUserObject();

                MainApplicationAdapter.LOG("BUTTON_CLICKED", "" + items.get("lastValue"));
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
                            uncheckAll();
                            removeTb();
                            CameraEngine.getInstance().switchCamera();
                            ((Button)layout.getFromId("camera_button")).getStyle().up = (Drawable)StyleFactory.getDrawable(CameraEngine.getInstance().getCurrentCameraUpdater().iconeName);
                            ModelHolder.getInstance().getBatiment().setCamera(CameraEngine.getInstance().getCurrentCamera());
                            break;
                        case MOVE:
                            uncheckNonControlButtons();
                            removeTb();
                            if (button.isChecked()) {
                                Logic.getInstance().move();
                            }
                            else
                                Logic.getInstance().stop();
                            break;
                        case DELETE:
                            uncheckNonControlButtons();
                            removeTb();
                            if (button.isChecked()) {
                                if (MainApplicationAdapter.getSelected() != null)
                                    Logic.getInstance().delete(MainApplicationAdapter.getSelected(), button);
                                else
                                    Logic.getInstance().delete();
                            }
                            break;
                        case ROTATE_D:
                            uncheckNonControlButtons();
                            removeTb();
                            Logic.getInstance().rotate_d(MainApplicationAdapter.getSelected(), button);
                            break;
                        case ROTATE_G:
                            uncheckNonControlButtons();
                            removeTb();
                            Logic.getInstance().rotate_g(MainApplicationAdapter.getSelected(), button);
                            break;
                        case MUR:
                            uncheckNonCalculButtons();
                            if (button.isChecked()) {
                                tabName = "Infos murs";
                                addTb(DpeUi.getPropertyWindow(DpeEvent.INFOS_MURS));
                            } else
                                removeTb();
                            break;
                        case FENETRE:
                            uncheckNonCalculButtons();
                            if (button.isChecked()) {
                                tabName = "Infos fenetres";
                                addTb(DpeUi.getPropertyWindow(DpeEvent.INFOS_FENETRES));
                            } else
                                removeTb();
                            break;
                        case PIECE:
                            uncheckNonGeneralButtons();
                            if (button.isChecked()) {
                                Logic.getInstance().startPiece();
                                removeTb();
                            }
                            break;
                        case DPE:
                            uncheckNonCalculButtons();
                            if (button.isChecked()) {
                                tabName = "Infos generales";
                                addTb(DpeUi.getPropertyWindow(DpeEvent.INFOS_GENERALES));
                            } else
                                removeTb();
                            break;
                        case CHAUFFAGE:
                            uncheckNonCalculButtons();
                            if (button.isChecked()) {
                                tabName = "Infos chauffage";
                                addTb(DpeUi.getPropertyWindow(DpeEvent.INFOS_CHAUFFAGE));
                            }else
                                removeTb();
                            break;
                        case MENUISERIE:
                            uncheckNonCalculButtons();
                            removeTb();
                            if (button.isChecked()) {
                                tabName = "Menuiserie";
                                addTb(ModelLibrary.getInstance().getTabWindow());
                            }
                            break;
                        case ETAGE_PLUS:
                            uncheckAll();
                            removeTb();
                            ModelHolder.getInstance().getBatiment().etageSuperieur();
                            CameraEngine.getInstance().getCurrentCameraUpdater().reset();
                            ((TextButton)layout.getFromId("currentEtage")).setText("" + ModelHolder.getInstance().getBatiment().getCurrentEtage().getNumber());
                            break;
                        case ETAGE_MINUS:
                            uncheckAll();
                            removeTb();
                            ModelHolder.getInstance().getBatiment().etageInferieur();
                            CameraEngine.getInstance().getCurrentCameraUpdater().reset();
                            ((TextButton)layout.getFromId("currentEtage")).setText("" + ModelHolder.getInstance().getBatiment().getCurrentEtage().getNumber());
                            break;
                        case EXPORT_IFC:
                            uncheckNonSaveButtons();
                            removeTb();
                            tabName = "export";
                            addTb(getExportTb());
                            break;
                        case NEW_FILE:
                            uncheckNonSaveButtons();
                            removeTb();
                            HitMaker.makeHitOnNew();

                            Deleter.deleteBatiment();
                            ((TextButton)layout.getFromId("currentEtage")).setText("" + ModelHolder.getInstance().getBatiment().getCurrentEtage().getNumber());
                            CameraEngine.getInstance().reset();

                            ((Button)layout.getFromId("camera_button")).getStyle().up = (Drawable)StyleFactory.getDrawable(CameraEngine.getInstance().getCurrentCameraUpdater().iconeName);
                            ModelHolder.getInstance().getBatiment().setCamera(CameraEngine.getInstance().getCurrentCamera());
                            break;
                        case CALCUL_SURFACE:
                            uncheckNonInfoButtons();
                            Calculateur.getInstance().actualiseCalculs();
                            if (button.isChecked()) {
                                tabName = "Calculateur";
                                addTb(Calculateur.getInstance().getWindow());
                            }
                            else
                                removeTb();
                            break;
                        case VISIBILITY_TOIT:
                            uncheckNonCameraButtons();
                            removeTb();

                            ModelHolder.getInstance().getBatiment().setPlafondsVisibles(button.isChecked());
                            break;
                        case IMPORT_IFC:
                            uncheckNonSaveButtons();
                            removeTb();
//                            Deleter.deleteBatiment();
//                            IfcImporter.getInstance().realiseImportIfc();
                            CameraEngine.getInstance().reset();

                            ((Button)layout.getFromId("camera_button")).getStyle().up = (Drawable)StyleFactory.getDrawable(CameraEngine.getInstance().getCurrentCameraUpdater().iconeName);
                            ModelHolder.getInstance().getBatiment().setCamera(CameraEngine.getInstance().getCurrentCamera());
                            break;
                        case SAVE:
                            uncheckNonSaveButtons();
                            removeTb();
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("filename", MainApplicationAdapter.id + ".3dr");
                            Event e2 = new Event(UiEvent.SAVE_FILE, currentItems);
                            EventManager.getInstance().put(Channel.UI, e2);
                            break;
                        case LOAD:
                            uncheckNonSaveButtons();
                            removeTb();
                            tabName = "Load";
                            addTb(getLoadTb());
                            break;
                        case HELP:
                            uncheckNonInfoButtons();
                            if (button.isChecked()) {
                                tabName = "Help";
                                addTb(getHelp(), 0,0);
                            }
                            else
                                removeTb();
                            break;
                        case INFO:
                            uncheckNonInfoButtons();
                            if (button.isChecked()) {
                                tabName = "Info";
                                addTb(getInfo());
                            }
                            else
                                removeTb();
                            break;

                        default:
                            System.out.println(lastValue);
                    }
                }
            } else if (e.getEventType() == UiEvent.ITEM_SELECTED) {
                if (e.getUserObject() instanceof Objet) {
                    if (mainLayout.getFromId("Rotate_G") != null)
                        mainLayout.getFromId("Rotate_G").setVisible(true);
                    if (mainLayout.getFromId("Rotate_D") != null)
                        mainLayout.getFromId("Rotate_D").setVisible(true);
                } else {
                    if (mainLayout.getFromId("Rotate_G") != null)
                        mainLayout.getFromId("Rotate_G").setVisible(false);
                    if (mainLayout.getFromId("Rotate_D") != null)
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
                if (mainLayout.getFromId("Rotate_G") != null)
                    mainLayout.getFromId("Rotate_G").setVisible(false);
                if (mainLayout.getFromId("Rotate_D") != null)
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
                        slab.setPlancherMaterialType((String) items.get("lastValue"));

                        MainApplicationAdapter.LOG("AMENAGEMENT", "TEXTURE_SOL", "" + slab.getSurface(), (String) items.get("lastValue"));

                    } else if (items.get("userObject") instanceof Mur) {
                        Mur mur = (Mur)items.get("userObject");
                        mur.setExteriorMaterialType((String) items.get("lastValue"));

                        MainApplicationAdapter.LOG("AMENAGEMENT", "TEXTURE_MUR_EXT", "" + mur.getB().getPosition().cpy().sub(mur.getA().getPosition()), (String) items.get("lastValue"));

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
                        slab.setPlafondMaterialType((String) items.get("lastValue"));
                    } else if (items.get("userObject") instanceof Mur) {
                        Mur mur = (Mur)items.get("userObject");
                        mur.setInteriorMaterialType1((String) items.get("lastValue"));

                        MainApplicationAdapter.LOG("AMENAGEMENT", "TEXTURE_MUR_INT1", "" + mur.getB().getPosition().cpy().sub(mur.getA().getPosition()), (String) items.get("lastValue"));

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
                        mur.setInteriorMaterialType2((String) items.get("lastValue"));

                        MainApplicationAdapter.LOG("AMENAGEMENT", "TEXTURE_MUR_INT2", "" + mur.getB().getPosition().cpy().sub(mur.getA().getPosition()), (String) items.get("lastValue"));

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
            else if (e.getEventType() == UiEvent.SAVE_FILE) {
                HashMap<String,Object> items = (HashMap<String,Object>) e.getUserObject();
                String filename = (String)items.get("filename");
                HitMaker.makeHitOnSave();
                Serializer.saveAll(filename);
                Gdx.app.exit();
            }
            else if (e.getEventType() == UiEvent.LOAD_FILE) {
                HashMap<String,Object> items = (HashMap<String,Object>) e.getUserObject();
                String filename = (String)items.get("filename");

                Deleter.deleteBatiment();
                if (mainLayout.getFromId("currentEtage") != null)
                    ((TextButton)mainLayout.getFromId("currentEtage")).setText("" + ModelHolder.getInstance().getBatiment().getCurrentEtage().getNumber());
                CameraEngine.getInstance().reset();

                ((Button)mainLayout.getFromId("camera_button")).getStyle().up = (Drawable)StyleFactory.getDrawable(CameraEngine.getInstance().getCurrentCameraUpdater().iconeName);
                ModelHolder.getInstance().getBatiment().setCamera(CameraEngine.getInstance().getCurrentCamera());

                Deserializer.loadAll(filename);

                CameraEngine.getInstance().reset();
                HitMaker.makeHitOnLoad();

            }
            else if (e.getEventType() == UiEvent.EXPORT_FILE) {
                HashMap<String,Object> items = (HashMap<String,Object>) e.getUserObject();
                String filename = (String)items.get("filename");

                HitMaker.makeHitOnExport();
                IfcExporter.getInstance().realiseExportIfc(filename);
            }

            // model sizes
            else if (e.getEventType() == UiEvent.HAUTEUR_MODELE) {
                HashMap<String,Object> items = (HashMap<String,Object>) e.getUserObject();
                EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                if (eventRequest == EventRequest.UPDATE_STATE) {
                    ModelContainer model = MainApplicationAdapter.getSelected();
                    if (model instanceof Ouverture) {
                        ((Ouverture) model).setHeight((Float) items.get("lastValue") / 100);
                    } else if (model instanceof Mur) {
                        ModelHolder.getInstance().getBatiment().getCurrentEtage().setHeight((Float) items.get("lastValue") / 100);
                    }
                }
                else if (eventRequest == EventRequest.GET_STATE) {
                    HashMap<String,Object> currentItems = new HashMap<String,Object>();
                    ModelContainer model = MainApplicationAdapter.getSelected();
                    if (model instanceof Ouverture) {
                        currentItems.put("lastValue", 100*((Ouverture) model).getHeight());
                    } else if (model instanceof Mur) {
                        currentItems.put("lastValue", 100 * ModelHolder.getInstance().getBatiment().getCurrentEtage().getHeight());
                    }

                    currentItems.put("userObject", items.get("userObject"));
                    currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                    Event e2 = new Event(UiEvent.HAUTEUR_MODELE, currentItems);
                    EventManager.getInstance().put(Channel.UI, e2);
                }
            }
            else if (e.getEventType() == UiEvent.LARGEUR_MODELE) {
                HashMap<String,Object> items = (HashMap<String,Object>) e.getUserObject();
                EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                if (eventRequest == EventRequest.UPDATE_STATE) {
                    ModelContainer model = MainApplicationAdapter.getSelected();
                    if (model instanceof Ouverture) {
                        ((Ouverture) model).setWidth((Float) items.get("lastValue") / 100);
                    }
                }
                else if (eventRequest == EventRequest.GET_STATE) {
                    HashMap<String,Object> currentItems = new HashMap<String,Object>();

                    ModelContainer model = MainApplicationAdapter.getSelected();
                    if (model instanceof Ouverture) {
                        currentItems.put("lastValue", 100*((Ouverture) model).getWidth());
                    }

                    currentItems.put("userObject", items.get("userObject"));
                    currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                    Event e2 = new Event(UiEvent.LARGEUR_MODELE, currentItems);
                    EventManager.getInstance().put(Channel.UI, e2);
                }
            }
            else if (e.getEventType() == UiEvent.FONCTION_PIECE) {
                HashMap<String,Object> items = (HashMap<String,Object>) e.getUserObject();
                EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                if (eventRequest == EventRequest.UPDATE_STATE) {
                    ModelContainer model = MainApplicationAdapter.getSelected();
                    if (model instanceof Slab) {
                        ((Slab) model).setFonction((String) items.get("lastValue"));

                        MainApplicationAdapter.LOG("AMENAGEMENT", e.getEventType().toString(),""+((Slab) model).getSurface(), (String) items.get("lastValue"));
                    }

                }
                else if (eventRequest == EventRequest.GET_STATE) {
                    HashMap<String,Object> currentItems = new HashMap<String,Object>();

                    ModelContainer model = MainApplicationAdapter.getSelected();
                    if (model instanceof Slab) {
                        currentItems.put("lastValue", ((Slab) model).getFonction());
                    }

                    currentItems.put("userObject", items.get("userObject"));
                    currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                    Event e2 = new Event(UiEvent.FONCTION_PIECE, currentItems);
                    EventManager.getInstance().put(Channel.UI, e2);
                }
            }
            else if (e.getEventType() == UiEvent.RATIO_MODELE) {
                HashMap<String,Object> items = (HashMap<String,Object>) e.getUserObject();
                EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                if (eventRequest == EventRequest.UPDATE_STATE) {
                }
                else if (eventRequest == EventRequest.GET_STATE) {
                    HashMap<String,Object> currentItems = new HashMap<String,Object>();

                    currentItems.put("lastValue", false);

                    currentItems.put("userObject", items.get("userObject"));
                    currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                    Event e2 = new Event(UiEvent.RATIO_MODELE, currentItems);
                    EventManager.getInstance().put(Channel.UI, e2);
                }
            }
            else if (e.getEventType() == UiEvent.HAUTEUR_SOL_MODELE) {
                HashMap<String,Object> items = (HashMap<String,Object>) e.getUserObject();
                EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                if (eventRequest == EventRequest.UPDATE_STATE) {
                    ModelContainer model = MainApplicationAdapter.getSelected();
                    if (model instanceof Fenetre) {
                        ((Fenetre) model).setY((Float) items.get("lastValue") / 100);
                    }
                }
                else if (eventRequest == EventRequest.GET_STATE) {
                    HashMap<String,Object> currentItems = new HashMap<String,Object>();

                    Layout layout = (Layout)items.get("layout");

                    ModelContainer model = MainApplicationAdapter.getSelected();
                    if (model instanceof Fenetre) {
                        currentItems.put("lastValue", 100*model.getPosition().y);
                    } else if (model instanceof PorteFenetre) {
                        currentItems.put("lastValue", 100*model.getPosition().y);
                    }

                    currentItems.put("userObject", items.get("userObject"));
                    currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                    Event e2 = new Event(UiEvent.HAUTEUR_SOL_MODELE, currentItems);
                    EventManager.getInstance().put(Channel.UI, e2);

                    Actor actor;
                    do Thread.sleep(15); while ((actor = layout.getFromId("hauteur_au_sol")) == null);
                    actor.setVisible(model instanceof Fenetre);
                }
            }

        }
    }

    private Window getSaveTb() {
        Skin skin = (Skin) AssetManager.getInstance().get("uiskin");
        Label.LabelStyle ls = new Label.LabelStyle((BitmapFont)AssetManager.getInstance().get("default.fnt"), Color.BLACK);
        TextField.TextFieldStyle tfs = skin.get("default", TextField.TextFieldStyle.class);
        tfs.font = (BitmapFont)AssetManager.getInstance().get("default.fnt");
        final Window w = new Window("Sauvegarder",skin);
        final TextField tf = new TextField("model",tfs);
        Label label = new Label(".3dr",ls);
        TextButton.TextButtonStyle tbs = skin.get("default", TextButton.TextButtonStyle.class);
        tbs.font = (BitmapFont) AssetManager.getInstance().get("default.fnt");
        final TextButton button = new TextButton("Sauvegarder", tbs);
        w.add(tf).left().pad(2);
        w.add(label).pad(2).left().row();
        w.add(button).pad(2).left();
        w.setWidth(w.getPrefWidth());
        w.setHeight(w.getPrefHeight());
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tf.getText().toString() != "") {

                    HashMap<String,Object> currentItems = new HashMap<String,Object>();
                    currentItems.put("filename",tf.getText().toString() + ".3dr");
                    Event e = new Event(UiEvent.SAVE_FILE, currentItems);
                    EventManager.getInstance().put(Channel.UI, e);

                    w.remove();
                }
            }
        });
        return w;
    }

    private Window getExportTb() {
        Skin skin = (Skin) AssetManager.getInstance().get("uiskin");
        Label.LabelStyle ls = new Label.LabelStyle((BitmapFont)AssetManager.getInstance().get("default.fnt"), Color.BLACK);
        TextField.TextFieldStyle tfs = skin.get("default", TextField.TextFieldStyle.class);
        tfs.font = (BitmapFont)AssetManager.getInstance().get("default.fnt");
        final Window w = new Window("Exporter",skin);
        final TextField tf = new TextField("model",tfs);
        Label label = new Label(".ifc",ls);
        TextButton.TextButtonStyle tbs = skin.get("default", TextButton.TextButtonStyle.class);
        tbs.font = (BitmapFont) AssetManager.getInstance().get("default.fnt");
        final TextButton button = new TextButton("Exporter", tbs);
        w.add(tf).left().pad(2);
        w.add(label).pad(2).left().row();
        w.add(button).pad(2).left();
        w.setWidth(w.getPrefWidth());
        w.setHeight(w.getPrefHeight());
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tf.getText().toString() != "") {

                    HashMap<String,Object> currentItems = new HashMap<String,Object>();
                    currentItems.put("filename",tf.getText().toString() + ".ifc");
                    Event e = new Event(UiEvent.EXPORT_FILE, currentItems);
                    EventManager.getInstance().put(Channel.UI, e);

                    w.remove();
                }
            }
        });
        return w;
    }

    private Actor getHelp() {
        Image image = new Image((Texture)AssetManager.getInstance().get("help-panel"));
        image.setAlign(Align.center);
        image.setHeight(Gdx.graphics.getHeight());
        image.setWidth(Gdx.graphics.getWidth());
        image.setScaling(Scaling.fit);
        return image;
    }

    private Window getInfo() {
        Skin skin = (Skin) AssetManager.getInstance().get("uiskin");
        Label.LabelStyle title = new Label.LabelStyle((BitmapFont)AssetManager.getInstance().get("buttons.fnt"), Color.BLACK);
        Label.LabelStyle ls = new Label.LabelStyle((BitmapFont)AssetManager.getInstance().get("defaultTitle.fnt"), Color.BLACK);
        Label.LabelStyle link = new Label.LabelStyle((BitmapFont)AssetManager.getInstance().get("defaultTitle.fnt"), Color.BLUE);

        TextButton.TextButtonStyle tbs = skin.get("default", TextButton.TextButtonStyle.class);
        tbs.font = (BitmapFont) AssetManager.getInstance().get("default.fnt");

        final Window w = new Window("Informations",skin);

        Table tab = new Table();

        String rd = "Équipe R&D";
        String julien = "Julien CHRISTOPHE\n    Ingénieur CNRS\n    Développement mobile";
        String thomas = "Thomas RICORDEAU\n    Ingénieur CNRS\n    Développement thermique";
        String mehdi = "Mehdi AMMI\n    Responsable de projet\n    Maitre de conférences\n    Université Paris-Sud, LIMSI-CNRS";


        Image logo_rpe = new Image((Texture)AssetManager.getInstance().get("logo-rpe"));
        Image logo_cnrs = new Image((Texture)AssetManager.getInstance().get("logo-cnrs"));
        Image logo_limsi = new Image((Texture)AssetManager.getInstance().get("logo-limsi"));
        Image logo_upsud = new Image((Texture)AssetManager.getInstance().get("logo-upsud"));

        int img_size = 200;

        logo_rpe.setHeight(img_size);
        logo_rpe.setWidth(img_size);
        logo_rpe.setScaling(Scaling.fit);

        logo_cnrs.setHeight(img_size);
        logo_cnrs.setWidth(img_size);
        logo_cnrs.setScaling(Scaling.fit);

        logo_limsi.setHeight(img_size);
        logo_limsi.setWidth(img_size);
        logo_limsi.setScaling(Scaling.fit);

        logo_upsud.setHeight(img_size);
        logo_upsud.setWidth(img_size);
        logo_upsud.setScaling(Scaling.fit);

        String site = "Accéder au site web de l'application";

        String renov = "Estimez vos travaux de rénovation";

        String mail = "Contacter l'équipe de développement";

        String feedback = "Laissez votre avis";

        Label label1 = new Label(rd,title);
        Label label2 = new Label(julien,ls);
        Label label3 = new Label(thomas,ls);
        Label label4 = new Label(mehdi,ls);

        TextButton button1 = new TextButton(site, tbs);
        TextButton button2 = new TextButton(renov, tbs);
        TextButton button3 = new TextButton(mail, tbs);
        TextButton button4 = new TextButton(feedback, tbs);

        button1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.net.openURI("http://blog.laplateformedelarenovation.fr/plan-3d-energy/");
            }
        });

        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.net.openURI("http://laplateformedelarenovation.fr");
            }
        });

        button3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                ActionResolver actionResolver = MainApplicationAdapter.getActionResolver();
                actionResolver.sendEmail("");
            }
        });

        button4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.net.openURI("http://blog.laplateformedelarenovation.fr/plan-3d-energy/plan-3d-energy-avis-betat-testeur/");
            }
        });

        Table imgtab = new Table();

        imgtab.add(logo_rpe)/*.left()*/.pad(10).size(img_size,img_size);
        imgtab.add(logo_cnrs)/*.right()*/.pad(10).size(img_size, img_size);//.row();
        imgtab.add(logo_limsi)/*.left()*/.pad(10).size(img_size, img_size);
        imgtab.add(logo_upsud)/*.right()*/.pad(10).size(img_size, img_size).row();

        tab.add(label1).center().padBottom(20).row();
        tab.add(label2).left().padBottom(10).row();
        tab.add(label3).left().padBottom(10).row();
        tab.add(label4).left().row();

        tab.add(imgtab).center().size(4 * img_size + 80, 1 * img_size + 40).pad(20).row();

        tab.add(button1).center().pad(10).row();
        tab.add(button2).center().pad(10).row();
        tab.add(button3).center().pad(10).row();
        tab.add(button4).center().pad(10).row();

        w.add(tab).pad(10);

        w.setWidth(w.getPrefWidth());
        w.setHeight(w.getPrefHeight());

        return w;
    }

    private Window getIntro() {
        Skin skin = (Skin) AssetManager.getInstance().get("uiskin");
        Label.LabelStyle ls = new Label.LabelStyle((BitmapFont)AssetManager.getInstance().get("defaultTitle.fnt"), Color.BLACK);

        TextButton.TextButtonStyle tbs = skin.get("default", TextButton.TextButtonStyle.class);
        tbs.font = (BitmapFont) AssetManager.getInstance().get("default.fnt");

        final Window w = new Window("Bienvenue",skin);

        Table tab = new Table();

        I18NBundle config_file = I18NBundle.createBundle(Gdx.files.getFileHandle("data/misc/config", Files.FileType.Internal), Locale.FRENCH);
        int version = Integer.parseInt(config_file.get("VERSION"));
        String intro = config_file.get("INTRO#" + version);

        String ok = "J'ai compris";

        Label label = new Label(intro,ls);

        TextButton button = new TextButton(ok, tbs);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                removeTb();
            }
        });

        tab.add(label).center().padBottom(20).row();

        tab.add(button).center().pad(10).row();

        w.add(tab).pad(10);

        w.setWidth(w.getPrefWidth());
        w.setHeight(w.getPrefHeight());

        return w;
    }

    private Window getLoadTb() {
        Skin skin = (Skin) AssetManager.getInstance().get("uiskin");
        final Window w = new Window("Charger",skin);

        FileHandle handle = Gdx.files.external(".");
        ArrayList<String> filenames = new ArrayList<String>();
        for (FileHandle fh : handle.list(".3dr"))
            filenames.add(fh.name());

        if (filenames.isEmpty()) {
            Label.LabelStyle ls = new Label.LabelStyle((BitmapFont)AssetManager.getInstance().get("default.fnt"), Color.BLACK);
            Label label = new Label("Aucun fichier 3dr n'a été trouvé",ls);
            TextButton.TextButtonStyle tbs = skin.get("default", TextButton.TextButtonStyle.class);
            tbs.font = (BitmapFont) AssetManager.getInstance().get("default.fnt");
            final TextButton button = new TextButton("Ok", tbs);
            w.add(label).pad(2).left().row();
            w.add(button).pad(2);
            w.setWidth(w.getPrefWidth());
            w.setHeight(w.getPrefHeight());

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    w.remove();
                }
            });

            return w;

        }

        List.ListStyle lls = skin.get("default", List.ListStyle.class);
        lls.font = (BitmapFont)AssetManager.getInstance().get("default.fnt");
        final List list = new List(lls);
        list.setItems(filenames.toArray());
        ScrollPane.ScrollPaneStyle sps = skin.get("perso",ScrollPane.ScrollPaneStyle.class);
        final ScrollPane scrollPane = new ScrollPane(list, sps);
        scrollPane.setFlickScroll(false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.layout();
        scrollPane.updateVisualScroll();

//        scrollPane.setupFadeScrollBars(1f,0.5f);

        list.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (list.getSelected() != null) {
                    HashMap<String,Object> currentItems = new HashMap<String,Object>();
                    currentItems.put("filename",list.getSelected());
                    Event e = new Event(UiEvent.LOAD_FILE, currentItems);
                    EventManager.getInstance().put(Channel.UI, e);

                    w.remove();
                }
            }
        });

        w.add(scrollPane).left().pad(2);
        w.setWidth(w.getPrefWidth());
        w.setHeight(w.getPrefHeight());

        return w;
    }
}
