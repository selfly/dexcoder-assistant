package com.dexcoder.dal.build;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.dexcoder.commons.utils.ClassUtils;
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
    private MappingHandler         mappingHandler;

    /**
     * select 列
     */
    private List<AutoField>        columnAutoFields;

    /**
     * 操作的各类字段
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

    /**
     * 类的属性
     */
    private Set<String>              classFields;

    /** field -> column 映射 名称不一样时 */
    private Map<String, String>    mappedFieldColumns;

    public MetaTable() {
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

    public Set<String> getClassFields() {
        return classFields;
    }

    public String getPkFieldName() {
        if (StringUtils.isBlank(pkFieldName)) {
            pkFieldName = this.mappingHandler.getPkFieldName(tableClass);
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
     * 根据属性名获取列名
     *
     * @param fieldName
     * @return
     */
    public String getColumnName(String fieldName) {
        String annColumnName = this.mappedFieldColumns.get(fieldName);
        return this.mappingHandler.getColumnName(tableClass, fieldName, annColumnName);
    }

    /**
     * 是否拥有列
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

    public MappingHandler getMappingHandler() {
        return mappingHandler;
    }

    /**
     * 获取有表别名的column名
     *
     * @param autoField
     * @return
     */
    public String getColumnAndTableAliasName(AutoField autoField) {

        return getColumnAndTableAliasName(autoField.getName());
    }

    public String getColumnAndTableAliasName(String fieldName) {
        String annColumnName = this.mappedFieldColumns.get(fieldName);
        String columnName = mappingHandler.getColumnName(tableClass, fieldName, annColumnName);
        if (StringUtils.isBlank(this.tableAlias)) {
            return columnName;
        }
        return new StringBuilder(this.tableAlias).append(".").append(columnName).toString();
    }

    public String applyColumnTableAlias(String columnName) {
        if (StringUtils.isBlank(this.tableAlias)) {
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
        if (StringUtils.isBlank(this.tableAlias)) {
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
        if (StringUtils.isNotBlank(annotationTableName)) {
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

    public MetaTable initAutoFields() {
        this.autoFields = new LinkedHashMap<String, AutoField>();
        return this;
    }

    public MetaTable initColumnAutoFields() {
        this.columnAutoFields = new ArrayList<AutoField>();
        return this;
    }

    public MetaTable initIncludeFields() {
        this.includeFields = new ArrayList<String>();
        return this;
    }

    public MetaTable initExcludeFields() {
        this.excludeFields = new ArrayList<String>();
        return this;
    }

    public MetaTable initFuncAutoFields() {
        this.funcAutoFields = new ArrayList<AutoField>();
        return this;
    }

    public MetaTable tableClass(Class<?> tableClass) {
        this.classFields = new HashSet<String>();
        this.mappedFieldColumns = new HashMap<String, String>();
        this.tableClass = tableClass;
        Table aTable = tableClass.getAnnotation(Table.class);
        if (aTable != null) {
            this.annotationTableName = aTable.name();
            this.pkFieldName = aTable.pkField();
            if (StringUtils.isBlank(this.tableAlias)) {
                this.tableAlias = aTable.alias();
            }
            if (!Object.class.equals(aTable.mappingHandler())) {
                this.mappingHandler = ((MappingHandler) ClassUtils.newInstance(aTable.mappingHandler()));
            }
        }
        BeanInfo selfBeanInfo = ClassUtils.getSelfBeanInfo(tableClass);
        PropertyDescriptor[] propertyDescriptors = selfBeanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : propertyDescriptors) {
            Method readMethod = pd.getReadMethod();
            if (readMethod == null) {
                continue;
            }
            classFields.add(pd.getName());
            Column column = readMethod.getAnnotation(Column.class);
            if (column == null) {
                continue;
            }
            this.mappedFieldColumns.put(pd.getName(), column.value());
        }
        return this;
    }

    public MetaTable tableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
        return this;
    }

    public MetaTable entity(Object entity, boolean isIgnoreNull) {
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
            if (this.columnAutoFields != null) {
                AutoField autoField = new AutoField.Builder().name(fieldName).build();
                this.columnAutoFields.add(autoField);
            }
            //已经有了，以Criteria中为准
            if (this.hasAutoField(fieldName)) {
                continue;
            }
            Object value = ClassUtils.invokeMethod(readMethod, entity);

            //忽略掉null
            if (value == null && isIgnoreNull) {
                continue;
            }
            AutoField autoField = new AutoField.Builder().name(fieldName).logicalOperator("and").fieldOperator("=")
                .type(AutoFieldType.NORMAL).value(value).build();
            this.getAutoFields().put(fieldName, autoField);
        }
        return this;
    }

    public MetaTable isFieldExclusion(boolean isFieldExclusion) {
        this.isFieldExclusion = isFieldExclusion;
        return this;
    }

    public MetaTable isOrderBy(boolean isOrderBy) {
        this.isOrderBy = isOrderBy;
        return this;
    }

    public MetaTable mappingHandler(MappingHandler mappingHandler) {
        this.mappingHandler = mappingHandler;
        return this;
    }

}
