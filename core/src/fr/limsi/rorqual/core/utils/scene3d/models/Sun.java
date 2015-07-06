package fr.limsi.rorqual.core.utils.scene3d.models;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import fr.limsi.rorqual.core.utils.AssetManager;
import fr.limsi.rorqual.core.view.shaders.ShaderAttribute;

/**
 * Created by christophe on 06/07/15.
 */
public class Sun {

    private static Model model = null;

    public static ModelInstance getModelInstance() {
        if (model == null)
            makeModel();
        return new ModelInstance(model);
    }

    public static void makeModel() {

        Texture diffuse = (Texture) AssetManager.getInstance().get("sunmap");

        diffuse.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);

        TextureAttribute tad = TextureAttribute.createDiffuse(diffuse);

        ShaderAttribute sa = new ShaderAttribute(ShaderAttribute.Sun);

        Material material = new Material();
        material.set(tad);
        material.set(sa);

        ModelBuilder modelBuilder = new ModelBuilder();

        modelBuilder.begin();

        Node node = modelBuilder.node();
        node.id = "node1";

        MeshPartBuilder meshBuilder = modelBuilder.part("part1", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, material);
        meshBuilder.setUVRange(0, 0, 1, 1);
        meshBuilder.sphere(10, 10, 10, 30, 30);

        model = modelBuilder.end();
    }
}
