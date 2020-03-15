package server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author xkl
 * @date 2020/3/15
 * @description 服务器启动器
 **/
public class RpcBootstrap {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring.xml");
    }
}
