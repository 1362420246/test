package conm.qbk.zk.clientdemo.util;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.client.ZKClientConfig;

import java.util.concurrent.CountDownLatch;

/**
 * zk链接
 */
public class ZkConnUtil {
    private static ZooKeeper zookeeper;
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);
    /**
     * 获得zkConn
     */
    public static ZooKeeper getZkConn(String zkServer) throws Exception {
        zookeeper = new ZooKeeper(zkServer, 30000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                Event.KeeperState state = event.getState();
                if (Event.KeeperState.SyncConnected == state) {
                    System.out.println("连接zkServer成功.");
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
        return zookeeper;
    }
    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = getZkConn("101.43.76.164:2181");
        System.out.println(zooKeeper.getState());
    }
}
