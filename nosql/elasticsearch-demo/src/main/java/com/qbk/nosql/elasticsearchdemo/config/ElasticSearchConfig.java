package com.qbk.nosql.elasticsearchdemo.config;

import org.apache.http.HttpHost;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.client.config.RequestConfig.Builder;
import org.elasticsearch.client.RestClientBuilder.RequestConfigCallback;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 集成 restHighLevelClient 客户端
 *  restHighLevelClient 采用rest方式
 *
 * 相较于TransportClient，RestHighLevelClient最优之处在于可以兼容当前版本及以后版本的ES Cluster，
 *  而TransportClient却必须和ES Cluster版本一致否则报错。
 *  而且RestHighLevelClient和TransportClient API很类似，包括参数和返回数据，比较容易迁移。
 *  TransportClient将在7中deprecated，8中删除。
 *
 *  目前spring-data-elasticsearch底层采用es官方TransportClient，
 *  而es官方计划放弃TransportClient，工具以es官方推荐的RestHighLevelClient进行封装
 */
@Configuration
public class ElasticSearchConfig {

    /**
     * 使用 restHighLevelClient 客户端
     * 连接的端口是 9200
     */
    @Bean(name = "restHighLevelClient")
    public RestHighLevelClient restHighLevelClient() {

        HttpHost httpHost = new HttpHost("20.1.120.29", 9200);

        RestClientBuilder builder = RestClient.builder(new HttpHost[]{httpHost});
        //失败重试超时时间
        //builder.setMaxRetryTimeoutMillis(5 * 60 * 1000);

        // 异步httpclient连接延时配置
        builder.setRequestConfigCallback(new RequestConfigCallback() {
            @Override
            public Builder customizeRequestConfig(Builder requestConfigBuilder) {
                //TODO 超时 设置 在 版本 7.x 才生效
                requestConfigBuilder.setConnectTimeout(30000);// 连接超时时间
                requestConfigBuilder.setSocketTimeout(300 * 1000);//更改客户端的超时限制默认30秒现在改为5分钟
                requestConfigBuilder.setConnectionRequestTimeout(30000);// 获取连接的超时时间
                return requestConfigBuilder;
            }
        });

        // 异步httpclient连接数配置
        builder.setHttpClientConfigCallback(new HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                httpClientBuilder.setMaxConnTotal(100);// 最大连接数
                httpClientBuilder.setMaxConnPerRoute(100);// 最大路由连接数
                return httpClientBuilder;
            }
        });

        return new RestHighLevelClient(builder);
    }
}

