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

import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Extrude;
import eu.mihosoft.vrl.v3d.Vector3d;
import fr.limsi.rorqual.core.dpe.Dpe;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.DateIsolationMurEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.OrientationEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.TypeIsolationMurEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.TypeMurEnum;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.event.EventRequest;
import fr.limsi.rorqual.core.model.primitives.MaterialTypeEnum;
import fr.limsi.rorqual.core.model.utils.Coin;
import fr.limsi.rorqual.core.model.utils.MyVector3;
import fr.limsi.rorqual.core.utils.CSGUtils;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.models.Cote;

/**
 * Created by ricordeau on 20/07/15.
 */
public class Mur extends ModelContainer implements Cote.Cotable {

    public final static float DEFAULT_DEPTH = 0.2f;

    private Coin A;
    private Coin B;
    private float width;
    private float depth;
    private double surface;
    private TypeMurEnum typeMur;
    private TypeIsolationMurEnum typeIsolationMur;
    private DateIsolationMurEnum dateIsolationMur;
    private OrientationEnum orientationMur;
    private double coeffTransmissionThermique;
    private Slab slabGauche = null;
    private Slab slabDroit = null;

    private ArrayList<MaterialTypeEnum> materialLayersMaterials = new ArrayList<MaterialTypeEnum>();

    private ArrayList<Coin> coins = new ArrayList<Coin>();

    private ArrayList<Ouverture> ouvertures = new ArrayList<Ouverture>();

    private Etage etage = null;

    private boolean changed = true;

    public Mur(Coin a, Coin b) {
        this(a, b, DEFAULT_DEPTH);
    }

    public Mur(Coin a, Coin b, float d) {
        super();
        this.A = a;
        this.B = b;
        A.addMur(this);
        B.addMur(this);
        this.depth = d;
        this.width = b.getPosition().cpy().sub(a.getPosition()).len();
        this.surface = Etage.DEFAULT_HEIGHT * this.width;
        this.typeMur=TypeMurEnum.MUR_DONNANT_SUR_EXTERIEUR;
        this.typeIsolationMur=TypeIsolationMurEnum.NON_ISOLE;
        this.dateIsolationMur=DateIsolationMurEnum.JAMAIS;
        this.orientationMur=OrientationEnum.NORD;
        materialLayersMaterials.add(MaterialTypeEnum.BRIQUE);
        materialLayersMaterials.add(MaterialTypeEnum.PIERRE);
    }

    boolean areMaterialSet = false;

    private void makeMaterials() {
        if (materialLayersMaterials.size() > 0) {
            Texture texture1_diff = materialLayersMaterials.get(0).getDiffuse();
            Texture texture1_norm = materialLayersMaterials.get(0).getNormal();

            Texture texture2_diff = materialLayersMaterials.get(materialLayersMaterials.size()-1).getDiffuse();
            Texture texture2_norm = materialLayersMaterials.get(materialLayersMaterials.size()-1).getNormal();

            texture1_diff.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
            texture1_norm.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
            texture2_diff.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
            texture2_norm.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);

            TextureAttribute ta1_diff = TextureAttribute.createDiffuse(texture1_diff);
            TextureAttribute ta1_norm = TextureAttribute.createNormal(texture1_norm);
            TextureAttribute ta2_diff = TextureAttribute.createDiffuse(texture2_diff);
            TextureAttribute ta2_norm = TextureAttribute.createNormal(texture2_norm);

            ta1_diff.scaleU = ta1_diff.scaleV = 0.5f;
            ta1_norm.scaleU = ta1_norm.scaleV = 0.5f;
            ta2_diff.scaleU = ta2_diff.scaleV = 0.5f;
            ta2_norm.scaleU = ta2_norm.scaleV = 0.5f;

            frontMaterial.set(ta1_diff, ta1_norm);
            backMaterial.set(ta2_diff, ta2_norm);
        }
        else {
            frontMaterial.set(ColorAttribute.createDiffuse(Color.WHITE));
            backMaterial.set(ColorAttribute.createDiffuse(Color.WHITE));
        }

        areMaterialSet = true;
    }

    public void setEtage(Etage e) {
        etage = e;
    }

    public float getWidth() {
        return width;
    }

    private void setWidth(float width) { // PRIVATE ! changed programatically
        this.width = width;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
        setChanged();
    }

    public Coin getA() {
        return A;
    }

    public void setA(Coin a) {
        A.removeMur(this);
        A = a;
        if (a != null) {
            a.addMur(this);
            this.width = B.getPosition().cpy().sub(A.getPosition()).len();
        };
        setChanged();
    }

    public Coin getB() {
        return B;
    }

    public void setB(Coin b) {
        B.removeMur(this);
        B = b;
        if (b != null) {
            b.addMur(this);
            this.width = B.getPosition().cpy().sub(A.getPosition()).len();
        }
        setChanged();
    }

    public TypeMurEnum getTypeMur() {
        return typeMur;
    }

    public void setTypeMur(TypeMurEnum typeMur) {
        this.typeMur = typeMur;
        this.mitoyenneteChanged();
    }

    public TypeIsolationMurEnum getTypeIsolationMurEnum() {
        return typeIsolationMur;
    }

    public void setTypeIsolationMurEnum(TypeIsolationMurEnum typeIsolationMurEnum) {
        this.typeIsolationMur = typeIsolationMurEnum;
    }

    public DateIsolationMurEnum getDateIsolationMurEnum() {
        return dateIsolationMur;
    }

    public void setDateIsolationMurEnum(DateIsolationMurEnum dateIsolationMurEnum) {
        this.dateIsolationMur = dateIsolationMurEnum;
    }

    public OrientationEnum getOrientationMur() {
        return orientationMur;
    }

    public double getCoeffTransmissionThermique() {
        return coeffTransmissionThermique;
    }

    public void setCoeffTransmissionThermique(double newCoeff) {
        this.coeffTransmissionThermique=newCoeff;
    }

    public void setOrientationMur(OrientationEnum orientationMur) {
        this.orientationMur = orientationMur;
        this.orientationChanged();
        float dx = B.getPosition().x - A.getPosition().x;
        float dy = B.getPosition().y - A.getPosition().y;
        // What is the orientation of X ?
        this.etage.getBatiment().setOrientation(orientationMur.wrapX(dx, dy));
    }

    public void setGlobalOrientation(OrientationEnum orientationMur) {
        float dx = B.getPosition().x - A.getPosition().x;
        float dy = B.getPosition().y - A.getPosition().y;
        this.orientationMur = orientationMur.wrapX(dx, dy);
        this.orientationChanged();
    }

    public double getSurface(){
        return this.surface;
    }

    public ArrayList<Coin> getAnchors(Vector3 pt, float depth) {
        ArrayList<Coin> anchors = new ArrayList<>();
        anchors.add(A);
        anchors.add(B);
        return anchors;
    }

    private Model model_non_perce = null;

    Material frontMaterial = new Material();
    Material backMaterial = new Material();

    private void makeMesh() {
        if (A == null || B == null || B.getPosition().equals(A.getPosition()))
            return;
        Vector3 z_shape = Vector3.Z.cpy().scl(etage != null ? etage.getHeight() : Etage.DEFAULT_HEIGHT);
        Vector3 positive_offset = Vector3.X.cpy().setLength(this.depth / 2);
        Vector3 negative_offset = Vector3.X.cpy().setLength(this.depth / 2).scl(-1);
        Vector3 p1 = Vector3.Zero.cpy();
        Vector3 p2 = Vector3.X.cpy().setLength(A.getPosition().dst(B.getPosition()));
        if (A.isFirst(this)) {
            p1.add(negative_offset);
        } else {
            p1.add(positive_offset);
        }
        if (B.isFirst(this)) {
            p2.add(positive_offset);
        } else {
            p2.add(negative_offset);
        }
        Vector3 y_dir = Vector3.Y.cpy().setLength(this.depth / 2);

        Vector3d z = CSGUtils.castVector(z_shape);

        List<Vector3d> face = new ArrayList<Vector3d>();
        face.add(CSGUtils.castVector(p2.cpy().add(y_dir)));
        face.add(CSGUtils.castVector(p2.cpy().sub(y_dir)));
        face.add(CSGUtils.castVector(p1.cpy().sub(y_dir)));
        face.add(CSGUtils.castVector(p1.cpy().add(y_dir)));

        CSG csg = Extrude.points(z, face);
        model_non_perce = CSGUtils.toModel(csg);

        for (Ouverture o : ouvertures) {
            csg = csg.difference(o.getCSG());
        }

        Model model = CSGUtils.toModel(csg, frontMaterial, backMaterial);

        this.setModel(model);

        Matrix4 mx = new Matrix4();
        Vector2 dir = B.getPosition().cpy().sub(A.getPosition());
        final float angle = Vector2.X.angle(dir);
        mx.translate(new MyVector3(A.getPosition())).rotate(Vector3.Z, angle);
        local_transform.idt();
        local_transform.mul(mx);
    }

    public void act() {
        super.act();

        if (!areMaterialSet)
            makeMaterials();

        if (!changed)
            return;

        makeMesh();
        changed = false;
    }

    public void addOuverture(Ouverture o) {
        ouvertures.add(o);
        this.add(o);
        if (etage != null)
            etage.addOuverture(o);
        setChanged();
    }

    public ArrayList<Ouverture> getOuvertures() {
        return ouvertures;
    }

    public void removeOuverture(Ouverture o) {
        ouvertures.remove(o);
        this.remove(o);
        etage.removeOuverture(o);
        setChanged();
    }

    public void setChanged() {
        changed = true;
        for (Ouverture o : ouvertures)
            o.setChanged();
    }

    private Vector3 getIntersection(Ray ray, Matrix4 global_transform) {
        float min_dist = -1;
        Vector3 intersection = null;
        for (Mesh mesh : model_non_perce.meshes) {
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

    public Slab getSlabGauche() {
        return slabGauche;
    }

    public void setSlabGauche(Slab slab_gauche) {
        this.slabGauche = slab_gauche;

        if (isInterieur()) {
            this.typeMur = TypeMurEnum.MUR_INTERIEUR;
            this.mitoyenneteChanged();
        }
        else if (this.typeMur == TypeMurEnum.MUR_INTERIEUR){
            this.typeMur = TypeMurEnum.MUR_DONNANT_SUR_EXTERIEUR;
            this.mitoyenneteChanged();
        }
    }

    public Slab getSlabDroit() {
        return slabDroit;
    }

    public void setSlabDroit(Slab slab_droit) {
        this.slabDroit = slab_droit;
        if (isInterieur()) {
            this.typeMur = TypeMurEnum.MUR_INTERIEUR;
            this.mitoyenneteChanged();
        }
        else if (this.typeMur == TypeMurEnum.MUR_INTERIEUR){
            this.typeMur = TypeMurEnum.MUR_DONNANT_SUR_EXTERIEUR;
            this.mitoyenneteChanged();
        }
    }

    public boolean isInterieur() {
        return (slabGauche != null && slabDroit != null);
    }

    public void mitoyenneteChanged(){
        HashMap<String,Object> currentItems = new HashMap<String,Object>();
        currentItems.put("userObject", this);
        Event e = new Event(DpeEvent.MITOYENNETE_MUR_CHANGEE, currentItems);
        EventManager.getInstance().put(Channel.DPE, e);
    }

    public void orientationChanged(){
        HashMap<String,Object> currentItems = new HashMap<String,Object>();
        currentItems.put("userObject", this);
        Event e = new Event(DpeEvent.ORIENTATION_MUR_CHANGEE, currentItems);
        EventManager.getInstance().put(Channel.DPE, e);
    }
}
