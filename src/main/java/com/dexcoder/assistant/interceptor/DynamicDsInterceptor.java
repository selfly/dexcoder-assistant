package com.dexcoder.assistant.interceptor;

import com.dexcoder.assistant.persistence.DynamicDataSourceHolder;
import com.dexcoder.assistant.utils.StrUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 动态数据源拦截器
 * <p/>
 * Created by liyd on 2015-11-2.
 */
@Aspect
@Component
public class DynamicDsInterceptor {

    @Pointcut("execution(* org.springframework.jdbc.core.JdbcOperations.*(..))")
    public void executeMethod() {
    }

    @Around("executeMethod()")
    public Object methodAspect(ProceedingJoinPoint pjp) throws Throwable {

        String methodName = pjp.getSignature().getName();
        if (StrUtils.startsWith(methodName, "query")) {
            DynamicDataSourceHolder.setIsWrite(false);
        } else {
            DynamicDataSourceHolder.setIsWrite(true);
        }

        return pjp.proceed();
    }

}
