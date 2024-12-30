package com.tryRPC.provider;

import com.tryRPC.model.User;
import com.tryRPC.service.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println("Get user by name: " + user.getName());
        return user;
    }

    

}
