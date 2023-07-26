package org.secwallet.generate.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class TableModel {
    private String modelPackageName;
    private String daoPackageName;
    private String sqlMapPackageName;
    private String servicePackageName;
    private String serviceImplPackageName;
    private String controllerPackageName;
    private String viewPackageName;
    private String className;
    private String objectName;
    private ColumnModel primaryKey;

    private String name;
    private String description;
    private String tablespace;
    private List<ColumnModel> columns = new ArrayList<ColumnModel>();
}
