package com.dexcoder.jdbc.batis.wrapper;

import com.dexcoder.jdbc.batis.reflection.MetaObject;
import com.dexcoder.jdbc.batis.build.PropertyTokenizer;

import java.util.List;

/**
 * Created by liyd on 2015-11-30.
 */
public interface ObjectWrapper {

    Object get(PropertyTokenizer prop);

    void set(PropertyTokenizer prop, Object value);

    String findProperty(String name, boolean useCamelCaseMapping);

    String[] getGetterNames();

    String[] getSetterNames();

//    Class<?> getSetterType(String name);
//
//    Class<?> getGetterType(String name);

    boolean hasSetter(String name);

    boolean hasGetter(String name);

    MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop);

    boolean isCollection();

    public void add(Object element);

    public <E> void addAll(List<E> element);

}
