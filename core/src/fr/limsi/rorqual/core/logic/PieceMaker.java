package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.model.Etage;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Ouverture;
import fr.limsi.rorqual.core.model.Slab;
import fr.limsi.rorqual.core.model.utils.Coin;
import fr.limsi.rorqual.core.model.utils.MyVector2;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.models.Anchor;
import fr.limsi.rorqual.core.utils.scene3d.models.Cote;

/**
 * Created by christophe on 05/08/15.
 */
// Classe permetant l'ajout d'une pièce (4 murs + un sol + 1 plafond)
// TODO: ajouter les plafonds
public class PieceMaker extends ModelMaker {

    Coin start;
    boolean making_piece = false;
    Mur[] murs = new Mur[4];
    Cote[] cotes = new Cote[2];
    Slab slab;
    Anchor anchor = null;

    public void begin(int screenX, int screenY) {
        Vector2 intersection;

        ModelContainer obj = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().hit(screenX, screenY);
        int etage = ModelHolder.getInstance().getBatiment().getCurrentEtage().getNumber();

        if (obj == null) {
            making_piece = false;
            return;
        } else {
            intersection = new MyVector2(obj.getIntersection());

            ArrayList<Object> forbidden = new ArrayList<Object>();
            for (Mur m : this.murs)
                if (m != null)
                    forbidden.add(m);

            Anchor a = calculateAnchor(etage, intersection, forbidden);

            if (a != null) {
                start = a.getPt();
                anchor = a;
            } else {
                start = Coin.getCoin(etage, intersection);
                anchor = null;
            }

            for (int i = 0; i < 4; i++) {
                murs[i] = new Mur(start, start);
                murs[i].setSelectable(false);
                ModelHolder.getInstance().getBatiment().getCurrentEtage().addMur(murs[i]);
            }

            cotes[0] = new Cote(murs[0]);
            murs[0].add(cotes[0]);

            cotes[1] = new Cote(murs[1]);
            murs[1].add(cotes[1]);

            ArrayList<Vector3> coins = new ArrayList<Vector3>();
            slab = new Slab(null);
            for (Mur mur : murs) {
                mur.setSlabGauche(slab);
                slab.addMur(mur);
            }
            slab.setEtage(ModelHolder.getInstance().getBatiment().getCurrentEtage());
            slab.setSelectable(false);
            ModelHolder.getInstance().getBatiment().getCurrentEtage().addSlab(slab);

            if (anchor != null)
                ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().add(anchor);

            making_piece = true;
        }

    }

    Coin end;

    public void update(int screenX, int screenY) {

        if (!making_piece)
            return;

        ModelContainer obj = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().hit(screenX, screenY);
        int etage = ModelHolder.getInstance().getBatiment().getCurrentEtage().getNumber();

        if (obj != null) {
            Vector2 intersection = new MyVector2(obj.getIntersection());

            ArrayList<Object> forbidden = new ArrayList<Object>();
            for (Mur m : this.murs)
                if (m != null)
                    forbidden.add(m);

            Anchor a = calculateAnchor(etage, intersection, forbidden);

            if (a != null) {
                end = a.getPt();
                if (anchor != null) {
                    anchor.setPt(a.getPt());
                    anchor.setA(a.getA());
                    anchor.setB(a.getB());
                } else {
                    anchor = a;
                    ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().add(anchor);
                }
            } else {
                end = Coin.getCoin(etage, intersection.cpy());
                if (anchor != null) {
                    ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().remove(anchor);
                    anchor = null;
                }
            }

            Coin[] coins = new Coin[4];
            coins[0] = start;
            coins[1] = Coin.getCoin(etage, new Vector2(start.getPosition().x, end.getPosition().y));
            coins[2] = end;
            coins[3] = Coin.getCoin(etage, new Vector2(end.getPosition().x, start.getPosition().y));;

            for (int i = 0; i < 4; i++) {
                murs[i].setA(coins[i]);
                murs[i].setB(coins[(i+1)%4]);
            }

            if (murs[0].getWidth() != 0 && murs[1].getWidth() != 0) {
                slab.setCoins(Arrays.asList(coins));
            } else {
                slab.setCoins(null);
            }

        } else {
            for (Mur mur : murs)
                mur.setB(start);
        }
    }

    public void end(int screenX, int screenY) {

        if (!making_piece)
            return;

        making_piece = false;

        if (anchor != null)
            ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().remove(anchor);
        anchor = null;


        murs[0].remove(cotes[0]);
        murs[1].remove(cotes[1]);

        if (murs[0].getWidth() == 0 || murs[1].getWidth() == 0) {
            for (Mur mur : murs)
                ModelHolder.getInstance().getBatiment().getCurrentEtage().removeMur(mur);
            ModelHolder.getInstance().getBatiment().getCurrentEtage().removeSlab(slab);
        }else{
            for (Mur mur: murs){
                mur.setSelectable(true);
                ModelHolder.notify(mur);
            }
            slab.setSelectable(true);
            ModelHolder.notify(slab);
            fixConflicts();
        }
        murs[0] = murs[1] = murs[2] = murs[3] = null;
    }

    private void fixConflicts() {
        Etage etage = ModelHolder.getInstance().getBatiment().getCurrentEtage();

        do {
            extraWalls.clear();

            for (Mur m : murs) {
                fixConflicts(m, etage.getMurs());
            }

            for (Mur m : extraWalls){
                etage.addMur(m);
                ModelHolder.notify(m);
            }

        } while (extraWalls.size() > 0);

        ArrayList<Mur> removed = new ArrayList<Mur>();
        for (Mur m : etage.getMurs()) {
            for (Mur n : etage.getMurs()) {
                if (!m.equals(n))
                    if (!removed.contains(m) && !removed.contains(n) && areDouble(m, n)) {
                        fixDoubleWalls(m, n);
                        removed.add(n);
                        ModelHolder.notify(m);
                    }

            }
        }

        for (Mur m : removed) {
            Deleter.deleteMur(m);
            dpeEventMurRemoved(m);
        }
    }

    private void fixDoubleWalls(Mur restant, Mur removed) {
        Slab slabGauche = removed.getSlabGauche();
        Slab slabDroit = removed.getSlabDroit();

        if (restant.getSlabGauche() != slabGauche && restant.getSlabDroit() != slabGauche) { // s'il existe pas deja
            if (restant.getSlabGauche() == null) {
                restant.setSlabGauche(slabGauche);
                slabGauche.addMur(restant);
            }
            else if (restant.getSlabDroit() == null) {
                restant.setSlabDroit(slabGauche);
                slabGauche.addMur(restant);
            }
            else; // no more place for this slab :'(
        }
        if (restant.getSlabGauche() != slabDroit && restant.getSlabDroit() != slabDroit) { // s'il existe pas deja
            if (restant.getSlabDroit() == null) {
                restant.setSlabDroit(slabDroit);
                slabDroit.addMur(restant);
            }
            else if (restant.getSlabGauche() == null) {
                restant.setSlabGauche(slabDroit);
                slabDroit.addMur(restant);
            }
            else; // no more place for this slab :'(
        }

        for (Ouverture o : removed.getOuvertures()) {
            removed.removeOuverture(o);
            if (restant.getA() == removed.getA() && restant.getB() == removed.getB()) { // meme sense
                // nothing to do
            } else {
                o.getPosition().x = restant.getWidth()-o.getPosition().x;
            }
            restant.addOuverture(o);
        }
    }

    ArrayList<Mur> extraWalls = new ArrayList<Mur>();

    private void fixConflicts(Mur mur, ArrayList<Mur> murs) {
        Iterator<Mur> it = murs.listIterator();
        while(it.hasNext()) {
            Mur m = it.next();
            if (m != mur) {
                fixConflicts(mur, m);
            }
        }
    }

    private void fixOuvertures (Mur m1, Mur extra) {
        assert m1.getB().equals(extra.getA());
        for (Ouverture o : m1.getOuvertures()) {
            if (o.getPosition().x > m1.getWidth()) {
                m1.removeOuverture(o);
                o.getPosition().x = o.getPosition().x - m1.getWidth();
                extra.addOuverture(o);
            }
        }
    }

    private void fixSlab(Slab slab, Coin A, Coin M, Coin B) {
        if (slab == null)
            return;
        ArrayList<Coin> coins = new ArrayList<Coin>(slab.getCoins());
        if (coins.contains(A) && coins.contains(B) && !coins.contains(M)) {
            int indiceA = coins.indexOf(A);
            int indiceB = coins.indexOf(B);
            if (indiceA < indiceB) {
                coins.add(indiceA+1, M);
            } else {
                coins.add(indiceB+1, M);
            }
            slab.setCoins(coins);
        }
    }

    private void fixConflicts(Mur m1, Mur m2) {
        Vector2 a1 = m1.getA().getPosition();
        Vector2 b1 = m1.getB().getPosition();
        Vector2 a2 = m2.getA().getPosition();
        Vector2 b2 = m2.getB().getPosition();
        Mur extra;
        if (m1.getA() != m2.getA() && m1.getB() != m2.getA() && Intersector.distanceSegmentPoint(a1, b1, a2) < EPSILON) {
            // m2.A est entre m1.A et m1.B
            Coin A = m1.getA();
            Coin B = m1.getB();
            Coin C = m2.getA();
            m1.setB(C); // AC
            extra = new Mur(C, B, m1); // CB
            extraWalls.add(extra);
            fixOuvertures(m1, extra);
            fixSlab(m1.getSlabGauche(), A, C, B);
            fixSlab(m1.getSlabDroit(), A, C, B);
            this.dpeEventSizeChanged(m1);
            ModelHolder.notify(m1);

        } else
        if (m1.getA() != m2.getB() && m1.getB() != m2.getB() && Intersector.distanceSegmentPoint(a1, b1, b2) < EPSILON) {
            // m2.B est entre m1.A et m1.B
            Coin A = m1.getA();
            Coin B = m1.getB();
            Coin C = m2.getB();
            m1.setB(C); // AC
            extra = new Mur(C, B, m1); // CB
            extraWalls.add(extra);
            fixOuvertures(m1, extra);
            fixSlab(m1.getSlabGauche(), A, C, B);
            fixSlab(m1.getSlabDroit(), A, C, B);
            this.dpeEventSizeChanged(m1);
            ModelHolder.notify(m1);
        } else
        if (m1.getA() != m2.getA() && m1.getA() != m2.getB() && Intersector.distanceSegmentPoint(a2, b2, a1) < EPSILON) {
            // m1.A est entre m2.A et m2.B
            Coin A = m2.getA();
            Coin B = m2.getB();
            Coin C = m1.getA();
            m2.setB(C); // AC
            extra = new Mur(C, B, m2); // CB
            extraWalls.add(extra);
            fixOuvertures(m2, extra);
            fixSlab(m2.getSlabGauche(), A, C, B);
            fixSlab(m2.getSlabDroit(), A, C, B);
            this.dpeEventSizeChanged(m2);
            ModelHolder.notify(m2);
        } else
        if (m1.getB() != m2.getA() && m1.getB() != m2.getB() && Intersector.distanceSegmentPoint(a2, b2, b1) < EPSILON) {
            // m1.B est entre m2.A et m2.B
            Coin A = m2.getA();
            Coin B = m2.getB();
            Coin C = m1.getB();
            m2.setB(C); // AC
            extra = new Mur(C, B, m2); // CB
            extraWalls.add(extra);
            fixOuvertures(m2, extra);
            fixSlab(m2.getSlabGauche(), A, C, B);
            fixSlab(m2.getSlabDroit(), A, C, B);
            this.dpeEventSizeChanged(m2);
            ModelHolder.notify(m2);
        }

    }

    private void dpeEventSizeChanged(Mur mur){
        HashMap<String,Object> currentItems = new HashMap<String,Object>();
        currentItems.put("userObject", mur);
        Event e = new Event(DpeEvent.SIZE_MUR_CHANGED, currentItems);
        EventManager.getInstance().put(Channel.DPE, e);
    }

    private void dpeEventMurRemoved(Mur mur){
        HashMap<String,Object> currentItems = new HashMap<String,Object>();
        currentItems.put("userObject", mur);
        Event e = new Event(DpeEvent.MUR_REMOVED, currentItems);
        EventManager.getInstance().put(Channel.DPE, e);
    }

    private boolean areDouble(Mur m1, Mur m2) {
        if ((m1.getA() == m2.getA() && m1.getB() == m2.getB())
                || (m1.getA() == m2.getB() && m1.getB() == m2.getA()))
            return true;
        return false;
    }

    public void abort() {

        if (!making_piece)
            return;

        if (anchor != null)
            ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().remove(anchor);

        murs[0].remove(cotes[0]);
        murs[1].remove(cotes[1]);

        for (Mur mur: murs)
            ModelHolder.getInstance().getBatiment().getCurrentEtage().removeMur(mur);
        ModelHolder.getInstance().getBatiment().getCurrentEtage().removeSlab(slab);
        murs[0] = murs[1] = murs[2] = murs[3] = null;
        making_piece = false;

    }


    @Override
    public boolean isStarted() {
        return this.making_piece;
    }
}
