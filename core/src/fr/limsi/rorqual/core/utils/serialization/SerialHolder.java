package fr.limsi.rorqual.core.utils.serialization;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.limsi.rorqual.core.model.Batiment;
import fr.limsi.rorqual.core.model.Etage;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.utils.Coin;

/**
 * Created by christophe on 11/01/16.
 */

@XStreamAlias("model")
public class SerialHolder {

    @XStreamAlias("coins")
    HashMap<Integer, ArrayList<Coin>> coins = Coin.getCoins();

    @XStreamAlias("batiment")
    Batiment batiment = ModelHolder.getInstance().getBatiment();

    public void recreateModel() {
        Coin.clearAll();
        ModelHolder.getInstance().setBatiment(batiment);
        batiment.reload();
    }

}
