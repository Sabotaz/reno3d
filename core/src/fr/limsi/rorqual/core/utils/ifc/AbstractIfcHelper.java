package fr.limsi.rorqual.core.utils.ifc;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import fr.limsi.rorqual.core.model.Etage;
import fr.limsi.rorqual.core.model.Fenetre;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Porte;
import fr.limsi.rorqual.core.model.PorteFenetre;
import fr.limsi.rorqual.core.model.Slab;
import fr.limsi.rorqual.core.model.utils.Coin;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifc2x3tc1.LIST;

/**
 * Created by christophe on 26/01/16.
 */
public abstract class AbstractIfcHelper {

    public static class WallContainer {
        Object wall;
        public WallContainer(Object w) {
            wall = w;
        }
    }

    public abstract void addBuildingStorey(String name, float elevation);

    public abstract WallContainer loadWall(Etage e,Mur m);

    public abstract void loadSlab(Etage e, Slab s);

    public abstract void loadPorte(Porte p, WallContainer w);

    public abstract void loadFenetre(Fenetre f, WallContainer w);

    public abstract void loadPorteFenetre(PorteFenetre pf, WallContainer w);

    public abstract void initialiseIfcModel();

    public abstract void saveIfcModel(String filename);
}
