package fr.limsi.rorqual.core.event;

/**
 * Created by christophe on 03/06/15.
 */
public class Event {

    private EventType event;
    private Object userObject;

    public Event() {}

    public Event(EventType t) {
        setEventType(t);
    }

    public Event(EventType t, Object o) {
        setEventType(t);
        setUserObject(o);
    }


    public void setEventType(EventType e) {
        event = e;
    }

    public EventType getEventType() {
        return event;
    }

    public void setUserObject(Object o) {
        userObject = o;
    }

    public Object getUserObject() {
        return userObject;
    }

}
