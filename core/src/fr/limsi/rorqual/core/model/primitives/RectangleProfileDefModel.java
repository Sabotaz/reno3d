package fr.limsi.rorqual.core.model.primitives;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;

import fr.limsi.rorqual.core.utils.CSGUtils;
import fr.limsi.rorqual.core.utils.IfcObjectPlacementUtils;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRectangleProfileDef;

/**
 * Created by christophe on 13/04/15.
 */
public class RectangleProfileDefModel extends AbstractModelProvider {

    public RectangleProfileDefModel(IfcRectangleProfileDef rectangleProfileDef) {
        placement = IfcObjectPlacementUtils.toMatrix(rectangleProfileDef.getPosition());
        float xdim = (float)rectangleProfileDef.getXDim().value;
        float ydim = (float)rectangleProfileDef.getYDim().value;
        System.out.println(xdim + "," + ydim);
        Vector3 v1 = new Vector3(0,0,0);
        Vector3 v2 = new Vector3(xdim,0,0);
        Vector3 v3 = new Vector3(xdim,ydim,0);
        Vector3 v4 = new Vector3(0,ydim,0);
        Vector3 normal = new Vector3(0,0,1);
        vertex.add(CSGUtils.toVertex(v1,normal));
        vertex.add(CSGUtils.toVertex(v2,normal));
        vertex.add(CSGUtils.toVertex(v3,normal));
        vertex.add(CSGUtils.toVertex(v4,normal));
    }

}
