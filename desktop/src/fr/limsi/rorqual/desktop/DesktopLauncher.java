package fr.limsi.rorqual.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.io.File;
import java.util.Collection;

import fr.limsi.rorqual.core.model.IfcHolder;
import fr.limsi.rorqual.core.model.IfcHelper;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;

import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcExtrudedAreaSolid;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLengthMeasure;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLocalPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelContainedInSpatialStructure;
import ifc2x3javatoolbox.ifc2x3tc1.IfcTrimmedCurve;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifc2x3tc1.LIST;
import ifc2x3javatoolbox.ifc2x3tc1.SET;
import ifc2x3javatoolbox.ifcmodel.IfcModel;

public class DesktopLauncher {
	public static void main (String[] arg) {
        IfcModel ifcModel = new IfcModel();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        MainApplicationAdapter application = new MainApplicationAdapter();

        IfcHelper.initialiseIfcModel(ifcModel);
        IfcHelper.addBuildingStorey(ifcModel,"2nd floor",2.8f);
        IfcHelper.addBuildingStorey(ifcModel,"3rd floor",5.6f);
        IfcHelper.addBuildingStorey(ifcModel,"roof",8.4f);
        IfcHelper.addWall(ifcModel,"1st floor",8.0f,0.4f,2.8f,0.0f,0.0f,1.0f,0.0f);
        IfcHelper.addWall(ifcModel,"1st floor",8.0f,0.4f,2.8f,0.0f,8.0f,1.0f,0.0f);
        IfcHelper.addWall(ifcModel,"1st floor",8.0f,0.4f,2.8f,0.0f,0.0f,0.0f,1.0f);
        IfcHelper.addWall(ifcModel,"1st floor",8.0f,0.4f,2.8f,8.0f,0.0f,0.0f,1.0f);
        IfcHelper.addWall(ifcModel,"2nd floor",8.0f,0.4f,2.8f,0.0f,0.0f,1.0f,0.0f);
        IfcHelper.addWall(ifcModel,"2nd floor",8.0f,0.4f,2.8f,0.0f,8.0f,1.0f,0.0f);
        IfcHelper.addWall(ifcModel,"2nd floor",8.0f,0.4f,2.8f,0.0f,0.0f,0.0f,1.0f);
        IfcHelper.addWall(ifcModel,"2nd floor",8.0f,0.4f,2.8f,8.0f,0.0f,0.0f,1.0f);
        IfcHelper.addWall(ifcModel,"3rd floor",8.0f,0.4f,2.8f,0.0f,0.0f,1.0f,0.0f);
        IfcHelper.addWall(ifcModel,"3rd floor",8.0f,0.4f,2.8f,0.0f,8.0f,1.0f,0.0f);
        IfcHelper.addWall(ifcModel,"3rd floor",8.0f,0.4f,2.8f,0.0f,0.0f,0.0f,1.0f);
        IfcHelper.addWall(ifcModel,"3rd floor",8.0f,0.4f,2.8f,8.0f,0.0f,0.0f,1.0f);

        LIST<IfcCartesianPoint> listCartesianPoint = new LIST<>();
        listCartesianPoint.add(IfcHelper.createCartesianPoint2D(0.0f,0.0f));
        listCartesianPoint.add(IfcHelper.createCartesianPoint2D(8.0f,0.0f));
        listCartesianPoint.add(IfcHelper.createCartesianPoint2D(8.0f,8.0f));
        listCartesianPoint.add(IfcHelper.createCartesianPoint2D(0.0f,8.0f));

        IfcHelper.addSlabs(ifcModel,"1st floor",listCartesianPoint);
        IfcHelper.addSlabs(ifcModel,"2nd floor",listCartesianPoint);
        IfcHelper.addSlabs(ifcModel,"3rd floor",listCartesianPoint);

        IfcHelper.saveIfcModel(ifcModel);


        try {
            IfcHolder.getInstance().openModel(new File("data/ifc/two_curved_walls.ifc"));
        } catch (Exception e) {
            e.printStackTrace();
        }

		new LwjglApplication(application, config);
	}
}
