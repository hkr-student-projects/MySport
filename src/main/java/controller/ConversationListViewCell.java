package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import model.Client.viewModel.ConversationRowData;

import java.io.File;
import java.io.IOException;



public class ConversationListViewCell extends ListCell<ConversationRowData> {

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private FontAwesomeIconView fxIconGender;

    @FXML
    private AnchorPane gridPane;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(ConversationRowData conversation, boolean empty) {
        super.updateItem(conversation, empty);

        if (empty || conversation == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                try {
                    File file = new File("src/main/resources/view/ListCell2.fxml");
                    mLLoader = new FXMLLoader(file.toURI().toURL());
                    //mLLoader = new FXMLLoader(getClass().getResource("ListCell2.fxml"));
                    mLLoader.setController(this);
                    System.out.println("Loaded Conversation FXML");

                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            label1.setText(conversation.getName());
            label2.setText(conversation.getMostRecentMessage());

            setText(null);
            setGraphic(gridPane);
        }

    }
}
