package fr.limsi.rorqual.core.utils.serialization;

import com.thoughtworks.xstream.XStream;

import fr.limsi.rorqual.core.model.Batiment;
import fr.limsi.rorqual.core.model.ModelHolder;

/**
 * Created by christophe on 08/01/16.
 */
public class Serializer {

    public static void saveAll() {
        XStream stream = new XStream();
        stream.autodetectAnnotations(true);
        System.out.println(stream.toXML(ModelHolder.getInstance().getBatiment()));
    }

}
