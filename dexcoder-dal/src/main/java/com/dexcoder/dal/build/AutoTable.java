package com.dexcoder.dal.build;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dexcoder.commons.utils.ClassUtils;
import com.dexcoder.commons.utils.StrUtils;
import com.dexcoder.dal.annotation.Column;
import com.dexcoder.dal.annotation.Table;
import com.dexcoder.dal.annotation.Transient;
import com.dexcoder.dal.handler.NameHandler;

/**
 * 表信息
 * 
 * Created by liyd on 2016-1-4.
 */
public class AutoTable {

    /**
     * 表名
     */
    private String                 tableName;

    /**
     * 别名
     */
    private String                 tableAlias;

    /**
     * 主键列名
     */
    private String                 pkColumnName;

    /**
     * 列
     */
    protected List<String>         columnFields;

    /**
     * 类
     */
    private Class<?>               tableClass;

    /**
     * 主键属性名
     */
    private String                 pkFieldName;

    /**
     * 操作的字段
     */
    private Map<String, AutoField> autoFields;

    /**
     * 名称处理器
     */
    private NameHandler            nameHandler;

    public AutoTable() {
        autoFields = new LinkedHashMap<String, AutoField>();
    }

    /**
     * 是否包含属性
     * 
     * @return
     */
    public boolean hasFields() {
        return (!autoFields.isEmpty());
    }

    /**
     * 是否包含某个属性
     * 
     * @param fieldName
     * @return
     */
    public boolean hasField(String fieldName) {
        return (this.autoFields.get(fieldName) != null);
    }

    public Map<String, AutoField> getAutoFields() {
        return autoFields;
    }

    //    /**
    //     * 处理column别名
    //     *
    //     * @param columnName
    //     * @return
    //     */
    //    protected String applyColumnAlias(String columnName) {
    //        if (StrUtils.isBlank(getTableAlias())) {
    //            return columnName;
    //        }
    //        return new StringBuilder(getTableAlias()).append(".").append(columnName).toString();
    //    }
    //
    //    /**
    //     * 处理table别名
    //     * @param tableName
    //     * @return
    //     */
    //    protected String applyTableAlias(String tableName) {
    //        if (StrUtils.isBlank(getTableAlias())) {
    //            return tableName;
    //        }
    //        return new StringBuilder(tableName).append(" ").append(getTableAlias()).toString();
    //    }

    public String getTableName() {
        return tableName;
    }

    public NameHandler getNameHandler() {
        return nameHandler;
    }

    public Class<?> getTableClass() {
        return tableClass;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    /**
     * AutoTable Builder
     */
    public static class Builder {

        private AutoTable autoTable = new AutoTable();

        public Builder(Class<?> clazz) {
            autoTable.tableClass = clazz;
        }

        public Builder tableAlias(String tableAlias) {
            autoTable.tableAlias = tableAlias;
            return this;
        }

        /**
         * 添加操作属性
         *
         * @param fieldName
         * @param sqlOperator
         * @param fieldOperator
         * @param type
         * @param value
         * @param fieldAlias
         */
        public void autoField(String fieldName, String sqlOperator, String fieldOperator, AutoFieldType type,
                              Object value, String fieldAlias) {
            AutoField autoField = new AutoField();
            autoField.setName(fieldName);
            autoField.setSqlOperator(sqlOperator);
            autoField.setFieldOperator(fieldOperator);
            autoField.setValue(value);
            autoField.setType(type);
            autoField.setAlias(fieldAlias);
            autoTable.autoFields.put(fieldName, autoField);
        }

        public Builder nameHandler(NameHandler nameHandler) {
            autoTable.nameHandler = nameHandler;
            return this;
        }

        /**
         * 合并entity中的field
         *
         * @param entity
         * @param autoFieldType
         * @param isIgnoreNull
         */
        public Builder mergeEntityFields(Object entity, AutoFieldType autoFieldType, boolean isIgnoreNull) {
            if (entity == null) {
                return this;
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
                String fieldAlias = null;
                Column aColumn = readMethod.getAnnotation(Column.class);
                if (aColumn != null) {
                    fieldName = aColumn.name();
                    fieldAlias = aColumn.alias();
                } else {
                    fieldName = pd.getName();
                }
                autoTable.columnFields.add(fieldName);
                //已经有了，以Criteria中为准
                if (autoTable.hasField(fieldName)) {
                    continue;
                }
                Object value = ClassUtils.invokeMethod(readMethod, entity);

                //忽略掉null
                if (value == null && isIgnoreNull) {
                    continue;
                }
                this.autoField(fieldName, "and", "=", autoFieldType, value, fieldAlias);
            }
            return this;
        }

        public AutoTable build() {
            assert autoTable.tableClass != null;

            Table aTable = autoTable.tableClass.getAnnotation(Table.class);
            if (aTable != null) {
                autoTable.tableName = aTable.name();
                autoTable.tableAlias = aTable.alias();
                autoTable.pkColumnName = aTable.pkColumn();
                autoTable.pkFieldName = aTable.pkField();
                if (!Object.class.equals(aTable.nameHandler())) {
                    autoTable.nameHandler = (NameHandler) ClassUtils.newInstance(aTable.nameHandler());
                }
            } else {
                autoTable.tableName = autoTable.nameHandler.getTableName(autoTable.getTableClass(),
                    autoTable.autoFields);
            }
            assert autoTable.tableName != null;
            assert autoTable.nameHandler != null;
            if (StrUtils.isBlank(autoTable.pkColumnName)) {
                autoTable.pkColumnName = autoTable.nameHandler.getPkColumnName(autoTable.tableClass);
            }
            if (StrUtils.isBlank(autoTable.pkFieldName)) {
                autoTable.pkFieldName = autoTable.nameHandler.getPkFieldName(autoTable.tableClass);
            }
            return autoTable;
        }
    }

}
