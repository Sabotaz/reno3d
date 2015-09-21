package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Slab;
import fr.limsi.rorqual.core.model.utils.Coin;
import fr.limsi.rorqual.core.model.utils.MyVector2;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;
import fr.limsi.rorqual.core.utils.scene3d.models.Anchor;

/**
 * Created by christophe on 04/09/15.
 */
// modification de la position de modèles
public class Mover extends ModelMaker {

    Coin initialCoin;
    Coin lastCoin;
    Coin newCoin;
    ArrayList<Mur> murs;
    ArrayList<Slab> slabs;
    boolean moving = false;

    @Override
    public void begin(int screenX, int screenY) {

        ModelGraph modelGraph = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph();
        ModelContainer modelContainer = modelGraph.getObject(screenX, screenY);
        Vector2 pos = new MyVector2(modelContainer.getIntersection());
        if (modelContainer == null) {
            moving = false;
        } else if (modelContainer instanceof Mur) {
            Coin A = ((Mur)modelContainer).getA();
            Coin B = ((Mur)modelContainer).getB();
            float d1 = A.getPosition().dst(pos);
            float d2 = B.getPosition().dst(pos);
            if (d1 < d2)
                initialCoin = A;
            else
                initialCoin = B;

            murs = (ArrayList)initialCoin.getMurs().clone();
            slabs = (ArrayList)initialCoin.getSlabs().clone();

            moving = true;
        }
    }

    Anchor anchor;

    @Override
    public void update(int screenX, int screenY) {

        if (!moving)
            return;

        ModelContainer obj = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().hit(screenX, screenY);
        int etage = ModelHolder.getInstance().getBatiment().getCurrentEtage().getNumber();

        if (obj != null) {
            Vector2 intersection = new MyVector2(obj.getIntersection());


            ArrayList<Object> forbidden = new ArrayList<Object>();

            Anchor a = calculateAnchor(etage, intersection, forbidden);

            lastCoin = newCoin;

            if (a != null) {
                newCoin = a.getPt();
                if (anchor != null) {
                    anchor.setPt(a.getPt());
                    anchor.setA(a.getA());
                    anchor.setB(a.getB());
                } else {
                    anchor = a;
                    ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().add(anchor);
                }
            } else {
                newCoin = Coin.getCoin(etage, intersection.cpy());
                if (anchor != null) {
                    ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().remove(anchor);
                    anchor = null;
                }
            }
            for (Slab s : slabs)
                s.remplaceCoin(lastCoin, newCoin);
            for (Mur m : murs)
                m.remplaceCoin(lastCoin, newCoin);

        } else {
            for (Slab s : slabs)
                s.remplaceCoin(newCoin, initialCoin);
            for (Mur m : murs)
                m.remplaceCoin(newCoin, initialCoin);
        }
    }

    @Override
    public void end(int screenX, int screenY) {
        moving = false;
    }

    @Override
    public void abort() {
        // TODO
    }

    @Override
    public boolean isStarted() {
        return moving;
    }
}
