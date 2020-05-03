package controller;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.App;
import model.Tools.ArrayList;
import model.Tools.Block;
import model.Tools.Serializable;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Calendar extends Menu implements Initializable, Serializable<Calendar.WeekProperties> {

    private static ArrayList<WeekProperties> weeks;
    @FXML
    private Text month;
    @FXML
    private GridPane gridPane;
    private Node[][] gridPaneFast;
    private Node tempPane, prevPane;
    @FXML
    private HBox options, dates;
    @FXML
    private Button prev, next, bug;
    private int tempRow, tempCol, initY;
    private boolean flag = true, ctrl, modified = false;
    private final String color = "-fx-background-color: rgba(255,51,61,0.83)";
    private LocalDate currentWeek;

    static {
        weeks = new ArrayList<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentWeek = LocalDate.now();
        gridPaneFast = new Node[gridPane.getRowConstraints().size()][gridPane.getColumnConstraints().size()];
        bindTab(this);
        loadWeeksDB();
        loadTable(0);
        fillWeek(0);

        loadAsEditor();

        gridPane.setOnMousePressed(e -> modified = true);
        prev.setOnMouseClicked(e -> {
            loadTable(-7);//currentWeek unmodified
            fillWeek(-7);//currentWeek modified
            //gridPane.getChildren().forEach(pane -> pane.setOnMouseClicked(e1 -> buildActivity(pane)));
            modified = false;
        });
        next.setOnMouseClicked(e -> {
            loadTable(7);//currentWeek unmodified
            fillWeek(7);//currentWeek modified
            //gridPane.getChildren().forEach(pane -> pane.setOnMouseClicked(e1 -> buildActivity(pane)));
            modified = false;
        });
//        fillGrids(this.gridPane, this.gridPaneFast);
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
    protected void onSceneSwitch() {
        if(modified)
            saveCurrentTable();
    }

    @Override
    protected void onAppClose() {
        saveWeeksDB();
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
                    //moving sides
                }
                else {
                    if(cords[0] == 0 || checkFreeSpace(cords, -1)){
                        return;
                    }

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
                    //moving sides
                }
                else {
                    if(cords[0] + span == gridPane.getRowConstraints().size() || checkFreeSpace(cords, span))//grindPane.rows.count
                    {
                        return;
                    }
                    var0 = span;
                    addBetween(cords[0], cords[0], cords[1]);//strict order
                    gridPane.getChildren().remove(pane);//strict order
                    gridPane.add(pane, cords[1], cords[0] + 1);//strict order
                    gridPaneFast[cords[0] + var0][cords[1]] = pane;
                    buildTime(pane, span);
                    GridPane.setRowSpan(pane, span);
                }
            }
            //generalize methods, put commons parts away
        }
    }

    private void saveWeeksDB() { //DATABASE SAVE
        byte[][] weeks = new byte[Calendar.weeks.size()][];
        for(int i = 0; i < Calendar.weeks.size(); i ++){
            weeks[i] = serialize(Calendar.weeks.get(i));
        }
        App.databaseManager.saveWeeks(weeks);
    }

    private void saveCurrentTable(){//modified true NOT DATABASE SAVE
        for(int i = 0; i < weeks.size(); i++)
            if(weeks.get(i).isBetween(currentWeek.getDayOfYear()))
                weeks.removeAt(i);

        WeekProperties newWeek = new WeekProperties(currentWeek.getDayOfYear() - currentWeek.getDayOfWeek().getValue() + 1, currentWeek.getDayOfYear() + (7 - currentWeek.getDayOfWeek().getValue()));
        for(Node pane : gridPane.getChildren()) {//grabbing all baked panes
            ObservableMap<Object, Object> props = pane.getProperties();
            if (props.containsKey("span")) {
                int[] cords = getNodeCords(pane);
                newWeek.addProperty(new WeekProperties.PaneProperties(
                        cords[0], cords[1], (int) pane.getProperties().get("span"),
                        (String) props.get("hf"), (String)props.get("mf"), (String)props.get("ht"), (String)props.get("mt"), pane.getStyle(),
                        (Text) ((Pane) pane).getChildren().get(0), (Text) ((Pane) pane).getChildren().get(1)
                ));
            }
        }
        weeks.add(newWeek);
    }

    private void loadWeeksDB() {
        byte[][] weeks = App.databaseManager.loadWeeks();
        for(byte[] week : weeks)
            Calendar.weeks.add(deserialize(week));
    }

    private void loadTable(int offset){      //FIX!!!!!!!!
        tempPane = null;
        tempRow = -1;
        tempCol = -1;
        flag = true;
        if(modified)
            saveCurrentTable();
        WeekProperties desiredWeek = null;
        int desiredDay = currentWeek.plusDays(offset).getDayOfYear();
        for(int i = 0; i < Calendar.weeks.size(); i++){
            if(Calendar.weeks.get(i).isBetween(desiredDay)){//found timetable for desired week (prev or next)
                desiredWeek = Calendar.weeks.get(i);
                //desiredWeek.panes = Calendar.weeks.get(i).panes;
                //desiredWeek.panes.setSize(Calendar.weeks.get(i).panes.size());
                break;
            }
        }
        fillGrids(gridPane, gridPaneFast, true);
        if(desiredWeek != null)
            for(int i = 0; i < desiredWeek.panes.size(); i++){
                WeekProperties.PaneProperties paneProp = desiredWeek.panes.get(i);
                Pane pane = (Pane) getNode(paneProp.row, paneProp.col);
                ObservableMap<Object, Object> props = pane.getProperties();
                ObservableList<Node> childs = pane.getChildren();
                props.put("span", paneProp.span);
                props.put("hf", paneProp.hf);
                props.put("mf", paneProp.mf);
                props.put("ht", paneProp.ht);
                props.put("mt", paneProp.mt);
                childs.add(paneProp.time);
                childs.add(paneProp.sport);
                GridPane.setRowSpan(pane, paneProp.span);
                deleteBetween(pane, paneProp.row + 1, paneProp.row + paneProp.span - 1, paneProp.col);
                pane.setStyle(paneProp.color);
                pane.setOnMouseClicked(null);
                pane.setOnMousePressed(e -> initY = (short)e.getSceneY());
                pane.setOnMouseDragged(e -> move(pane, (int) e.getSceneY()));
                for(int j = 1; j < paneProp.span; j++)
                    gridPaneFast[paneProp.row + i][paneProp.col] = pane;
            }
    }

    public void loadAsEditor(){
        gridPane.getChildren().forEach(pane -> pane.setOnMouseClicked(e -> buildActivity(pane)));
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
        block.writeInt16((short) week.dayFrom);
        block.writeInt16((short) week.dayTo);
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
            block.writeString(pane.sport.getText());
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
            String tm = String.format("%s:%s - %s:%s", hf, mf, ht, mt);
            Text time = new Text(10, 20, tm);
            time.setFill(Paint.valueOf("#FFFFFF"));
            time.setFont(new Font("Helvetica Light", 11.0));
            String sp = block.readString();
            Text sport = new Text(10, 40, sp);
            sport.setFill(Paint.valueOf("#FFFFFF"));
            sport.setFont(new Font("Helvetica Light", 11.0));

            week.addProperty(new WeekProperties.PaneProperties(row, col, span, hf, mf, ht, mt, color, time, sport));
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

        public boolean isBetween(int currentDay){
            return currentDay >= dayFrom && currentDay <= dayTo;
        }

        public static class PaneProperties {
            int row, col, span;
            Text time, sport;
            String color, hf, mf, ht, mt;

            private PaneProperties(int row, int col, int span, String hf, String mf, String ht, String mt, String color, Text time, Text sport){
                this.row = row;
                this.col = col;
                this.span = span;
                this.hf = hf;
                this.mf = mf;
                this.ht = ht;
                this.mt = mt;
                this.color = color;
                this.time = time;
                this.sport = sport;
            }
        }
    }

    private String getRGBAColor(String unformatted){
        return unformatted.substring(unformatted.indexOf('(') + 1, unformatted.indexOf(')'));
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
}
// row: 15px, 4 rows per hour, textfield: 22px, spacing: 4(15px) - 2(22px / 2)