package org.secwallet.generate.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * index
 */
@Data
public class Index {
    
    @ApiModelProperty(value = "name")
    private String name;
    @ApiModelProperty(value = "indexType")
    private String indexType;
    @ApiModelProperty(value = "tableName")
    private String tableName;
    @ApiModelProperty(value = "unique")
    private boolean unique;
    @ApiModelProperty(value = "cloumns")
    private List<String> cloumns = new ArrayList<String>();
    public void addCloumn(String cloumn) {
        this.cloumns.add(cloumn);
    }
}
