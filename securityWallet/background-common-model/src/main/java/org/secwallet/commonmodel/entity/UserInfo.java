package org.secwallet.commonmodel.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfo implements Serializable {
    private Long id;
    private String nickName;
    private String headImage;
    private String mobile;
    private String loginPassword;
    private String token;
    private Long inviteUser;
    private Long createTime;
    private String remark;
}
