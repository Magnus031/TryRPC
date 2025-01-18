package com.tryRPC.utils;

import com.tryRPC.config.RegisterConfig;
import com.tryRPC.serializer.SerializerKeys;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {@code RPC} 配置
 * This class is used to store the configuration of the RPC framework.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RpcConfig {
    /**
     * 名称
     */
    private String name = "TryRPC";

    /**
     * 版本
     */
    private String version = "1.0.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务端口
     */
    private Integer serverPort = 8000;

    /**
     * 模拟调用 (默认为 false)
     */
    private Boolean isMock = false;

    // 注册中心配置;
    private RegisterConfig registerConfig = new RegisterConfig();


    private String Serializer = SerializerKeys.JDK;
}
