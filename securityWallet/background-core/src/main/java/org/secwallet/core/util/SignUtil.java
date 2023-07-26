package org.secwallet.core.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class SignUtil {
    public static final String MD5 = "MD5";
    public static final String HMAC_SHA256 = "HMAC-SHA256";

    /**
     * sign
     * @param data
     * @param key
     * @param signType
     * @return
     */
    public static String sign(Map data, String key, String signType){
        if(null == data || data.size() == 0 || null == key || key.trim().equals("") || null == signType || !(MD5.equals(signType) || HMAC_SHA256.equals(signType))){
            throw new RuntimeException("param miss");
        }
        // 签名值
        String signKey = "";
        SortedMap sortedMap = new TreeMap<>(data);
        String paramStr = "";
        for (Object entry: data.keySet()) {
            paramStr += "&" +entry + "=" + data.get(entry);
        }
        for (Object entry: data.keySet()) {
            data.get(entry);
        }
        paramStr = paramStr + key;
        if(signType.equals(MD5)){
            signKey = getMD5(paramStr.trim());
        }else{
            signKey = getHmacSha256(paramStr,key);
        }
        return signKey;
    }

    public static String signForTelephoneFee(Map data, String key, String signType){
        if(null == data || data.size() == 0 || null == key || key.trim().equals("") || null == signType || !(MD5.equals(signType) || HMAC_SHA256.equals(signType))){
            throw new RuntimeException("param miss");
        }
        // sign value
        String signKey = "";
        StringBuilder paramStr = new StringBuilder();
        for (Object entry: data.keySet()) {
            paramStr.append("&").append(entry).append("=").append(data.get(entry));
        }
        paramStr.deleteCharAt(0);
        if(signType.equals(MD5)){
            signKey = getMD5(paramStr.toString().trim());
        }else{
            signKey = getHmacSha256(paramStr.toString(),key);
        }
        return signKey;
    }
    /**
     * get MD5
     * @param str
     * @return
     */
    private static String getMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return byteArrayToHexString(md.digest());
        } catch (Exception e) {
            throw new RuntimeException("MD5 encryption error");
        }
    }

    /**
     * get HmacSha256
     * @param message
     * @param key
     * @return
     */
    private static String getHmacSha256(String message,String key){
        String outPut= null;
        try{
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(),"HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
            outPut = byteArrayToHexString(bytes);
        }catch (Exception e){
            throw new RuntimeException("HmacSHA256 encryption error");
        }
        return outPut;
    }

    /**
     * byte array to hexadecimal string
     * @param b
     * @return
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                sb.append('0');
            sb.append(stmp);
        }
        return sb.toString().toLowerCase();
    }

}