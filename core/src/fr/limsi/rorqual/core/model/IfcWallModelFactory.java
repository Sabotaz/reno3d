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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import javax.lang.model.type.PrimitiveType;

import fr.limsi.rorqual.core.utils.IfcObjectPlacementUtils;
import ifc2x3javatoolbox.ifc2x3tc1.ENUM;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCircle;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDirectionSenseEnum;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLayerSetDirectionEnum;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLengthMeasure;
import ifc2x3javatoolbox.ifc2x3tc1.IfcMaterialLayer;
import ifc2x3javatoolbox.ifc2x3tc1.IfcMaterialLayerSet;
import ifc2x3javatoolbox.ifc2x3tc1.IfcMaterialLayerSetUsage;
import ifc2x3javatoolbox.ifc2x3tc1.IfcMaterialSelect;
import ifc2x3javatoolbox.ifc2x3tc1.IfcPolyline;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProduct;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelAssociates;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelAssociatesMaterial;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRepresentation;
import ifc2x3javatoolbox.ifc2x3tc1.IfcShapeRepresentation;
import ifc2x3javatoolbox.ifc2x3tc1.IfcTrimmedCurve;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWall;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifc2x3tc1.LIST;
import ifc2x3javatoolbox.ifc2x3tc1.SET;

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
                // MaterialLayers
                ArrayList<Float> materialLayersThickness = new ArrayList<Float>();
                float offset = 0;
                int direction = 1;

                for (IfcRelAssociates association : wall.getHasAssociations_Inverse()) {
                    System.out.println(association);
                    if (association instanceof IfcRelAssociatesMaterial) {
                        IfcMaterialSelect materialSelect = ((IfcRelAssociatesMaterial) association).getRelatingMaterial();
                        if (materialSelect instanceof IfcMaterialLayerSetUsage) {
                            IfcMaterialLayerSetUsage layerSetUsage = (IfcMaterialLayerSetUsage) materialSelect;
                            IfcMaterialLayerSet layerSet = layerSetUsage.getForLayerSet();
                            IfcDirectionSenseEnum directionSense = layerSetUsage.getDirectionSense();
                            IfcLayerSetDirectionEnum layerSetDirection = layerSetUsage.getLayerSetDirection();
                            IfcLengthMeasure offset_measure = layerSetUsage.getOffsetFromReferenceLine();
                            offset = (float)offset_measure.value;
                            LIST<IfcMaterialLayer> layers = layerSet.getMaterialLayers();

                            for (IfcMaterialLayer layer : layers)
                                materialLayersThickness.add((float)layer.getLayerThickness().value);

                            if (layerSetDirection.value.name().equals("AXIS2")) {
                                // OK
                                if (directionSense.value.name().equals("POSITIVE")) {
                                    direction = 1;
                                } else { // NEGATIVE
                                    direction = -1;
                                }
                            }
                        }
                        break; //Fixme can we have multiple IfcRelAssociatesMaterial ?
                    }
                }
                // Shape
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



                            Vector3 XY = IfcObjectPlacementUtils.getAxis2(wall.getObjectPlacement());
                            Vector3 Z = IfcObjectPlacementUtils.getAxis3(wall.getObjectPlacement());

                            Vector3 start = IfcObjectPlacementUtils.toVector(p1);
                            Vector3 end = IfcObjectPlacementUtils.toVector(p2);

                            builder.begin();

                            Node node = builder.node();
                            node.id = "base";

                            MeshPartBuilder meshBuilder;

                            meshBuilder = builder.part("part1", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED)));
                            meshBuilder.line(start, end);

                            Vector3 layerOffset = XY.cpy().scl(offset);

                            for (int i = 0; i < materialLayersThickness.size(); i++) {
                                node = builder.node();
                                node.id = "layer"+i;

                                float thickness = materialLayersThickness.get(i);

                                meshBuilder = builder.part("part"+i, GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material());
                                //meshBuilder.line(start, end);
                                Vector3 corner00 = start.cpy().add(layerOffset);
                                Vector3 corner01 = start.cpy().add(layerOffset).add(XY.cpy().scl(direction * thickness));
                                Vector3 corner10 = end.cpy().add(layerOffset).add(XY.cpy().scl(direction * thickness));
                                Vector3 corner11 = end.cpy().add(layerOffset);
                                meshBuilder.rect(corner00, corner01, corner10, corner11, Z);
                                layerOffset.add(XY.cpy().scl(direction * thickness));
                            }

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
                    } else { // not an IfcPolyline or an IfcTrimedCurve
                        System.out.println("ASSERT TYPE FALSE"); //Todo throw exception
                    }
                } else { // no items
                    System.out.println("ASSERT TYPE FALSE"); //Todo throw exception
                }

                System.out.println(axis.getItems());

            } else { // IfcWall
                System.out.println("Not a standard case wall");

            }
            return builder.createBox(10f, 10f, 10f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), VertexAttributes.Usage.Position);
        }
        return new Model();
    }
}
