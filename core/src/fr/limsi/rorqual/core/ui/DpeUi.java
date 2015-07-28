package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.HashMap;

import fr.limsi.rorqual.core.dpe.enums.DpePropertiesEnum;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.utils.AssetManager;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDoor;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWindow;

/**
 * Created by christophe on 03/06/15.
 */

public class DpeUi  {

    private static HashMap<DpePropertiesEnum,Layout> sauvegarde_layout = new HashMap<DpePropertiesEnum,Layout>();

    public static Actor getPropertyWindow(Object o) {
        if (o instanceof IfcWallStandardCase) {
            Actor a = Layout.fromJson("data/ui/layout/wallProperties.json", o).getRoot();
            return a;
        }
        if (o instanceof IfcWindow) {
            Actor a = Layout.fromJson("data/ui/layout/windowProperties.json", o).getRoot();
            return a;
        }
        if (o instanceof IfcDoor) {
            Actor a = Layout.fromJson("data/ui/layout/doorProperties.json", o).getRoot();
            return a;
        }
        if(o == DpeEvent.INFOS_GENERALES){
            Actor a = null;
            if (!sauvegarde_layout.containsKey(DpePropertiesEnum.GENERAL)){
                Layout l = Layout.fromJson("data/ui/layout/informationsGenerales.json", null);
                a = l.getRoot();
                sauvegarde_layout.put(DpePropertiesEnum.GENERAL,l);
            }else{
                Layout l = sauvegarde_layout.get(DpePropertiesEnum.GENERAL);
                a = l.getRoot();
            }
            return a;
        }
        if(o == DpeEvent.INFOS_CHAUFFAGE){
            Actor a = Layout.fromJson("data/ui/layout/chauffageProperties.json", null).getRoot();
            return a;
        }
        return null;
    }
}
