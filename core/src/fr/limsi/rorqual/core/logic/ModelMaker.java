package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.utils.Coin;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.models.Anchor;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by christophe on 05/08/15.
 */

// Classe servant de base à l'édition de modèles
// contient notamment le calcul des ancres
public abstract class ModelMaker {

    public abstract void begin(int screenX, int screenY);

    public abstract void update(int screenX, int screenY);

    public abstract void end(int screenX, int screenY);

    public abstract void abort();

    public abstract boolean isStarted();

    final static float EPSILON = 0.000_001f;

    protected static Anchor calculateAnchor(int etage, Vector2 intersection, ArrayList<Object> forbidden) {
        // anchor
        ArrayList<Mur> murs = ModelHolder.getInstance().getBatiment().getMurs();

        ArrayList<Anchor> coins = new ArrayList<Anchor>();

        ArrayList<Coin> c = new ArrayList<Coin>();

        // les coins sont des ancres
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

        // la grille est une ancre
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
}
