package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Vertex;
import fr.limsi.rorqual.core.model.primitives.ExtrudedAreaSolidModel;
import fr.limsi.rorqual.core.model.primitives.PolylineModel;
import fr.limsi.rorqual.core.model.primitives.TrimmedCurveModel;
import fr.limsi.rorqual.core.utils.CSGUtils;
import fr.limsi.rorqual.core.utils.IfcObjectPlacementUtils;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDirectionSenseEnum;
import ifc2x3javatoolbox.ifc2x3tc1.IfcExtrudedAreaSolid;
import ifc2x3javatoolbox.ifc2x3tc1.IfcFeatureElementSubtraction;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLayerSetDirectionEnum;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLengthMeasure;
import ifc2x3javatoolbox.ifc2x3tc1.IfcMaterialLayer;
import ifc2x3javatoolbox.ifc2x3tc1.IfcMaterialLayerSet;
import ifc2x3javatoolbox.ifc2x3tc1.IfcMaterialLayerSetUsage;
import ifc2x3javatoolbox.ifc2x3tc1.IfcMaterialSelect;
import ifc2x3javatoolbox.ifc2x3tc1.IfcOpeningElement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcPolyline;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProduct;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelAssociates;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelAssociatesMaterial;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelVoidsElement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRepresentation;
import ifc2x3javatoolbox.ifc2x3tc1.IfcShapeRepresentation;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSlab;
import ifc2x3javatoolbox.ifc2x3tc1.IfcTrimmedCurve;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWall;
import ifc2x3javatoolbox.ifc2x3tc1.LIST;
import ifc2x3javatoolbox.ifc2x3tc1.SET;

/**
 * Created by christophe on 31/03/15.
 */
public class IfcSlabModelFactory {

    private static ModelBuilder builder = new ModelBuilder();
    private static ShapeRenderer shapeRenderer = new ShapeRenderer();

    IfcSlab slab = null;

    public IfcSlabModelFactory(IfcProduct ifcProduct) {
        if (ifcProduct instanceof IfcSlab) {
            this.slab = (IfcSlab) ifcProduct;
            this.make();
        }
    }

    private void make() {
        this.setLayers();
        this.setRepresentations();
        this.setBody();
        this.setOpenings();
        this.mergeOpenings();
    }

    private List<ModelProvider> opening_models = new ArrayList<ModelProvider>();

    private void setOpenings() {
        ArrayList<IfcShapeRepresentation> openings = new ArrayList<IfcShapeRepresentation>();
        if (slab.getHasOpenings_Inverse() != null) {
            SET<IfcRelVoidsElement> voids = slab.getHasOpenings_Inverse();
            for (IfcRelVoidsElement voidElement : voids) {
                IfcFeatureElementSubtraction subs = voidElement.getRelatedOpeningElement();
                if (subs instanceof  IfcOpeningElement) {
                    IfcOpeningElement opening = (IfcOpeningElement) subs;
                    Matrix4 position = IfcObjectPlacementUtils.computeMatrix(opening.getObjectPlacement());
                    LIST<IfcRepresentation> reprs = opening.getRepresentation().getRepresentations();
                    IfcShapeRepresentation repr = null;
                    for (IfcRepresentation lrepr : reprs) {
                        if (lrepr instanceof IfcShapeRepresentation) {
                            IfcShapeRepresentation ifcShapeRepresentation = (IfcShapeRepresentation) lrepr;
                            if (ifcShapeRepresentation.getRepresentationIdentifier().getDecodedValue().equals("Body")) {
                                repr = ifcShapeRepresentation;
                                break;
                            }
                        }
                    }

                    if (repr != null) {
                        if (!repr.getRepresentationType().getDecodedValue().equals("SweptSolid"))
                            System.out.println("ASSERT TYPE FALSE"); //Todo throw exception
                        Object[] items = repr.getItems().toArray();
                        if (items.length > 0) {
                            if (items[0] instanceof IfcExtrudedAreaSolid) {
                                ExtrudedAreaSolidModel model = new ExtrudedAreaSolidModel((IfcExtrudedAreaSolid) items[0], position);
                                opening_models.add(model);
                            }
                        }
                    }

                }
            }
        }
    }

    private ArrayList<Float> materialLayersThickness = new ArrayList<Float>();
    private float offset = 0;
    private int direction = 1;

    private void setLayers() {
        // MaterialLayers
        if (slab.getHasAssociations_Inverse() == null)
            return;
        for (IfcRelAssociates association : slab.getHasAssociations_Inverse()) {
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

    private IfcShapeRepresentation body = null;
    private void setRepresentations() {
        for (IfcRepresentation ifcRepresentation : slab.getRepresentation().getRepresentations()) {
            if (ifcRepresentation instanceof IfcShapeRepresentation) {
                IfcShapeRepresentation ifcShapeRepresentation = (IfcShapeRepresentation) ifcRepresentation;
                if (ifcShapeRepresentation.getRepresentationIdentifier().getDecodedValue().equals("Body"))
                    body = ifcShapeRepresentation;
            }
        }
    }

    private ModelProvider body_model;

    private void setBody() {

        if (!body.getRepresentationType().getDecodedValue().equals("SweptSolid"))
            System.out.println("ASSERT TYPE FALSE"); //Todo throw exception

        Object[] items = body.getItems().toArray();
        if (items.length > 0) {
            if (items[0] instanceof IfcExtrudedAreaSolid) {
                body_model = new ExtrudedAreaSolidModel((IfcExtrudedAreaSolid) items[0]);
            }
        }
    }


    private ModelProvider wall_model;
    private CSG wall_csg;

    private void mergeOpenings() {
        CSG cube;
        List<Vertex> body_points = body_model.getVertex();
        wall_model = body_model;

        wall_csg = wall_model.toCSG();

        for (ModelProvider opening_model : opening_models) {
            //wall_csg.setOptType(CSG.OptType.NONE);
            CSG opening_csg = opening_model.toCSG();
            wall_csg = wall_csg.difference(opening_csg);
            //wall_csg = wall_csg.union(opening_csg);
            //wall_csg = opening_csg;
        }

    }


    public Model getModel() {
        //return wall_model.getModel();
        return CSGUtils.toModel(wall_csg);
    }
}