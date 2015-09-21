package fr.limsi.rorqual.core.event;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

// Singleton
// Gestionnaire d'événements
public class EventManager {

    private HashMap<Channel,BlockingQueue<Event>> eventQueues = new HashMap<Channel, BlockingQueue<Event>>();
    private HashMap<Channel,List<EventListener>> eventListeners = new HashMap<Channel,List<EventListener>>();

    private boolean running = false;

    public EventManager() {
        // création des canaux
        for (Channel c : Channel.values()) {
            eventQueues.put(c, new LinkedBlockingQueue<Event>());
            eventListeners.put(c, new ArrayList<EventListener>());
        }
    }

    // demarrage des différents canaux
    public void start() {
        if(!running) {
            running = true;
            for (Channel c : Channel.values()) {
                start(c);
            }
        }
    }

    public void addListener(Channel c, EventListener l) {
        List<EventListener> listeners = eventListeners.get(c);
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    public void put(Channel c, Event e) {
        eventQueues.get(c).offer(e);
    }

    // Holder
    private static class EventManagerHolder
    {
        // Instance unique non préinitialisée
        private final static EventManager INSTANCE = new EventManager();
    }

    public static synchronized EventManager getInstance() {
        return EventManagerHolder.INSTANCE;
    }

    private void start(final Channel c) {
        Thread t = new Thread() {
            public void run() {
                BlockingQueue<Event> eventQueue = eventQueues.get(c);
                List<EventListener> listeners = eventListeners.get(c);

                while (running) { // s'il y a des événements, on notifie les listeners
                    try {
                        Event e = eventQueue.poll(100, TimeUnit.MILLISECONDS);
                        if (e != null) {
                            synchronized (listeners) {
                                for (EventListener l : listeners) {
                                    makeNotificationThread(l, c, e);
                                }
                            }
                        }
                    } catch (InterruptedException ie) {

                    }
                }
                eventQueue.clear();
            }
        };
        t.start();
    }

    // notification d'un listener dans un thread séparé
    private void makeNotificationThread(final EventListener l, final Channel c, final Event e) {
        Thread t = new Thread() {
            public void run() {
                try {
                        l.notify(c, e);
                } catch (InterruptedException ie) {

                }
            }
        };
        t.start();
    }

    // arret du manager
    public void stop() {
        running = false;
    }

}