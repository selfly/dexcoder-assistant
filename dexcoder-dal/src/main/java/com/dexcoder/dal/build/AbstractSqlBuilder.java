package com.dexcoder.dal.build;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

import com.dexcoder.commons.annotation.Column;
import com.dexcoder.commons.annotation.Transient;
import com.dexcoder.commons.utils.ClassUtils;
import com.dexcoder.commons.utils.StrUtils;
import com.dexcoder.dal.handler.*;

/**
 * Created by liyd on 2015-12-7.
 */
public abstract class AbstractSqlBuilder implements SqlBuilder {

    /**
     * 表别名
     */
    protected String                  tableAlias;

    /**
     * 列
     */
    protected List<String>            columnFields;

    /**
     * 操作的字段
     */
    protected Map<String, AutoField>  autoFields;

    /**
     * parser map
     */
    protected Set<GenericTokenParser> tokenParsers;

    public AbstractSqlBuilder() {
        columnFields = new ArrayList<String>();
        autoFields = new LinkedHashMap<String, AutoField>();

    }

    public void setTableAlias(String alias) {
        this.tableAlias = alias;
    }

    public String getTableAlias() {
        return this.tableAlias;
    }

    public Map<String, AutoField> getFields() {
        return this.autoFields;
    }

    public boolean hasFields() {
        return (!autoFields.isEmpty());
    }

    public boolean hasField(String fieldName) {
        return (this.autoFields.get(fieldName) != null);
    }

    /**
     * 初始化 TokenParsers
     * 
     * @param clazz
     * @param nameHandler
     * @return
     */
    protected Set<GenericTokenParser> initTokenParsers(Class<?> clazz, NameHandler nameHandler) {
        if (tokenParsers == null) {
            tokenParsers = new HashSet<GenericTokenParser>(2);
            TokenHandler tokenHandler = new NativeTokenHandler(clazz, getTableAlias(), nameHandler);
            tokenParsers.add(new GenericTokenParser(AutoField.NATIVE_FIELD_TOKEN[0], AutoField.NATIVE_FIELD_TOKEN[1],
                tokenHandler));
            tokenHandler = new NativeTokenHandler(clazz, null, new NoneNameHandler());
            tokenParsers.add(new GenericTokenParser(AutoField.NATIVE_CODE_TOKEN[0], AutoField.NATIVE_CODE_TOKEN[1],
                tokenHandler));
        }
        return tokenParsers;
    }

    /**
     * TokenParsers 解析
     * 
     * @param content
     * @param clazz
     * @param nameHandler
     * @return
     */
    protected String tokenParse(String content, Class<?> clazz, NameHandler nameHandler) {
        Set<GenericTokenParser> tokenParsers = initTokenParsers(clazz, nameHandler);
        String result = content;
        for (GenericTokenParser tokenParser : tokenParsers) {
            result = tokenParser.parse(result);
        }
        return result;
    }

    /**
     * 合并entity中的field
     * 
     * @param entity
     * @param autoFieldType
     * @param nameHandler
     * @param isIgnoreNull
     */
    protected void mergeEntityFields(Object entity, AutoFieldType autoFieldType, NameHandler nameHandler,
                                     boolean isIgnoreNull) {
        if (entity == null) {
            return;
        }
        BeanInfo selfBeanInfo = ClassUtils.getSelfBeanInfo(entity.getClass());
        PropertyDescriptor[] propertyDescriptors = selfBeanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : propertyDescriptors) {
            Method readMethod = pd.getReadMethod();
            if (readMethod == null) {
                continue;
            }
            Transient aTransient = readMethod.getAnnotation(Transient.class);
            if (aTransient != null) {
                continue;
            }

            String fieldName;
            String alias;
            Column aColumn = readMethod.getAnnotation(Column.class);
            if (aColumn != null) {
                fieldName = aColumn.name();
                alias = aColumn.alias();
            } else {
                fieldName = pd.getName();
            }
            columnFields.add(fieldName);
            Object value = ClassUtils.invokeMethod(readMethod, entity);

            //忽略掉null
            if (value == null && isIgnoreNull) {
                continue;
            }
            //已经有了，以Criteria中为准
            if (this.hasField(fieldName)) {
                continue;
            }
            AutoField autoField = this.buildAutoField(fieldName, "and", "=", autoFieldType, value);
            this.autoFields.put(fieldName, autoField);
        }
    }

    /**
     * 构建操作的字段
     *
     * @param fieldName the field name
     * @param sqlOperator the build operator
     * @param fieldOperator the field operator
     * @param type the type
     * @param value the values
     * @return auto field
     */
    protected AutoField buildAutoField(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type,
                                       Object value) {
        AutoField autoField = new AutoField();
        autoField.setName(fieldName);
        autoField.setSqlOperator(sqlOperator);
        autoField.setFieldOperator(fieldOperator);
        autoField.setValue(value);
        autoField.setType(type);
        return autoField;
    }

    /**
     * 处理column别名
     * 
     * @param columnName
     * @return
     */
    protected String applyColumnAlias(String columnName) {
        if (StrUtils.isBlank(getTableAlias())) {
            return columnName;
        }
        return new StringBuilder(getTableAlias()).append(".").append(columnName).toString();
    }

    /**
     * 处理table别名
     * @param tableName
     * @return
     */
    protected String applyTableAlias(String tableName) {
        if (StrUtils.isBlank(getTableAlias())) {
            return tableName;
        }
        return new StringBuilder(tableName).append(" ").append(getTableAlias()).toString();
    }
}
