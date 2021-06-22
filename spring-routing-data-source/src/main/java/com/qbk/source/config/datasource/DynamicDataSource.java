package com.qbk.source.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.DruidDataSourceStatManager;
import com.qbk.source.config.tx.ConnectionFactory;
import com.qbk.source.config.tx.ConnectionProxy;
import com.qbk.source.config.tx.TransactionContext;
import com.qbk.source.domain.DateSource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 自定义数数据路由（也是数据源）
 * AbstractRoutingDataSource 实现了DataSource接口 和 InitializingBean 接口  初始化方法
 * @author DXYT
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     *  数据源集合
     */
    private LinkedHashMap<Object, Object> dynamicTargetDataSources;
    /**
     * 默认数据源
     */
    private Object dynamicDefaultTargetDataSource;

    /**
     * 获取连接
     */
    @Override
    public Connection getConnection() throws SQLException {
       // return determineTargetDataSource().getConnection();
        String xid = TransactionContext.getXID();
        if (StringUtils.isEmpty(xid)) {
            return determineTargetDataSource().getConnection();
        } else {
            String ds = DBContextHolder.peek();
            ConnectionProxy connection = ConnectionFactory.getConnection(ds);
            return connection == null ? getConnectionProxy(ds, determineTargetDataSource().getConnection()) : connection;
        }
    }
    private Connection getConnectionProxy(String ds, Connection connection) {
        ConnectionProxy connectionProxy = new ConnectionProxy(connection, ds);
        ConnectionFactory.putConnection(ds, connectionProxy);
        return connectionProxy;
    }

    /**
     *  获取数据源
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String datasource = DBContextHolder.peek();
        //如果数据源不存在则使用默认数据源
        if (!dynamicTargetDataSources.containsKey(datasource)) {
            log.info("当前数据源：默认数据源【{}】",
                    ((DruidDataSource)dynamicDefaultTargetDataSource).getName());
        } else {
            log.info("当前数据源：切换数据源【{}】",datasource);
        }
        return datasource;
    }

    /**
     * 设置默认数据源
     */
    @Override
    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        this.dynamicDefaultTargetDataSource = defaultTargetDataSource;
    }

    /**
     * 设置数据源集合
     */
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        this.dynamicTargetDataSources = (LinkedHashMap<Object, Object>) targetDataSources;
    }

    /**
     * 创建数据源
     */
    public boolean createDataSource(DateSource dateSource) {
        try {
            Assert.isTrue(testDatasource(dateSource),"数据源连接无效");
            //创建数据源
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setName(dateSource.getKey());
            druidDataSource.setDriverClassName(dateSource.getDriveClass());
            druidDataSource.setUrl(dateSource.getUrl());
            druidDataSource.setUsername(dateSource.getUsername());
            druidDataSource.setPassword(dateSource.getPassword());
            druidDataSource.setMaxWait(dateSource.getMaxWait());
            druidDataSource.setFilters("stat");
            Map<Object, Object> map = this.dynamicTargetDataSources;
            //避免重复添加
            if(map.containsKey(dateSource.getKey())){
               Assert.isTrue( delDatasources(dateSource.getKey()),"删除数据源失败");
            }
            //数据源初始化
            druidDataSource.init();
            // 加入map
            map.put(dateSource.getKey(), druidDataSource);
            // 将map赋值给父类的TargetDataSources
            setTargetDataSources(map);
            // 将TargetDataSources中的连接信息放入resolvedDataSources管理
            super.afterPropertiesSet();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除数据源
     */
    public boolean delDatasources(String key) {
        Map<Object, Object> targetDataSource = this.dynamicTargetDataSources;
        if (targetDataSource.containsKey(key)) {
            Set<DruidDataSource> druidDataSourceInstances = DruidDataSourceStatManager.getDruidDataSourceInstances();
            for (DruidDataSource druidDataSource : druidDataSourceInstances) {
                if (key.equals(druidDataSource.getName())) {
                    System.out.println(druidDataSource);
                    targetDataSource.remove(key);
                    DruidDataSourceStatManager.removeDataSource(druidDataSource);
                    // 将map赋值给父类的TargetDataSources
                    setTargetDataSources(targetDataSource);
                    // 将TargetDataSources中的连接信息放入resolvedDataSources管理
                    super.afterPropertiesSet();
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * 测试数据源连接是否有效
     */
    public boolean testDatasource(DateSource dateSource) {
        try {
            Class.forName(dateSource.getDriveClass());
            DriverManager.getConnection(dateSource.getUrl(),
                    dateSource.getUsername(), dateSource.getPassword());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}