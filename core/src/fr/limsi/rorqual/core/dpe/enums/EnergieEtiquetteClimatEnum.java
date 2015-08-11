package fr.limsi.rorqual.core.dpe.enums;

/**
 * Created by ricordeau on 10/08/15.
 */
public enum EnergieEtiquetteClimatEnum {
    BOIS_BIOMASSE(0.013),
    GAZ_NATUREL(0.234),
    FIOUL_DOMESTIQUE(0.300),
    CHARBON(0.342),
    GAZ_PROPANE_BUTANE(0.274),
    AUTRES_COMBUSTIBLES_FOSSILES(0.320),
    ELECTRICITE_RENOUVELABLE(0),
    ELECTRICITE_NON_RENOUVELABLE_CHAUFFAGE(0.180),
    ELECTRICITE_NON_RENOUVELABLE_ECS_REFROIDISSEMENT(0.040),
    ELECTRICITE_NON_RENOUVELABLE_AUTRE(0.084);

    private double facteurConversion;
    EnergieEtiquetteClimatEnum(double conversion){
        this.facteurConversion = conversion;
    }
    public double getFacteurConversion(){
        return this.facteurConversion;
    }
}
