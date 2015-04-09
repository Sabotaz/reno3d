package fr.limsi.rorqual.core.model.primitives;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

import fr.limsi.rorqual.core.model.ModelProvider;

/**
 * Created by christophe on 08/04/15.
 */
public abstract class AbstractModelProvider implements ModelProvider {
    protected Matrix4 placement = new Matrix4();
    protected List<Vector3> points = new ArrayList<Vector3>();

    public List<Vector3> getPoints() {
        return points;
    }

    public Matrix4 getPosition() {
        return placement;
    }
}
