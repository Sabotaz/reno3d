package fr.limsi.rorqual.core.model.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by christophe on 20/07/15.
 */
// wrapper Vector3 -> Vector2
public class MyVector2 extends Vector2 {
    public MyVector2(Vector3 v3) {
        super(v3.x, v3.y);
    }
}
