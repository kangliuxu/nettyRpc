package server.service.impl;

import server.annontation.RpcService;
import server.service.HelloService;

/**
 * @author xkl
 * @date 2020/3/15
 * @description
 **/
@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {
    public String hello(String name) {
        return "Hello" + name;
    }
}
