package com.dexcoder.assistant.persistence;

import com.dexcoder.assistant.exceptions.AssistantException;
import com.dexcoder.assistant.utils.ArrUtils;
import com.dexcoder.assistant.utils.ClassUtils;
import com.dexcoder.assistant.utils.StrUtils;
import com.dexcoder.assistant.utils.UUIDUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by liyd on 3/3/15.
 */
public class SqlUtils {

    /** 日志对象 */
    private static final Logger LOG    = LoggerFactory.getLogger(SqlUtils.class);

    /** sql中的in */
    private static final String IN     = "IN";

    /** sql中的not in */
    private static final String NOT_IN = "NOT IN";

    /**
     * 获取实体类对象
     * 
     * @param entity
     * @param criteria
     * @return
     */
    public static Class<?> getEntityClass(Object entity, Criteria criteria) {
        return entity == null ? criteria.getEntityClass() : entity.getClass();
    }

    /**
     * 构建insert语句
     *
     * @param entity 实体映射对象
     * @param criteria the criteria
     * @param nameHandler 名称转换处理器
     * @return bound sql
     */
    public static BoundSql buildInsertSql(Object entity, Criteria criteria, NameHandler nameHandler) {

        Class<?> entityClass = getEntityClass(entity, criteria);
        Map<String, AutoField> autoFields = mergeAutoField(entity, criteria, AutoField.FIELD_UPDATE);

        String tableName = nameHandler.getTableName(entityClass, autoFields);
        String pkName = nameHandler.getPKName(entityClass);

        StringBuilder sql = new StringBuilder("INSERT INTO ");
        List<Object> params = new ArrayList<Object>();
        sql.append(tableName);

        sql.append("(");
        StringBuilder args = new StringBuilder();
        args.append("(");

        for (AutoField autoField : autoFields.values()) {

            if (autoField.getType() != AutoField.FIELD_UPDATE
                && autoField.getType() != AutoField.PK_VALUE_NAME) {
                continue;
            }
            String columnName = nameHandler.getColumnName(autoField.getName());
            Object value = autoField.getValues()[0];

            sql.append(columnName);
            //如果是主键，且是主键的值名称
            if (StrUtils.equalsIgnoreCase(pkName, columnName)
                && autoField.getType() == AutoField.PK_VALUE_NAME) {
                //参数直接append，传参方式会无法调用序列等问题
                args.append(value);
            } else {
                args.append("?");
                params.add(value);
            }
            sql.append(",");
            args.append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        args.deleteCharAt(args.length() - 1);
        args.append(")");
        sql.append(")");
        sql.append(" VALUES ");
        sql.append(args);
        return new BoundSql(sql.toString(), pkName, params);
    }

    /**
     * 构建更新sql
     *
     * @param entity the entity
     * @param criteria the criteria
     * @param nameHandler the name handler
     * @return bound sql
     */
    public static BoundSql buildUpdateSql(Object entity, Criteria criteria, NameHandler nameHandler) {

        Class<?> entityClass = getEntityClass(entity, criteria);
        Map<String, AutoField> autoFields = mergeAutoField(entity, criteria, AutoField.FIELD_UPDATE);

        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<Object>();
        String tableName = nameHandler.getTableName(entityClass, autoFields);
        String primaryName = nameHandler.getPKName(entityClass);

        sql.append("UPDATE ").append(tableName).append(" SET ");

        Object primaryValue = null;

        Iterator<AutoField> iterator = autoFields.values().iterator();
        while (iterator.hasNext()) {

            AutoField autoField = iterator.next();

            if (AutoField.FIELD_UPDATE != autoField.getType()) {
                continue;
            }

            String columnName = nameHandler.getColumnName(autoField.getName());

            //如果是主键
            if (StrUtils.equalsIgnoreCase(primaryName, columnName)) {

                Object[] values = autoField.getValues();

                if (ArrUtils.isEmpty(values) || StrUtils.isBlank(values[0].toString())) {
                    throw new AssistantException("主键值不能设为空");
                }
                primaryValue = values[0];
            }

            //白名单 黑名单
            if (criteria != null && !CollectionUtils.isEmpty(criteria.getIncludeFields())
                && !criteria.getIncludeFields().contains(autoField.getName())) {
                continue;
            } else if (criteria != null && !CollectionUtils.isEmpty(criteria.getExcludeFields())
                       && criteria.getExcludeFields().contains(autoField.getName())) {
                continue;
            }

            sql.append(columnName).append(" ").append(autoField.getFieldOperator()).append(" ");
            if (ArrUtils.isEmpty(autoField.getValues()) || autoField.getValues()[0] == null) {
                sql.append("NULL");
            } else {
                sql.append("?");
                params.add(autoField.getValues()[0]);
            }
            sql.append(",");

            //移除掉操作过的元素
            iterator.remove();
        }

        sql.deleteCharAt(sql.length() - 1);
        sql.append(" WHERE ");

        if (primaryValue != null) {
            sql.append(primaryName).append(" = ?");
            params.add(primaryValue);
        } else {
            BoundSql boundSql = SqlUtils.buildWhereSql(autoFields, nameHandler);
            sql.append(boundSql.getSql());
            params.addAll(boundSql.getParams());
        }
        return new BoundSql(sql.toString(), primaryName, params);
    }

    /**
     * 合并所有的操作属性，entity非null字段将被转换到列表
     *
     * @param entity the entity
     * @param criteria the criteria
     * @param operateType the operate type
     * @return
     */
    protected static Map<String, AutoField> mergeAutoField(Object entity, Criteria criteria,
                                                           int operateType) {

        Map<String, AutoField> autoFieldMap = new LinkedHashMap<String, AutoField>();

        //汇总的所有操作属性,必须criteria字段在前，防止or等操作被覆盖
        if (criteria != null && !CollectionUtils.isEmpty(criteria.getAutoFields())) {
            Iterator<AutoField> iterator = criteria.getAutoFields().iterator();
            while (iterator.hasNext()) {
                AutoField autoField = iterator.next();
                //括号字段生成唯一key，防止sql中多个括号操作时覆盖
                String key = (autoField.getType() == AutoField.FIELD_BRACKET ? UUIDUtils.getUUID8()
                    : autoField.getName());
                autoFieldMap.put(key, autoField);
            }
        }

        if (entity == null) {
            return autoFieldMap;
        }

        //获取属性信息
        BeanInfo beanInfo = ClassUtils.getSelfBeanInfo(entity.getClass());
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

        AutoField autoField;
        for (PropertyDescriptor pd : pds) {

            String fieldName = pd.getName();

            //null值，忽略 (单独指定的可以指定为null)
            Object value = getReadMethodValue(pd.getReadMethod(), entity);
            if (value == null) {
                continue;
            }

            if (value instanceof String && StrUtils.isBlank(value.toString())) {
                continue;
            }

            autoField = new AutoField();
            autoField.setName(fieldName);
            autoField.setSqlOperator("and");
            autoField.setFieldOperator("=");
            autoField.setValues(new Object[] { value });
            autoField.setType(operateType);

            autoFieldMap.put(autoField.getName(), autoField);
        }
        return autoFieldMap;
    }

    /**
     * 构建where条件sql
     *
     * @param autoFields the auto fields
     * @param nameHandler the name handler
     * @return bound sql
     */
    protected static BoundSql buildWhereSql(Map<String, AutoField> autoFields,
                                            NameHandler nameHandler) {

        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<Object>();
        Iterator<AutoField> iterator = autoFields.values().iterator();
        while (iterator.hasNext()) {
            AutoField autoField = iterator.next();
            if (AutoField.FIELD_WHERE != autoField.getType()
                && AutoField.FIELD_BRACKET != autoField.getType()) {
                continue;
            }
            //操作过，移除
            iterator.remove();
            if (sql.length() > 0 && sql.charAt(sql.length() - 1) != '('
                && StrUtils.isNotBlank(autoField.getSqlOperator())) {
                sql.append(" ").append(autoField.getSqlOperator()).append(" ");
            }
            if (AutoField.FIELD_BRACKET == autoField.getType()) {
                sql.append(autoField.getName());
                continue;
            }
            String columnName = nameHandler.getColumnName(autoField.getName());
            Object[] values = autoField.getValues();

            if (StrUtils.equalsIgnoreCase(IN, StrUtils.trim(autoField.getFieldOperator()))
                || StrUtils.equalsIgnoreCase(NOT_IN,
                    StrUtils.trim(autoField.getFieldOperator()))) {

                //in，not in的情况
                sql.append(columnName).append(" ").append(autoField.getFieldOperator()).append(" ");
                sql.append("(");
                for (int j = 0; j < values.length; j++) {
                    sql.append(" ?");
                    params.add(values[j]);
                    if (j != values.length - 1) {
                        sql.append(",");
                    }
                }
                sql.append(")");
            } else if (values == null) {
                //null 值
                sql.append(columnName).append(" ").append(autoField.getFieldOperator())
                    .append(" NULL");
            } else if (values.length == 1) {
                //一个值 =
                sql.append(columnName).append(" ").append(autoField.getFieldOperator())
                    .append(" ?");
                params.add(values[0]);
            } else {
                //多个值，or的情况
                sql.append("(");
                for (int j = 0; j < values.length; j++) {
                    sql.append(columnName).append(" ").append(autoField.getFieldOperator())
                        .append(" ?");
                    params.add(values[j]);
                    if (j != values.length - 1) {
                        sql.append(" OR ");
                    }
                }
                sql.append(")");
            }
        }
        return new BoundSql(sql.toString(), null, params);
    }

    /**
     * 构建根据主键删除sql
     *
     * @param clazz
     * @param id
     * @param nameHandler
     * @return
     */
    public static BoundSql buildDeleteSql(Class<?> clazz, Long id, NameHandler nameHandler) {

        List<Object> params = new ArrayList<Object>();
        params.add(id);
        String pkName = nameHandler.getPKName(clazz);
        Map<String, AutoField> fieldMap = AutoFieldUtils.buildPKMap(pkName, id);
        String tableName = nameHandler.getTableName(clazz, fieldMap);
        String sql = "DELETE FROM " + tableName + " WHERE " + pkName + " = ?";
        return new BoundSql(sql, pkName, params);
    }

    /**
     * 构建删除sql
     *
     * @param entity the entity
     * @param criteria the criteria
     * @param nameHandler the name handler
     * @return bound sql
     */
    public static BoundSql buildDeleteSql(Object entity, Criteria criteria, NameHandler nameHandler) {

        Class<?> entityClass = getEntityClass(entity, criteria);
        Map<String, AutoField> autoFields = mergeAutoField(entity, criteria, AutoField.FIELD_WHERE);

        String tableName = nameHandler.getTableName(entityClass, autoFields);
        String primaryName = nameHandler.getPKName(entityClass);

        StringBuilder sql = new StringBuilder("DELETE FROM " + tableName + " WHERE ");
        BoundSql boundSql = SqlUtils.buildWhereSql(autoFields, nameHandler);
        boundSql.setSql(sql.append(boundSql.getSql()).toString());
        boundSql.setPrimaryKey(primaryName);

        return boundSql;
    }

    /**
     * 构建根据id查询sql
     *
     * @param clazz the clazz
     * @param id the id
     * @param criteria the criteria
     * @param nameHandler the name handler
     * @return bound sql
     */
    public static BoundSql buildByIdSql(Class<?> clazz, Long id, Criteria criteria,
                                        NameHandler nameHandler) {

        Class<?> entityClass = (clazz == null ? criteria.getEntityClass() : clazz);
        String pkName = nameHandler.getPKName(entityClass);
        Map<String, AutoField> fieldMap = AutoFieldUtils.buildPKMap(pkName, id);
        String tableName = nameHandler.getTableName(entityClass, fieldMap);

        String columns = SqlUtils.buildColumnSql(entityClass, nameHandler, criteria == null ? null
            : criteria.getIncludeFields(), criteria == null ? null : criteria.getExcludeFields());
        String sql = "SELECT " + columns + " FROM " + tableName + " WHERE " + pkName + " = ?";
        List<Object> params = new ArrayList<Object>();
        params.add(id);

        return new BoundSql(sql, pkName, params);
    }

    /**
     * 按设置的条件构建查询sql
     *
     * @param entity the entity
     * @param criteria the criteria
     * @param nameHandler the name handler
     * @return bound sql
     */
    public static BoundSql buildQuerySql(Object entity, Criteria criteria, NameHandler nameHandler) {

        Class<?> entityClass = getEntityClass(entity, criteria);
        Map<String, AutoField> autoFields = mergeAutoField(entity, criteria, AutoField.FIELD_WHERE);

        String tableName = nameHandler.getTableName(entityClass, autoFields);
        String primaryName = nameHandler.getPKName(entityClass);

        String columns = SqlUtils.buildColumnSql(entityClass, nameHandler, criteria == null ? null
            : criteria.getIncludeFields(), criteria == null ? null : criteria.getExcludeFields());
        StringBuilder querySql = new StringBuilder("SELECT " + columns + " FROM ");
        querySql.append(tableName);

        List<Object> params = null;
        if (!MapUtils.isEmpty(autoFields)) {
            querySql.append(" WHERE ");

            BoundSql boundSql = SqlUtils.buildWhereSql(autoFields, nameHandler);
            params = boundSql.getParams();
            querySql.append(boundSql.getSql());
        }

        return new BoundSql(querySql.toString(), primaryName,
            params == null ? new ArrayList<Object>() : params);
    }

    /**
     * 构建列表查询sql
     *
     * @param entity the entity
     * @param criteria the criteria
     * @param nameHandler the name handler
     * @return bound sql
     */
    public static BoundSql buildListSql(Object entity, Criteria criteria, NameHandler nameHandler) {

        BoundSql boundSql = SqlUtils.buildQuerySql(entity, criteria, nameHandler);

        StringBuilder sb = new StringBuilder(" ORDER BY ");
        if (criteria != null) {

            for (AutoField autoField : criteria.getOrderByFields()) {

                sb.append(nameHandler.getColumnName(autoField.getName())).append(" ")
                    .append(autoField.getFieldOperator()).append(",");
            }

            if (sb.length() > 10) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }

        if (sb.length() < 11) {
            sb.append(boundSql.getPrimaryKey()).append(" DESC");
        }
        boundSql.setSql(boundSql.getSql() + sb.toString());
        return boundSql;
    }

    /**
     * 构建记录数查询sql
     *
     * @param entity the entity
     * @param criteria the criteria
     * @param nameHandler the name handler
     * @return bound sql
     */
    public static BoundSql buildCountSql(Object entity, Criteria criteria, NameHandler nameHandler) {

        Class<?> entityClass = getEntityClass(entity, criteria);
        Map<String, AutoField> autoFields = mergeAutoField(entity, criteria, AutoField.FIELD_WHERE);

        String tableName = nameHandler.getTableName(entityClass, autoFields);
        StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM ");
        countSql.append(tableName);

        List<Object> params = new ArrayList<Object>();
        if (!MapUtils.isEmpty(autoFields)) {
            countSql.append(" WHERE ");
            BoundSql boundSql = buildWhereSql(autoFields, nameHandler);
            countSql.append(boundSql.getSql());
            params.addAll(boundSql.getParams());
        }

        return new BoundSql(countSql.toString(), null, params);
    }

    /**
     * 构建查询的列sql
     *
     * @param clazz the clazz
     * @param nameHandler the name handler
     * @param includeField the include field
     * @param excludeField the exclude field
     * @return string string
     */
    public static String buildColumnSql(Class<?> clazz, NameHandler nameHandler,
                                        List<String> includeField, List<String> excludeField) {

        StringBuilder columns = new StringBuilder();

        //获取属性信息
        BeanInfo beanInfo = ClassUtils.getSelfBeanInfo(clazz);
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor pd : pds) {

            String fieldName = pd.getName();

            //白名单 黑名单
            if (!CollectionUtils.isEmpty(includeField) && !includeField.contains(fieldName)) {
                continue;
            } else if (!CollectionUtils.isEmpty(excludeField) && excludeField.contains(fieldName)) {
                continue;
            }

            String columnName = nameHandler.getColumnName(fieldName);
            columns.append(columnName);
            columns.append(",");
        }
        columns.deleteCharAt(columns.length() - 1);
        return columns.toString();
    }

    /**
     * 构建排序条件
     *
     * @param sort
     * @param nameHandler
     * @param properties
     */
    public static String buildOrderBy(String sort, NameHandler nameHandler, String... properties) {

        StringBuilder sb = new StringBuilder();
        for (String property : properties) {
            String columnName = nameHandler.getColumnName(property);
            sb.append(columnName);
            sb.append(" ");
            sb.append(sort);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 获取属性值
     *
     * @param readMethod
     * @param entity
     * @return
     */
    protected static Object getReadMethodValue(Method readMethod, Object entity) {
        if (readMethod == null) {
            return null;
        }
        try {
            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                readMethod.setAccessible(true);
            }
            return readMethod.invoke(entity);
        } catch (Exception e) {
            LOG.error("获取属性值失败", e);
            throw new AssistantException(e);
        }
    }
}
