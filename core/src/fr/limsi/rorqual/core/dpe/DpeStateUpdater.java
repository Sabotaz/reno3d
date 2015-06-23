package fr.limsi.rorqual.core.dpe;

import com.badlogic.gdx.graphics.Color;

import java.util.HashMap;
import java.util.Map;

import fr.limsi.rorqual.core.event.*;
import scene3d.Actor3d;
import scene3d.Stage3d;

/**
 * Created by christophe on 04/06/15.
 */
public class DpeStateUpdater implements EventListener {

    HashMap<Object, DpeState> states = new HashMap<Object, DpeState>();
    Stage3d stage;

    public DpeStateUpdater(Stage3d s) {
        stage = s;
        EventManager.getInstance().addListener(Channel.DPE, this);
        EventManager.getInstance().addListener(Channel.UI, this);
    }

    public void setState(Object o, DpeState s) {
        states.put(o, s);
    }

    public DpeState getState(Object o) {
        if (states.containsKey(o)) return states.get(o);
        else return DpeState.NONE;
    }

    public void verifyAllStates() {
        boolean all_known = true;
        for (DpeState value : states.values())
            if (value == DpeState.UNKNOWN) all_known = false;

        if (all_known == true) {
            Event response = new Event(DpeEvent.DPE_STATE_NO_MORE_UNKNOWN, null);
            EventManager.getInstance().put(Channel.DPE, response);
        }
    }

    public void notify(Channel c, Event e) {
        EventType eventType = e.getEventType();
        if (c == Channel.DPE) {
            if (eventType instanceof DpeEvent) {
                DpeEvent event = (DpeEvent) eventType;
                Object o = e.getUserObject();
                switch (event) {
                    case DPE_STATE_CHANGED:
                        Object items[] = (Object[]) o;
                        setState(items[0], (DpeState) items[1]);
                        Actor3d actor = stage.getFromUserObject(items[0]);
                        switch ((DpeState) items[1]) {
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
                        setState(items[0], (DpeState) items[1]);
                        verifyAllStates();
                        break;
                }
            }
        } else if (c == Channel.UI) {
            if (eventType instanceof UiEvent) {
                UiEvent event = (UiEvent) eventType;
                Object o = e.getUserObject();
                switch (event) {
                    case ITEM_SELECTED:
                        if (getState(o) == DpeState.UNKNOWN || getState(o) == DpeState.GUESSED) {
                            Event response = new Event(DpeEvent.DPE_REQUEST, o);
                            EventManager.getInstance().put(Channel.DPE, response);
                        }
                }
            }
        }
    }
}
