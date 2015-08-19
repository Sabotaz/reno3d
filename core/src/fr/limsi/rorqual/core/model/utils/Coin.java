package fr.limsi.rorqual.core.model.utils;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Arrays;

import fr.limsi.rorqual.core.model.Mur;

/**
 * Created by christophe on 20/07/15.
 */
public class Coin {

    private ArrayList<Mur> murs = new ArrayList<Mur>();

    private Vector3 position = new Vector3();

    private static ArrayList<Coin> coins = new ArrayList<Coin>();

    public static Coin getCoin(Vector3 p) {
        for (Coin c : coins) {
            if (c.getPosition().epsilonEquals(p, 0.000_001f))
                return c;
        }
        return new Coin(p);
    }

    private Coin(Vector3 p) {
        position.set(p);
        coins.add(this);
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 v) {
        position.set(v);
    }

    public void addMur(Mur mur) {
        if (!murs.contains(mur))
            murs.add(mur);
    }

    public ArrayList<Mur> getMurs() {
        return murs;
    }

    public void removeMur(Mur mur) {
        murs.remove(mur);
        for (Mur m : murs)
            m.setChanged();
    }

    public boolean isFirst(Mur mur) {
        return murs.size() > 0 && murs.get(0).equals(mur);
    }

}
