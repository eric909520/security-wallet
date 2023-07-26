package org.secwallet.core.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApiAuthenticationManager implements AuthenticationManager {
    static final List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();

    // Build a list of roles
    static {
        AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // Authentication method
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        // Here we have customized the verification pass conditions: if the username and password are the same, the authentication can be passed.
        //if (auth.getName().equals(auth.getCredentials())) {
            return new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), AUTHORITIES);
        //}
        // If the authentication is not passed, a password error exception is thrown
        //throw new BadCredentialsException("Bad Credentials");
    }
}
