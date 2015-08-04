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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.utils.viewport.Viewport;

import fr.limsi.rorqual.core.dpe.Dpe;
import fr.limsi.rorqual.core.dpe.DpeStateUpdater;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.event.UiEvent;
import fr.limsi.rorqual.core.logic.Logic;
import fr.limsi.rorqual.core.model.Batiment;
import fr.limsi.rorqual.core.model.Etage;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.ui.DpeUi;
import fr.limsi.rorqual.core.ui.Layout;
import fr.limsi.rorqual.core.ui.MainUiControleur;
import fr.limsi.rorqual.core.ui.Popup;
import fr.limsi.rorqual.core.utils.AssetManager;
import fr.limsi.rorqual.core.utils.DefaultMutableTreeNode;

import fr.limsi.rorqual.core.model.IfcHolder;
import fr.limsi.rorqual.core.utils.SceneGraphMaker;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;
import fr.limsi.rorqual.core.view.shaders.ShaderChooser;

public class MainApplicationAdapter extends InputAdapter implements ApplicationListener {

    private ShapeRenderer shape;
    private static ModelGraph modelGraph;
    private Stage stageMenu;
    private Skin skin;
    private Button buttonDPE, buttonExit;
    private BitmapFont fontBlack;
    private BitmapFont fontWhite;
    private static Camera[] cameras = new Camera[2];
    private static int ncam = 1;
    private Environment environnement;
    private ShaderProvider shaderProvider;
    private Dpe dpe;
    private DpeUi dpeui;
    private DpeStateUpdater state;
    private Model model;
    private ModelBatch modelBatch;
    private ModelInstance modelInstance;
    private AssetManager assets;
    private Viewport viewport;
    private Popup popup;
    private DirectionalLight light;
    private ModelContainer sun;
    private ModelContainer pin;
    private Vector3 decal_pos;
    PerspectiveCameraUpdater cam_updater;
    private MainUiControleur mainUiControleur;

    @Override
	public void create () {
        shape = new ShapeRenderer();

        /*** Chargement des boutons et des polices d'écriture ***/
        fontBlack = new BitmapFont(Gdx.files.internal("data/font/black.fnt"));
        fontWhite = new BitmapFont(Gdx.files.internal("data/font/white.fnt"));
        skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(skin.getDrawable("default-round"),skin.getDrawable("default-round-down"),null,fontBlack);

        AssetManager.getInstance().init();

        /*** ??? ***/
        DefaultMutableTreeNode spatialStructureTreeNode = IfcHolder.getInstance().getSpatialStructureTreeNode();

        ModelHolder.getInstance().setBatiment(new Batiment());
        ModelHolder.getInstance().getBatiment().setCurrentEtage(new Etage());

        /*** Création de la caméra 2D vue de dessus ***/
        OrthographicCamera camera1 = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera1.viewportHeight = Gdx.graphics.getHeight();
        camera1.viewportWidth = Gdx.graphics.getWidth();
        camera1.zoom = 1f/100;
        camera1.position.set(0.f,0,10f);
        camera1.lookAt(0f, 0f, 0f);
        camera1.up.set(0, 1, 0);
        camera1.update();
        cameras[0] = camera1;

        /*** Création de la caméra 3D ***/
        PerspectiveCamera camera2 = new PerspectiveCamera(30f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera2.viewportHeight = Gdx.graphics.getHeight();
        camera2.viewportWidth = Gdx.graphics.getWidth();
        camera2.position.set(0, -20, 1.65f);
        camera2.near = 1f;
        camera2.far = 10000f;
        //camera2.lookAt(0, 0, 0);
        camera2.direction.set(0,1,0);
        camera2.up.set(0, 0, 1);
        camera2.update();
        cameras[1] = camera2;

        Logic.getInstance().setCamera(camera2);

        cam_updater = new PerspectiveCameraUpdater(camera2);

        //viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera1);

        /*** Création d'un tableau de caméras ***/
        Camera baseCamera = cameras[ncam%cameras.length];

        /*** Chargement des shaders ***/
        shaderProvider = new ShaderChooser();

        modelGraph = new ModelGraph();
        modelGraph.setCamera(baseCamera);

        modelBatch = new ModelBatch(shaderProvider);

        stageMenu = new Stage();
//        stageMenu.setDebugAll(true);
        //System.out.println(stageMenu.getWidth());

        /*** Création des lumières ***/
        environnement = new Environment();
        environnement.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        light = new DirectionalLight();
        light.set(1f, 1f, 1f, 0f, 0f, 0f);
        environnement.add(light);

        sun = new ModelContainer();
        sun.setSelectable(false);
        sun.local_transform.setToTranslation(new Vector3(-200, 0, 0));

        //pin.local_transform.translate(5, 0, 5);

        //modelGraph.getRoot().add(popup);

        //modelGraph.getRoot().add(popup);
        ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().add(sun);
        ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().setCamera(camera2);

        SceneGraphMaker.makeSceneGraph(spatialStructureTreeNode, modelGraph);

        /*** On autorise les inputs en entrée ***/
        Gdx.input.setInputProcessor(new InputMultiplexer(stageMenu, Logic.getInstance(), this, cam_updater));

        state = new DpeStateUpdater(modelGraph);

        dpe = new Dpe();

        mainUiControleur = MainUiControleur.getInstance();
        mainUiControleur.setStage(stageMenu);

        stageMenu.addActor(Layout.fromJson("data/ui/layout/mainUI.json", null).getRoot());


        /*** test affichage fenetre ***/
        // A ModelBatch is like a SpriteBatch, just for models.  Use it to batch up geometry for OpenGL
        //modelBatch = new ModelBatch();
        // Model loader needs a binary json reader to decode
        UBJsonReader jsonReader = new UBJsonReader();
        // Create a model loader passing in our json reader
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        // Now load the model by name
        // Note, the model (g3db file ) and textures need to be added to the assets folder of the Android proj
        this.assets = AssetManager.getInstance();
        model = (Model)assets.get("modelWindowTest");
        // Now create an instance.  Instance holds the positioning data, etc of an instance of your model
        modelInstance = new ModelInstance(model);
        //fbx-conv is supposed to perform this rotation for you... it doesnt seem to
        //modelInstance.local_transform.rotate(1, 0, 0, -90);
        //move the model down a bit on the screen ( in a z-up world, down is -z ).
        modelInstance.transform.translate(0, 0, 4);
//        modelInstance.local_transform.scale(0.5f, 0.5f, 0.5f);

        //shader = new BillboardShader();
        //shader.init();
        //program = shader.program;
        //popup = new Popup(0,0,800,800);
        /*Actor a = LayoutReader.readLayout("data/ui/layout/wallProperties.json");
        a.setDebug(true);
        a.setPosition(300, Gdx.graphics.getHeight() - 300);
        stageMenu.addActor(a);*/
	}

    public void act() {
        update_cam();

        sun.local_transform.mulLeft(new Matrix4().idt().rotate(0,-1,1,.5f));

        Vector3 light_dir = new Vector3();
        light_dir = sun.local_transform.getTranslation(light_dir).scl(-1).nor();
        light.direction.set(light_dir);

        stageMenu.act();

        modelGraph.act();
        ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().act();
    }

    @Override
	public void render () {
        act();

        modelBatch.begin(cameras[ncam % cameras.length]);

        Gdx.gl.glClearColor(0.12f, 0.38f, 0.55f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glEnable(Gdx.gl.GL_DEPTH_TEST);

        //modelGraph.draw(modelBatch, environnement);

        ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().draw(modelBatch, environnement);

        modelBatch.end();

        //Gdx.gl.glDisable(Gdx.gl.GL_DEPTH_TEST);
        synchronized (stageMenu) {
            stageMenu.draw();
        }

        Gdx.gl.glDisable(Gdx.gl.GL_BLEND);
	}

    @Override
    public void dispose() {
        fontBlack.dispose();
        fontWhite.dispose();
        EventManager.getInstance().stop();
    }

    @Override
    public void resize(int width, int height) {
        cameras[0].viewportHeight = height;
        cameras[0].viewportWidth = width;
        cameras[1].viewportHeight = height;
        cameras[1].viewportWidth = width;
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
                modelGraph.setCamera(cameras[ncam % cameras.length]);
                ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().setCamera(cameras[ncam % cameras.length]);
                Logic.getInstance().setCamera(cameras[ncam % cameras.length]);
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
            cameras[ncam%cameras.length].position.set(selected.local_transform.getTranslation(new Vector3()).add(5, 5, 5));
            cameras[ncam%cameras.length].lookAt(selected.local_transform.getTranslation(new Vector3()).add(0, 0, 2));
            cameras[ncam%cameras.length].up.set(0, 0, 1);
            cameras[ncam%cameras.length].update();
        }
    }

    public static void deselect() {
        if (selected != null) {
            selected.removeColor();
            cameras[ncam%cameras.length].position.set(0, -20, 20);
            cameras[ncam%cameras.length].lookAt(0, 0, 0);
            cameras[ncam%cameras.length].up.set(0, 0, 1);
            cameras[ncam%cameras.length].update();
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
                mainUiControleur.addTb(dpeui.getPropertyWindow(selected.getUserData()));
                //selected.add(pin);
                //pin.local_transform.setToTranslation(selected.getTop());
            }
            return selected != null;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        dragged = true;
        int diffX = screenX - last_screenX;
        int diffY = screenY - last_screenY;
        Camera camera = cameras[ncam%cameras.length];
        if (camera instanceof OrthographicCamera) {
            OrthographicCamera oc = (OrthographicCamera) camera;
            oc.translate(-diffX * oc.zoom, diffY * oc.zoom, 0);
            last_screenX = screenX;
            last_screenY = screenY;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {

        Camera camera = cameras[ncam%cameras.length];
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
        if (cameras[ncam%cameras.length] instanceof PerspectiveCamera) {
            cam_updater.act();
        }
    }
}