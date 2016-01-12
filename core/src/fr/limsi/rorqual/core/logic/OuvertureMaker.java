package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.math.Vector2;

import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Ouverture;
import fr.limsi.rorqual.core.model.utils.MyVector2;
import fr.limsi.rorqual.core.ui.ModelLibrary;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;

/**
 * Created by christophe on 05/08/15.
 */
// classe permetant l'ajout d'ouvertures (Fenetres, Portes)
public class OuvertureMaker extends ModelMaker {

    String modelId;

    public OuvertureMaker(String modelId) {
        this.modelId = modelId;
    }

    Ouverture ouverture;
    boolean making_ouverture = false;

    @Override
    public boolean isStarted() {
        return this.making_ouverture;
    }

    public void begin(int screenX, int screenY) {

        ModelGraph modelGraph = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph();
        ModelContainer modelContainer = modelGraph.getObject(screenX, screenY);
        if (modelContainer == null) {
            making_ouverture = false;
        } else if (modelContainer instanceof Mur) {
            Mur mur = (Mur) modelContainer;
            Vector2 intersection = new MyVector2(mur.getIntersection());
            // intersection in world space, not in wall space
            Vector2 v1 = mur.getB().getPosition().cpy().sub(mur.getA().getPosition()).nor();
            Vector2 v2 = intersection.cpy().sub(mur.getA().getPosition());
            float x = v2.dot(v1);
            ModelContainer container = ModelLibrary.getInstance().getModelContainerFromId(modelId);
            if (container instanceof Ouverture) {
                ouverture = (Ouverture) container;
                ouverture.setModelId(modelId);
                ouverture.setMur(mur);
                ouverture.setX(x);
                ouverture.setSelectable(false);
                making_ouverture = true;
            } else {
                System.out.println("A very bad thing append here... " );
                making_ouverture = false;
            }
        }
    }

    public void update(int screenX, int screenY) {
        if (!making_ouverture)
            return;

        ModelGraph modelGraph = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph();
        ModelContainer modelContainer = modelGraph.getObject(screenX, screenY);
        if (modelContainer == null)
            return;
        if (modelContainer instanceof Mur) {
            Mur mur = (Mur) modelContainer;
            Vector2 intersection = new MyVector2(mur.getIntersection());
            // intersection in world space, not in wall space
            Vector2 v1 = mur.getB().getPosition().cpy().sub(mur.getA().getPosition()).nor();
            Vector2 v2 = intersection.cpy().sub(mur.getA().getPosition());
            ouverture.setX(v2.dot(v1));
            ouverture.setMur(mur);
        }
    }

    public void end(int screenX, int screenY) {

        if (!making_ouverture)
            return;

        ouverture.setSelectable(true);
        ModelHolder.notify(ouverture);

        making_ouverture = false;
    }

    public void abort() {

        if (!making_ouverture)
            return;

        ouverture.setMur(null);

        making_ouverture = false;
    }
}
