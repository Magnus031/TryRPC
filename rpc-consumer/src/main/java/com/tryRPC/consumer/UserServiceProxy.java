package com.tryRPC.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.tryRPC.model.RpcRequest;
import com.tryRPC.model.RpcResponse;
import com.tryRPC.model.User;
import com.tryRPC.serializer.JdkSerializer;
import com.tryRPC.serializer.Serializer;
import com.tryRPC.service.UserService;

public class UserServiceProxy implements UserService {

    @Override
    public User getUser(User user) {
        // Serialize the object;
        Serializer serializer = new JdkSerializer();

        // 发送请求
        // -> 构造了一个 rpcRequest 对象,利用 @Builder 注解
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class<?>[]{User.class})
                .args(new Object[]{user})
                .build();

        try{
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // 发送请求
            byte[] result;

            // 模拟了一个 HTTP 请求的过程;
            try(HttpResponse httpResponse = HttpRequest.post("http://localhost:8000/")
                    .header("Content-Type", "application/json")
                    .body(bodyBytes)
                    .execute()){
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return (User) rpcResponse.getData();

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
