package org.secwallet.commonmodel.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserOperating implements Serializable {

    private Long id;
    //
    private Long userId;
    // operation type
    private String type;
    // operation describe
    private String desc;
    //
    private String ip;
    //
    private String os;
    //
    private String address;
    //
    private Long time;
}
