package org.secwallet.core.util.security;



import java.util.UUID;

/**
 * password encryption
 */
public class EncryptUtil {

    /**
     * match password
     * @param salt
     * @param rawPass plaintext
     * @param encPass ciphertext
     * @return
     */
    public static boolean matches(String salt, String rawPass, String encPass) {
        return new PasswordEncoder(salt).matches(encPass, rawPass);
    }

    /**
     * plaintext password encryption
     * @param rawPass plaintext
     * @param salt
     * @return
     */
    public static String encode(String rawPass, String salt) {
        return new PasswordEncoder(salt).encode(rawPass);
    }

    /**
     * get encryption salt
     * @return
     */
    public static String getSalt() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
    }
}
