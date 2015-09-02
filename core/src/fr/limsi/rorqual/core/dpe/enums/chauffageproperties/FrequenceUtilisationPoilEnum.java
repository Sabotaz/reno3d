package fr.limsi.rorqual.core.dpe.enums.chauffageproperties;

/**
 * Created by ricordeau on 21/07/15.
 */
public enum FrequenceUtilisationPoilEnum {
    TRES_PEU(0.1f),
    DE_TEMPS_EN_TEMPS(0.25f),
    PRINCIPALEMENT(0.75f),
    QUASI_EXCLUSIVEMENT(0.9f);

    private float k;
    FrequenceUtilisationPoilEnum(float k){
        this.k=k;
    }
    public float getFrequence(){ return this.k; }
}
