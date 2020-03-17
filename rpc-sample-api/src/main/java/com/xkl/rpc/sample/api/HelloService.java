package com.xkl.rpc.sample.api;

/**
 * @author xkl
 * @date 2020/3/16
 * @description
 **/
public interface HelloService {
    String hello(String name);

    String hello(Person person);
}
