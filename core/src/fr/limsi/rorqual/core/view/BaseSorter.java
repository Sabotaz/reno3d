package fr.limsi.rorqual.core.view;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderableSorter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

/**
 * Created by christophe on 01/09/15.
 */
// Classe permétant de trier les renderables suivant la distance à la caméra
// utile pour ordonner les renderables transparents
public class BaseSorter implements RenderableSorter, Comparator<Renderable> {
    private Camera camera;
    private final Vector3 tmpV1 = new Vector3();
    private final Vector3 tmpV2 = new Vector3();

    @Override
    public void sort(final Camera camera, final Array<Renderable> renderables) {
        this.camera = camera;
        renderables.sort(this);
    }

    @Override
    public int compare(final Renderable o1, final Renderable o2) {
        // FIXME implement better sorting algorithm
        // final boolean same = o1.shader == o2.shader && o1.mesh == o2.mesh && (o1.lights == null) == (o2.lights == null) &&
        // o1.material.equals(o2.material);
        o1.worldTransform.getTranslation(tmpV1);
        o2.worldTransform.getTranslation(tmpV2);
        final float dst = (int) (1000f * camera.position.dst2(tmpV1)) - (int) (1000f * camera.position.dst2(tmpV2));
        final int result = dst < 0 ? -1 : (dst > 0 ? 1 : 0);
        return result;
    }
}