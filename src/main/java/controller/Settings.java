package controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import model.App;
import model.People.User;
import model.Tools.Language;
import model.Tools.SceneSwitcher;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class Settings extends Menu implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private JFXComboBox languages;
    private Locale locale;
    @FXML
    private Button about, save;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        bindTab(this);
        colorPicker.setValue(Color.valueOf("#763dee"));
        languages.setItems(getLanguages());
        languages.setOnAction(e -> new Thread(() -> {
            User user = App.instance.getSession();
            setAppLanguage(((Label) languages.getValue()).getText());
            App.instance.setSession(user);
            ((Calendar)SceneSwitcher.instance.getController("Calendar")).loadUser();
            SceneSwitcher.controllers.forEach((n, c) -> {
                if(c instanceof Menu)
                    ((Menu)c).buildSessionName();
            });
            Login.setupMessaging(user);
        }).start());
        about.setOnMouseClicked(e -> App.instance.setScene(SceneSwitcher.instance.getScene("About")));
        save.setOnMouseClicked(e -> Menu.changeThemeColor(colorPicker.getValue()));
    }

    private void setAppLanguage(String currentLabel) {
        switch (currentLabel) {
            case "English":
                SceneSwitcher.changeLanguage(Language.EN);
                return;

            case "Swedish":
                SceneSwitcher.changeLanguage(Language.SE);
                return;

            case "Norwegian":
                SceneSwitcher.changeLanguage(Language.NO);

        }
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

//    public void changeBackground() {
//
////        anchorPane.setBackground(new Background(new BackgroundFill(Paint.valueOf(selectedColor.toString()), CornerRadii.EMPTY, Insets.EMPTY)));
//    }

    public void buttonAboutClick() {
        App.instance.setScene(SceneSwitcher.instance.getScene("About"));
    }
}
