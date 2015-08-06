package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;

import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeFenetreEnum;
import fr.limsi.rorqual.core.utils.AssetManager;

/**
 * Created by ricordeau on 20/07/15.
 */
public class Fenetre extends Ouverture {

    static float DEFAULT_Y = 1.f;
    static float DEFAULT_WIDTH = 0.6f;
    static float DEFAULT_HEIGHT = 0.75f;

    // Attributs
    private TypeFenetreEnum typeFenetre;

    // Constructeur
    public Fenetre(Mur mur, float x) {
        // x est le milieu ?
        this(mur, x - DEFAULT_WIDTH / 2, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Fenetre(Mur mur, float x, float y, float width, float height) {
        super(mur, new Vector2(x, y), width, height);
    }

    // Getter & Setter
    public TypeFenetreEnum getTypeFenetre() {
        return typeFenetre;
    }
    public void setTypeFenetre(TypeFenetreEnum typeFenetre) {
        this.typeFenetre = typeFenetre;
    }

    @Override
    protected void makeModel() {

        Model m = (Model) AssetManager.getInstance().get("modelWindow");
        BoundingBox b = new BoundingBox();
        m.calculateBoundingBox(b);
        float w = this.getWidth() / b.getWidth();
        float h = this.getMur().getDepth() / b.getHeight();
        float d = this.getHeight() / b.getDepth();
        this.setModel(m);
        model_transform.idt().scale(w, h, d);
    }
}
