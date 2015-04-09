package fr.limsi.rorqual.core.model.primitives;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.limsi.rorqual.core.model.ModelProvider;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCompositeCurve;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCompositeCurveSegment;
import ifc2x3javatoolbox.ifc2x3tc1.IfcPolyline;
import ifc2x3javatoolbox.ifc2x3tc1.IfcTrimmedCurve;
import ifc2x3javatoolbox.ifc2x3tc1.LIST;

/**
 * Created by christophe on 08/04/15.
 */
public class CompositeCurveModel extends AbstractModelProvider {

    public CompositeCurveModel(IfcCompositeCurve compositeCurve) {

        LIST<IfcCompositeCurveSegment> segments = compositeCurve.getSegments();
        System.out.println("segments size: " + segments.size());
        for (IfcCompositeCurveSegment segment : segments) {
            ModelProvider parent = null;
            if (segment.getParentCurve() instanceof IfcPolyline) {
                parent = new PolylineModel((IfcPolyline) segment.getParentCurve());
            } else if (segment.getParentCurve() instanceof IfcTrimmedCurve) {
                parent = new TrimmedCurveModel((IfcTrimmedCurve) segment.getParentCurve());
            }
            if (parent != null) {
                List<Vector3> pts = new ArrayList<Vector3>(parent.getPoints());

                if (!segment.getSameSense().value) {
                    Collections.reverse(pts);
                }
                for (Vector3 pt : pts) {
                    points.add(pt.cpy().mul(parent.getPosition()));
                }
            }
            System.out.println("segment: " + segment);
            System.out.println("parent curve:" + segment.getParentCurve());
            System.out.println("sense:" + segment.getSameSense());
            System.out.println("transition:" + segment.getTransition());
        }
    }

    public Model getModel() {
        return null;
    }

}
