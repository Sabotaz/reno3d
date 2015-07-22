package fr.limsi.rorqual.core.model;

import java.io.File;

import fr.limsi.rorqual.core.utils.DefaultMutableTreeNode;
import ifc2x3javatoolbox.helpers.IfcSpatialStructure;
import ifc2x3javatoolbox.ifcmodel.IfcModel;

/**
 * Created by christophe on 20/03/15.
 */
public class ModelHolder {

    private Batiment batiment = null;

    private ModelHolder() {}

    /** Holder */
    private static class ModelHolderHolder
    {
        /** Instance unique non préinitialisée */
        private final static ModelHolder INSTANCE = new ModelHolder();
    }

    public static synchronized ModelHolder getInstance() {
        return ModelHolderHolder.INSTANCE;
    }

    public Batiment getBatiment() {
        return this.batiment;
    }

    public void setBatiment(Batiment model) {
        this.batiment = model;
    }
}
