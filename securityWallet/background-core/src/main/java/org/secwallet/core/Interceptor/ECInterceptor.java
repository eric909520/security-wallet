package org.secwallet.core.Interceptor;

import com.alibaba.fastjson.JSONObject;
import org.secwallet.core.cache.ICache;
import org.secwallet.core.model.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Component
public class ECInterceptor implements HandlerInterceptor {
    @Resource
    private ICache redisService;
    /**
     * @Description Called before the business handler processes the request. Preprocessing can be used for encoding, security control and other processing;
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println(request.getRequestURL()+"===========preHandle===========");
//        String url = request.getRequestURL().toString();
//        int version = JwtTokenUtils.getVersion(request);
//        if(version  < 150 ){
//            if(url.indexOf("config") > -1){
//                return true;
//            }
//            versionJson(response);
//            return false;
//        }
//        String token = request.getHeader("Authorization");
//        if(token == null){
//            return true;
//        }
//        String aesInfo = (String) redisService.vGet(UserInfoKeyPrefix.userinfo,token);

//        Long userId = JwtTokenUtils.getUserId(aesInfo);
//        Object o = redisService.vGet(UserInfoKeyPrefix.userinfo,"user:"+userId);
//        if(o != null && "1".equals(o.toString())){
//            returnJson(response);
//            return false;
//        }
        return true;
    }


    private void returnJson(HttpServletResponse response){
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            Result r = new Result();
            r.setCode(401);
            r.setMessage("You have been banned");
            writer.print(JSONObject.toJSONString(r));
        } catch (IOException e){
            e.getStackTrace();
//            LoggerUtil.logError(ECInterceptor.class, "Interceptor output stream exception"+e);
        } finally {
            if(writer != null){
                writer.close();
            }
        }
    }


    private void versionJson(HttpServletResponse response){
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            Result r = new Result();
            r.setCode(11);
            r.setMessage("Please update to version 1.5.1 or later");
            writer.print(JSONObject.toJSONString(r));
        } catch (IOException e){
            e.getStackTrace();
//            LoggerUtil.logError(ECInterceptor.class, "Interceptor output stream exception"+e);
        } finally {
            if(writer != null){
                writer.close();
            }
        }
    }
}
