package ifc2x3utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by ricordeau on 08/04/2015.
 */
public class Ifc2x3Helper extends AbstractIfcHelper {

    /*** Mis en place du singleton ***/
    private Ifc2x3Helper(){

    }
    private static class IfcHelperHolder
    {
        private final static Ifc2x3Helper INSTANCE = new Ifc2x3Helper();
    }

    public static synchronized Ifc2x3Helper getInstance() {
        return IfcHelperHolder.INSTANCE;
    }

    @Override
    public void addBuildingStorey(String name, float elevation) {
    }

    @Override
    public WallContainer loadWall(String etage_name, int etage_number, float mur_ax, float mur_ay, float mur_bx, float mur_by, float mur_depth, float mur_height) {
        return new WallContainer(null);
    }

    @Override
    public void loadSlab(String etage_name, float[][] coins) {
    }

    @Override
    public void loadPorte(float porte_width, float porte_height, float porte_x, float porte_y, WallContainer w){
    }

    @Override
    public void loadFenetre(float fenetre_width, float fenetre_height, float fenetre_x, float fenetre_y, WallContainer w){
    }

    @Override
    public void loadPorteFenetre(float portefenetre_width, float portefenetre_height, float portefenetre_x, float portefenetre_y, WallContainer w){
    }

    @Override
    public void initialiseIfcModel() {
    }

    // Permet d'exporter le model au format .ifc
    @Override
    public void saveIfcModel(File saveStepFile){
    }
}



