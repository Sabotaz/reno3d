package fr.limsi.rorqual.core.event;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class EventManager {

    private HashMap<Channel,BlockingQueue<Event>> eventQueues = new HashMap<Channel, BlockingQueue<Event>>();
    private HashMap<Channel,List<EventListener>> eventListeners = new HashMap<Channel,List<EventListener>>();

    private boolean running = true;

    public EventManager() {
        for (Channel c : Channel.values()) {
            eventQueues.put(c, new LinkedBlockingQueue<Event>());
            eventListeners.put(c, new ArrayList<EventListener>());
            start(c);
        }
    }

    public void addListener(Channel c, EventListener l) {
        eventListeners.get(c).add(l);
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

                while (running) {
                    try {
                        Event e = eventQueue.poll(100, TimeUnit.MILLISECONDS);
                        if (e != null) {
                            for (EventListener l : listeners) {
                                l.notify(c, e);
                            }
                        }
                    } catch (InterruptedException ie) {

                    }
                }
            }
        };

        t.start();
    }

}