package com.tryRPC.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 响应类 RpcResponse 封装调用方法得到的返回值、以及调用的信息（比如异常情况）等。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RpcResponse implements Serializable {
    // The return data;
    private Object data;

    // 响应数据类型（预留）
    private Class<?> dataType;

    // The exception;
    private Exception exception;

    // The response message;
    private String message;

}
