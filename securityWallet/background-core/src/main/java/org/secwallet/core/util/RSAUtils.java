package org.secwallet.core.util;

import cn.hutool.core.codec.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
  
/**
 * <p>
 * RSA public/private key/signature toolkit
 * </p>
 * <p>
 * Ron [R]ivest, Adi [S]hamir and Leonard [A]dleman
 * </p>
 * <p>
 * Keys in string format are in BASE64 encoding format unless otherwise specified<br/>
 * Due to the extremely slow speed of asymmetric encryption, general files do not use it to encrypt but use symmetric encryption,<br/>
 * The asymmetric encryption algorithm can be used to encrypt the symmetric encryption key, so as to ensure the security of the key and the security of the data
 * </p>
 * @date 2018-09-23
 */
public class RSAUtils {  
  
    /**
     * Encryption Algorithm RSA
     */  
    public static final String KEY_ALGORITHM = "RSA";  
      
    /**
     * Signature algorithm
     */  
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";  
  
    /**
     * Get the key of the public key
     */  
    public static final String PUBLIC_KEY = "RSAPublicKey";  
      
    /**
     * Get the key of the private key
     */  
    public static final String PRIVATE_KEY = "RSAPrivateKey";  
      
    /**
     * RSA maximum encrypted plaintext size
     */  
    public static final int MAX_ENCRYPT_BLOCK = 117;  
      
    /**
     * RSA maximum decrypted ciphertext size
     */  
    public static final int MAX_DECRYPT_BLOCK = 128;  
  
    /**
     * <p> 
     * Generate key pair (public and private key)
     * </p> 
     *  
     * @return 
     * @throws Exception 
     */  
    public static Map<String, Object> genKeyPair() throws Exception {  
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);  
        keyPairGen.initialize(1024);  
        KeyPair keyPair = keyPairGen.generateKeyPair();  
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();  
        Map<String, Object> keyMap = new HashMap<String, Object>(2);  
        keyMap.put(PUBLIC_KEY, publicKey);  
        keyMap.put(PRIVATE_KEY, privateKey);  
        return keyMap;  
    }  
      
    /**
     * <p> 
     * Generate digital signature on message with private key
     * </p> 
     *  
     * @param data encrypted data
     * @param privateKey Private key (BASE64 encoded)
     *  
     * @return 
     * @throws Exception 
     */  
    public static String sign(byte[] data, String privateKey) throws Exception {  
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);  
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);  
        signature.initSign(privateK);  
        signature.update(data);  
        return Base64.encode(signature.sign());
    }  
  
    /**
     * <p> 
     * Verify digital signature
     * </p> 
     *  
     * @param data encrypted data
     * @param publicKey Public key (BASE64 encoded)
     * @param sign digital signature
     *  
     * @return 
     * @throws Exception 
     *  
     */  
    public static boolean verify(byte[] data, String publicKey, String sign)  
            throws Exception {  
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        PublicKey publicK = keyFactory.generatePublic(keySpec);  
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);  
        signature.initVerify(publicK);  
        signature.update(data);  
        return signature.verify(Base64.decode(sign));
    }  
  
    /**
     * <P> 
     * private key decryption
     * </p> 
     *  
     * @param encryptedData encrypted data
     * @param privateKey Private key (BASE64 encoded)
     * @return 
     * @throws Exception 
     */  
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)  
            throws Exception {  
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, privateK);  
        int inputLen = encryptedData.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {  
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);
            i++;  
            offSet = i * MAX_DECRYPT_BLOCK;  
        }  
        byte[] decryptedData = out.toByteArray();  
        out.close();  
        return decryptedData;  
    }  
  
    /**
     * <p> 
     * public key decryption
     * </p> 
     *  
     * @param encryptedData
     * @param publicKey
     * @return 
     * @throws Exception 
     */  
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)  
            throws Exception {  
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key publicK = keyFactory.generatePublic(x509KeySpec);  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, publicK);  
        int inputLen = encryptedData.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {  
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_DECRYPT_BLOCK;  
        }  
        byte[] decryptedData = out.toByteArray();  
        out.close();  
        return decryptedData;  
    }  
  
    /**
     * <p> 
     * public key encryption
     * </p> 
     *  
     * @param data
     * @param publicKey
     * @return 
     * @throws Exception 
     */  
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)  
            throws Exception {  
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key publicK = keyFactory.generatePublic(x509KeySpec);  
        // 对数据加密  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, publicK);  
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {  
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(data, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_ENCRYPT_BLOCK;  
        }  
        byte[] encryptedData = out.toByteArray();  
        out.close();  
        return encryptedData;  
    }  
  
    /**
     * <p> 
     * private key encryption
     * </p> 
     *  
     * @param data
     * @param privateKey
     * @return 
     * @throws Exception 
     */  
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)  
            throws Exception {  
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, privateK);  
        int inputLen = data.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {  
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(data, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_ENCRYPT_BLOCK;  
        }  
        byte[] encryptedData = out.toByteArray();  
        out.close();  
        return encryptedData;  
    }  
  
    /**
     * <p> 
     * get private key
     * </p> 
     *  
     * @param keyMap
     * @return 
     * @throws Exception 
     */  
    public static String getPrivateKey(Map<String, Object> keyMap)  
            throws Exception {  
        Key key = (Key) keyMap.get(PRIVATE_KEY);  
        return Base64.encode(key.getEncoded());
    }  
  
    /**
     * <p> 
     * get public key
     * </p> 
     *  
     * @param keyMap
     * @return 
     * @throws Exception 
     */  
    public static String getPublicKey(Map<String, Object> keyMap)  
            throws Exception {  
        Key key = (Key) keyMap.get(PUBLIC_KEY);  
        return Base64.encode(key.getEncoded());
    }  
    
    /**
     * read key file
     * @param filePath
     * @param charSet
     * @return
     * @throws Exception
     */
    public static String readFile(String filePath, String charSet) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        try {
            FileChannel fileChannel = fileInputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) fileChannel.size());
            fileChannel.read(byteBuffer);
            byteBuffer.flip();
            String keySet = new String(byteBuffer.array(), charSet);
            if (keySet.contains("-----BEGIN PRIVATE KEY-----")) {
            	return keySet.replaceAll("-----\\w+ PRIVATE KEY-----", "");
            }else if (keySet.contains("-----BEGIN PUBLIC KEY-----")) {
            	return keySet.replaceAll("\\-{5}[\\w\\s]+\\-{5}[\\r\\n|\\n]", "");
			}
            return new String(byteBuffer.array(), charSet);
        } finally {
            fileInputStream.close();
        }
    }

}  