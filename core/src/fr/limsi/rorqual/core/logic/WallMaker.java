package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import java.util.ArrayList;

import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.models.Anchor;
import fr.limsi.rorqual.core.utils.scene3d.models.Cote;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by christophe on 05/08/15.
 */
public class WallMaker extends ModelMaker {

    Vector3 start = new Vector3();
    boolean making_wall = false;
    Mur mur;
    Cote cote;
    Anchor anchor = null;

    public void begin(int screenX, int screenY) {

        Vector3 intersection;

        ModelContainer obj = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().hit(screenX, screenY);

        if (obj == null) {
            making_wall = false;
            return;
        } else {
            intersection = obj.getIntersection();

            Anchor a = calculateAnchor(intersection);

            if (a != null) {
                start = new Vector3(a.getPt());
                anchor = a;
            } else {
                start = new Vector3(intersection);
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

        if (obj != null) {
            Vector3 intersection = obj.getIntersection();

            Anchor a = calculateAnchor(intersection);

            if (a != null) {
                mur.setB(a.getPt());
                if (anchor != null) {
                    anchor.setPt(a.getPt());
                    anchor.setA(a.getA());
                    anchor.setB(a.getB());
                } else {
                    anchor = a;
                    ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().add(anchor);
                }
            } else {
                Vector3 pos = intersection.cpy();
                pos.z = 0;
                mur.setB(pos);
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


    private Anchor calculateAnchor(Vector3 intersection) {
        // anchor
        float anchor_length = 0.5f;
        ArrayList<Mur> murs = ModelHolder.getInstance().getBatiment().getCurrentEtage().getMurs();

        ArrayList<Anchor> anchors = new ArrayList<Anchor>();

        for (Mur mur : murs) {
            if (mur != this.mur)
                for (Vector3 v : mur.getAnchors(intersection, Mur.DEFAULT_DEPTH))
                    anchors.add(new Anchor(v));
        }

        // anchor-aligned drawing
        boolean project = true;
        /*for (Anchor a : anchors) { // don't project if too close to an anchor
            if (intersection.dst(a.getPt()) <= anchor_length)
                project = false;
        }*/

        if (project) {
            ArrayList<Anchor> alignments = new ArrayList<Anchor>();
            for (Anchor a : anchors) {
                Vector3 projx = intersection.cpy();
                projx.x = a.getPt().x;
                // add the projection on Y
                Vector3 projy = intersection.cpy();
                projy.y = a.getPt().y;

                if (start != null) { // y or x align each x or y  aligned anchor to the start
                    Vector3 projyx = projx.cpy();
                    projyx.y = start.y;
                    if (projyx.dst(projx) > anchor_length)
                        alignments.add(new Anchor(projx, a.getPt()));
                    alignments.add(new Anchor(projyx, a.getPt(), start));

                    Vector3 projxy = projy.cpy();
                    projxy.x = start.x;
                    if (projxy.dst(projy) > anchor_length)
                        alignments.add(new Anchor(projy, a.getPt()));
                    alignments.add(new Anchor(projxy, a.getPt(), start));
                }
            }

            anchors.addAll(alignments);

        }


        if (start != null) { // axe-aligned drawing
            // add the projection on X
            Vector3 projx = intersection.cpy();
            projx.x = start.x;
            anchors.add(new Anchor(projx, start));
            // add the projection on Y
            Vector3 projy = intersection.cpy();
            projy.y = start.y;
            anchors.add(new Anchor(projy, start));
        }


        Anchor anchor = null;
        float dist = -1;
        for (Anchor a : anchors) {
            float d = intersection.dst(a.getPt());
            if (d < anchor_length && (d < dist || dist == -1)) {
                dist = d;
                anchor = a;
            }
        }
        return anchor;
    }


}
