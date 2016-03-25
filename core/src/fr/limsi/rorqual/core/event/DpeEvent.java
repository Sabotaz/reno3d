package fr.limsi.rorqual.core.event;

/**
 * Created by christophe on 03/06/15.
 */
// events possibles pour la mise à jour du DPE
public enum DpeEvent implements EventType {
    INFOS_GENERALES,
    INFOS_CHAUFFAGE,
    INFOS_ECS,
    INFOS_MURS,
    INFOS_FENETRES,

    TYPE_BATIMENT,
    CATEGORIE_BATIMENT,
    SURFACE_HABITABLE,
    NB_NIVEAUX_MAISON,
    FORME_MAISON,
    MITOYENNETE_MAISON,
    POSITION_APPARTEMENT,
    DEPARTEMENT_BATIMENT,
    ANNEE_CONSTRUCTION,
    TYPE_VENTILATION,
    ENERGIE_CONSTRUCTION,
    ABONNEMENT_ELECTRIQUE,
    CLIMATISATION_LOGEMENT,
    SURFACE_CLIMATISATION,
    EQUIPEMENT_ECLAIRAGE,
    EQUIPEMENT_CUISSON,
    EQUIPEMENT_ELECTROMENAGER,
    NOMBRE_PERSONNES_DANS_LOGEMENT,
    NOMBRE_JOURS_ABSENCE,

    INSTALLATION_CHAUFFAGE,
    CHAUFFAGE_UNIQUE,
    CHAUFFAGE_SANS_POIL,
    CHAUDIERE_AVEC_PAC,
    CHAUDIERE_AVEC_PAC_ET_POELE,
    CHAUDIERE_GAZ_FIOUL,
    CHAUDIERE_BOIS,
    POMPE_A_CHALEUR_AVEC_CHAUDIERE,
    POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE,
    POELE_OU_INSERT_AVEC_CHAUFFAGE,
    POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC,
    FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUFFAGE,
    FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC,
    TEMPERATURE_INTERIEUR,
    TYPE_EMETTEUR_DE_CHALEUR,
    PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR,
    PRESENCE_ROBINET_THERMOSTATIQUE,
    SYSTEME_PROGRAMMABLE,

    TYPE_EQUIPEMENT_ECS,
    CHAUDIERE_ECS,
    PRESENCE_INSTALLATION_SOLAIRE,
    LOCAL_EQUIPEMENT_ECS,
    DECLENCHEMENT_CHAUDIERE_ROBINET,
    USAGE_EAU_CHAUDE,

    TYPE_MUR,
    DATE_ISOLATION_MUR,
    TYPE_ISOLATION_MUR,
    ORIENTATION_MUR,

    MITOYENNETE_PLANCHER,
    MITOYENNETE_PLAFOND,
    DATE_ISOLATION_PLANCHER,
    DATE_ISOLATION_PLAFOND,
    TYPE_ISOLATION_PLANCHER,
    TYPE_ISOLATION_PLAFOND,

    TYPE_FENETRE,
    TYPE_PORTE,
    TYPE_MATERIAU_MENUISERIE,
    TYPE_VITRAGE_MENUISERIE,
    TYPE_FERMETURE_MENUISERIE,
    MASQUE_PROCHE_MENUISERIE,
    MASQUE_LOINTAIN_MENUISERIE,

    MUR_AJOUTE,
    MUR_REMOVED,
    SIZE_MUR_CHANGED,
    SLAB_AJOUTE,
    SLAB_REMOVED,
    OUVERTURE_AJOUTE,
    OUVERTURE_REMOVED,
    FENETRE_AJOUTEE,
    PORTE_FENETRE_AJOUTEE,
    PORTE_AJOUTE,
    MITOYENNETE_MUR_CHANGEE,
    MITOYENNETE_PLANCHER_CHANGEE,
    MITOYENNETE_PLAFOND_CHANGEE,
    ORIENTATION_GLOBALE_CHANGEE,
    SURFACE_CHANGED,

    DEPERDITION_MURS_CHANGED,
    DEPERDITION_TOITS_CHANGED,
    DEPERDITION_PLANCHERS_CHANGED,
    DEPERDITION_FENETRES_CHANGED,
    DEPERDITION_PORTES_CHANGED,
    DEPERDITION_PORTES_FENETRES_CHANGED,

    DPE_STATE_CHANGED,
    DPE_STATE_REQUIRED,
    DPE_REQUEST,
    DPE_STATE_NO_MORE_WALL_UNKNOWN,
    DPE_STATE_NO_MORE_UNKNOWN,

    IS_NO_MORE_FIRST_FLOOR,
    IS_NO_MORE_LAST_FLOOR,
    IS_NOW_FIRST_FLOOR,
    IS_NOW_LAST_FLOOR,
    ;
}
