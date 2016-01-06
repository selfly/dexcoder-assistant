package com.dexcoder.commons.utils;

import com.dexcoder.commons.cache.CacheMap;
import com.dexcoder.commons.cache.LRUCache;

/**
 * 缓存
 *
 * Created by liyd on 1/5/15.
 */
@SuppressWarnings("unchecked")
public class CacheUtils {

    /** 默认缓存大小 */
    public static final int                 DEFAULT_CACHE_SIZE      = 5000;

    /** 默认缓存存活时间 毫秒 一个小时 */
    public static final long                DEFAULT_CACHE_LIVE_TIME = 1000 * 60 * 60;

    /** 缓存map */
    private static CacheMap<Object, Object> cacheMap                = new LRUCache<Object, Object>(DEFAULT_CACHE_SIZE,
                                                                        DEFAULT_CACHE_LIVE_TIME);

    /**
     * 获取缓存大小
     *
     * @return
     */
    public static int getCacheSize() {
        return cacheMap.getCacheSize();
    }

    /**
     * 当前缓存大小
     *
     * @return
     */
    public static int getCurrentCacheSize() {
        return cacheMap.size();
    }

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    public static Object get(Object key) {
        return cacheMap.get(key);
    }

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    public static String getForString(Object key) {
        return (String) cacheMap.get(key);
    }

    /**
     * 获取父缓存下的子缓存
     *
     * @param key
     * @return
     */
    public static Object getChild(Object parentKey, Object key) {
        CacheMap<Object, Object> map = (CacheMap<Object, Object>) cacheMap.get(key);
        if (map == null) {
            return null;
        }
        return map.get(key);
    }

    /**
     * 添加缓存
     *
     * @param key the key
     * @param value the value
     */
    public static void put(Object key, Object value) {
        put(key, value, DEFAULT_CACHE_LIVE_TIME);
    }

    /**
     * 添加缓存
     *
     * @param key the key
     * @param value the value
     * @param liveTime the live time
     */
    public static void put(Object key, Object value, long liveTime) {
        cacheMap.put(key, value, liveTime);
    }

    /**
     * 添加子缓存
     *
     * @param parentKey the parent key
     * @param key the key
     * @param value the value
     */
    public static void putChild(Object parentKey, Object key, Object value) {
        putChild(parentKey, key, value, DEFAULT_CACHE_LIVE_TIME);
    }

    /**
     * 添加子缓存
     *
     * @param parentKey the parent key
     * @param key the key
     * @param value the value
     * @param liveTime the live time
     */
    public static void putChild(Object parentKey, Object key, Object value, long liveTime) {
        CacheMap<Object, Object> map = (CacheMap<Object, Object>) cacheMap.get(parentKey);
        if (map == null) {
            map = new LRUCache<Object, Object>(DEFAULT_CACHE_SIZE, DEFAULT_CACHE_LIVE_TIME);
        }
        map.put(key, value);

        cacheMap.put(parentKey, map, liveTime);
    }

    /**
     * 移除元素
     *
     * @param key the key
     */
    public static void remove(Object key) {
        cacheMap.remove(key);
    }

    /**
     * 移除子元素
     * 
     * @param parentKey
     * @param key
     */
    public static void removeChild(Object parentKey, Object key) {
        CacheMap<Object, Object> map = (CacheMap<Object, Object>) cacheMap.get(parentKey);
        if (map != null) {
            map.remove(key);
        }
    }

    /**
     * 清空缓存
     */
    public static void clear() {
        cacheMap.clear();
    }
}
