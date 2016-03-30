package fr.limsi.rorqual.core.dpe.enums.chauffageproperties;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.ArrayList;

import fr.limsi.rorqual.core.dpe.enums.ecsproperties.DeclenchementChaudiereEnum;
import fr.limsi.rorqual.core.dpe.enums.ecsproperties.LocalEquipementEcsEnum;

/**
 * Created by ricordeau on 21/07/15.
 */
@XStreamAlias("chauffage")
public class Chauffage {

    public enum Generateur {
        CHAUDIERE_ELECTRIQUE("Chaudière électrique"),
        CHAUDIERE_GAZ_STANDARD_APRES_2001("Chaudière gaz standard (après 2001)"),
        CHAUDIERE_GAZ_BASSE_TEMPERATURE_APRES_2001("Chaudière gaz basse température (après 2001)"),
        CHAUDIERE_GAZ_CONDENSATION_APRES_2001("Chaudière gaz à condensation (après 2001)"),
        POMPE_A_CHALEUR_AIR_AIR("Pompe à chaleur air/air"),
        POMPE_A_CHALEUR_AIR_EAU("Pompe à chaleur air/eau"),
        POMPE_A_CHALEUR_GEOTHERMIQUE("Pompe à chaleur géothermique"),
        ;

        private String name;

        Generateur(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public enum Type {
        CENTRALE,
        DIVISE,
        ;
    }

    public enum Emission {
        RADIATEUR,
        PLANCHER_CHAUFFANT,
        BOUCHE_DE_SOUFFLAGE,
        SOLUTION_MIXTE,
    }

    private void actualiseType(){
        this.type=Type.CENTRALE;
    }

    private Generateur generateur;
    private Type type;
    private Emission emission;
    private float prs1;
    private float rg;
    private float re;
    private float rd;
    private float rr;
    private float ich;
    private float pn;
    private float rpn;
    private float rpint;
    private float qp0;
    private float puissanceVeilleuse;
    private boolean generateurDansVolumeChauffe;
    private boolean presenceRobinetThermostatique;

    public Chauffage(){

    }

    public Chauffage(Generateur generateur,boolean generateurDansVolumeChauffe,boolean presenceRobinetThermostatique,Emission typeEmission) {
        this.generateur=generateur;
        this.emission = typeEmission;
        this.generateurDansVolumeChauffe=generateurDansVolumeChauffe;
        this.presenceRobinetThermostatique=presenceRobinetThermostatique;
        this.actualisePrs1();
        this.actualiseType();
        this.actualiseRd();
        this.actualiseRr();
        this.actualiseRe();
        this.actualiseRg();
        this.pn=0;
        this.pn=0;
        this.rpint=0;
        this.qp0=0;
        this.puissanceVeilleuse=0;
    }

    // Refresh values
    private void actualisePrs1(){
        if (!generateurDansVolumeChauffe){
            this.prs1=0;
        }else{
            switch(this.generateur){
                case CHAUDIERE_ELECTRIQUE:
                case POMPE_A_CHALEUR_AIR_EAU:
                case CHAUDIERE_GAZ_STANDARD_APRES_2001:
                case CHAUDIERE_GAZ_BASSE_TEMPERATURE_APRES_2001:
                case CHAUDIERE_GAZ_CONDENSATION_APRES_2001:
                    this.prs1 = 3.6f;
                    break;
                default:
                    this.prs1 = 0;
                    break;
            }
        }
    }
    private void actualiseRe(){
        switch (this.generateur){
            case POMPE_A_CHALEUR_AIR_AIR:
                this.re=0.95f;
                break;
            default:
                if(this.emission.equals(Emission.RADIATEUR)){
                    this.re=0.95f;
                    break;
                }else{
                    this.re=1;
                    break;
                }
        }
        actualiseIch();
    }
    private void actualiseRd(){
        this.rd=0.9f;
        actualiseIch();
    }
    private void actualiseRr(){
        if(this.emission!=Emission.RADIATEUR){
            this.rr=0.96f;
        }else{
            if(this.presenceRobinetThermostatique){
                this.rr=0.95f;
            }else{
                this.rr=0.9f;
            }
        }
        actualiseIch();
    }
    private void actualiseRg(){
        switch (this.generateur) {
            case CHAUDIERE_ELECTRIQUE:
                this.rg=0.77f;
                break;
            case POMPE_A_CHALEUR_AIR_AIR:
                this.rg=2.2f;
                break;
            case POMPE_A_CHALEUR_AIR_EAU:
                this.rg=2;
                break;
            case POMPE_A_CHALEUR_GEOTHERMIQUE:
                this.rg=4;
                break;
            default:
                this.rg=0.66f;
                break;
        }
        actualiseIch();
    }
    private void actualiseIch(){
        this.ich=1/rg*re*rd*rr;
    }

    // Getter
    public Generateur getGenerateur(){return this.generateur;}
    public Type getType(){return this.type;}
    public float getPrs1() {return this.prs1;}
    public float getRe() {return this.re;}
    public float getRd() {return this.rd;}
    public float getRr() {return this.rr;}
    public float getQp0() {return qp0;}
    public float getRpint() {return rpint;}
    public float getRpn(){ return rpn; }
    public float getPn() {return pn;}
    public float getIch() {return ich;}
    public float getPuissanceVeilleuse() {return puissanceVeilleuse;}

    // Setter
    public void setEmission(Emission emission){
        this.emission = emission;
        this.actualiseRe();
        this.actualiseRr();
    }
    public void setGenerateurDansVolumeChauffe(boolean generateurDansVolumeChauffe){
        this.generateurDansVolumeChauffe = generateurDansVolumeChauffe;
        this.actualisePrs1();
    }
    public void setPresenceRobinetThermostatique(boolean presence){
        this.presenceRobinetThermostatique=presence;
        this.actualiseRr();
    }
    public void setPn(float pn) {
        this.pn = pn;
        switch(this.generateur){
            case CHAUDIERE_GAZ_STANDARD_APRES_2001:
                this.rpn=84+2*(float)Math.log(pn);
                this.rpint=80+3*(float)Math.log(pn);
                this.qp0=0.01f*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_GAZ_BASSE_TEMPERATURE_APRES_2001:
                this.rpn=87.5f+1.5f*(float)Math.log(pn);
                this.rpint=87.5f+1.5f*(float)Math.log(pn);
                this.qp0=0.01f*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_GAZ_CONDENSATION_APRES_2001:
                this.rpn=91+1*(float)Math.log(pn);
                this.rpint=97+1*(float)Math.log(pn);
                this.qp0=0.01f*pn;
                this.puissanceVeilleuse=0;
                break;
        }
    }
    public void setRg(float rg){
        this.rg=rg;
        this.actualiseIch();
    }

    @Override
    public String toString(){
        return "||Générateur = "+this.generateur.toString()+" ||ich = " + ich+" ||rg = " + rg+" ||rr = " + rr+" ||re = " + re+" ||rd = " + rd+" ||pn = " + pn+" ||rpn = " + rpn+" ||rpint = " + rpint+" ||qp0 = " + qp0+" ||puissanceVeilleuse = " + puissanceVeilleuse;
    }

}
