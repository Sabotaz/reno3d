package fr.limsi.rorqual.core.view.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.utils.BufferUtils;

import java.nio.IntBuffer;
import java.util.HashMap;

/**
 * Created by christophe on 20/04/15.
 */
public class BumpedTextureShader extends FileShader {

    /*
    protected final int u_projTrans = register(new Uniform("u_projTrans"));
    protected final int u_worldTrans = register(new Uniform("u_worldTrans"));
    protected final int u_test = register(new Uniform("u_test"));
    protected final int u_color = register(new Uniform("u_color"));*/
    protected final int u_projTrans = register(new Uniform("u_projTrans"));
    protected final int u_worldTrans = register(new Uniform("u_worldTrans"));
    protected final int u_normal_matrix = register(new Uniform("u_normal_matrix"));

    protected final int u_texture_diffuse = register(new Uniform("u_texture_diffuse"));
    protected final int u_texture_bump = register(new Uniform("u_texture_bump"));
    protected final int u_texture_normal = register(new Uniform("u_texture_normal"));

    protected final int u_textureUV = register(new Uniform("u_textureUV"));

    protected final int u_light_direction = register(new Uniform("u_light_direction"));
    protected final int u_light_color = register(new Uniform("u_light_color"));
    protected final int u_ambient_color = register(new Uniform("u_ambient_color"));

    protected final int u_is_colored = register(new Uniform("u_is_colored"));
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
        return renderable.material.has(TextureAttribute.Normal);
    }

    @Override
    public void begin (Camera camera, RenderContext context) {
        program.begin();
        set(u_projTrans, camera.combined);
    }

    @Override
    public void render (Renderable renderable) {
        set(u_worldTrans, renderable.worldTransform);

        TextureAttribute tad = ((TextureAttribute) renderable.material.get(TextureAttribute.Diffuse));
        //TextureAttribute tab = ((TextureAttribute) renderable.material.get(TextureAttribute.Bump));
        TextureAttribute tan = ((TextureAttribute) renderable.material.get(TextureAttribute.Normal));

        Texture texture_diffuse = tad.textureDescription.texture;
        //Texture texture_bump = tab.textureDescription.texture;
        Texture texture_normal = tan.textureDescription.texture;

        set(u_textureUV, tad.offsetU, tad.offsetV, tad.scaleU, tad.scaleV);

        Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE2);
        texture_normal.bind();
        set(u_texture_normal, 2);

        //System.out.println( tad.offsetU + "," + tad.offsetV + "," +  tad.scaleU + "," +  tad.scaleV);

        //Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE1);
        //texture_bump.bind();
        //set(u_texture_bump, 1);

        Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
        texture_diffuse.bind();
        set(u_texture_diffuse, 0);

        set(u_worldTrans, renderable.worldTransform);

        final Matrix3 tmpM = new Matrix3();
        set(u_normal_matrix, tmpM.set(renderable.worldTransform).inv().transpose());


        /*if (renderable.environment.directionalLights.size > 0) {
            DirectionalLight light0 = renderable.environment.directionalLights.get(0);
            set(u_light_color, light0.color);
            set(u_light_direction, light0.direction);
        }*/

        Attribute attribute =  renderable.environment.get(ColorAttribute.AmbientLight);
        if (attribute != null) {
            ColorAttribute colorAttribute = (ColorAttribute) attribute;
            set(u_ambient_color, colorAttribute.color);
        }

        ColorAttribute colorAttr = (ColorAttribute)renderable.material.get(ColorAttribute.Diffuse);
        if (colorAttr != null) {
            set(u_is_colored, 1);
            set(u_color, colorAttr.color);
        } else {
            set(u_is_colored, 0);
        }

        HashMap<String, Object> attrs = (HashMap<String, Object>)renderable.userData;
        if (attrs != null && attrs.containsKey("Color") && attrs.get("Color") != null) {
            set(u_is_tinted, 1);
            set(u_tint, (Color)attrs.get("Color"));

        } else {
            set(u_is_tinted, 0);
        }
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
