package fr.limsi.rorqual.core.utils.scene3d.models;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import fr.limsi.rorqual.core.utils.AssetManager;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;

/**
 * Created by christophe on 03/07/15.
 */
public class Floor {

    private static Model model = null;

    public static ModelContainer getModel() {
        if (model == null)
            makeModel();
        return new ModelContainer(model);
    }

    public static void makeModel() {

        Texture diffuse = (Texture)AssetManager.getInstance().get("grid");

        diffuse.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        TextureAttribute tad = TextureAttribute.createDiffuse(diffuse);

        Material material = new Material();
        material.set(tad);

        ModelBuilder modelBuilder = new ModelBuilder();

        modelBuilder.begin();

        Node node = modelBuilder.node();
        node.id = "node1";

        MeshPartBuilder meshBuilder = modelBuilder.part("part1", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, material);
        meshBuilder.setUVRange(-10,-10,10,10);
        meshBuilder.rect(-100, -100, 0, 100, -100, 0, 100,100,0, -100,100, 0,0,0,1);

        model = modelBuilder.end();
    }
}
