package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import ifc2x3javatoolbox.ifc2x3tc1.IfcDoor;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProduct;
import fr.limsi.rorqual.core.utils.AssetManager;

/**
 * Created by ricordeau on 25/06/15.
 */
public class IfcDoorModelFactory {

    /*** Attributs ***/
    private IfcDoor door;
    private ModelInstance modelInstanceDoor;
    private AssetManager assets;

    /*** Constructeur ***/
    public IfcDoorModelFactory(IfcProduct ifcProduct) {
        if (ifcProduct instanceof IfcDoor) {
            this.assets = AssetManager.getInstance();
            this.door = (IfcDoor) ifcProduct;
            this.make();
        }
    }

    /*** Méthodes ***/
    private void make() {
        loadDoorModel();
        setDoorPosition();
        setDoorSize();
        setDoorOrientation();
    }

    private void loadDoorModel(){
        Model m = (Model)assets.get("modelDoor");
        modelInstanceDoor = new ModelInstance((Model)assets.get("modelDoor"));
        BoundingBox b = new BoundingBox();
        m.calculateBoundingBox(b);
        float d = (float)IfcHelper.getDoorDepth(door) / b.getHeight();
        float w = (float)IfcHelper.getDoorWidth(door) / b.getWidth();
        float h = (float)IfcHelper.getDoorHeight(door) / b.getDepth();
        modelInstanceDoor.transform.scl(w, d, h);
        modelInstanceDoor.transform.rotate(0, 0, 1, 180);
        //modelInstanceDoor.transform.setTranslation(1.0f,1.0f,1.0f);
        //modelInstanceDoor = (Model)assets.get("modelDoor");
    }

    private void setDoorPosition(){

    }

    private void setDoorSize(){

    }

    private void setDoorOrientation(){

    }

    public ModelInstance getModel(){
        return modelInstanceDoor;
    }
}
