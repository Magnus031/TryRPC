package com.tryRPC.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.tryRPC.model.RpcRequest;
import com.tryRPC.model.RpcResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This is a class for using Kryo to serialize and deserialize.
 * Another thing to say is that the Kryo is not thread-safe, so we need to use ThreadLocal to store the Kryo object.
 */
public class KryoSerializer implements  Serializer{

    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(()->{
       Kryo kryo = new Kryo();
       kryo.setRegistrationRequired(false);
       return kryo;
    });

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        // 获得当前线程的独立 {@code Kryo} 对象
        KRYO_THREAD_LOCAL.get().writeObject(output,obj);
        output.close();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        // Here input class is a InputStream buffer for bytes;
        Input input = new Input(byteArrayInputStream);
        T obj = KRYO_THREAD_LOCAL.get().readObject(input,type);
        input.close();
        return obj;
    }
}
