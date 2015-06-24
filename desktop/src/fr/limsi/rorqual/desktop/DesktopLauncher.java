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
        config.title = "3DReno";
        config.height = 1080;
        config.width = 1920;

        IfcHelper.initialiseIfcModel(ifcModel);
        IfcHelper.createApartmentTest(ifcModel);
        IfcHelper.saveIfcModel(ifcModel);

        try {
            IfcHolder.getInstance().openModel(new File("data/ifc/coucou.ifc"));
        } catch (Exception e) {
            e.printStackTrace();
        }

		new LwjglApplication(new MainApplicationAdapter(), config);
	}
}
