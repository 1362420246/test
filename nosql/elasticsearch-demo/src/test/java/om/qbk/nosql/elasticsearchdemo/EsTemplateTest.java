package om.qbk.nosql.elasticsearchdemo;

import com.qbk.nosql.elasticsearchdemo.ElasticSearchDemoApplication;
import com.qbk.nosql.elasticsearchdemo.entity.Item;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * ElasticsearchTemplate　测试
 *
 * term是代表完全匹配，也就是精确查询，搜索前不会再对搜索词进行分词，所以我们的搜索词必须是文档分词集合中的一个
 *
 * TermsBuilder:构造聚合函数
 *
 * AggregationBuilders:创建聚合函数工具类
 *
 * BoolQueryBuilder:拼装连接(查询)条件
 *
 * QueryBuilders:简单的静态工厂”导入静态”使用。主要作用是查询条件(关系),如区间\精确\多值等条件
 *
 * NativeSearchQueryBuilder:将连接条件和聚合函数等组合
 *
 * SearchQuery:生成查询
 *
 * elasticsearchTemplate.query:进行查询
 *
 * Aggregations:Represents a set of computed addAggregation.代表一组添加聚合函数统计后的数据
 *
 * Bucket:满足某个条件(聚合)的文档集合
 */
@SpringBootTest(classes = ElasticSearchDemoApplication.class)
public class EsTemplateTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 创建索引
     */
    @Test
    public void createIndex() {
        // 创建索引，会根据Item类的@Document注解信息来创建
        boolean result = elasticsearchTemplate.createIndex(Item.class);
        System.out.println(result);

        // 配置映射，会根据Item类中的@Id、@Field等字段来自动完成映射
        result = elasticsearchTemplate.putMapping(Item.class);
        System.out.println(result);
    }

    /**
     * 查询 分词器 分词结果
     */
    @Test
    public void getIkAnalyzeSearchTerms() {
        // 调用 IK 分词分词
        AnalyzeRequestBuilder ikRequest = new AnalyzeRequestBuilder(
                elasticsearchTemplate.getClient(),
                AnalyzeAction.INSTANCE,
                "qbk",
                "中华人民共和国人民"
        );
        /*
            ik分词器提供的分词规则：
            ik_max_word：会将文本做最细粒度的拆分，比如会将“中华人民共和国人民 大会堂”拆分为“中华人民共和国、中华人民、中华、华人、人民共和国、人民、共 和国、大会堂、大会、会堂等词语。
            ik_smart ：会做最粗粒度的拆分，比如会将“中华人民共和国人民大会堂”拆分为中 华人民共和国、人民大会堂
            两种分词器使用的最佳实践是：索引时用ik_max_word，在搜索时用ik_smart。即：索引时最大化的将文章内容分词，搜索时更精确的搜索到想要的结果。
         */
        ikRequest.setTokenizer("ik_max_word");
        List<AnalyzeResponse.AnalyzeToken> ikTokenList = ikRequest.execute().actionGet().getTokens();
        ikTokenList.forEach(ikToken -> System.out.println(ikToken.getTerm()));
    }

    /**
     * 删除索引
     */
    @Test
    public void deleteIndex() {
        //删除索引
        boolean result = elasticsearchTemplate.deleteIndex(Item.class);
        System.out.println(result);
    }

    /**
     * 插入 或 修改
     */
    @Test
    public void insertOrUpdate() {

        Item build = Item.builder().id(12L).title("中华人民共和国人民2").category("s").brand("ss").price(2.2).images("xxx").build();

        IndexQuery indexQuery = new IndexQueryBuilder()
                .withIndexName("qbk")
                .withType("item")
                .withId("5").withObject(build)
                .build();

        String result = elasticsearchTemplate.index(indexQuery);

        System.out.println(result);
    }


    /**
     * 删除
     */
    @Test
    public void delete() {
        String result = elasticsearchTemplate.delete(Item.class, "1");
        System.out.println(result);
    }

    /**
     * 批量插入
     */
    @Test
    public void addAll() {
        List<IndexQuery> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            Item build = Item.builder().id(200L + i).title("中华人民共和国人民" + i).category("测试" + (10 - i)).brand("测试").price(2.2).images("测试").build();

            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withIndexName("qbk")
                    .withType("item")
                    .withId(i + "").withObject(build)
                    .build();

            list.add(indexQuery);
        }
        elasticsearchTemplate.bulkIndex(list);
    }

    /**
     * query 查询所有
     */
    @Test
    public void search() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        matchAllQuery()
                ).build();

        List<Item> items = elasticsearchTemplate.queryForList(searchQuery, Item.class);
        items.forEach(System.out::println);
    }

    /**
     * query 匹配对应字段  搜索
     */
    @Test
    public void searchQuery() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        matchQuery("title", "1")
                )
                .build();

        List<Item> items = elasticsearchTemplate.queryForList(searchQuery, Item.class);
        items.forEach(System.out::println);
    }

    /**
     * query 匹配对应字段  过滤 搜索
     */
    @Test
    public void searchFilterQuery() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        matchQuery("title", "人民")
                )
                .withFilter(
                        rangeQuery("id")
                                .gt(205) //大于
                )
                .build();

        List<Item> items = elasticsearchTemplate.queryForList(searchQuery, Item.class);
        items.forEach(System.out::println);
    }


    /**
     * query 匹配对应字段  分页 搜索
     */
    @Test
    public void pageRequest() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("title", "人民"))
                .withPageable(PageRequest.of(1, 2)).build();   //分页

        List<Item> items = elasticsearchTemplate.queryForList(searchQuery, Item.class);
        items.forEach(System.out::println);
    }

    /**
     * query 匹配对应字段  排序 搜索
     */
    @Test
    public void order() {
        SortBuilder sortBuilder = SortBuilders.fieldSort("id")   //排序字段
                .order(SortOrder.DESC);//排序方式

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("title", "人民"))
                .withSort(sortBuilder)
                .build();

        List<Item> items = elasticsearchTemplate.queryForList(searchQuery, Item.class);
        items.forEach(System.out::println);
    }

    /**
     * 设置显示部分字段 搜索
     */
    @Test
    public void filter() {
        String[] include = {"category", "title"};

        //两个参数分别是要显示的和不显示的
        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(include, null);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withSourceFilter(fetchSourceFilter)
                .build();

        List<Item> items = elasticsearchTemplate.queryForList(searchQuery, Item.class);
        items.forEach(System.out::println);
    }

    /**
     * bool 搜索
     * <p>
     * Bool查询现在包括四种子句:must，filter,should,must_not
     */
    @Test
    public void boolQue() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFilter(
                        boolQuery()
                                //结构类似.bool -> must ->match
                                .must(
                                        matchQuery("title", "1")
                                )
                )
                .build();

        List<Item> items = elasticsearchTemplate.queryForList(searchQuery, Item.class);
        items.forEach(System.out::println);
    }

    /**
     * bool not 搜索
     */
    @Test
    public void boolNotQue() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFilter(
                        boolQuery()
                                //结构类似.bool -> must ->match
                                .mustNot(
                                        matchQuery("title", "1")
                                )
                )
                .build();

        List<Item> items = elasticsearchTemplate.queryForList(searchQuery, Item.class);
        items.forEach(System.out::println);
    }

    /**
     * bool filter 搜索
     * <p>
     * <p>
     * filter比query快
     * <p>
     * query的时候，会先比较查询条件，然后计算分值，最后返回文档结果；
     * 而filter则是先判断是否满足查询条件，如果不满足，会缓存查询过程（记录该文档不满足结果）；
     * 满足的话，就直接缓存结果
     * 综上所述，filter快在两个方面：
     * 1.对结果进行缓存
     * 2.避免计算分值
     */
    @Test
    public void boolFilterQue() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFilter(
                        boolQuery()
                                //结构类似.bool -> must ->match
                                .must(
                                        matchQuery("title", "中华人民共和国人民")
                                )
                                .filter(
                                        rangeQuery("id")
                                                .gt(205) //大于
                                )

                )
                .build();

        List<Item> items = elasticsearchTemplate.queryForList(searchQuery, Item.class);
        items.forEach(System.out::println);
    }

    /**
     * aggregations 聚合查询
     *
     *  等于:
     * select category, count(*) as category_count
     * from docs
     * group by category;
     */
    @Test
    public void aggregationsGroupBy() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .addAggregation(
                        AggregationBuilders.terms("category_count")
                                .field("category")
                )
                .build();

        //拿到 聚合 结果
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery,
                new ResultsExtractor<Aggregations>() {
                    @Override
                    public Aggregations extract(SearchResponse response) {
                        return response.getAggregations();
                    }
                });

        //类似于 map .get
        StringTerms modelTerms = (StringTerms) aggregations.asMap().get("category_count");

        for (Terms.Bucket actionTypeBucket : modelTerms.getBuckets()) {
            // actionTypeBucket.getKey().toString()聚合字段的相应名称
            // actionTypeBucket.getDocCount()相应聚合结果
            System.out.println(actionTypeBucket.getKey().toString() + ":" + actionTypeBucket.getDocCount());
        }
    }


    /**
     * aggregations 聚合 嵌套 查询
     *
     * 等于:
     * select category, count(*) as category_count , sum(price) as price_sum
     * from docs
     * group by category;
     */
    @Test
    public void aggregationsGroup() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .addAggregation(
                        AggregationBuilders
                                .terms("category_count")
                                .field("category")
                                .subAggregation(
                                        AggregationBuilders
                                                .sum("price_sum")
                                                .field("price")
                                )
                )
                .build();

        //拿到所有结果
        SearchResponse response = elasticsearchTemplate.query(
                searchQuery,
                new ResultsExtractor<SearchResponse>() {
                    @Override
                    public SearchResponse extract(SearchResponse response) {
                        return response;
                    }
                });

        //输出 查询结果列表
        SearchHits hits = response.getHits();
        SearchHit[] hitarr = hits.getHits();
        for (SearchHit hit : hitarr) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);
        }

        //数据聚合
        Aggregations aggregations = response.getAggregations();
        //类似于 map .get
        StringTerms modelTerms = (StringTerms) aggregations.asMap().get("category_count");
        for (Terms.Bucket actionTypeBucket : modelTerms.getBuckets()) {

            //聚合嵌套
            Aggregations aggregations1 = actionTypeBucket.getAggregations();
            //求和
            InternalSum internalSum = (InternalSum) aggregations1.asMap().get("price_sum");

            // actionTypeBucket.getKey().toString()聚合字段的相应名称
            // actionTypeBucket.getDocCount()相应聚合结果
            System.out.println(
                    actionTypeBucket.getKey().toString() + ":"
                            + actionTypeBucket.getDocCount() + " -> "
                            + internalSum.getValue()

            );
        }

    }
}
