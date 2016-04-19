package fr.limsi.rorqual.core.utils.serialization;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.limsi.rorqual.core.dpe.Dpe;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.logic.CameraEngine;
import fr.limsi.rorqual.core.logic.Deleter;
import fr.limsi.rorqual.core.logic.Logic;
import fr.limsi.rorqual.core.model.Batiment;
import fr.limsi.rorqual.core.model.Etage;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.utils.Coin;
import fr.limsi.rorqual.core.ui.DpeUi;

/**
 * Created by christophe on 11/01/16.
 */

@XStreamAlias("model")
public class SerialHolder {

    @XStreamAlias("coins")
    HashMap<Integer, ArrayList<Coin>> coins = Coin.getCoins();

    @XStreamAlias("batiment")
    Batiment batiment = ModelHolder.getInstance().getBatiment();

    @XStreamAlias("dpe")
    Dpe dpe = Dpe.getInstance();

    public void recreateModel() {
        Logic.getInstance().stop();
        Deleter.deleteBatiment();
        CameraEngine.getInstance().reset();
        Coin.clearAll();
        ModelHolder.getInstance().setBatiment(batiment);
        batiment.reload();
        EventManager.getInstance().removeListener(Channel.DPE, Dpe.getInstance());
        dpe.setFenetreList(batiment.getFenetres());
        dpe.setPorteFenetreList(batiment.getPorteFenetres());
        dpe.setPorteList(batiment.getPortes());
        dpe.actualiseDpMur();
        Dpe.loadDpe(dpe);
        DpeUi.clear();
    }

}
