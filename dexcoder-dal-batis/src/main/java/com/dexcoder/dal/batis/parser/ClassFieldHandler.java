//package com.dexcoder.dal.batis.parser;
//
//import java.beans.BeanInfo;
//import java.beans.PropertyDescriptor;
//import java.lang.reflect.Method;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
//
//import com.dexcoder.commons.utils.ClassUtils;
//import com.dexcoder.dal.annotation.Column;
//import com.dexcoder.dal.annotation.Transient;
//import com.dexcoder.dal.build.AutoField;
//import com.dexcoder.dal.build.MetaTable;
//import com.dexcoder.dal.handler.MappingHandler;
//import com.dexcoder.dal.handler.TokenHandler;
//
///**
// * Created by liyd on 16/3/31.
// */
//public class ClassFieldHandler implements TokenHandler {
//
//    private MappingHandler mappingHandler;
//
//    public ClassFieldHandler(MappingHandler mappingHandler) {
//        this.mappingHandler = mappingHandler;
//    }
//
//    public String handleToken(String content) {
//        Class<?> clazz = ClassUtils.loadClass(StringUtils.trim(content));
//        BeanInfo selfBeanInfo = ClassUtils.getSelfBeanInfo(clazz);
//        PropertyDescriptor[] propertyDescriptors = selfBeanInfo.getPropertyDescriptors();
//
//        MetaTable metaTable = new MetaTable.Builder().initAutoFields().tableClass(clazz)
//            .mappingHandler(this.mappingHandler).build();
//        StringBuilder sb = new StringBuilder();
//        for (PropertyDescriptor pd : propertyDescriptors) {
//            Method readMethod = pd.getReadMethod();
//            if (readMethod == null) {
//                continue;
//            }
//            Transient aTransient = readMethod.getAnnotation(Transient.class);
//            if (aTransient != null) {
//                continue;
//            }
//            String fieldName = pd.getName();
//            String fieldAnnotationName = null;
//            Column aColumn = readMethod.getAnnotation(Column.class);
//            if (aColumn != null) {
//                fieldAnnotationName = aColumn.value();
//            }
//            AutoField autoField = new AutoField.Builder().name(fieldName).annotationName(fieldAnnotationName).build();
//            metaTable.getAutoFields().put(fieldName, autoField);
//        }
//        for (Map.Entry<String, AutoField> entry : metaTable.getAutoFields().entrySet()) {
//            String columnName = metaTable.getColumnAndTableAliasName(entry.getValue());
//            sb.append(columnName).append(",");
//        }
//        if (sb.length() > 0) {
//            sb.deleteCharAt(sb.length() - 1);
//        }
//        return sb.toString();
//    }
//}
