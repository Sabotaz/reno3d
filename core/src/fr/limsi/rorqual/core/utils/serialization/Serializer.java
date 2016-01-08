package fr.limsi.rorqual.core.utils.serialization;

import com.thoughtworks.xstream.XStream;

import fr.limsi.rorqual.core.model.Batiment;

/**
 * Created by christophe on 08/01/16.
 */
public class Serializer {

    public static void saveAll() {
        XStream stream = new XStream();
        stream.processAnnotations(Batiment.class);
    }

}
