package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

import java.util.HashMap;

import fr.limsi.rorqual.core.dpe.enums.DpePropertiesEnum;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.model.Fenetre;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Objet;
import fr.limsi.rorqual.core.model.Porte;
import fr.limsi.rorqual.core.model.PorteFenetre;
import fr.limsi.rorqual.core.model.Slab;

/**
 * Created by christophe on 03/06/15.
 */
// Classe permetant le chargement des fenètres pour le DPE, suivant l'élément
public class DpeUi  {

    private static HashMap<Object,Layout> sauvegarde_layout = new HashMap<Object,Layout>();

    public static Actor getPropertyWindow(Object o) {
//        if (o instanceof Porte) {
//            Actor a = Layout.fromJson("data/ui/layout/doorProperties.json", o).getRoot();
//            return a;
//        }
        System.out.println(o.toString()+"\n");
        if (o instanceof Fenetre || o instanceof PorteFenetre) {
            Actor a;
            if (!sauvegarde_layout.containsKey(o)){
                Layout l = Layout.fromJson("data/ui/layout/windowProperties.json", o);
                a = l.getRoot();
                sauvegarde_layout.put(o,l);
            }else{
                Layout l = sauvegarde_layout.get(o);
                a = l.getRoot();
            }
            return a;
        } else if (o instanceof Porte) {
            Actor a;
            if (!sauvegarde_layout.containsKey(o)){
                Layout l = Layout.fromJson("data/ui/layout/doorProperties.json", o);
                a = l.getRoot();
                sauvegarde_layout.put(o,l);
            }else{
                Layout l = sauvegarde_layout.get(o);
                a = l.getRoot();
            }
            return a;
        } else if (o instanceof Mur) {
            Actor a;
            if (!sauvegarde_layout.containsKey(o)){
                Layout l = Layout.fromJson("data/ui/layout/wallProperties.json", o);
                a = l.getRoot();
                sauvegarde_layout.put(o,l);
            }else{
                Layout l = sauvegarde_layout.get(o);
                a = l.getRoot();
            }
            return a;
        } else if (o instanceof Slab) {
            Actor a;
            if (!sauvegarde_layout.containsKey(o)){
                Layout l = Layout.fromJson("data/ui/layout/slabProperties.json", o);
                a = l.getRoot();
                sauvegarde_layout.put(o,l);
            }else{
                Layout l = sauvegarde_layout.get(o);
                a = l.getRoot();
            }
            return a;
        } else if (o instanceof Objet) {
            Actor a = makeObjetTab((Objet) o);
            return a;
        }

        else if(o == DpeEvent.INFOS_GENERALES){
            Actor a;
            if (!sauvegarde_layout.containsKey(o)){
                Layout l = Layout.fromJson("data/ui/layout/informationsGenerales.json", null);
                a = l.getRoot();
                sauvegarde_layout.put(o,l);
            }else{
                Layout l = sauvegarde_layout.get(o);
                a = l.getRoot();
            }
            return a;
        }

        else if(o == DpeEvent.INFOS_CHAUFFAGE){
            Actor a;
            if (!sauvegarde_layout.containsKey(o)){
                Layout l = Layout.fromJson("data/ui/layout/chauffageProperties.json", null);
                a = l.getRoot();
                sauvegarde_layout.put(o,l);
            }else{
                Layout l = sauvegarde_layout.get(o);
                a = l.getRoot();
            }
            return a;
        }

        return null;
    }

    public static void clear() {
        sauvegarde_layout.clear();
    }

    private static Table makeObjetTab(Objet o) {
        float width = new Value.Fixed(Gdx.graphics.getWidth() * 0.45f).get(null);
        TabWindow tabWindow = new TabWindow(width);
        System.out.println(o.getCategory());
        tabWindow.addTable(ModelLibrary.getInstance().getModelTable(o.getCategory()));
        return tabWindow;
    }
}
