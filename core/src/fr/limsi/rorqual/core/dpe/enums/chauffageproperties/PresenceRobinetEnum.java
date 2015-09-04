package fr.limsi.rorqual.core.dpe.enums.chauffageproperties;

/**
 * Created by ricordeau on 22/07/15.
 */
public enum PresenceRobinetEnum {
    PRESENCE_ROBINET_THERMOSTATIQUE(true),
    ABSENCE_ROBINET_THERMOSTATIQUE(false);
    private boolean isPresent;
    PresenceRobinetEnum(boolean bool){
        this.isPresent=bool;
    }
    public boolean getBoolean(){
        return this.isPresent;
    }
}
