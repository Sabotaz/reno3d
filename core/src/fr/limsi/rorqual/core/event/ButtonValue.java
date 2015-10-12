package fr.limsi.rorqual.core.event;

/**
 * Created by christophe on 28/07/15.
 */
// Valeurs possibles renvoyées par les boutons des UI
public enum ButtonValue {
    EXIT,
    SWITCH_2D_3D, // camera
    MUR,
    PIECE,
    @Deprecated
    FENETRE,
    @Deprecated
    PORTE,
    MOVE,
    DELETE,
    DPE,
    CHAUFFAGE,
    ECS,
    ELECTROMENAGER,
    MENUISERIE,
    ETAGE_PLUS,
    ETAGE_MINUS,
    EXPORT_IFC,
    ROTATE_G,
    ROTATE_D,

    ;
}
