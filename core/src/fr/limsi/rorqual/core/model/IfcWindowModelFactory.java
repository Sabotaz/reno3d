package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

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
    private ModelInstance modelWindow;
    private AssetManager assets;

    /*** Constructeur ***/
    public IfcWindowModelFactory(IfcProduct ifcProduct) {
        if (ifcProduct instanceof IfcWindow) {
            this.assets = AssetManager.getInstance();
            this.window = (IfcWindow) ifcProduct;
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
        modelWindow = new ModelInstance((Model)assets.get("modelWindow"));
//        modelInstanceDoor.transform.setTranslation(1.0f,1.0f,1.0f);
//        modelWindow = (Model)assets.get("modelWindow");
    }

    private void setDoorPosition(){

    }

    private void setDoorSize(){

    }

    private void setDoorOrientation(){

    }

    public ModelInstance getModel(){
        return modelWindow;
    }

}
