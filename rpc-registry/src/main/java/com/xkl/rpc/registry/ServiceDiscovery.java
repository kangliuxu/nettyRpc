package com.xkl.rpc.registry;

/**
 * @author xkl
 * @date 2020/3/16
 * @description 服务注册发现
 **/
public interface ServiceDiscovery {
    /**
     * 根据服务名称查找服务地址
     * @param serviceName
     * @return 服务地址
     */
    String discover(String serviceName);

}
