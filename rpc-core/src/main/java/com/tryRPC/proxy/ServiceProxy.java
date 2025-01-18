package com.tryRPC.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.tryRPC.Holder;
import com.tryRPC.constant.RpcConstant;
import com.tryRPC.model.RpcRequest;
import com.tryRPC.model.RpcResponse;
import com.tryRPC.model.ServiceMetaInfo;
import com.tryRPC.register.RegisterFactory;
import com.tryRPC.register.RegisterTry;
import com.tryRPC.serializer.JdkSerializer;
import com.tryRPC.serializer.Serializer;
import com.tryRPC.serializer.SerializerFactory;
import com.tryRPC.utils.RpcConfig;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * This Method ServiceProxy is after the ServiceProxyFactory;
 * 1. We have got the ClassLoader
 * 2. We have got the Class Array -> Class[] {serviceClass}
 * 3. Then we return a new ServiceProxy(); which is a new Proxy instance;
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /**
         * 总结一下
         * 首先，我们知道在invoke的总体逻辑就是通过一个动态代理来帮助我们调用远程的服务。
         * 我们所需要做的就是构造一个请求，然后通过在注册中心中找到服务的方法，并且调用它，然后返回服务方法运行的结果。这个就是我们的总体逻辑。
         *
         * 细分一下就是:
         * Step1:
         *  首先，我们需要一个序列化器来帮助我们传输RpcRequest和RpcResponse。
         *      这个时候我们就需要从SerializerFactory工厂中来获取实例，因为我们假设在通过配置文件指定了一个序列化器。那么就需要从序列化工厂中找到对应的序列化器。
         * Step2:
         *  构造 RpcRequest 请求。这个是我们需要利用builder来创建一个RpcRequest对象。
         * Step3:
         *  发送请求。这个时候我们需要从注册中心来获取服务提供者的地址，然后发送请求。
         *    Step3.1 :
         *      首先，我们需要序列化RpcRequest请求。
         *    Step3.2 :
         *      然后，我们先从当前的RpcConfig的配置文件中找到需要的注册中心的配置。
         *    Step3.3 :
         *      然后我们就可以在注册中心工厂中寻找我们的注册中心实例。如果没有，就直接的利用默认的注册中心。
         *    Step3.4 :
         *      在注册中心中寻找我们需要的服务中的所有节点。
         *    Step3.5 :
         *      这里，我们选择服务中的第一个节点来进行请求。
         * Step 4:
         *  我们找到了可以提供服务的节点之后，我们就可以发送请求了。往找到的服务节点中发送Request请求。
         *
         * Step5 :
         * 返回结果。
         *
         */


        // 指定序列化器
        Serializer serializer = SerializerFactory.getInstance(Holder.getRpcConfig().getSerializer());

        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();


        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // 从注册中心来获取服务提供者请求地址
            RpcConfig rpcConfig = Holder.getRpcConfig();
            RegisterTry registerTry = RegisterFactory.getInstance(rpcConfig.getRegisterConfig().getRegisterType());

            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFUALT_SERVICE_VERSION);
            System.out.println(serviceMetaInfo);
            // 找到所有提供该服务的节点;
            List<ServiceMetaInfo> list = registerTry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            System.out.println("list  : has "+list.size()+" numbers");
            // 没有节点提供这样的服务；
            if(CollUtil.isEmpty(list))
                throw new RuntimeException("暂无服务地址");
            // 我们优先选取第一个服务;
            ServiceMetaInfo selectedServiceMetaInfo = list.get(0);
            // 在找到可以提供服务的节点之后，我们进行发送请求;
            try(HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress()).header("Content-Type", "application/json").body(bodyBytes).execute()){
                byte[] results = httpResponse.bodyBytes();
                RpcResponse rpcResponse = serializer.deserialize(results, RpcResponse.class);
                return rpcResponse.getData();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}