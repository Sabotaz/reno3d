package fr.limsi.rorqual.core.utils.scene3d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;

/**
 * Created by christophe on 30/06/15.
 */
public class ModelContainer extends ActableModel {

    protected ModelContainer parent;
    protected ModelGraph root;

    protected List<ModelContainer> children = new ArrayList<ModelContainer>();
    public Matrix4 transform;
    protected Matrix4 model_transform;
    private Shader prefered_shader;
    private Object userData;
    private HashMap<String,Object> modelData = new HashMap<String, Object>();

    public ModelContainer() {
        transform = new Matrix4();
        model_transform = new Matrix4();
    }

    public ModelContainer(Model model) {
        transform = new Matrix4();
        model_transform = new Matrix4();

        super.materials.clear();     this.materials.addAll(model.materials);
        super.meshes.clear();        this.meshes.addAll(model.meshes);
        super.meshParts.clear();     this.meshParts.addAll(model.meshParts);
        super.nodes.clear();         this.nodes.addAll(model.nodes);
        super.animations.clear();    this.animations.addAll(model.animations);
    }

    public ModelContainer(ModelInstance modelInstance) {
        transform = new Matrix4();
        model_transform = modelInstance.transform;

        Model model = modelInstance.model;

        super.materials.clear();     this.materials.addAll(model.materials);
        super.meshes.clear();        this.meshes.addAll(model.meshes);
        super.meshParts.clear();     this.meshParts.addAll(model.meshParts);
        super.nodes.clear();         this.nodes.addAll(model.nodes);
        super.animations.clear();    this.animations.addAll(model.animations);
    }

    public void act() {
        for (ModelContainer c : children) {
            c.act();
        }
    }

    public void setShaderProgram(Shader s) {
        prefered_shader = s;
    }

    public void setUserData(Object o) {
        if (root != null)
            root.remove(this);
        userData = o;
        if (root != null)
            root.add(this);
    }
    public Object getUserData() {
        return userData;
    }

    public void add(ModelContainer child) {
        child.remove();
        children.add(child);
        child.setParent(this);
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
        if (children.contains(child))
            children.remove(child);
        child.parent = null;
        child.root = null;

        if (root != null) {
            // remove root datas
            Deque<ModelContainer> removed = new ArrayDeque<ModelContainer>();
            removed.add(child);
            do {
                ModelContainer current = removed.remove();
                root.remove(current);
                for (ModelContainer c : current.getChildren())
                    removed.add(c);
            } while (!removed.isEmpty());
        }

    }

    private void setParent(ModelContainer p) {
        parent = p;
        root = parent.root;
        if (root != null) {
            if (root != null) {
                // remove root datas
                Deque<ModelContainer> added = new ArrayDeque<ModelContainer>();
                added.add(this);
                do {
                    ModelContainer current = added.remove();
                    root.add(current);
                    for (ModelContainer c : current.getChildren())
                        added.add(c);
                } while (!added.isEmpty());
            }
        }
    }

    public void draw(ModelBatch modelBatch, Environment environment){
        draw(modelBatch, environment, new Matrix4());
    }

    public Matrix4 getFullTransform() {
        Matrix4 mx = new Matrix4();
        mx.idt().mul(model_transform);
        ModelContainer current = this;
        do {
            mx.mulLeft(current.transform);
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

    protected void draw(ModelBatch modelBatch, Environment environment, Matrix4 global_transform){
        if (isVisible()) {
            // update mx
            Matrix4 updated_global_transform = global_transform.cpy().mul(transform);

            // update model mx
            transform = updated_global_transform.cpy().mul(model_transform);
            // draw
            modelBatch.render(this, environment);

            drawChildren(modelBatch, environment, updated_global_transform);
        }
    }

    protected void drawChildren(ModelBatch modelBatch, Environment environment, Matrix4 global_transform){
        for (ModelContainer child : children) {
            child.draw(modelBatch, environment, global_transform);
        }
    }

    //public ModelInstance getModel() {
    //    return model;
    //}

    private float intersectsMesh(Ray ray, BoundingBox boundBox) {
        Vector3 pt = new Vector3();
        Boolean intersect = Intersector.intersectRayBounds(ray, boundBox, pt);

        final float dist2cam = pt.dst(ray.origin);
        return intersect ? dist2cam : -1f;
    }

    private boolean selectable = true;

    public void setSelectable(boolean b) {
        selectable = b;
    }

    protected float intersects(Ray ray, Matrix4 global_transform) {

        if (!selectable) return -1;

        BoundingBox boundBox = new BoundingBox();
        Vector3 center = new Vector3();

        calculateBoundingBox(boundBox);

        Matrix4 local_transform = global_transform.cpy().mul(model_transform);
        boundBox.mul(local_transform);
        boundBox.getCenter(center);

        Vector3 dimensions = new Vector3();
        boundBox.getDimensions(dimensions);
        float radius = dimensions.len() / 2f;

        //transform.getTranslation(position).cpy().add(center);
        final float len = ray.direction.dot(center.x-ray.origin.x, center.y-ray.origin.y, center.z-ray.origin.z);
        //final float dist2cam = position.dst(ray.origin);
        if (len < 0f)
            return -1f;
        float dist2 = center.dst2(ray.origin.x + ray.direction.x * len, ray.origin.y + ray.direction.y * len, ray.origin.z + ray.direction.z * len);
        //return (dist2 <= radius * radius) ? dist2cam : -1f;
        return (dist2 <= radius * radius) ? intersectsMesh(ray, boundBox) : -1f;
    }

    private class Hit {
        ModelContainer hit;
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
        Matrix4 current_mx = mx.cpy().mul(transform);
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

    public ModelContainer hit(Ray ray) {
        Hit hit = hit(ray, new Matrix4());
        return hit.hit;
    }

    public Vector3 getTop() {
        BoundingBox boundBox = new BoundingBox();

        calculateBoundingBox(boundBox);

        boundBox.mul(model_transform);

        return new Vector3(boundBox.getCenterX(), boundBox.getCenterY(), boundBox.getCenterZ() + boundBox.getDepth()*0.5f);
    }

    public void setColor(Color color){
        modelData.put("Color", color);
    }

    public void removeColor(){
        modelData.put("Color", null);
    }

}
