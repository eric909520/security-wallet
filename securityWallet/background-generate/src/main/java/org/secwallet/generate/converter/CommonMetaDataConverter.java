package org.secwallet.generate.converter;


import org.secwallet.generate.dao.IMetaDataConverter;
import org.secwallet.generate.model.*;

import java.util.Map;

/**
 * Universal Metadata Converter
 */
public class CommonMetaDataConverter implements IMetaDataConverter {

	// The field name returned by the query is all uppercase, so when using map to get the value, you need to use the uppercase field name
	private static final IMetaDataConverter instance = new CommonMetaDataConverter();

	public static IMetaDataConverter getInstance() {
		return instance;
	}

	@Override
	public Table convertMap2Table(Map<String, String> map) {
		Table table = new Table();
		table.setName(getValue(map, "TABLE_NAME"));
		table.setTablespace(getValue(map, "TABLESPACE"));
		table.setDescription(getValue(map, "DESCRIPTION"));
		return table;
	}

	@Override
	public Column convertMap2Column(Map<String, String> map) {
		Column column = new Column();
		column.setName(getValue(map, "COLUMN_NAME"));
		column.setTableName(getValue(map, "TABLE_NAME"));
		column.setDataType(getValue(map, "DATA_TYPE"));
		// Oracle/MySQL，The value of DATA_PRECISION of a non-numeric type field is null
		String dataPrecision = getValue(map, "DATA_PRECISION");
		if (null != dataPrecision) {
			column.setLength(dataPrecision);
		} else {
			column.setLength(getValue(map, "DATA_LENGTH"));
		}
		// data_scale is the decimal place
//		column.setPrecision(getValue(map, "PRECISION"));
		column.setPrecision(getValue(map, "DATA_SCALE"));
		// The isNullable of the Column class is true, which means that it is not allowed to be empty.
		if("N".equalsIgnoreCase(getValue(map, "NULLABLE")) || "NO".equalsIgnoreCase(getValue(map, "NULLABLE"))
		        || "NOT".equalsIgnoreCase(getValue(map, "NULLABLE"))) {
		    column.setNullable(false);
		} else {
		    column.setNullable(true);
		}
		column.setDescription(getValue(map, "DESCRIPTION"));
		column.setDefaultValue(getValue(map, "DEFAULT_VALUE"));
		return column;
	}

	@Override
	public PrimaryKey convertMap2PrimaryKey(Map<String, String> map) {
		PrimaryKey primaryKey = new PrimaryKey();
		primaryKey.setName(getValue(map, "CONSTRAINT_NAME"));
		primaryKey.setTableName(getValue(map, "TABLE_NAME"));
		primaryKey.setCloumn(getValue(map, "COLUMN_NAME"));

		return primaryKey;
	}

	@Override
	public ForeignKey convertMap2ForeignKey(Map<String, String> map) {
		ForeignKey fk = new ForeignKey();
		fk.setFkName(getValue(map, "FK_NAME"));
		fk.setFkTableName(getValue(map, "FK_TABLE_NAME"));
		fk.setFkColumnName(getValue(map, "FK_COLUMN_NAME"));
		fk.setPkTableName(getValue(map, "PK_TABLE_NAME"));
		fk.setPkColumnName(getValue(map, "PK_COLUMN_NAME"));
		return fk;
	}

	@Override
	public Index convertMap2Index(Map<String, String> map) {
		Index index = new Index();
		index.setName(getValue(map, "INDEX_NAME"));
		index.setTableName(getValue(map, "TABLE_NAME"));
		index.setIndexType(getValue(map, "INDEX_TYPE"));
		index.setUnique(Boolean.valueOf(getValue(map, "IS_UNIQUE")));
		index.getCloumns().add(getValue(map, "COLUMN_NAME"));
		return index;
	}

	@Override
	public Trigger convertMap2Trigger(Map<String, String> map) {
		Trigger trigger = new Trigger();
		trigger.setName(getValue(map, "TRIGGER_NAME"));
		trigger.setTriggerType(getValue(map, "TRIGGER_TYPE"));
		trigger.setEventType(getValue(map, "EVENT_TYPE"));
		trigger.setDefinition(getValue(map, "TRIGGER_BODY"));
		trigger.setDescription(getValue(map, "DESCRIPTION"));

		return trigger;
	}

	protected String getValue(Map<String, String> map, String key) {
		assert !map.containsKey(key) : map.toString() + "不含有key【" + key + "】";
		return map.get(key);
	}

}
