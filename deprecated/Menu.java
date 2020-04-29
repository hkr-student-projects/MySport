package model;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Menu {

    private Text username;
    private HBox container;
    private Button home;
    private Button messages;
    private Button forum;
    private Button logout;
    private Button calendar;
    private Button settings;
    private Button account;
    public static Menu instance = new Menu();

    private Menu() {

        Text username = new Text();
        username.setStrokeWidth(0);
        username.setStrokeType(StrokeType.OUTSIDE);
        username.setLayoutX(14.0);
        username.setLayoutY(24.0);
        //username.setText(user);
        username.setFill(Paint.valueOf("#FFFFFF"));
        username.setWrappingWidth(341.0);
        username.setFont(new Font("AppleGothic Regular", 14));

        home = createButton("view/img/home-icon_2.png");
        messages = createButton("view/img/mail-icon_14.png");
        forum = createButton("view/img/forum-icon_1.png");
        calendar = createButton("view/img/calendar-icon_4.png");
        logout = createButton("view/img/power-off-icon_1.png");
        settings = createButton("view/img/settings-icon_10.png");
        account = createButton("view/img/user-icon_1.png");

        container = new HBox();
        container.setPrefHeight(31);
        container.setSpacing(25.0);
        container.setPrefWidth(400);
        container.setLayoutX(400.0);
        container.setLayoutY(3.0);
        container.setAlignment(Pos.CENTER_RIGHT);
        container.setStyle("-fx-border-color: transparent");
        container.getChildren().addAll(home, account, messages, forum, calendar, settings, logout);
    }

    private Button createButton(String img){
        Button b = new Button();
        b.setPrefHeight(9.0);
        b.setPrefWidth(32.0);
        b.setMnemonicParsing(false);
        ImageView imageview = new ImageView(new Image(img));
        imageview.setPickOnBounds(true);
        imageview.setFitWidth(22.0);
        imageview.setFitHeight(22.0);
        imageview.setPreserveRatio(true);
        b.setGraphic(imageview);
        b.getStylesheets().add("view/css/general.css");
        b.getStyleClass().add("buttonTab");

        return b;
    }

    public HBox getTab(){
        return container;
    }

    public Text getUsername() {
        return username;
    }

    public void setUsername(Text username) {
        this.username = username;
    }
}
