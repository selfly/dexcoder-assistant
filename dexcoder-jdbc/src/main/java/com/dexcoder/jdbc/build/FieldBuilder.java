//package com.dexcoder.jdbc.build;
//
//import com.dexcoder.jdbc.BoundSql;
//import com.dexcoder.jdbc.handler.NameHandler;
//import com.dexcoder.jdbc.utils.StrUtils;
//
///**
// * Created by liyd on 2015-12-8.
// */
//public class FieldBuilder extends WhereBuilder {
//
//    public BoundSql build(Class<?> clazz, Object entity, boolean isIgnoreNull, NameHandler nameHandler) {
//        BoundSql boundSql = super.build(clazz, entity, isIgnoreNull, nameHandler);
//        return new CriteriaBoundSql(StrUtils.substring(boundSql.getSql(), super.COMMAND_OPEN.length()), boundSql.getParameters());
//    }
//}
