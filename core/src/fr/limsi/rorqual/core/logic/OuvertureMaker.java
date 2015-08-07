package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

import fr.limsi.rorqual.core.model.Fenetre;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.ModelLoader;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Ouverture;
import fr.limsi.rorqual.core.model.Porte;
import fr.limsi.rorqual.core.model.utils.MyVector2;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;

/**
 * Created by christophe on 05/08/15.
 */

public class OuvertureMaker extends ModelMaker {

    String properties;

    public OuvertureMaker(String file) {
        properties = file;
    }

    Ouverture ouverture;
    boolean making_ouverture = false;

    public void begin(int screenX, int screenY) {

        ModelGraph modelGraph = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph();
        ModelContainer modelContainer = modelGraph.getObject(screenX, screenY);
        if (modelContainer == null) {
            making_ouverture = false;
        } else if (modelContainer instanceof Mur) {
            Mur mur = (Mur) modelContainer;
            Vector3 intersection = mur.getIntersection();
            // intersection in world space, not in wall space
            Vector2 v1 = new MyVector2(mur.getB().cpy().sub(mur.getA())).nor();
            Vector2 v2 = new MyVector2(intersection.cpy().sub(mur.getA()));
            float x = v2.dot(v1);
            ModelContainer container = ModelLoader.fromJson(properties);
            if (container instanceof Ouverture) {
                ouverture = (Ouverture) container;
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
            Vector3 intersection = mur.getIntersection();
            // intersection in world space, not in wall space
            Vector2 v1 = new MyVector2(mur.getB().cpy().sub(mur.getA())).nor();
            Vector2 v2 = new MyVector2(intersection.cpy().sub(mur.getA()));
            float x = v2.dot(v1);
            ouverture.setX(v2.dot(v1));
            ouverture.setMur(mur);
        }
    }

    public void end(int screenX, int screenY) {

        if (!making_ouverture)
            return;

        ouverture.setSelectable(true);

        making_ouverture = false;
    }

    public void abort() {

        if (!making_ouverture)
            return;

        ouverture.setMur(null);

        making_ouverture = false;
    }
}
