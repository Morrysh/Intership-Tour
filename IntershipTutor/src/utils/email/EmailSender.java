package utils.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/*
 * 
 * @author Stefano Martella
 * 
 * This class is used to send emails.
 * 
 */
public class EmailSender {
	
	private final static String USERNAME = System.getenv("email");
	private final static String PASSWORD = System.getenv("password");
	
	public static void sendEmail(String messageTitle, String messageBody, String[] recipientsEmails) throws MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        
        InternetAddress[] recipients = new InternetAddress [recipientsEmails.length];
        
        for(int i = 0; i < recipientsEmails.length; i++) {
        	recipients[i] = new InternetAddress(recipientsEmails[i]);
        }
        
        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
          });
        
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("intershiptutorunivaq@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, recipients);
        message.setSubject(messageTitle);
        message.setText(messageBody);

        Transport.send(message);
	}

}
