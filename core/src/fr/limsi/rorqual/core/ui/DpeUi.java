package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.concurrent.Semaphore;

import fr.limsi.rorqual.core.dpe.DpeEvent;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventListener;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.event.EventType;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWallStandardCase;

/**
 * Created by christophe on 03/06/15.
 */
public class DpeUi implements EventListener {

    private Skin _skin;
    private Stage _stage;

    public DpeUi(Stage stage) {
        EventManager.getInstance().addListener(Channel.DPE, this);
        _skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));
        _stage = stage;
    }
    Semaphore s = new Semaphore(1,true);

    public void notify(Channel c, Event e) {
        try {
            EventType eventType = e.getEventType();
            if (c == Channel.DPE)
                if (eventType instanceof DpeEvent) {
                    DpeEvent event = (DpeEvent) eventType;
                    Object o = e.getUserObject();
                    switch (event) {
                        case DERRIERE_MUR:

                            s.acquire();

                            final IfcWallStandardCase wall = (IfcWallStandardCase) o;


                            String name = wall.getName().getDecodedValue();

                            Dialog dialog = new Dialog(" Qu'est-ce qu'il y a derriere le mur : " + name, _skin, "dialog") {
                                protected void result(Object object) {
                                    String derriere = "";
                                    if (object.equals(1)) {
                                        derriere = "ext";
                                    } else if (object.equals(2)) {
                                        derriere = "int";
                                    } else if (object.equals(3)) {
                                        derriere = "lnc";
                                    } else if (object.equals(4)) {
                                        derriere = "ah";
                                    } else if (object.equals(5)) {
                                        derriere = "ver";
                                    }

                                    DpeEvent responseType = DpeEvent.DERRIERE_MUR_RESPONSE;
                                    Object items[] = {wall, derriere};
                                    Event response = new Event(responseType, items);

                                    EventManager.getInstance().put(Channel.DPE, response);

                                    s.release();
                                }
                            }.button("Exterieur", 1).button("Interieur", 2).button("Local non chauffe", 3).button("Autre habitation", 4).button("Veranda", 5).show(_stage);
                            dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight() - 10));
                    }
                }
        } catch (InterruptedException ie) {

        }
    }

}
