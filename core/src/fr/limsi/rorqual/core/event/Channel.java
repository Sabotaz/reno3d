package fr.limsi.rorqual.core.event;

/**
 * Created by christophe on 03/06/15.
 */
// Canaux possibles pour les events
public enum Channel {
    DPE, // tout ce qui concerne la mise à jour du DPE
    UI, // tout ce qui concerne l'UI (boutons, champs...)
    @Deprecated
    IFC, // tout ce qui concerne les modifications de l'IFC
    FAKE_DPE;
}
