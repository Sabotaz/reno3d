package fr.limsi.rorqual.core.model;

import java.util.ArrayList;

import fr.limsi.rorqual.core.dpe.enums.wallproperties.OrientationEnum;

/**
 * Created by christophe on 22/07/15.
 */
public class Batiment {

    private ArrayList<Etage> etages = new ArrayList<Etage>();

    Etage current;

    public ArrayList<Etage> getEtages() {
        return etages;
    }

    public void addEtage(Etage e) {
        etages.add(e);
        e.setBatiment(this);
    }

    public void setCurrentEtage(Etage e) {
        current = e;
        if (!etages.contains(e))
            etages.add(e);
    }

    public Etage getCurrentEtage() {
        return current;
    }

    public void setOrientation(OrientationEnum orientation) {
        for (Etage e : etages)
            e.setOrientation(orientation);
    }

}
