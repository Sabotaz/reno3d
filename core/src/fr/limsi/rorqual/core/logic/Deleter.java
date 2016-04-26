package fr.limsi.rorqual.core.logic;

import java.util.ArrayList;
import java.util.HashMap;

import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.model.Batiment;
import fr.limsi.rorqual.core.model.Etage;
import fr.limsi.rorqual.core.model.Fenetre;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Objet;
import fr.limsi.rorqual.core.model.Ouverture;
import fr.limsi.rorqual.core.model.Porte;
import fr.limsi.rorqual.core.model.Slab;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;

/**
 * Created by christophe on 04/09/15.
 */
// Classe gérant la suppression de models
public class Deleter extends ModelMaker {
    boolean deleting_objet = false;

    ModelContainer deleted_object;
    Etage currentEtage;

    @Override
    public void begin(int screenX, int screenY) {
        if (deleting_objet == true) return;
        currentEtage = ModelHolder.getInstance().getBatiment().getCurrentEtage();
        deleted_object = currentEtage.getModelGraph().getObject(screenX, screenY);
        if (deleted_object == null || !(deleted_object instanceof Objet)) {
            deleting_objet = false;
        } else {
            deleting_objet = true;
        }
    }

    @Override
    public void update(int screenX, int screenY) {
        if (!deleting_objet) return;

        ModelGraph modelGraph = ModelHolder.getInstance().getBatiment().getCurrentEtage().getModelGraph();
        ModelContainer current = modelGraph.getObject(screenX, screenY);
        if (deleted_object != current) {
            deleting_objet = false;
        }

    }

    @Override
    public void end(int screenX, int screenY) {

        if (deleting_objet) {
            if (deleted_object instanceof Objet) {
                deleteObjet((Objet) deleted_object);
            } else if (deleted_object instanceof Mur) {
                deleteMur((Mur)deleted_object);
            } else if (deleted_object instanceof Slab) {
                deleteSlab((Slab) deleted_object);
            } else if (deleted_object instanceof Ouverture) {
                deleteOuverture((Ouverture) deleted_object);
            }
        }
        deleting_objet = false;
        deleted_object = null;
        Logic.getInstance().end();
    }

    @Override
    public void abort() {
        deleting_objet = false;
        deleted_object = null;
    }

    @Override
    public boolean isStarted() {
        return deleting_objet;
    }

    static public void deleteMur(Mur m) {

        for (Ouverture o : new ArrayList<Ouverture>(m.getOuvertures()))
            deleteOuverture(o);

        m.getEtage().removeMur(m);

        m.setA(null);
        m.setB(null);

        if (m.getSlabGauche() != null) {
            m.getSlabGauche().removeMur(m);
            if (m.getSlabGauche().getMurs().isEmpty())
                deleteSlab((m.getSlabGauche()));
            m.setSlabGauche(null);
        }

        if (m.getSlabDroit() != null) {
            m.getSlabDroit().removeMur(m);
            if (m.getSlabDroit().getMurs().isEmpty())
                deleteSlab((m.getSlabDroit()));
            m.setSlabDroit(null);
        }

        HashMap<String,Object> currentItems = new HashMap<String,Object>();
        currentItems.put("userObject", m);
        Event e = new Event(DpeEvent.MUR_REMOVED, currentItems);
        EventManager.getInstance().put(Channel.DPE, e);

    }

    static public void deleteSlab(Slab s) {
        s.getEtage().removeSlab(s);
        s.setEtage(null);
        s.setCoins(null);
        for (Mur m : new ArrayList<Mur>(s.getMurs())) {
            if (s.equals(m.getSlabGauche()))
                m.setSlabGauche(null);
            if (s.equals(m.getSlabDroit()))
                m.setSlabDroit(null);
            if (m.getSlabGauche() == null && m.getSlabDroit() == null)
                deleteMur(m);
        }

        for (Objet o : new ArrayList<Objet>(s.getObjets())) {
            deleteObjet(o);
        }

        HashMap<String,Object> currentItems = new HashMap<String,Object>();
        currentItems.put("userObject", s);
        Event e = new Event(DpeEvent.SLAB_REMOVED, currentItems);
        EventManager.getInstance().put(Channel.DPE, e);
    }

    static public void deleteOuverture(Ouverture o) {
        o.getMur().removeOuverture(o);

        HashMap<String,Object> currentItems = new HashMap<String,Object>();
        currentItems.put("userObject", o);
        Event e = new Event(DpeEvent.OUVERTURE_REMOVED, currentItems);
        EventManager.getInstance().put(Channel.DPE, e);
    }

    static public void deleteBatiment(){
        Batiment bat = ModelHolder.getInstance().getBatiment();
        for (Etage e : bat.getAllEtages()){

            for (Ouverture o : new ArrayList<Ouverture>(e.getOuvertures())){
                deleteOuverture(o);
            }
            for (Mur m : new ArrayList<Mur>(e.getMurs())){
                deleteMur(m);
            }
            for (Slab s : new ArrayList<Slab>(e.getSlabs())){
                deleteSlab(s);
            }
        }
        bat.reset();
    }

    static public void deleteObjet(Objet o) {
        MainApplicationAdapter.LOG("AMENAGEMENT", "REMOVE_OBJET", "" + o.getModelId(), "" + o.getPosition());
        o.getSlab().removeObjet(o);
        o.setSlab(null);
    }
    static public void delete(ModelContainer m) {
        if (m instanceof Objet)
            deleteObjet((Objet) m);
        MainApplicationAdapter.deselect();
    }
}
