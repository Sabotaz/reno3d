package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Objet;
import fr.limsi.rorqual.core.model.Ouverture;
import fr.limsi.rorqual.core.model.Slab;
import fr.limsi.rorqual.core.model.utils.Coin;
import fr.limsi.rorqual.core.model.utils.MyVector2;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;
import fr.limsi.rorqual.core.utils.scene3d.models.Anchor;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;

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

    boolean translate = false;
    Mur translatedMur;
    Coin initialCoinA, initialCoinB;
    Coin lastCoinA, lastCoinB;
    Coin newCoinA, newCoinB;
    int startx, starty;

    Objet movedObjet = null;
    boolean movingObject = false;

    Ouverture movedOuverture = null;
    boolean movingOuverture = false;

    @Override
    public void begin(int screenX, int screenY) {

        ModelGraph modelGraph = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph();
        ModelContainer modelContainer = modelGraph.getObject(screenX, screenY);
        if (modelContainer == null) {
            moving = false;
        } else if (modelContainer instanceof Mur) {
            Vector2 pos = new MyVector2(modelContainer.getIntersection());
            Coin A = ((Mur)modelContainer).getA();
            Coin B = ((Mur)modelContainer).getB();
            float d1 = A.getPosition().dst(pos);
            float d2 = B.getPosition().dst(pos);
            float d = A.getPosition().dst(B.getPosition()); // min dist to move
            if (d1 < d/4) {
                newCoin = initialCoin = A;
                translate = false;

                murs = new ArrayList<Mur>(initialCoin.getMurs());
                slabs = new ArrayList<Slab>(initialCoin.getSlabs());
            }
            else if (d2 < d/4) {
                newCoin = initialCoin = B;
                translate = false;

                murs = new ArrayList<Mur>(initialCoin.getMurs());
                slabs = new ArrayList<Slab>(initialCoin.getSlabs());
            }
            else {
                newCoinA = initialCoinA = A;
                newCoinB = initialCoinB = B;
                translatedMur = (Mur)modelContainer;
                translatedMur.setSelectable(false);
                startx = screenX;
                starty = screenY;
                translate = true;
                murs = new ArrayList<Mur>(initialCoinA.getMurs());
                murs.addAll(initialCoinB.getMurs());
                slabs = new ArrayList<Slab>(initialCoinA.getSlabs());
                slabs.addAll(initialCoinB.getSlabs());
            }

            for (Mur mur : murs)
                mur.setSelectable(false);

            moving = true;
        } else if (modelContainer instanceof Slab) {
            Vector2 pos = new MyVector2(modelContainer.getIntersection());

            float d = 1.5f; // min dist to move
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
        } else if (modelContainer instanceof Objet) {
            movedObjet = (Objet) modelContainer;
            movedObjet.setSelectable(false);
            moving = movingObject = true;
            translate = false;
        } else if (modelContainer instanceof Ouverture) {
            movedOuverture = (Ouverture) modelContainer;
            movedOuverture.setSelectable(false);
            moving = movingOuverture = true;
            translate = false;

        }
    }

    Anchor anchor;

    @Override
    public synchronized void update(int screenX, int screenY) {

        if (!moving)
            return;

        if (movingObject) {
            moveObjet(screenX, screenY);
        } else if (movingOuverture) {
            moveOuverture(screenX, screenY);
        } else if (translate) {
            translateMur(screenX, screenY);
        } else {
            moveCoin(screenX, screenY);
        }
    }

    public void moveObjet(int screenX, int screenY) {

        ModelGraph modelGraph = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph();
        ModelContainer modelContainer = modelGraph.getObject(screenX, screenY);
        if (modelContainer == null)
            return;
        if (modelContainer instanceof Slab) {
            Slab slab = (Slab) modelContainer;
            Vector2 intersection = new MyVector2(slab.getIntersection());
            movedObjet.setPosition(intersection.x, intersection.y);
            movedObjet.setSlab(slab);
        }
    }

    public void moveOuverture(int screenX, int screenY) {

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
            float x = v2.dot(v1);
            movedOuverture.setMur(mur);
            movedOuverture.setX(x);
        }
    }


    public void translateMur(int screenX, int screenY) {

        ModelContainer obj = ModelHolder.getInstance().getBatiment().hitCurrentEtage(screenX, screenY);
        int etage = ModelHolder.getInstance().getBatiment().getCurrentEtage().getNumber();

        if (obj != null) {
            Vector2 intersection = new MyVector2(obj.getIntersection());
            Vector2 normal = initialCoinB.getPosition().cpy().sub(initialCoinA.getPosition()).rotate90(0).nor();

            // projection
            Vector2 dv = normal.cpy().scl(normal.dot(intersection));
            Vector2 projA = normal.cpy().scl(normal.dot(initialCoinA.getPosition()));
            Vector2 projB = normal.cpy().scl(normal.dot(initialCoinB.getPosition()));

            Vector2 newA = dv.cpy().add(initialCoinA.getPosition()).sub(projA);
            Vector2 newB = dv.cpy().add(initialCoinB.getPosition()).sub(projB);

            lastCoinA = newCoinA;
            lastCoinB = newCoinB;
            newCoinA = Coin.getCoin(etage, newA.cpy());
            newCoinB = Coin.getCoin(etage, newB.cpy());

            for (Slab s : slabs) {
                s.remplaceCoin(lastCoinA, newCoinA);
                s.remplaceCoin(lastCoinB, newCoinB);
            }
            for (Mur m : murs) {
                m.remplaceCoin(lastCoinA, newCoinA);
                m.remplaceCoin(lastCoinB, newCoinB);
            }

        } else {
            for (Slab s : slabs) {
                s.remplaceCoin(newCoinA, initialCoinA);
                s.remplaceCoin(newCoinB, initialCoinB);
            }
            for (Mur m : murs) {
                m.remplaceCoin(newCoinA, initialCoinA);
                m.remplaceCoin(newCoinB, initialCoinB);
            }
        }
    }

    public void moveCoin(int screenX, int screenY) {

        ModelContainer obj = ModelHolder.getInstance().getBatiment().hitCurrentEtage(screenX, screenY);
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

            if (movingObject) {
                movedObjet.setSelectable(true);
                moving = false;
                movingObject = false;
                return;
            } else if (movingOuverture) {
                movedOuverture.setSelectable(true);
                moving = false;
                movingOuverture = false;
                return;
            }

            for (Slab s : slabs) {
                if (!s.isValide()) {
                    abort();
                    return;
                }
            }

            moving = false;
            for (Mur mur : murs)
                mur.setSelectable(true);
            if (translatedMur != null)
                translatedMur.setSelectable(true);
        }
        initialCoin = lastCoin = newCoin = null;
        murs = null;
        translatedMur = null;
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
        translate = false;
    }

    @Override
    public boolean isStarted() {
        return moving;
    }
}
