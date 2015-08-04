package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.collision.BoundingBox;

import fr.limsi.rorqual.core.utils.AssetManager;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDoor;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProduct;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWindow;

/**
 * Created by ricordeau on 25/06/15.
 */
public class IfcWindowModelFactory {


    /*** Attributs ***/
    private IfcWindow window;
    private ModelInstance modelInstanceWindow;
    private AssetManager assets;
    private IfcHelper ifcHelper;

    /*** Constructeur ***/
    public IfcWindowModelFactory(IfcProduct ifcProduct) {
        if (ifcProduct instanceof IfcWindow) {
            this.assets = AssetManager.getInstance();
            this.window = (IfcWindow) ifcProduct;
            this.ifcHelper = new IfcHelper(IfcHolder.getInstance().getIfcModel());
            this.make();
        }
    }

    /*** Méthodes ***/
    private void make() {
        Model m = (Model)assets.get("modelWindow");
        modelInstanceWindow = new ModelInstance((Model)assets.get("modelWindow"));
        BoundingBox b = new BoundingBox();
        m.calculateBoundingBox(b);
        float d = (float)ifcHelper.getWindowDepth(window) / b.getHeight();
        float w = (float)ifcHelper.getWindowWidth(window) / b.getWidth();
        float h = (float)ifcHelper.getWindowHeight(window) / b.getDepth();
        System.out.println("Depth "+ifcHelper.getWindowDepth(window)+" Width "+ifcHelper.getWindowWidth(window)+" Height "+ifcHelper.getWindowHeight(window));
//        modelInstanceWindow.local_transform.rotate(0, 1, 0, 90);
        modelInstanceWindow.transform.scale(w, d, h);
    }

    public ModelInstance getModel(){
        return modelInstanceWindow;
    }

}
