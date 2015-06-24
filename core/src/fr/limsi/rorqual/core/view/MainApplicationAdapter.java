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
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fr.limsi.rorqual.core.dpe.Dpe;
import fr.limsi.rorqual.core.dpe.DpeStateUpdater;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.event.UiEvent;
import fr.limsi.rorqual.core.ui.DpeUi;
import fr.limsi.rorqual.core.utils.AssetManager;
import fr.limsi.rorqual.core.utils.DefaultMutableTreeNode;

import fr.limsi.rorqual.core.model.IfcHolder;
import fr.limsi.rorqual.core.utils.SceneGraphMaker;
import scene3d.Actor3d;
import scene3d.Stage3d;

public class MainApplicationAdapter extends InputAdapter implements ApplicationListener {

    private ShapeRenderer shape;
    private static Stage3d stage3d;
    private Stage stageMenu;
    private Skin skin;
    private Button buttonDPE, buttonExit;
    private TextButton.TextButtonStyle textButtonStyle;
    private BitmapFont fontBlack;
    private BitmapFont fontWhite;
    private static Camera[] cameras = new Camera[2];
    private static int ncam = 1;
    private Environment environnement;
    private ShaderProvider shaderProvider;
    private Dpe dpe;
    private DpeStateUpdater state;
    private Model model;
    private ModelBatch modelBatch;
    private ModelInstance modelInstance;
    private Viewport viewport;

    @Override
	public void create () {
        shape = new ShapeRenderer();

        /*** Chargement des boutons et des polices d'écriture ***/
        fontBlack = new BitmapFont(Gdx.files.internal("data/font/black.fnt"));
        fontWhite = new BitmapFont(Gdx.files.internal("data/font/white.fnt"));
        skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));
        textButtonStyle = new TextButton.TextButtonStyle(skin.getDrawable("default-round"),skin.getDrawable("default-round-down"),null,fontBlack);

        AssetManager.getInstance().init();

        /*** ??? ***/
        DefaultMutableTreeNode spatialStructureTreeNode = IfcHolder.getInstance().getSpatialStructureTreeNode();

        /*** Création des lumières ***/
        environnement = new Environment();
        environnement.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environnement.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 0.8f, 0.4f, -0.6f));

        /*** Création de la caméra 2D vue de dessus ***/
        OrthographicCamera camera1 = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera1.viewportHeight = Gdx.graphics.getHeight();
        camera1.viewportWidth = Gdx.graphics.getWidth();
        camera1.zoom = 1f/10;
        camera1.position.set(0.f, 0, 10f);
        camera1.lookAt(0f, 0f, 0f);
        camera1.up.set(0, 1, 0);
        camera1.update();
        cameras[0] = camera1;

        /*** Création de la caméra 3D ***/
        PerspectiveCamera camera2 = new PerspectiveCamera(30f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera2.viewportHeight = Gdx.graphics.getHeight();
        camera2.viewportWidth = Gdx.graphics.getWidth();
        camera2.position.set(0, -20, 20);
        camera2.near = 1f;
        camera2.far = 1000f;
        camera2.lookAt(0, 0, 0);
        camera2.up.set(0, 0, 1);
        camera2.update();
        cameras[1] = camera2;

        //viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera1);

        /*** Création d'un tableau de caméras ***/
        Camera baseCamera = cameras[ncam%cameras.length];

        /*** Chargement des shaders ***/
        shaderProvider = new DefaultShaderProvider() {
            @Override
            protected Shader createShader(Renderable renderable) {
                return new LightShader(renderable);
                //return new TestShader(renderable);
                //return super.createShader(renderable);
            }
        };

        stage3d = new Stage3d(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), baseCamera, environnement, shaderProvider);
        stageMenu = new Stage();
        stageMenu.setDebugAll(true);
        System.out.println(stageMenu.getWidth());
        SceneGraphMaker.makeSceneGraph(spatialStructureTreeNode, stage3d);

        /*** On autorise les inputs en entrée ***/
        Gdx.input.setInputProcessor(new InputMultiplexer(stageMenu, this, stage3d));

        /*** Ajout du bouton EXIT ***/
        buttonExit = new TextButton("EXIT", textButtonStyle);
        buttonExit.setName("EXIT");
        buttonExit.setSize(100, 40);
        buttonExit.setPosition((Gdx.graphics.getWidth() - buttonExit.getWidth()), (Gdx.graphics.getHeight() - buttonExit.getHeight()));
        buttonExit.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        stageMenu.addActor(buttonExit);

        state = new DpeStateUpdater(stage3d);

        new DpeUi(stageMenu);
        dpe = new Dpe(stageMenu);
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/ui/ui_001.atlas"));
        TextureAtlas.AtlasRegion region = atlas.findRegion("dpe");
        TextureRegionDrawable dpe_drawable = new TextureRegionDrawable(region);
        Drawable dpe_drawable2 = dpe_drawable.tint(new Color(0.8f, 0.8f, 0.8f, 1));
        //NinePatch patch = atlas.createPatch("wall");

        textButtonStyle = new TextButton.TextButtonStyle(dpe_drawable,dpe_drawable2,null,fontBlack);

        /*** Ajout du bouton DPE ***/
        buttonDPE = new Button(textButtonStyle);
        buttonDPE.setName("DPE");
        buttonDPE.setSize(150, 150);
        buttonDPE.setPosition((Gdx.graphics.getWidth() - buttonDPE.getWidth()), (Gdx.graphics.getHeight() - buttonDPE.getHeight() - buttonExit.getHeight()));
        buttonDPE.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
//                buttonDPE.setVisible(false);
                dpe.startDPE();
            }
        });
        stageMenu.addActor(buttonDPE);

        /*** test affichage fenetre ***/
        // A ModelBatch is like a SpriteBatch, just for models.  Use it to batch up geometry for OpenGL
        modelBatch = new ModelBatch();
        // Model loader needs a binary json reader to decode
        UBJsonReader jsonReader = new UBJsonReader();
        // Create a model loader passing in our json reader
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        // Now load the model by name
        // Note, the model (g3db file ) and textures need to be added to the assets folder of the Android proj
        model = modelLoader.loadModel(Gdx.files.getFileHandle("data/blender/fenetreSimpleVentailPvc.g3db", FileType.Internal));
        // Now create an instance.  Instance holds the positioning data, etc of an instance of your model
        modelInstance = new ModelInstance(model);
        //fbx-conv is supposed to perform this rotation for you... it doesnt seem to
        //modelInstance.transform.rotate(1, 0, 0, -90);
        //move the model down a bit on the screen ( in a z-up world, down is -z ).
        modelInstance.transform.translate(0, 0, 4);
        modelInstance.transform.scale(2,2,2);
	}

	@Override
	public void render () {
        update_cam();

		Gdx.gl.glClearColor(0.12f, 0.38f, 0.55f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glEnable(Gdx.gl.GL_DEPTH_TEST);
        Gdx.gl.glDisable(Gdx.gl.GL_DEPTH_TEST);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setProjectionMatrix(cameras[ncam%cameras.length].combined);
        int grid_size = 1;
        int grid_div = 10;

        for (int i = -100; i < 100; i+=grid_size) {
            if (i % grid_div == 0)
                shape.setColor(new Color(1, 1, 1, 0.15f));
            else
                shape.setColor(new Color(1, 1, 1, 0.05f));
            shape.line(-100, i, 0, 100, i, 0);
        }
        for (int i = -100; i < 100; i+=grid_size) {
            if (i % grid_div == 0)
                shape.setColor(new Color(1, 1, 1, 0.15f));
            else
                shape.setColor(new Color(1, 1, 1, 0.05f));
            shape.line(i, -100, 0, i, 100, 0);
        }
        shape.end();

        Gdx.gl.glDisable(Gdx.gl.GL_BLEND);
        stage3d.act();
        stage3d.draw();
        stageMenu.act();
        stageMenu.draw();
        Gdx.gl.glDisable(Gdx.gl.GL_DEPTH_TEST);
        modelBatch.begin(cameras[ncam%cameras.length]);
        modelBatch.render(modelInstance, environnement);
        modelBatch.end();
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

    private static enum Sense {
        GAUCHE,
        DROITE,
        HAUT,
        BAS,
        NONE;
    }

    private Sense camera_mov = Sense.NONE;

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                camera_mov = Sense.GAUCHE;
                return true;
            case Input.Keys.RIGHT:
                camera_mov = Sense.DROITE;
                return true;
            case Input.Keys.UP:
                camera_mov = Sense.HAUT;
                return true;
            case Input.Keys.DOWN:
                camera_mov = Sense.BAS;
                return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
        //NinePatch patch = atlas.createPatch("wall");
            case Input.Keys.LEFT:
            case Input.Keys.RIGHT:
            case Input.Keys.UP:
            case Input.Keys.DOWN:
                camera_mov = Sense.NONE;
                return true;
            case Input.Keys.C:
                ncam++;
                stage3d.setCamera(cameras[ncam % cameras.length]);
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

    static Actor3d selected = null;

    public static Actor3d getFromUserObject(Object o) {
        return stage3d.getFromUserObject(o);
    }

    public static void select(Object o) {
        deselect();
        selected = stage3d.getFromUserObject(o);
        if (selected != null) {
            //EventManager.getInstance().put(Channel.UI, new Event(UiEvent.ITEM_SELECTED, o));
            selected.setColor(Color.YELLOW);
            cameras[ncam%cameras.length].position.set(selected.getTransform().getTranslation(new Vector3()).add(5, 5, 5));
            cameras[ncam%cameras.length].lookAt(selected.getTransform().getTranslation(new Vector3()).add(0, 0, 2));
            cameras[ncam%cameras.length].up.set(0, 0, 1);
            cameras[ncam%cameras.length].update();
        }
    }

    public static void deselect() {
        if (selected != null) {
            selected.setColor(Color.WHITE);
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
            if (selected != null)
                selected.setColor(Color.WHITE);
            selected = stage3d.getObject(screenX, screenY);
            if (selected != null) {
                EventManager.getInstance().put(Channel.UI, new Event(UiEvent.ITEM_SELECTED, selected.userData));
                System.out.println("TOUCH: " + selected.userData);
                selected.setColor(Color.YELLOW);
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
            oc.translate(-diffX*oc.zoom, diffY*oc.zoom, 0);
        } else {
            Vector3 before = camera.unproject(new Vector3(last_screenX, last_screenY,1)).sub(camera.position).nor();
            Vector3 after = camera.unproject(new Vector3(screenX, screenY,1)).sub(camera.position).nor();

            if (!before.isCollinear(after))
                camera.rotate(after.cpy().crs(before), (float)(Math.acos(after.dot(before)) * 180. / Math.PI));
            camera.up.set(0,0,1);
        }
        last_screenX = screenX;
        last_screenY = screenY;
        return true;
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
        } else if (camera instanceof PerspectiveCamera) {
            PerspectiveCamera pc = (PerspectiveCamera) camera;
            pc.fieldOfView = pc.fieldOfView * (1+amount/10f);
        }
        return true;
    }


    public void print(DefaultMutableTreeNode treeNode, int tab) {

        System.out.print(new String(new char[tab]).replace('\0', ' '));

        System.out.println(treeNode.getUserObject() + " [" + treeNode.getUserObject().getClass().getName() + "]");

        for (int i = 0; i < treeNode.getChildCount(); i++) {
            DefaultMutableTreeNode currentTreeNode = (DefaultMutableTreeNode)treeNode.getChildAt(i);
            print(currentTreeNode, tab + 1);
        }
    }

    private void update_cam() {
        if (cameras[ncam%cameras.length] instanceof PerspectiveCamera) {
            PerspectiveCamera camera = (PerspectiveCamera) cameras[ncam%cameras.length];
            switch (camera_mov) {
                case GAUCHE:
                    camera.rotateAround(new Vector3(), new Vector3(0,0,1), -5);
                    break;
                case DROITE:
                    camera.rotateAround(new Vector3(), new Vector3(0,0,1), 5);
                    break;
                case HAUT:
                    camera.rotateAround(new Vector3(), camera.up.cpy().crs(camera.direction), 5);
                    break;
                case BAS:
                    camera.rotateAround(new Vector3(), camera.up.cpy().crs(camera.direction), -5);
                    break;
            }
        }
    }
}