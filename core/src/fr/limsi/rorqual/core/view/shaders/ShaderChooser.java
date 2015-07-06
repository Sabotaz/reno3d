package fr.limsi.rorqual.core.view.shaders;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.BaseShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;

/**
 * Created by christophe on 30/06/15.
 */
public class ShaderChooser extends BaseShaderProvider {

    protected Shader createShader (final Renderable renderable) {
        //if (renderable.shader != null && renderable.shader.canRender(renderable))
        //    return renderable.shader;

        if (renderable.material.has(ShaderAttribute.Billboard)) {
            System.out.println("BILLBOARD");
            return new BillboardShader();
        }
        if (renderable.material.has(ShaderAttribute.Sun)) {
            System.out.println("SUN");
            return new SunShader();
        }
        if (renderable.material.has(TextureAttribute.Normal)) {
            return new BumpedTextureShader();
        }
        if (renderable.material.has(TextureAttribute.Diffuse)) {
            return new TextureShader();
        }
        return new LightShader();
    }
}
