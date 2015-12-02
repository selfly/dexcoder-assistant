//package com.dexcoder.assistant.persistence;
//
//import java.util.List;
//
//import org.apache.commons.lang.StringUtils;
//
//import com.dexcoder.assistant.exceptions.AssistantException;
//
///**
// * Created by liyd on 2015-11-18.
// */
//public class ListWrapper implements ObjectWrapper {
//
//    private List<Object> list;
//
//    public ListWrapper(List<Object> list) {
//        this.list = list;
//    }
//
//    public Object getValue(String name) {
//        if (!StringUtils.isNumeric(name)) {
//            throw new AssistantException("参数设置错误:" + name);
//        }
//        return list.get(Integer.valueOf(name));
//    }
//}
