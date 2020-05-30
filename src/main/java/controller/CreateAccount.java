package controller;

import com.jfoenix.controls.JFXDatePicker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import model.App;
import model.Tools.ArrayList;
import model.Tools.SceneSwitcher;
import model.Tools.ThreadResult;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

public class CreateAccount implements Initializable {

    @FXML
    private TextField firstname, middlename, surname, ssn, mobile, email;
    @FXML
    private PasswordField password, repassword;
    @FXML
    private JFXDatePicker birthday;
    @FXML
    private Line line0, line1, line2, line3, line4, line5, line6, line7, line8;
    @FXML
    private Label passError, emailError;
    private boolean emailFormat = true;
    private String emailStr;
    private Map<String, model.Tools.ArrayList<String>> emailsList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addFocusStyle(new TextField[]{firstname, middlename, surname, ssn, mobile, email, password, repassword},
                line0, line1, line2, line3, line4, line5, line6, line7
        );
        emailsList = App.mySqlManager.getEmail();
        email.setOnKeyTyped(e -> {
                if (email.getText().matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9]{2,10}\\.)+[A-Za-z]{2,8}$")) {
                    ThreadResult<String, Boolean> emailCheck = new ThreadResult<>(this::getEmails, email.getText());
                    Thread thread = new Thread(emailCheck);
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    if (emailCheck.getValue()) {
                        emailFormat = false;
                        redLine(line5);
                        emailError.setVisible(true);
                    } else
                        emailFormat = true;
                } else {
                    emailFormat = false;
                    resetLine(line5);
                    emailError.setVisible(false);
                }

                if (e.getCode() == KeyCode.BACK_SPACE && email.getText().isEmpty())
                    emailStr = "";
                else if (e.getCode() == KeyCode.BACK_SPACE && email.getText().length() > 0)
                    emailStr = emailStr.substring(0, emailStr.length() - 2);
                else
                    emailStr += e.getText();

        });
    }

    private void addFocusStyle(TextField[] fields, Line... lines) {
        if (fields.length != lines.length)
            throw new IllegalArgumentException("TextField array and Line array have different lengths");
        for (int i = 0; i < fields.length; i++) {
            int i2 = i;
            fields[i].focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (isNowFocused) {
                    //fields[i2].setText("");
                    lines[i2].setStroke(Paint.valueOf("#000000"));
                    lines[i2].setStrokeWidth(1);
                    passError.setVisible(false);
                }
            });
        }
    }

//    private boolean existsEmail(String email) {
//        return App.mySqlManager.existsEmail(email);
//    }

    private Boolean getEmails(String email) {
        ArrayList<String> entries = emailsList.get("email");

        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).matches(email)) {
                return true;
            }
        }
        return false;
    }

    private void resetLine(Line line) {
        line.setStroke(Paint.valueOf("#000000"));
        line.setStrokeWidth(1);
    }

    private void resetLine(Line... lines) {
        for (Line line : lines)
            resetLine(line);
    }

    private void redLine(Line... lines) {
        for (Line line : lines)
            redLine(line);
    }

    private void redLine(Line line) {
        line.setStrokeWidth(2);
        line.setStroke(Paint.valueOf("#ff4c4c"));
    }

    private boolean fieldCheck(TextField field, String pattern, Line line) {
        if (field.getText().matches(pattern))
            return true;
        redLine(line);

        return false;
    }

    private boolean passwordCheck() {
        if (password.getText().length() < 5) {
            redLine(line6, line7);
            passError.setVisible(true);
            passError.setText("Password length must be at least 5 symbols");
            return false;
        }
        if (!password.getText().equals(repassword.getText())) {
            redLine(line6, line7);
            passError.setVisible(true);
            passError.setText("Password does not match");
            return false;
        }

        return true;
    }

    private boolean dateCheck() {
        if (birthday.getValue() == null || ((LocalDate.now().getYear() - birthday.getValue().getYear() < 16) & (LocalDate.now().getYear() - birthday.getValue().getYear() > 100))) {
            redLine(line8);
            return false;
        }
        return true;
    }

    @FXML
    private void saveClick() {
        String naming = "[A-Z][a-z]{1,50}";

        if (fieldCheck(firstname, naming, line0) & (middlename.getText().isEmpty() || fieldCheck(middlename, naming, line1))
                & fieldCheck(surname, naming, line2) & fieldCheck(ssn, "\\d{8}\\-\\d{4}", line3) & fieldCheck(mobile, "\\+\\d{3}\\-\\d{3}\\-\\d{2}\\-\\d{2}", line4)
                & emailFormat & passwordCheck() & dateCheck()
        ) {
            new Thread(() -> App.mySqlManager.addAccount(firstname.getText(), middlename.getText(), surname.getText(), ssn.getText(), mobile.getText(), email.getText(), password.getText(), Date.valueOf(birthday.getValue()))).start();
            App.instance.setScene(SceneSwitcher.instance.getScene("Login"));
        }
    }

    @FXML
    public void clearFields() {

        firstname.setText("");
        surname.setText("");
        middlename.setText("");
        ssn.setText("");
        email.setText("");
        mobile.setText("");

        resetLine(line0, line1, line2, line3, line4, line5, line6, line7, line8);
        passError.setVisible(false);
        emailError.setVisible(false);
    }

    @FXML
    public void returnHome() {
        App.instance.setScene(SceneSwitcher.instance.getScene("Login"));
    }
}
