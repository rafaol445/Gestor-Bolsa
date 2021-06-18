package es.florida;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class EmailService {

    public void SendEmail(String dest, String subject, String text) throws EmailException {
        Email email = new SimpleEmail();
        email.setHostName("Localhost");
        email.setSmtpPort(1025);
        email.setFrom("rol19991@gmail.com");
        email.setSubject(subject);
        email.setMsg(text);
        email.addTo(dest);
        email.send();
        }
    }






