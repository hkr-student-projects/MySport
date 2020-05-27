package controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import model.App;
import model.Database.MongoManager;
import model.Logging.Logger;
import model.People.Leader;
import model.People.Member;
import model.Tools.ArrayList;
import model.Tools.Decomposition.Block;
import model.Tools.EventType;
import model.Tools.SceneSwitcher;
import model.Tools.Serializable;
import model.Tools.Tags.*;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class Calendar extends Menu implements Initializable, Serializable<Calendar.WeekProperties> {

    @FXML
    private Text month;
    @FXML
    private GridPane gridPane;
    @FXML
    private HBox options, dates;
    @FXML
    private Button prev, next, adjust;
    @FXML
    private Pane dimming, sportContainer;
    @FXML
    private JFXComboBox<String> sportList;
    @FXML
    private ColorPicker sportColor;
    private Node[][] gridPaneFast;
    private Node tempPane, prevPane;
    private int tempRow, tempCol, initY, initX;
    private boolean flag = true, editor = false, modified = false, altDown = false;
    private LocalDate currentWeek;
    private static ArrayList<WeekProperties> weeks;

    static {
        weeks = new ArrayList<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Thread thread = new Thread(() -> {
            currentWeek = LocalDate.now();
            loadWeeksDB();
            loadTable(0);
            fillWeek(0);
            bindTab(this);
        });
        thread.start();
        gridPaneFast = new Node[gridPane.getRowConstraints().size()][gridPane.getColumnConstraints().size()];
        //gridPane.setOnMousePressed(e -> modified = true);
        prev.setOnMouseClicked(e -> {
            loadTable(-7);//currentWeek unmodified
            fillWeek(-7);//currentWeek modified
            modified = false;
        });
        next.setOnMouseClicked(e -> {
            loadTable(7);//currentWeek unmodified
            fillWeek(7);//currentWeek modified
            modified = false;
        });
        SceneSwitcher.addListener("Calendar", EventType.ON_KEY_PRESSED, e -> {
             if(e.getCode() == KeyCode.ALT) altDown = true;
        });
        SceneSwitcher.addListener("Calendar", EventType.ON_KEY_RELEASED, e -> {
            if(e.getCode() == KeyCode.ALT) altDown = false;
        });
        loadSports(new String[] { "Chess", "Volleyball" });
        try {
            thread.join();
        } catch (InterruptedException e) {
            Logger.logException(e);
        }
    }

    @Override
    protected void onBurgerOpen() {
        super.toggleTab();
        //polymorphic starts
        options.setVisible(true);
        options.setDisable(false);
    }

    @Override
    protected void onBurgerClose() {
        super.toggleTab();
        //polymorphic starts
        options.setVisible(false);
        options.setDisable(true);
    }

    @Override
    protected void onBeforeSceneSwitch() {
        if(modified)
            saveCurrentTable();
    }

    @Override
    protected void onBeforeLogout() {
        if(modified)
            saveCurrentTable();
        saveWeeksDB();
        unloadUser();
    }

    private void buildActivity(Node pane){//works only if editable true (loadWeek -> fillGrid -> createPane (trackable))
        if((tempPane != null && getNodeCords(pane)[1] != getNodeCords(tempPane)[1]))
            return;

        if(flag = !flag) {
            int[] tempPaneCords = getNodeCords(tempPane);//tempPane cannot be null because at first "!flag" will return false and else block will assign reference to tempPane
            int extendBy = tempRow - tempPaneCords[0];
            if (tempRow == tempPaneCords[0] || Math.abs(extendBy) < 5 || paneOverlap(tempPane, pane))
            {
                nodeTrackMouse(false);
                resetColorBetween(tempPaneCords, getNodeCords(pane));
                tempPane = null;
                return;
            }
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
            bakePane(tempPane, Math.abs(extendBy) + 1);
        }
        else {
            pane.setStyle(getBackgroundColor());
            int[] cords = getNodeCords(pane);
            tempRow = cords[0];
            tempCol = cords[1];
            tempPane = pane;
            nodeTrackMouse(true);
        }
    }

    private boolean paneOverlap(Node tempPane, Node clickedPane){//clicked pane usually is below
        int[] cords0 = getNodeCords(tempPane);
        int[] cords1 = getNodeCords(clickedPane);
        if(cords1[0] < cords0[0]){//if clicked is upper change places
            int temp = cords1[0];
            cords1[0] = cords0[0];
            cords0[0] = temp;
        }
        for(int i = cords0[0]; i < cords1[0] + 1; i++)
            if(getNode(i, cords0[1]).getProperties().containsKey("span"))
                return true;

        return false;
    }

    private void bakePane(Node pane, int span) {
        nodeTrackMouse(false);
        buildProps((Pane)pane, span);
        modified = true;
        reset();
    }

    private void reset(){//reset pane creation to default
        prevPane = null;
        tempPane = null;
        tempCol = -1;
        tempRow = -1;
        flag = true;
    }

    private void buildProps(Pane pane, int span){
        buildTime(pane, span);
        String sp = sportList.getValue() == null ? sportList.getItems().get(0) : sportList.getValue();
        new Thread(() -> App.mongoManager.addActivity(
                LocalDate.of(
                        this.currentWeek.getYear(),
                        this.currentWeek.getMonthValue(),
                        this.currentWeek.getDayOfYear()
                ),
                new MongoManager.Activity(
                        sp, "Hus 7", 0, 930, 1005,
                        new java.util.ArrayList<>(),
                        new java.util.ArrayList<>()
                )
        )).start();
        addChildProps(pane, span, 0, sp);
        addChilds(pane,
                buildSport(sp),
                buildJoins(span, 0),
                buildJoin(span)
        );
    }

    @Related(to = { "Baked pane", "Calendar.buildProps()", "Calendar.loadWeek()" })
    private void addChilds(Pane pane, Node... nodes){
        setEntered(pane);
        pane.getChildren().addAll(nodes);
        pane.setOnMousePressed(e -> { initY = (short)e.getSceneY(); initX = (short)e.getSceneX(); });
        if(editor){
            pane.setOnMouseDragged(e -> move(pane, (int)e.getSceneY(), (int)e.getSceneX()));
            pane.getStyleClass().add( "cursorHResize");
        }
        pane.setOnMouseClicked(null);
        pane.getStyleClass().addAll("hoverScale", "bakedPane");
    }

    @Related(to = { "Baked pane", "Calendar.buildProps()", "Calendar.loadWeek()" })
    private void addTimeProps(Pane pane, String hf, String mf, String ht, String mt){
        ObservableMap<Object, Object> props = pane.getProperties();
        props.put("hf", hf);
        props.put("mf", mf);
        props.put("ht", ht);
        props.put("mt", mt);
    }

    @Related(to = { "Baked pane", "Calendar.buildProps()", "Calendar.loadWeek()" })
    private void addChildProps(Pane pane, int span, int jns, String sp){//sp = sport
        ObservableMap<Object, Object> props = pane.getProperties();
        props.put("span", span);
        props.put("jns", jns);
        props.put("sp", sp);
    }

    @Related(to = { "Baked pane", "Calendar.buildProps()", "Calendar.loadWeek()" })
    private Text buildTime(String time){//baked time
        Text text = new Text(10, 20, time);
        text.setFill(Paint.valueOf("#FFFFFF"));
        text.setFont(new Font("Helvetica Light", 11.0));

        return text;
    }

    @Related(to = { "Baked pane", "Calendar.buildProps()", "Calendar.loadWeek()" })
    private Text buildSport(String name){//baked sport
        Text sport = new Text(10, 40, name);
        sport.setFill(Paint.valueOf("#FFFFFF"));
        sport.setFont(new Font("Helvetica Light", 11.0));

        return sport;
    }

    @Related(to = { "Baked pane", "Calendar.buildProps()", "Calendar.loadWeek()" })
    private Text buildJoins(int span, int jns){//baked sport
        Text joins = new Text(90, 15 * (span - 1) - 1, jns + "+");
        joins.setFill(Paint.valueOf("#FFFFFF"));
        joins.setFont(new Font("Helvetica Light", 11.0));

        return joins;
    }

    @Related(to = { "Baked pane", "Calendar.buildProps()", "Calendar.loadWeek()" })
    private Button buildJoin(int span){//baked sport
        Button join = new Button("Join");
        join.getStylesheets().add("/view/css/general.css");
        join.getStyleClass().addAll("joinButton", "cursorHand");
        join.setPrefWidth(30);
        join.setPrefHeight(30);
        join.setFont(new Font("Helvetica Light", 8));
        join.setLayoutX(10);
        join.setLayoutY(15 * (span - 1) - 20);
        join.setOnMouseClicked(e -> switchAction(join));

        return join;
    }

    @Related(to = { "Calendar.buildJoin()" })
    private void switchAction(Button button){
        modified = true;
        boolean flag = button.getText().equals("Join");
        Pane p = (Pane) button.getParent();
        Text t = (Text) p.getChildren().get(2);
        ObservableMap<Object, Object> props = p.getProperties();
        int jns = (int) props.get("jns");
        joinActivity(flag, ((Text) p.getChildren().get(1)).getText());
        if(flag){
            t.setText(++jns + "+");
            props.remove("jns");
            props.put("jns", jns);
            button.setText("Out");
            button.setDisable(true);
            button.setVisible(false);
        }
        else{
            t.setText(--jns + "+");
            props.remove("jns");
            props.put("jns", jns);
            button.setText("Join");
        }
    }

    @Related(to = { "Calendar.switchAction()" })
    private void joinActivity(boolean join, String sport){
        if(join)
            App.mongoManager.addParticipant(
                    this.currentWeek, sport, App.instance.getSession().getId(),
                    App.instance.getSession().getClass() == Leader.class
            );
        else
            App.mongoManager.removeParticipant(
                    this.currentWeek, sport, App.instance.getSession().getId(),
                    App.instance.getSession().getClass() == Leader.class
            );
    }

    @Related(to = {"Calendar.move()"})
    private void buildTime(Pane pane, int span){//unite method with for deserialization week method
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

        String time = String.format("%s:%s - %s:%s", h0, m0, h1, m1);
        addTimeProps(pane, h0, m0, h1, m1);

        if(pane.getChildren().size() != 0)
            ((Text)(pane.getChildren().get(0))).setText(time);
        else
            pane.getChildren().add(buildTime(time));
    }

    private void move(Pane pane, int sceneY, int sceneX){
        int difY = initY - sceneY;
        int difX = initX - sceneX;
        if(Math.abs(difY) > 15){
            int[] cords = getNodeCords(pane);
            int span = (int)pane.getProperties().get("span");
            int var0 = span;
            int var1 = 0;
            int var2 = 1;
            if(difY > 0){
                if(cords[0] == 0 || checkFreeSpace(cords, -1))
                    return;
                var0 = -1;
                var1 = span - 1;
                var2 = -1;
            }
            else
                if(cords[0] + span == gridPane.getRowConstraints().size() || checkFreeSpace(cords, span))//grindPane.rows.count
                    return;
            addBetween(cords[0] + var1, cords[0] + var1, cords[1]);
            gridPane.getChildren().remove(pane);//strict order
            gridPane.add(pane, cords[1], cords[0] + var2);//strict order
            gridPaneFast[cords[0] + var0][cords[1]] = pane;
            buildTime(pane, span);
            GridPane.setRowSpan(pane, span);
        }
        else if(Math.abs(difX) > 50){
            int[] cords = getNodeCords(pane);
            int span = (int)pane.getProperties().get("span");
            gridPane.getChildren().remove(pane);
            addBetween(cords[0], cords[0] + span - 1, cords[1]);
        }
    }


    private void saveWeeksDB() { //DATABASE SAVE
        if(weeks.size() == 0)
            return;
        byte[][] weeks = new byte[Calendar.weeks.size()][];
        for(int i = 0; i < Calendar.weeks.size(); i ++)
            weeks[i] = serialize(Calendar.weeks.get(i));

        App.mySqlManager.saveWeeks(weeks);
    }

    private void saveCurrentTable(){//modified true NOT DATABASE SAVE
        for(int i = 0; i < weeks.size(); i++)
            if(weeks.get(i).isBetween(currentWeek.getDayOfYear()))
                weeks.removeAt(i);

        WeekProperties newWeek = new WeekProperties(
                currentWeek.getDayOfYear() - currentWeek.getDayOfWeek().getValue() + 1,
                currentWeek.getDayOfYear() + (7 - currentWeek.getDayOfWeek().getValue())
        );
        for(Node pane : gridPane.getChildren()) {//grabbing all baked panes
            ObservableMap<Object, Object> props = pane.getProperties();
            if (props.containsKey("span")) {
                int[] cords = getNodeCords(pane);
                newWeek.addProperty(new WeekProperties.PaneProperties(
                        cords[0], cords[1], (int) pane.getProperties().get("span"), (int) pane.getProperties().get("jns"),
                        (String) props.get("hf"), (String)props.get("mf"), (String)props.get("ht"), (String)props.get("mt"), pane.getStyle(),
                        (Text) ((Pane) pane).getChildren().get(0), (Text) ((Pane) pane).getChildren().get(1), (Text) ((Pane) pane).getChildren().get(2), (Button) ((Pane) pane).getChildren().get(3)
                ));
            }
        }
        weeks.add(newWeek);
    }

    private void loadWeeksDB() {
        //leaderPanes = new ArrayList<>(30);//for changing color once for all belonging panes
        byte[][] weeks = App.mySqlManager.loadWeeks();
        for(byte[] week : weeks)
            Calendar.weeks.add(deserialize(week));
    }

    private void loadTable(int offset){
        if(modified)
            saveCurrentTable();
        WeekProperties desiredWeek = null;
        int desiredDay = currentWeek.plusDays(offset).getDayOfYear();
        for(int i = 0; i < Calendar.weeks.size(); i++){
            if(Calendar.weeks.get(i).isBetween(desiredDay)){
                desiredWeek = Calendar.weeks.get(i);
                break;
            }
        }
        fillGrids(gridPane, gridPaneFast, editor);
        if(desiredWeek != null)
            for(int i = 0; i < desiredWeek.panes.size(); i++){
                WeekProperties.PaneProperties paneProp = desiredWeek.panes.get(i);
                Pane pane = (Pane) getNode(paneProp.row, paneProp.col);
                pane.setStyle(paneProp.color);
                addChildProps(pane, paneProp.span, paneProp.jns, paneProp.activity.getText());
                addTimeProps(pane, paneProp.hf, paneProp.mf, paneProp.ht, paneProp.mt);
                addChilds(pane, paneProp.time, paneProp.activity, paneProp.joins, paneProp.join);
                GridPane.setRowSpan(pane, paneProp.span);
                deleteBetween(pane, paneProp.row + 1, paneProp.row + paneProp.span - 1, paneProp.col);
            }
    }

    @Related(to = { "Baked pane" })
    private void setEntered(Pane pane) {
        pane.setOnMouseEntered(e -> {
            Button b = (Button) pane.getChildren().get(3);
            if(altDown && b.isDisable())
            {
                b.setDisable(false);
                b.setVisible(true);
                b.setOnMouseClicked(e1 -> switchAction(b));
            }
        });
    }
    //  count
//  week: {year, mon, day}
// {
//  row;
//  col;
//  span;
//  color;
//  time: {hour from, min from, hour to, min to}
//  sport;
// }
    @Override
    public byte[] serialize(WeekProperties week) {
        Block block = new Block();
        block.writeByte((byte) week.panes.size());
        block.writeInt16(week.dayFrom);
        block.writeInt16(week.dayTo);
        for(int i = 0; i < week.panes.size(); i++){
            WeekProperties.PaneProperties pane = week.panes.get(i);
            block.writeByte((byte) pane.row);
            block.writeByte((byte) pane.col);
            block.writeByte((byte) pane.span);
            block.writeColor(getRGBAColor(pane.color).split(","));
            block.writeString(pane.hf);
            block.writeString(pane.mf);
            block.writeString(pane.ht);
            block.writeString(pane.mt);
            block.writeString(pane.activity.getText());
            block.writeInt16((short) pane.jns);
        }
        byte[] bytes = new byte[block.getSize()];
        System.arraycopy(block.getBuffer(), 0, bytes, 0, block.getSize());

        return bytes;
    }
    //  count
    //  week: {year, mon, day}
    // {
    //  row;
    //  col;
    //  span;
    //  color;
    //  time: {hour from, min from, hour to, min to}
    //  sport;
    // }
    @Override
    public WeekProperties deserialize(byte[] bytes) {
        Block block = new Block(bytes);
        byte count = block.readByte();
        WeekProperties week = new WeekProperties(block.readInt16(), block.readInt16());
        for (int i = 0; i < count; i++){
            byte row = block.readByte();
            byte col = block.readByte();
            byte span = block.readByte();
            String color = "-fx-background-color: rgba("+ block.readColor() +")";
            String hf = block.readString();
            String mf = block.readString();
            String ht = block.readString();
            String mt = block.readString();
            String sportName = block.readString();
            short jns = block.readInt16();

            week.addProperty(new WeekProperties.PaneProperties(row, col, span, jns, hf, mf, ht, mt, color,
                    buildTime(String.format("%s:%s - %s:%s", hf, mf, ht, mt)),
                    buildSport(sportName),
                    buildJoins(span, jns),
                    buildJoin(span)
            ));
        }

        return week;
    }

    public static class WeekProperties {
        ArrayList<PaneProperties> panes;
        short dayFrom;
        short dayTo;

        private WeekProperties(int dayFrom, int dayTo){
            panes = new ArrayList<>(32);
            this.dayFrom = (short) dayFrom; //day of month
            this.dayTo = (short) dayTo;
        }

        public void addProperty(PaneProperties paneProperty){
            this.panes.add(paneProperty);
        }

        public void removeProperty(int row, int col){
            for(int i = 0; i < panes.size(); i++) {
                PaneProperties prop = panes.get(i);
                if (prop.row == row && prop.col == col)
                    panes.removeAt(i);
            }
        }

        @Related(to = { "Calendar.saveCurrentTable", "Calendar.loadTable" })
        public boolean isBetween(int currentDay){
            return currentDay >= dayFrom && currentDay <= dayTo;
        }

        public static class PaneProperties {
            int row, col, span, jns;
            Text time, activity, joins;
            String color, hf, mf, ht, mt;
            Button join;

            private PaneProperties(int row, int col, int span, int jns, String hf, String mf, String ht, String mt, String color, Text time, Text activity, Text joins, Button join){
                this.row = row;
                this.col = col;
                this.span = span;
                this.hf = hf;
                this.mf = mf;
                this.ht = ht;
                this.mt = mt;
                this.color = color;
                this.time = time;
                this.activity = activity;
                this.joins = joins;
                this.join = join;
                this.jns = jns;
            }
        }
    }

    private String getRGBAColor(String unformatted){
        return unformatted.substring(unformatted.indexOf('(') + 1, unformatted.indexOf(')'));
    }

    private boolean checkFreeSpace(int[] cords, int span) {
        return gridPaneFast[cords[0] + span][cords[1]].getProperties().containsKey("span");
    }

    @Related(to = { "Calendar.move()" })
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

    @Related(to = { "Calendar.move()" })
    private void deleteBetween(int startRow, int endRow, int col) {
        for(int i = startRow; i < endRow + 1; i++){
            gridPane.getChildren().remove(getNode(i, col));
            gridPaneFast[i][col] = createPane(true);
        }
    }

    @Related(to = { "Calendar.move()" })
    private void deleteBetween(Node extendee, int startRow, int endRow, int col) {
        for(int i = startRow; i < endRow + 1; i++){
            gridPane.getChildren().remove(getNode(i, col));
            gridPaneFast[i][col] = extendee;
        }
    }

    private void nodeTrackMouse(boolean flag){
        if(flag){
            gridPane.getChildren().forEach(p -> p.setOnMouseEntered(e -> {
                int[] curCords = getNodeCords(p);
                int[] tempCords = getNodeCords(tempPane);
                if(curCords[1] != tempCords[1])
                    return;
                if(prevPane != null
                        && !prevPane.getProperties().containsKey("span")
                        && isAfter(getNodeCords(p), getNodeCords(prevPane))
                ) {
                    int[] prevCords = getNodeCords(prevPane);
                    resetColorBetween(curCords, prevCords);
                }
                if(!p.getProperties().containsKey("span"))//do not allow to go over it
                    p.setStyle(getBackgroundColor());

                tempRow = curCords[0];
                tempCol = curCords[1];
                checkColorBetween(tempCords, curCords);
                prevPane = p;
            }));
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
            if(node.getStyle().isEmpty())
                node.setStyle(getBackgroundColor());
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
            if(!node.getProperties().containsKey("span"))
                node.setStyle("");
        }
    }

    @Related(to = { "Calendar.nodeTrackMouse()" })
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

    private void fillWeek(int offset){
        LocalDate now = LocalDate.of(currentWeek.getYear(), currentWeek.getMonth(), currentWeek.getDayOfMonth());
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
        currentWeek = currentWeek.plusDays(offset);
        month.setText(currentWeek.format(DateTimeFormatter.ofPattern("MMMM")));
        //loadTable(currWeek.getMonth().getValue() + "-" + (byte)Math.ceil((currWeek.getDayOfMonth() / (float)currWeek.lengthOfMonth()) * 4.0));
    }

    private void fillGrids(GridPane gridPane, Node[][] gridPaneFast, boolean trackable){
        gridPane.getChildren().clear();
        for(int col = 0; col < gridPane.getColumnConstraints().size(); col++){
            for(int row = 0; row < gridPane.getRowConstraints().size(); row++){
                Pane p = createPane(trackable);
                gridPane.add(p, col, row);
                gridPaneFast[row][col] = p;
            }
        }
    }

    private String getBackgroundColor(){
        Color color = sportColor.getValue();
        return getBackgroundColor((int)color.getRed() * 255, (int)color.getGreen() * 255, (int)color.getBlue() * 255, color.getOpacity());
    }

    private String getBackgroundColor(int r, int g, int b, double o){
        return String.format("-fx-background-color: rgba(%s,%s,%s,%s)", r, g, b, o);
    }

    public void loadUser(){
        if(App.instance.getSession().getClass() == Member.class)
            return;
        loadSports(((Leader)App.instance.getSession()).getLeaderOf());
    }

    private void loadSports(String[] sports){
        editor = true;
        adjust.setDisable(false);
        adjust.setVisible(true);
        adjust.setOnMouseClicked(e -> {
            dimming.setDisable(false);
            dimming.setVisible(true);
            sportContainer.setDisable(false);
            sportContainer.setVisible(true);
            SceneSwitcher.addListener("Calendar", EventType.ON_KEY_PRESSED, keyEvent -> {
                {
                    if(keyEvent.getCode() == KeyCode.ESCAPE)
                    {
                        dimming.setDisable(true);
                        dimming.setVisible(false);
                        sportContainer.setDisable(true);
                        sportContainer.setVisible(false);
                    }
                }
            });
        });
        sportColor.setValue(Color.color(255 / 255.0, 51 / 255.0, 61 / 255.0, 0.83));
        this.sportList.setItems(FXCollections.observableArrayList(List.of(sports)));//dont use FXCollections.observableList because of unknown css exception
        gridPane.getChildren().forEach(pane -> pane.setOnMouseClicked(e -> {
            buildActivity(pane);
        }));
    }

    public void unloadUser(){
        sessionName.setText("");
        editor = false;
        adjust.setDisable(true);
        adjust.setVisible(false);
        adjust.setOnMouseClicked(null);
        sportColor.setValue(null);
        this.sportList.getItems().clear();//dont use FXCollections.observableList because of unknown css exception
        gridPane.getChildren().forEach(pane -> pane.setOnMouseClicked(null));
    }
}
// row: 15px, 4 rows per hour, textfield: 22px, spacing: 4(15px) - 2(22px / 2)
