package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.InputProcessor;

import java.util.Timer;
import java.util.TimerTask;

import fr.limsi.rorqual.core.ui.ModelLibrary;

/**
 * Created by christophe on 20/03/15.
 */
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

        modelMaker = new OuvertureMaker(2);
    }

    public void startModel() {
        stop();
        modelMaker = new OuvertureMaker(ModelLibrary.getInstance().getCurrentModelId());
    }

    public void startPorte() {
        stop();

        modelMaker = new OuvertureMaker(1);

    }

    public void stop() {
        if (modelMaker != null)
            modelMaker.abort();
        modelMaker = null;
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
