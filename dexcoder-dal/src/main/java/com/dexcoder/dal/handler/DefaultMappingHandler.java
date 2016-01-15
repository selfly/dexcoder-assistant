package com.dexcoder.dal.handler;

import java.util.Map;

import com.dexcoder.commons.utils.NameUtils;
import com.dexcoder.commons.utils.StrUtils;
import com.dexcoder.dal.build.AutoField;

/**
 * 默认名称处理handler
 * <p/>
 * User: liyd
 * Date: 2/12/14
 * Time: 4:51 PM
 */
public class DefaultMappingHandler implements MappingHandler {

    /**
     * 主键属性后缀
     */
    private static final String PRI_FIELD_SUFFIX  = "Id";

    /**
     * 主键列后缀
     */
    private static final String PRI_COLUMN_SUFFIX = "_ID";

    public String getTableName(Class<?> entityClass, Map<String, AutoField> fieldMap) {
        //Java属性的骆驼命名法转换回数据库下划线“_”分隔的格式
        return NameUtils.getUnderlineName(entityClass.getSimpleName());
    }

    public String getPkFieldName(Class<?> entityClass) {
        String firstLowerName = NameUtils.getFirstLowerName(entityClass.getSimpleName());
        //主键以类名加上“Id” 如user表主键属性即userId
        return firstLowerName + PRI_FIELD_SUFFIX;
    }

    public String getPkColumnName(Class<?> entityClass) {
        String underlineName = NameUtils.getUnderlineName(entityClass.getSimpleName());
        return underlineName + PRI_COLUMN_SUFFIX;
    }

    public String getColumnName(Class<?> entityClass, String fieldName) {
        //主键field跟column不一致的情况
        String pkFieldName = this.getPkFieldName(entityClass);
        if (StrUtils.equals(pkFieldName, fieldName)) {
            return this.getPkColumnName(entityClass);
        }
        return NameUtils.getUnderlineName(fieldName);
    }

    public String getPkNativeValue(Class<?> entityClass, String dialect) {
        if (StrUtils.equalsIgnoreCase(dialect, "oracle")) {
            //获取序列就可以了，默认seq_加上表名为序列名
            String tableName = this.getTableName(entityClass, null);
            return String.format("SEQ_%s.NEXTVAL", tableName);
        }
        return null;
    }
}
