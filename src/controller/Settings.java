package controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class Settings extends Menu implements Initializable {
    @FXML
    private JFXComboBox<String> comboBox;
    private ResourceBundle resourceBundle;
    private Locale locale;
    ObservableList list = FXCollections.observableArrayList("English", "Swedish", "Norwegian");


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBox = new JFXComboBox<>();
        comboBox.setItems(list);

        bindTab(this);
    }

    @Override
    protected void onBurgerOpen() {

    }

    @Override
    protected void onBurgerClose() {

    }

    @Override
    protected void onSceneSwitch() {

    }

    @Override
    protected void onAppClose() {

    }
    private void loadLangBundle(String lang){
        locale = new Locale(lang);
        resourceBundle = ResourceBundle.getBundle("src/langBundle", locale);
    }
}
