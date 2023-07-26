package org.secwallet.core.security;


import com.alibaba.fastjson.JSONObject;
import org.secwallet.core.util.security.SecurityUtil;
import org.secwallet.core.cache.ICache;
import org.secwallet.core.model.Result;
import org.secwallet.core.model.SecUser;
import org.secwallet.core.util.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Login verification filter
 */
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private ICache redisService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            String requestURI = request.getRequestURI();
            if (requestURI.indexOf("/healthz") > -1) {
                healthz(response);
                return;
            }

            if (requestURI.indexOf("/api/order/find") > -1) {
                chain.doFilter(request,response);
                return;
            }


            if (requestURI.indexOf("/favicon.ico") > -1 ||
                    requestURI.indexOf("/doc.html") > -1
            ) {
                chain.doFilter(request, response);
                return;
            }

            if(255 > JwtTokenUtils.getVersion(request)){
                if(requestURI.indexOf("/favicon.ico") <= -1 &&
                        requestURI.indexOf("/doc.html") <= -1 &&
                        requestURI.indexOf("webjars") <= -1 &&
                        requestURI.indexOf("bycdao-ui") <= -1 &&
                        requestURI.indexOf("swagger") <= -1 &&
                        requestURI.indexOf("api-docs") <= -1 &&
                        requestURI.indexOf("getKey") <= -1 &&
                        requestURI.indexOf("config") <= -1

                ){
                    versionError(response);
                }
            }

            SecUser secUser = securityUtil.getUser(request);
//            log.info("secUser:"+secUser);
//            String requestURI = request.getRequestURI();
//            log.info("requestURI:"+requestURI);
//            if(secUser != null && !"/api/user/login".equals(requestURI)){
//                String token = securityUtil.getToken(request);
//                    log.info("userId:"+secUser.getId()+" token:"+token);
//                    log.info("userId:"+secUser.getId()+" DBtoken:"+secUser.getToken());
//                    if (!token.equals(secUser.getToken())) {
//                        checkToken(response);
//                }else {
//                    resetLogin(response);
//                }
//            }
            if (secUser != null) {
                Object resetPass = redisService.vGet("user:resetPass:" + secUser.getId());
                if(resetPass != null){
                    logger.info("Password reset, blocking login");
                    resetPass(response);
                    redisService.delete("user:resetPass:" + secUser.getId());
                }
                Object o = redisService.vGet("USER:BLOCK:" + secUser.getId());
                if(o != null){
                    lock(response);
                }
            }else{
                resetLogin(response);
                return;
            }
            securityUtil.checkAuthentication(request);
        } catch (Exception e) {
            log.info("Token problem User re-login");
            resetLogin(response);
            return;
        }
        chain.doFilter(request,response);
    }


    private void resetLogin(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            Result r = new Result();
            r.setCode(401);
            r.setMessage("please login again");
            response.getOutputStream().write(JSONObject.toJSONString(r).getBytes());
        } catch (IOException e){
            log.error("User expired, return re-login error", e);
        }
    }
    private void resetPass(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            Result r = new Result();
            r.setCode(401);
            r.setMessage("User password has been reset, please log in again");
            response.getOutputStream().write(JSONObject.toJSONString(r).getBytes());
        } catch (IOException e){
            e.getStackTrace();
        }
    }

    private void lock(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            Result r = new Result();
            r.setCode(80);
            r.setMessage("The current user has been blocked for violating the terms of use, if you have any questions, please contact customer service");
            response.getOutputStream().write(JSONObject.toJSONString(r).getBytes());
        } catch (IOException e){
            e.getStackTrace();
        }
    }
    private void versionError(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            Result r = new Result();
            r.setCode(80);
            r.setMessage("The current version is not available, please update to the latest version");
            response.getOutputStream().write(JSONObject.toJSONString(r).getBytes());
        } catch (IOException e){
            e.getStackTrace();
        }
    }
    private void checkToken(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            Result r = new Result();
            r.setCode(401);
            r.setMessage("This account has been logged in, if you are not logged in by yourself, please change the password");
            response.getOutputStream().write(JSONObject.toJSONString(r).getBytes());
        } catch (IOException e){
            e.getStackTrace();
        }
    }

    /**
     * Use of health check
     * @param response
     */
    private void healthz(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            Result r = new Result();
            if(!checkRedis()){
                r.setCode(1);
                r.setMessage("redis error");
                response.getOutputStream().write(JSONObject.toJSONString(r).getBytes());
                return;
            }
            if(!checkDb()){
                r.setCode(1);
                r.setMessage("db error");
                response.getOutputStream().write(JSONObject.toJSONString(r).getBytes());
                return;
            }
            log.info("health check success");
            r.setCode(200);
            r.setMessage("success");
            response.getOutputStream().write(JSONObject.toJSONString(r).getBytes());
        } catch (IOException e){
            e.getStackTrace();
        }
    }

    public Boolean checkRedis() {
       return redisService.checkRedis();
    }

    public boolean checkDb() {
        String result = jdbcTemplate.queryForObject("select 1 from dual", String.class);
        return "1".equals(result);
    }
}
