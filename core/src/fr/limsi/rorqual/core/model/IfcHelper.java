package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.math.Vector3;

import fr.limsi.rorqual.core.model.MaterialTypeEnum;
import ifc2x3javatoolbox.ifc2x3tc1.*;
import ifc2x3javatoolbox.ifcmodel.IfcModel;
import ifc2x3javatoolbox.ifc2x3tc1.IfcChangeActionEnum.IfcChangeActionEnum_internal;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSIUnitName.IfcSIUnitName_internal;
import ifc2x3javatoolbox.ifc2x3tc1.IfcUnitEnum.IfcUnitEnum_internal;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ricordeau on 08/04/2015.
 */
public class IfcHelper {

    private IfcModel ifcModel;
    private IfcOwnerHistory ownerHistory;
    private IfcBuilding building;
    private IfcRelAggregates relationBuildingToBuildingStorey;

    /*** Mis en place du singleton ***/
    private IfcHelper(){

    }
    private static class IfcHelperHolder
    {
        private final static IfcHelper INSTANCE = new IfcHelper();
    }

    public static synchronized IfcHelper getInstance() {
        return IfcHelperHolder.INSTANCE;
    }

    public IfcModel getIfcModel(){
        return this.ifcModel;
    }

    // Permet de compléter un model de départ contenant les informations du projet, un site,
    // un building
    public IfcModel initialiseIfcModel (){
        this.ifcModel = new IfcModel();
        relationBuildingToBuildingStorey=null;
        // Initialize File Name
        LIST<STRING> authors = new LIST<STRING>();
        authors.add(new STRING("Thomas Ricordeau",true));
        authors.add(new STRING("Julien Christophe",true));
        LIST<STRING> organisation = new LIST<STRING>();
        organisation.add(new STRING("LIMSI-CNRS",true));
        File_Name fileName = new File_Name(new STRING("3DReno first IFC",true),null,authors,
                organisation,new STRING("",true),new STRING("",true),new STRING("",true));
        ifcModel.setFile_Name(fileName);

        // Create persons
        IfcPerson ifcPerson = new IfcPerson(null,
                new IfcLabel("Ricordeau", true), new IfcLabel("Thomas", true),
                null, null, null, null, null);

        IfcPerson ifcPerson2 = new IfcPerson(null,
                new IfcLabel("Christophe", true), new IfcLabel("Julien", true),
                null, null, null, null, null);

        // Create the Organisation informations
        IfcOrganization ifcOrganization = new IfcOrganization(null,
                new IfcLabel("LIMSI-CNRS", true), null, null, null);

        // Create links between Persons and Organisation
        IfcPersonAndOrganization ifcPersonAndOrganization = new IfcPersonAndOrganization(
                ifcPerson, ifcOrganization, null);
        IfcPersonAndOrganization ifcPersonAndOrganization2 = new IfcPersonAndOrganization(
                ifcPerson2, ifcOrganization, null);

        // Create IfcApplication
        IfcApplication ifcApplication = new IfcApplication(ifcOrganization,
                new IfcLabel("1.0f", true), new IfcLabel("3D-Reno Application",
                true),null);

        // Create IfcOwnerHistory
        IfcOwnerHistory ifcOwnerHistory = new IfcOwnerHistory(
                ifcPersonAndOrganization, ifcApplication, null,
                new IfcChangeActionEnum(IfcChangeActionEnum_internal.ADDED
                        .name()), null, null,
                null, new IfcTimeStamp(
                (int) (System.currentTimeMillis() / 10.0f)));
        this.ownerHistory=ifcOwnerHistory;

        // Create WorldCoordinateSystem
        IfcCartesianPoint ifcCartesianPointOriginWorld = createCartesianPoint3D(0.0f,0.0f,0.0f);
        IfcAxis2Placement3D ifcAxis2Placement3DWorld = new IfcAxis2Placement3D(
                ifcCartesianPointOriginWorld, null, null);

        // Create RepresentationContexts
        IfcGeometricRepresentationContext ifcGeometricRepresentationContext =
                new IfcGeometricRepresentationContext(null, new IfcLabel("Model", true),
                new IfcDimensionCount(3),new DOUBLE(1.0E-5), ifcAxis2Placement3DWorld, null);
        SET<IfcRepresentationContext> contexts = new SET<IfcRepresentationContext>();
        contexts.add(ifcGeometricRepresentationContext);

        // Create UnitsInContext
        SET<IfcUnit> units = new SET<IfcUnit>();
        IfcSIUnit lengthUnit = new IfcSIUnit(null, new IfcUnitEnum(
                IfcUnitEnum_internal.LENGTHUNIT.name()), null/*new IfcSIPrefix(IfcSIPrefix.IfcSIPrefix_internal.CENTI.name())*/,
                new IfcSIUnitName(IfcSIUnitName_internal.METRE.name()));
        units.add(lengthUnit);
        IfcSIUnit planeAngleUnit = new IfcSIUnit(null, new IfcUnitEnum(
                IfcUnitEnum_internal.PLANEANGLEUNIT.name()), null,
                new IfcSIUnitName(IfcSIUnitName_internal.RADIAN.name()));
        units.add(planeAngleUnit);
        IfcSIUnit timeUnit = new IfcSIUnit(null, new IfcUnitEnum(
                IfcUnitEnum_internal.TIMEUNIT.name()), null, new IfcSIUnitName(
                IfcSIUnitName_internal.SECOND.name()));
        units.add(timeUnit);
        IfcUnitAssignment ifcUnitAssignment = new IfcUnitAssignment(units);

        // Create IfcProject
        IfcProject ifcProject = new IfcProject(new IfcGloballyUniqueId(
                ifcModel.getNewGlobalUniqueId()), ifcOwnerHistory, new IfcLabel(
                "Projet 3D-RENO", true), new IfcText(
                "Modelisation interieur + calcul DPE", true), null, null, null,
                contexts, ifcUnitAssignment);

        // Create IfcSite
        IfcCartesianPoint ifcCartesianPointOriginSite = createCartesianPoint3D(0.0f,0.0f,0.0f);
        IfcDirection ifcDirectionZAxisSite = createDirection3D(0.0f,0.0f,1.0f);
        IfcDirection ifcDirectionXDirectionSite = createDirection3D(1.0f, 0.0f, 0.0f);
        IfcAxis2Placement3D ifcAxis2Placement3DSite = new IfcAxis2Placement3D(
                ifcCartesianPointOriginSite, ifcDirectionZAxisSite, ifcDirectionXDirectionSite);
        IfcLocalPlacement ifcLocalPlacementSite = new IfcLocalPlacement(null,
                ifcAxis2Placement3DSite);
        IfcSite ifcSite = new IfcSite(new IfcGloballyUniqueId(
                ifcModel.getNewGlobalUniqueId()), ifcOwnerHistory, new IfcLabel(
                "Site de la simple maison", true), new IfcText("Description du site", true),
                null, ifcLocalPlacementSite, null, null,
                new IfcElementCompositionEnum("ELEMENT"), null, null, null, null, null);

        // Create IfcBuilding
        IfcCartesianPoint ifcCartesianPointOriginBuilding = createCartesianPoint3D(0.0f,0.0f,0.0f);
        IfcDirection ifcDirectionZAxisBuilding = createDirection3D(0.0f,0.0f,1.0f);
        IfcDirection ifcDirectionXDirectionBuilding = createDirection3D(1.0f, 0.0f, 0.0f);
        IfcAxis2Placement3D ifcAxis2Placement3DBuilding = new IfcAxis2Placement3D(
                ifcCartesianPointOriginBuilding, ifcDirectionZAxisBuilding, ifcDirectionXDirectionBuilding);
        IfcLocalPlacement ifcLocalPlacementBuilding = new IfcLocalPlacement(null,
                ifcAxis2Placement3DBuilding);
        IfcBuilding ifcBuilding = new IfcBuilding(new IfcGloballyUniqueId(
                ifcModel.getNewGlobalUniqueId()), ifcOwnerHistory, new IfcLabel(
                "Simple maison", true), new IfcText("Description de la maison", true),
                null, ifcLocalPlacementBuilding, null, null,
                new IfcElementCompositionEnum("ELEMENT"), null, null, null);
        this.building=ifcBuilding;

        // Create IfcBuilgingStorey
        IfcCartesianPoint ifcCartesianPointOriginBuildingStorey = createCartesianPoint3D(0.0f,0.0f,0.0f);
        IfcDirection ifcDirectionZAxisBuildingStorey = createDirection3D(0.0f,0.0f,1.0f);
        IfcDirection ifcDirectionXDirectionBuildingStorey = createDirection3D(1.0f, 0.0f, 0.0f);
        IfcAxis2Placement3D ifcAxis2Placement3DBuildingStorey = new IfcAxis2Placement3D(
                ifcCartesianPointOriginBuildingStorey, ifcDirectionZAxisBuildingStorey, ifcDirectionXDirectionBuildingStorey);
        IfcLocalPlacement ifcLocalPlacementBuildingStorey = new IfcLocalPlacement(null,
                ifcAxis2Placement3DBuildingStorey);
        IfcBuildingStorey ifcBuildingStorey = new IfcBuildingStorey(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcOwnerHistory, new IfcLabel("1st floor", true),
                new IfcText("1st floor / elevation = 0.0fm", true), null, ifcLocalPlacementBuildingStorey,
                null, null, new IfcElementCompositionEnum("ELEMENT"), new IfcLengthMeasure(0.0f));

        // Create relation IfcProject --> IfcSite
        SET<IfcObjectDefinition> relatedObjects;
        relatedObjects = new SET<IfcObjectDefinition>();
        relatedObjects.add(ifcSite);
        IfcRelAggregates relationProjectToSite;
        relationProjectToSite = new IfcRelAggregates(new IfcGloballyUniqueId(
                ifcModel.getNewGlobalUniqueId()), ifcOwnerHistory, new IfcLabel("ProjectContainer",true),
                new IfcText("ProjectContainer for Sites",true), ifcProject, relatedObjects);

        // Create relation IfcSite --> IfcBuilding
        relatedObjects = new SET<IfcObjectDefinition>();
        relatedObjects.add(ifcBuilding);
        IfcRelAggregates relationSiteToBuilding;
        relationSiteToBuilding = new IfcRelAggregates(new IfcGloballyUniqueId(
                ifcModel.getNewGlobalUniqueId()), ifcOwnerHistory, new IfcLabel("SiteContainer",true),
                new IfcText("SiteContainer for Building",true), ifcSite, relatedObjects);

//        // Create relation IfcBuilding --> IfcBuildingStorey
//        relatedObjects = new SET<IfcObjectDefinition>();
//        relatedObjects.add(ifcBuildingStorey);
//        IfcRelAggregates relationBuildingToBuildingStorey;
//        relationBuildingToBuildingStorey = new IfcRelAggregates(new IfcGloballyUniqueId(
//                ifcModel.getNewGlobalUniqueId()), ifcOwnerHistory, new IfcLabel("BuildingContainer",true),
//                new IfcText("BuildingContainer for BuildingStorey",true), ifcBuilding, relatedObjects);

        // add all IFC-objects to the model
        ifcModel.addIfcObject(ifcProject);
        ifcModel.addIfcObject(ifcOwnerHistory);
        ifcModel.addIfcObject(ifcPersonAndOrganization);
        ifcModel.addIfcObject(ifcPersonAndOrganization2);
        ifcModel.addIfcObject(ifcPerson);
        ifcModel.addIfcObject(ifcPerson2);
        ifcModel.addIfcObject(ifcOrganization);
        ifcModel.addIfcObject(ifcApplication);
        ifcModel.addIfcObject(ifcUnitAssignment);
        ifcModel.addIfcObject(lengthUnit);
        ifcModel.addIfcObject(planeAngleUnit);
        ifcModel.addIfcObject(timeUnit);
        ifcModel.addIfcObject(ifcGeometricRepresentationContext);
        ifcModel.addIfcObject(ifcAxis2Placement3DWorld);
        ifcModel.addIfcObject(ifcCartesianPointOriginWorld);

        ifcModel.addIfcObject(ifcSite);
        ifcModel.addIfcObject(ifcLocalPlacementSite);
        ifcModel.addIfcObject(ifcAxis2Placement3DSite);
        ifcModel.addIfcObject(ifcCartesianPointOriginSite);
        ifcModel.addIfcObject(ifcDirectionZAxisSite);
        ifcModel.addIfcObject(ifcDirectionXDirectionSite);

        ifcModel.addIfcObject(ifcBuilding);
        ifcModel.addIfcObject(ifcLocalPlacementBuilding);
        ifcModel.addIfcObject(ifcAxis2Placement3DBuilding);
        ifcModel.addIfcObject(ifcCartesianPointOriginBuilding);
        ifcModel.addIfcObject(ifcDirectionZAxisBuilding);
        ifcModel.addIfcObject(ifcDirectionXDirectionBuilding);

//        ifcModel.addIfcObject(ifcBuildingStorey);
//        ifcModel.addIfcObject(ifcLocalPlacementBuildingStorey);
//        ifcModel.addIfcObject(ifcAxis2Placement3DBuildingStorey);
//        ifcModel.addIfcObject(ifcCartesianPointOriginBuildingStorey);
//        ifcModel.addIfcObject(ifcDirectionZAxisBuildingStorey);
//        ifcModel.addIfcObject(ifcDirectionXDirectionBuildingStorey);

        ifcModel.addIfcObject(relationProjectToSite);
        ifcModel.addIfcObject(relationSiteToBuilding);
//        ifcModel.addIfcObject(relationBuildingToBuildingStorey);

        return ifcModel;
    }

    // Permet de récupérer le contexte géométrique d'un model
    public IfcGeometricRepresentationContext getGeometricRepresentationContext (){
        Collection<IfcGeometricRepresentationContext> collectionBuilding = ifcModel.getCollection(IfcGeometricRepresentationContext.class);
        Iterator<IfcGeometricRepresentationContext> it = collectionBuilding.iterator();
        return it.next();
    }

    // Permet de récupérer un étage dans un model en fonction de son nom
    public IfcBuildingStorey getBuildingStorey (String nameBuildingStorey){
        Collection<IfcBuildingStorey> collectionBuildingStorey = ifcModel.getCollection(IfcBuildingStorey.class);
        for (IfcBuildingStorey actualBuildingStorey : collectionBuildingStorey){
            if(actualBuildingStorey.getName().getDecodedValue().equals(nameBuildingStorey)){
                return actualBuildingStorey;
            }
        }
        return null;
    }

    // Permet de récupérer l'étage dans lequel se trouve un product
    public IfcBuildingStorey getBuildingStorey (IfcProduct product){
        if(product instanceof IfcWallStandardCase){
            IfcWallStandardCase wall = (IfcWallStandardCase) product;
            SET<IfcRelContainedInSpatialStructure> relContainedInSpatialStructure = wall.getContainedInStructure_Inverse();
            Iterator<IfcRelContainedInSpatialStructure> it = relContainedInSpatialStructure.iterator();
            return (IfcBuildingStorey)it.next().getRelatingStructure();
        }
        else if(product instanceof IfcSlab){
            IfcSlab slab = (IfcSlab) product;
            SET<IfcRelContainedInSpatialStructure> relContainedInSpatialStructure = slab.getContainedInStructure_Inverse();
            Iterator<IfcRelContainedInSpatialStructure> it = relContainedInSpatialStructure.iterator();
            return (IfcBuildingStorey)it.next().getRelatingStructure();
        }
        return null;
    }

    // Permet de récupérer un WallStandardCase dans un model en fonction de son nom
    public IfcWallStandardCase getWall (String nameWall){
        Collection<IfcWallStandardCase> collectionWallStandardCase = ifcModel.getCollection(IfcWallStandardCase.class);
        for (IfcWallStandardCase actualWall : collectionWallStandardCase){
            if(actualWall.getName().getDecodedValue().equals(nameWall)){
                return actualWall;
            }
        }
        return null;
    }

    public Collection<IfcWallStandardCase> getAllWalls (){
        return ifcModel.getCollection(IfcWallStandardCase.class);
    }

    // Permet de récuperer l'épaisseur d'un wall dans le model
    public float getWallThickness (IfcWallStandardCase wall){
        IfcProductRepresentation productRepresentation = wall.getRepresentation();
        LIST<IfcRepresentation> representationLIST = productRepresentation.getRepresentations();
        for(IfcRepresentation actualRepresentation : representationLIST) {
            if (actualRepresentation.getRepresentationType().getDecodedValue().equals("SweptSolid")) {
                SET<IfcRepresentationItem> representationItemSET = actualRepresentation.getItems();
                for (IfcRepresentationItem actualRepresentationItem : representationItemSET) {
                    if (actualRepresentationItem instanceof IfcExtrudedAreaSolid) {
                        IfcProfileDef profileDef = ((IfcExtrudedAreaSolid) actualRepresentationItem).getSweptArea();
                        if (profileDef instanceof IfcArbitraryClosedProfileDef) {
                            IfcCurve curve = ((IfcArbitraryClosedProfileDef) profileDef).getOuterCurve();
                            if (curve instanceof IfcPolyline) {
                                LIST<IfcCartesianPoint> cartesianPointLIST = ((IfcPolyline) curve).getPoints();
                                return (float)(2.0*cartesianPointLIST.get(0).getCoordinates().get(1).value);
                            }
                        }
                    }
                }
            }
        }
        return -1.0f;
    }

    // Permet de récuperer la hauteur d'un wall dans le model
    public float getWallHeight (IfcWallStandardCase wall){
        IfcProductRepresentation productRepresentation = wall.getRepresentation();
        LIST<IfcRepresentation> representationLIST = productRepresentation.getRepresentations();
        for(IfcRepresentation actualRepresentation : representationLIST){
            if(actualRepresentation.getRepresentationType().getDecodedValue().equals("SweptSolid")){
                SET<IfcRepresentationItem> representationItemSET = actualRepresentation.getItems();
                for(IfcRepresentationItem actualRepresentationItem : representationItemSET){
                    if(actualRepresentationItem instanceof IfcExtrudedAreaSolid){
                        return (float)(((IfcExtrudedAreaSolid) actualRepresentationItem).getDepth().value);
                    }
                }
            }
        }
        return -1.0f;
    }

    // Permet de récupérer la longueur d'un mur
    public float getWallLength (IfcWallStandardCase wall){
        IfcProductRepresentation productRepresentation = wall.getRepresentation();
        LIST<IfcRepresentation> representationLIST = productRepresentation.getRepresentations();
        for(IfcRepresentation actualRepresentation : representationLIST){
            if(actualRepresentation.getRepresentationType().getDecodedValue().equals("SweptSolid")){
                SET<IfcRepresentationItem> representationItemSET = actualRepresentation.getItems();
                for(IfcRepresentationItem actualRepresentationItem : representationItemSET){
                    if(actualRepresentationItem instanceof IfcExtrudedAreaSolid){
                        IfcProfileDef profileDef = ((IfcExtrudedAreaSolid) actualRepresentationItem).getSweptArea();
                        if(profileDef instanceof IfcArbitraryClosedProfileDef){
                            IfcCurve curve = ((IfcArbitraryClosedProfileDef) profileDef).getOuterCurve();
                            if (curve instanceof IfcPolyline){
                                LIST<IfcCartesianPoint> cartesianPointLIST = ((IfcPolyline) curve).getPoints();
                                // Set of the first point which use the length
                                IfcCartesianPoint cartesianPoint1 = cartesianPointLIST.get(1);
                                LIST<IfcLengthMeasure> lengthMeasureLIST = cartesianPoint1.getCoordinates();
                                return (float)(lengthMeasureLIST.get(0).value);
                            }
                        }
                    }
                }
            }
            else if (actualRepresentation.getRepresentationType().getDecodedValue().equals("Curve2D")){
                SET<IfcRepresentationItem> representationItemSET = actualRepresentation.getItems();
                for(IfcRepresentationItem actualRepresentationItem : representationItemSET){
                    if(actualRepresentationItem instanceof IfcTrimmedCurve){
                        IfcCurve curve = ((IfcTrimmedCurve) actualRepresentationItem).getBasisCurve();
                        if(curve instanceof IfcLine){
                            IfcVector vector = ((IfcLine) curve).getDir();
                            return (float)(vector.getMagnitude().value);
                        }
                    }
                }
            }
        }
        return -1.0f;
    }

    // Permet de récupérer la surface d'un mur
    public float getWallSurface (IfcWallStandardCase wall){
        float wallHeight = getWallHeight(wall);
        float wallLength = getWallLength(wall);
        float surfaceWall = (wallHeight*wallLength);
        SET<IfcOpeningElement> openingElementSET = getOpeningRelToWall(wall);
        for (IfcOpeningElement actualOpening : openingElementSET){
            surfaceWall -= getOpeningSurface(actualOpening);
        }
        return surfaceWall;
    }

    public ArrayList<IfcCartesianPoint> getWallPositionCoins (IfcWallStandardCase wall){
        ArrayList<IfcCartesianPoint> cartesianPointArrayList = new ArrayList<IfcCartesianPoint>();
        IfcProductRepresentation productRepresentation = wall.getRepresentation();
        LIST<IfcRepresentation> representationLIST = productRepresentation.getRepresentations();
        for(IfcRepresentation actualRepresentation : representationLIST) {
            if (actualRepresentation.getRepresentationType().getDecodedValue().equals("SweptSolid")) {
                SET<IfcRepresentationItem> representationItemSET = actualRepresentation.getItems();
                for (IfcRepresentationItem actualRepresentationItem : representationItemSET) {
                    if (actualRepresentationItem instanceof IfcExtrudedAreaSolid) {
                        IfcProfileDef profileDef = ((IfcExtrudedAreaSolid) actualRepresentationItem).getSweptArea();
                        if (profileDef instanceof IfcArbitraryClosedProfileDef) {
                            IfcCurve curve = ((IfcArbitraryClosedProfileDef) profileDef).getOuterCurve();
                            if (curve instanceof IfcPolyline) {
                                LIST<IfcCartesianPoint> cartesianPointLIST = ((IfcPolyline) curve).getPoints();
                                for (IfcCartesianPoint actualCartesianPoint : cartesianPointLIST){
                                    cartesianPointArrayList.add(actualCartesianPoint);
                                }
                            }
                        }
                    }
                }
            }
        }
        return cartesianPointArrayList;
    }

    public ArrayList<IfcCartesianPoint> getWallPosition (IfcWallStandardCase wall){
        ArrayList<IfcCartesianPoint> cartesianPointArrayList = new ArrayList<IfcCartesianPoint>();
        IfcCartesianPoint pointA;
        IfcCartesianPoint pointB;
        IfcDirection direction;
        float wallLength = this.getWallLength(wall);
        float xA=0,yA=0,zA=0;
        float xB=0,yB=0,zB=0;

        IfcObjectPlacement objectPlacement = wall.getObjectPlacement();
        if (objectPlacement instanceof IfcLocalPlacement){
            IfcAxis2Placement axis2Placement = ((IfcLocalPlacement) objectPlacement).getRelativePlacement();
            if (axis2Placement instanceof IfcAxis2Placement3D){
                pointA = ((IfcAxis2Placement3D) axis2Placement).getLocation();
                xA = (float)pointA.getCoordinates().get(0).value;
                yA = (float)pointA.getCoordinates().get(1).value;
                zA = (float)pointA.getCoordinates().get(2).value;
                direction = ((IfcAxis2Placement3D) axis2Placement).getRefDirection();
                LIST<DOUBLE> directionRatios = direction.getDirectionRatios();
                xB = (float)directionRatios.get(0).value*wallLength+xA;
                yB = (float)directionRatios.get(1).value*wallLength+yA;
                zB = (float)directionRatios.get(2).value*wallLength+zA;
                pointA = this.createCartesianPoint3D(xA,yA,zA);
                pointB = this.createCartesianPoint3D(xB,yB,zB);
                cartesianPointArrayList.add(pointA);
                cartesianPointArrayList.add(pointB);
            }
        }
        return cartesianPointArrayList;
    }

    // Permet de récupérer un Opening dans un model en fonction de son nom
    public IfcOpeningElement getOpening (String nameOpening){
        Collection<IfcOpeningElement> collectionOpening = ifcModel.getCollection(IfcOpeningElement.class);
        for (IfcOpeningElement actualOpening : collectionOpening){
            if(actualOpening.getName().getDecodedValue().equals(nameOpening)){
                return actualOpening;
            }
        }
        return null;
    }

    // Permet de récupérer l'Opening lié à une door
    public IfcOpeningElement getOpeningRelToDoor (IfcDoor door){
        Collection<IfcRelFillsElement> collectionRelFillsElement = ifcModel.getCollection(IfcRelFillsElement.class);
        for (IfcRelFillsElement actualRelFillsElement : collectionRelFillsElement){
            if(actualRelFillsElement.getRelatedBuildingElement().equals(door)){
                return actualRelFillsElement.getRelatingOpeningElement();
            }
        }
        return null;
    }

    // Permet de récupérer l'Opening lié à une window
    public IfcOpeningElement getOpeningRelToWindow (IfcWindow window){
        Collection<IfcRelFillsElement> collectionRelFillsElement = ifcModel.getCollection(IfcRelFillsElement.class);
        for (IfcRelFillsElement actualRelFillsElement : collectionRelFillsElement){
            if(actualRelFillsElement.getRelatedBuildingElement().equals(window)){
                return actualRelFillsElement.getRelatingOpeningElement();
            }
        }
        return null;
    }

    // Permet de récupérer les openings liées à un wall
    public SET<IfcOpeningElement> getOpeningRelToWall (IfcWallStandardCase wall){
        SET<IfcOpeningElement> openingElementSET = new SET<IfcOpeningElement>();
        if (wall.getHasOpenings_Inverse()!=null){
            SET<IfcRelVoidsElement> relVoidsElementSET = wall.getHasOpenings_Inverse();
            for(IfcRelVoidsElement actualRelVoidsElement : relVoidsElementSET){
                IfcFeatureElementSubtraction featureElementSubtraction = actualRelVoidsElement.getRelatedOpeningElement();
                if(featureElementSubtraction instanceof IfcOpeningElement){
                    openingElementSET.add((IfcOpeningElement)featureElementSubtraction);
                }
            }
        }
        return openingElementSET;
    }

    // Permet de récupérer la largeur d'un opening
    public float getOpeningWidth (IfcOpeningElement opening){
        IfcProductRepresentation productRepresentation = opening.getRepresentation();
        LIST<IfcRepresentation> representationLIST = productRepresentation.getRepresentations();
        for(IfcRepresentation actualRepresentation : representationLIST) {
            if (actualRepresentation.getRepresentationType().getDecodedValue().equals("SweptSolid")) {
                SET<IfcRepresentationItem> representationItemSET = actualRepresentation.getItems();
                for (IfcRepresentationItem actualRepresentationItem : representationItemSET) {
                    if (actualRepresentationItem instanceof IfcExtrudedAreaSolid) {
                        IfcProfileDef profileDef = ((IfcExtrudedAreaSolid) actualRepresentationItem).getSweptArea();
                        if(profileDef instanceof IfcRectangleProfileDef){
                            return (float)(((IfcRectangleProfileDef) profileDef).getYDim().value);
                        }
                        IfcAxis2Placement3D axis2Placement3D = ((IfcExtrudedAreaSolid) actualRepresentationItem).getPosition();
                        IfcCartesianPoint cartesianPoint = axis2Placement3D.getLocation();
                        LIST<IfcLengthMeasure> lengthMeasureLIST = cartesianPoint.getCoordinates();
                        return (float)(lengthMeasureLIST.get(0).value*2);
                    }
                }
            }
        }
        return -1;
    }

    // Permet de récupérer la hauteur d'un opening
    public float getOpeningHeight (IfcOpeningElement opening){
        IfcProductRepresentation productRepresentation = opening.getRepresentation();
        LIST<IfcRepresentation> representationLIST = productRepresentation.getRepresentations();
        for(IfcRepresentation actualRepresentation : representationLIST) {
            if (actualRepresentation.getRepresentationType().getDecodedValue().equals("SweptSolid")) {
                SET<IfcRepresentationItem> representationItemSET = actualRepresentation.getItems();
                for (IfcRepresentationItem actualRepresentationItem : representationItemSET) {
                    if (actualRepresentationItem instanceof IfcExtrudedAreaSolid) {
                        IfcProfileDef profileDef = ((IfcExtrudedAreaSolid) actualRepresentationItem).getSweptArea();
                        if(profileDef instanceof IfcRectangleProfileDef){
                            return (float)(((IfcRectangleProfileDef) profileDef).getXDim().value);
                        }
                        IfcAxis2Placement3D axis2Placement3D = ((IfcExtrudedAreaSolid) actualRepresentationItem).getPosition();
                        IfcCartesianPoint cartesianPoint = axis2Placement3D.getLocation();
                        LIST<IfcLengthMeasure> lengthMeasureLIST = cartesianPoint.getCoordinates();
                        return (float)(lengthMeasureLIST.get(2).value*2);
                    }
                }
            }
        }
        return -1;
    }

    // Permet de récupérer la hauteur d'un opening
    public float getOpeningDepth (IfcOpeningElement opening){
        IfcProductRepresentation productRepresentation = opening.getRepresentation();
        LIST<IfcRepresentation> representationLIST = productRepresentation.getRepresentations();
        for(IfcRepresentation actualRepresentation : representationLIST) {
            if (actualRepresentation.getRepresentationType().getDecodedValue().equals("SweptSolid")) {
                SET<IfcRepresentationItem> representationItemSET = actualRepresentation.getItems();
                for (IfcRepresentationItem actualRepresentationItem : representationItemSET) {
                    if (actualRepresentationItem instanceof IfcExtrudedAreaSolid) {
                        return (float)((IfcExtrudedAreaSolid) actualRepresentationItem).getDepth().value;
                    }
                }
            }
        }
        return -1;
    }

    // Permet de récupérer la surface d'un opening
    public float getOpeningSurface (IfcOpeningElement opening){
        float openingWidth = getOpeningWidth(opening);
        float openingHeight = getOpeningHeight(opening);
        return openingWidth*openingHeight;
    }

    // Permet de récupérer une Door dans un model en fonction de son nom
    public IfcDoor getDoor (String nameDoor){
        Collection<IfcDoor> collectionDoor = ifcModel.getCollection(IfcDoor.class);
        for (IfcDoor actualDoor : collectionDoor){
            if(actualDoor.getName().getDecodedValue().equals(nameDoor)){
                return actualDoor;
            }
        }
        return null;
    }

    // Permet de récupérer le wall sur lequel se trouve une porte
    public IfcWallStandardCase getWallRelToDoor(IfcDoor door){
        IfcWallStandardCase wall = new IfcWallStandardCase();
        IfcOpeningElement openingRelToDoor = this.getOpeningRelToDoor(door);
        IfcRelVoidsElement relVoidsElement = openingRelToDoor.getVoidsElements_Inverse();
        IfcElement element = relVoidsElement.getRelatingBuildingElement();
        if (element instanceof IfcWallStandardCase){
            wall = (IfcWallStandardCase)element;
        }
        return wall;
    }

    // Permet de récupérer le wall sur lequel se trouve une fenetre
    public IfcWallStandardCase getWallRelToWindow(IfcWindow window){
        IfcWallStandardCase wall = new IfcWallStandardCase();
        IfcOpeningElement openingRelToWindow = this.getOpeningRelToWindow(window);
        IfcRelVoidsElement relVoidsElement = openingRelToWindow.getVoidsElements_Inverse();
        IfcElement element = relVoidsElement.getRelatingBuildingElement();
        if (element instanceof IfcWallStandardCase){
            wall = (IfcWallStandardCase)element;
        }
        return wall;
    }

    // Permet de récupérer la largeur d'une door
    public float getDoorWidth (IfcDoor door){
        IfcOpeningElement opening = this.getOpeningRelToDoor(door);
        return (this.getOpeningWidth(opening));
    }

    // Permet de récupérer la hauteur d'une door
    public float getDoorHeight (IfcDoor door){
        IfcOpeningElement opening = this.getOpeningRelToDoor(door);
        return (this.getOpeningHeight(opening));
    }

    // Permet de récupérer la profondeur d'une door
    public float getDoorDepth (IfcDoor door){
        IfcOpeningElement opening = this.getOpeningRelToDoor(door);
        return (this.getOpeningDepth(opening));
    }

    // Permet de récupérer la surface d'une door
    public float getDoorSurface (IfcDoor door){
        IfcOpeningElement opening = this.getOpeningRelToDoor(door);
        return (this.getOpeningSurface(opening));
    }

    // Permet de récupérer une window dans un model en fonction de son nom
    public IfcWindow getWindow (String nameWindow){
        Collection<IfcWindow> collectionWindow = ifcModel.getCollection(IfcWindow.class);
        for (IfcWindow actualWindow : collectionWindow){
            if(actualWindow.getName().getDecodedValue().equals(nameWindow)){
                return actualWindow;
            }
        }
        return null;
    }

    // Permet de récupérer la profondeur d'une window
    public float getWindowDepth (IfcWindow window){
        IfcOpeningElement opening = this.getOpeningRelToWindow(window);
        return (this.getOpeningDepth(opening));
    }

    // Permet de récupérer la largeur d'une window
    public float getWindowWidth (IfcWindow window){
        IfcOpeningElement opening = this.getOpeningRelToWindow(window);
        return (this.getOpeningWidth(opening));
    }

    // Permet de récupérer la hauteur d'une window
    public float getWindowHeight (IfcWindow window){
        IfcOpeningElement opening = this.getOpeningRelToWindow(window);
        return (this.getOpeningHeight(opening));
    }

    // Permet de récupérer la surface d'une window
    public float getWindowSurface (IfcWindow window){
        IfcOpeningElement opening = this.getOpeningRelToWindow(window);
        return (this.getOpeningSurface(opening));
    }

    // Permet de récupérer la relation entre le building et ses étages
    public IfcRelAggregates getBuildingRelations (){
        Collection<IfcRelAggregates> collectionRelAggregates = ifcModel.getCollection(IfcRelAggregates.class);
        for (IfcRelAggregates actualRelAggregates : collectionRelAggregates){
            if(actualRelAggregates.getRelatingObject().getClass().equals(IfcBuilding.class)){
                return actualRelAggregates;
            }
        }
        return null;
    }

    // Permet de récupérer la relation entre un étage et ses produits
    public IfcRelContainedInSpatialStructure getRelContainedInSpatialStructure (String nameBuildingStorey){
        Collection<IfcRelContainedInSpatialStructure> collectionRelContainedInSpatialStructure = ifcModel.getCollection(IfcRelContainedInSpatialStructure.class);
        for (IfcRelContainedInSpatialStructure actualRelContainedInSpatialStructure : collectionRelContainedInSpatialStructure){
            if(actualRelContainedInSpatialStructure.getRelatingStructure().getName().getDecodedValue().equals(nameBuildingStorey)){
                return actualRelContainedInSpatialStructure;
            }
        }
        return null;
    }

    // Permet de récupérer la liaison entre un openingElement et un relFillsElement
    public IfcRelFillsElement getRelFillsElement (IfcOpeningElement openingElement){
        Collection<IfcRelFillsElement> collectionRelFillsElement = ifcModel.getCollection(IfcRelFillsElement.class);
        for (IfcRelFillsElement actualRelFillsElement : collectionRelFillsElement){
            if(actualRelFillsElement.getRelatingOpeningElement().equals(openingElement)){
                return (actualRelFillsElement);
            }
        }
        return null;
    }

    // Permet de récupérer l'ensemble des slabs rattaché à un building Storey
    public LIST<IfcSlab> getSlabs (String nameBuildingStorey){
        LIST<IfcSlab> slabs = new LIST<IfcSlab>();
        IfcRelContainedInSpatialStructure relContainedInSpatialStructure = this.getRelContainedInSpatialStructure(nameBuildingStorey);
        SET<IfcProduct> productSET = relContainedInSpatialStructure.getRelatedElements();
        for(IfcProduct actualProduct : productSET){
            if(actualProduct instanceof IfcSlab){
                slabs.add((IfcSlab)actualProduct);
            }
        }
        return slabs;
    }

    // Permet de récupérer l'épaisseur d'un slab
    public float getSlabThickness (IfcSlab slab){
        IfcProductRepresentation productRepresentation = slab.getRepresentation();
        LIST<IfcRepresentation> representationLIST = productRepresentation.getRepresentations();
        for(IfcRepresentation actualRepresentation : representationLIST) {
            if (actualRepresentation.getRepresentationType().getDecodedValue().equals("SweptSolid")) {
                SET<IfcRepresentationItem> representationItemSET = actualRepresentation.getItems();
                for (IfcRepresentationItem actualRepresentationItem : representationItemSET) {
                    if (actualRepresentationItem instanceof IfcExtrudedAreaSolid) {
                        return (float)(((IfcExtrudedAreaSolid) actualRepresentationItem).getDepth().value);
                    }
                }
            }
        }
        return -1;
    }

    // Permet de récupérer la surface d'un slab
    public float getSlabSurface (IfcSlab slab){
        IfcProductRepresentation productRepresentation = slab.getRepresentation();
        LIST<IfcRepresentation> representationLIST = productRepresentation.getRepresentations();
        for(IfcRepresentation actualRepresentation : representationLIST) {
            if (actualRepresentation.getRepresentationType().getDecodedValue().equals("SweptSolid")) {
                SET<IfcRepresentationItem> representationItemSET = actualRepresentation.getItems();
                for (IfcRepresentationItem actualRepresentationItem : representationItemSET) {
                    if (actualRepresentationItem instanceof IfcExtrudedAreaSolid) {
                        IfcProfileDef ifcProfileDef = ((IfcExtrudedAreaSolid) actualRepresentationItem).getSweptArea();
                        if (ifcProfileDef instanceof IfcArbitraryClosedProfileDef){
                            IfcCurve ifcCurve = ((IfcArbitraryClosedProfileDef) ifcProfileDef).getOuterCurve();
                            if (ifcCurve instanceof IfcPolyline){
                                // Cf calcul de l'aire d'un poligone : http://fr.wikihow.com/calculer-la-surface-d%27un-polygone
                                LIST<IfcCartesianPoint> ifcCartesianPointLIST = ((IfcPolyline) ifcCurve).getPoints();
                                ifcCartesianPointLIST.add(ifcCartesianPointLIST.get(0));
                                float xActuel=0,xSuivant=0,yActuel=0,ySuivant=0,totX=0,totY=0, airePolygone=0;
                                for (int i=0; i<ifcCartesianPointLIST.size()-1;i++){
                                    xActuel = (float)ifcCartesianPointLIST.get(i).getCoordinates().get(0).value;
                                    xSuivant = (float)ifcCartesianPointLIST.get(i+1).getCoordinates().get(0).value;
                                    yActuel = (float)ifcCartesianPointLIST.get(i).getCoordinates().get(1).value;
                                    ySuivant = (float)ifcCartesianPointLIST.get(i+1).getCoordinates().get(1).value;
                                    totX += (xActuel*ySuivant);
                                    totY += (yActuel*xSuivant);
                                }
                                airePolygone=(totX-totY)/2;
                                return Math.abs(airePolygone);
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }

    // Permet de créer un point cartésien en 2D à partir de deux floats
    public IfcCartesianPoint createCartesianPoint2D(float x, float y){
        LIST<IfcLengthMeasure> coordinates = new LIST<IfcLengthMeasure>();
        coordinates.add(new IfcLengthMeasure(x));
        coordinates.add(new IfcLengthMeasure(y));
        return new IfcCartesianPoint(coordinates);
    }

    // Permet de créer un point cartésien en 3D à partir de trois floats
    public IfcCartesianPoint createCartesianPoint3D(float x, float y, float z){
        LIST<IfcLengthMeasure> coordinates = new LIST<IfcLengthMeasure>();
        coordinates.add(new IfcLengthMeasure(x));
        coordinates.add(new IfcLengthMeasure(y));
        coordinates.add(new IfcLengthMeasure(z));
        return new IfcCartesianPoint(coordinates);
    }

    public IfcCartesianPoint createCartesianPoint3D(Vector3 v){
        LIST<IfcLengthMeasure> coordinates = new LIST<IfcLengthMeasure>();
        coordinates.add(new IfcLengthMeasure(v.x));
        coordinates.add(new IfcLengthMeasure(v.y));
        coordinates.add(new IfcLengthMeasure(v.z));
        return new IfcCartesianPoint(coordinates);
    }

    // Permet de créer une direction à partir de deux floats
    public IfcDirection createDirection2D(float x, float y){
        LIST<DOUBLE> coordinates = new LIST<DOUBLE>();
        coordinates.add(new DOUBLE(x));
        coordinates.add(new DOUBLE(y));
        return new IfcDirection(coordinates);
    }

    // Permet de créer une direction à partir de trois floats
    public IfcDirection createDirection3D(float x, float y, float z){
        LIST<DOUBLE> coordinates = new LIST<DOUBLE>();
        coordinates.add(new DOUBLE(x));
        coordinates.add(new DOUBLE(y));
        coordinates.add(new DOUBLE(z));
        return new IfcDirection(coordinates);
    }

    // Permet d'ajouter un étage au batiment en lui rentrant son nom et son élévation
    public void addBuildingStorey (String nameFloor , float elevation){
        IfcCartesianPoint ifcCartesianPointOriginBuildingStorey = createCartesianPoint3D(0.0f,0.0f,elevation);
        IfcDirection ifcDirectionZAxisBuildingStorey = createDirection3D(0.0f,0.0f,1.0f);
        IfcDirection ifcDirectionXDirectionBuildingStorey = createDirection3D(1.0f,0.0f,0.0f);
        IfcAxis2Placement3D ifcAxis2Placement3DBuildingStorey = new IfcAxis2Placement3D(
                ifcCartesianPointOriginBuildingStorey, ifcDirectionZAxisBuildingStorey, ifcDirectionXDirectionBuildingStorey);
        IfcLocalPlacement ifcLocalPlacementBuildingStorey = new IfcLocalPlacement(null,
                ifcAxis2Placement3DBuildingStorey);
        IfcBuildingStorey ifcBuildingStorey = new IfcBuildingStorey(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(), new IfcLabel(nameFloor, true),
                new IfcText(nameFloor+" / elevation = "+elevation+"m", true), null, ifcLocalPlacementBuildingStorey,
                null, null, new IfcElementCompositionEnum("ELEMENT"), new IfcLengthMeasure(elevation));

        // Create relation IfcBuilding --> IfcBuildingStorey
        SET<IfcObjectDefinition> relatedObjects = new SET<IfcObjectDefinition>();
        relatedObjects.add(ifcBuildingStorey);
        if(relationBuildingToBuildingStorey==null){
            relationBuildingToBuildingStorey = new IfcRelAggregates(new IfcGloballyUniqueId(
                    ifcModel.getNewGlobalUniqueId()), this.ownerHistory, new IfcLabel("BuildingContainer",true),
                    new IfcText("BuildingContainer for BuildingStorey",true), this.building, relatedObjects);
            ifcModel.addIfcObject(relationBuildingToBuildingStorey);
        }else{
            relationBuildingToBuildingStorey.addRelatedObjects(ifcBuildingStorey);
        }


        relationBuildingToBuildingStorey.addRelatedObjects(ifcBuildingStorey);

        ifcModel.addIfcObject(ifcBuildingStorey);
        ifcModel.addIfcObject(ifcLocalPlacementBuildingStorey);
        ifcModel.addIfcObject(ifcAxis2Placement3DBuildingStorey);
        ifcModel.addIfcObject(ifcCartesianPointOriginBuildingStorey);
        ifcModel.addIfcObject(ifcDirectionZAxisBuildingStorey);
        ifcModel.addIfcObject(ifcDirectionXDirectionBuildingStorey);
    }

    // Permet d'ajouter un mur à un IfcModel
    // Arguments : IfcModel, étage sur lequel on implémente le mur, dimension, position, et orientation du mur
    public void addWall(String nameBuildingStorey, String nameWall, float wallLength, float wallHeight, float wallThickness, float posX, float posY, float dirX, float dirY){
        IfcBuildingStorey buildingStorey = getBuildingStorey(nameBuildingStorey);
        IfcCartesianPoint ifcCartesianPointOriginWall = createCartesianPoint3D(posX,posY,0.0f);
        IfcDirection ifcDirectionZAxisWall = createDirection3D(0.0f,0.0f,1.0f);
        IfcDirection ifcDirectionXDirectionWall = createDirection3D(dirX,dirY,0.0f);
        IfcAxis2Placement3D ifcAxis2Placement3DWall = new IfcAxis2Placement3D(
                ifcCartesianPointOriginWall, ifcDirectionZAxisWall, ifcDirectionXDirectionWall);
        IfcLocalPlacement ifcLocalPlacementWall = new IfcLocalPlacement(buildingStorey.getObjectPlacement(),
                ifcAxis2Placement3DWall);
        LIST<IfcRepresentation> ifcWallRepresentationsList = new LIST<IfcRepresentation>();

        // First representation : Geometric representation (2D)
        IfcCartesianPoint ifcWallPoints2D1 = createCartesianPoint2D(0.0f,0.0f);
        IfcCartesianPoint ifcWallPoints2D2 = createCartesianPoint2D(wallLength,0.0f);
        IfcDirection ifcWallAxisDirection = createDirection2D(1.0f,0.0f);
        IfcVector wallAxisVector = new IfcVector(ifcWallAxisDirection,new IfcLengthMeasure(wallLength));
        IfcCartesianPoint ifcWallPoints2D0 = createCartesianPoint2D(0.0f,0.0f);
        IfcLine wallAxisLine =new IfcLine(ifcWallPoints2D0,wallAxisVector);
        SET<IfcTrimmingSelect> Trim1 = new SET<IfcTrimmingSelect>();
        SET<IfcTrimmingSelect> Trim2 = new SET<IfcTrimmingSelect>();
        Trim1.add(ifcWallPoints2D1);
        Trim2.add(ifcWallPoints2D2);
        IfcTrimmedCurve wallTrimmedCurve = new IfcTrimmedCurve(wallAxisLine,Trim1,Trim2,
                new BOOLEAN(true),new IfcTrimmingPreference("CARTESIAN"));
        SET<IfcRepresentationItem> ifcWallRepresentation2DItem = new SET<IfcRepresentationItem>();
        ifcWallRepresentation2DItem.add(wallTrimmedCurve);
        IfcShapeRepresentation ifcWallCurve2DRepresentation = new IfcShapeRepresentation(getGeometricRepresentationContext(),
                new IfcLabel ("Axis",true), new IfcLabel("Curve2D",true), ifcWallRepresentation2DItem);
        ifcWallRepresentationsList.add(ifcWallCurve2DRepresentation);

        // Second representation : SweptSolid representation (3D)
        LIST<IfcCartesianPoint> wallAllPoints = new LIST<IfcCartesianPoint>();
        IfcCartesianPoint wallCartesianPoint1 = createCartesianPoint2D(0.0f,wallThickness/2);
        wallAllPoints.add(wallCartesianPoint1);
        IfcCartesianPoint wallCartesianPoint2 = createCartesianPoint2D(wallLength,wallThickness/2);
        wallAllPoints.add(wallCartesianPoint2);
        IfcCartesianPoint wallCartesianPoint3 = createCartesianPoint2D(wallLength,-wallThickness/2);
        wallAllPoints.add(wallCartesianPoint3);
        IfcCartesianPoint wallCartesianPoint4 = createCartesianPoint2D(0.0f,-wallThickness/2);
        wallAllPoints.add(wallCartesianPoint4);
        IfcPolyline wallPolyline = new IfcPolyline(wallAllPoints);
        IfcArbitraryClosedProfileDef wallArbitraryClosedProfileDef = new IfcArbitraryClosedProfileDef(
                new IfcProfileTypeEnum ("AREA"), null, wallPolyline);
        IfcCartesianPoint ifcCartesianPointOriginWallRepresentation = createCartesianPoint3D(0.0f,0.0f,0.0f);
        IfcAxis2Placement3D ifcAxis2Placement3DWallRepresentation = new IfcAxis2Placement3D(
                ifcCartesianPointOriginWallRepresentation, null, null);
        IfcDirection ifcWallExtrudedDirection = createDirection3D(0.0f,0.0f,1.0f);
        IfcLengthMeasure lengthExtrusion = new IfcLengthMeasure(wallHeight);
        IfcExtrudedAreaSolid extrudedWall = new IfcExtrudedAreaSolid(wallArbitraryClosedProfileDef,
                ifcAxis2Placement3DWallRepresentation,ifcWallExtrudedDirection,new IfcPositiveLengthMeasure(lengthExtrusion));
        SET<IfcRepresentationItem> ifcWallRepresentation3DItem = new SET<IfcRepresentationItem>();
        ifcWallRepresentation3DItem.add(extrudedWall);
        IfcShapeRepresentation ifcWallSweptSolidRepresentation = new IfcShapeRepresentation(getGeometricRepresentationContext(),
                new IfcLabel ("Body",true), new IfcLabel("SweptSolid",true), ifcWallRepresentation3DItem);
        ifcWallRepresentationsList.add(ifcWallSweptSolidRepresentation);

        // Create the wallStandardCase
        IfcProductDefinitionShape ifcWallDefinitionShape = new IfcProductDefinitionShape(null,null,ifcWallRepresentationsList);
        IfcWallStandardCase ifcWallStandardCase = new IfcWallStandardCase(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(), new IfcLabel(nameWall, true),
                new IfcText(nameWall + " / etage = " + nameBuildingStorey, true), null, ifcLocalPlacementWall,
                ifcWallDefinitionShape, null);

        // Create relation IfcBuildingStorey --> IfcWallStandardCase
        IfcRelContainedInSpatialStructure relContainedInSpatialStructure = this.getRelContainedInSpatialStructure(nameBuildingStorey);
        if(relContainedInSpatialStructure == null){
            SET<IfcProduct> relatedObject;
            relatedObject = new SET<IfcProduct>();
            relatedObject.add(ifcWallStandardCase);
            IfcRelContainedInSpatialStructure relationBuildingStoreyToWall;
            relationBuildingStoreyToWall = new IfcRelContainedInSpatialStructure(new IfcGloballyUniqueId(
                    ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
                    new IfcLabel("BuildingStoreyContainer",true),new IfcText("BuildingStoreyContainer for Wall",true),
                    relatedObject, buildingStorey);
            ifcModel.addIfcObject(relationBuildingStoreyToWall);
        }
        else{
            relContainedInSpatialStructure.addRelatedElements(ifcWallStandardCase);
        }

        // add new Ifc-objects to the model
        ifcModel.addIfcObject(ifcWallStandardCase);
        ifcModel.addIfcObject(ifcLocalPlacementWall);
        ifcModel.addIfcObject(ifcAxis2Placement3DWall);
        ifcModel.addIfcObject(ifcCartesianPointOriginWall);
        ifcModel.addIfcObject(ifcDirectionZAxisWall);
        ifcModel.addIfcObject(ifcDirectionXDirectionWall);

        ifcModel.addIfcObject(ifcWallDefinitionShape);

        ifcModel.addIfcObject(ifcWallCurve2DRepresentation);
        ifcModel.addIfcObject(wallTrimmedCurve);
        ifcModel.addIfcObject(wallAxisLine);
        ifcModel.addIfcObject(ifcWallPoints2D0);
        ifcModel.addIfcObject(wallAxisVector);
        ifcModel.addIfcObject(ifcWallAxisDirection);
        ifcModel.addIfcObject(ifcWallPoints2D1);
        ifcModel.addIfcObject(ifcWallPoints2D2);

        ifcModel.addIfcObject(ifcWallSweptSolidRepresentation);
        ifcModel.addIfcObject(extrudedWall);
        ifcModel.addIfcObject(ifcAxis2Placement3DWallRepresentation);
        ifcModel.addIfcObject(ifcCartesianPointOriginWallRepresentation);
        ifcModel.addIfcObject(ifcWallExtrudedDirection);
        ifcModel.addIfcObject(wallArbitraryClosedProfileDef);
        ifcModel.addIfcObject(wallPolyline);
        ifcModel.addIfcObject(wallCartesianPoint1);
        ifcModel.addIfcObject(wallCartesianPoint2);
        ifcModel.addIfcObject(wallCartesianPoint3);
        ifcModel.addIfcObject(wallCartesianPoint4);
    }

    public IfcWallStandardCase addWall(String nameBuildingStorey, String nameWall, IfcCartesianPoint pointA, IfcCartesianPoint pointB, float wallThickness, float wallHeight){
        IfcBuildingStorey buildingStorey = getBuildingStorey(nameBuildingStorey);
        float xPointA = (float) pointA.getCoordinates().get(0).value;
        float yPointA = (float)pointA.getCoordinates().get(1).value;
        float xPointB = (float)pointB.getCoordinates().get(0).value;
        float yPointB = (float)pointB.getCoordinates().get(1).value;
        float wallLength = (float)Math.sqrt((xPointB-xPointA)*(xPointB-xPointA)+(yPointB-yPointA)*(yPointB-yPointA));
        float dirX = (xPointB-xPointA)/wallLength;
        float dirY = (yPointB-yPointA)/wallLength;
        IfcCartesianPoint ifcCartesianPointOriginWall = createCartesianPoint3D(xPointA,yPointA,0.0f);
        IfcDirection ifcDirectionZAxisWall = createDirection3D(0.0f,0.0f,1.0f);
        IfcDirection ifcDirectionXDirectionWall = createDirection3D(dirX,dirY,0.0f);
        IfcAxis2Placement3D ifcAxis2Placement3DWall = new IfcAxis2Placement3D(
                ifcCartesianPointOriginWall, ifcDirectionZAxisWall, ifcDirectionXDirectionWall);
        IfcLocalPlacement ifcLocalPlacementWall = new IfcLocalPlacement(buildingStorey.getObjectPlacement(),
                ifcAxis2Placement3DWall);
        LIST<IfcRepresentation> ifcWallRepresentationsList = new LIST<IfcRepresentation>();

        // First representation : Geometric representation (2D)
        IfcCartesianPoint ifcWallPoints2D1 = createCartesianPoint2D(0.0f,0.0f);
        IfcCartesianPoint ifcWallPoints2D2 = createCartesianPoint2D(wallLength,0.0f);
        IfcDirection ifcWallAxisDirection = createDirection2D(1.0f,0.0f);
        IfcVector wallAxisVector = new IfcVector(ifcWallAxisDirection,new IfcLengthMeasure(wallLength));
        IfcCartesianPoint ifcWallPoints2D0 = createCartesianPoint2D(0.0f,0.0f);
        IfcLine wallAxisLine =new IfcLine(ifcWallPoints2D0,wallAxisVector);
        SET<IfcTrimmingSelect> Trim1 = new SET<IfcTrimmingSelect>();
        SET<IfcTrimmingSelect> Trim2 = new SET<IfcTrimmingSelect>();
        Trim1.add(ifcWallPoints2D1);
        Trim2.add(ifcWallPoints2D2);
        IfcTrimmedCurve wallTrimmedCurve = new IfcTrimmedCurve(wallAxisLine,Trim1,Trim2,
                new BOOLEAN(true),new IfcTrimmingPreference("CARTESIAN"));
        SET<IfcRepresentationItem> ifcWallRepresentation2DItem = new SET<IfcRepresentationItem>();
        ifcWallRepresentation2DItem.add(wallTrimmedCurve);
        IfcShapeRepresentation ifcWallCurve2DRepresentation = new IfcShapeRepresentation(getGeometricRepresentationContext(),
                new IfcLabel ("Axis",true), new IfcLabel("Curve2D",true), ifcWallRepresentation2DItem);
        ifcWallRepresentationsList.add(ifcWallCurve2DRepresentation);

        // Second representation : SweptSolid representation (3D)
        LIST<IfcCartesianPoint> wallAllPoints = new LIST<IfcCartesianPoint>();
        IfcCartesianPoint wallCartesianPoint1 = createCartesianPoint2D(0.0f,wallThickness/2);
        wallAllPoints.add(wallCartesianPoint1);
        IfcCartesianPoint wallCartesianPoint2 = createCartesianPoint2D(wallLength,wallThickness/2);
        wallAllPoints.add(wallCartesianPoint2);
        IfcCartesianPoint wallCartesianPoint3 = createCartesianPoint2D(wallLength,-wallThickness/2);
        wallAllPoints.add(wallCartesianPoint3);
        IfcCartesianPoint wallCartesianPoint4 = createCartesianPoint2D(0.0f,-wallThickness/2);
        wallAllPoints.add(wallCartesianPoint4);
        wallAllPoints.add(wallCartesianPoint1);
        IfcPolyline wallPolyline = new IfcPolyline(wallAllPoints);
        IfcArbitraryClosedProfileDef wallArbitraryClosedProfileDef = new IfcArbitraryClosedProfileDef(
                new IfcProfileTypeEnum ("AREA"), null, wallPolyline);
        IfcCartesianPoint ifcCartesianPointOriginWallRepresentation = createCartesianPoint3D(0.0f,0.0f,0.0f);
        IfcAxis2Placement3D ifcAxis2Placement3DWallRepresentation = new IfcAxis2Placement3D(
                ifcCartesianPointOriginWallRepresentation, null, null);
        IfcDirection ifcWallExtrudedDirection = createDirection3D(0.0f,0.0f,1.0f);
        IfcLengthMeasure lengthExtrusion = new IfcLengthMeasure(wallHeight);
        IfcExtrudedAreaSolid extrudedWall = new IfcExtrudedAreaSolid(wallArbitraryClosedProfileDef,
                ifcAxis2Placement3DWallRepresentation,ifcWallExtrudedDirection,new IfcPositiveLengthMeasure(lengthExtrusion));
        SET<IfcRepresentationItem> ifcWallRepresentation3DItem = new SET<IfcRepresentationItem>();
        ifcWallRepresentation3DItem.add(extrudedWall);
        IfcShapeRepresentation ifcWallSweptSolidRepresentation = new IfcShapeRepresentation(getGeometricRepresentationContext(),
                new IfcLabel ("Body",true), new IfcLabel("SweptSolid",true), ifcWallRepresentation3DItem);
        ifcWallRepresentationsList.add(ifcWallSweptSolidRepresentation);

        // Create the wallStandardCase
        IfcProductDefinitionShape ifcWallDefinitionShape = new IfcProductDefinitionShape(null,null,ifcWallRepresentationsList);
        IfcWallStandardCase ifcWallStandardCase = new IfcWallStandardCase(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(), new IfcLabel(nameWall, true),
                new IfcText(nameWall + " / etage = " + nameBuildingStorey, true), null, ifcLocalPlacementWall,
                ifcWallDefinitionShape, null);

        // Create relation IfcBuildingStorey --> IfcWallStandardCase
        IfcRelContainedInSpatialStructure relContainedInSpatialStructure = this.getRelContainedInSpatialStructure(nameBuildingStorey);
        if(relContainedInSpatialStructure == null){
            SET<IfcProduct> relatedObject;
            relatedObject = new SET<IfcProduct>();
            relatedObject.add(ifcWallStandardCase);
            IfcRelContainedInSpatialStructure relationBuildingStoreyToWall;
            relationBuildingStoreyToWall = new IfcRelContainedInSpatialStructure(new IfcGloballyUniqueId(
                    ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
                    new IfcLabel("BuildingStoreyContainer",true),new IfcText("BuildingStoreyContainer for Wall",true),
                    relatedObject, buildingStorey);
            ifcModel.addIfcObject(relationBuildingStoreyToWall);
        }
        else{
            relContainedInSpatialStructure.addRelatedElements(ifcWallStandardCase);
        }

        // add new Ifc-objects to the model
        ifcModel.addIfcObject(ifcWallStandardCase);
        ifcModel.addIfcObject(ifcLocalPlacementWall);
        ifcModel.addIfcObject(ifcAxis2Placement3DWall);
        ifcModel.addIfcObject(ifcCartesianPointOriginWall);
        ifcModel.addIfcObject(ifcDirectionZAxisWall);
        ifcModel.addIfcObject(ifcDirectionXDirectionWall);

        ifcModel.addIfcObject(ifcWallDefinitionShape);

        ifcModel.addIfcObject(ifcWallCurve2DRepresentation);
        ifcModel.addIfcObject(wallTrimmedCurve);
        ifcModel.addIfcObject(wallAxisLine);
        ifcModel.addIfcObject(ifcWallPoints2D0);
        ifcModel.addIfcObject(wallAxisVector);
        ifcModel.addIfcObject(ifcWallAxisDirection);
        ifcModel.addIfcObject(ifcWallPoints2D1);
        ifcModel.addIfcObject(ifcWallPoints2D2);

        ifcModel.addIfcObject(ifcWallSweptSolidRepresentation);
        ifcModel.addIfcObject(extrudedWall);
        ifcModel.addIfcObject(ifcAxis2Placement3DWallRepresentation);
        ifcModel.addIfcObject(ifcCartesianPointOriginWallRepresentation);
        ifcModel.addIfcObject(ifcWallExtrudedDirection);
        ifcModel.addIfcObject(wallArbitraryClosedProfileDef);
        ifcModel.addIfcObject(wallPolyline);
        ifcModel.addIfcObject(wallCartesianPoint1);
        ifcModel.addIfcObject(wallCartesianPoint2);
        ifcModel.addIfcObject(wallCartesianPoint3);
        ifcModel.addIfcObject(wallCartesianPoint4);

        return ifcWallStandardCase;
//        ArrayList<MaterialTypeEnum> materialTypeEnumArrayList = new ArrayList<MaterialTypeEnum>();
//        materialTypeEnumArrayList.add(MaterialTypeEnum.BRIQUE);
//        materialTypeEnumArrayList.add(MaterialTypeEnum.PIERRE);
//        this.addMaterialLayer(ifcWallStandardCase, materialTypeEnumArrayList);
//        addPropertyTypeWall(ifcWallStandardCase, TypeMurEnum.INCONNUE);
//        addPropertyTypeIsolationWall(ifcWallStandardCase, TypeIsolationMurEnum.INCONNUE);
//        addPropertyDateIsolationWall(ifcWallStandardCase, DateIsolationMurEnum.INCONNUE);
//        addPropertyTransmittanceThermiqueWall(ifcWallStandardCase,"INCONNUE");
    }

    // Permet d'ajouter un slab à un IfcModel
    public void addSlab(String nameBuildingStorey, LIST<IfcCartesianPoint> listSlabCartesianPoint){
        IfcBuildingStorey buildingStorey = getBuildingStorey(nameBuildingStorey);
        IfcCartesianPoint ifcCartesianPointOriginSlab = createCartesianPoint3D(0.0f, 0.0f, 0.0f);
        IfcDirection ifcDirectionZAxisLocalSlab = createDirection3D(0.0f, 0.0f, 1.0f);
        IfcDirection ifcDirectionXDirectionLocalSlab = createDirection3D(1.0f, 0.0f, 0.0f);
        IfcAxis2Placement3D ifcAxis2Placement3DSlab = new IfcAxis2Placement3D(
                ifcCartesianPointOriginSlab, ifcDirectionZAxisLocalSlab, ifcDirectionXDirectionLocalSlab);
        IfcLocalPlacement ifcLocalPlacementSlab = new IfcLocalPlacement(buildingStorey.getObjectPlacement(),
                ifcAxis2Placement3DSlab);
        LIST<IfcRepresentation> ifcSlabRepresentationsList = new LIST<IfcRepresentation>();
        LIST<IfcLengthMeasure> lengthMeasureLIST = listSlabCartesianPoint.get(0).getCoordinates();
        IfcCartesianPoint firstCartesianPoint = new IfcCartesianPoint(lengthMeasureLIST);
        listSlabCartesianPoint.add(firstCartesianPoint);

        // Slab representation : SweptSolid representation (3D)
        IfcPolyline slabPolyline = new IfcPolyline(listSlabCartesianPoint);
        IfcArbitraryClosedProfileDef slabArbitraryClosedProfileDef = new IfcArbitraryClosedProfileDef(
                new IfcProfileTypeEnum ("AREA"), null, slabPolyline);
        IfcCartesianPoint ifcCartesianPointOriginSlabRepresentation = createCartesianPoint3D(0.0f,0.0f,0.0f);
        IfcAxis2Placement3D ifcAxis2Placement3DSlabRepresentation = new IfcAxis2Placement3D(
                ifcCartesianPointOriginSlabRepresentation, null, null);
        IfcDirection ifcSlabExtrudedDirection = createDirection3D(0.0f,0.0f,1.0f);
        IfcLengthMeasure lengthExtrusion = new IfcLengthMeasure(0.2);
        IfcExtrudedAreaSolid extrudedSlab = new IfcExtrudedAreaSolid(slabArbitraryClosedProfileDef,
                ifcAxis2Placement3DSlabRepresentation,ifcSlabExtrudedDirection,new IfcPositiveLengthMeasure(lengthExtrusion));
        SET<IfcRepresentationItem> ifcSlabRepresentation3DItem = new SET<IfcRepresentationItem>();
        ifcSlabRepresentation3DItem.add(extrudedSlab);
        IfcShapeRepresentation ifcSlabSweptSolidRepresentation = new IfcShapeRepresentation(getGeometricRepresentationContext(),
                new IfcLabel ("Body",true), new IfcLabel("SweptSolid",true), ifcSlabRepresentation3DItem);
        ifcSlabRepresentationsList.add(ifcSlabSweptSolidRepresentation);

        // Create the Slab
        IfcProductDefinitionShape ifcSlabDefinitionShape = new IfcProductDefinitionShape(null,null,ifcSlabRepresentationsList);
        IfcSlab ifcSlab = new IfcSlab(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(), new IfcLabel("Slab", true),
                new IfcText("Slab = floor / etage = " + nameBuildingStorey, true), null, ifcLocalPlacementSlab,
                ifcSlabDefinitionShape, null, new IfcSlabTypeEnum("FLOOR"));

        // Create relation IfcBuildingStorey --> IfcWallStandardCase
        IfcRelContainedInSpatialStructure relContainedInSpatialStructure = this.getRelContainedInSpatialStructure(nameBuildingStorey);
        if(relContainedInSpatialStructure == null){
            SET<IfcProduct> relatedObject;
            relatedObject = new SET<IfcProduct>();
            relatedObject.add(ifcSlab);
            IfcRelContainedInSpatialStructure relationBuildingStoreyToSlab;
            relationBuildingStoreyToSlab = new IfcRelContainedInSpatialStructure(new IfcGloballyUniqueId(
                    ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
                    new IfcLabel("BuildingStoreyContainer",true),new IfcText("BuildingStoreyContainer for Slab",true),
                    relatedObject, buildingStorey);
            ifcModel.addIfcObject(relationBuildingStoreyToSlab);
        }
        else{
            relContainedInSpatialStructure.addRelatedElements(ifcSlab);
        }
//        ArrayList<MaterialTypeEnum> materialTypeEnumArrayList = new ArrayList<MaterialTypeEnum>();
//        materialTypeEnumArrayList.add(MaterialTypeEnum.PARQUET);
//        this.addMaterialLayerToSlab(ifcSlab, materialTypeEnumArrayList);
//        this.addPropertyLocationSlab(ifcSlab, LocationSlabEnum.INCONNUE);

        // add new Ifc-objects to the model
        ifcModel.addIfcObject(ifcSlab);
        ifcModel.addIfcObject(ifcLocalPlacementSlab);
        ifcModel.addIfcObject(ifcAxis2Placement3DSlab);
        ifcModel.addIfcObject(ifcCartesianPointOriginSlab);
        ifcModel.addIfcObject(ifcDirectionZAxisLocalSlab);
        ifcModel.addIfcObject(ifcDirectionXDirectionLocalSlab);

        ifcModel.addIfcObject(ifcSlabDefinitionShape);

        ifcModel.addIfcObject(ifcSlabSweptSolidRepresentation);
        ifcModel.addIfcObject(extrudedSlab);
        ifcModel.addIfcObject(ifcAxis2Placement3DSlabRepresentation);
        ifcModel.addIfcObject(ifcCartesianPointOriginSlabRepresentation);
        ifcModel.addIfcObject(ifcSlabExtrudedDirection);
        ifcModel.addIfcObject(slabArbitraryClosedProfileDef);
        ifcModel.addIfcObject(slabPolyline);
        for(IfcCartesianPoint actualPoint : listSlabCartesianPoint){
            ifcModel.addIfcObject(actualPoint);
        }
    }

    // Permet d'ajouter un opening à un wall
    public void addOpeningToWall (String nameOpening,IfcWallStandardCase wall, float openingWidth, float openingHeight, float xLocal, float zLocal){

        // Calcul of openingThickness = wallThickness
        float openingThickness = this.getWallThickness(wall);

        IfcCartesianPoint localPointOpening = createCartesianPoint3D(xLocal, -openingThickness / 2, zLocal);
        IfcDirection zLocalOpening = createDirection3D(0.0f,0.0f,1.0f);
        IfcDirection xLocalOpening = createDirection3D(1.0f, 0.0f, 0.0f);
        IfcAxis2Placement3D placementOpening = new IfcAxis2Placement3D(
                localPointOpening, zLocalOpening, xLocalOpening);
        IfcLocalPlacement localPlacementOpening = new IfcLocalPlacement(wall.getObjectPlacement(),
                placementOpening);
        LIST<IfcRepresentation> openingRepresentationsList = new LIST<IfcRepresentation>();

        // Opening geometry with extruded area solid placement
        IfcDirection zLocalExtrusion = createDirection3D(0.0f, 1.0f, 0.0f);
        IfcDirection xLocalExtrusion = createDirection3D(0.0f,0.0f,1.0f);
        IfcCartesianPoint centerOpening = createCartesianPoint3D(openingWidth/2,0.0f,openingHeight/2);
        IfcAxis2Placement3D placementCenterOpening = new IfcAxis2Placement3D(
                centerOpening, zLocalExtrusion, xLocalExtrusion);
        IfcDirection zLocalRectangle = createDirection2D(1.0f, 0.0f);
        IfcCartesianPoint originOpening = createCartesianPoint2D(0.0f, 0.0f);
        IfcAxis2Placement2D placementRectangle = new IfcAxis2Placement2D(
                originOpening, zLocalRectangle);
        IfcRectangleProfileDef rectangle = new IfcRectangleProfileDef(new IfcProfileTypeEnum("AREA"),
                null,placementRectangle,new IfcPositiveLengthMeasure(new IfcLengthMeasure(openingHeight)),
                new IfcPositiveLengthMeasure(new IfcLengthMeasure(openingWidth)));
        IfcDirection openingExtrusionDirection = createDirection3D(0.0f,0.0f,1.0f);
        IfcExtrudedAreaSolid extrudedOpening = new IfcExtrudedAreaSolid(rectangle, placementCenterOpening,
                openingExtrusionDirection,new IfcPositiveLengthMeasure(new IfcLengthMeasure(openingThickness)));
        SET<IfcRepresentationItem> openingRepresentation3DItem = new SET<IfcRepresentationItem>();
        openingRepresentation3DItem.add(extrudedOpening);
        IfcShapeRepresentation openingSweptSolidRepresentation = new IfcShapeRepresentation(getGeometricRepresentationContext(),
                new IfcLabel ("Body",true), new IfcLabel("SweptSolid",true), openingRepresentation3DItem);
        openingRepresentationsList.add(openingSweptSolidRepresentation);
        IfcProductDefinitionShape openingDefinitionShape = new IfcProductDefinitionShape(null,null,openingRepresentationsList);

        // Create the Opening
        IfcOpeningElement opening = new IfcOpeningElement(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(), new IfcLabel(nameOpening, true),
                new IfcText(nameOpening + " / wall = " + wall.getName().getDecodedValue(), true), null, localPlacementOpening,
                openingDefinitionShape, null);

        // Create relation IfcWallStandardCase --> IfcOpeningElement
        IfcRelVoidsElement relWallToOpening = new IfcRelVoidsElement(new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(),new IfcLabel("Wall Container", true),
                new IfcText("WallContainer for OpeningElement",true),wall,opening);
        ifcModel.addIfcObject(relWallToOpening);

        // add new Ifc-objects to the model
        ifcModel.addIfcObject(opening);
        ifcModel.addIfcObject(localPlacementOpening);
        ifcModel.addIfcObject(localPointOpening);
        ifcModel.addIfcObject(zLocalOpening);
        ifcModel.addIfcObject(xLocalOpening);
        ifcModel.addIfcObject(placementOpening);

        ifcModel.addIfcObject(openingDefinitionShape);

        ifcModel.addIfcObject(openingSweptSolidRepresentation);
        ifcModel.addIfcObject(extrudedOpening);
        ifcModel.addIfcObject(rectangle);
        ifcModel.addIfcObject(placementRectangle);
        ifcModel.addIfcObject(originOpening);
        ifcModel.addIfcObject(zLocalRectangle);
        ifcModel.addIfcObject(placementCenterOpening);
        ifcModel.addIfcObject(centerOpening);
        ifcModel.addIfcObject(zLocalExtrusion);
        ifcModel.addIfcObject(xLocalExtrusion);
        ifcModel.addIfcObject(openingExtrusionDirection);
    }

    // Permet d'ajouter un opening à slab
    public void addOpeningToSlab (String nameOpening,IfcSlab slab, float openingWidth, float openingHeight, float xLocal, float yLocal){

        // Calcul of openingThickness = slabThickness
        float openingThickness = this.getSlabThickness(slab);

        IfcCartesianPoint localPointOpening = createCartesianPoint3D(xLocal,yLocal,0.0f);
        IfcDirection zLocalOpening = createDirection3D(0.0f,0.0f,1.0f);
        IfcDirection xLocalOpening = createDirection3D(1.0f, 0.0f, 0.0f);
        IfcAxis2Placement3D placementOpening = new IfcAxis2Placement3D(
                localPointOpening, zLocalOpening, xLocalOpening);
        IfcLocalPlacement localPlacementOpening = new IfcLocalPlacement(slab.getObjectPlacement(),
                placementOpening);
        LIST<IfcRepresentation> openingRepresentationsList = new LIST<IfcRepresentation>();

        // Opening geometry with extruded area solid placement
        IfcDirection zLocalExtrusion = createDirection3D(0.0f, 0.0f, 1.0f);
        IfcDirection xLocalExtrusion = createDirection3D(1.0f, 0.0f, 0.0f);
        IfcCartesianPoint centerOpening = createCartesianPoint3D(openingWidth / 2, openingHeight / 2, 0.0f);
        IfcAxis2Placement3D placementCenterOpening = new IfcAxis2Placement3D(
                centerOpening, zLocalExtrusion, xLocalExtrusion);
        IfcDirection zLocalRectangle = createDirection2D(0.0f, 1.0f);
        IfcCartesianPoint originOpening = createCartesianPoint2D(0.0f, 0.0f);
        IfcAxis2Placement2D placementRectangle = new IfcAxis2Placement2D(
                originOpening, zLocalRectangle);
        IfcRectangleProfileDef rectangle = new IfcRectangleProfileDef(new IfcProfileTypeEnum("AREA"),
                null,placementRectangle,new IfcPositiveLengthMeasure(new IfcLengthMeasure(openingHeight)),
                new IfcPositiveLengthMeasure(new IfcLengthMeasure(openingWidth)));
        IfcDirection openingExtrusionDirection = createDirection3D(0.0f, 0.0f, 1.0f);
        IfcExtrudedAreaSolid extrudedOpening = new IfcExtrudedAreaSolid(rectangle, placementCenterOpening,
                openingExtrusionDirection,new IfcPositiveLengthMeasure(new IfcLengthMeasure(openingThickness)));
        SET<IfcRepresentationItem> openingRepresentation3DItem = new SET<IfcRepresentationItem>();
        openingRepresentation3DItem.add(extrudedOpening);
        IfcShapeRepresentation openingSweptSolidRepresentation = new IfcShapeRepresentation(getGeometricRepresentationContext(),
                new IfcLabel ("Body",true), new IfcLabel("SweptSolid",true), openingRepresentation3DItem);
        openingRepresentationsList.add(openingSweptSolidRepresentation);
        IfcProductDefinitionShape openingDefinitionShape = new IfcProductDefinitionShape(null,null,openingRepresentationsList);

        // Create the Opening
        IfcOpeningElement opening = new IfcOpeningElement(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(), new IfcLabel(nameOpening, true),
                new IfcText(nameOpening + " / wall = " + slab.getName().getDecodedValue(), true), null, localPlacementOpening,
                openingDefinitionShape, null);

        // Create relation IfcSlab --> IfcOpeningElement
        IfcRelVoidsElement relSlabToOpening = new IfcRelVoidsElement(new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(),new IfcLabel("Slab Container", true),
                new IfcText("SlabContainer for OpeningElement",true),slab,opening);
        ifcModel.addIfcObject(relSlabToOpening);

        // add new Ifc-objects to the model
        ifcModel.addIfcObject(opening);
        ifcModel.addIfcObject(localPlacementOpening);
        ifcModel.addIfcObject(localPointOpening);
        ifcModel.addIfcObject(zLocalOpening);
        ifcModel.addIfcObject(xLocalOpening);
        ifcModel.addIfcObject(placementOpening);

        ifcModel.addIfcObject(openingDefinitionShape);

        ifcModel.addIfcObject(openingSweptSolidRepresentation);
        ifcModel.addIfcObject(extrudedOpening);
        ifcModel.addIfcObject(rectangle);
        ifcModel.addIfcObject(placementRectangle);
        ifcModel.addIfcObject(originOpening);
        ifcModel.addIfcObject(zLocalRectangle);
        ifcModel.addIfcObject(placementCenterOpening);
        ifcModel.addIfcObject(centerOpening);
        ifcModel.addIfcObject(zLocalExtrusion);
        ifcModel.addIfcObject(xLocalExtrusion);
        ifcModel.addIfcObject(openingExtrusionDirection);
    }

    // Permet d'ajouter un opening
    public void addOpening (String nameOpening,IfcProduct product, float openingWidth, float openingHeight, float xLocal, float yzLocal){
        if (product instanceof IfcWallStandardCase){
            addOpeningToWall (nameOpening, (IfcWallStandardCase)product, openingWidth, openingHeight, xLocal, yzLocal);
        }
        else if (product instanceof IfcSlab){
            addOpeningToSlab(nameOpening, (IfcSlab) product, openingWidth, openingHeight, xLocal, yzLocal);
        }
    }

    // Permet d'ajouter une door à un produit (wall ou slab)
    public void addDoor (String nameDoor,IfcProduct product, float doorWidth, float doorHeight, float xLocal, float zLocal){
        IfcBuildingStorey buildingStorey = getBuildingStorey(product);
        addOpening(nameDoor, product, doorWidth, doorHeight, xLocal, zLocal);
        IfcOpeningElement opening = getOpening(nameDoor);

//        // Door style definitions
//        SET<IfcPropertySetDefinition> propertySetDefinitions = new SET<IfcPropertySetDefinition>();
//
//        IfcDoorLiningProperties doorLiningProperties = new IfcDoorLiningProperties(
//                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
//                null, null, new IfcPositiveLengthMeasure(new IfcLengthMeasure(0.12)), new IfcPositiveLengthMeasure(new IfcLengthMeasure(0.0f5)),
//                null, null, null, null, null, null, null, null, null);
//
//        IfcDoorPanelProperties doorPanelProperties = new IfcDoorPanelProperties(
//                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
//                null, null, new IfcPositiveLengthMeasure(new IfcLengthMeasure(0.0f5)), new IfcDoorPanelOperationEnum("SWINGING"),
//                new IfcNormalisedRatioMeasure(new IfcRatioMeasure(1.0f)), new IfcDoorPanelPositionEnum("LEFT"), null);
//
//        propertySetDefinitions.add(doorPanelProperties);
//        propertySetDefinitions.add(doorLiningProperties);
//
//        IfcDoorStyle doorStyle = new IfcDoorStyle(
//                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
//                new IfcLabel("Standard", true), null, new IfcLabel(), propertySetDefinitions, null, new IfcLabel(),
//                new IfcDoorStyleOperationEnum("SINGLE_SWING_LEFT"), new IfcDoorStyleConstructionEnum("NOTDEFINED"),
//                new BOOLEAN(true), new BOOLEAN(false));

        // Door definition
        IfcCartesianPoint localPointDoor = createCartesianPoint3D(0.0f,0.0f,0.0f);
        IfcDirection xLocalDoor = createDirection3D(1.0f, 0.0f, 0.0f);
        IfcDirection zLocalDoor = createDirection3D(0.0f,0.0f,1.0f);
        IfcAxis2Placement3D placementDoor = new IfcAxis2Placement3D(
                localPointDoor, xLocalDoor, zLocalDoor);
        IfcLocalPlacement localPlacementDoor = new IfcLocalPlacement(opening.getObjectPlacement(),
                placementDoor);

        // Create the door
        IfcDoor door = new IfcDoor(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(), new IfcLabel(nameDoor, true),
                new IfcText("", true), null, localPlacementDoor,
                null, null, new IfcPositiveLengthMeasure(new IfcLengthMeasure(doorHeight)), new IfcPositiveLengthMeasure(new IfcLengthMeasure(doorWidth)));

        // Create relation buildingStorey -> door
        IfcRelContainedInSpatialStructure relContainedInSpatialStructure = this.getRelContainedInSpatialStructure(buildingStorey.getName().getDecodedValue());
        if(relContainedInSpatialStructure == null){
            SET<IfcProduct> relatedObject;
            relatedObject = new SET<IfcProduct>();
            relatedObject.add(door);
            IfcRelContainedInSpatialStructure relationBuildingStoreyToDoor;
            relationBuildingStoreyToDoor = new IfcRelContainedInSpatialStructure(new IfcGloballyUniqueId(
                    ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
                    new IfcLabel("BuildingStoreyContainer",true),new IfcText("BuildingStoreyContainer for Door",true),
                    relatedObject, buildingStorey);
            ifcModel.addIfcObject(relationBuildingStoreyToDoor);
        }
        else{
            relContainedInSpatialStructure.addRelatedElements(door);
        }

        // Create relation IfcOpeningElement <-> IfcDoor
        IfcRelFillsElement relFillsElement = new IfcRelFillsElement(new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(),new IfcLabel("Opening Container", true),
                new IfcText("OpeningContainer for Door",true),opening,door);

//        // Create relation IfcDoor <-> IfcDoorStyle
//        SET<IfcObject> ifcDoorSET = new SET<IfcObject>();
//        ifcDoorSET.add(door);
//        IfcRelDefinesByType relDefinesByType = new IfcRelDefinesByType(new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
//                ifcModel.getIfcProject().getOwnerHistory(),new IfcLabel("Door Container", true),
//                new IfcText("DoorContainer for DoorStyle",true),ifcDoorSET,doorStyle);

        // add new Ifc-objects to the model
        ifcModel.addIfcObject(relFillsElement);

        ifcModel.addIfcObject(door);
        ifcModel.addIfcObject(localPlacementDoor);
        ifcModel.addIfcObject(placementDoor);
        ifcModel.addIfcObject(localPointDoor);
        ifcModel.addIfcObject(zLocalDoor);
        ifcModel.addIfcObject(xLocalDoor);


//        ifcModel.addIfcObject(relDefinesByType);
//        ifcModel.addIfcObject(doorStyle);
//        ifcModel.addIfcObject(doorLiningProperties);
//        ifcModel.addIfcObject(doorPanelProperties);
    }

    // Permet d'ajouter une window à un produit (wall ou slab)
    public void addWindow (String nameWindow,IfcProduct product, float windowWidth, float windowHeight, float xLocal, float zLocal){

        IfcBuildingStorey buildingStorey = getBuildingStorey(product);
        addOpening(nameWindow,product,windowWidth,windowHeight,xLocal,zLocal);
        IfcOpeningElement opening = getOpening(nameWindow);

//        // Window style definitions
//        SET<IfcPropertySetDefinition> propertySetDefinitions = new SET<IfcPropertySetDefinition>();
//
//        IfcWindowLiningProperties windowLiningProperties = new IfcWindowLiningProperties(
//                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
//                null, null, new IfcPositiveLengthMeasure(new IfcLengthMeasure(0.1)), new IfcPositiveLengthMeasure(new IfcLengthMeasure(0.0f5)),
//                null, null, null, null, null, null, null);
//
//        IfcWindowPanelProperties windowPanelProperties = new IfcWindowPanelProperties(
//                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
//                null, null, new IfcWindowPanelOperationEnum("SIDEHUNGLEFTHAND"), new IfcWindowPanelPositionEnum("NOTDEFINED"),
//                new IfcPositiveLengthMeasure(new IfcLengthMeasure(0.5)), new IfcPositiveLengthMeasure(new IfcLengthMeasure(0.5)), null);
//
//        propertySetDefinitions.add(windowPanelProperties);
//        propertySetDefinitions.add(windowLiningProperties);
//
//        IfcWindowStyle windowStyle = new IfcWindowStyle(
//                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
//                new IfcLabel("Standard", true), null, new IfcLabel(), propertySetDefinitions, null, new IfcLabel(),
//                new IfcWindowStyleConstructionEnum("NOTDEFINED"), new IfcWindowStyleOperationEnum("SINGLE_PANEL"),
//                new BOOLEAN(true), new BOOLEAN(false));

        // Window definition
        IfcCartesianPoint localPointWindow = createCartesianPoint3D(0.0f,0.05f,0.01f);
        IfcDirection xLocalWindow = createDirection3D(1.0f,0.0f,0.0f);
        IfcDirection zLocalWindow = createDirection3D(0.0f,0.0f,1.0f);
        IfcAxis2Placement3D placementWindow = new IfcAxis2Placement3D(
                localPointWindow, zLocalWindow, xLocalWindow);
        IfcLocalPlacement localPlacementWindow = new IfcLocalPlacement(opening.getObjectPlacement(),
                placementWindow);

        // Create the window
        IfcWindow window = new IfcWindow(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(), new IfcLabel(nameWindow, true),
                new IfcText("", true), new IfcLabel("",true), localPlacementWindow,
                null, null, new IfcPositiveLengthMeasure(new IfcLengthMeasure(windowHeight)), new IfcPositiveLengthMeasure(new IfcLengthMeasure(windowWidth)));

        // Create relation buildingStorey -> window
        IfcRelContainedInSpatialStructure relContainedInSpatialStructure = this.getRelContainedInSpatialStructure(buildingStorey.getName().getDecodedValue());
        if(relContainedInSpatialStructure == null){
            SET<IfcProduct> relatedObject;
            relatedObject = new SET<IfcProduct>();
            relatedObject.add(window);
            IfcRelContainedInSpatialStructure relationBuildingStoreyToWindow;
            relationBuildingStoreyToWindow = new IfcRelContainedInSpatialStructure(new IfcGloballyUniqueId(
                    ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
                    new IfcLabel("BuildingStoreyContainer",true),new IfcText("BuildingStoreyContainer for Window",true),
                    relatedObject, buildingStorey);
            ifcModel.addIfcObject(relationBuildingStoreyToWindow);
        }
        else{
            relContainedInSpatialStructure.addRelatedElements(window);
        }

        // Create relation IfcOpeningElement <-> IfcWindow
        IfcRelFillsElement relFillsElement = new IfcRelFillsElement(new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(),new IfcLabel("Opening Container", true),
                new IfcText("OpeningContainer for Window",true),opening,window);

//        // Create relation IfcWindow <-> IfcWindowStyle
//        SET<IfcObject> ifcWindowSET = new SET<IfcObject>();
//        ifcWindowSET.add(window);
//        IfcRelDefinesByType relDefinesByType = new IfcRelDefinesByType(new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
//                ifcModel.getIfcProject().getOwnerHistory(),new IfcLabel("Window Container", true),
//                new IfcText("WindowContainer for WindowStyle",true),ifcWindowSET,windowStyle);

        // add new Ifc-objects to the model
        ifcModel.addIfcObject(relFillsElement);

        ifcModel.addIfcObject(window);
        ifcModel.addIfcObject(localPlacementWindow);
        ifcModel.addIfcObject(placementWindow);
        ifcModel.addIfcObject(localPointWindow);
        ifcModel.addIfcObject(zLocalWindow);
        ifcModel.addIfcObject(xLocalWindow);

//        ifcModel.addIfcObject(relDefinesByType);
//        ifcModel.addIfcObject(windowStyle);
//        ifcModel.addIfcObject(windowLiningProperties);
//        ifcModel.addIfcObject(windowPanelProperties);

    }

    // Permet d'ajouter une float window à un produit (wall ou slab)
    public void addDoubleWindow (String nameWindow,IfcProduct product, float windowWidth, float windowHeight, float xLocal, float zLocal){

        IfcBuildingStorey buildingStorey = getBuildingStorey(product);
        addOpening(nameWindow,product,windowWidth,windowHeight,xLocal,zLocal);
        IfcOpeningElement opening = getOpening(nameWindow);

        // Window style definitions
        SET<IfcPropertySetDefinition> propertySetDefinitions = new SET<IfcPropertySetDefinition>();

        IfcWindowLiningProperties windowLiningProperties = new IfcWindowLiningProperties(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
                null, null, new IfcPositiveLengthMeasure(new IfcLengthMeasure(0.1)), new IfcPositiveLengthMeasure(new IfcLengthMeasure(0.05f)),
                null, null, null, null, null, null, null);

        IfcWindowPanelProperties windowPanelProperties1 = new IfcWindowPanelProperties(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
                null, null, new IfcWindowPanelOperationEnum("SIDEHUNGLEFTHAND"), new IfcWindowPanelPositionEnum("NOTDEFINED"),
                new IfcPositiveLengthMeasure(new IfcLengthMeasure(0.5)), new IfcPositiveLengthMeasure(new IfcLengthMeasure(0.5)), null);

        IfcWindowPanelProperties windowPanelProperties2 = new IfcWindowPanelProperties(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
                null, null, new IfcWindowPanelOperationEnum("SIDEHUNGRIGHTHAND"), new IfcWindowPanelPositionEnum("NOTDEFINED"),
                new IfcPositiveLengthMeasure(new IfcLengthMeasure(0.5)), new IfcPositiveLengthMeasure(new IfcLengthMeasure(0.5)), null);

        propertySetDefinitions.add(windowLiningProperties);
        propertySetDefinitions.add(windowPanelProperties1);
        propertySetDefinitions.add(windowPanelProperties2);

        IfcWindowStyle windowStyle = new IfcWindowStyle(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
                new IfcLabel("Standard", true), null, new IfcLabel(), propertySetDefinitions, null, new IfcLabel(),
                new IfcWindowStyleConstructionEnum("NOTDEFINED"), new IfcWindowStyleOperationEnum("DOUBLE_PANEL_VERTICAL"),
                new BOOLEAN(true), new BOOLEAN(false));

        // Window definition
        IfcCartesianPoint localPointWindow = createCartesianPoint3D(0.0f,0.05f,1.0f);
        IfcDirection zLocalWindow = createDirection3D(1.0f,0.0f,0.0f);
        IfcDirection xLocalWindow = createDirection3D(0.0f,0.0f,-1.0f);
        IfcAxis2Placement3D placementWindow = new IfcAxis2Placement3D(
                localPointWindow, zLocalWindow, xLocalWindow);
        IfcLocalPlacement localPlacementWindow = new IfcLocalPlacement(opening.getObjectPlacement(),
                placementWindow);

        // Create the window
        IfcWindow window = new IfcWindow(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(), new IfcLabel(nameWindow, true),
                new IfcText("", true), null, localPlacementWindow,
                null, null, new IfcPositiveLengthMeasure(new IfcLengthMeasure(windowHeight)), new IfcPositiveLengthMeasure(new IfcLengthMeasure(windowWidth)));

        // Create relation buildingStorey -> window
        IfcRelContainedInSpatialStructure relContainedInSpatialStructure = this.getRelContainedInSpatialStructure(buildingStorey.getName().getDecodedValue());
        if(relContainedInSpatialStructure == null){
            SET<IfcProduct> relatedObject;
            relatedObject = new SET<IfcProduct>();
            relatedObject.add(window);
            IfcRelContainedInSpatialStructure relationBuildingStoreyToWindow;
            relationBuildingStoreyToWindow = new IfcRelContainedInSpatialStructure(new IfcGloballyUniqueId(
                    ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
                    new IfcLabel("BuildingStoreyContainer",true),new IfcText("BuildingStoreyContainer for Window",true),
                    relatedObject, buildingStorey);
            ifcModel.addIfcObject(relationBuildingStoreyToWindow);
        }
        else{
            relContainedInSpatialStructure.addRelatedElements(window);
        }

        // Create relation IfcOpeningElement <-> IfcWindow
        IfcRelFillsElement relFillsElement = new IfcRelFillsElement(new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(),new IfcLabel("Opening Container", true),
                new IfcText("OpeningContainer for Window",true),opening,window);

        // Create relation IfcWindow <-> IfcWindowStyle
        SET<IfcObject> ifcWindowSET = new SET<IfcObject>();
        ifcWindowSET.add(window);
        IfcRelDefinesByType relDefinesByType = new IfcRelDefinesByType(new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(),new IfcLabel("Window Container", true),
                new IfcText("WindowContainer for WindowStyle",true),ifcWindowSET,windowStyle);

        // add new Ifc-objects to the model
        ifcModel.addIfcObject(relFillsElement);

        ifcModel.addIfcObject(window);
        ifcModel.addIfcObject(localPlacementWindow);
        ifcModel.addIfcObject(placementWindow);
        ifcModel.addIfcObject(localPointWindow);
        ifcModel.addIfcObject(zLocalWindow);
        ifcModel.addIfcObject(xLocalWindow);

        ifcModel.addIfcObject(relDefinesByType);
        ifcModel.addIfcObject(windowStyle);
        ifcModel.addIfcObject(windowLiningProperties);
        ifcModel.addIfcObject(windowPanelProperties1);
        ifcModel.addIfcObject(windowPanelProperties2);
    }

    // Permet d'ajouter un MaterialLayer à un wallStandardCase
    public void addMaterialLayer (IfcWallStandardCase wall, ArrayList<MaterialTypeEnum> listMaterial){
        SET<IfcRoot> relatedObjects = new SET<IfcRoot>();
        relatedObjects.add(wall);
        LIST<IfcMaterialLayer> materialLayerLIST = new LIST<IfcMaterialLayer>();
        float nbMaterial = listMaterial.size();
        float wallThickness = this.getWallThickness(wall);

        for (MaterialTypeEnum actualMaterial:listMaterial){
            IfcMaterial material = new IfcMaterial(new IfcLabel(actualMaterial.toString(),true));
            ifcModel.addIfcObject(material);
            IfcMaterialLayer materialLayer = new IfcMaterialLayer(material,
                    new IfcPositiveLengthMeasure(new IfcLengthMeasure(wallThickness/nbMaterial)),null);
            materialLayerLIST.add(materialLayer);
            ifcModel.addIfcObject(materialLayer);
        }

        IfcMaterialLayerSet materialLayerSet = new IfcMaterialLayerSet(materialLayerLIST,new IfcLabel("LayeredWall",true));
        IfcMaterialLayerSetUsage materialLayerSetUsage = new IfcMaterialLayerSetUsage(materialLayerSet,
                new IfcLayerSetDirectionEnum("AXIS2"),new IfcDirectionSenseEnum("POSITIVE"),new IfcLengthMeasure(-wallThickness/2));

        // Create relation wall <-> material
        IfcRelAssociatesMaterial relAssociatesMaterial = new IfcRelAssociatesMaterial(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(), null,null,relatedObjects,materialLayerSetUsage);

        // add new Ifc-objects to the model
        ifcModel.addIfcObject(relAssociatesMaterial);
        ifcModel.addIfcObject(materialLayerSetUsage);
        ifcModel.addIfcObject(materialLayerSet);
    }

    // Permet d'ajouter un MaterialLayer à un slab
    public void addMaterialLayerToSlab (IfcSlab slab, ArrayList<MaterialTypeEnum> listMaterial){
        SET<IfcRoot> relatedObjects = new SET<IfcRoot>();
        relatedObjects.add(slab);
        LIST<IfcMaterialLayer> materialLayerLIST = new LIST<IfcMaterialLayer>();
        float nbMaterial = listMaterial.size();
        float slabThickness = this.getSlabThickness(slab);

        for (MaterialTypeEnum actualMaterial:listMaterial){
            IfcMaterial material = new IfcMaterial(new IfcLabel(actualMaterial.toString(),true));
            ifcModel.addIfcObject(material);
            IfcMaterialLayer materialLayer = new IfcMaterialLayer(material,
                    new IfcPositiveLengthMeasure(new IfcLengthMeasure(slabThickness/nbMaterial)),null);
            materialLayerLIST.add(materialLayer);
            ifcModel.addIfcObject(materialLayer);
        }

        IfcMaterialLayerSet materialLayerSet = new IfcMaterialLayerSet(materialLayerLIST,new IfcLabel("LayeredSlab",true));
        IfcMaterialLayerSetUsage materialLayerSetUsage = new IfcMaterialLayerSetUsage(materialLayerSet,
                new IfcLayerSetDirectionEnum("AXIS2"),new IfcDirectionSenseEnum("POSITIVE"),new IfcLengthMeasure(-slabThickness/2));

        // Create relation slab <-> material
        IfcRelAssociatesMaterial relAssociatesMaterial = new IfcRelAssociatesMaterial(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(), null,null,relatedObjects,materialLayerSetUsage);

        // add new Ifc-objects to the model
        ifcModel.addIfcObject(relAssociatesMaterial);
        ifcModel.addIfcObject(materialLayerSetUsage);
        ifcModel.addIfcObject(materialLayerSet);
    }

    // Permet de supprimer le placement d'un objet dans un model
    public void deleteObjectPlacement(IfcObjectPlacement objectPlacement){
        if(objectPlacement instanceof IfcLocalPlacement){
            IfcAxis2Placement axis2Placement =((IfcLocalPlacement) objectPlacement).getRelativePlacement();
            ifcModel.removeIfcObject(objectPlacement);
            if (axis2Placement instanceof IfcAxis2Placement2D){
                this.deleteAxis2Placement2D((IfcAxis2Placement2D) axis2Placement);
            }else{
                // axis2Placement instanceof IfcAxis2Placement3D
                this.deleteAxis2Placement3D((IfcAxis2Placement3D) axis2Placement);
            }
        }else{
            // objectPlacement instanceof IfcGridPlacement
        }
    }

    // Permet de supprimer le placement d'un objet dans un model
    public void deleteAxis2Placement3D(IfcAxis2Placement3D axis2Placement3D){
        IfcCartesianPoint cartesianPoint = axis2Placement3D.getLocation();
        IfcDirection axis = axis2Placement3D.getAxis();
        IfcDirection refDirection = axis2Placement3D.getRefDirection();
        ifcModel.removeIfcObject(axis2Placement3D);
        ifcModel.removeIfcObject(cartesianPoint);
        if (axis != null){
            ifcModel.removeIfcObject(axis);
        }
        if (refDirection != null){
            ifcModel.removeIfcObject(refDirection);
        }
    }

    // Permet de supprimer le placement d'un objet dans un model
    public void deleteAxis2Placement2D(IfcAxis2Placement2D axis2Placement2D){
        IfcCartesianPoint cartesianPoint = axis2Placement2D.getLocation();
        IfcDirection refDirection = axis2Placement2D.getRefDirection();
        ifcModel.removeIfcObject(axis2Placement2D);
        ifcModel.removeIfcObject(cartesianPoint);
        ifcModel.removeIfcObject(refDirection);
    }

    // Permet de supprimer une représentation
    public void deleteRepresentation(IfcProductRepresentation productRepresentation){
        if(productRepresentation instanceof IfcProductDefinitionShape){
            IfcProductDefinitionShape productDefinitionShape = (IfcProductDefinitionShape) productRepresentation;
            ifcModel.removeIfcObject(productDefinitionShape);
            LIST<IfcRepresentation> representationLIST = productDefinitionShape.getRepresentations();
            for(IfcRepresentation actualRepresentation : representationLIST){
                ifcModel.removeIfcObject(actualRepresentation);
                SET<IfcRepresentationItem> representationItemSET = actualRepresentation.getItems();
                for(IfcRepresentationItem actualRepresentationItem : representationItemSET){
                    if(actualRepresentationItem instanceof IfcExtrudedAreaSolid){
                        IfcExtrudedAreaSolid extrudedAreaSolid = (IfcExtrudedAreaSolid) actualRepresentationItem;
                        ifcModel.removeIfcObject(extrudedAreaSolid);
                        ifcModel.removeIfcObject(extrudedAreaSolid.getExtrudedDirection());
                        this.deleteAxis2Placement3D(extrudedAreaSolid.getPosition());
                        IfcProfileDef profileDef = extrudedAreaSolid.getSweptArea();
                        if (profileDef instanceof IfcRectangleProfileDef){
                            IfcRectangleProfileDef rectangleProfileDef = (IfcRectangleProfileDef)profileDef;
                            ifcModel.removeIfcObject(rectangleProfileDef);
                            this.deleteAxis2Placement2D(rectangleProfileDef.getPosition());
                        }
                        if (profileDef instanceof IfcArbitraryClosedProfileDef){
                            IfcArbitraryClosedProfileDef arbitraryClosedProfileDef = (IfcArbitraryClosedProfileDef)profileDef;
                            ifcModel.removeIfcObject(arbitraryClosedProfileDef);
                            IfcCurve outerCurve = arbitraryClosedProfileDef.getOuterCurve();
                            if(outerCurve instanceof IfcPolyline){
                                IfcPolyline polyline = (IfcPolyline) outerCurve;
                                ifcModel.removeIfcObject(outerCurve);
                                LIST<IfcCartesianPoint> cartesianPointLIST = polyline.getPoints();
                                for(IfcCartesianPoint actualCartesianPoint : cartesianPointLIST){
                                    ifcModel.removeIfcObject(actualCartesianPoint);
                                }
                            }
                        }
                    }
                    else if(actualRepresentationItem instanceof IfcTrimmedCurve){
                        IfcTrimmedCurve trimmedCurve = (IfcTrimmedCurve) actualRepresentationItem;
                        ifcModel.removeIfcObject(trimmedCurve);
                        IfcCurve curve = trimmedCurve.getBasisCurve();
                        if(curve instanceof IfcLine){
                            IfcLine line = (IfcLine)curve;
                            ifcModel.removeIfcObject(line);
                            ifcModel.removeIfcObject(line.getPnt());
                            ifcModel.removeIfcObject(line.getDir());
                            ifcModel.removeIfcObject(line.getDir().getOrientation());
                        }
                        SET<IfcTrimmingSelect> trimmingSelectSET = trimmedCurve.getTrim1();
                        for(IfcTrimmingSelect actualTrimmingSelect : trimmingSelectSET){
                            if(actualTrimmingSelect instanceof IfcCartesianPoint){
                                ifcModel.removeIfcObject((IfcCartesianPoint) actualTrimmingSelect);
                            }
                        }
                        trimmingSelectSET = trimmedCurve.getTrim2();
                        for(IfcTrimmingSelect actualTrimmingSelect : trimmingSelectSET){
                            if(actualTrimmingSelect instanceof IfcCartesianPoint){
                                ifcModel.removeIfcObject((IfcCartesianPoint) actualTrimmingSelect);
                            }
                        }
                    }
                }
            }
        }
    }

    // Permet de supprimer un opening
    public void deleteOpening(IfcOpeningElement opening){

        // Remove the opening placement
        this.deleteObjectPlacement(opening.getObjectPlacement());

        // Remove the relation WallStandardCase <-> OpeningElement
        ifcModel.removeIfcObject(opening.getVoidsElements_Inverse());

        // Remove the representation
        this.deleteRepresentation(opening.getRepresentation());

        // Remove the opening
        ifcModel.removeIfcObject(opening);

    }

    // Permet de supprimer une door dans un model
    public void deleteDoor (IfcDoor door){

        // Remove the door placement
        this.deleteObjectPlacement(door.getObjectPlacement());

        // Remove the opening
        this.deleteOpening(this.getOpening(door.getName().getDecodedValue()));

        // Remove the parameters of the door
        SET<IfcRelDefines> relDefinesSET = door.getIsDefinedBy_Inverse();
        for (IfcRelDefines actualRelDefines : relDefinesSET){
            ifcModel.removeIfcObject(actualRelDefines);
            if(actualRelDefines instanceof IfcRelDefinesByType){
                ifcModel.removeIfcObject(((IfcRelDefinesByType)actualRelDefines).getRelatingType());
                SET<IfcPropertySetDefinition> propertySetDefinitionsSET = ((IfcRelDefinesByType)actualRelDefines).getRelatingType().getHasPropertySets();
                for (IfcPropertySetDefinition actualPropertySetDefinition : propertySetDefinitionsSET){
                    ifcModel.removeIfcObject(actualPropertySetDefinition);
                }
            }
        }

        // Remove the relation OpeningElement <-> Door
        SET<IfcRelFillsElement> relFillsElements = door.getFillsVoids_Inverse();
        for (IfcRelFillsElement actualRelFillsElement : relFillsElements){
            ifcModel.removeIfcObject(actualRelFillsElement);
        }

        // Remove the relation BuildingStorey <-> Door
        SET<IfcRelContainedInSpatialStructure> relContainedInSpatialStructureSET = door.getContainedInStructure_Inverse();
        for (IfcRelContainedInSpatialStructure actualRelContainedInSpatialStructure : relContainedInSpatialStructureSET){
            SET<IfcProduct> ifcProductSET = actualRelContainedInSpatialStructure.getRelatedElements();
            for(IfcProduct actualIfcProduct : ifcProductSET){
                if(actualIfcProduct.equals(door)){
                    actualRelContainedInSpatialStructure.removeRelatedElements(actualIfcProduct);
                }
            }
        }

        // Remove the door
        ifcModel.removeIfcObject(door);
    }

    // Permet de supprimer une window dans un model
    public void deleteWindow (IfcWindow window){

        // Remove the window placement
        this.deleteObjectPlacement(window.getObjectPlacement());

        // Remove the opening
        this.deleteOpening(this.getOpening(window.getName().getDecodedValue()));

        // Remove the parameters of the window
        SET<IfcRelDefines> relDefinesSET = window.getIsDefinedBy_Inverse();
        for (IfcRelDefines actualRelDefines : relDefinesSET){
            ifcModel.removeIfcObject(actualRelDefines);
            if(actualRelDefines instanceof IfcRelDefinesByType){
                ifcModel.removeIfcObject(((IfcRelDefinesByType)actualRelDefines).getRelatingType());
                SET<IfcPropertySetDefinition> propertySetDefinitionsSET = ((IfcRelDefinesByType)actualRelDefines).getRelatingType().getHasPropertySets();
                for (IfcPropertySetDefinition actualPropertySetDefinition : propertySetDefinitionsSET){
                    ifcModel.removeIfcObject(actualPropertySetDefinition);
                }
            }
        }

        // Remove the relation OpeningElement <-> window
        SET<IfcRelFillsElement> relFillsElements = window.getFillsVoids_Inverse();
        for (IfcRelFillsElement actualRelFillsElement : relFillsElements){
            ifcModel.removeIfcObject(actualRelFillsElement);
        }

        // Remove the relation BuildingStorey <-> window
        SET<IfcRelContainedInSpatialStructure> relContainedInSpatialStructureSET = window.getContainedInStructure_Inverse();
        for (IfcRelContainedInSpatialStructure actualRelContainedInSpatialStructure : relContainedInSpatialStructureSET){
            SET<IfcProduct> ifcProductSET = actualRelContainedInSpatialStructure.getRelatedElements();
            for(IfcProduct actualIfcProduct : ifcProductSET){
                if(actualIfcProduct.equals(window)){
                    actualRelContainedInSpatialStructure.removeRelatedElements(actualIfcProduct);
                }
            }
        }

        // Remove the window
        ifcModel.removeIfcObject(window);
    }

    // Permet de supprimer un standardWall dans un model avec tous les éléments associés (doors, windows, ...)
    public void deleteWallStandardCase (IfcWallStandardCase wallStandardCase){

        // Remove the wall placement
        this.deleteObjectPlacement(wallStandardCase.getObjectPlacement());

        // Remove all the elements associated to the Wall
        SET<IfcRelVoidsElement> relVoidsElementSET = wallStandardCase.getHasOpenings_Inverse();
        if(relVoidsElementSET.isEmpty()){
            // No openings Element -> do nothing
        }
        else{
            for(IfcRelVoidsElement actualRelVoidsElement : relVoidsElementSET){
                IfcElement openingElement = actualRelVoidsElement.getRelatedOpeningElement();
                if(openingElement instanceof IfcOpeningElement){
                    IfcRelFillsElement relFillsElement = this.getRelFillsElement((IfcOpeningElement) openingElement);
                    IfcElement buildingElement =  relFillsElement.getRelatedBuildingElement();
                    if (buildingElement instanceof IfcDoor){
                        this.deleteDoor((IfcDoor) buildingElement);
                    }
                    else if (buildingElement instanceof IfcWindow){
                        this.deleteWindow((IfcWindow) buildingElement);
                    }
                }
            }
        }

        // Remove the representations
        this.deleteRepresentation(wallStandardCase.getRepresentation());

        // Remove the relation BuildingStorey <-> wallStandardCase
        SET<IfcRelContainedInSpatialStructure> relContainedInSpatialStructureSET = wallStandardCase.getContainedInStructure_Inverse();
        for (IfcRelContainedInSpatialStructure actualRelContainedInSpatialStructure : relContainedInSpatialStructureSET){
            SET<IfcProduct> ifcProductSET = actualRelContainedInSpatialStructure.getRelatedElements();
            if(ifcProductSET.size() == 1){
                ifcModel.removeIfcObject(actualRelContainedInSpatialStructure);
            }
            else {
                for(IfcProduct actualIfcProduct : ifcProductSET){
                    if(actualIfcProduct.equals(wallStandardCase)){
                        actualRelContainedInSpatialStructure.removeRelatedElements(actualIfcProduct);
                    }
                }
            }
        }

        // Remove the wallStandardCase
        ifcModel.removeIfcObject(wallStandardCase);
    }

    // Permet de supprimer un slab dans un model
    public void deleteSlab (IfcSlab slab){

        // Remove the slab placement
        this.deleteObjectPlacement(slab.getObjectPlacement());

        // Remove the slab representation
        this.deleteRepresentation(slab.getRepresentation());

        // Remove all the elements associated to the Slab
        SET<IfcRelVoidsElement> relVoidsElementSET = slab.getHasOpenings_Inverse();
        if(relVoidsElementSET.isEmpty()){
            // No openings Element -> do nothing
        }
        else{
            for(IfcRelVoidsElement actualRelVoidsElement : relVoidsElementSET){
                IfcElement openingElement = actualRelVoidsElement.getRelatedOpeningElement();
                if(openingElement instanceof IfcOpeningElement){
                    IfcRelFillsElement relFillsElement = this.getRelFillsElement((IfcOpeningElement) openingElement);
                    IfcElement buildingElement =  relFillsElement.getRelatedBuildingElement();
                    if (buildingElement instanceof IfcDoor){
                        this.deleteDoor((IfcDoor) buildingElement);
                    }
                    else if (buildingElement instanceof IfcWindow){
                        this.deleteWindow((IfcWindow) buildingElement);
                    }
                }
            }
        }

        // Remove the relation BuildingStorey <-> Slab
        SET<IfcRelContainedInSpatialStructure> relContainedInSpatialStructureSET = slab.getContainedInStructure_Inverse();
        for (IfcRelContainedInSpatialStructure actualRelContainedInSpatialStructure : relContainedInSpatialStructureSET){
            SET<IfcProduct> ifcProductSET = actualRelContainedInSpatialStructure.getRelatedElements();
            if(ifcProductSET.size() == 1){
                ifcModel.removeIfcObject(actualRelContainedInSpatialStructure);
            }
            else {
                for(IfcProduct actualIfcProduct : ifcProductSET){
                    if(actualIfcProduct.equals(slab)){
                        actualRelContainedInSpatialStructure.removeRelatedElements(actualIfcProduct);
                    }
                }
            }
        }

        // Remove the slab
        ifcModel.removeIfcObject(slab);

    }

    // Permet de supprimer la materiau d'un wall
    public void deleteMaterialLayerWall (IfcWallStandardCase wall){
        System.out.println("coucou");
        SET<IfcRelAssociates> relAssociatesSET = wall.getHasAssociations_Inverse();
        for (IfcRelAssociates actualRelAssociates : relAssociatesSET){
            if(actualRelAssociates instanceof IfcRelAssociatesMaterial){
                IfcRelAssociatesMaterial associatesMaterial = (IfcRelAssociatesMaterial) actualRelAssociates;
                ifcModel.removeIfcObject(associatesMaterial);
                IfcMaterialSelect relatingMaterial = associatesMaterial.getRelatingMaterial();
                if (relatingMaterial instanceof IfcMaterial){
                    ifcModel.removeIfcObject((IfcMaterial)relatingMaterial);
                }
            }
        }
    }

    // Permet de modifier la position d'un mur
    public void setWallPosition (IfcWallStandardCase wall, float newPosX, float newPosY){
        IfcObjectPlacement objectPlacement = wall.getObjectPlacement();
        if(objectPlacement instanceof IfcLocalPlacement){
            IfcAxis2Placement axis2Placement = ((IfcLocalPlacement) objectPlacement).getRelativePlacement();
            if(axis2Placement instanceof IfcAxis2Placement3D){
                IfcCartesianPoint cartesianPoint = ((IfcAxis2Placement3D) axis2Placement).getLocation();
                LIST<IfcLengthMeasure> coordinatesPoint = cartesianPoint.getCoordinates();
                coordinatesPoint.set(0,new IfcLengthMeasure(newPosX));
                coordinatesPoint.set(1,new IfcLengthMeasure(newPosY));
                cartesianPoint.setCoordinates(coordinatesPoint);
            }
        }
    }

    // Permet de modifier l'orientation d'un mur
    public void setWallOrientation (IfcWallStandardCase wall, float newDirectionRatioX, float newDirectionRatioY){
        IfcObjectPlacement objectPlacement = wall.getObjectPlacement();
        if(objectPlacement instanceof IfcLocalPlacement){
            IfcAxis2Placement axis2Placement = ((IfcLocalPlacement) objectPlacement).getRelativePlacement();
            if(axis2Placement instanceof IfcAxis2Placement3D){
                IfcDirection direction = ((IfcAxis2Placement3D) axis2Placement).getRefDirection();
                LIST<DOUBLE> directionRatiosLIST = direction.getDirectionRatios();
                directionRatiosLIST.set(0,new DOUBLE(newDirectionRatioX));
                directionRatiosLIST.set(1,new DOUBLE(newDirectionRatioY));
                direction.setDirectionRatios(directionRatiosLIST);
            }
        }
    }

    // Permet de modifier l'épaisseur d'un mur
    public void setWallThickness (IfcWallStandardCase wall, float newThickness){
        IfcProductRepresentation productRepresentation = wall.getRepresentation();
        LIST<IfcRepresentation> representationLIST = productRepresentation.getRepresentations();
        for(IfcRepresentation actualRepresentation : representationLIST){
            if(actualRepresentation.getRepresentationType().getDecodedValue().equals("SweptSolid")){
                SET<IfcRepresentationItem> representationItemSET = actualRepresentation.getItems();
                for(IfcRepresentationItem actualRepresentationItem : representationItemSET){
                    if(actualRepresentationItem instanceof IfcExtrudedAreaSolid){
                        IfcProfileDef profileDef = ((IfcExtrudedAreaSolid) actualRepresentationItem).getSweptArea();
                        if(profileDef instanceof IfcArbitraryClosedProfileDef){
                            IfcCurve curve = ((IfcArbitraryClosedProfileDef) profileDef).getOuterCurve();
                            if (curve instanceof IfcPolyline){
                                LIST<IfcCartesianPoint> cartesianPointLIST = ((IfcPolyline) curve).getPoints();
                                for(IfcCartesianPoint actualCartesianPoint : cartesianPointLIST){
                                    LIST<IfcLengthMeasure> lengthMeasureLIST = actualCartesianPoint.getCoordinates();
                                    if(lengthMeasureLIST.get(1).value > 0){
                                        lengthMeasureLIST.set(1,new IfcLengthMeasure(newThickness/2));
                                    }
                                    else{
                                        lengthMeasureLIST.set(1,new IfcLengthMeasure(-newThickness/2));
                                    }
                                    actualCartesianPoint.setCoordinates(lengthMeasureLIST);
                                }
                            }
                        }
                    }
                }
            }
        }
        // Change also thickness of the openings present in the wall
        SET<IfcOpeningElement> openingElementSET = this.getOpeningRelToWall(wall);
        for(IfcOpeningElement actualOpeningElement : openingElementSET){
            this.setOpeningThickness(actualOpeningElement, newThickness);
        }
    }

    // Permet de modifier la longueur d'un mur
    public void setWallLength (IfcWallStandardCase wall, float newLength){
        IfcProductRepresentation productRepresentation = wall.getRepresentation();
        LIST<IfcRepresentation> representationLIST = productRepresentation.getRepresentations();
        for(IfcRepresentation actualRepresentation : representationLIST){
            if(actualRepresentation.getRepresentationType().getDecodedValue().equals("SweptSolid")){
                SET<IfcRepresentationItem> representationItemSET = actualRepresentation.getItems();
                for(IfcRepresentationItem actualRepresentationItem : representationItemSET){
                    if(actualRepresentationItem instanceof IfcExtrudedAreaSolid){
                        IfcProfileDef profileDef = ((IfcExtrudedAreaSolid) actualRepresentationItem).getSweptArea();
                        if(profileDef instanceof IfcArbitraryClosedProfileDef){
                            IfcCurve curve = ((IfcArbitraryClosedProfileDef) profileDef).getOuterCurve();
                            if (curve instanceof IfcPolyline){
                                LIST<IfcCartesianPoint> cartesianPointLIST = ((IfcPolyline) curve).getPoints();
                                // Set of the first point which use the length
                                IfcCartesianPoint cartesianPoint1 = cartesianPointLIST.get(1);
                                LIST<IfcLengthMeasure> lengthMeasureLIST = cartesianPoint1.getCoordinates();
                                lengthMeasureLIST.set(0,new IfcLengthMeasure(newLength));
                                cartesianPoint1.setCoordinates(lengthMeasureLIST);
                                // Set of the second point which use the length
                                IfcCartesianPoint cartesianPoint2 = cartesianPointLIST.get(2);
                                lengthMeasureLIST = cartesianPoint2.getCoordinates();
                                lengthMeasureLIST.set(0,new IfcLengthMeasure(newLength));
                                cartesianPoint2.setCoordinates(lengthMeasureLIST);
                            }
                        }
                    }
                }
            }
            else if (actualRepresentation.getRepresentationType().getDecodedValue().equals("Curve2D")){
                SET<IfcRepresentationItem> representationItemSET = actualRepresentation.getItems();
                for(IfcRepresentationItem actualRepresentationItem : representationItemSET){
                    if(actualRepresentationItem instanceof IfcTrimmedCurve){
                        IfcCurve curve = ((IfcTrimmedCurve) actualRepresentationItem).getBasisCurve();
                        if(curve instanceof IfcLine){
                            IfcVector vector = ((IfcLine) curve).getDir();
                            vector.setMagnitude(new IfcLengthMeasure(newLength));
                        }
                        SET<IfcTrimmingSelect> trimmingSelectSET = ((IfcTrimmedCurve) actualRepresentationItem).getTrim2();
                        for(IfcTrimmingSelect actualTrimmingSelect : trimmingSelectSET){
                            if(actualTrimmingSelect instanceof IfcCartesianPoint){
                                LIST<IfcLengthMeasure> lengthMeasureLIST = ((IfcCartesianPoint) actualTrimmingSelect).getCoordinates();
                                lengthMeasureLIST.set(0,new IfcLengthMeasure(newLength));
                                ((IfcCartesianPoint) actualTrimmingSelect).setCoordinates(lengthMeasureLIST);
                            }
                        }
                    }
                }
            }
        }
    }

    // Permet de modifier la hauteur d'un mur
    public void setWallHeight (IfcWallStandardCase wall, float newHeight){
        IfcProductRepresentation productRepresentation = wall.getRepresentation();
        LIST<IfcRepresentation> representationLIST = productRepresentation.getRepresentations();
        for(IfcRepresentation actualRepresentation : representationLIST){
            if(actualRepresentation.getRepresentationType().getDecodedValue().equals("SweptSolid")){
                SET<IfcRepresentationItem> representationItemSET = actualRepresentation.getItems();
                for(IfcRepresentationItem actualRepresentationItem : representationItemSET){
                    if(actualRepresentationItem instanceof IfcExtrudedAreaSolid){
                        ((IfcExtrudedAreaSolid) actualRepresentationItem).setDepth(new IfcPositiveLengthMeasure(new IfcLengthMeasure(newHeight)));
                    }
                }
            }
        }
    }

    // Permet de modifier la position d'un opening
    public void setOpeningPosition (IfcOpeningElement opening, float newPosX, float newPosZ){
        IfcObjectPlacement objectPlacement = opening.getObjectPlacement();
        if(objectPlacement instanceof IfcLocalPlacement){
            IfcAxis2Placement axis2Placement = ((IfcLocalPlacement) objectPlacement).getRelativePlacement();
            if(axis2Placement instanceof IfcAxis2Placement3D){
                IfcCartesianPoint cartesianPoint = ((IfcAxis2Placement3D) axis2Placement).getLocation();
                LIST<IfcLengthMeasure> coordinatesPoint = cartesianPoint.getCoordinates();
                coordinatesPoint.set(0,new IfcLengthMeasure(newPosX));
                coordinatesPoint.set(2,new IfcLengthMeasure(newPosZ));
                cartesianPoint.setCoordinates(coordinatesPoint);
            }
        }
    }

    // Permet de modifier l'épaisseur d'un opening
    public void setOpeningThickness (IfcOpeningElement opening, float newThickness){
        IfcObjectPlacement objectPlacement = opening.getObjectPlacement();
        if(objectPlacement instanceof IfcLocalPlacement){
            IfcAxis2Placement axis2Placement = ((IfcLocalPlacement) objectPlacement).getRelativePlacement();
            if(axis2Placement instanceof IfcAxis2Placement3D){
                IfcCartesianPoint cartesianPoint = ((IfcAxis2Placement3D) axis2Placement).getLocation();
                LIST<IfcLengthMeasure> coordinatesPoint = cartesianPoint.getCoordinates();
                coordinatesPoint.set(1,new IfcLengthMeasure(-newThickness/2));
                cartesianPoint.setCoordinates(coordinatesPoint);
            }
        }
        IfcProductRepresentation productRepresentation = opening.getRepresentation();
        LIST<IfcRepresentation> representationLIST = productRepresentation.getRepresentations();
        for(IfcRepresentation actualRepresentation : representationLIST) {
            if (actualRepresentation.getRepresentationType().getDecodedValue().equals("SweptSolid")) {
                SET<IfcRepresentationItem> representationItemSET = actualRepresentation.getItems();
                for (IfcRepresentationItem actualRepresentationItem : representationItemSET) {
                    if (actualRepresentationItem instanceof IfcExtrudedAreaSolid) {
                        ((IfcExtrudedAreaSolid) actualRepresentationItem).setDepth(new IfcPositiveLengthMeasure(new IfcLengthMeasure(newThickness)));
                    }
                }
            }
        }
    }

    // Permet de modifier la largeur d'un opening
    public void setOpeningWidth (IfcOpeningElement opening, float newWidth){
        IfcProductRepresentation productRepresentation = opening.getRepresentation();
        LIST<IfcRepresentation> representationLIST = productRepresentation.getRepresentations();
        for(IfcRepresentation actualRepresentation : representationLIST) {
            if (actualRepresentation.getRepresentationType().getDecodedValue().equals("SweptSolid")) {
                SET<IfcRepresentationItem> representationItemSET = actualRepresentation.getItems();
                for (IfcRepresentationItem actualRepresentationItem : representationItemSET) {
                    if (actualRepresentationItem instanceof IfcExtrudedAreaSolid) {
                        IfcProfileDef profileDef = ((IfcExtrudedAreaSolid) actualRepresentationItem).getSweptArea();
                        if(profileDef instanceof IfcRectangleProfileDef){
                            ((IfcRectangleProfileDef) profileDef).setYDim(new IfcPositiveLengthMeasure(new IfcLengthMeasure(newWidth)));
                        }
                        IfcAxis2Placement3D axis2Placement3D = ((IfcExtrudedAreaSolid) actualRepresentationItem).getPosition();
                        IfcCartesianPoint cartesianPoint = axis2Placement3D.getLocation();
                        LIST<IfcLengthMeasure> lengthMeasureLIST = cartesianPoint.getCoordinates();
                        lengthMeasureLIST.set(0,new IfcLengthMeasure(newWidth/2));
                        cartesianPoint.setCoordinates(lengthMeasureLIST);
                    }
                }
            }
        }
    }

    // Permet de modifier la hauteur d'un opening
    public void setOpeningHeight (IfcOpeningElement opening, float newHeight){
        IfcProductRepresentation productRepresentation = opening.getRepresentation();
        LIST<IfcRepresentation> representationLIST = productRepresentation.getRepresentations();
        for(IfcRepresentation actualRepresentation : representationLIST) {
            if (actualRepresentation.getRepresentationType().getDecodedValue().equals("SweptSolid")) {
                SET<IfcRepresentationItem> representationItemSET = actualRepresentation.getItems();
                for (IfcRepresentationItem actualRepresentationItem : representationItemSET) {
                    if (actualRepresentationItem instanceof IfcExtrudedAreaSolid) {
                        IfcProfileDef profileDef = ((IfcExtrudedAreaSolid) actualRepresentationItem).getSweptArea();
                        if(profileDef instanceof IfcRectangleProfileDef){
                            ((IfcRectangleProfileDef) profileDef).setXDim(new IfcPositiveLengthMeasure(new IfcLengthMeasure(newHeight)));
                        }
                        IfcAxis2Placement3D axis2Placement3D = ((IfcExtrudedAreaSolid) actualRepresentationItem).getPosition();
                        IfcCartesianPoint cartesianPoint = axis2Placement3D.getLocation();
                        LIST<IfcLengthMeasure> lengthMeasureLIST = cartesianPoint.getCoordinates();
                        lengthMeasureLIST.set(2,new IfcLengthMeasure(newHeight/2));
                        cartesianPoint.setCoordinates(lengthMeasureLIST);
                    }
                }
            }
        }
    }

    // Permet de modifier la position d'une door
    public void setDoorPosition (IfcDoor door, float newPosX){
        IfcOpeningElement opening = this.getOpeningRelToDoor(door);
        this.setOpeningPosition(opening, newPosX, 0.0f);
    }

    // Permet de modifier la largeur d'une door
    public void setDoorWidth (IfcDoor door, float newWidth){
        IfcOpeningElement opening = this.getOpeningRelToDoor(door);
        this.setOpeningWidth(opening, newWidth);
        door.setOverallWidth(new IfcPositiveLengthMeasure(new IfcLengthMeasure(newWidth)));
    }

    // Permet de modifier la hauteur d'une door
    public void setDoorHeight (IfcDoor door, float newHeight){
        IfcOpeningElement opening = this.getOpeningRelToDoor(door);
        this.setOpeningHeight(opening, newHeight);
        door.setOverallHeight(new IfcPositiveLengthMeasure(new IfcLengthMeasure(newHeight)));
    }

    // Permet de modifier la position d'une window
    public void setWindowPosition (IfcWindow window, float newPosX, float newPosY){
        IfcOpeningElement opening = this.getOpeningRelToWindow(window);
        this.setOpeningPosition(opening, newPosX, newPosY);
    }

    // Permet de modifier la largeur d'une window
    public void setWindowWidth (IfcWindow window, float newWidth){
        IfcOpeningElement opening = this.getOpeningRelToWindow(window);
        this.setOpeningWidth(opening, newWidth);
        window.setOverallWidth(new IfcPositiveLengthMeasure(new IfcLengthMeasure(newWidth)));
    }

    // Permet de modifier la hauteur d'une window
    public void setWindowHeight (IfcWindow window, float newHeight){
        IfcOpeningElement opening = this.getOpeningRelToWindow(window);
        this.setOpeningHeight(opening, newHeight);
        window.setOverallHeight(new IfcPositiveLengthMeasure(new IfcLengthMeasure(newHeight)));
    }

    public void addCoins(IfcProduct product, float xA, float yA, float xB, float yB, int elevation){

        boolean hasProperties=false;
        boolean hasSameProperty=false;

        // On créer la property
        IfcIdentifier nameProperty = new IfcIdentifier(Propertie.COINS.getName(),true);
        IfcValue valueProperty = new IfcIdentifier(xA+"$"+yA+"$"+xB+"$"+yB+"$"+elevation,true);
        IfcPropertySingleValue property = new IfcPropertySingleValue(nameProperty,null,valueProperty,null);

        // On va voir si des propriétés existent déja
        SET<IfcRelDefines> relDefinesSET = product.getIsDefinedBy_Inverse();
        if (relDefinesSET != null){
            for (IfcRelDefines actualRelDefines : relDefinesSET){
                if (actualRelDefines instanceof IfcRelDefinesByProperties){ // des propriétés éxistent déja sur l'objet
                    SET<IfcRelDefinesByProperties> relDefinesByPropertiesSET = ((IfcRelDefinesByProperties) actualRelDefines).getRelatingPropertyDefinition().getPropertyDefinitionOf_Inverse();
                    for (IfcRelDefinesByProperties actualRelDefinesByProperties : relDefinesByPropertiesSET){
                        IfcPropertySetDefinition propertySetDefinition = actualRelDefinesByProperties.getRelatingPropertyDefinition();
                        if (propertySetDefinition instanceof IfcPropertySet){
                            hasProperties=true;
                            SET<IfcProperty> propertySET = ((IfcPropertySet) propertySetDefinition).getHasProperties();
                            for (IfcProperty actualProperty : propertySET){
                                if (actualProperty instanceof IfcPropertySingleValue){
                                    if (actualProperty.getName().getDecodedValue().equals(nameProperty.getDecodedValue())) { // notre propriétée existe déja
                                        hasSameProperty=true;
                                        ((IfcPropertySingleValue) actualProperty).setNominalValue(valueProperty); // On change la valeur de la propriété qui existe déja
                                    }
                                }
                            }
                            if (!hasSameProperty){ // Si la propriétée n'existe pas, on l'ajoute au tableau de propriétées
                                propertySET.add(property);
                                ((IfcPropertySet) propertySetDefinition).setHasProperties(propertySET);
                                ifcModel.addIfcObject(property);
                            }
                        }
                    }
                }
            }
        }

        // Si aucune RelDefinesProperty ne se trouvent sur l'objet, on la créer
        if (!hasProperties){
            SET<IfcProperty> propertySET = new SET<IfcProperty>();
            propertySET.add(property);
            IfcPropertySet propertySet = new IfcPropertySet(
                    new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
                    new IfcLabel("3DReno-Properties",true),null,propertySET);
            SET<IfcObject> objectSET = new SET<IfcObject>();
            objectSET.add(product);
            IfcRelDefinesByProperties relDefinesByProperties = new IfcRelDefinesByProperties(
                    new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
                    null,null,objectSET,propertySet);
            ifcModel.addIfcObject(propertySet);
            ifcModel.addIfcObject(property);
            ifcModel.addIfcObject(relDefinesByProperties);
        }
    }

    // Permet de récupérer les propriétées liées à une door
    public HashMap<String,String> get3DRenoProperties(IfcProduct product) {
        HashMap<String, String> hm = new HashMap<String, String>();
        // On va voir si des propriétés existent déja
        SET<IfcRelDefines> relDefinesSET = product.getIsDefinedBy_Inverse();
        if (relDefinesSET != null) {
            for (IfcRelDefines actualRelDefines : relDefinesSET) {
                if (actualRelDefines instanceof IfcRelDefinesByProperties) { // des propriétés éxistent déja sur l'objet
                    String namePropertie = ((((IfcRelDefinesByProperties) actualRelDefines)).getRelatingPropertyDefinition().getName().toString());
                    if("3DReno-Properties".equals(namePropertie)){ // On ne prend en compte que nos propriétées
                        SET<IfcRelDefinesByProperties> relDefinesByPropertiesSET = ((IfcRelDefinesByProperties) actualRelDefines).getRelatingPropertyDefinition().getPropertyDefinitionOf_Inverse();
                        for (IfcRelDefinesByProperties actualRelDefinesByProperties : relDefinesByPropertiesSET) {
                            IfcPropertySetDefinition propertySetDefinition = actualRelDefinesByProperties.getRelatingPropertyDefinition();
                            if (propertySetDefinition instanceof IfcPropertySet) {
                                SET<IfcProperty> propertySET = ((IfcPropertySet) propertySetDefinition).getHasProperties();
                                for (IfcProperty actualProperty : propertySET) {
                                    if(actualProperty instanceof IfcPropertySingleValue){
                                        String key = actualProperty.getName().toString();
                                        String value = ((IfcPropertySingleValue) actualProperty).getNominalValue().toString();
                                        hm.put(key,value);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return hm;
    }

    // Permet d'exporter le model au format .ifc
    public void saveIfcModel(){
        File saveStepFile = new File("data/ifc/coucou.ifc");
        try {
            ifcModel.writeStepfile(saveStepFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Permet d'exporter le model au format .ifc
    public void saveIfcModel2(){
        File saveStepFile = new File("data/ifc/coucou2.ifc");
        try {
            ifcModel.writeStepfile(saveStepFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Permet d'importer le model au format .ifc
    public void loadIfcModel(){
        this.ifcModel = new IfcModel();
        File loadStepFile = new File("data/ifc/coucou.ifc");
        try {
            ifcModel.readStepFile(loadStepFile);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}



