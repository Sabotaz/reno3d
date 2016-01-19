package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import fr.limsi.rorqual.core.model.Etage;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Objet;
import fr.limsi.rorqual.core.model.Slab;
import fr.limsi.rorqual.core.model.utils.MyVector2;
import fr.limsi.rorqual.core.ui.ModelLibrary;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;

/**
 * Created by christophe on 15/09/15.
 */
// Classe permetant l'ajout d'objets (mobilier...)
public class ObjetMaker extends ModelMaker {

    String modelId;

    public ObjetMaker(String modelId) {
        this.modelId = modelId;
    }

    Objet obj;
    boolean making_objet = false;
    boolean has_valid_pos = false;
    boolean valid = false;
    float last_valid_x = 0;
    float last_valid_y = 0;
    Slab last_valid_slab = null;

    @Override
    public boolean isStarted() {
        return this.making_objet;
    }

    public void begin(int screenX, int screenY) {
        has_valid_pos = false;

        ModelGraph modelGraph = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph();
        ModelContainer modelContainer = modelGraph.getObject(screenX, screenY);
        if (modelContainer == null) {
            making_objet = false;
        } else if (modelContainer instanceof Slab) {
            Slab slab = (Slab) modelContainer;
            Vector2 intersection = new MyVector2(slab.getIntersection());
            // intersection in world space, not in wall space
            ModelContainer container = ModelLibrary.getInstance().getModelContainerFromId(modelId);
            if (container instanceof Objet) {
                obj = (Objet) container;
                obj.setPosition(intersection.x, intersection.y);
                obj.setSelectable(false);
                obj.setSlab(slab);
                obj.setModelId(modelId);
                slab.addObjet(obj);
                making_objet = true;
                obj.calculateBoundingBox(new BoundingBox());
                checkCollisions(intersection.x, intersection.y, slab);
            } else {
                System.out.println("A very bad thing append here... " );
                making_objet = false;
            }
        }
    }

    public void update(int screenX, int screenY) {
        if (!making_objet)
            return;
        ModelGraph modelGraph = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph();
        ModelContainer modelContainer = modelGraph.getObject(screenX, screenY);
        if (modelContainer == null)
            return;
        if (modelContainer instanceof Slab) {
            Slab slab = (Slab) modelContainer;
            Vector2 intersection = new MyVector2(slab.getIntersection());
            obj.setPosition(intersection.x, intersection.y);
            obj.setSlab(slab);
            checkCollisions(intersection.x, intersection.y, slab);
        }
    }

    private void checkCollisions(float x, float y, Slab slab) {
        Etage etage = obj.getSlab().getEtage();
        valid = true;
        for (Mur mur : etage.getMurs()) {
            if (obj.intersects(mur))
                valid = false;
        }
        for (Objet objet : etage.getObjets()) {
            if (obj != objet && obj.intersects(objet))
                valid = false;
        }

        if (valid) {
            has_valid_pos = true;
            last_valid_x = x;
            last_valid_y = y;
            last_valid_slab = slab;
        } else {
            if (has_valid_pos) {
                obj.setPosition(last_valid_x, last_valid_y);
                obj.setSlab(last_valid_slab);
            }
        }
    }

    public void end(int screenX, int screenY) {

        if (!making_objet)
            return;

        obj.setSelectable(true);
        ModelHolder.notify(obj);

        making_objet = false;
    }

    public void abort() {

        if (!making_objet)
            return;

        obj.setSlab(null);

        making_objet = false;
    }
}
