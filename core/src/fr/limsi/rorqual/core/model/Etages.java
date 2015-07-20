package fr.limsi.rorqual.core.model;

import java.util.ArrayList;

/**
 * Created by christophe on 20/07/15.
 */
public class Etages {
    private ArrayList<Mur> murs = new ArrayList<Mur>();
    private int number;
    public ArrayList<Mur> getMurs() {
        return murs;
    }

    public void setMurs(ArrayList<Mur> murs) {
        this.murs = murs;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
