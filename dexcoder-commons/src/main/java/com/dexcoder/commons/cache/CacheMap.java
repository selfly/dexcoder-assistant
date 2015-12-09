package com.dexcoder.commons.cache;

/**
 * 缓存MAP接口
 *
 * Created by liyd on 7/24/14.
 */
public interface CacheMap<K, V> {

    /**
     * 返回当前缓存的大小
     *
     * @return
     */
    public int size();

    /**
     * 返回默认存活时间
     *
     * @return
     */
    public long getDefaultExpire();

    /**
     * 向缓存添加value对象,其在缓存中生存时间为默认值
     *
     * @param key
     * @param value
     */
    public void put(K key, V value);

    /**
     * 向缓存添加value对象,并指定存活时间
     *
     * @param key
     * @param value
     * @param expire  过期时间
     */
    public void put(K key, V value, long expire);

    /**
     * 查找缓存对象
     *
     * @param key
     * @return
     */
    public V get(K key);

    /**
     * 淘汰对象
     *
     * @return  被删除对象大小
     */
    public int eliminate();

    /**
     * 缓存是否已经满
     * 
     * @return
     */
    public boolean isFull();

    /**
     * 删除缓存对象
     *
     * @param key
     */
    public void remove(K key);

    /**
     * 清除所有缓存对象
     */
    public void clear();

    /**
     * 返回缓存大小
     *
     * @return
     */
    public int getCacheSize();

    /**
     * 缓存中是否为空
     */
    public boolean isEmpty();
}
