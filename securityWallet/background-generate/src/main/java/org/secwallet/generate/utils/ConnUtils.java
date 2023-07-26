package org.secwallet.generate.utils;


import org.secwallet.generate.exception.DAOException;
import org.secwallet.generate.dao.DatabaseDAOFactory;
import org.secwallet.generate.dao.IDatabaseDAO;
import org.secwallet.generate.vo.ConnParam;

/**
 * Connection tool class
 */
public class ConnUtils {

	/**
	 * Test database connection
	 * @param connParam
	 * @return
	 */
	public static boolean testConnection(ConnParam connParam) {
		IDatabaseDAO dao = DatabaseDAOFactory.getDAO(connParam);
		if (dao != null) {
			try {
				dao.openConnection();
				System.out.println("Database connection succeeded!");
				return true;
			} catch (Exception e) {
				System.out.println("Database connection failed, please check the port number, username or password !");
			} finally {
				try {
					dao.closeConnection();
				} catch (DAOException e) {
				}
			}
		}
		return false;
	}
}
