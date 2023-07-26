package org.secwallet.generate.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class DataSourceConnModel implements Serializable {

    @ApiModelProperty(value = "dbType")
    private String dbType;
    @ApiModelProperty(value = "host")
    private String host;
    @ApiModelProperty(value = "port")
    private int port;
    @ApiModelProperty(value = "dbName")
    private String dbName;
    @ApiModelProperty(value = "userName")
    private String userName;
    @ApiModelProperty(value = "password")
    private String password;
}
