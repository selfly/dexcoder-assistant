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
import com.dexcoder.dal.handler.MappingHandler;

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
    private MappingHandler mappingHandler;

    /**
     * 列
     */
    private List<AutoField>        columnAutoFields;

    /**
     * 操作的字段
     */
    private Map<String, AutoField> autoFields;

    /**
     * 白名单
     */
    private List<String>           includeFields;

    /**
     * 黑名单
     */
    private List<String>           excludeFields;

    /**
     * 函数
     */
    private List<AutoField>        funcAutoFields;

    /**
     * 是否和字段互斥
     */
    private boolean                isFieldExclusion = false;

    /**
     * 是否需要排序
     */
    private boolean                isOrderBy        = true;

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

    public String getPkColumnName() {
        if (StrUtils.isBlank(pkColumnName)) {
            pkColumnName = mappingHandler.getPkColumnName(tableClass);
        }
        return pkColumnName;
    }

    public String getPkFieldName() {
        if (StrUtils.isBlank(pkFieldName)) {
            pkFieldName = this.mappingHandler.getPkFieldName(tableClass);
        }
        return pkFieldName;
    }

    public String getPkFieldName(MappingHandler mappingHandler) {
        if (StrUtils.isBlank(pkFieldName)) {
            pkFieldName = mappingHandler.getPkFieldName(tableClass);
        }
        return pkFieldName;
    }

    public Class<?> getTableClass() {
        return tableClass;
    }

    public List<AutoField> getColumnAutoFields() {
        return columnAutoFields;
    }

    /**
     * 是否拥用列
     *
     * @return
     */
    public boolean hasColumnFields() {
        return this.columnAutoFields != null && !this.columnAutoFields.isEmpty();
    }

    /**
     * 是否包含属性
     *
     * @return
     */
    public boolean hasAutoFields() {
        return !autoFields.isEmpty();
    }

    /**
     * 是否包含某个属性
     *
     * @param fieldName
     * @return
     */
    public boolean hasAutoField(String fieldName) {
        return this.autoFields.get(fieldName) != null;
    }

    public Map<String, AutoField> getAutoFields() {
        return autoFields;
    }

    /**
     * 获取有表别名的column名
     *
     * @param autoField
     * @return
     */
    public String getColumnAndTableAliasName(AutoField autoField) {
        if (StrUtils.isBlank(autoField.getAnnotationName())) {
            return getColumnAndTableAliasName(autoField.getName());
        }
        if (StrUtils.isBlank(this.tableAlias)) {
            return autoField.getAnnotationName();
        }
        return new StringBuilder(this.tableAlias).append(".").append(autoField.getAnnotationName()).toString();
    }

    public String getColumnAndTableAliasName(String fieldName) {
        String columnName = StrUtils.equals(fieldName, getPkFieldName()) ? getPkColumnName() : mappingHandler
            .getColumnName(tableClass, fieldName);
        if (StrUtils.isBlank(this.tableAlias)) {
            return columnName;
        }
        return new StringBuilder(this.tableAlias).append(".").append(columnName).toString();
    }

    public String applyColumnTableAlias(String columnName) {
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
        return funcAutoFields != null && !funcAutoFields.isEmpty();
    }

    public boolean isIncludeField(String fieldName) {
        if (this.includeFields == null || this.includeFields.isEmpty()) {
            //名单为空，默认全是白名单
            return true;
        }
        return includeFields.contains(fieldName);
    }

    public boolean isExcludeField(String fieldName) {
        if (this.excludeFields == null || this.excludeFields.isEmpty()) {
            return false;
        }
        return excludeFields.contains(fieldName);
    }

    /**
     * 获取表名
     * <pre>
     *     如果有注解，直接返回注解的表名
     *     否则用mappingHandler获取，这个必须实时获取以便在水平分表时能正确处理表名
     * </pre>
     *
     * @return
     */
    public String getTableName() {
        if (StrUtils.isNotBlank(annotationTableName)) {
            return annotationTableName;
        }
        return this.mappingHandler.getTableName(this.tableClass, this.autoFields);
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

        public Builder initColumnAutoFields() {
            metaTable.columnAutoFields = new ArrayList<AutoField>();
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
                if (!Object.class.equals(aTable.mappingHandler())) {
                    metaTable.mappingHandler = ((MappingHandler) ClassUtils.newInstance(aTable.mappingHandler()));
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
                String fieldName = pd.getName();
                String fieldAnnotationName = null;
                Column aColumn = readMethod.getAnnotation(Column.class);
                if (aColumn != null) {
                    fieldAnnotationName = aColumn.name();
                }
                if (metaTable.columnAutoFields != null) {
                    AutoField autoField = new AutoField.Builder().name(fieldName).annotationName(fieldAnnotationName)
                        .build();
                    metaTable.columnAutoFields.add(autoField);
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
                AutoField autoField = new AutoField.Builder().name(fieldName).annotationName(fieldAnnotationName)
                    .logicalOperator("and").fieldOperator("=").type(AutoFieldType.NORMAL).value(value).build();
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

        public Builder mappingHandler(MappingHandler mappingHandler) {
            metaTable.mappingHandler = mappingHandler;
            return this;
        }

        public MetaTable build() {
            assert metaTable.tableClass != null;
            //            assert metaTable.mappingHandler != null;
            return this.metaTable;
        }
    }

}
