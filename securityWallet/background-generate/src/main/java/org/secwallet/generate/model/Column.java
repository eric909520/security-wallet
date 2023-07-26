package org.secwallet.generate.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class Column {

    @ApiModelProperty(value = "name")
    private String name;
    @ApiModelProperty(value = "tableName")
    private String tableName;
    @ApiModelProperty(value = "dataType")
    private String dataType;
    @ApiModelProperty(value = "isNullable")
    private boolean isNullable;
    @ApiModelProperty(value = "defaultValue")
    private String defaultValue;
    @ApiModelProperty(value = "length")
    private String length;
    @ApiModelProperty(value = "precision")
    private String precision;
    @ApiModelProperty(value = "description")
    private String description;


}

