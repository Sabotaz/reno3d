package fr.limsi.rorqual.core.dpe.enums.ecsproperties;

/**
 * Created by ricordeau on 29/07/15.
 */
public enum LocalEquipementEcsEnum {
    SITUE_DANS_LOCAL_CHAUFFE(true),
    SITUE_DANS_LOCAL_NON_CHAUFFE(false);
    private boolean isChauffe;
    LocalEquipementEcsEnum(boolean bool){
        this.isChauffe=bool;
    }
    public boolean getBoolean(){
        return this.isChauffe;
    }
}
