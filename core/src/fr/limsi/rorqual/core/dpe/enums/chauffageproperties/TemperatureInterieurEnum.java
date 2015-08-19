package fr.limsi.rorqual.core.dpe.enums.chauffageproperties;

/**
 * Created by ricordeau on 21/07/15.
 */
public enum TemperatureInterieurEnum {
    ENTRE_16_ET_17(16.5),
    ENTRE_18_ET_19(18.5),
    ENTRE_20_ET_21(20.5),
    ENTRE_22_ET_23(22.5);

    private double temperatureInterieure;
    TemperatureInterieurEnum(double temp){this.temperatureInterieure=temp;}
    public double getTemperatureInterieure(){return this.temperatureInterieure;}
}
