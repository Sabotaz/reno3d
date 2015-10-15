package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import java.util.Timer;
import java.util.TimerTask;

import fr.limsi.rorqual.core.model.Objet;
import fr.limsi.rorqual.core.model.Ouverture;
import fr.limsi.rorqual.core.ui.ModelLibrary;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;

/**
 * Created by christophe on 20/03/15.
 */
// Singleton
// Moteur logique pour l'édition de la scene
public class Logic implements InputProcessor {

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

    ModelMaker modelMaker = null;

    public void startWall() {
        stop();

        modelMaker = new WallMaker();
    }

    public void startPiece() {
        stop();

        modelMaker = new PieceMaker();
    }

    public void startFenetre() {
        stop();

        //modelMaker = new OuvertureMaker(2);
    }

    public void move() {
        stop();

        modelMaker = new Mover();
    }

    public void delete() {
        stop();

        modelMaker = new Deleter();
    }

    public void delete(ModelContainer obj, Button deleteButton) {
        stop();

        Deleter.delete(obj);
        deleteButton.setChecked(false);
    }

    public void rotate_g() {
        stop();

        modelMaker = new Rotater(+1);
    }

    public void rotate_d() {
        stop();

        modelMaker = new Rotater(-1);
    }

    public void rotate_g(ModelContainer m, Button rotateButton) {
        stop();

        Rotater.rotate(+1, m, rotateButton);
    }

    public void rotate_d(ModelContainer m, Button rotateButton) {
        stop();

        Rotater.rotate(-1, m, rotateButton);
    }

    public void startModel() {
        stop();
        String id = ModelLibrary.getInstance().getCurrentModelId();
        Class cls = ModelLibrary.getInstance().getModelClassFromId(id);

        try {
            if (cls.newInstance() instanceof Ouverture)
                modelMaker = new OuvertureMaker(id);
            else if (cls.newInstance() instanceof Objet)
                modelMaker = new ObjetMaker(id);
        } catch (Exception e) {
            modelMaker = null;
        }
    }

    public void updateModel() {
        String id = ModelLibrary.getInstance().getCurrentModelId();
        ModelContainer newModelContainer = ModelLibrary.getInstance().getModelContainerFromId(id);

        ModelContainer oldModelContainer = MainApplicationAdapter.getSelected();
        if (oldModelContainer != null && newModelContainer != null && oldModelContainer.getCategory().equals(newModelContainer.getCategory())) {
            if (oldModelContainer instanceof Objet && newModelContainer instanceof Objet) {
                ((Objet) newModelContainer).setSlab(((Objet) oldModelContainer).getSlab());
                ((Objet) oldModelContainer).setSlab(null);
            } else {
                ModelContainer parent = oldModelContainer.getParent();

                parent.remove(oldModelContainer);
                parent.add(newModelContainer);
            }
            newModelContainer.local_transform = oldModelContainer.local_transform;

            Vector3 tra = new Vector3();

            newModelContainer.setPosition(oldModelContainer.getPosition());
            MainApplicationAdapter.setSelected(newModelContainer);
        }
    }

    public void startPorte() {
        stop();

        //modelMaker = new OuvertureMaker(1);

    }

    public void stop() {
        if (modelMaker != null)
            modelMaker.abort();
        modelMaker = null;
    }

    // INPUTS

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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (modelMaker != null) {
            if (modelMaker.isStarted())
                timer.cancel();
            else
                modelMaker.begin(screenX, screenY);
            return true;
        }
        return false;
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {

        }
    };


    Timer timer = new Timer();

    @Override
    public boolean touchUp(final int screenX, final int screenY, int pointer, int button) {
        if (modelMaker != null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    modelMaker.end(screenX, screenY);
                }
            }, 50L // 20ms ?
            );
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (modelMaker != null) {
            modelMaker.update(screenX, screenY);
            return true;
        }
        return false;
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
