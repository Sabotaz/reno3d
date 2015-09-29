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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import fr.limsi.rorqual.core.model.utils.Coin;
import fr.limsi.rorqual.core.model.utils.MyVector3;
import fr.limsi.rorqual.core.utils.AssetManager;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;

/**
 * Created by christophe on 30/07/15.
 */
public class Anchor extends ModelContainer {

    Coin pt = null;
    Coin A = null;
    Coin B = null;

    public Anchor(Coin pt) {
        this(pt, null, null);
    }

    public Anchor(Coin pt, Coin A) {
        this(pt, A, null);
    }

    public Anchor(Coin pt, Coin A, Coin B) {
        this.pt = pt;
        this.A = A;
        this.B = B;
        this.setSelectable(false);
    }

    boolean dirty = true;

    public Coin getPt() {
        return pt;
    }

    public Coin getA () {
        return A;
    }

    public Coin getB () {
        return B;
    }

    public void setPt(Coin pt) {
        this.pt = pt;
        dirty = true;
    }

    public void setA(Coin A) {
        this.A = A;
        dirty = true;
    }

    public void setB(Coin B) {
        this.B = B;
        dirty = true;
    }

    public void act() {
        super.act();
        if (dirty)
            makeMesh();
        dirty = false;
    }

    private void makeMesh() {
        /*

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder meshBuilder;

        if (A != null) {

            meshBuilder = modelBuilder.part("part1", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED)));

            Vector3 dir = new MyVector3(A.getPosition().cpy().sub(pt.getPosition()).setLength(100));
            Vector3 p1 = new Vector3(pt.getPosition(), 0.1f).sub(dir);
            Vector3 p2 = new Vector3(A.getPosition(), 0.1f).add(dir);
            meshBuilder.line(p1, p2);

            if (B != null) {
                dir = new MyVector3(B.getPosition().cpy().sub(pt.getPosition()).setLength(100));
                p1 = new Vector3(pt.getPosition(), 0.1f).sub(dir);
                p2 = new Vector3(B.getPosition(), 0.1f).add(dir);
                meshBuilder.line(p1, p2);
            }
        }

        meshBuilder = modelBuilder.part("part2", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED)));

        //meshBuilder.circle(0.05f, 30, pt, Vector3.Z.cpy().scl(1));
        if (A != null)
            meshBuilder.circle(0.05f, 30, new MyVector3(A.getPosition()), Vector3.Z.cpy().scl(1));
        if (B != null)
            meshBuilder.circle(0.05f, 30, new MyVector3(B.getPosition()), Vector3.Z.cpy().scl(1));


        Model model = modelBuilder.end();
        this.setModel(model);*/
    }

    @Override
    protected void draw(ModelBatch modelBatch, Environment environment, Type type, Matrix4 global_transform){
        Gdx.gl.glLineWidth(2);
        super.draw(modelBatch, environment, type, global_transform);
        Gdx.gl.glLineWidth(1);
    }

}
