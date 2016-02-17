package ifc2x3utils;

import java.io.File;

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

    public abstract WallContainer loadWall(String etage_name, int etage_number, float mur_ax, float mur_ay, float mur_bx, float mur_by, float mur_depth, float mur_height);

    public abstract void loadSlab(String etage_name, float[][] coins);

    public abstract void loadPorte(float porte_width, float porte_height, float porte_x, float porte_y, WallContainer w);

    public abstract void loadFenetre(float fenetre_width, float fenetre_height, float fenetre_x, float fenetre_y, WallContainer w);

    public abstract void loadPorteFenetre(float portefenetre_width, float portefenetre_height, float portefenetre_x, float portefenetre_y, WallContainer w);

    public abstract void initialiseIfcModel();

    public abstract void saveIfcModel(File saveStepFile);

}
