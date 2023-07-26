package org.secwallet.commonmodel.advice;

import com.alibaba.fastjson.JSON;
import org.secwallet.commonmodel.constant.CommonConstants;
import org.secwallet.commonmodel.dto.Body;
import org.secwallet.core.Interceptor.AuthorizationInterceptor;
import org.secwallet.core.util.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.UUID;


@Slf4j
@RestControllerAdvice
public class RequestAopLogComponent implements RequestBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        PostMapping postMapping = parameter.getMethodAnnotation(PostMapping.class);
        String uuid = UUID.randomUUID().toString();
        if (AuthorizationInterceptor.threadLocal.get() == null) {
            AuthorizationInterceptor.threadLocal.set(new HashMap<String, Object>());
        }
        AuthorizationInterceptor.threadLocal.get().put(CommonConstants.REQUEST_UUID, uuid);
        try {
            log.info("request path :{} \n,address:{},param:{},sign:{}", uuid, postMapping != null ? StringUtils.arrayToDelimitedString(postMapping.value(), ",") : "postMapping is null", JSON.toJSONString(AesUtil.aesDecryptCbc(((Body) body).getObject())), ((Body) body).getSign());
        } catch (Exception e) {
            log.info("log request param error:", e);
        }
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                  Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        PostMapping postMapping = parameter.getMethodAnnotation(PostMapping.class);
        try {
            log.info("request address is empty body,address:{}", postMapping != null ? StringUtils.arrayToDelimitedString(postMapping.value(), ",") : "postMapping is null");
        } catch (Exception e) {
            log.info("log request param error:", e);
        }
        return body;
    }
}