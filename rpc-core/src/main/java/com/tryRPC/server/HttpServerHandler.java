package com.tryRPC.server;

import com.tryRPC.Holder;
import com.tryRPC.model.RpcRequest;
import com.tryRPC.model.RpcResponse;
import com.tryRPC.register.LocalRegister;
import com.tryRPC.serializer.JdkSerializer;
import com.tryRPC.serializer.Serializer;
import com.tryRPC.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;


public class HttpServerHandler implements Handler<HttpServerRequest> {
    /**
     * This class is used to handler the request from the client.
     * 1. 反序列化请求为对象，并从请求对象中获取参数
     * 2. 根据服务名称从本地注册中心获取服务实例
     * 3. 反射调用服务实例的方法
     * 4. 将调用结果封装为响应对象并序列化返回
     */
    @Override
    public void handle(HttpServerRequest request) {
        // 指定序列化器皿 -> We use the Serializer Factory to get the serializer
        final Serializer serializer = SerializerFactory.getInstance(Holder.getRpcConfig().getSerializer());

        // 记录日志
        System.out.println("Received request " + request.method() + " " + request.uri());

        // 异步处理HTTP请求
        request.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();

            if (rpcRequest == null) {
                rpcResponse.setMessage("Request is null");
                doResponse(request, rpcResponse, serializer);
                return;
            }

            // 通过反射方法调用服务器端的方法
            try{
                Class<?> implClass = LocalRegister.get(rpcRequest.getServiceName());
                // 因为我们是通过反射来寻找，也就是函数标签 -> 函数名字 + 参数类型 来找到某个设备中的函数
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(implClass.newInstance(),rpcRequest.getArgs());

                // 封装返回的结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("Ok");
            }catch (Exception e){
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }

            // 处理响应结果
            doResponse(request, rpcResponse, serializer);
        });
    }

    /**
     * 处理响应结果
     * @param request
     * @param rpcResponse
     * @param serializer
     */
    void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        // 序列化响应结果，并且通过HTTP响应返回给客户端。
        HttpServerResponse httpServerResponse = request.response().putHeader("content-type", "application/json");
        try{
            byte[] serialized = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}