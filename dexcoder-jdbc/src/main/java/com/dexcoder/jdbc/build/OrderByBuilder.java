package com.dexcoder.jdbc.build;

/**
 * Created by liyd on 2015-12-4.
 */
public class OrderByBuilder extends FieldBuilder {

    /**
     * asc 排序属性
     *
     * @param field the field
     */
    public void asc(String... field) {
        for (String f : field) {
            this.buildAutoField(f, null, "ASC", AutoFieldType.ORDER_BY, null);
        }
    }

    /**
     * desc 排序属性
     *
     * @param field the field
     */
    public void desc(String... field) {
        for (String f : field) {
            this.buildAutoField(f, null, "DESC", AutoFieldType.ORDER_BY, null);
        }
    }

}
