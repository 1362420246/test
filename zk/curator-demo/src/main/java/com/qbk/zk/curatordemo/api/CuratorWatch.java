package com.qbk.zk.curatordemo.api;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;

/**
 * watch监听
 */
public class CuratorWatch {
    public static void main(String[] args) {
//        curatorWatchOnce();
        curatorWatchPersistent();
    }
    /**
     * 一次性监听
     */
    private static void curatorWatchOnce() {
        String url = "101.43.76.164:2181";
        //认证
        String scheme = "digest";
        byte[] auth = "qbk:123456".getBytes();
        //Curator
        CuratorFramework curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectionTimeoutMs(20000)
                .connectString(url)
                .authorization(scheme, auth)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        curatorFramework.start();
        try {
            // 创建节点 curator-watch-once
            String znode = curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/curatorwatch-once", "".getBytes());
            System.out.println("节点创建成功: " + znode);
            // 给节点 curator-watch-once 添加一次性watch
            curatorFramework.getData().usingWatcher(new CuratorWatcher() {
                @Override
                public void process(WatchedEvent event) throws Exception {
                    System.out.println("节点发生变化: " + event);
                }
            }).forPath(znode);
            System.out.println("给节点curator-watch-once 添加watch成功.");
            // 让当前进程不结束
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 永久监听
     */
    private static void curatorWatchPersistent() {
        String url = "101.43.76.164:2181";
        //认证
        String scheme = "digest";
        byte[] auth = "qbk:123456".getBytes();
        //Curator
        CuratorFramework curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectionTimeoutMs(20000)
                .connectString(url)
                .authorization(scheme, auth)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        curatorFramework.start();
        try {
            // 创建节点 curator-watch-persistent
            String znode = curatorFramework.create().forPath("/curator-watchpersistent", "".getBytes());
            System.out.println("节点创建成功: " + znode);
            // 永久的监听
            CuratorCache curatorCache = CuratorCache.build(curatorFramework,znode, CuratorCache.Options.SINGLE_NODE_CACHE);
            CuratorCacheListener listener =
                    CuratorCacheListener.builder().forAll(new CuratorCacheListener() {
                        @Override
                        public void event(Type type, ChildData oldData, ChildData data) {
                            // 等同于Watch#process回调
                            System.out.println("节点 "+data.getPath()+" 发生改变, 事件类型 为: " + type);
                        }
                    }).build();
            curatorCache.listenable().addListener(listener);
            curatorCache.start();
            System.out.println("给节点curator-watch-persistent 添加watch成功.");
            // 让当前进程不结束
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
