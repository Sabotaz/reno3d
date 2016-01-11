package fr.limsi.rorqual.core.utils.serialization;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.Model;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;

import java.io.IOException;

import fr.limsi.rorqual.core.model.Batiment;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.utils.scene3d.ActableModel;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;

/**
 * Created by christophe on 08/01/16.
 */
public class Serializer {

    public static void saveAll() {
        XStream stream = new XStream() {
            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    @Override
                    public boolean shouldSerializeMember(Class definedIn,
                                                         String fieldName) {

                        if (definedIn == ModelContainer.class)
                            return false;
                        if (definedIn == ActableModel.class)
                            return false;
                        if (definedIn == Model.class)
                            return false;

                        return super
                                .shouldSerializeMember(definedIn, fieldName);
                    }
                };
            }
        };

        stream.autodetectAnnotations(true);
        stream.setMode(XStream.ID_REFERENCES);

        FileHandle handle = Gdx.files.external("save.3dr");
        handle.writeString(stream.toXML(new SerialHolder()), false);
    }

}
