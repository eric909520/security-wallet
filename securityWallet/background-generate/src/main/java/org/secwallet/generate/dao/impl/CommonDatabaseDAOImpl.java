package org.secwallet.generate.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.secwallet.generate.exception.DAOException;
import org.secwallet.generate.utils.DatabaseType;
import org.secwallet.generate.utils.Dom4jUtils;
import org.secwallet.generate.vo.ConnParam;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;


/**
 * General database metadata query class
 */
public class CommonDatabaseDAOImpl extends AbstractDatabasetDAOImpl {

    private static final String ELEMENT_DRIVER = DRIVER;
    private static final String ELEMENT_URL = URL;
    private static final String ELEMENT_SELECT = "select";
    private static final String ATTRIBUTE_NAME = "name";
    
    private String driver;
    private String url;
    private Map<String, String> selectMap = new HashMap<String,String>();
    
    private DatabaseType dbType;
    
    /**
     * @param connParam
     */
    public CommonDatabaseDAOImpl(ConnParam connParam, DatabaseType dbType) {
        super(connParam);
        
        setDbType(dbType);
        loadSqlXml(dbType);
    }

    /**
     * @param dbType the dbType to set
     */
    public void setDbType(DatabaseType dbType) {
        this.dbType = dbType;
        setConverter(dbType.getConverter());
    }
    
    @Override
    protected String getDriver() throws DAOException {
        return driver;
    }

    @Override
    protected String getUrl(String host, int port, String dbName) throws DAOException{
        return String.format(url, host, port, dbName);
    }
    
    protected String getQuerySql(String sqlKey) throws DAOException {
        if (selectMap.containsKey(sqlKey)) {
            return selectMap.get(sqlKey);
        }
        throw new DAOException(DAOException.QUERY_EXCEPTION,
                "Error getting sql query, database enumeration type is:" + dbType + "，The query statement is：" + sqlKey, null);
    }

    @SuppressWarnings("unchecked")
    private void loadSqlXml(DatabaseType dbType) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        InputStream is= null;
        try {
            is = resourceLoader.getResource(dbType.getFileName()).getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document doc = Dom4jUtils.getDocument(is);
        if (doc != null) {
            Element root = doc.getRootElement();
            
            driver = root.elementText(ELEMENT_DRIVER);
            url = root.elementText(ELEMENT_URL);
            
            for (Element selectElem : (List<Element>) root.elements(ELEMENT_SELECT)) {
                selectMap.put(selectElem.attributeValue(ATTRIBUTE_NAME), selectElem.getTextTrim());
            }
        }
        try {
            is.close();
        } catch (IOException e) {
        }
    }
}
