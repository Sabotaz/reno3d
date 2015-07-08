package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import fr.limsi.rorqual.core.utils.AssetManager;
import fr.limsi.rorqual.core.view.shaders.ShaderAttribute;

/**
 * Created by christophe on 23/06/15.
 */
public class Popup {

    private static Model model = null;
    private static ModelInstance modelInstance = null;

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public Popup (int w, int h) {
        super();
        NinePatchSprite background;
        Mesh mesh;
        Texture texture;
        float[] vertices;
        Matrix4 transform;
        //this.setPosition(actor.getX(), actor.getY(), actor.getZ());
        //actor.getParent().addActor3d(this);
        //texture = background.getTexture();
        texture = (Texture)AssetManager.getInstance().get("bulle");
        background = new NinePatchSprite(texture, 200, 200, 200, 300);
        //background.scale(0.01f, 0.01f);

        //background.setSize(0, 0, 10, 5);
        background.setSize(0, 0, w, h);
        vertices = background.vertices;

        mesh = new Mesh(true, 9*4, 9*2*3,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoords"));
        mesh.setVertices(vertices);
        mesh.setIndices(new short[]{
                0, 1, 2,
                0, 2, 3,

                4, 5, 6,
                4, 6, 7,

                8, 9, 10,
                8, 10, 11,

                12, 13, 14,
                12, 14, 15,

                16, 17, 18,
                16, 18, 19,

                20, 21, 22,
                20, 22, 23,

                24, 25, 26,
                24, 26, 27,

                28, 29, 30,
                28, 30, 31,

                32, 33, 34,
                32, 34, 35});
        /*mesh = new Mesh(true, 3, 3,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoords"));

        mesh.setVertices(new float[]{ -0.5f, -0.5f, 0, 0, 0,
                0.5f, -0.5f, 0, 0, 1,
                0, 0.5f, 0, 1, 0 });
        mesh.setIndices(new short[] { 0, 1, 2 });*/
        mesh.transform(new Matrix4().idt().scl(0.01f));
        transform = new Matrix4();
        //transform.translate(1, 1, 1);
        //transform.translate(5,0,5);
        transform.rotate(1, 0, 0, 180);
        //transform.val[15] = 0.008f;
        //transform.scale(0.01f, 0.01f, 0.01f);

        Material material = new Material();
        TextureAttribute ta = TextureAttribute.createDiffuse(texture);
        //ta.scaleU = ta.scaleV = 100f;
        material.set(ta);
        material.set(new ShaderAttribute(ShaderAttribute.Billboard));

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder meshBuilder;
        meshBuilder = modelBuilder.part("part1", GL20.GL_TRIANGLES, mesh.getVertexAttributes(), material);
        meshBuilder.addMesh(mesh);

        model = modelBuilder.end();

        modelInstance = new ModelInstance(model);
        modelInstance.transform = transform;
    }
}
