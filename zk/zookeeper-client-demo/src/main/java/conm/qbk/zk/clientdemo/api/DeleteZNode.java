package conm.qbk.zk.clientdemo.api;

import conm.qbk.zk.clientdemo.util.ZkConnUtil;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.ZooKeeper;

/**
 * 删除节点
 */
public class DeleteZNode {
    private ZooKeeper zooKeeper;
    public DeleteZNode(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }
    /**
     * 同步删除节点
     */
    public void deleteZNodeSync() throws Exception {
        zooKeeper.delete("/zookeeper-api-sync",-1);
    }
    /**
     * 异步删除节点
     */
    public void deleteZNodeAsync(){
        zooKeeper.delete("/zookeeper-apiasync", -1, new
                AsyncCallback.VoidCallback() {
                    @Override
                    public void processResult(int rc, String path, Object ctx) {
                        System.out.println("rc: "+rc);
                        System.out.println("path: "+path);
                        System.out.println("ctx: "+ctx);
                    }
                },"delete-znode-async");
    }
    public static void main(String[] args) throws Exception {
        //获取链接
        final ZooKeeper zkConn = ZkConnUtil.getZkConn("101.43.76.164:2181");
        //设置权限
        StringBuffer auth = new StringBuffer("qbk").append(":").append("123456");
        zkConn.addAuthInfo("digest", auth.toString().getBytes());
        //删除节点
        DeleteZNode deleteZNode = new DeleteZNode(zkConn);
        deleteZNode.deleteZNodeAsync();
        deleteZNode.deleteZNodeSync();
        System.in.read();
    }
}
