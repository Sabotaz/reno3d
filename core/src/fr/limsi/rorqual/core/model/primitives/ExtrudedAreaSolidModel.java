package fr.limsi.rorqual.core.model.primitives;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Extrude;
import eu.mihosoft.vrl.v3d.Polygon;
import eu.mihosoft.vrl.v3d.Vector3d;
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

    private CSG csg = null;

    public CSG toCSG() {
        return csg;
    }

    private void makePoints() {

        Matrix4 mx = placement.cpy().mul(outerCurve.getPosition());

        Vector3 z_shape = direction.cpy().scl(depth);


        Vector3d dir = CSGUtils.castVector(z_shape);

        List<Vector3d> face = new ArrayList<Vector3d>();
        for (Vertex v : outerCurve.getVertex())
            face.add(v.pos);

        csg = Extrude.points(dir, face);

        polygons = csg.getPolygons();

        for (Polygon p : polygons)
            for (Vertex v : p.vertices)
                v.pos.mul(mx);
    }

}
