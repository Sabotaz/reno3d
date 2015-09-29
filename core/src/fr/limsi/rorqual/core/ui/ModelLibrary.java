package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
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
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.UBJsonReader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

import fr.limsi.rorqual.core.event.EventRequest;
import fr.limsi.rorqual.core.logic.Logic;
import fr.limsi.rorqual.core.model.Slab;
import fr.limsi.rorqual.core.utils.AssetManager;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;

/**
 * Created by christophe on 10/08/15.
 */
// chargement de la bibliothèque de modèles d'objets
public class ModelLibrary {

    private HashMap<String, Table> categoriesTables = new HashMap<String, Table>();

    private class ModelLoader {

        private Model model;
        private String modelFile;
        private String iconFile;
        private String path;
        private String clazz;
        private String category;
        private float width = 0;
        private float depth = 0;
        private float height = 0;
        private float dropOnTopElevation = 0;
        private float elevation = 0;
        private HashMap<String, String> fields = new HashMap<String, String>();

        public ModelLoader(String path, I18NBundle i18n, int n) {
            this.path = path;
            clazz = i18n.get("class#" + n);
            if (clazz.startsWith("?"))
                clazz = "fr.limsi.rorqual.core.model.Objet";
            String name = i18n.get("name#"+n);
            String tags = i18n.get("tags#"+n);
            String creationDate = i18n.get("creationDate#"+n);
            category = i18n.get("category#"+n);
            iconFile = i18n.get("icon#"+n);
            modelFile = i18n.get("model#"+n);
            String multiPartModel = i18n.get("multiPartModel#"+n);

            width = i18n.get("width#"+n).startsWith("?") ? 0 : Float.parseFloat(i18n.get("width#"+n));
            depth = i18n.get("depth#"+n).startsWith("?") ? 0 : Float.parseFloat(i18n.get("depth#" + n));
            height = i18n.get("height#"+n).startsWith("?") ? 0 : Float.parseFloat(i18n.get("height#" + n));
            dropOnTopElevation = i18n.get("dropOnTopElevation#"+n).startsWith("?") ? 0 : Float.parseFloat(i18n.get("dropOnTopElevation#" + n));
            elevation = i18n.get("elevation#"+n).startsWith("?") ? 0 : Float.parseFloat(i18n.get("elevation#" + n));
            String doorOrWindow = i18n.get("doorOrWindow#" + n);
            String creator = i18n.get("creator#"+n);

            int f = 1;
            do {
                try {
                    i18n.setExceptionOnMissingKey(true);
                    String field_name = i18n.get("field#"+n+"name#"+f);
                    String field_value = i18n.get("field#"+n+"value#"+f);
                    i18n.setExceptionOnMissingKey(false);
                    fields.put(field_name, field_value);

                    f++;

                } catch (MissingResourceException mre) {
                    break;
                }
            } while (true);
        }

        public ModelContainer getInstance() {
            final ModelContainer container = newInstance();
            container.setCategory(category);
            if (model == null) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        UBJsonReader jsonReader = new UBJsonReader();
                        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
                        model = modelLoader.loadModel(Gdx.files.getFileHandle(path + "/" + modelFile.replace("obj","g3db"), Files.FileType.Internal));
                        container.setModel(model);

                        toScale(container);
                    }
                };

                Gdx.app.postRunnable(runnable);
            }
            else {
                container.setModel(model);
                toScale(container);
            }
            return container;
        }

        private void toScale(ModelContainer container) {

            BoundingBox b = new BoundingBox();
            container.calculateBoundingBox(b);
            //container.model_transform.idt().scale(1 / 10000f, 1 / 10000f, 1 / 10000f);
            Vector3 min = new Vector3();
            Vector3 c = new Vector3();
            b.getMin(min);
            b.getCenter(c);
            float dx = c.x * 0.01f * width / b.getWidth();
            float dy = c.z * 0.01f * height / b.getHeight();
            container.model_transform // TODO: is it X or Y ?
                    .translate(-dx,dy, Slab.DEFAULT_HEIGHT - min.y * 0.01f * depth / b.getDepth() + 0.01f * elevation)
                    .scale(0.01f * width / b.getWidth(), 0.01f * height / b.getHeight(), 0.01f * depth / b.getDepth())
                    .rotate(1, 0, 0, 90)
                    ;

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
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                try {
                Field field = obj.getClass().getField(entry.getKey());

                    Object value = null;
                    try {
                        value = Float.parseFloat(entry.getValue());
                    } catch (NumberFormatException e) { // cannot parse
                        String str = entry.getValue();

                        int last_point = str.lastIndexOf(".");
                        String enum_name = str.substring(0, last_point);
                        String enum_value = str.substring(last_point + 1, str.length());
                        Class<?> clz = Class.forName(enum_name);
                        Object[] consts = clz.getEnumConstants();
                        for (Object o : consts) {
                            if (o.toString().equals(enum_value))
                                value = o;
                        }
                    }

                    field.set(obj, value);

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

        public Image getImage() {
            if (image == null) {
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Texture t = new Texture(Gdx.files.internal(path + "/" + iconFile));
                        image = new Image(t);
                    }
                };
                Gdx.app.postRunnable(runnable);
                try {
                    while (image == null) {
                        Thread.sleep(15L);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return image;
        }

    }

    private String currentModel = "";

    public String getCurrentModelId() {
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

    static HashMap<String, HashMap<String, ModelLoader>> categories = new HashMap<String, HashMap<String, ModelLoader>>();
    static HashMap<String, ModelLoader> models = new HashMap<String, ModelLoader>();

    private void makeLibrary() {
        ArrayList<FileHandle> files = listFiles();
        makeCategories(files);
        makeTabWindow();
    }

    private ArrayList<FileHandle> listFiles() {
        String[] folders = {
                "3DModels-BlendSwap-CC-0-1.5.1/BlendSwap-CC-0.sh3f_FILES",
                "3DModels-BlendSwap-CC-BY-1.5.1/BlendSwap-CC-BY.sh3f_FILES",
                "3DModels-Contributions-1.5.1/Contributions.sh3f_FILES",
                "3DModels-KatorLegaz-1.5.1/KatorLegaz.sh3f_FILES",
                "3DModels-LucaPresidente-1.5.1/LucaPresidente.sh3f_FILES",
                "3DModels-Scopia-1.5.1/Scopia.sh3f_FILES"
        };
        ArrayList<FileHandle> files = new ArrayList<FileHandle>();
        for (String folder : folders) {
            files.add(Gdx.files.getFileHandle("data/models/g3db/" + folder + "/PluginFurnitureCatalog", Files.FileType.Internal));
        }
        return files;
    }

    private void makeCategories(ArrayList<FileHandle> files) {
        for (FileHandle file: files) {
            I18NBundle i18n = I18NBundle.createBundle(file, Locale.FRENCH);

            int n = 1;
            do {
                try {
                    i18n.setExceptionOnMissingKey(true);
                    String id = i18n.get("id#"+n);
                    i18n.setExceptionOnMissingKey(false);
                    String category = i18n.get("category#"+n);

                    if (!categories.containsKey(category))
                        categories.put(category, new HashMap<String, ModelLoader>());

                    /*
                    boolean exists = Gdx.files.getFileHandle(file.parent().path() + "/" + i18n.get("model#"+n).replace("obj","g3db"), Files.FileType.Internal).exists()
                            && Gdx.files.getFileHandle(file.parent().path() + "/" + i18n.get("icon#"+n), Files.FileType.Internal).exists();
                    */
                    /// IT WAS TOO SLOW !

                    boolean exists = false;
                    try {
                        // hack from https://code.google.com/p/libgdx/issues/detail?id=1655
                        Gdx.files.getFileHandle(file.parent().path() + "/" + i18n.get("model#"+n).replace("obj","g3db"), Files.FileType.Internal).read().close();
                        Gdx.files.getFileHandle(file.parent().path() + "/" + i18n.get("icon#"+n), Files.FileType.Internal).read().close();
                        exists = true;
                    } catch (Exception e) {
                        // doesn't exist !
                    }
                    if (exists) {

                        ModelLoader modelLoader = new ModelLoader(file.parent().path(), i18n, n);
                        categories.get(category).put(id, modelLoader);
                        models.put(id, modelLoader);

                    }

                    // end
                    n++;

                } catch (MissingResourceException mre) {
                    break;
                }
            } while (true);
        }
    }

    private TabWindow tabWindow = null;

    public TabWindow getTabWindow() {
        return tabWindow;
    }

    public Set<String> getCategories() {
        return categories.keySet();
    }

    private void makeTabWindow() {
        TabWindow tw = new TabWindow(450);
        tw.setTitle("Bibliothèque");
        for (Map.Entry<String, HashMap<String, ModelLoader>> entry : categories.entrySet())
            if (!entry.getValue().isEmpty())
                makeNewTab(tw, entry.getKey(), entry.getValue());
        tabWindow = tw;
    }

    public Table getModelTable(String category) {
        return categoriesTables.get(category);
    }

    private void makeNewTab(TabWindow tw, String category, HashMap<String, ModelLoader> models) {

        Table content = new Table();
        content.setSize(400,400);
        int start_x = 0;
        final int MAX_X = 4;

        ButtonGroup<ImageButton> group = new ButtonGroup<ImageButton>();

        // tab who start new model in logic logic
        for (Map.Entry<String, ModelLoader> entry : models.entrySet()) {
            final String id = entry.getKey();
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
        t.add(scrollPane).size(400+scrollPane.getScrollBarWidth(), 400).top().left();
        content.setSize(400 + scrollPane.getScrollBarWidth(), 400);
        t.setName(category);

        createUpdaterTab(category, models);

        tw.addTable(t);
    }

    private void createUpdaterTab(String category, HashMap<String, ModelLoader> models) {

        Table content = new Table();
        content.setSize(400,400);
        int start_x = 0;
        final int MAX_X = 4;

        ButtonGroup<ImageButton> group = new ButtonGroup<ImageButton>();

        // tab who update current selected model
        for (Map.Entry<String, ModelLoader> entry : models.entrySet()) {
            final String id = entry.getKey();
            ModelLoader modelLoader = entry.getValue();

            Image image = modelLoader.getImage();

            ImageButton imageButton = new Layout.ClickableImageButton(image.getDrawable());
            imageButton.setSize(100, 100);
            group.add(imageButton);

            imageButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    currentModel = id;
                    Logic.getInstance().updateModel();
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
        t.add(scrollPane).size(400+scrollPane.getScrollBarWidth(),400).top().left();
        content.setSize(400 + scrollPane.getScrollBarWidth(), 400);
        t.setName(category);

        categoriesTables.put(category, t);

    }

    public ModelContainer getModelContainerFromId(String id) {
        return models.get(id).getInstance();
    }
}
