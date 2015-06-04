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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.limsi.rorqual.core.dpe.Dpe;
import fr.limsi.rorqual.core.dpe.DpeStateUpdater;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.ui.DpeUi;
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
    private TextButton buttonDPE, buttonExit;
    private TextButton.TextButtonStyle textButtonStyle;
    private BitmapFont fontBlack;
    private BitmapFont fontWhite;
    private static Camera[] cameras = new Camera[2];
    private static int ncam = 1;
    private Environment environnement;
    private ShaderProvider shaderProvider;
    private Dpe dpe;
    private DpeStateUpdater state;

    @Override
	public void create () {
        shape = new ShapeRenderer();

        /*** Chargement des boutons et des polices d'écriture ***/
        fontBlack = new BitmapFont(Gdx.files.internal("data/font/black.fnt"));
        fontWhite = new BitmapFont(Gdx.files.internal("data/font/white.fnt"));
        skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));
        textButtonStyle = new TextButton.TextButtonStyle(skin.getDrawable("default-round"),skin.getDrawable("default-round-down"),null,fontBlack);

        /*** ??? ***/
        DefaultMutableTreeNode spatialStructureTreeNode = IfcHolder.getInstance().getSpatialStructureTreeNode();

        /*** Création des lumières ***/
        environnement = new Environment();
        environnement.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environnement.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 0.8f, 0.4f, -0.6f));

        /*** Création de la caméra 2D vue de dessus ***/
        OrthographicCamera camera1 = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera1.zoom = 1f/10;
        camera1.position.set(0.f, 0, 10f);
        camera1.lookAt(0f, 0f, 0f);
        camera1.up.set(0, 1, 0);
        camera1.update();
        cameras[0] = camera1;

        /*** Création de la caméra 3D ***/
        PerspectiveCamera camera2 = new PerspectiveCamera(30f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera2.position.set(0, -20, 20);
        camera2.near = 1f;
        camera2.far = 1000f;
        camera2.lookAt(0, 0, 0);
        camera2.up.set(0, 0, 1);
        camera2.update();
        cameras[1] = camera2;

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

        /*** Ajout du bouton DPE ***/
        buttonDPE = new TextButton("DPE", textButtonStyle);
        buttonDPE.setName("DPE");
        buttonDPE.setSize(100, 40);
        buttonDPE.setPosition((Gdx.graphics.getWidth() - buttonDPE.getWidth()), (Gdx.graphics.getHeight() - buttonDPE.getHeight() - buttonExit.getHeight()));
        buttonDPE.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
//                buttonDPE.setVisible(false);
                dpe = new Dpe(stageMenu);
                dpe.startDPE();
            }
        });
        new DpeUi(stageMenu);
        stageMenu.addActor(buttonDPE);
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
	}

    @Override
    public void dispose() {
        fontBlack.dispose();
        fontWhite.dispose();
        EventManager.getInstance().stop();
    }

    @Override
    public void resize(int width, int height) {

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

    public static void select(Object o) {
        deselect();
        selected = stage3d.getFromUserObject(o);
        if (selected != null) {
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
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (!dragged) {
            if (selected != null)
                selected.setColor(Color.WHITE);
            selected = stage3d.getObject(screenX, screenY);
            if (selected != null) {
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