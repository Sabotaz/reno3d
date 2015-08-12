package fr.limsi.rorqual.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;

import fr.limsi.rorqual.core.dpe.enums.generalproperties.DepartementBatimentEnum;
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

    private boolean initialised = false;
    public boolean isInitialised(){return this.initialised;}

    private class Updater implements EventListener {
        private Channel channel;
        private EventType eventType;

        private Object default_value = null;
        private boolean default_value_received = false;

        public Updater(Channel c, EventType e) {
            channel = c;
            eventType = e;
            EventManager.getInstance().addListener(channel, Updater.this);
            HashMap<String,Object> items = new HashMap<String, Object>();
            items.put("userObject",userObject);
            items.put("eventRequest", EventRequest.GET_STATE);
            items.put("layout", Layout.this);
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

        public EventType getEventType(){
            return this.eventType;
        }
    }

    Object userObject = null;

    private Layout(String name, Object o) {
        userObject = o;
        root = readLayout(name);
        this.initialised = true;
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

    private Object[] getClass(JsonValue json, String key) {
        try {
            String enum_name = json.getString(key);
            Class<?> clz = Class.forName(enum_name);
            Object[] consts = clz.getEnumConstants();
            return consts;
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
            case "Button":
                actor = makeButton(json, updater);
                break;
            case "ImageButton":
                actor = makeImageButton(json, updater);
                break;
            case "CheckBox":
                actor = makeCheckBox(json, updater);
                break;
            case "ScrollPaneElement":
                actor = makeScrollPane(json, updater);
                break;
            case "TextField":
                actor = makeTextField(json, updater);
                break;
            default:
                return null;
        }

        if (json.has("id"))
            actors_ids.put(json.getString("id"), actor);

        return actor;
    }

    private Actor makeTabWindow(JsonValue json, Updater updater) {

        TabWindow tabWindow= new TabWindow(json.getFloat("width",900));
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
//        table.setDebug(true);

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

        boolean row = true;
        String layout = json.getString("layout", "row");
        if (layout.equals("column"))
            row = false;

        if (json.has("label")){
            Label.LabelStyle lbs = skin.get("default",Label.LabelStyle.class);
            lbs.font = (BitmapFont)AssetManager.getInstance().get("default.fnt");
            lbs.fontColor = Color.DARK_GRAY;
            Label label = new Label(json.getString("label"),lbs);
            table.add(label).top().width(json.getFloat("labelWidth",516)).pad(json.getFloat("labelPad",1));

//            table.add(label).top().pad(json.getFloat("labelPad",1));
//            System.out.println("prefWidth"+table.getPrefWidth());
        }

        if (json.has("content")) {
            JsonValue json_child;
            Actor child;
            int i = 0;
            while ((json_child = json.get("content").get(i)) != null) {
                if ((child = getActor(json_child, updater)) != null) {
                    Cell c = table.add(child);
                    c.expandX().fillX().left();

                    c.padTop(json_child.getFloat("padTop", 1));
                    c.padBottom(json_child.getFloat("padBottom", 1));
                    c.padLeft(json_child.getFloat("padLeft", 1));
                    c.padRight(json_child.getFloat("padRight", 1));
                    c.pad(json_child.getFloat("pad", 1));

                    if (row)
                        c.left().row();
                }
                i++;
            }
        }

        if (json.has("position")){
            String position = json.getString("position");
            String[] xy = position.split("-");

            switch (xy[0]) {
                case "top":
                    table.setY(Gdx.graphics.getHeight() - table.getPrefHeight() / 2);
                    break;
                case "bottom":
                    table.setY(0);
                    break;
                case "center":
                    table.setY(Gdx.graphics.getHeight() / 2);
                    break;
            }

            switch (xy[1]) {
                case "left":
                    table.setX(0);
                    break;
                case "right":
                    table.setX(Gdx.graphics.getWidth() - table.getPrefWidth());
                    break;
                case "center":
                    table.setX(Gdx.graphics.getWidth()/2);
                    break;
            }
        }
        table.pad(7);
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

                    Cell c = table.add(child);

                    c.size(child.getWidth(), child.getHeight());

                        c.padTop(json_child.getFloat("padTop", 1));
                        c.padBottom(json_child.getFloat("padBottom", 1));
                        c.padLeft(json_child.getFloat("padLeft", 1));
                        c.padRight(json_child.getFloat("padRight", 1));
                        c.pad(json_child.getFloat("pad", 1));

                    if (row)
                        c.left().row();

                    if (child instanceof Button) {
                        buttons.add((Button)child);
                    }
                }
                i++;
            }
        }
        buttons.setMinCheckCount(json.getInt("minChecked", 1));
        buttons.setMaxCheckCount(json.getInt("maxChecked", 1));
        return table;
    }

    private Actor makeButton(JsonValue json, Updater updater) {

        final Button button;

        if (json.has("style")) {
            JsonValue style_list = json.get("style");
            TextButton.TextButtonStyle style = StyleFactory.getTextButtonStyle(style_list.asStringArray());
            button = new Button(style);
        } else {
            button = new Button();
        }

        if (json.has("height") && json.has("width"))
            button.setSize(json.getFloat("width"), json.getFloat("height"));

        if (updater != null & json.has("value")) {
            Object value_value = getEnumConstant(json, "value");

            if (updater.getDefaultValue() == value_value) {
                button.setChecked(true);
            }
            if (value_value != null) {
                final Object last_value = value_value;
                final Updater last_updater = updater;
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
//                        System.out.println("click");
                        HashMap<String,Object> items = new HashMap<String, Object>();
                        items.put("userObject",userObject);
                        items.put("eventRequest",EventRequest.UPDATE_STATE);
                        items.put("lastValue",last_value);
                        items.put("layout", Layout.this);
                        items.put("button",button);
                        last_updater.trigger(items);
                    }
                });
            }
        }
        return button;
    }

    private Actor makeTextButton(JsonValue json, Updater updater) {
        final TextButton textButton;
        TextButton.TextButtonStyle tbs;

        if (json.has("style")) {
            JsonValue style_list = json.get("style");
            tbs = StyleFactory.getTextButtonStyle(style_list.asStringArray());

        } else {
            tbs = skin.get("toggle", TextButton.TextButtonStyle.class);
            tbs.font = (BitmapFont) AssetManager.getInstance().get("default.fnt");
        }

        textButton = new TextButton(json.getString("text", ""), tbs);

        if (json.has("height") && json.has("width"))
            textButton.setSize(json.getFloat("width"), json.getFloat("height"));

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
                        items.put("button",textButton);
                        last_updater.trigger(items);
                    }
                });
            }
        }
        return textButton;
    }

    public static class ClickableImageButton extends ImageButton {

        public ClickableImageButton(Drawable d) {
            super(d);
        }

        Texture clicked_texture = null;
        {
            makeTexture();
        }

        private void makeTexture() {
            final Pixmap p = new Pixmap((int)this.getWidth(),(int)this.getHeight(),Pixmap.Format.RGBA8888);
            p.setColor(Color.RED);
            p.drawRectangle(0, 0, (int)this.getWidth(),(int)this.getHeight());
            p.drawRectangle(1, 1, (int)this.getWidth()-2,(int)this.getHeight()-2);

            // run on UI thread
            Runnable runable = new Runnable() {
                @Override
                public void run() {
                    clicked_texture = new Texture(p);
                }
            };

            Gdx.app.postRunnable(runable);
        }

        @Override
        public void setSize(float width, float height) {
            super.setSize(width, height);
            makeTexture();
        }

        @Override
        public void draw(Batch batch, float arg1) {
            super.draw(batch, arg1);
            if (this.isChecked() && clicked_texture != null)
                batch.draw(clicked_texture, this.getX(), this.getY());
        }
    }

    private Actor makeImageButton (JsonValue json, Updater updater){
        String nameImage = json.getString("image");
        Texture textureImage = (Texture) AssetManager.getInstance().get(nameImage);
        Image image = new Image(textureImage);

        final ImageButton imageButton = new ClickableImageButton(image.getDrawable());


        if (json.has("height") && json.has("width"))
            imageButton.setSize(json.getFloat("width"), json.getFloat("height"));

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
                        items.put("button",imageButton);
                        last_updater.trigger(items);
                    }
                });
            }
        }
        return imageButton;
    }

    private Actor makeCheckBox (JsonValue json, Updater updater){
        String text = " " + json.getString("label");
        CheckBox.CheckBoxStyle cbs = skin.get("default",CheckBox.CheckBoxStyle.class);
        cbs.font = (BitmapFont)AssetManager.getInstance().get("default.fnt");
        cbs.fontColor = Color.DARK_GRAY;
        CheckBox checkBox = new CheckBox(text,cbs);

        if (updater != null & json.has("value")) {
            Object value_value = getEnumConstant(json, "value");

            if (updater.getDefaultValue() == value_value) {
                checkBox.setChecked(true);
            }

            if (value_value != null) {
                final Object last_value = value_value;
                final Updater last_updater = updater;
                checkBox.addListener(new ClickListener() {
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
        return checkBox;
    }

    private Actor makeScrollPane(JsonValue json, Updater updater) {

        Table table = new Table();

        String align = json.getString("align", "center");
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

        Object[] tabObject=null;
        final Updater last_updater = updater;
        final Object[] valuesEnum = getClass(json, "enum");
        tabObject = new Object[valuesEnum.length];
        int sizeMaxEnum=0;
        for(int i=0;i<valuesEnum.length;i++) {
            tabObject[i] = valuesEnum[i].toString();
            if (valuesEnum[i].toString().length()>sizeMaxEnum){
                sizeMaxEnum = valuesEnum[i].toString().length();
            }
        }

        com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle lls = skin.get("default", com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle.class);
        lls.font = (BitmapFont)AssetManager.getInstance().get("default.fnt");
        final com.badlogic.gdx.scenes.scene2d.ui.List list = new com.badlogic.gdx.scenes.scene2d.ui.List(lls);
        list.setItems(tabObject);
        list.getSelection().setMultiple(false);
        list.getSelection().setRequired(true);
        if (updater.getDefaultValue() != null) {
            list.setSelected(updater.getDefaultValue());
        }
        ScrollPane.ScrollPaneStyle sps = skin.get("perso",ScrollPane.ScrollPaneStyle.class);
        final ScrollPane scrollPane = new ScrollPane(list, sps);
        scrollPane.setFlickScroll(false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.layout();
        scrollPane.updateVisualScroll();

//        scrollPane.setupFadeScrollBars(1f,0.5f);

        list.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int index = list.getSelectedIndex();
                HashMap<String,Object> items = new HashMap<String, Object>();
                items.put("userObject",userObject);
                items.put("eventRequest",EventRequest.UPDATE_STATE);
                items.put("lastValue",valuesEnum[index]);
                items.put("layout", Layout.this);
                last_updater.trigger(items);
            }
        });

        table.add(scrollPane).size(sizeMaxEnum * 7 + 50, 93).pad(5);
        return table;
    }

    private Actor makeTextField(JsonValue json, Updater updater) {

        Table table = new Table();

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

        final Updater last_updater = updater;

        TextField.TextFieldStyle tfs = skin.get("default", TextField.TextFieldStyle.class);
        tfs.font = (BitmapFont)AssetManager.getInstance().get("default.fnt");

        final TextField textField = new TextField("",tfs);
        textField.setFocusTraversal(false);

        if (json.has("maxLength")){
            textField.setMaxLength(json.getInt("maxLength"));
        }

        textField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char key) {
                if ((key == '\r' || key == '\n')) {
                    String textSaisie = textField.getText();
                    double result = 0;
                    try {
                        result = Double.parseDouble(textSaisie);
                    }catch (NumberFormatException e){

                    }
                    HashMap<String, Object> items = new HashMap<String, Object>();
                    items.put("userObject", userObject);
                    items.put("eventRequest", EventRequest.UPDATE_STATE);
                    items.put("lastValue", result);
                    items.put("layout", Layout.this);
                    last_updater.trigger(items);
                }
            }
        });

        textField.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9')
                    return true;
                return false;
            }
        });

        textField.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
                if (!focused) {
                    String textSaisie = textField.getText();
                    double result = 0;
                    try {
                        result = Double.parseDouble(textSaisie);
                    }catch (NumberFormatException e){

                    }
                    HashMap<String, Object> items = new HashMap<String, Object>();
                    items.put("userObject", userObject);
                    items.put("eventRequest", EventRequest.UPDATE_STATE);
                    items.put("lastValue", result);
                    items.put("layout", Layout.this);
                    last_updater.trigger(items);
                }
            }
        });

        table.add(textField).pad(5);
        return table;
    }

}
