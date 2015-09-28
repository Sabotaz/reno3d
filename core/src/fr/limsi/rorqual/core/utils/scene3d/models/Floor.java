package fr.limsi.rorqual.core.utils.scene3d.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import javax.swing.text.AttributeSet;

import fr.limsi.rorqual.core.utils.AssetManager;
import fr.limsi.rorqual.core.utils.Holder;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;

/**
 * Created by christophe on 03/07/15.
 */
public class Floor {

    public static ModelContainer getModel() {
        Model model = makeModel();
        ModelContainer container = new ModelContainer(model);
        container.setSelectable(false);

        return container;
    }

    public static Model makeModel() {

        final Holder holder = new Holder();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                Texture diffuse = (Texture)AssetManager.getInstance().get("grid");

                diffuse.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

                TextureAttribute tad = TextureAttribute.createDiffuse(diffuse);

                Material material = new Material();
                //material.set(tad);
                material.set(ColorAttribute.createDiffuse(Color.valueOf("FFFFFF80")));

                Material material2 = new Material();
                //material.set(tad);
                material2.set(ColorAttribute.createDiffuse(Color.BLACK));

                ModelBuilder modelBuilder = new ModelBuilder();

                modelBuilder.begin();

                Node node = modelBuilder.node();
                node.id = "node1";

                MeshPartBuilder meshBuilder2 = modelBuilder.part("part2", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material2);
                for (int i = -20; i < 20; i++) {
                    meshBuilder2.line(5*i,-100,0,5*i,100,0);
                    meshBuilder2.line(-100, 5 * i, 0, 100, 5 * i, 0);
                }

                MeshPartBuilder meshBuilder = modelBuilder.part("part1", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);
                for (int i = -100; i < 100; i++) {
                    //if (i % 5 != 0) {
                        meshBuilder.line(i, -100, 0, i, 100, 0);
                        meshBuilder.line(-100, i, 0, 100, i, 0);
                    //}
                }

                holder.set(modelBuilder.end());

            }
        };
        Gdx.app.postRunnable(runnable);

        return (Model)holder.get();
    }
}
