package fr.limsi.rorqual.core.model;

import java.io.File;
import java.util.Collection;

import fr.limsi.rorqual.core.utils.DefaultMutableTreeNode;

import ifc2x3javatoolbox.helpers.IfcSpatialStructure;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWall;
import ifc2x3javatoolbox.ifcmodel.IfcModel;

/**
 * Created by christophe on 20/03/15.
 */
public class IfcHolder {

    IfcModel ifcModel = null;
    DefaultMutableTreeNode spatialStructureTreeNode = new DefaultMutableTreeNode("no model loaded");

    private IfcHolder() {}

    /** Holder */
    private static class IfcHolderHolder
    {
        /** Instance unique non préinitialisée */
        private final static IfcHolder INSTANCE = new IfcHolder();
    }

    public static synchronized IfcHolder getInstance() {
        return IfcHolderHolder.INSTANCE;
    }

    public IfcModel getIfcModel() {
        return this.ifcModel;
    }

    public void openModel(File stepFile) throws Exception {
        //create a new instance of IfcModel
        ifcModel = new IfcModel();
        //load an IFC STEP file from the file system
        ifcModel.readStepFile(stepFile);
        spatialStructureTreeNode = new IfcSpatialStructure(ifcModel).getSpatialStructureRoot(false);
    }

    public DefaultMutableTreeNode getSpatialStructureTreeNode() {
        return spatialStructureTreeNode;
    }

}
