package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.MyArrayList;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {

    private static MyArrayList<CalendarController> weeks;
    @FXML
    private Text month, year;
    @FXML
    private GridPane gridPane;
    @FXML
    private Button burger;
    @FXML
    private Button homeButton;
    @FXML
    private HBox bar;
    private Node[][] gridPaneFast;
    private Node tempPane, prevPane;
    private int tempRow, tempCol;
    private boolean flag = true;

    static {
        weeks = new MyArrayList<>();
    }

    @FXML
    public void changeToHome(ActionEvent event) throws IOException {
        Parent newParent = FXMLLoader.load(getClass().getResource("/View/Home.fxml"));
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene newScene = new Scene(newParent);
        window.setScene(newScene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gridPaneFast = new Node[gridPane.getRowConstraints().size()][gridPane.getColumnConstraints().size()];
        fillGrid();
        burger.setOnMouseClicked(e -> {
            bar.setVisible(!bar.isVisible());
            bar.setDisable(!bar.isDisabled());
        });
        gridPane.getChildren().forEach(pane -> pane.setOnMouseClicked(e -> {
            saveTempCords(pane);
            //int[] clickCords = getNodeCords(pane);
            //out.println("row:"+ clickCords[0] +" " + "col:"+ clickCords[1] +"");
            if (flag = !flag) {
                int[] tempPaneCords = getNodeCords(tempPane);//[0:row, 1:col]
//                out.println("row:"+ tempPaneCords[0] +" " + "col:"+ tempPaneCords[1] +"");
//                out.println("clickCords[0]: " + tempPaneCords[0] + " tempR: " + tempRow);
//                out.println("clickCords[1]: " + tempPaneCords[1] + " tempC: " + tempCol);
//                if(tempPaneCords[0] != tempRow && tempPaneCords[1] == tempCol){
//                    deleteBelow(tempPaneCords[0] + 1, tempRow, tempCol);
//                    //setBelow(tempPaneCords, tempPaneCords[0] + 1, tempRow, tempCol);
//                }

//                out.println("temprow: " + tempRow);
//                out.println("getNodeCords(tempPane)[0]: " + getNodeCords(tempPane)[0]);
                //int extendBy = Math.max(tempRow, tempPaneCords[0]) - Math.min(tempRow, tempPaneCords[0]);
                int extendBy = tempRow - tempPaneCords[0];
//                out.println("tempRow: " + tempRow);
//                out.println("tempPaneCords[0]: " + tempPaneCords[0]);
//                out.println("extendBy: " + extendBy);
                if (extendBy != 0) {
//                    GridPane.setRowSpan(tempPane, extendBy + 1);
//                    GridPane.setColumnSpan(tempPane, 1);
                    if (extendBy < 0) {
                        int row = tempRow;
                        int col = tempCol;
                        tempRow = tempPaneCords[0];
                        tempCol = tempPaneCords[1];
                        tempPaneCords[0] = row;
                        tempPaneCords[1] = col;
                        tempPane = pane;
                        pane.setStyle("-fx-background-color: rgba(255,148,33,0.83);");
                    }
                    deleteBetween(tempPaneCords[0] + 1, tempRow, tempCol);
                    GridPane.setRowSpan(tempPane, Math.abs(extendBy) + 1);
                }

                nodeTrackMouse(false);
                Pane p = (Pane) tempPane;
                tempPane.getProperties().put("baked", true);
                Text t = new Text(10, 20, "09:35");
                t.setFill(Paint.valueOf("#FFFFFF"));
                t.setFont(new Font("Helvetica Light", 11.0));
                Text t1 = new Text(10, 40, "My new sport here");
                t1.setFill(Paint.valueOf("#FFFFFF"));
                t1.setFont(new Font("Helvetica Light", 11.0));
                p.getChildren().addAll(t, t1);
            } else {
                pane.setStyle("-fx-background-color: cyan;");
                tempPane = pane;
                nodeTrackMouse(true);
            }
        }));
    }

    private void saveTempCords(Node pane) {
        int[] cords = getNodeCords(pane);
        tempRow = cords[0];
        tempCol = cords[1];
    }

    private void deleteBetween(int startRow, int endRow, int col) {
        for (int i = startRow; i <= endRow; i++) {
            gridPane.getChildren().remove(getNode(i, col));
        }
    }

//    private void setBelow(int[] tempPaneCords, int startRow, int endRow, int col){
//        for(int i = startRow; i <= endRow; i ++){
//            //GridPane.setConstraints(getNode(i, col), tempPaneCords[1], tempPaneCords[0]);
//            gridPane.getChildren().remove(getNode(i, col));
//            gridPane.add(tempPane, col, i);
//        }
//    }

    private void nodeTrackMouse(boolean flag) {
        if (flag) {
            gridPane.getChildren().forEach(p -> {
                p.setOnMouseEntered(e -> {
                    if (p.getProperties().containsKey("baked"))
                        return;
                    if (prevPane != null && !prevPane.getProperties().containsKey("baked") && isAfter(getNodeCords(p), getNodeCords(prevPane)))
                        prevPane.setStyle("");
                    int[] cords = getNodeCords(p);
                    p.setStyle("-fx-background-color: yellow;");
                    tempRow = cords[0];
                    tempCol = cords[1];
                    checkColorBetween(getNodeCords(tempPane), cords);
                    prevPane = p;
                });
            });
        } else gridPane.getChildren().forEach(p -> {
            p.setOnMouseEntered(null);
            p.setOnMouseExited(null);
        });
    }

    private int[] getNodeCords(Node n) {
        Object var0 = n.getProperties().get("gridpane-row");
        Object var1 = n.getProperties().get("gridpane-column");
        int row = var0 == null ? 0 : (int) var0;
        int col = var1 == null ? 0 : (int) var1;

        return new int[]{row, col};
    }

    private Node getNode(int row, int column) {
//        Node result = null;
//
//        for (Node node : gridPane.getChildren()) {
//            if(node == null)
//                continue;
//            if(GridPane.getColumnIndex(node) == null)
//                GridPane.setColumnIndex(node, 0);
//            if(GridPane.getRowIndex(node) == null)
//                GridPane.setRowIndex(node, 0);
//            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
//                result = node;
//                break;
//            }
//        }
        return gridPaneFast[row][column];
    }

    private void checkColorBetween(int[] start, int[] end) {
        if (end[0] < start[0]) {
            int t = start[0];
            start[0] = end[0];
            end[0] = t;
        }
        for (int i = start[0]; i <= end[0]; i++) {
            Node node = getNode(i, end[1]);
            //out.println("i: " + i + " row: " + tempRow);
            if (node.getStyle().isBlank())
                node.setStyle("-fx-background-color: purple;");
        }
    }

    private boolean isAfter(int[] crnt, int[] prev) {
        int[] tempPaneCords = getNodeCords(tempPane);

        //return (base[0] > tempPaneCords[0] ? check[0] > base[0] : check[0] < base[0]) & check[1] == base[1];
        return ((Math.abs(crnt[0] - tempPaneCords[0])) < (Math.abs(prev[0] - tempPaneCords[0]))) & crnt[1] == prev[1];
    }

    private void fillGrid() {
        for (int col = 0; col < gridPane.getColumnConstraints().size(); col++) {
            for (int row = 0; row < gridPane.getRowConstraints().size(); row++) {
                Pane p = new Pane();
                p.getStylesheets().add("/view/css/general.css");
                p.getStyleClass().add("pane");
                gridPane.add(p, col, row);
                gridPaneFast[row][col] = p;
            }
        }
    }
}
