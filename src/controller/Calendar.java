package controller;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Tools.ArrayList;
import model.Tools.Block;
import model.Tools.Serializable;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Calendar extends Menu implements Initializable, Serializable<Calendar.GridsPair> {

    private static ArrayList<GridsPair> weeks;
    @FXML
    private Pane tableHolder;
    private int currentTable;
    @FXML
    private Text month;
    @FXML
    private GridPane gridPane;
    @FXML
    private HBox options, dates;
    @FXML
    private Button prev, next, bug;
    private Node[][] gridPaneFast;
    private Node tempPane, prevPane;
    private int tempRow, tempCol, initY;
    private boolean flag = true, ctrl;
    private final String color = "-fx-background-color: rgba(255,51,61,0.83)";
    private LocalDate currWeek;

    static {
        weeks = new ArrayList<>();
    }

    @Override
    protected void burgerOpenAction() {
        super.toggleTab();
        //polymorphic starts
        options.setVisible(true);
        options.setDisable(false);
    }

    @Override
    protected void burgerCloseAction() {
        super.toggleTab();
        //polymorphic starts
        options.setVisible(false);
        options.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currWeek = LocalDate.now();
        gridPaneFast = new Node[gridPane.getRowConstraints().size()][gridPane.getColumnConstraints().size()];
        new Thread(() -> {
            bindTab(this);
            fillWeek(0);
        }).start();
        //make synchronized
//        prev.setOnMouseClicked(e -> new Thread(() -> fillWeek(-7)).start());
//        next.setOnMouseClicked(e -> new Thread(() -> fillWeek(7)).start());
        prev.setOnMouseClicked(e -> {
            fillWeek(-7);
            loadTable(--currentTable);
        });
        next.setOnMouseClicked(e -> {
            fillWeek(7);
            loadTable(++currentTable);
        });
        bug.setOnMouseClicked(e -> {
            GridsPair pair = deserialize(serialize(new GridsPair(this.gridPane, this.gridPaneFast)));
            tableHolder.getChildren().remove(gridPane);
            gridPane = pair.gridPane;
            tableHolder.getChildren().add(pair.gridPane);
            gridPaneFast = pair.gridPaneFast;

        });
        fillGrids(this.gridPane, this.gridPaneFast);
        gridPane.getChildren().forEach(pane -> pane.setOnMouseClicked(e -> buildActivity(pane)));
    }



    private void buildActivity(Node pane){
        if((tempPane != null && getNodeCords(pane)[1] != getNodeCords(tempPane)[1]))
            return;
        if(flag = !flag) {
            int[] tempPaneCords = getNodeCords(tempPane);//tempPane cannot be null because at first "!flag" will return false and else block will assign reference to tempPane
            int extendBy = tempRow - tempPaneCords[0];
            if (tempRow == tempPaneCords[0])
            {
                nodeTrackMouse(false);
                tempPane.setStyle("");
                tempPane = null;
                return;
            }
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
                deleteBetween(tempPane, tempPaneCords[0] + 1, tempRow, tempCol);
                GridPane.setRowSpan(tempPane, Math.abs(extendBy) + 1);
            }
            bakePane(Math.abs(extendBy) + 1);
        }
        else {
            pane.setStyle(color);
            int[] cords = getNodeCords(pane);
            tempRow = cords[0];
            tempCol = cords[1];
            tempPane = pane;
            nodeTrackMouse(true);
        }
    }

    private void bakePane(int span){
        nodeTrackMouse(false);
        buildProps((Pane)tempPane, span);
        prevPane = null;
        tempPane = null;
    }

    private void buildProps(Pane pane, int span){
        buildTime(pane, span);
        String sp = "Leader.getSport()";
        pane.getProperties().put("sp", sp);
        Text activity = new Text(10, 40, sp);
        activity.setFill(Paint.valueOf("#FFFFFF"));
        activity.setFont(new Font("Helvetica Light", 11.0));
        pane.getChildren().add(activity);
        ObservableMap<Object, Object> props = pane.getProperties();
        props.put("span", span);
        pane.setOnMousePressed(e -> initY = (short)e.getSceneY());
        pane.setOnMouseDragged(e -> move(pane, (int) e.getSceneY()));
        pane.setOnMouseClicked(null);
    }

    private void buildTime(Pane pane, int span){
        int[] cords = getNodeCords(pane);
        byte hourFrom = (byte) (7 + (cords[0] >> 2));
        byte minFrom = (byte) ((cords[0] % 4) * 15);
        int dur = span * 15;
        int finalHour = (dur + minFrom) / 60;
        byte minTo = (byte) ((dur + minFrom) - finalHour * 60);
        int hourTo = hourFrom + finalHour;

        String h0 = (hourFrom < 10 ? "0" : "") + hourFrom;
        String m0 = minFrom + (minFrom == 0 ? "0" : "");
        String h1 = (hourTo < 10 ? "0" : "") + hourTo;
        String m1 = minTo + (minTo == 0 ? "0" : "");

        ObservableMap<Object, Object> props = pane.getProperties();

        String time = String.format("%s:%s - %s:%s", h0, m0, h1, m1);
        Text text = new Text(10, 20, time);
        text.setFill(Paint.valueOf("#FFFFFF"));
        text.setFont(new Font("Helvetica Light", 11.0));
        props.put("hf", h0);
        props.put("mf", m0);
        props.put("ht", h1);
        props.put("mt", m1);
        if(pane.getChildren().size() != 0)
            ((Text)(pane.getChildren().get(0))).setText(time);
        else
            pane.getChildren().add(text);
    }

    private void move(Pane pane, int sceneY){//in Maintenance, that's why strange look
        int dif = initY - sceneY;
        if(Math.abs(dif) > 15){
            int[] cords = getNodeCords(pane);
            int span = (int)pane.getProperties().get("span");
            int var0;
            if(dif > 0){
                if(ctrl){

                }
                else {
                    if(cords[0] == 0 || checkFreeSpace(cords, -1))
                        return;
                    var0 = -1;
                    addBetween(cords[0] + span - 1, cords[0] + span - 1, cords[1]);//strict order
                    gridPane.getChildren().remove(pane);//strict order
                    gridPane.add(pane, cords[1], cords[0] - 1);//strict order
                    gridPaneFast[cords[0] + var0][cords[1]] = pane;
                    buildTime(pane, span);
                    GridPane.setRowSpan(pane, span);
                }
            }
            else {
                if(ctrl){

                }
                else {
                    if(cords[0] + span == gridPane.getRowConstraints().size() || checkFreeSpace(cords, span))//grindPane.rows.count
                        return;
                    var0 = span;
                    addBetween(cords[0], cords[0], cords[1]);//strict order
                    gridPane.getChildren().remove(pane);//strict order
                    gridPane.add(pane, cords[1], cords[0] + 1);//strict order
                    gridPaneFast[cords[0] + var0][cords[1]] = pane;
                    buildTime(pane, span);
                    GridPane.setRowSpan(pane, span);
                }
            }
        }
    }

    private void loadTable(int offset){
        if(currentTable + offset >= weeks.size()){
            currentTable += offset;
            weeks.add(createGrids());
        }
        else if(currentTable + offset < 0){
            currentTable = 0;
            weeks.insertAt(createGrids(), 0);
        }
        this.gridPane = weeks.get(currentTable).gridPane;
        this.gridPaneFast = weeks.get(currentTable).gridPaneFast;
        tempCol = 0;
        tempRow = 0;
        tempPane = null;
    }

    private GridsPair createGrids(){
        GridPane grid = new GridPane();
        grid.setPrefHeight(975);
        grid.setPrefWidth(777.0);
        grid.setMaxWidth(777.0);
        grid.setMinWidth(777.0);
        grid.setLayoutX(75.0);
        grid.setLayoutY(23.0);
        grid.setAlignment(Pos.CENTER);
        ColumnConstraints[] cc = new ColumnConstraints[7];//7 days in week
        for(ColumnConstraints c : cc){
            c = new ColumnConstraints();
            c.setHgrow(Priority.NEVER);
            c.setPrefWidth(111.0);
            c.setMinWidth(111.0);
            c.setHalignment(HPos.CENTER);
            c.setPercentWidth(0.0);
            c.setMaxWidth(111.0);
        }
        RowConstraints[] rc = new RowConstraints[65];//or get how 15 slots needed
        for(RowConstraints r : rc){
            r = new RowConstraints();
            r.setMinHeight(15.0);
            r.setPrefHeight(15.0);
            r.setPercentHeight(0.0);
            r.setValignment(VPos.CENTER);
            r.setMaxHeight(15.0);
            r.setVgrow(Priority.NEVER);
        }
        Node[][] gridFast = new Pane[rc.length][cc.length];
        fillGrids(grid, gridFast);
        grid.getRowConstraints().addAll(rc);
        grid.getColumnConstraints().addAll(cc);

        return new GridsPair(grid, gridFast);
    }
    //byte[0] - pane count
    //{
    //  0. row
    //  1. col
    //  2. span
    //  3. color
    //  4. hFrom
    //  5. mFrom
    //  6. hTo
    //  7. mTo
    //  8. activity name
    //}
    @Override
    public byte[] serialize(GridsPair grid) {
        Block block = new Block();
        byte count = 0;
        block.writeByte(count);
        for(Node node : grid.gridPane.getChildren()){
            if(node.getProperties().containsKey("span")){
                count++;
                int[] cords = getNodeCords(node);
                Map<Object, Object> props = node.getProperties();
                block.writeByte((byte) cords[0]);
                block.writeByte((byte) cords[1]);
                block.writeByte((byte) ((int)(props.get("span"))));
                block.writeColor(getColor(this.color).split(","));
                block.writeString((String) props.get("hf"));
                block.writeString((String) props.get("mf"));
                block.writeString((String) props.get("ht"));
                block.writeString((String) props.get("mt"));
                block.writeString((String) props.get("sp"));
            }
        }
        block.getBuffer()[0] = count;
        byte[] bytes = new byte[block.getSize()];
        System.arraycopy(block.getBuffer(), 0, bytes, 0, block.getSize());

        return bytes;
    }
    //  0. row
    //  1. col
    //  2. span
    //  3. color
    //  4. hFrom
    //  5. mFrom
    //  6. hTo
    //  7. mTo
    //  8. activity name
    @Override
    public GridsPair deserialize(byte[] bytes) {
        GridsPair grids = createGrids();
        Block block = new Block(bytes);
        byte count = block.readByte();
        for (int i = 0; i < count; i++){
            byte row = block.readByte();
            byte col = block.readByte();
            Pane pane = (Pane) grids.gridPaneFast[row][col];
            ObservableMap<Object, Object> props = pane.getProperties();
            byte span = block.readByte();
            grids.spanRecords.put(span, pane);
            for(int j = 1; j < span; j++)
                gridPaneFast[row + j][col] = pane;
            props.put("span", span);
            //pane.setStyle("-fx-background-color: rgba("+ block.readColor() +")");
            pane.setStyle("-fx-background-color: rgba(0,59,160,0.87)");
            String h0 = block.readString();
            String m0 = block.readString();
            String h1 = block.readString();
            String m1 = block.readString();
            String time = String.format("%s:%s - %s:%s", h0, m0, h1, m1);
            Text text = new Text(10, 20, time);
            text.setFill(Paint.valueOf("#FFFFFF"));
            text.setFont(new Font("Helvetica Light", 11.0));
            props.put("hf", h0);
            props.put("mf", m0);
            props.put("ht", h1);
            props.put("mt", m1);
            pane.getChildren().add(text);
            String sp = block.readString();
            props.put("sp", sp);
            Text activity = new Text(10, 40, sp);
            activity.setFill(Paint.valueOf("#FFFFFF"));
            activity.setFont(new Font("Helvetica Light", 11.0));
            pane.getChildren().add(activity);
        }
        return grids;
    }

    private String getColor(String unformatted){
        return unformatted.substring(unformatted.indexOf('(') + 1, unformatted.indexOf(')'));
    }

    public static class GridsPair {
        GridPane gridPane;
        Node[][] gridPaneFast;
        Map<Byte, Node> spanRecords;

        private GridsPair(GridPane gridPane, Node[][] gridPaneFast){
            this.gridPane = gridPane;
            this.gridPaneFast = gridPaneFast;
            spanRecords = new HashMap<>(32);
        }
    }

    private void fillWeek(int offset){
        LocalDate now = LocalDate.of(currWeek.getYear(), currWeek.getMonth(), currWeek.getDayOfMonth());
        DayOfWeek today = now.getDayOfWeek();
        ObservableList<Node> childs = dates.getChildren();
        for(int i = today.getValue() - 1; i < 7; i++){
            TextField field = (TextField) childs.get(i);
            field.setText(String.valueOf(now.plusDays(i - (today.getValue() - 1) + offset).getDayOfMonth()));
        }
        for(int i = today.getValue() - 2; i > -1; i--){
            TextField field = (TextField) childs.get(i);
            field.setText(String.valueOf(now.minusDays((today.getValue() - 1) - i - offset).getDayOfMonth()));
        }

        TextField todayF = (TextField)(childs.get(today.getValue() - 1));
        if(Integer.parseInt(todayF.getText()) == LocalDate.now().getDayOfMonth()){
            todayF.getStyleClass().add("fieldTodayRed");
        }
        else
            todayF.getStyleClass().remove("fieldTodayRed");
        currWeek = currWeek.plusDays(offset);
        month.setText(currWeek.format(DateTimeFormatter.ofPattern("MMMM")));
        //loadTable(currWeek.getMonth().getValue() + "-" + (byte)Math.ceil((currWeek.getDayOfMonth() / (float)currWeek.lengthOfMonth()) * 4.0));
    }

    private void fillGrids(GridPane gridPane, Node[][] gridPaneFast){
        for(int col = 0; col < gridPane.getColumnConstraints().size(); col++){
            for(int row = 0; row < gridPane.getRowConstraints().size(); row++){
                Pane p = createPane(false);
                gridPane.add(p, col, row);
                gridPaneFast[row][col] = p;
            }
        }
    }

    private boolean checkFreeSpace(int[] cords, int span) {
        return gridPaneFast[cords[0] + span][cords[1]].getProperties().containsKey("span");
    }

    private void addBetween(int start, int end, int col){
        if(end < start){
            int temp = start;
            start = end;
            end = temp;
        }
        for(int i = start; i < end + 1; i++){
            Pane p = createPane(true);
            gridPane.add(p, col, i);
            gridPaneFast[i][col] = p;
        }
    }

    private void deleteBetween(Node extendee, int startRow, int endRow, int col) {
        for(int i = startRow; i <= endRow; i++){
            gridPane.getChildren().remove(getNode(i, col));
            gridPaneFast[i][col] = extendee;
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
                    if(prevPane != null && !prevPane.getProperties().containsKey("span") &&
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

    private int[] getNodeCords(Node n){ //[0:row, 1:col]
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