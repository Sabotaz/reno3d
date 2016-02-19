package fr.limsi.rorqual.core.utils.ifc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.util.ArrayList;

import fr.limsi.rorqual.core.model.Etage;
import fr.limsi.rorqual.core.model.Fenetre;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Ouverture;
import fr.limsi.rorqual.core.model.Porte;
import fr.limsi.rorqual.core.model.PorteFenetre;
import fr.limsi.rorqual.core.model.Slab;
import fr.limsi.rorqual.core.model.utils.Coin;
import ifc2x3utils.Ifc2x3Helper;

/**
 * Created by ricordeau on 02/10/15.
 */
public class IfcExporter {

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

    ifc2x3utils.AbstractIfcHelper helper;

    /*** Méthodes ***/
    public void realiseExportIfc(String filename){
        helper = Ifc2x3Helper.getInstance();
        helper.initialiseIfcModel();
        this.loadAllModels();
        this.saveFile(filename);
    }

    private void loadAllModels(){
        ArrayList<Etage> etageTab = ModelHolder.getInstance().getBatiment().getAllEtages();
        for(Etage e:etageTab){
            helper.addBuildingStorey(e.getName(), e.getElevation());
            for (Mur m:e.getMurs()){
                ifc2x3utils.AbstractIfcHelper.WallContainer wall =
                        helper.loadWall(
                                e.getName(),
                                e.getNumber(),
                                m.getA().getPosition().x,
                                m.getA().getPosition().y,
                                m.getB().getPosition().x,
                                m.getB().getPosition().y,
                                m.getDepth(),
                                e.getHeight());

                for(Ouverture o:m.getOuvertures()){
                    if(o instanceof Porte){
                        Porte p = (Porte)o;
                        helper.loadPorte(
                                p.getWidth(),
                                p.getHeight(),
                                p.getPosition().x,
                                p.getY(),
                                wall);
                    }
                    else if (o instanceof Fenetre){
                        Fenetre f = (Fenetre)o;
                        helper.loadFenetre(
                                f.getWidth(),
                                f.getHeight(),
                                f.getPosition().x,
                                f.getY(),
                                wall);
                    }
                    else if (o instanceof PorteFenetre){
                        PorteFenetre pf = (PorteFenetre)o;
                        helper.loadPorteFenetre(
                                pf.getWidth(),
                                pf.getHeight(),
                                pf.getPosition().x,
                                pf.getY(),
                                wall);
                    }
                }
            }
            for (Slab s:e.getSlabs()){
                float coins[][] = new float[s.getCoins().size()][2];
                int i = 0;
                for (Coin c : s.getCoins()) {
                    coins[i][0] = c.getPosition().x;
                    coins[i][1] = c.getPosition().y;
                    i++;
                }

                helper.loadSlab(e.getName(), coins, s.getHeight());
            }
        }
    }


    private void saveFile(String filename){
        FileHandle handle = Gdx.files.external(filename);
        File saveStepFile = handle.file();
        helper.saveIfcModel(saveStepFile);
    }
}
