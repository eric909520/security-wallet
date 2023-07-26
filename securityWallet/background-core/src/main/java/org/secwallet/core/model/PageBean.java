package org.secwallet.core.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PageBean implements Serializable {
    @ApiModelProperty(value = "Whether to disable paging，true or false")
    public boolean disablePaging;
    @ApiModelProperty(value = "order")
    public String order;
    @ApiModelProperty(value = "pageNum")
    public Integer pageNum;
    @ApiModelProperty(value = "pageSize")
    public Integer pageSize;
}
