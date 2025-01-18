package com.tryRPC;

import com.tryRPC.config.RegisterConfig;
import com.tryRPC.model.ServiceMetaInfo;
import com.tryRPC.register.EtcdRegister;
import com.tryRPC.register.RegisterTry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class RegisterTryTest {
    // Here we use the EtcdRegister class to test the RegisterTry interface.
    final RegisterTry registerTry = new EtcdRegister();

    @Before
    public void init(){
        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setAddress("http://localhost:2379");
        registerTry.init(registerConfig);
    }

    @Test
    public void testRegister() throws Exception {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1235);
        registerTry.register(serviceMetaInfo);
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);
        registerTry.register(serviceMetaInfo);
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("2.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1235);
        registerTry.register(serviceMetaInfo);
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1235);
        registerTry.unRegister(serviceMetaInfo);
    }

    @Test
    public void unRegisterTest() throws Exception {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);
        registerTry.unRegister(serviceMetaInfo);
    }

    @Test
    public void RegisterDiscoveryTest() throws Exception {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1235);
        registerTry.register(serviceMetaInfo);
        List<ServiceMetaInfo> list = registerTry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        Assert.assertEquals(1, list.size());
//        serviceMetaInfo.setServiceName("myService");
//        serviceMetaInfo.setServiceVersion("1.0");
//        serviceMetaInfo.setServiceHost("localhost");
//        serviceMetaInfo.setServicePort(1234);
//        registerTry.register(serviceMetaInfo);
//        list = registerTry.serviceDiscovery(serviceMetaInfo.getServiceKey());
//        Assert.assertEquals(2, list.size());
//        serviceMetaInfo.setServiceName("myService");
//        serviceMetaInfo.setServiceVersion("2.0");
//        serviceMetaInfo.setServiceHost("localhost");
//        serviceMetaInfo.setServicePort(1235);
//        registerTry.register(serviceMetaInfo);
//        list = registerTry.serviceDiscovery(serviceMetaInfo.getServiceKey());
//        for(ServiceMetaInfo info : list){
//            System.out.println(info);
//        }
//        Assert.assertEquals(1,list.size());
//        serviceMetaInfo.setServiceName("myService");
//        serviceMetaInfo.setServiceVersion("1.0");
//        serviceMetaInfo.setServiceHost("localhost");
//        serviceMetaInfo.setServicePort(1235);
//        registerTry.unRegister(serviceMetaInfo);
//        serviceMetaInfo.setServicePort(1234);
//        registerTry.unRegister(serviceMetaInfo);

    }
}
