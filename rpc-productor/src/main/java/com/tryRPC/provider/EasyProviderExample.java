package com.tryRPC.provider;

import com.tryRPC.register.LocalRegister;
import com.tryRPC.server.HttpServer;
import com.tryRPC.server.VertxHttpServer;
import com.tryRPC.service.UserService;

public class EasyProviderExample {
    public static void main(String[] args) {
        // 注册服务;
        // 由于我们现在先不考虑注册中心了，先考虑的是本地注册服务器；
        LocalRegister.register(UserService.class.getName(), UserServiceImpl.class);

        // here test for the web server
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
