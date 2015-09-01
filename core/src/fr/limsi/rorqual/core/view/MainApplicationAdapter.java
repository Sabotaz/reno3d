package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
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
import com.badlogic.gdx.math.Matrix4;
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

import fr.limsi.rorqual.core.dpe.Dpe;
import fr.limsi.rorqual.core.dpe.DpeStateUpdater;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.event.UiEvent;
import fr.limsi.rorqual.core.logic.CameraEngine;
import fr.limsi.rorqual.core.logic.Logic;
import fr.limsi.rorqual.core.model.Batiment;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.ui.DpeUi;
import fr.limsi.rorqual.core.ui.Layout;
import fr.limsi.rorqual.core.ui.MainUiControleur;
import fr.limsi.rorqual.core.ui.ModelLibrary;
import fr.limsi.rorqual.core.ui.Popup;
import fr.limsi.rorqual.core.utils.AssetManager;
import fr.limsi.rorqual.core.utils.DefaultMutableTreeNode;

import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;
import fr.limsi.rorqual.core.view.shaders.ShaderChooser;

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
    private Dpe dpe;
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

    private boolean loading_finished = false;

    @Override
	public void create () {

        createLoadingStage();

        Thread t = new Thread() {
            public void run() {
                load();

                try {
                    Thread.sleep(1_000);
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

        Image image = new Image(new Texture(Gdx.files.internal("data/img/loading.jpg")));
        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(Gdx.files.internal("data/font/black.fnt")), Color.BLACK);
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
        long start = System.currentTimeMillis();
        System.out.println("start init " + ((System.currentTimeMillis() - start) * 0.001f));

        setLoadingMessage("Starting event manager...");
        EventManager.getInstance().start();
        System.out.println("event manager ok " + ((System.currentTimeMillis() - start) * 0.001f));

        setLoadingMessage("Loading assets...");
        assets = AssetManager.getInstance();
        assets.init();
        System.out.println("assets ok " + ((System.currentTimeMillis() - start) * 0.001f));

        setLoadingMessage("Loading building...");
        ModelHolder.getInstance().setBatiment(new Batiment());
        System.out.println("batiment ok " + ((System.currentTimeMillis() - start) * 0.001f));

        setLoadingMessage("Loading shader engine...");
        shaderProvider = new ShaderChooser();
        System.out.println("shaders ok " + ((System.currentTimeMillis()-start)*0.001f));

        modelGraph = new ModelGraph();
        modelGraph.setCamera(CameraEngine.getInstance().getCurrentCamera());
//        stageMenu.setDebugAll(true);
        //System.out.println(stageMenu.getWidth());

        /*** Création des lumières ***/
        setLoadingMessage("Loading environment...");
        environnement = new Environment();
        environnement.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        light = new DirectionalLight();
        light.set(1f, 1f, 1f, 0f, 0f, 0f);
        environnement.add(light);
        System.out.println("environnement ok " + ((System.currentTimeMillis() - start) * 0.001f));

        sun = new ModelContainer();
        sun.setSelectable(false);
        sun.local_transform.setToTranslation(new Vector3(-200, 0, 0));

        setLoadingMessage("Loading cameras...");
        ModelHolder.getInstance().getBatiment().setCamera(CameraEngine.getInstance().getCurrentCamera());
        System.out.println("current camera ok " + ((System.currentTimeMillis() - start) * 0.001f));

        //modelGraph.getRoot().add(popup);

        //modelGraph.getRoot().add(popup);
        ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().add(sun);
        ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().setCamera(CameraEngine.getInstance().getCurrentCamera());

        //SceneGraphMaker.makeSceneGraph(spatialStructureTreeNode, modelGraph);

        /*** On autorise les inputs en entrée ***/
        Gdx.input.setInputProcessor(new InputMultiplexer(stageMenu, Logic.getInstance(), this, new GestureDetector(CameraEngine.getInstance())));

        state = new DpeStateUpdater(modelGraph);

        mainUiControleur = MainUiControleur.getInstance();
        mainUiControleur.setStage(stageMenu);
        System.out.println("main ui controleur ok " + ((System.currentTimeMillis() - start) * 0.001f));

        setLoadingMessage("Loading menu...");
        stageMenu.getRoot().addCaptureListener(
                new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        if (!(event.getTarget() instanceof TextField))
                            stageMenu.setKeyboardFocus(null);
                        return false;
                    }
                });

        System.out.println("stage menu ok " + ((System.currentTimeMillis() - start) * 0.001f));
        stageMenu.addActor(Layout.fromJson("data/ui/layout/mainUI.json", null).getRoot());
        System.out.println("layout ok " + ((System.currentTimeMillis() - start) * 0.001f));

        setLoadingMessage("Initializing EE...");

        state = new DpeStateUpdater(modelGraph);

        dpe=Dpe.getInstance();
        double scoreDpe=dpe.getScoreDpe();
        skin = (Skin)AssetManager.getInstance().get("uiskin");
        labelScore= new Label("("+Double.toString(scoreDpe)+")",skin);
        stageMenu.addActor(labelScore);
        lettreScore=new Label("F",skin);
        labelScore.setPosition(20, 0);
        stageMenu.addActor(lettreScore);
        System.out.println("dpe ok " + ((System.currentTimeMillis() - start) * 0.001f));

        setLoadingMessage("Loading models library...");

        ModelLibrary.getInstance();
        System.out.println("models library ok " + ((System.currentTimeMillis() - start) * 0.001f));

        setLoadingMessage("Setting input processor...");

        /*** On autorise les inputs en entrée ***/
        Gdx.input.setInputProcessor(new InputMultiplexer(stageMenu, Logic.getInstance(), this, new GestureDetector(CameraEngine.getInstance())));
        System.out.println("input processor ok " + ((System.currentTimeMillis() - start) * 0.001f));

        setLoadingMessage("Done !");
    }

    public void act() {
        update_cam();

        sun.local_transform.mulLeft(new Matrix4().idt().rotate(0,-1,1,.5f));

        Vector3 light_dir = new Vector3();
        light_dir = sun.local_transform.getTranslation(light_dir).scl(-1).nor();
        light.direction.set(light_dir);

        double score=dpe.getScoreDpe();
        labelScore.setText("("+Double.toString(score)+")");
        if(score<=50){
            lettreScore.setText("A");
        }else if (score <=90){
            lettreScore.setText("B");
        }else if (score <=150){
            lettreScore.setText("C");
        }else if (score <=230){
            lettreScore.setText("D");
        }else if (score <=330){
            lettreScore.setText("E");
        }else if (score <=450){
            lettreScore.setText("F");
        }else if (score > 450){
            lettreScore.setText("G");
        }

        stageMenu.act();
        modelGraph.act();
        ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().act();
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

    public static ModelContainer getFromUserObject(Object o) {
        return modelGraph.getFromUserObject(o);
    }

    public static void select(Object o) {
        deselect();
        selected = modelGraph.getFromUserObject(o);
        if (selected != null) {
            //EventManager.getInstance().put(Channel.UI, new Event(UiEvent.ITEM_SELECTED, o));
            selected.setColor(Color.YELLOW);
        }
    }

    public static void deselect() {
        if (selected != null) {
            selected.removeColor();
        }
        selected = null;
        //EventManager.getInstance().put(Channel.UI, new Event(UiEvent.ITEM_SELECTED, null));
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (!dragged) {
            mainUiControleur.removeTb();
            if (selected != null) {
                selected.removeColor();
                //selected.remove(pin);
            }
            //selected = modelGraph.getObject(screenX, screenY);
            selected = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getObject(screenX, screenY);
            //System.out.println("TOUCH: " + selected);
            if (selected != null) {
                EventManager.getInstance().put(Channel.UI, new Event(UiEvent.ITEM_SELECTED, selected.getUserData()));
                //System.out.println("TOUCH: " + selected.getUserData());
                selected.setColor(Color.YELLOW);
                mainUiControleur.addTb(dpeui.getPropertyWindow(selected));
                //selected.add(pin);
                //pin.local_transform.setToTranslation(selected.getTop());
            }
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
}