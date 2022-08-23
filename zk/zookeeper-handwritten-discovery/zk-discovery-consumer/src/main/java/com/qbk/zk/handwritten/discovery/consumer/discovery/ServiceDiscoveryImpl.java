package com.qbk.zk.handwritten.discovery.consumer.discovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

/**
 * 服务发现
 */
public class ServiceDiscoveryImpl implements ServiceDiscovery {

    private final CuratorFramework curatorFramework;
    private final String basePath = "/qbk-registry";
    private final String scheme = "digest";
    private final byte[] auth = "root:123456".getBytes();

    /**
     * 构造器中建立 zk链接
     */
    public ServiceDiscoveryImpl(String zkServer) {
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
     * 服务发现
     */
    @Override
    public List<String> discovery(String serviceName) {
        String serviceNamePath = basePath + "/" + serviceName;
        try {
            if (this.curatorFramework.checkExists().forPath(serviceNamePath) != null) {
                return this.curatorFramework.getChildren().forPath(serviceNamePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 服务监听
     */
    @Override
    public void registerWatch(String serviceNamePath) {
        // 永久的监听
        CuratorCache curatorCache = CuratorCache.build(curatorFramework, serviceNamePath);
        CuratorCacheListener listener = CuratorCacheListener.builder().forPathChildrenCache(serviceNamePath, curatorFramework, new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                // 拉模式
                System.out.println("最新的urls为: " + curatorFramework.getChildren().forPath(serviceNamePath));
            }
        }).build();
        curatorCache.listenable().addListener(listener);
        curatorCache.start();
    }
}