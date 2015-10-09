package com.dexcoder.assistant.persistence;

import org.apache.commons.lang.StringUtils;

import com.dexcoder.assistant.utils.NameUtils;

/**
 * 默认名称处理handler
 * 
 * User: liyd
 * Date: 2/12/14
 * Time: 4:51 PM
 */
public class DefaultNameHandler implements NameHandler {

    /** 主键后缀 */
    private static final String PRI_SUFFIX = "_ID";

    /**
     * 根据实体名获取表名
     *
     * @param entityClass
     * @return
     */
    public String getTableName(Class<?> entityClass) {
        //Java属性的骆驼命名法转换回数据库下划线“_”分隔的格式
        return NameUtils.getUnderlineName(entityClass.getSimpleName());
    }

    /**
     * 根据表名获取主键名
     *
     * @param entityClass
     * @return
     */
    public String getPKName(Class<?> entityClass) {
        String underlineName = NameUtils.getUnderlineName(entityClass.getSimpleName());
        //主键以表名加上“_id” 如user表主键即“user_id”
        return underlineName + PRI_SUFFIX;
    }

    /**
     * 根据属性名获取列名
     *
     * @param fieldName
     * @return
     */
    public String getColumnName(String fieldName) {
        String underlineName = NameUtils.getUnderlineName(fieldName);
        return underlineName;
    }

    /**
     * 根据实体名获取主键值 自增类主键数据库直接返回null即可
     *
     * @param entityClass the entity class
     * @param dialect the dialect
     * @return pK value
     */
    public String getPKValue(Class<?> entityClass, String dialect) {
        if (StringUtils.equalsIgnoreCase(dialect, "oracle")) {
            //获取序列就可以了，默认seq_加上表名为序列名
            String tableName = this.getTableName(entityClass);
            return String.format("SEQ_%s.NEXTVAL", tableName);
        }
        return null;
    }
}
