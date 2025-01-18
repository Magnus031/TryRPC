package com.tryRPC.serializer;

import com.tryRPC.spi.SpiLoader;

/**
 * Here we use the Factory Design Pattern to create the Serializer.
 * We have originally offered some Serializer implementations, and we can add more in the future.
 */
public class SerializerFactory {
    static{
        SpiLoader.load(Serializer.class);
    }

    private static final Serializer DEFUALT_SERIALIZER = new JdkSerializer();

    /**
     * Get the serializer by the key.
     */
    public static Serializer getInstance(String key){
        return SpiLoader.getInstance(Serializer.class,key);
    }
}
