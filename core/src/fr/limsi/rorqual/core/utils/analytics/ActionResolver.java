package fr.limsi.rorqual.core.utils.analytics;

/**
 * Created by christophe on 22/02/16.
 */
public interface ActionResolver {

    void setTrackerScreenName(String path);

    void sendTrackerEvent(Category category, Action action);

    void sendTrackerEvent(Category category, Action action, String label);

    void sendEmail(String subject);

}
