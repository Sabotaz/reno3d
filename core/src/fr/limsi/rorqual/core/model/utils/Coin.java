package fr.limsi.rorqual.core.model.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import fr.limsi.rorqual.core.model.Mur;

/**
 * Created by christophe on 20/07/15.
 */
public class Coin {

    private ArrayList<Mur> murs = new ArrayList<Mur>();

    private Vector2 position = new Vector2();
    private int etage = 0;

    private static HashMap<Integer, ArrayList<Coin>> coins = new HashMap<Integer, ArrayList<Coin>>();

    public static Coin getCoin(int etage, Vector2 p) {
        if (!coins.containsKey(etage)) {
            coins.put(etage, new ArrayList<Coin>());
        }
        for (Coin c : coins.get(etage)) {
            if (c.getPosition().epsilonEquals(p, 0.000_001f))
                return c;
        }
        return new Coin(etage, p);
    }

    private Coin(int etage, Vector2 p) {
        this.etage = etage;
        position.set(p);
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getEtage() {
        return etage;
    }

    public void setPosition(Vector2 v) {
        position.set(v);
    }

    public void addMur(Mur mur) {
        if (murs.size() == 0)
            coins.get(etage).add(this);
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
        mur.setChanged();

        if (murs.size() == 0)
            coins.get(etage).remove(this);
    }

    public boolean isFirst(Mur mur) {
        return murs.size() > 0 && murs.get(0).equals(mur);
    }

}
