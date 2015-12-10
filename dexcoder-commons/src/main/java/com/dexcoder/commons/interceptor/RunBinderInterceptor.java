package com.dexcoder.commons.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.dexcoder.commons.exceptions.DexcoderException;
import com.dexcoder.commons.result.RunBinder;

/**
 * 业务拦截器，主要拦截异常及错误信息，最好配合RunBinderOnMvcDestroyInterceptor使用防止内存溢出
 * 
 * Created by liyd on 4/9/14.
 */
public class RunBinderInterceptor implements MethodInterceptor {

    /** 日志对象 */
    private static final Logger LOG         = LoggerFactory.getLogger(RunBinderInterceptor.class);

    /** 执行时间超过打印warn日志毫秒数 */
    private static final long   LOG_TIMEOUT = 1000;

    public Object invoke(MethodInvocation invocation) throws Throwable {
        //被拦截的类
        String targetClass = invocation.getThis().getClass().getName();

        //被拦截方法
        String targetMethod = invocation.getMethod().getName();

        //当前时间毫秒数
        long startTime = System.currentTimeMillis();

        LOG.debug("start:[class={},method={},startTime={}]", new Object[] { targetClass, targetMethod, startTime });

        //返回结果
        Object result = null;
        try {
            //此处调用业务方法
            result = invocation.proceed();

        } catch (DexcoderException dexcoderException) {

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            RunBinder.addError(dexcoderException);
            LOG.info("出现已知异常，异常信息[resultCode={},resultMsg={}]", dexcoderException.getResultCode(),
                dexcoderException.getResultMsg());
            //ignore
        } catch (Throwable throwable) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            RunBinder.addError("UN_KNOWN_EXCEPTION", "未知异常");
            LOG.error("发现未知异常，异常信息", throwable);
            //ignore
        }

        long finishTime = System.currentTimeMillis();

        LOG.debug("finished:[class={},method={},finishTime={}]", new Object[] { targetClass, targetMethod, finishTime });

        long useTime = finishTime - startTime;

        if (useTime > LOG_TIMEOUT) {

            LOG.warn("Long processing time:[class={},method={},used time={}]", new Object[] { targetClass,
                    targetMethod, useTime });
        }
        return result;
    }
}
