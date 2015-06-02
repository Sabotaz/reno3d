package fr.limsi.rorqual.core.dpe;


import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

//public class EventManager {
//
//
//    public enum Event {
//        MURINFORMATION;
//    }
//    private Queue eventQueue = new LinkedBlockingQueue<Event>();
//    private boolean running = true;
//
//
//    public EventManager() {
//        start();
//    }
//
//    public void put(Event e) {
//        eventQueue.offer(e);
//    }
//
//    public void stop() {
//        running = false;
//    }
//
//    /** Holder */
//    private static class EventManagerHolder
//    {
//        /** Instance unique non préinitialisée */
//        private final static EventManager INSTANCE = new EventManager();
//    }
//
//    public static synchronized EventManager getInstance() {
//        return EventManagerHolder.INSTANCE;
//    }
//
//    public void start() {
//        new Thread() {
//
//            public void run() {
//                while (running) {
//                    Object o = eventQueue.pool(100L, TimeUnit.MILLISECONDS);
//                    if (o != null) {
//                        Event e = (Event) o;
//                        treat(e);
//                    }
//                }
//            }
//
//        }.start();
//    }
//
//    public enum ResponseEvent {
//        MURINFORMATION;
//    }
//
//    private treat(Event e) {
//
//        ResponseEvent response = null;
//        switch (e) {
//            case MURINFORMATION:
//
//        }
//
//        while (response == null) {
//            System.sleep(100);
//        }
//        listener.notify(response);
//    }
//
//}
//
//}