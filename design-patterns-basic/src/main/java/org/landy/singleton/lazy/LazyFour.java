package org.landy.singleton.lazy;

import java.util.HashMap;
import java.util.Map;

public class LazyFour {

    private LazyFour() {}
    //静态块，公共内存区域
    private static volatile LazyFour instance = null;

    public static LazyFour getInstance() {
        if (instance == null) { // 第一次检查
            synchronized (LazyFour.class) {
                if (instance == null) { // 第二次检查，"double check"的由来
                    instance = new LazyFour();
                    //新建对象的三个步骤：
                    //1、在内存中开辟一块地址
                    //2、对象初始化
                    //3、将指针指向这块内存地址
                    //指令重排会将以上 2 3 两步交换，会导致其他线程拿到一个没有初始化的实例。所以需要加 volatile 关键字防止指令重排
                }
            }
        }
        //如果已经初始化，直接返回之前已经保存好的结果
        return instance;
    }
}


//DCL在解决缓存雪崩中的用法
abstract class DoubleCheckCache {
    private final Map<String, Object> localCache = new HashMap<>();
    public Object get(String key) {
        Object res = null;
        if (localCache.get(key) == null) { // 第一次检查
            synchronized (this) {
                if (localCache.get(key) == null) { // 第二次检查，其他排队请求获取锁的线程走到这里时已经能够看到缓存中的值了，也就不用再发起远程调用了
                    res = loadExternal(key);
                    localCache.put(key, res);
                }
            }
        }
        return res;
    }
    // 从外部加载key对应的value，通常是从数据库加载或者是发起RPC调用来加载，此操作是耗时的
    protected abstract Object loadExternal(String key);
}
