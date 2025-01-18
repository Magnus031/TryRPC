package com.tryRPC.register;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.tryRPC.config.RegisterConfig;
import com.tryRPC.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class EtcdRegister implements RegisterTry{
    private Client client;

    // etcd 操作客户端
    private KV kvClient;

    /**
     * etcd 根路径
     */
    private static final String ETCD_ROOT_PATH = "/tryRPC/";

    @Override
    public void init(RegisterConfig registerConfig) {
        /**
         * Here first we create a etcd client;
         * We use the builder pattern to create a client;
         * Then we take out the kvClient to operate the etcd;
         */
        client = Client.builder().endpoints(registerConfig.getAddress()).connectTimeout(Duration.ofSeconds(registerConfig.getTimeout())).build();
        //client = Client.builder().endpoints(registerConfig.getAddress()).connectTimeout(Duration.ofMillis(registerConfig.getTimeout())).build();
        kvClient = client.getKVClient();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        // 创建 Lease 和 KV 客户端
        Lease leaseClient = client.getLeaseClient();

        // 创建一个 30s 的租约 grant 方法是创建一个租约，然后返回一个租约的 ID
        long leaseId = leaseClient.grant(3000).get().getID();

        // 注册服务
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        PutOption putOption = PutOption.newBuilder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) throws Exception {
        kvClient.delete(ByteSequence.from(ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey(), StandardCharsets.UTF_8));
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";
        try{
            // 前缀查询
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue>  keyValues = kvClient.get(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), getOption).get().getKvs();
            // 利用流将对象转化为Bean对象 解析服务信息
            return keyValues.stream().map(keyValue -> {
                String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);
            }).collect(Collectors.toList());

        }catch (Exception e){
            throw new RuntimeException("获取列表失败",e);
        }
    }

    @Override
    public void destory() {
        System.out.println("This Node is destroyed");
        if(kvClient!=null){
            kvClient.close();
        }
        if(client!=null){
            client.close();
        }
    }
}
