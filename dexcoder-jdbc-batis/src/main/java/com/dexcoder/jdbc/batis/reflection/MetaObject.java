package com.dexcoder.jdbc.batis.reflection;

import com.dexcoder.jdbc.batis.build.PropertyTokenizer;
import com.dexcoder.jdbc.batis.build.SystemMetaObject;
import com.dexcoder.jdbc.batis.wrapper.BeanWrapper;
import com.dexcoder.jdbc.batis.wrapper.CollectionWrapper;
import com.dexcoder.jdbc.batis.wrapper.MapWrapper;
import com.dexcoder.jdbc.batis.wrapper.ObjectWrapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by liyd on 2015-11-25.
 */
public class MetaObject {

    private Object        originalObject;
    private ObjectWrapper objectWrapper;

    private MetaObject(Object object) {
        this.originalObject = object;

        if (object instanceof ObjectWrapper) {
            this.objectWrapper = (ObjectWrapper) object;
        } else if (object instanceof Map) {
            this.objectWrapper = new MapWrapper(this, (Map) object);
        } else if (object instanceof Collection) {
            this.objectWrapper = new CollectionWrapper(this, (Collection) object);
        } else {
            this.objectWrapper = new BeanWrapper(this, object);
        }
    }

    public static MetaObject forObject(Object object) {
        if (object == null) {
            return SystemMetaObject.NULL_META_OBJECT;
        } else {
            return new MetaObject(object);
        }
    }

    public Object getOriginalObject() {
        return originalObject;
    }

    //    public String findProperty(String propName, boolean useCamelCaseMapping) {
    //        return objectWrapper.findProperty(propName, useCamelCaseMapping);
    //    }

    //    public String[] getGetterNames() {
    //        return objectWrapper.getGetterNames();
    //    }
    //
    //    public String[] getSetterNames() {
    //        return objectWrapper.getSetterNames();
    //    }

    //    public Class<?> getSetterType(String name) {
    //        return objectWrapper.getSetterType(name);
    //    }
    //
    //    public Class<?> getGetterType(String name) {
    //        return objectWrapper.getGetterType(name);
    //    }

    //    public boolean hasSetter(String name) {
    //        return objectWrapper.hasSetter(name);
    //    }

//    public boolean hasGetter(String name) {
//        return objectWrapper.hasGetter(name);
//    }

    public Object getValue(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = metaObjectForProperty(prop.getIndexedName());
            if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                return null;
            } else {
                return metaValue.getValue(prop.getChildren());
            }
        } else {
            return objectWrapper.get(prop);
        }
    }

    public void setValue(String name, Object value) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = metaObjectForProperty(prop.getIndexedName());
            if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                if (value == null && prop.getChildren() != null) {
                    // don't instantiate child path if value is null
                    return;
                } else {
                    metaValue = objectWrapper.instantiatePropertyValue(name, prop);
                }
            }
            metaValue.setValue(prop.getChildren(), value);
        } else {
            objectWrapper.set(prop, value);
        }
    }

    public MetaObject metaObjectForProperty(String name) {
        Object value = getValue(name);
        return MetaObject.forObject(value);
    }

//    public ObjectWrapper getObjectWrapper() {
//        return objectWrapper;
//    }
//
//    public boolean isCollection() {
//        return objectWrapper.isCollection();
//    }

//    public void add(Object element) {
//        objectWrapper.add(element);
//    }

//    public <E> void addAll(List<E> list) {
//        objectWrapper.addAll(list);
//    }

}
