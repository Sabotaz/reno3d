package fr.limsi.rorqual.core.model;
import com.badlogic.gdx.graphics.Texture;
import fr.limsi.rorqual.core.utils.AssetManager;
/**
 * Created by ricordeau on 02/10/15.
 */
public enum MaterialTypeEnum {
    BRIQUE("brickwork-texture", "brickwork_normal-map"),
    PIERRE("masonry-wall-texture", "masonry-wall-normal-map"),
    PARQUET("wood-floorboards-texture",null),
    BETON("beton",null),
    WALL1("wall_1",null),
    WALL2("wall_2",null),
    ;

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
        if (normal == null){
            return null;
        }
        else{
            return (Texture)AssetManager.getInstance().get(normal);
        }
    }
}

