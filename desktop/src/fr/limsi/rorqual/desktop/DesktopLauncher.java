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
import ifc2x3javatoolbox.ifc2x3tc1.IfcDoor;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWindow;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "3DReno";
        config.height = 720;
        config.width = 1280;

        IfcHelper ifcHelper = new IfcHelper();
//        ifcHelper.createApartmentTest();
        ifcHelper.createSecondAppartementTest();
//        IfcCartesianPoint pointA1 = ifcHelper.createCartesianPoint2D(2,1);
//        IfcCartesianPoint pointA2 = ifcHelper.createCartesianPoint2D(3,4);
//        ifcHelper.addWall("1st floor", "wall", pointA1, pointA2, 0.18);
//        IfcWallStandardCase wall = ifcHelper.getWall("wall");
//        ArrayList<IfcCartesianPoint> list = ifcHelper.getWallPosition(wall);
//        ifcHelper.addDoor("door", wall, 1, 2.2, 1);
//        ifcHelper.addWindow("window", wall, 1.0, 1.0, 5.5, 1.3);
//        IfcWindow window = ifcHelper.getWindow("window");
//        IfcDoor door = ifcHelper.getDoor("door");
//        ArrayList<MaterialTypeEnum> materialTypeEnumArrayList = new ArrayList<>();
//        materialTypeEnumArrayList.add(MaterialTypeEnum.BRIQUE);
//        materialTypeEnumArrayList.add(MaterialTypeEnum.PIERRE);
//        IfcWallStandardCase wall2 = ifcHelper.getWallRelToDoor(door);
//        IfcWallStandardCase wall3 = ifcHelper.getWallRelToWindow(window);
//        ifcHelper.addMaterialLayer(wall, materialTypeEnumArrayList);
//        ifcHelper.addPropertyTypeWindow(window, TypeFenetreEnum.INCONNUE);
//        ifcHelper.addPropertyTypeMenuiserie(window, TypeMateriauMenuiserieEnum.INCONNUE);
//        ifcHelper.addPropertyTypeVitrageMenuiserie(window, TypeVitrageEnum.INCONNUE);
//        ifcHelper.saveIfcModel();
//        ifcHelper.addPropertyTypeWindow(window, TypeFenetreEnum.BATTANTE);
//        ifcHelper.addPropertyTypeMenuiserie(window, TypeMateriauMenuiserieEnum.METALLIQUE);
//        ifcHelper.addPropertyTypeVitrageMenuiserie(window, TypeVitrageEnum.TRIPLE_VITRAGE);
//        System.out.println(ifcHelper.getPropertiesWindow(window, WindowPropertiesEnum.TYPE_FENETRE));
//        System.out.println(ifcHelper.getPropertiesWindow(window, WindowPropertiesEnum.TYPE_MATERIAU_MENUISERIE));
//        System.out.println(ifcHelper.getPropertiesWindow(window, WindowPropertiesEnum.TYPE_VITRAGE_MENUISERIE));

        ifcHelper.saveIfcModel();

        try {
            IfcHolder.getInstance().openModel(new File("data/ifc/coucou.ifc"));
        } catch (Exception e) {
            e.printStackTrace();
        }
		new LwjglApplication(new MainApplicationAdapter(), config);
	}
}
