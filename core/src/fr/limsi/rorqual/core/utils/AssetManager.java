package fr.limsi.rorqual.core.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

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
        TextureRegionDrawable drawable = new TextureRegionDrawable(region);
        assets.put("ask", drawable);

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

    }

    public Object get(String s) {
        return assets.get(s);
    }

}