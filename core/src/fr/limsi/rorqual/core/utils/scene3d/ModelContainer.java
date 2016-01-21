package fr.limsi.rorqual.core.utils.scene3d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

import java.nio.FloatBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;

import fr.limsi.rorqual.core.ui.TextureLibrary;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;
import fr.limsi.rorqual.core.view.shaders.ShaderAttribute;

/**
 * Created by christophe on 30/06/15.
 */
public class ModelContainer extends ActableModel {

    protected ModelContainer parent;
    protected ModelGraph root;

    protected List<ModelContainer> children = new ArrayList<ModelContainer>();
    private Shader prefered_shader;
    private Object userData;
    private HashMap<String,Object> modelData = new HashMap<String, Object>();
    private String category = "";

    protected float default_height = 0;
    protected float default_width = 0;
    protected float default_depth = 0;

    public enum Type {
        OPAQUE,
        TRANSPARENT;
    }

    public ModelContainer() {
        super();
        selectedAttribute = new ShaderAttribute(ShaderAttribute.Selectable);
        this.setSelected(false);
    }

    public ModelContainer(Model model) {
        this();
        setModel(model);
    }

    public ModelContainer(ModelInstance modelInstance) {
        this(modelInstance.model);
        model_transform = modelInstance.transform;
    }

    public void act() {
        synchronized (this) {
            for (ModelContainer c : children) {
                c.act();
            }
        }
    }

    protected void setMaterial(Material material, String type) {
        TextureLibrary.TextureLoader loader = TextureLibrary.getInstance().getTextureLoader(type);
        Texture texture = loader.getTexture();

        texture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        TextureAttribute ta_diff = TextureAttribute.createDiffuse(texture);
        ta_diff.scaleU = -loader.getWidth()/100;
        ta_diff.scaleV = -loader.getHeight()/100;
        material.set(ta_diff);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String cat) {
        category = cat;
    }

    public void setShaderProgram(Shader s) {
        prefered_shader = s;
    }

    public void add(ModelContainer child) {
        synchronized (this) {
            child.remove();
            children.add(child);
            child.setParent(this);
        }
    }

    public ModelContainer getParent() {
        return parent;
    }

    private void remove() {
        if (parent != null)
            parent.remove(this);
    }

    public List<ModelContainer> getChildren() {
        return children;
    }

    public void remove(ModelContainer child) {
        synchronized (this) {
            if (children.contains(child))
                children.remove(child);
            child.setParent(null);
            child.root = null;
        }
    }

    protected void setParent(ModelContainer p) {
        parent = p;
        if (p != null) {
            root = parent.root;
        } else {
            root = null;
        }
        setRoot(root);
    }

    private void setRoot(ModelGraph r) {
        root = r;
        for (ModelContainer c : getChildren()) {
            c.setRoot(root);
        }
    }

    public void draw(ModelBatch modelBatch, Environment environment, Type type){
        synchronized (this) {
            draw(modelBatch, environment, type, new Matrix4());
        }
    }

    public Matrix4 getFullTransform() {
        Matrix4 mx = new Matrix4();
        mx.idt()
                .mul(new Matrix4().idt().setToTranslation(position.x, position.y, 0))
                .mul(model_transform);
        ModelContainer current = this;
        do {
            mx.mulLeft(current.local_transform);
            current = current.getParent();
        } while (current != null);

        return mx;
    }

    private boolean visible = true;

    public void setVisible(boolean b) {
        visible = b;
    }

    public boolean isVisible() {
        return visible;
    }

    private boolean selected = false;

    public void setSelected(boolean b) {
        selected = b;
        selectedAttribute.setUserData(new Object[] {selected, System.nanoTime() *  1e-9f});
    }

    public boolean isSelected() {
        return selected;
    }

    protected void draw(ModelBatch modelBatch, Environment environment, Type type, Matrix4 global_transform){

        //validate();

        if (isVisible()) {
            renderTransparent = type == Type.TRANSPARENT;

            // update mx
            Matrix4 updated_global_transform = global_transform.cpy().mul(local_transform);

            // update model mx
            world_transform = updated_global_transform.cpy()
                    .mul(new Matrix4().idt().setToTranslation(position.x, position.y, 0))
                    .mul(model_transform);
            // draw
            modelBatch.render(this, environment);

            drawChildren(modelBatch, environment, type, updated_global_transform);
        }
    }

    protected void drawChildren(ModelBatch modelBatch, Environment environment, Type type, Matrix4 global_transform){
        for (ModelContainer child : children) {
            child.draw(modelBatch, environment, type, global_transform);
        }
    }

    //public ModelInstance getModel() {
    //    return model;
    //}

    private float intersectsMesh(Ray ray, BoundingBox boundBox) {
        Vector3 pt = new Vector3();
        Boolean intersect = Intersector.intersectRayBounds(ray, boundBox, pt);
        intersection = pt;
        final float dist2cam = pt.dst(ray.origin);
        return intersect ? dist2cam : -1f;
    }

    private boolean selectable = true;

    public void setSelectable(boolean b) {
        selectable = b;
    }

    protected Vector2 position = new Vector2();

    public void setPosition(float x, float y) {
        position.set(x,y);
    }
    public void setPosition(Vector2 p) {
        position.set(p);
    }

    public Vector2 getPosition() {
        return position;
    }

    protected Vector3 intersection = null;

    public Vector3 getIntersection() {
        return intersection;
    }

    private BoundingBox box = new BoundingBox();

    protected float intersects(Ray ray, Matrix4 global_transform) {

        intersection = null;

        if (!selectable) return -1;

        Vector3 center = new Vector3();
        BoundingBox boundBox = new BoundingBox(getBoundingBox());

        Matrix4 local_transform = global_transform.cpy()
                .mul(new Matrix4().idt().setToTranslation(position.x, position.y, 0))
                .mul(model_transform);
        boundBox.mul(local_transform);
        boundBox.getCenter(center);

        Vector3 dimensions = new Vector3();
        boundBox.getDimensions(dimensions);
        float radius = dimensions.len() / 2f;

        //local_transform.getTranslation(position).cpy().add(center);

        final float len = ray.direction.dot(center.x-ray.origin.x, center.y-ray.origin.y, center.z-ray.origin.z);
        //final float dist2cam = position.dst(ray.origin);
        /*if (len < 0f)
            return -1f;*/
        float dist2 = center.dst2(ray.origin.x + ray.direction.x * len, ray.origin.y + ray.direction.y * len, ray.origin.z + ray.direction.z * len);
        //return (dist2 <= radius * radius) ? dist2cam : -1f;
        return (dist2 <= radius * radius) ? intersectsMesh(ray, boundBox) : -1f;
    }

    public boolean intersects(ModelContainer other) {

        BoundingBox boundBox1 = new BoundingBox(getBoundingBox());
        BoundingBox boundBox2 = new BoundingBox(other.getBoundingBox());
        boundBox1.mul(this.getFullTransform());
        boundBox2.mul(other.getFullTransform());

        if (!boundBox1.intersects(boundBox2))
            return false;
        return true;
    }

    private class Hit {
        ModelContainer hit = null;
        float distance = -1;

        public boolean isCloserThan(Hit other) {
            if (this.hit == null || this.distance == -1)
                return false;
            else if (other.hit == null || other.distance == -1)
                return true;
            else return distance < other.distance;
        }
    }

    private Hit hit(Ray ray, Matrix4 mx) {
        synchronized (this) {
            Matrix4 current_mx = mx.cpy().mul(local_transform);
            Hit temp = new Hit();
            for (ModelContainer child : children) {
                Hit hit = child.hit(ray, current_mx);
                if (hit.isCloserThan(temp))
                    temp = hit;
            }

            if (temp.hit == null) {
                float dist2 = this.intersects(ray, current_mx);
                if (dist2 >= 0f) {
                    temp.hit = this;
                    temp.distance = dist2;
                }
            }
            return temp;
        }
    }

    public ModelContainer hit(Ray ray) {
        Hit hit = hit(ray, new Matrix4());
        return hit.hit;
    }

    public Vector3 getTop() {
        BoundingBox boundBox = new BoundingBox(getBoundingBox());

        boundBox
                .mul(new Matrix4().idt().setToTranslation(position.x, position.y, 0))
                .mul(model_transform);

        return new Vector3(boundBox.getCenterX(), boundBox.getCenterY(), boundBox.getCenterZ() + boundBox.getDepth()*0.5f);
    }

    public void setColor(Color color){
        modelData.put("Color", color);
    }

    public void removeColor(){
        modelData.put("Color", null);
    }

    ShaderAttribute selectedAttribute;

    public void setModel(Model m) {
        setModel(m, true);

    }

    public void setModel(Model m, boolean calculateBoundingBox) {
        super.setModel(m);

        if (calculateBoundingBox) {
            box = new BoundingBox();
            calculateBoundingBox(box);
        } else {
            box = new BoundingBox();
        }

        for (Material material : materials)
            material.set(selectedAttribute);

    }

    public void addModel(Model m) {
        super.addModel(m);

        for (Material material : materials)
            material.set(selectedAttribute);
    }

    public synchronized BoundingBox getBoundingBox() {
        return box;
    }

    public void setBoundingBox(BoundingBox b) {
        box = b;
    }

    public void setDefaultSize(float h, float w, float d) {
        this.default_height = h;
        this.default_width = w;
        this.default_depth = d;
        defaultSizeChanged();
    }

    protected void defaultSizeChanged() {

    }

}
