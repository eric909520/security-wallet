package org.secwallet.core.util.security;

import java.io.UnsupportedEncodingException;

/**
 * Base64
 * <br>Function: BASE64 encoding and decoding of strings.
 */
public class Base64 {
    /**
     * Convert string to base64 encoding
     * @param s
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getBase64(String s) throws UnsupportedEncodingException {
        byte[] bytes= org.apache.commons.codec.binary.Base64.encodeBase64(s.getBytes("utf-8"));
        return new String(bytes, "utf-8");

    }

    /**
     * Decode BASE64 encoded string s
     * @param s
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getFromBase64(String s) throws UnsupportedEncodingException {
        byte[] bytes=s.getBytes("GBK");
        byte[] convertBytes=org.apache.commons.codec.binary.Base64.decodeBase64(bytes);
        return new String(convertBytes, "GBK");
    }
}
