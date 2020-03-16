package com.xkl.rpc.registry.zookeeper;

import com.xkl.rpc.registry.ServiceRegistry;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author xkl
 * @date 2020/3/15
 * @description 实现服务注册功能
 **/
public class ZookeeperServiceRegistry implements ServiceRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperServiceRegistry.class);
    private CountDownLatch latch = new CountDownLatch(1);

    private final ZkClient zkClient;
    public ZookeeperServiceRegistry(String zkAddress) {
        zkClient = new ZkClient(zkAddress, Constants.ZK_SESSION_TIMEOUT,Constants.ZK_CONNECTION_TIMEOUT);
        LOGGER.debug("connect zookeeper");
    }

    @Override
    public void register(String serviceName, String serviceAddress) {
        //创建registry 节点(持久)
        String registryPath =  Constants.ZK_REGISTRY_PATH;
        if(!zkClient.exists(registryPath)){
            zkClient.createPersistent(registryPath);
            LOGGER.debug("create registry node: {}", registryPath);
        }
        //创建service 节点(持久)
        String servicePath  = registryPath + "/" +serviceName;
        if(!zkClient.exists(servicePath)){
            zkClient.createPersistent(servicePath);
            LOGGER.debug("create service node: {}", servicePath);

        }
        //创建 address 节点(临时)
        String addressPath =  servicePath +"/address-";
        String addressNode = zkClient.createEphemeralSequential(addressPath,serviceAddress);
        LOGGER.debug("create address node: {}", addressNode);
    }
}
