package fr.limsi.rorqual.core.model;

import java.util.ArrayList;

/**
 * Created by christophe on 22/07/15.
 */
public class Batiment {

    private ArrayList<Etage> etages = new ArrayList<Etage>();

    public ArrayList<Etage> getEtages() {
        return etages;
    }

    public void addEtage(Etage e) {
        etages.add(e);
    }

}
