package fr.limsi.rorqual.core.view.shaders;

import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.NumberUtils;

/**
 * Created by christophe on 06/07/15.
 */
public class ShaderAttribute extends Attribute {

    public final static String BillboardAlias = "Billboard";
    public final static long Billboard = register(BillboardAlias);
    public final static String SunAlias = "Sun";
    public final static long Sun = register(SunAlias);
    protected static long Mask = Billboard | Sun;
    /** Method to check whether the specified type is a valid ShaderAttribute type */
    public static Boolean is(final long type) {
        return (type & Mask) != 0;
    }

    public ShaderAttribute (final long type) {
        super(type);
        if (!is(type))
            throw new GdxRuntimeException("Invalid type specified");
    }

    public ShaderAttribute (final long type, final double value) {
        this(type);
    }

    /** copy constructor */
    public ShaderAttribute (ShaderAttribute other) {
        this(other.type);
    }

    @Override
    public Attribute copy () {
        return new ShaderAttribute(this);
    }

    @Override
    public int hashCode () {
        final int prime = 3671 /* pick a prime number and use it here */;
        //final long v = NumberUtils.doubleToLongBits(value);
        final long v = type;
        return prime * super.hashCode() + (int)(v^(v>>>32));
    }

}
