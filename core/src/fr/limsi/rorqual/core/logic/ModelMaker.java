package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.graphics.Camera;

import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by christophe on 05/08/15.
 */
public abstract class ModelMaker {

    public abstract void begin(int screenX, int screenY);

    public abstract void update(int screenX, int screenY);

    public abstract void end(int screenX, int screenY);

    public abstract void abort();

    public abstract boolean isStarted();

}
