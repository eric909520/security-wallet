package org.secwallet.datasource.aop;

import org.secwallet.datasource.annotation.TargetDataSource;
import org.secwallet.datasource.config.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
@Slf4j
public class DynamicDataSourceAnnotationInterceptor implements MethodInterceptor{
    /**
     *
     * Cache method annotation values
     */
    private static final Map<Method, String> METHOD_CACHE = new HashMap<>();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            String datasource = determineDatasource(invocation);
            if (! DynamicDataSourceContextHolder.containsDataSource(datasource)) {
                log.info( "Cache method annotation values>", datasource);
            }
            DynamicDataSourceContextHolder.setDataSourceRouterKey(datasource);
            return invocation.proceed();
        } finally {
            DynamicDataSourceContextHolder.removeDataSourceRouterKey();
        }
    }

    private String determineDatasource(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        if (METHOD_CACHE.containsKey(method)) {
            return METHOD_CACHE.get(method);
        } else {
            TargetDataSource ds = method.isAnnotationPresent(TargetDataSource.class) ? method.getAnnotation(TargetDataSource.class)
                    : AnnotationUtils.findAnnotation(method.getDeclaringClass(), TargetDataSource.class);
            METHOD_CACHE.put(method, ds.value());
            return ds.value();
        }
    }
}
