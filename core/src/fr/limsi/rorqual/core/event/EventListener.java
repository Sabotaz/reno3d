package fr.limsi.rorqual.core.event;

/**
 * Created by christophe on 03/06/15.
 */
// Interface permettant d'être notifié d'un événement
public interface EventListener {
    void notify(Channel c, Event e) throws InterruptedException;
}
