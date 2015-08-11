package fr.limsi.rorqual.core.dpe.enums.generalproperties;

/**
 * Created by ricordeau on 30/07/15.
 */
public enum TypeEquipementEclairageEnum {
    UNIQUEMENT_BASSE_CONSOMMATION(1.5),
    MAJORITAIREMENT_BASSE_CONSOMMATION(2),
    MAJORITAIREMENT_AMPOULE_A_INCANDESCENCE(3.7);

    private double consommationEclairage;
    TypeEquipementEclairageEnum(double conso){
        this.consommationEclairage=conso;
    }

    public double getConsommationEclairage(){
        return this.consommationEclairage;
    }
}
