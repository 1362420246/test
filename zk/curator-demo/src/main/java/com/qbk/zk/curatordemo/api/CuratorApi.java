package com.qbk.zk.curatordemo.api;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * 增删改查节点
 */
public class CuratorApi {
    public static void main(String[] args) {
        String url = "101.43.76.164:2181";
        //acl
        ACLProvider aclProvider = new ACLProvider() {
            private List<ACL> acl ;
            @Override
            public List<ACL> getDefaultAcl() {
                if(acl ==null){
                    ArrayList<ACL> acl = ZooDefs.Ids.CREATOR_ALL_ACL;
                    acl.clear();
                    acl.add(new ACL(ZooDefs.Perms.ALL, new Id("auth", "qbk:123456") ));
                    this.acl = acl;
                }
                return acl;
            }
            @Override
            public List<ACL> getAclForPath(String path) {
                return acl;
            }
        };
        //认证
        String scheme = "digest";
        byte[] auth = "qbk:123456".getBytes();
        //Curator
        CuratorFramework curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectionTimeoutMs(20000)
                .connectString(url)
                .aclProvider(aclProvider)
                .authorization(scheme, auth)
                // 设置客户端的重试策略，每隔10秒中重试一次，最多3次
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
            curatorFramework.start();
        try {
            // 创建节点
            String znode = curatorFramework
                    .create()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath("/curator-api", "666".getBytes());
            System.out.println("创建节点成功: " + znode);
            // 查询节点数据
            byte[] bytes = curatorFramework.getData().forPath(znode);
            System.out.println("节点curator-api 数据查询成功: " + new String(bytes));
            // 修改节点数据
            curatorFramework.setData().forPath(znode, "888".getBytes());
            System.out.println("节点curator-api 数据修改成功.");
            // 删除节点
            curatorFramework.delete().forPath(znode);
            System.out.println("节点curator-api 已被删除.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}