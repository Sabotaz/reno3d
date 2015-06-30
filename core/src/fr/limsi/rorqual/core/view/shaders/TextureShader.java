package fr.limsi.rorqual.core.view.shaders;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by christophe on 20/04/15.
 */
public class TextureShader extends FileShader {

    /*
    protected final int u_projTrans = register(new Uniform("u_projTrans"));
    protected final int u_worldTrans = register(new Uniform("u_worldTrans"));
    protected final int u_test = register(new Uniform("u_test"));
    protected final int u_color = register(new Uniform("u_color"));*/
    protected final int u_projTrans = register(new Uniform("u_projTrans"));
    protected final int u_worldTrans = register(new Uniform("u_worldTrans"));
    protected final int u_color = register(new Uniform("u_color"));

    //private boolean withColor;

    public ShaderProgram getProgram() {
        return program;
    }


    @Override
    public void init () {
        super.init(program, null);
    }

    @Override
    public int compareTo (Shader other) {
        return 0;
    }

    @Override
    public boolean canRender (Renderable instance) {
        return instance.material.has(ColorAttribute.Diffuse);
    }

    @Override
    public void begin (Camera camera, RenderContext context) {
        program.begin();
        context.setDepthTest(GL20.GL_LEQUAL, 0f, 1f);
        context.setDepthMask(true);
        set(u_projTrans, camera.combined);
    }

    @Override
    public void render (Renderable renderable) {
        set(u_worldTrans, renderable.worldTransform);

        ColorAttribute colorAttr = (ColorAttribute)renderable.material.get(ColorAttribute.Diffuse);
        set(u_color, colorAttr.color);

        renderable.mesh.render(program, renderable.primitiveType, renderable.meshPartOffset, renderable.meshPartSize);
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
}
