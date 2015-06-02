package fr.limsi.rorqual.android;

import android.os.Bundle;
import android.os.Environment;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import fr.limsi.rorqual.core.model.IfcHelper;
import fr.limsi.rorqual.core.model.IfcHolder;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;
import ifc2x3javatoolbox.ifcmodel.IfcModel;

public class AndroidLauncher extends AndroidApplication {
    IfcModel ifcModel = new IfcModel();
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        try {

            IfcHelper.initialiseIfcModel(ifcModel);
            IfcHelper.createApartmentTest(ifcModel);
            IfcHelper.saveIfcModel(ifcModel);
            InputStream inputStream = getAssets().open("data/ifc/example.ifc");
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

            IfcHolder.getInstance().openModel(f);

        } catch (Exception e) {
            System.out.println(e);
        }

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        MainApplicationAdapter application = new MainApplicationAdapter();

        initialize(application, config);

	}
}
