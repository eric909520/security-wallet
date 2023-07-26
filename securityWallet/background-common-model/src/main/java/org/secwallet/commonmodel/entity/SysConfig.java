package org.secwallet.commonmodel.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysConfig implements Serializable {

    private String key;

    private String value;

    private String remark;
}
