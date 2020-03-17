package com.xkl.rpc.sample.server;

import com.xkl.rpc.sample.api.HelloService;
import com.xkl.rpc.sample.api.Person;
import com.xkl.rpc.server.RpcService;

/**
 * @author xkl
 * @date 2020/3/16
 * @description
 **/
@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "hello"+ name;
    }

    @Override
    public String hello(Person person) {
        return null;
    }
}
