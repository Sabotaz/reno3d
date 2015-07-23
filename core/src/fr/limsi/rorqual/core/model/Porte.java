package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector2;

import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeDoorEnum;

/**
 * Created by ricordeau on 20/07/15.
 */
public class Porte extends Ouverture{

    static float DEFAULT_Y = 0.0f;
    static float DEFAULT_WIDHT = 1.0f;
    static float DEFAULT_HEIGHT = 2.15f;

    // Attributs
    private TypeDoorEnum typePorte;

    // Constructeur
    public Porte(Mur mur, float x) {
        this(mur, x, DEFAULT_Y, DEFAULT_WIDHT, DEFAULT_HEIGHT);
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
}
