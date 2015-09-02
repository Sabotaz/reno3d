package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

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

            Anchor a = calculateAnchor(etage, intersection);

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
            }
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

            Anchor a = calculateAnchor(etage, intersection);

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
            etage.removeMur(m);
            dpeEventMurRemoved(m);
            m.setA(null);
            m.setB(null);
        }
    }

    private void fixDoubleWalls(Mur restant, Mur removed) {
        Slab slabGauche = removed.getSlabGauche();
        Slab slabDroit = removed.getSlabDroit();

        if (restant.getA() == removed.getA() && restant.getB() == removed.getB()) { // meme sense
            if (slabGauche != null)
                restant.setSlabGauche(slabGauche);
            if (slabDroit != null)
                restant.setSlabDroit(slabDroit);
        } else { // sens inverse
            if (slabDroit != null)
                restant.setSlabGauche(slabDroit);
            if (slabGauche != null)
                restant.setSlabDroit(slabGauche);
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

    float EPSILON = 0.000_001f;

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


    private Anchor calculateAnchor(int etage, Vector2 intersection) {
        // anchor
        ArrayList<Mur> murs = ModelHolder.getInstance().getBatiment().getMurs();

        ArrayList<Mur> forbidden = new ArrayList<Mur>();
        for (Mur m : this.murs)
            if (m != null)
                forbidden.add(m);
        ArrayList<Anchor> coins = new ArrayList<Anchor>();

        ArrayList<Coin> c = new ArrayList<Coin>();

        for (Mur mur : murs) {
            if (!forbidden.contains(mur)) {
                if (!c.contains(mur.getA()))
                    c.add(mur.getA());
                if (!c.contains(mur.getB()))
                    c.add(mur.getB());
            }
        }
        for (Coin coin : c) {
            coins.add(new Anchor(coin));
        }

        // anchor-aligned drawing
        ArrayList<Anchor> coins_align = new ArrayList<Anchor>();
        for (Coin coin : c) {
            Vector2 projx = intersection.cpy();
            projx.x = coin.getPosition().x;
            coins_align.add(new Anchor(Coin.getCoin(etage, projx)));
            // add the projection on Y
            Vector2 projy = intersection.cpy();
            projy.y = coin.getPosition().y;
            coins_align.add(new Anchor(Coin.getCoin(etage, projy)));
        }

        ArrayList<Anchor> double_coins_align = new ArrayList<Anchor>();
        // double-anchor-aligned drawing
        for (Coin c1 : c) {
            for (Coin c2 : c) {
                if (!c1.equals(c2)) {
                    if (Math.abs(c1.getPosition().x - c2.getPosition().x) > EPSILON
                            && Math.abs(c1.getPosition().y - c2.getPosition().y) > EPSILON) { // s'ils ne sont pas sur la meme ligne / colone

                        Vector2 projxy = intersection.cpy();
                        projxy.x = c1.getPosition().x;
                        projxy.y = c2.getPosition().y;
                        double_coins_align.add(new Anchor(Coin.getCoin(etage, projxy)));

                        Vector2 projyx = intersection.cpy();
                        projyx.y = c1.getPosition().y;
                        projyx.x = c2.getPosition().x;
                        double_coins_align.add(new Anchor(Coin.getCoin(etage, projyx)));
                    }
                }
            }
        }

        ArrayList<Anchor> grid_align = new ArrayList<Anchor>();

        Vector2 p0 = intersection.cpy();
        Vector2 p1 = intersection.cpy();
        Vector2 p2 = intersection.cpy();
        Vector2 p3 = intersection.cpy();

        float scale = 10;
        p0.x = (float) Math.ceil(p0.x   *scale)/scale;
        p0.y = (float) Math.ceil(p0.y   *scale)/scale;

        p1.x = (float) Math.ceil(p1.x   *scale)/scale;
        p1.y = (float) Math.floor(p1.y  *scale)/scale;

        p2.x = (float) Math.floor(p2.x  *scale)/scale;
        p2.y = (float) Math.ceil(p2.y   *scale)/scale;

        p3.x = (float) Math.floor(p3.x  *scale)/scale;
        p3.y = (float) Math.floor(p3.y  *scale)/scale;

        grid_align.add(new Anchor(Coin.getCoin(etage, p0)));
        grid_align.add(new Anchor(Coin.getCoin(etage, p1)));
        grid_align.add(new Anchor(Coin.getCoin(etage, p2)));
        grid_align.add(new Anchor(Coin.getCoin(etage, p3)));

        ArrayList<Anchor> grid_align2 = new ArrayList<Anchor>();

        p0 = intersection.cpy();
        p1 = intersection.cpy();
        p2 = intersection.cpy();
        p3 = intersection.cpy();

        scale = 1;
        p0.x = (float) Math.ceil(p0.x   *scale)/scale;
        p0.y = (float) Math.ceil(p0.y   *scale)/scale;

        p1.x = (float) Math.ceil(p1.x   *scale)/scale;
        p1.y = (float) Math.floor(p1.y  *scale)/scale;

        p2.x = (float) Math.floor(p2.x  *scale)/scale;
        p2.y = (float) Math.ceil(p2.y   *scale)/scale;

        p3.x = (float) Math.floor(p3.x  *scale)/scale;
        p3.y = (float) Math.floor(p3.y  *scale)/scale;

        grid_align2.add(new Anchor(Coin.getCoin(etage, p0)));
        grid_align2.add(new Anchor(Coin.getCoin(etage, p1)));
        grid_align2.add(new Anchor(Coin.getCoin(etage, p2)));
        grid_align2.add(new Anchor(Coin.getCoin(etage, p3)));

        // last is higher priority
        final float GRID_ANCHOR_LENGTH = .1f;
        final float GRID2_ANCHOR_LENGTH = .2f;
        final float ALIGN_ANCHOR_LENGTH = .5f;
        final float DOUBLE_ALIGN_ANCHOR_LENGTH = 1f;
        final float COINS_ANCHOR_LENGTH = 1f;
        Object prior_anchors[][] =  new Object[][]{
                {grid_align,            GRID_ANCHOR_LENGTH},
                {grid_align2,           GRID2_ANCHOR_LENGTH},
                {coins_align,           ALIGN_ANCHOR_LENGTH},
                {double_coins_align,    DOUBLE_ALIGN_ANCHOR_LENGTH},
                {coins,                 COINS_ANCHOR_LENGTH}
        } ;

        // return best one
        Anchor anchor = null;

        for (Object obj[] : prior_anchors) {
            float dist = -1;
            ArrayList<Anchor> anchors = (ArrayList<Anchor>) obj[0];
            float anchor_length = (float) obj[1];

            for (Anchor a : anchors) {
                float d = intersection.dst(a.getPt().getPosition());
                if (d < anchor_length && (d <= dist || dist == -1)) {
                    dist = d;
                    anchor = a;
                }
            }
        }

        if (anchor != null && anchor.getPt().getEtage() != etage) { // anchoring in a wrong stage
            anchor = new Anchor(Coin.getCoin(etage, anchor.getPt().getPosition()));
        }

        return anchor;
    }

    @Override
    public boolean isStarted() {
        return this.making_piece;
    }
}
