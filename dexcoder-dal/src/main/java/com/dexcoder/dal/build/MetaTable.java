package com.dexcoder.dal.build;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
public class MetaTable {

    /**
     * 注解表名
     */
    private String                 annotationTableName;

    /**
     * 别名
     */
    private String                 tableAlias;

    /**
     * 主键列名
     */
    private String                 pkColumnName;

    /**
     * 类
     */
    private Class<?>               tableClass;

    /**
     * 主键属性名
     */
    private String                 pkFieldName;

    /**
     * 名称处理器
     */
    private NameHandler            nameHandler;

    /**
     * 列
     */
    protected List<String>         columnFields;

    /**
     * 操作的字段
     */
    private Map<String, AutoField> autoFields;

    /**
     * 白名单
     */
    protected List<String>         includeFields;

    /**
     * 黑名单
     */
    protected List<String>         excludeFields;

    /**
     * 函数
     */
    protected List<AutoField>      funcAutoFields;

    /**
     * 是否和字段互斥
     */
    private boolean              isFieldExclusion = false;

    /**
     * 是否需要排序
     */
    private boolean              isOrderBy        = true;

    private MetaTable() {
    }

    public List<String> getIncludeFields() {
        return includeFields;
    }

    public List<String> getExcludeFields() {
        return excludeFields;
    }

    public List<AutoField> getFuncAutoFields() {
        return funcAutoFields;
    }

    public String getPkFieldName() {
        if (StrUtils.isBlank(pkFieldName)) {
            pkFieldName = nameHandler.getPkFieldName(tableClass);
        }
        return pkFieldName;
    }

    public List<String> getColumnFields() {
        return columnFields;
    }

    /**
     * 是否拥用列
     * 
     * @return
     */
    public boolean hasColumnFields() {
        return (this.columnFields != null && !this.columnFields.isEmpty());
    }

    /**
     * 是否包含属性
     * 
     * @return
     */
    public boolean hasAutoFields() {
        return (!autoFields.isEmpty());
    }

    /**
     * 是否包含某个属性
     * 
     * @param fieldName
     * @return
     */
    public boolean hasAutoField(String fieldName) {
        return (this.autoFields.get(fieldName) != null);
    }

    public Map<String, AutoField> getAutoFields() {
        return autoFields;
    }

    /**
     * 获取有表别名的column名
     *
     * @param fieldName
     * @return
     */
    public String getColumnAndTableAliasName(String fieldName) {
        String columnName = StrUtils.equals(fieldName, pkFieldName) ? pkColumnName : nameHandler.getColumnName(
            tableClass, fieldName);
        if (StrUtils.isBlank(this.tableAlias)) {
            return columnName;
        }
        return new StringBuilder(this.tableAlias).append(".").append(columnName).toString();
    }

    /**
     * 获取有表别名的table名
     * @return
     */
    public String getTableAndAliasName() {
        String tableName = getTableName();
        if (StrUtils.isBlank(this.tableAlias)) {
            return tableName;
        }
        return new StringBuilder(tableName).append(" ").append(this.tableAlias).toString();
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public boolean hasFuncAutoField() {
        return (funcAutoFields != null && !funcAutoFields.isEmpty());
    }

    public boolean isIncludeField(String fieldName) {
        if (this.includeFields == null) {
            return false;
        }
        return includeFields.contains(fieldName);
    }

    public boolean isExcludeField(String fieldName) {
        if (this.excludeFields == null) {
            return false;
        }
        return excludeFields.contains(fieldName);
    }

    /**
     * 获取表名
     * <pre>
     *     如果有注解，直接返回注解的表名
     *     否则用nameHandler获取，这个必须实时获取以便在水平分表时能正确处理表名
     * </pre>
     * 
     * @return
     */
    public String getTableName() {
        if (StrUtils.isNotBlank(annotationTableName)) {
            return annotationTableName;
        }
        return this.nameHandler.getTableName(this.tableClass, this.autoFields);
    }

    public boolean isFieldExclusion() {
        return isFieldExclusion;
    }

    public boolean isOrderBy() {
        return isOrderBy;
    }

    /**
     * MetaTable Builder
     */
    public static class Builder {

        private MetaTable metaTable;

        public Builder() {
            metaTable = new MetaTable();
        }

        public Builder(MetaTable metaTable) {
            this.metaTable = metaTable;
        }

        public Builder initAutoFields() {
            metaTable.autoFields = new LinkedHashMap<String, AutoField>();
            return this;
        }

        public Builder initColumnFields() {
            metaTable.columnFields = new ArrayList<String>();
            return this;
        }

        public Builder initIncludeFields() {
            metaTable.includeFields = new ArrayList<String>();
            return this;
        }

        public Builder initExcludeFields() {
            metaTable.excludeFields = new ArrayList<String>();
            return this;
        }

        public Builder initFuncAutoFields() {
            metaTable.funcAutoFields = new ArrayList<AutoField>();
            return this;
        }

        public Builder tableClass(Class<?> tableClass) {
            metaTable.tableClass = tableClass;
            Table aTable = tableClass.getAnnotation(Table.class);
            if (aTable != null) {
                metaTable.annotationTableName = aTable.name();
                metaTable.pkColumnName = aTable.pkColumn();
                metaTable.pkFieldName = aTable.pkField();
                if (StrUtils.isBlank(metaTable.tableAlias)) {
                    metaTable.tableAlias = aTable.alias();
                }
                if (!Object.class.equals(aTable.nameHandler())) {
                    metaTable.nameHandler = ((NameHandler) ClassUtils.newInstance(aTable.nameHandler()));
                }
            }
            return this;
        }

        public Builder tableAlias(String tableAlias) {
            metaTable.tableAlias = tableAlias;
            return this;
        }

        public Builder entity(Object entity, boolean isIgnoreNull) {
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
                if (metaTable.columnFields != null) {
                    metaTable.columnFields.add(fieldName);
                }
                //已经有了，以Criteria中为准
                if (metaTable.hasAutoField(fieldName)) {
                    continue;
                }
                Object value = ClassUtils.invokeMethod(readMethod, entity);

                //忽略掉null
                if (value == null && isIgnoreNull) {
                    continue;
                }
                AutoField autoField = AutoField.Builder.build(fieldName, "and", "=", AutoFieldType.NORMAL, value,
                    fieldAlias);
                metaTable.getAutoFields().put(fieldName, autoField);
            }
            return this;
        }

        public Builder isFieldExclusion(boolean isFieldExclusion) {
            metaTable.isFieldExclusion = isFieldExclusion;
            return this;
        }

        public Builder isOrderBy(boolean isOrderBy) {
            metaTable.isOrderBy = isOrderBy;
            return this;
        }

        public Builder nameHandler(NameHandler nameHandler) {
            metaTable.nameHandler = nameHandler;
            return this;
        }

        public MetaTable build() {
            return this.metaTable;
        }
    }

}
