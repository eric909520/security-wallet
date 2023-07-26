package org.secwallet.commonmodel.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysSwitch implements Serializable {
    private int id;

    private String key;

    private int enable;

    private String remark;
}
