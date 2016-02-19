package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by christophe on 18/02/16.
 */
public class IfcRules {

    private static int n_rules = 0;
    private static ArrayList<IfcRules> rules = new ArrayList<IfcRules>();

    public static void restRules() {
        n_rules = 0;
        rules.clear();
    }

    public static ArrayList<IfcRules> getRules() {
        return rules;
    }

    enum CONST {
        ADDED,
        LENGTHUNIT, METRE, PLANEANGLEUNIT, RADIAN, TIMEUNIT, SECOND, ELEMENT, FLOOR, AREA, T, CARTESIAN;
    }

    enum SIGN {
        STAR("*"),
        ;

        String value;
        SIGN(String s) {
            value = s;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    /***********************************************/

    String content;
    int n;

    public IfcRules() {
        this.n = ++n_rules;
        rules.add(this);
    }

    public IfcRules(String content) {
        this();
        this.content = content;
    }

    public IfcRules(String name, Object... params) {
        this(name + "(" + makeString(params) + ");");
    }

    private static String makeString(Object ... params) {
        String c = "";
        for (Object o : params) {
            if (o == null)
                c += "$";
            else if (o instanceof IfcRules)
                c += ((IfcRules) o).ref();
            else if (o instanceof CONST)
                c += "." + o + ".";
            else if (o instanceof SIGN)
                c += o;
            else if (o instanceof String)
                c += "'" + o + "'";
            else if (o instanceof Object[])
                if (((Object[]) o).length == 0)
                    c += "$";
                else c += "(" + makeString((Object[])o) + ")";
            else
                c += o;

            c += ",";
        }
        return c.substring(0, c.length()-1);
    }

    @Override
    public String toString() {
        return ref() + "= " + content + "\n";
    }

    public String ref() {
        return "#" + n;
    }

    /***********************************************/




    static class IFCPROJECT extends IfcRules {
        public IFCPROJECT(IFCOWNERHISTORY IFCOWNERHISTORY, IFCGEOMETRICREPRESENTATIONCONTEXT IFCGEOMETRICREPRESENTATIONCONTEXT, IFCUNITASSIGNMENT IFCUNITASSIGNMENT) {
            super("IFCPROJECT",
                    GUID.uid(),
                    IFCOWNERHISTORY,
                    "Projet 3D-RENO",
                    "Modelisation interieur + calcul DPE",
                    null,
                    null,
                    null,
                    new Object[]{IFCGEOMETRICREPRESENTATIONCONTEXT},
                    IFCUNITASSIGNMENT);
        }
    }

    static class IFCOWNERHISTORY extends IfcRules {
        public IFCOWNERHISTORY(IFCPERSONANDORGANIZATION IFCPERSONANDORGANIZATION, IFCAPPLICATION IFCAPPLICATION) {
            super("IFCOWNERHISTORY",
                    IFCPERSONANDORGANIZATION,
                    IFCAPPLICATION,
                    null,
                    CONST.ADDED,
                    null,
                    null,
                    null,
                    2147483647);
        }
    }

    static class IFCPERSONANDORGANIZATION extends IfcRules {
        public IFCPERSONANDORGANIZATION(IFCPERSON IFCPERSON, IFCORGANIZATION IFCORGANIZATION) {
            super("IFCPERSONANDORGANIZATION",
                    IFCPERSON,
                    IFCORGANIZATION,
                    null);
        }
    }

    static class IFCPERSON extends IfcRules {
        public IFCPERSON(String nom, String prenom) {
            super("IFCPERSON",
                    null,
                    nom,
                    prenom,
                    null,
                    null,
                    null,
                    null,
                    null);
        }
    }

    static class IFCORGANIZATION extends IfcRules {
        public IFCORGANIZATION(String nom) {
            super("IFCORGANIZATION",
                    null,
                    nom,
                    null,
                    null,
                    null);
        }
    }

    static class IFCAPPLICATION extends IfcRules {
        public IFCAPPLICATION(IFCORGANIZATION IFCORGANIZATION, String version, String nom) {
            super("IFCAPPLICATION",
                    IFCORGANIZATION,
                    version,
                    nom,
                    null);
        }
    }

    static class IFCGEOMETRICREPRESENTATIONCONTEXT extends IfcRules {
        public IFCGEOMETRICREPRESENTATIONCONTEXT(IFCAXIS2PLACEMENT3D IFCAXIS2PLACEMENT3D) {
            super("IFCGEOMETRICREPRESENTATIONCONTEXT",
                    null,
                    "Model",
                    3,
                    1.0E-5,
                    IFCAXIS2PLACEMENT3D,
                    null);
        }
    }

    static class IFCAXIS2PLACEMENT3D extends IfcRules {
        public IFCAXIS2PLACEMENT3D(IFCCARTESIANPOINT IFCCARTESIANPOINT, IFCDIRECTION IFCDIRECTION1, IFCDIRECTION IFCDIRECTION2) {
            super("IFCAXIS2PLACEMENT3D",
                    IFCCARTESIANPOINT,
                    IFCDIRECTION1,
                    IFCDIRECTION2);
        }
    }

    static class IFCAXIS2PLACEMENT2D extends IfcRules {
        public IFCAXIS2PLACEMENT2D(IFCCARTESIANPOINT IFCCARTESIANPOINT, IFCDIRECTION IFCDIRECTION) {
            super("IFCAXIS2PLACEMENT2D",
                    IFCCARTESIANPOINT,
                    IFCDIRECTION);
        }
    }

    static class IFCCARTESIANPOINT extends IfcRules {
        public IFCCARTESIANPOINT(String name, Object... params) {
            super(name + "((" + makeString(params) + "));");
        }

        public IFCCARTESIANPOINT(float x, float y) {
            this("IFCCARTESIANPOINT",
                    new Object[]{x, y});
        }

        public IFCCARTESIANPOINT(float x, float y, float z) {
            this("IFCCARTESIANPOINT",
                    new Object[]{x, y, z});
        }
    }

    static class IFCDIRECTION extends IfcRules {
        public IFCDIRECTION(String name, Object... params) {
            super(name + "((" + makeString(params) + "));");
        }

        public IFCDIRECTION(float x, float y, float z) {
            this("IFCDIRECTION",
                    new Object[]{x, y, z});
        }

        public IFCDIRECTION(float x, float y) {
            this("IFCDIRECTION",
                    new Object[]{x, y});
        }
    }

    static class IFCUNITASSIGNMENT extends IfcRules {
        public IFCUNITASSIGNMENT(String name, Object... params) {
            super(name + "((" + makeString(params) + "));");
        }

        public IFCUNITASSIGNMENT(IFCSIUNIT[] units) {
            this("IFCUNITASSIGNMENT",
                    units);
        }
    }

    static class IFCSIUNIT extends IfcRules {
        public IFCSIUNIT(CONST name, CONST unit) {
            super("IFCSIUNIT",
                    SIGN.STAR,
                    name,
                    null,
                    unit);
        }
    }

    static class IFCSITE extends IfcRules {
        public IFCSITE(IFCOWNERHISTORY IFCOWNERHISTORY, IFCLOCALPLACEMENT IFCLOCALPLACEMENT) {
            super("IFCSITE",
                    GUID.uid(),
                    IFCOWNERHISTORY,
                    "Site de la simple maison",
                    "Description du site",
                    null,
                    IFCLOCALPLACEMENT,
                    null,
                    null,
                    CONST.ELEMENT,
                    null,
                    null,
                    null,
                    null,
                    null);
        }
    }

    static class IFCLOCALPLACEMENT extends IfcRules {
        public IFCLOCALPLACEMENT(IFCAXIS2PLACEMENT3D IFCAXIS2PLACEMENT3D) {
            super("IFCLOCALPLACEMENT",
                    null,
                    IFCAXIS2PLACEMENT3D);
        }
        public IFCLOCALPLACEMENT(IFCLOCALPLACEMENT IFCLOCALPLACEMENT, IFCAXIS2PLACEMENT3D IFCAXIS2PLACEMENT3D) {
            super("IFCLOCALPLACEMENT",
                    IFCLOCALPLACEMENT,
                    IFCAXIS2PLACEMENT3D);
        }
    }

    static class IFCBUILDING extends IfcRules {
        public IFCBUILDING(IFCOWNERHISTORY IFCOWNERHISTORY, IFCLOCALPLACEMENT IFCLOCALPLACEMENT) {
            super("IFCBUILDING",
                    GUID.uid(),
                    IFCOWNERHISTORY,
                    "Simple maison",
                    "Description de la maison",
                    null,
                    IFCLOCALPLACEMENT,
                    null,
                    null,
                    CONST.ELEMENT,
                    null,
                    null,
                    null);
        }
    }

    static class IFCBUILDINGSTOREY extends IfcRules {
        public IFCBUILDINGSTOREY(IFCOWNERHISTORY IFCOWNERHISTORY, String name, IFCLOCALPLACEMENT IFCLOCALPLACEMENT, float elevation) {
            super("IFCBUILDINGSTOREY",
                    GUID.uid(),
                    IFCOWNERHISTORY,
                    name,
                    name + " / elevation = " + elevation + "m",
                    null,
                    IFCLOCALPLACEMENT,
                    null,
                    null,
                    CONST.ELEMENT,
                    elevation);
            this.IFCLOCALPLACEMENT = IFCLOCALPLACEMENT;
        }

        IFCLOCALPLACEMENT IFCLOCALPLACEMENT;
    }

    static class IFCRELAGGREGATES extends IfcRules {
        String guid;
        IFCOWNERHISTORY IFCOWNERHISTORY;
        String rel_name;
        String inv_rel_name;
        IfcRules parent;
        List<IfcRules> children;
        public IFCRELAGGREGATES(IFCOWNERHISTORY IFCOWNERHISTORY, String rel_name, String inv_rel_name, IfcRules parent, IfcRules[] children) {
            super();
            guid = GUID.uid();
            this.IFCOWNERHISTORY = IFCOWNERHISTORY;
            this.rel_name = rel_name;
            this.inv_rel_name = inv_rel_name;
            this.parent = parent;
            this.children = Arrays.asList(children);
            makeContent();
        }

        public IFCRELAGGREGATES(IFCOWNERHISTORY IFCOWNERHISTORY, String rel_name, String inv_rel_name, IfcRules parent) {
            super();
            guid = GUID.uid();
            this.IFCOWNERHISTORY = IFCOWNERHISTORY;
            this.rel_name = rel_name;
            this.inv_rel_name = inv_rel_name;
            this.parent = parent;
            this.children = new ArrayList<IfcRules>();
            makeContent();
        }

        public void addChild(IfcRules child) {
            children.add(child);
            makeContent();
        }

        private void makeContent() {
            content = "IFCRELAGGREGATES" + "(" + makeString(
                    guid,
                    this.IFCOWNERHISTORY,
                    this.rel_name,
                    this.inv_rel_name,
                    this.parent,
                    this.children.toArray()
            ) + ");";
        }
    }

    static class IFCRELCONTAINEDINSPATIALSTRUCTURE extends IfcRules {
        String guid;
        IFCOWNERHISTORY IFCOWNERHISTORY;
        String rel_name;
        String inv_rel_name;
        IfcRules parent;
        List<IfcRules> children;
        public IFCRELCONTAINEDINSPATIALSTRUCTURE(IFCOWNERHISTORY IFCOWNERHISTORY, String rel_name, String inv_rel_name, IfcRules parent, IfcRules[] children) {
            super();
            guid = GUID.uid();
            this.IFCOWNERHISTORY = IFCOWNERHISTORY;
            this.rel_name = rel_name;
            this.inv_rel_name = inv_rel_name;
            this.parent = parent;
            this.children = Arrays.asList(children);
            makeContent();
        }

        public IFCRELCONTAINEDINSPATIALSTRUCTURE(IFCOWNERHISTORY IFCOWNERHISTORY, String rel_name, String inv_rel_name, IfcRules parent) {
            super();
            guid = GUID.uid();
            this.IFCOWNERHISTORY = IFCOWNERHISTORY;
            this.rel_name = rel_name;
            this.inv_rel_name = inv_rel_name;
            this.parent = parent;
            this.children = new ArrayList<IfcRules>();
            makeContent();
        }

        public void addChild(IfcRules child) {
            children.add(child);
            makeContent();
        }

        private void makeContent() {
            content = "IFCRELCONTAINEDINSPATIALSTRUCTURE" + "(" + makeString(
                    guid,
                    this.IFCOWNERHISTORY,
                    this.rel_name,
                    this.inv_rel_name,
                    this.children.toArray(),
                    this.parent
            ) + ");";
        }
    }

    static class IFCSLAB extends IfcRules {
        public IFCSLAB(IFCOWNERHISTORY IFCOWNERHISTORY, String name, IFCLOCALPLACEMENT IFCLOCALPLACEMENT, IFCPRODUCTDEFINITIONSHAPE IFCPRODUCTDEFINITIONSHAPE) {
            super("IFCSLAB",
                    GUID.uid(),
                    IFCOWNERHISTORY,
                    "Slab",
                    "Slab = floor / etage = " + name,
                    null,
                    IFCLOCALPLACEMENT,
                    IFCPRODUCTDEFINITIONSHAPE,
                    null,
                    CONST.FLOOR);
        }
    }

    static class IFCPRODUCTDEFINITIONSHAPE extends IfcRules {
        public IFCPRODUCTDEFINITIONSHAPE(IFCSHAPEREPRESENTATION[] IFCSHAPEREPRESENTATIONS) {
            super("IFCPRODUCTDEFINITIONSHAPE",
                    null,
                    null,
                    IFCSHAPEREPRESENTATIONS);
        }
    }

    static class IFCSHAPEREPRESENTATION extends IfcRules {
        public IFCSHAPEREPRESENTATION(IFCGEOMETRICREPRESENTATIONCONTEXT IFCGEOMETRICREPRESENTATIONCONTEXT, String type1, String type2, IFCSHAPEDEFINITION IFCSHAPEDEFINITION) {
            super("IFCSHAPEREPRESENTATION",
                    IFCGEOMETRICREPRESENTATIONCONTEXT,
                    type1,
                    type2,
                    new Object[]{IFCSHAPEDEFINITION});
        }
    }

    static class IFCSHAPEREPRESENTATION_SWEPTSOLID extends IFCSHAPEREPRESENTATION {
        public IFCSHAPEREPRESENTATION_SWEPTSOLID(IFCGEOMETRICREPRESENTATIONCONTEXT IFCGEOMETRICREPRESENTATIONCONTEXT, IFCEXTRUDEDAREASOLID IFCEXTRUDEDAREASOLID) {
            super(IFCGEOMETRICREPRESENTATIONCONTEXT,
                    "Body",
                    "SweptSolid",
                    IFCEXTRUDEDAREASOLID);
        }
    }

    static class IFCSHAPEREPRESENTATION_CURVE2D extends IFCSHAPEREPRESENTATION {
        public IFCSHAPEREPRESENTATION_CURVE2D(IFCGEOMETRICREPRESENTATIONCONTEXT IFCGEOMETRICREPRESENTATIONCONTEXT, IFCTRIMMEDCURVE IFCTRIMMEDCURVE) {
            super(IFCGEOMETRICREPRESENTATIONCONTEXT,
                    "Axis",
                    "Curve2D",
                    IFCTRIMMEDCURVE);
        }
    }
    static class IFCSHAPEDEFINITION extends IfcRules {
        public IFCSHAPEDEFINITION(String name, Object... params) {
            super(name, params);
        }
    }

    static class IFCEXTRUDEDAREASOLID extends IFCSHAPEDEFINITION {
        public IFCEXTRUDEDAREASOLID(IFCPROFILEDEF IFCPROFILEDEF, IFCAXIS2PLACEMENT3D IFCAXIS2PLACEMENT3D, IFCDIRECTION IFCDIRECTION, float extrusion_size) {
            super("IFCEXTRUDEDAREASOLID",
                    IFCPROFILEDEF,
                    IFCAXIS2PLACEMENT3D,
                    IFCDIRECTION,
                    extrusion_size);
        }
    }

    static class IFCTRIMMEDCURVE extends IFCSHAPEDEFINITION {
        public IFCTRIMMEDCURVE(IFCLINE IFCLINE, IFCCARTESIANPOINT[] IFCCARTESIANPOINTS1, IFCCARTESIANPOINT[] IFCCARTESIANPOINTS2) {
            super("IFCTRIMMEDCURVE",
                    IFCLINE,
                    IFCCARTESIANPOINTS1,
                    IFCCARTESIANPOINTS2,
                    CONST.T,
                    CONST.CARTESIAN);
        }
    }

    static class IFCLINE extends IfcRules {
        public IFCLINE(IFCCARTESIANPOINT IFCCARTESIANPOINT, IFCVECTOR IFCVECTOR) {
            super("IFCLINE",
                    IFCCARTESIANPOINT,
                    IFCVECTOR);
        }
    }

    static class IFCVECTOR extends IfcRules {
        public IFCVECTOR(IFCDIRECTION IFCDIRECTION, float norme) {
            super("IFCVECTOR",
                    IFCDIRECTION,
                    norme);
        }
    }

    static class IFCPROFILEDEF extends IfcRules {
        public IFCPROFILEDEF(String name, Object ... params) {
            super(name, params);
        }
    }

    static class IFCARBITRARYCLOSEDPROFILEDEF extends IFCPROFILEDEF {
        public IFCARBITRARYCLOSEDPROFILEDEF(IFCPOLYLINE IFCPOLYLINE) {
            super("IFCARBITRARYCLOSEDPROFILEDEF",
                    CONST.AREA,
                    null,
                    IFCPOLYLINE);
        }
    }

    static class IFCRECTANGLEPROFILEDEF extends IFCPROFILEDEF {
        public IFCRECTANGLEPROFILEDEF(IFCAXIS2PLACEMENT2D IFCAXIS2PLACEMENT2D, float height, float width) {
            super("IFCRECTANGLEPROFILEDEF",
                    CONST.AREA,
                    null,
                    IFCAXIS2PLACEMENT2D,
                    height, width);
        }
    }

    static class IFCPOLYLINE extends IfcRules {
        public IFCPOLYLINE(String name, Object... params) {
            super(name + "((" + makeString(params) + "));");
        }
        public IFCPOLYLINE(IFCCARTESIANPOINT[] points) {
            this("IFCPOLYLINE", points);
        }
    }

    static class IFCWALLSTANDARDCASE extends IfcRules {
        public IFCWALLSTANDARDCASE(IFCOWNERHISTORY IFCOWNERHISTORY, String name, IFCLOCALPLACEMENT IFCLOCALPLACEMENT, IFCPRODUCTDEFINITIONSHAPE IFCPRODUCTDEFINITIONSHAPE) {
            super("IFCWALLSTANDARDCASE",
                    GUID.uid(),
                    IFCOWNERHISTORY,
                    "Mur",
                    "Mur / etage = " + name,
                    null,
                    IFCLOCALPLACEMENT,
                    IFCPRODUCTDEFINITIONSHAPE,
                    null);
            this.IFCLOCALPLACEMENT = IFCLOCALPLACEMENT;
        }
        IFCLOCALPLACEMENT IFCLOCALPLACEMENT;
        float DEPTH;
    }

    static class IFCRELVOIDSELEMENT extends IfcRules {
        public IFCRELVOIDSELEMENT(IFCOWNERHISTORY IFCOWNERHISTORY, IFCWALLSTANDARDCASE IFCWALLSTANDARDCASE, IFCOPENINGELEMENT IFCOPENINGELEMENT) {
            super("IFCRELVOIDSELEMENT",
                    GUID.uid(),
                    IFCOWNERHISTORY,
                    "Wall Container",
                    "WallContainer for OpeningElement",
                    IFCWALLSTANDARDCASE,
                    IFCOPENINGELEMENT);
        }
    }

    static class IFCOPENINGELEMENT extends IfcRules {
        public IFCOPENINGELEMENT(IFCOWNERHISTORY IFCOWNERHISTORY, String name, IFCLOCALPLACEMENT IFCLOCALPLACEMENT, IFCPRODUCTDEFINITIONSHAPE IFCPRODUCTDEFINITIONSHAPE) {
            super("IFCOPENINGELEMENT",
                    GUID.uid(),
                    IFCOWNERHISTORY,
                    name,
                    name + " / wall = Mur",
                    null,
                    IFCLOCALPLACEMENT,
                    IFCPRODUCTDEFINITIONSHAPE,
                    null);
            this.IFCLOCALPLACEMENT = IFCLOCALPLACEMENT;
        }
        IFCLOCALPLACEMENT IFCLOCALPLACEMENT;
    }

    static class IFCRELFILLSELEMENT extends IfcRules {
        public IFCRELFILLSELEMENT(IFCOWNERHISTORY IFCOWNERHISTORY, String name, IFCOPENINGELEMENT IFCOPENINGELEMENT, IFCOPENING IFCOPENING) {
            super("IFCRELFILLSELEMENT",
                    GUID.uid(),
                    IFCOWNERHISTORY,
                    "Opening Container",
                    "Opening Container for " + name,
                    IFCOPENINGELEMENT,
                    IFCOPENING);
        }
    }

    static class IFCOPENING extends IfcRules {
        public IFCOPENING(String name, Object ... params) {
            super(name, params);
        }
    }

    static class IFCWINDOW extends IFCOPENING {
        public IFCWINDOW(IFCOWNERHISTORY IFCOWNERHISTORY, String name, IFCLOCALPLACEMENT IFCLOCALPLACEMENT, float height, float width) {
            super("IFCWINDOW",
                    GUID.uid(),
                    IFCOWNERHISTORY,
                    name,
                    "",
                    "",
                    IFCLOCALPLACEMENT,
                    null,
                    null,
                    height,
                    width);
        }
    }

    static class IFCDOOR extends IFCOPENING {
        public IFCDOOR(IFCOWNERHISTORY IFCOWNERHISTORY, String name, IFCLOCALPLACEMENT IFCLOCALPLACEMENT, float height, float width) {
            super("IFCDOOR",
                    GUID.uid(),
                    IFCOWNERHISTORY,
                    name,
                    "",
                    null,
                    IFCLOCALPLACEMENT,
                    null,
                    null,
                    height,
                    width);
        }
    }



}
