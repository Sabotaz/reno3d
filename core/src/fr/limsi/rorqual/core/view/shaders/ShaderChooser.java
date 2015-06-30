package fr.limsi.rorqual.core.view.shaders;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;

/**
 * Created by christophe on 30/06/15.
 */
public class ShaderChooser implements ShaderProvider {

    LightShader lightShader = new LightShader();
    BillboardShader billboardShader = new BillboardShader();
    TextureShader testShader;

    public ShaderChooser() {
        lightShader.init();
    }

    public void dispose() {
    }

    public Shader getShader(Renderable renderable) {
        return lightShader;
    }
}
