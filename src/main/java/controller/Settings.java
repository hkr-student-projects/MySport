package controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import model.App;
import model.People.Leader;
import model.Tools.SceneSwitcher;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class Settings extends Menu implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private JFXComboBox<String> comboBox;
    private Locale locale;
    public Button buttonAbout;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBox = new JFXComboBox<>();
        ObservableList<Label> langs = getLanguages();
        //anchorPane.getChildren().addAll(langs);
        //comboBox.setItems(langs);
        comboBox.setItems(FXCollections.observableArrayList(List.of(new String[] { "Sga", "ASDsas" })));

        bindTab(this);
    }

    @Override
    protected void onBurgerOpen() {

    }

    @Override
    protected void onBurgerClose() {

    }

    @Override
    protected void onBeforeSceneSwitch() {

    }

    @Override
    protected void onBeforeLogout() {

    }

    private ObservableList<Label> getLanguages(){
        ArrayList<Label> labels = new ArrayList<>();
        File[] files = new File("src/main/resources/view/img/flags").listFiles();//(dir, name) ->name.toLowerCase().startsWith("flag_")
        for(File f : files){
//            ImageView view = new ImageView(new Image("view/img/flags/"+f.getName()+""));
//            view.setPreserveRatio(true);
//            view.setFitHeight(32);
            Label label = new Label(f.getName().substring(0, f.getName().indexOf(".")));
            //label.setGraphic(view);
            labels.add(label);
        }

        return FXCollections.observableArrayList(labels);
    }

    public void buttonAboutClick() {
        App.instance.setScene(SceneSwitcher.instance.getScene("About"));
    }
}
