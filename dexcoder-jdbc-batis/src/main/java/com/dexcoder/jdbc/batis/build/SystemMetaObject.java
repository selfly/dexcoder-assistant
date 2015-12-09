package com.dexcoder.jdbc.batis.build;

import com.dexcoder.jdbc.batis.reflection.MetaObject;

/**
 * Created by liyd on 2015-12-1.
 */
public final class SystemMetaObject {

    public static final MetaObject NULL_META_OBJECT = MetaObject.forObject(NullObject.class);

    private SystemMetaObject() {
        // Prevent Instantiation of Static Class
    }

    private static class NullObject {
    }

    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object);
    }

}
