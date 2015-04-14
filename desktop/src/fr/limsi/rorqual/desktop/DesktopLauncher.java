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

/*        IfcHelper.initialiseIfcModel(ifcModel);
        IfcHelper.addBuildingStorey(ifcModel,"2nd floor", 2.8d);
        IfcHelper.addBuildingStorey(ifcModel,"3rd floor",5.6d);
        IfcHelper.addBuildingStorey(ifcModel,"roof",8.4d);
        IfcHelper.addWall(ifcModel,"1st floor","wall 1.1",8.0d,0.4d,2.8d,0.0d,0.0d,1.0d,0.0d);
        IfcHelper.addWall(ifcModel,"1st floor","wall 1.2",8.0d,0.4d,2.8d,0.0d,8.0d,1.0d,0.0d);
        IfcHelper.addWall(ifcModel,"1st floor","wall 1.3",8.0d,0.4d,2.8d,0.0d,0.0d,0.0d,1.0d);
        IfcHelper.addWall(ifcModel,"1st floor","wall 1.4",8.0d,0.4d,2.8d,8.0d,0.0d,0.0d,1.0d);
        IfcHelper.addWall(ifcModel,"2nd floor","wall 2.1",8.0d,0.4d,2.8d,0.0d,0.0d,1.0d,0.0d);
        IfcHelper.addWall(ifcModel,"2nd floor","wall 2.2",8.0d,0.4d,2.8d,0.0d,8.0d,1.0d,0.0d);
        IfcHelper.addWall(ifcModel,"2nd floor","wall 2.3",8.0d,0.4d,2.8d,0.0d,0.0d,0.0d,1.0d);
        IfcHelper.addWall(ifcModel,"2nd floor","wall 2.4",8.0d,0.4d,2.8d,8.0d,0.0d,0.0d,1.0d);
        IfcHelper.addWall(ifcModel,"3rd floor","wall 3.1",8.0d,0.4d,2.8d,0.0d,0.0d,1.0d,0.0d);
        IfcHelper.addWall(ifcModel,"3rd floor","wall 3.2",8.0d,0.4d,2.8d,0.0d,8.0d,1.0d,0.0d);
        IfcHelper.addWall(ifcModel,"3rd floor","wall 3.3",8.0d,0.4d,2.8d,0.0d,0.0d,0.0d,1.0d);
        IfcHelper.addWall(ifcModel,"3rd floor","wall 3.4",8.0d,0.4d,2.8d,8.0d,0.0d,0.0d,1.0d);

        LIST<IfcCartesianPoint> listCartesianPoint = new LIST<>();
        listCartesianPoint.add(IfcHelper.createCartesianPoint2D(0.0d,0.0d));
        listCartesianPoint.add(IfcHelper.createCartesianPoint2D(8.0d,0.0d));
        listCartesianPoint.add(IfcHelper.createCartesianPoint2D(8.0d,8.0d));
        listCartesianPoint.add(IfcHelper.createCartesianPoint2D(0.0d,8.0d));

        IfcHelper.addSlabs(ifcModel,"1st floor",listCartesianPoint);
        IfcHelper.addSlabs(ifcModel,"2nd floor",listCartesianPoint);
        IfcHelper.addSlabs(ifcModel,"3rd floor",listCartesianPoint);
        IfcHelper.addOpeningToWall(ifcModel,"wall 1.1");
        IfcHelper.addOpeningToWall(ifcModel,"wall 1.2");
        IfcHelper.addOpeningToWall(ifcModel,"wall 1.3");
        IfcHelper.addOpeningToWall(ifcModel,"wall 1.4");
        IfcHelper.addOpeningToWall(ifcModel,"wall 2.1");
        IfcHelper.addOpeningToWall(ifcModel,"wall 2.2");
        IfcHelper.addOpeningToWall(ifcModel,"wall 2.3");
        IfcHelper.addOpeningToWall(ifcModel,"wall 2.4");
        IfcHelper.addOpeningToWall(ifcModel,"wall 3.1");
        IfcHelper.addOpeningToWall(ifcModel,"wall 3.2");
        IfcHelper.addOpeningToWall(ifcModel,"wall 3.3");
        IfcHelper.addOpeningToWall(ifcModel,"wall 3.4");*/


        IfcHelper.saveIfcModel(ifcModel);


        try {
            IfcHolder.getInstance().openModel(new File("data/ifc/two_curved_walls.ifc"));
        } catch (Exception e) {
            e.printStackTrace();
        }

		new LwjglApplication(application, config);
	}
}
