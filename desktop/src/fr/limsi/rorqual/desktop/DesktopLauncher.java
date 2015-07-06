package fr.limsi.rorqual.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.io.File;
import java.util.ArrayList;

import fr.limsi.rorqual.core.model.IfcHolder;
import fr.limsi.rorqual.core.model.IfcHelper;
import fr.limsi.rorqual.core.model.primitives.MaterialTypeEnum;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;

import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifcmodel.IfcModel;

public class DesktopLauncher {
	public static void main (String[] arg) {
        IfcModel ifcModel = new IfcModel();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "3DReno";
        config.height = 720;
        config.width = 1280;

        IfcHelper.initialiseIfcModel(ifcModel);
//        IfcHelper.createApartmentTest(ifcModel);
        IfcCartesianPoint pointA1 = IfcHelper.createCartesianPoint2D(0,0);
        IfcCartesianPoint pointA2 = IfcHelper.createCartesianPoint2D(2, 0);
        IfcHelper.addWall(ifcModel, "1st floor", "wall", pointA1, pointA2, 0.18);
        IfcWallStandardCase wall = IfcHelper.getWall(ifcModel, "wall");
        IfcHelper.addDoor(ifcModel, "door", wall, 2, 2.7, 0);
        IfcHelper.addWindow(ifcModel, "window", wall, 1.0, 1.0, 5.5, 1.3);
        ArrayList<MaterialTypeEnum> materialTypeEnumArrayList = new ArrayList<>();
        materialTypeEnumArrayList.add(MaterialTypeEnum.BRIQUE);
        materialTypeEnumArrayList.add(MaterialTypeEnum.PIERRE);
        IfcHelper.addMaterialLayer(ifcModel,wall,materialTypeEnumArrayList);
        IfcHelper.saveIfcModel(ifcModel);

        try {
            IfcHolder.getInstance().openModel(new File("data/ifc/coucou.ifc"));
        } catch (Exception e) {
            e.printStackTrace();
        }
		new LwjglApplication(new MainApplicationAdapter(), config);
	}
}
