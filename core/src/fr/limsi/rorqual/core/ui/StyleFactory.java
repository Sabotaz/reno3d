package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import fr.limsi.rorqual.core.utils.AssetManager;

/**
 * Created by christophe on 28/07/15.
 */
public class StyleFactory {

    public static TextButton.TextButtonStyle getTextButtonStyle(String ... params) {
        assert params.length == 4;

        Drawable up = getDrawable(params[0]);
        Drawable down = getDrawable(params[1]);
        Drawable checked = getDrawable(params[2]);
        BitmapFont font = getFont(params[3]);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(up, down, checked, font);
        return style;
    }

    private static Drawable getDrawable(String id) {

        if (id == null)
            return null;

        // atlas:ui_001.atlas:chauffage:tint:0.8,0.8,0.8,1.0
        // skin:uiskin.json:default-round-down

        String[] tokens = id.split(":");
        Drawable drawable = null;
        switch (tokens[0]) {
            case "atlas":
                TextureAtlas atlas = (TextureAtlas) AssetManager.getInstance().get(tokens[1]);
                TextureAtlas.AtlasRegion region = atlas.findRegion(tokens[2]);
                TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(region);
                drawable = textureRegionDrawable;
                if (tokens.length > 4 && tokens[3].equals("tint"))
                    drawable = textureRegionDrawable.tint(Color.valueOf(tokens[4]));
                break;
            case "skin":
                Skin skin = (Skin) AssetManager.getInstance().get(tokens[1]);
                drawable = skin.getDrawable(tokens[2]);
                break;
        }

        return drawable;
    }

    private static BitmapFont getFont(String id) {
        return (BitmapFont)AssetManager.getInstance().get(id);
    }
}
