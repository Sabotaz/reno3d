package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector2;

import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeFenetreEnum;

/**
 * Created by ricordeau on 20/07/15.
 */
public class Fenetre extends Ouverture {

    // Attributs
    private TypeFenetreEnum typeFenetre;

    // Constructeur
    public Fenetre(Mur mur, Vector2 position, float width, float height, Model model, TypeFenetreEnum typeFenetre) {
        super(mur, position, width, height, model);
        this.typeFenetre = typeFenetre;
    }

    // Getter & Setter
    public TypeFenetreEnum getTypeFenetre() {
        return typeFenetre;
    }
    public void setTypeFenetre(TypeFenetreEnum typeFenetre) {
        this.typeFenetre = typeFenetre;
    }

}
