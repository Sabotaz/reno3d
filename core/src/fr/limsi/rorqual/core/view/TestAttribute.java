package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by christophe on 20/04/15.
 */
public class TestAttribute extends Attribute {
    public final static String Alias = "Test";
    public final static long ID = register(Alias);

    public float value;

    protected TestAttribute (final float value) {
        super(ID);
        this.value = value;
    }

    @Override
    public Attribute copy () {
        return new TestAttribute(value);
    }

    @Override
    protected boolean equals (Attribute other) {
        return ((TestAttribute)other).value == value;
    }

    public int compareTo (Attribute o) {
        if (type != o.type) return type < o.type ? -1 : 1;
        float otherValue = ((TestAttribute)o).value;
        return MathUtils.isEqual(value, otherValue) ? 0 : (value < otherValue ? -1 : 1);
    }
}