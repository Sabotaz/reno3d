package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Extrude;
import eu.mihosoft.vrl.v3d.Vector3d;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.DateIsolationMurEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.OrientationMurEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.TypeIsolationMurEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.TypeMurEnum;

import java.util.ArrayList;
import java.util.List;

import fr.limsi.rorqual.core.model.primitives.MaterialTypeEnum;
import fr.limsi.rorqual.core.model.utils.Coin;
import fr.limsi.rorqual.core.model.utils.MyVector2;
import fr.limsi.rorqual.core.utils.CSGUtils;
import fr.limsi.rorqual.core.utils.scene3d.ActableModel;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;

/**
 * Created by ricordeau on 20/07/15.
 */
public class Mur extends ModelContainer {

    private static float DEFAULT_DEPTH = 0.2f;
    private static float DEFAULT_HEIGHT = 2.8f;

    private Vector3 A = new Vector3();
    private Vector3 B = new Vector3();
    private float height;
    private float width;
    private float depth;
    private TypeMurEnum typeMurEnum = TypeMurEnum.INCONNUE;
    private TypeIsolationMurEnum typeIsolationMur = TypeIsolationMurEnum.NON_ISOLE;
    private DateIsolationMurEnum dateIsolationMur = DateIsolationMurEnum.JAMAIS;
    private OrientationMurEnum orientationMur = OrientationMurEnum.INCONNUE;

    private ArrayList<MaterialTypeEnum> materialLayersMaterials = new ArrayList<MaterialTypeEnum>();

    private ArrayList<Coin> coins = new ArrayList<Coin>();

    private ArrayList<Ouverture> ouvertures = new ArrayList<Ouverture>();

    private Etage etage = null;

    private boolean changed = true;

    public Mur(Mur other) {
        this(other.getA().cpy(), other.getB().cpy(), other.getDepth(), other.getHeight());
    }

    public Mur(Vector3 a, Vector3 b) {
        this(a, b, DEFAULT_DEPTH, DEFAULT_HEIGHT);
    }

    public Mur(Vector3 a, Vector3 b, float d) {
        this(a, b, d, DEFAULT_HEIGHT);
    }

    public Mur(Vector3 a, Vector3 b, float d, float h) {
        super();
        this.A = new Vector3(a);
        this.B = new Vector3(b);
        this.height = h;
        this.depth = d;
        this.width = b.cpy().sub(a).len();
    }

    public void setEtage(Etage e) {
        etage = e;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
        changed = true;
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
        changed = true;
    }

    public Vector3 getA() {
        return A;
    }

    public void setA(Vector3 a) {
        A.set(a);
        this.width = B.cpy().sub(A).len();
        changed = true;
    }

    public Vector3 getB() {
        return B;
    }

    public void setB(Vector3 b) {
        B.set(b);
        this.width = B.cpy().sub(A).len();
        changed = true;
    }

    public TypeMurEnum getTypeMurEnum() {
        return typeMurEnum;
    }

    public void setTypeMurEnum(TypeMurEnum typeMurEnum) {
        this.typeMurEnum = typeMurEnum;
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

    public OrientationMurEnum getOrientationMur() {
        return orientationMur;
    }

    public void setOrientationMur(OrientationMurEnum orientationMur) {
        this.orientationMur = orientationMur;
    }

    public ArrayList<Vector3> getAnchors(Vector3 pt, float depth) {
        ArrayList<Vector3> anchors = new ArrayList<Vector3>();
        anchors.add(A);
        anchors.add(B);

        Vector3 x_dir = B.cpy().sub(A).setLength(depth/2);
        Vector3 y_dir = x_dir.cpy().crs(Vector3.Z).setLength(this.depth/2);

        // px : +x_dir      mx : -x_dir
        // py : +y_dir      my : -y_dir
        Vector3 a_px_py = A.cpy().add(x_dir).add(y_dir);
        Vector3 a_mx_py = A.cpy().sub(x_dir).add(y_dir);
        Vector3 a_px_my = A.cpy().add(x_dir).sub(y_dir);
        Vector3 a_mx_my = A.cpy().sub(x_dir).sub(y_dir);
        anchors.add(a_px_py);
        anchors.add(a_mx_py);
        anchors.add(a_px_my);
        anchors.add(a_mx_my);

        Vector3 b_px_py = B.cpy().add(x_dir).add(y_dir);
        Vector3 b_mx_py = B.cpy().sub(x_dir).add(y_dir);
        Vector3 b_px_my = B.cpy().add(x_dir).sub(y_dir);
        Vector3 b_mx_my = B.cpy().sub(x_dir).sub(y_dir);
        anchors.add(b_px_py);
        anchors.add(b_mx_py);
        anchors.add(b_px_my);
        anchors.add(b_mx_my);

        Vector2 intersection = new Vector2();
        Vector2 pt1 = new MyVector2(pt);
        Vector2 pt2 = new MyVector2(pt.cpy().add(y_dir));
        // intersect +y_dir
        Vector2 coin1 = new MyVector2(A.cpy().add(y_dir));
        Vector2 coin2 = new MyVector2(A.cpy().sub(y_dir));
        if (Intersector.intersectLines(pt1, pt2, coin1, coin2, intersection))
            anchors.add(new Vector3(intersection.x, intersection.y, 0));
        // intersect -y_dir
        coin1 = new MyVector2(B.cpy().add(y_dir));
        coin2 = new MyVector2(B.cpy().sub(y_dir));
        if (Intersector.intersectLines(pt2, pt2, coin1, coin2, intersection))
            anchors.add(new Vector3(intersection.x, intersection.y, 0));

        return anchors;
    }

    private void makeMesh() {
        if (B.equals(A))
            return;
        Vector3 z_shape = Vector3.Z.cpy().scl(this.height);
        Vector3 y_dir = B.cpy().sub(A).crs(Vector3.Z).setLength(this.depth / 2);

        Vector3d dir = CSGUtils.castVector(z_shape);

        List<Vector3d> face = new ArrayList<Vector3d>();
        face.add(CSGUtils.castVector(B.cpy().add(y_dir)));
        face.add(CSGUtils.castVector(B.cpy().sub(y_dir)));
        face.add(CSGUtils.castVector(A.cpy().sub(y_dir)));
        face.add(CSGUtils.castVector(A.cpy().add(y_dir)));

        CSG csg = Extrude.points(dir, face);

        for (Ouverture o : ouvertures) {
            csg = csg.difference(o.getCSG());
        }

        Material frontMaterial = new Material();
        Material backMaterial = new Material();

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


        Model model = CSGUtils.toModel(csg, frontMaterial, backMaterial);

        this.setModel(model);
    }

    public void act() {
        if (!changed)
            return;
        makeMesh();
        changed = false;
    }

    public void addOuverture(Ouverture o) {
        ouvertures.add(o);
        this.add(o);
        etage.addOuverture(o);
        changed = true;
    }
}
