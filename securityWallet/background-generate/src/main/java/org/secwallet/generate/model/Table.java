package org.secwallet.generate.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class Table {

    @ApiModelProperty(value = "name")
    private String name;
    @ApiModelProperty(value = "description")
    private String description;
    @ApiModelProperty(value = "tablespace")
    private String tablespace;
    @ApiModelProperty(value = "columns")
    private List<Column> columns = new ArrayList<Column>();

    public void addColumn(Column clolumn) {
        this.columns.add(clolumn);
    }

}
