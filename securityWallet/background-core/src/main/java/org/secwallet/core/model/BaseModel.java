package org.secwallet.core.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseModel extends PageBean {

    @ApiModelProperty(value = "createUserId")
    public String createUserId;

    @ApiModelProperty(value = "createUser")
    public String createUser;

    @ApiModelProperty(value = "createTime")
    public Long createTime;

    @ApiModelProperty(value = "updateUserId")
    public String updateUserId;

    @ApiModelProperty(value = "updateUser")
    public String updateUser;

    @ApiModelProperty(value = "updateTime")
    public Long updateTime;

}
