package school.sptech.crudrisecanvas.utils;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailConfig {
    private Properties prop = new Properties();

    public EmailConfig() {
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.office365.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.ssl.trust", "smtp.office365.com");
    }

    public Session getSession() {
        return Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("", "");
            }
        });
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            Message emailMessage = new MimeMessage(getSession());
            emailMessage.setFrom(new InternetAddress(""));
            emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            emailMessage.setSubject(subject);

            MimeBodyPart emailText = new MimeBodyPart();
            emailText.setContent(body, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(emailText);

            emailMessage.setContent(multipart);
            Transport.send(emailMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}