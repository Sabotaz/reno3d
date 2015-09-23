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
                newCoin = initialCoin = A;
            else
                newCoin = initialCoin = B;

            murs = new ArrayList<Mur>(initialCoin.getMurs());
            slabs = new ArrayList<Slab>(initialCoin.getSlabs());

            for (Mur mur : murs)
                mur.setSelectable(false);

            moving = true;
        } else if (modelContainer instanceof Slab) {

            float d = 2.5f; // min dist to move
            for (Coin coin : ((Slab)modelContainer).getCoins()) {
                if (coin.getPosition().dst(pos) < d)
                    newCoin = initialCoin = coin;
            }
            if (initialCoin != null) {
                murs = new ArrayList<Mur>(initialCoin.getMurs());
                slabs = new ArrayList<Slab>(initialCoin.getSlabs());

                for (Mur mur : murs)
                    mur.setSelectable(false);

                moving = true;

            } else {
                moving = false;
            }
        }
    }

    Anchor anchor;

    @Override
    public synchronized void update(int screenX, int screenY) {

        if (!moving)
            return;

        ModelContainer obj = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().hit(screenX, screenY);
        int etage = ModelHolder.getInstance().getBatiment().getCurrentEtage().getNumber();

        if (obj != null) {
            Vector2 intersection = new MyVector2(obj.getIntersection());

            ArrayList<Object> forbidden = new ArrayList<Object>(murs);

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

        if (moving) {

            for (Slab s : slabs) {
                if (!s.isValide()) {
                    abort();
                    return;
                }
            }

            moving = false;
            for (Mur mur : murs)
                mur.setSelectable(true);
        }
        initialCoin = lastCoin = newCoin = null;
        murs = null;
        slabs = null;
        anchor = null;
    }

    @Override
    public void abort() {

        if (moving) {
            moving = false;
            for (Slab s : slabs)
                s.remplaceCoin(newCoin, initialCoin);
            for (Mur m : murs)
                m.remplaceCoin(newCoin, initialCoin);
            for (Mur mur : murs)
                mur.setSelectable(true);
        }
        initialCoin = lastCoin = newCoin = null;
        murs = null;
        slabs = null;
        anchor = null;
    }

    @Override
    public boolean isStarted() {
        return moving;
    }
}
