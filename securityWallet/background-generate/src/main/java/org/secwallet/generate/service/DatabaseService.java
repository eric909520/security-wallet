package org.secwallet.generate.service;

import org.secwallet.generate.dao.DatabaseDAOFactory;
import org.secwallet.generate.dao.IDatabaseDAO;
import org.secwallet.generate.exception.DAOException;
import org.secwallet.generate.vo.ConnParam;
import org.secwallet.generate.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Database metadata query service implementation class
 */
@Service
public class DatabaseService {


	public List<Map<String, String>> query(ConnParam connParam, String sql, String[] params) {
		List<Map<String, String>> maps = new ArrayList<Map<String, String>>();
		if(connParam == null) {
			return maps;
		}
		try {
		    IDatabaseDAO dao = DatabaseDAOFactory.getDAO(connParam);
		    long start = System.currentTimeMillis();
		    dao.openConnection();
		    maps = dao.query(sql, params);
		    dao.closeConnection();
		    long end = System.currentTimeMillis();
		    System.out.println("Time-consuming to retrieve database table information in reverse：" + (end - start) + "ms");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return maps;
	}


	public List<Table> getTables(ConnParam connParam) {
		List<Table> tables = new ArrayList<Table>();
		if(connParam == null) {
			return tables;
		}
		try {
		    IDatabaseDAO dao = DatabaseDAOFactory.getDAO(connParam);
		    long start = System.currentTimeMillis();
		    dao.openConnection();
		    tables = dao.getTables();
		    dao.closeConnection();
		    long end = System.currentTimeMillis();
		    System.out.println("Time-consuming to retrieve database table information in reverse：" + (end - start) + "ms");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tables;
	}


	public List<Column> getColumns(ConnParam connParam, String tableName) {
		List<Column> columns = new ArrayList<Column>();
		if(connParam == null) {
			return columns;
		}
		try {
		    IDatabaseDAO dao = DatabaseDAOFactory.getDAO(connParam);
		    dao.openConnection();
		    columns = dao.getColumns(tableName);
		    dao.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return columns;
	}


	public List<PrimaryKey> getPrimaryKeys(ConnParam connParam, String tableName) {
		List<PrimaryKey> primaryKeys = new ArrayList<PrimaryKey>();
		if(connParam == null) {
			return primaryKeys;
		}
		try {
		    IDatabaseDAO dao = DatabaseDAOFactory.getDAO(connParam);
		    dao.openConnection();
		    primaryKeys = dao.getPrimaryKeys(tableName);
		    dao.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return primaryKeys;
	}


	public List<ForeignKey> getForeignKeys(ConnParam connParam, String tableName) {
		List<ForeignKey> foreignKeys = new ArrayList<ForeignKey>();
		if(connParam == null) {
			return foreignKeys;
		}
		try {
		    IDatabaseDAO dao = DatabaseDAOFactory.getDAO(connParam);
		    dao.openConnection();
		    foreignKeys = dao.getForeignKeys(tableName);
		    dao.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return foreignKeys;
	}


	public List<Index> getIndexes(ConnParam connParam, String tableName) {
		List<Index> indexes = new ArrayList<Index>();
		if(connParam == null) {
			return indexes;
		}
		try {
		    IDatabaseDAO dao = DatabaseDAOFactory.getDAO(connParam);
		    dao.openConnection();
		    indexes = dao.getIndexes(tableName);
		    dao.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return indexes;
	}


	public List<Trigger> getTriggers(ConnParam connParam, String tableName) {
		List<Trigger> trigger = new ArrayList<Trigger>();
		if(connParam == null) {
			return trigger;
		}
		try {
		    IDatabaseDAO dao = DatabaseDAOFactory.getDAO(connParam);
		    dao.openConnection();
		    trigger = dao.getTriggers(tableName);
		    dao.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return trigger;
	}


	public boolean canConnect(ConnParam connParam) {
		IDatabaseDAO dao = DatabaseDAOFactory.getDAO(connParam);
		if (dao == null) {
			return false;
		}
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
		return false;
	}
	
}
