package fr.limsi.rorqual.core.model.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by christophe on 20/08/15.
 */
// wrapper Vector2 -> Vector3
public class MyVector3 extends Vector3 {
    public MyVector3(Vector2 v) {
        super(v.x, v.y, 0);
    }

    public MyVector3(Vector2 v, float z) {
        super(v.x, v.y, z);
    }
}
