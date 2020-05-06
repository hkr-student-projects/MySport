package model.Mailer;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Mailer {

    public static void sendMail(String recipients, String textMessage, String subject) {

        final String username = "mysport.hkr@gmail.com";
        final String password = "Pa55W0rd";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("mysport.hkr@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    //InternetAddress.parse("petrus.daniel@protonmail.com")
                    InternetAddress.parse(recipients)
            );
            message.setSubject(subject);
            message.setText(textMessage);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
