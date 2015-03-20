package fr.limsi.rorqual.core.model;

import java.io.File;
import java.util.Collection;

import ifc2x3javatoolbox.ifc2x3tc1.IfcWall;
import ifc2x3javatoolbox.ifcmodel.IfcModel;

/**
 * Created by christophe on 20/03/15.
 */
public class IfcHolder {

    IfcModel ifcModel = null;

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

    public void openModel(File stepFile) throws Exception {


        //create a new instance of IfcModel
        ifcModel = new IfcModel();
        //load an IFC STEP file from the file system
        ifcModel.readStepFile(stepFile);

        Collection<IfcWall> walls = ifcModel.getCollection(IfcWall.class);
        for (IfcWall wall: walls) {
            System.out.println(wall.getGlobalId() + ": " + wall.getDescription());
        }
    }

}
