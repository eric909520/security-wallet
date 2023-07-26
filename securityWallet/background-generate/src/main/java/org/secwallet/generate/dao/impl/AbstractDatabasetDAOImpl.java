package org.secwallet.generate.dao.impl;


import org.secwallet.generate.dao.IDatabaseDAO;
import org.secwallet.generate.dao.IMetaDataConverter;
import org.secwallet.generate.exception.DAOException;
import org.secwallet.generate.exception.QueryDAOException;
import org.secwallet.generate.vo.ConnParam;
import org.secwallet.generate.model.*;

import java.sql.*;
import java.util.*;


/**
 * Abstract database metadata query class
 */
public abstract class AbstractDatabasetDAOImpl implements IDatabaseDAO {

	protected IMetaDataConverter converter;
	
	protected ConnParam connParam;
	private Connection connection;

	protected static final String DRIVER = "driver";
	protected static final String URL = "url";
	protected static final String QUERY_TABLE = "query_table";
	protected static final String QUERY_COLUMN = "query_column";
	protected static final String QUERY_INDEX = "query_index";
	protected static final String QUERY_PRIMARY_KEY = "query_primary_key";
	protected static final String QUERY_FOREIGN_KEY = "query_foreign_key";
	protected static final String QUERY_TRIGGER = "query_trigger";

	public AbstractDatabasetDAOImpl(ConnParam connParam) {
		this.connParam = connParam;
	}

	@Override
	public List<Map<String, String>> query(String sql, String[] params)
			throws DAOException {
		if (sql == null) {
			Exception e = new IllegalArgumentException("The entered sql query statement is empty！");
			throw new DAOException(DAOException.QUERY_EXCEPTION, e.getMessage(), e);
		}
		List<Map<String, String>> result = new LinkedList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sql);
			if (params != null) {
				for (int paramIndex = 0; paramIndex < params.length; paramIndex++) {
					pstmt.setString(paramIndex + 1, params[paramIndex]);
				}
			}
			rs = pstmt.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnSize = rsmd.getColumnCount();
			while (rs.next()) {
				Map<String, String> map = new HashMap<>();
				for (int columnIndex = 1; columnIndex <= columnSize; columnIndex++) {
					String columnName = rsmd.getColumnLabel(columnIndex);
					String columnValue = rs.getString(columnName);
					map.put(columnName, columnValue);
				}
				result.add(map);
			}
		} catch (SQLException e) {
			throw new DAOException(DAOException.QUERY_EXCEPTION,
					e.getMessage(), e);
		} finally {
			close(rs, pstmt);
		}
		return result;
	}

	@Override
	public List<Table> getTables() throws DAOException {
		List<Table> tables = new ArrayList<Table>();

		try {
			List<Map<String, String>> result = query(getQuerySql(QUERY_TABLE), null);
			
			for (Map<String, String> map : result) {
				tables.add(converter.convertMap2Table(map));
			}
		} catch (DAOException e) {
			throw new DAOException(DAOException.QUERY_TABLE_EXCEPTION,
					e.getMessage(), e);
		}
		
		return tables;
	}
	
	@Override
	public List<Column> getColumns(String tableName) throws DAOException {
		List<Column> columns = new ArrayList<Column>();
		
		try {
			List<Map<String, String>> result = query(getQuerySql(QUERY_COLUMN),
					new String[] { tableName });
			
			for (Map<String, String> map : result) {
				columns.add(converter.convertMap2Column(map));
			}
		} catch (DAOException e) {
			throw new QueryDAOException(tableName,
					DAOException.QUERY_COLUMN_EXCEPTION, e.getMessage(), e);
		}
		
		return columns;
	}

	@Override
	public List<PrimaryKey> getPrimaryKeys(String tableName)
			throws DAOException {
		List<PrimaryKey> primaryKeys = new ArrayList<PrimaryKey>();
		
		try {
			List<Map<String, String>> result = query(getQuerySql(QUERY_PRIMARY_KEY),
					new String[] { tableName });
			
			for (Map<String, String> map : result) {
				primaryKeys.add(converter.convertMap2PrimaryKey(map));
			}

		} catch (DAOException e) {
			throw new QueryDAOException(tableName,
					DAOException.QUERY_PRIMARY_KEY_EXCEPTION, e.getMessage(), e);
		}
		
		return primaryKeys;
	}

	@Override
	public List<ForeignKey> getForeignKeys(String tableName)
			throws DAOException {
		List<ForeignKey> foreignKeys = new ArrayList<ForeignKey>(0);
		
		try {
			List<Map<String, String>> result = query(getQuerySql(QUERY_FOREIGN_KEY),
					new String[] { tableName });
			
			for (Map<String, String> map : result) {
				foreignKeys.add(converter.convertMap2ForeignKey(map));
			}
		} catch (DAOException e) {
			throw new QueryDAOException(tableName,
					DAOException.QUERY_FOREIGN_KEY_EXCEPTION, e.getMessage(), e);
		}
		
		return foreignKeys;
	}

	@Override
	public List<Index> getIndexes(String tableName) throws DAOException {
		Map<String, Index> indexMap = new HashMap<String, Index>();
		
		try {
			List<Map<String, String>> result = query(getQuerySql(QUERY_INDEX),
					new String[] { tableName });
			for (Map<String, String> map : result) {
				Index index = converter.convertMap2Index(map);
				String indexName = index.getName();
				if (!indexMap.containsKey(indexName)) {
					indexMap.put(indexName, index);
				} else {
					if (!index.getCloumns().isEmpty()) {
						indexMap.get(indexName).addCloumn(index.getCloumns().get(0));
					}
				}
			}
		} catch (DAOException e) {
			throw new QueryDAOException(tableName,
					DAOException.QUERY_INDEX_EXCEPTION, e.getMessage(), e);
		}

		return new LinkedList<Index>(indexMap.values());
	}

	@Override
	public List<Trigger> getTriggers(String tableName) throws DAOException {
		List<Trigger> triggers = new LinkedList<Trigger>();

		try {
			List<Map<String, String>> result = query(getQuerySql(QUERY_TRIGGER),
					new String[] { tableName });
			for (Map<String, String> map : result) {
				triggers.add(converter.convertMap2Trigger(map));
			}
		} catch (DAOException e) {
			throw new QueryDAOException(tableName,
					DAOException.QUERY_TRIGGER_EXCEPTION, e.getMessage(), e);
		}
		
		return triggers;
	}

	@Override
	public Connection openConnection() throws DAOException{
		try {
			closeConnection();

			Class.forName(getDriver());

			Properties props = new Properties();
			props.put("remarksReporting", "true");
			props.put("user", connParam.getUserName());
			props.put("password", connParam.getPassword());
			connection = DriverManager.getConnection(
					getUrl(connParam.getHost(), connParam.getPort(),
							connParam.getDbName()), props);
		} catch (ClassNotFoundException e) {
		    String errorMsg = "Connection creation failed, related driver class not found(" + e.getMessage() + ")";
			throw new DAOException(DAOException.OPEN_CONNECTION_EXCEPTION, errorMsg, e);
		} catch (SQLException e) {
			throw new DAOException(DAOException.OPEN_CONNECTION_EXCEPTION, e.getMessage(), e);
		}
		return connection;
	}

	@Override
	public void closeConnection() throws DAOException {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				throw new DAOException(DAOException.CLOSE_CONNECTION_EXCEPTION,
						e.getMessage(), e);
			}
		}
	}

	/**
     * Get the string description value of the driver class, abstract method, implemented by subclasses
     * 
     * @return
     */
    abstract protected String getDriver() throws DAOException;

    /**
	 * Get the database url to connect to, abstract method, implemented by subclasses
     * 
     * @param host
     *           database address
     * @param port
     *
     * @param dbName
     *		 DB instance name
     * @return
     */
    abstract protected String getUrl(String host, int port, String dbName)
            throws DAOException;

    /**
	 * Get sql query statement
     * @param sqlKey
     * @return
     */
    abstract protected String getQuerySql(String sqlKey) throws DAOException;

    /**
	 * close jdbc resource object
     * @param rs
     * @param stmt
     * @throws DAOException
     */
    protected void close(ResultSet rs, Statement stmt) throws DAOException {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException e) {
                throw new DAOException(DAOException.CLOSE_JDBC_EXCEPTION, e.getMessage(), e);
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                        stmt = null;
                    } catch (SQLException e) {
                        throw new DAOException(DAOException.CLOSE_JDBC_EXCEPTION, e.getMessage(), e);
                    }
                }
            }
        }
    }

    /**
     * @param converter the converter to set
     */
    public void setConverter(IMetaDataConverter converter) {
        this.converter = converter;
    }
}
