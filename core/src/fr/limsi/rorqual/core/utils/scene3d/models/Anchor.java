package fr.limsi.rorqual.core.utils.scene3d.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
 * Created by christophe on 30/07/15.
 */
public class Anchor extends ModelContainer {

    Vector3 pt = null;
    Vector3 A = null;
    Vector3 B = null;

    public Anchor(Vector3 pt) {
        this(pt, null, null);
    }

    public Anchor(Vector3 pt, Vector3 A) {
        this(pt, A, null);
    }

    public Anchor(Vector3 pt, Vector3 A, Vector3 B) {
        Vector3 offset = Vector3.Z.cpy().setLength(0.01f);
        this.pt = new Vector3(pt).add(offset);
        if (A != null)
            this.A = new Vector3(A).add(offset);
        if (B != null)
            this.B = new Vector3(B).add(offset);
        this.setSelectable(false);
    }

    boolean dirty = true;

    public Vector3 getPt () {
        return pt;
    }

    public Vector3 getA () {
        return A;
    }

    public Vector3 getB () {
        return B;
    }

    public void setPt(Vector3 pt) {
        this.pt = pt.cpy();
        dirty = true;
    }

    public void setA(Vector3 A) {
        if (A != null)
            this.A = A.cpy();
        else
            this.A = null;
        dirty = true;
    }

    public void setB(Vector3 B) {
        if (B != null)
            this.B = B.cpy();
        else
            this.B = null;
        dirty = true;
    }

    public void act() {
        super.act();
        if (dirty)
            makeMesh();
        dirty = false;
    }

    private void makeMesh() {

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder meshBuilder;

        if (A != null) {

            meshBuilder = modelBuilder.part("part1", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED)));

            Vector3 dir = A.cpy().sub(pt).setLength(100);
            Vector3 p1 = pt.cpy().sub(dir);
            Vector3 p2 = A.cpy().add(dir);
            meshBuilder.line(p1, p2);

            if (B != null) {
                dir = B.cpy().sub(pt).setLength(100);
                p1 = pt.cpy().sub(dir);
                p2 = B.cpy().add(dir);
                meshBuilder.line(p1, p2);
            }
        }

        meshBuilder = modelBuilder.part("part2", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED)));

        //meshBuilder.circle(0.05f, 30, pt, Vector3.Z.cpy().scl(1));
        if (A != null)
            meshBuilder.circle(0.05f, 30, A, Vector3.Z.cpy().scl(1));
        if (B != null)
            meshBuilder.circle(0.05f, 30, B, Vector3.Z.cpy().scl(1));


        Model model = modelBuilder.end();
        this.setModel(model);
    }

    @Override
    protected void draw(ModelBatch modelBatch, Environment environment, Matrix4 global_transform){
        Gdx.gl.glLineWidth(2);
        super.draw(modelBatch, environment, global_transform);
    }

}
