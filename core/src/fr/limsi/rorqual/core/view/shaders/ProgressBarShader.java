package fr.limsi.rorqual.core.view.shaders;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Attribute;
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
    protected final int u_ratio = register(new Uniform("u_ratio"));

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
}
