package fr.limsi.rorqual.core.dpe;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
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
import fr.limsi.rorqual.core.ui.DpeUi;
import fr.limsi.rorqual.core.ui.Layout;
import fr.limsi.rorqual.core.ui.MainUiControleur;
import fr.limsi.rorqual.core.utils.AssetManager;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;

/**
 * Created by christophe on 30/03/16.
 */
public class DpeKartoffelator {

    Hashtable<DpeEvent, ArrayList<Object>> fake_dpes = new Hashtable<DpeEvent, ArrayList<Object>>();

    public DpeKartoffelator() {
        create_fake_dpe();
    }

    public void create_fake_dpe() {

        fake_dpes.put(DpeEvent.DATE_ISOLATION_MUR, new ArrayList<Object>());
        for (DateIsolationMurEnum value : DateIsolationMurEnum.values()) {
            fake_dpes.get(DpeEvent.DATE_ISOLATION_MUR).add(value);
        }

        fake_dpes.put(DpeEvent.ABONNEMENT_ELECTRIQUE, new ArrayList<Object>());
        for (TypeAbonnementElectriqueEnum value : TypeAbonnementElectriqueEnum.values()) {
            fake_dpes.get(DpeEvent.ABONNEMENT_ELECTRIQUE).add(value);
        }

        fake_dpes.put(DpeEvent.EQUIPEMENT_ECLAIRAGE, new ArrayList<Object>());
        for (TypeEquipementEclairageEnum value : TypeEquipementEclairageEnum.values()) {
            fake_dpes.get(DpeEvent.EQUIPEMENT_ECLAIRAGE).add(value);
        }

        fake_dpes.put(DpeEvent.TYPE_VENTILATION, new ArrayList<Object>());
        for (TypeVentilationEnum value : TypeVentilationEnum.values()) {
            fake_dpes.get(DpeEvent.TYPE_VENTILATION).add(value);
        }

        fake_dpes.put(DpeEvent.EQUIPEMENT_CUISSON, new ArrayList<Object>());
        for (TypeEquipementCuissonEnum value : TypeEquipementCuissonEnum.values()) {
            fake_dpes.get(DpeEvent.EQUIPEMENT_CUISSON).add(value);
        }

        fake_dpes.put(DpeEvent.TYPE_VITRAGE_MENUISERIE, new ArrayList<Object>());
        for (TypeVitrageEnum value : TypeVitrageEnum.values()) {
            fake_dpes.get(DpeEvent.TYPE_VITRAGE_MENUISERIE).add(value);
        }

        fake_dpes.put(DpeEvent.TYPE_FERMETURE_MENUISERIE, new ArrayList<Object>());
        for (TypeFermetureEnum value : TypeFermetureEnum.values()) {
            fake_dpes.get(DpeEvent.TYPE_FERMETURE_MENUISERIE).add(value);
        }

        fake_dpes.put(DpeEvent.CHAUFFAGE_UNIQUE, new ArrayList<Object>());
        for (Chauffage.Generateur value : Chauffage.Generateur.values()) {
            fake_dpes.get(DpeEvent.CHAUFFAGE_UNIQUE).add(value);
        }
    }

    public void calculate_all() {

        FileHandle file = Gdx.files.getFileHandle("data/misc/prices", Files.FileType.Internal);
        I18NBundle i18n = I18NBundle.createBundle(file, Locale.FRENCH);

        Dpe dpe = Dpe.getInstance();
        for (Map.Entry<DpeEvent, ArrayList<Object>> keys : fake_dpes.entrySet()) {
            DpeEvent event = keys.getKey();
            for (Object value : keys.getValue()) {
                float avant = dpe.getScoreDpe();
                float score = dpe.emulateChange(event, value);
                float percent = ((int)(-((score-avant)/avant)*1000))/10.0f;
                int price = Integer.parseInt(i18n.get(event + "#" + (value instanceof Chauffage.Generateur ? ((Chauffage.Generateur)value).name() : value.toString())));
                update_score(event, value, percent, price);
            }
        }
    }

    public void update_score(DpeEvent event, Object value, float score, int price) {
        Layout layout = null;
        switch (event) {
            case ABONNEMENT_ELECTRIQUE:
            case EQUIPEMENT_ECLAIRAGE:
            case TYPE_VENTILATION:
            case EQUIPEMENT_CUISSON:
                layout = DpeUi.getLayout(DpeEvent.INFOS_GENERALES);
                break;
            case CHAUFFAGE_UNIQUE:
                layout = DpeUi.getLayout(DpeEvent.INFOS_CHAUFFAGE);
                break;
            case TYPE_VITRAGE_MENUISERIE:
            case TYPE_FERMETURE_MENUISERIE:
                layout = DpeUi.getLayout(DpeEvent.INFOS_FENETRES);
                break;
            case DATE_ISOLATION_MUR:
                layout = DpeUi.getLayout(DpeEvent.INFOS_MURS);
                break;
        }

        Actor actor = value instanceof Chauffage.Generateur ? layout.getFromId(((Chauffage.Generateur)value).name()) : layout.getFromId(value.toString());
        if (actor != null) {
            if (actor instanceof CheckBox) {
                CheckBox cb = (CheckBox) actor;
                Group g = cb.getLabel().getParent();
                if (!(g instanceof HorizontalGroup)) {
                    HorizontalGroup hg = new HorizontalGroup();
                    hg.addActor(cb.getLabel());

                    Skin skin = (Skin) AssetManager.getInstance().get("uiskin");
                    Label.LabelStyle lbs = skin.get("default",Label.LabelStyle.class);
                    lbs.font = (BitmapFont)AssetManager.getInstance().get("default.fnt");
                    lbs.fontColor = Color.DARK_GRAY;
                    Label label = new Label("",lbs);
                    Label label2 = new Label("",lbs);
                    hg.addActor(label);
                    hg.addActor(label2);

                    hg.padTop(-20).padLeft(20);

                    g.addActor(hg);
                }

                Label label = (Label)cb.getLabel().getParent().getChildren().get(1);

                label.setText(" ("+score + "%)");

                Label prix = (Label)cb.getLabel().getParent().getChildren().get(2);

                prix.setText(" "+price + " euros");

            }
        }

    }

}
