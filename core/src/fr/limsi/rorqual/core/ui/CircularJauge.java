package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;

import fr.limsi.rorqual.core.view.shaders.ProgressBarShader;

/**
 * Created by christophe on 01/09/15.
 */
// Jauge circulaire, utilisant le shader ProgressBarShader pour l'affichage
// Les scores suivent ceux du DPE
// TODO: couleurs, affichage texte ?
public class CircularJauge extends Widget {


    private enum Score {
        // 0 --A-- 50 --B-- 90 --C-- 150 --D-- 230 --E-- 330 --F-- 450 --G--
        /*A(1,50,Color.valueOf("810f7c")),
        B(51, 90,Color.valueOf("8856a7")),
        C(91,150,Color.valueOf("8c96c6")),
        D(151,230,Color.valueOf("b3cde3")),
        E(231,330,Color.valueOf("edf8fb")),
        F(331,450,Color.BLACK),
        G(451,700,Color.BLACK),
        ;*/
        X(1,450,Color.valueOf("810f7c")),
        ;

        Color color;
        Color background;
        int min, max;

        static {/*
            A.background = B.color;
            B.background = C.color;
            C.background = D.color;
            D.background = E.color;
            E.background = F.color;
            F.background = G.color;
            G.background = Color.BLACK;*/
            X.background = Color.valueOf("b3cde3");
        }

        Score(int min, int max, Color color) {
            this.color = color;
            this.min = min;
            this.max = max;
        }

        public static Score getScore(int value) {
            for (Score score : Score.values())
                if (value >= score.min && value <= score.max) return score;
            //return G;
            return X;
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

    public static class CircularJaugeStyle {
        public Texture background;
        public Texture foreground;
        public CircularJaugeStyle(Texture foreground, Texture background) {
            this.background = background;
            this.foreground = foreground;
        }
    }

    public CircularJauge(CircularJaugeStyle style) {
        this.background = style.background;
        this.foreground = style.foreground;
        shader = new ProgressBarShader(false);
    }

    public CircularJauge(Texture background, Texture foreground) {
        this.background = background;
        this.foreground = foreground;
        shader = new ProgressBarShader(false);
    }

    float current_value = 0;
    float consign_value = 0;
    int frames_for_consign = 30;
    int current_frame = 0;

    public void setCurrentValue (float value) {
        current_value = consign_value = value;
    }

    public void setConsignValue (float value) {
        if (consign_value == value) return;
        consign_value = value;
        current_frame = 0;
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {

        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();

        Score score = Score.getScore((int)current_value);

        float percent = (MathUtils.clamp(current_value, score.min, score.max) - score.min) / (score.max - score.min);

        // generate drawables

        batch.setShader(shader.getProgram());
        shader.prepare();
        shader.setFirstColor(score.getBackgroundColor());
        shader.setSecondColor(score.getColor());
        shader.setRatio(percent);

        batch.draw(background, x, y, width, height);
        batch.setShader(null);
        batch.draw(foreground, x, y, width, height);

    }

    @Override
    public void act(float delta) {
        if (!MathUtils.isEqual(current_value, consign_value)) {
            int frames_restantes = frames_for_consign - current_frame;

            if (frames_restantes <= 1) {
                current_value = consign_value;
            } else {
                float diff = consign_value - current_value;
                float step = diff / frames_restantes;
                current_value += step;
                current_frame++;
            }
        }
    }
}
