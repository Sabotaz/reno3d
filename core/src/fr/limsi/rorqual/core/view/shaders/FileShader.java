package fr.limsi.rorqual.core.view.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Created by christophe on 30/06/15.
 */
public abstract class FileShader extends BaseShader {

    private String vertexShader;
    private String fragmentShader;

    public FileShader() {
        super();
        String shaderName = this.getClass().getSimpleName();
        vertexShader = Gdx.files.internal("data/shaders/" + shaderName + ".vs").readString();
        fragmentShader = Gdx.files.internal("data/shaders/" + shaderName + ".fs").readString();

        String vs_prefix = "";
        String fs_prefix = "";

        switch(Gdx.app.getType()) {
            case Android:
                vs_prefix += "precision highp float; \n";
                fs_prefix += "precision highp float; \n";
                break;
            case Desktop:
                break;
        }

        program = new ShaderProgram(vs_prefix + vertexShader, fs_prefix + fragmentShader);

        if (!program.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader " + program.getLog());
        String log = program.getLog();
        if (log.length() > 0) Gdx.app.error("ShaderTest", "Shader compilation log: " + log);

    }

}
