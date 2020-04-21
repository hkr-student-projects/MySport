package model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import model.Logging.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import static java.lang.System.out;

public class SceneSwitcher {

    private static Map<String, Scene> loaders;
    public static SceneSwitcher sceneSwitcher = new SceneSwitcher();

    static {
        loaders = new HashMap<>(10);
        try {
            for(File f : new File("src/view").listFiles((dir, name) ->
                    name.toLowerCase().endsWith(".fxml") && !name.equals("Login.fxml"))){
                loaders.put(f.getName(), new Scene(new FXMLLoader(f.toURI().toURL()).load()));
            }
        } catch (IOException e) {
            Logger.logException(e);
        }
    }

//    private Map<String, Scene> getLoaders() throws IOException {
//
//        Field[] fields = this.getClass().getDeclaredFields();
//        ArrayList<Field> fs = Arrays.stream(fields).filter(f ->
//                f.getName().matches("field\\d+") && f.getType() == TextField.class
//        ).collect(Collectors.toCollection(ArrayList::new));
//        ArrayList<TextField> result = new ArrayList<>(fs.size());
//        fs.forEach(f -> {
//            try {
//                TextField fd = (TextField) f.get(this);
//                fd.setOnMouseClicked(e-> {
//                    fd.setText("");
//                    fd.getStyleClass().remove("fieldRed");
//                });
//                result.add(fd);
//            } catch (IllegalAccessException e) {
//                out.println(e.getMessage());
//                e.printStackTrace();
//            }
//        });
//
//        return loaders;
//    }

    public static Scene getScene(String name){
        return loaders.get(name);
    }
}
