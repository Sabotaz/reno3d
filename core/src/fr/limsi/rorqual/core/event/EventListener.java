package fr.limsi.rorqual.core.event;

/**
 * Created by christophe on 03/06/15.
 */
public interface EventListener {
    public void notify(Channel c, Event e);
}
