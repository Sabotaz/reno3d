package fr.limsi.rorqual.core.dpe.enums.chauffageproperties;

/**
 * Created by ricordeau on 21/07/15.
 */
public enum FrequenceUtilisationPoilEnum {
    TRES_PEU(0.1),
    DE_TEMPS_EN_TEMPS(0.25),
    PRINCIPALEMENT(0.75),
    QUASI_EXCLUSIVEMENT(0.9);

    private double k;
    FrequenceUtilisationPoilEnum(double k){
        this.k=k;
    }
    public double getFrequence(){ return this.k; }
}
