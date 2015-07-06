package fr.limsi.rorqual.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.io.File;
import java.util.ArrayList;

import fr.limsi.rorqual.core.dpe.TypeFenetreEnum;
import fr.limsi.rorqual.core.dpe.TypeMenuiserieFenetreEnum;
import fr.limsi.rorqual.core.dpe.TypeVitrageEnum;
import fr.limsi.rorqual.core.dpe.WindowPropertiesEnum;
import fr.limsi.rorqual.core.model.IfcHolder;
import fr.limsi.rorqual.core.model.IfcHelper;
import fr.limsi.rorqual.core.model.primitives.MaterialTypeEnum;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;

import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWindow;
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
        IfcHelper.createSecondAppartementTest(ifcModel);
//        IfcCartesianPoint pointA1 = IfcHelper.createCartesianPoint2D(0, 0);
//        IfcCartesianPoint pointA2 = IfcHelper.createCartesianPoint2D(8, 0);
//        IfcHelper.addWall(ifcModel, "1st floor", "wall", pointA1, pointA2, 0.18);
//        IfcWallStandardCase wall = IfcHelper.getWall(ifcModel, "wall");
//        IfcHelper.addDoor(ifcModel, "door", wall, 1, 2.2, 1);
//        IfcHelper.addWindow(ifcModel, "window", wall, 1.0, 1.0, 5.5, 1.3);
//        IfcWindow window = IfcHelper.getWindow(ifcModel,"window");
//        ArrayList<MaterialTypeEnum> materialTypeEnumArrayList = new ArrayList<>();
//        materialTypeEnumArrayList.add(MaterialTypeEnum.BRIQUE);
//        materialTypeEnumArrayList.add(MaterialTypeEnum.PIERRE);
//        IfcHelper.addMaterialLayer(ifcModel, wall, materialTypeEnumArrayList);
//        IfcHelper.addPropertyTypeWindow(ifcModel, window, TypeFenetreEnum.UNKNOWN);
//        IfcHelper.addPropertyTypeMenuiserieWindow(ifcModel, window, TypeMenuiserieFenetreEnum.UNKNOWN);
//        IfcHelper.addPropertyTypeVitrageWindow(ifcModel, window, TypeVitrageEnum.UNKNOWN);
//        IfcHelper.saveIfcModel(ifcModel);
//        IfcHelper.addPropertyTypeWindow(ifcModel, window, TypeFenetreEnum.BATTANTE);
//        IfcHelper.addPropertyTypeMenuiserieWindow(ifcModel, window, TypeMenuiserieFenetreEnum.METALLIQUE);
//        IfcHelper.addPropertyTypeVitrageWindow(ifcModel, window, TypeVitrageEnum.TRIPLE_VITRAGE);
//        System.out.println(IfcHelper.getPropertiesWindow(window, WindowPropertiesEnum.TYPE_FENETRE));
//        System.out.println(IfcHelper.getPropertiesWindow(window, WindowPropertiesEnum.TYPE_MENUISERIE));
//        System.out.println(IfcHelper.getPropertiesWindow(window, WindowPropertiesEnum.TYPE_VITRAGE));
        IfcHelper.saveIfcModel(ifcModel);

        try {
            IfcHolder.getInstance().openModel(new File("data/ifc/coucou.ifc"));
        } catch (Exception e) {
            e.printStackTrace();
        }
		new LwjglApplication(new MainApplicationAdapter(), config);
	}
}
