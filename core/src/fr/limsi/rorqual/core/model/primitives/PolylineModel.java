package fr.limsi.rorqual.core.model.primitives;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

import fr.limsi.rorqual.core.model.ModelProvider;
import fr.limsi.rorqual.core.utils.CSGUtils;
import fr.limsi.rorqual.core.utils.IfcObjectPlacementUtils;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcPolyline;

/**
 * Created by christophe on 08/04/15.
 */
public class PolylineModel extends AbstractModelProvider {

    public PolylineModel(IfcPolyline polyline) {
        Vector3 normal = new Vector3(0,0,1);
        /*if (polyline.getPoints().size() >= 3) {
            Vector3 p1 = IfcObjectPlacementUtils.castVector(polyline.getPoints().get(0));
            Vector3 p2 = IfcObjectPlacementUtils.castVector(polyline.getPoints().get(1));
            Vector3 p3 = IfcObjectPlacementUtils.castVector(polyline.getPoints().get(2));
            Vector3 v1 = p2.cpy().sub(p1);
            Vector3 v2 = p3.cpy().sub(p1);
            normal = v1.crs(v2);
        }*/
        for (IfcCartesianPoint point : polyline.getPoints()) {
            Vector3 v = IfcObjectPlacementUtils.toVector(point);
            vertex.add(CSGUtils.toVertex(v,normal));
        }
    }

}
