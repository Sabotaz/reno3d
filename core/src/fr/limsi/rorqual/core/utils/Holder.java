package fr.limsi.rorqual.core.utils;

/**
 * Created by christophe on 28/08/15.
 */
// Classe utilitaire permétant d'attendre une variable
public class Holder {

    public Object o = null;
    private boolean isSet = false;

    public void set(Object obj) {
        o = obj;
        isSet = true;
    }

    public Object get() {
        while (!isSet) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return o;
    }
}
