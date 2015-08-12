package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.UBJsonReader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.limsi.rorqual.core.event.EventRequest;
import fr.limsi.rorqual.core.logic.Logic;
import fr.limsi.rorqual.core.utils.AssetManager;
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

        public ModelLoader(final JsonValue json, final String path) {
            this.json = json;
            this.path = path;
            fields = json.get("properties");
            clazz = json.getString("class");

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    UBJsonReader jsonReader = new UBJsonReader();
                    G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
                    model = modelLoader.loadModel(Gdx.files.getFileHandle(path + "/" + json.getString("file") + ".g3db", Files.FileType.Internal));
                }
            };

            Gdx.app.postRunnable(runnable);

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

        private Image image = null;

        public synchronized Image getImage() {
            if (image == null) {
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Texture t = new Texture(Gdx.files.internal(path + "/" + json.getString("file") + ".jpg"));
                        image = new Image(t);
                    }
                };
                Gdx.app.postRunnable(runnable);
                try {
                    while (image == null)
                        Thread.sleep(15L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return image;
        }

    }

    private int currentModel = 0;

    public int getCurrentModelId() {
        return currentModel;
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

    public Set<String> getCategories() {
        return categories.keySet();
    }

    private void makeTabWindow(String category) {
        HashMap<String, HashMap<Integer, ModelLoader>> subcategories = categories.get(category);
        TabWindow tw = new TabWindow(400);
        tw.setTitle(category);
        for (Map.Entry<String, HashMap<Integer, ModelLoader>> entry : subcategories.entrySet())
            makeNewTab(tw, entry.getKey(), entry.getValue());
        tabWindows.put(category, tw);
    }

    private void makeNewTab(TabWindow tw, String subcategory, HashMap<Integer, ModelLoader> models) {

        Table content = new Table();
        content.setSize(400,400);
        int start_x = 0;
        final int MAX_X = 4;

        ButtonGroup<ImageButton> group = new ButtonGroup<ImageButton>();

        for (Map.Entry<Integer, ModelLoader> entry : models.entrySet()) {
            final int id = entry.getKey();
            ModelLoader modelLoader = entry.getValue();

            Image image = modelLoader.getImage();

            ImageButton imageButton = new Layout.ClickableImageButton(image.getDrawable());
            imageButton.setSize(100, 100);
            group.add(imageButton);

            imageButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    currentModel = id;
                    Logic.getInstance().startModel();
                }
            });
            content.add(imageButton).size(100, 100).left().top();

            start_x ++;
            if (start_x == MAX_X) {
                start_x = 0;
                content.row();
            }
        }
        content.layout();
        content.top().left();
        Skin skin = (Skin)AssetManager.getInstance().get("uiskin");
        final ScrollPane scrollPane = new ScrollPane(content, skin, "perso");
        scrollPane.setFlickScroll(false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.layout();
        scrollPane.updateVisualScroll();

        Table t = new Table();
        t.add(scrollPane).size(400,400).top().left();
        t.setName(subcategory);

        tw.addTable(t);

    }

    public ModelContainer getModelFromId(int id) {
        return models.get(id).getInstance();
    }
}
