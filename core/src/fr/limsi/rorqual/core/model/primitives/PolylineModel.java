package fr.limsi.rorqual.core.model.primitives;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

import fr.limsi.rorqual.core.model.ModelProvider;
import fr.limsi.rorqual.core.utils.IfcObjectPlacementUtils;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcPolyline;

/**
 * Created by christophe on 08/04/15.
 */
public class PolylineModel extends AbstractModelProvider {

    public PolylineModel(IfcPolyline polyline) {
        for (IfcCartesianPoint point : polyline.getPoints()) {
            points.add(IfcObjectPlacementUtils.toVector(point));
        }
    }

    public Model getModel() {
        return null;
    }

}
