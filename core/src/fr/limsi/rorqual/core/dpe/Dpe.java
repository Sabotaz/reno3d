package fr.limsi.rorqual.core.dpe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import fr.limsi.rorqual.core.dpe.enums.chauffageproperties.*;
import fr.limsi.rorqual.core.dpe.enums.generalproperties.*;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.*;
import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.*;
import fr.limsi.rorqual.core.dpe.enums.ecsproperties.*;
import fr.limsi.rorqual.core.event.*;
import fr.limsi.rorqual.core.model.*;
import fr.limsi.rorqual.core.ui.Layout;
import fr.limsi.rorqual.core.ui.TabWindow;

public class Dpe implements EventListener {

    private HashMap<Mur, HashMap<EventType,Object>> walls_properties = new HashMap<Mur, HashMap<EventType,Object>>();
    private HashMap<Slab, HashMap<EventType,Object>> slabs_properties = new HashMap<Slab, HashMap<EventType,Object>>();
    private HashMap<Fenetre, HashMap<EventType,Object>> windows_properties = new HashMap<Fenetre, HashMap<EventType,Object>>();
    private HashMap<Porte, HashMap<EventType,Object>> doors_properties = new HashMap<Porte, HashMap<EventType,Object>>();
    private HashMap<EventType,Object> general_properties = new HashMap<EventType,Object>();
    private HashMap<EventType,Object> chauffage_properties = new HashMap<EventType,Object>();
    private HashMap<EventType,Object> ecs_properties = new HashMap<EventType,Object>();
    private Set<Object> reponses_manquantes = new HashSet<Object>();

    /*** Attributs liés au calcul du DPE ***/

    // 0.Variables générales
    private double sh = 50;
    private double consommationTotaleAnnuel=0;
    private double NIV;
    private double MIT;
    private double MIT2;
    private double FOR;
    private double Per;
    private double PER;
    private double C_niv;

    // 1.Expression du besoin de chauffage
    private double bv=8847;
    private double gv=9000; //TODO : trouver le GV défavorable en faisant plusieurs simulations ...
    private double f=0.017; //TODO : trouver le F défavorable en faisant plusieurs simulations ...

    // 2.Calcul des déperditions de l'enveloppe GV
    private double dpMur;
    private double dpToit;
    private double dpPlancher;
    private double dpFenetre;
    private double dpPorte;
    private double pontThermique;
    private double renouvellementAir;
    private double tInt;
    private double sDep;

    // 2.6.Calcul de f : on cherche à minimiser x donc à minimiser aS et aI et à maximiser GV et DHcor
    private double x=0.017; //TODO : trouver le x défavorable en faisant plusieurs simulations ...
    private double dhCor=82600;
    private double dhRef=71000;
    private double kdh=2;
    private double nRef=5800;
    private double e=340;
    private double aI=1209399000;
    private double aS=680000;
    private double sse=2; //TODO : trouver le sse défavorable en faisant plusieurs simulations ...

    // 3.Traitement de l'intermittence
    private double intermittence = 1;
    private double i0 = 1; // Cas le plus défavorable (cf partie 3)
    private double g = 72; // (9000/2.5*50)
    public void actualiseIntermittence(){
        intermittence=i0/(1+0.1*(g-1));
        this.actualiseBch();
    }
    public void actualiseG(){
        g=gv/(2.5* sh);
        this.actualiseIntermittence();
    } // TODO : prendre en compte le changement sur gv
    public void actualiseI0(Chauffage chauffagePrincipal){ // On besoin du chauffage en paramètre afin de déterminer le type (divisé ou central)
        if (general_properties.get(DpeEvent.TYPE_BATIMENT).equals(TypeBatimentEnum.MAISON)){ // Maison
            if(chauffagePrincipal.getType().equals(Chauffage.Type.DIVISE)){ // le chauffage est de type divisé
                if (chauffage_properties.get(DpeEvent.SYSTEME_PROGRAMMABLE).equals(ProgrammationSystemeEnum.POSSIBLE)){ // Systeme programmable
                    i0=0.84;
                }else{ // Systeme non programmable
                    i0 = 0.86;
                }
            }else{ // le chauffage est de type central
                if (chauffage_properties.get(DpeEvent.PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR).equals(PresenceThermostatEnum.AUCUN_DES_DEUX)){ // Sans régulation par pièce
                    if(chauffage_properties.get(DpeEvent.SYSTEME_PROGRAMMABLE).equals(ProgrammationSystemeEnum.POSSIBLE)) { // Systeme programmable
                        i0 = 0.9;
                    }else{ // Systeme non programmable
                        if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                            i0 = 0.91;
                        }else if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.RADIATEUR)){ // Radiateur
                            i0 = 0.93;
                        }else{ // Plancher chauffant ou système mixte
                            i0 = 0.94;
                        }
                    }
                }else{ // Avec régulation pièce par pièce
                    if(chauffage_properties.get(DpeEvent.SYSTEME_PROGRAMMABLE).equals(ProgrammationSystemeEnum.POSSIBLE)) { // Systeme programmable
                        i0 = 0.87;
                    }else{ // Systeme non programmable
                        if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                            i0 = 0.88;
                        }else if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.RADIATEUR)){ // Radiateur
                            i0 = 0.9;
                        }else{ // Plancher chauffant ou système mixte
                            i0 = 0.92;
                        }
                    }
                }
            }

        }else{ //Appartement
            if(chauffagePrincipal.getType().equals(Chauffage.Type.DIVISE)){ // le chauffage est de type divisé
                if (chauffage_properties.get(DpeEvent.SYSTEME_PROGRAMMABLE).equals(ProgrammationSystemeEnum.POSSIBLE)){ // Systeme programmable
                    if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                        i0 = 0.88;
                    }else if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.RADIATEUR)){ // Radiateur
                        i0 = 0.88;
                    }else{ // Plancher chauffant ou système mixte
                        i0 = 0.93;
                    }
                }else{ // Systeme non programmable
                    if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                        i0 = 0.9;
                    }else if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.RADIATEUR)){ // Radiateur
                        i0 = 0.9;
                    }else{ // Plancher chauffant ou système mixte
                        i0 = 0.95;
                    }
                }
            }else{ // le chauffage est de type central
                if (chauffage_properties.get(DpeEvent.PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR).equals(PresenceThermostatEnum.AUCUN_DES_DEUX)){ // Sans régulation par pièce
                    if(chauffage_properties.get(DpeEvent.SYSTEME_PROGRAMMABLE).equals(ProgrammationSystemeEnum.POSSIBLE)) { // Systeme programmable
                        if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                            i0 = 0.93;
                        }else if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.RADIATEUR)){ // Radiateur
                            i0 = 0.94;
                        }else{ // Plancher chauffant ou système mixte
                            i0 = 0.95;
                        }
                    }else{ // Systeme non programmable
                        if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                            i0 = 0.91;
                        }else if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.RADIATEUR)){ // Radiateur
                            i0 = 0.93;
                        }else{ // Plancher chauffant ou système mixte
                            i0 = 0.95;
                        }
                    }
                }else{ // Avec régulation pièce par pièce
                    if(chauffage_properties.get(DpeEvent.SYSTEME_PROGRAMMABLE).equals(ProgrammationSystemeEnum.POSSIBLE)) { // Systeme programmable
                        if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                            i0 = 0.89;
                        }else if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.RADIATEUR)){ // Radiateur
                            i0 = 0.91;
                        }else{ // Plancher chauffant ou système mixte
                            i0 = 0.93;
                        }
                    }else{ // Systeme non programmable
                        if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.BOUCHE_DE_SOUFFLAGE)){ // Air soufflé
                            i0 = 0.95;
                        }else if (chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.RADIATEUR)){ // Radiateur
                            i0 = 0.96;
                        }else{ // Plancher chauffant ou système mixte
                            i0 = 0.97;
                        }
                    }
                }
            }
        }
        this.actualiseIntermittence();
    }
    public void tryActualiseI0(){
        if (chauffage_properties.containsKey(DpeEvent.INSTALLATION_CHAUFFAGE)) {
            if (general_properties.containsKey(DpeEvent.TYPE_BATIMENT)) {
                if (chauffage_properties.containsKey(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR)) {
                    if (chauffage_properties.containsKey(DpeEvent.PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR)) {
                        if (chauffage_properties.containsKey(DpeEvent.SYSTEME_PROGRAMMABLE)) {
                            if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUFFAGE_UNIQUE)
                                    && chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_UNIQUE)) {
                                this.actualiseI0((Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_UNIQUE));
                            } else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUFFAGE_AVEC_POIL_OU_INSERT_BOIS)
                                    && chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_SANS_POIL)) {
                                this.actualiseI0((Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_SANS_POIL));
                            } else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_GAZ_OU_FIOUL_AVEC_CHAUDIERE_BOIS)
                                    && chauffage_properties.containsKey(DpeEvent.CHAUDIERE_GAZ_FIOUL)) {
                                this.actualiseI0((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_GAZ_FIOUL));
                            } else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_AVEC_PAC)
                                    && chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC)){
                                this.actualiseI0((Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_UNIQUE));
                            } else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_AVEC_PAC_ET_INSERT_BOIS)
                                    && chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE)){
                                this.actualiseI0((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE));
                            }
                        }
                    }
                }
            }
        }
    }

    // 4.Calcul du besoin et des consommations
    private double cch;
    private double bch;
    private double pr;
    private double prs1=0; // Cas défavorable
    private double prs2=1.05; // Cas défavorable
    private double rrp;
    public void actualiseBch(){
        bch=((bv*dhCor/1000)-pr*rrp)*intermittence; // TODO : prendre en compte le changement sur bv,dhcor
    }
    public void actualisePr(){
        pr = sh*(prs1+prs2);
        this.actualiseBch();
    }
    public void actualiseRrp(){
        rrp=(1-3.6*Math.pow(x,2.6)+2.6*Math.pow(x,1.6))/Math.pow((1-Math.pow(x,3.6)),2); // TODO : prendre en compte le changement sur x
        this.actualiseBch();
    }
    public void actualisePrs1(){
        if (ecs_properties.containsKey(DpeEvent.LOCAL_EQUIPEMENT_ECS)){
            if (ecs_properties.containsKey(DpeEvent.TYPE_EQUIPEMENT_ECS)){
                if ((ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS).equals(TypeEquipementEcsEnum.CHAUDIERE)  // L'équipement est une chaudière mixte et le dictionnaire comporte une chaudière mixte
                        && ecs_properties.containsKey(DpeEvent.CHAUDIERE_ECS))){
                    Chauffage chaudiereMixte = (Chauffage)ecs_properties.get(DpeEvent.CHAUDIERE_ECS);
                    prs1=chaudiereMixte.getPrs1();
                }else{ // L'équipement n'est pas une chaudière
                    prs1=0;
                }
                this.actualisePr();
            }
        }
    }
    public void actualisePrs2(){
        if (ecs_properties.containsKey(DpeEvent.TYPE_EQUIPEMENT_ECS)){
            if(ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS).equals(TypeEquipementEcsEnum.CHAUFFE_EAU)){ // Chauffe-eau
                prs2=2.1;
                this.actualisePr();
            }else if (ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS).equals(TypeEquipementEcsEnum.CHAUDIERE)){ // Chaudière
                prs2=1.05;
                this.actualisePr();
            }
            else { // Accumulateur ou ballon électrique
                if (ecs_properties.containsKey(DpeEvent.LOCAL_EQUIPEMENT_ECS)){
                    if (ecs_properties.get(DpeEvent.LOCAL_EQUIPEMENT_ECS).equals(LocalEquipementEcsEnum.SITUE_DANS_LOCAL_CHAUFFE)){
                        prs2=3.7;
                    }else{
                        prs2=1.05;
                    }
                    this.actualisePr();
                }
            }
        }
    }
    public void actualiseCch(){
        if(chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUFFAGE_UNIQUE)){
            Chauffage chauffage = (Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_UNIQUE);
            double ich=chauffage.getIch();
            this.cch=this.bch*ich;
        }else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUFFAGE_AVEC_POIL_OU_INSERT_BOIS)){
            Chauffage chauffage = (Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_SANS_POIL);
            Chauffage poele = (Chauffage)chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE);
            double k = (double)chauffage_properties.get(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUFFAGE);
            double ichChauffage = chauffage.getIch();
            double ichPoele = poele.getIch();
            this.cch=k*bch*ichPoele+(1-k)*bch*ichChauffage;
        }else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_GAZ_OU_FIOUL_AVEC_CHAUDIERE_BOIS)){
            Chauffage chaudiereGazFioul = (Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_GAZ_FIOUL);
            Chauffage chaudiereBois = (Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_BOIS);
            double ichChaudiereGazFioul = chaudiereGazFioul.getIch();
            double ichChaudiereBois = chaudiereBois.getIch();
            this.cch=0.75*bch*ichChaudiereBois+0.25*bch*ichChaudiereGazFioul;
        }else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_AVEC_PAC)){
            Chauffage chaudiere = (Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC);
            Chauffage pac = (Chauffage)chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE);
            double ichChaudiere = chaudiere.getIch();
            double ichPac = pac.getIch();
            this.cch=0.8*bch*ichPac+0.2*bch*ichChaudiere;
        }else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_AVEC_PAC_ET_INSERT_BOIS)){
            Chauffage chaudiere = (Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE);
            Chauffage pac = (Chauffage)chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE);
            Chauffage poele = (Chauffage)chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC);
            double k = (double)chauffage_properties.get(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC);
            double ichChaudiere = chaudiere.getIch();
            double ichPac = pac.getIch();
            double ichPoele = poele.getIch();
            this.cch=(1-k)*(0.8*bch*ichPac)+(1-k)*(0.2*bch*ichChaudiere)+k*bch*ichPoele;
        }
    } // TODO-> prendre en compte les dépendances
    public void tryActualiseCch(){ // On s'assure qu'il y ait au moins les systèmes de présent
        if(chauffage_properties.containsKey(DpeEvent.INSTALLATION_CHAUFFAGE)){
            if(chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUFFAGE_UNIQUE)){
                if(chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_UNIQUE)){
                    this.actualiseCch();
                }
            }else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUFFAGE_AVEC_POIL_OU_INSERT_BOIS)){
                if(chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_SANS_POIL) && chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE)
                        && chauffage_properties.containsKey(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUFFAGE) ){
                    this.actualiseCch();
                }
            }else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_GAZ_OU_FIOUL_AVEC_CHAUDIERE_BOIS)){
                if(chauffage_properties.containsKey(DpeEvent.CHAUDIERE_GAZ_FIOUL) && chauffage_properties.containsKey(DpeEvent.CHAUDIERE_BOIS)){
                    this.actualiseCch();
                }
            }else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_AVEC_PAC)){
                if(chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC) && chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE)){
                    this.actualiseCch();
                }
            }else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_AVEC_PAC_ET_INSERT_BOIS)){
                if(chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE) && chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE)
                        && chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC) && chauffage_properties.containsKey(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC)){
                    this.actualiseCch();
                }
            }
        }
    } //TODO -> Utiliser cela !

    // 5.Rendements des installations
    public void actualiseEmetteurAllGenerateurs(Chauffage.Emission typeEmission){
        if (chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_UNIQUE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_UNIQUE)).setEmission(typeEmission);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_SANS_POIL)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_SANS_POIL)).setEmission(typeEmission);
        }
        if (chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE)).setEmission(typeEmission);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_GAZ_FIOUL)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_GAZ_FIOUL)).setEmission(typeEmission);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_BOIS)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_BOIS)).setEmission(typeEmission);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC)).setEmission(typeEmission);
        }
        if (chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE)).setEmission(typeEmission);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE)).setEmission(typeEmission);
        }
        if (chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE)).setEmission(typeEmission);
        }
        if (chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC)).setEmission(typeEmission);
        }
        if (ecs_properties.containsKey(DpeEvent.CHAUDIERE_ECS)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_ECS)).setEmission(typeEmission);
        }
    }
    public void actualiseLocalAllGenerateurs(LocalEquipementEcsEnum localEquipementEcs){
        boolean tampon;
        if (localEquipementEcs.equals(LocalEquipementEcsEnum.SITUE_DANS_LOCAL_CHAUFFE)){
            tampon=true;
        }else{
            tampon=false;
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_UNIQUE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_UNIQUE)).setGenerateurDansVolumeChauffe(tampon);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_SANS_POIL)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_SANS_POIL)).setGenerateurDansVolumeChauffe(tampon);
        }
        if (chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE)).setGenerateurDansVolumeChauffe(tampon);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_GAZ_FIOUL)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_GAZ_FIOUL)).setGenerateurDansVolumeChauffe(tampon);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_BOIS)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_BOIS)).setGenerateurDansVolumeChauffe(tampon);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC)).setGenerateurDansVolumeChauffe(tampon);
        }
        if (chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE)).setGenerateurDansVolumeChauffe(tampon);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE)).setGenerateurDansVolumeChauffe(tampon);
        }
        if (chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE)).setGenerateurDansVolumeChauffe(tampon);
        }
        if (chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC)).setGenerateurDansVolumeChauffe(tampon);
        }
        if (ecs_properties.containsKey(DpeEvent.CHAUDIERE_ECS)){
            ((Chauffage)ecs_properties.get(DpeEvent.CHAUDIERE_ECS)).setGenerateurDansVolumeChauffe(tampon);
        }
    }
    public void actualiseRobinetAllGenerateurs(PresenceRobinetEnum presenceRobinet){
        boolean presence=true;
        if (presenceRobinet.equals(PresenceRobinetEnum.ABSENCE_ROBINET_THERMOSTATIQUE)){
            presence=false;
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_UNIQUE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_UNIQUE)).setPresenceRobinetThermostatique(presence);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_SANS_POIL)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_SANS_POIL)).setPresenceRobinetThermostatique(presence);
        }
        if (chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE)).setPresenceRobinetThermostatique(presence);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_GAZ_FIOUL)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_GAZ_FIOUL)).setPresenceRobinetThermostatique(presence);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_BOIS)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_BOIS)).setPresenceRobinetThermostatique(presence);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC)).setPresenceRobinetThermostatique(presence);
        }
        if (chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE)).setPresenceRobinetThermostatique(presence);
        }
        if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE)).setPresenceRobinetThermostatique(presence);
        }
        if (chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE)).setPresenceRobinetThermostatique(presence);
        }
        if (chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC)){
            ((Chauffage)chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC)).setPresenceRobinetThermostatique(presence);
        }
        if (ecs_properties.containsKey(DpeEvent.CHAUDIERE_ECS)){
            ((Chauffage)ecs_properties.get(DpeEvent.CHAUDIERE_ECS)).setPresenceRobinetThermostatique(presence);
        }
    }
    public void actualiseRendementsChauffage(Chauffage chauffage){
        boolean situeDansLocalChauffe=false; // Initialisation défavorable
        Chauffage.Emission typeEmission = Chauffage.Emission.RADIATEUR; // Initialisation défavorable
        boolean presenceRobinetThermostatique=true; // Initialisation défavorable
        if (ecs_properties.containsKey(DpeEvent.LOCAL_EQUIPEMENT_ECS)){
            if(ecs_properties.get(DpeEvent.LOCAL_EQUIPEMENT_ECS).equals(LocalEquipementEcsEnum.SITUE_DANS_LOCAL_CHAUFFE)) {
                situeDansLocalChauffe = true;
            }
        }
        if (ecs_properties.containsKey(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR)){
            typeEmission=(Chauffage.Emission)ecs_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR);
        }
        if (ecs_properties.containsKey(DpeEvent.PRESENCE_ROBINET_THERMOSTATIQUE)){
            if (ecs_properties.get(DpeEvent.PRESENCE_ROBINET_THERMOSTATIQUE).equals(PresenceRobinetEnum.ABSENCE_ROBINET_THERMOSTATIQUE)){
                presenceRobinetThermostatique=false;
            }
        }
        chauffage.setEmission(typeEmission);
        chauffage.setGenerateurDansVolumeChauffe(situeDansLocalChauffe);
        chauffage.setPresenceRobinetThermostatique(presenceRobinetThermostatique);
    }

    // 6.Rendement de génération des chaudières
    private int tabTauxDeCharge[] = new int[9];
    private double tabCoeffPondX[] = new double[9];
    public void actualiseRendementGeneration(Chauffage chauffage){
        double tInt=23; // Cas défavorable
        double tExtBase=-15; // Cas défavorable
        double pch;
        double pecs=2.844923077;
        double pDim;
        double pn=0;
        double rr=chauffage.getRr();
        double rd=chauffage.getRd();
        double re=chauffage.getRe();
        double rg;
        double cdimRef;
        double tabTchxFinal[] = new double [9];
        double tabQpx[] = new double [9];
        double tfonc100 = this.getTfonc100();
        double tfonc30;
        double qp0,qp15,qp30,qp50,qp100;
        double pmfou=0;
        double pmcons=0;
        boolean haveRegulation=false;

        if (general_properties.get(DpeEvent.PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR).equals(PresenceThermostatEnum.PRESENCE_THERMOSTAT_OU_SONDE)){
            haveRegulation=true;
        }

        if (general_properties.containsKey(DpeEvent.DEPARTEMENT_BATIMENT)){
            tExtBase=((DepartementBatimentEnum)general_properties.get(DpeEvent.DEPARTEMENT_BATIMENT)).getTempExtBase();
        }
        if(chauffage_properties.containsKey(DpeEvent.TEMPERATURE_INTERIEUR)){
            tInt=((TemperatureInterieurEnum)chauffage_properties.get(DpeEvent.TEMPERATURE_INTERIEUR)).getTemperatureInterieure();
        }
        pch=1.2*gv*(tInt-tExtBase)/(1000*rr*rd*re);
        pDim=pch;

        if (chauffage.getGenerateur().equals(Chauffage.Generateur.RADIATEUR_GAZ_AVANT_2006)
                || chauffage.getGenerateur().equals(Chauffage.Generateur.RADIATEUR_GAZ_APRES_2006)){
            int n = (int) sh/12;
            pn=pDim/n;
            chauffage.setPn(pn);
        }else{
            if(chauffage.getGenereEgalementEcs()){
                if(ecs_properties.containsKey(DpeEvent.DECLENCHEMENT_CHAUDIERE_ROBINET)){
                    if(ecs_properties.get(DpeEvent.DECLENCHEMENT_CHAUDIERE_ROBINET).equals(DeclenchementChaudiereEnum.DECLENCHEMENT_OUVERTURE_ROBINET_EAU_CHAUDE)){
                        pecs=21;
                    }
                }
                pDim=Math.max(pch,pecs);
            }
            if (pDim<=5){
                pn=5;
                chauffage.setPn(pn);
            }else if (pDim>5 && pDim<=10){
                pn=10;
                chauffage.setPn(pn);
            }else if (pDim>10 && pDim<=13){
                pn=13;
                chauffage.setPn(pn);
            }else if (pDim>13 && pDim<=18){
                pn=18;
                chauffage.setPn(pn);
            }else if (pDim>18 && pDim<=24){
                pn=24;
                chauffage.setPn(pn);
            }else if (pDim>24 && pDim<=28){
                pn=28;
                chauffage.setPn(pn);
            }else if (pDim>28 && pDim<=32){
                pn=32;
                chauffage.setPn(pn);
            }else if (pDim>32 && pDim<=40){
                pn=40;
                chauffage.setPn(pn);
            }else if (pDim>40){
                pn = (((int)(pDim / 5)) + 1)*5;
                chauffage.setPn(pn);
            }
        }

        cdimRef=1000*pn/gv*(tInt-tExtBase);
        for(int i=0;i<9;i++){
            tabTchxFinal[i] = tabTauxDeCharge[i]/cdimRef;
        }
        tabTchxFinal[9]=tabTauxDeCharge[9];

        for (int i=0;i<9;i++){
            pmfou += pn*tabTchxFinal[i]*tabCoeffPondX[i];
        }

        double rpint = chauffage.getRpint();
        double rpn = chauffage.getRpn();
        double pveil = chauffage.getPuissanceVeilleuse();
        qp0=chauffage.getQp0();

        switch(chauffage.getGenerateur()){
            case CHAUDIERE_GAZ_BASSE_TEMPERATURE_AVANT_2001:
            case CHAUDIERE_GAZ_BASSE_TEMPERATURE_APRES_2001:
            case CHAUDIERE_FIOUL_BASSE_TEMPERATURE:
                tfonc30=this.getTfonc30ChaudiereBasseTemperature();
                if(haveRegulation){
                    qp30=(0.3*pn*(100-(rpint+0.1*(40-tfonc30))))/(rpint+0.1*(40-tfonc30));
                }else{
                    qp30=(0.3*pn*(100-(rpint+0.1*(40-tfonc100))))/(rpint+0.1*(40-tfonc100));
                }
                qp15=qp30/2;
                qp100=(pn*(100-(rpn+0.1*(70-tfonc100))))/(rpn+0.1*(70-tfonc100));
                for (int i=0;i<9;i++){
                    if(i<2){ // Entre 0 et 15% de charge
                        tabQpx[i] = ((qp15-0.15*qp0)*tabTchxFinal[i]/0.15)+0.15*qp0;
                    }else if (i==2){ // Entre 15 et 30% de charge
                        tabQpx[i] = ((qp30-qp15)/0.15)*tabTchxFinal[i]+qp15-((qp30-qp15)/0.15)*0.15;
                    }else if (i>2){ // Entre 30 et 100% de charge
                        tabQpx[i] = ((qp100-qp30)/0.7)*tabTchxFinal[i]+qp30-((qp100-qp30)/0.7)*0.3;
                    }
                }
                break;

            case CHAUDIERE_GAZ_CONDENSATION_AVANT_1986:
            case CHAUDIERE_GAZ_CONDENSATION_ENTRE_1986_ET_2001:
            case CHAUDIERE_GAZ_CONDENSATION_APRES_2001:
            case CHAUDIERE_FIOUL_CONDENSATION:
                tfonc30=this.getTfonc30ChaudiereCondensation();
                if(haveRegulation){
                    qp30=(0.3*pn*(100-(rpint+0.2*(33-tfonc30))))/(rpint+0.2*(33-tfonc30));
                }else{
                    qp30=(0.3*pn*(100-(rpint+0.2*(33-tfonc100))))/(rpint+0.2*(33-tfonc100));
                }
                qp15=qp30/2;
                qp100=(pn*(100-(rpn+0.1*(70-tfonc100))))/(rpn+0.1*(70-tfonc100));
                for (int i=0;i<9;i++){
                    if(i<2){ // Entre 0 et 15% de charge
                        tabQpx[i] = ((qp15-0.15*qp0)*tabTchxFinal[i]/0.15)+0.15*qp0;
                    }else if (i==2){ // Entre 15 et 30% de charge
                        tabQpx[i] = ((qp30-qp15)/0.15)*tabTchxFinal[i]+qp15-((qp30-qp15)/0.15)*0.15;
                    }else if (i>2){ // Entre 30 et 100% de charge
                        tabQpx[i] = ((qp100-qp30)/0.7)*tabTchxFinal[i]+qp30-((qp100-qp30)/0.7)*0.3;
                    }
                }
                break;

            case CHAUDIERE_GAZ_CLASSIQUE_AVANT_1981:
            case CHAUDIERE_GAZ_CLASSIQUE_ENTRE_1981_ET_1986:
            case CHAUDIERE_GAZ_CLASSIQUE_ENTRE_1986_ET_1991:
            case CHAUDIERE_FIOUL_CLASSIQUE_AVANT_1970:
            case CHAUDIERE_FIOUL_CLASSIQUE_ENTRE_1970_ET_1976:
            case CHAUDIERE_FIOUL_CLASSIQUE_ENTRE_1976_ET_1981:
            case CHAUDIERE_FIOUL_CLASSIQUE_ENTRE_1981_ET_1991:
                tfonc30=this.getTfonc30ChaudiereStandardAvant1990();
                if(haveRegulation){
                    qp30=(0.3*pn*(100-(rpint+0.1*(50-tfonc30))))/(rpint+0.1*(50-tfonc30));
                }else{
                    qp30=(0.3*pn*(100-(rpint+0.1*(50-tfonc100))))/(rpint+0.1*(50-tfonc100));
                }
                qp100=(pn*(100-(rpn+0.1*(70-tfonc100))))/(rpn+0.1*(70-tfonc100));
                for (int i=0;i<9;i++){
                    if(i<=2){ // Entre 0 et 30% de charge
                        tabQpx[i] = ((qp30-0.15*qp0)*tabTchxFinal[i]/0.3)+0.15*qp0;
                    }else if (i>2){ // Entre 30 et 100% de charge
                        tabQpx[i] = ((qp100-qp30)/0.7)*tabTchxFinal[i]+qp30-((qp100-qp30)/0.7)*0.3;
                    }
                }
                break;

            case CHAUDIERE_GAZ_STANDARD_ENTRE_1991_ET_2001:
            case CHAUDIERE_GAZ_STANDARD_APRES_2001:
            case CHAUDIERE_FIOUL_STANDARD_APRES_1991:
                tfonc30=this.getTfonc30ChaudiereStandardApres1991();
                if(haveRegulation){
                    qp30=(0.3*pn*(100-(rpint+0.1*(50-tfonc30))))/(rpint+0.1*(50-tfonc30));
                }else{
                    qp30=(0.3*pn*(100-(rpint+0.1*(50-tfonc100))))/(rpint+0.1*(50-tfonc100));
                }
                qp100=(pn*(100-(rpn+0.1*(70-tfonc100))))/(rpn+0.1*(70-tfonc100));
                for (int i=0;i<9;i++){
                    if(i<=2){ // Entre 0 et 30% de charge
                        tabQpx[i] = ((qp30-0.15*qp0)*tabTchxFinal[i]/0.3)+0.15*qp0;
                    }else if (i>2){ // Entre 30 et 100% de charge
                        tabQpx[i] = ((qp100-qp30)/0.7)*tabTchxFinal[i]+qp30-((qp100-qp30)/0.7)*0.3;
                    }
                }
                break;

            case CHAUDIERE_BOIS_PLUS_DE_15_ANS:
            case CHAUDIERE_BOIS_MOINS_DE_15_ANS:
                qp50=0.5*pn*(100-rpint)/rpint;
                qp100=pn*(100-rpn)/rpn;
                for (int i=0;i<9;i++){
                    if(i<=4){ // Entre 0 et 50% de charge
                        tabQpx[i] = ((qp50-qp0)/0.5)*tabTchxFinal[i]+qp0;
                    }else if (i>4){ // Entre 50 et 100% de charge
                        tabQpx[i] = ((qp100-qp50)/0.5)*tabTchxFinal[i]+2*qp50-qp100;
                    }
                }
                break;

            case RADIATEUR_GAZ_AVANT_2006:
            case RADIATEUR_GAZ_APRES_2006:
                for (int i=0;i<9;i++){
                    tabQpx[i]=((100-rpn)/rpn)*pn*tabTchxFinal[i];
                }
                break;
        }
        for (int i=0;i<9;i++){
            pmcons += pn*tabTchxFinal[i]*tabCoeffPondX[i]*((pn*tabTchxFinal[i]+tabQpx[i])/(pn*tabTchxFinal[i]));
        }
        rg = pmfou/(pmcons+0.3*0.15*qp0+pveil);
        chauffage.setRg(rg);
    }
    public double getTfonc100(){
        double tFonc100 = 80;
        if (general_properties.containsKey(DpeEvent.ANNEE_CONSTRUCTION)){
            if(chauffage_properties.containsKey(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR)){
                switch((DateConstructionBatimentEnum)general_properties.get(DpeEvent.ANNEE_CONSTRUCTION)){
                    case AVANT_1975:
                    case ENTRE_1975_ET_1977:
                    case ENTRE_1978_ET_1982:
                        if(chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.PLANCHER_CHAUFFANT)){
                            tFonc100 = 60;
                        }else{
                            tFonc100 = 80;
                        }
                        break;
                    default:
                        if(chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.PLANCHER_CHAUFFANT)){
                            tFonc100 = 35;
                        }else{
                            tFonc100 = 65;
                        }
                        break;
                }
            }
        }
        return tFonc100;
    }
    public double getTfonc30ChaudiereCondensation(){
        double tFonc30 = 38;
        if (general_properties.containsKey(DpeEvent.ANNEE_CONSTRUCTION)){
            if(chauffage_properties.containsKey(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR)){
                switch((DateConstructionBatimentEnum)general_properties.get(DpeEvent.ANNEE_CONSTRUCTION)){
                    case AVANT_1975:
                    case ENTRE_1975_ET_1977:
                    case ENTRE_1978_ET_1982:
                        if(chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.PLANCHER_CHAUFFANT)){
                            tFonc30 = 32;
                        }else{
                            tFonc30 = 38;
                        }
                        break;
                    default:
                        if(chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.PLANCHER_CHAUFFANT)){
                            tFonc30 = 24.5;
                        }else{
                            tFonc30 = 34;
                        }
                        break;
                }
            }
        }
        return tFonc30;
    }
    public double getTfonc30ChaudiereBasseTemperature(){
        double tFonc30 = 48.5;
        if (general_properties.containsKey(DpeEvent.ANNEE_CONSTRUCTION)){
            if(chauffage_properties.containsKey(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR)){
                switch((DateConstructionBatimentEnum)general_properties.get(DpeEvent.ANNEE_CONSTRUCTION)){
                    case AVANT_1975:
                    case ENTRE_1975_ET_1977:
                    case ENTRE_1978_ET_1982:
                        if(chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.PLANCHER_CHAUFFANT)){
                            tFonc30 = 42.5;
                        }else{
                            tFonc30 = 48.5;
                        }
                        break;
                    default:
                        if(chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.PLANCHER_CHAUFFANT)){
                            tFonc30 = 35;
                        }else{
                            tFonc30 = 44;
                        }
                        break;
                }
            }
        }
        return tFonc30;
    }
    public double getTfonc30ChaudiereStandardAvant1990(){
        double tFonc30 = 59;
        if (general_properties.containsKey(DpeEvent.ANNEE_CONSTRUCTION)){
            if(chauffage_properties.containsKey(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR)){
                switch((DateConstructionBatimentEnum)general_properties.get(DpeEvent.ANNEE_CONSTRUCTION)){
                    case AVANT_1975:
                    case ENTRE_1975_ET_1977:
                    case ENTRE_1978_ET_1982:
                        if(chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.PLANCHER_CHAUFFANT)){
                            tFonc30 = 53;
                        }else{
                            tFonc30 = 59;
                        }
                        break;
                    default:
                        if(chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.PLANCHER_CHAUFFANT)){
                            tFonc30 = 50;
                        }else{
                            tFonc30 = 54.5;
                        }
                        break;
                }
            }
        }
        return tFonc30;
    }
    public double getTfonc30ChaudiereStandardApres1991(){
        double tFonc30 = 55.5;
        if (general_properties.containsKey(DpeEvent.ANNEE_CONSTRUCTION)){
            if(chauffage_properties.containsKey(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR)){
                switch((DateConstructionBatimentEnum)general_properties.get(DpeEvent.ANNEE_CONSTRUCTION)){
                    case AVANT_1975:
                    case ENTRE_1975_ET_1977:
                    case ENTRE_1978_ET_1982:
                        if(chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.PLANCHER_CHAUFFANT)){
                            tFonc30 = 49.5;
                        }else{
                            tFonc30 = 55.5;
                        }
                        break;
                    default:
                        if(chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR).equals(TypeEmetteurEnum.PLANCHER_CHAUFFANT)){
                            tFonc30 = 45;
                        }else{
                            tFonc30 = 51.5;
                        }
                        break;
                }
            }
        }
        return tFonc30;
    }

    // 7.Expression du besoin de la consommation d'ECS
    private double bEcs;
    private double nbJoursAbsenceParAn;
    private double nbHabitant;
    private double cEcs;
    private double iEcs;
    private double fEcs;

    // 8.Rendements de l'installation d'ECS


    // 9.Consommation de climatisation
    private double cClimatisation=700; // Cas le plus défavorable (7*100)
    private double rClimatisation=7; // Cas le plus défavorable (voir 9)
    private double sClimatisation=100; // On considère que l'on climatise une grande surface
    public void actualiseResistanceClim(){
        if (general_properties.containsKey(DpeEvent.DEPARTEMENT_BATIMENT)){
            DepartementBatimentEnum.ZoneEte zoneEte = ((DepartementBatimentEnum) general_properties.get(DpeEvent.DEPARTEMENT_BATIMENT)).getZoneEte();
            switch(zoneEte){
                case Ea:
                    if (sClimatisation<150){rClimatisation=2;}
                    else{rClimatisation=4;}
                    break;
                case Eb:
                    if (sClimatisation<150){rClimatisation=3;}
                    else{rClimatisation=5;}
                    break;
                case Ec:
                    if (sClimatisation<150){rClimatisation=4;}
                    else{rClimatisation=6;}
                    break;
                case Ed:
                    if (sClimatisation<150){rClimatisation=5;}
                    else{rClimatisation=7;}
                    break;
            }
        }else{
            rClimatisation=7;
        }
        actualiseConsommationClimatisation();
    }
    public void actualiseConsommationClimatisation(){
        if (general_properties.containsKey(DpeEvent.CLIMATISATION_LOGEMENT)){
            if ((general_properties.get(DpeEvent.CLIMATISATION_LOGEMENT)).equals(PresenceClimatisationLogementEnum.NON)){
                cClimatisation=0;
            }else{
                cClimatisation = rClimatisation * sClimatisation;
            }
        }else{
            cClimatisation=700;
        }
        this.actualiseConsommationTotaleAnnuel();
        System.out.println("cClimatisation = " + cClimatisation + " rClimatisation = " + rClimatisation + " sClimatisation = " + sClimatisation);
    }

    // 10.Concommation des usages spécifiques
    private double cElectromenager=1906; // Comme si on possède tous les éléments électroménager (voir 10.3)
    private double cEclairageSurfacique = 3.7; // Consommation max annuel des lampes (voir 10.2)
    private double cEclairage = 185; // sh max * cEclairageSurfacique max = 50*3.7
    private double cCuisson = 1660; // Conso cuisson max (cf 10.1)
    public void actualiseConsommationEclairageSurfacique() {
        if (general_properties.containsKey(DpeEvent.EQUIPEMENT_ECLAIRAGE)){
            TypeEquipementEclairageEnum equipementEclairage = (TypeEquipementEclairageEnum)general_properties.get(DpeEvent.EQUIPEMENT_ECLAIRAGE);
            cEclairageSurfacique=equipementEclairage.getConsommationEclairage();
        }else{
            cEclairageSurfacique=3.7;
        }
        this.actualiseConsommationEclairage();
    }
    public void actualiseConsommationEclairage(){
        cEclairage=cEclairageSurfacique * sh;
        System.out.println("cEclairage = " + cEclairage + " cEclairageSurfacique = " + cEclairageSurfacique + " sh = " + sh);
    }
    public void actualiseConsommationElectromenager(){
        if(general_properties.containsKey(DpeEvent.EQUIPEMENT_ELECTROMENAGER)){
            cElectromenager=0;
            ArrayList<TypeEquipementElectromenagerEnum> listEquipement = (ArrayList<TypeEquipementElectromenagerEnum>)general_properties.get(DpeEvent.EQUIPEMENT_ELECTROMENAGER);
            for (TypeEquipementElectromenagerEnum actualEquipement : listEquipement){
                cElectromenager+=actualEquipement.getConsommation();
            }
        }
        System.out.println("cElectromenager = " + cElectromenager);
    }
    public void actualiseConsommationCuisson(){
        if (general_properties.containsKey(DpeEvent.EQUIPEMENT_CUISSON)){
            TypeEquipementCuissonEnum equipementCuisson = (TypeEquipementCuissonEnum)general_properties.get(DpeEvent.EQUIPEMENT_CUISSON);
            cCuisson = equipementCuisson.getConsommation();
        }else{
            cCuisson=1660;
        }
        this.actualiseConsommationTotaleAnnuel();
        System.out.println("cCuisson = " + cCuisson);
    }

    /*** Constructeur ***/
    public Dpe () {
        EventManager.getInstance().addListener(Channel.DPE, this);
        tabCoeffPondX[0]=0.1;
        tabCoeffPondX[1]=0.25;
        tabCoeffPondX[2]=0.2;
        tabCoeffPondX[3]=0.15;
        tabCoeffPondX[4]=0.1;
        tabCoeffPondX[5]=0.1;
        tabCoeffPondX[6]=0.05;
        tabCoeffPondX[7]=0.025;
        tabCoeffPondX[8]=0.025;
        tabCoeffPondX[9]=0;
        int tampon=-5;
        for(int i=0;i<tabTauxDeCharge.length;i++){
            tampon+=10;
            tabTauxDeCharge[i]=tampon;
        }
    }

    /*---------------------------------Calculateur DPE-------------------------------------------*/

    public void actualiseConsommationTotaleAnnuel(){
        consommationTotaleAnnuel = cElectromenager+cEclairage+cCuisson+cClimatisation;
//        System.out.println("consommationTotaleAnnuel = "+consommationTotaleAnnuel+" cElectromenager = "+cElectromenager+" cEclairage = "+cEclairage+" cCuisson = "+cCuisson+" cClimatisation = "+cClimatisation);
    }


    public void notify(Channel c, Event e) throws InterruptedException {

        EventType eventType = e.getEventType();
        if (c == Channel.DPE) {
            if (eventType instanceof DpeEvent) {
                DpeEvent event = (DpeEvent) eventType;
                Object o = e.getUserObject();
                switch (event) {
                    case TYPE_BATIMENT: {

                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        EventRequest eventRequest = (EventRequest) items.get("eventRequest");
                        Layout layout = (Layout) items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeBatimentEnum typeBatiment = (TypeBatimentEnum) items.get("lastValue");
                            if (typeBatiment == TypeBatimentEnum.MAISON) {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("maison"), true);
                            } else {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("maison"), false);
                            }
                            general_properties.put(DpeEvent.TYPE_BATIMENT, typeBatiment);
                            this.tryActualiseI0();

                        } else if (eventRequest == EventRequest.GET_STATE) {

                            TypeBatimentEnum type = (TypeBatimentEnum) general_properties.get(DpeEvent.TYPE_BATIMENT);

                            HashMap<String, Object> currentItems = new HashMap<String, Object>();
                            currentItems.put("lastValue", type);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_BATIMENT, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);

                            // wait for layout to be  populated

                            while (!layout.isInitialised()) {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException ie) {

                                }
                            }
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("maison"), false);
                        }
                        break;
                    }

                    case CATEGORIE_BATIMENT: {
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        EventRequest eventRequest = (EventRequest) items.get("eventRequest");
                        Layout layout = (Layout) items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            CategorieLogementEnum categorieLogement = (CategorieLogementEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.CATEGORIE_BATIMENT, categorieLogement);

                        } else if (eventRequest == EventRequest.GET_STATE) {
                            CategorieLogementEnum type = null;
                            if (general_properties.containsKey(DpeEvent.CATEGORIE_BATIMENT)){
                                type = (CategorieLogementEnum) general_properties.get(DpeEvent.CATEGORIE_BATIMENT);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue", type);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CATEGORIE_BATIMENT, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case SURFACE_HABITABLE: {
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        EventRequest eventRequest = (EventRequest) items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            sh = (double) items.get("lastValue");
                            general_properties.put(DpeEvent.SURFACE_HABITABLE, sh);

                            // On actualise toutes les données où la surface habitable entre en compte
                            this.actualiseConsommationEclairage();
                            this.actualiseG();
                            this.actualisePr();

                        } else if (eventRequest == EventRequest.GET_STATE) {
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue", sh);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.SURFACE_HABITABLE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case FORME_MAISON: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            FormeMaisonEnum formeMaison = (FormeMaisonEnum) items.get("lastValue");
                            if (formeMaison.equals(FormeMaisonEnum.CARRE)){FOR = 4.12;}
                            else if (formeMaison.equals(FormeMaisonEnum.ALLONGE)){FOR = 4.81;}
                            else if (formeMaison.equals(FormeMaisonEnum.DEVELOPPE)){FOR = 5.71;}
                            general_properties.put(DpeEvent.FORME_MAISON, formeMaison);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            FormeMaisonEnum type = null;
                            if (general_properties.containsKey(DpeEvent.FORME_MAISON)){
                                type = (FormeMaisonEnum) general_properties.get(DpeEvent.FORME_MAISON);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.FORME_MAISON, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case MITOYENNETE_MAISON: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            MitoyenneteMaisonEnum mitoyenneteMaison = (MitoyenneteMaisonEnum) items.get("lastValue");
                            if (mitoyenneteMaison.equals(MitoyenneteMaisonEnum.NON_ACCOLE)){MIT = 1;}
                            else if (mitoyenneteMaison.equals(MitoyenneteMaisonEnum.ACCOLE_PETIT_COTE)){MIT = 0.8;}
                            else if (mitoyenneteMaison.equals(MitoyenneteMaisonEnum.ACCOLE_GRAND_OU_DEUX_PETITS_COTES)){MIT = 0.7;}
                            else if (mitoyenneteMaison.equals(MitoyenneteMaisonEnum.ACCOLE_GRAND_ET_UN_PETIT_COTE)){MIT = 0.5;}
                            else if (mitoyenneteMaison.equals(MitoyenneteMaisonEnum.ACCOLE_DEUX_GRANDS_COTES)){MIT = 0.35;}
                            general_properties.put(DpeEvent.MITOYENNETE_MAISON, mitoyenneteMaison);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            MitoyenneteMaisonEnum type = null;
                            if (general_properties.containsKey(DpeEvent.MITOYENNETE_MAISON)){
                                type = (MitoyenneteMaisonEnum) general_properties.get(DpeEvent.MITOYENNETE_MAISON);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.MITOYENNETE_MAISON, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case DEPARTEMENT_BATIMENT:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            DepartementBatimentEnum departement = (DepartementBatimentEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.DEPARTEMENT_BATIMENT, departement);
                            this.actualiseResistanceClim();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            DepartementBatimentEnum type = null;
                            if (general_properties.containsKey(DpeEvent.DEPARTEMENT_BATIMENT)){
                                type = (DepartementBatimentEnum) general_properties.get(DpeEvent.DEPARTEMENT_BATIMENT);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.DEPARTEMENT_BATIMENT, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case POSITION_APPARTEMENT:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            PositionAppartementEnum positionAppartement = (PositionAppartementEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.POSITION_APPARTEMENT, positionAppartement);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            PositionAppartementEnum type = null;
                            if (general_properties.containsKey(DpeEvent.POSITION_APPARTEMENT)){
                                type = (PositionAppartementEnum) general_properties.get(DpeEvent.POSITION_APPARTEMENT);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.POSITION_APPARTEMENT, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case ANNEE_CONSTRUCTION: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            DateConstructionBatimentEnum dateConstructionBatiment = (DateConstructionBatimentEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.ANNEE_CONSTRUCTION, dateConstructionBatiment);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            DateConstructionBatimentEnum type = null;
                            if (general_properties.containsKey(DpeEvent.ANNEE_CONSTRUCTION)){
                                type = (DateConstructionBatimentEnum) general_properties.get(DpeEvent.ANNEE_CONSTRUCTION);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.ANNEE_CONSTRUCTION, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case ENERGIE_CONSTRUCTION: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeEnergieConstructionEnum typeEnergieConstruction = (TypeEnergieConstructionEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.ENERGIE_CONSTRUCTION, typeEnergieConstruction);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeEnergieConstructionEnum type = null;
                            if (general_properties.containsKey(DpeEvent.ENERGIE_CONSTRUCTION)){
                                type = (TypeEnergieConstructionEnum) general_properties.get(DpeEvent.ENERGIE_CONSTRUCTION);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.ENERGIE_CONSTRUCTION, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case ABONNEMENT_ELECTRIQUE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeAbonnementElectriqueEnum typeAbonnementElectrique = (TypeAbonnementElectriqueEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.ABONNEMENT_ELECTRIQUE, typeAbonnementElectrique);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeAbonnementElectriqueEnum type = null;
                            if (general_properties.containsKey(DpeEvent.ABONNEMENT_ELECTRIQUE)){
                                type = (TypeAbonnementElectriqueEnum) general_properties.get(DpeEvent.ABONNEMENT_ELECTRIQUE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.ABONNEMENT_ELECTRIQUE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case CLIMATISATION_LOGEMENT: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            PresenceClimatisationLogementEnum presenceClimatisationLogement = (PresenceClimatisationLogementEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.CLIMATISATION_LOGEMENT, presenceClimatisationLogement);
                            this.actualiseConsommationClimatisation();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            PresenceClimatisationLogementEnum type = null;
                            if (general_properties.containsKey(DpeEvent.CLIMATISATION_LOGEMENT)){
                                type = (PresenceClimatisationLogementEnum) general_properties.get(DpeEvent.CLIMATISATION_LOGEMENT);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CLIMATISATION_LOGEMENT, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case SURFACE_CLIMATISATION: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            this.sClimatisation = (double) items.get("lastValue");
                            general_properties.put(DpeEvent.SURFACE_CLIMATISATION, sClimatisation);
                            this.actualiseResistanceClim();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            PresenceClimatisationLogementEnum type = null;
                            if (general_properties.containsKey(DpeEvent.SURFACE_CLIMATISATION)){
                                type = (PresenceClimatisationLogementEnum) general_properties.get(DpeEvent.SURFACE_CLIMATISATION);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.SURFACE_CLIMATISATION, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case EQUIPEMENT_ECLAIRAGE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeEquipementEclairageEnum equipementEclairage = (TypeEquipementEclairageEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.EQUIPEMENT_ECLAIRAGE, equipementEclairage);
                            this.actualiseConsommationEclairageSurfacique();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeEquipementEclairageEnum type = null;
                            if (general_properties.containsKey(DpeEvent.EQUIPEMENT_ECLAIRAGE)){
                                type = (TypeEquipementEclairageEnum) general_properties.get(DpeEvent.EQUIPEMENT_ECLAIRAGE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.EQUIPEMENT_ECLAIRAGE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case EQUIPEMENT_ELECTROMENAGER: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        ArrayList<TypeEquipementElectromenagerEnum> listAppareils = new ArrayList<TypeEquipementElectromenagerEnum>();
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeEquipementElectromenagerEnum equipementElectromenager = (TypeEquipementElectromenagerEnum) items.get("lastValue");
                            if (general_properties.containsKey(DpeEvent.EQUIPEMENT_ELECTROMENAGER)){
                                listAppareils = (ArrayList) general_properties.get(DpeEvent.EQUIPEMENT_ELECTROMENAGER);
                                if (!listAppareils.contains(equipementElectromenager)){
                                    listAppareils.add(equipementElectromenager);
                                }else{
                                    listAppareils.remove(equipementElectromenager);
                                }
                            }else{
                                listAppareils.add(equipementElectromenager);
                            }
                            general_properties.put(DpeEvent.EQUIPEMENT_ELECTROMENAGER, listAppareils);
                            this.actualiseConsommationElectromenager();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeEquipementElectromenagerEnum type = null;
                            if (general_properties.containsKey(DpeEvent.EQUIPEMENT_ELECTROMENAGER)){
                                type = (TypeEquipementElectromenagerEnum) general_properties.get(DpeEvent.EQUIPEMENT_ELECTROMENAGER);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.EQUIPEMENT_ELECTROMENAGER, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case EQUIPEMENT_CUISSON: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeEquipementCuissonEnum equipementCuisson = (TypeEquipementCuissonEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.EQUIPEMENT_CUISSON, equipementCuisson);
                            this.actualiseConsommationCuisson();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeEquipementCuissonEnum type = null;
                            if (general_properties.containsKey(DpeEvent.EQUIPEMENT_CUISSON)){
                                type = (TypeEquipementCuissonEnum) general_properties.get(DpeEvent.EQUIPEMENT_CUISSON);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.EQUIPEMENT_CUISSON, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_VENTILATION: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeVentilationEnum typeVentilation = (TypeVentilationEnum) items.get("lastValue");
                            general_properties.put(DpeEvent.TYPE_VENTILATION, typeVentilation);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeVentilationEnum type = null;
                            if (general_properties.containsKey(DpeEvent.TYPE_VENTILATION)){
                                type = (TypeVentilationEnum) general_properties.get(DpeEvent.TYPE_VENTILATION);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_VENTILATION, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case NOMBRE_PERSONNES_DANS_LOGEMENT:{
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        EventRequest eventRequest = (EventRequest) items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            nbHabitant = (double) items.get("lastValue");
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            HashMap<String, Object> currentItems = new HashMap<String, Object>();
                            currentItems.put("lastValue", nbHabitant);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.NOMBRE_PERSONNES_DANS_LOGEMENT, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case NOMBRE_JOURS_ABSENCE:{
                        HashMap<String, Object> items = (HashMap<String, Object>) o;
                        EventRequest eventRequest = (EventRequest) items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            nbJoursAbsenceParAn = (double) items.get("lastValue");
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            HashMap<String, Object> currentItems = new HashMap<String, Object>();
                            currentItems.put("lastValue", nbJoursAbsenceParAn);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.NOMBRE_JOURS_ABSENCE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case INSTALLATION_CHAUFFAGE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        Layout layout = (Layout) items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            InstallationChauffageEnum typeSource = (InstallationChauffageEnum) items.get("lastValue");
                            if (typeSource == InstallationChauffageEnum.CHAUFFAGE_UNIQUE) {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), false);
                            } else if (typeSource == InstallationChauffageEnum.CHAUFFAGE_AVEC_POIL_OU_INSERT_BOIS){
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), false);
                            } else if (typeSource == InstallationChauffageEnum.CHAUDIERE_GAZ_OU_FIOUL_AVEC_CHAUDIERE_BOIS){
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                            } else if (typeSource == InstallationChauffageEnum.CHAUDIERE_AVEC_PAC){
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                            } else if (typeSource == InstallationChauffageEnum.CHAUDIERE_AVEC_PAC_ET_INSERT_BOIS){
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                            }
                            chauffage_properties.put(DpeEvent.INSTALLATION_CHAUFFAGE, typeSource);
                            this.tryActualiseI0();
                            System.out.println(chauffage_properties);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            InstallationChauffageEnum type = (InstallationChauffageEnum) chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE);

                            HashMap<String, Object> currentItems = new HashMap<String, Object>();
                            currentItems.put("lastValue", type);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.INSTALLATION_CHAUFFAGE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);

                            // wait for layout to be  populated

                            while (!layout.isInitialised()) {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException ie) {

                                }
                            }
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_chauffage"), true);
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chauffage_et_insert"), false);
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_gaz_et_chaudiere_bois"), false);
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC"), false);
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("chaudiere_et_PAC_insert"), false);
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                        }
                        break;
                    }

                    case CHAUFFAGE_UNIQUE :{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        Layout layout = (Layout) items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage chauffage = new Chauffage((Chauffage.Generateur) items.get("lastValue"));
                            chauffage_properties.put(DpeEvent.CHAUFFAGE_UNIQUE, chauffage);
                            this.actualiseRendementsChauffage(chauffage);
                            this.tryActualiseI0();

                            String nameGenerateur = chauffage.getGenerateur().toString();
                            if (nameGenerateur.startsWith("Chaudière")){ // Si le générateur est une chaudière
                                if (ecs_properties.containsKey(DpeEvent.TYPE_EQUIPEMENT_ECS)){
                                    if (ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS).equals(TypeEquipementEcsEnum.CHAUDIERE)) {
                                        chauffage.setGenereEgalementEcs(true);
                                        ecs_properties.put(DpeEvent.CHAUDIERE_ECS, chauffage);
                                        this.actualisePrs1();
                                    }else{
                                        chauffage.setGenereEgalementEcs(false);
                                    }
                                }
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                            }else{
                                if (ecs_properties.containsKey(DpeEvent.CHAUDIERE_ECS)){
                                    ecs_properties.remove(DpeEvent.CHAUDIERE_ECS);
                                }
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                            }
                            System.out.println(chauffage_properties);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage type = null;
                            if (chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_UNIQUE)){
                                type = (Chauffage) chauffage_properties.get(DpeEvent.CHAUFFAGE_UNIQUE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CHAUFFAGE_UNIQUE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case CHAUFFAGE_SANS_POIL: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        Layout layout = (Layout) items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage chauffage = new Chauffage((Chauffage.Generateur) items.get("lastValue"));
                            chauffage_properties.put(DpeEvent.CHAUFFAGE_SANS_POIL, chauffage);
                            this.actualiseRendementsChauffage(chauffage);
                            this.tryActualiseI0();

                            String nameGenerateur = chauffage.getGenerateur().toString();
                            if (nameGenerateur.startsWith("Chaudière")){ // Si le générateur est une chaudière
                                if (ecs_properties.containsKey(DpeEvent.TYPE_EQUIPEMENT_ECS)){
                                    if (ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS).equals(TypeEquipementEcsEnum.CHAUDIERE)){
                                        chauffage.setGenereEgalementEcs(true);
                                        ecs_properties.put(DpeEvent.CHAUDIERE_ECS, chauffage);
                                        this.actualisePrs1();
                                    }else{
                                        chauffage.setGenereEgalementEcs(false);
                                    }
                                }
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), true);
                            }else{
                                if (ecs_properties.containsKey(DpeEvent.CHAUDIERE_ECS)){
                                    ecs_properties.remove(DpeEvent.CHAUDIERE_ECS);
                                }
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_sans_chaudiere"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("installation_ECS_avec_chaudiere"), false);
                            }
                            System.out.println(chauffage_properties);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage type = null;
                            if (chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_SANS_POIL)){
                                type = (Chauffage) chauffage_properties.get(DpeEvent.CHAUFFAGE_SANS_POIL);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CHAUFFAGE_SANS_POIL, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case POELE_OU_INSERT_AVEC_CHAUFFAGE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage chauffage = new Chauffage((Chauffage.Generateur) items.get("lastValue"));
                            chauffage_properties.put(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE, chauffage);
                            this.actualiseRendementsChauffage(chauffage);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage type = null;
                            if (chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE)){
                                type = (Chauffage) chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.POELE_OU_INSERT_AVEC_CHAUFFAGE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUFFAGE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            FrequenceUtilisationPoilEnum frequenceUtilisationPoil = (FrequenceUtilisationPoilEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUFFAGE, frequenceUtilisationPoil);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            FrequenceUtilisationPoilEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUFFAGE)){
                                type = (FrequenceUtilisationPoilEnum) chauffage_properties.get(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUFFAGE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUFFAGE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case CHAUDIERE_GAZ_FIOUL:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage chaudiere = new Chauffage((Chauffage.Generateur) items.get("lastValue"));
                            chauffage_properties.put(DpeEvent.CHAUDIERE_GAZ_FIOUL, chaudiere);
                            this.actualiseRendementsChauffage(chaudiere);
                            this.tryActualiseI0();

                            if (ecs_properties.containsKey(DpeEvent.TYPE_EQUIPEMENT_ECS)){
                                if (ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS).equals(TypeEquipementEcsEnum.CHAUDIERE)){
                                    chaudiere.setGenereEgalementEcs(true);
                                    ecs_properties.put(DpeEvent.CHAUDIERE_ECS, chaudiere);
                                    this.actualisePrs1();
                                }else{
                                    chaudiere.setGenereEgalementEcs(false);
                                }
                            }
                            System.out.println(chauffage_properties);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage type = null;
                            if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_GAZ_FIOUL)){
                                type = (Chauffage) chauffage_properties.get(DpeEvent.CHAUDIERE_GAZ_FIOUL);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CHAUDIERE_GAZ_FIOUL, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case CHAUDIERE_BOIS:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage chaudiere = new Chauffage((Chauffage.Generateur) items.get("lastValue"));
                            chauffage_properties.put(DpeEvent.CHAUDIERE_BOIS, chaudiere);
                            this.actualiseRendementsChauffage(chaudiere);
                            System.out.println(chauffage_properties);

                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage type = null;
                            if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_BOIS)){
                                type = (Chauffage) chauffage_properties.get(DpeEvent.CHAUDIERE_BOIS);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CHAUDIERE_BOIS, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case CHAUDIERE_AVEC_PAC:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage chaudiere = new Chauffage((Chauffage.Generateur) items.get("lastValue"));
                            chauffage_properties.put(DpeEvent.CHAUDIERE_AVEC_PAC, chaudiere);
                            this.actualiseRendementsChauffage(chaudiere);
                            this.tryActualiseI0();

                            if (ecs_properties.containsKey(DpeEvent.TYPE_EQUIPEMENT_ECS)){
                                if (ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS).equals(TypeEquipementEcsEnum.CHAUDIERE)){
                                    chaudiere.setGenereEgalementEcs(true);
                                    ecs_properties.put(DpeEvent.CHAUDIERE_ECS, chaudiere);
                                    this.actualisePrs1();
                                }else{
                                    chaudiere.setGenereEgalementEcs(false);
                                }
                            }
                            System.out.println(chauffage_properties);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage type = null;
                            if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC)){
                                type = (Chauffage) chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CHAUDIERE_AVEC_PAC, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case POMPE_A_CHALEUR_AVEC_CHAUDIERE :{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage pac = new Chauffage((Chauffage.Generateur) items.get("lastValue"));
                            chauffage_properties.put(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE, pac);
                            this.actualiseRendementsChauffage(pac);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage type = null;
                            if (chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE)){
                                type = (Chauffage) chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case CHAUDIERE_AVEC_PAC_ET_POELE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage chaudiere = new Chauffage((Chauffage.Generateur) items.get("lastValue"));
                            chauffage_properties.put(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE, chaudiere);
                            this.actualiseRendementsChauffage(chaudiere);
                            this.tryActualiseI0();
                            System.out.println(chauffage_properties);

                            if (ecs_properties.containsKey(DpeEvent.TYPE_EQUIPEMENT_ECS)){
                                if (ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS).equals(TypeEquipementEcsEnum.CHAUDIERE)){
                                    chaudiere.setGenereEgalementEcs(true);
                                    ecs_properties.put(DpeEvent.CHAUDIERE_ECS, chaudiere);
                                    this.actualisePrs1();
                                }else{
                                    chaudiere.setGenereEgalementEcs(false);
                                }
                            }
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage type = null;
                            if (chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE)){
                                type = (Chauffage) chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE :{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage pac = new Chauffage((Chauffage.Generateur) items.get("lastValue"));
                            chauffage_properties.put(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE, pac);
                            this.actualiseRendementsChauffage(pac);
                            System.out.println(chauffage_properties);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage type = null;
                            if (chauffage_properties.containsKey(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE)){
                                type = (Chauffage) chauffage_properties.get(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.POMPE_A_CHALEUR_AVEC_CHAUDIERE_ET_POELE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage poele = new Chauffage((Chauffage.Generateur) items.get("lastValue"));
                            chauffage_properties.put(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC, poele);
                            this.actualiseRendementsChauffage(poele);
                            System.out.println(chauffage_properties);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage type = null;
                            if (chauffage_properties.containsKey(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC)){
                                type = (Chauffage) chauffage_properties.get(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            FrequenceUtilisationPoilEnum frequenceUtilisationPoil = (FrequenceUtilisationPoilEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC, frequenceUtilisationPoil);
                            System.out.println(chauffage_properties);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            FrequenceUtilisationPoilEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC)){
                                type = (FrequenceUtilisationPoilEnum) chauffage_properties.get(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.FREQUENCE_UTILISATION_POELE_OU_INSERT_AVEC_CHAUDIERE_ET_PAC, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TEMPERATURE_INTERIEUR:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TemperatureInterieurEnum temperatureInterieur = (TemperatureInterieurEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.TEMPERATURE_INTERIEUR, temperatureInterieur);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TemperatureInterieurEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.TEMPERATURE_INTERIEUR)){
                                type = (TemperatureInterieurEnum) chauffage_properties.get(DpeEvent.TEMPERATURE_INTERIEUR);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TEMPERATURE_INTERIEUR, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_EMETTEUR_DE_CHALEUR:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            Chauffage.Emission typeEmission = (Chauffage.Emission) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR, typeEmission);
                            this.actualiseEmetteurAllGenerateurs(typeEmission);
                            this.tryActualiseI0();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            Chauffage.Emission type = null;
                            if (chauffage_properties.containsKey(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR)){
                                type = (Chauffage.Emission) chauffage_properties.get(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_EMETTEUR_DE_CHALEUR, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            PresenceThermostatEnum presenceThermostat = (PresenceThermostatEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR, presenceThermostat);
                            this.tryActualiseI0();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            PresenceThermostatEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR)){
                                type = (PresenceThermostatEnum) chauffage_properties.get(DpeEvent.PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.PRESENCE_THERMOSTAT_OU_SONDE_EXTERIEUR, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case PRESENCE_ROBINET_THERMOSTATIQUE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            PresenceRobinetEnum presenceRobinet = (PresenceRobinetEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.PRESENCE_ROBINET_THERMOSTATIQUE, presenceRobinet);
                            this.actualiseRobinetAllGenerateurs(presenceRobinet);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            PresenceRobinetEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.PRESENCE_ROBINET_THERMOSTATIQUE)){
                                type = (PresenceRobinetEnum) chauffage_properties.get(DpeEvent.PRESENCE_ROBINET_THERMOSTATIQUE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.PRESENCE_ROBINET_THERMOSTATIQUE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case SYSTEME_PROGRAMMABLE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            ProgrammationSystemeEnum programmationSysteme = (ProgrammationSystemeEnum) items.get("lastValue");
                            chauffage_properties.put(DpeEvent.SYSTEME_PROGRAMMABLE, programmationSysteme);
                            this.tryActualiseI0();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            ProgrammationSystemeEnum type = null;
                            if (chauffage_properties.containsKey(DpeEvent.SYSTEME_PROGRAMMABLE)){
                                type = (ProgrammationSystemeEnum) chauffage_properties.get(DpeEvent.SYSTEME_PROGRAMMABLE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.SYSTEME_PROGRAMMABLE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_EQUIPEMENT_ECS:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        Layout layout = (Layout) items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeEquipementEcsEnum typeEquipementEcs = (TypeEquipementEcsEnum) items.get("lastValue");
                            if (typeEquipementEcs == TypeEquipementEcsEnum.CHAUDIERE) {
                                if (chauffage_properties.containsKey(DpeEvent.INSTALLATION_CHAUFFAGE)) {
                                    if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUFFAGE_UNIQUE)
                                            && chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_UNIQUE)) {
                                        Chauffage chauffage = (Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_UNIQUE);
                                        chauffage.setGenereEgalementEcs(true);
                                        ecs_properties.put(DpeEvent.CHAUDIERE_ECS,chauffage);
                                    } else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUFFAGE_AVEC_POIL_OU_INSERT_BOIS)
                                            && chauffage_properties.containsKey(DpeEvent.CHAUFFAGE_SANS_POIL)) {
                                        Chauffage chauffage = (Chauffage)chauffage_properties.get(DpeEvent.CHAUFFAGE_SANS_POIL);
                                        chauffage.setGenereEgalementEcs(true);
                                        ecs_properties.put(DpeEvent.CHAUDIERE_ECS,chauffage);
                                    } else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_GAZ_OU_FIOUL_AVEC_CHAUDIERE_BOIS)
                                            && chauffage_properties.containsKey(DpeEvent.CHAUDIERE_GAZ_FIOUL)) {
                                        Chauffage chauffage = (Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_GAZ_FIOUL);
                                        chauffage.setGenereEgalementEcs(true);
                                        ecs_properties.put(DpeEvent.CHAUDIERE_ECS,chauffage);
                                    } else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_AVEC_PAC)
                                            && chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC)){
                                        Chauffage chauffage = (Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC);
                                        chauffage.setGenereEgalementEcs(true);
                                        ecs_properties.put(DpeEvent.CHAUDIERE_ECS,chauffage);
                                    } else if (chauffage_properties.get(DpeEvent.INSTALLATION_CHAUFFAGE).equals(InstallationChauffageEnum.CHAUDIERE_AVEC_PAC_ET_INSERT_BOIS)
                                            && chauffage_properties.containsKey(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE)){
                                        Chauffage chauffage = (Chauffage)chauffage_properties.get(DpeEvent.CHAUDIERE_AVEC_PAC_ET_POELE);
                                        chauffage.setGenereEgalementEcs(true);
                                        ecs_properties.put(DpeEvent.CHAUDIERE_ECS,chauffage);
                                    }
                                }
                            }
                            ecs_properties.put(DpeEvent.TYPE_EQUIPEMENT_ECS, typeEquipementEcs);
                            this.actualisePrs1();
                            this.actualisePrs2();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {

                            TypeEquipementEcsEnum type = (TypeEquipementEcsEnum) ecs_properties.get(DpeEvent.TYPE_EQUIPEMENT_ECS);

                            HashMap<String, Object> currentItems = new HashMap<String, Object>();
                            currentItems.put("lastValue", type);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_EQUIPEMENT_ECS, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_BALLON_ELECTRIQUE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeBallonElectriqueEnum typeBallonElectrique= (TypeBallonElectriqueEnum) items.get("lastValue");
                            ecs_properties.put(DpeEvent.TYPE_BALLON_ELECTRIQUE, typeBallonElectrique);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeBallonElectriqueEnum type = null;
                            if (ecs_properties.containsKey(DpeEvent.TYPE_BALLON_ELECTRIQUE)){
                                type = (TypeBallonElectriqueEnum) ecs_properties.get(DpeEvent.TYPE_BALLON_ELECTRIQUE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_BALLON_ELECTRIQUE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_CHAUFFE_EAU:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeChauffeEauEnum typeChauffeEau= (TypeChauffeEauEnum) items.get("lastValue");
                            ecs_properties.put(DpeEvent.TYPE_CHAUFFE_EAU, typeChauffeEau);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeChauffeEauEnum type = null;
                            if (ecs_properties.containsKey(DpeEvent.TYPE_CHAUFFE_EAU)){
                                type = (TypeChauffeEauEnum) ecs_properties.get(DpeEvent.TYPE_CHAUFFE_EAU);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_CHAUFFE_EAU, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_ACCUMULATEUR:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeAccumulateurEnum typeAccumulateur= (TypeAccumulateurEnum) items.get("lastValue");
                            ecs_properties.put(DpeEvent.TYPE_ACCUMULATEUR, typeAccumulateur);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            TypeAccumulateurEnum type = null;
                            if (ecs_properties.containsKey(DpeEvent.TYPE_ACCUMULATEUR)){
                                type = (TypeAccumulateurEnum) ecs_properties.get(DpeEvent.TYPE_ACCUMULATEUR);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_ACCUMULATEUR, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case PRESENCE_INSTALLATION_SOLAIRE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            PresenceInstallationSolaireEnum isPresent= (PresenceInstallationSolaireEnum) items.get("lastValue");
                            ecs_properties.put(DpeEvent.PRESENCE_INSTALLATION_SOLAIRE, isPresent);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            PresenceInstallationSolaireEnum type = null;
                            if (ecs_properties.containsKey(DpeEvent.NOMBRE_PERSONNES_DANS_LOGEMENT)){
                                type = (PresenceInstallationSolaireEnum) ecs_properties.get(DpeEvent.PRESENCE_INSTALLATION_SOLAIRE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.PRESENCE_INSTALLATION_SOLAIRE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case LOCAL_EQUIPEMENT_ECS:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            LocalEquipementEcsEnum localEquipementEcs= (LocalEquipementEcsEnum) items.get("lastValue");
                            ecs_properties.put(DpeEvent.LOCAL_EQUIPEMENT_ECS, localEquipementEcs);
                            this.actualiseLocalAllGenerateurs(localEquipementEcs);
                            this.actualisePrs1();
                            this.actualisePrs2();
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            LocalEquipementEcsEnum type = null;
                            if (ecs_properties.containsKey(DpeEvent.LOCAL_EQUIPEMENT_ECS)){
                                type = (LocalEquipementEcsEnum) ecs_properties.get(DpeEvent.LOCAL_EQUIPEMENT_ECS);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.LOCAL_EQUIPEMENT_ECS, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case USAGE_EAU_CHAUDE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            UsageEauChaudeEnum usageEauChaude= (UsageEauChaudeEnum) items.get("lastValue");
                            ecs_properties.put(DpeEvent.USAGE_EAU_CHAUDE, usageEauChaude);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            UsageEauChaudeEnum type = null;
                            if (ecs_properties.containsKey(DpeEvent.USAGE_EAU_CHAUDE)){
                                type = (UsageEauChaudeEnum) ecs_properties.get(DpeEvent.USAGE_EAU_CHAUDE);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.USAGE_EAU_CHAUDE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case DECLENCHEMENT_CHAUDIERE_ROBINET:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            DeclenchementChaudiereEnum declenchementChaudiere= (DeclenchementChaudiereEnum) items.get("lastValue");
                            ecs_properties.put(DpeEvent.DECLENCHEMENT_CHAUDIERE_ROBINET, declenchementChaudiere);
                        }
                        else if (eventRequest == EventRequest.GET_STATE) {
                            DeclenchementChaudiereEnum type = null;
                            if (ecs_properties.containsKey(DpeEvent.DECLENCHEMENT_CHAUDIERE_ROBINET)){
                                type = (DeclenchementChaudiereEnum) ecs_properties.get(DpeEvent.DECLENCHEMENT_CHAUDIERE_ROBINET);
                            }
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.DECLENCHEMENT_CHAUDIERE_ROBINET, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_MUR: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        Mur mur = (Mur)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeMurEnum typeMur = (TypeMurEnum)items.get("lastValue");
                            Layout layout = (Layout)items.get("layout");
                            if (!walls_properties.containsKey(mur))
                                walls_properties.put(mur, new HashMap<EventType, Object>());
                            walls_properties.get(mur).put(event, typeMur);
                            if (!typeMur.equals(TypeMurEnum.MUR_INTERIEUR)) {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("date_isolation"), true);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_isolation"), true);
                            } else {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("date_isolation"), false);
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_isolation"), false);
                            }
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            TypeMurEnum type = null;
                            if (walls_properties.containsKey(mur))
                                if (walls_properties.get(mur).containsKey(DpeEvent.TYPE_MUR))
                                    type = (TypeMurEnum) walls_properties.get(mur).get(DpeEvent.TYPE_MUR);
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", mur);
                            Event e2 = new Event(DpeEvent.TYPE_MUR, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case DATE_ISOLATION_MUR: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        Mur mur = (Mur)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            DateIsolationMurEnum dateIsolationMur = (DateIsolationMurEnum)items.get("lastValue");
                            Layout layout = (Layout) items.get("layout");
                            if (!walls_properties.containsKey(mur))
                                walls_properties.put(mur, new HashMap<EventType, Object>());
                            walls_properties.get(mur).put(event, dateIsolationMur);
                            if (dateIsolationMur.equals(DateIsolationMurEnum.JAMAIS) || dateIsolationMur.equals(DateIsolationMurEnum.INCONNUE)) {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_isolation"), false);
                            } else {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("type_isolation"), true);
                            }
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            DateIsolationMurEnum type = null;
                            if (walls_properties.containsKey(mur))
                                if (walls_properties.get(mur).containsKey(DpeEvent.DATE_ISOLATION_MUR))
                                    type = (DateIsolationMurEnum) walls_properties.get(mur).get(DpeEvent.DATE_ISOLATION_MUR);
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", mur);
                            Event e2 = new Event(DpeEvent.DATE_ISOLATION_MUR, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_ISOLATION_MUR: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        Mur mur = (Mur)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeIsolationMurEnum typeIsolationMur = (TypeIsolationMurEnum) items.get("lastValue");
                            if (!walls_properties.containsKey(mur))
                                walls_properties.put(mur, new HashMap<EventType, Object>());
                            walls_properties.get(mur).put(event, typeIsolationMur);
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            TypeIsolationMurEnum type = null;
                            if (walls_properties.containsKey(mur))
                                if (walls_properties.get(mur).containsKey(DpeEvent.TYPE_ISOLATION_MUR))
                                    type = (TypeIsolationMurEnum) walls_properties.get(mur).get(DpeEvent.TYPE_ISOLATION_MUR);
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", mur);
                            Event e2 = new Event(DpeEvent.TYPE_ISOLATION_MUR, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case ORIENTATION_MUR: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        Mur mur = (Mur)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            OrientationEnum orientationMur = (OrientationEnum) items.get("lastValue");
                            if (!walls_properties.containsKey(mur))
                                walls_properties.put(mur, new HashMap<EventType, Object>());
                            walls_properties.get(mur).put(event, orientationMur);
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            OrientationEnum type = null;
                            if (walls_properties.containsKey(mur))
                                if (walls_properties.get(mur).containsKey(DpeEvent.ORIENTATION_MUR))
                                    type = (OrientationEnum) walls_properties.get(mur).get(DpeEvent.ORIENTATION_MUR);
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", mur);
                            Event e2 = new Event(DpeEvent.ORIENTATION_MUR, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_FENETRE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        Fenetre fenetre = (Fenetre)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeFenetreEnum typeFenetre = (TypeFenetreEnum)items.get("lastValue");
                            if (!windows_properties.containsKey(fenetre)){
                                windows_properties.put(fenetre, new HashMap<EventType, Object>());
                            }
                            windows_properties.get(fenetre).put(event, typeFenetre);

                        } else if (eventRequest == EventRequest.GET_STATE) {
                            TypeFenetreEnum type = null;
                            if (windows_properties.containsKey(fenetre))
                                if (windows_properties.get(fenetre).containsKey(DpeEvent.TYPE_FENETRE))
                                    type = (TypeFenetreEnum)windows_properties.get(fenetre).get(DpeEvent.TYPE_FENETRE);
                            HashMap<String,Object> currentItems = new HashMap<String,Object>();
                            currentItems.put("lastValue",type);
                            currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                            currentItems.put("userObject", fenetre);
                            Event e2 = new Event(DpeEvent.TYPE_FENETRE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);
                        }
                        break;
                    }

                    case TYPE_MATERIAU_MENUISERIE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (items.get("userObject") instanceof Fenetre){
                            Fenetre fenetre = (Fenetre)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMateriauMenuiserieEnum typeMateriauMenuiserie = (TypeMateriauMenuiserieEnum)items.get("lastValue");
                                if (!windows_properties.containsKey(fenetre))
                                    windows_properties.put(fenetre, new HashMap<EventType, Object>());
                                windows_properties.get(fenetre).put(event, typeMateriauMenuiserie);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMateriauMenuiserieEnum type = null;
                                if (windows_properties.containsKey(fenetre))
                                    if (windows_properties.get(fenetre).containsKey(DpeEvent.TYPE_MATERIAU_MENUISERIE))
                                        type = (TypeMateriauMenuiserieEnum) windows_properties.get(fenetre).get(DpeEvent.TYPE_MATERIAU_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", fenetre);
                                Event e2 = new Event(DpeEvent.TYPE_MATERIAU_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }
                        }
                        else if (items.get("userObject") instanceof Porte){
                            Porte porte = (Porte)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMateriauMenuiserieEnum typeMateriauMenuiserie = (TypeMateriauMenuiserieEnum)items.get("lastValue");
                                if (!doors_properties.containsKey(porte))
                                    doors_properties.put(porte, new HashMap<EventType, Object>());
                                doors_properties.get(porte).put(event, typeMateriauMenuiserie);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMateriauMenuiserieEnum type = null;
                                if (doors_properties.containsKey(porte))
                                    if (doors_properties.get(porte).containsKey(DpeEvent.TYPE_MATERIAU_MENUISERIE))
                                        type = (TypeMateriauMenuiserieEnum) doors_properties.get(porte).get(DpeEvent.TYPE_MATERIAU_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", porte);
                                Event e2 = new Event(DpeEvent.TYPE_MATERIAU_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }
                        }
                        break;
                    }

                    case TYPE_VITRAGE_MENUISERIE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (items.get("userObject") instanceof Fenetre){
                            Fenetre fenetre = (Fenetre)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeVitrageEnum TypeVitrage = (TypeVitrageEnum)items.get("lastValue");
                                if (!windows_properties.containsKey(fenetre))
                                    windows_properties.put(fenetre, new HashMap<EventType, Object>());
                                windows_properties.get(fenetre).put(event, TypeVitrage);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeVitrageEnum type = null;
                                if (windows_properties.containsKey(fenetre))
                                    if (windows_properties.get(fenetre).containsKey(DpeEvent.TYPE_VITRAGE_MENUISERIE))
                                        type = (TypeVitrageEnum) windows_properties.get(fenetre).get(DpeEvent.TYPE_VITRAGE_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", fenetre);
                                Event e2 = new Event(DpeEvent.TYPE_VITRAGE_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }

                        }
                        else if (items.get("userObject") instanceof Porte){
                            Porte porte = (Porte)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeVitrageEnum TypeVitrage = (TypeVitrageEnum)items.get("lastValue");
                                if (!doors_properties.containsKey(porte))
                                    doors_properties.put(porte, new HashMap<EventType, Object>());
                                doors_properties.get(porte).put(event, TypeVitrage);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeVitrageEnum type = null;
                                if (doors_properties.containsKey(porte))
                                    if (doors_properties.get(porte).containsKey(DpeEvent.TYPE_VITRAGE_MENUISERIE))
                                        type = (TypeVitrageEnum) doors_properties.get(porte).get(DpeEvent.TYPE_VITRAGE_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", porte);
                                Event e2 = new Event(DpeEvent.TYPE_VITRAGE_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }
                        }
                        break;
                    }

                    case TYPE_FERMETURE_MENUISERIE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (items.get("userObject") instanceof Fenetre){
                            Fenetre fenetre = (Fenetre)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeFermetureEnum typeFermeture = (TypeFermetureEnum)items.get("lastValue");
                                if (!windows_properties.containsKey(fenetre))
                                    windows_properties.put(fenetre, new HashMap<EventType, Object>());
                                windows_properties.get(fenetre).put(event, typeFermeture);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeFermetureEnum type = null;
                                if (windows_properties.containsKey(fenetre))
                                    if (windows_properties.get(fenetre).containsKey(DpeEvent.TYPE_FERMETURE_MENUISERIE))
                                        type = (TypeFermetureEnum) windows_properties.get(fenetre).get(DpeEvent.TYPE_FERMETURE_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", fenetre);
                                Event e2 = new Event(DpeEvent.TYPE_FERMETURE_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }

                        }
                        else if (items.get("userObject") instanceof Porte){
                            Porte porte = (Porte)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeFermetureEnum typeFermeture = (TypeFermetureEnum)items.get("lastValue");
                                if (!doors_properties.containsKey(porte))
                                    doors_properties.put(porte, new HashMap<EventType, Object>());
                                doors_properties.get(porte).put(event, typeFermeture);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeFermetureEnum type = null;
                                if (doors_properties.containsKey(porte))
                                    if (doors_properties.get(porte).containsKey(DpeEvent.TYPE_FERMETURE_MENUISERIE))
                                        type = (TypeFermetureEnum) doors_properties.get(porte).get(DpeEvent.TYPE_FERMETURE_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", porte);
                                Event e2 = new Event(DpeEvent.TYPE_FERMETURE_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }
                        }
                        break;
                    }

                    case MASQUE_PROCHE_MENUISERIE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (items.get("userObject") instanceof Fenetre){
                            Fenetre fenetre = (Fenetre)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMasqueEnum masque = (TypeMasqueEnum)items.get("lastValue");
                                if (!windows_properties.containsKey(fenetre))
                                    windows_properties.put(fenetre, new HashMap<EventType, Object>());
                                windows_properties.get(fenetre).put(event, masque);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMasqueEnum type = null;
                                if (windows_properties.containsKey(fenetre))
                                    if (windows_properties.get(fenetre).containsKey(DpeEvent.MASQUE_PROCHE_MENUISERIE))
                                        type = (TypeMasqueEnum) windows_properties.get(fenetre).get(DpeEvent.MASQUE_PROCHE_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", fenetre);
                                Event e2 = new Event(DpeEvent.MASQUE_PROCHE_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }

                        }
                        else if (items.get("userObject") instanceof Porte){
                            Porte porte = (Porte)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMasqueEnum masque = (TypeMasqueEnum)items.get("lastValue");
                                if (!doors_properties.containsKey(porte))
                                    doors_properties.put(porte, new HashMap<EventType, Object>());
                                doors_properties.get(porte).put(event, masque);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMasqueEnum type = null;
                                if (doors_properties.containsKey(porte))
                                    if (doors_properties.get(porte).containsKey(DpeEvent.MASQUE_PROCHE_MENUISERIE))
                                        type = (TypeMasqueEnum) doors_properties.get(porte).get(DpeEvent.MASQUE_PROCHE_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", porte);
                                Event e2 = new Event(DpeEvent.MASQUE_PROCHE_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }
                        }
                        break;
                    }

                    case MASQUE_LOINTAIN_MENUISERIE:{
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        if (items.get("userObject") instanceof Fenetre){
                            Fenetre fenetre = (Fenetre)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMasqueEnum masque = (TypeMasqueEnum)items.get("lastValue");
                                if (!windows_properties.containsKey(fenetre))
                                    windows_properties.put(fenetre, new HashMap<EventType, Object>());
                                windows_properties.get(fenetre).put(event, masque);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMasqueEnum type = null;
                                if (windows_properties.containsKey(fenetre))
                                    if (windows_properties.get(fenetre).containsKey(DpeEvent.MASQUE_LOINTAIN_MENUISERIE))
                                        type = (TypeMasqueEnum) windows_properties.get(fenetre).get(DpeEvent.MASQUE_LOINTAIN_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", fenetre);
                                Event e2 = new Event(DpeEvent.MASQUE_LOINTAIN_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }

                        }
                        else if (items.get("userObject") instanceof Porte){
                            Porte porte = (Porte)items.get("userObject");
                            if (eventRequest == EventRequest.UPDATE_STATE) {
                                TypeMasqueEnum masque = (TypeMasqueEnum)items.get("lastValue");
                                if (!doors_properties.containsKey(porte))
                                    doors_properties.put(porte, new HashMap<EventType, Object>());
                                doors_properties.get(porte).put(event, masque);
                                //tryActualiseWallDP(window);
                            } else if (eventRequest == EventRequest.GET_STATE) {
                                TypeMasqueEnum type = null;
                                if (doors_properties.containsKey(porte))
                                    if (doors_properties.get(porte).containsKey(DpeEvent.MASQUE_LOINTAIN_MENUISERIE))
                                        type = (TypeMasqueEnum) doors_properties.get(porte).get(DpeEvent.MASQUE_LOINTAIN_MENUISERIE);
                                HashMap<String,Object> currentItems = new HashMap<String,Object>();
                                currentItems.put("lastValue",type);
                                currentItems.put("eventRequest",EventRequest.CURRENT_STATE);
                                currentItems.put("userObject", porte);
                                Event e2 = new Event(DpeEvent.MASQUE_LOINTAIN_MENUISERIE, currentItems);
                                EventManager.getInstance().put(Channel.DPE, e2);
                            }
                        }
                        break;
                    }

                    case TYPE_PORTE: {
                        HashMap<String,Object> items = (HashMap<String,Object>) o;
                        Porte porte = (Porte)items.get("userObject");
                        EventRequest eventRequest = (EventRequest)items.get("eventRequest");
                        Layout layout = (Layout)items.get("layout");
                        if (eventRequest == EventRequest.UPDATE_STATE) {
                            TypeDoorEnum typeDoor = (TypeDoorEnum)items.get("lastValue");
                            if (!doors_properties.containsKey(porte))
                                doors_properties.put(porte, new HashMap<EventType, Object>());
                            doors_properties.get(porte).put(event, typeDoor);
                            if (typeDoor.equals(TypeDoorEnum.PORTE_FENETRE_COULISSANTE)||typeDoor.equals(TypeDoorEnum.PORTE_FENETRE_BATTANTE)) {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("vitrage_et_fermeture"), true);
                            } else {
                                ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("vitrage_et_fermeture"), false);
                            }
                        } else if (eventRequest == EventRequest.GET_STATE) {
                            TypeDoorEnum type = null;
                            HashMap<String, Object> currentItems = new HashMap<String, Object>();
                            currentItems.put("lastValue", type);
                            currentItems.put("userObject", porte);
                            currentItems.put("eventRequest", EventRequest.CURRENT_STATE);
                            Event e2 = new Event(DpeEvent.TYPE_PORTE, currentItems);
                            EventManager.getInstance().put(Channel.DPE, e2);

                            // wait for layout to be  populated

                            while (!layout.isInitialised()) {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException ie) {

                                }
                            }
                            ((TabWindow) layout.getFromId("tab_window")).setTableDisabled(layout.getFromId("vitrage_et_fermeture"), false);
                        }
                        break;
                    }
                }
            }
        }
    }
}
