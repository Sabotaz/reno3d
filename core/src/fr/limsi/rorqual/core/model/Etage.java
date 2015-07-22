package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;

import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;
import fr.limsi.rorqual.core.utils.scene3d.models.Floor;

/**
 * Created by christophe on 20/07/15.
 */
public class Etage {
    private ArrayList<Mur> murs = new ArrayList<Mur>();
    private int number;
    private ModelGraph modelGraph = new ModelGraph();

    {
        ModelContainer floor = new ModelContainer(Floor.getModelInstance());
        floor.setSelectable(false);
        modelGraph.getRoot().add(floor);
    }

    public ArrayList<Mur> getMurs() {
        return murs;
    }

    public void addMur(Mur mur) {
        this.murs.add(mur);
        this.modelGraph.getRoot().add(new ModelContainer(mur));
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
