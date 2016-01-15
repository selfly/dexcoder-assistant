package com.dexcoder.test.persistence;

import java.util.Map;

import com.dexcoder.commons.exceptions.AssistantException;
import com.dexcoder.commons.utils.NameUtils;
import com.dexcoder.dal.build.AutoField;
import com.dexcoder.dal.handler.DefaultNameHandler;
import com.dexcoder.test.model.Book;
import com.dexcoder.test.model.Chapter;

/**
 * 这里主要重写了分表的获取表名逻辑
 * <p/>
 * User: liyd
 * Date: 2/12/14
 * Time: 4:51 PM
 */
public class CustomNameHandler extends DefaultNameHandler {

    /**
     * 主键后缀
     */
    private static final String PRI_SUFFIX = "_ID";

    public String getTableName(Class<?> entityClass, Map<String, AutoField> fieldMap) {
        //Java属性的骆驼命名法转换回数据库下划线“_”分隔的格式
        String tableName = NameUtils.getUnderlineName(entityClass.getSimpleName());
        if (Book.class.equals(entityClass)) {
            AutoField autoField = fieldMap.get("bookId");
            if (autoField == null || autoField.getValue() == null) {
                throw new AssistantException("书籍bookId不能为空");
            }
            if (!(autoField.getValue() instanceof Long)) {
                throw new AssistantException("书籍bookId错误");
            }
            Long id = (Long) autoField.getValue();
            //书籍3张表
            long tableNum = id % 3;
            return tableName + "_" + tableNum;
        } else if (Chapter.class.equals(entityClass)) {
            AutoField autoField = fieldMap.get("bookId");
            if (autoField == null || autoField.getValue() == null) {
                throw new AssistantException("章节bookId不能为空");
            }
            if (!(autoField.getValue() instanceof Long)) {
                throw new AssistantException("书籍bookId错误");
            }
            Long id = (Long) autoField.getValue();
            //章节5张表
            long tableNum = id % 5;
            return tableName + "_" + tableNum;
        }
        return tableName;
    }
}
