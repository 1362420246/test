package conm.qbk.zk.clientdemo.api;

import conm.qbk.zk.clientdemo.util.ZkConnUtil;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 修改节点
 */
public class UpdateZNodeData {
    private ZooKeeper zooKeeper;
    public UpdateZNodeData(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }
    /**
     * 同步修改节点数据
     */
    public void setDataSync() throws Exception {
        // 版本号为-1，表示可以直接修改，不用关心版本号
        zooKeeper.setData("/zookeeper-api-sync", "222".getBytes(), -1);
    }
    /**
     * 异步修改节点数据
     */
    public void setDataAsync() {
        zooKeeper.setData("/zookeeper-apiasync", "222".getBytes(), -1, new
                AsyncCallback.StatCallback() {
                    @Override
                    public void processResult(int rc, String path, Object ctx, Stat
                            stat) {
                        System.out.println("rc: " + rc);
                        System.out.println("path: " + path);
                        System.out.println("actx: " + ctx);
                        System.out.println("stat: " + stat);
                    }
                }, "set-data-async");
    }
    /**
     * 根据版本修改同步节点数据
     */
    public void setDataSyncWithVersion() throws Exception {
        Stat stat = new Stat();
        zooKeeper.getData("/zookeeper-api-sync", false, stat);
        zooKeeper.setData("/zookeeper-api-sync", "555".getBytes(), stat.getVersion());
    }

    public static void main(String[] args) throws Exception {
        //获取链接
        final ZooKeeper zkConn = ZkConnUtil.getZkConn("101.43.76.164:2181");
        //设置权限
        StringBuffer auth = new StringBuffer("qbk").append(":").append("123456");
        zkConn.addAuthInfo("digest", auth.toString().getBytes());
        //修改节点
        UpdateZNodeData updateZNodeData = new UpdateZNodeData(zkConn);
        updateZNodeData.setDataSync();
        updateZNodeData.setDataAsync();
        updateZNodeData.setDataSyncWithVersion();
        System.in.read();
    }
}
