package com.tryRPC.service;

import com.tryRPC.model.User;

public interface UserService {

    /**
     * Get user by user
     * @param user
     * @return
     */
    User getUser(User user);


    /**
     * Used to set for a mock part method;
     * @return
     */
    default int getNumber(){
        return 3;
    }
}
