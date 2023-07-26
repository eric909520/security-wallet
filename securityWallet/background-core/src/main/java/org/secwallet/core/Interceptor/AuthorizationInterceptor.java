package org.secwallet.core.Interceptor;

import org.secwallet.core.util.bean.AppUtil;
import org.secwallet.core.util.security.SecurityUtil;
import org.secwallet.core.util.string.Constants;
import org.secwallet.core.annontation.AuthIgnore;
import org.secwallet.core.exception.BizException;
import org.secwallet.core.model.ResultCode;
import org.secwallet.core.model.SecUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 **/
@Component
@Slf4j
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    public static final String IGNORE_LIST_SWAGGER = "/swagger-ui.html";

    public static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthIgnore authIgnore;
        threadLocal.set(new HashMap<>());
        if (request.getRequestURI().indexOf(IGNORE_LIST_SWAGGER) > 0) {
            return true;
        }

        if (handler instanceof HandlerMethod) {
            if (((HandlerMethod) handler).getMethod().getDeclaringClass().isAnnotationPresent(AuthIgnore.class)) {
                return true;
            }
        }

        if (handler instanceof HandlerMethod) {
            authIgnore = ((HandlerMethod) handler).getMethodAnnotation(AuthIgnore.class);
        } else {
            return true;
        }

        // If there is an @Authignore annotation, the token is not verified
        if (authIgnore != null) {
            return true;
        }

        // Get user credentials
        SecurityUtil securityUtil = AppUtil.getBean(SecurityUtil.class);
        SecUser secUser = securityUtil.getUser(request);
        String uuid = UUID.randomUUID().toString();

        if(secUser == null){
            throw new BizException(ResultCode.ILLEGAL);
        }
        threadLocal.get().put(Constants.THREAD_LOCAL_USER_KEY, secUser);
        return true;
    }

}
