package com.qbk.zk.handwritten.discovery.provider.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * zk 服务注册
 */
public class ZookeeperServiceRegistry implements ServiceRegistry {

    private CuratorFramework curatorFramework;
    private final String ip;
    private final String port;
    private final String serviceName;
    private final String basePath = "/qbk-registry";
    private final String scheme = "digest";
    private final byte[] auth = "qbk:123456".getBytes();

    /**
     * 构造器中建立 zk链接
     */
    public ZookeeperServiceRegistry(String serviceName, String ip, String port, String zkServer) {
        this.serviceName = serviceName;
        this.ip = ip;
        this.port = port;
        this.curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectionTimeoutMs(20000)
                .connectString(zkServer)
                .authorization(scheme, auth)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        curatorFramework.start();
    }

    /**
     * 注册服务
     */
    @Override
    public void register() {
        // 服务名称
        String serviceNamePath = basePath + "/" + serviceName;
        try {
            //检查父节点
            if (curatorFramework.checkExists().forPath(serviceNamePath) == null) {
                // 创建持久化的节点，作为服务名称
                this.curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(serviceNamePath);
            }
            //创建临时子节点
            String urlNode = curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(serviceNamePath + "/" + ip + ":" + port);
            System.out.println("服务:[ " + urlNode + " ]注册成功...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}