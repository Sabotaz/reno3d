package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.HashMap;

import fr.limsi.rorqual.core.utils.AssetManager;

/**
 * Created by christophe on 08/07/15.
 */
public class TabWindow extends Table {
    HashMap<Button, Actor> tabs = new HashMap<Button, Actor>();
    HashMap<Actor, Button> reversed_tabs = new HashMap<Actor, Button>();
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

        Window.WindowStyle ws = skin.get(Window.WindowStyle.class);
        ws.titleFont = (BitmapFont)AssetManager.getInstance().get("default.fnt");

        window = new Window("", ws);
        buttonTab = new Table();
        contentTab = new Table();
        window.add(buttonTab).left().expandX().row();
        window.add(contentTab).left().padTop(5).expandX().fillX().left();
        contentTab.setDebug(true);

        window.addListener(new EventListener() {
            public boolean handle(Event event) {
                return true;
            }
        });
        this.add(window);
    }

    public void setTitle(String title) {
        window.setTitle(title);
    }
    public void addTable(Actor tab) {
        Skin skin = (Skin) AssetManager.getInstance().get("uiskin");

        TextButton.TextButtonStyle tbs = skin.get("tab", TextButton.TextButtonStyle.class);
        tbs.font = (BitmapFont)AssetManager.getInstance().get("default.fnt");

        Button button = new TextButton(tab.getName(), tbs);
        button.addListener(clickListener);
        tabs.put(button, tab);
        reversed_tabs.put(tab, button);

        buttons.add(button);
        buttonTab.add(button).padLeft(1).padTop(1);
        if (no_content_yet) {
            last = tabs.get(button);
            contentTab.add(last).expandX().fillX().left();
            no_content_yet = false;
        }
    }

    Actor last = null;

    public void buttonClicked(Button button) {

        Actor next = tabs.get(button);
        float lastWidth = last.getWidth();
        if (lastWidth<buttonTab.getWidth()){
            lastWidth=buttonTab.getWidth();
        }
        float lastHeight = last.getHeight();
        float nextWidth=0;
        float nextHeight = 0;
        float lastX=this.getX();
        float lastY=this.getY();
        float nextX=lastX;
        float nextY=lastY;

        if (next instanceof Table){
            nextWidth = ((Table) next).getPrefWidth();
            nextHeight = ((Table) next).getPrefHeight();
            if (nextWidth<buttonTab.getWidth()){
                nextWidth=buttonTab.getWidth();
            }
            nextX += (int)(nextWidth-lastWidth)/2;
            nextY += (int)(lastHeight-nextHeight)/2;
        }

        contentTab.clear();
        contentTab.add(next).expandX().fillX().left();

        last = next;
        button.setChecked(true);
        this.setPosition(nextX,nextY);
    }

    public void setTableDisabled(Actor table, boolean visibility) {
        if (reversed_tabs.containsKey(table)) {
            Button b = reversed_tabs.get(table);
            b.setVisible(visibility);
        }
    }
}
