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
    IfcHelper ifcHelper = new IfcHelper();
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        MainApplicationAdapter application = new MainApplicationAdapter();

        initialize(application, config);

	}
}
