package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector;

/**
 * Created by christophe on 06/08/15.
 */
public abstract class CameraUpdater implements GestureDetector.GestureListener {

    public abstract void act();

    public abstract Camera getCamera();

    public abstract void updateViewport(int height, int width);

}
