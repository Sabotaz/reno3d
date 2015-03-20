package fr.limsi.rorqual.android;

import android.os.Bundle;
import android.os.Environment;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import ifc2x3javatoolbox.ifc2x3tc1.IfcWall;
import ifc2x3javatoolbox.ifcmodel.IfcModel;

import fr.limsi.rorqual.core.MainApplicationAdapter;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        try {

            InputStream inputStream = getAssets().open("TestData/data/example.ifc");
            String root = Environment.getExternalStorageDirectory().toString();
            File f = new File(root + "/example.ifc");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            //create a new instance of IfcModel
            IfcModel ifcModel = new IfcModel();
            //load an IFC STEP file from the file system
            File stepFile = f;
            ifcModel.readStepFile(stepFile);

            Collection<IfcWall> walls = ifcModel.getCollection(IfcWall.class);
            for (IfcWall wall: walls) {
                System.out.println(wall.getGlobalId() + ": " + wall.getDescription());
            }
        } catch (Exception e) {
            System.out.println(e);
        }

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MainApplicationAdapter(), config);

	}
}
