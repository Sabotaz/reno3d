package fr.limsi.rorqual.core.dpe.enums.chauffageproperties;

/**
 * Created by ricordeau on 22/07/15.
 */
public enum PresenceThermostatEnum {
    PRESENCE_THERMOSTAT_OU_SONDE(true),
    AUCUN_DES_DEUX(false);
    private boolean isPresent;
    PresenceThermostatEnum(boolean bool){
        this.isPresent=bool;
    }
    public boolean getBoolean(){
        return this.isPresent;
    }
}
