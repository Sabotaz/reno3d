package fr.limsi.rorqual.core.view.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;

import java.util.HashMap;

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
    protected final int u_normal_matrix = register(new Uniform("u_normal_matrix"));

    protected final int u_texture = register(new Uniform("u_texture"));
    protected final int u_textureUV = register(new Uniform("u_textureUV"));

    protected final int u_light_direction = register(new Uniform("u_light_direction"));
    protected final int u_light_color = register(new Uniform("u_light_color"));
    protected final int u_ambient_color = register(new Uniform("u_ambient_color"));
    protected final int u_color = register(new Uniform("u_color"));

    protected final int u_is_tinted = register(new Uniform("u_is_tinted"));
    protected final int u_tint = register(new Uniform("u_tint"));

    //private boolean withColor;

    public ShaderProgram getProgram() {
        return program;
    }

    @Override
    public void init () {
        super.init(program, null);
    }

    @Override
    public boolean canRender (Renderable renderable) {
        return renderable.material.has(TextureAttribute.Diffuse);
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
        TextureAttribute ta = ((TextureAttribute) renderable.material.get(TextureAttribute.Diffuse));
        Texture texture = ta.textureDescription.texture;
        texture.bind();
        set(u_texture, 0);
        set(u_textureUV, ta.offsetU, ta.offsetV, ta.scaleU, ta.scaleV);

        set(u_worldTrans, renderable.worldTransform);

        final Matrix3 tmpM = new Matrix3();
        set(u_normal_matrix, tmpM.set(renderable.worldTransform).inv().transpose());


        if (renderable.environment.directionalLights.size > 0) {
            DirectionalLight light0 = renderable.environment.directionalLights.get(0);
            set(u_light_color, light0.color);
            set(u_light_direction, light0.direction);
        }

        Attribute attribute =  renderable.environment.get(ColorAttribute.AmbientLight);
        if (attribute != null) {
            ColorAttribute colorAttribute = (ColorAttribute) attribute;
            set(u_ambient_color, colorAttribute.color);
        }

        HashMap<String, Object> attrs = (HashMap<String, Object>)renderable.userData;
        if (attrs.containsKey("Color") && attrs.get("Color") != null) {
            set(u_is_tinted, 1);
            set(u_tint, (Color)attrs.get("Color"));

        } else {
            set(u_is_tinted, 0);
        }

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
