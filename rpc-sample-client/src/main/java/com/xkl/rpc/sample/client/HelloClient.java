package com.xkl.rpc.sample.client;

import com.xkl.rpc.client.RpcProxy;
import com.xkl.rpc.sample.api.HelloService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author xkl
 * @date 2020/3/17
 * @description
 **/
public class HelloClient {

    public static void main(String[] args) {
        //测试
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        RpcProxy rpcProxy = context.getBean(RpcProxy.class);
        //远程动态代理执行
        HelloService service = rpcProxy.create(HelloService.class);
        String result = service.hello("xukangliu");
        System.out.println(result);

      /*  HelloService service2 = rpcProxy.create(HelloService.class,"sample.hello2");
        String result2 = service2.hello("世界");
        System.out.println(result2);*/

    }


}
