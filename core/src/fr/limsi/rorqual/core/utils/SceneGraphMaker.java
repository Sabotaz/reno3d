package fr.limsi.rorqual.core.utils;

import com.badlogic.gdx.math.Matrix4;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import fr.limsi.rorqual.core.model.ModelFactoryStrategy;
import ifc2x3javatoolbox.ifc2x3tc1.IfcGridPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLocalPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcObjectPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProduct;
import scene3d.Group3d;
import scene3d.Stage3d;

/**
 * Created by christophe on 31/03/15.
 */
public class SceneGraphMaker {

    public static void makeSceneGraph(DefaultMutableTreeNode treeNode, Stage3d stage) {

        HashMap<IfcObjectPlacement, Group3d> nodes = new HashMap<IfcObjectPlacement,Group3d>();

        stage.getRoot().setTransform(new Matrix4());

        Queue<IfcObjectPlacement> queue = new LinkedList<IfcObjectPlacement>();
        getProductsPlacements(treeNode, nodes, queue);

        while (!queue.isEmpty()) {
            IfcObjectPlacement placement = queue.poll();
            Group3d node = nodes.get(placement);
            Matrix4 loc = new Matrix4();
            if (placement instanceof IfcLocalPlacement) {
                IfcLocalPlacement localPlacement = (IfcLocalPlacement) placement;
                loc = IfcObjectPlacementUtils.toMatrix(localPlacement.getRelativePlacement());

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
                    stage.getRoot().addActor3d(node);
                }

            } else if (placement instanceof IfcGridPlacement) {
                IfcGridPlacement gridPlacement = (IfcGridPlacement) placement;
                //ToDo
            }
            node.setTransform(loc);
        }
    }


    public static void getProductsPlacements(DefaultMutableTreeNode treeNode, HashMap<IfcObjectPlacement, Group3d> nodes, Queue<IfcObjectPlacement> queue) {
        if (treeNode.getUserObject() instanceof IfcProduct) {
            IfcProduct product = (IfcProduct) treeNode.getUserObject();
            IfcObjectPlacement placement = product.getObjectPlacement();
            Group3d node = new Group3d(ModelFactoryStrategy.getModel(product));
            node.setName(product.getGlobalId().getEncodedValue());
            node.setTransform(IfcObjectPlacementUtils.computeMatrix(placement));
            node.userData = product;
            nodes.put(product.getObjectPlacement(), node);
            queue.add(placement);
        }
        for (int i = 0; i < treeNode.getChildCount(); i++) {
            getProductsPlacements(treeNode.getChildAt(i), nodes, queue);
        }
    }
}
