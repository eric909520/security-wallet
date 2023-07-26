package org.secwallet.core.util.security;

import java.security.MessageDigest;


public class PasswordEncoder {

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	private final static String MD5 = "MD5";
	private final static String SHA = "SHA";
	
	private Object salt;
	private String algorithm;

	public PasswordEncoder(Object salt) {
		this(salt, MD5);
	}
	
	public PasswordEncoder(Object salt, String algorithm) {
		this.salt = salt;
		this.algorithm = algorithm;
	}

	/**
	 * password encryption
	 * @param rawPass
	 * @return
	 */
	public String encode(String rawPass) {
		String result = null;
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			// encrypted string
			result = byteArrayToHexString(md.digest(mergePasswordAndSalt(rawPass).getBytes("utf-8")));
		} catch (Exception ex) {
		}
		return result;
	}

	/**
	 * Password match verification
	 * @param encPass ciphertext
	 * @param rawPass plaintext
	 * @return
	 */
	public boolean matches(String encPass, String rawPass) {
		String pass1 = "" + encPass;
		String pass2 = encode(rawPass);

		return pass1.equals(pass2);
	}

	private String mergePasswordAndSalt(String password) {
		if (password == null) {
			password = "";
		}

		if ((salt == null) || "".equals(salt)) {
			return password;
		} else {
			return password + "{" + salt.toString() + "}";
		}
	}

	/**
	 * Convert byte array to hexadecimal string
	 * 
	 * @param b byte array
	 * @return hexadecimal string
	 */
	private String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * convert bytes to hexadecimal
	 * @param b
	 * @return
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

//	public static void main(String[] args) {
//		String salt = "helloworld";
//		PasswordEncoder encoderMd5 = new PasswordEncoder(salt, "MD5");
//		String encode = encoderMd5.encode("test");
//		System.out.println(encode);
//		boolean passwordValid = encoderMd5.validPassword("1bd98ed329aebc7b2f89424b5a38926e", "test");
//		System.out.println(passwordValid);
//
//		PasswordEncoder encoderSha = new PasswordEncoder(salt, "SHA");
//		String pass2 = encoderSha.encode("test");
//		System.out.println(pass2);
//		boolean passwordValid2 = encoderSha.validPassword("1bd98ed329aebc7b2f89424b5a38926e", "test");
//		System.out.println(passwordValid2);
//	}

}