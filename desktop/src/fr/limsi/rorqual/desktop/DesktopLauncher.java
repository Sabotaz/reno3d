package fr.limsi.rorqual.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.io.File;

import fr.limsi.rorqual.core.model.IfcHolder;
import fr.limsi.rorqual.core.model.IfcHelper;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;

import ifc2x3javatoolbox.ifc2x3tc1.IfcBuildingStorey;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifc2x3tc1.LIST;
import ifc2x3javatoolbox.ifcmodel.IfcModel;

public class DesktopLauncher {
	public static void main (String[] arg) {
        IfcModel ifcModel = new IfcModel();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        MainApplicationAdapter application = new MainApplicationAdapter();

        IfcHelper.initialiseIfcModel(ifcModel);
        IfcHelper.addBuildingStorey(ifcModel,"2nd floor", 2.8);
        IfcHelper.addBuildingStorey(ifcModel,"3rd floor",5.6);
        IfcHelper.addBuildingStorey(ifcModel,"roof",8.4);
        IfcHelper.addWall(ifcModel,"1st floor","wall 1.1",8.0,2.8,0.4,0.0,0.0,1.0,0.0);
        IfcHelper.addWall(ifcModel,"1st floor","wall 1.2",8.0,2.8,0.4,0.0,8.0,1.0,0.0);
        IfcHelper.addWall(ifcModel,"1st floor","wall 1.3",8.0,2.8,0.4,0.0,0.0,0.0,1.0);
        IfcHelper.addWall(ifcModel,"1st floor","wall 1.4",8.0,2.8,0.4,8.0,0.0,0.0,1.0);
        IfcHelper.addWall(ifcModel,"2nd floor","wall 2.1",8.0,2.8,0.4,0.0,0.0,1.0,0.0);
        IfcHelper.addWall(ifcModel,"2nd floor","wall 2.2",8.0,2.8,0.4,0.0,8.0,1.0,0.0);
        IfcHelper.addWall(ifcModel,"2nd floor","wall 2.3",8.0,2.8,0.4,0.0,0.0,0.0,1.0);
        IfcHelper.addWall(ifcModel,"2nd floor","wall 2.4",8.0,2.8,0.4,8.0,0.0,0.0,1.0);
        IfcHelper.addWall(ifcModel,"3rd floor","wall 3.1",8.0,2.8,0.4,0.0,0.0,1.0,0.0);
        IfcHelper.addWall(ifcModel,"3rd floor","wall 3.2",8.0,2.8,0.4,0.0,8.0,1.0,0.0);
        IfcHelper.addWall(ifcModel,"3rd floor","wall 3.3",8.0,2.8,0.4,0.0,0.0,0.0,1.0);
        IfcHelper.addWall(ifcModel,"3rd floor","wall 3.4",8.0,2.8,0.4,8.0,0.0,0.0,1.0);

        LIST<IfcCartesianPoint> listCartesianPoint = new LIST<>();
        listCartesianPoint.add(IfcHelper.createCartesianPoint2D(0.0,0.0));
        listCartesianPoint.add(IfcHelper.createCartesianPoint2D(8.0,0.0));
        listCartesianPoint.add(IfcHelper.createCartesianPoint2D(8.0,8.0));
        listCartesianPoint.add(IfcHelper.createCartesianPoint2D(0.0,8.0));

        IfcHelper.addSlabs(ifcModel,"1st floor",listCartesianPoint);
        IfcHelper.addSlabs(ifcModel,"2nd floor",listCartesianPoint);
        IfcHelper.addSlabs(ifcModel,"3rd floor",listCartesianPoint);

        IfcHelper.addDoor(ifcModel,"Porte entree", IfcHelper.getWall(ifcModel,"wall 1.4"),1.0,2.0,0.4,3.5,0.0);
        IfcHelper.addWindow(ifcModel,"fenetre a", IfcHelper.getWall(ifcModel,"wall 2.4"),1.0,1.2,0.4,1.0,1.0);
        IfcHelper.addWindow(ifcModel,"fenetre b", IfcHelper.getWall(ifcModel,"wall 2.4"),1.0,1.2,0.4,6.0,1.0);
        IfcHelper.addWindow(ifcModel,"fenetre c", IfcHelper.getWall(ifcModel,"wall 3.4"),1.0,1.2,0.4,1.0,1.0);
        IfcHelper.addWindow(ifcModel,"fenetre d", IfcHelper.getWall(ifcModel,"wall 3.4"),1.0,1.2,0.4,6.0,1.0);
        IfcHelper.addWindow(ifcModel,"fenetre e", IfcHelper.getWall(ifcModel,"wall 1.3"),4.0,1.6,0.4,2.0,0.5);
        IfcHelper.addWindow(ifcModel,"fenetre f", IfcHelper.getWall(ifcModel,"wall 2.3"),4.0,1.6,0.4,2.0,0.5);
        IfcHelper.addWindow(ifcModel,"fenetre g", IfcHelper.getWall(ifcModel,"wall 3.3"),4.0,1.6,0.4,2.0,0.5);


        IfcHelper.saveIfcModel(ifcModel);


//        try {
//            IfcHolder.getInstance().openModel(new File("data/ifc/3etage12murs3slabs12openings.ifc"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

		new LwjglApplication(application, config);
	}
}
