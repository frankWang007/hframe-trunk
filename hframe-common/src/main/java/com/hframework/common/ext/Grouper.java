package com.hframework.common.ext;

/**
 * User: zhangqh6
 * Date: 2016/2/18 18:09:09
 */
public interface Grouper<K, V> {
    <K> K groupKey(V v);
}
