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
        window.add(contentTab).left().padTop(5);
        this.add(window);
    }

    public void addTable(Actor tab) {
        Skin skin = (Skin) AssetManager.getInstance().get("uiskin");
        Button button = new TextButton(tab.getName(), skin, "tab");
        button.addListener(clickListener);
        tabs.put(button, tab);
        buttons.add(button);
        buttonTab.add(button).padLeft(1).padTop(1);

        if (no_content_yet) {
            last = tabs.get(button);
            contentTab.add(last);
            no_content_yet = false;
        }
    }

    Actor last = null;

    public void buttonClicked(Button button) {
        float wcx = window.getX();
        float wcy = window.getY();
        float wcw = window.getWidth();
        float wch = window.getHeight();
        float tx = this.getX();
        float ty = this.getY();

        Actor next = tabs.get(button);
        float last_mid_height = last.getHeight()/2;
        float next_mid_height = next.getHeight()/2;
        float last_mid_width = last.getWidth()/2;
        float next_mid_width = next.getWidth()/2;

        float wx = tx + wcx + wcw/2 - (next_mid_width-last_mid_width);
        float wy = ty + wcy + wch/2+1 - (next_mid_height-last_mid_height);


        contentTab.clear();
        contentTab.add(next);
        last = next;
        //button.setChecked(true);

        this.setPosition((int) wx, (int)wy);
        window.setPosition(-(int)(wcw/2), -(int)(wch/2));
    }


}
