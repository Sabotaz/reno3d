package fr.limsi.rorqual.core.dpe;

import com.badlogic.gdx.graphics.Color;

import java.util.HashMap;

import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventListener;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.event.EventType;
import scene3d.Actor3d;
import scene3d.Stage3d;

/**
 * Created by christophe on 04/06/15.
 */
public class DpeStateUpdater implements EventListener {

    public enum State {
        KNOWN,
        UNKNOWN,
        GUESSED,
        NONE,
        ;
    }

    HashMap<Object, State> states = new HashMap<Object, State>();
    Stage3d stage;

    public DpeStateUpdater(Stage3d s) {
        stage = s;
        EventManager.getInstance().addListener(Channel.DPE, this);
    }

    public void setState(Object o, State s) {
        states.put(o, s);
    }

    public State getState(Object o) {
        if (states.containsKey(o)) return states.get(o);
        else return State.NONE;
    }
    public void notify(Channel c, Event e) {

        EventType eventType = e.getEventType();
        if (c == Channel.DPE)
            if (eventType instanceof DpeEvent) {
                DpeEvent event = (DpeEvent) eventType;
                Object o = e.getUserObject();
                switch (event) {
                    case DPE_STATE_CHANGED:
                        Object items[] = (Object[]) o;
                        setState(items[0], (State)items[1]);
                        Actor3d actor = stage.getFromUserObject(items[0]);
                        switch ((State)items[1]) {
                            case UNKNOWN:
                                actor.setColor(Color.RED);
                                break;
                            case GUESSED:
                                actor.setColor(Color.YELLOW);
                                break;
                            case KNOWN:
                            default:
                                actor.setColor(Color.WHITE);
                                break;
                        }
                    break;
                }
            }
    }

}
