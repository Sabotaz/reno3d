package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;

import fr.limsi.rorqual.core.event.Channel;
import fr.limsi.rorqual.core.event.Event;
import fr.limsi.rorqual.core.event.EventListener;
import fr.limsi.rorqual.core.event.EventManager;
import fr.limsi.rorqual.core.event.EventRequest;
import fr.limsi.rorqual.core.event.EventType;
import fr.limsi.rorqual.core.utils.AssetManager;

/**
 * Created by christophe on 08/07/15.
 */
public class Layout {

    Skin skin = (Skin) AssetManager.getInstance().get("uiskin");

    HashMap<String, Actor> actors_ids = new HashMap<String, Actor>();

    Actor root = null;

    private class Updater implements EventListener {
        private Channel channel;
        private EventType eventType;

        private Object default_value = null;
        private boolean default_value_received = false;

        public Updater(Channel c, EventType e) {
            channel = c;
            eventType = e;
            HashMap<String,Object> items = new HashMap<String, Object>();
            items.put("userObject",userObject);
            items.put("eventRequest",EventRequest.GET_STATE);
            EventManager.getInstance().addListener(channel, Updater.this);
            Event ev = new Event(eventType, items);
            EventManager.getInstance().put(channel, ev);
        }

        public void trigger(Object content) {
            Event e = new Event(eventType, content);
            EventManager.getInstance().put(channel, e);
        }

        public void notify(Channel c, Event e) {
            if (e.getEventType() == (EventType) eventType) {
                HashMap<String,Object> response = (HashMap<String,Object>) e.getUserObject();
                if (response.get("userObject") == userObject && response.get("eventRequest") == EventRequest.CURRENT_STATE) {
                    default_value = response.get("lastValue");
                    default_value_received = true;
                }
            }
        }

        public Object getDefaultValue() {
            while (!default_value_received) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ie) {

                }
            }
            return default_value;
        }
    }

    Object userObject = null;

    private Layout(String name, Object o) {
        userObject = o;
        root = readLayout(name);
    }

    public static Layout fromJson(String name, Object userObject) {
        return new Layout(name, userObject);
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
        Actor root = getActor(json, null);
        return root;
    }

    private Object getEnumConstant(JsonValue json, String key) {
        try {
            String str = json.getString(key);
            int last_point = str.lastIndexOf(".");
            String enum_name = str.substring(0, last_point);
            String enum_value = str.substring(last_point + 1, str.length());
            Class<?> clz = Class.forName(enum_name);
            Object[] consts = clz.getEnumConstants();
            Object value = null;
            for (Object o : consts) {
                if (o.toString().equals(enum_value))
                    value = o;
            }
            return value;
        }
        catch (ClassNotFoundException cnfe) {
            System.out.println(cnfe);
            return null;
        }
    }

    private Actor getActor(JsonValue json, Updater parent_updater) {
        Actor actor;
        Updater updater = parent_updater;

        if (json.has("event") && json.has("channel")) {
            Object event_value = getEnumConstant(json, "event");
            Object channel_value = getEnumConstant(json, "channel");

            if (event_value != null && channel_value != null) {
                updater = new Updater((Channel) channel_value, (EventType) event_value);
            }
        }

        switch(json.getString("type", "")) {
            case "TabWindow":
                actor = makeTabWindow(json, updater);
                break;
            case "Table":
                actor = makeTable(json, updater);
                break;
            case "ButtonGroup":
                actor = makeButtonGroup(json, updater);
                break;
            case "TextButton":
                actor = makeTextButton(json, updater);
                break;
            case "ImageButton":
                actor = makeImageButton(json, updater);
                break;
            default:
                return null;
        }

        if (json.has("id"))
            actors_ids.put(json.getString("id"), actor);

        return actor;
    }

    private Actor makeTabWindow(JsonValue json, Updater updater) {

        TabWindow tabWindow= new TabWindow();
        tabWindow.setTitle(json.getString("name", ""));
        if (json.get("content") != null) {
            JsonValue json_tab;
            Actor tab;
            int i = 0;
            while ((json_tab = json.get("content").get(i)) != null) {
                if ((tab = getActor(json_tab, updater)) != null)
                    tabWindow.addTable(tab);
                i++;
            }
        }
        return tabWindow;
    }

    private Actor makeTable(JsonValue json, Updater updater) {

        Table table = new Table();
        table.setName(json.getString("name", ""));
        if (json.get("content") != null) {
            JsonValue json_child;
            Actor child;
            int i = 0;
            while ((json_child = json.get("content").get(i)) != null) {
                if ((child = getActor(json_child, updater)) != null)
                    table.add(child).expandX().fillX().left();
                i++;
            }
        }
        return table;
    }

    private Actor makeButtonGroup(JsonValue json, Updater updater) {

        ButtonGroup<Button> buttons= new ButtonGroup<Button>();
        buttons.setMinCheckCount(0);
        Table table= new Table();
        boolean row = true;
        String layout = json.getString("layout", "row");
        if (layout.equals("column"))
            row = false;

        String align = json.getString("align", "left");
        switch (align) {
            case "left":
                table.align(Align.left);
                break;
            case "right":
                table.align(Align.right);
                break;
            case "center":
                table.align(Align.center);
                break;
        }

        if (json.get("content") != null) {
            JsonValue json_child;
            Actor child;
            int i = 0;
            while ((json_child = json.get("content").get(i)) != null) {
                if ((child = getActor(json_child, updater)) != null) {
                    if (child instanceof Button) {
                        if (row)
                            table.add(child).left().pad(1).row();
                        else
                            table.add(child).pad(1);
                        buttons.add((Button)child);
                    }
                }
                i++;
            }
        }
        buttons.setMinCheckCount(1);
        return table;
    }

    private Actor makeTextButton(JsonValue json, Updater updater) {
        TextButton.TextButtonStyle tbs = skin.get("toggle", TextButton.TextButtonStyle.class);
        tbs.font = (BitmapFont)AssetManager.getInstance().get("default.fnt");
        TextButton textButton = new TextButton(json.getString("text", ""), tbs);

        if (updater != null & json.has("value")) {
            Object value_value = getEnumConstant(json, "value");

            if (updater.getDefaultValue() == value_value) {
                textButton.setChecked(true);
            }

            if (value_value != null) {
                final Object last_value = value_value;
                final Updater last_updater = updater;
                textButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        HashMap<String,Object> items = new HashMap<String, Object>();
                        items.put("userObject",userObject);
                        items.put("eventRequest",EventRequest.UPDATE_STATE);
                        items.put("lastValue",last_value);
                        items.put("layout",Layout.this);
                        last_updater.trigger(items);
                    }
                });
            }
        }
        return textButton;
    }

    private Actor makeImageButton (JsonValue json, Updater updater){
        String nameImage = json.getString("image");
        Texture textureImage = (Texture) AssetManager.getInstance().get(nameImage);
        Image image = new Image(textureImage);

        final ImageButton imageButton = new ImageButton(image.getDrawable()) {

            Texture clicked_texture;
            {
                Pixmap p = new Pixmap((int)this.getWidth(),(int)this.getHeight(),Pixmap.Format.RGBA8888);
                p.setColor(Color.RED);
                p.drawRectangle(0, 0, (int)this.getWidth(),(int)this.getHeight());
                p.drawRectangle(1, 1, (int)this.getWidth()-2,(int)this.getHeight()-2);
                clicked_texture = new Texture(p);
            }

            @Override
            public void draw(Batch batch, float arg1) {
                super.draw(batch, arg1);
                if (this.isChecked())
                    batch.draw(clicked_texture, this.getX(), this.getY());
            }
        };

        if (updater != null & json.has("value")) {
            Object value_value = getEnumConstant(json, "value");

            if (updater.getDefaultValue() == value_value) {
                imageButton.setChecked(true);
            }

            if (value_value != null) {
                final Object last_value = value_value;
                final Updater last_updater = updater;
                imageButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        HashMap<String,Object> items = new HashMap<String, Object>();
                        items.put("userObject",userObject);
                        items.put("eventRequest",EventRequest.UPDATE_STATE);
                        items.put("lastValue",last_value);
                        items.put("layout",Layout.this);
                        last_updater.trigger(items);
                    }
                });
            }
        }
        return imageButton;
    }
}
