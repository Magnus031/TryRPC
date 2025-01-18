package com.tryRPC.register;

import com.tryRPC.config.RegisterConfig;
import com.tryRPC.model.ServiceMetaInfo;

import java.util.List;

public interface RegisterTry {
    void init(RegisterConfig registerConfig);

    /**
     * 注册服务
     * @param serviceMetaInfo
     * @throws Exception
     */
    void register(ServiceMetaInfo serviceMetaInfo)throws Exception;

    /**
     * 取消注册服务
     * @param serviceMetaInfo
     * @throws Exception
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo)throws Exception;

    /**
     * 服务发现
     * @param serviceKey
     * @return
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    // 销毁
    void destory();
}
