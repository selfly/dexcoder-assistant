package com.dexcoder.dal.handler;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dexcoder.commons.utils.NameUtils;
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

    public String getColumnName(Class<?> entityClass, String fieldName, String annColumnName) {

        //默认如果有注解的列名,直接返回
        if (StringUtils.isNotBlank(annColumnName)) {
            return annColumnName;
        }
        return NameUtils.getUnderlineName(fieldName);
    }

}
