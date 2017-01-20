package com.dexcoder.dal.cache;

/**
 * 缓存处理
 *
 * Created by liyd on 16/10/9.
 */
public interface CacheManager {

    /**
     * 添加缓存
     *
     * @param key
     * @param value
     */
    void put(Object key, Object value);

    /**
     * 获取缓存
     *
     * @param key
     * @param <T>
     * @return
     */
    <T> T get(Object key);

//    List getKeys();

    /**
     * 移除缓存
     *
     * @param key
     */
    void remove(Object key);

    /**
     * 移除所有缓存
     */
    void removeAll();

//    <T> T get(Object key, DataLoader dataLoader);
//
//    <T> T get(Object key, Class<? extends DataLoader> dataLoaderClass);

}
