package fr.limsi.rorqual.core.utils.serialization;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.thoughtworks.xstream.XStream;

import java.io.IOException;

import fr.limsi.rorqual.core.model.Batiment;

/**
 * Created by christophe on 08/01/16.
 */
public class Deserializer {

    public static void loadAll(String filename) {
        XStream stream = new XStream();
        stream.processAnnotations(SerialHolder.class);
        stream.autodetectAnnotations(true);
        stream.setMode(XStream.ID_REFERENCES);

        FileHandle handle = Gdx.files.external(filename);
        SerialHolder serialHolder = (SerialHolder) stream.fromXML(handle.read());
        serialHolder.recreateModel();

    }

}
