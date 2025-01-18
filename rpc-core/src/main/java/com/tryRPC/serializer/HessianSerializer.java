package com.tryRPC.serializer;

import com.caucho.hessian.io.*;
import com.caucho.hessian.io.SerializerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianSerializer implements Serializer {

    private static final SerializerFactory SERIALIZER_FACTORY = new SerializerFactory();

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Hessian2Output hessian2Output = new Hessian2Output(byteArrayOutputStream);
            hessian2Output.setSerializerFactory(SERIALIZER_FACTORY);
            hessian2Output.writeObject(obj);
            hessian2Output.close();
            return byteArrayOutputStream.toByteArray();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            Hessian2Input hessian2Input = new Hessian2Input(byteArrayInputStream);
            hessian2Input.setSerializerFactory(SERIALIZER_FACTORY);
            return (T) hessian2Input.readObject(type);
        }
    }
}
