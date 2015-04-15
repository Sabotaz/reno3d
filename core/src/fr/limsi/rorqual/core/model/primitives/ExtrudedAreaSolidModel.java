package fr.limsi.rorqual.core.model.primitives;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

import eu.mihosoft.vrl.v3d.Polygon;
import eu.mihosoft.vrl.v3d.Vertex;
import fr.limsi.rorqual.core.model.ModelProvider;
import fr.limsi.rorqual.core.utils.CSGUtils;
import fr.limsi.rorqual.core.utils.IfcObjectPlacementUtils;
import ifc2x3javatoolbox.ifc2x3tc1.IfcArbitraryClosedProfileDef;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCompositeCurve;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCurve;
import ifc2x3javatoolbox.ifc2x3tc1.IfcExtrudedAreaSolid;
import ifc2x3javatoolbox.ifc2x3tc1.IfcPolyline;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProfileDef;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRectangleProfileDef;

/**
 * Created by christophe on 08/04/15.
 */
public class ExtrudedAreaSolidModel extends AbstractModelProvider {

    private ModelProvider outerCurve;
    private Vector3 direction;
    private float depth;

    public ExtrudedAreaSolidModel(IfcExtrudedAreaSolid extrudedAreaSolid, Matrix4 lplacement) {
        this.placement = lplacement;
        placement.mul(IfcObjectPlacementUtils.toMatrix(extrudedAreaSolid.getPosition()));

        IfcProfileDef profile = extrudedAreaSolid.getSweptArea();

        if (profile instanceof IfcArbitraryClosedProfileDef) {
            IfcArbitraryClosedProfileDef arbitraryClosedProfileDef = (IfcArbitraryClosedProfileDef) profile;
            IfcCurve curve = arbitraryClosedProfileDef.getOuterCurve();
            if (curve instanceof IfcPolyline) {
                outerCurve = new PolylineModel((IfcPolyline) curve);
            } else if (curve instanceof IfcCompositeCurve) {
                outerCurve = new CompositeCurveModel((IfcCompositeCurve) curve);
            }
        } else if (profile instanceof IfcRectangleProfileDef) {
            outerCurve = new RectangleProfileDefModel((IfcRectangleProfileDef) profile);
        }

        direction = IfcObjectPlacementUtils.toVector(extrudedAreaSolid.getExtrudedDirection().getDirectionRatios());
        depth = (float)extrudedAreaSolid.getDepth().value;

        makePoints();
    }

    public ExtrudedAreaSolidModel(IfcExtrudedAreaSolid extrudedAreaSolid) {
        this(extrudedAreaSolid, new Matrix4());
    }

    private void makePoints() {
        polygons.clear();

        List<Vertex> base = new ArrayList<Vertex>();
        List<Vertex> top = new ArrayList<Vertex>();

        Matrix4 mx = placement.cpy().mul(outerCurve.getPosition());

        Vector3 z_shape = direction.cpy().scl(depth);
        for (int i = 0; i < outerCurve.getVertex().size(); i++) {
            Vertex p1 = outerCurve.getVertex().get(i).cpy();
            Vertex p2 = outerCurve.getVertex().get((i + 1) % outerCurve.getVertex().size()).cpy();
            Vertex p3 = p1.cpy();
            Vertex p4 = p2.cpy();

            Vector3 normal = CSGUtils.castVector(p1.pos).sub(CSGUtils.castVector(p2.pos)).crs(z_shape).mul(mx);

            p1.pos.mul(mx);
            p2.pos.mul(mx);
            p3.pos.add(z_shape).mul(mx);
            p4.pos.add(z_shape).mul(mx);

            p1.normal = CSGUtils.castVector(normal);
            p2.normal = CSGUtils.castVector(normal);
            p3.normal = CSGUtils.castVector(normal);
            p4.normal = CSGUtils.castVector(normal);

            List<Vertex> v = new ArrayList<Vertex>();
            v.add(p1);
            v.add(p2);
            v.add(p4);
            v.add(p3);

            Vertex bp = CSGUtils.toVertex(CSGUtils.castVector(p1.pos),direction.cpy().mul(mx).nor());
            Vertex tp = CSGUtils.toVertex(CSGUtils.castVector(p3.pos),direction.cpy().scl(-1).mul(mx).nor());

            base.add(bp);
            top.add(tp);

            polygons.add(new Polygon(v));
        }
        polygons.add(new Polygon(base));
        polygons.add(new Polygon(top));

    }

}
