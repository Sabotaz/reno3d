package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.UBJsonReader;

import java.awt.Container;
import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

import fr.limsi.rorqual.core.event.EventRequest;
import fr.limsi.rorqual.core.logic.Logic;
import fr.limsi.rorqual.core.model.Ouverture;
import fr.limsi.rorqual.core.model.Slab;
import fr.limsi.rorqual.core.utils.AssetManager;
import fr.limsi.rorqual.core.utils.Holder;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;

/**
 * Created by christophe on 10/08/15.
 */
// chargement de la bibliothèque de modèles d'objets
public class ModelLibrary {

    private HashMap<String, Table> categoriesTables = new HashMap<String, Table>();

    public class ModelLoader {

        private Model model;
        private BoundingBox box = new BoundingBox();
        private String modelFile;
        private String iconFile;
        private String path;
        private String clazz;
        private String category;
        private float prix = 0;
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
            modelFile = i18n.get("model#" + n);
            prix = i18n.get("prix#"+n).startsWith("?") ? 0 : Float.parseFloat(i18n.get("prix#"+n));
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

        boolean loading = false;

        public ModelContainer getInstance() {
            final ModelContainer container = newInstance();
            container.setCategory(category);
            if (model == null && !loading) {
                loading = true;
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        synchronized (ModelLoader.this) {
                            UBJsonReader jsonReader = new UBJsonReader();
                            G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
                            model = modelLoader.loadModel(Gdx.files.getFileHandle(path + "/" + modelFile, Files.FileType.Internal));
                            container.setModel(model);
                            toScale(container);

                            box = container.getBoundingBox();
                            // update normals
                            validate(model);
                            container.setModel(model);
                            //toScale(container);

                            loading = false;
                            container.setChanged();
                        }
                    }
                };

                Gdx.app.postRunnable(runnable);
            } else {
                while (loading) try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (ModelLoader.this) {
                    container.setModel(model, false);
                    container.setBoundingBox(box);
                    toScale(container);
                    container.setChanged();
                }
            }
            return container;
        }

        public synchronized void preload() {
            if (model == null && !loading) {
                loading = true;
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        synchronized(ModelLoader.this) {
                            UBJsonReader jsonReader = new UBJsonReader();
                            G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
                            model = modelLoader.loadModel(Gdx.files.getFileHandle(path + "/" + modelFile, Files.FileType.Internal));
                            model.calculateBoundingBox(box);
                            // update normals
                            validate(model);
                            loading = false;
                        }
                    }
                };

                Gdx.app.postRunnable(runnable);
            }
        }

        private void toScale(ModelContainer container) {
            container.setDefaultSize(height, width, depth);
            BoundingBox b = container.getBoundingBox();
            //container.model_transform.idt().scale(1 / 10000f, 1 / 10000f, 1 / 10000f);
            Vector3 min = new Vector3();
            Vector3 c = new Vector3();
            b.getMin(min);
            b.getCenter(c);
            float dx = c.x * 0.01f * width / b.getWidth();
            float dy = c.z * 0.01f * height / b.getHeight();
            container.model_transform // TODO: is it X or Y ?
                    .translate(-dx,dy, Slab.DEFAULT_HEIGHT/2 - min.y * 0.01f * depth / b.getDepth() + 0.01f * elevation)
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

        boolean image_loading_done = false;

        public String getImageName() {
            if (AssetManager.getInstance().get(path + "/" + iconFile) == null) {
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Texture t = new Texture(Gdx.files.internal(path + "/" + iconFile));
                        //System.out.println(t);

                        AssetManager.getInstance().put(path + "/" + iconFile, t);
                        image_loading_done = true;
                    }
                };
                Gdx.app.postRunnable(runnable);
                try {
                    while (!image_loading_done) {
                        Thread.sleep(15L);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return path + "/" + iconFile;
        }

        public float getPrix() {
            return prix;
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
        makeCategories(Gdx.files.getFileHandle("data/models/g3db/PluginFurnitureCatalog", Files.FileType.Internal));
        makeTabWindow();
    }

    private void makeCategories(FileHandle file) {
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
                    Gdx.files.getFileHandle(file.parent().path() + "/" + i18n.get("model#"+n), Files.FileType.Internal).read().close();
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

    private TabWindow tabWindow = null;

    public TabWindow getTabWindow() {
        return tabWindow;
    }

    public Set<String> getCategories() {
        return categories.keySet();
    }

    private void makeTabWindow() {
        float width = new Value.Fixed(Gdx.graphics.getWidth() * 0.45f).get(null);
        TabWindow tw = new TabWindow(width);
        tw.setTitle("Bibliothèque");
        for (Map.Entry<String, HashMap<String, ModelLoader>> entry : categories.entrySet())
            if (!entry.getKey().equals("Portes et fenetres"))
                if (!entry.getValue().isEmpty())
                    makeNewTab(tw, entry.getKey(), entry.getValue());
        tabWindow = tw;
    }

    public Table getModelTable(String category) {
        return categoriesTables.get(category);
    }

    private void makeNewTab(TabWindow tw, String category, HashMap<String, ModelLoader> models) {

        Table content = new Table();
        float width = new Value.Fixed(Gdx.graphics.getWidth() * 0.4f).get(null);
        float height = new Value.Fixed(Gdx.graphics.getHeight() * 0.6f).get(null);
        content.setSize(width, height);
        int start_x = 0;
        final int MAX_X = 2;

        ButtonGroup<ImageButton> group = new ButtonGroup<ImageButton>();

        // tab who start new model in logic logic
        for (Map.Entry<String, ModelLoader> entry : models.entrySet()) {
            final String id = entry.getKey();
            ModelLoader modelLoader = entry.getValue();

            ImageButton imageButton = new Layout.ClickableImageButton(modelLoader.getImageName(), 128, 128);
            group.add(imageButton);

            imageButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    currentModel = id;
                    ModelLibrary.this.preload(id); // preload
                    Logic.getInstance().startModel();
                }
            });
            content.add(imageButton).size(128, 128).left().top();
            Label.LabelStyle lbs = new Label.LabelStyle((BitmapFont)AssetManager.getInstance().get("defaultTitle.fnt"),Color.WHITE);
            content.add(new Label(modelLoader.getPrix() + " euros",lbs)).padLeft(10).width(128-10);

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
        t.add(scrollPane).size(width+scrollPane.getScrollBarWidth(), height).top().left();
        content.setSize(width + scrollPane.getScrollBarWidth(), height);
        t.setName(category);

        createUpdaterTab(category, models);

        tw.addTable(t);
    }

    private void createUpdaterTab(String category, HashMap<String, ModelLoader> models) {
        Table content = new Table();
        float width = new Value.Fixed(Gdx.graphics.getWidth() * 0.4f).get(null);
        float height = new Value.Fixed(Gdx.graphics.getHeight() * 0.6f).get(null);
        content.setSize(width,height);
        int start_x = 0;
        final int MAX_X = 2;

        ButtonGroup<ImageButton> group = new ButtonGroup<ImageButton>();

        // tab who update current selected model
        for (Map.Entry<String, ModelLoader> entry : models.entrySet()) {
            final String id = entry.getKey();
            ModelLoader modelLoader = entry.getValue();

            ImageButton imageButton = new Layout.ClickableImageButton(modelLoader.getImageName(),128,128);
            group.add(imageButton);

            imageButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    currentModel = id;
                    Logic.getInstance().updateModel();
                }
            });
            content.add(imageButton).size(128, 128).left().top();
            Label.LabelStyle lbs = new Label.LabelStyle((BitmapFont)AssetManager.getInstance().get("defaultTitle.fnt"),Color.WHITE);
            content.add(new Label(modelLoader.getPrix() + " euros", lbs)).padLeft(10).width(128-10);

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
        t.add(scrollPane).size(width+scrollPane.getScrollBarWidth(),height).top().left();
        content.setSize(width + scrollPane.getScrollBarWidth(), height);
        t.setName(category);

        categoriesTables.put(category, t);

    }

    public ModelContainer getModelContainerFromId(String id) {
        System.out.println(id + ": " + models.get(id));
        return models.get(id).getInstance();
    }

    public ModelLoader getModelFromId(String modelId) {
        return models.get(modelId);
    }

    public Class getModelClassFromId(String id) {
        try {
            return Class.forName(models.get(id).clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public void preload(String id) {
        models.get(id).preload();
    }

    private static void validate(Model model) {
        model.meshes.clear();
        for (MeshPart part : model.meshParts)
            model.meshes.add(checkNormals(part));
    }

    private static Mesh checkNormals(final MeshPart meshPart) {
        if (meshPart.mesh.getVertexAttribute(VertexAttributes.Usage.Normal) == null) {
            meshPart.mesh = createNormals(meshPart.mesh);
        }
        return meshPart.mesh;
    }

    private static Mesh createNormals(final Mesh mesh) {
        VertexAttribute atrs[] = new VertexAttribute[mesh.getVertexAttributes().size()+1];
        for (int i = 0; i < mesh.getVertexAttributes().size(); i++) {
            atrs[i] = mesh.getVertexAttributes().get(i);
        }
        atrs[mesh.getVertexAttributes().size()] = new VertexAttribute(VertexAttributes.Usage.Normal, 3, "a_normal");
        final VertexAttributes newAttributes = new VertexAttributes(atrs);

        final short[] indices = {0, 0, 0};

        ShortBuffer shbu = mesh.getIndicesBuffer().asReadOnlyBuffer();
        shbu.position(0);
        final short[] fullIndices = new short[shbu.remaining()];
        shbu.get(fullIndices);

        FloatBuffer flbu = mesh.getVerticesBuffer().asReadOnlyBuffer();
        flbu.position(0);
        int l = flbu.remaining();
        float[] fb = new float[l];
        flbu.get(fb);

        final float[] newVertices = new float[l + mesh.getNumVertices() * 3];

        int size = mesh.getVertexSize(); // bytes

        VertexAttribute va = mesh.getVertexAttribute(VertexAttributes.Usage.Position);

        int fsize = Float.SIZE / 8;
        int nfloats = size / fsize;

        assert va.numComponents == 3;

        float[] v1 = {0, 0, 0};
        float[] v2 = {0, 0, 0};
        float[] normal = {0, 0, 0};

        for (int i = 0; i < mesh.getNumIndices(); i += 3) {
            mesh.getIndices(i, 3, indices, 0);
            int offset = va.offset; // bytes

            int n1 = (indices[0] * size + offset) / fsize;
            int n2 = (indices[1] * size + offset) / fsize;
            int n3 = (indices[2] * size + offset) / fsize;

            v1[0] = fb[n2 + 0] - fb[n1 + 0];
            v1[1] = fb[n2 + 1] - fb[n1 + 1];
            v1[2] = fb[n2 + 2] - fb[n1 + 2];

            v2[0] = fb[n3 + 0] - fb[n1 + 0];
            v2[1] = fb[n3 + 1] - fb[n1 + 1];
            v2[2] = fb[n3 + 2] - fb[n1 + 2];

            normal[0] = v1[1] * v2[2] - v1[2] * v2[1];
            normal[1] = v1[2] * v2[0] - v1[0] * v2[2];
            normal[2] = v1[0] * v2[1] - v1[1] * v2[0];

            // replacer au bon endroit
            for (int j = 0; j < 3; j++) {
                System.arraycopy(
                        fb, indices[j] * nfloats,
                        newVertices, indices[j] * (nfloats + 3),
                        nfloats);

                System.arraycopy(
                        normal, 0,
                        newVertices, indices[j] * (nfloats + 3) + nfloats,
                        3);
            }
        }

        Mesh newMesh = new Mesh(true, mesh.getNumVertices(), mesh.getNumIndices(), newAttributes);
        newMesh.setVertices(newVertices);
        newMesh.setIndices(fullIndices);

        return newMesh;
    }
}
