package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import java.util.ArrayList;
import java.util.List;

import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Extrude;
import eu.mihosoft.vrl.v3d.Vector3d;
import fr.limsi.rorqual.core.utils.CSGUtils;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;

/**
 * Created by ricordeau on 20/07/15.
 */
// Classe servant de base aux portes et fenètres (modèle)
public abstract class Ouverture extends ModelContainer {

    // Attributs
    protected Mur mur;
    protected Vector2 position;
    protected float width;
    protected float height;
    protected float surface;

    // Constructeur
    public Ouverture(Mur mur, Vector2 position, float width, float height){
        this.setMur(mur);
        this.position=position;
        this.width=width;
        this.height=height;
        this.surface=width*height;
    }

    // Getter & Setter
    public Mur getMur() {
        return mur;
    }
    public void setMur(Mur mur) {
        if (this.mur != null)
            this.mur.removeOuverture(this);

        this.mur = mur;
        if (this.mur != null) {
            this.mur.addOuverture(this);
        }
    }

    public Vector2 getPosition() {
        return position;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
        mur.setChanged();
        changed = true;
    }
    public void setX(float x) {
        this.position.x = x-this.width/2;
        mur.setChanged();
    }

    private boolean changed = true;

    public float getWidth() {
        return width;
    }
    public void setWidth(float width) {
        this.width = width;
        changed = true;
    }
    public float getHeight() {
        return height;
    }
    public void setHeight(float height) {
        this.height = height;
        changed = true;
    }
    public float getSurface() {
        return surface;
    }
    public void setSurface(float surface) {
        this.surface = surface;
    }

    public CSG getCSG() {

        Vector3 z_shape = Vector3.Z.cpy().scl(this.height);

        Vector3 p1 = new Vector3(this.position.x, 0, this.position.y);
        Vector3 p2 = p1.cpy().add(Vector3.X.cpy().setLength(this.width));

        Vector3 y_dir = Vector3.Y.cpy().setLength(mur.getDepth() / 2 + 0.001f);

        Vector3d dir = CSGUtils.castVector(z_shape);

        List<Vector3d> face = new ArrayList<Vector3d>();
        face.add(CSGUtils.castVector(p2.cpy().add(y_dir)));
        face.add(CSGUtils.castVector(p2.cpy().sub(y_dir)));
        face.add(CSGUtils.castVector(p1.cpy().sub(y_dir)));
        face.add(CSGUtils.castVector(p1.cpy().add(y_dir)));

        CSG csg = Extrude.points(dir, face);

        return csg;
    }

    public void setChanged() {
        changed = true;
    }

    private Matrix4 scaleMatrix = new Matrix4();

    public void act() {
        super.act();
        if (mur == null)
            return;
        if (changed) {
            BoundingBox b = new BoundingBox();
            this.calculateBoundingBox(b).mul(model_transform);
            float w = this.getWidth() / b.getWidth();
            float h = this.getMur().getDepth() / b.getHeight();
            float d = this.getHeight() / b.getDepth();
            Vector3 dmin = b.getMin(new Vector3()).scl(-1);
            scaleMatrix.idt().scale(w, h, d).translate(dmin);
        }
        changed = false;
        Matrix4 mx = new Matrix4();
        Vector3 vx = new Vector3(position.x, -mur.getDepth()/2, position.y);
        mx.translate(vx);
        local_transform.idt();
        local_transform.mul(mx).mul(scaleMatrix);
    }

    @Override
    protected void draw(ModelBatch modelBatch, Environment environment, Type type, Matrix4 global_transform) {
        super.draw(modelBatch, environment, type, global_transform);

    }

}
