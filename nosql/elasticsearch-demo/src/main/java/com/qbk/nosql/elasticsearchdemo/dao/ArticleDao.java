package com.qbk.nosql.elasticsearchdemo.dao;

import com.qbk.nosql.elasticsearchdemo.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 继承 ElasticsearchRepository 接口
 * 泛型：实体类型　主键类型
 *
 * 自定义方法按照字段名写法
 */
public interface ArticleDao extends ElasticsearchRepository<Article,Integer> {

    /**
     * 根据标题 搜索
     */
    List<Article> findByTitle(String context);

    /**
     * 根据标题 根据标题或者 内容 搜索
     */
    List<Article> findByTitleOrContext(String title, String context);

    /**
     * 根据标题 根据标题或者 内容 搜索  带分页
     */
    List<Article> findByTitleOrContext(String title, String context, Pageable pageable);
}
