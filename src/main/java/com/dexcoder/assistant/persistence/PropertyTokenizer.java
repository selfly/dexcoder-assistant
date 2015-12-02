//package com.dexcoder.assistant.persistence;
//
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by liyd on 2015-11-18.
// */
//public class PropertyTokenizer implements Iterable<PropertyTokenizer>, Iterator<PropertyTokenizer> {
//    private String        name;
//    private String        indexedName;
//    private String        index;
//    private String        children;
//
//    private ObjectWrapper objectWrapper;
//
//    public PropertyTokenizer(String fullname, Object object) {
//        int delim = fullname.indexOf('.');
//        if (delim > -1) {
//            name = fullname.substring(0, delim);
//            children = fullname.substring(delim + 1);
//        } else {
//            name = fullname;
//            children = null;
//        }
//        indexedName = name;
//        delim = name.indexOf('[');
//        if (delim > -1) {
//            index = name.substring(delim + 1, name.length() - 1);
//            name = name.substring(0, delim);
//        }
//
//        if (object instanceof Map) {
//            this.objectWrapper = new MapWrapper((Map<String, Object>) object);
//        } else if (object instanceof List) {
//            this.objectWrapper = new ListWrapper((List<Object>) object);
//        } else {
//            this.objectWrapper = new BeanWrapper(object);
//        }
//    }
//
//    public ObjectWrapper getObjectWrapper() {
//        return objectWrapper;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getIndex() {
//        return index;
//    }
//
//    public String getIndexedName() {
//        return indexedName;
//    }
//
//    public String getChildren() {
//        return children;
//    }
//
//    public boolean hasNext() {
//        return children != null;
//    }
//
//    public PropertyTokenizer next() {
//        //        return new PropertyTokenizer(children);
//        return null;
//    }
//
//    public void remove() {
//        throw new UnsupportedOperationException(
//            "Remove is not supported, as it has no meaning in the context of properties.");
//    }
//
//    public Iterator<PropertyTokenizer> iterator() {
//        return this;
//    }
//}
