package fr.limsi.rorqual.core.model.primitives;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import fr.limsi.rorqual.core.model.ModelProvider;
import fr.limsi.rorqual.core.utils.IfcObjectPlacementUtils;
import ifc2x3javatoolbox.ifc2x3tc1.IfcArbitraryClosedProfileDef;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCompositeCurve;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCompositeCurveSegment;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCurve;
import ifc2x3javatoolbox.ifc2x3tc1.IfcExtrudedAreaSolid;
import ifc2x3javatoolbox.ifc2x3tc1.IfcPolyline;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProfileDef;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRectangleProfileDef;
import ifc2x3javatoolbox.ifc2x3tc1.LIST;

/**
 * Created by christophe on 08/04/15.
 */
public class ExtrudedAreaSolidModel extends AbstractModelProvider {

    private ModelProvider outerCurve;
    private Vector3 direction;
    private float depth;

    public ExtrudedAreaSolidModel(IfcExtrudedAreaSolid extrudedAreaSolid) {

        placement = IfcObjectPlacementUtils.toMatrix(extrudedAreaSolid.getPosition());

        IfcProfileDef profile = extrudedAreaSolid.getSweptArea();
        System.out.println("profile: " + profile.getProfileType()); // should be AREA :o

        if (profile instanceof IfcArbitraryClosedProfileDef) {
            IfcArbitraryClosedProfileDef arbitraryClosedProfileDef = (IfcArbitraryClosedProfileDef) profile;
            IfcCurve curve = arbitraryClosedProfileDef.getOuterCurve();
            System.out.println("curve: " + curve);
            if (curve instanceof IfcPolyline) {
                outerCurve = new PolylineModel((IfcPolyline) curve);
            } else if (curve instanceof IfcCompositeCurve) {
                outerCurve = new CompositeCurveModel((IfcCompositeCurve) curve);
            }
        } else if (profile instanceof IfcRectangleProfileDef) {
            outerCurve = new RectangleProfileDefModel((IfcRectangleProfileDef) profile);
            System.out.println("rect: " + profile);
        }

        direction = IfcObjectPlacementUtils.toVector(extrudedAreaSolid.getExtrudedDirection().getDirectionRatios());
        depth = (float)extrudedAreaSolid.getDepth().value;
    }

    public Model getModel() {
        ModelBuilder builder = new ModelBuilder();

        builder.begin();

        Node node = builder.node();
        node.id = "base";

        MeshPartBuilder meshBuilder;

        meshBuilder = builder.part("sweptsolid", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.WHITE)));

        meshBuilder.setVertexTransform(placement);
        for (int i = 0; i < outerCurve.getPoints().size(); i++) {
            Vector3 z_shape = direction.cpy().scl(depth);
            Vector3 p1 = outerCurve.getPoints().get(i);
            Vector3 p2 = outerCurve.getPoints().get((i + 1) % outerCurve.getPoints().size());
            Vector3 p3 = p1.cpy().add(z_shape);
            Vector3 p4 = p2.cpy().add(z_shape);
            meshBuilder.rect(p1, p2, p4, p3, direction);
        }
        return builder.end();
    }

}
