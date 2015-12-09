package com.dexcoder.jdbc.batis.reflection;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dexcoder.jdbc.exceptions.JdbcAssistantException;
import com.dexcoder.jdbc.utils.ClassUtils;

/**
 * Created by liyd on 2015-12-1.
 */
public class MetaClass {

    private Class<?>            typeClass;
    private List<String>        fields       = new ArrayList<String>();
    private Map<String, Method> readMethods  = new HashMap<String, Method>();
    private Map<String, Method> writeMethods = new HashMap<String, Method>();

    public MetaClass(Class<?> typeClass) {
        this.typeClass = typeClass;
        BeanInfo beanInfo = ClassUtils.getBeanInfo(typeClass);
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            String fieldName = pd.getName();
            this.fields.add(fieldName);
            this.readMethods.put(fieldName, pd.getReadMethod());
            this.writeMethods.put(fieldName, pd.getWriteMethod());
        }
    }

    public Method getReadMethod(String field) {
        if (!fields.contains(field) || readMethods.get(field) == null) {
            throw new JdbcAssistantException(String.format("属性[%s]在类[%s]中不存在或没有getter方法", field, typeClass.getName()));
        }
        return readMethods.get(field);
    }

    public Method getWriteMethod(String field) {
        if (!field.contains(field) || writeMethods.get(field) == null) {
            throw new JdbcAssistantException(String.format("属性[%s]在类[%s]中不存在或没有setter方法", field, typeClass.getName()));
        }
        return writeMethods.get(field);
    }

    //    public boolean hasSetter(String name) {
    //        PropertyTokenizer prop = new PropertyTokenizer(name);
    //        if (prop.hasNext()) {
    //            if (reflector.hasSetter(prop.getName())) {
    //                MetaClass metaProp = metaClassForProperty(prop.getName());
    //                return metaProp.hasSetter(prop.getChildren());
    //            } else {
    //                return false;
    //            }
    //        } else {
    //            return reflector.hasSetter(prop.getName());
    //        }
    //    }
    //
    //    public boolean hasGetter(String name) {
    //    }

}
