package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

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
 * Created by ricordeau on 20/07/15.
 */
// classe modélisant un plancher (modèle + thermique)
@XStreamAlias("slab")
public class Slab extends ModelContainer implements SurfaceCote.SurfaceCotable, Cote2D.Cotable2D {

    @XStreamOmitField
    public final static float DEFAULT_HEIGHT = 0.2f;
    @XStreamAlias("mitoyennetePlafond")
    private MitoyennetePlafond mitoyennetePlafond;
    @XStreamAlias("mitoyennetePlancher")
    private MitoyennetePlancher mitoyennetePlancher;
    @XStreamOmitField
    private float uPlafond;
    @XStreamOmitField
    private float uPlancher;
    @XStreamOmitField
    private float deperditionPlafond;
    @XStreamOmitField
    private float deperditionPlancher;
    @XStreamOmitField
    private float volume;
    @XStreamAlias("dateIsolationPlafond")
    private DateIsolationSlab dateIsolationPlafond;
    @XStreamAlias("dateIsolationPlancher")
    private DateIsolationSlab dateIsolationPlancher;
    @XStreamAlias("typeIsolationPlancher")
    private TypeIsolationSlab typeIsolationPlancher;
    @XStreamOmitField
    private float surface;
    @XStreamOmitField
    private Etage etage = null;
    @XStreamOmitField
    private boolean changed = true;
    @XStreamImplicit(itemFieldName="coin")
    private List<Coin> coins;
    @XStreamImplicit(itemFieldName="wall")
    private ArrayList <Mur> murs = new ArrayList<Mur>();
    @XStreamImplicit(itemFieldName="objet")
    private ArrayList <Objet> objets = new ArrayList<Objet>();
    @XStreamAlias("height")
    private float height;
    @XStreamOmitField
    private Polygon polygon;
    @XStreamOmitField
    private boolean valide = false;
    @XStreamAlias("plafondMaterial")
    private String plafondType = "eTeksScopia#pavement_2";
    @XStreamAlias("plancherMaterial")
    private String plancherType = "eTeksScopia#old-brown-parquet";
    @XStreamOmitField
    private Material plafondMaterial = new Material();
    @XStreamOmitField
    private Material plancherMaterial = new Material();
    @XStreamOmitField
    boolean areMaterialSet = false;
    @XStreamOmitField
    Plafond plafond;

    private Slab() {
    }

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
        this.dateIsolationPlafond=DateIsolationSlab.INCONNUE;
        this.dateIsolationPlancher=DateIsolationSlab.INCONNUE;
        this.typeIsolationPlancher=TypeIsolationSlab.NON_ISOLE;
        plafond = new Plafond(this);
        this.add(plafond);
    }

    public Slab(List<Coin> coins, Slab model) {
        this(coins, model.getHeight());
        for (Coin coin : coins)
            coin.addSlab(this);
        this.height = model.height;
        this.mitoyennetePlafond=model.getMitoyennetePlafond();
        this.mitoyennetePlancher=model.getMitoyennetePlancher();
        this.uPlafond=model.getuPlafond();
        this.uPlancher=model.getuPlancher();
        this.dateIsolationPlafond=model.getDateIsolationPlafond();
        this.dateIsolationPlancher=model.getDateIsolationPlancher();
        this.typeIsolationPlancher=model.getTypeIsolationPlancher();
        this.setPlafondMaterialType(model.getPlafondMaterialType());
        this.setPlancherMaterialType(model.getPlancherMaterialType());
        plafond = new Plafond(this);
        this.add(plafond);

        this.setEtage(model.getEtage());
    }

    public void actualiseSurface(){
        if (coins == null || coins.isEmpty()) {
            surface = 0;
            return;
        }
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

    public void createCorrespondantPolygon(){
        float vertices[] = new float[coins.size()*2];
        for (int i=0,j=0; j<coins.size();i+=2,j++){
            vertices[i]=coins.get(j).getPosition().x;
            vertices[i+1]=coins.get(j).getPosition().y;
        }
        GeometryUtils.ensureCCW(vertices);
        this.polygon=new Polygon(vertices);
    }

    public Polygon getPolygon(){
        return this.polygon;
    }

    public void setEtage(Etage e) {
        etage = e;
    }

    public Etage getEtage() {
        return etage;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
        setChanged();
    }

    public void addMur(Mur m) {
        murs.add(m);
    }

    public void removeMur(Mur m) {
        murs.remove(m);
    }

    public ArrayList<Mur> getMurs() {
        return murs;
    }

    public void addObjet(Objet o) {
        objets.add(o);
        if (etage != null)
            etage.addObjet(o);
    }

    public void removeObjet(Objet o) {
        objets.remove(o);
        if (etage != null)
            etage.removeObjet(o);
    }

    public ArrayList<Objet> getObjets() {
        return objets;
    }

    public void setCoins(List<Coin> coins) {
        if (this.coins != null)
            for (Coin coin : this.coins)
                coin.removeSlab(this);
        this.coins = coins;
        if (this.coins != null)
            for (Coin coin : this.coins)
                coin.addSlab(this);
        setChanged();
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public void setPlafondMaterialType(String mat) {
        plafondType = mat;
        areMaterialSet = false;
    }

    public void setPlancherMaterialType(String mat) {
        plancherType = mat;
        areMaterialSet = false;
    }

    private void makeMaterials() {
        setMaterial(plafondMaterial, plafondType);
        setMaterial(plancherMaterial, plancherType);
        areMaterialSet = true;
        setChanged();
    }

    private void makeMesh() {
        if (coins == null) {
            this.setModel(new Model());
            valide = false;
            return;
        }

        actualiseSurface();
        if (getSurface() == 0) {
            this.setModel(new Model());
            valide = false;
            return;
        }

        Vector3 z_shape = Vector3.Z.cpy().scl(this.height/2);

        Vector3d z = CSGUtils.castVector(z_shape);

        List<Vector3d> face = new ArrayList<Vector3d>();
        for (Coin c : coins) {
            face.add(CSGUtils.castVector(new MyVector3(c.getPosition())));
        }

        try {

            CSG csg = Extrude.points(z, face);

            Model model = CSGUtils.toModel(csg,plafondMaterial, plafondMaterial, plancherMaterial, plafondMaterial, plafondMaterial);
            this.setModel(model);
            valide = true;

        } catch (RuntimeException re) {
            valide = false;
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
        if (!areMaterialSet)
            makeMaterials();
        if (!changed)
            return;
        actualiseSurface();
        makeMesh();
        changed = false;
    }

    public void setChanged() {
        changed = true;
        plafond.setChanged();
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
        Event e = new Event(DpeEvent.DEPERDITION_PLANCHERS_CHANGED, null);
        EventManager.getInstance().put(Channel.DPE, e);
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
        Event e = new Event(DpeEvent.DEPERDITION_TOITS_CHANGED, null);
        EventManager.getInstance().put(Channel.DPE, e);
    }

    public float getDeperditionPlafond() {
        return deperditionPlafond;
    }

    public float getDeperditionPlancher() {
        return deperditionPlancher;
    }

    public void remplaceCoin(Coin last, Coin next) {
        int i = coins.indexOf(last);
        if (i == -1) return;
        coins.set(i, next);
        last.removeSlab(this);
        next.addSlab(this);
        this.setChanged();
    }

    @Override
    public String toString(){
        return "Slab ->  s="+surface+" uPlancher="+uPlancher+ " dpPlancher="+deperditionPlancher+" uPlafond="+uPlafond+ " deperditionPlafond="+deperditionPlafond+" mitoyennetePlancher="+mitoyennetePlancher+" mitoyennetePlafond="+mitoyennetePlafond;
    }

    public Vector2 getCenter() {
        if (coins == null || coins.isEmpty())
            return new Vector2();
        Vector2 pos = new Vector2();
        for (Coin coin : coins) {
            pos.add(coin.getPosition());
        }
        pos.scl(1.0f/coins.size());
        return pos;
    }

    @Override
    public Vector3 getCotePos() {
        Vector2 pos = getCenter();
        return new Vector3(pos.x, pos.y, etage.getHeight() + 0.0001f);
    }

    @Override
    public float getCoteValue() {
        return surface;
    }

    @Override
    public Vector3 getCotePosVerticalA() { // en bas a droite
        if (coins == null || coins.isEmpty())
            return new Vector3();
        Vector2 pos = new Vector2(-Float.MAX_VALUE, Float.MAX_VALUE);
        for (Coin coin : coins) {
            Vector2 p = coin.getPosition();
            if (p.x >= pos.x && p.y <= pos.y)
                pos = p;
        }
        return new Vector3(pos.x, pos.y, etage.getHeight() + 0.0001f);
    }

    @Override
    public Vector3 getCotePosVerticalB() { // en haut a droite
        if (coins == null || coins.isEmpty())
            return new Vector3();
        Vector2 pos = new Vector2(-Float.MAX_VALUE, -Float.MAX_VALUE);
        for (Coin coin : coins) {
            Vector2 p = coin.getPosition();
            if (p.x >= pos.x && p.y >= pos.y)
                pos = p;
        }
        return new Vector3(pos.x, pos.y, etage.getHeight() + 0.0001f);
    }

    @Override
    public Vector3 getCotePosHorizontalA() { // en bas a gauche
        if (coins == null || coins.isEmpty())
            return new Vector3();
        Vector2 pos = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
        for (Coin coin : coins) {
            Vector2 p = coin.getPosition();
            if (p.x <= pos.x && p.y <= pos.y)
                pos = p;
        }
        return new Vector3(pos.x, pos.y, etage.getHeight() + 0.0001f);
    }

    @Override
    public Vector3 getCotePosHorizontalB() { // en bas a droite
        if (coins == null || coins.isEmpty())
            return new Vector3();
        Vector2 pos = new Vector2(-Float.MAX_VALUE, Float.MAX_VALUE);
        for (Coin coin : coins) {
            Vector2 p = coin.getPosition();
            if (p.x >= pos.x && p.y <= pos.y)
                pos = p;
        }
        return new Vector3(pos.x, pos.y, etage.getHeight() + 0.0001f);
    }

    @Override
    public float getCoteValueVertical() {
        return getCotePosVerticalB().cpy().sub(getCotePosVerticalA()).len();
    }

    @Override
    public float getCoteValueHorizontal() {
        return getCotePosHorizontalA().cpy().sub(getCotePosHorizontalB()).len();
    }

    public String getPlancherMaterialType() {
        return plancherType;
    }

    public String getPlafondMaterialType() {
        return plafondType;
    }

    public Material getPlafondMaterial() {
        return plafondMaterial;
    }

    private void calculateVolume(){
        volume = this.etage.getHeight()*this.getSurface();
    }

    public float getVolume(){
        this.calculateVolume();
        return this.volume;
    }
}
