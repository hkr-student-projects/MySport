package model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import model.Logging.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneSwitcher {

    private static Map<String, Scene> loaders;
    public static SceneSwitcher instance = new SceneSwitcher();

    static  {
        try {
            File[] files = new File("src/view").listFiles((dir, name) ->
                    name.toLowerCase().endsWith(".fxml"));
            assert files != null;
            loaders = new HashMap<>(files.length);
            for(File f : files){
                loaders.put(f.getName(), new Scene(new FXMLLoader(f.toURI().toURL()).load(), 900, 600));
            }
        } catch (Exception e) {
            System.out.println("excep");
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

    public Scene getScene(String name){
        return loaders.get(name);
    }
}
