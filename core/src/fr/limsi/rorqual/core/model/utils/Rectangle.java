package fr.limsi.rorqual.core.model.utils;

import com.badlogic.gdx.math.ConvexHull;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.FloatArray;

import java.util.ArrayList;

/**
 * Created by christophe on 27/01/16.
 */
public class Rectangle {
    ArrayList<Vector3> pts = new ArrayList<Vector3>();
    public Rectangle(BoundingBox box) {
        pts.add(box.getCorner000(new Vector3()));
        pts.add(box.getCorner010(new Vector3()));
        pts.add(box.getCorner110(new Vector3()));
        pts.add(box.getCorner100(new Vector3()));
        pts.add(box.getCorner001(new Vector3()));
        pts.add(box.getCorner011(new Vector3()));
        pts.add(box.getCorner111(new Vector3()));
        pts.add(box.getCorner101(new Vector3()));
    }

    public Rectangle mul(Matrix4 mx) {
        for (Vector3 v : pts)
            v.mul(mx);
        return this;
    }

    public boolean overlaps(Rectangle other, Intersector.MinimumTranslationVector mtv) {
        Polygon p1 = this.getPolygon();
        Polygon p2 = other.getPolygon();
        if (Intersector.overlaps(p1.getBoundingRectangle(), p2.getBoundingRectangle()))
            return Intersector.overlapConvexPolygons(p2, p1, mtv);
        return false;
    }

    public Polygon getPolygon() {
        ConvexHull ch = new ConvexHull();
        FloatArray floatArray = new FloatArray();
        for (Vector3 v : pts)
            floatArray.addAll(v.x, v.y);
        float[] polypts = ch.computePolygon(floatArray, false).toArray();
        GeometryUtils.ensureCCW(polypts);
        float[] polypts2 = new float[polypts.length-2];
        System.arraycopy(polypts, 0, polypts2, 0, polypts.length-2);
        Polygon polygon = new Polygon(polypts2);
        return polygon;
    }
}
