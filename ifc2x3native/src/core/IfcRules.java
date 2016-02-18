package core;

import java.util.Random;

/**
 * Created by christophe on 18/02/16.
 */
public class IfcRules {

    private static int n_rules = 0;

    enum CONST {
        ADDED,
        LENGTHUNIT, METRE, PLANEANGLEUNIT, RADIAN, TIMEUNIT, SECOND, ELEMENT;
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

    public IfcRules(String content) {
        this.content = content;
        this.n = ++n_rules;
    }

    public IfcRules(String name, Object... params) {
        this("");
        content = name + "(" + makeString(params) + ");";
    }

    private String makeString(Object ... params) {
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
                c += "(" + makeString(o) + ")";
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

    static class IFCCARTESIANPOINT extends IfcRules {
        public IFCCARTESIANPOINT(float x, float y, float z) {
            super("IFCCARTESIANPOINT",
                    new Object[]{x, y, z});
        }
    }

    static class IFCDIRECTION extends IfcRules {
        public IFCDIRECTION(float x, float y, float z) {
            super("IFCDIRECTION",
                    new Object[]{x, y, z});
        }
    }

    static class IFCUNITASSIGNMENT extends IfcRules {
        public IFCUNITASSIGNMENT(IFCSIUNIT[] units) {
            super("IFCUNITASSIGNMENT",
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
        }
    }

    static class IFCRELAGGREGATES extends IfcRules {
        public IFCRELAGGREGATES(IFCOWNERHISTORY IFCOWNERHISTORY, String rel_name, String inv_rel_name, IfcRules parent, IfcRules[] children) {
            super("IFCRELAGGREGATES",
                    GUID.uid(),
                    IFCOWNERHISTORY,
                    rel_name,
                    inv_rel_name,
                    parent,
                    children);
        }
    }
}
