package org.secwallet.generate.utils;


import org.secwallet.generate.converter.CommonMetaDataConverter;
import org.secwallet.generate.converter.MySQL5MetaDataConverter;
import org.secwallet.generate.converter.OracleMetaDataConverter;
import org.secwallet.generate.dao.IMetaDataConverter;

/**
 * Database query statement file
 */
public enum DatabaseType {

	Oracle {

		@Override
		public String getFileName() {
			return FOLDER + "/Oracle.xml";
		}

		@Override
		public IMetaDataConverter getConverter() {
			return OracleMetaDataConverter.getInstance();
		}
		
	},
	
	MySql5 {
		@Override
		public String getFileName() {
			return FOLDER + "/MySQL5.xml";
		}

		@Override
		public IMetaDataConverter getConverter() {
			return MySQL5MetaDataConverter.getInstance();
		}
	}, 
	
	MSSQLServer {
		@Override
		public String getFileName() {
			return FOLDER + "/MSSQL.xml";
		}
	};
	
	//private static final String FOLDER	= DatabaseType.class.getPackage().getName().replace('.', '/');
    private static final String FOLDER	= "classpath:/sql";
	
	abstract public String getFileName();
	
	public IMetaDataConverter getConverter() {
		return CommonMetaDataConverter.getInstance();
	}
}
