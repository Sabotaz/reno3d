package fr.limsi.rorqual.core.utils.analytics;

/**
 * Created by christophe on 22/02/16.
 */
public interface ActionResolver {

    void setTrackerScreenName(String path);

    void sendTrackerEvent(Category category, Action action);

    void sendTrackerEvent(Category category, Action action, String label);

    void sendTrackerEvent(Category category, Action action, long value);

    void sendTrackerEvent(Category category, Action action, String label, long value);

    void sendTiming(Category category, long value);

    void sendTiming(Category category, long value, String name);

    void sendTiming(Category category, long value, String name, String label);

    void sendEmail(String subject);

}
