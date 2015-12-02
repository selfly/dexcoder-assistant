package com.dexcoder.assistant.persistence.manual;

import com.dexcoder.assistant.exceptions.AssistantException;
import com.dexcoder.assistant.utils.ClassUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by liyd on 2015-12-1.
 */
public class BeanWrapper extends BaseWrapper {

    private Object object;
    private MetaClass metaClass;

    public BeanWrapper(MetaObject metaObject, Object object) {
        super(metaObject);
        this.object = object;
        this.metaClass = new MetaClass(object.getClass());
    }

    public Object get(PropertyTokenizer prop) {
        if (prop.getIndex() != null) {
            Object collection = resolveCollection(prop, object);
            return getCollectionValue(prop, collection);
        } else {
            return getBeanProperty(prop, object);
        }
    }

    public void set(PropertyTokenizer prop, Object value) {
        if (prop.getIndex() != null) {
            Object collection = resolveCollection(prop, object);
            setCollectionValue(prop, collection, value);
        } else {
            setBeanProperty(prop, object, value);
        }
    }

    private Object getBeanProperty(PropertyTokenizer prop, Object object) {
        Method readMethod = metaClass.getReadMethod(prop.getName());
        return ClassUtils.invokeMethod(readMethod, object);
    }

    private void setBeanProperty(PropertyTokenizer prop, Object object, Object value) {
        Method writeMethod = metaClass.getWriteMethod(prop.getName());
        ClassUtils.invokeMethod(writeMethod, object, value);
    }


    public String findProperty(String name, boolean useCamelCaseMapping) {
        return null;
    }

    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop) {
        Object newObject = ClassUtils.newInstance(object.getClass());
        MetaObject metaValue = MetaObject.forObject(newObject);
        set(prop, newObject);
        return metaValue;
    }

    public String[] getGetterNames() {
        return new String[0];
    }

    public String[] getSetterNames() {
        return new String[0];
    }

    public Class<?> getSetterType(String name) {
        return null;
    }

    public Class<?> getGetterType(String name) {
        return null;
    }

    public boolean hasSetter(String name) {
        return false;
    }

    public boolean hasGetter(String name) {
        return false;
    }

    public boolean isCollection() {
        return false;
    }

    public void add(Object element) {
        throw new UnsupportedOperationException();
    }

    public <E> void addAll(List<E> list) {
        throw new UnsupportedOperationException();
    }

}
