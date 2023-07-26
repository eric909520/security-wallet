package org.secwallet.core.security;

import com.alibaba.fastjson.JSON;

import org.secwallet.core.exception.CTException;
import org.secwallet.core.security.annotation.TokenAuth;
import org.secwallet.core.util.security.TokenUtil;
import org.secwallet.core.model.SecUser;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class tokenAspect {
    @Autowired
    private TokenUtil tokenUtil;



    private AuthenticationManager myAuthenticationManager=new ApiAuthenticationManager();
    @Around("@annotation(org.secwallet.core.security.annotation.TokenAuth)")
    public Object tokenAuth(ProceedingJoinPoint point) throws Throwable {
        TokenAuth tokenAuth = getAnnotationFromMethod(point, TokenAuth.class);
        Boolean isAuth = tokenAuth.value();
        if (isAuth){
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authToken = tokenUtil.getToken(request);
            if (StringUtils.isBlank(authToken)){
                throw new CTException("token value is empty！");
            }
            //Verify token validity
            Claims claims =tokenUtil.getAllClaimsFromToken(authToken);
            SecUser user= JSON.parseObject(claims.get("user").toString()).toJavaObject(SecUser.class);
            usernamePasswordAuthentication(user,request);

        }
        return point.proceed();
    }
    /**
     * Get the pointcut method by reflection from the join point, and then get the annotation
     *
     * @param point Junction
     * @param clazz Annotation class
     * @param <T>   annotation
     */
    private <T extends Annotation> T getAnnotationFromMethod(JoinPoint point, Class<T> clazz) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(clazz);
    }

    /**
     * User information authorization, cache user information
     * @param user
     * @param request
     */
    private void usernamePasswordAuthentication(SecUser user, HttpServletRequest request){
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user, user.getUsername(), user.getAuthorities());
        Authentication authentication = myAuthenticationManager.authenticate(authRequest); //调用loadUserByUsername
        SecurityContextHolder.getContext().setAuthentication(authentication);
        HttpSession session = request.getSession();
        session.setAttribute("user", SecurityContextHolder.getContext());
    }
}
