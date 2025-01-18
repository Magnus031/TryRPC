package com.tryRPC.consumer;

import com.tryRPC.model.User;
import com.tryRPC.proxy.ServiceProxyFactory;
import com.tryRPC.service.UserService;

/**
 * Hello world!
 *
 */
public class EasyConsumerExample
{
    public static void main( String[] args )
    {
        // This is a code for the easy consumer;
        // Now we cannot get the instance of UserService, so we set it as null;
        // Then we will come to implement it.

        // 静态代理;
        // UserService userService = new UserServiceProxy();
        // 改用动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        System.out.println("UserService.class : "+ UserService.class);
        User user = new User();
        user.setName("Kobe");

        User newUser = userService.getUser(user);
        if(newUser!=null){
            System.out.println("Get user by name: " + newUser.getName());
        }else{
            System.out.println("user == null");
        }
        long number = userService.getNumber();
        System.out.println("Number is: " + number);

    }
}
