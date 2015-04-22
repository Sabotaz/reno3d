package fr.limsi.rorqual.core.model;

import ifc2x3javatoolbox.ifc2x3tc1.IfcAxis2Placement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcAxis2Placement2D;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCurve;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDoor;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDoorLiningProperties;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDoorPanelOperationEnum;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDoorPanelPositionEnum;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDoorPanelProperties;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDoorStyle;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDoorStyleConstructionEnum;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDoorStyleOperationEnum;
import ifc2x3javatoolbox.ifc2x3tc1.IfcElement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcGridPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcNormalisedRatioMeasure;
import ifc2x3javatoolbox.ifc2x3tc1.IfcObject;
import ifc2x3javatoolbox.ifc2x3tc1.IfcObjectPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcOpeningElement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProductRepresentation;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProfileDef;
import ifc2x3javatoolbox.ifc2x3tc1.IfcPropertySetDefinition;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRatioMeasure;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRectangleProfileDef;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelDefines;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelDefinesByType;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelFillsElement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelVoidsElement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSlab;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSlabTypeEnum;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSpatialStructureElement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWindow;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWindowLiningProperties;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWindowPanelOperationEnum;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWindowPanelPositionEnum;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWindowPanelProperties;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWindowStyle;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWindowStyleConstructionEnum;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWindowStyleOperationEnum;
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
        IfcCartesianPoint ifcCartesianPointOriginWorld = createCartesianPoint3D(0.0,0.0,0.0);
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
        IfcCartesianPoint ifcCartesianPointOriginSite = createCartesianPoint3D(0.0,0.0,0.0);
        IfcDirection ifcDirectionZAxisSite = createDirection3D(0.0,0.0,1.0);
        IfcDirection ifcDirectionXDirectionSite = createDirection3D(1.0,0.0,0.0);
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
        IfcCartesianPoint ifcCartesianPointOriginBuilding = createCartesianPoint3D(0.0,0.0,0.0);
        IfcDirection ifcDirectionZAxisBuilding = createDirection3D(0.0,0.0,1.0);
        IfcDirection ifcDirectionXDirectionBuilding = createDirection3D(1.0,0.0,0.0);
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
        IfcCartesianPoint ifcCartesianPointOriginBuildingStorey = createCartesianPoint3D(0.0,0.0,0.0);
        IfcDirection ifcDirectionZAxisBuildingStorey = createDirection3D(0.0,0.0,1.0);
        IfcDirection ifcDirectionXDirectionBuildingStorey = createDirection3D(1.0,0.0,0.0);
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

    // Permet de récupérer l'étage dans lequel se trouve un wall
    public static IfcSpatialStructureElement getBuildingStorey (IfcModel ifcModel, IfcWallStandardCase wall){
        SET<IfcRelContainedInSpatialStructure> relContainedInSpatialStructure = wall.getContainedInStructure_Inverse();
        Iterator<IfcRelContainedInSpatialStructure> it = relContainedInSpatialStructure.iterator();
        return it.next().getRelatingStructure();
    }

    // Permet de récupérer un WallStandardCase dans un model en fonction de son nom
    public static IfcWallStandardCase getWall (IfcModel ifcModel, String nameWall){
        Collection<IfcWallStandardCase> collectionWallStandardCase = ifcModel.getCollection(IfcWallStandardCase.class);
        for (IfcWallStandardCase actualWall : collectionWallStandardCase){
            if(actualWall.getName().getDecodedValue().equals(nameWall)){
                return actualWall;
            }
        }
        return null;
    }

    // Permet de récupérer un Opening dans un model en fonction de son nom
    public static IfcOpeningElement getOpening (IfcModel ifcModel, String nameOpening){
        Collection<IfcOpeningElement> collectionOpening = ifcModel.getCollection(IfcOpeningElement.class);
        for (IfcOpeningElement actualOpening : collectionOpening){
            if(actualOpening.getName().getDecodedValue().equals(nameOpening)){
                return actualOpening;
            }
        }
        return null;
    }

    // Permet de récupérer une Door dans un model en fonction de son nom
    public static IfcDoor getDoor (IfcModel ifcModel, String nameDoor){
        Collection<IfcDoor> collectionDoor = ifcModel.getCollection(IfcDoor.class);
        for (IfcDoor actualDoor : collectionDoor){
            if(actualDoor.getName().getDecodedValue().equals(nameDoor)){
                return actualDoor;
            }
        }
        return null;
    }

    // Permet de récupérer une Door dans un model en fonction de son nom
    public static IfcWindow getWindow (IfcModel ifcModel, String nameWindow){
        Collection<IfcWindow> collectionWindow = ifcModel.getCollection(IfcWindow.class);
        for (IfcWindow actualWindow : collectionWindow){
            if(actualWindow.getName().getDecodedValue().equals(nameWindow)){
                return actualWindow;
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

    // Permet de récupérer la liaison entre un openingElement et un relFillsElement
    public static IfcRelFillsElement getRelFillsElement (IfcModel ifcModel, IfcOpeningElement openingElement){
        Collection<IfcRelFillsElement> collectionRelFillsElement = ifcModel.getCollection(IfcRelFillsElement.class);
        for (IfcRelFillsElement actualRelFillsElement : collectionRelFillsElement){
            if(actualRelFillsElement.getRelatingOpeningElement().equals(openingElement)){
                return (actualRelFillsElement);
            }
        }
        return null;
    }

    // Permet de créer un point cartésien en 2D à partir de deux doubles
    public static IfcCartesianPoint createCartesianPoint2D(double x, double y){
        LIST<IfcLengthMeasure> coordinates = new LIST<>();
        coordinates.add(new IfcLengthMeasure(x));
        coordinates.add(new IfcLengthMeasure(y));
        return new IfcCartesianPoint(coordinates);
    }

    // Permet de créer un point cartésien en 3D à partir de trois doubles
    public static IfcCartesianPoint createCartesianPoint3D(double x, double y, double z){
        LIST<IfcLengthMeasure> coordinates = new LIST<>();
        coordinates.add(new IfcLengthMeasure(x));
        coordinates.add(new IfcLengthMeasure(y));
        coordinates.add(new IfcLengthMeasure(z));
        return new IfcCartesianPoint(coordinates);
    }

    // Permet de créer une direction à partir de deux doubles
    public static IfcDirection createDirection2D(double x, double y){
        LIST<DOUBLE> coordinates = new LIST<>();
        coordinates.add(new DOUBLE(x));
        coordinates.add(new DOUBLE(y));
        return new IfcDirection(coordinates);
    }

    // Permet de créer une direction à partir de trois doubles
    public static IfcDirection createDirection3D(double x, double y, double z){
        LIST<DOUBLE> coordinates = new LIST<>();
        coordinates.add(new DOUBLE(x));
        coordinates.add(new DOUBLE(y));
        coordinates.add(new DOUBLE(z));
        return new IfcDirection(coordinates);
    }

    // Permet d'ajouter un étage au batiment en lui rentrant son nom et son élévation
    public static void addBuildingStorey (IfcModel ifcModel,String nameFloor , double elevation){
        IfcCartesianPoint ifcCartesianPointOriginBuildingStorey = createCartesianPoint3D(0.0,0.0,elevation);
        IfcDirection ifcDirectionZAxisBuildingStorey = createDirection3D(0.0,0.0,1.0);
        IfcDirection ifcDirectionXDirectionBuildingStorey = createDirection3D(1.0,0.0,0.0);
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
    public static void addWall(IfcModel ifcModel, String nameBuildingStorey, String nameWall, double wallLength, double wallHeight, double wallThickness, double posX, double posY, double dirX, double dirY){
        IfcBuildingStorey buildingStorey = getBuildingStorey(ifcModel,nameBuildingStorey);
        IfcCartesianPoint ifcCartesianPointOriginWall = createCartesianPoint3D(posX,posY,0.0);
        IfcDirection ifcDirectionZAxisWall = createDirection3D(0.0,0.0,1.0);
        IfcDirection ifcDirectionXDirectionWall = createDirection3D(dirX,dirY,0.0);
        IfcAxis2Placement3D ifcAxis2Placement3DWall = new IfcAxis2Placement3D(
                ifcCartesianPointOriginWall, ifcDirectionZAxisWall, ifcDirectionXDirectionWall);
        IfcLocalPlacement ifcLocalPlacementWall = new IfcLocalPlacement(buildingStorey.getObjectPlacement(),
                ifcAxis2Placement3DWall);
        LIST<IfcRepresentation> ifcWallRepresentationsList = new LIST<>();

        // First representation : Geometric representation (2D)
        IfcCartesianPoint ifcWallPoints2D1 = createCartesianPoint2D(0.0,0.0);
        IfcCartesianPoint ifcWallPoints2D2 = createCartesianPoint2D(wallLength,0.0);
        IfcDirection ifcWallAxisDirection = createDirection2D(1.0,0.0);
        IfcVector wallAxisVector = new IfcVector(ifcWallAxisDirection,new IfcLengthMeasure(wallLength));
        IfcCartesianPoint ifcWallPoints2D0 = createCartesianPoint2D(0.0,0.0);
        IfcLine wallAxisLine =new IfcLine(ifcWallPoints2D0,wallAxisVector);
        SET<IfcTrimmingSelect> Trim1 = new SET<>();
        SET<IfcTrimmingSelect> Trim2 = new SET<>();
        Trim1.add(ifcWallPoints2D1);
        Trim2.add(ifcWallPoints2D2);
        IfcTrimmedCurve wallTrimmedCurve = new IfcTrimmedCurve(wallAxisLine,Trim1,Trim2,
                new BOOLEAN(true),new IfcTrimmingPreference("CARTESIAN"));
        SET<IfcRepresentationItem> ifcWallRepresentation2DItem = new SET<>();
        ifcWallRepresentation2DItem.add(wallTrimmedCurve);
        IfcShapeRepresentation ifcWallCurve2DRepresentation = new IfcShapeRepresentation(getGeometricRepresentationContext(ifcModel),
                new IfcLabel ("Axis",true), new IfcLabel("Curve2D",true), ifcWallRepresentation2DItem);
        ifcWallRepresentationsList.add(ifcWallCurve2DRepresentation);

        // Second representation : SweptSolid representation (3D)
        LIST<IfcCartesianPoint> wallAllPoints = new LIST<>();
        IfcCartesianPoint wallCartesianPoint1 = createCartesianPoint2D(0.0,wallThickness/2);
        wallAllPoints.add(wallCartesianPoint1);
        IfcCartesianPoint wallCartesianPoint2 = createCartesianPoint2D(wallLength,wallThickness/2);
        wallAllPoints.add(wallCartesianPoint2);
        IfcCartesianPoint wallCartesianPoint3 = createCartesianPoint2D(wallLength,-wallThickness/2);
        wallAllPoints.add(wallCartesianPoint3);
        IfcCartesianPoint wallCartesianPoint4 = createCartesianPoint2D(0.0,-wallThickness/2);
        wallAllPoints.add(wallCartesianPoint4);
        IfcPolyline wallPolyline = new IfcPolyline(wallAllPoints);
        IfcArbitraryClosedProfileDef wallArbitraryClosedProfileDef = new IfcArbitraryClosedProfileDef(
                new IfcProfileTypeEnum ("AREA"), null, wallPolyline);
        IfcCartesianPoint ifcCartesianPointOriginWallRepresentation = createCartesianPoint3D(0.0,0.0,0.0);
        IfcDirection ifcDirectionZAxisWallRepresentation = createDirection3D(0.0,0.0,1.0);
        IfcDirection ifcDirectionXDirectionWallRepresentation = createDirection3D(1.0,0.0,0.0);
        IfcAxis2Placement3D ifcAxis2Placement3DWallRepresentation = new IfcAxis2Placement3D(
                ifcCartesianPointOriginWallRepresentation, ifcDirectionZAxisWallRepresentation, ifcDirectionXDirectionWallRepresentation);
        IfcDirection ifcWallExtrudedDirection = createDirection3D(0.0,0.0,1.0);
        IfcLengthMeasure lengthExtrusion = new IfcLengthMeasure(wallHeight);
        IfcExtrudedAreaSolid extrudedWall = new IfcExtrudedAreaSolid(wallArbitraryClosedProfileDef,
                ifcAxis2Placement3DWallRepresentation,ifcWallExtrudedDirection,new IfcPositiveLengthMeasure(lengthExtrusion));
        SET<IfcRepresentationItem> ifcWallRepresentation3DItem = new SET<>();
        ifcWallRepresentation3DItem.add(extrudedWall);
        IfcShapeRepresentation ifcWallSweptSolidRepresentation = new IfcShapeRepresentation(getGeometricRepresentationContext(ifcModel),
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
        IfcRelContainedInSpatialStructure relContainedInSpatialStructure = IfcHelper.getRelContainedInSpatialStructure(ifcModel,nameBuildingStorey);
        if(relContainedInSpatialStructure==null){
            SET<IfcProduct> relatedObject;
            relatedObject = new SET<>();
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
        IfcCartesianPoint ifcCartesianPointOriginSlab = createCartesianPoint3D(0.0,0.0,0.0);
        IfcDirection ifcDirectionZAxisLocalSlab = createDirection3D(0.0,0.0,1.0);
        IfcDirection ifcDirectionXDirectionLocalSlab = createDirection3D(1.0,0.0,0.0);
        IfcAxis2Placement3D ifcAxis2Placement3DSlab = new IfcAxis2Placement3D(
                ifcCartesianPointOriginSlab, ifcDirectionZAxisLocalSlab, ifcDirectionXDirectionLocalSlab);
        IfcLocalPlacement ifcLocalPlacementSlab = new IfcLocalPlacement(buildingStorey.getObjectPlacement(),
                ifcAxis2Placement3DSlab);
        LIST<IfcRepresentation> ifcSlabRepresentationsList = new LIST<>();

        // Slab representation : SweptSolid representation (3D)
        IfcPolyline slabPolyline = new IfcPolyline(listSlabCartesianPoint);
        IfcArbitraryClosedProfileDef slabArbitraryClosedProfileDef = new IfcArbitraryClosedProfileDef(
                new IfcProfileTypeEnum ("AREA"), null, slabPolyline);
        IfcCartesianPoint ifcCartesianPointOriginSlabRepresentation = createCartesianPoint3D(0.0,0.0,0.0);
        IfcDirection ifcDirectionZAxisSlabRepresentation = createDirection3D(0.0,0.0,1.0);
        IfcDirection ifcDirectionXDirectionSlabRepresentation = createDirection3D(1.0,0.0,0.0);
        IfcAxis2Placement3D ifcAxis2Placement3DSlabRepresentation = new IfcAxis2Placement3D(
                ifcCartesianPointOriginSlabRepresentation, ifcDirectionZAxisSlabRepresentation, ifcDirectionXDirectionSlabRepresentation);
        IfcDirection ifcSlabExtrudedDirection = createDirection3D(0.0,0.0,1.0);
        IfcLengthMeasure lengthExtrusion = new IfcLengthMeasure(0.2);
        IfcExtrudedAreaSolid extrudedSlab = new IfcExtrudedAreaSolid(slabArbitraryClosedProfileDef,
                ifcAxis2Placement3DSlabRepresentation,ifcSlabExtrudedDirection,new IfcPositiveLengthMeasure(lengthExtrusion));
        SET<IfcRepresentationItem> ifcSlabRepresentation3DItem = new SET<>();
        ifcSlabRepresentation3DItem.add(extrudedSlab);
        IfcShapeRepresentation ifcSlabSweptSolidRepresentation = new IfcShapeRepresentation(getGeometricRepresentationContext(ifcModel),
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
        IfcRelContainedInSpatialStructure relContainedInSpatialStructure = IfcHelper.getRelContainedInSpatialStructure(ifcModel,nameBuildingStorey);
        if(relContainedInSpatialStructure==null){
            SET<IfcProduct> relatedObject;
            relatedObject = new SET<>();
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

    // Permet d'ajouter un opening à un mur
    public static void addOpening (IfcModel ifcModel, String nameOpening,IfcWallStandardCase wall, double openingWidth, double openingHeight, double openingThickness, double xLocal, double zLocal){

        IfcCartesianPoint localPointOpening = createCartesianPoint3D(xLocal,-openingThickness/2,zLocal);
        IfcDirection zLocalOpening = createDirection3D(0.0,0.0,1.0);
        IfcDirection xLocalOpening = createDirection3D(1.0,0.0,0.0);
        IfcAxis2Placement3D placementOpening = new IfcAxis2Placement3D(
                localPointOpening, zLocalOpening, xLocalOpening);
        IfcLocalPlacement localPlacementOpening = new IfcLocalPlacement(wall.getObjectPlacement(),
                placementOpening);
        LIST<IfcRepresentation> openingRepresentationsList = new LIST<>();

        // Opening geometry with extruded area solid placement
        IfcDirection zLocalExtrusion = createDirection3D(0.0,1.0,0.0);
        IfcDirection xLocalExtrusion = createDirection3D(0.0,0.0,1.0);
        IfcCartesianPoint centerOpening = createCartesianPoint3D(openingWidth/2,0.0,openingHeight/2);
        IfcAxis2Placement3D placementCenterOpening = new IfcAxis2Placement3D(
                centerOpening, zLocalExtrusion, xLocalExtrusion);
        IfcDirection zLocalRectangle = createDirection2D(1.0, 0.0);
        IfcCartesianPoint originOpening = createCartesianPoint2D(0.0,0.0);
        IfcAxis2Placement2D placementRectangle = new IfcAxis2Placement2D(
                originOpening, zLocalRectangle);
        IfcRectangleProfileDef rectangle = new IfcRectangleProfileDef(new IfcProfileTypeEnum("AREA"),
                null,placementRectangle,new IfcPositiveLengthMeasure(new IfcLengthMeasure(openingHeight)),
                new IfcPositiveLengthMeasure(new IfcLengthMeasure(openingWidth)));
        IfcDirection openingExtrusionDirection = createDirection3D(0.0,0.0,1.0);
        IfcExtrudedAreaSolid extrudedOpening = new IfcExtrudedAreaSolid(rectangle, placementCenterOpening,
                openingExtrusionDirection,new IfcPositiveLengthMeasure(new IfcLengthMeasure(openingThickness)));
        SET<IfcRepresentationItem> openingRepresentation3DItem = new SET<>();
        openingRepresentation3DItem.add(extrudedOpening);
        IfcShapeRepresentation openingSweptSolidRepresentation = new IfcShapeRepresentation(getGeometricRepresentationContext(ifcModel),
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

        // add new Ifc-objects to the model
        ifcModel.addIfcObject(relWallToOpening);

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

    // Permet d'ajouter une door à un wall
    public static void addDoor (IfcModel ifcModel, String nameDoor,IfcWallStandardCase wall, double doorWidth, double doorHeight, double wallThickness, double xLocal, double zLocal){
        IfcBuildingStorey buildingStorey = (IfcBuildingStorey) getBuildingStorey(ifcModel,wall);
        addOpening(ifcModel,nameDoor,wall,doorWidth,doorHeight,wallThickness,xLocal,zLocal);
        IfcOpeningElement opening = getOpening(ifcModel, nameDoor);

        // Door style definitions
        SET<IfcPropertySetDefinition> propertySetDefinitions = new SET<>();

        IfcDoorLiningProperties doorLiningProperties = new IfcDoorLiningProperties(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
                null, null, new IfcPositiveLengthMeasure(new IfcLengthMeasure(0.12)), new IfcPositiveLengthMeasure(new IfcLengthMeasure(0.05)),
                null, null, null, null, null, null, null, null, null);

        IfcDoorPanelProperties doorPanelProperties = new IfcDoorPanelProperties(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
                null, null, new IfcPositiveLengthMeasure(new IfcLengthMeasure(0.05)), new IfcDoorPanelOperationEnum("SWINGING"),
                new IfcNormalisedRatioMeasure(new IfcRatioMeasure(1.0)), new IfcDoorPanelPositionEnum("LEFT"), null);

        propertySetDefinitions.add(doorPanelProperties);
        propertySetDefinitions.add(doorLiningProperties);

        IfcDoorStyle doorStyle = new IfcDoorStyle(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
                new IfcLabel("Standard", true), null, new IfcLabel(), propertySetDefinitions, null, new IfcLabel(),
                new IfcDoorStyleOperationEnum("SINGLE_SWING_LEFT"), new IfcDoorStyleConstructionEnum("NOTDEFINED"),
                new BOOLEAN(true), new BOOLEAN(false));

        // Door definition
        IfcCartesianPoint localPointDoor = createCartesianPoint3D(0.0,0.16,0.0);
        IfcDirection zLocalDoor = createDirection3D(1.0,0.0,0.0);
        IfcDirection xLocalDoor = createDirection3D(0.0,0.0,1.0);
        IfcAxis2Placement3D placementDoor = new IfcAxis2Placement3D(
                localPointDoor, zLocalDoor, xLocalDoor);
        IfcLocalPlacement localPlacementDoor = new IfcLocalPlacement(opening.getObjectPlacement(),
                placementDoor);

        // Create the door
        IfcDoor door = new IfcDoor(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(), new IfcLabel(nameDoor, true),
                new IfcText("", true), null, localPlacementDoor,
                null, null, new IfcPositiveLengthMeasure(new IfcLengthMeasure(10.2)), new IfcPositiveLengthMeasure(new IfcLengthMeasure(1.0)));

        // Create relation buildingStorey -> door
        IfcRelContainedInSpatialStructure relContainedInSpatialStructure = IfcHelper.getRelContainedInSpatialStructure(ifcModel,buildingStorey.getName().getDecodedValue());
        if(relContainedInSpatialStructure==null){
            SET<IfcProduct> relatedObject;
            relatedObject = new SET<>();
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

        // Create relation IfcDoor <-> IfcDoorStyle
        SET<IfcObject> ifcDoorSET = new SET<>();
        ifcDoorSET.add(door);
        IfcRelDefinesByType relDefinesByType = new IfcRelDefinesByType(new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcModel.getIfcProject().getOwnerHistory(),new IfcLabel("Door Container", true),
                new IfcText("DoorContainer for DoorStyle",true),ifcDoorSET,doorStyle);

        // add new Ifc-objects to the model
        ifcModel.addIfcObject(relFillsElement);

        ifcModel.addIfcObject(door);
        ifcModel.addIfcObject(localPlacementDoor);
        ifcModel.addIfcObject(placementDoor);
        ifcModel.addIfcObject(localPointDoor);
        ifcModel.addIfcObject(zLocalDoor);
        ifcModel.addIfcObject(xLocalDoor);

        ifcModel.addIfcObject(relDefinesByType);
        ifcModel.addIfcObject(doorStyle);
        ifcModel.addIfcObject(doorLiningProperties);
        ifcModel.addIfcObject(doorPanelProperties);
    }

    // Permet d'ajouter une window à un wall
    public static void addWindow (IfcModel ifcModel, String nameWindow,IfcWallStandardCase wall, double windowWidth, double windowHeight, double wallThickness, double xLocal, double zLocal){
        IfcBuildingStorey buildingStorey = (IfcBuildingStorey) getBuildingStorey(ifcModel,wall);
        addOpening(ifcModel,nameWindow,wall,windowWidth,windowHeight,wallThickness,xLocal,zLocal);
        IfcOpeningElement opening = getOpening(ifcModel, nameWindow);

        // Window style definitions
        SET<IfcPropertySetDefinition> propertySetDefinitions = new SET<>();

        IfcWindowLiningProperties windowLiningProperties = new IfcWindowLiningProperties(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
                null, null, new IfcPositiveLengthMeasure(new IfcLengthMeasure(0.1)), new IfcPositiveLengthMeasure(new IfcLengthMeasure(0.05)),
                null, null, null, null, null, null, null);

        IfcWindowPanelProperties windowPanelProperties = new IfcWindowPanelProperties(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
                null, null, new IfcWindowPanelOperationEnum("SIDEHUNGLEFTHAND"), new IfcWindowPanelPositionEnum("NOTDEFINED"),
                new IfcPositiveLengthMeasure(new IfcLengthMeasure(0.5)), new IfcPositiveLengthMeasure(new IfcLengthMeasure(0.5)), null);

        propertySetDefinitions.add(windowPanelProperties);
        propertySetDefinitions.add(windowLiningProperties);

        IfcWindowStyle windowStyle = new IfcWindowStyle(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()), ifcModel.getIfcProject().getOwnerHistory(),
                new IfcLabel("Standard", true), null, new IfcLabel(), propertySetDefinitions, null, new IfcLabel(),
                new IfcWindowStyleConstructionEnum("NOTDEFINED"), new IfcWindowStyleOperationEnum("SINGLE_PANEL"),
                new BOOLEAN(true), new BOOLEAN(false));

        // Window definition
        IfcCartesianPoint localPointWindow = createCartesianPoint3D(0.0,0.05,1.01);
        IfcDirection zLocalWindow = createDirection3D(1.0,0.0,0.0);
        IfcDirection xLocalWindow = createDirection3D(0.0,0.0,-1.0);
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
        IfcRelContainedInSpatialStructure relContainedInSpatialStructure = IfcHelper.getRelContainedInSpatialStructure(ifcModel,buildingStorey.getName().getDecodedValue());
        if(relContainedInSpatialStructure==null){
            SET<IfcProduct> relatedObject;
            relatedObject = new SET<>();
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
        SET<IfcObject> ifcWindowSET = new SET<>();
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
        ifcModel.addIfcObject(windowPanelProperties);

    }

    // Permet de supprimer le placement d'un objet dans un model
    public static void deleteObjectPlacement(IfcModel ifcModel, IfcObjectPlacement objectPlacement){
        if(objectPlacement instanceof IfcLocalPlacement){
            IfcAxis2Placement axis2Placement =((IfcLocalPlacement) objectPlacement).getRelativePlacement();
            ifcModel.removeIfcObject(objectPlacement);
            if (axis2Placement instanceof IfcAxis2Placement2D){
                IfcHelper.deleteAxis2Placement2D(ifcModel, (IfcAxis2Placement2D) axis2Placement);
            }else{
                // axis2Placement instanceof IfcAxis2Placement3D
                IfcHelper.deleteAxis2Placement3D(ifcModel,(IfcAxis2Placement3D)axis2Placement);
            }
        }else{
            // objectPlacement instanceof IfcGridPlacement
        }
    }

    // Permet de supprimer le placement d'un objet dans un model
    public static void deleteAxis2Placement3D(IfcModel ifcModel, IfcAxis2Placement3D axis2Placement3D){
        IfcCartesianPoint cartesianPoint = axis2Placement3D.getLocation();
        IfcDirection axis = axis2Placement3D.getAxis();
        IfcDirection refDirection = axis2Placement3D.getRefDirection();
        ifcModel.removeIfcObject(axis2Placement3D);
        ifcModel.removeIfcObject(cartesianPoint);
        ifcModel.removeIfcObject(axis);
        ifcModel.removeIfcObject(refDirection);
    }

    // Permet de supprimer le placement d'un objet dans un model
    public static void deleteAxis2Placement2D(IfcModel ifcModel, IfcAxis2Placement2D axis2Placement2D){
        IfcCartesianPoint cartesianPoint = axis2Placement2D.getLocation();
        IfcDirection refDirection = axis2Placement2D.getRefDirection();
        ifcModel.removeIfcObject(axis2Placement2D);
        ifcModel.removeIfcObject(cartesianPoint);
        ifcModel.removeIfcObject(refDirection);
    }

    // Permet de supprimer une représentation
    public static void deleteRepresentation(IfcModel ifcModel, IfcProductRepresentation productRepresentation){
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
                        IfcHelper.deleteAxis2Placement3D(ifcModel, extrudedAreaSolid.getPosition());
                        IfcProfileDef profileDef = extrudedAreaSolid.getSweptArea();
                        if (profileDef instanceof IfcRectangleProfileDef){
                            IfcRectangleProfileDef rectangleProfileDef = (IfcRectangleProfileDef)profileDef;
                            ifcModel.removeIfcObject(rectangleProfileDef);
                            IfcHelper.deleteAxis2Placement2D(ifcModel,rectangleProfileDef.getPosition());
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
    public static void deleteOpening(IfcModel ifcModel, IfcOpeningElement opening){

        // Remove the opening placement
        IfcHelper.deleteObjectPlacement(ifcModel, opening.getObjectPlacement());

        // Remove the relation WallStandardCase <-> OpeningElement
        ifcModel.removeIfcObject(opening.getVoidsElements_Inverse());

        // Remove the representation
        IfcHelper.deleteRepresentation(ifcModel, opening.getRepresentation());

        // Remove the opening
        ifcModel.removeIfcObject(opening);

    }

    // Permet de supprimer une door dans un model
    public static void deleteDoor (IfcModel ifcModel, IfcDoor door){

        // Remove the door placement
        IfcHelper.deleteObjectPlacement(ifcModel, door.getObjectPlacement());

        // Remove the opening
        IfcHelper.deleteOpening(ifcModel,IfcHelper.getOpening(ifcModel, door.getName().getDecodedValue()));

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
    public static void deleteWindow (IfcModel ifcModel, IfcWindow window){

        // Remove the window placement
        IfcHelper.deleteObjectPlacement(ifcModel, window.getObjectPlacement());

        // Remove the opening
        IfcHelper.deleteOpening(ifcModel,IfcHelper.getOpening(ifcModel,window.getName().getDecodedValue()));

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

        // Remove the door
        ifcModel.removeIfcObject(window);
    }

    // Permet de supprimer un standardWall dans un model avec tous les éléments associés (doors, windows, ...)
    public static void deleteWallStandardCase (IfcModel ifcModel, IfcWallStandardCase wallStandardCase){

        // Remove the wall placement
        IfcHelper.deleteObjectPlacement(ifcModel, wallStandardCase.getObjectPlacement());

        // Remove all the elements associated to the Wall
        SET<IfcRelVoidsElement> relVoidsElementSET = wallStandardCase.getHasOpenings_Inverse();
        if(relVoidsElementSET.isEmpty()){
            // No openings Element -> do nothing
        }
        else{
            for(IfcRelVoidsElement actualRelVoidsElement : relVoidsElementSET){
                IfcElement openingElement = actualRelVoidsElement.getRelatedOpeningElement();
                if(openingElement instanceof IfcOpeningElement){
                    IfcRelFillsElement relFillsElement = IfcHelper.getRelFillsElement(ifcModel,(IfcOpeningElement) openingElement);
                    IfcElement buildingElement =  relFillsElement.getRelatedBuildingElement();
                    if (buildingElement instanceof IfcDoor){
                        IfcHelper.deleteDoor(ifcModel,(IfcDoor) buildingElement);
                    }
                    else if (buildingElement instanceof IfcWindow){
                        IfcHelper.deleteWindow(ifcModel,(IfcWindow) buildingElement);
                    }
                }
            }
        }

        // Remove the representations
        IfcHelper.deleteRepresentation(ifcModel, wallStandardCase.getRepresentation());

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
