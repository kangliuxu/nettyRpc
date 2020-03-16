package com.xkl.rpc.registry.zookeeper;

/**
 * @author xkl
 * @date 2020/3/15
 * @description
 **/
public interface Constants {
    int ZK_SESSION_TIMEOUT = 5000;
    String ZK_REGISTRY_PATH = "/registry";
    String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";
    int ZK_CONNECTION_TIMEOUT = 1000;
}
