package fr.limsi.rorqual.core.dpe.enums.generalproperties;

/**
 * Created by ricordeau on 04/08/15.
 */
public enum NombreJoursAbsenceEnum {
    UNE(1),
    DEUX(2),
    TROIS(3),
    QUATRE(4),
    CINQ(5),
    SIX(6),
    SEPT(7),
    HUIT(8),
    NEUF(9),
    DIX(10),
    ONZE(11),
    DOUZE(12),
    TREIZE(13),
    QUATORZE(14),
    QUINZE(15),
    SEIZE(16),
    DIX_SEPT(17),
    DIX_HUIT(18),
    DIX_NEUF(19),
    VINGT(20);

    private int nombre;

    NombreJoursAbsenceEnum(int nb){
        this.nombre=nb;
    }

    @Override
    public String toString(){
        if (this.nombre>1){
            return Integer.toString(this.nombre)+" jours";
        }else{
            return Integer.toString(this.nombre)+" jour";
        }
    }
}
