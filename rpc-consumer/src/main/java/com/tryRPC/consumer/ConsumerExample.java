package com.tryRPC.consumer;

import com.tryRPC.utils.ConfigUtils;
import com.tryRPC.utils.Monitor;
import com.tryRPC.utils.RpcConfig;

public class ConsumerExample {
    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc", null);
        System.out.println(rpc);
        Monitor monitor = new Monitor();
        monitor.startWatching();
    }
}
