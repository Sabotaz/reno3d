package fr.limsi.rorqual.core.dpe.enums.chauffageproperties;

import java.util.ArrayList;

import fr.limsi.rorqual.core.dpe.enums.ecsproperties.DeclenchementChaudiereEnum;

/**
 * Created by ricordeau on 21/07/15.
 */
public class Chauffage {

    public enum Generateur {
        CHAUDIERE_ELECTRIQUE("Chaudière électrique"),
        CHAUDIERE_GAZ_CLASSIQUE_AVANT_1981("Chaudière gaz classique (<1981)"),
        CHAUDIERE_GAZ_CLASSIQUE_ENTRE_1981_ET_1986("Chaudière gaz classique (entre 1981 et 1985)"),
        CHAUDIERE_GAZ_CLASSIQUE_ENTRE_1986_ET_1991("Chaudière gaz classique (entre 1986 et 1990)"),
        CHAUDIERE_GAZ_STANDARD_ENTRE_1991_ET_2001("Chaudière gaz standard (entre 1991 et 2000)"),
        CHAUDIERE_GAZ_STANDARD_APRES_2001("Chaudière gaz standard (à partir de 2001)"),
        CHAUDIERE_GAZ_BASSE_TEMPERATURE_AVANT_2001("Chaudière gaz basse température (avant 2001)"),
        CHAUDIERE_GAZ_BASSE_TEMPERATURE_APRES_2001("Chaudière gaz basse température (à partir de 2001)"),
        CHAUDIERE_GAZ_CONDENSATION_AVANT_1986("Chaudière gaz à condensation (<1986)"),
        CHAUDIERE_GAZ_CONDENSATION_ENTRE_1986_ET_2001("Chaudière gaz à condensation (entre 1986 et 2001)"),
        CHAUDIERE_GAZ_CONDENSATION_APRES_2001("Chaudière gaz à condensation (à partir de 2001)"),
        CHAUDIERE_FIOUL_CLASSIQUE_AVANT_1970("Chaudière fioul classique (<1970)"),
        CHAUDIERE_FIOUL_CLASSIQUE_ENTRE_1970_ET_1976("Chaudière fioul classique (entre 1970 et 1975)"),
        CHAUDIERE_FIOUL_CLASSIQUE_ENTRE_1976_ET_1981("Chaudière fioul classique (entre 1976 et 1980)"),
        CHAUDIERE_FIOUL_CLASSIQUE_ENTRE_1981_ET_1991("Chaudière fioul classique (entre 1981 et 1990)"),
        CHAUDIERE_FIOUL_STANDARD_APRES_1991("Chaudière fioul standard (à partir de 1991)"),
        CHAUDIERE_FIOUL_BASSE_TEMPERATURE("Chaudière fioul basse température"),
        CHAUDIERE_FIOUL_CONDENSATION("Chaudière fioul condensation"),
        CHAUDIERE_BOIS_PLUS_DE_15_ANS("Chaudière bois ancienne (age > 15 ans)"),
        CHAUDIERE_BOIS_MOINS_DE_15_ANS("Chaudière bois récente (age < 15 ans)"),
        POMPE_A_CHALEUR_AIR_AIR("Pompe à chaleur air/air"),
        POMPE_A_CHALEUR_AIR_EAU("Pompe à chaleur air/eau"),
        POMPE_A_CHALEUR_SUR_NAPPE("Pompe à chaleur sur nappe"),
        POMPE_A_CHALEUR_GEOTHERMIQUE("Pompe à chaleur géothermique"),
        CHAUFFAGE_ELECTRIQUE_DIRECT_ANCIEN("Chauffage électrique direct ancien"),
        CHAUFFAGE_ELECTRIQUE_DIRECT_RECENT("Chauffage électrique direct récent"),
        RADIATEUR_GAZ_AVANT_2006("Radiateur gaz (<2006)"),
        RADIATEUR_GAZ_APRES_2006("Radiateur gaz (à partir de 2006)"),
        POIL_OU_INSERT_BOIS_AVANT_2001("Poil ou insert bois installé avant 2001"),
        POIL_OU_INSERT_BOIS_APRES_2001("Poil ou insert bois installé à partir de 2001"),
        POIL_FIOUL_OU_GPL("Poil fioul ou GPL");

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
        if (this.generateur.equals(Generateur.CHAUFFAGE_ELECTRIQUE_DIRECT_ANCIEN)
                || this.generateur.equals(Generateur.CHAUFFAGE_ELECTRIQUE_DIRECT_RECENT)
                || this.generateur.equals(Generateur.POIL_FIOUL_OU_GPL)
                || this.generateur.equals(Generateur.POIL_OU_INSERT_BOIS_APRES_2001)
                || this.generateur.equals(Generateur.POIL_OU_INSERT_BOIS_AVANT_2001)
                || this.generateur.equals(Generateur.RADIATEUR_GAZ_AVANT_2006)
                || this.generateur.equals(Generateur.RADIATEUR_GAZ_APRES_2006))
        {
            this.type=Type.DIVISE;
        }else{
            this.type=Type.CENTRALE;
        }
    }

    private Generateur generateur;
    private Type type;
    private Emission emission;
    private double prs1;
    private double rg;
    private double re;
    private double rd;
    private double rr;
    private double ich;
    private double pn;
    private double rpn;
    private double rpint;
    private double qp0;
    private double puissanceVeilleuse;
    private boolean generateurDansVolumeChauffe;
    private boolean presenceRobinetThermostatique;
    private boolean genereEgalementEcs;

    public Chauffage(Generateur generateur) {
        this.generateur=generateur;
        this.actualiseType();
        this.actualiseRd();
        this.actualiseRr();
        this.actualiseRe();
        this.actualiseRg();
        this.emission = Emission.RADIATEUR;
        this.prs1=0;
        this.pn=0;
        this.generateurDansVolumeChauffe=false;
        this.presenceRobinetThermostatique=false;
        this.pn=0;
        this.rpint=0;
        this.qp0=0;
        this.puissanceVeilleuse=0;
        this.genereEgalementEcs=false; // Par défaut, on considère que le chauffage ne sert qu'à chauffer le logement (et non l'eau chaude sanitaire)
    }

    // Refresh values
    private void actualisePrs1(){
        if (!generateurDansVolumeChauffe){
            this.prs1=0;
        }else{
            switch(this.generateur){
                case CHAUDIERE_ELECTRIQUE:
                case POMPE_A_CHALEUR_AIR_EAU:
                case CHAUDIERE_GAZ_CLASSIQUE_AVANT_1981:
                case CHAUDIERE_GAZ_CLASSIQUE_ENTRE_1981_ET_1986:
                case CHAUDIERE_GAZ_CLASSIQUE_ENTRE_1986_ET_1991:
                case CHAUDIERE_GAZ_STANDARD_ENTRE_1991_ET_2001:
                case CHAUDIERE_GAZ_STANDARD_APRES_2001:
                case CHAUDIERE_GAZ_BASSE_TEMPERATURE_AVANT_2001:
                case CHAUDIERE_GAZ_BASSE_TEMPERATURE_APRES_2001:
                case CHAUDIERE_GAZ_CONDENSATION_AVANT_1986:
                case CHAUDIERE_GAZ_CONDENSATION_ENTRE_1986_ET_2001:
                case CHAUDIERE_GAZ_CONDENSATION_APRES_2001:
                    this.prs1 = 3.6;
                    break;
                default:
                    this.prs1 = 0;
                    break;
            }
        }
    }
    private void actualiseRe(){
        switch (this.generateur){
            case CHAUFFAGE_ELECTRIQUE_DIRECT_ANCIEN:
            case POMPE_A_CHALEUR_AIR_AIR:
            case POIL_FIOUL_OU_GPL:
            case POIL_OU_INSERT_BOIS_APRES_2001:
            case POIL_OU_INSERT_BOIS_AVANT_2001:
            case RADIATEUR_GAZ_AVANT_2006:
            case RADIATEUR_GAZ_APRES_2006:
                this.re=0.95;
                break;
            case CHAUFFAGE_ELECTRIQUE_DIRECT_RECENT:
                this.re=0.96;
                break;
            default:
                if(this.emission.equals(Emission.RADIATEUR)){
                    this.re=0.95;
                    break;
                }else{
                    this.re=1;
                    break;
                }
        }
        actualiseIch();
    }
    private void actualiseRd(){
        switch (this.generateur){
            case CHAUFFAGE_ELECTRIQUE_DIRECT_ANCIEN:
            case CHAUFFAGE_ELECTRIQUE_DIRECT_RECENT:
            case POIL_FIOUL_OU_GPL:
            case POIL_OU_INSERT_BOIS_AVANT_2001:
            case POIL_OU_INSERT_BOIS_APRES_2001:
            case RADIATEUR_GAZ_AVANT_2006:
            case RADIATEUR_GAZ_APRES_2006:
                this.rd=1;
                break;
            default:
                this.rd=0.9;
        }
        actualiseIch();
    }
    private void actualiseRr(){
        switch (this.generateur){
            case CHAUFFAGE_ELECTRIQUE_DIRECT_ANCIEN:
            case RADIATEUR_GAZ_AVANT_2006:
            case RADIATEUR_GAZ_APRES_2006:
                this.rr=0.96;
                break;
            case CHAUFFAGE_ELECTRIQUE_DIRECT_RECENT:
                this.rr=0.99;
                break;
            case POIL_FIOUL_OU_GPL:
            case POIL_OU_INSERT_BOIS_APRES_2001:
            case POIL_OU_INSERT_BOIS_AVANT_2001:
                this.rr=0.8;
                break;
            default:
                if(this.emission!=Emission.RADIATEUR){
                    this.rr=0.96;
                }else{
                    if(this.presenceRobinetThermostatique){
                        this.rr=0.95;
                    }else{
                        this.rr=0.9;
                    }
                }
        }
        actualiseIch();
    }
    private void actualiseRg(){
        switch (this.generateur) {
            case CHAUFFAGE_ELECTRIQUE_DIRECT_ANCIEN:
            case CHAUFFAGE_ELECTRIQUE_DIRECT_RECENT:
                this.rg=1;
            case CHAUDIERE_ELECTRIQUE:
                this.rg=0.77;
            case POMPE_A_CHALEUR_AIR_AIR:
                this.rg=2.2;
            case POMPE_A_CHALEUR_AIR_EAU:
                this.rg=2;
            case POMPE_A_CHALEUR_SUR_NAPPE:
                this.rg=3.2;
            case POMPE_A_CHALEUR_GEOTHERMIQUE:
                this.rg=4;
            case POIL_OU_INSERT_BOIS_AVANT_2001:
                this.rg=0.66;
            case POIL_OU_INSERT_BOIS_APRES_2001:
                this.rg=0.78;
            case POIL_FIOUL_OU_GPL:
                this.rg=0.72;
            default:
                this.rg=0.66;
        }
        actualiseIch();
    }
    private void actualiseIch(){
        this.ich=1/rg*re*rd*rr;
    }

    // Getter
    public Generateur getGenerateur(){return this.generateur;}
    public Type getType(){return this.type;}
    public double getPrs1() {return this.prs1;}
    public double getRe() {return this.re;}
    public double getRd() {return this.rd;}
    public double getRr() {return this.rr;}
    public double getQp0() {return qp0;}
    public double getRpint() {return rpint;}
    public double getRpn(){ return rpn; }
    public double getPn() {return pn;}
    public double getIch() {return ich;}
    public double getPuissanceVeilleuse() {return puissanceVeilleuse;}
    public boolean getGenereEgalementEcs(){ return this.genereEgalementEcs; }

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
    public void setPn(double pn) {
        this.pn = pn;
        switch(this.generateur){
            case CHAUDIERE_GAZ_CLASSIQUE_AVANT_1981:
                this.rpn=79+2*Math.log(pn);
                this.rpint=73+3*Math.log(pn);
                this.qp0=0.04*pn;
                this.puissanceVeilleuse=240;
                break;
            case CHAUDIERE_GAZ_CLASSIQUE_ENTRE_1981_ET_1986:
                this.rpn=82+2*Math.log(pn);
                this.rpint=76+3*Math.log(pn);
                this.qp0=0.02*pn;
                this.puissanceVeilleuse=150;
                break;
            case CHAUDIERE_GAZ_CLASSIQUE_ENTRE_1986_ET_1991:
                this.rpn=83+2*Math.log(pn);
                this.rpint=79+3*Math.log(pn);
                this.qp0=0.015*pn;
                this.puissanceVeilleuse=150;
                break;
            case CHAUDIERE_GAZ_STANDARD_ENTRE_1991_ET_2001:
                this.rpn=84+2*Math.log(pn);
                this.rpint=80+3*Math.log(pn);
                this.qp0=0.012*pn;
                this.puissanceVeilleuse=120;
                break;
            case CHAUDIERE_GAZ_STANDARD_APRES_2001:
                this.rpn=84+2*Math.log(pn);
                this.rpint=80+3*Math.log(pn);
                this.qp0=0.01*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_GAZ_BASSE_TEMPERATURE_AVANT_2001:
                this.rpn=87.5+1.5*Math.log(pn);
                this.rpint=87.5+1.5*Math.log(pn);
                this.qp0=0.012*pn;
                this.puissanceVeilleuse=120;
                break;
            case CHAUDIERE_GAZ_BASSE_TEMPERATURE_APRES_2001:
                this.rpn=87.5+1.5*Math.log(pn);
                this.rpint=87.5+1.5*Math.log(pn);
                this.qp0=0.01*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_GAZ_CONDENSATION_AVANT_1986:
                this.rpn=91+1*Math.log(pn);
                this.rpint=97+1*Math.log(pn);
                this.qp0=0.01*pn;
                this.puissanceVeilleuse=150;
                break;
            case CHAUDIERE_GAZ_CONDENSATION_ENTRE_1986_ET_2001:
                this.rpn=91+1*Math.log(pn);
                this.rpint=97+1*Math.log(pn);
                this.qp0=0.01*pn;
                this.puissanceVeilleuse=120;
                break;
            case CHAUDIERE_GAZ_CONDENSATION_APRES_2001:
                this.rpn=91+1*Math.log(pn);
                this.rpint=97+1*Math.log(pn);
                this.qp0=0.01*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_FIOUL_CLASSIQUE_AVANT_1970:
                this.rpn=74+2*Math.log(pn);
                this.rpint=63+3*Math.log(pn);
                this.qp0=0.04*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_FIOUL_CLASSIQUE_ENTRE_1970_ET_1976:
                this.rpn=77+2*Math.log(pn);
                this.rpint=71+3*Math.log(pn);
                this.qp0=0.03*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_FIOUL_CLASSIQUE_ENTRE_1976_ET_1981:
                this.rpn=78+2*Math.log(pn);
                this.rpint=76+3*Math.log(pn);
                this.qp0=0.02*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_FIOUL_CLASSIQUE_ENTRE_1981_ET_1991:
                this.rpn=80+2*Math.log(pn);
                this.rpint=78+3*Math.log(pn);
                this.qp0=0.01*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_FIOUL_STANDARD_APRES_1991:
                this.rpn=84+2*Math.log(pn);
                this.rpint=80+3*Math.log(pn);
                this.qp0=0.01*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_FIOUL_BASSE_TEMPERATURE:
                this.rpn=87.5+1.5*Math.log(pn);
                this.rpint=87.5+1.5*Math.log(pn);
                this.qp0=0.01*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_FIOUL_CONDENSATION:
                this.rpn=91+1*Math.log(pn);
                this.rpint=97+1*Math.log(pn);
                this.qp0=0.01*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_BOIS_PLUS_DE_15_ANS:
                this.rpn=57+6*Math.log(pn);
                this.rpint=58+6*Math.log(pn);
                this.qp0=0.07*Math.pow(pn,0.7);
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_BOIS_MOINS_DE_15_ANS:
                this.rpn=80+2*Math.log(pn);
                this.rpint=81+2*Math.log(pn);
                this.qp0=0.085*Math.pow(pn,0.6);
                this.puissanceVeilleuse=0;
                break;
            case RADIATEUR_GAZ_AVANT_2006:
                this.rpn=70;
                break;
            case RADIATEUR_GAZ_APRES_2006:
                this.rpn=80*Math.log(pn);
                break;
        }
    }
    public void setRg(double rg){
        this.rg=rg;
        this.actualiseIch();
    }
    public void setGenereEgalementEcs(boolean bool){this.genereEgalementEcs = bool;}

    @Override
    public String toString(){
        return "||Générateur = "+this.generateur.toString()+"||GenereEcs = "+this.genereEgalementEcs;
    }

}
