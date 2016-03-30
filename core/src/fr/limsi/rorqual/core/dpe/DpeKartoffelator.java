package fr.limsi.rorqual.core.dpe;

import java.util.Hashtable;

import fr.limsi.rorqual.core.dpe.enums.chauffageproperties.Chauffage;
import fr.limsi.rorqual.core.dpe.enums.generalproperties.TypeAbonnementElectriqueEnum;
import fr.limsi.rorqual.core.dpe.enums.generalproperties.TypeEquipementCuissonEnum;
import fr.limsi.rorqual.core.dpe.enums.generalproperties.TypeEquipementEclairageEnum;
import fr.limsi.rorqual.core.dpe.enums.generalproperties.TypeVentilationEnum;
import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeFermetureEnum;
import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeVitrageEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.DateIsolationMurEnum;
import fr.limsi.rorqual.core.event.DpeEvent;

/**
 * Created by christophe on 30/03/16.
 */
public class DpeKartoffelator {

    Hashtable<Object, Hashtable<Object, Dpe>> fake_dpes = new Hashtable<Object, Hashtable<Object, Dpe>>();

    public DpeKartoffelator() {
        create_fake_dpe();
    }

    public void create_fake_dpe() {

        fake_dpes.put(DpeEvent.DATE_ISOLATION_MUR, new Hashtable<Object, Dpe>());
        for (DateIsolationMurEnum value : DateIsolationMurEnum.values())
            fake_dpes.get(DpeEvent.DATE_ISOLATION_MUR).put(value, new Dpe(true));

        fake_dpes.put(DpeEvent.ABONNEMENT_ELECTRIQUE, new Hashtable<Object, Dpe>());
        for (TypeAbonnementElectriqueEnum value : TypeAbonnementElectriqueEnum.values())
            fake_dpes.get(DpeEvent.ABONNEMENT_ELECTRIQUE).put(value, new Dpe(true));

        fake_dpes.put(DpeEvent.EQUIPEMENT_ECLAIRAGE, new Hashtable<Object, Dpe>());
        for (TypeEquipementEclairageEnum value : TypeEquipementEclairageEnum.values())
            fake_dpes.get(DpeEvent.EQUIPEMENT_ECLAIRAGE).put(value, new Dpe(true));

        fake_dpes.put(DpeEvent.TYPE_VENTILATION, new Hashtable<Object, Dpe>());
        for (TypeVentilationEnum value : TypeVentilationEnum.values())
            fake_dpes.get(DpeEvent.TYPE_VENTILATION).put(value, new Dpe(true));

        fake_dpes.put(DpeEvent.EQUIPEMENT_CUISSON, new Hashtable<Object, Dpe>());
        for (TypeEquipementCuissonEnum value : TypeEquipementCuissonEnum.values())
            fake_dpes.get(DpeEvent.EQUIPEMENT_CUISSON).put(value, new Dpe(true));

        fake_dpes.put(DpeEvent.TYPE_VITRAGE_MENUISERIE, new Hashtable<Object, Dpe>());
        for (TypeVitrageEnum value : TypeVitrageEnum.values())
            fake_dpes.get(DpeEvent.TYPE_VITRAGE_MENUISERIE).put(value, new Dpe(true));

        fake_dpes.put(DpeEvent.TYPE_FERMETURE_MENUISERIE, new Hashtable<Object, Dpe>());
        for (TypeFermetureEnum value : TypeFermetureEnum.values())
            fake_dpes.get(DpeEvent.TYPE_FERMETURE_MENUISERIE).put(value, new Dpe(true));

        fake_dpes.put(DpeEvent.CHAUFFAGE_UNIQUE, new Hashtable<Object, Dpe>());
        for (Chauffage.Generateur value : Chauffage.Generateur.values())
            fake_dpes.get(DpeEvent.CHAUFFAGE_UNIQUE).put(value, new Dpe(true));
    }

    public void calculate_all() {

        //DpeEvent.DATE_ISOLATION_MUR
        //DateIsolationMurEnum.INCONNUE

    }

}
