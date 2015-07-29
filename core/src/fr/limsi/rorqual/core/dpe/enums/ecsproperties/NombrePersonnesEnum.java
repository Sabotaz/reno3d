package fr.limsi.rorqual.core.dpe.enums.ecsproperties;

/**
 * Created by ricordeau on 29/07/15.
 */
public enum NombrePersonnesEnum {
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

    NombrePersonnesEnum(int nb){
        this.nombre=nb;
    }

    @Override
    public String toString(){
        return Integer.toString(this.nombre);
    }

}
