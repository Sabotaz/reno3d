package fr.limsi.rorqual.core.model.primitives;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.mihosoft.vrl.v3d.Polygon;
import eu.mihosoft.vrl.v3d.PropertyStorage;
import eu.mihosoft.vrl.v3d.Vertex;
import fr.limsi.rorqual.core.model.ModelProvider;
import fr.limsi.rorqual.core.utils.CSGUtils;
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
        for (IfcCompositeCurveSegment segment : segments) {
            ModelProvider parent = null;
            System.out.println(segment.getParentCurve() +": " + segment.getSameSense());
            if (segment.getParentCurve() instanceof IfcPolyline) {
                parent = new PolylineModel((IfcPolyline) segment.getParentCurve());
            } else if (segment.getParentCurve() instanceof IfcTrimmedCurve) {
                parent = new TrimmedCurveModel((IfcTrimmedCurve) segment.getParentCurve());
            }
            if (parent != null) {
                List<Vertex> pts = new ArrayList<Vertex>(parent.getVertex());

                if (!segment.getSameSense().value) {
                    Collections.reverse(pts);
                }
                for (Vertex pt : pts) {
                    pt.pos.mul(parent.getPosition());
                    vertex.add(pt);
                }
            }
        }
    }

}
