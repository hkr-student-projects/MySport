package model.Tools;

import com.mysql.cj.log.Log;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import model.App;
import model.Logging.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SceneSwitcher {

    private static Map<String, Map<EventType, ArrayList<EventHandler<? super KeyEvent>>>> sceneEventHandlers;// A trick for having multiple listeners (handlers) of: onKeyPressed, onKeyReleased, onMouseCLicked etc. events on every scene (each scene has several events (onMouseCLicked, onKeyPressed etc.), each event has many listeners (with a help of this trick (before it was only possible to have 1 listener per event))).
    public static Map<String, Scene> scenes;
    public static Map<String, Object> controllers;
    public static Map<String, FXMLLoader> loaders;
    public static SceneSwitcher instance = new SceneSwitcher();

    static {
        load(true, "se");
        bindListeners();
    }

    public Scene getScene(String name){
        return scenes.get(name);
    }

    public <T> T getController(String name){ return (T)controllers.get(name); }

    public FXMLLoader getLoader(String name){
        return loaders.get(name);
    }

    public static void addListener(String sceneName, EventType eventName, EventHandler<? super KeyEvent> handler){
        //get events and their listeners of particular scene
        //get current listeners of particular event
        //add new listener for particular scene
        sceneEventHandlers.get(sceneName).get(eventName).add(handler);
    }

    public void removeListener(String sceneName, EventType eventName, EventHandler<? super KeyEvent> handler){
        sceneEventHandlers.get(sceneName).get(eventName).remove(handler);
    }

    public static void changeLanguage(Language lang){
        load(true, lang.getCode());
    }

    private static void load(boolean eventLoad, String lang){
        File[] files = new File("src/main/resources/view").listFiles((dir, name) ->
                name.toLowerCase().endsWith(".fxml"));
        scenes = new HashMap<>(files.length);
        controllers = new HashMap<>(files.length);
        loaders = new HashMap<>(files.length);
        sceneEventHandlers = new HashMap<>(files.length);

        for (File f : files) {
            String name = f.getName().substring(0, f.getName().length() - 5);
            FXMLLoader loader = null;
            try {
                loader = new FXMLLoader(f.toURI().toURL());
            } catch (MalformedURLException e) {
                Logger.logException(e);
            }
            loader.setResources(ResourceBundle.getBundle("lang_" + lang + ""));
            Map<EventType, ArrayList<EventHandler<? super KeyEvent>>> events = new HashMap<>(3);
            if(eventLoad){
                events.put(EventType.ON_KEY_PRESSED, new ArrayList<>(5));// 5 is the apx. number of listeners of onKeyPressed event on "name" scene
                events.put(EventType.ON_KEY_RELEASED, new ArrayList<>(5));
            }
            loaders.put(name, loader);
            sceneEventHandlers.put(name, events);
            try {
                scenes.put(name, new Scene(loader.load(), 900, 600));
            } catch (IOException e) {
                Logger.logException(e);
            }

            if ((loader.getController()) != null)
                controllers.put(name, loader.getController());
        }
    }

    private static void bindListeners(){//find a way to iterate
       // scenes.forEach((n, s) -> System.out.println(n));
        scenes.get("Calendar").setOnKeyPressed(e -> {
            ArrayList<EventHandler<? super KeyEvent>> listeners = sceneEventHandlers.get("Calendar").get(EventType.ON_KEY_PRESSED);
            for(int i = 0; i < listeners.size(); i++)
                listeners.get(i).handle(e);
        });
        scenes.get("Calendar").setOnKeyReleased(e -> {
            ArrayList<EventHandler<? super KeyEvent>> listeners = sceneEventHandlers.get("Calendar").get(EventType.ON_KEY_RELEASED);
            for(int i = 0; i < listeners.size(); i++)
                listeners.get(i).handle(e);
        });
//        scenes.get("Calendar").setOnMouseClicked(e -> {
//            ArrayList<EventHandler<? super KeyEvent>> listeners = sceneEventHandlers.get("Calendar").get(EventType.ON_MOUSE_CLICKED);
//            for(int i = 0; i < listeners.size(); i++)
//                listeners.get(i).handle(e);
//        });
    }
}