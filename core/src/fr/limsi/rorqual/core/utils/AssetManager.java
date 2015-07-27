package fr.limsi.rorqual.core.utils;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.UBJsonReader;

import java.util.HashMap;

/**
 * Created by christophe on 23/06/15.
 */
public class AssetManager {


    private AssetManager assetManager = null;
    private DefaultMutableTreeNode spatialStructureTreeNode = new DefaultMutableTreeNode("no model loaded");

    public AssetManager() {}

    /** Holder */
    private static class AssetManagerHolder
    {
        /** Instance unique non préinitialisée */
        private final static AssetManager INSTANCE = new AssetManager();
    }

    public static synchronized AssetManager getInstance() {
        return AssetManagerHolder.INSTANCE;
    }

    HashMap<String, Object> assets = new HashMap<String, Object>();

    public void init() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/ui/ui_001.atlas"));
        TextureAtlas.AtlasRegion region = atlas.findRegion("ask");
        System.out.println(atlas.getRegions().size);
        System.out.println(atlas.getTextures().size);
        TextureRegionDrawable drawable = new TextureRegionDrawable(region);

        NinePatch patch = atlas.createPatch("ask");
        assets.put("ask", patch);
        assets.put("ask_texture", region.getTexture());

        assets.put("bulle", new Texture(Gdx.files.internal("data/ui/bulle.png")));

        assets.put("black.fnt", new BitmapFont(Gdx.files.internal("data/font/black.fnt")));

        assets.put("uiskin", new Skin(Gdx.files.internal("data/ui/uiskin.json")));
        assets.put("textureStartDpe", new Texture(Gdx.files.internal("data/img/dpe/StartDpe.png")));
        assets.put("textureTypeBatiment1", new Texture(Gdx.files.internal("data/img/dpe/TypeBatiment/maison.png")));
        assets.put("textureTypeBatiment2", new Texture(Gdx.files.internal("data/img/dpe/TypeBatiment/appt.png")));
        assets.put("textureNbNiveau1", new Texture(Gdx.files.internal("data/img/dpe/NbNiveaux/plainPied.png")));
        assets.put("textureNbNiveau2", new Texture(Gdx.files.internal("data/img/dpe/NbNiveaux/plainPiedCa.png")));
        assets.put("textureNbNiveau3", new Texture(Gdx.files.internal("data/img/dpe/NbNiveaux/r+1.png")));
        assets.put("textureNbNiveau4", new Texture(Gdx.files.internal("data/img/dpe/NbNiveaux/r+1Ca.png")));
        assets.put("textureNbNiveau5", new Texture(Gdx.files.internal("data/img/dpe/NbNiveaux/r+2.png")));
        assets.put("textureForme1", new Texture(Gdx.files.internal("data/img/dpe/FormeMaison/carre.png")));
        assets.put("textureForme2", new Texture(Gdx.files.internal("data/img/dpe/FormeMaison/allongee.png")));
        assets.put("textureForme3", new Texture(Gdx.files.internal("data/img/dpe/FormeMaison/developpee.png")));
        assets.put("textureMit1", new Texture(Gdx.files.internal("data/img/dpe/Mitoyennete/independante.png")));
        assets.put("textureMit2", new Texture(Gdx.files.internal("data/img/dpe/Mitoyennete/accoleePetitCote.png")));
        assets.put("textureMit3", new Texture(Gdx.files.internal("data/img/dpe/Mitoyennete/accoleeUnGrandOuDeuxPetits.png")));
        assets.put("textureMit4", new Texture(Gdx.files.internal("data/img/dpe/Mitoyennete/accoleeUnGrandEtUnPetit.png")));
        assets.put("textureMit5", new Texture(Gdx.files.internal("data/img/dpe/Mitoyennete/accoleeDeuxGrandsCotes.png")));
        assets.put("texturePosAppt1", new Texture(Gdx.files.internal("data/img/dpe/PositionAppartement/1erEtage.png")));
        assets.put("texturePosAppt2", new Texture(Gdx.files.internal("data/img/dpe/PositionAppartement/etageInt.png")));
        assets.put("texturePosAppt3", new Texture(Gdx.files.internal("data/img/dpe/PositionAppartement/dernierEtage.png")));
        assets.put("textureWindowMateriauBois", new Texture(Gdx.files.internal("data/img/dpe/Fenetre/Materiaux/bois.png")));
        assets.put("textureWindowMateriauMetallique", new Texture(Gdx.files.internal("data/img/dpe/Fenetre/Materiaux/metallique.png")));
        assets.put("textureWindowMateriauPvc", new Texture(Gdx.files.internal("data/img/dpe/Fenetre/Materiaux/pvc.png")));
        assets.put("textureWindowTypeBattante", new Texture(Gdx.files.internal("data/img/dpe/Fenetre/Type/battante.png")));
        assets.put("textureWindowTypeCoulissante", new Texture(Gdx.files.internal("data/img/dpe/Fenetre/Type/coulissante.png")));
        assets.put("textureBoutonChauffage", new Texture(Gdx.files.internal("data/img/menuPrincipal/chauffage.jpeg")));
        assets.put("textureOrientationNord", new Texture(Gdx.files.internal("data/img/dpe/Orientation/nord.png")));
        assets.put("textureOrientationEst", new Texture(Gdx.files.internal("data/img/dpe/Orientation/est.png")));
        assets.put("textureOrientationSud", new Texture(Gdx.files.internal("data/img/dpe/Orientation/sud.png")));
        assets.put("textureOrientationOuest", new Texture(Gdx.files.internal("data/img/dpe/Orientation/ouest.png")));

        UBJsonReader jsonReader = new UBJsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        assets.put("modelDoor", modelLoader.loadModel(Gdx.files.getFileHandle("data/blender/Door5.g3db", Files.FileType.Internal)));
        assets.put("modelWindow", modelLoader.loadModel(Gdx.files.getFileHandle("data/blender/window7.g3db", Files.FileType.Internal)));
        assets.put("modelWindowTest", modelLoader.loadModel(Gdx.files.getFileHandle("data/blender/window7.g3db", Files.FileType.Internal)));

        FileHandle file = Gdx.files.internal("data/textures/");
        if (file.isDirectory())
            for (FileHandle f : file.list()) {
                assets.put(f.nameWithoutExtension(), new Texture(f));
            }

        // fonts
        BitmapFont font;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/fonts/FreeSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 14;
        font = generator.generateFont(parameter); // font size 24*d pixels
        //generator.dispose(); // don't forget to dispose to avoid memory leaks!
        assets.put("default.fnt", font);
        assets.put("default.fnt.generator", generator);

    }

    public Object get(String s) {
        return assets.get(s);
    }
}
