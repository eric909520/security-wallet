package org.secwallet.core.model;


public enum ResultCode {
    SUCCESS(0, "success"),
    FAIL(11, "failure"),
    UNSPECIFIED(500, ""),
    ILLEGAL(401, ""),
    /* Parameter error：1001-1999 */
    PARAM_IS_INVALID(1001, ""),
    PARAM_IS_BLANK(1002, ""),

    /* System error：4001-4999 */
    SYSTEM_INNER_ERROR(4001, ""),
    FILE_NOT_FOUND(4002, ""),
    FILE_UPLOAD_NOT_ALLOWED(4003, ""),
    FILE_UPLOAD_OVER_SIZE(4004, ""),
    RESULT_DATA_NONE(5001, ""),
    DATA_IS_WRONG(5002, ""),
    DATA_ALREADY_EXISTED(5003, ""),
    DUPLICATE_KEY(5003, ""),
    DATA_ALREADY_CHANGED(5004, ""),
    /* permission error：7001-7999 */
    PERMISSION_NO_ACCESS(7001, ""),
    IP_NO_ACCESS(7002, ""),
    @Deprecated
    USE_PROXY(305, "Use Proxy"),
    TEMPORARY_REDIRECT(307, "Temporary Redirect"),
    PERMANENT_REDIRECT(308, "Permanent Redirect"),
    BAD_REQUEST(400, "Bad Request"),
    PAYMENT_REQUIRED(402, "Payment Required"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
    REQUEST_TIMEOUT(408, "Request Timeout"),
    CONFLICT(409, "Conflict"),
    GONE(410, "Gone"),
    LENGTH_REQUIRED(411, "Length Required"),
    PRECONDITION_FAILED(412, "Precondition Failed"),
    PAYLOAD_TOO_LARGE(413, "Payload Too Large"),

    ;

    private int code;

    private String msg;

    private boolean status;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public boolean getStatus() {
        return status;
    }
}
