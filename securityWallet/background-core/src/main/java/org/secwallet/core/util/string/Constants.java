package org.secwallet.core.util.string;

public class Constants {
    /**
     * file separator
     */
    public static final String SF_FILE_SEPARATOR = System.getProperty("file.separator");
    /**
     * line separator
     */
    public static final String SF_LINE_SEPARATOR = System.getProperty("line.separator");
    /**
     * path separator
     */
    public static final String SF_PATH_SEPARATOR = System.getProperty("path.separator");
    public static final String THREAD_LOCAL_USER_KEY = "THREAD_LOCAL_USER_KEY";
    public static final String REQUEST_UUID = "REQUEST_UUID";
    public static final String MATCH_BY_DISTANCE = "1";
    public static final Integer INT_NO = 0;
    public static final Integer INT_YES = 1;
    /**
     * The order user IP of the order
     */
    public static final String REDIS_KEY_ORDER_LIMIT_USER_IP = "ORDER_OF_USER_IP:";
    /**
     * Too many orders placed
     */
    public static final String REDIS_KEY_ORDER_LIMIT_LOCK = "ORDER_LIMIT_LOCK_USER_IP:";
    /**
     * userIP order collection
     */
    public static final String REDIS_KEY_ORDER_FAILED_IN_REDIS = "ORDER_LIMIT_FAILED_USER_IP:";

    public static final String REDIS_KEY_CUSTOM_ORDER = "BEHALF_CUSTOM_ORDER:";


}
