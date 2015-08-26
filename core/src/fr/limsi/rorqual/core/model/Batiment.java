package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import fr.limsi.rorqual.core.dpe.enums.wallproperties.OrientationEnum;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.models.Floor;

/**
 * Created by christophe on 22/07/15.
 */
public class Batiment {

    ModelContainer floor = Floor.getModel();

    private class EtageHolder {

        private ArrayList<Etage> etages = new ArrayList<Etage>();
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

    private EtageHolder etages = new EtageHolder();

    int current;

    public Batiment() {
        current = 0;
        Etage etage = new Etage();
        etage.setBatiment(this);
        etage.setOrientation(globalOrientation);
        etage.getModelGraph().setCamera(camera);
        etage.getModelGraph().getRoot().add(floor);
        etages.add(etage);
    }

    public Etage getCurrentEtage() {
        return etages.get(current);
    }

    OrientationEnum globalOrientation = OrientationEnum.INCONNUE;

    public void setOrientation(OrientationEnum orientation) {
        globalOrientation = orientation;
        for (Etage e : etages.list())
            e.setOrientation(orientation);
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
        for (int i = etages.getMin(); i <= current; i++) {
            Etage etage = etages.get(i);
            etage.getModelGraph().draw(modelBatch, environnement, type);
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

    private Camera camera = null;

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

}
