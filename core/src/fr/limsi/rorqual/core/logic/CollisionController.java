package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.math.Intersector;

import fr.limsi.rorqual.core.model.Etage;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Objet;
import fr.limsi.rorqual.core.model.Slab;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;

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
    Intersector.MinimumTranslationVector mtv;

    public void checkCollisions(float x, float y, Slab slab) {
        Etage etage = movedObjet.getSlab().getEtage();
        valid = true;
        Intersector.MinimumTranslationVector _mtv = new Intersector.MinimumTranslationVector();

        for (Mur mur : etage.getMurs()) {
            if (movedObjet.intersects(mur, _mtv)) {
                valid = false;
                mtv = _mtv;
            }
        }
        for (Objet objet : etage.getObjets()) {
            if (movedObjet != objet && movedObjet.intersects(objet, _mtv)) {
                valid = false;
                mtv = _mtv;
            }
        }

        if (valid) {
            has_valid_pos = true;
            last_valid_x = x;
            last_valid_y = y;
            last_valid_slab = slab;
        } else {

            //// try MTV
            movedObjet.setPosition(x + mtv.normal.x*(mtv.depth*1.1f), y + mtv.normal.y*(mtv.depth*1.1f));
            valid = true;

            for (Mur mur : etage.getMurs()) {
                if (movedObjet.intersects(mur, null))
                    valid = false;
            }
            for (Objet objet : etage.getObjets()) {
                if (movedObjet != objet && movedObjet.intersects(objet, null))
                    valid = false;
            }
            if (valid) {
                has_valid_pos = true;
                last_valid_x = x;
                last_valid_y = y;
                last_valid_slab = slab;
            }
            /// end try
            else if (has_valid_pos) {
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
