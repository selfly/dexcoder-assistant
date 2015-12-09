package com.dexcoder.jdbc.batis;

import java.util.HashMap;
import java.util.Map;

import com.dexcoder.jdbc.exceptions.JdbcAssistantException;

/**
 * Created by liyd on 2015-11-25.
 */
public class DynamicContext {

    private final Map<String, Object> bindings;
    private final StringBuilder       sqlBuilder   = new StringBuilder();
    private int                       uniqueNumber = 0;

    public DynamicContext(Configuration configuration, Object parameterObject) {
        bindings = new HashMap<String, Object>();
        if (parameterObject != null && !(parameterObject instanceof Map)) {
            throw new JdbcAssistantException("参数错误");
        }
        if (parameterObject instanceof Map) {
            bindings.putAll((Map) parameterObject);
        }

        //
        //        if (parameterObject != null && !(parameterObject instanceof Map)) {
        //            throw new AssistantException("参数错误");
        ////            MetaObject metaObject = configuration.newMetaObject(parameterObject);
        ////            bindings.put("value",parameterObject);
        //        } else {
        //            bindings = new ContextMap(null);
        //        }
        //        bindings.put(PARAMETER_OBJECT_KEY, parameterObject);
    }

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public void bind(String name, Object value) {
        bindings.put(name, value);
    }

    public void appendSql(String sql) {
        sqlBuilder.append(sql);
        sqlBuilder.append(" ");
    }

    public String getSql() {
        return sqlBuilder.toString().trim();
    }

    public int getUniqueNumber() {
        return uniqueNumber++;
    }

    //    static class ContextMap extends HashMap<String, Object> {
    //        private static final long serialVersionUID = 2977601501966151582L;
    //
    //        private MetaObject parameterMetaObject;
    //        public ContextMap(MetaObject parameterMetaObject) {
    //            this.parameterMetaObject = parameterMetaObject;
    //        }
    //
    //        @Override
    //        public Object get(Object key) {
    //            String strKey = (String) key;
    //            if (super.containsKey(strKey)) {
    //                return super.get(strKey);
    //            }
    //
    //            if (parameterMetaObject != null) {
    //                // issue #61 do not modify the context when reading
    //                return parameterMetaObject.getValue(strKey);
    //            }
    //
    //            return null;
    //        }
    //    }
    //
    //    static class ContextAccessor implements PropertyAccessor {
    //
    //        public Object getProperty(Map context, Object target, Object name)
    //                throws OgnlException {
    //            Map map = (Map) target;
    //
    //            Object result = map.get(name);
    //            if (map.containsKey(name) || result != null) {
    //                return result;
    //            }
    //
    //            Object parameterObject = map.get(PARAMETER_OBJECT_KEY);
    //            if (parameterObject instanceof Map) {
    //                return ((Map)parameterObject).get(name);
    //            }
    //
    //            return null;
    //        }
    //
    //        public void setProperty(Map context, Object target, Object name, Object value)
    //                throws OgnlException {
    //            Map<Object, Object> map = (Map<Object, Object>) target;
    //            map.put(name, value);
    //        }
    //
    //        public String getSourceAccessor(OgnlContext arg0, Object arg1, Object arg2) {
    //            return null;
    //        }
    //
    //        public String getSourceSetter(OgnlContext arg0, Object arg1, Object arg2) {
    //            return null;
    //        }
    //    }
}
