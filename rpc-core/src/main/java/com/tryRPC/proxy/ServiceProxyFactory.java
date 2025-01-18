package com.tryRPC.proxy;

import com.tryRPC.Holder;
import com.tryRPC.utils.ConfigUtils;
import com.tryRPC.utils.RpcConfig;

import java.lang.reflect.Proxy;

public class ServiceProxyFactory {
    public static <T> T getProxy(Class<T> serviceClass){
        // By distinguishing the configuration,we can know whether to use the mock proxy or the real proxy;
        if(Holder.getRpcConfig().getIsMock())
            return getMockProxy(serviceClass);

        /**
         * 解释一下下吗这个返回一个代理的对象;
         * 1. serviceClass.getClassLoader() -> 获取类加载器; 告诉Java从哪里加载这个类;
         *  可以看成是一个工具箱子.
         * 2. new Class[]{serviceClass} -> 代理的接口;
         *  告诉Java代理类需要实现哪些接口？我们这里only传入了的是 serviceClass;
         * 3. new ServiceProxy() -> 代理对象;
         *  告诉Java当代理类的方法被调用的时候，应该执行什么逻辑。
         */
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }


    public static <T> T getMockProxy(Class<T> serviceClass){
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServiceProxy());
    }
}
