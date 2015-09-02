package fr.limsi.rorqual.core.dpe.enums.generalproperties;

/**
 * Created by ricordeau on 30/07/15.
 */
public enum TypeEquipementEclairageEnum {
    UNIQUEMENT_BASSE_CONSOMMATION(1.5f),
    MAJORITAIREMENT_BASSE_CONSOMMATION(2),
    MAJORITAIREMENT_AMPOULE_A_INCANDESCENCE(3.7f);

    private float consommationEclairage;
    TypeEquipementEclairageEnum(float conso){
        this.consommationEclairage=conso;
    }

    public float getConsommationEclairage(){
        return this.consommationEclairage;
    }
}
