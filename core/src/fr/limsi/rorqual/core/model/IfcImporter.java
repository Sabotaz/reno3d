package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.math.Vector2;

import java.util.Collection;
import java.util.HashMap;

import fr.limsi.rorqual.core.model.utils.Coin;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;

/**
 * Created by ricordeau on 21/10/15.
 */
public class IfcImporter {

    /*** Mis en place du singleton ***/
    private IfcImporter(){
    }

    private static class IfcImporterHolder
    {
        private final static IfcImporter INSTANCE = new IfcImporter();
    }

    public static synchronized IfcImporter getInstance() {
        return IfcImporterHolder.INSTANCE;
    }

    /*** Méthodes ***/
    public void realiseImportIfc(){
        IfcHelper.getInstance().loadIfcModel();
        this.loadMurs();
    }

    private void loadMurs(){
        Collection<IfcWallStandardCase> tabMurs = IfcHelper.getInstance().getAllWalls();
        for(IfcWallStandardCase wall : tabMurs){
            float depth = IfcHelper.getInstance().getWallThickness(wall);
            HashMap<String,String> hm = IfcHelper.getInstance().get3DRenoProperties(wall);
            if (hm.containsKey(Propertie.COINS.getName())){
                String value = hm.get(Propertie.COINS.getName());
                String[] parts = value.split("\\$");
                float xA = Float.valueOf(parts[0]);
                float yA = Float.valueOf(parts[1]);
                float xB = Float.valueOf(parts[2]);
                float yB = Float.valueOf(parts[3]);
                int etage = Integer.valueOf(parts[4]);
                Vector2 vecA = new Vector2(xA,yA);
                Vector2 vecB = new Vector2(xB,yB);
                Coin coinA = Coin.getCoin(etage,vecA);
                Coin coinB = Coin.getCoin(etage,vecB);
                Mur mur = new Mur(coinA,coinB,depth);
            }else{
                // On a pas les coins, on fait quoi ? En gros, ce n'est pas notre save que l'on essai de lire ...
            }
        }
    }
}
