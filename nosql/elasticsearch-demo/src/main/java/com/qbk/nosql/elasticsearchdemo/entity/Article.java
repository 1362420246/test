package com.qbk.nosql.elasticsearchdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 指定 index 和 type 名称
 *
 * 单index多type的弊端:
 *  1、elasticsearch中同一 Index 下，同名 Field 类型必须相同，即使不同的 Type
 *  2、同一 Index下 ，不同结构的type会导致稀疏存储
 *  3、Score 评分机制是 index-wide 的，不同的type之间评分也会造成干扰
 *  4、多type导致index臃肿，某个type如果涉及到大量字段变更或元数据变更，会影响整个index的正常使用
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * shards 分片 即使是一个节点中的数据也会通过hash算法，分成多个片存放，默认是5片
 * replicas 副本
 * index	相当于rdbms的database, 对于用户来说是一个逻辑数据库，虽然物理上会被分多个shard存放，也可能存放在多个node中。
 * type	    类似于rdbms的table，但是与其说像table，其实更像面向对象中的class , 同一Json的格式的数据集合。
 * document	类似于rdbms的 row、面向对象里的object
 * field	相当于字段、属性
 */
@Document(indexName = "qbk2",type = "article",shards = 5,replicas = 1)
public class Article {

    @Id
    @Field(index=false ,type = FieldType.Integer)
    private Integer id;

    /**
     * index 是否設置分詞
     * analyzer: 存结时使用的分词器
     * earchanatyze:搜索时使用的分词器
     * store:是否存储 默认是false
     * type:数据类型
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;

    /**
     *   ik分词器提供的分词规则：
     *   ik_max_word：会将文本做最细粒度的拆分，比如会将“中华人民共和国人民 大会堂”拆分为“中华人民共和国、中华人民、中华、华人、人民共和国、人民、共 和国、大会堂、大会、会堂等词语。
     *   ik_smart ：会做最粗粒度的拆分，比如会将“中华人民共和国人民大会堂”拆分为中 华人民共和国、人民大会堂
     *   两种分词器使用的最佳实践是：索引时用ik_max_word，在搜索时用ik_smart。即：索引时最大化的将文章内容分词，搜索时更精确的搜索到想要的结果。
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String context;

    @Field(index=true ,type = FieldType.Integer)
    private Integer hits;

}
