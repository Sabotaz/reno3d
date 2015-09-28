package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

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

    public void startModel() {
        stop();
        String id = ModelLibrary.getInstance().getCurrentModelId();
        ModelContainer model = ModelLibrary.getInstance().getModelContainerFromId(id);
        if (model instanceof Ouverture)
            modelMaker = new OuvertureMaker(id);
        else if (model instanceof Objet)
            modelMaker = new ObjetMaker(id);
    }

    public void updateModel() {
        String id = ModelLibrary.getInstance().getCurrentModelId();
        ModelContainer newModelContainer = ModelLibrary.getInstance().getModelContainerFromId(id);

        ModelContainer oldModelContainer = MainApplicationAdapter.getSelected();
        if (oldModelContainer != null && newModelContainer != null && oldModelContainer.getCategory().equals(newModelContainer.getCategory())) {
            ModelContainer parent = oldModelContainer.getParent();
            newModelContainer.local_transform = oldModelContainer.local_transform;

            Vector3 tra = new Vector3();
            oldModelContainer.model_transform.getTranslation(tra);
            newModelContainer.model_transform.setTranslation(tra);

            parent.remove(oldModelContainer);
            parent.add(newModelContainer);
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
            }, 100L // 20ms ?
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
