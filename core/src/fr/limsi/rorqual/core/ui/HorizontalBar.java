package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Created by christophe on 22/09/15.
 */
public class HorizontalBar extends Widget {

    public Drawable drawable;

    public static class HorizontalBarStyle {
        public Drawable drawable;
        public HorizontalBarStyle(Drawable drawable) {
            this.drawable = drawable;
        }
    }

    public HorizontalBar(HorizontalBarStyle style) {
        this.drawable = style.drawable;
    }

    public HorizontalBar(Drawable drawable) {
        this.drawable = drawable;
    }

    public void draw (Batch batch, float parentAlpha) {
        validate();

        if (drawable == null) return;

        drawable.draw(batch, this.getX(), this.getY(), getWidth(), getHeight());

        super.draw(batch, parentAlpha);
    }

}
