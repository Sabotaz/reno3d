package fr.limsi.rorqual.core.dpe.enums.ecsproperties;

/**
 * Created by ricordeau on 29/07/15.
 */
public enum DeclenchementChaudiereEnum {
    DECLENCHEMENT_OUVERTURE_ROBINET_EAU_CHAUDE(true),
    NON_DECLENCHEMENT_OUVERTURE_ROBINET_EAU_CHAUDE(false);
    private boolean isChauffe;
    DeclenchementChaudiereEnum(boolean bool){
        this.isChauffe=bool;
    }
    public boolean getBoolean(){
        return this.isChauffe;
    }
}
