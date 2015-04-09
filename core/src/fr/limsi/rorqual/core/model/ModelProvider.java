package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.List;

/**
 * Created by christophe on 08/04/15.
 */
public interface ModelProvider {

    public Model getModel();

    public List<Vector3> getPoints();
    public Matrix4 getPosition();

}
