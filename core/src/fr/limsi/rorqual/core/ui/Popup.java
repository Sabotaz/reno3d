package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import fr.limsi.rorqual.core.utils.AssetManager;
import scene3d.Actor3d;

/**
 * Created by christophe on 23/06/15.
 */
public class Popup extends Window {

    public Popup (Skin skin, Actor3d actor) {
        super("", skin);

        WindowStyle style = new WindowStyle((BitmapFont)AssetManager.getInstance().get("black.fnt"), Color.BLACK, (Drawable)AssetManager.getInstance().get("ask"));
        this.setStyle(style);
        this.setPosition(actor.getX(),actor.getY());
    }
}
