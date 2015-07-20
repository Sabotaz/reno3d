package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector2;

import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeDoorEnum;

/**
 * Created by ricordeau on 20/07/15.
 */
public class Porte extends Ouverture{

    // Attributs
    private TypeDoorEnum typePorte;

    // Constructeur
    public Porte(Mur mur, Vector2 position, float width, float height, Model model,TypeDoorEnum typeDoor) {
        super(mur, position, width, height, model);
        this.typePorte=typeDoor;
    }

    // Getter & Setter
    public TypeDoorEnum getTypePorte() {
        return typePorte;
    }
    public void setTypePorte(TypeDoorEnum typePorte) {
        this.typePorte = typePorte;
    }
}
