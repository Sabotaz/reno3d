package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;


import ifc2x3javatoolbox.ifc2x3tc1.IfcDoor;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProduct;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSlab;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWall;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWindow;

/**
 * Created by christophe on 30/03/15.
 */
public class ModelFactoryStrategy {

    public static ModelInstance getModel(IfcProduct ifcProduct) {
        if (ifcProduct instanceof IfcWall) {
            return new IfcWallModelFactory(ifcProduct).getModel();
        } else if (ifcProduct instanceof IfcSlab) {
            return new IfcSlabModelFactory(ifcProduct).getModel();
        } else if (ifcProduct instanceof IfcDoor) {
            return new IfcDoorModelFactory(ifcProduct).getModel();
        } else if (ifcProduct instanceof IfcWindow) {
            return new IfcWindowModelFactory(ifcProduct).getModel();
        }
        return new ModelInstance(new Model());
    }
}
