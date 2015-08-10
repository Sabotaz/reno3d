package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.UBJsonReader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;

/**
 * Created by christophe on 10/08/15.
 */
public class ModelLibrary {

    private class ModelLoader {

        private Model model;
        private JsonValue json;
        private String path;
        private JsonValue fields;
        private String clazz;

        public ModelLoader(JsonValue json, String path) {
            this.json = json;
            this.path = path;

            UBJsonReader jsonReader = new UBJsonReader();
            G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
            model = modelLoader.loadModel(Gdx.files.getFileHandle(path + "/" + json.getString("file") + ".g3db", Files.FileType.Internal));

            fields = json.get("properties");

            clazz = json.getString("class");

        }

        public ModelContainer getInstance() {
            ModelContainer container = newInstance();
            container.setModel(model);
            return container;
        }

        private ModelContainer newInstance() {
            try {
                Object obj = Class.forName(clazz).newInstance();
                if (obj instanceof ModelContainer) {
                    ModelContainer modelContainer = (ModelContainer) obj;

                    setFields(obj);

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

        private void setFields(Object obj) {
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


    private ModelLibrary() {
        makeLibrary();
    }

    /** Holder */
    private static class ModelLibraryHolder
    {
        /** Instance unique non préinitialisée */
        private final static ModelLibrary INSTANCE = new ModelLibrary();
    }

    public static synchronized ModelLibrary getInstance() {
        return ModelLibraryHolder.INSTANCE;
    }

    static HashMap<String, HashMap<String, HashMap<Integer, ModelLoader>>> categories = new HashMap<String, HashMap<String, HashMap<Integer, ModelLoader>>>();
    static HashMap<Integer, ModelLoader> models = new HashMap<Integer, ModelLoader>();

    private void makeLibrary() {
        ArrayList<FileHandle> files = listFiles();
        makeCategories(files);
    }

    private ArrayList<FileHandle> listFiles() {
        FileHandle base = Gdx.files.getFileHandle("data/models/g3db/", Files.FileType.Internal);
        return collectJsons(base);
    }

    private ArrayList<FileHandle> collectJsons(FileHandle directory) {
        ArrayList<FileHandle> files = new ArrayList<FileHandle>();
        for (FileHandle file : directory.list()) {
            if (file.isDirectory())
                files.addAll(collectJsons(file));
            else if (file.extension().equals("json"))
                files.add(file);
        }
        return files;

    }

    private void makeCategories(ArrayList<FileHandle> files) {
        for (FileHandle file: files) {
            JsonValue json = new JsonReader().parse(file.readString());
            String category = json.getString("category");
            String subcategory = json.getString("subcategory");
            Integer id = json.getInt("id");
            if (!categories.containsKey(category))
                categories.put(category, new HashMap<String, HashMap<Integer, ModelLoader>>());
            if (!categories.get(category).containsKey(subcategory))
                categories.get(category).put(subcategory, new HashMap<Integer, ModelLoader>());
            ModelLoader model = new ModelLoader(json, file.parent().path());
            categories.get(category).get(subcategory).put(id, model);
            models.put(id, model);
        }
    }

    private HashMap<String, TabWindow> tabWindows = new HashMap<String, TabWindow>();

    public TabWindow getTabWindow(String category) {
        if (!tabWindows.containsKey(category))
            makeTabWindow(category);
        return tabWindows.get(category);
    }

    private void makeTabWindow(String category) {
        HashMap<String, HashMap<Integer, ModelLoader>> subcategories = categories.get(category);
        TabWindow tw = new TabWindow();
        tw.setTitle(category);
        for (Map.Entry<String, HashMap<Integer, ModelLoader>> entry : subcategories.entrySet())
            makeNewTab(tw, entry.getKey(), entry.getValue());
        tabWindows.put(category, tw);
    }

    private void makeNewTab(TabWindow tw, String subcategory, HashMap<Integer, ModelLoader> models) {

    }
}
