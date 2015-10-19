package fr.limsi.rorqual.core.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.HashMap;
import java.util.Map;

import fr.limsi.rorqual.core.view.shaders.ProgressBarShader;

/**
 * Created by christophe on 23/06/15.
 */
// Singleton
// Classe permetant le chargement et la centralisation des assets
public class AssetManager {


    private AssetManager assetManager = null;
    private DefaultMutableTreeNode spatialStructureTreeNode = new DefaultMutableTreeNode("no model loaded");

    private AssetManager() {

    }

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

    private Object loadOnUi(String name, Class type) {
        return loadOnUi(Gdx.files.internal(name), type);
    }

    private Object loadOnUi(HashMap<String, String> names, Class type) {
        HashMap<String, FileHandle> files = new HashMap<String, FileHandle>();
        for (Map.Entry<String, String> entry : names.entrySet()) {
            files.put(entry.getKey(), Gdx.files.internal(entry.getValue()));
        }
        return loadFilesOnUi(files, type);
    }

    private Object loadFilesOnUi(final HashMap<String, FileHandle> files, final Class type) {
        final Holder holder = new Holder();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (type == Texture.class) {
                    HashMap<String, Texture> map = new HashMap<String, Texture>();
                    for (Map.Entry<String, FileHandle> entry : files.entrySet()) {
                        map.put(entry.getKey(), new Texture(entry.getValue()));
                    }
                    holder.set(map);
                }
                else if (type == TextureAtlas.class) {
                    HashMap<String, TextureAtlas> map = new HashMap<String, TextureAtlas>();
                    for (Map.Entry<String, FileHandle> entry : files.entrySet()) {
                        map.put(entry.getKey(), new TextureAtlas(entry.getValue()));
                    }
                    holder.set(map);
                }
                else if (type == Skin.class) {
                    HashMap<String, Skin> map = new HashMap<String, Skin>();
                    for (Map.Entry<String, FileHandle> entry : files.entrySet()) {
                        map.put(entry.getKey(), new Skin(entry.getValue()));
                    }
                    holder.set(map);
                }
                else if (type == BitmapFont.class) {
                    HashMap<String, BitmapFont> map = new HashMap<String, BitmapFont>();
                    for (Map.Entry<String, FileHandle> entry : files.entrySet()) {
                        map.put(entry.getKey(), new BitmapFont(entry.getValue()));
                    }
                    holder.set(map);
                }
            }
        };

        Gdx.app.postRunnable(runnable);

        return holder.get();
    }

    private Object loadOnUi(final FileHandle file, final Class type) {
        final Holder holder = new Holder();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (type == Texture.class)
                    holder.set(new Texture(file));
                else if (type == TextureAtlas.class)
                    holder.set(new TextureAtlas(file));
                else if (type == Skin.class)
                    holder.set(new Skin(file));
                else if (type == BitmapFont.class)
                    holder.set(new BitmapFont(file));
            }
        };
        Gdx.app.postRunnable(runnable);

        return holder.get();
    }

    private Object loadOnUi(final FreeTypeFontGenerator generator, final FreeTypeFontGenerator.FreeTypeFontParameter parameter) {

        final Holder holder = new Holder();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                holder.set(generator.generateFont(parameter));
            }
        };
        Gdx.app.postRunnable(runnable);

        return holder.get();
    }

    public void init() {
        float density = Gdx.graphics.getDensity();
        int dpi = (int)(160 * density);
        TextureAtlas atlas;
        System.out.println(density);
        if (dpi <= 140) { // ldpi ~ 0.6x
            assets.put("uiskin", loadOnUi("data/ui/ldpi/uiskin.json", Skin.class));
            assets.put("uiskin.json", loadOnUi("data/ui/ldpi/uiskin.json", Skin.class));
            atlas = (TextureAtlas)loadOnUi("data/ui/ldpi/ui_001.atlas", TextureAtlas.class);
        } else if (dpi <= 200) { // mdpi ~ 1x
            assets.put("uiskin", loadOnUi("data/ui/mdpi/uiskin.json", Skin.class));
            assets.put("uiskin.json", loadOnUi("data/ui/mdpi/uiskin.json", Skin.class));
            atlas = (TextureAtlas)loadOnUi("data/ui/mdpi/ui_001.atlas", TextureAtlas.class);
        } else if (dpi <= 380) { // hdpi ~ 1.5x
            assets.put("uiskin", loadOnUi("data/ui/hdpi/uiskin.json", Skin.class));
            assets.put("uiskin.json", loadOnUi("data/ui/hdpi/uiskin.json", Skin.class));
            atlas = (TextureAtlas)loadOnUi("data/ui/hdpi/ui_001.atlas", TextureAtlas.class);
        } else { //xhdpi ~ 2x
            assets.put("uiskin", loadOnUi("data/ui/xhdpi/uiskin.json", Skin.class));
            assets.put("uiskin.json", loadOnUi("data/ui/xhdpi/uiskin.json", Skin.class));
            atlas = (TextureAtlas)loadOnUi("data/ui/xhdpi/ui_001.atlas", TextureAtlas.class);
        }

        assets.put("ui_001.atlas", atlas);
        TextureAtlas.AtlasRegion region = atlas.findRegion("ask");
        TextureRegionDrawable drawable = new TextureRegionDrawable(region);

        NinePatch patch = atlas.createPatch("ask");
        assets.put("ask", patch);
        assets.put("ask_texture", region.getTexture());

        assets.put("bulle", loadOnUi("data/ui/bulle.png", Texture.class));

        assets.put("black.fnt", loadOnUi("data/font/black.fnt", BitmapFont.class));

        HashMap<String, String> textures = new HashMap<String, String>();

        textures.put("textureStartDpe", "data/img/dpe/StartDpe.png");
        textures.put("textureTypeBatiment1", "data/img/dpe/TypeBatiment/maison.png");
        textures.put("textureTypeBatiment2", "data/img/dpe/TypeBatiment/appt.png");
        textures.put("textureNbNiveau1", "data/img/dpe/NbNiveaux/plainPied.png");
        textures.put("textureNbNiveau2", "data/img/dpe/NbNiveaux/plainPiedCa.png");
        textures.put("textureNbNiveau3", "data/img/dpe/NbNiveaux/r+1.png");
        textures.put("textureNbNiveau4", "data/img/dpe/NbNiveaux/r+1Ca.png");
        textures.put("textureNbNiveau5", "data/img/dpe/NbNiveaux/r+2.png");
        textures.put("textureForme1", "data/img/dpe/FormeMaison/carre.png");
        textures.put("textureForme2", "data/img/dpe/FormeMaison/allongee.png");
        textures.put("textureForme3", "data/img/dpe/FormeMaison/developpee.png");
        textures.put("textureMit1", "data/img/dpe/Mitoyennete/independante.png");
        textures.put("textureMit2", "data/img/dpe/Mitoyennete/accoleePetitCote.png");
        textures.put("textureMit3", "data/img/dpe/Mitoyennete/accoleeUnGrandOuDeuxPetits.png");
        textures.put("textureMit4", "data/img/dpe/Mitoyennete/accoleeUnGrandEtUnPetit.png");
        textures.put("textureMit5", "data/img/dpe/Mitoyennete/accoleeDeuxGrandsCotes.png");
        textures.put("texturePosAppt1", "data/img/dpe/PositionAppartement/1erEtage.png");
        textures.put("texturePosAppt2", "data/img/dpe/PositionAppartement/etageInt.png");
        textures.put("texturePosAppt3", "data/img/dpe/PositionAppartement/dernierEtage.png");
        textures.put("textureWindowMateriauBois", "data/img/dpe/Fenetre/Materiaux/bois.png");
        textures.put("textureWindowMateriauMetallique", "data/img/dpe/Fenetre/Materiaux/metallique.png");
        textures.put("textureWindowMateriauPvc", "data/img/dpe/Fenetre/Materiaux/pvc.png");
        textures.put("textureWindowTypeBattante", "data/img/dpe/Fenetre/Type/battante.png");
        textures.put("textureWindowTypeCoulissante", "data/img/dpe/Fenetre/Type/coulissante.png");
        textures.put("textureBoutonChauffage", "data/img/menuPrincipal/chauffage.jpeg");
        textures.put("textureOrientationNord", "data/img/dpe/Orientation/nord.png");
        textures.put("textureOrientationEst", "data/img/dpe/Orientation/est.png");
        textures.put("textureOrientationSud", "data/img/dpe/Orientation/sud.png");
        textures.put("textureOrientationOuest", "data/img/dpe/Orientation/ouest.png");
        textures.put("roulette", "data/ui/roulette.png");
        textures.put("roulette_arriere", "data/ui/roulette_arriere.png");
        textures.put("bar", "data/ui/bar.png");
        textures.put("bar_background", "data/ui/bar_background.png");
        textures.put("camera", "data/ui/camera.png");
        textures.put("2D", "data/ui/2D.png");
        textures.put("3D", "data/ui/3D.png");
        textures.put("gyro", "data/ui/gyro.png");
        textures.put("close", "data/ui/close.png");
        textures.put("delete", "data/ui/delete.png");
        textures.put("mobilier", "data/ui/mobilier.png");
        textures.put("move", "data/ui/move.png");
        textures.put("room", "data/ui/room.png");
        textures.put("wall", "data/ui/wall.png");
        textures.put("porte", "data/ui/porte.png");
        textures.put("fenetre", "data/ui/fenetre.png");
/*
        UBJsonReader jsonReader = new UBJsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        assets.put("modelDoor", modelLoader.loadModel(Gdx.files.getFileHandle("data/blender/Door5.g3db", Files.FileType.Internal)));
        assets.put("modelWindow", modelLoader.loadModel(Gdx.files.getFileHandle("data/blender/window7.g3db", Files.FileType.Internal)));
        assets.put("modelWindowTest", modelLoader.loadModel(Gdx.files.getFileHandle("data/blender/window7.g3db", Files.FileType.Internal)));
        assets.put("fenetre_coulissante", modelLoader.loadModel(Gdx.files.getFileHandle("data/blender/fenetre_coulissante.g3db", Files.FileType.Internal)));
*/
        FileHandle file = Gdx.files.internal("data/textures/");
        if (file.isDirectory())
            for (FileHandle f : file.list()) {
                textures.put(f.nameWithoutExtension(), f.path());
            }

        assets.putAll((Map<String, Texture>)loadOnUi(textures, Texture.class));

        // fonts
        BitmapFont font,fontTitle;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/fonts/FreeSans.ttf"));
        FreeTypeFontGenerator generatorTitle = new FreeTypeFontGenerator(Gdx.files.internal("data/fonts/FreeSansBold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (20 * Gdx.graphics.getDensity());

        font = (BitmapFont) loadOnUi(generator, parameter);
        parameter.size = (int) (25 * Gdx.graphics.getDensity());
        fontTitle = (BitmapFont) loadOnUi(generatorTitle, parameter);

        //generator.dispose(); // don't forget to dispose to avoid memory leaks!
        assets.put("default.fnt", font);
        assets.put("defaultTitle.fnt", fontTitle);
        assets.put("default.fnt.generator", generator);

    }

    public void dispose() {
        for (Map.Entry<String, Object> entry : assets.entrySet()) {
            if (entry.getValue() instanceof Texture)
                ((Texture)entry.getValue()).dispose();
            else if (entry.getValue() instanceof BitmapFont)
                ((BitmapFont)entry.getValue()).dispose();
            else if (entry.getValue() instanceof FreeTypeFontGenerator)
                ((FreeTypeFontGenerator)entry.getValue()).dispose();
            else if (entry.getValue() instanceof TextureAtlas)
                ((TextureAtlas)entry.getValue()).dispose();
        }
    }

    public Object get(String s) {
        return assets.get(s);
    }
}
