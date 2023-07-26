package org.secwallet.core.mail;

public interface MailService {

    public void send(Email mail) throws Exception;

    public void sendHtml(Email mail) throws Exception;

    public void sendFreemarker(Email mail) throws Exception;


    public void sendThymeleaf(Email mail) throws Exception;


    public void sendQueue(Email mail) throws Exception;


    public void sendRedisQueue(Email mail) throws Exception;
}
