package fr.limsi.rorqual.core.view.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
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
public class LightShader extends FileShader {
    /*
    protected final int u_projTrans = register(new Uniform("u_projTrans"));
    protected final int u_worldTrans = register(new Uniform("u_worldTrans"));
    protected final int u_test = register(new Uniform("u_test"));
    protected final int u_color = register(new Uniform("u_color"));*/
    protected final int u_projTrans = register(new Uniform("u_projTrans"));
    protected final int u_worldTrans = register(new Uniform("u_worldTrans"));
    protected final int u_color = register(new Uniform("u_color"));
    protected final int u_light_direction = register(new Uniform("u_light_direction"));
    protected final int u_light_color = register(new Uniform("u_light_color"));
    protected final int u_ambient_color = register(new Uniform("u_ambient_color"));
    protected final int u_normal_matrix = register(new Uniform("u_normal_matrix"));

    protected final int u_is_blended = register(new Uniform("u_is_blended"));
    protected final int u_opacity = register(new Uniform("u_opacity"));

    protected final int u_is_tinted = register(new Uniform("u_is_tinted"));
    protected final int u_tint = register(new Uniform("u_tint"));

    protected final int u_is_selected = register(new Uniform("u_is_selected"));
    protected final int u_time = register(new Uniform("u_time"));

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
    public boolean canRender (Renderable renderable) {
        if (renderable.material.has(TextureAttribute.Diffuse))
            return false;
        else if (renderable.material.has(ColorAttribute.Diffuse))
            return true;
        else return false;
    }

    @Override
    public void begin (Camera camera, RenderContext context) {
        program.begin();
        set(u_projTrans, camera.combined);
    }

    @Override
    public void render (Renderable renderable) {
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

        final Matrix3 tmpM = new Matrix3();
        set(u_normal_matrix, tmpM.set(renderable.worldTransform).inv().transpose());

        set(u_worldTrans, renderable.worldTransform);

        ColorAttribute colorAttr = (ColorAttribute)renderable.material.get(ColorAttribute.Diffuse);
        set(u_color, colorAttr.color);

        HashMap<String, Object> attrs = (HashMap<String, Object>)renderable.userData;
        if (attrs != null && attrs.containsKey("Color") && attrs.get("Color") != null) {
            set(u_is_tinted, 1);
            set(u_tint, (Color)attrs.get("Color"));

        } else {
            set(u_is_tinted, 0);
        }

        if(renderable.material.get(BlendingAttribute.Type) != null) {
            set(u_is_blended, ((BlendingAttribute) renderable.material.get(BlendingAttribute.Type)).blended
                    && ((BlendingAttribute) renderable.material.get(BlendingAttribute.Type)).opacity != 0
                    ? 1 : 0);
            set(u_opacity, ((BlendingAttribute) renderable.material.get(BlendingAttribute.Type)).opacity);
        } else {
            set(u_is_blended, 0);
            set(u_opacity, 0.0f);
        }

        if (renderable.material.has(ShaderAttribute.Selectable)) {
            ShaderAttribute attr = (ShaderAttribute)renderable.material.get(ShaderAttribute.Selectable);
            set(u_is_selected, (boolean) ((Object[])attr.getUserData())[0] ? 1 : 0);
            set(u_time, System.nanoTime() * 1e-9f - (float)((Object[])attr.getUserData())[1]);
        } else {
            set(u_is_selected, 0);
            set(u_time, System.nanoTime() * 1e-9f);
        }

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
