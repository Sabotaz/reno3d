package fr.limsi.rorqual.core.dpe.enums.wallproperties;

/**
 * Created by ricordeau on 20/07/15.
 */
public enum OrientationEnum {
    NORD,
    SUD,
    EST,
    OUEST,
    INCONNUE;

    private OrientationEnum next;

    static {
        NORD.next = EST;
        EST.next = SUD;
        SUD.next = OUEST;
        OUEST.next = NORD;
        INCONNUE.next = INCONNUE;
    }

    public OrientationEnum wrapX(float dx, float dy) {
        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0)
                return this;
            else
                return this.next.next;
        } else {
            if (dy > 0)
                return this.next;
            else
                return this.next.next.next;
        }
    }
}
