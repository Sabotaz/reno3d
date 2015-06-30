package fr.limsi.rorqual.core.utils;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import fr.limsi.rorqual.core.model.ModelFactoryStrategy;
import fr.limsi.rorqual.core.utils.scene3d.ModelContainer;
import fr.limsi.rorqual.core.utils.scene3d.ModelGraph;
import ifc2x3javatoolbox.ifc2x3tc1.IfcGridPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLocalPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcObjectPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProduct;

/**
 * Created by christophe on 31/03/15.
 */
public class SceneGraphMaker {

    public static void makeSceneGraph(DefaultMutableTreeNode treeNode, ModelGraph stage) {

        HashMap<IfcObjectPlacement, ModelContainer> nodes = new HashMap<IfcObjectPlacement,ModelContainer>();

        Queue<IfcObjectPlacement> queue = new LinkedList<IfcObjectPlacement>();
        getProductsPlacements(treeNode, nodes, queue);

        while (!queue.isEmpty()) {
            IfcObjectPlacement placement = queue.poll();
            ModelContainer node = nodes.get(placement);
            Matrix4 loc = new Matrix4();
            if (placement instanceof IfcLocalPlacement) {
                IfcLocalPlacement localPlacement = (IfcLocalPlacement) placement;
                loc = IfcObjectPlacementUtils.toMatrix(localPlacement.getRelativePlacement());

                // parents
                if (localPlacement.getPlacementRelTo() != null) {
                    if (nodes.containsKey(localPlacement.getPlacementRelTo())) {
                        ModelContainer parent = nodes.get(localPlacement.getPlacementRelTo());
                        parent.add(node);
                    } else {
                        ModelContainer parent = new ModelContainer();
                        parent.add(node);
                        nodes.put(localPlacement.getPlacementRelTo(), parent);
                        queue.add(localPlacement.getPlacementRelTo());
                    }
                } else { // root
                    stage.getRoot().add(node);
                }
            } else if (placement instanceof IfcGridPlacement) {
                IfcGridPlacement gridPlacement = (IfcGridPlacement) placement;
                //ToDo
            }
            node.transform.mul(loc);
        }
    }


    public static void getProductsPlacements(DefaultMutableTreeNode treeNode, HashMap<IfcObjectPlacement, ModelContainer> nodes, Queue<IfcObjectPlacement> queue) {
        if (treeNode.getUserObject() instanceof IfcProduct) {
            IfcProduct product = (IfcProduct) treeNode.getUserObject();
            IfcObjectPlacement placement = product.getObjectPlacement();
            ModelInstance model = ModelFactoryStrategy.getModel(product);
            ModelContainer node = new ModelContainer(model);
            node.setUserData(product);
            nodes.put(product.getObjectPlacement(), node);
            queue.add(placement);
        }
        for (int i = 0; i < treeNode.getChildCount(); i++) {
            getProductsPlacements(treeNode.getChildAt(i), nodes, queue);
        }
    }
}
