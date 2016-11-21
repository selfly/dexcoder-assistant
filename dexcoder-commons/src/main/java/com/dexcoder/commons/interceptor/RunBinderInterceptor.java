package com.dexcoder.commons.interceptor;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dexcoder.commons.exceptions.DexcoderException;
import com.dexcoder.commons.result.RunBinder;

/**
 * 业务拦截器，主要拦截异常及错误信息，最好配合RunBinderOnMvcDestroyInterceptor使用防止内存溢出
 * 
 * Created by liyd on 4/9/14.
 */
@Aspect
public class RunBinderInterceptor {

    /** 日志对象 */
    private static final Logger               LOG             = LoggerFactory.getLogger(RunBinderInterceptor.class);

    private static ThreadLocal<AtomicInteger> methodHierarchy = new ThreadLocal<AtomicInteger>();

    /** 执行时间超过打印warn日志毫秒数 */
    private static final long                 LOG_TIMEOUT     = 1000;

    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void serviceAnnotation() {

    }

    @Pointcut("bean(*ServiceImpl)")
    public void serviceName() {

    }

    @Around("serviceAnnotation() || serviceName()")
    public Object around(ProceedingJoinPoint pjp) {

        AtomicInteger ai = methodHierarchy.get();
        if (ai == null) {
            ai = new AtomicInteger(1);
            methodHierarchy.set(ai);
        } else {
            ai.incrementAndGet();
        }

        //被拦截的类
        String targetClass = pjp.getTarget().getClass().getName();

        //被拦截方法
        Signature signature = pjp.getSignature();
        String targetMethod = signature.getName();

        //当前时间毫秒数
        long beginTime = System.currentTimeMillis();

        if (LOG.isDebugEnabled()) {
            LOG.debug("start:[class={},method={},beginTime={}]", new Object[] { targetClass, targetMethod, beginTime });
        }

        //返回结果
        Object result = null;
        try {
            //此处调用业务方法
            result = pjp.proceed();

        } catch (DexcoderException dexcoderException) {
            if (ai.get() > 1) {
                throw dexcoderException;
            }
            RunBinderTransactionAspectSupport.setRollbackOnly();
            RunBinder.addError(dexcoderException);
            result = this.getDefaultValue(signature);
            //已知异常,debug才输出异常堆栈信息
            if (LOG.isDebugEnabled()) {
                LOG.warn(String.format("已知异常,方法:[class=%s,method=%s],信息:[resultCode=%s,resultMsg=%s],参数:[%s]",
                    targetClass, targetMethod, dexcoderException.getResultCode(), dexcoderException.getResultMsg(),
                    argsToString(pjp)), dexcoderException);
            } else {
                LOG.info(String.format("已知异常,方法:[class=%s,method=%s],信息:[resultCode=%s,resultMsg=%s],参数:[%s]",
                    targetClass, targetMethod, dexcoderException.getResultCode(), dexcoderException.getResultMsg(),
                    argsToString(pjp)));
            }
        } catch (Throwable throwable) {
            if (ai.get() > 1) {
                throw new RuntimeException(throwable);
            }
            RunBinderTransactionAspectSupport.setRollbackOnly();
            RunBinder.addError("UN_KNOWN_EXCEPTION", "未知异常");
            result = this.getDefaultValue(signature);
            LOG.error(
                String.format("未知异常,方法:[class=%s,method=%s],参数:[%s]", targetClass, targetMethod, argsToString(pjp)),
                throwable);
        } finally {
            if (ai.decrementAndGet() == 0) {
                methodHierarchy.remove();
            }
        }

        long endTime = System.currentTimeMillis();

        long useTime = endTime - beginTime;

        if (LOG.isDebugEnabled()) {
            LOG.debug("end:[class={},method={},endTime={},usedTime={}]", new Object[] { targetClass, targetMethod,
                    endTime, useTime });
        }

        if (useTime > LOG_TIMEOUT) {
            LOG.warn("Long processing time:[class={},method={},usedTime={}]", new Object[] { targetClass, targetMethod,
                    useTime });
        }
        return result;

    }

    /**
     * 获取基本类型的默认值
     * 如果方法返回的是基本的值类型,直接返回null会出异常
     *
     * @param signature
     * @return
     */
    private Object getDefaultValue(Signature signature) {
        if (!(signature instanceof MethodSignature)) {
            return null;
        }

        MethodSignature methodSignature = (MethodSignature) signature;
        Class returnType = methodSignature.getReturnType();
        if (!returnType.isPrimitive()) {
            return null;
        }
        if (returnType == Boolean.TYPE) {
            return Boolean.FALSE;
        } else if (returnType == Character.TYPE) {
            return '\u0000';
        } else if (returnType == Byte.TYPE) {
            return (byte) 0;
        } else if (returnType == Short.TYPE) {
            return (short) 0;
        } else if (returnType == Integer.TYPE) {
            return 0;
        } else if (returnType == Long.TYPE) {
            return 0L;
        } else if (returnType == Float.TYPE) {
            return 0.0F;
        } else if (returnType == Double.TYPE) {
            return 0.0D;
        }
        return null;
    }

    /**
     * 获取参数字符串
     * 
     * @param pjp
     * @return
     */
    private String argsToString(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        return ToStringBuilder.reflectionToString(args, ToStringStyle.MULTI_LINE_STYLE);
        //        if (ArrayUtils.isEmpty(args)) {
        //            return "";
        //        }
        //        StringBuilder sb = new StringBuilder();
        //        for (Object obj : args) {
        //            if (obj == null) {
        //                sb.append("null;");
        //            } else {
        //                sb.append(obj.getClass().getName()).append("=").append(obj).append(";");
        //            }
        //        }
        //        return sb.toString();
    }
}
