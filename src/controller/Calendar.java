package controller;

import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.ArrayList;
import java.net.URL;
import java.util.ResourceBundle;

public class Calendar extends Menu implements Initializable {

    private static ArrayList<Calendar> weeks;
    @FXML
    private Text month, year;
    @FXML
    private GridPane gridPane;
    @FXML
    private HBox bar;
    private Node[][] gridPaneFast;
    private Node tempPane, prevPane;
    private int tempRow, tempCol, tempY, initY;
    private boolean flag = true;
    private final String color = "-fx-background-color: rgba(255,51,61,0.83)";

    static {
        weeks = new ArrayList<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindTab(this);
        gridPaneFast = new Node[gridPane.getRowConstraints().size()][gridPane.getColumnConstraints().size()];
        burger.setOnMouseClicked(e -> {
            bar.setVisible(!bar.isVisible());
            bar.setDisable(!bar.isDisabled());
        });
        fillGrid();
        gridPane.getChildren().forEach(pane -> pane.setOnMouseClicked(e -> buildActivity(pane)));
    }

    private void move(Pane pane, int sceneY){
        int dif = initY - sceneY;
        if(Math.abs(dif) > 15){
            int[] cords = getNodeCords(pane);
            if(cords[0] == 0)
                return;
            if(dif > 0){
                int span = (int)pane.getProperties().get("span");
                GridPane.setRowSpan(pane, span - 1);
                addBetween(cords[0] + span - 1, span + cords[0], cords[1]);
                gridPane.getChildren().remove(pane);
                gridPane.getChildren().remove(64 * cords[1] + cords[0] - 1);
                gridPane.add(pane, cords[1], cords[0] - 1);
                gridPaneFast[cords[0]][cords[1]] = createPane(true);
                GridPane.setRowSpan(pane, span);
//                Pane above = (Pane)gridPaneFast[cords1[0] - 1][cords1[1]];
//                int[] aboveCords = getNodeCords(above);
//                above.setStyle(color);
//                ObservableMap<Object, Object> props = above.getProperties();
//                props.put("baked", true);
//                int span1 = (int)pane.getProperties().get("span");
//                props.put("span", span1);
//                //cal new time
//                pane.setStyle("");
//                pane.getChildren().removeAll();
//                deleteBetween(aboveCords[0] + 1, aboveCords[0] + span1, tempCol);
//                GridPane.setRowSpan(pane, 1);
//                GridPane.setRowSpan(above, span1);
//                above.setOnMouseDragged(e -> move(above, (int) e.getSceneY()));
//                    int span1 = (int)pane.getProperties().get("span");
//                    GridPane.setRowSpan(pane, 1);
//                    gridPaneFast[cords1[0] - 1][cords1[1]] = pane;
//                    //deleteBetween(cords1[0], cords1[0] + span1, cords1[1]);
//                    GridPane.setRowSpan(pane, span1);
//                    gridPaneFast[cords1[0]][cords1[1]] = new Pane();
            }
            else {

            }
        }
    }

    private boolean bakedAbove(int[] cords){
        Pane above = (Pane)gridPaneFast[cords[0] - 1][cords[1]];
        System.out.println(getNodeCords(above)[0]);
        return above.getProperties().containsKey("baked");
    }

    private void buildActivity(Node pane){

        if(tempPane != null && getNodeCords(pane)[1] != getNodeCords(tempPane)[1])
            return;
        if(flag = !flag) {
            int[] tempPaneCords = getNodeCords(tempPane);//[0:row, 1:col]
            int extendBy = tempRow - tempPaneCords[0];
            if(extendBy != 0){
                if(extendBy < 0){
                    int row = tempRow;
                    int col = tempCol;
                    tempRow = tempPaneCords[0];
                    tempCol = tempPaneCords[1];
                    tempPaneCords[0] = row;
                    tempPaneCords[1] = col;
                    tempPane = pane;
                }
                deleteBetween(tempPaneCords[0] + 1, tempRow, tempCol);
                GridPane.setRowSpan(tempPane, Math.abs(extendBy) + 1);
            }
            bakePane(Math.abs(extendBy) + 1);
        }
        else {
            pane.setStyle(color);
            tempPane = pane;
            nodeTrackMouse(true);
        }
    }

    private void bakePane(int span){
        nodeTrackMouse(false);
        setText((Pane)tempPane, span);
        prevPane = null;
        tempPane = null;
    }

    private void setText(Pane pane, int span){
        int[] cords = getNodeCords(pane);
        byte hour = (byte) (7 + (cords[0] >> 2));
        byte min = (byte) ((cords[0] % 4) * 15);
        String time = String.format("%s%s:%s%s", hour < 10 ? "0" : "", hour, min, min == 0 ? "0" : "");
        Text text = new Text(10, 20, time);
        text.setFill(Paint.valueOf("#FFFFFF"));
        text.setFont(new Font("Helvetica Light", 11.0));
        Text activity = new Text(10, 40, "Leader.getSport()");
        activity.setFill(Paint.valueOf("#FFFFFF"));
        activity.setFont(new Font("Helvetica Light", 11.0));
        pane.getChildren().addAll(text, activity);
        ObservableMap<Object, Object> props = pane.getProperties();
        props.put("baked", true);
        props.put("span", span);
        //props.put("time", time);
        pane.setOnMousePressed(e -> initY = (short)e.getSceneY());
        //gridPaneFast[cords[0]][cords[1]] = pane;
        pane.setOnMouseDragged(e -> {
            move(pane, (int) e.getSceneY());
//            short dif = (short) (initY - e.getSceneY());
//            if(Math.abs(dif) > 15){
//                int[] cords1 = getNodeCords(pane);
//                if(dif > 0){
//                    Pane above = (Pane)gridPaneFast[cords1[0] - 1][cords1[1]];
//                    int[] aboveCords = getNodeCords(above);
//                    int span1 = (int)pane.getProperties().get("span");
//                    above.setStyle(color);
//                    //cal new time
//                    pane.setStyle("");
//                    pane.getChildren().removeAll();
//                    deleteBetween(aboveCords[0] + 1, aboveCords[0] + span1, tempCol);
//                    GridPane.setRowSpan(pane, 1);
//                    GridPane.setRowSpan(above, span1);
////                    int span1 = (int)pane.getProperties().get("span");
////                    GridPane.setRowSpan(pane, 1);
////                    gridPaneFast[cords1[0] - 1][cords1[1]] = pane;
////                    //deleteBetween(cords1[0], cords1[0] + span1, cords1[1]);
////                    GridPane.setRowSpan(pane, span1);
////                    gridPaneFast[cords1[0]][cords1[1]] = new Pane();
//                }
//                else {
//
//                }
//            }
        });
    }

    private void addBetween(int start, int end, int col){
        if(end < start){
            int temp = start;
            start = end;
            end = temp;
        }
        for(int i = start; i < end + 1; i++){
            Pane p = createPane(true);
            gridPaneFast[i][col] = p;
            gridPane.add(p, col, i);
        }
    }

    private void deleteBetween(int startRow, int endRow, int col) {
        for(int i = startRow; i <= endRow; i++){
            gridPane.getChildren().remove(getNode(i, col));
        }
    }

    private void nodeTrackMouse(boolean flag){
        if(flag){
            gridPane.getChildren().forEach(p -> {
                p.setOnMouseEntered(e -> {
                    int[] curCords = getNodeCords(p);
                    int[] tempCords = getNodeCords(tempPane);
                    if(curCords[1] != tempCords[1])
                        return;
                    if(prevPane != null && !prevPane.getProperties().containsKey("baked") &&
                            isAfter(getNodeCords(p), getNodeCords(prevPane))) {
                        int[] prevCords = getNodeCords(prevPane);
                        resetColorBetween(curCords, prevCords);
                    }
                    p.setStyle(color);
                    tempRow = curCords[0];
                    tempCol = curCords[1];
                    checkColorBetween(tempCords, curCords);
                    prevPane = p;
                });
            });
        }
        else gridPane.getChildren().forEach(p -> {
            p.setOnMouseEntered(null);
            p.setOnMouseExited(null);
        });
    }

    private int[] getNodeCords(Node n){
        Object var0 = n.getProperties().get("gridpane-row");
        Object var1 = n.getProperties().get("gridpane-column");
        int row = var0 == null ? 0 : (int) var0;
        int col = var1 == null ? 0 : (int) var1;

        return new int[] { row, col };
    }

    private Node getNode(int row, int column) {
        return gridPaneFast[row][column];
    }

    private void checkColorBetween(int[] start, int[] end) {
        if(end[0] < start[0]){
            int[] temp = start;
            start = end;
            end = temp;
        }
        for(int i = start[0]; i < end[0] + 1; i++){
            Node node = getNode(i, end[1]);
            if(node.getStyle().isBlank())
                node.setStyle(color);
        }
    }

    private void resetColorBetween(int[] start, int[] end){
        if(end[0] < start[0]){
            int[] temp = start;
            start = end;
            end = temp;
        }
        for(int i = start[0]; i < end[0] + 1; i++){
            Node node = getNode(i, end[1]);
            node.setStyle("");
        }
    }

    private boolean isAfter(int[] curr, int[] prev){
        int[] tempPaneCords = getNodeCords(tempPane);
        //return (base[0] > tempPaneCords[0] ? check[0] > base[0] : check[0] < base[0]) & check[1] == base[1];
        return ((Math.abs(curr[0] - tempPaneCords[0])) < (Math.abs(prev[0] - tempPaneCords[0]))) ||
                ((prev[0] < tempPaneCords[0]) & (curr[0] > tempPaneCords[0])) ||
                ((curr[0] < tempPaneCords[0]) & (prev[0] > tempPaneCords[0]));
    }

    private void fillGrid(){
        for(int col = 0; col < gridPane.getColumnConstraints().size(); col++){
            for(int row = 0; row < gridPane.getRowConstraints().size(); row++){
                Pane p = createPane(false);
                gridPane.add(p, col, row);
                gridPaneFast[row][col] = p;
            }
        }
    }

    private Pane createPane(boolean trackable){
        Pane p = new Pane();
        p.getStylesheets().add("/view/css/general.css");
        p.getStyleClass().add("pane");
        if(trackable)
            p.setOnMouseClicked(e -> buildActivity(p));

        return p;
    }
}
// row: 15px, 4 rows per hour, textfield: 22px, spacing: 4(15px) - 2(22px / 2)