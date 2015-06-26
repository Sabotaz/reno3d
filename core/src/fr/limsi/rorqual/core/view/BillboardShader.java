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
public class BillboardShader extends BaseShader {


    public final static String vertexShader =
            "attribute vec3 a_position;         \n" +
            "uniform mat4 u_proj;               \n" +
            "uniform mat4 u_model;              \n" +
            "uniform mat4 u_view;               \n" +
            "attribute vec2 a_texCoords;        \n" +
            "varying vec2 v_texCoords;          \n" +

            "void main()                        \n" +
            "{                                  \n" +
            "    v_texCoords = a_texCoords;     \n" +
                    "float scl = u_model[3][3];" +
                    "vec4 pos;" +
                    "if (scl != 1.0) {" +
                    "   pos = vec4(scl * a_position,1.0);" +
                    "   mat4 model = u_model;" +
                    "   model[3][3] = 1.0;" +
                    "   pos = model * pos;" +
                    "   pos += vec4((u_view * model * vec4(0.0, 0.0, 0.0, 1.0)).xyz, 0.0);"+
                    "} else {" +
                    "   pos = u_model * vec4(a_position,1.0);" +
                    "   pos += vec4((u_view * u_model * vec4(0.0, 0.0, 0.0, 1.0)).xyz, 0.0);"+
                    "} " +
                    "gl_Position = u_proj * pos;  \n"      +
            //"    gl_Position = u_proj * (vec4((u_view * u_model)[3].xyz + (mv * vec4(a_position,1.0)).xyz, 1.0));  \n"      +
            "}                            \n" ;

    public final static String fragmentShader =
            "varying vec2 v_texCoords;          \n" +
            "uniform sampler2D u_texture;       \n" +

            "void main()                        \n" +
            "{                                  \n" +
            "    vec4 texColor = texture2D(u_texture, v_texCoords);\n" +
            "    gl_FragColor = texColor;       \n" +
            "}                                  \n";

    protected final int u_proj = register(new Uniform("u_proj"));
    protected final int u_view = register(new Uniform("u_view"));
    protected final int u_model = register(new Uniform("u_model"));

    protected final ShaderProgram program;
    //private boolean withColor;

    public ShaderProgram getProgram() {
        return program;
    }

    public BillboardShader(Renderable renderable) {
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
        set(u_proj, camera.projection);
        set(u_view, camera.view);
    }

    @Override
    public void render (Renderable renderable) {
        set(u_model, renderable.worldTransform);

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
