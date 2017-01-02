/**
 * Yolema.com Inc.
 * Copyright (c) 2011-2012 All Rights Reserved.
 */
package com.dexcoder.commons.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 包含分页信息的List
 * 
 * @author liyd
 * @version $Id: PageList.java, v 0.1 2012-4-18 下午5:08:32 selfly Exp $
 */
public class PageList<T> extends ArrayList<T> {

    /** serialVersionUID */
    private static final long   serialVersionUID = 1418639564542250276L;

    private Pager               pager;

    /** 附加的数据 */
    private Map<String, Object> extraData;

    /**
     * 创建一个<code>PageList</code>。
     */
    public PageList() {
        pager = new Pager();
    }

    /**
     * 创建<code>PageList</code>，并将指定<code>Collection</code>中的内容复制到新的list中。
     *
     * @param c 要复制的<code>Collection</code>
     */
    public PageList(Collection<T> c) {
        this(c, null);
    }

    /**
     * 创建<code>PageList</code>，并将指定<code>Collection</code>中的内容复制到新的list中。
     *
     * @param c 要复制的<code>Collection</code>
     */
    public PageList(Collection<T> c, Pager pager) {
        super(c);
        this.pager = (pager == null) ? new Pager() : pager;
    }

    public Object getExtraData(String key) {
        return extraData != null ? extraData.get(key) : null;
    }

    public void addExtraData(String key, Object obj) {
        if (this.extraData == null) {
            this.extraData = new HashMap<String, Object>();
        }
        this.extraData.put(key, obj);
    }

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

}
