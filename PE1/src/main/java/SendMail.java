import es.florida.EmailService;
import org.apache.commons.mail.EmailException;

public class SendMail implements Runnable {

    private final String email;
    private final String subject;
    private final String text;

    public SendMail(String email, String subject, String text) {
        this.email = email;
        this.subject = subject;
        this.text = text;
    }

    @Override
    public void run() {
        EmailService emailService = new EmailService();
        try {
            emailService.SendEmail(email, subject, text);
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }
}
