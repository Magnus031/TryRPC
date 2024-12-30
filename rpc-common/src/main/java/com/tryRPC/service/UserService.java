package com.tryRPC.service;

import com.tryRPC.model.User;

public interface UserService {

    /**
     * Get user by user
     * @param user
     * @return
     */
    User getUser(User user);
}
