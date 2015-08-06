package fr.limsi.rorqual.core.dpe.enums.chauffageproperties;

/**
 * Created by ricordeau on 04/08/15.
 */
public enum TypeChaudiereGazFioulEnum {
    CHAUDIERE_GAZ_STANDARD_ENTRE_1991_ET_2001("Chaudière gaz standard (entre 1991 et 2000)"),
    CHAUDIERE_GAZ_STANDARD_APRES_2001("Chaudière gaz standard (à partir de 2001)"),
    CHAUDIERE_GAZ_BASSE_TEMPERATURE_AVANT_2001("Chaudière basse température (avant 2001)"),
    CHAUDIERE_GAZ_BASSE_TEMPERATURE_APRES_2001("Chaudière basse température (à partir de 2001)"),
    CHAUDIERE_GAZ_CONDENSATION_AVANT_1986("Chaudière gaz à condensation (<1986)"),
    CHAUDIERE_GAZ_CONDENSATION_ENTRE_1986_ET_2001("Chaudière gaz à condensation (entre 1986 et 2001)"),
    CHAUDIERE_GAZ_CONDENSATION_APRES_2001("Chaudière gaz à condensation (à partir de 2001)"),
    CHAUDIERE_FIOUL_CLASSIQUE_AVANT_1970("Chaudière fioul classique (<1970)"),
    CHAUDIERE_FIOUL_CLASSIQUE_ENTRE_1970_ET_1976("Chaudière fioul classique (entre 1970 et 1975)"),
    CHAUDIERE_FIOUL_CLASSIQUE_ENTRE_1976_ET_1981("Chaudière fioul classique (entre 1976 et 1980)"),
    CHAUDIERE_FIOUL_CLASSIQUE_ENTRE_1981_ET_1991("Chaudière fioul classique (entre 1981 et 1990)"),
    CHAUDIERE_FIOUL_STANDARD_APRES_1991("Chaudière fioul standard (à partir de 1991)"),
    CHAUDIERE_FIOUL_BASSE_TEMPERATURE("Chaudière fioul basse température"),
    CHAUDIERE_FIOUL_CONDENSATION("Chaudière fioul condensation");

    private String name;
    TypeChaudiereGazFioulEnum(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
