//package com.dexcoder.commons.cache;
//
//import java.beans.BeanInfo;
//import java.beans.PropertyDescriptor;
//import java.lang.reflect.Method;
//import java.lang.reflect.Modifier;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.aopalliance.intercept.MethodInterceptor;
//import org.aopalliance.intercept.MethodInvocation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.dexcoder.assistant.cache.DisableCache;
//import com.dexcoder.assistant.cache.EnableCache;
//import com.dexcoder.assistant.pager.Pageable;
//
///**
// * 缓存拦截器
// * <p/>
// * Created by liyd on 9/26/14.
// */
//public class CacheInterceptor implements MethodInterceptor {
//
//    /**
//     * 日志对象
//     */
//    private static final Logger LOG = LoggerFactory
//            .getLogger(CacheInterceptor.class);
//
//    /**
//     * 是否开启自动缓存
//     */
//    private boolean enableAutoCache = false;
//
//    /**
//     * 更新数据方法名开始前缀
//     */
//    private String[] updateMethodNamesBegin = {"insert", "save", "update", "del", "exe"};
//
//    /**
//     * 类名固定后缀
//     */
//    private String classNameFixEnd = "ServiceImpl";
//
//    public Object invoke(MethodInvocation invocation) throws Throwable {
//
//        Method method = invocation.getMethod();
//        //        Class<?> returnType = method.getReturnType();
//        //
//        //        //没有返回值，不处理直接调用方法
//        //        if (StringUtils.equals(returnType.getName(), "void")) {
//        //            return invocation.proceed();
//        //        }
//
//        //被拦截的类
//        Class<?> targetClass = invocation.getThis().getClass();
//
//        DisableCache disableCache = targetClass.getAnnotation(DisableCache.class);
//        if (disableCache == null) {
//            disableCache = method.getAnnotation(DisableCache.class);
//        }
//        //注解了不处理缓存，直接调用方法
//        if (disableCache != null) {
//            return invocation.proceed();
//        }
//
//        EnableCache enableCache = targetClass.getAnnotation(EnableCache.class);
//
//        List<String> entityNames = null;
//        if (enableCache != null) {
//            entityNames = new ArrayList<String>();
//            Class<?>[] entityClasses = enableCache.value();
//            for (Class<?> entityClass : entityClasses) {
//                entityNames.add(entityClass.getSimpleName());
//            }
//        } else if (enableAutoCache) {
//            entityNames = new ArrayList<String>();
//            entityNames.add(StrUtils.replace(targetClass.getSimpleName(), classNameFixEnd, ""));
//        } else {
//            //没有开启缓存，直接调用方法
//            return invocation.proceed();
//        }
//
//        //被拦截方法
//        String targetMethod = invocation.getMethod().getName();
//
//        //方法参数
//        Object[] arguments = invocation.getArguments();
//
//        Object result = null;
//        //列表查询
//        if (StrUtils.startsWith(targetMethod, "query")) {
//
//            String argsId = this.getArgsUUID(arguments);
//            String parentCacheKey = entityNames.get(0) + "_query";
//            String childCacheKey = parentCacheKey + "_" + argsId;
//            result = CacheUtils.getChild(parentCacheKey, childCacheKey);
//            if (result == null) {
//                result = invocation.proceed();
//                CacheUtils.putChild(parentCacheKey, childCacheKey, result);
//            }
//        } else if (StrUtils.startsWith(targetMethod, "get")) {
//
//            Long primaryValue = this.getPrimaryValue(arguments);
//            //返回null，不启用缓存
//            if (primaryValue == null) {
//                result = invocation.proceed();
//            } else {
//                String argsId = this.getArgsUUID(arguments);
//                String parentCacheKey = entityNames.get(0) + "_get_" + primaryValue;
//                String childCacheKey = parentCacheKey + "_" + argsId;
//                result = CacheUtils.getChild(parentCacheKey, childCacheKey);
//                if (result == null) {
//                    result = invocation.proceed();
//                    CacheUtils.putChild(parentCacheKey, childCacheKey, result);
//                }
//            }
//        } else {
//
//            result = invocation.proceed();
//
//            Long primaryValue = null;
//            for (String updateMethodNameBegin : updateMethodNamesBegin) {
//
//                if (!StrUtils.startsWith(targetMethod, updateMethodNameBegin)) {
//                    continue;
//                }
//                primaryValue = this.getPrimaryValue(arguments);
//                break;
//            }
//
//            //数据变更，移除缓存
//            for (String entityName : entityNames) {
//                // query缓存
//                CacheUtils.remove(entityName + "_query");
//                //get缓存
//                CacheUtils.remove(entityName + "_get_" + primaryValue);
//            }
//        }
//
//        return result;
//    }
//
//    /**
//     * 获取参数唯一id
//     *
//     * @param arguments
//     * @return
//     */
//    private String getArgsUUID(Object[] arguments) {
//
//        StringBuilder sb = new StringBuilder();
//        if (!ArrUtils.isEmpty(arguments)) {
//            for (Object arg : arguments) {
//                String argStr = SerializeUtils.objectToString(arg);
//                sb.append(argStr).append("#");
//            }
//        }
//        String uuid = UUIDUtils.getUUID8(sb.toString().getBytes());
//        return uuid;
//    }
//
//    /**
//     * 获取实体类型主键值
//     *
//     * @param arguments
//     * @return
//     */
//    private Long getPrimaryValue(Object[] arguments) {
//
//        if (ArrUtils.isEmpty(arguments)) {
//            return null;
//        }
//        Object entityObject = null;
//        for (Object arg : arguments) {
//
//            if (!(arg instanceof Pageable)) {
//                continue;
//            }
//            entityObject = arg;
//            break;
//        }
//        if (entityObject != null) {
//
//            String entityObjectClassName = entityObject.getClass().getSimpleName();
//            if (StrUtils.endsWith(entityObjectClassName, "Vo")) {
//                entityObjectClassName = StrUtils.substring(entityObjectClassName, 0,
//                        entityObjectClassName.length() - 2);
//            }
//
//            BeanInfo beanInfo = ClassUtils.getSelfBeanInfo(entityObject.getClass());
//            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
//            for (PropertyDescriptor pd : pds) {
//
//                if (!StrUtils.equalsIgnoreCase(entityObjectClassName + "Id", pd.getName())) {
//
//                    continue;
//                }
//
//                Method readMethod = pd.getReadMethod();
//                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
//                    readMethod.setAccessible(true);
//                }
//                try {
//                    Object value = readMethod.invoke(entityObject);
//                    return (Long) value;
//                } catch (Exception e) {
//                    LOG.error("缓存拦截器获取实体主键值失败", e);
//                    //缓存失败，不影响主业务
//                    return null;
//                }
//            }
//        } else {
//            for (Object arg : arguments) {
//
//                if (arg instanceof Long) {
//                    return (Long) arg;
//                }
//            }
//        }
//        return null;
//    }
//
//    public void setUpdateMethodNamesBegin(String[] updateMethodNamesBegin) {
//        this.updateMethodNamesBegin = updateMethodNamesBegin;
//    }
//
//    public void setClassNameFixEnd(String classNameFixEnd) {
//        this.classNameFixEnd = classNameFixEnd;
//    }
//
//    public void setEnableAutoCache(boolean enableAutoCache) {
//        this.enableAutoCache = enableAutoCache;
//    }
//}
