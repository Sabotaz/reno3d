package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by christophe on 02/07/15.
 */
public class NinePatchSprite {

    public static final int TOP_LEFT = 0;
    public static final int TOP_CENTER = 1;
    public static final int TOP_RIGHT = 2;
    public static final int MIDDLE_LEFT = 3;
    public static final int MIDDLE_CENTER = 4;
    public static final int MIDDLE_RIGHT = 5;
    public static final int BOTTOM_LEFT = 6;
    /** Indices for {@link #NinePatch(TextureRegion...)} constructor */
    // alphabetically first in javadoc
    public static final int BOTTOM_CENTER = 7;
    public static final int BOTTOM_RIGHT = 8;

    private int bottomLeft = -1, bottomCenter = -1, bottomRight = -1;
    private int middleLeft = -1, middleCenter = -1, middleRight = -1;
    private int topLeft = -1, topCenter = -1, topRight = -1;

    private float leftWidth, rightWidth, middleWidth, middleHeight, topHeight, bottomHeight;
    private int idx;

    public float[] vertices = new float[9 * 4 * 5];


    public NinePatchSprite(Texture texture, int left, int right, int top, int bottom) {
        if (texture == null) throw new IllegalArgumentException("region cannot be null.");
        final int middleWidth = texture.getWidth()- left - right;
        final int middleHeight = texture.getHeight() - top - bottom;

        TextureRegion[] patches = new TextureRegion[9];
        if (top > 0) {
            if (left > 0) patches[TOP_LEFT] = new TextureRegion(texture, 0, top + middleHeight, left, bottom);
            if (middleWidth > 0) patches[TOP_CENTER] = new TextureRegion(texture, left, top + middleHeight, middleWidth, bottom);
            if (right > 0) patches[TOP_RIGHT] = new TextureRegion(texture, left + middleWidth, top + middleHeight, right, bottom);
        }
        if (middleHeight > 0) {
            if (left > 0) patches[MIDDLE_LEFT] = new TextureRegion(texture, 0, top, left, middleHeight);
            if (middleWidth > 0) patches[MIDDLE_CENTER] = new TextureRegion(texture, left, top, middleWidth, middleHeight);
            if (right > 0) patches[MIDDLE_RIGHT] = new TextureRegion(texture, left + middleWidth, top, right, middleHeight);
        }
        if (bottom > 0) {
            if (left > 0) patches[BOTTOM_LEFT] = new TextureRegion(texture, 0, 0, left, top);
            if (middleWidth > 0) patches[BOTTOM_CENTER] = new TextureRegion(texture, left, 0, middleWidth, top);
            if (right > 0) patches[BOTTOM_RIGHT] = new TextureRegion(texture, left + middleWidth, 0, right, top);
        }

        // If split only vertical, move splits from right to center.
        if (left == 0 && middleWidth == 0) {
            patches[TOP_CENTER] = patches[TOP_RIGHT];
            patches[MIDDLE_CENTER] = patches[MIDDLE_RIGHT];
            patches[BOTTOM_CENTER] = patches[BOTTOM_RIGHT];
            patches[TOP_RIGHT] = null;
            patches[MIDDLE_RIGHT] = null;
            patches[BOTTOM_RIGHT] = null;
        }
        // If split only horizontal, move splits from bottom to center.
        if (top == 0 && middleHeight == 0) {
            patches[MIDDLE_LEFT] = patches[BOTTOM_LEFT];
            patches[MIDDLE_CENTER] = patches[BOTTOM_CENTER];
            patches[MIDDLE_RIGHT] = patches[BOTTOM_RIGHT];
            patches[BOTTOM_LEFT] = null;
            patches[BOTTOM_CENTER] = null;
            patches[BOTTOM_RIGHT] = null;
        }

        load(patches);
    }


    private void load (TextureRegion[] patches) {

        if (patches[BOTTOM_LEFT] != null) {
            bottomLeft = add(patches[BOTTOM_LEFT], false, false);
            leftWidth = patches[BOTTOM_LEFT].getRegionWidth();
            bottomHeight = patches[BOTTOM_LEFT].getRegionHeight();
        }
        if (patches[BOTTOM_CENTER] != null) {
            bottomCenter = add(patches[BOTTOM_CENTER], true, false);
            middleWidth = Math.max(middleWidth, patches[BOTTOM_CENTER].getRegionWidth());
            bottomHeight = Math.max(bottomHeight, patches[BOTTOM_CENTER].getRegionHeight());
        }
        if (patches[BOTTOM_RIGHT] != null) {
            bottomRight = add(patches[BOTTOM_RIGHT], false, false);
            rightWidth = Math.max(rightWidth, patches[BOTTOM_RIGHT].getRegionWidth());
            bottomHeight = Math.max(bottomHeight, patches[BOTTOM_RIGHT].getRegionHeight());
        }
        if (patches[MIDDLE_LEFT] != null) {
            middleLeft = add(patches[MIDDLE_LEFT], false, true);
            leftWidth = Math.max(leftWidth, patches[MIDDLE_LEFT].getRegionWidth());
            middleHeight = Math.max(middleHeight, patches[MIDDLE_LEFT].getRegionHeight());
        }
        if (patches[MIDDLE_CENTER] != null) {
            middleCenter = add(patches[MIDDLE_CENTER], true, true);
            middleWidth = Math.max(middleWidth, patches[MIDDLE_CENTER].getRegionWidth());
            middleHeight = Math.max(middleHeight, patches[MIDDLE_CENTER].getRegionHeight());
        }
        if (patches[MIDDLE_RIGHT] != null) {
            middleRight = add(patches[MIDDLE_RIGHT], false, true);
            rightWidth = Math.max(rightWidth, patches[MIDDLE_RIGHT].getRegionWidth());
            middleHeight = Math.max(middleHeight, patches[MIDDLE_RIGHT].getRegionHeight());
        }
        if (patches[TOP_LEFT] != null) {
            topLeft = add(patches[TOP_LEFT], false, false);
            leftWidth = Math.max(leftWidth, patches[TOP_LEFT].getRegionWidth());
            topHeight = Math.max(topHeight, patches[TOP_LEFT].getRegionHeight());
        }
        if (patches[TOP_CENTER] != null) {
            topCenter = add(patches[TOP_CENTER], true, false);
            middleWidth = Math.max(middleWidth, patches[TOP_CENTER].getRegionWidth());
            topHeight = Math.max(topHeight, patches[TOP_CENTER].getRegionHeight());
        }
        if (patches[TOP_RIGHT] != null) {
            topRight = add(patches[TOP_RIGHT], false, false);
            rightWidth = Math.max(rightWidth, patches[TOP_RIGHT].getRegionWidth());
            topHeight = Math.max(topHeight, patches[TOP_RIGHT].getRegionHeight());
        }
        if (idx < vertices.length) {
            float[] newVertices = new float[idx];
            System.arraycopy(vertices, 0, newVertices, 0, idx);
            vertices = newVertices;
        }
    }

    private Texture texture;
    private int add (TextureRegion region, boolean isStretchW, boolean isStretchH) {
        if (texture == null)
            texture = region.getTexture();
        else if (texture != region.getTexture()) //
            throw new IllegalArgumentException("All regions must be from the same texture.");

        float u = region.getU();
        float v = region.getV();
        float u2 = region.getU2();
        float v2 = region.getV2();

        // Add half pixel offsets on stretchable dimensions to avoid color bleeding when GL_LINEAR
        // filtering is used for the texture. This nudges the texture coordinate to the center
        // of the texel where the neighboring pixel has 0% contribution in linear blending mode.
        if (isStretchW) {
            float halfTexelWidth = 0.5f * 1.0f / texture.getWidth();
            u += halfTexelWidth;
            u2 -= halfTexelWidth;
        }
        if (isStretchH) {
            float halfTexelHeight = 0.5f * 1.0f / texture.getHeight();
            v -= halfTexelHeight;
            v2 += halfTexelHeight;
        }

        final float[] vertices = this.vertices;

        idx += 3;
        vertices[idx++] = u;
        vertices[idx++] = v;
        idx += 3;
        vertices[idx++] = u;
        vertices[idx++] = v2;
        idx += 3;
        vertices[idx++] = u2;
        vertices[idx++] = v2;
        idx += 3;
        vertices[idx++] = u2;
        vertices[idx++] = v;

        return idx - 4 * 5;
    }

    public void setSize(float x, float y, float width, float height) {
        final float centerColumnX = x + leftWidth;
        final float rightColumnX = x + width - rightWidth;
        final float middleRowY = y + bottomHeight;
        final float topRowY = y + height - topHeight;

        if (bottomLeft != -1) set(bottomLeft, x, y, centerColumnX - x, middleRowY - y);
        if (bottomCenter != -1) set(bottomCenter, centerColumnX, y, rightColumnX - centerColumnX, middleRowY - y);
        if (bottomRight != -1) set(bottomRight, rightColumnX, y, x + width - rightColumnX, middleRowY - y);
        if (middleLeft != -1) set(middleLeft, x, middleRowY, centerColumnX - x, topRowY - middleRowY);
        if (middleCenter != -1)
            set(middleCenter, centerColumnX, middleRowY, rightColumnX - centerColumnX, topRowY - middleRowY);
        if (middleRight != -1) set(middleRight, rightColumnX, middleRowY, x + width - rightColumnX, topRowY - middleRowY);
        if (topLeft != -1) set(topLeft, x, topRowY, centerColumnX - x, y + height - topRowY);
        if (topCenter != -1) set(topCenter, centerColumnX, topRowY, rightColumnX - centerColumnX, y + height - topRowY);
        if (topRight != -1) set(topRight, rightColumnX, topRowY, x + width - rightColumnX, y + height - topRowY);
    }

    /** Set the coordinates of a ninth of the patch. */
    private void set (int idx, float x, float y, float width, float height) {
        final float fx2 = x + width;
        final float fy2 = y + height;
        final float[] vertices = this.vertices;
        vertices[idx++] = x;
        vertices[idx++] = y;
        vertices[idx++] = 0; //z
        idx += 2;
        vertices[idx++] = x;
        vertices[idx++] = fy2;
        vertices[idx++] = 0; //z
        idx += 2;
        vertices[idx++] = fx2;
        vertices[idx++] = fy2;
        vertices[idx++] = 0; //z
        idx += 2;
        vertices[idx++] = fx2;
        vertices[idx++] = y;
        vertices[idx] = 0; //z
    }
}
