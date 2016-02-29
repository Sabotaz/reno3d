package fr.limsi.rorqual.core.utils.analytics;

import fr.limsi.rorqual.core.logic.Calculateur;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Slab;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;

/**
 * Created by christophe on 29/02/16.
 */
public class HitMaker {

    public static void makeHitOnExport() {
        makeHitsOnBatiment(Action.EXPORT);
    }

    public static void makeHitOnSave() {
        makeHitsOnBatiment(Action.SAVE);
    }

    public static void makeHitOnLoad() {
        makeHitsOnBatiment(Action.LOAD);
    }

    public static void makeHitOnNew() {
        ActionResolver ar = MainApplicationAdapter.getActionResolver();
        ar.sendTrackerEvent(Category.BATIMENT, Action.NEW);
    }

    private static void makeHitsOnBatiment(Action action) {
        ActionResolver ar = MainApplicationAdapter.getActionResolver();
        ar.sendTrackerEvent(Category.BATIMENT, action);
        ar.sendTrackerEvent(Category.BATIMENT, action, "Nombre etages", ModelHolder.getInstance().getBatiment().getAllEtages().size());
        ar.sendTrackerEvent(Category.BATIMENT, action, "Nombre murs", ModelHolder.getInstance().getBatiment().getMurs().size());
        ar.sendTrackerEvent(Category.BATIMENT, action, "Nombre ouvertures", ModelHolder.getInstance().getBatiment().getOuvertures().size());
        ar.sendTrackerEvent(Category.BATIMENT, action, "Nombre pieces", ModelHolder.getInstance().getBatiment().getSlabs().size());

        long nb_meubles = 0;
        for (Slab s : ModelHolder.getInstance().getBatiment().getSlabs())
            nb_meubles += s.getObjets().size();
        ar.sendTrackerEvent(Category.BATIMENT, action, "Nombre meubles", nb_meubles);

        Calculateur.getInstance().actualiseCalculs();
        float area = Calculateur.getInstance().getSurfaceHabitable() * 10E2f; // to dm²
        ar.sendTrackerEvent(Category.BATIMENT, action, "Taille moyenne pièces (dm²)", (long)area/ModelHolder.getInstance().getBatiment().getSlabs().size());
        ar.sendTrackerEvent(Category.BATIMENT, action, "Surface habitable (dm²)", (long)area);
        area = Calculateur.getInstance().getSurfaceAuSol() * 10E2f; // to dm²
        ar.sendTrackerEvent(Category.BATIMENT, action, "Surface au sol (dm²)", (long)area);
        area = Calculateur.getInstance().getSurfaceTotaleMurInterieur() * 10E2f; // to dm²
        ar.sendTrackerEvent(Category.BATIMENT, action, "Surface murs intérieurs (dm²)", (long)area);
        area = Calculateur.getInstance().getSurfaceTotaleMurExterieur() * 10E2f; // to dm²
        ar.sendTrackerEvent(Category.BATIMENT, action, "Surface murs extérieur (dm²)", (long)area);
        float volume = Calculateur.getInstance().getVolumeTotal() * 10E3f; // to dm²
        ar.sendTrackerEvent(Category.BATIMENT, action, "Volume total (dm³)", (long)volume);

    }

}
