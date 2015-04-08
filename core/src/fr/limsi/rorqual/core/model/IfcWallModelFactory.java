package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.type.PrimitiveType;

import fr.limsi.rorqual.core.utils.IfcObjectPlacementUtils;
import ifc2x3javatoolbox.ifc2x3tc1.BOOLEAN;
import ifc2x3javatoolbox.ifc2x3tc1.ENUM;
import ifc2x3javatoolbox.ifc2x3tc1.IfcArbitraryClosedProfileDef;
import ifc2x3javatoolbox.ifc2x3tc1.IfcAxis2Placement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcAxis2Placement3D;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCircle;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCurve;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDirection;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDirectionSenseEnum;
import ifc2x3javatoolbox.ifc2x3tc1.IfcExtrudedAreaSolid;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLayerSetDirectionEnum;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLengthMeasure;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLine;
import ifc2x3javatoolbox.ifc2x3tc1.IfcMaterialLayer;
import ifc2x3javatoolbox.ifc2x3tc1.IfcMaterialLayerSet;
import ifc2x3javatoolbox.ifc2x3tc1.IfcMaterialLayerSetUsage;
import ifc2x3javatoolbox.ifc2x3tc1.IfcMaterialSelect;
import ifc2x3javatoolbox.ifc2x3tc1.IfcParameterValue;
import ifc2x3javatoolbox.ifc2x3tc1.IfcPolyline;
import ifc2x3javatoolbox.ifc2x3tc1.IfcPositiveLengthMeasure;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProduct;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProfileDef;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelAssociates;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelAssociatesMaterial;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRepresentation;
import ifc2x3javatoolbox.ifc2x3tc1.IfcShapeRepresentation;
import ifc2x3javatoolbox.ifc2x3tc1.IfcTrimmedCurve;
import ifc2x3javatoolbox.ifc2x3tc1.IfcTrimmingSelect;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWall;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifc2x3tc1.LIST;
import ifc2x3javatoolbox.ifc2x3tc1.SET;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by christophe on 31/03/15.
 */
public class IfcWallModelFactory {

    private static ModelBuilder builder = new ModelBuilder();
    private static ShapeRenderer shapeRenderer = new ShapeRenderer();

    IfcWall wall = null;

    public IfcWallModelFactory(IfcProduct ifcProduct) {
        if (ifcProduct instanceof IfcWall) {
            this.wall = (IfcWall) ifcProduct;
            this.make();
        }
    }

    private void make() {
        this.setLayers();
        this.setRepresentations();
        this.setAxis();
        this.setBody();

    }

    private ArrayList<Float> materialLayersThickness = new ArrayList<Float>();
    private float offset = 0;
    private int direction = 1;

    private void setLayers() {
        // MaterialLayers
        if (wall.getHasAssociations_Inverse() == null)
            return;
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
    }

    private IfcShapeRepresentation axis = null;
    private IfcShapeRepresentation body = null;
    private void setRepresentations() {
        for (IfcRepresentation ifcRepresentation : wall.getRepresentation().getRepresentations()) {
            if (ifcRepresentation instanceof IfcShapeRepresentation) {
                IfcShapeRepresentation ifcShapeRepresentation = (IfcShapeRepresentation) ifcRepresentation;
                if (ifcShapeRepresentation.getRepresentationIdentifier().getDecodedValue().equals("Axis"))
                    axis = ifcShapeRepresentation;
                else if (ifcShapeRepresentation.getRepresentationIdentifier().getDecodedValue().equals("Body"))
                    body = ifcShapeRepresentation;
            }
        }
    }


    private Vector3 XY;
    private Vector3 Z;

    private Vector3 start;
    private Vector3 end;

    private boolean is_circle = false;
    private Matrix4 circle_pos;
    private float circle_radius;
    private float angle_from;
    private float angle_to;

    private void setAxis() {

        if (!axis.getRepresentationType().getDecodedValue().equals("Curve2D"))
            System.out.println("ASSERT TYPE FALSE"); //Todo throw exception

        XY = IfcObjectPlacementUtils.getAxis2(wall.getObjectPlacement());
        Z = IfcObjectPlacementUtils.getAxis3(wall.getObjectPlacement());

        Object[] items = axis.getItems().toArray();
        if (items.length > 0) {
            if (items[0] instanceof IfcPolyline) {
                IfcPolyline polyline = (IfcPolyline) items[0];
                if (polyline.getPoints().size() != 2) {
                    System.out.println("ASSERT TYPE FALSE"); //Todo throw exception
                } else {
                    IfcCartesianPoint p1 = polyline.getPoints().get(0);
                    IfcCartesianPoint p2 = polyline.getPoints().get(1);

                    start = IfcObjectPlacementUtils.toVector(p1);
                    end = IfcObjectPlacementUtils.toVector(p2);
                }
            } else if (items[0] instanceof IfcTrimmedCurve) {
                IfcTrimmedCurve ifcTrimmedCurve = (IfcTrimmedCurve) items[0];
                if (ifcTrimmedCurve.getBasisCurve() instanceof IfcCircle) {
                    is_circle = true;
                    //Todo
                    //http://www.buildingsmart-tech.org/ifc/IFC2x3/TC1/html/ifcgeometryresource/lexical/ifctrimmedcurve.htm
                    //http://www.buildingsmart-tech.org/ifc/IFC2x3/TC1/html/ifcgeometryresource/lexical/ifccircle.htm

                    IfcCircle circle = (IfcCircle) ifcTrimmedCurve.getBasisCurve();
                    circle_pos = IfcObjectPlacementUtils.toMatrix(circle.getPosition());
                    circle_radius = (float)circle.getRadius().value;
                    boolean sense = ifcTrimmedCurve.getSenseAgreement().value;

                    IfcCartesianPoint CPtrim1 = null, CPtrim2 = null;
                    IfcParameterValue PVtrim1 = null, PVtrim2 = null;

                    for (IfcTrimmingSelect t : ifcTrimmedCurve.getTrim1()) {
                        if (t instanceof IfcCartesianPoint)
                            CPtrim1 = (IfcCartesianPoint) t;
                        else if (t instanceof IfcParameterValue)
                            PVtrim1 = (IfcParameterValue) t;
                    }
                    for (IfcTrimmingSelect t : ifcTrimmedCurve.getTrim2()) {
                        if (t instanceof IfcCartesianPoint)
                            CPtrim2 = (IfcCartesianPoint) t;
                        else if (t instanceof IfcParameterValue)
                            PVtrim2 = (IfcParameterValue) t;
                    }


                    if (PVtrim1 == null || PVtrim2 == null)
                        throw new NotImplementedException();
                    if (sense) {
                        angle_from = (float)(PVtrim1.value * Math.PI / 180.);
                        angle_to = (float)(PVtrim2.value * Math.PI / 180.);
                    } else {
                        angle_from = (float)(PVtrim2.value * Math.PI / 180.);
                        angle_to = (float)(PVtrim1.value * Math.PI / 180.);
                    }
                    while (angle_to < angle_from)
                        angle_to += 2 * Math.PI;
                    System.out.println("angles: " + angle_from + "," + angle_to);

                }
                else if (ifcTrimmedCurve.getBasisCurve() instanceof IfcLine) {
                    IfcLine line = (IfcLine) ifcTrimmedCurve.getBasisCurve();
                    Vector3 pt = IfcObjectPlacementUtils.toVector(line.getPnt());
                    Vector3 d = IfcObjectPlacementUtils.toVector(line.getDir());
                    IfcCartesianPoint CPtrim1 = null, CPtrim2 = null;
                    IfcParameterValue PVtrim1 = null, PVtrim2 = null;

                    System.out.println("sense: "+ifcTrimmedCurve.getSenseAgreement());

                    for (IfcTrimmingSelect t : ifcTrimmedCurve.getTrim1()) {
                        if (t instanceof IfcCartesianPoint)
                            CPtrim1 = (IfcCartesianPoint) t;
                        else if (t instanceof IfcParameterValue)
                            PVtrim1 = (IfcParameterValue) t;
                    }
                    for (IfcTrimmingSelect t : ifcTrimmedCurve.getTrim2()) {
                        if (t instanceof IfcCartesianPoint)
                            CPtrim2 = (IfcCartesianPoint) t;
                        else if (t instanceof IfcParameterValue)
                            PVtrim2 = (IfcParameterValue) t;
                    }

                    if (CPtrim1 != null)
                        start = IfcObjectPlacementUtils.toVector(CPtrim1);
                    else if (PVtrim1 != null)
                        start = pt.cpy().add(d.cpy().scl((float)PVtrim1.value));
                    else
                        start = new Vector3();

                    if (CPtrim2 != null)
                        end = IfcObjectPlacementUtils.toVector(CPtrim2);
                    else if (PVtrim2 != null)
                        end = pt.cpy().add(d.cpy().scl((float)PVtrim2.value));
                    else
                        end = new Vector3();

                } else {
                    System.out.println("ASSERT TYPE FALSE"); //Todo throw exception
                }
            } else { // not an IfcPolyline or an IfcTrimedCurve
                System.out.println("ASSERT TYPE FALSE"); //Todo throw exception
            }
        } else { // no items
            System.out.println("ASSERT TYPE FALSE"); //Todo throw exception
        }
    }

    private Vector3 dir;
    private float depth;
    private List<Vector3> sweptarea = new ArrayList<Vector3>();
    private Matrix4 body_placement;

    private void setBody() {

        if (!body.getRepresentationType().getDecodedValue().equals("SweptSolid"))
            System.out.println("ASSERT TYPE FALSE"); //Todo throw exception

        Object[] items = body.getItems().toArray();
        System.out.println("body: " + body);
        if (items.length > 0) {
            System.out.println("item: " + items[0]);
            if (items[0] instanceof IfcExtrudedAreaSolid) {
                IfcExtrudedAreaSolid extrudedAreaSolid = (IfcExtrudedAreaSolid) items[0];
                body_placement = IfcObjectPlacementUtils.toMatrix(extrudedAreaSolid.getPosition());
                IfcProfileDef profile = extrudedAreaSolid.getSweptArea();
                System.out.println("profile: " + profile.getProfileType()); // should be AREA :o
                if (profile instanceof IfcArbitraryClosedProfileDef) {
                    IfcArbitraryClosedProfileDef arbitraryClosedProfileDef = (IfcArbitraryClosedProfileDef) profile;
                    IfcCurve curve = arbitraryClosedProfileDef.getOuterCurve();
                    System.out.println("curve: " + curve);
                    if (curve instanceof IfcPolyline) {
                        IfcPolyline polyline = (IfcPolyline) curve;
                        for (IfcCartesianPoint point : polyline.getPoints()) {
                            sweptarea.add(IfcObjectPlacementUtils.toVector(point));
                        }
                    }
                }
                dir = IfcObjectPlacementUtils.toVector(extrudedAreaSolid.getExtrudedDirection().getDirectionRatios());
                depth = (float)extrudedAreaSolid.getDepth().value;

            }
        }
    }

    private Model model = null;
    private void buildModel() {
        builder.begin();

        Node node = builder.node();
        node.id = "base";

        MeshPartBuilder meshBuilder;

        if (is_circle) {
            meshBuilder = builder.part("part1", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED)));
            meshBuilder.setVertexTransform(circle_pos);
            //meshBuilder.circle(circle_radius, 30, new Vector3(), Z, angle_from, angle_to);
            for (int i = 0; i < 30; i++) {
                float theta1 = angle_from + (angle_to-angle_from) * (i / 30f);
                float theta2 = angle_from + (angle_to-angle_from) * ((i+1) / 30f);
                float x1 = (float)(circle_radius * Math.cos(theta1));
                float y1 = (float)(circle_radius * Math.sin(theta1));
                float x2 = (float)(circle_radius * Math.cos(theta2));
                float y2 = (float)(circle_radius * Math.sin(theta2));
                meshBuilder.line(x1, y1, 0, x2, y2, 0);
            }

        } else {
            meshBuilder = builder.part("part1", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED)));
            meshBuilder.line(start, end);

            Vector3 layerOffset = XY.cpy().scl(offset);

            for (int i = 0; i < materialLayersThickness.size(); i++) {
                node = builder.node();
                node.id = "layer" + i;

                float thickness = materialLayersThickness.get(i);
                System.out.println(thickness);
                meshBuilder = builder.part("part" + i, GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.YELLOW)));
                //meshBuilder.line(start, end);
                Vector3 corner000 = start.cpy().add(layerOffset);
                Vector3 corner001 = start.cpy().add(layerOffset).add(XY.cpy().scl(direction * thickness));
                Vector3 corner010 = end.cpy().add(layerOffset).add(XY.cpy().scl(direction * thickness));
                Vector3 corner011 = end.cpy().add(layerOffset);
                meshBuilder.rect(corner000, corner001, corner010, corner011, Z);
                layerOffset.add(XY.cpy().scl(direction * thickness));
            }
            meshBuilder = builder.part("sweptsolid", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.WHITE)));
            meshBuilder.setVertexTransform(body_placement);
            for (int i = 0; i < sweptarea.size(); i++) {
                Vector3 z_shape = dir.cpy().scl(depth);
                Vector3 p1 = sweptarea.get(i);
                Vector3 p2 = sweptarea.get((i + 1) % sweptarea.size());
                Vector3 p3 = p1.cpy().add(z_shape);
                Vector3 p4 = p2.cpy().add(z_shape);
                meshBuilder.rect(p1, p2, p4, p3, Z);
            }
        }
        model = builder.end();
    }

    public Model getModel() {
        if (model == null)
            buildModel();
        return model;
    }
}
