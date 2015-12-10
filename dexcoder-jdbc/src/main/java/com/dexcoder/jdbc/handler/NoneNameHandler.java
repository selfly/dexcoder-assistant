package com.dexcoder.jdbc.handler;

import java.util.Map;

import com.dexcoder.jdbc.build.AutoField;

/**
 * 不作任务特殊处理的nameHandler
 * 
 * Created by liyd on 2015-12-9.
 */
public class NoneNameHandler implements NameHandler {

    public String getTableName(Class<?> entityClass, Map<String, AutoField> fieldMap) {
        return entityClass.getSimpleName();
    }

    public String getPkFieldName(Class<?> entityClass) {
        return entityClass.getSimpleName() + "Id";
    }

    public String getPkColumnName(Class<?> entityClass) {
        return entityClass.getSimpleName() + "Id";
    }

    public String getColumnName(Class<?> entityClass, String fieldName) {
        return fieldName;
    }

    public String getPkNativeValue(Class<?> entityClass, String dialect) {
        return null;
    }
}
