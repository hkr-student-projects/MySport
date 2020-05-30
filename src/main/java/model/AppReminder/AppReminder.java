package model.AppReminder;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

public class AppReminder {

    public void displayTray() throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();

        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");

        TrayIcon trayIcon = new TrayIcon(image, " Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("demo");
        tray.add(trayIcon);

        trayIcon.displayMessage("MySport", " New Activity Created:" , MessageType.INFO);
    }
}

