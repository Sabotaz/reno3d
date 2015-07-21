package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by christophe on 20/07/15.
 */
public abstract class ActableModel extends Model implements RenderableProvider {

    public Matrix4 transform = new Matrix4().idt();
    public Object userData = null;

    public abstract void act();
    /** Traverses the Node hierarchy and collects {@link Renderable} instances for every node with a graphical representation.
     * Renderables are obtained from the provided pool. The resulting array can be rendered via a {@link ModelBatch}.
     *
     * @param renderables the output array
     * @param pool the pool to obtain Renderables from */
    public void getRenderables (Array<Renderable> renderables, Pool<Renderable> pool) {
        for (Node node : nodes) {
            getRenderables(node, renderables, pool);
        }
    }

    /** @return The renderable of the first node's first part. */
    public Renderable getRenderable (final Renderable out) {
        return getRenderable(out, nodes.get(0));
    }

    /** @return The renderable of the node's first part. */
    public Renderable getRenderable (final Renderable out, final Node node) {
        return getRenderable(out, node, node.parts.get(0));
    }

    public Renderable getRenderable (final Renderable out, final Node node, final NodePart nodePart) {
        nodePart.setRenderable(out);
        if (nodePart.bones == null && transform != null)
            out.worldTransform.set(transform).mul(node.globalTransform);
        else if (transform != null)
            out.worldTransform.set(transform);
        else
            out.worldTransform.idt();
        out.userData = userData;
        return out;
    }

    protected void getRenderables (Node node, Array<Renderable> renderables, Pool<Renderable> pool) {
        if (node.parts.size > 0) {
            for (NodePart nodePart : node.parts) {
                if (nodePart.enabled) renderables.add(getRenderable(pool.obtain(), node, nodePart));
            }
        }

        for (Node child : node.getChildren()) {
            getRenderables(child, renderables, pool);
        }
    }
}
