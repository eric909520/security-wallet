package org.secwallet.core.model;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class SecUser implements UserDetails {
    private static final long serialVersionUID = 815860132208273186L;

    public SecUser() {
    }

    public SecUser(
            Long id, String nick_name, String mobile, String login_password
    ) {
        this.id = id;
        this.nick_name = nick_name;
        this.mobile = mobile;
        this.login_password = login_password;
    }

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "nick_name")
    private String nick_name;
    @ApiModelProperty(value = "mobile")
    private String mobile;
    @ApiModelProperty(value = "login_password")
    private String login_password;
    @ApiModelProperty(value = "token")
    private String token;
    private Long timesatamp;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public String getPassword() {
        return login_password;
    }

    @Override
    public String getUsername() {
        return mobile;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
