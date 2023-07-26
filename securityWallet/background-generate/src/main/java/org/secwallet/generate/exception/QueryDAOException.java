package org.secwallet.generate.exception;

/**
 * DAO query operation is abnormal
 */
public class QueryDAOException extends DAOException {

    /**
     * 
     */
    private static final long serialVersionUID = 970988278493482388L;

    private final String tableName;

    /**
     * @param type
     * @param message
     * @param cause
     */
    public QueryDAOException(String tableName, int type, String message,
            Throwable cause) {
        super(type, message, cause);
        this.tableName = tableName;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    @Override
    public String toString() {
        String typeStr = getTypeString();
        if (typeStr != null) {
            return String.format("Error in querying %s information in table [%s]. The reason for the error is: %s.", tableName, typeStr, getMessage());
        }
        
        return super.toString();
    }

    private String getTypeString() {
        switch (getType()) {
        case QUERY_COLUMN_EXCEPTION:
            return "field";
        case QUERY_PRIMARY_KEY_EXCEPTION:
            return "primary key";
        case QUERY_FOREIGN_KEY_EXCEPTION:
            return "foreign key";
        case QUERY_INDEX_EXCEPTION:
            return "index";
        case QUERY_TRIGGER_EXCEPTION:
            return "trigger";
        default:
            return null;
        }
    }
}
