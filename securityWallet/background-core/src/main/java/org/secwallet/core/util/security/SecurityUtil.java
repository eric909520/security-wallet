package org.secwallet.core.util.security;

import com.alibaba.fastjson.JSON;
import org.secwallet.core.security.mapper.SecUserMapper;
import org.secwallet.core.Interceptor.AuthorizationInterceptor;
import org.secwallet.core.model.SecUser;
import org.secwallet.core.security.GrantedAuthorityImpl;
import org.secwallet.core.security.JwtAuthenticatioToken;
import org.secwallet.core.util.string.Constants;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * security tools
 */
@Component
@Slf4j
public class SecurityUtil {

    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private SecUserMapper secUserMapper;

    /**
     * System login authentication
     * @param request
     * @param user
     * @param password
     * @param authenticationManager
     * @return
     */
    public JwtAuthenticatioToken login(HttpServletRequest request, SecUser user, Object password, AuthenticationManager authenticationManager) {
        JwtAuthenticatioToken token = new JwtAuthenticatioToken(user.getMobile(), password);
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // Execute the login authentication process
        Authentication authentication = authenticationManager.authenticate(token);
        // Authentication successfully stores the authentication information to the context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Generate token and return to client
        user.setLogin_password(password.toString());
        token.setToken(tokenUtil.generateToken(user));
        return token;
    }
    /**
     * Get a token for authentication
     * @param request
     */
    public  void checkAuthentication(HttpServletRequest request) {
        // Get the token and get the login authentication information based on the token
        Authentication authentication = getAuthenticationeFromToken(request);
        if (authentication != null) {
            // Set up login user
            if(AuthorizationInterceptor.threadLocal.get() == null){
                AuthorizationInterceptor.threadLocal.set(new HashMap<String, Object>());
            }
            AuthorizationInterceptor.threadLocal.get().put(Constants.THREAD_LOCAL_USER_KEY,authentication.getPrincipal());
        }
        // Set login credentials to context
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Get current username
     * @return
     */
    public String getUsername() {
        String username = null;
        Authentication authentication = getAuthentication();
        if(authentication != null) {
            Object principal = authentication.getPrincipal();
            if(principal != null && principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            }
        }
        return username;
    }

    /**
     * Get current username
     * @return
     */
    public SecUser getUser() {
        Authentication authentication = getAuthentication();
        if(authentication != null) {
            Object principal = authentication.getPrincipal();
            if(principal != null && principal instanceof SecUser) {
                return  ((SecUser) principal);
            }
        }
        return null;
    }


    /**
     * Get username
     * @return
     */
    public String getUsername(Authentication authentication) {
        String username = null;
        if(authentication != null) {
            Object principal = authentication.getPrincipal();
            if(principal != null && principal instanceof SecUser) {
                username = ((SecUser) principal).getUsername();
            }
        }
        return username;
    }

    /**
     * Get current login information
     * @return
     */
    public Authentication getAuthentication() {
        if(SecurityContextHolder.getContext() == null) {
            return null;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }
    /**
     * Obtain the login authentication information according to the request token, without verifying the access source, there is a certain risk, and the follow-up needs to be improved
     * Users who log in through the platform normally create a token through their account, and subsequent accesses are verified based on this token
     * For users who log in from a third party, check whether the user information is correct through the user id. If it is correct and create a token, subsequent access will be verified based on this token.
     * @param request
     * @return username
     */
    public  Authentication getAuthenticationeFromToken(HttpServletRequest request) {
        Authentication authentication = null;
        // Get the token carried by the request
        String token = tokenUtil.getToken(request);
        SecUser user=null;
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        if(token != null) {
            // Request token cannot be empty
            // if(getAuthentication() == null) {
            // Whether the token is invalid
            if (tokenUtil.validateToken(token)){
                if (tokenUtil.isTokenExpired(token)) {
                    return null;
                }
                // Authentication is empty in the context
                Claims claims = tokenUtil.getAllClaimsFromToken(token);
                if (claims == null) {
                    return null;
                }
                String username = claims.getSubject();
                if (username == null) {
                    return null;
                }

                Object authors = claims.get("authorities");

                if (authors != null && authors instanceof List) {
                    for (Object object : (List) authors) {
                        authorities.add(new GrantedAuthorityImpl((String) ((Map) object).get("authority")));
                    }
                }
                user = JSON.toJavaObject((JSON) JSON.toJSON(claims.get("user")), SecUser.class);
            }else{// Third-party user id access
                user=secUserMapper.findById(token);
                token=tokenUtil.generateToken(user);//create token
            }
            authentication = new JwtAuthenticatioToken(user, user.getPassword(), authorities, token);

        }
        return authentication;
    }
    public SecUser getUser(HttpServletRequest request){
        String token = tokenUtil.getToken(request);
        if(token == null){
            return null;
        }
        String usernameFromToken = tokenUtil.getUsernameFromToken(token);
        SecUser user = secUserMapper.findByAccount(usernameFromToken);
        // Set as the token in the login request, user single sign-on verification
        user.setToken(token);
        return user;
    }
    public String getToken(HttpServletRequest request){
        String token = tokenUtil.getToken(request);
        return token;
    }
    public void cleanUser(){
        Long id = this.getUser().getId();
        secUserMapper.updateUserToken(id);
    }
}
