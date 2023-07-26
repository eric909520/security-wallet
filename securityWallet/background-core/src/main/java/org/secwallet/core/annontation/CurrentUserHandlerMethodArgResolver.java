package org.secwallet.core.annontation;

import org.secwallet.core.Interceptor.AuthorizationInterceptor;
import org.secwallet.core.util.string.Constants;
import org.secwallet.core.model.SecUser;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


@Component
public class CurrentUserHandlerMethodArgResolver implements HandlerMethodArgumentResolver {

    /**
     *
     * Determine whether parameters annotated with @CurrentUser are supported
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterAnnotation(CurrentUser.class) != null && methodParameter.getParameterType() == SecUser.class;
    }

    /**
     *
     * inject parameter value
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        if (AuthorizationInterceptor.threadLocal.get() == null) {
            return null;
        }
        return AuthorizationInterceptor.threadLocal.get().get(Constants.THREAD_LOCAL_USER_KEY);
    }
}