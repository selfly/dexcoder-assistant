//package com.dexcoder.assistant.persistence;
//
//import java.beans.BeanInfo;
//import java.beans.PropertyDescriptor;
//import java.lang.reflect.Method;
//
//import org.apache.commons.lang.StringUtils;
//
//import com.dexcoder.assistant.utils.ClassUtils;
//
///**
// * Created by liyd on 2015-11-18.
// */
//public class BeanWrapper implements ObjectWrapper {
//
//    private Object object;
//
//    public BeanWrapper(Object object) {
//        this.object = object;
//    }
//
//    public Object getValue(String name) {
//
//        //获取属性信息
//        BeanInfo beanInfo = ClassUtils.getBeanInfo(object.getClass());
//        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
//
//        Object value = null;
//        for (PropertyDescriptor pd : pds) {
//
//            if (StringUtils.equals(name, pd.getName())) {
//                Method readMethod = pd.getReadMethod();
//                value = ClassUtils.invokeMethod(readMethod, this.object);
//                break;
//            }
//        }
//        return value;
//    }
//}
