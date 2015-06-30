package fr.limsi.rorqual.core.view.shaders;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;

import fr.limsi.rorqual.core.view.BillboardShader;

/**
 * Created by christophe on 30/06/15.
 */
public class ShaderChooser implements ShaderProvider {

    LightShader lightShader = new fr.limsi.rorqual.core.view.LightShader();
    fr.limsi.rorqual.core.view.shaders.BillboardShader billboardShader = new BillboardShader();
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
