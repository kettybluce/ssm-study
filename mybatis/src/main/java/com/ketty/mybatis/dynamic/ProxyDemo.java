package com.ketty.mybatis.dynamic;

import java.lang.reflect.Method;

/**
 * @Author ketty bluce
 * @Create 2023/4/26
 * 动态代理
 * @Version 1.0
 */
public class ProxyDemo {

    public static void main(String[] args) {


    }
}


interface Test {
    /**
     * 测试方法
     */
    public void say();
}

interface InvokeHandler {
    Object invoke(Object obj, Method method, Object... args);
}
