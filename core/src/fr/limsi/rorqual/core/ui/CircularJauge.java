package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;

import fr.limsi.rorqual.core.view.shaders.ProgressBarShader;

/**
 * Created by christophe on 01/09/15.
 */
public class CircularJauge extends ProgressBar {


    private enum Score {
        // 0 --A-- 50 --B-- 90 --C-- 150 --D-- 230 --E-- 330 --F-- 450 --G--
        A(1,50,Color.BLACK),
        B(51, 90,Color.BLUE),
        C(91,150,Color.CLEAR),
        D(151,230,Color.CYAN),
        E(231,330,Color.DARK_GRAY),
        F(331,450,Color.GRAY),
        G(451,700, Color.GREEN),
        ;

        Color color;
        Color background;
        int min, max;

        static {
            A.background = B.color;
            B.background = C.color;
            C.background = D.color;
            D.background = E.color;
            E.background = F.color;
            F.background = G.color;
            G.background = Color.WHITE;
        }

        Score(int min, int max, Color color) {
            this.color = color;
            this.min = min;
            this.max = max;
        }

        public static Score getScore(int value) {
            for (Score score : Score.values())
                if (value >= score.min && value <= score.max) return score;
            return G;
        }

        public Color getColor() {
            return color;
        }

        public Color getBackgroundColor() {
            return background;
        }
    }

    Texture background;
    Texture foreground;
    ProgressBarShader shader;

    public CircularJauge(Texture background, Texture foreground, Skin skin) {
        super(0, 1, 20, true, skin);
        this.background = background;
        this.foreground = foreground;
        shader = new ProgressBarShader(false);
    }

    private CircularJauge(float min, float max, float stepSize, boolean vertical, ProgressBar.ProgressBarStyle style) {
        super(min, max, stepSize, vertical, style);
    }

    private CircularJauge(float min, float max, float stepSize, boolean vertical, Skin skin) {
        this(min, max, stepSize, vertical, skin.get("default-vertical", ProgressBarStyle.class));
    }

    private CircularJauge(float min, float max, float stepSize, boolean vertical, Skin skin, java.lang.String styleName) {
        this(min, max, stepSize, vertical, skin.get(styleName, ProgressBarStyle.class));
    }

    float position;
    Score currentScore = Score.G;
    @Override
    public boolean setValue (float value) {
        Score score = Score.getScore((int)value);
        if (currentScore != score) {
            float min = score.min;
            float max = score.max;
            currentScore = score;
            this.setRange(min, max);
        }
        return super.setValue(value);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {

        Color color = getColor();
        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();

        Score score = Score.getScore((int)this.getVisualValue());

        float min = score.min;
        float max = score.max;
        this.setRange(min, max);

        float percent = getVisualPercent();

        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        // generate drawables

        batch.setShader(shader.getProgram());
        shader.setFirstColor(score.getBackgroundColor());
        shader.setSecondColor(score.getColor());
        shader.setRatio(percent);

        batch.draw(background, x, y, width, height);
        batch.setShader(null);
        batch.draw(foreground, x, y, width, height);

    }
}
