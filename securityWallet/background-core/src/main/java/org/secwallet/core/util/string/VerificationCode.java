package org.secwallet.core.util.string;

import org.apache.commons.lang3.RandomStringUtils;

public class VerificationCode {
    // Identification ID Mobile phone number/email address/graphic verification code ID
    private String idKey;

    // verification code
    private String verificationCode;

    //Timeout time (ms)
    private long timeout = 1000 * 60 * 5;

    //Verification code digits
    private int digit = 6;

    // number of repetitions allowed
    private int attempts = 3;

    //refresh time
    private long flushedDate = System.currentTimeMillis();

    public void flushVerificationCode(){
        this.verificationCode = RandomStringUtils.randomNumeric(this.digit);
    }

    public boolean isRepeatedlySent(long cycle){
        return System.currentTimeMillis() - cycle < this.flushedDate;
    }

    public boolean isTimeOut(){
        return System.currentTimeMillis() -this.timeout > this.flushedDate;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public int getDigit() {
        return digit;
    }

    public void setDigit(int digit) {
        this.digit = digit;
    }

    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getVerificationCode() {
        if(this.verificationCode == null){
            this.verificationCode = RandomStringUtils.randomNumeric(this.digit);
        }
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public long getFlushedDate() {
        return flushedDate;
    }

    public void setFlushedDate(long flushedDate) {
        this.flushedDate = flushedDate;
    }
}
