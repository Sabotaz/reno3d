package fr.limsi.rorqual.core.utils.scene3d.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;

import fr.limsi.rorqual.core.utils.AssetManager;
import fr.limsi.rorqual.core.view.shaders.ShaderAttribute;

/**
 * Created by christophe on 08/07/15.
 */
public class Pin {

    private static Model model = null;

    public static ModelInstance getModelInstance() {
        if (model == null)
            makeModel();
        return new ModelInstance(model);
    }

    public static void makeModel() {

        ColorAttribute ca = ColorAttribute.createDiffuse(Color.RED);

        Material material = new Material();
        material.set(ca);

        ModelBuilder modelBuilder = new ModelBuilder();

        modelBuilder.begin();

        Node node = modelBuilder.node();
        node.id = "node1";

        MeshPartBuilder meshBuilder = modelBuilder.part("part1", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);
        meshBuilder.setVertexTransform(new Matrix4().idt().translate(0, 0, 0.5f).rotate(1,0,0,90));
        meshBuilder.capsule(0.25f, 1f, 30);

        model = modelBuilder.end();
    }
}
