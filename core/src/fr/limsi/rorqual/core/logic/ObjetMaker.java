package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;

import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Objet;
import fr.limsi.rorqual.core.model.Slab;
import fr.limsi.rorqual.core.model.utils.MyVector2;
import fr.limsi.rorqual.core.ui.ModelLibrary;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;

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

    CollisionController collisionController;

    @Override
    public boolean isStarted() {
        return this.making_objet;
    }

    public void begin(int screenX, int screenY) {

        ModelGraph modelGraph = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph();
        ModelContainer modelContainer = modelGraph.getObject(screenX, screenY);
        if (modelContainer == null || modelId == null) {
            making_objet = false;
        } else if (modelContainer instanceof Slab) {
            Slab slab = (Slab) modelContainer;
            Vector2 intersection = new MyVector2(slab.getIntersection());
            // intersection in world space, not in wall space
            ModelContainer container = ModelLibrary.getInstance().getModelContainerFromId(modelId);
            obj = (Objet) container;
            obj.setPosition(intersection.x, intersection.y);
            obj.setSelectable(false);
            obj.setSlab(slab);
            obj.setModelId(modelId);
            slab.addObjet(obj);
            making_objet = true;
            obj.calculateBoundingBox(new BoundingBox());
            collisionController = new CollisionController();
            collisionController.startNewCollision(obj);
            collisionController.checkCollisions(intersection.x, intersection.y, slab);
            for (Mur mur : ModelHolder.getInstance().getBatiment().getCurrentEtage().getMurs())
                mur.setSelectable(false);
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
            collisionController.checkCollisions(intersection.x, intersection.y, slab);
        }
    }

    public void end(int screenX, int screenY) {

        if (!making_objet)
            return;

        for (Mur mur : ModelHolder.getInstance().getBatiment().getCurrentEtage().getMurs())
            mur.setSelectable(true);

        obj.setSelectable(true);
        ModelHolder.notify(obj);

        MainApplicationAdapter.LOG("AMENAGEMENT", "ADD_OBJET", "" + modelId);

        ModelLibrary.getInstance().uncheckAll();
        Logic.getInstance().end();

        making_objet = false;
        modelId = null;
    }

    public void abort() {

        if (!making_objet)
            return;

        for (Mur mur : ModelHolder.getInstance().getBatiment().getCurrentEtage().getMurs())
            mur.setSelectable(true);

        obj.setSlab(null);

        making_objet = false;
    }
}
