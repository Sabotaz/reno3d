package fr.limsi.rorqual.core.event;

/**
 * Created by christophe on 03/06/15.
 */
public enum DpeEvent implements EventType {
    START_DPE,
    START_DPE_RESPONSE,
    TYPE_BATIMENT,
    TYPE_BATIMENT_RESPONSE,
    NB_NIVEAUX,
    NB_NIVEAUX_RESPONSE,
    FORME,
    FORME_RESPONSE,
    MITOYENNETE,
    MITOYENNETE_RESPONSE,
    POSITION_APPARTEMENT,
    POSITION_APPARTEMENT_RESPONSE,
    ANNEE_CONSTRUCTION,
    ANNEE_CONSTRUCTION_RESPONSE,
    ENERGIE_CONSTRUCTION,
    ENERGIE_CONSTRUCTION_RESPONSE,

    TYPE_MUR,
    DATE_ISOLATION_MUR,
    TYPE_ISOLATION_MUR,
    TYPE_FENETRE,
    TYPE_PORTE,
    TYPE_MATERIAU_MENUISERIE,
    TYPE_VITRAGE_MENUISERIE,

    DPE_STATE_CHANGED,
    DPE_STATE_REQUIRED,
    DPE_REQUEST,
    DPE_STATE_NO_MORE_WALL_UNKNOWN,
    DPE_STATE_NO_MORE_UNKNOWN,
    ;
}
