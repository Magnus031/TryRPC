package com.tryRPC.config;

import lombok.Data;

/**
 * 注册中心的配置类
 * 用于储存的是用户配置连接注册中心所需要的信息;
 */
@Data
public class RegisterConfig {
    private String registerType = "etcd";

    private String address = "http://localhost:2380";

    private String userName;

    private String password;

    // 超出时间；
    private Long timeout = 10000L;
}
