package org.secwallet.generate.model;


import org.secwallet.core.model.PageBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SysDataSource extends PageBean implements Serializable{
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("name")
    private String name;
    @ApiModelProperty("databaseType")
    private String databaseType;
    @ApiModelProperty("host")
    private String host;
    @ApiModelProperty("port")
    private String port;
    @ApiModelProperty("loginName")
    private String loginName;
    @ApiModelProperty("password")
    private String password;
    @ApiModelProperty("databaseName")
    private String databaseName;

    @ApiModelProperty(value = "createTime")
    public String createTime;

    @ApiModelProperty(value = "createUserId")
    public String createUserId;

    @ApiModelProperty(value = "createUser")
    public String createUser;


    @ApiModelProperty(value = "updateUserId")
    public String updateUserId;
    @ApiModelProperty(value = "updateUser")
    public String updateUser;


    @ApiModelProperty(value = "updateTime")
    public String updateTime;
}