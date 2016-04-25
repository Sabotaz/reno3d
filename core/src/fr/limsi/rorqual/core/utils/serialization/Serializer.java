package fr.limsi.rorqual.core.utils.serialization;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.Model;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;

import java.io.IOException;
import java.net.URISyntaxException;

import fr.limsi.rorqual.core.model.Batiment;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.utils.scene3d.ActableModel;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;

/**
 * Created by christophe on 08/01/16.
 */
public class Serializer {

    public static void saveAll(String filename) {
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

        FileHandle file;
        try {
            String path = MainApplicationAdapter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            if (path.endsWith("/jar/desktop-1.0.jar")) {
                file = Gdx.files.absolute(path.replace("/jar/desktop-1.0.jar", "/models/" + filename));
            }
            else {
                file = Gdx.files.external(filename);
            }
        } catch (URISyntaxException e) {
            file = Gdx.files.external(filename);
        }

        file.writeString(stream.toXML(new SerialHolder()), false);
    }

}
