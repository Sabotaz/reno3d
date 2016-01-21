package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

import fr.limsi.rorqual.core.logic.Logic;
import fr.limsi.rorqual.core.utils.AssetManager;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;

/**
 * Created by christophe on 10/08/15.
 */
// chargement de la bibliothèque de modèles d'objets
public class TextureLibrary {

    private HashMap<String, Table> categoriesTables = new HashMap<String, Table>();

    class TextureLoader {

        private Texture texture;
        private Image image;
        private String textureFile;
        private String path;
        private String category;
        private String id;
        private String name;
        private float width = 0;
        private float height = 0;
        private HashMap<String, String> fields = new HashMap<String, String>();

        public TextureLoader(String path, I18NBundle i18n, int n) {
            this.path = path;

            i18n.setExceptionOnMissingKey(true);
            id = i18n.get("id#"+n);
            i18n.setExceptionOnMissingKey(false);

            name = i18n.get("name#" + n);
            category = i18n.get("category#" + n);
            textureFile = i18n.get("image#" + n);

            width = i18n.get("width#" + n).startsWith("?") ? 100 : Float.parseFloat(i18n.get("width#" + n));
            height = i18n.get("height#" + n).startsWith("?") ? 100 : Float.parseFloat(i18n.get("height#" + n));
            preload();

        }

        public Texture getTexture() {
            return texture;
        }

        public float getHeight() {
            return height;
        }

        public float getWidth() {
            return width;
        }

        public Image getImage() {
            return image;
        }

        public String getCategory() {
            return category;
        }

        public String getId() {
            return id;
        }

        public synchronized void preload() {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    synchronized (TextureLoader.this) {
                        texture = new Texture(path + "/" + textureFile);
                        image = new Image(texture);
                    }
                }
            };

            Gdx.app.postRunnable(runnable);
        }
    }

    private String currentModel = "";

    public String getCurrentModelId() {
        return currentModel;
    }

    private TextureLibrary() {
        makeLibrary();
    }

    /** Holder */
    private static class TextureLibraryHolder
    {
        /** Instance unique non préinitialisée */
        private final static TextureLibrary INSTANCE = new TextureLibrary();
    }

    public static synchronized TextureLibrary getInstance() {
        return TextureLibraryHolder.INSTANCE;
    }

    static HashMap<String, HashMap<String, TextureLoader>> categories = new HashMap<String, HashMap<String, TextureLoader>>();
    static HashMap<String, TextureLoader> textures = new HashMap<String, TextureLoader>();

    private void makeLibrary() {
        makeCategories(Gdx.files.getFileHandle("data/textures/contributions/PluginTexturesCatalog", Files.FileType.Internal));
        makeCategories(Gdx.files.getFileHandle("data/textures/eTeksScopia/PluginTexturesCatalog", Files.FileType.Internal));
    }

    private void makeCategories(FileHandle file) {
        I18NBundle i18n = I18NBundle.createBundle(file, Locale.FRENCH);

        int n = 1;
        do {
            try {


                TextureLoader textureLoader = new TextureLoader(file.parent().path(), i18n, n);
                String category = textureLoader.getCategory();
                String id = textureLoader.getId();

                if (!categories.containsKey(category))
                    categories.put(category, new HashMap<String, TextureLoader>());
                categories.get(category).put(id, textureLoader);
                textures.put(id, textureLoader);

                // end
                n++;

            } catch (MissingResourceException mre) {
                break;
            }
        } while (true);
    }

    public Set<String> getCategories() {
        return categories.keySet();
    }

    public Set<String> getCategory(String category) {
        return categories.get(category).keySet();
    }

    public TextureLoader getTextureLoader(String name) {
        return textures.get(name);
    }

}
