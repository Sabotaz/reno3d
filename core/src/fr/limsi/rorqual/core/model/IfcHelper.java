package fr.limsi.rorqual.core.model;

import ifc2x3javatoolbox.ifc2x3tc1.ClassInterface;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSlab;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSlabTypeEnum;
import ifc2x3javatoolbox.ifcmodel.IfcModel;
import ifc2x3javatoolbox.ifc2x3tc1.DOUBLE;
import ifc2x3javatoolbox.ifc2x3tc1.File_Name;
import ifc2x3javatoolbox.ifc2x3tc1.IfcApplication;
import ifc2x3javatoolbox.ifc2x3tc1.IfcArbitraryClosedProfileDef;
import ifc2x3javatoolbox.ifc2x3tc1.IfcAxis2Placement3D;
import ifc2x3javatoolbox.ifc2x3tc1.IfcBuilding;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcChangeActionEnum;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDimensionCount;
import ifc2x3javatoolbox.ifc2x3tc1.IfcElementCompositionEnum;
import ifc2x3javatoolbox.ifc2x3tc1.IfcExtrudedAreaSolid;
import ifc2x3javatoolbox.ifc2x3tc1.IfcGeometricRepresentationContext;
import ifc2x3javatoolbox.ifc2x3tc1.IfcGloballyUniqueId;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLabel;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLengthMeasure;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLine;
import ifc2x3javatoolbox.ifc2x3tc1.IfcObjectDefinition;
import ifc2x3javatoolbox.ifc2x3tc1.IfcOrganization;
import ifc2x3javatoolbox.ifc2x3tc1.IfcOwnerHistory;
import ifc2x3javatoolbox.ifc2x3tc1.IfcPerson;
import ifc2x3javatoolbox.ifc2x3tc1.IfcPersonAndOrganization;
import ifc2x3javatoolbox.ifc2x3tc1.IfcPolyline;
import ifc2x3javatoolbox.ifc2x3tc1.IfcPositiveLengthMeasure;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProduct;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProductDefinitionShape;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProfileTypeEnum;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProject;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelAggregates;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelContainedInSpatialStructure;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRepresentation;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRepresentationContext;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRepresentationItem;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSIUnit;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSIUnitName;
import ifc2x3javatoolbox.ifc2x3tc1.IfcShapeRepresentation;
import ifc2x3javatoolbox.ifc2x3tc1.IfcText;
import ifc2x3javatoolbox.ifc2x3tc1.IfcTimeStamp;
import ifc2x3javatoolbox.ifc2x3tc1.IfcTrimmedCurve;
import ifc2x3javatoolbox.ifc2x3tc1.IfcTrimmingPreference;
import ifc2x3javatoolbox.ifc2x3tc1.IfcTrimmingSelect;
import ifc2x3javatoolbox.ifc2x3tc1.IfcUnit;
import ifc2x3javatoolbox.ifc2x3tc1.IfcUnitAssignment;
import ifc2x3javatoolbox.ifc2x3tc1.IfcUnitEnum;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifc2x3tc1.IfcVector;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLocalPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcBuildingStorey;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDirection;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSite;
import ifc2x3javatoolbox.ifc2x3tc1.LIST;
import ifc2x3javatoolbox.ifc2x3tc1.SET;
import ifc2x3javatoolbox.ifc2x3tc1.STRING;
import ifc2x3javatoolbox.ifc2x3tc1.BOOLEAN;
import ifc2x3javatoolbox.ifc2x3tc1.IfcChangeActionEnum.IfcChangeActionEnum_internal;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSIUnitName.IfcSIUnitName_internal;
import ifc2x3javatoolbox.ifc2x3tc1.IfcUnitEnum.IfcUnitEnum_internal;

import java.io.File;
import java.io.IOException;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by ricordeau on 08/04/2015.
 */
public class IfcHelper {


    // Permet de compléter un model de départ contenant les informations du projet, un site,
    // un building et un building storey (par défaut à l'altitude 0)
    public static IfcModel initialiseIfcModel (IfcModel ifcModel){

        // Initialize File Name
        LIST<STRING> authors = new LIST<>();
        authors.add(new STRING("Thomas Ricordeau",true));
        authors.add(new STRING("Julien Christophe",true));
        LIST<STRING> organisation = new LIST<>();
        organisation.add(new STRING("LIMSI-CNRS",true));
        File_Name fileName = new File_Name(new STRING("3DReno first IFC",true),null,authors,
                organisation,new STRING("",true),new STRING("",true),new STRING("",true));
        ifcModel.setFile_Name(fileName);

        // Create persons
        IfcPerson ifcPerson = new IfcPerson(null,
                new IfcLabel("Ricordeau", true), new IfcLabel("Thomas", true),
                null, null, null, null, null);

        IfcPerson ifcPerson2 = new IfcPerson(null,
                new IfcLabel("Chrisptophe", true), new IfcLabel("Julien", true),
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
                new IfcLabel("1.0", true), new IfcLabel("3D-Reno Application",
                true),null);

        // Create IfcOwnerHistory
        IfcOwnerHistory ifcOwnerHistory = new IfcOwnerHistory(
                ifcPersonAndOrganization, ifcApplication, null,
                new IfcChangeActionEnum(IfcChangeActionEnum_internal.ADDED
                        .name()), null, null,
                null, new IfcTimeStamp(
                (int) (System.currentTimeMillis() / 1000)));

        // Create WorldCoordinateSystem
        LIST<IfcLengthMeasure> coordinatesOrigin = new LIST<>();
        coordinatesOrigin.add(new IfcLengthMeasure(0.0));
        coordinatesOrigin.add(new IfcLengthMeasure(0.0));
        coordinatesOrigin.add(new IfcLengthMeasure(0.0));
        IfcCartesianPoint ifcCartesianPointOriginWorld = new IfcCartesianPoint(coordinatesOrigin);
        IfcAxis2Placement3D ifcAxis2Placement3DWorld = new IfcAxis2Placement3D(
                ifcCartesianPointOriginWorld, null, null);

        // Create RepresentationContexts
        IfcGeometricRepresentationContext ifcGeometricRepresentationContext =
                new IfcGeometricRepresentationContext(null, new IfcLabel("Model", true),
                new IfcDimensionCount(3),new DOUBLE(1.0E-5), ifcAxis2Placement3DWorld, null);
        SET<IfcRepresentationContext> contexts = new SET<>();
        contexts.add(ifcGeometricRepresentationContext);

        // Create UnitsInContext
        SET<IfcUnit> units = new SET<>();
        IfcSIUnit lengthUnit = new IfcSIUnit(null, new IfcUnitEnum(
                IfcUnitEnum_internal.LENGTHUNIT.name()), null,
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
        LIST<IfcLengthMeasure> coordinatesSite = new LIST<>();
        coordinatesSite.add(new IfcLengthMeasure(0.0));
        coordinatesSite.add(new IfcLengthMeasure(0.0));
        coordinatesSite.add(new IfcLengthMeasure(0.0));
        IfcCartesianPoint ifcCartesianPointOriginSite = new IfcCartesianPoint(coordinatesSite);
        LIST<DOUBLE> ZAxisSite = new LIST<>();
        ZAxisSite.add(new DOUBLE(0.0));
        ZAxisSite.add(new DOUBLE(0.0));
        ZAxisSite.add(new DOUBLE(1.0));
        IfcDirection ifcDirectionZAxisSite = new IfcDirection(ZAxisSite);
        LIST<DOUBLE> XDirectionSite = new LIST<>();
        XDirectionSite.add(new DOUBLE(1.0));
        XDirectionSite.add(new DOUBLE(0.0));
        XDirectionSite.add(new DOUBLE(0.0));
        IfcDirection ifcDirectionXDirectionSite = new IfcDirection(XDirectionSite);
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
        LIST<IfcLengthMeasure> coordinatesBuilding = new LIST<>();
        coordinatesBuilding.add(new IfcLengthMeasure(0.0));
        coordinatesBuilding.add(new IfcLengthMeasure(0.0));
        coordinatesBuilding.add(new IfcLengthMeasure(0.0));
        IfcCartesianPoint ifcCartesianPointOriginBuilding = new IfcCartesianPoint(coordinatesBuilding);
        LIST<DOUBLE> ZAxisBuilding = new LIST<>();
        ZAxisBuilding.add(new DOUBLE(0.0));
        ZAxisBuilding.add(new DOUBLE(0.0));
        ZAxisBuilding.add(new DOUBLE(1.0));
        IfcDirection ifcDirectionZAxisBuilding = new IfcDirection(ZAxisBuilding);
        LIST<DOUBLE> XDirectionBuilding = new LIST<>();
        XDirectionBuilding.add(new DOUBLE(1.0));
        XDirectionBuilding.add(new DOUBLE(0.0));
        XDirectionBuilding.add(new DOUBLE(0.0));
        IfcDirection ifcDirectionXDirectionBuilding = new IfcDirection(XDirectionBuilding);
        IfcAxis2Placement3D ifcAxis2Placement3DBuilding = new IfcAxis2Placement3D(
                ifcCartesianPointOriginBuilding, ifcDirectionZAxisBuilding, ifcDirectionXDirectionBuilding);
        IfcLocalPlacement ifcLocalPlacementBuilding = new IfcLocalPlacement(null,
                ifcAxis2Placement3DBuilding);
        IfcBuilding ifcBuilding = new IfcBuilding(new IfcGloballyUniqueId(
                ifcModel.getNewGlobalUniqueId()), ifcOwnerHistory, new IfcLabel(
                "Simple maison", true), new IfcText("Description de la maison", true),
                null, ifcLocalPlacementBuilding, null, null,
                new IfcElementCompositionEnum("ELEMENT"), null, null, null);

        // Create IfcBuilgingStorey
        LIST<IfcLengthMeasure> coordinatesBuildingStorey = new LIST<>();
        coordinatesBuildingStorey.add(new IfcLengthMeasure(0.0));
        coordinatesBuildingStorey.add(new IfcLengthMeasure(0.0));
        coordinatesBuildingStorey.add(new IfcLengthMeasure(0.0));
        IfcCartesianPoint ifcCartesianPointOriginBuildingStorey = new IfcCartesianPoint(coordinatesBuildingStorey);
        LIST<DOUBLE> ZAxisBuildingStorey = new LIST<>();
        ZAxisBuildingStorey.add(new DOUBLE(0.0));
        ZAxisBuildingStorey.add(new DOUBLE(0.0));
        ZAxisBuildingStorey.add(new DOUBLE(1.0));
        IfcDirection ifcDirectionZAxisBuildingStorey = new IfcDirection(ZAxisBuildingStorey);
        LIST<DOUBLE> XDirectionBuildingStorey = new LIST<>();
        XDirectionBuildingStorey.add(new DOUBLE(1.0));
        XDirectionBuildingStorey.add(new DOUBLE(0.0));
        XDirectionBuildingStorey.add(new DOUBLE(0.0));
        IfcDirection ifcDirectionXDirectionBuildingStorey = new IfcDirection(XDirectionBuildingStorey);
        IfcAxis2Placement3D ifcAxis2Placement3DBuildingStorey = new IfcAxis2Placement3D(
                ifcCartesianPointOriginBuildingStorey, ifcDirectionZAxisBuildingStorey, ifcDirectionXDirectionBuildingStorey);
        IfcLocalPlacement ifcLocalPlacementBuildingStorey = new IfcLocalPlacement(null,
                ifcAxis2Placement3DBuildingStorey);
        IfcBuildingStorey ifcBuildingStorey = new IfcBuildingStorey(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcOwnerHistory, new IfcLabel("1st floor", true),
                new IfcText("1st floor / elevation = 0.0m", true), null, ifcLocalPlacementBuildingStorey,
                null, null, new IfcElementCompositionEnum("ELEMENT"), new IfcLengthMeasure(0.0));

        // Create relation IfcProject --> IfcSite
        SET<IfcObjectDefinition> relatedObjects;
        relatedObjects = new SET<>();
        relatedObjects.add(ifcSite);
        IfcRelAggregates relationProjectToSite;
        relationProjectToSite = new IfcRelAggregates(new IfcGloballyUniqueId(
                ifcModel.getNewGlobalUniqueId()), ifcOwnerHistory, new IfcLabel("ProjectContainer",true),
                new IfcText("ProjectContainer for Sites",true), ifcProject, relatedObjects);

        // Create relation IfcSite --> IfcBuilding
        relatedObjects = new SET<>();
        relatedObjects.add(ifcBuilding);
        IfcRelAggregates relationSiteToBuilding;
        relationSiteToBuilding = new IfcRelAggregates(new IfcGloballyUniqueId(
                ifcModel.getNewGlobalUniqueId()), ifcOwnerHistory, new IfcLabel("SiteContainer",true),
                new IfcText("SiteContainer for Building",true), ifcSite, relatedObjects);

        // Create relation IfcBuilding --> IfcBuildingStorey
        relatedObjects = new SET<>();
        relatedObjects.add(ifcBuildingStorey);
        IfcRelAggregates relationBuildingToBuildingStorey;
        relationBuildingToBuildingStorey = new IfcRelAggregates(new IfcGloballyUniqueId(
                ifcModel.getNewGlobalUniqueId()), ifcOwnerHistory, new IfcLabel("BuildingContainer",true),
                new IfcText("BuildingContainer for BuildingStorey",true), ifcBuilding, relatedObjects);

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

        ifcModel.addIfcObject(ifcBuildingStorey);
        ifcModel.addIfcObject(ifcLocalPlacementBuildingStorey);
        ifcModel.addIfcObject(ifcAxis2Placement3DBuildingStorey);
        ifcModel.addIfcObject(ifcCartesianPointOriginBuildingStorey);
        ifcModel.addIfcObject(ifcDirectionZAxisBuildingStorey);
        ifcModel.addIfcObject(ifcDirectionXDirectionBuildingStorey);

        ifcModel.addIfcObject(relationProjectToSite);
        ifcModel.addIfcObject(relationSiteToBuilding);
        ifcModel.addIfcObject(relationBuildingToBuildingStorey);

        return ifcModel;
    }

    // Permet de récupérer le contexte géométrique d'un model
    public static IfcGeometricRepresentationContext getGeometricRepresentationContext (IfcModel ifcModel){
        Collection<IfcGeometricRepresentationContext> collectionBuilding = ifcModel.getCollection(IfcGeometricRepresentationContext.class);
        Iterator<IfcGeometricRepresentationContext> it = collectionBuilding.iterator();
        return it.next();
    }

    // Permet de récupérer le batiment contenu dans le model (on considère qu'il n'y a qu'un batiment)
    public static IfcBuilding getBuilding (IfcModel ifcModel){
        Collection<IfcBuilding> collectionBuilding = ifcModel.getCollection(IfcBuilding.class);
        Iterator<IfcBuilding> it = collectionBuilding.iterator();
        return it.next();
    }

    // Permet de récupérer un étage dans un model en fonction de son nom
    public static IfcBuildingStorey getBuildingStorey (IfcModel ifcModel, String nameBuildingStorey){
        Collection<IfcBuildingStorey> collectionBuildingStorey = ifcModel.getCollection(IfcBuildingStorey.class);
        for (IfcBuildingStorey actualBuildingStorey : collectionBuildingStorey){
            if(actualBuildingStorey.getName().getDecodedValue().equals(nameBuildingStorey)){
                return actualBuildingStorey;
            }
        }
        return null;
    }

    // Permet de récupérer la relation entre le building et ses étages
    public static IfcRelAggregates getBuildingRelations (IfcModel ifcModel){
        Collection<IfcRelAggregates> collectionRelAggregates = ifcModel.getCollection(IfcRelAggregates.class);
        for (IfcRelAggregates actualRelAggregates : collectionRelAggregates){
            if(actualRelAggregates.getRelatingObject().getClass().equals(IfcBuilding.class)){
                return actualRelAggregates;
            }
        }
        return null;
    }

    // Permet de récupérer la relation entre un étage et ses produits
    public static IfcRelContainedInSpatialStructure getRelContainedInSpatialStructure (IfcModel ifcModel, String nameBuildingStorey){
        Collection<IfcRelContainedInSpatialStructure> collectionRelContainedInSpatialStructure = ifcModel.getCollection(IfcRelContainedInSpatialStructure.class);
        for (IfcRelContainedInSpatialStructure actualRelContainedInSpatialStructure : collectionRelContainedInSpatialStructure){
            if(actualRelContainedInSpatialStructure.getRelatingStructure().getName().getDecodedValue().equals(nameBuildingStorey)){
                return actualRelContainedInSpatialStructure;
            }
        }
        return null;
    }

    // Permet de créer un point cartésien en 2D à partir de deux float
    public static IfcCartesianPoint createCartesianPoint2D(float x, float y){
        LIST<IfcLengthMeasure> coordinates = new LIST<>();
        coordinates.add(new IfcLengthMeasure(x));
        coordinates.add(new IfcLengthMeasure(y));
        IfcCartesianPoint cartesianPoint = new IfcCartesianPoint(coordinates);
        return cartesianPoint;
    }

    // Permet de créer un point cartésien en 3D à partir de trois float
    public static IfcCartesianPoint createCartesianPoint3D(float x, float y, float z){
        LIST<IfcLengthMeasure> coordinates = new LIST<>();
        coordinates.add(new IfcLengthMeasure(x));
        coordinates.add(new IfcLengthMeasure(y));
        coordinates.add(new IfcLengthMeasure(z));
        IfcCartesianPoint cartesianPoint = new IfcCartesianPoint(coordinates);
        return cartesianPoint;
    }

    // Permet d'ajouter un étage au batiment en lui rentrant son nom et son élévation
    public static void addBuildingStorey (IfcModel ifcModel,String nameFloor , float elevation){
        IfcBuilding ifcBuilding = getBuilding(ifcModel);
        LIST<IfcLengthMeasure> coordinatesBuildingStorey = new LIST<>();
        coordinatesBuildingStorey.add(new IfcLengthMeasure(0.0));
        coordinatesBuildingStorey.add(new IfcLengthMeasure(0.0));
        coordinatesBuildingStorey.add(new IfcLengthMeasure(elevation));
        IfcCartesianPoint ifcCartesianPointOriginBuildingStorey = new IfcCartesianPoint(coordinatesBuildingStorey);
        LIST<DOUBLE> ZAxisBuildingStorey = new LIST<>();
        ZAxisBuildingStorey.add(new DOUBLE(0.0));
        ZAxisBuildingStorey.add(new DOUBLE(0.0));
        ZAxisBuildingStorey.add(new DOUBLE(1.0));
        IfcDirection ifcDirectionZAxisBuildingStorey = new IfcDirection(ZAxisBuildingStorey);
        LIST<DOUBLE> XDirectionBuildingStorey = new LIST<>();
        XDirectionBuildingStorey.add(new DOUBLE(1.0));
        XDirectionBuildingStorey.add(new DOUBLE(0.0));
        XDirectionBuildingStorey.add(new DOUBLE(0.0));
        IfcDirection ifcDirectionXDirectionBuildingStorey = new IfcDirection(XDirectionBuildingStorey);
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
        IfcRelAggregates relationBuildingToBuildingStorey = IfcHelper.getBuildingRelations(ifcModel);
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
    public static void addWall(IfcModel ifcModel, String nameBuildingStorey, float wallLength, float wallWidth, float wallHeight, float posX, float posY,float dirX, float dirY){
        IfcBuildingStorey buildingStorey = getBuildingStorey(ifcModel,nameBuildingStorey);
        LIST<IfcLengthMeasure> coordinatesWall = new LIST<IfcLengthMeasure>();
        coordinatesWall.add(new IfcLengthMeasure(posX));
        coordinatesWall.add(new IfcLengthMeasure(posY));
        coordinatesWall.add(new IfcLengthMeasure(0.0));
        IfcCartesianPoint ifcCartesianPointOriginWall = new IfcCartesianPoint(coordinatesWall);
        LIST<DOUBLE> zAxisWall = new LIST<DOUBLE>();
        zAxisWall.add(new DOUBLE(0.0));
        zAxisWall.add(new DOUBLE(0.0));
        zAxisWall.add(new DOUBLE(1.0));
        IfcDirection ifcDirectionZAxisWall = new IfcDirection(zAxisWall);
        LIST<DOUBLE> xDirectionWall = new LIST<DOUBLE>();
        xDirectionWall.add(new DOUBLE(dirX));
        xDirectionWall.add(new DOUBLE(dirY));
        xDirectionWall.add(new DOUBLE(0.0));
        IfcDirection ifcDirectionXDirectionWall = new IfcDirection(xDirectionWall);
        IfcAxis2Placement3D ifcAxis2Placement3DWall = new IfcAxis2Placement3D(
                ifcCartesianPointOriginWall, ifcDirectionZAxisWall, ifcDirectionXDirectionWall);
        IfcLocalPlacement ifcLocalPlacementWall = new IfcLocalPlacement(buildingStorey.getObjectPlacement(),
                ifcAxis2Placement3DWall);
        LIST<IfcRepresentation> ifcWallRepresentationsList = new LIST<IfcRepresentation>();

        // First representation : Geometric representation (2D)
        LIST<IfcLengthMeasure> wallPoints2D1 = new LIST<IfcLengthMeasure>();
        wallPoints2D1.add(new IfcLengthMeasure(0.0));
        wallPoints2D1.add(new IfcLengthMeasure(0.0));
        IfcCartesianPoint ifcWallPoints2D1 = new IfcCartesianPoint(wallPoints2D1);
        LIST<IfcLengthMeasure> wallPoints2D2 = new LIST<IfcLengthMeasure>();
        wallPoints2D2.add(new IfcLengthMeasure(wallLength));
        wallPoints2D2.add(new IfcLengthMeasure(0.0));
        IfcCartesianPoint ifcWallPoints2D2 = new IfcCartesianPoint(wallPoints2D2);
        LIST<DOUBLE> wallAxisDirection = new LIST<DOUBLE>();
        wallAxisDirection.add(new DOUBLE(1.0));
        wallAxisDirection.add(new DOUBLE(0.0));
        IfcDirection ifcWallAxisDirection = new IfcDirection(wallAxisDirection);
        IfcVector wallAxisVector = new IfcVector(ifcWallAxisDirection,new IfcLengthMeasure(wallLength));
        LIST<IfcLengthMeasure> wallPoints2D0 = new LIST<IfcLengthMeasure>();
        wallPoints2D0.add(new IfcLengthMeasure(0.0));
        wallPoints2D0.add(new IfcLengthMeasure(0.0));
        IfcCartesianPoint ifcWallPoints2D0 = new IfcCartesianPoint(wallPoints2D0);
        IfcLine wallAxisLine =new IfcLine(ifcWallPoints2D0,wallAxisVector);
        SET<IfcTrimmingSelect> Trim1 = new SET<IfcTrimmingSelect>();
        SET<IfcTrimmingSelect> Trim2 = new SET<IfcTrimmingSelect>();
        Trim1.add(ifcWallPoints2D1);
        Trim2.add(ifcWallPoints2D2);
        IfcTrimmedCurve wallTrimmedCurve = new IfcTrimmedCurve(wallAxisLine,Trim1,Trim2,
                new BOOLEAN(true),new IfcTrimmingPreference("CARTESIAN"));
        SET<IfcRepresentationItem> ifcWallRepresentation2DItem = new SET<IfcRepresentationItem>();
        ifcWallRepresentation2DItem.add(wallTrimmedCurve);
        IfcShapeRepresentation ifcWallCurve2DRepresentation = new IfcShapeRepresentation(getGeometricRepresentationContext(ifcModel),
                new IfcLabel ("Axis",true), new IfcLabel("Curve2D",true), ifcWallRepresentation2DItem);
        ifcWallRepresentationsList.add(ifcWallCurve2DRepresentation);

        // Second representation : SweptSolid representation (3D)
        LIST<IfcCartesianPoint> wallAllPoints = new LIST<IfcCartesianPoint>();
        LIST<IfcLengthMeasure> wallPoints1 = new LIST<IfcLengthMeasure>();
        wallPoints1.add(new IfcLengthMeasure(0.0));
        wallPoints1.add(new IfcLengthMeasure(wallWidth/2));
        IfcCartesianPoint wallCartesianPoint1 = new IfcCartesianPoint(wallPoints1);
        wallAllPoints.add(wallCartesianPoint1);
        LIST<IfcLengthMeasure> wallPoints2 = new LIST<IfcLengthMeasure>();
        wallPoints2.add(new IfcLengthMeasure(wallLength));
        wallPoints2.add(new IfcLengthMeasure(wallWidth/2));
        IfcCartesianPoint wallCartesianPoint2 = new IfcCartesianPoint(wallPoints2);
        wallAllPoints.add(wallCartesianPoint2);
        LIST<IfcLengthMeasure> wallPoints3 = new LIST<IfcLengthMeasure>();
        wallPoints3.add(new IfcLengthMeasure(wallLength));
        wallPoints3.add(new IfcLengthMeasure(-wallWidth/2));
        IfcCartesianPoint wallCartesianPoint3 = new IfcCartesianPoint(wallPoints3);
        wallAllPoints.add(wallCartesianPoint3);
        LIST<IfcLengthMeasure> wallPoints4 = new LIST<IfcLengthMeasure>();
        wallPoints4.add(new IfcLengthMeasure(0.0));
        wallPoints4.add(new IfcLengthMeasure(-wallWidth/2));
        IfcCartesianPoint wallCartesianPoint4 = new IfcCartesianPoint(wallPoints4);
        wallAllPoints.add(wallCartesianPoint4);
        IfcPolyline wallPolyline = new IfcPolyline(wallAllPoints);
        IfcArbitraryClosedProfileDef wallArbitraryClosedProfileDef = new IfcArbitraryClosedProfileDef(
                new IfcProfileTypeEnum ("AREA"), null, wallPolyline);
        LIST<IfcLengthMeasure> coordinatesWallRepresentation = new LIST<IfcLengthMeasure>();
        coordinatesWallRepresentation.add(new IfcLengthMeasure(0.0));
        coordinatesWallRepresentation.add(new IfcLengthMeasure(0.0));
        coordinatesWallRepresentation.add(new IfcLengthMeasure(0.0));
        IfcCartesianPoint ifcCartesianPointOriginWallRepresentation = new IfcCartesianPoint(coordinatesWallRepresentation);
        LIST<DOUBLE> zAxisWallRepresentation = new LIST<DOUBLE>();
        zAxisWallRepresentation.add(new DOUBLE(0.0));
        zAxisWallRepresentation.add(new DOUBLE(0.0));
        zAxisWallRepresentation.add(new DOUBLE(1.0));
        IfcDirection ifcDirectionZAxisWallRepresentation = new IfcDirection(zAxisWallRepresentation);
        LIST<DOUBLE> xDirectionWallRepresentation = new LIST<DOUBLE>();
        xDirectionWallRepresentation.add(new DOUBLE(1.0));
        xDirectionWallRepresentation.add(new DOUBLE(0.0));
        xDirectionWallRepresentation.add(new DOUBLE(0.0));
        IfcDirection ifcDirectionXDirectionWallRepresentation = new IfcDirection(xDirectionWallRepresentation);
        IfcAxis2Placement3D ifcAxis2Placement3DWallRepresentation = new IfcAxis2Placement3D(
                ifcCartesianPointOriginWallRepresentation, ifcDirectionZAxisWallRepresentation, ifcDirectionXDirectionWallRepresentation);
        LIST<DOUBLE> wallExtrudedDirection = new LIST<DOUBLE>();
        wallExtrudedDirection.add(new DOUBLE(0.0));
        wallExtrudedDirection.add(new DOUBLE(0.0));
        wallExtrudedDirection.add(new DOUBLE(1.0));
        IfcDirection ifcWallExtrudedDirection = new IfcDirection(wallExtrudedDirection);
        IfcLengthMeasure lengthExtrusion = new IfcLengthMeasure(wallHeight);
        IfcExtrudedAreaSolid extrudedWall = new IfcExtrudedAreaSolid(wallArbitraryClosedProfileDef,
                ifcAxis2Placement3DWallRepresentation,ifcWallExtrudedDirection,new IfcPositiveLengthMeasure(lengthExtrusion));
        SET<IfcRepresentationItem> ifcWallRepresentation3DItem = new SET<IfcRepresentationItem>();
        ifcWallRepresentation3DItem.add(extrudedWall);
        IfcShapeRepresentation ifcWallSweptSolidRepresentation = new IfcShapeRepresentation(getGeometricRepresentationContext(ifcModel),
                new IfcLabel ("Body",true), new IfcLabel("SweptSolid",true), ifcWallRepresentation3DItem);
        ifcWallRepresentationsList.add(ifcWallSweptSolidRepresentation);

        // Create the wallStandardCase
        IfcProductDefinitionShape ifcWallDefinitionShape = new IfcProductDefinitionShape(null,null,ifcWallRepresentationsList);
        IfcWallStandardCase ifcWallStandardCase = new IfcWallStandardCase(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(), new IfcLabel("Mur", true),
                new IfcText("Description du mur", true), null, ifcLocalPlacementWall,
                ifcWallDefinitionShape, null);

        // Create relation IfcBuildingStorey --> IfcWallStandardCase
        IfcRelContainedInSpatialStructure relContainedInSpatialStructure = IfcHelper.getRelContainedInSpatialStructure(ifcModel,nameBuildingStorey);
        if(relContainedInSpatialStructure==null){
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

    // Permet d'ajouter un slab à un IfcModel
    public static void addSlabs(IfcModel ifcModel,String nameBuildingStorey, LIST<IfcCartesianPoint> listSlabCartesianPoint){
        IfcBuildingStorey buildingStorey = getBuildingStorey(ifcModel,nameBuildingStorey);
        LIST<IfcLengthMeasure> coordinatesSlab = new LIST<IfcLengthMeasure>();
        coordinatesSlab.add(new IfcLengthMeasure(0.0));
        coordinatesSlab.add(new IfcLengthMeasure(0.0));
        coordinatesSlab.add(new IfcLengthMeasure(0.0));
        IfcCartesianPoint ifcCartesianPointOriginSlab = new IfcCartesianPoint(coordinatesSlab);
        LIST<DOUBLE> zAxisLocalSlab = new LIST<DOUBLE>();
        zAxisLocalSlab.add(new DOUBLE(0.0));
        zAxisLocalSlab.add(new DOUBLE(0.0));
        zAxisLocalSlab.add(new DOUBLE(1.0));
        IfcDirection ifcDirectionZAxisLocalSlab = new IfcDirection(zAxisLocalSlab);
        LIST<DOUBLE> xDirectionLocalSlab = new LIST<DOUBLE>();
        xDirectionLocalSlab.add(new DOUBLE(1.0));
        xDirectionLocalSlab.add(new DOUBLE(0.0));
        xDirectionLocalSlab.add(new DOUBLE(0.0));
        IfcDirection ifcDirectionXDirectionLocalSlab = new IfcDirection(xDirectionLocalSlab);
        IfcAxis2Placement3D ifcAxis2Placement3DSlab = new IfcAxis2Placement3D(
                ifcCartesianPointOriginSlab, ifcDirectionZAxisLocalSlab, ifcDirectionXDirectionLocalSlab);
        IfcLocalPlacement ifcLocalPlacementSlab = new IfcLocalPlacement(buildingStorey.getObjectPlacement(),
                ifcAxis2Placement3DSlab);
        LIST<IfcRepresentation> ifcSlabRepresentationsList = new LIST<IfcRepresentation>();

        // Slab representation : SweptSolid representation (3D)
        IfcPolyline slabPolyline = new IfcPolyline(listSlabCartesianPoint);
        IfcArbitraryClosedProfileDef slabArbitraryClosedProfileDef = new IfcArbitraryClosedProfileDef(
                new IfcProfileTypeEnum ("AREA"), null, slabPolyline);
        LIST<IfcLengthMeasure> coordinatesSlabRepresentation = new LIST<IfcLengthMeasure>();
        coordinatesSlabRepresentation.add(new IfcLengthMeasure(0.0));
        coordinatesSlabRepresentation.add(new IfcLengthMeasure(0.0));
        coordinatesSlabRepresentation.add(new IfcLengthMeasure(0.0));
        IfcCartesianPoint ifcCartesianPointOriginSlabRepresentation = new IfcCartesianPoint(coordinatesSlabRepresentation);
        LIST<DOUBLE> zAxisSlabRepresentation = new LIST<DOUBLE>();
        zAxisSlabRepresentation.add(new DOUBLE(0.0));
        zAxisSlabRepresentation.add(new DOUBLE(0.0));
        zAxisSlabRepresentation.add(new DOUBLE(1.0));
        IfcDirection ifcDirectionZAxisSlabRepresentation = new IfcDirection(zAxisSlabRepresentation);
        LIST<DOUBLE> xDirectionSlabRepresentation = new LIST<DOUBLE>();
        xDirectionSlabRepresentation.add(new DOUBLE(1.0));
        xDirectionSlabRepresentation.add(new DOUBLE(0.0));
        xDirectionSlabRepresentation.add(new DOUBLE(0.0));
        IfcDirection ifcDirectionXDirectionSlabRepresentation = new IfcDirection(xDirectionSlabRepresentation);
        IfcAxis2Placement3D ifcAxis2Placement3DSlabRepresentation = new IfcAxis2Placement3D(
                ifcCartesianPointOriginSlabRepresentation, ifcDirectionZAxisSlabRepresentation, ifcDirectionXDirectionSlabRepresentation);
        LIST<DOUBLE> slabExtrudedDirection = new LIST<DOUBLE>();
        slabExtrudedDirection.add(new DOUBLE(0.0));
        slabExtrudedDirection.add(new DOUBLE(0.0));
        slabExtrudedDirection.add(new DOUBLE(1.0));
        IfcDirection ifcSlabExtrudedDirection = new IfcDirection(slabExtrudedDirection);
        IfcLengthMeasure lengthExtrusion = new IfcLengthMeasure(0.2);
        IfcExtrudedAreaSolid extrudedSlab = new IfcExtrudedAreaSolid(slabArbitraryClosedProfileDef,
                ifcAxis2Placement3DSlabRepresentation,ifcSlabExtrudedDirection,new IfcPositiveLengthMeasure(lengthExtrusion));
        SET<IfcRepresentationItem> ifcSlabRepresentation3DItem = new SET<IfcRepresentationItem>();
        ifcSlabRepresentation3DItem.add(extrudedSlab);
        IfcShapeRepresentation ifcSlabSweptSolidRepresentation = new IfcShapeRepresentation(getGeometricRepresentationContext(ifcModel),
                new IfcLabel ("Body",true), new IfcLabel("SweptSolid",true), ifcSlabRepresentation3DItem);
        ifcSlabRepresentationsList.add(ifcSlabSweptSolidRepresentation);

        // Create the Slab
        IfcProductDefinitionShape ifcSlabDefinitionShape = new IfcProductDefinitionShape(null,null,ifcSlabRepresentationsList);
        IfcSlab ifcSlab = new IfcSlab(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(), new IfcLabel("Slab", true),
                new IfcText("Description du slab", true), null, ifcLocalPlacementSlab,
                ifcSlabDefinitionShape, null, new IfcSlabTypeEnum("FLOOR"));

        // Create relation IfcBuildingStorey --> IfcWallStandardCase
        IfcRelContainedInSpatialStructure relContainedInSpatialStructure = IfcHelper.getRelContainedInSpatialStructure(ifcModel,nameBuildingStorey);
        if(relContainedInSpatialStructure==null){
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

    // Permet d'importer un model au format .ifc
    public static IfcModel loadIfcModel(String path){
        IfcModel ifcModel = new IfcModel();
        File stepFile = new File(path);
        try {
            ifcModel.readStepFile(stepFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ifcModel;
    }

    // Permet d'exporter le model au format .ifc
    public static void saveIfcModel(IfcModel ifcModel){
        File saveStepFile = new File("C:\\Users\\ricordeau\\Desktop\\Coucou.ifc");
        try {
            ifcModel.writeStepfile(saveStepFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
