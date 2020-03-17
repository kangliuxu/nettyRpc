package com.xkl.rpc.client;

import com.xkl.rpc.common.bean.RpcRequest;
import com.xkl.rpc.common.bean.RpcResponse;
import com.xkl.rpc.registry.ServiceDiscovery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @author xkl
 * @date 2020/3/16
 * @description  RPC 代理（用于创建 RPC 服务代理）
 **/
public class RpcProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcProxy.class);
    private String serviceAddress;
    private ServiceDiscovery serviceDiscovery;

    public RpcProxy(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public RpcProxy(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public <T> T create(final Class<?> interfaceClass) {
        return create(interfaceClass, "");
    }

    public <T> T create(final Class<?> interfaceClass, final String serviceVersion) {
        //创建动态代理对象
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // 创建 RPC 请求对象并设置请求属性
                        RpcRequest request = new RpcRequest();
                        request.setRequestId(UUID.randomUUID().toString());
                        request.setInterfaceName(method.getDeclaringClass().getName());
                        request.setServiceVersion(serviceVersion);
                        request.setMethodName(method.getName());
                        request.setParameterTypes(method.getParameterTypes());
                        request.setParameters(args);

                        //获取RPC地址
                        if(serviceDiscovery!=null){
                            String serviceName = interfaceClass.getName();
                            if(StringUtils.isNoneEmpty(serviceVersion)){
                                serviceName += "-"+serviceVersion;
                            }
                            serviceAddress = serviceDiscovery.discover(serviceName);
                            LOGGER.debug("discover service: {} => {}", serviceName, serviceAddress);
                        }

                        if (StringUtils.isEmpty(serviceAddress)) {
                            throw new RuntimeException("server address is empty");
                        }

                        //解析主机名和端口号
                        String[] array = StringUtils.split(serviceAddress,":");
                        String host = array[0];
                        Integer port = Integer.parseInt(array[1]);

                        //创建rpc客户端对象并发送RPC请求
                        RpcClient client = new RpcClient(port, host);
                        long time = System.currentTimeMillis();
                        RpcResponse response = client.send(request);
                        LOGGER.debug("time:{}ms",System.currentTimeMillis()-time);

                        if(response == null){
                            throw new RuntimeException("response is null");
                        }

                        // 返回 RPC 响应结果
                        if (response.hasException()) {
                            throw response.getException();
                        } else {
                            return response.getResult();
                        }


                    }
                });
    }
}
