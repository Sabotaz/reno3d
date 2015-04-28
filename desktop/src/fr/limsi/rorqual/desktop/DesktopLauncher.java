package fr.limsi.rorqual.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.io.File;
import java.util.List;
import java.util.Set;

import fr.limsi.rorqual.core.model.IfcHolder;
import fr.limsi.rorqual.core.model.IfcHelper;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;

import ifc2x3javatoolbox.ifc2x3tc1.IfcBuildingStorey;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDoor;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLabel;
import ifc2x3javatoolbox.ifc2x3tc1.IfcMaterial;
import ifc2x3javatoolbox.ifc2x3tc1.IfcOpeningElement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSlab;
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
        IfcHelper.addBuildingStorey(ifcModel,"2nd floor", 2.8);
        IfcHelper.addBuildingStorey(ifcModel,"3rd floor",5.6);
        IfcHelper.addWall(ifcModel,"1st floor","wall 1.1",8.0,2.8,0.4,0.0,0.0,1.0,0.0);
        IfcHelper.addWall(ifcModel,"1st floor","wall 1.2",8.0,2.8,0.4,0.0,8.0,1.0,0.0);
        IfcHelper.addWall(ifcModel,"1st floor","wall 1.3",7.6,2.8,0.4,0.2,0.2,0.0,1.0);
        IfcHelper.addWall(ifcModel,"1st floor","wall 1.4",7.6,2.8,0.4,7.8,0.2,0.0,1.0);
        IfcHelper.addWall(ifcModel,"2nd floor","wall 2.1",8.0,2.8,0.4,0.0,0.0,1.0,0.0);
        IfcHelper.addWall(ifcModel,"2nd floor","wall 2.2",8.0,2.8,0.4,0.0,8.0,1.0,0.0);
        IfcHelper.addWall(ifcModel,"2nd floor","wall 2.3",7.6,2.8,0.4,0.2,0.2,0.0,1.0);
        IfcHelper.addWall(ifcModel,"2nd floor","wall 2.4",7.6,2.8,0.4,7.8,0.2,0.0,1.0);
        IfcHelper.addWall(ifcModel,"3rd floor","wall 3.1",8.0,2.8,0.4,0.0,0.0,1.0,0.0);
        IfcHelper.addWall(ifcModel,"3rd floor","wall 3.2",8.0,2.8,0.4,0.0,8.0,1.0,0.0);
        IfcHelper.addWall(ifcModel,"3rd floor","wall 3.3",7.6,2.8,0.4,0.2,0.2,0.0,1.0);
        IfcHelper.addWall(ifcModel,"3rd floor","wall 3.4",7.6,2.8,0.4,7.8,0.2,0.0,1.0);

        LIST<IfcCartesianPoint> listCartesianPoint = new LIST<>();
        listCartesianPoint.add(IfcHelper.createCartesianPoint2D(0.4,0.2));
        listCartesianPoint.add(IfcHelper.createCartesianPoint2D(7.6,0.2));
        listCartesianPoint.add(IfcHelper.createCartesianPoint2D(7.6,7.8));
        listCartesianPoint.add(IfcHelper.createCartesianPoint2D(0.4,7.8));

        LIST<IfcCartesianPoint> listCartesianPoint2 = new LIST<>();
        listCartesianPoint2.add(IfcHelper.createCartesianPoint2D(0.0,-0.2));
        listCartesianPoint2.add(IfcHelper.createCartesianPoint2D(8.0,-0.2));
        listCartesianPoint2.add(IfcHelper.createCartesianPoint2D(8.0,8.2));
        listCartesianPoint2.add(IfcHelper.createCartesianPoint2D(0.0,8.2));

        IfcHelper.addFloor(ifcModel,"1st floor",listCartesianPoint);
        IfcHelper.addFloor(ifcModel,"2nd floor",listCartesianPoint);
        IfcHelper.addFloor(ifcModel,"3rd floor",listCartesianPoint);
        IfcHelper.addRoof(ifcModel,"3rd floor",listCartesianPoint2, 2.8);

        IfcHelper.addDoor(ifcModel,"Porte entree", IfcHelper.getWall(ifcModel,"wall 1.4"),1.0,2.0,0.4,3.5);
        IfcHelper.addWindow(ifcModel,"window", IfcHelper.getWall(ifcModel, "wall 1.4"), 1.0,1.5,0.4,6.0,1.0);
        IfcHelper.addWindow(ifcModel,"fenetre a", IfcHelper.getWall(ifcModel,"wall 2.4"),1.0,1.2,0.4,1.0,1.0);
        IfcHelper.addWindow(ifcModel,"fenetre b", IfcHelper.getWall(ifcModel,"wall 2.4"),1.0,1.2,0.4,6.0,1.0);
        IfcHelper.addWindow(ifcModel,"fenetre c", IfcHelper.getWall(ifcModel,"wall 3.4"),1.0,1.2,0.4,1.0,1.0);
        IfcHelper.addWindow(ifcModel,"fenetre d", IfcHelper.getWall(ifcModel,"wall 3.4"),1.0,1.2,0.4,6.0,1.0);
        IfcHelper.addWindow(ifcModel,"fenetre e", IfcHelper.getWall(ifcModel,"wall 1.3"),4.0,1.6,0.4,2.0,0.5);
        IfcHelper.addWindow(ifcModel,"fenetre f", IfcHelper.getWall(ifcModel,"wall 2.3"),4.0,1.6,0.4,2.0,0.5);
        IfcHelper.addWindow(ifcModel,"fenetre g", IfcHelper.getWall(ifcModel,"wall 3.3"),4.0,1.6,0.4,2.0,0.5);

        List<IfcSlab> slabs = IfcHelper.getSlabs(ifcModel,"3rd floor");
        IfcHelper.addWindow(ifcModel,"fenetre slab1", slabs.get(0),1.0,1.0,0.2,6.0,6.0);
        IfcHelper.addWindow(ifcModel,"fenetre slab2", slabs.get(0),1.0,1.0,0.2,1.0,1.0);

//        IfcHelper.addWall(ifcModel,"1st floor","wall 1.1",8.0,2.8,0.4,0.0,0.0,1.0,0.0);
//        IfcHelper.addDoor(ifcModel,"Porte entree", IfcHelper.getWall(ifcModel,"wall 1.1"),1.0,2.0,0.4,3.5);
//        IfcHelper.addWindow(ifcModel,"window", IfcHelper.getWall(ifcModel, "wall 1.1"), 1.0,1.5,0.4,6.0,1.0);
//        IfcWallStandardCase wall = IfcHelper.getWall(ifcModel,"wall 1.1");
//        IfcDoor door = IfcHelper.getDoor(ifcModel,"Porte entree");
//        IfcWindow window = IfcHelper.getWindow(ifcModel,"window");
//        IfcHelper.setDoorPosition(ifcModel,door,4.0);
//        IfcHelper.setDoorWidth(ifcModel,door,3.2);
//        IfcHelper.setDoorHeight(ifcModel,door,1.1);
//        IfcHelper.setWindowPosition(ifcModel,window,1.01,0.01);
//        IfcHelper.setWindowWidth(ifcModel,window,0.85);
//        IfcHelper.setWindowHeight(ifcModel,window,0.24);
//        System.out.println("Épaisseur du mur = " + IfcHelper.getWallThickness(wall) + " mètre");




        ///////////////////////////////////DEBUT MESURE DE TEMPS//////////////////////////////////////////////////////////////////////////////
//        long startTime = System.nanoTime();
//        // Méthode à timer
//        IfcHelper.setWallLength(ifcModel,wall,20.0);
//        long test = System.nanoTime() - startTime;
//        System.out.println("Temps total : " + test + " nanosecondes");
        ///////////////////////////////////FIN DE MESURE DE TEMPS//////////////////////////////////////////////////////////////////////////////

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
