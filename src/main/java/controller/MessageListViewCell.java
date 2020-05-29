package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import model.Client.viewModel.MessageRowData;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



public class MessageListViewCell extends ListCell<MessageRowData> {

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private Label label3;

    @FXML
    private FontAwesomeIconView fxIconGender;

    @FXML
    private GridPane gridPane;

    private FXMLLoader mLLoader;

    public static Map<String, Integer> usersMap = new HashMap<>();

    private static int allocationCounter = 0;

    @Override
    protected void updateItem(MessageRowData message, boolean empty) {
        super.updateItem(message, empty);

        if (empty || message == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                try {
                    File file = new File("src/main/resources/view/ListCell.fxml");
                    mLLoader = new FXMLLoader(file.toURI().toURL());
                    //mLLoader = new FXMLLoader(getClass().getResource("ListCell.fxml"));
                    mLLoader.setController(this);
                    System.out.println("Loaded Message FXML");

                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            label1.setText(message.getTimestamp());
            label2.setText(message.getName());
            label3.setText(message.getMessage());

            Integer fai = usersMap.get(message.getName());
            if (fai == null) {
                usersMap.put(message.getName(), allocationCounter++);
            }
            if(usersMap.get(message.getName()) % 2 == 0){
                fxIconGender.setIcon(FontAwesomeIcon.MAGIC);
            }
            else  {
                fxIconGender.setIcon(FontAwesomeIcon.BLACK_TIE);
            }
            setText(null);
            setGraphic(gridPane);
        }

    }
}
