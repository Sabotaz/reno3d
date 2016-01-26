package fr.limsi.rorqual.core.utils.ifc;

import java.util.ArrayList;

import fr.limsi.rorqual.core.model.Etage;
import fr.limsi.rorqual.core.model.Fenetre;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Ouverture;
import fr.limsi.rorqual.core.model.Porte;
import fr.limsi.rorqual.core.model.PorteFenetre;
import fr.limsi.rorqual.core.model.Slab;

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

    AbstractIfcHelper helper;

    /*** Méthodes ***/
    public void realiseExportIfc(String filename){
        helper = Ifc2x3JavaToolboxHelper.getInstance();
        helper.initialiseIfcModel();
        this.loadAllModels();
        this.saveFile(filename);
    }

    private void loadAllModels(){
        ArrayList<Etage> etageTab = ModelHolder.getInstance().getBatiment().getAllEtages();
        for(Etage e:etageTab){
            helper.addBuildingStorey(e.getName(), e.getElevation());
            for (Mur m:e.getMurs()){
                AbstractIfcHelper.WallContainer wall =  helper.loadWall(e, m);
                for(Ouverture o:m.getOuvertures()){
                    if(o instanceof Porte){
                        helper.loadPorte((Porte) o, wall);
                    }
                    else if (o instanceof Fenetre){
                        helper.loadFenetre((Fenetre) o, wall);
                    }
                    else if (o instanceof PorteFenetre){
                        helper.loadPorteFenetre((PorteFenetre) o, wall);
                    }
                }
            }
            for (Slab s:e.getSlabs()){
                helper.loadSlab(e, s);
            }
        }
    }


    private void saveFile(String filename){
        helper.saveIfcModel(filename);
    }
}
