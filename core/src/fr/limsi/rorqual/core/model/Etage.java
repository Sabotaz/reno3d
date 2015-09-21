package fr.limsi.rorqual.core.model;

import java.util.ArrayList;

import fr.limsi.rorqual.core.dpe.Dpe;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.OrientationEnum;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;

/**
 * Created by christophe on 20/07/15.
 */
// un étage d'un batiment
//  * contient des murs, slabs, ouvertures
public class Etage {
    private ArrayList<Mur> murs = new ArrayList<Mur>();
    private ArrayList<Slab> slabs = new ArrayList<Slab>();
    private ArrayList<Ouverture> ouvertures = new ArrayList<Ouverture>();
    private int number;
    private Batiment batiment;
    private ModelGraph modelGraph = new ModelGraph();

    public final static float DEFAULT_HEIGHT = 2.8f;

    private float height = DEFAULT_HEIGHT;

    public ArrayList<Mur> getMurs() {
        return murs;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float h) {
        height = h;
        if (batiment != null)
            batiment.heightChanged();
        for (Mur m : murs) {
            m.setChanged();
        }
    }

    public boolean isEmpty() {
        return murs.isEmpty() && slabs.isEmpty();
    }

    public void addMur(Mur mur) {
        boolean wasEmpty = isEmpty();
        this.murs.add(mur);
        mur.setEtage(this);
        mur.setGlobalOrientation(globalOrientation);
        this.modelGraph.getRoot().add(mur);
        if (wasEmpty)
            batiment.emptinessChanged(this);
    }

    public void removeMur(Mur mur) {
        this.murs.remove(mur);
        this.modelGraph.getRoot().remove(mur);
        mur.setEtage(null);
        if (isEmpty())
            batiment.emptinessChanged(this);
    }

    public void addSlab(Slab slab) {
        boolean wasEmpty = isEmpty();
        this.slabs.add(slab);

        this.modelGraph.getRoot().add(slab);
        if (wasEmpty)
            batiment.emptinessChanged(this);
    }

    public void removeSlab(Slab slab) {
        this.slabs.remove(slab);
        this.modelGraph.getRoot().remove(slab);
        if (isEmpty())
            batiment.emptinessChanged(this);
    }

    public ArrayList<Slab> getSlabs() {
        return slabs;
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

    public Batiment getBatiment() {
        return batiment;
    }

    public void setBatiment(Batiment batiment) {
        this.batiment = batiment;
    }

    private OrientationEnum globalOrientation = OrientationEnum.SUD;

    public void setOrientation(OrientationEnum orientation) {
        globalOrientation = orientation;
        for (Mur m : murs){
            m.setGlobalOrientation(orientation);
        }
    }
    public void updateOrientation(Mur mur){
        mur.setGlobalOrientation(globalOrientation);
    }
}
