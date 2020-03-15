package server.registry;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.common.Constants;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author xkl
 * @date 2020/3/15
 * @description 实现服务注册功能
 **/
public class ServiceRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);
    private CountDownLatch latch = new CountDownLatch(1);
    private String registryAddress;
    public ServiceRegistry(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public void register(String data){
        if(data !=null){
            ZooKeeper zk = connectServer();
            if(zk != null){
                createNode(zk,data);
            }
        }
    }

    private void createNode(ZooKeeper zk, String data) {
        try{
            byte[] bytes = data.getBytes();
            String path = zk.create(Constants.ZK_DATA_PATH,bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            LOGGER.debug("create zookeeper node ({} => {})", path, data);
        } catch (InterruptedException | KeeperException e) {
            LOGGER.error("", e);
        }
    }

    private ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(registryAddress, Constants.ZK_SESSION_TIMEOUT,(event) ->{
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }
            });
            latch.await();
        }catch (IOException | InterruptedException e){
            LOGGER.error("", e);
        }
        return zk;
    }
}
