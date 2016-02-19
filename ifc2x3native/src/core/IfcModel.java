package core;

import java.util.ArrayList;

import core.IfcRules;

/**
 * Created by christophe on 17/02/16.
 */
public class IfcModel {

    IfcRules.IFCOWNERHISTORY IFCOWNERHISTORY;
    IfcRules.IFCPROJECT IFCPROJECT;
    String filename = "projet.ifc";
    IfcRules.IFCBUILDINGSTOREY ACTIVE_IFCBUILDINGSTOREY;
    IfcRules.IFCRELAGGREGATES STOREYS_IFCRELAGGREGATES;

    public IfcModel() {
        IfcRules.restRules();
        PROJECT();
        BUILDING();
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {

        String data = "";
        for (IfcRules rule : IfcRules.getRules())
            data += rule;

        return FILE (
                HEADER(filename),
                DATA(data)
            );
    }


    public String HEADER(String filename) {
        return
            "HEADER;\n" +
            "FILE_DESCRIPTION(('ViewDefinition [CoordinationView]'),'2;1');\n" +
            "FILE_NAME('" + filename + "','2016-02-17T15:08:32+0100',('Thomas Ricordeau','Julien Christophe'),('LIMSI-CNRS'),'IFC2x3native','','');\n" +
            "FILE_SCHEMA(('IFC2X3'));\n" +
            "ENDSEC;\n";
    }

    public String DATA(String content) {
        return
            "DATA;\n" +
            content +
            "ENDSEC;\n";
    }

    public String FILE(String header, String data) {
        return
            "ISO-10303-21;\n" +
            header +
            "\n" +
            data +
            "\n" +
            "END-ISO-10303-21;\n";
    }

    public void PROJECT() {
        IfcRules.IFCPERSON IFCPERSON1 = new IfcRules.IFCPERSON("Ricordeau", "Thomas");
        IfcRules.IFCPERSON IFCPERSON2 = new IfcRules.IFCPERSON("Christophe", "Julien");
        IfcRules.IFCORGANIZATION IFCORGANIZATION = new IfcRules.IFCORGANIZATION("LIMSI-CNRS");

        IfcRules.IFCPERSONANDORGANIZATION IFCPERSONANDORGANIZATION1 = new IfcRules.IFCPERSONANDORGANIZATION(IFCPERSON1, IFCORGANIZATION);
        IfcRules.IFCPERSONANDORGANIZATION IFCPERSONANDORGANIZATION2 = new IfcRules.IFCPERSONANDORGANIZATION(IFCPERSON2, IFCORGANIZATION);

        IfcRules.IFCAPPLICATION IFCAPPLICATION = new IfcRules.IFCAPPLICATION(IFCORGANIZATION, "1.0f", "3D-Reno Application");

        this.IFCOWNERHISTORY = new IfcRules.IFCOWNERHISTORY(IFCPERSONANDORGANIZATION1, IFCAPPLICATION);

        IfcRules.IFCCARTESIANPOINT IFCCARTESIANPOINT = new IfcRules.IFCCARTESIANPOINT(0,0,0);
        IfcRules.IFCAXIS2PLACEMENT3D IFCAXIS2PLACEMENT3D = new IfcRules.IFCAXIS2PLACEMENT3D(IFCCARTESIANPOINT, null, null);
        IfcRules.IFCGEOMETRICREPRESENTATIONCONTEXT IFCGEOMETRICREPRESENTATIONCONTEXT = new IfcRules.IFCGEOMETRICREPRESENTATIONCONTEXT(IFCAXIS2PLACEMENT3D);

        IfcRules.IFCSIUNIT IFCSIUNIT1 = new IfcRules.IFCSIUNIT(IfcRules.CONST.LENGTHUNIT, IfcRules.CONST.METRE);
        IfcRules.IFCSIUNIT IFCSIUNIT2 = new IfcRules.IFCSIUNIT(IfcRules.CONST.PLANEANGLEUNIT, IfcRules.CONST.RADIAN);
        IfcRules.IFCSIUNIT IFCSIUNIT3 = new IfcRules.IFCSIUNIT(IfcRules.CONST.TIMEUNIT, IfcRules.CONST.SECOND);
        IfcRules.IFCUNITASSIGNMENT IFCUNITASSIGNMENT = new IfcRules.IFCUNITASSIGNMENT(new IfcRules.IFCSIUNIT[]{IFCSIUNIT1, IFCSIUNIT2, IFCSIUNIT3});

        this.IFCPROJECT = new IfcRules.IFCPROJECT(IFCOWNERHISTORY, IFCGEOMETRICREPRESENTATIONCONTEXT, IFCUNITASSIGNMENT);
    }

    public void BUILDING() {
        IfcRules.IFCCARTESIANPOINT IFCCARTESIANPOINT = new IfcRules.IFCCARTESIANPOINT(0,0,0);
        IfcRules.IFCDIRECTION IFCDIRECTION1 = new IfcRules.IFCDIRECTION(0,0,1);
        IfcRules.IFCDIRECTION IFCDIRECTION2 = new IfcRules.IFCDIRECTION(1,0,0);
        IfcRules.IFCAXIS2PLACEMENT3D IFCAXIS2PLACEMENT3D = new IfcRules.IFCAXIS2PLACEMENT3D(IFCCARTESIANPOINT, IFCDIRECTION1, IFCDIRECTION2);
        IfcRules.IFCLOCALPLACEMENT IFCLOCALPLACEMENT = new IfcRules.IFCLOCALPLACEMENT(IFCAXIS2PLACEMENT3D);

        IfcRules.IFCSITE IFCSITE = new IfcRules.IFCSITE(IFCOWNERHISTORY, IFCLOCALPLACEMENT);
        IfcRules.IFCRELAGGREGATES IFCRELAGGREGATES = new IfcRules.IFCRELAGGREGATES(IFCOWNERHISTORY, "ProjectContainer", "ProjectContainer for Sites", IFCPROJECT, new IfcRules[]{IFCSITE});

        IFCCARTESIANPOINT = new IfcRules.IFCCARTESIANPOINT(0,0,0);
        IFCDIRECTION1 = new IfcRules.IFCDIRECTION(0,0,1);
        IFCDIRECTION2 = new IfcRules.IFCDIRECTION(1,0,0);
        IFCAXIS2PLACEMENT3D = new IfcRules.IFCAXIS2PLACEMENT3D(IFCCARTESIANPOINT, IFCDIRECTION1, IFCDIRECTION2);
        IFCLOCALPLACEMENT = new IfcRules.IFCLOCALPLACEMENT(IFCAXIS2PLACEMENT3D);

        IfcRules.IFCBUILDING IFCBUILDING = new IfcRules.IFCBUILDING(IFCOWNERHISTORY, IFCLOCALPLACEMENT);
        IFCRELAGGREGATES = new IfcRules.IFCRELAGGREGATES(IFCOWNERHISTORY, "SiteContainer", "SiteContainer for Building", IFCSITE, new IfcRules[]{IFCBUILDING});

        STOREYS_IFCRELAGGREGATES = new IfcRules.IFCRELAGGREGATES(IFCOWNERHISTORY, "BuildingContainer", "BuildingContainer for BuildingStorey", IFCBUILDING);
    }

    public void STOREY(String name, float elevation) {
        IfcRules.IFCCARTESIANPOINT IFCCARTESIANPOINT = new IfcRules.IFCCARTESIANPOINT(0,0,elevation);
        IfcRules.IFCDIRECTION IFCDIRECTION1 = new IfcRules.IFCDIRECTION(0,0,0);
        IfcRules.IFCDIRECTION IFCDIRECTION2 = new IfcRules.IFCDIRECTION(0,0,0);
        IfcRules.IFCAXIS2PLACEMENT3D IFCAXIS2PLACEMENT3D = new IfcRules.IFCAXIS2PLACEMENT3D(IFCCARTESIANPOINT, IFCDIRECTION1, IFCDIRECTION2);
        IfcRules.IFCLOCALPLACEMENT IFCLOCALPLACEMENT = new IfcRules.IFCLOCALPLACEMENT(IFCAXIS2PLACEMENT3D);
        IfcRules.IFCBUILDINGSTOREY IFCBUILDINGSTOREY = new IfcRules.IFCBUILDINGSTOREY(IFCOWNERHISTORY, name, IFCLOCALPLACEMENT, elevation);
        STOREYS_IFCRELAGGREGATES.addChild(IFCBUILDINGSTOREY);
        ACTIVE_IFCBUILDINGSTOREY = IFCBUILDINGSTOREY;
    }

}