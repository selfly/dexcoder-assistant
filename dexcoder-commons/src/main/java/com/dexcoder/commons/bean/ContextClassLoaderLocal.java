package com.dexcoder.commons.bean;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * 该类的一个实例表示为线程的ContextClassLoader提供一个值。
 * <br/>
 * 有时需要将值存储在全局变量“global”，（包括使用的单例模式）
 * <br/>
 * 在只有一个classloader的应用程序，数据可以简单的存储在一些类的“static”成员。
 * 当涉及多个classloader，该方法可能会失败。
 * 尤其是，这些不工作的代码可能会运行在一个servlet容器或j2ee容器，
 * 通过“共享”的classloader加载的类的静态成员变量，在容器内运行的所有组件都是可见的。
 * 这个类提供了一个ClassLoader实例数据关联机制，确保代码在这样的容器运行时每个组件都有自己的副本在“全局”的变量而不是意外的提供一个副本，
 * 当变量和其它组件碰巧同一时间运行在同一容器（比如：servlets 或 EJBs）。
 *
 * 这个类是java.lang.ThreadLocal 的加强模式，允许与特定线程相关联的数据执行类型的任务。
 *
 * 当使用这个类的代码作为“一般”应用程序运行，即不在容器内，与只使用一个静态成员变量来存储数据的效果是相同的
 * 因为Thread.getContextClassLoader总是返回相同的classloader（系统的classloader）。
 *
 * <p>期望的使用方法如下:<br>
 * <pre>
 *  public class SomeClass {
 *    private static final ContextClassLoaderLocal global
 *      = new ContextClassLoaderLocal() {
 *          protected Object initialValue() {
 *              return new String("Initial value");
 *          };
 *
 *    public void testGlobal() {
 *      String s = (String) global.get();
 *      System.out.println("global value:" + s);
 *      buf.set("New Value");
 *    }
 * </pre>
 * </p>
 *
 * <p>
 * <strong>注意:</strong>
 * 这个类需要小心一些，确保使用这个类的组件由容器来“取消部署”，
 * 特定的组件classloader和所有相关联的类（和他们的表态变量）进行垃圾回收。
 *
 * 不幸的是有一个场景这个类不会正常工作，更不幸的是没有已知的解决方法：
 * 当组件被取消部署，组件（或容器）调用该类实例的“unset”方法
 *
 * 该问题发生于：
 * <ul>
 *  <li>通过一个共享的classloader加载的这个类包含静态的实例</li>
 *  <li>实例中存储的值是一个通过特定组件的classloader加载的对象(或任何对象，它指的是通过那个classloader加载)。</li>
 * </ul>
 * 其结果是，该map管理的这个对象，仍包含所存储对象的强引用，它是由包含一个强引用的classloader加载的，
 * 这意味着容器“取消部署”，组件、特定组件的classloader和所有相关的类和静态变量不能被垃圾回收。
 *
 * 如果容器需要每个组件加载所有的设置和类，需要避免通过“共享的”classloader来提供类加载。
 *
 * <p/>
 * User: liyd
 * Date: 13-5-9 下午4:10
 * version $Id: ContextClassLoaderLocal.java, v 0.1 Exp $
 */
public class ContextClassLoaderLocal {

    /** 保存实际实例的数据信息 */
    private Map<ClassLoader, Object> valueByClassLoader     = new WeakHashMap<ClassLoader, Object>();

    /** 全局初始值初始化标记 */
    private boolean                  globalValueInitialized = false;

    /** 全局初始值 */
    private Object                   globalValue;

    /**
     * 构造方法
     */
    public ContextClassLoaderLocal() {
        super();
    }

    /**
     * 返回这个 ContextClassLoaderLocal 的初始值变量
     * 每一次调用该方法的为每一个 ContextClassLoaderLocal 的 ContextClassLoader
     * 首次访问为get或set。
     * 如果程序员希望 ContextClassLoaderLocal 变量被初始化为null以外的一些值，
     * ContextClassLoaderLocal 必须被继承并且该方法被重写。
     * 通常情况下，使用一个匿名的内部类。
     * initialValue 的典型实现是调用一个适当的构造函数，并且返回新构造的对象。
     *
     * @return 该ContextClassLoaderLocal的一个初始值，用来作为使用的新的对象
     */
    protected Object initialValue() {
        return null;
    }

    /**
     * 获取实例
     * 这是一个伪单例。
     * 这是一个伪单例 - 每一个线程的ContextClassLoader提供一个单例的实例
     * 这种机制提供了在同一个web容器中部署的应用程序之间的隔离
     *
     * @return 当前线程ContextClassLoader的关联对象
     */
    public synchronized Object get() {

        // 同步整个方法会有点慢，
        // 但是保证不会有微秒的线程问题,并且不需要同步 valueByClassLoader

        // make sure that the map is given a change to purge itself
        //        valueByClassLoader.isEmpty();
        try {

            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {

                Object value = valueByClassLoader.get(contextClassLoader);
                if ((value == null) && !valueByClassLoader.containsKey(contextClassLoader)) {
                    value = initialValue();
                    valueByClassLoader.put(contextClassLoader, value);
                }
                return value;

            }

        } catch (SecurityException e) {
        }

        // 出现异常，返回全局变量值
        if (!globalValueInitialized) {
            globalValue = initialValue();
            globalValueInitialized = true;
        }
        return globalValue;
    }

    /**
     * 设置值 - 一个值提供一个线程的 ContextClassLoader
     * 这种机制提供了在同一个web容器中部署的应用程序之间的隔离
     *
     * @param value 新的线程ContextClassLoader关联的对象
     */
    public synchronized void set(Object value) {
        // 同步整个方法会有点慢，
        // 但是保证不会有微秒的线程问题,并且不需要同步 valueByClassLoader

        //        // make sure that the map is given a change to purge itself
        //        valueByClassLoader.isEmpty();
        try {

            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                valueByClassLoader.put(contextClassLoader, value);
                return;
            }

        } catch (SecurityException e) {
        }

        //如有异常，设置全局值
        globalValue = value;
        globalValueInitialized = true;
    }

    /**
     * 卸载当前线程ContextClassLoader关联的对象
     */
    public synchronized void unset() {
        try {

            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            unset(contextClassLoader);

        } catch (SecurityException e) { /* SWALLOW - should we log this? */
        }
    }

    /**
     * 卸载当前线程ContextClassLoader关联的对象
     * 
     * @param classLoader 需要卸载的ClassLoader
     */
    public synchronized void unset(ClassLoader classLoader) {
        valueByClassLoader.remove(classLoader);
    }
}
