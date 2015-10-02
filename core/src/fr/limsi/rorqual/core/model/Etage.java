package fr.limsi.rorqual.core.model;

import java.util.ArrayList;

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
    private ArrayList<Fenetre> fenetres = new ArrayList<Fenetre>();
    private ArrayList<Porte> portes = new ArrayList<Porte>();
    private ArrayList<PorteFenetre> porteFenetres = new ArrayList<PorteFenetre>();
    private int number;
    private float elevation;
    private String name;
    private Batiment batiment;
    private ModelGraph modelGraph = new ModelGraph();

    public final static float DEFAULT_HEIGHT = 2.8f;
    private float height = DEFAULT_HEIGHT;

    public Etage() {}

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
        this.actualiseElevation();
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
        if (ouverture instanceof Fenetre){
            this.fenetres.add((Fenetre) ouverture);
        }else if (ouverture instanceof Porte){
            this.portes.add((Porte) ouverture);
        }else if (ouverture instanceof PorteFenetre){
            this.porteFenetres.add((PorteFenetre) ouverture);
        }
    }

    public void removeOuverture(Ouverture ouverture) {
        this.ouvertures.remove(ouverture);
        if (ouverture instanceof Fenetre){
            this.fenetres.remove((Fenetre) ouverture);
        }else if (ouverture instanceof Porte){
            this.portes.remove((Porte) ouverture);
        }else if (ouverture instanceof PorteFenetre){
            this.porteFenetres.remove((PorteFenetre) ouverture);
        }
    }

    public ArrayList<Ouverture> getOuvertures() {
        return ouvertures;
    }

    public ArrayList<Fenetre> getFenetres() {
        return fenetres;
    }

    public ArrayList<Porte> getPortes() {
        return portes;
    }

    public ArrayList<PorteFenetre> getPorteFenetres() {
        return porteFenetres;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        this.actualiseName();
        this.actualiseElevation();
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

    private void actualiseName(){
        if (this.number<0){
            switch (this.number){
                case -1:
                    this.name = "1er sous-sol";
                    break;
                default:
                    int a = this.number*-1;
                    this.name = Integer.toString(a)+"eme sous-sol";
                    break;
            }
        }else{
            switch (this.number){
                case 0:
                    this.name = "Rez-de-chaussee";
                    break;
                case 1:
                    this.name = "1er etage";
                    break;
                default:
                    this.name = Integer.toString(this.number)+"eme etage";
                    break;
            }
        }
    }

    private void actualiseElevation(){
        this.elevation = this.getNumber()*this.getHeight();
    }

    public float getElevation(){
        return this.elevation;
    }

    public String getName(){
        return this.name;
    }
}
