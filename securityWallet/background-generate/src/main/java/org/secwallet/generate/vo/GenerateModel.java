package org.secwallet.generate.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class GenerateModel {
    private String outPutFolderPath;
    private String basePackage = "com.cthl";
    private ConnParam connParam;
    private List<TableModel> tableModels = new ArrayList<>();
    public String getOutPutFolderPath() {
        return outPutFolderPath;
    }
    public void setOutPutFolderPath(String outPutFolderPath) {
        this.outPutFolderPath = outPutFolderPath;
    }
    public String getBasePackage() {
        return basePackage;
    }
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
    public ConnParam getConnParam() {
        return connParam;
    }
    public void setConnParam(ConnParam connParam) {
        this.connParam = connParam;
    }
    public List<TableModel> getTableModels() {
        return tableModels;
    }
    public void setTableModels(List<TableModel> tableModels) {
        this.tableModels = tableModels;
    }
}
