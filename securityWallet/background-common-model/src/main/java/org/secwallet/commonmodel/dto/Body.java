package org.secwallet.commonmodel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "reqJSON")
public class Body implements Serializable {
    /**
     * request parameters
     */
    @ApiModelProperty(value = "input")
    private String object;
    @ApiModelProperty(value = "sign")
    private String sign;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
