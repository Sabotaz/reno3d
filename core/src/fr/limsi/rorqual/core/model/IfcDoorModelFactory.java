package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
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
    private Model modelInstanceDoor;
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
//        modelInstanceDoor = new ModelInstance((Model)assets.get("modelDoor"));
//        modelInstanceDoor.transform.setTranslation(1.0f,1.0f,1.0f);
        modelInstanceDoor = (Model)assets.get("modelDoor");
    }

    private void setDoorPosition(){

    }

    private void setDoorSize(){

    }

    private void setDoorOrientation(){

    }

    public Model getModel(){
        return modelInstanceDoor;
    }
}
