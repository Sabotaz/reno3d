package fr.limsi.rorqual.core.view.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by christophe on 20/04/15.
 */
public class BillboardShader extends FileShader {

    protected final int u_proj = register(new Uniform("u_proj"));
    protected final int u_view = register(new Uniform("u_view"));
    protected final int u_model = register(new Uniform("u_model"));
    protected final int u_texture = register(new Uniform("u_texture"));
    protected final int u_textureUV = register(new Uniform("u_textureUV"));
    //private boolean withColor;

    @Override
    public void init () {
        super.init(program, null);
    }

    @Override
    public boolean canRender (Renderable instance) {
        return instance.material.has(ShaderAttribute.Billboard);
    }

    @Override
    public void begin (Camera camera, RenderContext context) {
        program.begin();
        set(u_proj, camera.projection);
        set(u_view, camera.view);
    }

    @Override
    public void render (Renderable renderable) {
        set(u_model, renderable.worldTransform);

        TextureAttribute ta = ((TextureAttribute) renderable.material.get(TextureAttribute.Diffuse));
        Texture texture = ta.textureDescription.texture;
        texture.bind();
        set(u_texture, 0);
        set(u_textureUV, ta.offsetU, ta.offsetV, ta.scaleU, ta.scaleV);
        renderable.meshPart.render(program);
        //renderable.mesh.render(program, renderable.primitiveType, renderable.meshPartOffset, renderable.meshPartSize);
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
