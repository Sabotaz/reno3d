package fr.limsi.rorqual.core.model;

import java.util.ArrayList;

import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;

/**
 * Created by christophe on 20/07/15.
 */
public class Etage {
    private ArrayList<Mur> murs = new ArrayList<Mur>();
    private int number;
    public ArrayList<Mur> getMurs() {
        return murs;
    }

    public void addMur(Mur mur) {
        this.murs.add(mur);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ModelGraph getModelGraph() {
        return new ModelGraph();
    }

}
