package com.tryRPC.model;


import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used to encapsulate the meta information of all kinds of services;
 */
@Slf4j
@Data
public class ServiceMetaInfo {

    private String serviceName;

    private String serviceVersion = "1.0.0";

    private String serviceHost;

    private Integer servicePort;
    // 设置的是服务分组;
    private String serviceGroup = "default";

    // The following is the methods of the service;
    /**
     * Get the key of the service;
     * @return
     */
    public String getServiceKey(){
        return String.format("%s:%s",serviceName,serviceVersion);
    }
    /**
     * @brief Here is the example: key = /service:1.0.0
     *        -> nodeKey : /service:1.0.0/localhost:8080
     * @return
     */
    public String getServiceNodeKey(){
        return String.format("%s/%s:%s",getServiceKey(),serviceHost,servicePort);
    }


    public String getServiceAddress(){
        if (!StrUtil.contains(serviceHost,"http")){
            return String.format("http://%s:%s",serviceHost,servicePort);
        }
        return String.format("%s:%s",serviceHost,servicePort);
    }
}
