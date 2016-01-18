package fr.limsi.rorqual.core.model;

import java.io.File;
import java.util.HashMap;

import fr.limsi.rorqual.core.dpe.Dpe;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.utils.DefaultMutableTreeNode;

/**
 * Created by christophe on 20/03/15.
 */
// Holder pour le batiment actuel
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

    public void resetModel(){
        this.batiment=null;
    }

    public static void notify(Object object){
        if (object instanceof Mur){
            HashMap<String,Object> currentItems = new HashMap<String,Object>();
            currentItems.put("userObject", object);
            Event e = new Event(DpeEvent.MUR_AJOUTE, currentItems);
            EventManager.getInstance().put(Channel.DPE, e);
        }else if (object instanceof Slab){
            HashMap<String,Object> currentItems = new HashMap<String,Object>();
            currentItems.put("userObject", object);
            Event e = new Event(DpeEvent.SLAB_AJOUTE, currentItems);
            EventManager.getInstance().put(Channel.DPE, e);
        }else if (object instanceof Porte){
            HashMap<String,Object> currentItems = new HashMap<String,Object>();
            currentItems.put("userObject", object);
            Event e = new Event(DpeEvent.PORTE_AJOUTE, currentItems);
            EventManager.getInstance().put(Channel.DPE, e);
        }else if (object instanceof Fenetre){
            HashMap<String,Object> currentItems = new HashMap<String,Object>();
            currentItems.put("userObject", object);
            Event e = new Event(DpeEvent.FENETRE_AJOUTEE, currentItems);
            EventManager.getInstance().put(Channel.DPE, e);
        }else if (object instanceof PorteFenetre){
            HashMap<String,Object> currentItems = new HashMap<String,Object>();
            currentItems.put("userObject", object);
            Event e = new Event(DpeEvent.PORTE_FENETRE_AJOUTEE, currentItems);
            EventManager.getInstance().put(Channel.DPE, e);
        }
    }
}
