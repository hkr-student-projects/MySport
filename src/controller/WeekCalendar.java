package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.System.out;

public class WeekCalendar implements Initializable {

    @FXML
    private GridPane gridPane;
    private Node tempPane;
    private int tempRow;
    private int tempCol;
    private String tempStyle;
    private boolean flag = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillGrid();
        gridPane.getChildren().forEach(pane -> pane.setOnMouseClicked(e -> {
            int[] c = getNodeCords(pane);
            out.println("row:"+ c[0] +" " + "col:"+ c[1] +"");
            if(flag = !flag) {
                int[] cords = getNodeCords(tempPane);
//                out.println("row:"+ cords[0] +" " + "col:"+ cords[1] +"");
//                out.println("c[0]: " + cords[0] + " tempR: " + tempRow);
//                out.println("c[1]: " + cords[1] + " tempC: " + tempCol);
                if(cords[0] != tempRow && cords[1] == tempCol)
                    checkBelow(getNodeCords(tempPane)[0] + 1, tempRow, tempCol);
//                out.println("temprow: " + tempRow);
//                out.println("getNodeCords(tempPane)[0]: " + getNodeCords(tempPane)[0]);
                int extendBy = tempRow - getNodeCords(tempPane)[0];
                if(extendBy > 0)
                    GridPane.setRowSpan(tempPane, extendBy + 1);
                nodeTrackMouse(false);
                Pane p = (Pane)tempPane;
                p.setStyle("-fx-background-color: rgba(255,51,61,0.83)");
                Text t = new Text(10, 20, "09:35");
                t.setFill(Paint.valueOf("#FFFFFF"));
                t.setFont(new Font("Helvetica Light", 11.0));
                Text t1 = new Text(10, 40, "My new sport here");
                t1.setFill(Paint.valueOf("#FFFFFF"));
                t1.setFont(new Font("Helvetica Light", 11.0));
                p.getChildren().addAll(t, t1);
            }
            else {
                pane.setStyle("-fx-background-color: rgba(255,51,61,0.83);");
                tempPane = pane;
                nodeTrackMouse(true);
            }
        }));
    }

    private void checkBelow(int startRow, int endRow, int col) {
        //first version
        //out.println("preremoved");
        for(int i = startRow; i <= endRow; i++){
            gridPane.getChildren().remove(getNode(i, col));
            //out.println("removed");
        }
    }

    private void nodeTrackMouse(boolean flag){
        if(flag){
            gridPane.getChildren().forEach(n -> {
                n.setOnMouseEntered(e -> {
                    int[] cords = getNodeCords(n);
                    tempRow = cords[0];
                    tempCol = cords[1];
                    tempStyle = n.getStyle();
                    n.setStyle("-fx-background-color: rgba(255,51,61,0.83);");
                    //uniteCells(cords[0], cords[1]);
                });
//                n.setOnMouseExited(e -> {
//                    n.setStyle(tempStyle);
//                });
            });
        }
        else gridPane.getChildren().forEach(p -> p.setOnMouseEntered(null));
    }

    private void uniteCells(int toRow, int toCol){
        int[] cords = getNodeCords(tempPane);
        GridPane.setColumnSpan(tempPane, toCol);
    }

    private int[] getNodeCords(Node n){
        Object var0 = n.getProperties().get("gridpane-row");
        Object var1 = n.getProperties().get("gridpane-column");
        int row = var0 == null ? 0 : (int) var0;
        int col = var1 == null ? 0 : (int) var1;

        return new int[] { row, col };
    }

    private Node getNode (int row, int column) {
        Node result = null;

        for (Node node : gridPane.getChildren()) {
            if(node == null)
                continue;
            if(GridPane.getColumnIndex(node) == null)
                GridPane.setColumnIndex(node, 0);
            if(GridPane.getRowIndex(node) == null)
                GridPane.setRowIndex(node, 0);
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

    private void fillGrid(){
        for(int i = 0; i < gridPane.getColumnConstraints().size(); i++){
            for(int j = 0; j < gridPane.getRowConstraints().size(); j++){
                gridPane.add(new Pane(), i, j);
            }
        }
    }
}
