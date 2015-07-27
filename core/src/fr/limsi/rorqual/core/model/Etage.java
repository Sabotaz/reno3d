package fr.limsi.rorqual.core.model;

import java.util.ArrayList;
import java.util.HashMap;

import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;
import fr.limsi.rorqual.core.utils.scene3d.models.Floor;

/**
 * Created by christophe on 20/07/15.
 */
public class Etage {
    private ArrayList<Mur> murs = new ArrayList<Mur>();
    private ArrayList<Ouverture> ouvertures = new ArrayList<Ouverture>();
    private HashMap<Object, ModelContainer> containerHashMap = new HashMap<Object, ModelContainer>();
    private int number;
    private ModelGraph modelGraph = new ModelGraph();

    {
        ModelContainer floor = Floor.getModel();
        containerHashMap.put(Floor.getModel(), floor);
        //floor.setSelectable(false);
        modelGraph.getRoot().add(floor);
    }

    public ArrayList<Mur> getMurs() {
        return murs;
    }

    public void addMur(Mur mur) {
        this.murs.add(mur);
        mur.setEtage(this);
        this.modelGraph.getRoot().add(mur);
    }

    public void addOuverture(Ouverture ouverture) {
        this.ouvertures.add(ouverture);
    }
    public void removeOuverture(Ouverture ouverture) {
        this.ouvertures.remove(ouverture);
    }

    public ArrayList<Ouverture> getOuvertures() {
        return ouvertures;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ModelGraph getModelGraph() {
        return modelGraph;
    }

}
