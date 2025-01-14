package com.tryRPC.register;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalRegister {
    // 使用线程安全的ConcurrentHashMap存储服务名称和服务对象,key为服务名称，value为服务的实现类。
    // 之后就可以根据要调用的服务名称来获取到具体的实现类。然后通过反射进行方法调用。

    /**
     * 用来储存注册信息
     */
    private static final Map<String, Class<?>> map = new ConcurrentHashMap<>();

    /**
     *
     * @param serviceName
     * @param implclass
     */
    public static void register(String serviceName,Class<?> implclass){
        map.put(serviceName,implclass);
    }

    /**
     * The function to get the function class;
     * @param serviceName
     * @return
     */
    public static Class<?> get(String serviceName){
        return map.get(serviceName);
    }

    /**
     * The function to remove the function class;
     * @param serviceName
     */
    public void remove(String serviceName){
        map.remove(serviceName);
    }



}
