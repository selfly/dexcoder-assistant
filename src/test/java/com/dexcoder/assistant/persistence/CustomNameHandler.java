package com.dexcoder.assistant.persistence;

import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import com.dexcoder.assistant.exceptions.AssistantException;
import com.dexcoder.assistant.model.Book;
import com.dexcoder.assistant.model.Chapter;
import com.dexcoder.assistant.utils.NameUtils;

/**
 * 默认名称处理handler
 * 
 * User: liyd
 * Date: 2/12/14
 * Time: 4:51 PM
 */
public class CustomNameHandler implements NameHandler {

    /** 主键后缀 */
    private static final String PRI_SUFFIX = "_ID";

    public String getTableName(Class<?> entityClass, Map<String, AutoField> fieldMap) {
        //Java属性的骆驼命名法转换回数据库下划线“_”分隔的格式
        String tableName = NameUtils.getUnderlineName(entityClass.getSimpleName());
        if (Book.class.equals(entityClass)) {
            AutoField autoField = fieldMap.get("bookId");
            if (autoField == null || ArrayUtils.isEmpty(autoField.getValues())
                || autoField.getValues()[0] == null) {
                throw new AssistantException("书籍bookId不能为空");
            }
            Long id = (Long) autoField.getValues()[0];
            //书籍3张表
            long tableNum = id % 3;
            return tableName + "_" + tableNum;
        } else if (Chapter.class.equals(entityClass)) {
            AutoField autoField = fieldMap.get("bookId");
            if (autoField == null || ArrayUtils.isEmpty(autoField.getValues())
                || autoField.getValues()[0] == null) {
                throw new AssistantException("章节bookId不能为空");
            }
            Long id = (Long) autoField.getValues()[0];
            //章节5张表
            long tableNum = id % 5;
            return tableName + "_" + tableNum;
        }
        return tableName;
    }

    public String getPKName(Class<?> entityClass) {
        String underlineName = NameUtils.getUnderlineName(entityClass.getSimpleName());
        //主键以表名加上“_id” 如user表主键即“user_id”
        return underlineName + PRI_SUFFIX;
    }

    public String getColumnName(String fieldName) {
        String underlineName = NameUtils.getUnderlineName(fieldName);
        return underlineName;
    }

    public String getPKValue(Class<?> entityClass, String dialect) {
        return null;
    }
}
