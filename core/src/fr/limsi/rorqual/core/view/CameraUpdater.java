package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;

/**
 * Created by christophe on 06/08/15.
 */
public abstract class CameraUpdater implements InputProcessor {

    public abstract void act();

    public abstract Camera getCamera();

}
