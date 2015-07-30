package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import java.util.ArrayList;

import fr.limsi.rorqual.core.model.Ouverture;
import fr.limsi.rorqual.core.model.Porte;
import fr.limsi.rorqual.core.model.utils.MyVector2;
import fr.limsi.rorqual.core.model.Fenetre;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;
import fr.limsi.rorqual.core.utils.scene3d.models.Anchor;
import fr.limsi.rorqual.core.utils.scene3d.models.Cote;

/**
 * Created by christophe on 20/03/15.
 */
public class Logic implements InputProcessor {

    private enum State {
        NONE,
        WALL,
        FENETRE,
        PORTE,
        ;
    }

    private Camera camera = null;

    private State currentState = State.NONE;

    private Logic() {}

    /** Holder */
    private static class LogicHolder
    {
        /** Instance unique non préinitialisée */
        private final static Logic INSTANCE = new Logic();
    }

    public static synchronized Logic getInstance() {
        return LogicHolder.INSTANCE;
    }

    public void setCamera(Camera c) {
        camera = c;
    }

    public void startWall() {
        currentState = State.WALL;
    }

    public void startFenetre() {
        currentState = State.FENETRE;
    }

    public void startPorte() {
        currentState = State.PORTE;
    }

    public void stop() {
        currentState = State.NONE;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    Vector3 start = new Vector3();
    Vector3 end = new Vector3();
    boolean making_wall = false;
    Mur mur;
    Ouverture ouverture;
    boolean making_ouverture = false;
    Vector2 pos = new Vector2();
    Anchor anchor = null;
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (currentState == State.WALL) {
            Ray ray = camera.getPickRay(screenX, screenY);
            Plane plane = new Plane(Vector3.Z.cpy(), Vector3.Zero.cpy()); /// floor

            Vector3 intersection = new Vector3();
//            boolean intersect = Intersector.intersectRayPlane(ray, plane, intersection);
//            if (!intersect) {
//                making_wall = false;
//                return false;
//            } else {
//                making_wall = true;
//            }
            ModelContainer obj = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().hit(screenX, screenY);

            if (obj == null) {
                making_wall = false;
                return false;
            } else {
                intersection = obj.getIntersection();
                making_wall = true;
            }

            Anchor a = calculateAnchor(intersection);

            if (a != null) {
                start = new Vector3(a.getPt());
                end = new Vector3(a.getPt());
                anchor = a;
            } else {
                start = new Vector3(intersection);
                end = new Vector3(intersection);
                anchor = null;
            }

            mur = new Mur(start, end) {
                public void act() {
                    super.act();
                    setA(start);
                    setB(end);
                }
            };

            mur.setSelectable(false);
            mur.add(new Cote(mur));
            //ModelHolder.getInstance().getBatiment().getCurrentEtage().addMur(mur);
            ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().add(mur);

            if (anchor != null)
                ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().add(anchor);
            //modelGraph.getRoot().add(wall);

            return true;
        } else if (currentState == State.FENETRE || currentState == State.PORTE) {
            Ray ray = camera.getPickRay(screenX, screenY);
            ModelGraph modelGraph = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph();
            ModelContainer modelContainer = modelGraph.getObject(screenX, screenY);
            if (modelContainer == null) {
                making_ouverture = false;
            } else if (modelContainer instanceof Mur) {
                Mur mur = (Mur) modelContainer;
                Vector3 intersection = mur.getIntersection();
                // intersection in world space, not in wall space
                Vector2 v1 = new MyVector2(mur.getB().cpy().sub(mur.getA())).nor();
                Vector2 v2 = new MyVector2(intersection.cpy().sub(mur.getA()));
                pos.x = v2.dot(v1);
                if (currentState == State.FENETRE) {
                    ouverture = new Fenetre(mur, pos.x) {
                        public void act() {
                            super.act();
                            setX(pos.x);
                        }
                    };
                } else {
                    ouverture = new Porte(mur, pos.x) {
                        public void act() {
                            super.act();
                            setX(pos.x);
                        }
                    };
                }
                making_ouverture = true;
            }
            return true;
        } else
            return false;
    }

    private Anchor calculateAnchor(Vector3 intersection) {
        // anchor
        float anchor_length = 0.5f;
        ArrayList<Mur> murs = ModelHolder.getInstance().getBatiment().getCurrentEtage().getMurs();

        ArrayList<Anchor> anchors = new ArrayList<Anchor>();

        for (Mur mur : murs) {
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

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (currentState == State.WALL && making_wall) {
            ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().remove(mur);
            if (anchor != null)
                ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().remove(anchor);
            anchor = null;

            if (!start.equals(end)) {
                Mur copy_mur = new Mur(mur);
                ModelHolder.getInstance().getBatiment().getCurrentEtage().addMur(copy_mur);
                start = end = null;
            }
            return true;

        } else if ((currentState == State.FENETRE || currentState == State.PORTE) && making_ouverture) {
            Mur mur = ouverture.getMur();
            mur.remove(ouverture);
            ouverture.setMur(null);
            if (currentState == State.FENETRE)
                new Fenetre(mur, pos.x);
            else if (currentState == State.PORTE)
                new Porte(mur, pos.x);
            return true;

        } else if (currentState == State.NONE)
            return false;
        else
            return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (currentState == State.WALL && making_wall) {
            Ray ray = camera.getPickRay(screenX, screenY);
            Plane plane = new Plane(Vector3.Z.cpy(), Vector3.Zero.cpy()); /// floor

            Vector3 intersection = new Vector3();
            //boolean intersect = Intersector.intersectRayPlane(ray, plane, intersection);

            ModelContainer obj = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().hit(screenX, screenY);

            if (obj != null) {
                intersection = obj.getIntersection();

                Anchor a = calculateAnchor(intersection);

                if (a != null) {
                    end.set(a.getPt());
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
                    end.set(pos);
                    if (anchor != null) {
                        ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().remove(anchor);
                        anchor = null;
                    }
                }
            }
            else
                end.set(start);

            return true;
        } else if ((currentState == State.FENETRE || currentState == State.PORTE) && making_ouverture) {
            Ray ray = camera.getPickRay(screenX, screenY);
            ModelGraph modelGraph = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph();
            ModelContainer modelContainer = modelGraph.getObject(screenX, screenY);
            if (modelContainer == null)
                return true;
            if (modelContainer instanceof Mur) {
                Mur mur = (Mur) modelContainer;
                Vector3 intersection = mur.getIntersection();
                // intersection in world space, not in wall space
                Vector2 v1 = new MyVector2(mur.getB().cpy().sub(mur.getA())).nor();
                Vector2 v2 = new MyVector2(intersection.cpy().sub(mur.getA()));
                pos.x = v2.dot(v1);
                ouverture.setMur(mur);
            }
            return true;
        } else if (currentState == State.NONE) {
            return false;
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
