package controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import model.App;
import model.Tools.SceneSwitcher;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class Settings extends Menu implements Initializable {
    @FXML
    private JFXComboBox<String> comboBox;
    private ResourceBundle resourceBundle;
    private Locale locale;
    public Button buttonAbout;
    private ObservableList<String> langs = FXCollections.observableArrayList("English", "Swedish", "Norwegian");


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        comboBox.setItems(langs);

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
    private void loadLangBundle(String lang){
        locale = new Locale(lang);
        resourceBundle = ResourceBundle.getBundle("src/langBundle", locale);
    }

    public void buttonAboutClick() {
        App.instance.setScene(SceneSwitcher.instance.getScene("About"));
    }
}
