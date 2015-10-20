package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import fr.limsi.rorqual.core.utils.AssetManager;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;

/**
 * Created by christophe on 19/10/15.
 */
public class OuverturePlane extends ModelContainer {

    public Ouverture ouverture;

    public OuverturePlane(ModelContainer parent) {
        setSelectable(false);
        makeModel(parent);
    }

    private void makeModel(ModelContainer p) {
        if (p instanceof Ouverture) {
            ouverture = (Ouverture)p;
            TextureAttribute ta;
            if (p instanceof Porte) {
                ta = TextureAttribute.createDiffuse((Texture)AssetManager.getInstance().get("porte"));
            } else if (p instanceof Fenetre) {
                ta = TextureAttribute.createDiffuse((Texture)AssetManager.getInstance().get("fenetre"));
            } else if (p instanceof PorteFenetre) {
                ta = TextureAttribute.createDiffuse((Texture)AssetManager.getInstance().get("fenetre"));
            } else {
                ta = TextureAttribute.createDiffuse((Texture)AssetManager.getInstance().get("fenetre"));
            }

            ModelBuilder modelBuilder = new ModelBuilder();
            modelBuilder.begin();
            MeshPartBuilder meshBuilder;
            meshBuilder = modelBuilder.part("cylinder", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, new Material(ta));
            meshBuilder.rect(
                    new Vector3( 0.5f, 0, 0),
                    new Vector3(-0.5f, 0, 0),
                    new Vector3(-0.5f, 1, 0),
                    new Vector3( 0.5f, 1, 0),
                    new Vector3(0, 0, 1));
            Model model = modelBuilder.end();

            setModel(model);
        }
    }

    @Override
    public void act() {
        if (ouverture != null && ouverture.getMur() != null) {
            float width = ouverture.getWidth();
            this.model_transform.idt()
                    .mul(new Matrix4().translate(
                            ouverture.getPosition().x + ouverture.getWidth() / 2,
                            -ouverture.getMur().getDepth() / 2,
                            Slab.DEFAULT_HEIGHT + 0.01f))
                    .mul(new Matrix4().setToScaling(ouverture.getWidth(), ouverture.getWidth(), 1))
            ;
            if (ouverture.getMur().getSlabGauche() == null) { // vers la gauche
            } else { // vers la droite
                if (ouverture instanceof Fenetre)
                    model_transform.mul(new Matrix4().setToRotation(0,0,1,180));
            }
        }
        this.setVisible(this.root.getCamera() instanceof OrthographicCamera);
    }
}
