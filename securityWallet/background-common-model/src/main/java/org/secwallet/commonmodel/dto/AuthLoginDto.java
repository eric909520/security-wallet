package org.secwallet.commonmodel.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * login response data
 */
@Data
public class AuthLoginDto implements Serializable {

	private static final long serialVersionUID = -5459281702531561000L;

	private String token;

	private String salt;
	//id
	private long userId;

	private String nickName ;

	private String avatarAddress;


}
