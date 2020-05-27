package model.PasswordUnmasking;

import javafx.scene.control.PasswordField;


public class PeekablePasswordField extends PasswordField {

    public PeekablePasswordField() {
        setSkin(new PeekablePasswordFieldSkin(this));
    }    

}
