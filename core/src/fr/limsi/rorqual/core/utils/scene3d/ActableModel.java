package fr.limsi.rorqual.core.utils.scene3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import fr.limsi.rorqual.core.utils.Holder;

/**
 * Created by christophe on 20/07/15.
 */
public abstract class ActableModel extends Model implements RenderableProvider {

    public Matrix4 local_transform = new Matrix4().idt();
    public Matrix4 model_transform = new Matrix4().idt();
    protected Matrix4 world_transform = new Matrix4().idt();

    public Object userData = null;

    boolean renderTransparent = false;

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
        if (nodePart.bones == null && world_transform != null) {
            out.worldTransform.set(world_transform).mul(node.globalTransform);
        }
        else if (world_transform != null)
            out.worldTransform.set(world_transform);
        else
            out.worldTransform.idt();
        out.userData = userData;
        return out;
    }

    protected void getRenderables (Node node, Array<Renderable> renderables, Pool<Renderable> pool) {
        if (node.parts.size > 0) {
            for (NodePart nodePart : node.parts) {

                boolean isBlended = false;

                if(nodePart.material.get(BlendingAttribute.Type) != null)
                    isBlended = ((BlendingAttribute) nodePart.material.get(BlendingAttribute.Type)).blended && ((BlendingAttribute) nodePart.material.get(BlendingAttribute.Type)).opacity != 1.f;

                if(nodePart.material.get(TextureAttribute.Diffuse) != null) {
                    Pixmap.Format format = ((TextureAttribute) nodePart.material.get(TextureAttribute.Diffuse)).textureDescription.texture.getTextureData().getFormat();
                    isBlended = (format == Pixmap.Format.RGBA4444
                            || format == Pixmap.Format.RGBA8888
                            || format == Pixmap.Format.LuminanceAlpha
                            || format == Pixmap.Format.Alpha
                            || format == Pixmap.Format.Intensity);
                }

                if (nodePart.enabled && (isBlended == renderTransparent)) renderables.add(getRenderable(pool.obtain(), node, nodePart));
            }
        }

        for (Node child : node.getChildren()) {
            getRenderables(child, renderables, pool);
        }
    }

    public void setModel(Model model) {
        this.dispose();
        this.materials.clear();
        this.meshes.clear();
        this.meshParts.clear();
        this.nodes.clear();
        if (model != null)
            copyNodes(model.nodes);
        this.animations.clear();
        if (model != null)
            this.animations.addAll(model.animations);
    }

    public void addModel(Model model) {
        copyNodes(model.nodes);
    }

    protected void copyNodes (Array<Node> nodes) {
        for (int i = 0, n = nodes.size; i < n; ++i) {
            final Node node = nodes.get(i);
            this.nodes.add(copyNode(node));
        }
    }

    private Node copyNode (Node node) {
        Node copy = new Node();
        copy.id = node.id;
        copy.inheritTransform = node.inheritTransform;
        copy.translation.set(node.translation);
        copy.rotation.set(node.rotation);
        copy.scale.set(node.scale);
        copy.localTransform.set(node.localTransform);
        copy.globalTransform.set(node.globalTransform);
        for (NodePart nodePart : node.parts) {
            copy.parts.add(copyNodePart(nodePart));
        }
        for (Node child : node.getChildren()) {
            copy.addChild(copyNode(child));
        }
        return copy;
    }

    private NodePart copyNodePart (NodePart nodePart) {
        NodePart copy = new NodePart();
        copy.meshPart = new MeshPart();
        copy.meshPart.id = nodePart.meshPart.id;
        copy.meshPart.indexOffset = nodePart.meshPart.indexOffset;
        copy.meshPart.numVertices = nodePart.meshPart.numVertices;
        copy.meshPart.primitiveType = nodePart.meshPart.primitiveType;
        copy.meshPart.mesh = nodePart.meshPart.mesh;
        meshParts.add(copy.meshPart);
        meshes.add(copy.meshPart.mesh);

        final int index = materials.indexOf(nodePart.material, false);
        if (index < 0)
            materials.add(copy.material = nodePart.material.copy());
        else
            copy.material = materials.get(index);

        return copy;
    }
}

