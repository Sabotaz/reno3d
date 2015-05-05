package fr.limsi.rorqual.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.io.File;

import fr.limsi.rorqual.core.model.IfcHolder;
import fr.limsi.rorqual.core.model.IfcHelper;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;

import ifc2x3javatoolbox.ifc2x3tc1.IfcBuildingStorey;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDoor;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLengthMeasure;
import ifc2x3javatoolbox.ifc2x3tc1.IfcOpeningElement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWindow;
import ifc2x3javatoolbox.ifc2x3tc1.LIST;
import ifc2x3javatoolbox.ifc2x3tc1.SET;
import ifc2x3javatoolbox.ifcmodel.IfcModel;

public class DesktopLauncher {
	public static void main (String[] arg) {
        IfcModel ifcModel = new IfcModel();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        MainApplicationAdapter application = new MainApplicationAdapter();

        IfcHelper.initialiseIfcModel(ifcModel);
        IfcHelper.createApartmentTest(ifcModel);
//        IfcCartesianPoint pointA = IfcHelper.createCartesianPoint2D(0.0,0.0);
//        IfcCartesianPoint pointB = IfcHelper.createCartesianPoint2D(500,200);
//        IfcHelper.addWall(ifcModel,"1st floor","wall",pointA,pointB,18);
//        IfcWallStandardCase wall = IfcHelper.getWall(ifcModel,"wall");
//        IfcHelper.addDoubleWindow(ifcModel,"window",wall,100,150,200,50);

        IfcHelper.saveIfcModel(ifcModel);

//        try {
//            IfcHolder.getInstance().openModel(new File("data/ifc/3etage12murs3slabs12openings.ifc"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            IfcHolder.getInstance().openModel(new File("C:\\Users\\ricordeau\\Desktop\\Coucou.ifc"));
        } catch (Exception e) {
            e.printStackTrace();
        }
		new LwjglApplication(application, config);
	}
}
