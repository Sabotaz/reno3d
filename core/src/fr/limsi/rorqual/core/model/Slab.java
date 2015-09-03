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
import fr.limsi.rorqual.core.dpe.enums.wallproperties.DateIsolationMurEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.OrientationEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.TypeIsolationMurEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.TypeMurEnum;
import fr.limsi.rorqual.core.model.primitives.MaterialTypeEnum;
import fr.limsi.rorqual.core.model.utils.Coin;
import fr.limsi.rorqual.core.model.utils.MyVector2;
import fr.limsi.rorqual.core.model.utils.MyVector3;
import fr.limsi.rorqual.core.utils.CSGUtils;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.models.Cote;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcPolyline;
import ifc2x3javatoolbox.ifc2x3tc1.LIST;

/**
 * Created by ricordeau on 20/07/15.
 */
public class Slab extends ModelContainer {

    public final static float DEFAULT_HEIGHT = 0.2f;
    private MitoyennetePlafond mitoyennetePlafond;
    private MitoyennetePlancher mitoyennetePlancher;
    private float uPlafond;
    private float uPlancher;
    private float deperditionPlafond;
    private float deperditionPlancher;
    private DateIsolationSlab dateIsolationPlafond;
    private DateIsolationSlab dateIsolationPlancher;
    private TypeIsolationSlab typeIsolationPlancher;
    private float surface;
    private Etage etage = null;

    private boolean changed = true;

    private List<Coin> coins;
    private float height;

    public Slab(List<Coin> coins) {
        this(coins, DEFAULT_HEIGHT);
    }

    public Slab(List<Coin> coins, float h) {
        super();
        this.coins = coins;
        this.height = h;
        this.mitoyennetePlafond=MitoyennetePlafond.TERRASSE;
        this.mitoyennetePlancher=MitoyennetePlancher.TERRE_PLEIN;
        this.uPlafond=2;
        this.uPlancher=2;
        this.dateIsolationPlafond=DateIsolationSlab.JAMAIS;
        this.dateIsolationPlancher=DateIsolationSlab.JAMAIS;
        this.typeIsolationPlancher=TypeIsolationSlab.NON_ISOLE;
    }

    public void actualiseSurface(){
        ArrayList<Coin> listCoin = new ArrayList<Coin>();
        listCoin.addAll(coins);
        listCoin.add(listCoin.get(0));
        float xActuel=0,xSuivant=0,yActuel=0,ySuivant=0,totX=0,totY=0, airePolygone=0;
        for (int i=0; i<listCoin.size()-1;i++){
            xActuel = listCoin.get(i).getPosition().x;
            xSuivant = listCoin.get(i + 1).getPosition().x;
            yActuel = listCoin.get(i).getPosition().y;
            ySuivant = listCoin.get(i+1).getPosition().y;
            totX += (xActuel*ySuivant);
            totY += (yActuel*xSuivant);
        }
        airePolygone=(totX-totY)/2;
        this.surface = Math.abs(airePolygone);
    }

    public void setEtage(Etage e) {
        etage = e;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
        setChanged();
    }

    public void setCoins(List<Coin> coins) {
        this.coins = coins;
        setChanged();
    }

    private void makeMesh() {
        if (coins == null)
            return;

        Vector3 z_shape = Vector3.Z.cpy().scl(this.height);

        Vector3d z = CSGUtils.castVector(z_shape);

        List<Vector3d> face = new ArrayList<Vector3d>();
        for (Coin c : coins) {
            face.add(CSGUtils.castVector(new MyVector3(c.getPosition())));
        }

        CSG csg = Extrude.points(z, face);

        Model model = CSGUtils.toModel(csg);

        this.setModel(model);
    }

    public void act() {
        super.act();
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

    public MitoyennetePlancher getMitoyennetePlancher() {
        return mitoyennetePlancher;
    }

    public void setMitoyennetePlancher(MitoyennetePlancher mitoyennetePlancher) {
        this.mitoyennetePlancher = mitoyennetePlancher;
    }

    public MitoyennetePlafond getMitoyennetePlafond() {
        return mitoyennetePlafond;
    }

    public void setMitoyennetePlafond(MitoyennetePlafond mitoyennetePlafond) {
        this.mitoyennetePlafond = mitoyennetePlafond;
    }

    public float getuPlafond() {
        return uPlafond;
    }

    public void setuPlafond(float uPlafond) {
        this.uPlafond = uPlafond;
    }

    public float getuPlancher() {
        return uPlancher;
    }

    public void setuPlancher(float uPlancher) {
        this.uPlancher = uPlancher;
    }

    public float getSurface() {
        return surface;
    }

    public void setSurface(float surface) {
        this.surface = surface;
    }

    public DateIsolationSlab getDateIsolationPlancher() {
        return dateIsolationPlancher;
    }

    public void setDateIsolationPlancher(DateIsolationSlab dateIsolationPlancher) {
        this.dateIsolationPlancher = dateIsolationPlancher;
    }

    public TypeIsolationSlab getTypeIsolationPlancher() {
        return typeIsolationPlancher;
    }

    public void setTypeIsolationPlancher(TypeIsolationSlab typeIsolationPlancher) {
        this.typeIsolationPlancher = typeIsolationPlancher;
    }

    public DateIsolationSlab getDateIsolationPlafond() {
        return dateIsolationPlafond;
    }

    public void setDateIsolationPlafond(DateIsolationSlab dateIsolationPlafond) {
        this.dateIsolationPlafond = dateIsolationPlafond;
    }

    public void actualiseDeperditionPlancher(){
        switch (this.mitoyennetePlancher){
            case VIDE_SANITAIRE:
                this.deperditionPlancher = 0.8f*this.surface*this.uPlancher;
                break;
            case TERRE_PLEIN:
                this.deperditionPlancher = this.surface*this.uPlancher;
                break;
            case SOUS_SOL:
            case LOCAL_NON_CHAUFFE:
                switch (this.dateIsolationPlancher){
                    case INCONNUE:
                    case JAMAIS:
                        // Non isolé
                        this.deperditionPlancher= 0.95f*this.surface*this.uPlancher;
                        break;
                    default:
                        // isolé
                        this.deperditionPlancher= 0.85f*this.surface*this.uPlancher;
                        break;
                }
                break;
            case AUTRE_HABITATION:
                this.deperditionPlancher = 0.2f*this.surface*this.uPlancher;
                break;
            case AUTRE_ETAGE_DU_LOGEMENT:
                this.deperditionPlancher = 0;
                break;
        }
    }

    public void actualiseDeperditionPlafond(){
        switch (this.mitoyennetePlafond){
            case COMBLE_PERDU:
            case LOCAL_NON_CHAUFFE:
                switch(this.dateIsolationPlafond){
                    case INCONNUE:
                    case JAMAIS:
                        // Non isolé
                        this.deperditionPlafond= 0.95f*this.surface*this.uPlafond;
                        break;
                    default:
                        // isolé
                        this.deperditionPlafond= 0.9f*this.surface*this.uPlafond;
                        break;
                }
                break;
            case COMBLE_AMMENAGEE:
            case TERRASSE:
                this.deperditionPlafond = this.surface*this.uPlafond;
                break;
            case AUTRE_HABITATION:
                this.deperditionPlafond = 0.2f*this.surface*this.uPlafond;
                break;
            case AUTRE_ETAGE_DU_LOGEMENT:
                this.deperditionPlafond = 0;
                break;
        }
    }
}
