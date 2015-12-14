package com.dexcoder.jdbc.batis.wrapper;

import java.util.Collection;
import java.util.List;

import com.dexcoder.jdbc.batis.build.PropertyTokenizer;
import com.dexcoder.jdbc.batis.reflection.MetaObject;

/**
 * Created by liyd on 2015-12-1.
 */
public class CollectionWrapper implements ObjectWrapper {

    private Collection<Object> object;

    public CollectionWrapper(MetaObject metaObject, Collection<Object> object) {
        this.object = object;
    }

    public Object get(PropertyTokenizer prop) {
        throw new UnsupportedOperationException();
    }

    public void set(PropertyTokenizer prop, Object value) {
        throw new UnsupportedOperationException();
    }

//    public boolean hasGetter(String name) {
//        throw new UnsupportedOperationException();
//    }

    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop) {
        throw new UnsupportedOperationException();
    }
//
//    public boolean isCollection() {
//        return true;
//    }

//    public void add(Object element) {
//        object.add(element);
//    }

//    public <E> void addAll(List<E> element) {
//        object.addAll(element);
//    }

}
