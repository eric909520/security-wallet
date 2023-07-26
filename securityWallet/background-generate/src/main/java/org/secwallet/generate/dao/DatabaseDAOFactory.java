package org.secwallet.generate.dao;


import org.secwallet.generate.constants.DBMSConstants;
import org.secwallet.generate.dao.impl.CommonDatabaseDAOImpl;
import org.secwallet.generate.dao.impl.MySql5DatabaseDAO;
import org.secwallet.generate.utils.DatabaseType;
import org.secwallet.generate.vo.ConnParam;

/**
 * Queryer generation factory
 */
public class DatabaseDAOFactory {

	public static IDatabaseDAO getDAO(ConnParam connParam) {
		String upperCaseDbName = connParam.getDbType().toUpperCase();
		
		if (upperCaseDbName.contains(DBMSConstants.ORACLE)) {
			return new CommonDatabaseDAOImpl(connParam, DatabaseType.Oracle);
		}  else if (upperCaseDbName.contains(DBMSConstants.SQL_SERVER) || upperCaseDbName.contains(DBMSConstants.SQLSERVER)) {
			return new CommonDatabaseDAOImpl(connParam, DatabaseType.MSSQLServer);
		} else if (upperCaseDbName.contains(DBMSConstants.MYSQL)) {
			return new MySql5DatabaseDAO(connParam, DatabaseType.MySql5);
//		} else if (upperCaseDbName.contains(DBMSConstants.SYBASE)) {
//
//		} else if (upperCaseDbName.contains(DBMSConstants.POSTGRE_SQL)) {
//			
//		} else if (upperCaseDbName.contains(DBMSConstants.DB2)) {
//			
//		} else if (upperCaseDbName.contains(DBMSConstants.HSQLDB)) {
//			
//		} else if (upperCaseDbName.contains((DBMSConstants.FIREBIRD)) {
//			
//		} else if (upperCaseDbName.contains(DBMSConstants.DERBY)) {
//			
		} else {
			
		}
		return new MySql5DatabaseDAO(connParam, DatabaseType.MySql5);
	}
}
