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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailConfig {

    private String email;
    private String password;

    private Properties prop = new Properties();

    @Autowired
    public EmailConfig(
            @Value("${recover.email}") String email,
            @Value("${recover.password}") String password,
            @Value("${recover.host}") String host,
            @Value("${recover.port}") String port
    ) {
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.ssl.trust", host);
        this.email = email;
        this.password = password;
    }

    public Session getSession() {
        return Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            Message emailMessage = new MimeMessage(getSession());
            emailMessage.setFrom(new InternetAddress(email));
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