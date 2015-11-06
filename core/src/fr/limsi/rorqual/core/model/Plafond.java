package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Extrude;
import eu.mihosoft.vrl.v3d.Vector3d;
import fr.limsi.rorqual.core.dpe.enums.slabproperties.DateIsolationSlab;
import fr.limsi.rorqual.core.dpe.enums.slabproperties.MitoyennetePlafond;
import fr.limsi.rorqual.core.dpe.enums.slabproperties.MitoyennetePlancher;
import fr.limsi.rorqual.core.dpe.enums.slabproperties.TypeIsolationSlab;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.model.utils.Coin;
import fr.limsi.rorqual.core.model.utils.MyVector3;
import fr.limsi.rorqual.core.utils.CSGUtils;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.models.Cote2D;
import fr.limsi.rorqual.core.utils.scene3d.models.SurfaceCote;

/**
 * Created by christophe on 05/11/15.
 */
// classe modélisant un plafond
public class Plafond extends ModelContainer {
    private Slab plancher;
    private boolean changed = true;
    private boolean valide = false;
    boolean areMaterialSet = false;

    public Plafond(Slab slab) {
        super();
        this.plancher = slab;
        this.setSelectable(false);
    }

    public List<Coin> getCoins() {
        return plancher.getCoins();
    }

    private void makeMesh() {
        if (getCoins() == null) {
            this.setModel(new Model());
            valide = false;
            return;
        }

        if (plancher.getSurface() == 0) {
            this.setModel(new Model());
            valide = false;
            return;
        }

        Vector3 z_shape = Vector3.Z.cpy().scl(this.plancher.getHeight()/2);

        Vector3d z = CSGUtils.castVector(z_shape);

        List<Vector3d> face = new ArrayList<Vector3d>();
        for (Coin c : getCoins()) {
            face.add(CSGUtils.castVector(new MyVector3(c.getPosition(), plancher.getEtage().getHeight()-this.plancher.getHeight()/2)));
        }

        try {

            CSG csg = Extrude.points(z, face);

            Material plafondMaterial = plancher.getPlafondMaterial();
            Model model = CSGUtils.toModel(csg,plafondMaterial, plafondMaterial, terrasseMaterial, plafondMaterial, plafondMaterial);
            this.setModel(model);
            valide = true;

        } catch (RuntimeException re) {
            valide = false;
        }
    }

    MaterialTypeEnum terrasseType = MaterialTypeEnum.TOIT1;
    Material terrasseMaterial = new Material();

    public void setTerasseMaterialType(MaterialTypeEnum mat) {
        terrasseType = mat;
        areMaterialSet = false;
    }

    private void makeMaterials() {
        setMaterial(terrasseMaterial, terrasseType);
        areMaterialSet = true;
        setChanged();
    }

    private void setMaterial(Material material, MaterialTypeEnum type) {
        Texture texture_diff = type.getDiffuse();
        Texture texture_norm = type.getNormal();

        texture_diff.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        TextureAttribute ta_diff = TextureAttribute.createDiffuse(texture_diff);
        ta_diff.scaleU = ta_diff.scaleV = 0.2f;
        if (texture_norm != null) {
            texture_norm.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
            TextureAttribute ta_norm = TextureAttribute.createNormal(texture_norm);
            ta_norm.scaleU = ta_norm.scaleV = 0.2f;
            material.set(ta_diff, ta_norm);
        } else {
            material.set(ta_diff);
        }
    }

    public boolean isValide() {
        if (!areMaterialSet)
            makeMaterials();
        if (changed) {
            makeMesh();
            changed = false;
        }
        return valide;
    }

    public void act() {
        super.act();

        this.setVisible(plancher.getEtage().getBatiment().arePlafondsVisibles());

        if (!areMaterialSet)
            makeMaterials();
        if (!changed)
            return;
        makeMesh();
        changed = false;
    }

    public void setChanged() {
        changed = true;
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
            intersection = getIntersection(ray, global_transform.cpy().mul(model_transform));
            return intersection == null ? -1 : intersection.dst(ray.origin);
        }
    }
}
