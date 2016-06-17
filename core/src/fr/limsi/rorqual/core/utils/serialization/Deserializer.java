package fr.limsi.rorqual.core.utils.serialization;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.thoughtworks.xstream.XStream;

import java.io.IOException;
import java.net.URISyntaxException;

import fr.limsi.rorqual.core.model.Batiment;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;

/**
 * Created by christophe on 08/01/16.
 */
public class Deserializer {

    public static void loadAll(String filename) {
        XStream stream = new XStream();
        stream.processAnnotations(SerialHolder.class);
        stream.autodetectAnnotations(true);
        stream.setMode(XStream.ID_REFERENCES);
        FileHandle file;

        try {
            String path = MainApplicationAdapter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            if (path.endsWith("/jar/desktop-1.0.jar")) {
                file = Gdx.files.absolute(path.replace("/jar/desktop-1.0.jar", "/models/" + filename));
                if (file.exists()) {
                    int i = 0;
                    do {
                        FileHandle next = Gdx.files.absolute(path.replace("/jar/desktop-1.0.jar", "/models/" + filename + "." + i));
                        if (!next.exists()) {
                            next.write(file.read(), false);
                            break;
                        }
                        i++;
                    } while (true);
                } else {
                    file = Gdx.files.external(filename);
                }
            }
            else {
                file = Gdx.files.external(filename);
            }
        } catch (URISyntaxException e) {
            file = Gdx.files.external(filename);
        }

        SerialHolder serialHolder = (SerialHolder) stream.fromXML(file.read());
        serialHolder.recreateModel();

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MainApplicationAdapter.kartoffelator.setInitScore();
        MainApplicationAdapter.startLogs();
    }

}