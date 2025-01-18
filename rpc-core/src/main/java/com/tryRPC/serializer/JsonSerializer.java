package com.tryRPC.serializer;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tryRPC.model.RpcRequest;
import com.tryRPC.model.RpcResponse;

import java.io.IOException;

public class JsonSerializer implements Serializer{

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        // Here we trans the Obj to the Json bytes arrays;
        return OBJECT_MAPPER.writeValueAsBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        // plz use hutools 's JSONUtils to deserialize json to object
        T object = OBJECT_MAPPER.readValue(bytes,type);

        if(object instanceof RpcRequest) {
            return handleRequest((RpcRequest) object, type);
        }else if(object instanceof RpcResponse){
            return handleResponse((RpcResponse) object,type);
        }

        return object;
    }

    /**
     * 因为在进行反序列化的时候，会进行泛型擦拭，就导致了需要单独对 rpcRequest 和 rpcResponse 进行处理。
     * @param rpcRequest
     * @param type
     * @return
     * @param <T>
     * @throws IOException
     */
    private <T> T handleRequest(RpcRequest rpcRequest, Class<T> type) throws IOException {
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] args = rpcRequest.getArgs();

        for(int i=0;i<parameterTypes.length;i++){
            Class<?> clazz = parameterTypes[i];
            if(!clazz.isAssignableFrom(args[i].getClass())){
                // 如果参数类型不匹配，抛出异常
                // 如果出现了异常，那么我们就进行重新的序列化为既定的类型;
                byte[] argBytes = OBJECT_MAPPER.writeValueAsBytes(args[i]);
                args[i] = OBJECT_MAPPER.readValue(argBytes,clazz);
            }
        }
        return type.cast(rpcRequest);
    }


    private <T> T handleResponse(RpcResponse rpcResponse, Class<T> type) throws IOException {
        byte[] data = OBJECT_MAPPER.writeValueAsBytes(rpcResponse.getData());
        rpcResponse.setData(OBJECT_MAPPER.readValue(data,rpcResponse.getDataType()));
        return type.cast(rpcResponse);
    }


}
