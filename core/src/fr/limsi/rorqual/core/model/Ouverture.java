package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

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
    @XStreamAlias("wall")
    protected Mur mur;
    @XStreamAlias("position")
    protected Vector2 position = new Vector2();
    @XStreamAlias("width")
    protected float width = 1;
    @XStreamAlias("height")
    protected float height = 1;
    @XStreamOmitField
    protected float surface = 0;

    @XStreamOmitField
    OuverturePlane fausseOuverture = null;

    @XStreamOmitField
    private boolean changed = true;

    @XStreamOmitField
    protected Matrix4 scaleMatrix = new Matrix4();
    @XStreamOmitField
    private Vector3 dmin = new Vector3();
    @XStreamAlias("modelId")
    private String modelId = "";

    // Constructeur
    public Ouverture(Mur mur, Vector2 position, float width, float height){
        this.setMur(mur);
        this.position=position;
        this.width=width;
        this.height=height;
        this.surface=width*height;
        fausseOuverture = new OuverturePlane(this);

    }

    // Getter & Setter
    public Mur getMur() {
        return mur;
    }
    public void setMur(Mur mur) {
        if (this.mur != null) {
            this.mur.removeOuverture(this);
            this.mur.remove(fausseOuverture);
        }

        this.mur = mur;
        if (this.mur != null) {
            this.mur.addOuverture(this);
            this.mur.add(fausseOuverture);
        }
        this.setChanged();
    }

    public Vector2 getPosition() {
        return position;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
        if (mur != null)
            mur.setChanged();
        changed = true;
    }
    public void setX(float x) {
        this.position.x = x-this.width/2;
        if (mur != null)
            mur.setChanged();
    }


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

    public float getY() {
        float s1 = Slab.DEFAULT_HEIGHT/2;
        float s2 = Slab.DEFAULT_HEIGHT/2;
        if (this.getMur() == null)
            return Slab.DEFAULT_HEIGHT/2 + this.position.y;
        if (this.getMur().getSlabGauche() != null)
            s1 = this.getMur().getSlabGauche().getHeight()/2;
        if (this.getMur().getSlabDroit() != null)
            s2 = this.getMur().getSlabDroit().getHeight()/2;
        return Math.max(s1,s2) + this.position.y;
    }
    public float getSurface() {
        return surface;
    }
    public void setSurface(float surface) {
        this.surface = surface;
    }

    public CSG getCSG() {

        Vector3 z_shape = Vector3.Z.cpy().scl(this.height);

        Vector3 p1 = new Vector3(this.position.x, 0, getY());
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

    @Override
    public void setChanged() {
        if (changed != true) {
            changed = true;
            if (mur != null)
                mur.setChanged();
        }
    }

    public void act() {
        super.act();
        if (changed) {
            BoundingBox b = new BoundingBox(getBoundingBox());
            b.mul(model_transform);
            float w = this.getWidth() / b.getWidth();
            float h = (mur != null ? this.getMur().getDepth() : Mur.DEFAULT_DEPTH) / b.getHeight();
            float d = this.getHeight() / b.getDepth();
            dmin = b.getMin(new Vector3()).scl(-1);
            dmin.z = dmin.z + (getY()-this.position.y) / d;
            scaleMatrix.idt().scale(w, h, d);
            this.mur.setChanged();
            changed = false;
        }
        Matrix4 mx = new Matrix4();
        Vector3 vx = new Vector3(position.x, -(mur != null ? mur.getDepth() : Mur.DEFAULT_DEPTH) / 2, position.y);
        mx.translate(vx);
        local_transform.idt();
        local_transform.mul(mx).mul(scaleMatrix.cpy().translate(dmin));
        // sense ?
        if (this.getMur() != null) {
            if (this.getMur().getSlabGauche() == null) { // vers la gauche
                local_transform.mul(new Matrix4().setToRotation(0,0,1,180));
            } else { // vers la droite

            }
        }
    }

    @Override
    protected void draw(ModelBatch modelBatch, Environment environment, Type type, Matrix4 global_transform) {
        super.draw(modelBatch, environment, type, global_transform);

    }

    @Override
    protected void defaultSizeChanged() {
        this.setWidth(this.height * default_width / default_height);
    }

    @Override
    public void setParent(ModelContainer p) {
        if (parent != null)
            parent.remove(fausseOuverture);
        super.setParent(p);
        if (parent != null)
            parent.add(fausseOuverture);
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelId() {
        return modelId;
    }

    public void copy(Ouverture other) {
        this.position = other.getPosition();
    }
}
