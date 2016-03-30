package fr.limsi.rorqual.core.dpe;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import fr.limsi.rorqual.core.dpe.enums.chauffageproperties.Chauffage;
import fr.limsi.rorqual.core.dpe.enums.generalproperties.TypeAbonnementElectriqueEnum;
import fr.limsi.rorqual.core.dpe.enums.generalproperties.TypeEquipementCuissonEnum;
import fr.limsi.rorqual.core.dpe.enums.generalproperties.TypeEquipementEclairageEnum;
import fr.limsi.rorqual.core.dpe.enums.generalproperties.TypeVentilationEnum;
import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeFermetureEnum;
import fr.limsi.rorqual.core.dpe.enums.menuiserieproperties.TypeVitrageEnum;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.DateIsolationMurEnum;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.DpeEvent;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.event.EventRequest;

/**
 * Created by christophe on 30/03/16.
 */
public class DpeKartoffelator {

    Hashtable<DpeEvent, Hashtable<Object, Dpe>> fake_dpes = new Hashtable<DpeEvent, Hashtable<Object, Dpe>>();

    public DpeKartoffelator() {
        create_fake_dpe();
        calculate_all();
    }

    public void create_fake_dpe() {

        fake_dpes.put(DpeEvent.DATE_ISOLATION_MUR, new Hashtable<Object, Dpe>());
        for (DateIsolationMurEnum value : DateIsolationMurEnum.values()) {
            Dpe dpe = new Dpe(true);
            EventManager.getInstance().addListener(Channel.DPE, dpe);
            EventManager.getInstance().addListener(Channel.FAKE_DPE, dpe);
            fake_dpes.get(DpeEvent.DATE_ISOLATION_MUR).put(value, dpe);
        }

        fake_dpes.put(DpeEvent.ABONNEMENT_ELECTRIQUE, new Hashtable<Object, Dpe>());
        for (TypeAbonnementElectriqueEnum value : TypeAbonnementElectriqueEnum.values()) {
            Dpe dpe = new Dpe(true);
            EventManager.getInstance().addListener(Channel.DPE, dpe);
            EventManager.getInstance().addListener(Channel.FAKE_DPE, dpe);
            fake_dpes.get(DpeEvent.ABONNEMENT_ELECTRIQUE).put(value, dpe);
        }

        fake_dpes.put(DpeEvent.EQUIPEMENT_ECLAIRAGE, new Hashtable<Object, Dpe>());
        for (TypeEquipementEclairageEnum value : TypeEquipementEclairageEnum.values()) {
            Dpe dpe = new Dpe(true);
            EventManager.getInstance().addListener(Channel.DPE, dpe);
            EventManager.getInstance().addListener(Channel.FAKE_DPE, dpe);
            fake_dpes.get(DpeEvent.EQUIPEMENT_ECLAIRAGE).put(value, dpe);
        }

        fake_dpes.put(DpeEvent.TYPE_VENTILATION, new Hashtable<Object, Dpe>());
        for (TypeVentilationEnum value : TypeVentilationEnum.values()) {
            Dpe dpe = new Dpe(true);
            EventManager.getInstance().addListener(Channel.DPE, dpe);
            EventManager.getInstance().addListener(Channel.FAKE_DPE, dpe);
            fake_dpes.get(DpeEvent.TYPE_VENTILATION).put(value, dpe);
        }

        fake_dpes.put(DpeEvent.EQUIPEMENT_CUISSON, new Hashtable<Object, Dpe>());
        for (TypeEquipementCuissonEnum value : TypeEquipementCuissonEnum.values()) {
            Dpe dpe = new Dpe(true);
            EventManager.getInstance().addListener(Channel.DPE, dpe);
            EventManager.getInstance().addListener(Channel.FAKE_DPE, dpe);
            fake_dpes.get(DpeEvent.EQUIPEMENT_CUISSON).put(value, dpe);
        }

        fake_dpes.put(DpeEvent.TYPE_VITRAGE_MENUISERIE, new Hashtable<Object, Dpe>());
        for (TypeVitrageEnum value : TypeVitrageEnum.values()) {
            Dpe dpe = new Dpe(true);
            EventManager.getInstance().addListener(Channel.DPE, dpe);
            EventManager.getInstance().addListener(Channel.FAKE_DPE, dpe);
            fake_dpes.get(DpeEvent.TYPE_VITRAGE_MENUISERIE).put(value, dpe);
        }

        fake_dpes.put(DpeEvent.TYPE_FERMETURE_MENUISERIE, new Hashtable<Object, Dpe>());
        for (TypeFermetureEnum value : TypeFermetureEnum.values()) {
            Dpe dpe = new Dpe(true);
            EventManager.getInstance().addListener(Channel.DPE, dpe);
            EventManager.getInstance().addListener(Channel.FAKE_DPE, dpe);
            fake_dpes.get(DpeEvent.TYPE_FERMETURE_MENUISERIE).put(value, dpe);
        }

        fake_dpes.put(DpeEvent.CHAUFFAGE_UNIQUE, new Hashtable<Object, Dpe>());
        for (Chauffage.Generateur value : Chauffage.Generateur.values()) {
            Dpe dpe = new Dpe(true);
            EventManager.getInstance().addListener(Channel.DPE, dpe);
            EventManager.getInstance().addListener(Channel.FAKE_DPE, dpe);
            fake_dpes.get(DpeEvent.CHAUFFAGE_UNIQUE).put(value, dpe);
        }
    }

    public void calculate_all() {
        for (Map.Entry<DpeEvent, Hashtable<Object, Dpe>> dpes : fake_dpes.entrySet()) {
            DpeEvent event = dpes.getKey();
            for (Map.Entry<Object, Dpe> entry : dpes.getValue().entrySet()) {
                Object value = entry.getKey();
                Dpe dpe = entry.getValue();

                HashMap<String, Object> currentItems = new HashMap<String, Object>();
                currentItems.put("lastValue", value);
                currentItems.put("dpe", dpe);
                currentItems.put("eventRequest", EventRequest.UPDATE_STATE);
                Event e = new Event(event, currentItems);
                EventManager.getInstance().put(Channel.FAKE_DPE, e);
            }
        }
    }

}
