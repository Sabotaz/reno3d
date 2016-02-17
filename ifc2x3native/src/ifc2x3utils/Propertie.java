package ifc2x3utils;

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
