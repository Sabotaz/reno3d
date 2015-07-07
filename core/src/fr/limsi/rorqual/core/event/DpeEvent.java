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
    TYPE_MUR_RESPONSE,
    DATE_ISOLATION_MUR,
    DATE_ISOLATION_MUR_RESPONSE,
    TYPE_ISOLATION_MUR,
    TYPE_ISOLATION_MUR_RESPONSE,
    TYPE_FENETRE,
    TYPE_FENETRE_RESPONSE,
    TYPE_MENUISERIE_FENETRE,
    TYPE_MENUISERIE_FENETRE_RESPONSE,
    TYPE_VITRAGE_FENETRE,
    TYPE_VITRAGE_FENETRE_RESPONSE,
    TYPE_DOOR,
    TYPE_DOOR_RESPONSE,


    DERRIERE_SLAB,
    DERRIERE_SLAB_RESPONSE,
    ISOLATION_SLAB,
    ISOLATION_SLAB_RESPONSE,
    DATE_ISOLATION_SLAB,
    DATE_ISOLATION_SLAB_RESPONSE,


    DPE_STATE_CHANGED,
    DPE_STATE_REQUIRED,
    DPE_REQUEST,
    DPE_STATE_NO_MORE_UNKNOWN,
    ;
}
