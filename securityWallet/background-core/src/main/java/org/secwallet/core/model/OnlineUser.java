package org.secwallet.core.model;

import lombok.Data;

/**
 * OnlineUser
 */
@Data
public class OnlineUser {

    private String sessionId;

    private Long userId;

    private String username;

    private String fullname;

    private Long orgId;

    protected String orgName;

    private Short title;
}
