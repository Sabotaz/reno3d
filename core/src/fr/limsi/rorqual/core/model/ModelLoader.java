package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.UBJsonReader;

import java.lang.reflect.Field;

import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;

/**
 * Created by christophe on 07/08/15.
 */
public class ModelLoader {

    public static ModelContainer fromJson(String file) {
        FileHandle handle = Gdx.files.internal(file);
        JsonValue json = new JsonReader().parse(handle.readString());
        try {
            Object obj = Class.forName(json.getString("class")).newInstance();
            if (obj instanceof ModelContainer) {
                ModelContainer modelContainer = (ModelContainer) obj;

                JsonValue fields = json.get("properties");
                setFields(obj, fields);

                UBJsonReader jsonReader = new UBJsonReader();
                G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
                Model m = modelLoader.loadModel(Gdx.files.getFileHandle(handle.parent().path() + "/" + json.getString("file") + ".g3db", Files.FileType.Internal));

                modelContainer.setModel(m);

                return modelContainer;
            }

        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        } catch (InstantiationException c) {
            c.printStackTrace();
        } catch (IllegalAccessException c) {
            c.printStackTrace();
        }
        return null;
    }

    private static void setFields(Object obj, JsonValue fields) {
        JsonValue.JsonIterator iterator = fields.iterator();
        while(iterator.hasNext()) {
            JsonValue field_value = iterator.next();
            try {
                Field field = obj.getClass().getField(field_value.name);
                Object value = null;
                try {
                    value = field_value.asFloat();
                } catch (NumberFormatException e) { // cannot parse
                    String str = field_value.asString();

                    int last_point = str.lastIndexOf(".");
                    String enum_name = str.substring(0, last_point);
                    String enum_value = str.substring(last_point + 1, str.length());
                    Class<?> clz = Class.forName(enum_name);
                    Object[] consts = clz.getEnumConstants();
                    for (Object o : consts) {
                        if (o.toString().equals(enum_value))
                            value = o;
                    }

                    field.set(obj, value);
                }

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
