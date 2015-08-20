package fr.limsi.rorqual.core.model.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Arrays;

import fr.limsi.rorqual.core.model.Mur;

/**
 * Created by christophe on 20/07/15.
 */
public class Coin {

    private ArrayList<Mur> murs = new ArrayList<Mur>();

    private Vector2 position = new Vector2();

    private static ArrayList<Coin> coins = new ArrayList<Coin>();

    public static Coin getCoin(Vector2 p) {
        for (Coin c : coins) {
            if (c.getPosition().epsilonEquals(p, 0.000_001f))
                return c;
        }
        return new Coin(p);
    }

    private Coin(Vector2 p) {
        position.set(p);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 v) {
        position.set(v);
    }

    public void addMur(Mur mur) {
        if (murs.size() == 0)
            coins.add(this);
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
            coins.remove(this);
    }

    public boolean isFirst(Mur mur) {
        return murs.size() > 0 && murs.get(0).equals(mur);
    }

}
