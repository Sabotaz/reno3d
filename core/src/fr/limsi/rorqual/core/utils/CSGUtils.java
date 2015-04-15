package fr.limsi.rorqual.core.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.List;

import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Polygon;
import eu.mihosoft.vrl.v3d.Vector3d;
import eu.mihosoft.vrl.v3d.Vertex;

/**
 * Created by christophe on 14/04/15.
 */
public class CSGUtils {

    public static Vertex toVertex(Vector3 v) {
        return new Vertex(new Vector3d(v.x, v.y, v.z), new Vector3d(0,0,1));
    }

    public static Vertex toVertex(Vector3 v, Vector3 n) {
        return new Vertex(new Vector3d(v.x, v.y, v.z), new Vector3d(n.x, n.y, n.z));
    }

    public static Vector3 castVector(Vector3d v) {
        return new Vector3((float)v.x, (float)v.y, (float)v.z);
    }

    public static Vector3d castVector(Vector3 v) {
        return new Vector3d(v.x, v.y, v.z);
    }

    public static Model toModel(CSG csg) {

        ModelBuilder builder = new ModelBuilder();

        builder.begin();

        Node node = builder.node();
        node.id = "base";

        MeshPartBuilder meshBuilder;

        List<Polygon> polygons = csg.getPolygons();
        System.out.println("np: " + polygons.size());

        for (int p = 0; p < polygons.size(); p++) {
            Polygon polygon = polygons.get(p);
            System.out.println("npp: " + polygon.vertices.size());

            meshBuilder = builder.part("polygon_triangles_"+p, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.WHITE)));

            /*Vector3 p1 = CSGUtils.castVector(polygon.vertices.get(0).pos);
            for (int i = 0; i < polygon.vertices.size()-2; i++) {
                Vector3 p2 = CSGUtils.castVector(polygon.vertices.get(i + 1).pos);
                Vector3 p3 = CSGUtils.castVector(polygon.vertices.get(i + 2).pos);
                meshBuilder.triangle(p1, p3, p2);
                meshBuilder.triangle(p1, p2, p3);
            }*/
            /*
            Vector3 p1 = CSGUtils.castVector(polygon.vertices.get(0).pos);
            Vector3 n1 = CSGUtils.castVector(polygon.vertices.get(0).normal);
            short i1 = meshBuilder.vertex(p1,n1,Color.WHITE,new Vector2());
            for (int i = 0; i < polygon.vertices.size()-2; i++) {
                Vector3 p2 = CSGUtils.castVector(polygon.vertices.get(i + 1).pos);
                Vector3 n2 = CSGUtils.castVector(polygon.vertices.get(i + 1).normal);
                Vector3 p3 = CSGUtils.castVector(polygon.vertices.get(i + 2).pos);
                Vector3 n3 = CSGUtils.castVector(polygon.vertices.get(i + 2).normal);
                short i2 = meshBuilder.vertex(p2,n2,Color.WHITE,new Vector2());
                short i3 = meshBuilder.vertex(p3, n3, Color.WHITE, new Vector2());
                meshBuilder.triangle(i1, i2, i3);
                meshBuilder.triangle(i1, i3, i2);
            }*/

            Vector3 p1 = CSGUtils.castVector(polygon.vertices.get(0).pos);
            Vector3 n1 = CSGUtils.castVector(polygon.vertices.get(0).normal);
            MeshPartBuilder.VertexInfo vi1 = new MeshPartBuilder.VertexInfo().setPos(p1).setNor(n1);
            for (int i = 0; i < polygon.vertices.size()-2; i++) {
                Vector3 p2 = CSGUtils.castVector(polygon.vertices.get(i + 1).pos);
                Vector3 n2 = CSGUtils.castVector(polygon.vertices.get(i + 1).normal);
                Vector3 p3 = CSGUtils.castVector(polygon.vertices.get(i + 2).pos);
                Vector3 n3 = CSGUtils.castVector(polygon.vertices.get(i + 2).normal);
                MeshPartBuilder.VertexInfo vi2 = new MeshPartBuilder.VertexInfo().setPos(p2).setNor(n2);
                MeshPartBuilder.VertexInfo vi3 = new MeshPartBuilder.VertexInfo().setPos(p3).setNor(n3);
                meshBuilder.triangle(vi1,vi2,vi3);
                meshBuilder.triangle(vi1,vi3,vi2);
            }
        }

        for (int p = 0; p < polygons.size(); p++) {
            Polygon polygon = polygons.get(p);
            System.out.println("npp: " + polygon.vertices.size());

            meshBuilder = builder.part("polygon_lines_"+p, GL20.GL_LINES, VertexAttributes.Usage.Position, new Material(ColorAttribute.createDiffuse(Color.RED)));

            Vector3 p1 = CSGUtils.castVector(polygon.vertices.get(0).pos);
            for (int i = 0; i < polygon.vertices.size()-2; i++) {
                Vector3 p2 = CSGUtils.castVector(polygon.vertices.get(i + 1).pos);
                Vector3 p3 = CSGUtils.castVector(polygon.vertices.get(i + 2).pos);
                meshBuilder.triangle(p1, p2, p3);
            }
        }

        return builder.end();
    }

}
