package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.HashMap;

import fr.limsi.rorqual.core.utils.AssetManager;

/**
 * Created by christophe on 08/07/15.
 */
public class TabWindow extends Table {
    HashMap<Button, Actor> tabs = new HashMap<Button, Actor>();
    ButtonGroup<Button> buttons = new ButtonGroup<Button>();

    Window window;
    Table buttonTab;
    Table contentTab;

    boolean no_content_yet = true;

    private InputListener clickListener = new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            if (event.getTarget() instanceof Button) {
                buttonClicked((Button) event.getTarget());
            } else if (event.getTarget().getParent() instanceof Button) {
                buttonClicked((Button) event.getTarget().getParent());
            }
        };
    };

    public TabWindow() {
        Skin skin = (Skin) AssetManager.getInstance().get("uiskin");
        window = new Window("Properties", skin);
        buttonTab = new Table();
        contentTab = new Table();
        window.add(buttonTab).row();
        window.add(contentTab);
        window.setDebug(true);
        this.add(window);
    }

    public void addTable(Actor tab) {
        Skin skin = (Skin) AssetManager.getInstance().get("uiskin");
        Button button = new TextButton(tab.getName(), skin);
        button.addListener(clickListener);
        tabs.put(button, tab);
        buttons.add(button);
        buttonTab.add(button);

        if (no_content_yet) {
            contentTab.add(tabs.get(button));
            no_content_yet = false;
        }
    }

    public void buttonClicked(Button button) {
        float wcx = window.getX();
        float wcy = window.getY();
        float wcw = window.getWidth();
        float wch = window.getHeight();
        float tx = this.getX();
        float ty = this.getY();

        float wx = tx + wcx + wcw/2;
        float wy = ty + wcy + wch/2;

        contentTab.clear();
        contentTab.add(tabs.get(button));

        this.setPosition(wx, wy);
        window.setPosition(-wcw/2, -wch/2);
    }


}
