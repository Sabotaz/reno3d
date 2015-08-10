package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import fr.limsi.rorqual.core.model.Ouverture;
import fr.limsi.rorqual.core.model.Porte;
import fr.limsi.rorqual.core.model.utils.MyVector2;
import fr.limsi.rorqual.core.model.Fenetre;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;

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

        modelMaker = new OuvertureMaker("data/models/g3db/windows/fenetreCoulissante/properties.json");
    }

    public void startPorte() {
        stop();

        modelMaker = new OuvertureMaker("data/models/g3db/doors/DoorTest/properties.json");

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
            modelMaker.begin(screenX, screenY);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (modelMaker != null) {
            modelMaker.end(screenX, screenY);
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
