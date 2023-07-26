package org.secwallet.generate.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class PrimaryKey {

    @ApiModelProperty(value = "name")
    private String name;
    @ApiModelProperty(value = "tableName")
    private String tableName;

    @ApiModelProperty(value = "cloumn")
    private String cloumn;

}
