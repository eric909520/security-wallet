package org.secwallet.generate.vo;

import lombok.Data;

/**
 * Code Generation Model Fields
 */
@Data
public class ColumnModel {
    private String fieldName;
    private String javaType;
    private String jdbcType;
    private boolean isPrimaryKey;
    private String getter;
    private String setter;

    private String name;
    private String tableName;
    private String dataType;
    private boolean isNullable;
    private String defaultValue;
    private String length;
    private String precision;
    private String description;
}
