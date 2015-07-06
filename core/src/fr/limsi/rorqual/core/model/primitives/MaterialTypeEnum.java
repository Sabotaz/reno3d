package fr.limsi.rorqual.core.model.primitives;

import com.badlogic.gdx.graphics.Texture;

import fr.limsi.rorqual.core.utils.AssetManager;

/**
 * Created by ricordeau on 03/07/15.
 */
public enum MaterialTypeEnum {
    BRIQUE("brickwork-texture", "brickwork_normal-map"),
    PIERRE("masonry-wall-texture", "masonry-wall-normal-map");

    private String diffuse;
    private String normal;

    private MaterialTypeEnum(String d, String n) {
        diffuse = d;
        normal = n;
    }

    public Texture getDiffuse() {
        return (Texture)AssetManager.getInstance().get(diffuse);
    }

    public Texture getNormal() {
        return (Texture)AssetManager.getInstance().get(normal);
    }
}