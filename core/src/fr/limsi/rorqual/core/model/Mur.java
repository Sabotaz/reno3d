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
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Extrude;
import eu.mihosoft.vrl.v3d.Vector3d;
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
import fr.limsi.rorqual.core.model.utils.Coin;
import fr.limsi.rorqual.core.model.utils.MyVector3;
import fr.limsi.rorqual.core.utils.CSGUtils;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.models.Cote;

/**
 * Created by ricordeau on 20/07/15.
 */
// Mur (modèle + thermique)
@XStreamAlias("wall")
public class Mur extends ModelContainer implements Cote.Cotable {

    public final static float DEFAULT_DEPTH = 0.2f;

    @XStreamAlias("A")
    private Coin A;
    @XStreamAlias("B")
    private Coin B;
    @XStreamAlias("width")
    private float width;
    @XStreamAlias("depth")
    private float depth;
    @XStreamOmitField
    private float surfaceInitiale;
    @XStreamOmitField
    private float surface;
    @XStreamAlias("wallType")
    private TypeMurEnum typeMur;
    @XStreamAlias("isolationType")
    private TypeIsolationMurEnum typeIsolationMur;
    @XStreamAlias("isolationDate")
    private DateIsolationMurEnum dateIsolationMur;
    @XStreamAlias("orientation")
    private OrientationEnum orientationMur;
    @XStreamOmitField
    private float coeffTransmissionThermique;
    @XStreamOmitField
    private float deperdition;
    @XStreamAlias("leftSlab")
    private Slab slabGauche = null;
    @XStreamAlias("rightSlab")
    private Slab slabDroit = null;
    @XStreamOmitField
    private ArrayList<Ouverture> ouvertures = new ArrayList<Ouverture>();
    @XStreamOmitField
    private Etage etage = null;
    @XStreamOmitField
    private boolean changed = true;
    @XStreamOmitField
    private boolean areMaterialSet = false;
    @XStreamAlias("exteriorMaterial")
    private MaterialTypeEnum exteriorMaterialType = MaterialTypeEnum.BRIQUE;
    @XStreamAlias("interiorMaterial1")
    private MaterialTypeEnum interiorMaterialType1 = MaterialTypeEnum.WALL1;
    @XStreamAlias("interiorMaterial2")
    private MaterialTypeEnum interiorMaterialType2 = MaterialTypeEnum.WALL1;
    @XStreamAlias("defaultMaterial")
    private MaterialTypeEnum defaultMaterialType = MaterialTypeEnum.BETON;
    @XStreamOmitField
    private Material exteriorMaterial = new Material();
    @XStreamOmitField
    private Material interiorMaterial1 = new Material();
    @XStreamOmitField
    private Material interiorMaterial2 = new Material();
    @XStreamOmitField
    private Material defaultMaterial = new Material();
    @XStreamOmitField
    private boolean isCreated = false;
    @XStreamOmitField
    private Model model_non_perce = null;

    private Mur() {
    }

    public Mur(Coin a, Coin b, Mur model) {
        this(a, b, model.depth);
        typeMur = model.typeMur;
        typeIsolationMur = model.typeIsolationMur;
        dateIsolationMur = model.dateIsolationMur;
        coeffTransmissionThermique = model.coeffTransmissionThermique;

        setSlabGauche(model.getSlabGauche());
        if (model.getSlabGauche() != null)
            model.getSlabGauche().addMur(this);

        setSlabDroit(model.getSlabDroit());
        if (model.getSlabDroit() != null)
            model.getSlabDroit().addMur(this);

        this.setEtage(model.getEtage());

        this.setDefaultMaterialType(model.getDefaultMaterialType());
        this.setExteriorMaterialType(model.getExteriorMaterialType());
        this.setInteriorMaterialType1(model.getInteriorMaterialType1());
        this.setInteriorMaterialType2(model.getInteriorMaterialType2());
    }

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
        this.surfaceInitiale = this.surface;
        this.typeMur=TypeMurEnum.MUR_DONNANT_SUR_EXTERIEUR;
        this.typeIsolationMur=TypeIsolationMurEnum.INCONNUE;
        this.dateIsolationMur=DateIsolationMurEnum.INCONNUE;
        this.orientationMur=OrientationEnum.NORD;
    }

    public void setInteriorMaterialType1(MaterialTypeEnum mat) {
        interiorMaterialType1 = mat;
        areMaterialSet = false;
    }

    public void setInteriorMaterialType2(MaterialTypeEnum mat) {
        interiorMaterialType2 = mat;
        areMaterialSet = false;
    }

    public void setExteriorMaterialType(MaterialTypeEnum mat) {
        exteriorMaterialType = mat;
        areMaterialSet = false;
    }

    public void setDefaultMaterialType(MaterialTypeEnum mat) {
        defaultMaterialType = mat;
        areMaterialSet = false;
    }
    
    private void makeMaterials() {
        setMaterial(exteriorMaterial, exteriorMaterialType);
        setMaterial(interiorMaterial1, interiorMaterialType1);
        setMaterial(interiorMaterial2, interiorMaterialType2);
        setMaterial(defaultMaterial, defaultMaterialType);
        areMaterialSet = true;
        setChanged();
    }

    private void setMaterial(Material material, MaterialTypeEnum type) {
        Texture texture_diff = type.getDiffuse();
        Texture texture_norm = type.getNormal();

        texture_diff.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        TextureAttribute ta_diff = TextureAttribute.createDiffuse(texture_diff);
        ta_diff.scaleU = ta_diff.scaleV = 0.5f;
        if (texture_norm != null) {
            texture_norm.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
            TextureAttribute ta_norm = TextureAttribute.createNormal(texture_norm);
            ta_norm.scaleU = ta_norm.scaleV = 0.5f;
            material.set(ta_diff, ta_norm);
        } else {
            material.set(ta_diff);
        }
    }

    public void setEtage(Etage e) {
        etage = e;
    }

    public Etage getEtage() {
        return etage;
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
        if (A != null)
            A.removeMur(this);
        A = a;
        if (a != null) {
            a.addMur(this);
            this.width = B.getPosition().cpy().sub(A.getPosition()).len();
            this.surface = Etage.DEFAULT_HEIGHT * this.width;
            this.surfaceInitiale = this.surface;
        };
        setChanged();
    }

    public Coin getB() {
        return B;
    }

    public void setB(Coin b) {
        if (B != null)
            B.removeMur(this);
        B = b;
        if (b != null) {
            b.addMur(this);
            this.width = B.getPosition().cpy().sub(A.getPosition()).len();
            this.surface = Etage.DEFAULT_HEIGHT * this.width;
            this.surfaceInitiale = this.surface;
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

    public float getCoeffTransmissionThermique() {
        return coeffTransmissionThermique;
    }

    public void setCoeffTransmissionThermique(float newCoeff) {
        this.coeffTransmissionThermique=newCoeff;
    }

    public void setOrientationMur(OrientationEnum orientationMur) {
        this.orientationMur = orientationMur;
        float dx = B.getPosition().x - A.getPosition().x;
        float dy = B.getPosition().y - A.getPosition().y;
        // What is the orientation of X ?
        this.etage.getBatiment().setGlobalOrientation(orientationMur.wrapX(dx, dy));
    }

    public void setGlobalOrientation(OrientationEnum orientationMur) {
        float dx = B.getPosition().x - A.getPosition().x;
        float dy = B.getPosition().y - A.getPosition().y;
        this.orientationMur = orientationMur.wrapX(dx, dy);
    }

    public float getSurface(){
        return this.surface;
    }

    public ArrayList<Coin> getAnchors(Vector3 pt, float depth) {
        ArrayList<Coin> anchors = new ArrayList<>();
        anchors.add(A);
        anchors.add(B);
        return anchors;
    }

    private void makeMesh() {
        if (A == null || B == null || B.getPosition().epsilonEquals(A.getPosition(), 0.000_001f))
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

        if (p1.epsilonEquals(p2, 0.000_001f))
            return;

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

        Material front = slabGauche == null ? exteriorMaterial : interiorMaterial1;
        Material back = slabDroit == null ? exteriorMaterial : interiorMaterial2;

        Model model = CSGUtils.toModel(csg, front, back, defaultMaterial, exteriorMaterial, defaultMaterial);

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
        if (etage!=null)
            etage.updateOrientation(this);
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

    public void setCreated() {
        isCreated = true;
        this.mitoyenneteChanged();
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

    public float getDeperdition(){
        return this.deperdition;
    }

    public void mitoyenneteChanged(){
        if (!isCreated) return;
        HashMap<String,Object> currentItems = new HashMap<String,Object>();
        currentItems.put("userObject", this);
        Event e = new Event(DpeEvent.MITOYENNETE_MUR_CHANGEE, currentItems);
        EventManager.getInstance().put(Channel.DPE, e);
    }

    public void actualiseDeperdition(){
        switch (this.typeMur){
            case MUR_DONNANT_SUR_EXTERIEUR:
                this.deperdition = this.surface*this.coeffTransmissionThermique;
                break;
            case MUR_DONNANT_SUR_UNE_AUTRE_HABITATION:
                this.deperdition = 0.2f*this.surface*this.coeffTransmissionThermique;
                break;
            case MUR_DONNANT_SUR_UNE_VERANDA_NON_CHAUFFE:
                switch (this.orientationMur){
                    case INCONNUE:
                    case NORD:
                        switch(this.dateIsolationMur){
                            case INCONNUE:
                            case JAMAIS:
                                // Non isolé
                                this.deperdition=0.95f*this.surface*this.coeffTransmissionThermique;
                                break;
                            default:
                                // isolé
                                this.deperdition=0.85f*this.surface*this.coeffTransmissionThermique;
                                break;
                        }
                        break;
                    case EST:
                    case OUEST:
                        switch(this.dateIsolationMur){
                            case INCONNUE:
                            case JAMAIS:
                                // Non isolé
                                this.deperdition = 0.63f*this.surface*this.coeffTransmissionThermique;
                                break;
                            default:
                                // isolé
                                this.deperdition = 0.6f*this.surface*this.coeffTransmissionThermique;
                                break;
                        }
                        break;
                    case SUD:
                        switch(this.dateIsolationMur){
                            case INCONNUE:
                            case JAMAIS:
                                // Non isolé
                                this.deperdition = 0.6f*this.surface*this.coeffTransmissionThermique;
                                break;
                            default:
                                // isolé
                                this.deperdition = 0.55f*this.surface*this.coeffTransmissionThermique;
                                break;
                        }
                        break;
                }
                break;
            case MUR_DONNANT_SUR_UN_LOCAL_NON_CHAUFFE:
                switch (this.dateIsolationMur){
                    case INCONNUE:
                    case JAMAIS:
                        // Non isolé
                        this.deperdition = 0.95f*this.surface*this.coeffTransmissionThermique;
                        break;
                    default:
                        // isolé
                        this.deperdition = 0.85f*this.surface*this.coeffTransmissionThermique;
                        break;
                }
                break;
            case MUR_INTERIEUR:
            case MUR_DONNANT_SUR_UNE_VERANDA_CHAUFFE:
                this.deperdition=0;
                break;
        }
        Event e = new Event(DpeEvent.DEPERDITION_MURS_CHANGED, null);
        EventManager.getInstance().put(Channel.DPE, e);
    }

    public void actualiseSurface(){
        if (!this.getOuvertures().isEmpty()){
            float tampon = this.surfaceInitiale;
            for (Ouverture o:this.getOuvertures()){
                tampon -= o.getSurface();
            }
            this.surface=tampon;
            this.actualiseDeperdition();
        }
    }

    @Override
    public String toString(){
        return "Mur ->  s="+surface+" u="+coeffTransmissionThermique+ " dp="+deperdition;
    }

    public void remplaceCoin(Coin last, Coin next) {
        if (A.equals(last)) {
            this.setA(next);
        } else if (B.equals(last)) {
            this.setB(next);
        }
    }

    @Override
    public Vector3 getCotePosA() {
        return new Vector3(0,0,etage.getHeight());
    }

    @Override
    public Vector3 getCotePosB() {
        return new Vector3(this.getWidth(), 0, getEtage().getHeight());
    }

    @Override
    public float getCoteValue() {
        return this.getWidth();
    }

    public MaterialTypeEnum getExteriorMaterialType() {
        return exteriorMaterialType;
    }

    public MaterialTypeEnum getInteriorMaterialType1() {
        return interiorMaterialType1;
    }

    public MaterialTypeEnum getInteriorMaterialType2() {
        return interiorMaterialType2;
    }

    public MaterialTypeEnum getDefaultMaterialType() {
        return defaultMaterialType;
    }

    public void reload() {

    }
}
