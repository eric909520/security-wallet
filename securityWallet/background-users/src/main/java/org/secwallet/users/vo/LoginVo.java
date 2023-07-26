package org.secwallet.users.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginVo implements Serializable {
    private String token;
    private Long userId;
    private String nickName ;
    private String avatarAddress;
}
