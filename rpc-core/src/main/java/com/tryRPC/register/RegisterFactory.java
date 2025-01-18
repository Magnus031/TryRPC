package com.tryRPC.register;

import com.tryRPC.spi.SpiLoader;

public class RegisterFactory{
    static{
        // load for the RegisterClass
        SpiLoader.load(RegisterTry.class);
    }

    // 定义默认的注册中心
    private static final RegisterTry DEAFUALT_REGISTER = new EtcdRegister();

    public static RegisterTry getInstance(String key){
        return SpiLoader.getInstance(RegisterTry.class,key);
    }
}