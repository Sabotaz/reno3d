package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;

import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.event.EventType;
import fr.limsi.rorqual.core.utils.AssetManager;

/**
 * Created by christophe on 08/07/15.
 */
public class Layout {

    Skin skin = (Skin) AssetManager.getInstance().get("uiskin");

    HashMap<String, Actor> actors_ids = new HashMap<String, Actor>();

    Actor root = null;

    private class Updater {
        private Channel channel;
        private EventType event;
        public Updater(Channel c, EventType e) {
            channel = c;
            event = e;
        }
        public void trigger(Object content) {
            Event e = new Event(event, content);
            EventManager.getInstance().put(channel, e);
        }
    }

    private Layout(String name) {
        root = readLayout(name);
    }

    public static Layout fromJson(String name) {
        return new Layout(name);
    }

    public Actor getRoot() {
        return root;
    }

    public Actor getFromId(String id) {
        return actors_ids.get(id);
    }

    private Actor readLayout(String name) {
        FileHandle handle = Gdx.files.internal(name);
        JsonValue json = new JsonReader().parse(handle.readString());
        Actor root = getActor(json, null, null);
        return root;
    }

    private Actor getActor(JsonValue json, Updater parent_updater, Actor root) {
        Actor actor;
        Updater updater = parent_updater;

        if (json.has("event") && json.has("channel")) {
            try {
                String event =  json.getString("event");
                int last_point = event.lastIndexOf(".");
                String enum_name = event.substring(0, last_point);
                String enum_value = event.substring(last_point + 1, event.length());
                Class<?> clz = Class.forName(enum_name);
                Object[] consts = clz.getEnumConstants();
                Object event_value = null;
                for (Object o : consts) {
                    if (o.toString().equals(enum_value))
                        event_value = o;
                }

                String channel =  json.getString("channel");
                last_point = channel.lastIndexOf(".");
                enum_name = channel.substring(0, last_point);
                enum_value = channel.substring(last_point + 1, channel.length());
                clz = Class.forName(enum_name);
                consts = clz.getEnumConstants();
                Object channel_value = null;
                for (Object o : consts) {
                    if (o.toString().equals(enum_value))
                        channel_value = o;
                }

                if (event_value != null && channel_value != null) {
                    updater = new Updater((Channel) channel_value, (EventType) event_value);
                }

            } catch (ClassNotFoundException cnfe) {
                System.out.println(cnfe);
            }
        }

        switch(json.getString("type", "")) {
            case "TabWindow":
                actor = makeTabWindow(json, updater, root);
                break;
            case "Table":
                actor = makeTable(json, updater, root);
                break;
            case "ButtonGroup":
                actor = makeButtonGroup(json, updater, root);
                break;
            case "TextButton":
                actor = makeTextButton(json, updater, root);
                break;
            default:
                return null;
        }

        if (json.has("id"))
            actors_ids.put(json.getString("id"), actor);

        return actor;
    }

    private Actor makeTabWindow(JsonValue json, Updater updater, Actor root) {

        TabWindow tabWindow= new TabWindow();
        tabWindow.setName(json.getString("name", ""));
        if (root == null)
            root = tabWindow;
        if (json.get("content") != null) {
            JsonValue json_tab;
            Actor tab;
            int i = 0;
            while ((json_tab = json.get("content").get(i)) != null) {
                if ((tab = getActor(json_tab, updater, root)) != null)
                    tabWindow.addTable(tab);
                i++;
            }
        }
        return tabWindow;
    }

    private Actor makeTable(JsonValue json, Updater updater, Actor root) {

        Table table = new Table();
        table.setName(json.getString("name", ""));
        if (root == null)
            root = table;
        if (json.get("content") != null) {
            JsonValue json_child;
            Actor child;
            int i = 0;
            while ((json_child = json.get("content").get(i)) != null) {
                if ((child = getActor(json_child, updater, root)) != null)
                    table.add(child);
                i++;
            }
        }
        return table;
    }

    private Actor makeButtonGroup(JsonValue json, Updater updater, Actor root) {

        ButtonGroup<Button> buttons= new ButtonGroup<Button>();
        Table table= new Table();
        if (root == null)
            root = table;
        if (json.get("content") != null) {
            JsonValue json_child;
            Actor child;
            int i = 0;
            while ((json_child = json.get("content").get(i)) != null) {
                if ((child = getActor(json_child, updater, root)) != null) {
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

    private Actor makeTextButton(JsonValue json, Updater updater, Actor root) {

        TextButton textButton = new TextButton(json.getString("text", ""), skin);

        if (root == null)
            root = textButton;

        if (updater != null & json.has("value")) {
            try {
                String value =  json.getString("value");
                int last_point = value.lastIndexOf(".");
                String enum_name = value.substring(0, last_point);
                String enum_value = value.substring(last_point + 1, value.length());
                Class<?> clz = Class.forName(enum_name);
                Object[] consts = clz.getEnumConstants();
                Object value_value = null;
                for (Object o : consts) {
                    if (o.toString().equals(enum_value))
                        value_value = o;
                }

                if (value_value != null) {
                    final Object last_value = value_value;
                    final Updater last_updater = updater;
                    final Actor final_root = root;
                    textButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            Object[] value = new Object[3];
                            value[0] = final_root.getUserObject();
                            value[1] = last_value;
                            value[2] = Layout.this;

                            last_updater.trigger(value);
                        }
                    });
                }

            } catch (ClassNotFoundException cnfe) {
                System.out.println(cnfe);
            }
        }

        return textButton;
    }

}
