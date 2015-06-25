package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Created by christophe on 20/04/15.
 */
public class TestShader extends BaseShader {


    public final static String vertexShader = "attribute vec3 a_position;    \n" +
            "attribute vec4 a_color;    \n" +
            "uniform mat4 u_projTrans;\n" +
            "varying vec4 vColor;" +
            "attribute vec2 a_texCoords;\n" +
            "varying vec2 v_texCoords;\n" +
            "void main()                  \n" +
            "{                            \n" +
            " vColor = a_color;\n" +
            "    v_texCoords = a_texCoords;\n" +
            "    gl_Position = u_projTrans * vec4(a_position.xy, 0.0, 1.0);  \n"      +
            "}                            \n" ;
    public final static String fragmentShader =
            "varying vec4 vColor;" +
                    "varying vec2 v_texCoords;\n" +
                    "uniform sampler2D u_texture;\n" +
    "void main()                                  \n" +
            "{                                            \n" +
            "\n" +
                    "    vec4 texColor = texture2D(u_texture, v_texCoords);\n" +
                    "    gl_FragColor = texColor;" +
            "}";
    /*
    public final static String vertexShader =
            "attribute vec3 a_position;\n"
                    + "uniform mat4 u_projTrans;\n"
                    + "uniform mat4 u_worldTrans;\n"
                    + "void main() {\n"
                    + "	gl_Position = u_projTrans * u_worldTrans * vec4(a_position, 1.0);\n"
                    + "}\n";

    public final static String fragmentShader =
            "#ifdef GL_ES\n"
                    + "#define LOWP lowp\n"
                    + "precision mediump float;\n"
                    + "#else\n"
                    + "#define LOWP\n"
                    + "#endif\n"

                    + "uniform float u_test;\n"
                    + "#ifdef HasDiffuseColor\n"
                    + "uniform vec4 u_color;\n"
                    + "#endif //HasDiffuseColor\n"

                    + "void main() {\n"
                    + "#ifdef HasDiffuseColor\n"
                    + "	gl_FragColor.rgb = u_color.rgb * vec3(u_test);\n"
                    + "#else\n"
                    + "	gl_FragColor.rgb = vec3(u_test);\n"
                    + "#endif //HasDiffuseColor\n"
                    + "}\n";
*/
    /*
    protected final int u_projTrans = register(new Uniform("u_projTrans"));
    protected final int u_worldTrans = register(new Uniform("u_worldTrans"));
    protected final int u_test = register(new Uniform("u_test"));
    protected final int u_color = register(new Uniform("u_color"));*/
    protected final int u_projTrans = register(new Uniform("u_projTrans"));
    protected final int u_worldTrans = register(new Uniform("u_worldTrans"));
    protected final int u_color = register(new Uniform("u_color"));

    protected final ShaderProgram program;
    //private boolean withColor;

    public ShaderProgram getProgram() {
        return program;
    }

    public TestShader (Renderable renderable) {
        super();
        //withColor = renderable.material.has(ColorAttribute.Diffuse);
        //if (withColor)
        //    Gdx.app.log("ShaderTest", "Compiling test shader with u_color uniform");
        //else
        //    Gdx.app.log("ShaderTest", "Compiling test shader without u_color uniform");

        //String prefix = withColor ? "#define HasDiffuseColor\n" : "";
        program = new ShaderProgram(vertexShader, /*prefix +*/ fragmentShader);

        if (!program.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader " + program.getLog());
        String log = program.getLog();
        if (log.length() > 0) Gdx.app.error("ShaderTest", "Shader compilation log: " + log);
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
