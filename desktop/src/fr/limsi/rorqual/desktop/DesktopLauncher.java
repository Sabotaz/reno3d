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
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "3DReno";
        config.height = 720;
        config.width = 1280;

        IfcHelper ifcHelper = new IfcHelper();
//        ifcHelper.createApartmentTest(ifcModel);
        ifcHelper.createSecondAppartementTest();
//        IfcCartesianPoint pointA1 = ifcHelper.createCartesianPoint2D(0, 0);
//        IfcCartesianPoint pointA2 = ifcHelper.createCartesianPoint2D(8, 0);
//        ifcHelper.addWall("1st floor", "wall", pointA1, pointA2, 0.18);
//        IfcWallStandardCase wall = ifcHelper.getWall("wall");
//        ifcHelper.addDoor("door", wall, 1, 2.2, 1);
//        ifcHelper.addWindow("window", wall, 1.0, 1.0, 5.5, 1.3);
//        IfcWindow window = ifcHelper.getWindow("window");
//        ArrayList<MaterialTypeEnum> materialTypeEnumArrayList = new ArrayList<>();
//        materialTypeEnumArrayList.add(MaterialTypeEnum.BRIQUE);
//        materialTypeEnumArrayList.add(MaterialTypeEnum.PIERRE);
//        ifcHelper.addMaterialLayer(wall, materialTypeEnumArrayList);
//        ifcHelper.addPropertyTypeWindow(window, TypeFenetreEnum.UNKNOWN);
//        ifcHelper.addPropertyTypeMenuiserieWindow(window, TypeMenuiserieFenetreEnum.UNKNOWN);
//        ifcHelper.addPropertyTypeVitrageWindow(window, TypeVitrageEnum.UNKNOWN);
//        ifcHelper.saveIfcModel();
//        ifcHelper.addPropertyTypeWindow(window, TypeFenetreEnum.BATTANTE);
//        ifcHelper.addPropertyTypeMenuiserieWindow(window, TypeMenuiserieFenetreEnum.METALLIQUE);
//        ifcHelper.addPropertyTypeVitrageWindow(window, TypeVitrageEnum.TRIPLE_VITRAGE);
//        System.out.println(ifcHelper.getPropertiesWindow(window, WindowPropertiesEnum.TYPE_FENETRE));
//        System.out.println(ifcHelper.getPropertiesWindow(window, WindowPropertiesEnum.TYPE_MENUISERIE));
//        System.out.println(ifcHelper.getPropertiesWindow(window, WindowPropertiesEnum.TYPE_VITRAGE));
        ifcHelper.saveIfcModel();

        try {
            IfcHolder.getInstance().openModel(new File("data/ifc/coucou.ifc"));
        } catch (Exception e) {
            e.printStackTrace();
        }
		new LwjglApplication(new MainApplicationAdapter(), config);
	}
}
