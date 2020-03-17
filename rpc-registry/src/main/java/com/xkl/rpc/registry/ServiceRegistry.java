package com.xkl.rpc.registry;

/**
 * @author xkl
 * @date 2020/3/16
 * @description 服务注册接口
 **/
public interface ServiceRegistry {
    
    /**
     *@描述  注册服务名称与服务地址
     *@参数  
     *@返回值  
     *@创建人  xkl
     *@创建时间  2020/3/16
     *@修改人和其它信息
     */
    void register(String serviceName,String serviceAddress);
}
