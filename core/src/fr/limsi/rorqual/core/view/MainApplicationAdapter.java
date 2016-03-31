package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.HashMap;

import fr.limsi.rorqual.core.dpe.Dpe;
import fr.limsi.rorqual.core.dpe.DpeKartoffelator;
import fr.limsi.rorqual.core.dpe.DpeStateUpdater;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.event.UiEvent;
import fr.limsi.rorqual.core.logic.CameraEngine;
import fr.limsi.rorqual.core.logic.Logic;
import fr.limsi.rorqual.core.model.Batiment;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.ui.CircularJauge;
import fr.limsi.rorqual.core.ui.DpeUi;
import fr.limsi.rorqual.core.ui.Layout;
import fr.limsi.rorqual.core.ui.MainUiControleur;
import fr.limsi.rorqual.core.ui.ModelLibrary;
import fr.limsi.rorqual.core.ui.Popup;
import fr.limsi.rorqual.core.ui.TextureLibrary;
import fr.limsi.rorqual.core.utils.Timeit;
import fr.limsi.rorqual.core.utils.analytics.ActionResolver;
import fr.limsi.rorqual.core.utils.AssetManager;
import fr.limsi.rorqual.core.utils.DefaultMutableTreeNode;

import fr.limsi.rorqual.core.utils.analytics.Category;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;
import fr.limsi.rorqual.core.view.shaders.ShaderChooser;

// MAIN
public class MainApplicationAdapter extends InputAdapter implements ApplicationListener {

    private ShapeRenderer shape;
    private static ModelGraph modelGraph;
    private Stage stageMenu;
    private Stage loadingStage;
    private Skin skin;
    private Button buttonDPE, buttonExit;
    private BitmapFont fontBlack;
    private BitmapFont fontWhite;
    private static int ncam = 1;
    private Environment environnement;
    private ShaderProvider shaderProvider;
    private DpeUi dpeui;
    private DpeStateUpdater state;
    private Model model;
    private ModelBatch modelBatchOpaque;
    private ModelBatch modelBatchTransparent;
    private ModelInstance modelInstance;
    private AssetManager assets;
    private Viewport viewport;
    private Popup popup;
    private DirectionalLight light;
    private ModelContainer sun;
    private ModelContainer pin;
    private Vector3 decal_pos;
    private MainUiControleur mainUiControleur;
    private Label labelScore;
    private Label lettreScore;
    private DpeKartoffelator kartoffelator;

    private CircularJauge score;

    private boolean loading_finished = false;

    public static float time[] = {0, 0, 0, 0};
    public static String versionName = "1.0.x";
    public static int versionCode = 1;

    private static ActionResolver actionResolver;

    public MainApplicationAdapter(ActionResolver actionResolver) {
        this.actionResolver = actionResolver;
    }

    public static ActionResolver getActionResolver() {
        return actionResolver;
    }

    @Override
	public void create () {

        createLoadingStage();

        Thread t = new Thread() {
            public void run() {
                load();

                try {

                    HashMap<String,Object> currentItems = new HashMap<String,Object>();
                    currentItems.put("filename","expe.3dr.hidden");
                    Event e = new Event(UiEvent.LOAD_FILE, currentItems);
                    EventManager.getInstance().put(Channel.UI, e);

                    Thread.sleep(750);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                loading_finished = true;
            }
        };

        t.start();

	}

    Label loadingLabel;
    private void createLoadingStage() {

        stageMenu = new Stage();
        loadingStage = new Stage();

        Stack stack = new Stack();
        Container container = new Container();

        Image image = new Image(new Texture(Gdx.files.internal("data/img/screen.png")));
        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(Gdx.files.internal("data/font/white.fnt")), Color.WHITE);
        loadingLabel = new Label("loading...", labelStyle);
        float img_ratio = image.getWidth() / image.getImageHeight();
        float height = Gdx.graphics.getHeight();
        float width = Gdx.graphics.getWidth();

        image.setSize(height * img_ratio, height);
        stack.setSize(width, height);

        container.setActor(loadingLabel);
        container.padTop(height/3);

        stack.add(image);
        stack.add(container);
        loadingStage.addActor(stack);
        //loadingStage.addActor(loadingLabel);

    }

    private void setLoadingMessage(String msg) {
        synchronized (loadingStage) {
            loadingLabel.setText(msg);
        }
    }

    private void load() {
        Timeit start = new Timeit().start();
        Timeit timeit;
        System.out.println("start init 0");

        setLoadingMessage("Starting event manager...");
        timeit = new Timeit().start();
        EventManager.getInstance().start();
        timeit.stop();
        System.out.println("event manager ok " + (timeit.value() * 0.001f));
        actionResolver.sendTiming(Category.LOADING, timeit.value(), "Event manager loading time");

        setLoadingMessage("Loading assets...");
        timeit = new Timeit().start();
        assets = AssetManager.getInstance();
        assets.init();

        // copy examples
        for (FileHandle fileHandle : Gdx.files.internal("data/examples/").list(".3dr")) {
            FileHandle handle = Gdx.files.external(fileHandle.name());
            if (!handle.exists()) {
                handle.write(fileHandle.read(), false);
            }
        }

        timeit.stop();
        System.out.println("assets ok " + (timeit.value() * 0.001f));
        actionResolver.sendTiming(Category.LOADING, timeit.value(), "Assets loading time");

        setLoadingMessage("Loading building...");
        timeit = new Timeit().start();
        ModelHolder.getInstance().setBatiment(new Batiment());
        timeit.stop();
        System.out.println("batiment ok " + (timeit.value() * 0.001f));
        actionResolver.sendTiming(Category.LOADING, timeit.value(), "Batiment loading time", "Batiment vide");

        setLoadingMessage("Loading shader engine...");
        timeit = new Timeit().start();
        shaderProvider = new ShaderChooser();
        timeit.stop();
        System.out.println("shaders ok " + (timeit.value() * 0.001f));
        actionResolver.sendTiming(Category.LOADING, timeit.value(), "Shader engine loading time");

        modelGraph = new ModelGraph();
        modelGraph.setCamera(CameraEngine.getInstance().getCurrentCamera());
//        stageMenu.setDebugAll(true);
        //System.out.println(stageMenu.getWidth());

        /*** Création des lumières ***/
        setLoadingMessage("Loading environment...");
        timeit = new Timeit().start();
        environnement = new Environment();
        environnement.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        light = new DirectionalLight();
        light.set(1f, 1f, 1f, 0f, 0f, 0f);
        environnement.add(light);

        sun = new ModelContainer();
        sun.setSelectable(false);
        sun.local_transform.setToTranslation(new Vector3(-200, -100, 100));

        timeit.stop();
        System.out.println("environnement ok " + (timeit.value() * 0.001f));
        actionResolver.sendTiming(Category.LOADING, timeit.value(), "Environment loading time");

        setLoadingMessage("Loading cameras...");
        timeit = new Timeit().start();
        ModelHolder.getInstance().getBatiment().setCamera(CameraEngine.getInstance().getCurrentCamera());
        timeit.stop();
        System.out.println("current camera ok " + (timeit.value() * 0.001f));
        actionResolver.sendTiming(Category.LOADING, timeit.value(), "Cameras loading time");

        //modelGraph.getRoot().add(popup);

        //modelGraph.getRoot().add(popup);
        ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().add(sun);
        ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().setCamera(CameraEngine.getInstance().getCurrentCamera());

        //SceneGraphMaker.makeSceneGraph(spatialStructureTreeNode, modelGraph);

        /*** On autorise les inputs en entrée ***/
        //Gdx.input.setInputProcessor(new InputMultiplexer(stageMenu, Logic.getInstance(), this, new GestureDetector(CameraEngine.getInstance())));

        state = new DpeStateUpdater(modelGraph);

        timeit = new Timeit().start();

        mainUiControleur = MainUiControleur.getInstance();
        mainUiControleur.setStage(stageMenu);
        timeit.stop();
        System.out.println("main ui controleur ok " + (timeit.value() * 0.001f));
        actionResolver.sendTiming(Category.LOADING, timeit.value(), "Main ui controller loading time");

        setLoadingMessage("Loading menu...");
        timeit = new Timeit().start();
        stageMenu.getRoot().addCaptureListener(
                new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        if (!(event.getTarget() instanceof TextField))
                            stageMenu.setKeyboardFocus(null);
                        return false;
                    }
                });

        timeit.stop();
        System.out.println("stage menu ok " + (timeit.value() * 0.001f));
        actionResolver.sendTiming(Category.LOADING, timeit.value(), "Stage loading time");

        timeit = new Timeit().start();
        Layout layout = Layout.fromJson("data/ui/layout/mainUI.json", null);
        mainUiControleur.setMainLayout(layout);
        stageMenu.addActor(layout.getRoot());
        score = (CircularJauge)layout.getFromId("dpe_jauge");
        timeit.stop();
        System.out.println("layout ok " + (timeit.value() * 0.001f));
        actionResolver.sendTiming(Category.LOADING, timeit.value(), "Layout loading time");

        setLoadingMessage("Loading models library...");
        timeit = new Timeit().start();
        ModelLibrary.getInstance();
        timeit.stop();
        System.out.println("models library ok " + (timeit.value() * 0.001f));
        actionResolver.sendTiming(Category.LOADING, timeit.value(), "Models library loading time");

        setLoadingMessage("Loading textures library...");
        timeit = new Timeit().start();
        TextureLibrary.getInstance();
        timeit.stop();
        System.out.println("textures library ok " + (timeit.value() * 0.001f));
        actionResolver.sendTiming(Category.LOADING, timeit.value(), "Textures library loading time");

        setLoadingMessage("Setting fake dpes...");
        timeit = new Timeit().start();
        kartoffelator = new DpeKartoffelator();
        timeit.stop();
        System.out.println("fake dpes ok " + (timeit.value() * 0.001f));

        setLoadingMessage("Initializing EE...");
        timeit = new Timeit().start();
        state = new DpeStateUpdater(modelGraph);

        score.setCurrentValue(Dpe.getInstance().getScoreDpe());

        DpeUi.getPropertyWindow(DpeEvent.INFOS_GENERALES);
        DpeUi.getPropertyWindow(DpeEvent.INFOS_CHAUFFAGE);
        timeit.stop();
        System.out.println("EE ok " + (timeit.value() * 0.001f));
        actionResolver.sendTiming(Category.LOADING, timeit.value(), "EE loading time");

        setLoadingMessage("Setting input processor...");
        timeit = new Timeit().start();

        /*** On autorise les inputs en entrée ***/
        Gdx.input.setInputProcessor(new InputMultiplexer(stageMenu, Logic.getInstance(), this, new GestureDetector(CameraEngine.getInstance())));
        timeit.stop();
        System.out.println("input processor ok " + (timeit.value() * 0.001f));
        actionResolver.sendTiming(Category.LOADING, timeit.value(), "Input processor loading time");

        start.stop();
        System.out.println("all ok " + (start.value() * 0.001f));
        actionResolver.sendTiming(Category.LOADING, start.value(), "Global loading time");

        setLoadingMessage("Done !");
    }

    int frame = 0;

    public void act() {
        update_cam();

        //sun.local_transform.mulLeft(new Matrix4().idt().rotate(0,-1,1,.5f));

        Vector3 light_dir = new Vector3();
        light_dir = sun.local_transform.getTranslation(light_dir).scl(-1).nor();
        light.direction.set(light_dir);

        score.setConsignValue(Dpe.getInstance().getScoreDpe());

        if (++frame%60 == 0)
            kartoffelator.calculate_all();

        synchronized (stageMenu) {
            stageMenu.act();
        }
        modelGraph.act();
        ModelHolder.getInstance().getBatiment().act();
    }

    private void renderStartScreen() {
        synchronized (loadingStage) {
            loadingStage.draw();
        }
    }

    @Override
	public void render () {
        if (!loading_finished) {
            renderStartScreen();
            return;
        }

        act();

        if (modelBatchOpaque == null)
            modelBatchOpaque = new ModelBatch(shaderProvider);
        if (modelBatchTransparent == null)
            modelBatchTransparent = new ModelBatch(shaderProvider, new BaseSorter());

        modelBatchOpaque.begin(CameraEngine.getInstance().getCurrentCamera());
        Gdx.gl.glClearColor(0.12f, 0.38f, 0.55f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //Gdx.gl.glDisable(Gdx.gl.GL_BLEND);

        Gdx.gl.glEnable(Gdx.gl.GL_DEPTH_TEST);
        Gdx.gl.glDepthMask(true);

        //modelGraph.draw(modelBatchOpaque, environnement);
        ModelHolder.getInstance().getBatiment().draw(modelBatchOpaque, environnement, ModelContainer.Type.OPAQUE);

        modelBatchOpaque.end();

        modelBatchTransparent.begin(CameraEngine.getInstance().getCurrentCamera());

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //Gdx.gl.glDisable(Gdx.gl.GL_BLEND);

        Gdx.gl.glEnable(Gdx.gl.GL_DEPTH_TEST);
        Gdx.gl.glDepthMask(false);

        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

        ModelHolder.getInstance().getBatiment().draw(modelBatchTransparent, environnement, ModelContainer.Type.TRANSPARENT);

        modelBatchTransparent.end();

        //Gdx.gl.glDisable(Gdx.gl.GL_DEPTH_TEST);
        synchronized (stageMenu) {
            stageMenu.draw();
        }
    }

    @Override
    public void dispose() {
        EventManager.getInstance().stop();
        AssetManager.getInstance().dispose();
    }

    @Override
    public void resize(int width, int height) {
        CameraEngine.getInstance().updateViewport(height, width);
        stageMenu.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.C:
                ncam++;
                modelGraph.setCamera(CameraEngine.getInstance().getCurrentCamera());
                ModelHolder.getInstance().getBatiment().setCamera(CameraEngine.getInstance().getCurrentCamera());
                return true;
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    private int last_screenX = 0;
    private int last_screenY = 0;
    private boolean dragged = false;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        last_screenX = screenX;
        last_screenY = screenY;
        dragged = false;
        return false;
    }

    static ModelContainer selected = null;

    public static ModelContainer getSelected() {
        return selected;
    }

    public static void setSelected(ModelContainer model) {
        if (selected != null);
            selected.setSelected(false);
        selected = model;
        if (selected != null);
            selected.setSelected(true);
    }

    public static void deselect() {
        MainUiControleur.getInstance().removeTb();
        if (selected != null) {
            selected.removeColor();
            selected.setSelected(false);
            EventManager.getInstance().put(Channel.UI, new Event(UiEvent.ITEM_DESELECTED, selected));
        }
        selected = null;
//        EventManager.getInstance().put(Channel.UI, new Event(UiEvent.ITEM_SELECTED, null));
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (!dragged) {
            mainUiControleur.removeTb();
            mainUiControleur.uncheckAll();
            if (selected != null) {
                selected.removeColor();
                selected.setSelected(false);
                EventManager.getInstance().put(Channel.UI, new Event(UiEvent.ITEM_DESELECTED, selected));
                //selected.remove(pin);
            }/*
            //selected = modelGraph.getObject(screenX, screenY);
            ModelContainer next = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getObject(screenX, screenY);
            //System.out.println("TOUCH: " + selected);
            if (next != null && next != selected) {
                selected = next;
                EventManager.getInstance().put(Channel.UI, new Event(UiEvent.ITEM_SELECTED, selected));
//                System.out.println("TOUCH: " + selected.getUserData());
//                System.out.println(selected);
                selected.setColor(Color.YELLOW);
                selected.setSelected(true);
                mainUiControleur.addTb(dpeui.getPropertyWindow(selected), "Properties");
                //selected.add(pin);
                //pin.local_transform.setToTranslation(selected.getTop());
            } else {
                selected = null;
            }*/
            return selected != null;
        }
        return false;
    }

    int DRAG_EPSILON = 5;
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (Math.abs(screenX - last_screenX) > DRAG_EPSILON || Math.abs(screenY - last_screenY) > DRAG_EPSILON )
            dragged = true;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {

        Camera camera = CameraEngine.getInstance().getCurrentCamera();
        if (camera instanceof OrthographicCamera) {
            OrthographicCamera oc = (OrthographicCamera) camera;
            oc.zoom = oc.zoom * (1+amount/10f);
        }
        return true;
    }

    public void print(DefaultMutableTreeNode treeNode, int tab) {

        //System.out.print(new String(new char[tab]).replace('\0', ' '));

        //System.out.println(treeNode.getUserObject() + " [" + treeNode.getUserObject().getClass().getName() + "]");

        for (int i = 0; i < treeNode.getChildCount(); i++) {
            DefaultMutableTreeNode currentTreeNode = (DefaultMutableTreeNode)treeNode.getChildAt(i);
            print(currentTreeNode, tab + 1);
        }
    }

    private void update_cam() {
        if (CameraEngine.getInstance().getCurrentCamera() instanceof PerspectiveCamera) {
            CameraEngine.getInstance().getCurrentCameraUpdater().act();
        }
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}