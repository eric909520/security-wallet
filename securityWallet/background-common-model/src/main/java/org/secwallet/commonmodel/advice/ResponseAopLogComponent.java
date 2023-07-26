package org.secwallet.commonmodel.advice;

import com.alibaba.fastjson.JSON;
import org.secwallet.commonmodel.constant.CommonConstants;
import org.secwallet.core.Interceptor.AuthorizationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


@Slf4j
//@RestControllerAdvice
public class ResponseAopLogComponent implements ResponseBodyAdvice {


    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (AuthorizationInterceptor.threadLocal.get() == null) {
            log.info("response \nrequest path:{},response:{}", request.getURI().getPath(), JSON.toJSONString(body));
        } else {
            log.info("response {} \nrequest path:{},response:{}", AuthorizationInterceptor.threadLocal.get().get(CommonConstants.REQUEST_UUID), request.getURI().getPath(), JSON.toJSONString(body));
        }
        AuthorizationInterceptor.threadLocal.remove();
        return body;
    }
}