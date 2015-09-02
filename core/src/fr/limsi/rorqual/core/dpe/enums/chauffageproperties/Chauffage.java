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
    private boolean genereEgalementEcs;

    public Chauffage(Generateur generateur) {
        this.generateur=generateur;
        this.emission = Emission.RADIATEUR;
        this.generateurDansVolumeChauffe=false;
        this.presenceRobinetThermostatique=false;
        this.actualiseType();
        this.actualiseRd();
        this.actualiseRr();
        this.actualiseRe();
        this.actualiseRg();
        this.prs1=0;
        this.pn=0;
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
            case CHAUFFAGE_ELECTRIQUE_DIRECT_ANCIEN:
            case POMPE_A_CHALEUR_AIR_AIR:
            case POIL_FIOUL_OU_GPL:
            case POIL_OU_INSERT_BOIS_APRES_2001:
            case POIL_OU_INSERT_BOIS_AVANT_2001:
            case RADIATEUR_GAZ_AVANT_2006:
            case RADIATEUR_GAZ_APRES_2006:
                this.re=0.95f;
                break;
            case CHAUFFAGE_ELECTRIQUE_DIRECT_RECENT:
                this.re=0.96f;
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
                this.rd=0.9f;
        }
        actualiseIch();
    }
    private void actualiseRr(){
        switch (this.generateur){
            case CHAUFFAGE_ELECTRIQUE_DIRECT_ANCIEN:
            case RADIATEUR_GAZ_AVANT_2006:
            case RADIATEUR_GAZ_APRES_2006:
                this.rr=0.96f;
                break;
            case CHAUFFAGE_ELECTRIQUE_DIRECT_RECENT:
                this.rr=0.99f;
                break;
            case POIL_FIOUL_OU_GPL:
            case POIL_OU_INSERT_BOIS_APRES_2001:
            case POIL_OU_INSERT_BOIS_AVANT_2001:
                this.rr=0.8f;
                break;
            default:
                if(this.emission!=Emission.RADIATEUR){
                    this.rr=0.96f;
                }else{
                    if(this.presenceRobinetThermostatique){
                        this.rr=0.95f;
                    }else{
                        this.rr=0.9f;
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
                this.rg=0.77f;
            case POMPE_A_CHALEUR_AIR_AIR:
                this.rg=2.2f;
            case POMPE_A_CHALEUR_AIR_EAU:
                this.rg=2;
            case POMPE_A_CHALEUR_SUR_NAPPE:
                this.rg=3.2f;
            case POMPE_A_CHALEUR_GEOTHERMIQUE:
                this.rg=4;
            case POIL_OU_INSERT_BOIS_AVANT_2001:
                this.rg=0.66f;
            case POIL_OU_INSERT_BOIS_APRES_2001:
                this.rg=0.78f;
            case POIL_FIOUL_OU_GPL:
                this.rg=0.72f;
            default:
                this.rg=0.66f;
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
    public void setPn(float pn) {
        this.pn = pn;
        switch(this.generateur){
            case CHAUDIERE_GAZ_CLASSIQUE_AVANT_1981:
                this.rpn=79+2*(float)Math.log(pn);
                this.rpint=73+3*(float)Math.log(pn);
                this.qp0=0.04f*pn;
                this.puissanceVeilleuse=0.24f;
                break;
            case CHAUDIERE_GAZ_CLASSIQUE_ENTRE_1981_ET_1986:
                this.rpn=82+2*(float)Math.log(pn);
                this.rpint=76+3*(float)Math.log(pn);
                this.qp0=0.02f*pn;
                this.puissanceVeilleuse=0.15f;
                break;
            case CHAUDIERE_GAZ_CLASSIQUE_ENTRE_1986_ET_1991:
                this.rpn=83+2*(float)Math.log(pn);
                this.rpint=79+3*(float)Math.log(pn);
                this.qp0=0.015f*pn;
                this.puissanceVeilleuse=0.15f;
                break;
            case CHAUDIERE_GAZ_STANDARD_ENTRE_1991_ET_2001:
                this.rpn=84+2*(float)Math.log(pn);
                this.rpint=80+3*(float)Math.log(pn);
                this.qp0=0.012f*pn;
                this.puissanceVeilleuse=0.12f;
                break;
            case CHAUDIERE_GAZ_STANDARD_APRES_2001:
                this.rpn=84+2*(float)Math.log(pn);
                this.rpint=80+3*(float)Math.log(pn);
                this.qp0=0.01f*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_GAZ_BASSE_TEMPERATURE_AVANT_2001:
                this.rpn=87.5f+1.5f*(float)Math.log(pn);
                this.rpint=87.5f+1.5f*(float)Math.log(pn);
                this.qp0=0.012f*pn;
                this.puissanceVeilleuse=0.12f;
                break;
            case CHAUDIERE_GAZ_BASSE_TEMPERATURE_APRES_2001:
                this.rpn=87.5f+1.5f*(float)Math.log(pn);
                this.rpint=87.5f+1.5f*(float)Math.log(pn);
                this.qp0=0.01f*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_GAZ_CONDENSATION_AVANT_1986:
                this.rpn=91+1*(float)Math.log(pn);
                this.rpint=97+1*(float)Math.log(pn);
                this.qp0=0.01f*pn;
                this.puissanceVeilleuse=0.15f;
                break;
            case CHAUDIERE_GAZ_CONDENSATION_ENTRE_1986_ET_2001:
                this.rpn=91+1*(float)Math.log(pn);
                this.rpint=97+1*(float)Math.log(pn);
                this.qp0=0.01f*pn;
                this.puissanceVeilleuse=0.12f;
                break;
            case CHAUDIERE_GAZ_CONDENSATION_APRES_2001:
                this.rpn=91+1*(float)Math.log(pn);
                this.rpint=97+1*(float)Math.log(pn);
                this.qp0=0.01f*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_FIOUL_CLASSIQUE_AVANT_1970:
                this.rpn=74+2*(float)Math.log(pn);
                this.rpint=63+3*(float)Math.log(pn);
                this.qp0=0.04f*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_FIOUL_CLASSIQUE_ENTRE_1970_ET_1976:
                this.rpn=77+2*(float)Math.log(pn);
                this.rpint=71+3*(float)Math.log(pn);
                this.qp0=0.03f*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_FIOUL_CLASSIQUE_ENTRE_1976_ET_1981:
                this.rpn=78+2*(float)Math.log(pn);
                this.rpint=76+3*(float)Math.log(pn);
                this.qp0=0.02f*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_FIOUL_CLASSIQUE_ENTRE_1981_ET_1991:
                this.rpn=80+2*(float)Math.log(pn);
                this.rpint=78+3*(float)Math.log(pn);
                this.qp0=0.01f*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_FIOUL_STANDARD_APRES_1991:
                this.rpn=84+2*(float)Math.log(pn);
                this.rpint=80+3*(float)Math.log(pn);
                this.qp0=0.01f*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_FIOUL_BASSE_TEMPERATURE:
                this.rpn=87.5f+1.5f*(float)Math.log(pn);
                this.rpint=87.5f+1.5f*(float)Math.log(pn);
                this.qp0=0.01f*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_FIOUL_CONDENSATION:
                this.rpn=91+1*(float)Math.log(pn);
                this.rpint=97+1*(float)Math.log(pn);
                this.qp0=0.01f*pn;
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_BOIS_PLUS_DE_15_ANS:
                this.rpn=57+6*(float)Math.log(pn);
                this.rpint=58+6*(float)Math.log(pn);
                this.qp0=0.07f*(float)Math.pow(pn,0.7);
                this.puissanceVeilleuse=0;
                break;
            case CHAUDIERE_BOIS_MOINS_DE_15_ANS:
                this.rpn=80+2*(float)Math.log(pn);
                this.rpint=81+2*(float)Math.log(pn);
                this.qp0=0.085f*(float)Math.pow(pn,0.6);
                this.puissanceVeilleuse=0;
                break;
            case RADIATEUR_GAZ_AVANT_2006:
                this.rpn=70;
                break;
            case RADIATEUR_GAZ_APRES_2006:
                this.rpn=80*(float)Math.log(pn);
                break;
        }
    }
    public void setRg(float rg){
        this.rg=rg;
        this.actualiseIch();
    }
    public void setGenereEgalementEcs(boolean bool){this.genereEgalementEcs = bool;}

    @Override
    public String toString(){
        return "||Générateur = "+this.generateur.toString()+"||GenereEcs = "+this.genereEgalementEcs;
    }

}
