package model.AppReminder;

import model.App;
import model.Database.MongoManager;
import model.Logging.Logger;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

public class ActivityReminder {

        public static AppReminder appReminder = new AppReminder();
        public static ActivityReminder activityReminder = new ActivityReminder();
        private MongoManager.Day today;

        private ActivityReminder(){
                today = App.mongoManager.getDay();
                displayTray();
        }


        public void displayTray() {
                SystemTray tray = SystemTray.getSystemTray();

                Image image = Toolkit.getDefaultToolkit().createImage("icon.png");

                TrayIcon trayIcon = new TrayIcon(image, " Demo");
                trayIcon.setImageAutoSize(true);
                trayIcon.setToolTip("demo");
                try {
                        tray.add(trayIcon);
                } catch (AWTException e) {
                        Logger.logException(e);
                }

                trayIcon.displayMessage("MySport", " Activity will start 1 Hour before: " , MessageType.INFO);
        }
}