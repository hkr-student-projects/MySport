package controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import model.App;
import model.Tools.Language;
import model.Tools.SceneSwitcher;
import model.Tools.ThreadResult;

import java.io.File;
import java.net.URL;
import java.util.*;

public class Settings extends Menu implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private JFXComboBox comboBox;
    private Locale locale;
    @FXML
    public Button buttonAbout, saveButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Label> list = getLanguages();
        comboBox.setItems(list);
        bindTab(this);

        comboBox.setOnAction(e -> {

            Label label = (Label) comboBox.getValue();

            ThreadResult<String, Boolean> checkLanguage = new ThreadResult<>(this::language, label.getText());
            Thread thread = new Thread(checkLanguage);
            thread.start();
        });
    }

    private Boolean language(String currentLabel) {
        switch (currentLabel) {
            case "English":
                SceneSwitcher.changeLanguage(Language.EN);
                return true;

            case "Swedish":
                SceneSwitcher.changeLanguage(Language.SE);
                return true;

            case "Norwegian":
                SceneSwitcher.changeLanguage(Language.NO);
                return true;

        }

        return false;
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

    private ObservableList<Label> getLanguages() {
        ArrayList<Label> labels = new ArrayList<>();
        File[] files = new File("src/main/resources/view/img/flags").listFiles();//(dir, name) ->name.toLowerCase().startsWith("flag_")
        for (File f : files) {
            ImageView view = new ImageView(new Image("view/img/flags/" + f.getName() + ""));
            view.setPreserveRatio(true);
            view.setFitHeight(32);
            view.setFitWidth(32);
            Label label = new Label(f.getName().substring(0, f.getName().indexOf(".")));
            label.setGraphic(view);
            labels.add(label);
        }

        return FXCollections.observableArrayList(labels);
    }
    @FXML
    public void changeBackgroundColor(ActionEvent event) {
        Color selectedColor = colorPicker.getValue();
        anchorPane.setBackground(new Background(new BackgroundFill(Paint.valueOf(selectedColor.toString()), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void buttonAboutClick() {
        App.instance.setScene(SceneSwitcher.instance.getScene("About"));
    }
}
