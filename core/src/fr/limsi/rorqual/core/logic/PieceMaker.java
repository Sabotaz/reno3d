package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Slab;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.models.Anchor;
import fr.limsi.rorqual.core.utils.scene3d.models.Cote;

/**
 * Created by christophe on 05/08/15.
 */
public class PieceMaker extends ModelMaker {

    Vector3 start = new Vector3();
    boolean making_piece = false;
    Mur[] murs = new Mur[4];
    Cote[] cotes = new Cote[2];
    Slab slab;
    Anchor anchor = null;

    public void begin(int screenX, int screenY) {

        Vector3 intersection;

        ModelContainer obj = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().hit(screenX, screenY);

        if (obj == null) {
            making_piece = false;
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

    public void update(int screenX, int screenY) {

        if (!making_piece)
            return;

        ModelContainer obj = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().hit(screenX, screenY);

        if (obj != null) {
            Vector3 intersection = obj.getIntersection();

            Anchor a = calculateAnchor(intersection);
            Vector3 end;

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
                end = intersection.cpy();
                end.z = 0;
                if (anchor != null) {
                    ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().remove(anchor);
                    anchor = null;
                }
            }

            Vector3[][] pts = new Vector3[4][];
            int signe_x = end.cpy().sub(start).x >= 0 ? 1 : -1;
            int signe_y = end.cpy().sub(start).y >= 0 ? 1 : -1;
            float d = Mur.DEFAULT_DEPTH;

            Vector3 pt0, pt1;

            pt0 = new Vector3(start.x, start.y, 0);
            pt0.add(Vector3.X.cpy().setLength(d/2).scl(signe_x));
            pt1 = new Vector3(start.x, end.y, 0);
            pt1.add(Vector3.X.cpy().setLength(d/2).scl(signe_x));
            pts[0] = new Vector3[]{pt0, pt1};

            pt0 = new Vector3(start.x, end.y, 0);
            pt0.add(Vector3.X.cpy().setLength(d).scl(signe_x)).add(Vector3.Y.cpy().setLength(d/2).scl(-signe_y));
            pt1 = new Vector3(end.x, end.y, 0);
            pt1.add(Vector3.X.cpy().setLength(d).scl(-signe_x)).add(Vector3.Y.cpy().setLength(d/2).scl(-signe_y));
            pts[1] = new Vector3[]{pt0, pt1};

            pt0 = new Vector3(end.x, end.y, 0);
            pt0.add(Vector3.X.cpy().setLength(d/2).scl(-signe_x));
            pt1 = new Vector3(end.x, start.y, 0);
            pt1.add(Vector3.X.cpy().setLength(d/2).scl(-signe_x));
            pts[2] = new Vector3[]{pt0, pt1};

            pt0 = new Vector3(end.x, start.y, 0);
            pt0.add(Vector3.X.cpy().setLength(d).scl(-signe_x)).add(Vector3.Y.cpy().setLength(d/2).scl(signe_y));
            pt1 = new Vector3(start.x, start.y, 0);
            pt1.add(Vector3.X.cpy().setLength(d).scl(signe_x)).add(Vector3.Y.cpy().setLength(d/2).scl(signe_y));
            pts[3] = new Vector3[]{pt0, pt1};

            for (int i = 0; i < 4; i++) {
                murs[i].setA(pts[i][0]);
                murs[i].setB(pts[i][1]);
            }

            ArrayList<Vector3> coins = new ArrayList<Vector3>();
            coins.add(new Vector3(start.x, start.y, 0).add(Vector3.X.cpy().setLength(d).scl(signe_x)).add(Vector3.Y.cpy().setLength(d).scl(signe_y)));
            coins.add(new Vector3(start.x, end.y, 0).add(Vector3.X.cpy().setLength(d).scl(signe_x)).add(Vector3.Y.cpy().setLength(d).scl(-signe_y)));
            coins.add(new Vector3(end.x, end.y, 0).add(Vector3.X.cpy().setLength(d).scl(-signe_x)).add(Vector3.Y.cpy().setLength(d).scl(-signe_y)));
            coins.add(new Vector3(end.x, start.y, 0).add(Vector3.X.cpy().setLength(d).scl(-signe_x)).add(Vector3.Y.cpy().setLength(d).scl(signe_y)));
            slab.setCoins(coins);

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


    private Anchor calculateAnchor(Vector3 intersection) {
        // anchor
        float anchor_length = 0.5f;
        ArrayList<Mur> murs = ModelHolder.getInstance().getBatiment().getCurrentEtage().getMurs();

        ArrayList<Anchor> anchors = new ArrayList<Anchor>();

        for (Mur mur : murs) {
            if (!murs.contains(mur))
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
