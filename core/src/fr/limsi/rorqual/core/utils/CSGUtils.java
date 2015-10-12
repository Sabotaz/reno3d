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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Extrude;
import eu.mihosoft.vrl.v3d.Polygon;
import eu.mihosoft.vrl.v3d.Vector3d;
import eu.mihosoft.vrl.v3d.Vertex;
import eu.mihosoft.vrl.v3d.ext.org.poly2tri.DelaunayTriangle;
import eu.mihosoft.vrl.v3d.ext.org.poly2tri.Poly2Tri;
import eu.mihosoft.vrl.v3d.ext.org.poly2tri.PolygonPoint;
import eu.mihosoft.vrl.v3d.ext.org.poly2tri.TriangulationPoint;

/**
 * Created by christophe on 14/04/15.
 */
// Classe utilitaire pour la conversion de/en CSG
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

        for (int p = 0; p < polygons.size(); p++) {
            Polygon polygon = polygons.get(p);

            meshBuilder = builder.part("polygon_triangles_"+p, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material("Color", ColorAttribute.createDiffuse(Color.WHITE)));

            Vector3 p1 = CSGUtils.castVector(polygon.vertices.get(0).pos);
            Vector3 N = CSGUtils.castVector(polygon.plane.normal);
            MeshPartBuilder.VertexInfo vi1 = new MeshPartBuilder.VertexInfo().setPos(p1).setNor(N);
            for (int i = 0; i < polygon.vertices.size()-2; i++) {
                Vector3 p2 = CSGUtils.castVector(polygon.vertices.get(i + 1).pos);
                Vector3 p3 = CSGUtils.castVector(polygon.vertices.get(i + 2).pos);
                MeshPartBuilder.VertexInfo vi2 = new MeshPartBuilder.VertexInfo().setPos(p2).setNor(N);
                MeshPartBuilder.VertexInfo vi3 = new MeshPartBuilder.VertexInfo().setPos(p3).setNor(N);
                meshBuilder.triangle(vi1,vi2,vi3);
                //meshBuilder.triangle(vi1,vi3,vi2);
            }
        }

        return builder.end();
    }


    public static Model toModel(CSG csg, Material frontMaterial, Material backMaterial, Material top, Material depth) {

        ModelBuilder builder = new ModelBuilder();

        builder.begin();

        Node node = builder.node();
        node.id = "base";

        MeshPartBuilder meshBuilder;

        List<Polygon> polygons = csg.getPolygons();

        for (int p = 0; p < polygons.size(); p++) {
            Polygon polygon = polygons.get(p);

            Vector3 p1 = CSGUtils.castVector(polygon.vertices.get(0).pos);
            Vector3 N = CSGUtils.castVector(polygon.plane.normal);

            if (N.epsilonEquals(0,1,0,0))
                meshBuilder = builder.part("polygon_triangles_"+p, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, frontMaterial);
            else if (N.epsilonEquals(0,-1,0,0))
                meshBuilder = builder.part("polygon_triangles_"+p, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, backMaterial);
            else if (N.epsilonEquals(1,0,0,0) || N.epsilonEquals(-1,0,0,0))
                meshBuilder = builder.part("polygon_triangles_"+p, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, depth);
            else
                meshBuilder = builder.part("polygon_triangles_"+p, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, top);

            Vector2 uv1, uv2, uv3;
            if (N.epsilonEquals(0,1,0,0) || N.epsilonEquals(0,-1,0,0))
                uv1 = new Vector2(p1.x, p1.z);
            else if (N.epsilonEquals(1,0,0,0) || N.epsilonEquals(-1,0,0,0))
                uv1 = new Vector2(p1.y, p1.z);
            else
                uv1 = new Vector2(p1.x, p1.y);

            MeshPartBuilder.VertexInfo vi1 = new MeshPartBuilder.VertexInfo().setPos(p1).setNor(N).setUV(uv1);

            for (int i = 0; i < polygon.vertices.size()-2; i++) {

                Vector3 p2 = CSGUtils.castVector(polygon.vertices.get(i + 1).pos);
                Vector3 p3 = CSGUtils.castVector(polygon.vertices.get(i + 2).pos);

                if (N.epsilonEquals(0,1,0,0) || N.epsilonEquals(0,-1,0,0)) {
                    uv2 = new Vector2(p2.x, p2.z);
                    uv3 = new Vector2(p3.x, p3.z);
                } else if (N.epsilonEquals(1,0,0,0) || N.epsilonEquals(-1,0,0,0)) {
                    uv2 = new Vector2(p2.y, p2.z);
                    uv3 = new Vector2(p3.y, p3.z);
                } else {
                    uv2 = new Vector2(p2.x, p2.y);
                    uv3 = new Vector2(p3.x, p3.y);
                }
                //System.out.println(uv1 + "," + uv2 + "," + uv3);
                MeshPartBuilder.VertexInfo vi2 = new MeshPartBuilder.VertexInfo().setPos(p2).setNor(N).setUV(uv2);
                MeshPartBuilder.VertexInfo vi3 = new MeshPartBuilder.VertexInfo().setPos(p3).setNor(N).setUV(uv3);
                meshBuilder.triangle(vi1,vi2,vi3);
                //meshBuilder.triangle(vi1,vi3,vi2);
            }
        }

        return builder.end();
    }

}
