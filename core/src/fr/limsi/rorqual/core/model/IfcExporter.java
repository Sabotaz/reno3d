package fr.limsi.rorqual.core.model;

import com.badlogic.gdx.math.Vector2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import fr.limsi.rorqual.core.model.utils.Coin;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifc2x3tc1.LIST;

/**
 * Created by ricordeau on 02/10/15.
 */
public class IfcExporter {
    /*** Attributs ***/

    /*** Mis en place du singleton ***/
    private IfcExporter(){
    }

    private static class IfcExporterHolder
    {
        private final static IfcExporter INSTANCE = new IfcExporter();
    }

    public static synchronized IfcExporter getInstance() {
        return IfcExporterHolder.INSTANCE;
    }

    /*** Méthodes ***/
    public void realiseExportIfc(){
        IfcHelper.getInstance().initialiseIfcModel();
        this.loadAllModels();
        this.saveFile();
    }
    private void loadAllModels(){
        ArrayList<Etage> etageTab = ModelHolder.getInstance().getBatiment().getAllEtages();
        for(Etage e:etageTab){
            IfcHelper.getInstance().addBuildingStorey(e.getName(),e.getElevation());
            for (Mur m:e.getMurs()){
                IfcWallStandardCase wall = this.loadWall(e, m);
                for(Ouverture o:m.getOuvertures()){
                    if(o instanceof Porte){
                        this.loadPorte((Porte)o,wall);
                    }
                }
            }
            for (Slab s:e.getSlabs()){
                this.loadSlab(e, s);
            }
        }
    }
    private IfcWallStandardCase loadWall(Etage e,Mur m){
        Vector2 a = m.getA().getPosition();
        Vector2 b = m.getB().getPosition();
        IfcCartesianPoint pointA1 = IfcHelper.getInstance().createCartesianPoint2D(a.x, a.y);
        IfcCartesianPoint pointA2 = IfcHelper.getInstance().createCartesianPoint2D(b.x, b.y);
        return(IfcHelper.getInstance().addWall(e.getName(),"Mur",pointA1,pointA2,m.getDepth(),e.getHeight()));
    }
    private void loadSlab(Etage e, Slab s){
        LIST<IfcCartesianPoint> listSlabCartesianPoint = new LIST<IfcCartesianPoint>();
        ArrayList<Coin> coins = new ArrayList<Coin>(s.getCoins());
        for (Coin c:coins){
            IfcCartesianPoint point = IfcHelper.getInstance().createCartesianPoint2D(c.getPosition().x, c.getPosition().y);
            listSlabCartesianPoint.add(point);
        }
        IfcHelper.getInstance().addSlab(e.getName(),listSlabCartesianPoint);
    }
    private void loadPorte(Porte p,IfcWallStandardCase wall){
        IfcHelper.getInstance().addDoor("Porte",wall,p.getWidth(),p.getHeight(),p.getPosition().x,p.getY());
    }
    private void saveFile(){
        IfcHelper.getInstance().saveIfcModel();
    }
}
