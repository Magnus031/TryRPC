package com.tryRPC.model;

import java.io.Serializable;


public class User implements Serializable {
    // The reason we implement Serializable is because we want to serialize this object and send it over the network;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
