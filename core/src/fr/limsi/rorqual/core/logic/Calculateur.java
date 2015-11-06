package fr.limsi.rorqual.core.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import eu.mihosoft.vrl.v3d.Main;
import fr.limsi.rorqual.core.dpe.enums.wallproperties.TypeMurEnum;
import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.model.ModelHolder;
import fr.limsi.rorqual.core.model.Mur;
import fr.limsi.rorqual.core.model.Slab;
import fr.limsi.rorqual.core.ui.MainUiControleur;
import fr.limsi.rorqual.core.utils.AssetManager;

/**
 * Created by ricordeau on 05/11/15.
 */
public class Calculateur {

    private float surfaceHabitable;
    private float surfaceAuSol;
    private float surfaceTotaleMurInterieur;
    private float surfaceTotaleMurExterieur;
    private float volumeTotal;

    /*** Constructeur en singleton ***/
    private static class CalculateurHolder
    {
        /** Instance unique non préinitialisée */
        private final static Calculateur INSTANCE = new Calculateur();
    }

    public static synchronized Calculateur getInstance() {
        return CalculateurHolder.INSTANCE;
    }

    public void actualiseCalculs(){
        this.calcSurfaceHabitable();
        this.calcSurfaceAuSol();
        this.calcSurfaceTotaleMurInterieur();
        this.calcSurfaceTotaleMurExterieur();
        this.calcVolumeTotal();
    }

    public Window getWindow(){
        Skin skin = (Skin)AssetManager.getInstance().get("uiskin");
        Label.LabelStyle ls = new Label.LabelStyle((BitmapFont)AssetManager.getInstance().get("default.fnt"), Color.BLACK);
        Window w = new Window("Information",skin);
        Label sh_label = new Label("Surface habitable = "+Float.toString(surfaceHabitable)+" m²",ls);
        Label ss_label = new Label("Surface au sol = "+Float.toString(surfaceAuSol)+" m²",ls);
        Label stmi_label = new Label("Surface mur intérieur = "+Float.toString(surfaceTotaleMurInterieur)+" m²",ls);
        Label stme_label = new Label("Surface mur extérieur = "+Float.toString(surfaceTotaleMurExterieur)+" m²",ls);
        Label vt_label = new Label("Volume total = "+Float.toString(volumeTotal)+" m³",ls);
        w.setDebug(true);
        w.add(sh_label).left().pad(2).row();
        w.add(ss_label).left().pad(2).row();
        w.add(stmi_label).left().pad(2).row();
        w.add(stme_label).left().pad(2).row();
        w.add(vt_label).pad(2).left();
        w.setWidth(w.getPrefWidth());
        MainUiControleur.getInstance().addTb(w);
        return w;
    }

    private void calcSurfaceHabitable(){
        float tampon=0;
        for (Slab s: ModelHolder.getInstance().getBatiment().getSlabs()){
            tampon+=s.getSurface();
        }
        this.surfaceHabitable = tampon;
    }

    private void calcSurfaceAuSol(){
        float tampon=0;
        for (Slab s: ModelHolder.getInstance().getBatiment().getSlabs()){
            if(s.getEtage().isFirst()){
                tampon+=s.getSurface();
            }
        }
        this.surfaceAuSol = tampon;
    }

    private void calcSurfaceTotaleMurInterieur(){
        float tampon=0;
        for (Mur m : ModelHolder.getInstance().getBatiment().getMurs()) {
            if(m.getTypeMur().equals(TypeMurEnum.MUR_INTERIEUR)){
                tampon += 2*m.getSurface();
            }else{
                tampon += m.getSurface();
            }
        }
        this.surfaceTotaleMurInterieur = tampon;
    }

    private void calcSurfaceTotaleMurExterieur(){
        float tampon=0;
        for (Mur m : ModelHolder.getInstance().getBatiment().getMurs()) {
            if(m.getTypeMur().equals(TypeMurEnum.MUR_DONNANT_SUR_EXTERIEUR)){
                tampon += m.getSurface();
            }
        }
        this.surfaceTotaleMurExterieur = tampon;
    }

    private void calcVolumeTotal(){
        float tampon=0;
        for (Slab s: ModelHolder.getInstance().getBatiment().getSlabs()){
            tampon+=s.getVolume();
        }
        this.volumeTotal = tampon;
    }

    public float getSurfaceHabitable() {
        return surfaceHabitable;
    }

    public float getSurfaceAuSol() {
        return surfaceAuSol;
    }

    public float getSurfaceTotaleMurInterieur() {
        return surfaceTotaleMurInterieur;
    }

    public float getSurfaceTotaleMurExterieur() {
        return surfaceTotaleMurExterieur;
    }

    public float getVolumeTotal() {
        return volumeTotal;
    }

}
