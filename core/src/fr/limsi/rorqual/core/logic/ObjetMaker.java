package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Objet;
import fr.limsi.rorqual.core.model.Slab;
import fr.limsi.rorqual.core.model.utils.MyVector2;
import fr.limsi.rorqual.core.ui.ModelLibrary;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;

/**
 * Created by christophe on 15/09/15.
 */
public class ObjetMaker extends ModelMaker {

    String modelId;

    public ObjetMaker(String modelId) {
        this.modelId = modelId;
    }

    Objet obj;
    boolean making_objet = false;

    @Override
    public boolean isStarted() {
        return this.making_objet;
    }

    public void begin(int screenX, int screenY) {

        ModelGraph modelGraph = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph();
        ModelContainer modelContainer = modelGraph.getObject(screenX, screenY);
        if (modelContainer == null) {
            making_objet = false;
        } else if (modelContainer instanceof Slab) {
            Slab slab = (Slab) modelContainer;
            Vector2 intersection = new MyVector2(slab.getIntersection());
            // intersection in world space, not in wall space
            ModelContainer container = ModelLibrary.getInstance().getModelFromId(modelId);
            if (container instanceof Objet) {
                obj = (Objet) container;
                Vector3 tra = new Vector3();
                obj.model_transform.getTranslation(tra);
                obj.model_transform.setTranslation(intersection.x, intersection.y, tra.z);
                obj.setSelectable(false);
                obj.setSlab(slab);
                making_objet = true;
                System.out.println(obj.calculateBoundingBox(new BoundingBox()));
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
            Vector3 tra = new Vector3();
            obj.model_transform.getTranslation(tra);
            obj.model_transform.setTranslation(intersection.x, intersection.y, tra.z);
            obj.setSlab(slab);
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
