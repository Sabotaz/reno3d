package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by christophe on 06/08/15.
 */
// Classe servant de base aux différents types de caméras
public abstract class CameraUpdater implements GestureDetector.GestureListener, InputProcessor {

    public String iconeName = "drawable:camera";

    protected Vector3 pos = new Vector3();

    public abstract void act();

    public abstract Camera getCamera();

    public abstract void updateViewport(int height, int width);

    public abstract void reset();

    public Vector3 getPosition() {
        return pos;
    }

}
