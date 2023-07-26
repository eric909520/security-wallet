package org.secwallet.generate.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils {

	private static Pattern linePattern = Pattern.compile("_(\\w)");
	private static Pattern humpPattern = Pattern.compile("[A-Z]");


	public static boolean isEmpty(String value) {
		return value == null || "".equals(value) || "null".equals(value) || "undefined".equals(value);
	}

	/**
	 * Underscore to CamelCase
	 * 
	 * @param str
	 * @return
	 */
	public static String lineToHump(String str) {
		str = str.toLowerCase();
		Matcher matcher = linePattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * Hump ​​to underline, the efficiency is higher than the above
	 * 
	 * @param str
	 * @return
	 */
	public static String humpToLine(String str) {
		Matcher matcher = humpPattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * Convert the first letter to lowercase
	 * @param str
	 * @return
	 */
	public static String uncapitalize(String str) {
		if (Character.isLowerCase(str.charAt(0)))
			return str;
		else
			return (new StringBuilder()).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();
	}

	/**
	 * Capitalize the first letter
	 * @param str
	 * @return
	 */
	public static String capitalize(String str) {
		if (Character.isUpperCase(str.charAt(0)))
			return str;
		else
			return (new StringBuilder()).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).toString();
	}
}
