package fr.limsi.rorqual.core.model.primitives;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Polygon;
import eu.mihosoft.vrl.v3d.PropertyStorage;
import eu.mihosoft.vrl.v3d.Vertex;
import fr.limsi.rorqual.core.model.ModelProvider;

/**
 * Created by christophe on 08/04/15.
 */
public abstract class AbstractModelProvider implements ModelProvider {
    protected Matrix4 placement = new Matrix4();
    protected List<Vertex> vertex = new ArrayList<Vertex>();
    protected final PropertyStorage properties = new PropertyStorage();
    protected List<Polygon> polygons = new ArrayList<Polygon>();

    public Matrix4 getPosition() {
        return placement;
    }

    public CSG toCSG() {
        return CSG.fromPolygons(getProperties(), toPolygons());
    }
    public PropertyStorage getProperties() {
        return properties;
    }
    public List<Polygon> toPolygons() {
        return polygons;
    }
    public List<Vertex> getVertex() {
        return vertex;
    }
}
