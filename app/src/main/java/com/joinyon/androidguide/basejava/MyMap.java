package com.joinyon.androidguide.basejava;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 作者： JoinYon on 2018/8/10.
 * 邮箱：2816886869@qq.com
 * Map 是一个接口，一个map不能包含重复的key，每个key只能映射唯一一个value
 * Map 接口是用来取代Dictionary抽象类的
 * <p>
 * Map 接口提供三个集合视图:
 * 1.key的集合视图
 * 2.value的集合视图
 * 3.key-value的集合视图
 * <p>
 * Map内元素的顺序取决于Iterator的具体实现，获取集合视图其实是获取一个迭代器，实现对遍历元素过程的隐藏。
 * <p>
 * 依此分析:为什么TreeMap为啥是有序的，而HashMap为何无法保证顺序？
 * <p>
 * 需要注意的是:当使用一个可以变对象作为key时要小心，map是根据hashCode和equals方法觉得存放位置的。
 * 一个特殊的案例是不允许一个map将自己作为一个key，但允许将自己作为一个value why？？？
 */
public interface MyMap<K, V> {
    //查询操作

    /**
     * 返回map里的键值对个数，如果包含的元素个数大于int类型的最大值，则返回 Integer.MAX_VALUE
     *
     * @return
     */
    int size();

    /**
     * 如果map中没有包含键值对，则返回 true
     *
     * @return
     */
    boolean isEmpty();

    /**
     * @param key
     * @return
     */
    boolean containsKey(Object key);

    /**
     * @param value
     * @return
     */
    boolean containsValue(Object value);

    /**
     * @param key
     * @return
     */
    V get(Object key);

    /**
     * @param key
     * @param value
     * @return
     */
    V put(K key, V value);

    /**
     * @param key
     * @return
     */
    V remove(Object key);

    /**
     * @param m
     */
    void putAll(Map<? extends K, ? extends V> m);

    /**
     * 清空
     */
    void ckear();

    /**
     * 返回所有key的集合
     *
     * @return
     */
    Set<K> keySet();

    /**
     * 返回所有value的集合
     *
     * @return
     */
    Collection<V> values();

    interface MyEntry<K, V> {

        K getKey();

        V getValue();

        V setValue(V value);

        boolean equals(Object o);

        int hashCOde();
    }
}
