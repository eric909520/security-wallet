//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package org.secwallet.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class AesUtil {
    public static final String PASSWORD = "";
    private static final String ALGORITHMSTR = "AES/CBC/PKCS5Padding";
    private static final String IV_STRING = "";

    public AesUtil() {
    }

    public static String aesDecryptCbc2(String encrypt,String key) {
        try {
            return aesDecryptCbc(encrypt, key);
        } catch (Exception var2) {
            throw new RuntimeException("Decryption error");
        }
    }
    public static String aesEncryptCbc2(String encrypt,String key) {
        try {
            return aesEncrypt(encrypt, key);
        } catch (Exception var2) {
            throw new RuntimeException("Decryption error");
        }
    }

    public static String aesDecryptCbc(String encrypt) {
        try {
            return aesDecryptCbc(encrypt, PASSWORD);
        } catch (Exception var2) {
            throw new RuntimeException("Decryption error");
        }
    }

    public static String aesEncrypt(String content) {
        try {
            return aesEncrypt(content, PASSWORD).replaceAll("\r\n", "").replaceAll("\n", "");
        } catch (Exception var2) {
            var2.printStackTrace();
            return "";
        }
    }

    private static String base64Encode(byte[] bytes) {
//        return (new BASE64Encoder()).encode(bytes);
        return Base64.encodeBase64String(bytes);
    }

    private static byte[] base64Decode(String base64Code) throws Exception {
//        return (new BASE64Decoder()).decodeBuffer(base64Code);
        return Base64.decodeBase64(base64Code);
    }

    private static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        byte[] initParam = IV_STRING.getBytes();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, new SecretKeySpec(encryptKey.getBytes(), "AES"), ivParameterSpec);
        return cipher.doFinal(content.getBytes("utf-8"));
    }

    /**
     * BASE64 encryption
     * @param clearText plaintext, the content to be encrypted
     * @param password password, encrypted password
     * @return returns the ciphertext, the content obtained after encryption. Encryption error returns null
     */
    public static String encryptBase64(String clearText, String password) {
        try {
            // 1 Get encrypted ciphertext byte array
            byte[] cipherTextBytes = encrypt(clearText.getBytes("UTF-8"), pwdHandler(password));

            // 2 Perform BASE64 encoder on the ciphertext byte array to get the ciphertext output by BASE6
//            BASE64Encoder base64Encoder = new BASE64Encoder();
//            String cipherText = base64Encoder.encode(cipherTextBytes);
            String cipherText = Base64.encodeBase64String(cipherTextBytes);

            // 3 Returns the ciphertext of the BASE64 output
            return cipherText;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Encryption error returns null
        return null;
    }

    /**
     * Original encryption
     * @param clearTextBytes plaintext byte array, byte array to be encrypted
     * @param pwdBytes encrypted password byte array
     * @return returns the encrypted ciphertext byte array, and returns null for encryption errors
     */
    public static byte[] encrypt(byte[] clearTextBytes, byte[] pwdBytes) {
        try {
            // 1 Get encryption key
            SecretKeySpec keySpec = new SecretKeySpec(pwdBytes, "AES");

            // 2 Get Cipher instance
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            // View the number of bits in the data block The default is 16 (byte) * 8 = 128 bit

            // 3 Initialize the Cipher instance. Set the execution mode and encryption key
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            // 4 implement
            byte[] cipherTextBytes = cipher.doFinal(clearTextBytes);

            // 5 Returns the ciphertext character set
            return cipherTextBytes;

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] pwdHandler(String password) throws UnsupportedEncodingException {
        byte[] data = null;
        if (password != null) {
            byte[] bytes = password.getBytes("UTF-8");
            if (password.length() < 16) {
                System.arraycopy(bytes, 0, data = new byte[16], 0, bytes.length);
            } else {
                data = bytes;
            }
        }
        return data;
    }

    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    private static String aesDecryptByBytesCbc(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(decryptKey.getBytes());
        kgen.init(128,secureRandom);
        byte[] initParam = IV_STRING.getBytes();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(2, new SecretKeySpec(decryptKey.getBytes(), "AES"), ivParameterSpec);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    public static String aesDecryptCbc(String encryptStr, String decryptKey) throws Exception {
        return aesDecryptByBytesCbc(base64Decode(encryptStr), decryptKey);
    }

    private static HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        Map appVersionMap = new HashMap<String ,String>(){{
            put("appVersion","235");
            put("os","235");
        }};
        headers.add("Content-Type","application/json");
        headers.add("version-info", new JSONObject(appVersionMap).toJSONString());
        return headers;
    }


    private static void testMonitorSwitch(){
        RestTemplate restTemplate = new RestTemplate();
        JSONObject jsonObject = new JSONObject();
        Map<String, String> map = new HashMap<>();
        map.put("status","0");
        String encrptStr = aesEncryptCbc2(JSON.toJSONString(map), PASSWORD);
        jsonObject.put("object", encrptStr);
        HttpHeaders headers = getHeaders();

        HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(jsonObject,headers);

        String result = restTemplate.postForObject("", requestEntity, String.class);
        System.out.println("result:" + result.toString());
    }
}