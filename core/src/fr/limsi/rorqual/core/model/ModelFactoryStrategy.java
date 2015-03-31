package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import ifc2x3javatoolbox.ifc2x3tc1.IfcProduct;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWall;

/**
 * Created by christophe on 30/03/15.
 */
public class ModelFactoryStrategy {

    public static Model getModel(IfcProduct ifcProduct) {
        if (ifcProduct instanceof IfcWall) {
            System.out.println("WALL !");
            return IfcWallModelFactory.getModel(ifcProduct);
        }
        return new Model();
    }

}
