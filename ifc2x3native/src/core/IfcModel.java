package core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by christophe on 17/02/16.
 */
public class IfcModel {

    IfcRules.IFCOWNERHISTORY IFCOWNERHISTORY;
    IfcRules.IFCPROJECT IFCPROJECT;
    String filename = "projet.ifc";
    IfcRules.IFCBUILDINGSTOREY ACTIVE_IFCBUILDINGSTOREY;
    IfcRules.IFCRELAGGREGATES STOREYS_IFCRELAGGREGATES;
    IfcRules.IFCGEOMETRICREPRESENTATIONCONTEXT IFCGEOMETRICREPRESENTATIONCONTEXT;
    IfcRules.IFCRELCONTAINEDINSPATIALSTRUCTURE ACTIVE_IFCRELCONTAINEDINSPATIALSTRUCTURE;

    public IfcModel(String versionName, int versionCode) {
        IfcRules.restRules();
        PROJECT(versionName, versionCode);
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String date = sdf.format(new Date());
        return
            "HEADER;\n" +
            "FILE_DESCRIPTION(('ViewDefinition [CoordinationView]'),'2;1');\n" +
            "FILE_NAME('" + filename + "','" + date + "',('Thomas Ricordeau','Julien Christophe'),('LIMSI-CNRS'),'IFC2x3native','PLAN 3D ENERGY home edition','');\n" +
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

    public void PROJECT(String versionName, int versionCode) {
        IfcRules.IFCPERSON IFCPERSON1 = new IfcRules.IFCPERSON("Ricordeau", "Thomas");
        IfcRules.IFCPERSON IFCPERSON2 = new IfcRules.IFCPERSON("Christophe", "Julien");
        IfcRules.IFCORGANIZATION IFCORGANIZATION = new IfcRules.IFCORGANIZATION("LIMSI-CNRS");

        IfcRules.IFCPERSONANDORGANIZATION IFCPERSONANDORGANIZATION1 = new IfcRules.IFCPERSONANDORGANIZATION(IFCPERSON1, IFCORGANIZATION);
        IfcRules.IFCPERSONANDORGANIZATION IFCPERSONANDORGANIZATION2 = new IfcRules.IFCPERSONANDORGANIZATION(IFCPERSON2, IFCORGANIZATION);

        IfcRules.IFCAPPLICATION IFCAPPLICATION = new IfcRules.IFCAPPLICATION(IFCORGANIZATION, versionName, "PLAN 3D ENERGY home edition");

        this.IFCOWNERHISTORY = new IfcRules.IFCOWNERHISTORY(IFCPERSONANDORGANIZATION1, IFCAPPLICATION);

        IfcRules.IFCCARTESIANPOINT IFCCARTESIANPOINT = new IfcRules.IFCCARTESIANPOINT(0,0,0);
        IfcRules.IFCAXIS2PLACEMENT3D IFCAXIS2PLACEMENT3D = new IfcRules.IFCAXIS2PLACEMENT3D(IFCCARTESIANPOINT, null, null);
        IFCGEOMETRICREPRESENTATIONCONTEXT = new IfcRules.IFCGEOMETRICREPRESENTATIONCONTEXT(IFCAXIS2PLACEMENT3D);

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
        IfcRules.IFCDIRECTION IFCDIRECTION1 = new IfcRules.IFCDIRECTION(0,0,1);
        IfcRules.IFCDIRECTION IFCDIRECTION2 = new IfcRules.IFCDIRECTION(1,0,0);
        IfcRules.IFCAXIS2PLACEMENT3D IFCAXIS2PLACEMENT3D = new IfcRules.IFCAXIS2PLACEMENT3D(IFCCARTESIANPOINT, IFCDIRECTION1, IFCDIRECTION2);
        IfcRules.IFCLOCALPLACEMENT IFCLOCALPLACEMENT = new IfcRules.IFCLOCALPLACEMENT(IFCAXIS2PLACEMENT3D);
        IfcRules.IFCBUILDINGSTOREY IFCBUILDINGSTOREY = new IfcRules.IFCBUILDINGSTOREY(IFCOWNERHISTORY, name, IFCLOCALPLACEMENT, elevation);

        STOREYS_IFCRELAGGREGATES.addChild(IFCBUILDINGSTOREY);
        ACTIVE_IFCBUILDINGSTOREY = IFCBUILDINGSTOREY;

        ACTIVE_IFCRELCONTAINEDINSPATIALSTRUCTURE = new IfcRules.IFCRELCONTAINEDINSPATIALSTRUCTURE(IFCOWNERHISTORY, "BuildingStoreyContainer", "BuildingStoreyContainer for Wall", ACTIVE_IFCBUILDINGSTOREY);
    }

    public void SLAB(String name, float[][] coins, float height) {

        IfcRules.IFCCARTESIANPOINT IFCCARTESIANPOINT = new IfcRules.IFCCARTESIANPOINT(0,0,0);
        IfcRules.IFCDIRECTION IFCDIRECTION1 = new IfcRules.IFCDIRECTION(0,0,1);
        IfcRules.IFCDIRECTION IFCDIRECTION2 = new IfcRules.IFCDIRECTION(1,0,0);
        IfcRules.IFCAXIS2PLACEMENT3D IFCAXIS2PLACEMENT3D = new IfcRules.IFCAXIS2PLACEMENT3D(IFCCARTESIANPOINT, IFCDIRECTION1, IFCDIRECTION2);
        IfcRules.IFCLOCALPLACEMENT IFCLOCALPLACEMENT = new IfcRules.IFCLOCALPLACEMENT(ACTIVE_IFCBUILDINGSTOREY.IFCLOCALPLACEMENT, IFCAXIS2PLACEMENT3D);

        IfcRules.IFCDIRECTION IFCDIRECTION = new IfcRules.IFCDIRECTION(0,0,1);

        ArrayList<IfcRules.IFCCARTESIANPOINT> IFCCARTESIANPOINTS = new ArrayList<IfcRules.IFCCARTESIANPOINT>();

        for (int i = 0; i < coins.length; i++)
            IFCCARTESIANPOINTS.add(new IfcRules.IFCCARTESIANPOINT(coins[i][0], coins[i][1]));
        IFCCARTESIANPOINTS.add(IFCCARTESIANPOINTS.get(0)); // boucle

        IfcRules.IFCPOLYLINE IFCPOLYLINE = new IfcRules.IFCPOLYLINE(IFCCARTESIANPOINTS.toArray(new IfcRules.IFCCARTESIANPOINT[IFCCARTESIANPOINTS.size()]));

        IfcRules.IFCARBITRARYCLOSEDPROFILEDEF IFCARBITRARYCLOSEDPROFILEDEF = new IfcRules.IFCARBITRARYCLOSEDPROFILEDEF(IFCPOLYLINE);

        IfcRules.IFCEXTRUDEDAREASOLID IFCEXTRUDEDAREASOLID = new IfcRules.IFCEXTRUDEDAREASOLID(IFCARBITRARYCLOSEDPROFILEDEF, IFCAXIS2PLACEMENT3D, IFCDIRECTION, height);

        IfcRules.IFCSHAPEREPRESENTATION IFCSHAPEREPRESENTATION = new IfcRules.IFCSHAPEREPRESENTATION_SWEPTSOLID(IFCGEOMETRICREPRESENTATIONCONTEXT, IFCEXTRUDEDAREASOLID);
        IfcRules.IFCPRODUCTDEFINITIONSHAPE IFCPRODUCTDEFINITIONSHAPE = new IfcRules.IFCPRODUCTDEFINITIONSHAPE(new IfcRules.IFCSHAPEREPRESENTATION[]{IFCSHAPEREPRESENTATION});

        IfcRules.IFCSLAB IFCSLAB = new IfcRules.IFCSLAB(IFCOWNERHISTORY, name, IFCLOCALPLACEMENT, IFCPRODUCTDEFINITIONSHAPE);

        ACTIVE_IFCRELCONTAINEDINSPATIALSTRUCTURE.addChild(IFCSLAB);
    }

    public Object WALL(String etage_name, int etage_number, float mur_ax, float mur_ay, float mur_bx, float mur_by, float mur_depth, float mur_height) {

        IfcRules.IFCCARTESIANPOINT IFCCARTESIANPOINT = new IfcRules.IFCCARTESIANPOINT(mur_ax, mur_ay,0);
        IfcRules.IFCDIRECTION IFCDIRECTION1 = new IfcRules.IFCDIRECTION(0,0,1);

        float wallLength = (float)Math.sqrt((mur_bx-mur_ax)*(mur_bx-mur_ax)+(mur_by-mur_ay)*(mur_by-mur_ay));
        float dirX = (mur_bx-mur_ax)/wallLength;
        float dirY = (mur_by-mur_ay)/wallLength;

        IfcRules.IFCDIRECTION IFCDIRECTION2 = new IfcRules.IFCDIRECTION(dirX,dirY,0);
        IfcRules.IFCAXIS2PLACEMENT3D IFCAXIS2PLACEMENT3D = new IfcRules.IFCAXIS2PLACEMENT3D(IFCCARTESIANPOINT, IFCDIRECTION1, IFCDIRECTION2);
        IfcRules.IFCLOCALPLACEMENT IFCLOCALPLACEMENT = new IfcRules.IFCLOCALPLACEMENT(ACTIVE_IFCBUILDINGSTOREY.IFCLOCALPLACEMENT, IFCAXIS2PLACEMENT3D);

        // first representation : Curve2D representation (2D)
        IFCCARTESIANPOINT = new IfcRules.IFCCARTESIANPOINT(0, 0);
        IfcRules.IFCDIRECTION IFCDIRECTION = new IfcRules.IFCDIRECTION(1, 0);
        IfcRules.IFCVECTOR IFCVECTOR = new IfcRules.IFCVECTOR(IFCDIRECTION, wallLength);
        IfcRules.IFCLINE IFCLINE = new IfcRules.IFCLINE(IFCCARTESIANPOINT, IFCVECTOR);

        IfcRules.IFCCARTESIANPOINT IFCCARTESIANPOINT1 = new IfcRules.IFCCARTESIANPOINT(0, 0);
        IfcRules.IFCCARTESIANPOINT IFCCARTESIANPOINT2 = new IfcRules.IFCCARTESIANPOINT(wallLength, 0);

        IfcRules.IFCTRIMMEDCURVE IFCTRIMMEDCURVE = new IfcRules.IFCTRIMMEDCURVE(IFCLINE, new IfcRules.IFCCARTESIANPOINT[]{IFCCARTESIANPOINT1}, new IfcRules.IFCCARTESIANPOINT[]{IFCCARTESIANPOINT2});

        IfcRules.IFCSHAPEREPRESENTATION_CURVE2D IFCSHAPEREPRESENTATION_CURVE2D = new IfcRules.IFCSHAPEREPRESENTATION_CURVE2D(IFCGEOMETRICREPRESENTATIONCONTEXT, IFCTRIMMEDCURVE);

        // Second representation : SweptSolid representation (3D)
        IFCCARTESIANPOINT = new IfcRules.IFCCARTESIANPOINT(0, 0, 0);
        IFCAXIS2PLACEMENT3D = new IfcRules.IFCAXIS2PLACEMENT3D(IFCCARTESIANPOINT, null, null);

        IFCDIRECTION = new IfcRules.IFCDIRECTION(0,0,1);

        ArrayList<IfcRules.IFCCARTESIANPOINT> IFCCARTESIANPOINTS = new ArrayList<IfcRules.IFCCARTESIANPOINT>();
        IFCCARTESIANPOINTS.add(new IfcRules.IFCCARTESIANPOINT(0, mur_depth/2));
        IFCCARTESIANPOINTS.add(new IfcRules.IFCCARTESIANPOINT(wallLength, mur_depth/2));
        IFCCARTESIANPOINTS.add(new IfcRules.IFCCARTESIANPOINT(wallLength, -mur_depth/2));
        IFCCARTESIANPOINTS.add(new IfcRules.IFCCARTESIANPOINT(0, -mur_depth/2));
        IFCCARTESIANPOINTS.add(IFCCARTESIANPOINTS.get(0));

        IfcRules.IFCPOLYLINE IFCPOLYLINE = new IfcRules.IFCPOLYLINE(IFCCARTESIANPOINTS.toArray(new IfcRules.IFCCARTESIANPOINT[IFCCARTESIANPOINTS.size()]));

        IfcRules.IFCARBITRARYCLOSEDPROFILEDEF IFCARBITRARYCLOSEDPROFILEDEF = new IfcRules.IFCARBITRARYCLOSEDPROFILEDEF(IFCPOLYLINE);

        IfcRules.IFCEXTRUDEDAREASOLID IFCEXTRUDEDAREASOLID = new IfcRules.IFCEXTRUDEDAREASOLID(IFCARBITRARYCLOSEDPROFILEDEF, IFCAXIS2PLACEMENT3D, IFCDIRECTION, mur_height);

        IfcRules.IFCSHAPEREPRESENTATION_SWEPTSOLID IFCSHAPEREPRESENTATION_SWEPTSOLID = new IfcRules.IFCSHAPEREPRESENTATION_SWEPTSOLID(IFCGEOMETRICREPRESENTATIONCONTEXT, IFCEXTRUDEDAREASOLID);

        // create wall

        IfcRules.IFCPRODUCTDEFINITIONSHAPE IFCPRODUCTDEFINITIONSHAPE = new IfcRules.IFCPRODUCTDEFINITIONSHAPE(new IfcRules.IFCSHAPEREPRESENTATION[]{IFCSHAPEREPRESENTATION_CURVE2D, IFCSHAPEREPRESENTATION_SWEPTSOLID});

        IfcRules.IFCWALLSTANDARDCASE IFCWALLSTANDARDCASE = new IfcRules.IFCWALLSTANDARDCASE(IFCOWNERHISTORY, etage_name, IFCLOCALPLACEMENT, IFCPRODUCTDEFINITIONSHAPE);

        IFCWALLSTANDARDCASE.DEPTH = mur_depth;
        ACTIVE_IFCRELCONTAINEDINSPATIALSTRUCTURE.addChild(IFCWALLSTANDARDCASE);

        return IFCWALLSTANDARDCASE;
    }

    public IfcRules.IFCOPENINGELEMENT OPENING(float width, float height, float x, float y, IfcRules.IFCWALLSTANDARDCASE IFCWALLSTANDARDCASE, String type) {
        IfcRules.IFCCARTESIANPOINT IFCCARTESIANPOINT = new IfcRules.IFCCARTESIANPOINT(width/2, 0, height/2);
        IfcRules.IFCDIRECTION IFCDIRECTION1 = new IfcRules.IFCDIRECTION(0,1,0);
        IfcRules.IFCDIRECTION IFCDIRECTION2 = new IfcRules.IFCDIRECTION(0,0,1);
        IfcRules.IFCAXIS2PLACEMENT3D IFCAXIS2PLACEMENT3D = new IfcRules.IFCAXIS2PLACEMENT3D(IFCCARTESIANPOINT, IFCDIRECTION1, IFCDIRECTION2);

        IFCCARTESIANPOINT = new IfcRules.IFCCARTESIANPOINT(0, 0);
        IfcRules.IFCDIRECTION IFCDIRECTION = new IfcRules.IFCDIRECTION(1,0);
        IfcRules.IFCAXIS2PLACEMENT2D IFCAXIS2PLACEMENT2D = new IfcRules.IFCAXIS2PLACEMENT2D(IFCCARTESIANPOINT, IFCDIRECTION);

        IFCDIRECTION = new IfcRules.IFCDIRECTION(0,0,1);

        IfcRules.IFCRECTANGLEPROFILEDEF IFCRECTANGLEPROFILEDEF = new IfcRules.IFCRECTANGLEPROFILEDEF(IFCAXIS2PLACEMENT2D, height, width);

        IfcRules.IFCEXTRUDEDAREASOLID IFCEXTRUDEDAREASOLID = new IfcRules.IFCEXTRUDEDAREASOLID(IFCRECTANGLEPROFILEDEF, IFCAXIS2PLACEMENT3D, IFCDIRECTION, IFCWALLSTANDARDCASE.DEPTH);

        IfcRules.IFCSHAPEREPRESENTATION_SWEPTSOLID IFCSHAPEREPRESENTATION_SWEPTSOLID = new IfcRules.IFCSHAPEREPRESENTATION_SWEPTSOLID(IFCGEOMETRICREPRESENTATIONCONTEXT, IFCEXTRUDEDAREASOLID);

        IfcRules.IFCPRODUCTDEFINITIONSHAPE IFCPRODUCTDEFINITIONSHAPE = new IfcRules.IFCPRODUCTDEFINITIONSHAPE(new IfcRules.IFCSHAPEREPRESENTATION[]{IFCSHAPEREPRESENTATION_SWEPTSOLID});

        IFCCARTESIANPOINT = new IfcRules.IFCCARTESIANPOINT(x, -IFCWALLSTANDARDCASE.DEPTH / 2, y);
        IFCDIRECTION1 = new IfcRules.IFCDIRECTION(0,0,1);
        IFCDIRECTION2 = new IfcRules.IFCDIRECTION(1,0,0);
        IFCAXIS2PLACEMENT3D = new IfcRules.IFCAXIS2PLACEMENT3D(IFCCARTESIANPOINT, IFCDIRECTION1, IFCDIRECTION2);
        IfcRules.IFCLOCALPLACEMENT IFCLOCALPLACEMENT = new IfcRules.IFCLOCALPLACEMENT(IFCWALLSTANDARDCASE.IFCLOCALPLACEMENT, IFCAXIS2PLACEMENT3D);

        IfcRules.IFCOPENINGELEMENT IFCOPENINGELEMENT = new IfcRules.IFCOPENINGELEMENT(IFCOWNERHISTORY, type, IFCLOCALPLACEMENT, IFCPRODUCTDEFINITIONSHAPE);

        IfcRules.IFCRELVOIDSELEMENT IFCRELVOIDSELEMENT = new IfcRules.IFCRELVOIDSELEMENT(IFCOWNERHISTORY, IFCWALLSTANDARDCASE, IFCOPENINGELEMENT);

        return IFCOPENINGELEMENT;
    }

    public void DOOR(float porte_width, float porte_height, float porte_x, float porte_y, Object wall) {
        IfcRules.IFCWALLSTANDARDCASE IFCWALLSTANDARDCASE = (IfcRules.IFCWALLSTANDARDCASE) wall;

        IfcRules.IFCOPENINGELEMENT IFCOPENINGELEMENT = OPENING(porte_width, porte_height, porte_x, porte_y, IFCWALLSTANDARDCASE, "Door");

        IfcRules.IFCCARTESIANPOINT IFCCARTESIANPOINT = new IfcRules.IFCCARTESIANPOINT(0, 0, 0);
        IfcRules.IFCDIRECTION IFCDIRECTION1 = new IfcRules.IFCDIRECTION(0,0,1);
        IfcRules.IFCDIRECTION IFCDIRECTION2 = new IfcRules.IFCDIRECTION(1,0,0);
        IfcRules.IFCAXIS2PLACEMENT3D IFCAXIS2PLACEMENT3D = new IfcRules.IFCAXIS2PLACEMENT3D(IFCCARTESIANPOINT, IFCDIRECTION1, IFCDIRECTION2);
        IfcRules.IFCLOCALPLACEMENT IFCLOCALPLACEMENT = new IfcRules.IFCLOCALPLACEMENT(IFCOPENINGELEMENT.IFCLOCALPLACEMENT, IFCAXIS2PLACEMENT3D);

        IfcRules.IFCDOOR IFCDOOR = new IfcRules.IFCDOOR(IFCOWNERHISTORY, "Door", IFCLOCALPLACEMENT, porte_height, porte_width);

        IfcRules.IFCRELFILLSELEMENT IFCRELFILLSELEMENT = new IfcRules.IFCRELFILLSELEMENT(IFCOWNERHISTORY, "Door", IFCOPENINGELEMENT, IFCDOOR);

        ACTIVE_IFCRELCONTAINEDINSPATIALSTRUCTURE.addChild(IFCDOOR);

    }

    public void WINDOW(float porte_width, float porte_height, float porte_x, float porte_y, Object wall) {
        IfcRules.IFCWALLSTANDARDCASE IFCWALLSTANDARDCASE = (IfcRules.IFCWALLSTANDARDCASE) wall;

        IfcRules.IFCOPENINGELEMENT IFCOPENINGELEMENT = OPENING(porte_width, porte_height, porte_x, porte_y, IFCWALLSTANDARDCASE, "Window");

        IfcRules.IFCCARTESIANPOINT IFCCARTESIANPOINT = new IfcRules.IFCCARTESIANPOINT(0, 0, 0);
        IfcRules.IFCDIRECTION IFCDIRECTION1 = new IfcRules.IFCDIRECTION(0,0,1);
        IfcRules.IFCDIRECTION IFCDIRECTION2 = new IfcRules.IFCDIRECTION(1,0,0);
        IfcRules.IFCAXIS2PLACEMENT3D IFCAXIS2PLACEMENT3D = new IfcRules.IFCAXIS2PLACEMENT3D(IFCCARTESIANPOINT, IFCDIRECTION1, IFCDIRECTION2);
        IfcRules.IFCLOCALPLACEMENT IFCLOCALPLACEMENT = new IfcRules.IFCLOCALPLACEMENT(IFCOPENINGELEMENT.IFCLOCALPLACEMENT, IFCAXIS2PLACEMENT3D);

        IfcRules.IFCWINDOW IFCWINDOW = new IfcRules.IFCWINDOW(IFCOWNERHISTORY, "Window", IFCLOCALPLACEMENT, porte_height, porte_width);

        IfcRules.IFCRELFILLSELEMENT IFCRELFILLSELEMENT = new IfcRules.IFCRELFILLSELEMENT(IFCOWNERHISTORY, "Window", IFCOPENINGELEMENT, IFCWINDOW);

        ACTIVE_IFCRELCONTAINEDINSPATIALSTRUCTURE.addChild(IFCWINDOW);
    }

    public void WINDOWDOOR(float porte_width, float porte_height, float porte_x, float porte_y, Object wall) {
        IfcRules.IFCWALLSTANDARDCASE IFCWALLSTANDARDCASE = (IfcRules.IFCWALLSTANDARDCASE) wall;

        IfcRules.IFCOPENINGELEMENT IFCOPENINGELEMENT = OPENING(porte_width, porte_height, porte_x, porte_y, IFCWALLSTANDARDCASE, "FrenchDoor");

        IfcRules.IFCCARTESIANPOINT IFCCARTESIANPOINT = new IfcRules.IFCCARTESIANPOINT(0, 0, 0);
        IfcRules.IFCDIRECTION IFCDIRECTION1 = new IfcRules.IFCDIRECTION(0,0,1);
        IfcRules.IFCDIRECTION IFCDIRECTION2 = new IfcRules.IFCDIRECTION(1,0,0);
        IfcRules.IFCAXIS2PLACEMENT3D IFCAXIS2PLACEMENT3D = new IfcRules.IFCAXIS2PLACEMENT3D(IFCCARTESIANPOINT, IFCDIRECTION1, IFCDIRECTION2);
        IfcRules.IFCLOCALPLACEMENT IFCLOCALPLACEMENT = new IfcRules.IFCLOCALPLACEMENT(IFCOPENINGELEMENT.IFCLOCALPLACEMENT, IFCAXIS2PLACEMENT3D);

        IfcRules.IFCWINDOW IFCWINDOW = new IfcRules.IFCWINDOW(IFCOWNERHISTORY, "FrenchDoor", IFCLOCALPLACEMENT, porte_height, porte_width);

        IfcRules.IFCRELFILLSELEMENT IFCRELFILLSELEMENT = new IfcRules.IFCRELFILLSELEMENT(IFCOWNERHISTORY, "FrenchDoor", IFCOPENINGELEMENT, IFCWINDOW);

        ACTIVE_IFCRELCONTAINEDINSPATIALSTRUCTURE.addChild(IFCWINDOW);
    }
}