package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Main;
import model.ArrayList;
import model.SceneSwitcher;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.System.out;

public class Calendar implements Initializable {

    private static ArrayList<Calendar> weeks;
    @FXML
    private Text month, year;
    @FXML
    private GridPane gridPane;
    @FXML
    private Button burger, home, account, mail, forum, calendar, settings, logout;
    @FXML
    private HBox bar;
    private Node[][] gridPaneFast;
    private Node tempPane, prevPane;
    private int tempRow, tempCol;
    private boolean flag = true;

    static {
        weeks = new ArrayList<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindTab();
        gridPaneFast = new Node[gridPane.getRowConstraints().size()][gridPane.getColumnConstraints().size()];
        burger.setOnMouseClicked(e -> {
            bar.setVisible(!bar.isVisible());
            bar.setDisable(!bar.isDisabled());
        });
        fillGrid();
        gridPane.getChildren().forEach(pane -> pane.setOnMouseClicked(e -> {
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
                bakePane();
            }
            else {
                pane.setStyle("-fx-background-color: rgba(255,51,61,0.83);");
                tempPane = pane;
                nodeTrackMouse(true);
            }
        }));
    }

    private void bakePane(){
        nodeTrackMouse(false);
        setText((Pane)tempPane);
        prevPane = null;
        tempPane = null;
    }

    private void setText(Pane pane){
        int[] cords = getNodeCords(pane);
        byte hour = (byte) (7 + (cords[0] >> 2));
        byte min = (byte) ((cords[0] % 4) * 15);
        Text time = new Text(10, 20, String.format("%s%s:%s%s", hour < 10 ? "0" : "", hour, min, min == 0 ? "0" : ""));
        time.setFill(Paint.valueOf("#FFFFFF"));
        time.setFont(new Font("Helvetica Light", 11.0));
        Text activity = new Text(10, 40, "Leader.getSport()");
        activity.setFill(Paint.valueOf("#FFFFFF"));
        activity.setFont(new Font("Helvetica Light", 11.0));
        pane.getChildren().addAll(time, activity);
        pane.getProperties().put("baked", true);
        pane.setOnMouseEntered(null);
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
                    if(prevPane != null && !prevPane.getProperties().containsKey("baked") && isAfter(getNodeCords(p), getNodeCords(prevPane))){
                        //out.println("clearing");
                        int[] prevCords = getNodeCords(prevPane);
                        resetColorBetween(curCords, prevCords);
                    }
                    p.setStyle("-fx-background-color: rgba(255,51,61,0.83);");
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
        int row = var0 == null ? 0 : (int)var0;
        int col = var1 == null ? 0 : (int)var1;

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
                node.setStyle("-fx-background-color: rgba(255,51,61,0.83);");
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
                Pane p = new Pane();
                p.getStylesheets().add("/view/css/general.css");
                p.getStyleClass().add("pane");
                gridPane.add(p, col, row);
                gridPaneFast[row][col] = p;
            }
        }
    }

    private void bindTab(){
        home.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Home.fxml")));
        account.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Account.fxml")));
        mail.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Mail.fxml")));
        forum.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Forum.fxml")));
        calendar.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Calendar.fxml")));
        settings.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Settings.fxml")));
        logout.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Login.fxml")));
    }
}
// row: 15px, 4 rows per hour, textfield: 22px, spacing: 4(15px) - 2(22px / 2)