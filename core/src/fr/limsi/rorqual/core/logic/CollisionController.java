package fr.limsi.rorqual.core.logic;

import fr.limsi.rorqual.core.model.Etage;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Objet;
import fr.limsi.rorqual.core.model.Slab;

/**
 * Created by christophe on 27/01/16.
 */
public class CollisionController {

    boolean valid = false;
    float last_valid_x = 0;
    float last_valid_y = 0;
    Slab last_valid_slab = null;
    boolean has_valid_pos = false;
    Objet movedObjet = null;

    public void checkCollisions(float x, float y, Slab slab) {
        Etage etage = movedObjet.getSlab().getEtage();
        valid = true;
        for (Mur mur : etage.getMurs()) {
            if (movedObjet.intersects(mur))
                valid = false;
        }
        for (Objet objet : etage.getObjets()) {
            if (movedObjet != objet && movedObjet.intersects(objet))
                valid = false;
        }

        if (valid) {
            has_valid_pos = true;
            last_valid_x = x;
            last_valid_y = y;
            last_valid_slab = slab;
        } else {
            if (has_valid_pos) {
                movedObjet.setPosition(last_valid_x, last_valid_y);
                movedObjet.setSlab(last_valid_slab);
            }
        }
    }

    public void startNewCollision(Objet obj) {
        movedObjet = obj;
        has_valid_pos = false;
    }
}
