package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import ifc2x3javatoolbox.ifc2x3tc1.IfcDoor;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProduct;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSlab;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWall;

/**
 * Created by christophe on 30/03/15.
 */
public class ModelFactoryStrategy {

    public static Model getModel(IfcProduct ifcProduct) {
        if (ifcProduct instanceof IfcWall) {
            System.out.println("WALL !");
            return new IfcWallModelFactory(ifcProduct).getModel();
        } else if (ifcProduct instanceof IfcSlab) {
            System.out.println("SLAB !");
            return new IfcSlabModelFactory(ifcProduct).getModel();
        } else if (ifcProduct instanceof IfcDoor) {
            System.out.println("DOOR!");
            //return new IfcDoorModelFactory(ifcProduct).getModel();
        }
        return new Model();
    }
}
