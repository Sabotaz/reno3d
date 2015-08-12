package fr.limsi.rorqual.core.dpe.enums.chauffageproperties;

import java.util.ArrayList;

/**
 * Created by ricordeau on 21/07/15.
 */
public class Chauffage {

    public enum Generateur {
        CHAUDIERE_ELECTRIQUE("Chaudière électrique"),
        CHAUDIERE_CLASSIQUE_AVANT_1981("Chaudière classique (<1981)"),
        CHAUDIERE_CLASSIQUE_ENTRE_1981_ET_1986("Chaudière classique (entre 1981 et 1985)"),
        CHAUDIERE_CLASSIQUE_ENTRE_1986_ET_1991("Chaudière classique (entre 1986 et 1990)"),
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
        CHAUDIERE_FIOUL_CONDENSATION("Chaudière fioul condensation"),
        CHAUDIERE_BOIS_PLUS_DE_15_ANS("Chaudière bois ancienne (age > 15 ans)"),
        CHAUDIERE_BOIS_MOINS_DE_15_ANS("Chaudière bois récente (age < 15 ans)"),
        POMPE_A_CHALEUR_AIR_AIR("Pompe à chaleur air/air"),
        POMPE_A_CHALEUR_AIR_EAU("Pompe à chaleur air/eau"),
        POMPE_A_CHALEUR_SUR_NAPPE("Pompe à chaleur sur nappe"),
        POMPE_A_CHALEUR_GEOTHERMIQUE("Pompe à chaleur géothermique"),
        CHAUFFAGE_ELECTRIQUE_DIRECT_ANCIEN("Chauffage électrique direct ancien"),
        CHAUFFAGE_ELECTRIQUE_DIRECT_RECENT("Chauffage électrique direct récent"),
        RADIATEUR_GAZ("Radiateur gaz"),
        POIL_OU_INSERT_BOIS_AVANT_2001("Poil ou insert bois installé avant 2001"),
        POIL_OU_INSERT_BOIS_APRES_2001("Poil ou insert bois installé à partir de 2001"),
        POIL_FIOUL_OU_GPL("Poil fioul ou GPL");

        private String name;
        Generateur(String name){
            this.name = name;
        }

        @Override
        public String toString(){
            return this.name;
        }

        public float getPrs1() {
            switch (this) {
                case CHAUDIERE_ELECTRIQUE:
                case POMPE_A_CHALEUR_AIR_EAU:
                case CHAUDIERE_CLASSIQUE_AVANT_1981:
                case CHAUDIERE_CLASSIQUE_ENTRE_1981_ET_1986:
                case CHAUDIERE_CLASSIQUE_ENTRE_1986_ET_1991:
                case CHAUDIERE_GAZ_STANDARD_ENTRE_1991_ET_2001:
                case CHAUDIERE_GAZ_STANDARD_APRES_2001:
                case CHAUDIERE_GAZ_BASSE_TEMPERATURE_AVANT_2001:
                case CHAUDIERE_GAZ_BASSE_TEMPERATURE_APRES_2001:
                case CHAUDIERE_GAZ_CONDENSATION_AVANT_1986:
                case CHAUDIERE_GAZ_CONDENSATION_ENTRE_1986_ET_2001:
                case CHAUDIERE_GAZ_CONDENSATION_APRES_2001:
                    return 3.6f;
                default:
                    return 0;
            }
        }

        public class NotDefinedException extends Exception {}

        public float getRg() throws NotDefinedException {
            switch (this) {
                case CHAUFFAGE_ELECTRIQUE_DIRECT_ANCIEN:
                case CHAUFFAGE_ELECTRIQUE_DIRECT_RECENT:
                    return 1;
                case CHAUDIERE_ELECTRIQUE:
                    return 0.77f;
                case POMPE_A_CHALEUR_AIR_AIR:
                    return 2.2f;
                case POMPE_A_CHALEUR_AIR_EAU:
                    return 2.6f;
                case POMPE_A_CHALEUR_SUR_NAPPE:
                    return 3.2f;
                case POMPE_A_CHALEUR_GEOTHERMIQUE:
                    return 4.f;
                case POIL_OU_INSERT_BOIS_AVANT_2001:
                    return 0.66f;
                case POIL_OU_INSERT_BOIS_APRES_2001:
                    return 0.78f;
                case POIL_FIOUL_OU_GPL:
                    return 0.72f;
                default:
                    throw new NotDefinedException();
            }
        }

        public float getRd() {
            switch (this) {
                case CHAUFFAGE_ELECTRIQUE_DIRECT_RECENT:
                case CHAUFFAGE_ELECTRIQUE_DIRECT_ANCIEN:
                case POIL_OU_INSERT_BOIS_AVANT_2001:
                case POIL_OU_INSERT_BOIS_APRES_2001:
                case POIL_FIOUL_OU_GPL:
                case RADIATEUR_GAZ:
                    return 1.f;
                default:
                    return 0.9f;
            }
        }

        public float getRr() throws NotDefinedException {
            switch (this) {
                case CHAUFFAGE_ELECTRIQUE_DIRECT_ANCIEN:
                    return 0.96f;
                case CHAUFFAGE_ELECTRIQUE_DIRECT_RECENT:
                    return 0.99f;
                case POIL_OU_INSERT_BOIS_AVANT_2001:
                case POIL_OU_INSERT_BOIS_APRES_2001:
                case POIL_FIOUL_OU_GPL:
                    return 0.8f;
                case RADIATEUR_GAZ:
                    return 0.96f;
                default:
                    throw new NotDefinedException();
            }
        }
    }

    public enum Type {
        CENTRALE,
        DIVISE,
        ;
    }

    public enum Emission {
        RADIATEUR,
        PLANCHER_CHAUFFANT,
        SYSTEME_DE_SOUFFLAGE,
        SOLUTION_MIXTE;
    }

    private void actualiseType(){
        if (this.generateur.equals(Generateur.CHAUFFAGE_ELECTRIQUE_DIRECT_ANCIEN)
                || this.generateur.equals(Generateur.CHAUFFAGE_ELECTRIQUE_DIRECT_RECENT)
                || this.generateur.equals(Generateur.POIL_FIOUL_OU_GPL)
                || this.generateur.equals(Generateur.POIL_OU_INSERT_BOIS_APRES_2001)
                || this.generateur.equals(Generateur.POIL_OU_INSERT_BOIS_AVANT_2001)
                || this.generateur.equals(Generateur.RADIATEUR_GAZ))
        {
            this.type=Type.DIVISE;
        }else{
            this.type=Type.CENTRALE;
        }
    }

    private Generateur generateur;
    private Type type;
    private Emission emission;
    private boolean robinetsThermostatiques;

    public Chauffage(Generateur generateur) {
        this.generateur=generateur;
        this.actualiseType();
        this.emission = Emission.RADIATEUR;
        this.robinetsThermostatiques = false;
    }

    // Getter
    public float getRe() {
        if (emission == null)
            return 0.95f;
        switch (emission) {
            case PLANCHER_CHAUFFANT:
                return 1.f;
            default:
                return 0.95f;
        }
    }
    public float getDefaultRr() {
        if (emission==null)
            return 0.8f;
        switch (emission) {
            case RADIATEUR:
                return robinetsThermostatiques ? 0.95f : 0.9f;
            case SOLUTION_MIXTE:
                return 0.8f;
            default:
                return 0.8f;
        }
    }
    public Type getType(){
        return this.type;
    }
}
