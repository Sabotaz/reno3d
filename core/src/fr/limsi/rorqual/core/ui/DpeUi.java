package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.HashMap;

import fr.limsi.rorqual.core.dpe.enums.DpePropertiesEnum;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.model.Fenetre;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Objet;
import fr.limsi.rorqual.core.model.Porte;
import fr.limsi.rorqual.core.model.PorteFenetre;
import fr.limsi.rorqual.core.model.Slab;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDoor;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWindow;

/**
 * Created by christophe on 03/06/15.
 */
// Classe permetant le chargement des fenètres pour le DPE, suivant l'élément
public class DpeUi  {

    private static HashMap<DpePropertiesEnum,Layout> sauvegarde_layout = new HashMap<DpePropertiesEnum,Layout>();

    public static Actor getPropertyWindow(Object o) {
//        if (o instanceof Porte) {
//            Actor a = Layout.fromJson("data/ui/layout/doorProperties.json", o).getRoot();
//            return a;
//        }
        System.out.println(o.toString()+"\n");
        if (o instanceof Fenetre || o instanceof PorteFenetre) {
            Actor a = Layout.fromJson("data/ui/layout/windowProperties.json", o).getRoot();
            return a;
        } else if (o instanceof Mur) {
            Actor a = Layout.fromJson("data/ui/layout/wallProperties.json", o).getRoot();
            return a;
        } else if (o instanceof Slab) {
            Actor a = Layout.fromJson("data/ui/layout/slabProperties.json", o).getRoot();
            return a;
        } else if (o instanceof Objet) {
            Actor a = makeObjetTab((Objet) o);
            return a;
        }

        else if(o == DpeEvent.INFOS_GENERALES){
            Actor a;
            if (!sauvegarde_layout.containsKey(DpePropertiesEnum.GENERAL)){
                Layout l = Layout.fromJson("data/ui/layout/informationsGenerales.json", null);
                a = l.getRoot();
                sauvegarde_layout.put(DpePropertiesEnum.GENERAL,l);
            }else{
                Layout l = sauvegarde_layout.get(DpePropertiesEnum.GENERAL);
                a = l.getRoot();
            }
            return a;
        } else if(o == DpeEvent.INFOS_CHAUFFAGE){
            Actor a;
            if (!sauvegarde_layout.containsKey(DpePropertiesEnum.CHAUFFAGE)){
                Layout l = Layout.fromJson("data/ui/layout/chauffageProperties.json", null);
                a = l.getRoot();
                sauvegarde_layout.put(DpePropertiesEnum.CHAUFFAGE,l);
            }else{
                Layout l = sauvegarde_layout.get(DpePropertiesEnum.CHAUFFAGE);
                a = l.getRoot();
            }
            return a;
        }
        return null;
    }

    private static Table makeObjetTab(Objet o) {
        TabWindow tabWindow = new TabWindow(450);
        System.out.println(o.getCategory());
        tabWindow.addTable(ModelLibrary.getInstance().getModelTable(o.getCategory()));
        return tabWindow;
    }
}
