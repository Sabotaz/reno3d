package fr.limsi.rorqual.core.event;

/**
 * Created by christophe on 04/06/15.
 */
// events venant des UI
public enum UiEvent implements EventType {
    ITEM_SELECTED,
    ITEM_DESELECTED,
    BUTTON_CLICKED,
    TEXTURE1_PICKED,
    TEXTURE2_PICKED,
    TEXTURE3_PICKED,
    SAVE_FILE,
    LOAD_FILE,
    EXPORT_FILE,

    HAUTEUR_MODELE,
    LARGEUR_MODELE,
    HAUTEUR_SOL_MODELE,
    RATIO_MODELE,

    FONCTION_PIECE,

    LIMIT_INFRINGEMENT,

    CLOSE,

    ;
}
