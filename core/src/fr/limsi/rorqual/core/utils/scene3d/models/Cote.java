package fr.limsi.rorqual.core.utils.scene3d.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import fr.limsi.rorqual.core.utils.AssetManager;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;

/**
 * Created by christophe on 27/07/15.
 */
public class Cote extends ModelContainer {

    public interface Cotable {
        public Vector3 getA();
        public Vector3 getB();
    }

    private Cotable cotable;

    public Cote(Cotable c) {
        super();
        cotable = c;
        this.setSelectable(false);
    }

    public void act() {
        super.act();
        makeMesh();
    }

    private float len = 0.0f;

    private void makeMesh() {
        Vector3 p1 = cotable.getA();
        Vector3 p2 = cotable.getB();
        p1.add(Vector3.Z.cpy().setLength(0.01f));
        p2.add(Vector3.Z.cpy().setLength(0.01f));

        Vector3 x_dir = p2.cpy().sub(p1);
        len = x_dir.len();

        Vector3 mid = p1.cpy().add(x_dir.cpy().scl(0.5f));

        Vector3 y_dir = x_dir.cpy().crs(Vector3.Z);
        y_dir.setLength(1);
        Vector3 my_dir = y_dir.cpy().scl(-1);


        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder meshBuilder;
        meshBuilder = modelBuilder.part("part1", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED)));

        meshBuilder.line(p1, p1.cpy().add(y_dir));
        meshBuilder.line(p2, p2.cpy().add(y_dir));
        meshBuilder.line(p1, p1.cpy().add(my_dir));
        meshBuilder.line(p2, p2.cpy().add(my_dir));

        meshBuilder.line(
                p1.cpy().add(y_dir.cpy().setLength(0.8f)),
                p2.cpy().add(y_dir.cpy().setLength(0.8f)));

        meshBuilder.line(
                p1.cpy().add(my_dir.cpy().setLength(0.8f)),
                p2.cpy().add(my_dir.cpy().setLength(0.8f)));

        meshBuilder = modelBuilder.part("part2", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED)));

        meshBuilder.triangle(p1.cpy().add(y_dir.cpy().setLength(0.8f)),
                p1.cpy().add(y_dir.cpy().setLength(0.6f)).add(x_dir.cpy().setLength(0.25f)),
                p1.cpy().add(y_dir.cpy().setLength(1.0f)).add(x_dir.cpy().setLength(0.25f))
        );

        meshBuilder.triangle(p1.cpy().add(my_dir.cpy().setLength(0.8f)),
                p1.cpy().add(my_dir.cpy().setLength(0.6f)).add(x_dir.cpy().setLength(0.25f)),
                p1.cpy().add(my_dir.cpy().setLength(1.0f)).add(x_dir.cpy().setLength(0.25f))
        );

        meshBuilder.triangle(p2.cpy().add(y_dir.cpy().setLength(0.8f)),
                p2.cpy().add(y_dir.cpy().setLength(0.6f)).sub(x_dir.cpy().setLength(0.25f)),
                p2.cpy().add(y_dir.cpy().setLength(1.0f)).sub(x_dir.cpy().setLength(0.25f))
        );

        meshBuilder.triangle(p2.cpy().add(my_dir.cpy().setLength(0.8f)),
                p2.cpy().add(my_dir.cpy().setLength(0.6f)).sub(x_dir.cpy().setLength(0.25f)),
                p2.cpy().add(my_dir.cpy().setLength(1.0f)).sub(x_dir.cpy().setLength(0.25f))
        );

        FreeTypeFontGenerator generator = (FreeTypeFontGenerator) AssetManager.getInstance().get("default.fnt.generator");
        String text = String.format("%.2fm", len);
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
        resizedPixmap.drawPixmap(textPixmap,0,0,0,0,cursor,100);

        float half = (cursor / 100.0f) / 2;

        Texture textTexture = new Texture(resizedPixmap);

        meshBuilder = modelBuilder.part("part3", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, new Material(TextureAttribute.createDiffuse(textTexture)));

        meshBuilder.setUVRange(0, 0, 1, 1);
        meshBuilder.rect(
                mid.cpy().add(y_dir.cpy().setLength(2.0f)).sub(x_dir.cpy().setLength(half)),
                mid.cpy().add(y_dir.cpy().setLength(2.0f)).add(x_dir.cpy().setLength(half)),
                mid.cpy().add(y_dir.cpy().setLength(1.0f)).add(x_dir.cpy().setLength(half)),
                mid.cpy().add(y_dir.cpy().setLength(1.0f)).sub(x_dir.cpy().setLength(half)),
                Vector3.Z.cpy().scl(-1)
        );

        meshBuilder.rect(
                mid.cpy().add(my_dir.cpy().setLength(2.0f)).sub(x_dir.cpy().setLength(half)),
                mid.cpy().add(my_dir.cpy().setLength(2.0f)).add(x_dir.cpy().setLength(half)),
                mid.cpy().add(my_dir.cpy().setLength(1.0f)).add(x_dir.cpy().setLength(half)),
                mid.cpy().add(my_dir.cpy().setLength(1.0f)).sub(x_dir.cpy().setLength(half)),
                Vector3.Z.cpy().scl(-1)
        );

        Model model = modelBuilder.end();
        this.setModel(model);
    }

    @Override
    protected void draw(ModelBatch modelBatch, Environment environment, Matrix4 global_transform){
        Gdx.gl.glLineWidth(5);
        super.draw(modelBatch, environment, global_transform);
    }
}
