package com.xkl.rpc.registry.zookeeper;


import com.xkl.rpc.common.util.CollectionUtil;
import com.xkl.rpc.registry.ServiceDiscovery;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author xkl
 * @date 2020/3/16
 * @description
 **/
public class ZooKeeperServiceDiscovery implements ServiceDiscovery {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZooKeeperServiceDiscovery.class);
    private String zkAddress;

    public ZooKeeperServiceDiscovery(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    @Override
    public String discover(String serviceName) {
       //创建zk 客户端
        ZkClient zkClient = new ZkClient(zkAddress, Constants.ZK_SESSION_TIMEOUT,Constants.ZK_CONNECTION_TIMEOUT);
        LOGGER.debug("connect zookeeper");
        try {
            //获取service节点
            String servicePath = Constants.ZK_REGISTRY_PATH +"/" +serviceName;
            if(!zkClient.exists(servicePath)){
                throw new RuntimeException(String.format("can not find any service node on path:%s",servicePath));
            }
            List<String> addressList = zkClient.getChildren(servicePath);
            if(CollectionUtil.isEmpty(addressList)){
                throw new RuntimeException(String.format("can not find any address node on path: %s", servicePath));
            }

            //获取address 节点
            String address;
            int size = addressList.size();
            if(size==1){
                address = addressList.get(0);
                LOGGER.debug("get only address node: {}", address);
            }else{
                //存在多个地址,随机获取一个
                address = addressList.get(ThreadLocalRandom.current().nextInt(size));
                LOGGER.debug("get random address node: {}", address);
            }

            //获取address 节点的值
            String addressPath = servicePath+ "/" +address;
            return zkClient.readData(addressPath);
        }finally {
            zkClient.close();
        }
    }
}
