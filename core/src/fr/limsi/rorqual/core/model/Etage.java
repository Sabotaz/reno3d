package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.ArrayList;
import java.util.HashMap;

import fr.limsi.rorqual.core.dpe.enums.wallproperties.OrientationEnum;
import fr.limsi.rorqual.core.model.utils.Coin;
import fr.limsi.rorqual.core.ui.ModelLibrary;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;

/**
 * Created by christophe on 20/07/15.
 */
// un étage d'un batiment
//  * contient des murs, slabs, ouvertures
@XStreamAlias("floor")
public class Etage {
    @XStreamImplicit(itemFieldName="wall")
    private ArrayList<Mur> murs = new ArrayList<Mur>();
    @XStreamImplicit(itemFieldName="slab")
    private ArrayList<Slab> slabs = new ArrayList<Slab>();
    @XStreamImplicit(itemFieldName="opening")
    private ArrayList<Ouverture> ouvertures = new ArrayList<Ouverture>();
    @XStreamOmitField
    private ArrayList<Fenetre> fenetres = new ArrayList<Fenetre>();
    @XStreamOmitField
    private ArrayList<Porte> portes = new ArrayList<Porte>();
    @XStreamOmitField
    private ArrayList<PorteFenetre> porteFenetres = new ArrayList<PorteFenetre>();
    @XStreamAlias("number")
    private int number;
    @XStreamAlias("elevation")
    private float elevation;
    @XStreamAlias("name")
    private String name;
    @XStreamOmitField
    private Batiment batiment;
    @XStreamOmitField
    private ModelGraph modelGraph = new ModelGraph();

    @XStreamOmitField
    public final static float DEFAULT_HEIGHT = 2.8f;
    @XStreamAlias("height")
    private float height = DEFAULT_HEIGHT;

    @XStreamAlias("globalOrientation")
    private OrientationEnum globalOrientation = OrientationEnum.SUD;

    public Etage() {
    }

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
        if (this.getNumber() == 0)
            this.elevation = 0;
        else if (this.getNumber() > 0) {
            Etage e = batiment.getEtage(getNumber() - 1);
            e.actualiseElevation();
            this.elevation = e.getElevation() + e.getHeight();
        } else if (this.getNumber() < 0) {
            Etage e = batiment.getEtage(getNumber() + 1);
            e.actualiseElevation();
            this.elevation = e.getElevation() - this.getHeight();
        }
    }

    public float getElevation(){
        actualiseElevation();
        return this.elevation;
    }

    public String getName(){
        return this.name;
    }

    public boolean isFirst(){
        if (this == this.batiment.getFirstEtage()){
            return true;
        }else{
            return false;
        }
    }

    public boolean isLast(){
        if (this == this.batiment.getLastEtage()){
            return true;
        }else{
            return false;
        }
    }

    public void reload() {
        this.modelGraph = new ModelGraph();
        HashMap<Mur,Mur> reloadedMurs = new HashMap<Mur, Mur>();
        HashMap<Slab,Slab> reloadedSlabs = new HashMap<Slab, Slab>();
        HashMap<Fenetre,Fenetre> reloadedFenetres = new HashMap<Fenetre, Fenetre>();
        HashMap<Porte,Porte> reloadedPortes = new HashMap<Porte, Porte>();
        HashMap<PorteFenetre,PorteFenetre> reloadedPorteFenetres = new HashMap<PorteFenetre, PorteFenetre>();
        HashMap<Ouverture,Ouverture> reloadedOuvertures = new HashMap<Ouverture, Ouverture>();

        // reload walls
        if (murs != null)
            for (Mur m : murs) {
                Mur mur = new Mur(Coin.getCoin(number, m.getA().getPosition()), Coin.getCoin(number, m.getB().getPosition()), m);
                mur.setEtage(this);
                reloadedMurs.put(m, mur);
                modelGraph.getRoot().add(mur);
            }

        // reload slabs
        if (slabs != null)
            for (Slab s : slabs) {
                ArrayList<Coin> coins = new ArrayList<Coin>();
                for (Coin coin : s.getCoins())
                    coins.add(Coin.getCoin(number, coin.getPosition()));
                Slab slab = new Slab(coins, s);
                for (Mur m : s.getMurs())
                    if (reloadedMurs.containsKey(m))
                        slab.addMur(reloadedMurs.get(m));
                slab.setEtage(this);
                if (s.getObjets() != null)
                    for (Objet o : s.getObjets()) {
                        Objet obj = (Objet)(ModelLibrary.getInstance().getModelContainerFromId(o.getModelId()));
                        obj.setModelId(o.getModelId());
                        obj.setSelectable(false);
                        obj.setSlab(slab);
                        obj.setPosition(o.x, o.y);
                        obj.setToRotation(o.angle);
                        slab.addObjet(obj);
                        obj.calculateBoundingBox(new BoundingBox());
                    }
                reloadedSlabs.put(s,slab);
                modelGraph.getRoot().add(slab);
            }

        // reload left/right slab for walls
        for (Mur mur : reloadedMurs.values()) {
            if (mur.getSlabGauche() != null)
                mur.setSlabGauche(reloadedSlabs.get(mur.getSlabGauche()));
            if (mur.getSlabDroit() != null)
                mur.setSlabDroit(reloadedSlabs.get(mur.getSlabDroit()));
        }

        fenetres = new ArrayList<Fenetre>();
        portes = new ArrayList<Porte>();
        porteFenetres = new ArrayList<PorteFenetre>();
        ArrayList<Ouverture> badOuvertures = ouvertures;
        ouvertures = new ArrayList<Ouverture>();

        if (badOuvertures != null) {
            for (Ouverture o : badOuvertures) {
                Ouverture ouverture = (Ouverture) ModelLibrary.getInstance().getModelContainerFromId(o.getModelId());
                reloadedOuvertures.put(o, ouverture);

                ouverture.copy(o);
                ouverture.setMur(reloadedMurs.get(o.getMur()));
            }
        }

        murs = new ArrayList<Mur>(reloadedMurs.values());
        slabs = new ArrayList<Slab>(reloadedSlabs.values());
    }

}
