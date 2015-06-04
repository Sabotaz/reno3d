package fr.limsi.rorqual.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.io.File;

import fr.limsi.rorqual.core.model.IfcHolder;
import fr.limsi.rorqual.core.model.IfcHelper;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;

import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifcmodel.IfcModel;

public class DesktopLauncher {
	public static void main (String[] arg) {
        IfcModel ifcModel = new IfcModel();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        MainApplicationAdapter application = new MainApplicationAdapter();

        IfcHelper.initialiseIfcModel(ifcModel);

        IfcHelper.createApartmentTest(ifcModel);
//        IfcCartesianPoint pointA = IfcHelper.createCartesianPoint3D(0, 0, 0);
//        IfcCartesianPoint pointB = IfcHelper.createCartesianPoint3D(4, 0, 0);
//        IfcHelper.addWall(ifcModel, "1st floor", "wall", pointA, pointB, 0.18);
//        IfcWallStandardCase wall = IfcHelper.getWall(ifcModel, "wall");
//        IfcHelper.addPropertyTypeIsolation(ifcModel,wall,"ITI");
//        IfcHelper.addPropertyTypeIsolation(ifcModel,wall,"ITE");
//            IfcHelper.addPropertyTypeWall(ifcModel,wall,true);
        IfcHelper.saveIfcModel(ifcModel);

        try {
            IfcHolder.getInstance().openModel(new File("data/ifc/coucou.ifc"));
        } catch (Exception e) {
            e.printStackTrace();
        }
		new LwjglApplication(application, config);
	}
}
