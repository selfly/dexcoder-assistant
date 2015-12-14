package com.dexcoder.jdbc.batis.wrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dexcoder.jdbc.batis.build.PropertyTokenizer;
import com.dexcoder.jdbc.batis.build.SystemMetaObject;
import com.dexcoder.jdbc.batis.reflection.MetaObject;

/**
 * Created by liyd on 2015-12-1.
 */
public class MapWrapper extends BaseWrapper {

    private Map<String, Object> map;

    public MapWrapper(MetaObject metaObject, Map<String, Object> map) {
        super(metaObject);
        this.map = map;
    }

    public Object get(PropertyTokenizer prop) {
        if (prop.getIndex() != null) {
            Object collection = resolveCollection(prop, map);
            return getCollectionValue(prop, collection);
        } else {
            return map.get(prop.getName());
        }
    }

    public void set(PropertyTokenizer prop, Object value) {
        if (prop.getIndex() != null) {
            Object collection = resolveCollection(prop, map);
            setCollectionValue(prop, collection, value);
        } else {
            map.put(prop.getName(), value);
        }
    }

//    public boolean hasGetter(String name) {
//        PropertyTokenizer prop = new PropertyTokenizer(name);
//        if (prop.hasNext()) {
//            if (map.containsKey(prop.getIndexedName())) {
//                MetaObject metaValue = metaObject.metaObjectForProperty(prop.getIndexedName());
//                if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
//                    return true;
//                } else {
//                    return metaValue.hasGetter(prop.getChildren());
//                }
//            } else {
//                return false;
//            }
//        } else {
//            return map.containsKey(prop.getName());
//        }
//    }

    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        set(prop, map);
        return MetaObject.forObject(map);
    }

//    public boolean isCollection() {
//        return false;
//    }

//    public void add(Object element) {
//        throw new UnsupportedOperationException();
//    }
//
//    public <E> void addAll(List<E> element) {
//        throw new UnsupportedOperationException();
//    }

}
