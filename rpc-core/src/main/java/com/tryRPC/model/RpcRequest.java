package com.tryRPC.model;

import com.tryRPC.constant.RpcConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 请求处理器是 RPC 框架的实现关键，它的作用是：处理接收到的请求，并根据请求参数找到对应的服务和方法，
 * 通过反射实现调用，最后封装返回结果并响应请求。
 *
 * 请求类的作用是封装了调用信息，包括服务名称、方法名称、参数类型列表、参数列表等。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数类型列表
     */
    private Class<?>[] parameterTypes;

    /**
     * 参数列表
     */
    private Object[] args;

    /**
     * 请求的服务版本
     */
    private String serviceVersion = RpcConstant.DEFUALT_SERVICE_VERSION;

}