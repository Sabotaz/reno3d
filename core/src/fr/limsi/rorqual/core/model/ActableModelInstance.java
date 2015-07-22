package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Created by christophe on 22/07/15.
 */
public class ActableModelInstance extends ActableModel {

    public ActableModelInstance(ModelInstance m) {
        Model model = m.model;

        super.materials.clear();     this.materials.addAll(model.materials);
        super.meshes.clear();        this.meshes.addAll(model.meshes);
        super.meshParts.clear();     this.meshParts.addAll(model.meshParts);
        super.nodes.clear();         this.nodes.addAll(model.nodes);
        super.animations.clear();    this.animations.addAll(model.animations);
    }

    @Override
    public void act() {

    }
}
