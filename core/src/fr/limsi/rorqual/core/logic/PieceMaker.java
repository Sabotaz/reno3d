package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import fr.limsi.rorqual.core.model.Etage;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
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

        if (obj == null) {
            making_piece = false;
            return;
        } else {
            intersection = new MyVector2(obj.getIntersection());

            Anchor a = calculateAnchor(intersection);

            if (a != null) {
                start = a.getPt();
                anchor = a;
            } else {
                start = Coin.getCoin(intersection);
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
                mur.setSlab1(slab);
            }
            slab.setSelectable(false);
            ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().add(slab);

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

        if (obj != null) {
            Vector2 intersection = new MyVector2(obj.getIntersection());

            Anchor a = calculateAnchor(intersection);

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
                end = Coin.getCoin(intersection.cpy());
                if (anchor != null) {
                    ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().remove(anchor);
                    anchor = null;
                }
            }

            Coin[] coins = new Coin[4];
            coins[0] = start;
            coins[1] = Coin.getCoin(new Vector2(start.getPosition().x, end.getPosition().y));
            coins[2] = end;
            coins[3] = Coin.getCoin(new Vector2(end.getPosition().x, start.getPosition().y));;

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
            ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().remove(slab);
        }
        for (Mur mur: murs)
            mur.setSelectable(true);

        fixConflicts();

    }

    private void fixConflicts() {
        Etage etage = ModelHolder.getInstance().getBatiment().getCurrentEtage();
        for (Mur m : murs) {
            fixConflicts(m, etage.getMurs());
        }

        for (Mur m : extraWalls)
            etage.addMur(m);

        do {
            extraWalls.clear();

            for (Mur m : murs) {
                fixConflicts(m, etage.getMurs());
            }

            for (Mur m : extraWalls)
                etage.addMur(m);

        } while (extraWalls.size() > 0);

        ArrayList<Mur> removed = new ArrayList<Mur>();
        for (Mur m : etage.getMurs()) {
            for (Mur n : etage.getMurs()) {
                if (!m.equals(n))
                    if (!removed.contains(n) && areDouble(m, n))
                        removed.add(n);

            }
        }
        etage.getMurs().removeAll(removed);

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

    private void fixConflicts(Mur m1, Mur m2) {
        Vector2 a1 = m1.getA().getPosition();
        Vector2 b1 = m1.getB().getPosition();
        Vector2 a2 = m2.getA().getPosition();
        Vector2 b2 = m2.getB().getPosition();
        if (m1.getA() != m2.getA() && m1.getB() != m2.getA() && Intersector.distanceSegmentPoint(a1, b1, a2) < EPSILON) {
            // m2.A est entre m1.A et m1.B
            Coin A = m1.getA();
            Coin B = m1.getB();
            Coin C = m2.getA();
            m1.setB(C); // AC
            Mur extra = new Mur(C, B); // CB
            extraWalls.add(extra);
        } else
        if (m1.getA() != m2.getB() && m1.getB() != m2.getB() && Intersector.distanceSegmentPoint(a1, b1, b2) < EPSILON) {
            // m2.B est entre m1.A et m1.B
            Coin A = m1.getA();
            Coin B = m1.getB();
            Coin C = m2.getB();
            m1.setB(C); // AC
            Mur extra = new Mur(C, B); // CB
            extraWalls.add(extra);
        } else
        if (m1.getA() != m2.getA() && m1.getA() != m2.getB() && Intersector.distanceSegmentPoint(a2, b2, a1) < EPSILON) {
            // m1.A est entre m2.A et m2.B
            Coin A = m2.getA();
            Coin B = m2.getB();
            Coin C = m1.getA();
            m2.setB(C); // AC
            Mur extra = new Mur(C, B); // CB
            extraWalls.add(extra);
        }
        if (m1.getB() != m2.getA() && m1.getB() != m2.getB() && Intersector.distanceSegmentPoint(a2, b2, b1) < EPSILON) {
            // m1.B est entre m2.A et m2.B
            Coin A = m2.getA();
            Coin B = m2.getB();
            Coin C = m1.getB();
            m2.setB(C); // AC
            Mur extra = new Mur(C, B); // CB
            extraWalls.add(extra);
        }

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
        ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().remove(slab);

        making_piece = false;

    }


    private Anchor calculateAnchor(Vector2 intersection) {
        // anchor
        float anchor_length = 1f;
        ArrayList<Mur> murs = ModelHolder.getInstance().getBatiment().getCurrentEtage().getMurs();
        List<Mur> forbidden = Arrays.asList(this.murs);
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
            coins_align.add(new Anchor(Coin.getCoin(projx)));
            // add the projection on Y
            Vector2 projy = intersection.cpy();
            projy.y = coin.getPosition().y;
            coins_align.add(new Anchor(Coin.getCoin(projy)));
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
                        double_coins_align.add(new Anchor(Coin.getCoin(projxy)));

                        Vector2 projyx = intersection.cpy();
                        projyx.y = c1.getPosition().y;
                        projyx.x = c2.getPosition().x;
                        double_coins_align.add(new Anchor(Coin.getCoin(projyx)));
                    }
                }
            }
        }
        
        // last is higher priority
        Object prior_anchors[] =  new Object[]{coins_align, double_coins_align, coins} ;

        // return best one
        Anchor anchor = null;

        for (Object obj : prior_anchors) {
            float dist = -1;
            ArrayList<Anchor> anchors = (ArrayList<Anchor>) obj;
            for (Anchor a : anchors) {
                float d = intersection.dst(a.getPt().getPosition());
                if (d < anchor_length && (d <= dist || dist == -1)) {
                    dist = d;
                    anchor = a;
                }
            }
        }
        return anchor;
    }


}
