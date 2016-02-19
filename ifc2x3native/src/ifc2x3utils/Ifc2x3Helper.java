package ifc2x3utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import core.IfcModel;
import core.IfcRules;

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

    IfcModel ifcModel;

    @Override
    public void addBuildingStorey(String name, float elevation) {
        ifcModel.STOREY(name, elevation);
    }

    @Override
    public WallContainer loadWall(String etage_name, int etage_number, float mur_ax, float mur_ay, float mur_bx, float mur_by, float mur_depth, float mur_height) {
        Object wall = ifcModel.WALL(etage_name, etage_number, mur_ax, mur_ay, mur_bx, mur_by, mur_depth, mur_height);
        return new WallContainer(wall);
    }

    @Override
    public void loadSlab(String etage_name, float[][] coins, float height) {
        ifcModel.SLAB(etage_name, coins, height);
    }

    @Override
    public void loadPorte(float porte_width, float porte_height, float porte_x, float porte_y, WallContainer w){
        ifcModel.DOOR(porte_width, porte_height, porte_x, porte_y, w.wall);
    }

    @Override
    public void loadFenetre(float fenetre_width, float fenetre_height, float fenetre_x, float fenetre_y, WallContainer w){
        ifcModel.WINDOW(fenetre_width, fenetre_height, fenetre_x, fenetre_y, w.wall);
    }

    @Override
    public void loadPorteFenetre(float portefenetre_width, float portefenetre_height, float portefenetre_x, float portefenetre_y, WallContainer w){
        ifcModel.WINDOWDOOR(portefenetre_width, portefenetre_height, portefenetre_x, portefenetre_y, w.wall);
    }

    @Override
    public void initialiseIfcModel() {
        ifcModel = new IfcModel();
    }

    // Permet d'exporter le model au format .ifc
    @Override
    public void saveIfcModel(File saveStepFile){
        try {
            FileOutputStream outputStream = new FileOutputStream(saveStepFile);
            ifcModel.setFilename(saveStepFile.getAbsolutePath());
            outputStream.write(ifcModel.toString().getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



