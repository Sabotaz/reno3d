package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.math.Matrix4;

import java.util.ArrayList;
import java.util.HashMap;

import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.model.Etage;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Objet;
import fr.limsi.rorqual.core.model.Ouverture;
import fr.limsi.rorqual.core.model.Slab;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;

/**
 * Created by christophe on 04/09/15.
 */
// Classe gérant la suppression de models
public class Rotater extends ModelMaker {

    int sens = 0;

    public Rotater(int sens) {
        super();
        this.sens = sens;
    }

    boolean rotating_object = false;

    ModelContainer rotated_object;

    @Override
    public void begin(int screenX, int screenY) {
        if (rotating_object == true) return;
        Etage currentEtage = ModelHolder.getInstance().getBatiment().getCurrentEtage();
        rotated_object = currentEtage.getModelGraph().getObject(screenX, screenY);
        if (rotated_object == null || !(rotated_object instanceof Objet)) {
            rotating_object = false;
        } else {
            rotating_object = true;
            new Thread() {
                @Override
                public void run() {
                    while (rotating_object) {
                        ((Objet) rotated_object).model_transform.mulLeft(new Matrix4().setToRotation(0,0,1,sens * 15f));
                        try {
                            sleep(150);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
    }

    @Override
    public void update(int screenX, int screenY) {
    }

    @Override
    public void end(int screenX, int screenY) {
        rotating_object = false;
        rotated_object = null;
    }

    @Override
    public void abort() {
        rotating_object = false;
        rotated_object = null;
    }

    @Override
    public boolean isStarted() {
        return rotating_object;
    }

}