package com.tryRPC.utils;

import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import cn.hutool.setting.yaml.YamlUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class is used to load the configuration of the RPC framework and
 * return the configuration object.
 * This is convient for the user to multi-use the configuration object.
 */
@Data
public class ConfigUtils {

    // 支持reload的配置文件加载
    public static <T> T loadConfig(Class<T> configClass,String filePath) throws FileNotFoundException {
        try(InputStream input = new FileInputStream(filePath)){
            Yaml yaml = new Yaml();
            return yaml.loadAs(input,configClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static <T> T loadConfig(Class<T> tClass,String prefix,String environment){
            //return  loadConfigProperties(tClass,prefix,environment);
            return loadConfigYaml(tClass,prefix,environment);
    }


    /**
     * 加载配置 for Properties file
     * @param tClass
     * @param prefix
     * @param environment This variable is used to add the prefix name like:
     *                    "dev", "test", "prod" to the configuration file name.
     *                    For example, if the configuration file name is "rpc.properties",
     *                    the configuration file name will be "rpc-dev.properties"
     * @param <T>
     * @return
     */
    public static <T> T loadConfigProperties(Class<T> tClass,String prefix,String environment){
        StringBuilder builder = new StringBuilder("application");
        if(!StrUtil.isBlankIfStr(environment)) {
            builder.append("-").append(environment);
        }
        // Because that this is a properties file.
        builder.append(".properties");
        Props props = new Props(builder.toString());
        /**
         * This method is used to convert the configuration file to a bean object.
         * It will traversal all the KV pairs in the configuration file and
         * set the value to the corresponding field in the bean object.
         */
        return props.toBean(tClass,prefix);
    }
    /**
     * 类加载器
     * 首先，我们这里的 {@code ConfigUtils.class.getClassLoader()} 是一个类加载器对象，因为我们的
     * 配置文件是放在 resources 目录下的，而在打包项目的时候，resources 目录下的文件会被打包到 jar 包中，存在于JVM的类路径中。
     * 如果想要准确的读取resources目录下的文件，我们就需要使用类加载器来正确的读取文件。
     */
    public static <T> T loadConfigYaml(Class<T> tClass,String prefix,String environment){

        // 创建 Yaml 对象;
        Yaml yaml = new Yaml();
        System.out.println("Now we are loading the yaml file.");
        StringBuilder builder = new StringBuilder("application");
        if(!StrUtil.isBlankIfStr(environment)) {
            builder.append("-").append(environment);
        }
        // Because that this is a yaml file.
        builder.append(".yaml");

        // 加载配置文件;
        // 从资源文件夹中加载 yaml 文件，并将其转化为 InputStream 输入流对象 以供 SnakeYaml 读取和解析
        try(InputStream in = ConfigUtils.class.getClassLoader().getResourceAsStream(builder.toString())){
            if(in == null){
                throw new Exception("Sorry,the yaml file is not found.");
            }
            return yaml.loadAs(in,tClass);
        }catch (Exception e){
            //log.info("[Error] : We cannot load the yaml file.");
            e.printStackTrace();
        }
        return null;
    }


}
