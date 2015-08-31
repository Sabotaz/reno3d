package fr.limsi.rorqual.core.model;

import java.io.File;
import java.util.HashMap;

import fr.limsi.rorqual.core.dpe.Dpe;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventManager;
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

    public static void notify(Object object){
        if (object instanceof Mur){
            HashMap<String,Object> currentItems = new HashMap<String,Object>();
            currentItems.put("userObject", (Mur)object);
            Event e = new Event(DpeEvent.MUR_AJOUTE, currentItems);
            EventManager.getInstance().put(Channel.DPE, e);
        }else if (object instanceof Slab){
            HashMap<String,Object> currentItems = new HashMap<String,Object>();
            currentItems.put("userObject", (Slab)object);
            Event e = new Event(DpeEvent.SLAB_AJOUTE, currentItems);
            EventManager.getInstance().put(Channel.DPE, e);
        }else if (object instanceof Porte){
            HashMap<String,Object> currentItems = new HashMap<String,Object>();
            currentItems.put("userObject", (Porte)object);
            Event e = new Event(DpeEvent.PORTE_AJOUTE, currentItems);
            EventManager.getInstance().put(Channel.DPE, e);
        }else if (object instanceof Fenetre){
            HashMap<String,Object> currentItems = new HashMap<String,Object>();
            currentItems.put("userObject", (Fenetre)object);
            Event e = new Event(DpeEvent.FENETRE_AJOUTEE, currentItems);
            EventManager.getInstance().put(Channel.DPE, e);
        }else if (object instanceof PorteFenetre){
            HashMap<String,Object> currentItems = new HashMap<String,Object>();
            currentItems.put("userObject", (PorteFenetre)object);
            Event e = new Event(DpeEvent.PORTE_FENETRE_AJOUTEE, currentItems);
            EventManager.getInstance().put(Channel.DPE, e);
        }
    }
}
