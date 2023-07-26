package org.secwallet.generate.dao;


import org.secwallet.generate.exception.DAOException;
import org.secwallet.generate.model.*;

import java.sql.Connection;
import java.util.List;
import java.util.Map;


/**
 * Database metadata query interface
 */
public interface IDatabaseDAO {
	
    /**
     * @param sql
     * @param params
     * @return
     * @throws DAOException
     */
	List<Map<String, String>> query(String sql, String[] params) throws DAOException;
	
	/**
     * @return
     * @throws DAOException
     */
	List<Table> getTables() throws DAOException;
	
	/**
     * @param tableName
     * @return
     * @throws DAOException
     */
	List<Column> getColumns(String tableName) throws DAOException;
	
	/**
     * @param tableName
     * @return
     * @throws DAOException
     */
	List<PrimaryKey> getPrimaryKeys(String tableName) throws DAOException;
	
	/**
     * @param tableName
     * @return
     * @throws DAOException
     */
	List<ForeignKey> getForeignKeys(String tableName) throws DAOException;
	
	/**
     * @return
     * @throws DAOException
     */
	List<Index> getIndexes(String tableName) throws DAOException;
	
	/**
     * @param tableName
     * @return
     * @throws DAOException
     */
	List<Trigger> getTriggers(String tableName) throws DAOException;
	
	/**
     *
	 * Open database connection
     */
	Connection openConnection() throws DAOException;
	
	/**
     * close database connection
     */
	void closeConnection() throws DAOException;
}
