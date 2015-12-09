package com.dexcoder.commons.cache;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 缓存map抽象实现
 * 
 * Created by liyd on 7/24/14.
 */
public abstract class AbstractCacheMap<K, V> implements CacheMap<K, V> {

    class CacheObject<K2, V2> {
        CacheObject(K2 key, V2 value, long liveTime) {
            this.key = key;
            this.cachedObject = value;
            this.liveTime = liveTime;
            this.lastAccess = System.currentTimeMillis();
        }

        /** 缓存key */
        final K2 key;

        /** 缓存对象 */
        final V2 cachedObject;

        /** 最后访问时间 */
        long     lastAccess;

        /** 访问次数 */
        long     accessCount;

        /** 对象存活时间(time-to-live) */
        long     liveTime;

        public boolean isExpired() {
            if (liveTime == 0) {
                return false;
            }
            return (lastAccess + liveTime) < System.currentTimeMillis();
        }

        public V2 getObject() {
            //当过缓存时间时失败，而不是每访问一次就重新计算
//            lastAccess = System.currentTimeMillis();
            accessCount++;
            return cachedObject;
        }
    }

    /** 缓存map */
    protected Map<K, CacheObject<K, V>>  cacheMap;

    /** 读写锁对象 */
    private final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();

    /** 读锁 */
    private final Lock                   readLock  = cacheLock.readLock();

    /** 写锁 */
    private final Lock                   writeLock = cacheLock.writeLock();

    /** 最大缓存大小 , 0表示无限制 */
    protected int                        cacheSize;

    /** 默认过期时间, 0表示永不过期 */
    protected long                       defaultExpire;

    /** 是否设置默认过期时间 */
    protected boolean                    existCustomExpire;

    /**
     * 获取最大缓存大小
     * 
     * @return
     */
    public int getCacheSize() {
        return cacheSize;
    }

    /**
     * 构造方法
     *
     * @param cacheSize
     * @param defaultExpire
     */
    public AbstractCacheMap(int cacheSize, long defaultExpire) {
        this.cacheSize = cacheSize;
        this.defaultExpire = defaultExpire;
    }

    /**
     * 获取默认过期时间
     *
     * @return
     */
    public long getDefaultExpire() {
        return defaultExpire;
    }

    /**
     * 是否需要清除过期对象
     *
     * @return
     */
    protected boolean isNeedClearExpiredObject() {
        return defaultExpire > 0 || existCustomExpire;
    }

    /**
     * 添加缓存对象
     *
     * @param key
     * @param value
     */
    public void put(K key, V value) {
        this.put(key, value, defaultExpire);
    }

    /**
     * 添加缓存对象
     * 
     * @param key
     * @param value
     * @param expire  过期时间
     */
    public void put(K key, V value, long expire) {
        writeLock.lock();
        try {
            CacheObject<K, V> co = new CacheObject<K, V>(key, value, expire);
            if (expire != 0) {
                existCustomExpire = true;
            }
            if (isFull()) {
                eliminate();
            }
            cacheMap.put(key, co);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 获取缓存对象
     * 
     * @param key
     * @return
     */
    public V get(K key) {
        readLock.lock();
        try {
            CacheObject<K, V> co = cacheMap.get(key);
            if (co == null) {
                return null;
            }
            if (co.isExpired() == true) {
                cacheMap.remove(key);
                return null;
            }

            return co.getObject();
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 回收对象
     *
     * @return
     */
    public final int eliminate() {
        writeLock.lock();
        try {
            return eliminateCache();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 淘汰对象具体实现
     *
     * @return
     */
    protected abstract int eliminateCache();

    /**
     * 缓存是否已满
     *
     * @return
     */
    public boolean isFull() {
        //无限制
        if (cacheSize == 0) {
            return false;
        }
        return cacheMap.size() >= cacheSize;
    }

    /**
     * 移除缓存对象
     *
     * @param key
     */
    public void remove(K key) {
        writeLock.lock();
        try {
            cacheMap.remove(key);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 清空缓存
     */
    public void clear() {
        writeLock.lock();
        try {
            cacheMap.clear();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 缓存大小
     * @return
     */
    public int size() {
        return cacheMap.size();
    }

    /**
     * 缓存是否为空
     * 
     * @return
     */
    public boolean isEmpty() {
        return size() == 0;
    }
}
