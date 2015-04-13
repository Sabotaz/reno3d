package fr.limsi.rorqual.core.model.primitives;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;

import fr.limsi.rorqual.core.utils.IfcObjectPlacementUtils;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRectangleProfileDef;

/**
 * Created by christophe on 13/04/15.
 */
public class RectangleProfileDefModel extends AbstractModelProvider {

    public RectangleProfileDefModel(IfcRectangleProfileDef rectangleProfileDef) {
        float xdim = (float)rectangleProfileDef.getXDim().value;
        float ydim = (float)rectangleProfileDef.getYDim().value;
        points.add(new Vector3(0,0,0));
        points.add(new Vector3(xdim,0,0));
        points.add(new Vector3(xdim,ydim,0));
        points.add(new Vector3(0,ydim,0));
        placement = IfcObjectPlacementUtils.toMatrix(rectangleProfileDef.getPosition());
        System.out.println("rect placement: " + placement);
    }

    public Model getModel() {
        return null;
    }
}
