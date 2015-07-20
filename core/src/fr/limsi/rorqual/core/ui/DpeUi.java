package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.utils.AssetManager;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDoor;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWindow;

/**
 * Created by christophe on 03/06/15.
 */

public class DpeUi  {

    private Stage stage;
    private AssetManager assets;

    public DpeUi(Stage stage2d) {
        stage = stage2d;
        assets = AssetManager.getInstance();
    }

    public Actor getPropertyWindow(Object o) {
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
        if(o.equals(DpeEvent.START_DPE)){
            Actor a = Layout.fromJson("data/ui/layout/informationsGenerales.json", null).getRoot();
            return a;
        }
        return null;
    }
}
