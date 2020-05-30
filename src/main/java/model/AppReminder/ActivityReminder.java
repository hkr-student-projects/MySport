package model.AppReminder;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

public class ActivityReminder {

        public static AppReminder appReminder = new AppReminder();
        public static ActivityReminder activityReminder = new ActivityReminder();


        public void displayTray() throws AWTException {
                SystemTray tray = SystemTray.getSystemTray();

                Image image = Toolkit.getDefaultToolkit().createImage("icon.png");

                TrayIcon trayIcon = new TrayIcon(image, " Demo");
                trayIcon.setImageAutoSize(true);
                trayIcon.setToolTip("demo");
                tray.add(trayIcon);

                trayIcon.displayMessage("MySport", " Activity will start 1 Hour before: " , MessageType.INFO);
        }
}