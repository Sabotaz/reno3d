package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

import java.util.ArrayList;
import java.util.Vector;

import fr.limsi.rorqual.core.model.Batiment;
import fr.limsi.rorqual.core.model.utils.MyVector2;
import fr.limsi.rorqual.core.utils.scene3d.ActableModel;
import fr.limsi.rorqual.core.model.Fenetre;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;

/**
 * Created by christophe on 20/03/15.
 */
public class Logic implements InputProcessor {

    private enum State {
        NONE,
        WALL,
        FENETRE,
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
    Fenetre fenetre;
    boolean making_fenetre = false;
    Vector2 pos = new Vector2();
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
            // anchor
            float anchor_length = 1.f;
            ArrayList<Mur> murs = ModelHolder.getInstance().getBatiment().getCurrentEtage().getMurs();

            ArrayList<Vector3> anchors = new ArrayList<Vector3>();

            for (Mur mur : murs)
                anchors.addAll(mur.getAnchors(intersection, Mur.DEFAULT_DEPTH));

            Vector3 anchor = null;
            float dist = -1;
            for (Vector3 v : anchors) {
                float d = intersection.cpy().sub(v).len();
                if (d < anchor_length && (d < dist || dist == -1)) {
                    dist = d;
                    anchor = v;
                }
            }

            if (anchor != null) {
                start = new Vector3(anchor);
                end = new Vector3(anchor);
            } else {
                start = new Vector3(intersection);
                end = new Vector3(intersection);
            }

            mur = new Mur(start, end) {
                public void act() {
                    super.act();
                    setA(start);
                    setB(end);
                }
            };
            mur.setSelectable(false);
            //ModelHolder.getInstance().getBatiment().getCurrentEtage().addMur(mur);
            ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().add(mur);
            //modelGraph.getRoot().add(wall);

            return true;
        } else if (currentState == State.FENETRE) {
            Ray ray = camera.getPickRay(screenX, screenY);
            ModelGraph modelGraph = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph();
            ModelContainer modelContainer = modelGraph.getObject(screenX, screenY);
            if (modelContainer == null) {
                making_fenetre = false;
            } else if (modelContainer instanceof Mur) {
                Mur mur = (Mur) modelContainer;
                Vector3 intersection = mur.getIntersection();
                // intersection in world space, not in wall space
                Vector2 v1 = new MyVector2(mur.getB().cpy().sub(mur.getA())).nor();
                Vector2 v2 = new MyVector2(intersection.cpy().sub(mur.getA()));
                pos.x = v2.dot(v1);
                fenetre = new Fenetre(mur, pos.x) {
                    public void act() {
                        super.act();
                        setX(pos.x);
                    }
                };
                making_fenetre = true;
            }
            return true;
        } else
            return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (currentState == State.WALL && making_wall) {
            ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph().getRoot().remove(mur);

            if (!start.equals(end)) {
                Mur copy_mur = new Mur(mur);
                ModelHolder.getInstance().getBatiment().getCurrentEtage().addMur(copy_mur);
            }
            return true;

        } else if (currentState == State.FENETRE && making_fenetre) {
            Mur mur = fenetre.getMur();
            mur.remove(fenetre);
            fenetre.setMur(null);

            new Fenetre(mur, pos.x);
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
                // anchor
                float anchor_length = 1.f;
                ArrayList<Mur> murs = ModelHolder.getInstance().getBatiment().getCurrentEtage().getMurs();

                ArrayList<Vector3> anchors = new ArrayList<Vector3>();

                for (Mur mur : murs)
                    anchors.addAll(mur.getAnchors(intersection, Mur.DEFAULT_DEPTH));

                Vector3 anchor = null;
                float dist = -1;
                for (Vector3 v : anchors) {
                    float d = intersection.cpy().sub(v).len();
                    if (d < anchor_length && (d < dist || dist == -1)) {
                        dist = d;
                        anchor = v;
                    }
                }

                if (anchor != null)
                    end.set(anchor);
                else {
                    Vector3 pos = intersection.cpy();
                    pos.z = 0;
                    end.set(pos);
                }
            }
            else
                end.set(start);

            return true;
        } else if (currentState == State.FENETRE && making_fenetre) {
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
                fenetre.setMur(mur);
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
