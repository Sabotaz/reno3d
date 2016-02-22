package fr.limsi.rorqual.core.utils.analytics;

/**
 * Created by christophe on 22/02/16.
 */
public interface ActionResolver {

    void setTrackerScreenName(String path);

    public void sendTrackerEvent(Category category, Action action);

    public void sendTrackerEvent(Category category, Action action, String label);

}
