package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import java.util.HashMap;

import fr.limsi.rorqual.core.event.ButtonValue;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventListener;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.event.EventRequest;
import fr.limsi.rorqual.core.event.UiEvent;
import fr.limsi.rorqual.core.logic.Logic;

/**
 * Created by christophe on 28/07/15.
 */
public class MainUiControleur implements EventListener {

    Actor tb = null;
    Stage stage = null;

    private MainUiControleur() {
        EventManager.getInstance().addListener(Channel.UI, this);
    }

    /** Holder */
    private static class MainUiControleurHolder
    {
        /** Instance unique non préinitialisée */
        private final static MainUiControleur INSTANCE = new MainUiControleur();
    }

    public static synchronized MainUiControleur getInstance() {
        return MainUiControleurHolder.INSTANCE;
    }


    public void setStage(Stage s) {
        stage = s;
    }

    public Stage getStage() {
        return stage;
    }

    public void removeTb() {
        if (tb != null)
            tb.remove();
    }

    public void addTb(Actor actor) {
        removeTb();
        tb = actor;
        if (tb != null) {
            synchronized (stage) {
                tb.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 100);
                stage.addActor(tb);
            }
        }
    }

    @Override
    public void notify(Channel c, Event e) throws InterruptedException {
        if (c == Channel.UI) {
            if (e.getEventType() == UiEvent.BUTTON_CLICKED) {

                HashMap<String,Object> items = (HashMap<String,Object>) e.getUserObject();
                if (items.get("eventRequest") == EventRequest.GET_STATE) {
                    // FIXME: disable this case (no state to get)
                    HashMap<String,Object> response = new HashMap<String,Object>();
                    response.put("lastValue",null);
                    response.put("userObject",null);
                    response.put("eventRequest", EventRequest.CURRENT_STATE);
                    Event e2 = new Event(UiEvent.BUTTON_CLICKED, response);
                    EventManager.getInstance().put(Channel.UI, e2);

                } else if (items.get("eventRequest") == EventRequest.UPDATE_STATE) {
                    ButtonValue lastValue = (ButtonValue) items.get("lastValue");
                    switch (lastValue) {
                        case EXIT:
                            Gdx.app.exit();
                            break;
                        case MUR:
                            if (((Button) items.get("button")).isChecked())
                                Logic.getInstance().startWall();
                            else
                                Logic.getInstance().stop();
                            break;
                        case FENETRE:
                            if (((Button) items.get("button")).isChecked())
                                Logic.getInstance().startFenetre();
                            else
                                Logic.getInstance().stop();
                            break;
                        case PORTE:
                            if (((Button) items.get("button")).isChecked())
                                Logic.getInstance().startPorte();
                            else
                                Logic.getInstance().stop();
                            break;
                        case DPE:
                            addTb(DpeUi.getPropertyWindow(DpeEvent.INFOS_GENERALES));
                            break;
                        case CHAUFFAGE:
                            addTb(DpeUi.getPropertyWindow(DpeEvent.INFOS_CHAUFFAGE));
                            break;
                        case ECS:
                            System.out.println("coucou");
                            break;
                        case ELECTROMENAGER:
                            System.out.println("coucou");
                            break;
                        default:
                            System.out.println(lastValue);
                    }
                }
            }
        }
    }
}
