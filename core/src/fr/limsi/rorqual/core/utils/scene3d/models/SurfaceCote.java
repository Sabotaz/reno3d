package fr.limsi.rorqual.core.utils.scene3d.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import fr.limsi.rorqual.core.utils.AssetManager;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;

/**
 * Created by christophe on 27/07/15.
 */
public class SurfaceCote extends ModelContainer {

    public interface SurfaceCotable {
        Vector3 getCotePos();
        float getCoteValue();
    }

    private SurfaceCotable cotable;

    public SurfaceCote(SurfaceCotable c) {
        super();
        cotable = c;
        this.setSelectable(false);
    }

    public void act() {
        super.act();
        setModel(new Model());
        if (cotable.getCoteValue() != 0) {
            makeMesh();
        }
        this.setVisible(this.root.getCamera() instanceof OrthographicCamera);
    }

    private float value = 0.0f;

    Texture textTexture;

    private void makeMesh() {
        Vector3 p = cotable.getCotePos();

        Vector3 x_dir = Vector3.X.cpy();
        value = cotable.getCoteValue();

        Vector3 mid = p.cpy();

        Vector3 y_dir = Vector3.Y.cpy();
        Vector3 my_dir = y_dir.cpy().scl(-1);


        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder meshBuilder;

        FreeTypeFontGenerator generator = (FreeTypeFontGenerator) AssetManager.getInstance().get("default.fnt.generator");
        String text = String.format("%.2fm²", value);
        int startX = 0;
        int startY = 0;

        int cursor = startX; // startX
        Pixmap textPixmap = new Pixmap(500,100, Pixmap.Format.RGBA8888);
        textPixmap.setColor(Color.RED);

        for (char c : text.toCharArray()) {
            FreeTypeFontGenerator.GlyphAndBitmap glyphAndBitmap = generator.generateGlyphAndBitmap(c, 100, true);

            Pixmap fontPixmap = glyphAndBitmap.bitmap.getPixmap(Pixmap.Format.RGBA8888, Color.RED);
            BitmapFont.Glyph glyph = glyphAndBitmap.glyph;

            textPixmap.drawPixmap(fontPixmap, glyph.srcX, glyph.srcY, glyph.width, glyph.height, cursor + glyph.xoffset, startY + glyph.yoffset, glyph.width, glyph.height);
            //textPixmap = fontPixmap;
            cursor += glyph.xadvance;
        }

        Pixmap resizedPixmap = new Pixmap(cursor,100, Pixmap.Format.RGBA8888);
        resizedPixmap.drawPixmap(textPixmap, 0, 0, 0, 0, cursor, 100);
        float half = (cursor / 100.0f) / 2;

        Texture texture = new Texture(resizedPixmap);
        meshBuilder = modelBuilder.part("part3", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, new Material(TextureAttribute.createDiffuse(texture)));

        meshBuilder.setUVRange(0, 0, 1, 1);

        meshBuilder.rect(
                mid.cpy().add(my_dir.cpy().setLength(0.5f)).sub(x_dir.cpy().setLength(half)),
                mid.cpy().add(my_dir.cpy().setLength(0.5f)).add(x_dir.cpy().setLength(half)),
                mid.cpy().add( y_dir.cpy().setLength(0.5f)).add(x_dir.cpy().setLength(half)),
                mid.cpy().add( y_dir.cpy().setLength(0.5f)).sub(x_dir.cpy().setLength(half)),
                Vector3.Z.cpy().scl(-1)
        );

        Model model = modelBuilder.end();
        this.addModel(model);

        textPixmap.dispose();
        resizedPixmap.dispose();
        if (textTexture != null)
            textTexture.dispose();
        textTexture = texture;
    }

    @Override
    protected void draw(ModelBatch modelBatch, Environment environment, Type type, Matrix4 global_transform){
        Gdx.gl.glLineWidth(5);
        super.draw(modelBatch, environment, type, global_transform);
        Gdx.gl.glLineWidth(1);
    }
}
