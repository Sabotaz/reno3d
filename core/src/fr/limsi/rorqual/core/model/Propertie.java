package fr.limsi.rorqual.core.model;

/**
 * Created by ricordeau on 21/10/15.
 */
public enum Propertie {
    COINS("Coins"),

    ;

    private String namePropertie;
    Propertie(String s){
        this.namePropertie=s;
    }

    public String getName(){
        return this.namePropertie;
    }

}
