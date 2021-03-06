package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Extrude;
import eu.mihosoft.vrl.v3d.Vector3d;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.DateIsolationMurEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.OrientationEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.TypeIsolationMurEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.TypeMurEnum;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.model.utils.Coin;
import fr.limsi.rorqual.core.model.utils.MyVector3;
import fr.limsi.rorqual.core.utils.CSGUtils;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.models.Cote;

/**
 * Created by ricordeau on 20/07/15.
 */
// Objet (mobilier...) (modèle)
@XStreamAlias("objet")
public class Objet extends ModelContainer {

    public Objet() {
    }

    @XStreamAlias("x")
    float x = 0;
    @XStreamAlias("y")
    float y = 0;
    @XStreamAlias("angle")
    float angle = 0;
    @XStreamAlias("modelId")
    String modelId = "";
    @XStreamAlias("slab")
    private Slab slab = null;

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        this.x = x;
        this.y = y;
    }

    public void setToRotation(float angle) {
        this.model_transform.mulLeft(new Matrix4().setToRotation(0, 0, 1, -this.angle));
        this.angle = angle;
        this.model_transform.mulLeft(new Matrix4().setToRotation(0, 0, 1, this.angle));
    }

    public void rotate(int sens) {
        this.model_transform.mulLeft(new Matrix4().setToRotation(0, 0, 1, -this.angle));
        this.angle += sens * 15f;
        this.model_transform.mulLeft(new Matrix4().setToRotation(0, 0, 1, this.angle));
    }

    private Vector3 getIntersection(Ray ray, Matrix4 global_transform) {
        float min_dist = -1;
        Vector3 intersection = null;
        for (Mesh mesh : this.meshes) {
            short[] indices = {0, 0, 0};
            FloatBuffer flbu = mesh.getVerticesBuffer().asReadOnlyBuffer();
            flbu.position(0);
            int l = flbu.remaining();
            float[] fb = new float[l];
            flbu.get(fb);
            for (int i = 0; i < mesh.getNumIndices(); i += 3) {
                mesh.getIndices(i, 3, indices, 0);
                int size = mesh.getVertexSize(); // bytes
                VertexAttribute va = mesh.getVertexAttribute(VertexAttributes.Usage.Position);
                assert va.numComponents == 3;
                int offset = va.offset; // bytes

                int n = (indices[0] * size + offset) / (Float.SIZE / 8);
                Vector3 p1 = new Vector3(
                        fb[n + 0],
                        fb[n + 1],
                        fb[n + 2]).mul(global_transform);

                n = (indices[1] * size + offset) / (Float.SIZE / 8);
                Vector3 p2 = new Vector3(
                        fb[n + 0],
                        fb[n + 1],
                        fb[n + 2]).mul(global_transform);

                n = (indices[2] * size + offset) / (Float.SIZE / 8);
                Vector3 p3 = new Vector3(
                        fb[n + 0],
                        fb[n + 1],
                        fb[n + 2]).mul(global_transform);

                Vector3 inter = new Vector3();
                boolean intersect = Intersector.intersectRayTriangle(ray, p1, p2, p3, inter);
                if (intersect) {
                    float dist2cam = inter.dst(ray.origin);
                    if (dist2cam < min_dist || min_dist == -1) {
                        min_dist = dist2cam;
                        intersection = inter;
                    }
                }
            }
        }
        return intersection;
    }

    protected float intersects(Ray ray, Matrix4 global_transform) {
        if (super.intersects(ray, global_transform) == -1)
            return -1;
        else {
            intersection = getIntersection(ray, global_transform.cpy()
                    .mul(new Matrix4().idt().setToTranslation(position.x, position.y, 0))
                    .mul(model_transform));
            return intersection == null ? -1 : intersection.dst(ray.origin);
        }
    }

    public void setSlab(Slab s) {
        if (slab != null)
            slab.remove(this);
        if (s != null)
            s.add(this);
        slab = s;
    }

    public Slab getSlab() {
        return slab;
    }

    public String getModelId() {
        return modelId;
    }
}
