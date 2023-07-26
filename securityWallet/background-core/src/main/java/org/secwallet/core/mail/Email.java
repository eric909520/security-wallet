package org.secwallet.core.mail;

import lombok.Data;

import java.util.HashMap;

@Data
public class Email {

    private String[]email;

    private String subject;

    private String content;

    private String contentType;

    private String template;

    private HashMap<String,String> kvMap;
}
