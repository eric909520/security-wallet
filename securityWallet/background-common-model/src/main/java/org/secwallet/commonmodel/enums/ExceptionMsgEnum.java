package org.secwallet.commonmodel.enums;

/**
 * Exception return prompt information enumeration class
 */
public enum ExceptionMsgEnum {
    common("，", 11),
    customer("，", 11),
    maintain("，", 11),
    frequently("", 11),
    illegal(" ", 401),
    param("", 11),
    ;

    private String message;

    private int code;

    ExceptionMsgEnum(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

}
