package fr.limsi.rorqual.core.utils;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import ifc2x3javatoolbox.ifc2x3tc1.DOUBLE;
import ifc2x3javatoolbox.ifc2x3tc1.IfcAxis2Placement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcAxis2Placement2D;
import ifc2x3javatoolbox.ifc2x3tc1.IfcAxis2Placement3D;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDirection;
import ifc2x3javatoolbox.ifc2x3tc1.IfcGridPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLengthMeasure;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLocalPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcObjectPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.LIST;

/**
 * Created by christophe on 31/03/15.
 */
public class IfcObjectPlacementUtils {

    public static Matrix4 toMatrix(IfcAxis2Placement placement) {

        if (placement instanceof IfcAxis2Placement3D)
            return toMatrix((IfcAxis2Placement3D) placement);
        else if (placement instanceof IfcAxis2Placement2D)
            return toMatrix((IfcAxis2Placement2D) placement);
        else
            return new Matrix4();
    }

    public static Matrix4 toMatrix(IfcAxis2Placement3D placement3D) {

        IfcDirection ifc_x = placement3D.getRefDirection();
        IfcDirection ifc_z = placement3D.getAxis();
        IfcCartesianPoint ifc_p = placement3D.getLocation();

        Vector3 x = new Vector3(1.f,0.f,0.f);
        Vector3 z = new Vector3(0.f,0.f,1.f);

        if (ifc_x != null) {
            LIST<DOUBLE> xd = ifc_x.getDirectionRatios();
            x.set((float) xd.get(0).value, (float) xd.get(1).value, (float) xd.get(2).value);
        }
        if (ifc_z != null) {
            LIST<DOUBLE> zd = ifc_z.getDirectionRatios();
            z.set((float)zd.get(0).value, (float)zd.get(1).value, (float)zd.get(2).value);
        }
        LIST<IfcLengthMeasure> pd = ifc_p.getCoordinates();

        Vector3 p = new Vector3((float)pd.get(0).value, (float)pd.get(1).value, (float)pd.get(2).value);
        Vector3 y = z.cpy().crs(x);

        return new Matrix4().set(x,y,z,p);
    }

    public static Matrix4 toMatrix(IfcAxis2Placement2D placement2D) {

        IfcDirection ifc_x = placement2D.getRefDirection();
        IfcCartesianPoint ifc_p = placement2D.getLocation();

        Vector3 x = new Vector3(1.f,0.f,0.f);
        Vector3 z = new Vector3(0.f,0.f,1.f);

        if (ifc_x != null) {
            LIST<DOUBLE> xd = ifc_x.getDirectionRatios();
            x.set((float) xd.get(0).value, (float) xd.get(1).value, 0.f);
        }
        LIST<IfcLengthMeasure> pd = ifc_p.getCoordinates();

        Vector3 p = new Vector3((float)pd.get(0).value, (float)pd.get(1).value, 0.f);
        Vector3 y = z.cpy().crs(x);

        return new Matrix4().set(x,y,z,p);
    }


    // recursively compute the matrix4 of a placement
    public static Matrix4 computeMatrix(IfcObjectPlacement placement) {
        if (placement instanceof IfcLocalPlacement) {
            IfcLocalPlacement localPlacement = (IfcLocalPlacement) placement;
            if (localPlacement.getPlacementRelTo() != null) {
                Matrix4 parentLoc = computeMatrix(localPlacement.getPlacementRelTo());
                Matrix4 loc = IfcObjectPlacementUtils.toMatrix(localPlacement.getRelativePlacement());
                return parentLoc.mul(loc); // est-ce le bon sens ?

            } else { // no parents
                return IfcObjectPlacementUtils.toMatrix(localPlacement.getRelativePlacement());
            }
        } else if (placement instanceof IfcGridPlacement) {
            return new Matrix4(); //ToDo
        }
        return new Matrix4(); // n'arrive normalement jamais
    }

}
