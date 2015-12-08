package com.dexcoder.jdbc.build;

import com.dexcoder.jdbc.NameHandler;
import com.dexcoder.jdbc.utils.ClassUtils;
import com.dexcoder.jdbc.utils.StrUtils;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by liyd on 2015-12-7.
 */
public abstract class AbstractSqlBuilder implements SqlBuilder {

    protected static final Map<Class<?>, List<String>> CLASS_FIELD_CACHE = new HashMap<Class<?>, List<String>>();

    protected String pkFieldName;

    protected List<String> columnFields;

    /**
     * 操作的字段
     */
    protected Map<String, AutoField> autoFields;

    public AbstractSqlBuilder() {
        columnFields = new ArrayList<String>();
        autoFields = new LinkedHashMap<String, AutoField>();
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

    public void mergeEntityFields(Object entity, AutoFieldType autoFieldType, NameHandler nameHandler, boolean isIgnoreNull) {
        if (entity == null) {
            return;
        }
        String pkName = nameHandler.getPKName(entity.getClass());
        BeanInfo selfBeanInfo = ClassUtils.getSelfBeanInfo(entity.getClass());
        PropertyDescriptor[] propertyDescriptors = selfBeanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : propertyDescriptors) {
            Method readMethod = pd.getReadMethod();
            if (readMethod == null) {
                continue;
            }
            String fieldName = pd.getName();
//            System.out.println(pd.getPropertyEditorClass());
//            System.out.println(pd.getReadMethod().getDeclaringClass());
//            if (pd.getReadMethod().getDeclaringClass().equals(entity.getClass())){
                columnFields.add(fieldName);
//            }

            Object value = ClassUtils.invokeMethod(readMethod, entity);

            //忽略掉null
            if (value == null && isIgnoreNull) {
                continue;
            }
            //已经有了，以Criteria中为准
            if (this.hasField(fieldName)) {
                continue;
            }
            String columnName = nameHandler.getColumnName(fieldName);
            AutoField autoField = this.buildAutoField(fieldName, "and", "=", autoFieldType, value);
            this.autoFields.put(fieldName, autoField);
            if (StrUtils.equals(pkName, columnName)) {
                this.pkFieldName = fieldName;
            }
        }
    }

    /**
     * 构建操作的字段
     *
     * @param fieldName     the field name
     * @param sqlOperator   the build operator
     * @param fieldOperator the field operator
     * @param type          the type
     * @param value         the values
     * @return auto field
     */
    protected AutoField buildAutoField(String fieldName, String sqlOperator,
                                       String fieldOperator, AutoFieldType type, Object value) {
        AutoField autoField = new AutoField();
        autoField.setName(fieldName);
        autoField.setSqlOperator(sqlOperator);
        autoField.setFieldOperator(fieldOperator);
        autoField.setValue(value);
        autoField.setType(type);
        return autoField;
    }


//    //    protected void buildEntityFields(Class<?> clazz) {
////        this.clazz = clazz;
////    }
////
//    protected void mergeEntityFields(Map<String, AutoField> autoFields) {
//        if (entityFields != null) {
//            autoFields.putAll(entityFields);
//        }
//    }
//
////    /**
////     * 构建只有主键的fieldMap
////     *
////     * @param fieldName
////     * @param value
////     * @return
////     */
////    @SuppressWarnings("serial")
////    public  Map<String, AutoField> buildPkFieldMap(String fieldName, Object value) {
////        final AutoField autoField = buildAutoField(fieldName, null, null, AutoFieldType.WHERE,
////                value);
////        return new LinkedHashMap<String, AutoField>() {
////            {
////                put(autoField.getName(), autoField);
////            }
////        };
////    }

}
