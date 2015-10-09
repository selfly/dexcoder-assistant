package com.dexcoder.assistant.pager;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.dexcoder.assistant.bean.BeanConverter;

/**
 * 分页等常用信息存储
 *
 * Created by liyd on 6/26/14.
 */
public class Pageable implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 4060766214127186912L;

    /** 每页显示条数 */
    protected int             itemsPerPage     = 20;

    /** 当前页码 */
    protected int             curPage          = 1;

    /** 关键字 */
    protected String          keywords;

    /**
     * 获取自动转换后的JavaBean对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getTargetObject(Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            return BeanConverter.convert(t, this);
        } catch (InstantiationException e) {
            //ignore
        } catch (IllegalAccessException e) {
            //ignore
        }
        return null;
    }

    /**
     * 重写toString方法
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
