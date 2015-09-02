package fr.limsi.rorqual.core.dpe.enums.chauffageproperties;

/**
 * Created by ricordeau on 21/07/15.
 */
public enum TemperatureInterieurEnum {
    ENTRE_16_ET_17(16.5f),
    ENTRE_18_ET_19(18.5f),
    ENTRE_20_ET_21(20.5f),
    ENTRE_22_ET_23(22.5f);

    private float temperatureInterieure;
    TemperatureInterieurEnum(float temp){this.temperatureInterieure=temp;}
    public float getTemperatureInterieure(){return this.temperatureInterieure;}
}
