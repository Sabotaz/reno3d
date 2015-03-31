package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import java.util.ArrayList;

import javax.lang.model.type.PrimitiveType;

import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCircle;
import ifc2x3javatoolbox.ifc2x3tc1.IfcPolyline;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProduct;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRepresentation;
import ifc2x3javatoolbox.ifc2x3tc1.IfcShapeRepresentation;
import ifc2x3javatoolbox.ifc2x3tc1.IfcTrimmedCurve;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWall;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;

/**
 * Created by christophe on 31/03/15.
 */
public class IfcWallModelFactory {

    private static ModelBuilder builder = new ModelBuilder();
    private static ShapeRenderer shapeRenderer = new ShapeRenderer();

    public static Model getModel(IfcProduct ifcProduct) {
        if (ifcProduct instanceof IfcWall) {
            IfcWall wall = (IfcWall) ifcProduct;
            if (wall instanceof IfcWallStandardCase) {
                IfcShapeRepresentation axis = null;
                IfcShapeRepresentation body = null;
                for (IfcRepresentation ifcRepresentation : wall.getRepresentation().getRepresentations()) {
                    if (ifcRepresentation instanceof IfcShapeRepresentation) {
                        IfcShapeRepresentation ifcShapeRepresentation = (IfcShapeRepresentation) ifcRepresentation;
                        if (ifcShapeRepresentation.getRepresentationIdentifier().getDecodedValue().equals("Axis"))
                            axis = ifcShapeRepresentation;
                        else if (ifcShapeRepresentation.getRepresentationIdentifier().getDecodedValue().equals("Body"))
                            body = ifcShapeRepresentation;
                    }
                }

                if (!axis.getRepresentationType().getDecodedValue().equals("Curve2D"))
                    System.out.println("ASSERT TYPE FALSE"); //Todo throw exception

                Object[] items = axis.getItems().toArray();
                if (items.length > 0) {
                    if (items[0] instanceof IfcPolyline) {
                        IfcPolyline polyline = (IfcPolyline) items[0];
                        if (polyline.getPoints().size() != 2) {
                            System.out.println("ASSERT TYPE FALSE"); //Todo throw exception
                        } else {
                            IfcCartesianPoint p1 = polyline.getPoints().get(0);
                            IfcCartesianPoint p2 = polyline.getPoints().get(1);
                            float x1 = 0, y1 = 0, z1 = 0, x2 = 0, y2 = 0, z2 = 0;

                            x1 = (float)p1.getCoordinates().get(0).value;
                            y1 = (float)p1.getCoordinates().get(1).value;
                            if (p1.getCoordinates().size() == 3)
                                z1 = (float)p1.getCoordinates().get(2).value;

                            x2 = (float)p2.getCoordinates().get(0).value;
                            y2 = (float)p2.getCoordinates().get(1).value;
                            if (p1.getCoordinates().size() == 3)
                                z2 = (float)p2.getCoordinates().get(2).value;

                            builder.begin();

                            Node node1 = builder.node();
                            node1.id = "wall1";

                            MeshPartBuilder meshBuilder;

                            meshBuilder = builder.part("part1", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material());
                            meshBuilder.line(x1,y1,z1,x2,y2,z2);

                            return builder.end();
                        }
                    } else if (items[0] instanceof IfcTrimmedCurve) {
                        IfcTrimmedCurve ifcTrimmedCurve = (IfcTrimmedCurve) items[0];
                        if (ifcTrimmedCurve.getBasisCurve() instanceof IfcCircle)
                            System.out.println("ASSERT TYPE FALSE"); //Todo throw exception
                        else {
                            //Todo
                            //http://www.buildingsmart-tech.org/ifc/IFC2x3/TC1/html/ifcgeometryresource/lexical/ifctrimmedcurve.htm
                            //http://www.buildingsmart-tech.org/ifc/IFC2x3/TC1/html/ifcgeometryresource/lexical/ifccircle.htm
                        }
                    } else {
                        System.out.println("ASSERT TYPE FALSE"); //Todo throw exception
                    }
                } else {
                    System.out.println("ASSERT TYPE FALSE"); //Todo throw exception
                }

                System.out.println(axis.getItems());

            } else {
                System.out.println("Not a standard case wall");

            }
            return builder.createBox(10f, 10f, 10f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), VertexAttributes.Usage.Position);
        }
        return new Model();
    }
}
