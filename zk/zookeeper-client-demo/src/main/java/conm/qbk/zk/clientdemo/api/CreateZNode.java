package conm.qbk.zk.clientdemo.api;

import conm.qbk.zk.clientdemo.util.ZkConnUtil;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * 创建节点
 */
public class CreateZNode {
    private ZooKeeper zooKeeper;
    public CreateZNode(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }
    /**
     * 同步创建节点
     */
    public void createZNodeWithSync() throws Exception {
        /*
         * @param path 节点的路径
         * @param data 节点的初始数据
         * @param acl 节点的acl
         * @param createMode 指定要创建的节点是否为临时节点和顺序
         */
        String znode = zooKeeper.create(
                "/zookeeper-api-sync",
                "111".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        System.out.println("创建节点成功: "+znode);
    }
    /**
     * 异步创建节点
     */
    public void createZNodeWithAsync(){
        zooKeeper.create("/zookeeper-apiasync","111".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT,new
                AsyncCallback.StringCallback(){
                    @Override
                    public void processResult(int rc, String path, Object ctx, String
                            name) {
                        System.out.println("rc: "+rc);
                        System.out.println("path: "+path);
                        System.out.println("ctx: "+ctx);
                        System.out.println("name: "+name);
                    }
                },"create-asyn");
    }
    public static void main(String[] args) throws Exception {
        //获取链接
        final ZooKeeper zkConn = ZkConnUtil.getZkConn("101.43.76.164:2181");
        //设置权限
        StringBuffer auth = new StringBuffer("qbk").append(":").append("123456");
        zkConn.addAuthInfo("digest", auth.toString().getBytes());
        //创建节点
        CreateZNode createZNode = new CreateZNode(zkConn);
        createZNode.createZNodeWithSync();
        createZNode.createZNodeWithAsync();
        System.in.read();
    }
}
