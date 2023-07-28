package org.landy.singleton.lazy;

/**
 * //懒汉式单例
 //特点：在外部类被调用的时候内部类才会被加载
 //内部类一定是要在方法调用之前初始化
 //巧妙地避免了线程安全问题
 //这种形式兼顾饿汉式的内存浪费，也兼顾synchronized性能问题
 //完美地屏蔽了这两个缺点
 //史上最牛B的单例模式的实现方式

 https://juejin.cn/post/6844903782187270151

 * Created by Landy on 2018/8/20.
 */
public class LazyThree {

    //构造函数中的代码和 initialized 变量的作用是为了防止反射注入。加了这两部分后在LazyThreeTest类中的测试代码会报错
    //如果去掉static，就可以被反射强制访问，调用两次了
    private static boolean initialized = false;

    //防止反射注入
    //默认使用LazyThree的时候，会先初始化内部类
    //如果没使用的话，内部类是不加载的
    private LazyThree(){
        synchronized (LazyThree.class){
            if(initialized == false){
                initialized = !initialized;
            }else{
                throw new RuntimeException("单例已被侵犯");
            }
        }
    }


    //每一个关键字都不是多余的
    //static 是为了使单例的空间共享
    //final 保证这个方法不会被重写，重载
    public static final LazyThree getInstance(){
        //在返回结果以前，一定会先加载内部类
        return LazyHolder.LAZY;
    }
    //默认不加载
    private static class LazyHolder{
        private static final LazyThree LAZY = new LazyThree();
    }


}
