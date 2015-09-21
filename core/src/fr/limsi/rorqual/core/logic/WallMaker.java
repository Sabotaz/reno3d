package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.utils.Coin;
import fr.limsi.rorqual.core.model.utils.MyVector2;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.models.Anchor;
import fr.limsi.rorqual.core.utils.scene3d.models.Cote;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by christophe on 05/08/15.
 */
// Classe permetant l'ajout de murs simples
public class WallMaker extends ModelMaker {

    Coin start;
    boolean making_wall = false;
    Mur mur;
    Cote cote;
    Anchor anchor = null;

    @Override
    public boolean isStarted() {
        return this.making_wall;
    }

    public void begin(int screenX, int screenY) {

        Vector2 intersection;

        ModelContainer obj = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().hit(screenX, screenY);
        int etage = ModelHolder.getInstance().getBatiment().getCurrentEtage().getNumber();

        if (obj == null) {
            making_wall = false;
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

            mur = new Mur(start, start);

            mur.setSelectable(false);
            cote = new Cote(mur);
            mur.add(cote);

            ModelHolder.getInstance().getBatiment().getCurrentEtage().addMur(mur);

            if (anchor != null)
                ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().add(anchor);

            making_wall = true;
        }

    }

    public void update(int screenX, int screenY) {

        if (!making_wall)
            return;

        ModelContainer obj = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().hit(screenX, screenY);
        int etage = ModelHolder.getInstance().getBatiment().getCurrentEtage().getNumber();

        if (obj != null) {
            Vector2 intersection = new MyVector2(obj.getIntersection());

            Anchor a = calculateAnchor(etage, intersection);

            if (a != null) {
                Coin end = a.getPt();
                mur.setB(end);
                if (anchor != null) {
                    anchor.setPt(a.getPt());
                    anchor.setA(a.getA());
                    anchor.setB(a.getB());
                } else {
                    anchor = a;
                    ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().add(anchor);
                }
            } else {
                Vector2 pos = intersection.cpy();
                mur.setB(Coin.getCoin(etage, pos));
                if (anchor != null) {
                    ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().remove(anchor);
                    anchor = null;
                }
            }
        } else
            mur.setB(start);
    }

    public void end(int screenX, int screenY) {

        if (!making_wall)
            return;

        making_wall = false;

        if (anchor != null)
            ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().remove(anchor);
        anchor = null;

        mur.remove(cote);

        if (mur.getWidth() == 0)
            ModelHolder.getInstance().getBatiment().getCurrentEtage().removeMur(mur);

        mur.setSelectable(true);
        ModelHolder.notify(mur);

    }

    public void abort() {

        if (!making_wall)
            return;

        if (anchor != null)
            ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().remove(anchor);

        mur.remove(cote);
        ModelHolder.getInstance().getBatiment().getCurrentEtage().removeMur(mur);

        making_wall = false;

    }

    float EPSILON = 0.000_001f;

    private Anchor calculateAnchor(int etage, Vector2 intersection) {
        // anchor
        float anchor_length = 1f;
        ArrayList<Mur> murs = ModelHolder.getInstance().getBatiment().getCurrentEtage().getMurs();
        ArrayList<Anchor> anchors = new ArrayList<Anchor>();

        for (Mur mur : murs) {
            if (mur != this.mur) {
                anchors.add(new Anchor(mur.getA()));
                anchors.add(new Anchor(mur.getB()));
            }
        }

        // anchor-aligned drawing
        ArrayList<Anchor> alignments = new ArrayList<Anchor>();
        for (Anchor a : anchors) {
            Vector2 projx = intersection.cpy();
            projx.x = a.getPt().getPosition().x;
            alignments.add(new Anchor(Coin.getCoin(etage, projx)));
            // add the projection on Y
            Vector2 projy = intersection.cpy();
            projy.y = a.getPt().getPosition().y;
            alignments.add(new Anchor(Coin.getCoin(etage, projy)));
        }

        // double-anchor-aligned drawing
        for (Anchor a : anchors) {
            for (Anchor b : anchors) {
                if (!a.equals(b)) {
                    if (Math.abs(a.getPt().getPosition().x - b.getPt().getPosition().x) < EPSILON
                            && Math.abs(a.getPt().getPosition().y - b.getPt().getPosition().y) < EPSILON) { // s'ils ne sont pas sur la meme ligne / colone

                        Vector2 projxy = intersection.cpy();
                        projxy.x = a.getPt().getPosition().x;
                        projxy.y = b.getPt().getPosition().y;
                        alignments.add(new Anchor(Coin.getCoin(etage, projxy)));

                        Vector2 projyx = intersection.cpy();
                        projxy.y = a.getPt().getPosition().y;
                        projxy.x = b.getPt().getPosition().x;
                        alignments.add(new Anchor(Coin.getCoin(etage, projxy)));
                    }
                }
            }
        }

        anchors.addAll(alignments);

        // return best one
        Anchor anchor = null;
        float dist = -1;
        for (Anchor a : anchors) {
            float d = intersection.dst(a.getPt().getPosition());
            if (d < anchor_length && (d <= dist || dist == -1)) {
                dist = d;
                anchor = a;
            }
        }

        if (anchor != null && anchor.getPt().getEtage() != etage) { // anchoring in a wrong stage
            anchor = new Anchor(Coin.getCoin(etage, anchor.getPt().getPosition()));
        }

        return anchor;
    }


}
