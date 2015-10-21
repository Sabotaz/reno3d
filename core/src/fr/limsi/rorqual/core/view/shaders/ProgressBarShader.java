package fr.limsi.rorqual.core.view.shaders;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;

import java.util.HashMap;

/**
 * Created by christophe on 01/09/15.
 */
public class ProgressBarShader extends FileShader {

    protected final int u_projTrans = register(new Uniform("u_projTrans"));

    protected final int u_firstColor = register(new Uniform("u_firstColor"));
    protected final int u_secondColor = register(new Uniform("u_secondColor"));
    protected final int u_colors = register(new Uniform("u_colors"));
    protected final int u_ratios = register(new Uniform("u_ratios"));
    protected final int u_nratios = register(new Uniform("u_nratios"));
    protected final int u_ratio = register(new Uniform("u_ratio"));
    protected final int u_time = register(new Uniform("u_time"));

    private Color firstColor = Color.BLACK;
    private Color secondColor = Color.WHITE;
    private float ratio = 0.5f;

    public ProgressBarShader(boolean autocompile) {
        super(autocompile);
    }

    public ShaderProgram getProgram() {
        if (program == null)
            init();
        return program;
    }

    @Override
    public void init () {
        if (program == null)
            compile();
        super.init(program, null);
    }

    @Override
    public boolean canRender (Renderable renderable) {
        return true;
    }

    @Override
    public void begin (Camera camera, RenderContext context) {
        program.begin();
        set(u_projTrans, camera.combined);
    }

    @Override
    public void end () {
        program.end();
    }

    @Override
    public void dispose () {
        super.dispose();
        program.dispose();
    }

    public void prepare() {
        set(u_time, System.nanoTime() * 1e-9f);
    }

    public void setFirstColor(Color color) {
        firstColor = color;
        set(u_firstColor, firstColor);
    }

    public void setSecondColor(Color color) {
        secondColor = color;
        set(u_secondColor, secondColor);
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
        set(u_ratio, this.ratio);
    }

    private float[] colors;

    public void setColors(Color colors[]) {
        this.colors = new float[colors.length*3];
        for (int i = 0; i < colors.length; i++) {
            this.colors[3*i+0] = colors[i].r;
            this.colors[3*i+1] = colors[i].g;
            this.colors[3*i+2] = colors[i].b;
        }
        program.setUniform3fv(loc(u_colors), this.colors, 0, this.colors.length);
    }

    private float[] ratios;

    public void setRatios(float ratios[]) {
        this.ratios = ratios;
        program.setUniform1fv(loc(u_ratios), this.ratios, 0, this.ratios.length);
        set(u_nratios, this.ratios.length);
    }
}
