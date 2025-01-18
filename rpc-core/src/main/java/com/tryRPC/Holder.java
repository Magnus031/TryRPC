package com.tryRPC;

import com.tryRPC.config.RegisterConfig;
import com.tryRPC.register.RegisterFactory;
import com.tryRPC.register.RegisterTry;
import com.tryRPC.utils.ConfigUtils;
import com.tryRPC.utils.RpcConfig;
import lombok.extern.slf4j.Slf4j;

import java.rmi.registry.Registry;

import static com.tryRPC.constant.RpcConstant.DEFAULT_CONFIG_PREFIX;

/**
 * This is a holder class for the RPC singleton.
 * Global configuration.
 *
 */
@Slf4j
public class Holder
{
    // The single rpcConfig instance.
    private static volatile RpcConfig rpcConfig;

    /**
     * @brief init the {@code rpcConfig} with the default configuration.
     * This method is used the {@code ConfigUtils} to load the configuration file given.
     * If the configuration file is not found, then use the default configuration.
     */
    public static void init(){
        RpcConfig rpcConfig;
        try{
            // load for the configuration
            rpcConfig = ConfigUtils.loadConfig(RpcConfig.class, DEFAULT_CONFIG_PREFIX, null);
        }catch (Exception e){
            // if load failed, then use the default configuration
            rpcConfig = new RpcConfig();
        }
        init(rpcConfig);
        log.info("[Processed]: init, rpcConfig: {}", rpcConfig);
    }

    // init with rpcConfig
    public static void init(RpcConfig rpcConfig){
        if(rpcConfig == null){
            throw new IllegalArgumentException("rpcConfig cannot be null");
        }
        Holder.rpcConfig = rpcConfig;
        log.info("[Processed]: init with argument, rpcConfig: {}", rpcConfig.toString());
        // 注册中心初始化
        RegisterConfig registerConfig = rpcConfig.getRegisterConfig();
        RegisterTry registerTry = RegisterFactory.getInstance(registerConfig.getRegisterType());
        registerTry.init(registerConfig);
        log.info("[Processed]: registerTry init, registerConfig: {}", registerConfig.toString());
    }


    // get the rpcConfig
    public static RpcConfig getRpcConfig(){
        // double lock check;
        if(rpcConfig == null){
            // ensure that only one thread can init the rpcConfig;
            synchronized (Holder.class){
                if(rpcConfig == null){
                    log.info("[Warning]: Sorry the rpcConfig now is empty,so that we need to init it just now.");
                    init();
                }
            }
        }
        return rpcConfig;
    }

    public static void main( String[] args )
    {
    }
}
