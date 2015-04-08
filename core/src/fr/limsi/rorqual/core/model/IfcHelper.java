package fr.limsi.rorqual.core.model;

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
import ifc2x3javatoolbox.ifc2x3tc1.IfcVector;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLocalPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcBuildingStorey;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDirection;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSite;
import ifc2x3javatoolbox.ifc2x3tc1.LIST;
import ifc2x3javatoolbox.ifc2x3tc1.SET;
import ifc2x3javatoolbox.ifc2x3tc1.STRING;
import ifc2x3javatoolbox.ifc2x3tc1.IfcChangeActionEnum.IfcChangeActionEnum_internal;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSIUnitName.IfcSIUnitName_internal;
import ifc2x3javatoolbox.ifc2x3tc1.IfcUnitEnum.IfcUnitEnum_internal;

import java.io.File;
import java.io.IOException;

/**
 * Created by ricordeau on 08/04/2015.
 */
public class IfcHelper {


    // Permet de compléter un model de départ contenant les informations du projet, un site,
    // un building et un building storey
    public static IfcModel initialiseAnIfcModel (IfcModel ifcModel){

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
        LIST<IfcLengthMeasure> coordinatesOrigin = new LIST<IfcLengthMeasure>();
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
        SET<IfcRepresentationContext> contexts = new SET<IfcRepresentationContext>();
        contexts.add(ifcGeometricRepresentationContext);

        // Create UnitsInContext
        SET<IfcUnit> units = new SET<IfcUnit>();
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
        LIST<IfcLengthMeasure> coordinatesSite = new LIST<IfcLengthMeasure>();
        coordinatesSite.add(new IfcLengthMeasure(0.0));
        coordinatesSite.add(new IfcLengthMeasure(0.0));
        coordinatesSite.add(new IfcLengthMeasure(0.0));
        IfcCartesianPoint ifcCartesianPointOriginSite = new IfcCartesianPoint(coordinatesSite);
        LIST<DOUBLE> ZAxisSite = new LIST<DOUBLE>();
        ZAxisSite.add(new DOUBLE(0.0));
        ZAxisSite.add(new DOUBLE(0.0));
        ZAxisSite.add(new DOUBLE(1.0));
        IfcDirection ifcDirectionZAxisSite = new IfcDirection(ZAxisSite);
        LIST<DOUBLE> XDirectionSite = new LIST<DOUBLE>();
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
        LIST<IfcLengthMeasure> coordinatesBuilding = new LIST<IfcLengthMeasure>();
        coordinatesBuilding.add(new IfcLengthMeasure(0.0));
        coordinatesBuilding.add(new IfcLengthMeasure(0.0));
        coordinatesBuilding.add(new IfcLengthMeasure(0.0));
        IfcCartesianPoint ifcCartesianPointOriginBuilding = new IfcCartesianPoint(coordinatesBuilding);
        LIST<DOUBLE> ZAxisBuilding = new LIST<DOUBLE>();
        ZAxisBuilding.add(new DOUBLE(0.0));
        ZAxisBuilding.add(new DOUBLE(0.0));
        ZAxisBuilding.add(new DOUBLE(1.0));
        IfcDirection ifcDirectionZAxisBuilding = new IfcDirection(ZAxisBuilding);
        LIST<DOUBLE> XDirectionBuilding = new LIST<DOUBLE>();
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
        LIST<IfcLengthMeasure> coordinatesBuildingStorey = new LIST<IfcLengthMeasure>();
        coordinatesBuildingStorey.add(new IfcLengthMeasure(0.0));
        coordinatesBuildingStorey.add(new IfcLengthMeasure(0.0));
        coordinatesBuildingStorey.add(new IfcLengthMeasure(0.0));
        IfcCartesianPoint ifcCartesianPointOriginBuildingStorey = new IfcCartesianPoint(coordinatesBuildingStorey);
        LIST<DOUBLE> ZAxisBuildingStorey = new LIST<DOUBLE>();
        ZAxisBuildingStorey.add(new DOUBLE(1.0));
        ZAxisBuildingStorey.add(new DOUBLE(0.0));
        ZAxisBuildingStorey.add(new DOUBLE(0.0));
        IfcDirection ifcDirectionZAxisBuildingStorey = new IfcDirection(ZAxisBuildingStorey);
        LIST<DOUBLE> XDirectionBuildingStorey = new LIST<DOUBLE>();
        XDirectionBuildingStorey.add(new DOUBLE(0.0));
        XDirectionBuildingStorey.add(new DOUBLE(0.0));
        XDirectionBuildingStorey.add(new DOUBLE(1.0));
        IfcDirection ifcDirectionXDirectionBuildingStorey = new IfcDirection(XDirectionBuildingStorey);
        IfcAxis2Placement3D ifcAxis2Placement3DBuildingStorey = new IfcAxis2Placement3D(
                ifcCartesianPointOriginBuildingStorey, ifcDirectionZAxisBuildingStorey, ifcDirectionXDirectionBuildingStorey);
        IfcLocalPlacement ifcLocalPlacementBuildingStorey = new IfcLocalPlacement(null,
                ifcAxis2Placement3DBuildingStorey);
        IfcBuildingStorey ifcBuildingStorey = new IfcBuildingStorey(
                new IfcGloballyUniqueId(ifcModel.getNewGlobalUniqueId()),
                ifcOwnerHistory, new IfcLabel("1er etage", true),
                new IfcText("Description du 1er etage", true), null, ifcLocalPlacementBuildingStorey,
                null, null, new IfcElementCompositionEnum("ELEMENT"), null);

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

        // Create relation IfcBuilding --> IfcBuildingStorey
        relatedObjects = new SET<IfcObjectDefinition>();
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

    // Permet d'exporter le model au format .ifc sur l'ordinateur
    public static void saveIfcModel(IfcModel ifcModel){
        File saveStepFile = new File("C:Projet3DReno.ifc");
        try {
            ifcModel.writeStepfile(saveStepFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
