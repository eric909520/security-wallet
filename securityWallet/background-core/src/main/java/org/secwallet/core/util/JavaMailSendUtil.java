package org.secwallet.core.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

/**
 * send mail tool
 */
public class JavaMailSendUtil {

    /**
     *
     * @param content
     * @param title
     * @param sendMail
     */
    public static void sendMailtoManage(String content,String title,String sendMail) throws  Exception{
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // Get a Properties object

        Properties props = System.getProperties();

        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");

        final String username = "";
        final String password = "";

        Session session = Session.getDefaultInstance(props, new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // -- Create a new message --
        Message msg = new MimeMessage(session);

        // -- Set the FROM and TO fields --
        msg.setFrom(new InternetAddress(username));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sendMail, false));
        msg.setSubject(title);
        msg.setText(content);
        msg.setSentDate(new Date());
        Transport.send(msg);

        System.out.println("Message sent.");
    }

}
