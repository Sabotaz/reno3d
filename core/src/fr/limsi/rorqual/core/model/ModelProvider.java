package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.List;

import eu.mihosoft.vrl.v3d.Primitive;
import eu.mihosoft.vrl.v3d.Vertex;

/**
 * Created by christophe on 08/04/15.
 */
public interface ModelProvider extends Primitive {

    public Matrix4 getPosition();

    public List<Vertex> getVertex();

}
