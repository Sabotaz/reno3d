package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

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
    ArrayList<Mur> blockingWalls = new ArrayList<Mur>();

    public void checkCollisions(float x, float y, Slab slab) {
        Etage etage = movedObjet.getSlab().getEtage();
        blockingWalls.clear();
        valid = true;
        last_valid_slab = movedObjet.getSlab();

        for (Mur mur : etage.getMurs()) {
            if (movedObjet.intersects(mur, null)) {
                valid = false;
                blockingWalls.add(mur);
            }
        }
        for (Objet objet : etage.getObjets()) {
            if (movedObjet != objet && movedObjet.intersects(objet, null)) {
                valid = false;
            }
        }

        if (valid) {
            has_valid_pos = true;
            last_valid_x = x;
            last_valid_y = y;
            last_valid_slab = slab;
        } else {

            if (blockingWalls.size() == 1) {
                Mur mur = blockingWalls.get(0);
                float size = new Vector3(
                        movedObjet.getBoundingBox().getHeight(),
                        movedObjet.getBoundingBox().getWidth(),
                        movedObjet.getBoundingBox().getDepth()).len();
                Vector2 normal = mur.getB().getPosition().cpy().sub(mur.getA().getPosition()).nor();
                if (slab == mur.getSlabGauche())
                    normal.rotate90(1);
                else
                    normal.rotate90(-1);

                for (float i = 0.01f; i < size; i += 0.01f) {
                    movedObjet.setPosition(x + normal.x * i, y + normal.y * i);
                    valid = true;
                    for (Mur m : etage.getMurs()) {
                        if (movedObjet.intersects(m, null)) {
                            valid = false;
                        }
                    }
                    for (Objet objet : etage.getObjets()) {
                        if (movedObjet != objet && movedObjet.intersects(objet, null)) {
                            valid = false;
                        }
                    }
                    if (valid) {
                        has_valid_pos = true;
                        last_valid_x = x + normal.x * i;
                        last_valid_y = y + normal.y * i;
                        last_valid_slab = slab;
                        break;
                    }
                }
            }

            // end try
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
