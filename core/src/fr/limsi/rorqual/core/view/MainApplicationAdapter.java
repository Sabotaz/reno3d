package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.Collection;

import fr.limsi.rorqual.core.utils.DefaultMutableTreeNode;

import fr.limsi.rorqual.core.model.IfcHolder;
import ifc2x3javatoolbox.ifc2x3tc1.DOUBLE;
import ifc2x3javatoolbox.ifc2x3tc1.IfcAxis2Placement2D;
import ifc2x3javatoolbox.ifc2x3tc1.IfcAxis2Placement3D;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDirection;
import ifc2x3javatoolbox.ifc2x3tc1.IfcGridPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLengthMeasure;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLocalPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcObjectPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRoot;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWall;
import ifc2x3javatoolbox.ifc2x3tc1.LIST;
import ifc2x3javatoolbox.ifc2x3tc1.SET;
import ifc2x3javatoolbox.ifcmodel.IfcModel;

public class MainApplicationAdapter extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;


    @Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("data/gdx/badlogic.jpg");

        DefaultMutableTreeNode spatialStructureTreeNode = IfcHolder.getInstance().getSpatialStructureTreeNode();
        print(spatialStructureTreeNode, 0);
	}

    private LIST<IfcLengthMeasure> computeLocation(IfcObjectPlacement placement) {
        if (placement instanceof IfcLocalPlacement) {
            IfcLocalPlacement localPlacement = (IfcLocalPlacement) placement;
            if (localPlacement.getPlacementRelTo() != null) {
                LIST<IfcLengthMeasure> parentLoc = computeLocation(localPlacement.getPlacementRelTo());
                if (localPlacement.getRelativePlacement() instanceof IfcAxis2Placement3D) {
                    IfcAxis2Placement3D placement3D = (IfcAxis2Placement3D) localPlacement.getRelativePlacement();
                    LIST<IfcLengthMeasure> loc = placement3D.getLocation().getCoordinates();
                    for (int i = 0; i < parentLoc.size(); i++) {
                        parentLoc.set(i, new IfcLengthMeasure(parentLoc.get(i).value + loc.get(i).value));
                    }
                    return parentLoc;
                }
            } else {
                if (localPlacement.getRelativePlacement() instanceof IfcAxis2Placement3D) {
                    IfcAxis2Placement3D placement3D = (IfcAxis2Placement3D) localPlacement.getRelativePlacement();
                    return placement3D.getLocation().getCoordinates();
                }
            }
        }
        return null;
    }

    private Matrix4 computeMatrix(IfcObjectPlacement placement) {
        if (placement instanceof IfcLocalPlacement) {
            IfcLocalPlacement localPlacement = (IfcLocalPlacement) placement;
            if (localPlacement.getPlacementRelTo() != null) {
                Matrix4 parentLoc = computeMatrix(localPlacement.getPlacementRelTo());
                if (localPlacement.getRelativePlacement() instanceof IfcAxis2Placement3D) {
                    Matrix4 loc = toMatrix((IfcAxis2Placement3D) localPlacement.getRelativePlacement());
                    return parentLoc.mul(loc); // est-ce le bon sens ?
                } else if (localPlacement.getRelativePlacement() instanceof IfcAxis2Placement2D) {
                    Matrix4 loc = toMatrix((IfcAxis2Placement2D) localPlacement.getRelativePlacement());
                    return parentLoc.mul(loc);
                }
            } else {
                if (localPlacement.getRelativePlacement() instanceof IfcAxis2Placement3D) {
                    return toMatrix((IfcAxis2Placement3D) localPlacement.getRelativePlacement());
                } else if (localPlacement.getRelativePlacement() instanceof IfcAxis2Placement2D) {
                    return toMatrix((IfcAxis2Placement2D) localPlacement.getRelativePlacement());
                }
            }
        } else if (placement instanceof IfcGridPlacement) {
            return new Matrix4(); //ToDo
        }
        return new Matrix4(); // n'arrive normalement jamais
    }

    public Matrix4 toMatrix(IfcAxis2Placement3D placement3D) {

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

    public Matrix4 toMatrix(IfcAxis2Placement2D placement2D) {

        IfcDirection ifc_x = placement2D.getRefDirection();
        IfcCartesianPoint ifc_p = placement2D.getLocation();

        Vector3 x = new Vector3(1.f,0.f,0.f);
        Vector3 z = new Vector3(0.f,0.f,1.f);

        if (ifc_x != null) {
            LIST<DOUBLE> xd = ifc_x.getDirectionRatios();
            x.set((float) xd.get(0).value, (float) xd.get(1).value, 0.f);
        }
        LIST<IfcLengthMeasure> pd = ifc_p.getCoordinates();

        Vector3 p = new Vector3((float)pd.get(0).value, (float)pd.get(1).value, (float)pd.get(2).value);
        Vector3 y = z.cpy().crs(x);

        return new Matrix4().set(x,y,z,p);
    }


    /*public Matrix4 computeLocation(IfcObjectPlacement placement) {
        return null;
    }*/

    public void print(DefaultMutableTreeNode treeNode, int tab) {

        for (int i = 0; i < tab; i++)
            System.out.print(" ");
        if (treeNode.getUserObject() instanceof IfcWall) {
            System.out.println("IfcWall: " + ((IfcWall) treeNode.getUserObject()).getName() + " (" + ((IfcWall) treeNode.getUserObject()).getGlobalId() + ")");
            IfcWall wall = (IfcWall)    treeNode.getUserObject();
            IfcObjectPlacement placement = wall.getObjectPlacement();
            System.out.println(placement);
            System.out.println(placement.getClass());
            if (placement instanceof IfcLocalPlacement) {
                IfcLocalPlacement localPlacement = (IfcLocalPlacement) placement;
                if (localPlacement.getRelativePlacement() instanceof IfcAxis2Placement3D) {
                    System.out.println("computeLocation: " + computeLocation(localPlacement));
                    System.out.println("computeMatrix: " + computeMatrix(localPlacement));
                    IfcAxis2Placement3D placement3D = (IfcAxis2Placement3D)localPlacement.getRelativePlacement();
                    System.out.println(placement3D.getAxis().getDirectionRatios());
                    System.out.println(placement3D.getRefDirection().getDirectionRatios());
                    System.out.println(placement3D.getLocation().getCoordinates());
                    System.out.println(placement3D);
                }
            }
            System.out.println(placement.getPlacesObject_Inverse());
            SET<IfcLocalPlacement> set = placement.getReferencedByPlacements_Inverse();
            if (set != null)
                for (IfcLocalPlacement ilp : set)
                    System.out.println(ilp.getRelativePlacement());
        } else if (treeNode.getUserObject() instanceof IfcRoot) {
            System.out.println("IfcRoot: " + ((IfcRoot) treeNode.getUserObject()).getName() + " (" + ((IfcRoot) treeNode.getUserObject()).getGlobalId() + ") [" + treeNode.getUserObject().getClass().getName() + "]");
        } else {
            System.out.println(treeNode.getUserObject() + " [" + treeNode.getUserObject().getClass().getName() + "]");
        }

        for (int i = 0; i < treeNode.getChildCount(); i++) {
            DefaultMutableTreeNode currentTreeNode = (DefaultMutableTreeNode)treeNode.getChildAt(i);
            print(currentTreeNode, tab + 1);
        }

    }

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
}
