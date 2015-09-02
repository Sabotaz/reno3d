package fr.limsi.rorqual.core.dpe.enums.generalproperties;

/**
 * Created by ricordeau on 30/07/15.
 */
public enum TypeEquipementCuissonEnum {
    PLAQUES_MIXTES_ET_FOUR_ELECTRIQUE(1340),
    PLAQUES_MIXTES_ET_FOUR_GAZ(1480),
    PLAQUES_ELECTRIQUES_ET_FOUR_ELECTRIQUE(1160),
    FEUX_GAZ_ET_FOUR_ELECTRIQUE(1520),
    FEUX_GAZ_ET_FOUR_GAZ(1660);

    private int consommation;
    TypeEquipementCuissonEnum(int conso){
        this.consommation = conso;
    }
    public int getConsommation(){
        return this.consommation;
    }
}
