package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;

import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeDoorEnum;
import fr.limsi.rorqual.core.utils.AssetManager;

/**
 * Created by ricordeau on 20/07/15.
 */
public class Porte extends Ouverture{

    static float DEFAULT_Y = 0.0f;
    static float DEFAULT_WIDTH = 1.0f;
    static float DEFAULT_HEIGHT = 2.15f;

    // Attributs
    private TypeDoorEnum typePorte;

    // Constructeur
    public Porte(Mur mur, float x) {
        // x est le milieu ?
        this(mur, x - DEFAULT_WIDTH / 2, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Porte(Mur mur, float x, float y, float width, float height) {
        super(mur, new Vector2(x, y), width, height);
    }

    // Getter & Setter
    public TypeDoorEnum getTypePorte() {
        return typePorte;
    }
    public void setTypePorte(TypeDoorEnum typePorte) {
        this.typePorte = typePorte;
    }

    @Override
    protected void makeModel() {

        Model m = (Model) AssetManager.getInstance().get("modelDoor");
        BoundingBox b = new BoundingBox();
        m.calculateBoundingBox(b);
        float w = this.getWidth() / b.getWidth();
        float h = this.getMur().getDepth() / b.getHeight();
        float d = this.getHeight() / b.getDepth();
        this.setModel(m);
        model_transform.idt().rotate(0, 0, 1, 180).scale(w, h, d);
    }
}
