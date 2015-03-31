package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Color;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import fr.limsi.rorqual.core.model.ModelFactoryStrategy;
import fr.limsi.rorqual.core.utils.DefaultMutableTreeNode;

import fr.limsi.rorqual.core.model.IfcHolder;
import ifc2x3javatoolbox.ifc2x3tc1.DOUBLE;
import ifc2x3javatoolbox.ifc2x3tc1.IfcAxis2Placement2D;
import ifc2x3javatoolbox.ifc2x3tc1.IfcAxis2Placement3D;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDirection;
import ifc2x3javatoolbox.ifc2x3tc1.IfcGridPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLengthMeasure;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLocalPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcObjectPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProduct;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRoot;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWall;
import ifc2x3javatoolbox.ifc2x3tc1.LIST;
import ifc2x3javatoolbox.ifc2x3tc1.SET;
import scene3d.Group3d;
import scene3d.Stage3d;

public class MainApplicationAdapter extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

    ShapeRenderer shape;
    Stage3d stage;

    @Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("data/gdx/badlogic.jpg");
        shape = new ShapeRenderer();

        DefaultMutableTreeNode spatialStructureTreeNode = IfcHolder.getInstance().getSpatialStructureTreeNode();
        //print(spatialStructureTreeNode, 0);

        stage = makeSceneGraph(spatialStructureTreeNode);
        OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1f/10;
        stage.setCamera(camera);
        //stage.getRoot().print();
	}

    private LIST<IfcLengthMeasure> computeLocation(IfcObjectPlacement placement) {
        if (placement instanceof IfcLocalPlacement) {
            IfcLocalPlacement localPlacement = (IfcLocalPlacement) placement;
            if (localPlacement.getPlacementRelTo() != null) {
                LIST<IfcLengthMeasure> parentLoc = computeLocation(localPlacement.getPlacementRelTo());
                if (localPlacement.getRelativePlacement() instanceof IfcAxis2Placement3D) {
                    IfcAxis2Placement3D placement3D = (IfcAxis2Placement3D) localPlacement.getRelativePlacement();
                    LIST<IfcLengthMeasure> loc = placement3D.getLocation().getCoordinates();
                    for (int i = 0; i < parentLoc.size(); i++) {
                        parentLoc.set(i, new IfcLengthMeasure(parentLoc.get(i).value + loc.get(i).value));
                    }
                    return parentLoc;
                }
            } else {
                if (localPlacement.getRelativePlacement() instanceof IfcAxis2Placement3D) {
                    IfcAxis2Placement3D placement3D = (IfcAxis2Placement3D) localPlacement.getRelativePlacement();
                    return placement3D.getLocation().getCoordinates();
                }
            }
        }
        return null;
    }

    private Matrix4 computeMatrix(IfcObjectPlacement placement) {
        if (placement instanceof IfcLocalPlacement) {
            IfcLocalPlacement localPlacement = (IfcLocalPlacement) placement;
            if (localPlacement.getPlacementRelTo() != null) {
                Matrix4 parentLoc = computeMatrix(localPlacement.getPlacementRelTo());
                if (localPlacement.getRelativePlacement() instanceof IfcAxis2Placement3D) {
                    Matrix4 loc = toMatrix((IfcAxis2Placement3D) localPlacement.getRelativePlacement());
                    return parentLoc.mul(loc); // est-ce le bon sens ?
                } else if (localPlacement.getRelativePlacement() instanceof IfcAxis2Placement2D) {
                    Matrix4 loc = toMatrix((IfcAxis2Placement2D) localPlacement.getRelativePlacement());
                    return parentLoc.mul(loc);
                }
            } else {
                if (localPlacement.getRelativePlacement() instanceof IfcAxis2Placement3D) {
                    return toMatrix((IfcAxis2Placement3D) localPlacement.getRelativePlacement());
                } else if (localPlacement.getRelativePlacement() instanceof IfcAxis2Placement2D) {
                    return toMatrix((IfcAxis2Placement2D) localPlacement.getRelativePlacement());
                }
            }
        } else if (placement instanceof IfcGridPlacement) {
            return new Matrix4(); //ToDo
        }
        return new Matrix4(); // n'arrive normalement jamais
    }

    public void getProductsPlacements(DefaultMutableTreeNode treeNode, HashMap<IfcObjectPlacement, Group3d> nodes, Queue<IfcObjectPlacement> queue) {
        if (treeNode.getUserObject() instanceof IfcProduct) {
            IfcProduct product = (IfcProduct) treeNode.getUserObject();
            IfcObjectPlacement placement = product.getObjectPlacement();
            Group3d node = new Group3d(ModelFactoryStrategy.getModel(product));
            node.setName(product.getGlobalId().getEncodedValue());
            node.userData = product;
            nodes.put(product.getObjectPlacement(), node);
            queue.add(placement);
        }
        for (int i = 0; i < treeNode.getChildCount(); i++) {
            getProductsPlacements(treeNode.getChildAt(i), nodes, queue);
        }
    }

    public Stage3d makeSceneGraph(DefaultMutableTreeNode treeNode) {

        HashMap<IfcObjectPlacement, Group3d> nodes = new HashMap<IfcObjectPlacement,Group3d>();

        Stage3d stage = new Stage3d();
        stage.getRoot().setTransform(new Matrix4());

        Queue<IfcObjectPlacement> queue = new LinkedList<IfcObjectPlacement>();
        getProductsPlacements(treeNode, nodes, queue);

        System.out.println(nodes.size());

        while (!queue.isEmpty()) {
            IfcObjectPlacement placement = queue.poll();
            Group3d node = nodes.get(placement);
            Matrix4 loc = new Matrix4();
            if (placement instanceof IfcLocalPlacement) {
                IfcLocalPlacement localPlacement = (IfcLocalPlacement) placement;
                if (localPlacement.getRelativePlacement() instanceof IfcAxis2Placement3D) {
                    loc = toMatrix((IfcAxis2Placement3D) localPlacement.getRelativePlacement());
                } else if (localPlacement.getRelativePlacement() instanceof IfcAxis2Placement2D) {
                    loc = toMatrix((IfcAxis2Placement2D) localPlacement.getRelativePlacement());
                }

                // parents
                if (localPlacement.getPlacementRelTo() != null) {
                    if (nodes.containsKey(localPlacement.getPlacementRelTo())) {
                        Group3d parent = nodes.get(localPlacement.getPlacementRelTo());
                        parent.addActor3d(node);
                    } else {
                        Group3d parent = new Group3d();
                        parent.addActor3d(node);
                        nodes.put(localPlacement.getPlacementRelTo(), parent);
                        queue.add(localPlacement.getPlacementRelTo());
                    }
                } else { // root
                    System.out.println("root...");
                    stage.getRoot().addActor3d(node);
                }

            } else if (placement instanceof IfcGridPlacement) {
                IfcGridPlacement gridPlacement = (IfcGridPlacement) placement;
                //ToDo
            }
            node.setTransform(loc);
            System.out.println(node.getTransform());
            System.out.println(node.getX());
            //node.transform = loc;
        }
        return stage;
    }

    public Matrix4 toMatrix(IfcAxis2Placement3D placement3D) {

        IfcDirection ifc_x = placement3D.getRefDirection();
        IfcDirection ifc_z = placement3D.getAxis();
        IfcCartesianPoint ifc_p = placement3D.getLocation();

        Vector3 x = new Vector3(1.f,0.f,0.f);
        Vector3 z = new Vector3(0.f,0.f,1.f);

        if (ifc_x != null) {
            LIST<DOUBLE> xd = ifc_x.getDirectionRatios();
            x.set((float) xd.get(0).value, (float) xd.get(1).value, (float) xd.get(2).value);
        }
        if (ifc_z != null) {
            LIST<DOUBLE> zd = ifc_z.getDirectionRatios();
            z.set((float)zd.get(0).value, (float)zd.get(1).value, (float)zd.get(2).value);
        }
        LIST<IfcLengthMeasure> pd = ifc_p.getCoordinates();

        Vector3 p = new Vector3((float)pd.get(0).value, (float)pd.get(1).value, (float)pd.get(2).value);
        Vector3 y = z.cpy().crs(x);

        return new Matrix4().set(x,y,z,p);
    }

    public Matrix4 toMatrix(IfcAxis2Placement2D placement2D) {

        IfcDirection ifc_x = placement2D.getRefDirection();
        IfcCartesianPoint ifc_p = placement2D.getLocation();

        Vector3 x = new Vector3(1.f,0.f,0.f);
        Vector3 z = new Vector3(0.f,0.f,1.f);

        if (ifc_x != null) {
            LIST<DOUBLE> xd = ifc_x.getDirectionRatios();
            x.set((float) xd.get(0).value, (float) xd.get(1).value, 0.f);
        }
        LIST<IfcLengthMeasure> pd = ifc_p.getCoordinates();

        Vector3 p = new Vector3((float)pd.get(0).value, (float)pd.get(1).value, 0.f);
        Vector3 y = z.cpy().crs(x);

        return new Matrix4().set(x,y,z,p);
    }


    /*public Matrix4 computeLocation(IfcObjectPlacement placement) {
        return null;
    }*/

    public void print(DefaultMutableTreeNode treeNode, int tab) {

        for (int i = 0; i < tab; i++)
            System.out.print(" ");
        if (treeNode.getUserObject() instanceof IfcWall) {
            System.out.println("IfcWall: " + ((IfcWall) treeNode.getUserObject()).getName() + " (" + ((IfcWall) treeNode.getUserObject()).getGlobalId() + ")");
            IfcWall wall = (IfcWall)    treeNode.getUserObject();
            IfcObjectPlacement placement = wall.getObjectPlacement();
            System.out.println(placement);
            System.out.println(placement.getClass());
            if (placement instanceof IfcLocalPlacement) {
                IfcLocalPlacement localPlacement = (IfcLocalPlacement) placement;
                if (localPlacement.getRelativePlacement() instanceof IfcAxis2Placement3D) {
                    System.out.println("computeLocation: " + computeLocation(localPlacement));
                    System.out.println("computeMatrix: " + computeMatrix(localPlacement));
                    IfcAxis2Placement3D placement3D = (IfcAxis2Placement3D)localPlacement.getRelativePlacement();
                    System.out.println(placement3D.getAxis().getDirectionRatios());
                    System.out.println(placement3D.getRefDirection().getDirectionRatios());
                    System.out.println(placement3D.getLocation().getCoordinates());
                    System.out.println(placement3D);
                }
            }
            System.out.println(placement.getPlacesObject_Inverse());
            SET<IfcLocalPlacement> set = placement.getReferencedByPlacements_Inverse();
            if (set != null)
                for (IfcLocalPlacement ilp : set)
                    System.out.println(ilp.getRelativePlacement());
        } else if (treeNode.getUserObject() instanceof IfcRoot) {
            System.out.println("IfcRoot: " + ((IfcRoot) treeNode.getUserObject()).getName() + " (" + ((IfcRoot) treeNode.getUserObject()).getGlobalId() + ") [" + treeNode.getUserObject().getClass().getName() + "]");
        } else {
            System.out.println(treeNode.getUserObject() + " [" + treeNode.getUserObject().getClass().getName() + "]");
        }

        for (int i = 0; i < treeNode.getChildCount(); i++) {
            DefaultMutableTreeNode currentTreeNode = (DefaultMutableTreeNode)treeNode.getChildAt(i);
            print(currentTreeNode, tab + 1);
        }

    }

	@Override
	public void render () {

        stage.getCamera().position.set(0.f, 0, 0f);
        stage.getCamera().lookAt(0f, 0f, -1f);
        stage.getCamera().up.set(0,1,0);

		Gdx.gl.glClearColor(0.12f, 0.38f, 0.55f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

        shape.begin(ShapeRenderer.ShapeType.Line);

        int grid_size = 10;

        for (int i = 0; i < Gdx.graphics.getHeight()/grid_size; i++) {
            if (i % 5 == 0)
                shape.setColor(new Color(1, 1, 1, 0.15f));
            else
                shape.setColor(new Color(1, 1, 1, 0.05f));
            shape.line(0, i*grid_size, Gdx.graphics.getWidth(), i*grid_size);
        }
        for (int i = 0; i < Gdx.graphics.getWidth()/grid_size; i++) {
            if (i % 5 == 0)
                shape.setColor(new Color(1, 1, 1, 0.15f));
            else
                shape.setColor(new Color(1, 1, 1, 0.05f));
            shape.line(i*grid_size, 0, i*grid_size, Gdx.graphics.getHeight());
        }

        shape.end();
        /*
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();*/
        Gdx.gl.glDisable(Gdx.gl.GL_BLEND);

        stage.act();
        stage.draw();

	}
}
