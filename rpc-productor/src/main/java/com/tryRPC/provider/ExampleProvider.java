package com.tryRPC.provider;

import com.tryRPC.Holder;
import com.tryRPC.config.RegisterConfig;
import com.tryRPC.model.ServiceMetaInfo;
import com.tryRPC.register.LocalRegister;
import com.tryRPC.register.RegisterFactory;
import com.tryRPC.register.RegisterTry;
import com.tryRPC.server.HttpServer;
import com.tryRPC.server.VertxHttpServer;
import com.tryRPC.service.UserService;
import com.tryRPC.utils.RpcConfig;

public class ExampleProvider {
    public static void main(String[] args) throws Exception {
        // RPC 框架初始化
        Holder.init();
        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegister.register(serviceName, UserServiceImpl.class);

        System.out.println("Server name : "+serviceName+" "+UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = Holder.getRpcConfig();
        RegisterConfig registerConfig = rpcConfig.getRegisterConfig();
        RegisterTry registerTry = RegisterFactory.getInstance(registerConfig.getRegisterType());
        // 定义新服务;
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try{
            registerTry.register(serviceMetaInfo);
        }catch (Exception e){
            e.printStackTrace();
        }

        HttpServer httpServer = new VertxHttpServer();
        System.out.println("Server start at " + rpcConfig.getServerHost() + ":" + rpcConfig.getServerPort());
        httpServer.doStart(rpcConfig.getServerPort());

    }
}
