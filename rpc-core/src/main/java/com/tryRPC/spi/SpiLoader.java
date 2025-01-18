package com.tryRPC.spi;


import cn.hutool.core.io.resource.ResourceUtil;
import com.tryRPC.register.RegisterTry;
import com.tryRPC.serializer.Serializer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Data
public class SpiLoader {
    /**
     * 1. 用Map来储存已加载的配置信息 键名 - > 实现类
     * 2. 扫描指定路径，读取每个配置文件，获取到键名-》实现类的信息并储存到Map中
     * 3. 定义获取实例方法，根据用户传入的接口和键名，从Map中获取实现类并实例化
     *
     */

    /**
     * 储存已经加载的类 接口名 => （key => 实现类);
     *
     * eg
     * Serializer => (jdk => JdkSerializer)
     * Serializer => (kryo => KryoSerializer)
     * Serializer => (hessian => HessianSerializer)
     *
     * Register => (etcd => EtcdRegister)
     * Register => (zookeeper => ZookeeperRegister)
     */
    private static Map<String,Map<String,Class<?>>> loaderMap = new ConcurrentHashMap<>();

    /**
     * 对象实例缓存，缓存已经加载过的对象实例
     */
    private static Map<String,Object> instanceCache = new ConcurrentHashMap<>();


    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    private static final String[] SCAN_DIRS = new String[]{RPC_CUSTOM_SPI_DIR,RPC_SYSTEM_SPI_DIR};

    /**
     * 动态加载类的列表
     * 1. We put the Serializer.class into the List;
     */
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class, RegisterTry.class);

    /**
     * 加载所有的spi配置
     */
    public static void loadAll(){
        log.info("load all spi");
        for(Class<?> load_class:LOAD_CLASS_LIST){
            load(load_class);
        }
    }

    /**
     * 获取服务实例
     * @param tClass
     * @param key
     * @return
     * @param <T>
     */
    public static <T> T getInstance(Class<?> tClass,String key){
        String tClassName = tClass.getName();
        Map<String,Class<?>> keyClassMap = loaderMap.get(tClassName);
        if(keyClassMap == null)
            throw new RuntimeException(String.format("SpiLoader 未加载这个类 %s",tClassName));
        if(!keyClassMap.containsKey(key))
            throw new RuntimeException(String.format("SpiLoader 未找到这个key=%s",key));
        // 假设是存在的 比如我们要从Serializer中获取key=jdk
        Class<?> implClass = keyClassMap.get(key);
        String implClassName = implClass.getName();
        // 如果在Cache缓存中没有找到实例化对象，那么就存入，我们只用存一次。
        if(!instanceCache.containsKey(implClassName)){
            try{
                Object instance = implClass.newInstance();
                instanceCache.put(implClassName,instance);
            }catch (InstantiationException | IllegalAccessException e){
                log.error("spi instance create error",e);
            }
        }
        return (T) instanceCache.get(implClassName);

    }



    public static Map<String,Class<?>> load(Class<?> load_class){
        log.info("load spi class:{}",load_class.getName());
        // 扫描路径，优先搜索Custom
        Map<String,Class<?>>keyClassMap = new HashMap<>();

        for(String scanDir:SCAN_DIRS){
            List<URL> resources = ResourceUtil.getResources(scanDir+load_class.getName());
            System.out.println("FilePath : "+ scanDir+load_class.getName());
            for(URL url : resources){
                try{
                    InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    String line ;
                    while((line = bufferedReader.readLine())!=null){
                        String[] split = line.split("=");
                        if(split.length!=2){
                            continue;
                        }
                        String key = split[0].trim();
                        String className = split[1].trim();
                        Class<?> aClass = Class.forName(className);
                        keyClassMap.put(key,aClass);
                    }

                }catch (Exception e){
                    log.error("spi resource load error",e);
                }
            }
        }
        loaderMap.put(load_class.getName(),keyClassMap);
        return keyClassMap;
    }

}
