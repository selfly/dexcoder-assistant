package com.dexcoder.commons.page;

import com.dexcoder.commons.bean.BeanKit;
import com.dexcoder.commons.exceptions.CommonsAssistantException;
import com.dexcoder.commons.utils.ClassUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 分页等常用信息存储
 * <p/>
 * Created by liyd on 6/26/14.
 */
public class Pageable implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long   serialVersionUID = 4060766214127186912L;

    /**
     * 每页显示条数
     */
    private int                 itemsPerPage     = 20;

    /**
     * 当前页码
     */
    private int                 curPage          = 1;

    /**
     * 关键字
     */
    private String              keywords;

    /**
     * 数据map
     */
    private Map<String, Object> extendData;

    /**
     * 放入数据
     * 
     * @param key
     * @param obj
     */
    public void put(String key, Object obj) {
        if (this.extendData == null) {
            extendData = new LinkedHashMap<String, Object>();
        }
        extendData.put(key, obj);
    }

    public void remove(String key) {
        if (this.extendData == null) {
            return;
        }
        this.extendData.remove(key);
    }

    /**
     * 获取数据
     * 
     * @param key
     * @return
     */
    public Object get(String key) {
        return this.get(key, Object.class);
    }

    /**
     * 获取数据
     * 
     * @param key
     * @param elementType
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> elementType) {
        if (this.extendData == null) {
            return null;
        }
        Object obj = this.extendData.get(key);
        if (obj == null) {
            return null;
        }
        if (!elementType.isAssignableFrom(obj.getClass())) {
            throw new CommonsAssistantException(
                "类型不匹配。expected:" + elementType.getName() + ",actual:" + obj.getClass());
        }
        return (T) obj;
    }

    public Map<String, Object> getExtendData() {
        return extendData;
    }

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
            return BeanKit.convert(t, this);
        } catch (Exception e) {
            throw new CommonsAssistantException("转换对象失败", e);
        }
    }

    /**
     * 获取指定属性值
     * 
     * @param fieldName
     * @return
     */
    public Object getFieldValue(String fieldName) {
        return ClassUtils.getFieldValue(this.getClass(), this, fieldName);
    }

    /**
     * 当前页 简化前端参数
     * 
     * @param curPage
     */
    public void setP(int curPage) {
        this.curPage = curPage;
    }

    /**
     * 关键字 简化前端参数
     * 
     * @param keywords
     */
    public void setK(String keywords) {
        this.keywords = keywords;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
