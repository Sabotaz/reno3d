package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import fr.limsi.rorqual.core.utils.AssetManager;

/**
 * Created by christophe on 08/07/15.
 */
public class LayoutReader {

    static Skin skin = (Skin) AssetManager.getInstance().get("uiskin");

    public static Actor readLayout(String name) {
        FileHandle handle = Gdx.files.internal(name);
        JsonValue json = new JsonReader().parse(handle.readString());
        return getActor(json);
    }

    public static Actor getActor(JsonValue json) {
        switch(json.getString("type", "")) {
            case "TabWindow":
                return makeTabWindow(json);
            case "Table":
                return makeTable(json);
            case "ButtonGroup":
                return makeButtonGroup(json);
            case "TextButton":
                return makeTextButton(json);
            default:
                return null;

        }
    }

    public static Actor makeTabWindow(JsonValue json) {

        TabWindow tabWindow= new TabWindow();
        tabWindow.setName(json.getString("name", ""));
        if (json.get("content") != null) {
            JsonValue json_tab;
            Actor tab;
            int i = 0;
            while ((json_tab = json.get("content").get(i)) != null) {
                if ((tab = getActor(json_tab)) != null)
                    tabWindow.addTable(tab);
                i++;
            }
        }
        return tabWindow;
    }

    public static Actor makeTable(JsonValue json) {

        Table table= new Table();
        table.setName(json.getString("name", ""));
        if (json.get("content") != null) {
            JsonValue json_child;
            Actor child;
            int i = 0;
            while ((json_child = json.get("content").get(i)) != null) {
                if ((child = getActor(json_child)) != null)
                    table.add(child);
                i++;
            }
        }
        return table;
    }

    public static Actor makeButtonGroup(JsonValue json) {

        ButtonGroup<Button> buttons= new ButtonGroup<Button>();

        Table table= new Table();
        if (json.get("content") != null) {
            JsonValue json_child;
            Actor child;
            int i = 0;
            while ((json_child = json.get("content").get(i)) != null) {
                if ((child = getActor(json_child)) != null) {
                    if (child instanceof Button) {
                        table.add(child).row();
                        buttons.add((Button)child);
                    }
                }
                i++;
            }
        }
        return table;
    }

    public static Actor makeTextButton(JsonValue json) {

        TextButton textButton = new TextButton(json.getString("text", ""), skin);
        return textButton;
    }

}
