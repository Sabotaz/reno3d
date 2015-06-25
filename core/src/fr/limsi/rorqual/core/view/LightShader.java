package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Created by christophe on 20/04/15.
 */
public class LightShader extends BaseShader {


    public final static String vertexShader = //"precision highp float; \n" +
            "attribute vec4 a_position;    \n" +
            "attribute vec3 a_normal;    \n" +
            "uniform mat4 u_worldTrans;\n" +
            "uniform mat4 u_projTrans;\n" +
            "uniform mat3 u_normal_matrix;\n" +
            "uniform vec3 u_light_direction;\n" +
            "varying vec3 N;\n" +
            "varying vec3 v;\n" +
            "void main()                  \n" +
            "{                            \n" +
            "    gl_Position =  u_projTrans * u_worldTrans * a_position;  \n" +
            "   v = vec3(u_projTrans * u_worldTrans * a_position);       \n" +
            "   N = normalize(u_normal_matrix * a_normal);\n"      +
            "}                            \n" ;
    public final static String fragmentShader = //"precision highp float; \n" +
            "uniform vec4 u_color;\n" +
            "uniform vec4 u_ambient_color;\n" +
            "uniform vec4 u_light_color;\n" +
            "uniform vec3 u_light_direction;\n" +
            "varying vec3 N;\n" +
            "varying vec3 v;\n" +
    "void main()                                  \n" +
            "{                                            \n" +
            "   vec4 Idiff = u_light_color * max(-dot(N,u_light_direction), 0.0);  \n" +
            //"   Idiff = clamp(Idiff, 0.0, 1.0); \n" +
            "  gl_FragColor.rgb = 0.4*vec3(Idiff) + 0.2 * vec3(u_ambient_color) + 0.4 * vec3(u_color);\n" +
            //"  gl_FragColor =Idiff;\n" +
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
    protected final int u_light_direction = register(new Uniform("u_light_direction"));
    protected final int u_light_color = register(new Uniform("u_light_color"));
    protected final int u_ambient_color = register(new Uniform("u_ambient_color"));
    protected final int u_normal_matrix = register(new Uniform("u_normal_matrix"));

    protected final ShaderProgram program;
    //private boolean withColor;

    public ShaderProgram getProgram() {
        return program;
    }

    public LightShader() {
        super();
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


    public LightShader(Renderable renderable) {
        super();
        //withColor = renderable.material.has(ColorAttribute.Diffuse);
        //if (withColor)
        //    Gdx.app.log("ShaderTest", "Compiling test shader with u_color uniform");
        //else
        //    Gdx.app.log("ShaderTest", "Compiling test shader without u_color uniform");

        //String prefix = withColor ? "#define HasDiffuseColor\n" : "";
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
