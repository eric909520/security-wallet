package org.secwallet.commonmodel.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AppConfig implements Serializable {

    private String key;

    private String value;
}
