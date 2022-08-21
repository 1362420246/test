package conm.qbk.zk.clientdemo.api;

import conm.qbk.zk.clientdemo.util.ZkConnUtil;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 获取节点
 */
public class GetZNodeData {
    private ZooKeeper zooKeeper;
    public GetZNodeData(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }
    /**
     * 同步获取数据
     */
    public void getDataSync() {
        //获取stat
        Stat stat = new Stat();
        try {
            byte[] data = zooKeeper.getData("/zookeeper-api-sync", new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    // 一旦节点发生变化，则会回调该方法
                    System.out.println("event: " + event);
                }
            }, stat);
            String s = new String(data);
            System.out.println("data: " + s);
            System.out.println("stat: " + stat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 异步获取数据
     */
    public void getDataAsync() {
        zooKeeper.getData("/zookeeper-apiasync", false, new
                AsyncCallback.DataCallback() {
                    @Override
                    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                        System.out.println("rc: " + rc);
                        System.out.println("path: " + path);
                        System.out.println("ctx: " + ctx);
                        System.out.println("data: " + new String(data));
                        System.out.println("stat: " + stat);
                    }
                }, "get-data-async");
    }

    public static void main(String[] args) throws Exception {
        //获取链接
        final ZooKeeper zkConn = ZkConnUtil.getZkConn("101.43.76.164:2181");
        //设置权限
        StringBuffer auth = new StringBuffer("qbk").append(":").append("123456");
        zkConn.addAuthInfo("digest", auth.toString().getBytes());
        //获取节点
        GetZNodeData getZNodeData = new GetZNodeData(zkConn);
        getZNodeData.getDataSync();
        getZNodeData.getDataAsync();
        System.in.read();
    }
}
