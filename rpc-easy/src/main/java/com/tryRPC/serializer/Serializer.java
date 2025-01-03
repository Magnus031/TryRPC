package com.tryRPC.serializer;

import java.io.IOException;

/**
 * Serializer interface
 */
public interface Serializer {
    /**
     * Serialize object
     * @param obj
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> byte[] serialize(T obj) throws IOException;


    /**
     * Deserialize object
     * @param bytes
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T deserialize(byte[] bytes, Class<T> type) throws IOException;


}
