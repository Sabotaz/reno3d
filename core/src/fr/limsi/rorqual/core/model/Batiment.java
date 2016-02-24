package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.limsi.rorqual.core.dpe.enums.wallproperties.OrientationEnum;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.logic.Logic;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.models.Floor;

/**
 * Created by christophe on 22/07/15.
 */
// Un batiment contient plusieurs étages
//  * etage courant
//  * etage min/max
//  * draw
//  * orientation globale du batiment
@XStreamAlias("batiment")
public class Batiment {

    @XStreamOmitField
    private ModelContainer floor = Floor.getModel();
    @XStreamOmitField
    private Etage first;
    @XStreamOmitField
    private Etage last;
    @XStreamAlias("currentFloor")
    private int current;
    @XStreamAlias("globalOrientation")
    private OrientationEnum globalOrientation = OrientationEnum.SUD;
    @XStreamOmitField
    private Camera camera = null;
    @XStreamAlias("etages")
    private EtageHolder etages = new EtageHolder();

    @XStreamAlias("plafondsVisibles")
    boolean plafondsVisibles = false;

    private static class EtageHolder {

        @XStreamImplicit(itemFieldName="floor")
        private ArrayList<Etage> etages = new ArrayList<Etage>();
        @XStreamAlias("floorOffset")
        private int diff = 0; // positive offset

        public void add(Etage e) {
            addAtTop(e);
        }

        public void addAtTop(Etage e) {
            etages.add(e);
            e.setNumber(getMax());
        }

        public void addAtBottom(Etage e) {
            diff++;
            etages.add(0, e);
            for (int k = 0; k < etages.size(); k++) {
                etages.get(k).setNumber(k-diff);
            }
        }

        public Etage get(int i) {
            return etages.get(i+diff);
        }

        public int getMin() {
            return -diff;
        }

        public int getMax() {
            return etages.size()-1-diff;
        }

        public ArrayList<Etage> list() {
            return etages;
        }

    }

    public Batiment() {
        current = 0;
        Etage etage = new Etage();
        etage.setBatiment(this);
        etage.setOrientation(globalOrientation);
        etage.getModelGraph().setCamera(camera);
        etage.getModelGraph().getRoot().add(floor);
        first = last = etage;
        etages.add(etage);
    }

    public Etage getCurrentEtage() {
        return etages.get(current);
    }

    public void setGlobalOrientation(OrientationEnum orientation) {
        globalOrientation = orientation;
        updateOrientations();
    }

    public void updateOrientations() {
        for (Etage e : etages.list())
            e.setOrientation(globalOrientation);

        EventManager.getInstance().put(Channel.DPE, new Event(DpeEvent.ORIENTATION_GLOBALE_CHANGEE));
    }

    public void etageSuperieur() {
        int max = etages.getMax();
        if (current == max) { // add one
            Etage etage = new Etage();
            etage.setBatiment(this);
            etage.setOrientation(globalOrientation);
            etage.getModelGraph().setCamera(camera);
            etages.addAtTop(etage);
        }
        current++;

        getCurrentEtage().getModelGraph().getRoot().add(floor);

        heightChanged();
    }

    public void etageInferieur() {
        int min = etages.getMin();
        if (current == min) { // add one
            Etage etage = new Etage();
            etage.setBatiment(this);
            etage.setOrientation(globalOrientation);
            etage.getModelGraph().setCamera(camera);
            etages.addAtBottom(etage);
        }
        current--;

        getCurrentEtage().getModelGraph().getRoot().add(floor);

        heightChanged();
    }

    public void draw(ModelBatch modelBatch, Environment environnement, ModelContainer.Type type) {
        synchronized (this) {
            if (camera instanceof OrthographicCamera) {
                for (int i = etages.getMin(); i <= current; i++) {
                    Etage etage = etages.get(i);
                    etage.getModelGraph().draw(modelBatch, environnement, type);
                }
            } else {
                drawAll(modelBatch, environnement, type);
            }
        }
    }

    public void drawAll(ModelBatch modelBatch, Environment environnement, ModelContainer.Type type) {
        for (Etage etage : etages.list()) {
            etage.getModelGraph().draw(modelBatch, environnement, type);
        }
    }

    public void heightChanged() {
        float height = 0;
        for (int i = 0; i <= etages.getMax(); i++) { // up
            Etage etage = etages.get(i);
            Vector3 translation = etage.getModelGraph().getRoot().local_transform.getTranslation(new Vector3());
            translation.z = height;
            etage.getModelGraph().getRoot().local_transform.setTranslation(translation);
            height += etage.getHeight();
        }

        height = 0;
        for (int i = -1; i >= etages.getMin(); i--) { // down
            Etage etage = etages.get(i);
            height -= etage.getHeight();
            Vector3 translation = etage.getModelGraph().getRoot().local_transform.getTranslation(new Vector3());
            translation.z = height;
            etage.getModelGraph().getRoot().local_transform.setTranslation(translation);
        }
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
        for (Etage etage : etages.list()) {
            etage.getModelGraph().setCamera(camera);
        }
    }

    public ArrayList<Mur> getMurs() {
        ArrayList<Mur> all = new ArrayList<Mur>();
        for (Etage etage : etages.list())
            all.addAll(etage.getMurs());
        return all;
    }

    public ArrayList<Fenetre> getFenetres() {
        ArrayList<Fenetre> all = new ArrayList<Fenetre>();
        for (Etage etage : etages.list())
            all.addAll(etage.getFenetres());
        return all;
    }

    public ArrayList<Porte> getPortes() {
        ArrayList<Porte> all = new ArrayList<Porte>();
        for (Etage etage : etages.list())
            all.addAll(etage.getPortes());
        return all;
    }

    public ArrayList<PorteFenetre> getPorteFenetres() {
        ArrayList<PorteFenetre> all = new ArrayList<PorteFenetre>();
        for (Etage etage : etages.list())
            all.addAll(etage.getPorteFenetres());
        return all;
    }

    public ArrayList<Ouverture> getOuvertures() {
        ArrayList<Ouverture> all = new ArrayList<Ouverture>();
        for (Etage etage : etages.list())
            all.addAll(etage.getOuvertures());
        return all;
    }

    public ArrayList<Slab> getSlabs() {
        ArrayList<Slab> all = new ArrayList<Slab>();
        for (Etage etage : etages.list())
            all.addAll(etage.getSlabs());
        return all;
    }

    public void emptinessChanged(Etage etage) {
        if (etage.isEmpty()) { // c'est qu'on a enlevé des trucs
            if (first == null || etage.getNumber() == first.getNumber()) { // ça n'est plus le first
                changeFirst(etage, searchNewFirst());
            }
            if (last == null || etage.getNumber() == last.getNumber()) { // ça n'est plus le last
                changeLast(etage, searchNewLast());
            }
        } else { // c'est qu'il etait vide avant
            if (first == null || etage.getNumber() < first.getNumber()) { // c'est le nouveau first
                changeFirst(first, etage);
            }
            if (last == null || etage.getNumber() > last.getNumber()) { // c'est le nouveau last
                changeLast(first, etage);
            }
        }
    }

    private void changeFirst(Etage previousFirst, Etage newFirst) {
        if (previousFirst != null) {
            HashMap<String, Object> currentItems = new HashMap<String, Object>();
            currentItems.put("userObject", previousFirst);
            Event e = new Event(DpeEvent.IS_NO_MORE_FIRST_FLOOR, currentItems);
            EventManager.getInstance().put(Channel.DPE, e);
        }
        if (newFirst != null) {
            HashMap<String, Object> currentItems = new HashMap<String, Object>();
            currentItems.put("userObject", newFirst);
            Event e = new Event(DpeEvent.IS_NOW_FIRST_FLOOR, currentItems);
            EventManager.getInstance().put(Channel.DPE, e);
            first = newFirst;
        }
    }

    private void changeLast(Etage previousLast, Etage newLast) {
        if (previousLast != null) {
            HashMap<String, Object> currentItems = new HashMap<String, Object>();
            currentItems.put("userObject", previousLast);
            Event e = new Event(DpeEvent.IS_NO_MORE_LAST_FLOOR, currentItems);
            EventManager.getInstance().put(Channel.DPE, e);
        }
        if (newLast != null) {
            HashMap<String, Object> currentItems = new HashMap<String, Object>();
            currentItems.put("userObject", newLast);
            Event e = new Event(DpeEvent.IS_NOW_LAST_FLOOR, currentItems);
            EventManager.getInstance().put(Channel.DPE, e);
            last = newLast;
        }
    }

    private Etage searchNewFirst() {
        for (int i = etages.getMin(); i <= etages.getMax(); i++)
            if (!etages.get(i).isEmpty())
                return etages.get(i);
        return null;
    }

    private Etage searchNewLast() {
        for (int i = etages.getMax(); i >= etages.getMin(); i--)
            if (!etages.get(i).isEmpty())
                return etages.get(i);
        return null;
    }

    public Etage getFirstEtage() {
        return first;
    }

    public Etage getLastEtage() {
        return last;
    }

    public Etage getEtage(int i) {
        return etages.get(i);
    }

    public ArrayList<Etage> getAllEtages(){
        return this.etages.list();
    }

    public ModelContainer hitCurrentEtage(int x, int y) {
        this.floor.setSelectable(true);
        ModelContainer modelContainer = this.getCurrentEtage().getModelGraph().hit(x,y);
        this.floor.setSelectable(false);
        return modelContainer;
    }

    public void setEtage(int i) {
        current = i; // etage
        getCurrentEtage().getModelGraph().getRoot().add(floor);
    }

    public void reset() {
        setEtage(0);
    }

    public boolean arePlafondsVisibles() {
        return plafondsVisibles;
    }

    public void setPlafondsVisibles(boolean b) {
        plafondsVisibles = b;
    }

    public void act() {
        synchronized (this) {
            for (Etage etage : etages.list()) {
                etage.getModelGraph().act();
            }
        }
    }

    public void reload() {
            System.out.println("reload...");
            floor = Floor.getModel();
            for (int i = etages.getMin(); i <= etages.getMax(); i++) {
                Etage etage = etages.get(i);
                etage.setBatiment(this);
                etage.reload();
                etage.setOrientation(globalOrientation);
            }
            getCurrentEtage().getModelGraph().getRoot().add(floor);
    }
}
