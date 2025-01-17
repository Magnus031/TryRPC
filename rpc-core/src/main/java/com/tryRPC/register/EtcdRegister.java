package com.tryRPC.register;

import com.tryRPC.config.RegisterConfig;
import com.tryRPC.model.ServiceMetaInfo;

import java.util.List;

public class EctdRegister implements RegisterTry{

    @Override
    public void init(RegisterConfig registerConfig) {
        
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {

    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) throws Exception {

    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        return List.of();
    }

    @Override
    public void destory() {

    }
}
