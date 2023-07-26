package org.secwallet.generate.exception;

/**
 * DAO operation is abnormal
 */
public class DAOException extends Exception {
    
    /**
     * 
     */
    private static final long serialVersionUID = -1041464715086167865L;

    public static final int OPEN_CONNECTION_EXCEPTION = 1;
    public static final int CLOSE_CONNECTION_EXCEPTION = 2;
    public static final int CLOSE_JDBC_EXCEPTION = 3;
    public static final int QUERY_EXCEPTION = 8;
    public static final int QUERY_TABLE_EXCEPTION = 9;
    public static final int QUERY_COLUMN_EXCEPTION = 10;
    public static final int QUERY_PRIMARY_KEY_EXCEPTION = 11;
    public static final int QUERY_FOREIGN_KEY_EXCEPTION = 12;
    public static final int QUERY_INDEX_EXCEPTION = 13;
    public static final int QUERY_TRIGGER_EXCEPTION = 14;
    
    private final int type;

    public DAOException(int type, String message, Throwable cause) {
        super(message, cause);
        this.type = type;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        String errorMsg = null;
        switch (type) {
        case OPEN_CONNECTION_EXCEPTION:
            errorMsg = "Failed to create database connection：";
            break;
        case CLOSE_CONNECTION_EXCEPTION:
            errorMsg = "Failed to close database connection：";
            break;
        case CLOSE_JDBC_EXCEPTION:
            errorMsg = "Error closing jdbc resource：";
            break;
        default:
            errorMsg = "query error：";
        }
        return errorMsg + getMessage();
    }
}
