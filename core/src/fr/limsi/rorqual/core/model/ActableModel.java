package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;

/**
 * Created by christophe on 20/07/15.
 */
public abstract class ActableModel extends Model {

    public Matrix4 transform = new Matrix4().idt();

    public abstract void act();

}
