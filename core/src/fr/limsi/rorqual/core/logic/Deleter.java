package fr.limsi.rorqual.core.logic;

import fr.limsi.rorqual.core.model.Etage;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Objet;
import fr.limsi.rorqual.core.model.Ouverture;
import fr.limsi.rorqual.core.model.Slab;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;

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
        if (deleted_object == null) {
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

    private void deleteMur(Mur m) {
        currentEtage.removeMur(m);

        m.setA(null);
        m.setB(null);

        if (m.getSlabGauche() != null) {
            System.out.println("m = " + m.getSlabGauche());
            m.getSlabGauche().removeMur(m);
            if (m.getSlabGauche().getMurs().isEmpty())
                deleteSlab((m.getSlabGauche()));
            m.setSlabGauche(null);
        }

        if (m.getSlabDroit() != null) {
            System.out.println("m = " + m.getSlabDroit());
            m.getSlabDroit().removeMur(m);
            if (m.getSlabDroit().getMurs().isEmpty())
                deleteSlab((m.getSlabDroit()));
            m.setSlabDroit(null);
        }


        for (Ouverture o : m.getOuvertures())
            deleteOuverture(o);

    }

    private void deleteSlab(Slab s) {
        currentEtage.removeSlab(s);
        s.setEtage(null);
        s.setCoins(null);
        for (Mur m : s.getMurs()) {
            System.out.println("slab = " + s);
            System.out.println("m.getSlabGauche() = " + m.getSlabGauche());
            System.out.println("m.getSlabDroit() = " + m.getSlabDroit());
            if (s.equals(m.getSlabGauche()))
                m.setSlabGauche(null);
            if (s.equals(m.getSlabDroit()))
                m.setSlabDroit(null);
            if (m.getSlabGauche() == null && m.getSlabDroit() == null)
                deleteMur(m);
        }
        for (Objet o : s.getObjets()) {
            deleteObjet(o);
        }
        s.getObjets().clear();
    }

    private void deleteOuverture(Ouverture o) {
        o.getMur().removeOuverture(o);
    }

    private void deleteObjet(Objet o) {
        currentEtage.getModelGraph().getRoot().remove(o);
    }
}
