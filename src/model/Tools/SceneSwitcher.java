package model.Tools;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import model.App;
import model.Logging.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SceneSwitcher {

    private static Map<String, Map<EventType, ArrayList<EventHandler<? super KeyEvent>>>> sceneEventHandlers;// A trick for having multiple listeners (handlers) of: onKeyPressed, onKeyReleased, onMouseCLicked etc. events on every scene (each scene has several events (onMouseCLicked, onKeyPressed etc.), each event has many listeners (with a help of this trick (before it was only possible to have 1 listener per event))).
    public static Map<String, Scene> scenes;
    private static Map<String, Object> controllers;
    public static SceneSwitcher instance = new SceneSwitcher();

    static {
        try {
            File[] files = new File("src/view").listFiles((dir, name) ->
                    name.toLowerCase().endsWith(".fxml"));
            scenes = new HashMap<>(files.length);
            controllers = new HashMap<>(files.length);
            sceneEventHandlers = new HashMap<>(files.length);
            for(File f : files) {
                String name = f.getName().substring(0, f.getName().length() - 5);
                FXMLLoader loader = new FXMLLoader(f.toURI().toURL());
                loader.setResources(ResourceBundle.getBundle("resources/lang_"+ App.config.languageCode +""));
                Map<EventType, ArrayList<EventHandler<? super KeyEvent>>> events = new HashMap<>(3);
                events.put(EventType.ON_KEY_PRESSED, new ArrayList<>(5));// 5 is the apx. number of listeners of onKeyPressed event on "name" scene
                events.put(EventType.ON_KEY_RELEASED, new ArrayList<>(5));// 5 is the apx. number of listeners of onKeyReleased event on "name" scene
                //events.put(EventType.ON_MOUSE_CLICKED, new ArrayList<>(5));// 5 is the apx. number of listeners of onMouseClicked event on "name" scene
                sceneEventHandlers.put(name, events);
                scenes.put(name, new Scene(loader.load(), 900, 600));

                if((loader.getController()) != null)
                    controllers.put(name, loader.getController());
            }
        }
        catch (IOException e) {
            Logger.logException(e);
        }
        bindListeners();
    }

    public Scene getScene(String name){
        return scenes.get(name);
    }
    public <T> T getController(String name){ return (T)controllers.get(name); }
    public void addListener(String sceneName, EventType eventName, EventHandler<? super KeyEvent> handler){
        //get events and their listeners of particular scene
        //get current listeners of particular event
        //add new listener for particular scene
        sceneEventHandlers.get(sceneName).get(eventName).add(handler);
    }

    public void removeListener(String sceneName, EventType eventName, EventHandler<? super KeyEvent> handler){
        sceneEventHandlers.get(sceneName).get(eventName).remove(handler);
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

enum EventType {
    ON_KEY_PRESSED,
    ON_KEY_RELEASED,
    //ON_MOUSE_CLICKED
}