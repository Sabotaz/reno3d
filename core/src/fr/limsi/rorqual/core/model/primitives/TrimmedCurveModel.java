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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import fr.limsi.rorqual.core.model.ModelProvider;
import fr.limsi.rorqual.core.utils.IfcObjectPlacementUtils;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCircle;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLine;
import ifc2x3javatoolbox.ifc2x3tc1.IfcParameterValue;
import ifc2x3javatoolbox.ifc2x3tc1.IfcTrimmedCurve;
import ifc2x3javatoolbox.ifc2x3tc1.IfcTrimmingSelect;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by christophe on 08/04/15.
 */
public class TrimmedCurveModel extends AbstractModelProvider {

    public Class basis_curve;

    public Vector3 start;
    public Vector3 end;

    public float circle_radius;
    public float angle_from;
    public float angle_to;

    public TrimmedCurveModel(IfcTrimmedCurve ifcTrimmedCurve) {

        if (ifcTrimmedCurve.getBasisCurve() instanceof IfcCircle) {
            basis_curve = IfcCircle.class;
            //Todo
            //http://www.buildingsmart-tech.org/ifc/IFC2x3/TC1/html/ifcgeometryresource/lexical/ifctrimmedcurve.htm
            //http://www.buildingsmart-tech.org/ifc/IFC2x3/TC1/html/ifcgeometryresource/lexical/ifccircle.htm

            IfcCircle circle = (IfcCircle) ifcTrimmedCurve.getBasisCurve();
            placement = IfcObjectPlacementUtils.toMatrix(circle.getPosition());
            circle_radius = (float)circle.getRadius().value;
            boolean sense = ifcTrimmedCurve.getSenseAgreement().value;

            IfcCartesianPoint CPtrim1 = null, CPtrim2 = null;
            IfcParameterValue PVtrim1 = null, PVtrim2 = null;

            for (IfcTrimmingSelect t : ifcTrimmedCurve.getTrim1()) {
                if (t instanceof IfcCartesianPoint)
                    CPtrim1 = (IfcCartesianPoint) t;
                else if (t instanceof IfcParameterValue)
                    PVtrim1 = (IfcParameterValue) t;
            }
            for (IfcTrimmingSelect t : ifcTrimmedCurve.getTrim2()) {
                if (t instanceof IfcCartesianPoint)
                    CPtrim2 = (IfcCartesianPoint) t;
                else if (t instanceof IfcParameterValue)
                    PVtrim2 = (IfcParameterValue) t;
            }


            if (PVtrim1 == null || PVtrim2 == null)
                throw new NotImplementedException();
            if (sense) {
                angle_from = (float)(PVtrim1.value * Math.PI / 180.);
                angle_to = (float)(PVtrim2.value * Math.PI / 180.);
            } else {
                angle_from = (float)(PVtrim2.value * Math.PI / 180.);
                angle_to = (float)(PVtrim1.value * Math.PI / 180.);
            }
            while (angle_to < angle_from)
                angle_to += 2 * Math.PI;
            System.out.println("angles: " + angle_from + "," + angle_to);

        }
        else if (ifcTrimmedCurve.getBasisCurve() instanceof IfcLine) {
            basis_curve = IfcLine.class;
            IfcLine line = (IfcLine) ifcTrimmedCurve.getBasisCurve();
            Vector3 pt = IfcObjectPlacementUtils.toVector(line.getPnt());
            Vector3 d = IfcObjectPlacementUtils.toVector(line.getDir());
            IfcCartesianPoint CPtrim1 = null, CPtrim2 = null;
            IfcParameterValue PVtrim1 = null, PVtrim2 = null;

            System.out.println("sense: "+ifcTrimmedCurve.getSenseAgreement());

            for (IfcTrimmingSelect t : ifcTrimmedCurve.getTrim1()) {
                if (t instanceof IfcCartesianPoint)
                    CPtrim1 = (IfcCartesianPoint) t;
                else if (t instanceof IfcParameterValue)
                    PVtrim1 = (IfcParameterValue) t;
            }
            for (IfcTrimmingSelect t : ifcTrimmedCurve.getTrim2()) {
                if (t instanceof IfcCartesianPoint)
                    CPtrim2 = (IfcCartesianPoint) t;
                else if (t instanceof IfcParameterValue)
                    PVtrim2 = (IfcParameterValue) t;
            }

            if (CPtrim1 != null)
                start = IfcObjectPlacementUtils.toVector(CPtrim1);
            else if (PVtrim1 != null)
                start = pt.cpy().add(d.cpy().scl((float)PVtrim1.value));
            else
                start = new Vector3();

            if (CPtrim2 != null)
                end = IfcObjectPlacementUtils.toVector(CPtrim2);
            else if (PVtrim2 != null)
                end = pt.cpy().add(d.cpy().scl((float)PVtrim2.value));
            else
                end = new Vector3();

        } else {
            System.out.println("ASSERT TYPE FALSE"); //Todo throw exception
        }

        make_points();
    }

    private void make_points() {

        if (basis_curve == IfcCircle.class) {
            for (int i = 0; i < 30; i++) {
                float theta1 = angle_from + (angle_to-angle_from) * (i / 30f);
                float x = (float)(circle_radius * Math.cos(theta1));
                float y = (float)(circle_radius * Math.sin(theta1));
                points.add(new Vector3(x,y,0));
            }

        } else {
            points.add(start);
            points.add(end);
        }
    }

    public Model getModel() {
        return null;
    }
}
