package org.secwallet.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.secwallet.core.security.mapper.SecUserMapper;
import org.secwallet.core.model.SecUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component("authenticationSuccessHandler")
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SecUserMapper userMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        //If you don't do anything, then call the method of the parent class directly
        //super.onAuthenticationSuccess(request, response, authentication);
        SecUser user = (SecUser) authentication.getPrincipal();
        //userMapper.updateLoginTime(user);
        new DefaultRedirectStrategy().sendRedirect(request, response, "/");
    }
}
