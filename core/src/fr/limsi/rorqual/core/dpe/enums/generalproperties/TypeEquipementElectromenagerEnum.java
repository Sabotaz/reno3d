package fr.limsi.rorqual.core.dpe.enums.generalproperties;

/**
 * Created by ricordeau on 30/07/15.
 */
public enum TypeEquipementElectromenagerEnum {
    TELEVISEUR(123),
    LECTEUR_DVD_MAGNETOSCOPE(122),
    ORDINATEUR_FIXE(163),
    ORDINATEUR_PORTABLE(17),
    SECHE_LINGE(129),
    LAVE_LINGE(65),
    LAVE_VAISSELLE(273),
    FER_A_REPASSER(12),
    REFRIGERATEUR(492),
    CONGELATEUR(492),
    ASPIRATEUR(18);

    private int consommation;
    TypeEquipementElectromenagerEnum(int consommation){
        this.consommation=consommation;
    }
    public int getConsommation(){
        return this.consommation;
    }
}
