package com.dexcoder.assistant.persistence.manual;

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
//        this.metaClass = MetaClass.forClass(object.getClass(), metaObject.getReflectorFactory());
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

    public String findProperty(String name, boolean useCamelCaseMapping) {
        return null;
//        return metaClass.findProperty(name, useCamelCaseMapping);
    }

    public String[] getGetterNames() {
        return null;
//        return metaClass.getGetterNames();
    }


    public String[] getSetterNames() {
        return null;
//        return metaClass.getSetterNames();
    }

    public Class<?> getSetterType(String name) {
        return null;
//        PropertyTokenizer prop = new PropertyTokenizer(name);
//        if (prop.hasNext()) {
//            MetaObject metaValue = metaObject.metaObjectForProperty(prop.getIndexedName());
//            if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
//                return metaClass.getSetterType(name);
//            } else {
//                return metaValue.getSetterType(prop.getChildren());
//            }
//        } else {
//            return metaClass.getSetterType(name);
//        }
    }

    public Class<?> getGetterType(String name) {
        return null;
//        PropertyTokenizer prop = new PropertyTokenizer(name);
//        if (prop.hasNext()) {
//            MetaObject metaValue = metaObject.metaObjectForProperty(prop.getIndexedName());
//            if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
//                return metaClass.getGetterType(name);
//            } else {
//                return metaValue.getGetterType(prop.getChildren());
//            }
//        } else {
//            return metaClass.getGetterType(name);
//        }
    }

    public boolean hasSetter(String name) {
        return true;
//        PropertyTokenizer prop = new PropertyTokenizer(name);
//        if (prop.hasNext()) {
//            if (metaClass.hasSetter(prop.getIndexedName())) {
//                MetaObject metaValue = metaObject.metaObjectForProperty(prop.getIndexedName());
//                if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
//                    return metaClass.hasSetter(name);
//                } else {
//                    return metaValue.hasSetter(prop.getChildren());
//                }
//            } else {
//                return false;
//            }
//        } else {
//            return metaClass.hasSetter(name);
//        }
    }

    public boolean hasGetter(String name) {
        return true;
//        PropertyTokenizer prop = new PropertyTokenizer(name);
//        if (prop.hasNext()) {
//            if (metaClass.hasGetter(prop.getIndexedName())) {
//                MetaObject metaValue = metaObject.metaObjectForProperty(prop.getIndexedName());
//                if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
//                    return metaClass.hasGetter(name);
//                } else {
//                    return metaValue.hasGetter(prop.getChildren());
//                }
//            } else {
//                return false;
//            }
//        } else {
//            return metaClass.hasGetter(name);
//        }
    }

//    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop) {
//        MetaObject metaValue;
//        Class<?> type = getSetterType(prop.getName());
//        try {
//            Object newObject = objectFactory.create(type);
//            metaValue = MetaObject.forObject(newObject, metaObject.getObjectFactory(), metaObject.getObjectWrapperFactory(), metaObject.getReflectorFactory());
//            set(prop, newObject);
//        } catch (Exception e) {
//            throw new ReflectionException("Cannot set value of property '" + name + "' because '" + name + "' is null and cannot be instantiated on instance of " + type.getName() + ". Cause:" + e.toString(), e);
//        }
//        return metaValue;
//    }

    private Object getBeanProperty(PropertyTokenizer prop, Object object) {
        return null;
//        try {
//            Invoker method = metaClass.getGetInvoker(prop.getName());
//            try {
//                return method.invoke(object, NO_ARGUMENTS);
//            } catch (Throwable t) {
//                throw ExceptionUtil.unwrapThrowable(t);
//            }
//        } catch (RuntimeException e) {
//            throw e;
//        } catch (Throwable t) {
//            throw new ReflectionException("Could not get property '" + prop.getName() + "' from " + object.getClass() + ".  Cause: " + t.toString(), t);
//        }
    }

    private void setBeanProperty(PropertyTokenizer prop, Object object, Object value) {
//        try {
//            Invoker method = metaClass.getSetInvoker(prop.getName());
//            Object[] params = {value};
//            try {
//                method.invoke(object, params);
//            } catch (Throwable t) {
//                throw ExceptionUtil.unwrapThrowable(t);
//            }
//        } catch (Throwable t) {
//            throw new ReflectionException("Could not set property '" + prop.getName() + "' of '" + object.getClass() + "' with value '" + value + "' Cause: " + t.toString(), t);
//        }
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
