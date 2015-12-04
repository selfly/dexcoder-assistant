package com.dexcoder.jdbc.build;

import com.dexcoder.jdbc.exceptions.JdbcAssistantException;

/**
 * Created by liyd on 2015-12-4.
 */
public class SelectBuilder extends FieldBuilder {

    /**
     * 添加白名单
     *
     * @param field
     */
    public void include(String... field) {
        if (this.isExclude) {
            throw new JdbcAssistantException("当前已设置了黑名单，不允许再设置白名单");
        }
        for (String f : field) {
            this.buildAutoField(f, null, null, AutoFieldType.INCLUDE, null);
        }
        this.isInclude = true;
    }

    /**
     * 添加黑名单
     *
     * @param field
     */
    public void exclude(String... field) {
        if (this.isInclude) {
            throw new JdbcAssistantException("当前已设置了白名单，不允许再设置黑名单");
        }
        for (String f : field) {
            this.buildAutoField(f, null, null, AutoFieldType.EXCLUDE, null);
        }
        this.isExclude = true;
    }

}
