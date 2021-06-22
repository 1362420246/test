package om.qbk.nosql.elasticsearchdemo;

import com.alibaba.fastjson.JSONObject;
import com.qbk.nosql.elasticsearchdemo.ElasticSearchDemoApplication;
import com.qbk.nosql.elasticsearchdemo.entity.Item;
import org.apache.commons.beanutils.BeanUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * restHighLevelClient 客户端测试
 */
@SpringBootTest(classes = ElasticSearchDemoApplication.class)
public class RestHighLevelClientTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 创建索引
     */
    @Test
    public void createIndex( ) {
        try{
            //创建索引请求
            CreateIndexRequest request = new CreateIndexRequest("qbk3");

            //设置分片和副本数
            request.settings(Settings.builder()
                    .put("index.number_of_shards", 5)
                    .put("index.number_of_replicas", 1)
            );

            //设置 mapping
            //如果不需要指定字段属性可以忽略以下mapping设置，新增文档后会自动创建mapping
            Map <String,Object> id = new HashMap <>();
            id.put("type","text");
            id.put("store",true);

            Map <String,Object> title = new HashMap <>();
            title.put("type","text");
            title.put("store",true);
            title.put("index",true);
            title.put("analyzer", "ik_max_word");

            Map <String,Object> properties = new HashMap <>();
            properties.put("id",id);
            properties.put("title",title);

            Map <String,Object> mapping = new HashMap <>();
            mapping.put("properties",properties);

            //  type
            request.mapping("quboka", mapping);

            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request);

            System.out.println(JSONObject.toJSONString(createIndexResponse));
            System.out.println("索引创建成功");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 查看索引是否存在
     */
    @Test
    public void existsIndex( ) throws IOException {
        GetIndexRequest request = new GetIndexRequest();
        request.indices("qbk3");
        boolean exists = restHighLevelClient.indices().exists(request);
        System.out.println("是否存在:" + exists);
    }

    /**
     * 删除索引
     */
    @Test
    public void delIndex( ) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest();
        request.indices("qbk3");
        restHighLevelClient.indices().delete(request);

    }

    /**
     * 添加
     */
    @Test
    public void add() throws IOException {

        //document  字段可以变
        Map<String,Object> map = new HashMap<>();
        map.put("id","2");
        map.put("title","这是一部不是");

        //设置 index type  id
        IndexRequest request = new IndexRequest("qbk3");
        request.type("quboka");
        request.id( "2" );

        //添加数据  id存在为修改
        request.source( JSONObject.toJSONString(map) , XContentType.JSON);

        IndexResponse response = restHighLevelClient.index(request );

        System.out.println(JSONObject.toJSONString(response));
    }

    /**
     * 批量添加
     */
    @Test
    public void addAll() throws IOException {
        //批量请求
        BulkRequest bulkRequest = new BulkRequest();

        IndexRequest request;

        for (int i = 0; i < 10; i++) {

            request = new IndexRequest("post");

            Map<String,Object> map = new HashMap<>();
            map.put("id", "" + i );
            map.put("title","这是一部不是" + i);

            if( i %2 == 0){
                map.put("name","只有双数才配有名称" + i);
            }

            request.index("qbk3")
                    .id( "" + i)
                    .type("quboka")
                    .source(
                            JSONObject.toJSONString(map),
                            XContentType.JSON
                    );

            bulkRequest.add(request);
        }

        BulkResponse response = restHighLevelClient.bulk(bulkRequest);

        System.out.println(JSONObject.toJSONString(response));
    }

    /**
     *  查询
     */
    @Test
    public void query() throws IOException, InvocationTargetException, IllegalAccessException {
        //"qbk "是es中的索引
        //SearchRequest是包装查询请求
        SearchRequest searchRequest = new SearchRequest("qbk");
        //sourceBuilder 是用来构建查询条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //添加查询条件就和transportClient中的一样了
        sourceBuilder.query(
                QueryBuilders.boolQuery()
                        .must(
                                QueryBuilders.matchQuery("title", "人民5")
                        )
        );
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);

        SearchHit[] searchHits = searchResponse.getHits().getHits();
        //Item 是我定义的实体类
        ArrayList<Item> list = new ArrayList<>();
        //用来转换存储查询到的
        for (SearchHit hit : searchHits) {
            float score = hit.getScore();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            if(score >= 1.0 ){
                System.out.println("符合分数的是:" + sourceAsMap + "得分:" + score );
            }else {
                System.out.println("不符合分数的是:" + sourceAsMap + "得分:" + score );
            }
            Item item = new Item();
            //map赋值给实体类对象（key和对象属性务必一致）
            BeanUtils.populate(item,sourceAsMap);
            list.add(item);
        }
        list.forEach(System.out::println);
    }

    /**
     * 打印结果集
     * @param searchHits SearchHit[]
     */
    private void printResult(SearchHit[] searchHits) {
        int num = 0;
        for (SearchHit searchHit : searchHits) {
            num++;
            // String index = searchHit.getIndex();
            String id = searchHit.getId();
            float score = searchHit.getScore();

            System.out.println("第" + num + "条数据:********" + "id:"+id + "，分数："+score);

            String sourceAsString = searchHit.getSourceAsString();
            // search结果字符串
            System.out.println("result String:" + sourceAsString);
            // search 结果map对象
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            System.out.println(sourceAsMap);
        }
    }

    /**
     * 搜索 索引中所有的doc
     */
    @Test
    public void testSearchAll() throws IOException {
        // 基础设置
        SearchRequest searchRequest = new SearchRequest("qbk");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 搜索方式
        searchSourceBuilder.query(
                QueryBuilders.matchAllQuery()
        );
        searchRequest.source(searchSourceBuilder);

        // 返回字段过滤（可选）
        // 第一个参数为结果集包含哪些字段，第二个参数为结果集不包含哪些字段
        searchSourceBuilder.fetchSource(new String[]{"title","category"}, new String[]{});

        // 发起请求，获取结果
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        // 得到匹配度高的文档
        SearchHit[] searchHits = hits.getHits();
        // 打印结果集
        printResult(searchHits);
    }

    /**
     * 以分页查询的请求方式得到结果
     */
    @Test
    public void testPageSearch() throws IOException {
        // 基础设置
        SearchRequest searchRequest = new SearchRequest("qbk");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 设置分页参数
        searchSourceBuilder.from(2).size(3);
        // 搜索方式
        searchSourceBuilder.query(
                QueryBuilders.matchAllQuery()
        );
        searchRequest.source(searchSourceBuilder);
        // 发起请求，获取结果
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest );
        SearchHits hits = searchResponse.getHits();
        // 得到匹配度高的文档
        SearchHit[] searchHits = hits.getHits();
        // 打印结果集
        printResult(searchHits);
    }

    /**
     * term query查询 该查询为精确查询（不会对查询条件进行分词），在查询时会以查询条件整体去匹配词库中的词（分词后的单个词）
     */
    @Test
    public void testTermSearch() throws IOException {
        // 基础设置
        SearchRequest searchRequest = new SearchRequest("qbk");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 搜索方式
        searchSourceBuilder.query(
                QueryBuilders.termQuery("title", "中华人民")
        );
        searchRequest.source(searchSourceBuilder);
        // 发起请求，获取结果
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest );
        SearchHits hits = searchResponse.getHits();
        // 得到匹配度高的文档
        SearchHit[] searchHits = hits.getHits();
        // 打印结果集
        printResult(searchHits);
    }

    /**
     * 根据id进行精确查询
     */
    @Test
    public void testSearchById() throws IOException {
        // 基础设置
        SearchRequest searchRequest = new SearchRequest("qbk");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        List<String> idList = new ArrayList<>();
        idList.add("200");
        idList.add("202");
        // 搜索方式
        searchSourceBuilder.query(
                QueryBuilders.termsQuery("id", idList)
        );
        searchRequest.source(searchSourceBuilder);
        // 发起请求，获取结果
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest );
        SearchHits hits = searchResponse.getHits();
        // 得到匹配度高的文档
        SearchHit[] searchHits = hits.getHits();
        // 打印结果集
        printResult(searchHits);
    }

    /**
     * 根据关键字搜索
     */
    @Test
    public void testMatchSearch() throws IOException {
        // 基础设置
        SearchRequest searchRequest = new SearchRequest("qbk");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 匹配关键字

        // 写法一：会将"spring实战"分成两个词，只有要有一个匹配成功，则返回该文档(Operator.OR)
        searchSourceBuilder.query(
                QueryBuilders.matchQuery("title", "人民5")
                        .operator(Operator.OR)
        );

        // 写法二:只要有两个词匹配成功，则返回文档（如果是3个词，则是0.7*3，向下取整得到2，匹配到两个词则返回文档）
//        searchSourceBuilder.query(
//                QueryBuilders.matchQuery("title", "人民5")
//                .minimumShouldMatch("70%")
//        );

        searchRequest.source(searchSourceBuilder);
        // 发起请求，获取结果
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest );
        SearchHits hits = searchResponse.getHits();
        // 得到匹配度高的文档
        SearchHit[] searchHits = hits.getHits();
        // 打印结果集
        printResult(searchHits);
    }

    /**
     * 根据关键字搜索（多个域）
     */
    @Test
    public void testMultiMatchSearch() throws IOException {
        // 基础设置
        SearchRequest searchRequest = new SearchRequest("qbk");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // matchQuery（title 、category 两个字段为搜索域，并且至少匹配到70%的词）
        MultiMatchQueryBuilder multiMatchQueryBuilder =
                QueryBuilders.multiMatchQuery("测试3", "title", "category")
                        .minimumShouldMatch("70%")
                        .field("title", 10) ;
        searchSourceBuilder.query(multiMatchQueryBuilder);
        // 给搜索请求对象设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 发起请求，获取结果
        SearchResponse searchResponse =  restHighLevelClient.search(searchRequest );
        SearchHits hits = searchResponse.getHits();
        // 得到匹配度高的文档
        SearchHit[] searchHits = hits.getHits();
        // 打印结果集
        printResult(searchHits);
    }

    /**
     * 布尔查询
     */
    @Test
    public void testBoolSearch() throws IOException {
        // 基础设置
        SearchRequest searchRequest = new SearchRequest("qbk");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // bool搜索
        // 第一个query
        MultiMatchQueryBuilder multiMatchQueryBuilder =
                QueryBuilders.multiMatchQuery("测试3", "title", "category")
                        .minimumShouldMatch("70%")
                        .field("title", 10);
        // 第二个query
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("price", 2.2);

        // 定义一个布尔query，将上面两个条件组合在一起
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(multiMatchQueryBuilder);
        boolQueryBuilder.must(termQueryBuilder);

        searchSourceBuilder.query(boolQueryBuilder);

        // 给搜索请求对象设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 发起请求，获取结果
        SearchResponse searchResponse =  restHighLevelClient.search(searchRequest );
        SearchHits hits = searchResponse.getHits();
        // 得到匹配度高的文档
        SearchHit[] searchHits = hits.getHits();
        // 打印结果集
        printResult(searchHits);
    }

    /**
     * 过滤器（对于搜索结果的过滤，效率高，推荐) + 排序
     */
    @Test
    public void testFilter() throws IOException {
        // 基础设置
        SearchRequest searchRequest = new SearchRequest("qbk");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // bool搜索
        // 第一个query
        MultiMatchQueryBuilder multiMatchQueryBuilder =
                QueryBuilders.multiMatchQuery("测试3", "title", "category")
                        .minimumShouldMatch("70%")
                        .field("title", 10);
        // 第二个query
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("price", 2.2);
        // 定义一个布尔query，将上面两个条件组合在一起
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(multiMatchQueryBuilder);
        boolQueryBuilder.must(termQueryBuilder);
        // 过滤器
        boolQueryBuilder.filter(
                QueryBuilders.termQuery("price", 2.2)
        );
        // 设置
        searchSourceBuilder.query(boolQueryBuilder);
        // 定义排序
        searchSourceBuilder.sort(new FieldSortBuilder("price").order(SortOrder.ASC));

        // 给搜索请求对象设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 发起请求，获取结果
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest );
        SearchHits hits = searchResponse.getHits();
        // 得到匹配度高的文档
        SearchHit[] searchHits = hits.getHits();
        // 打印结果集
        printResult(searchHits);
    }

    /**
     * 范围 查询 分页
     */
    @Test
    public void testRangeQuery() throws IOException {
        int size = 1500;
        LocalDateTime startTime = LocalDateTime.now();
        System.out.println(startTime);

        List<Map<String, Object>> list = new ArrayList<>();

        SearchRequest searchRequest = new SearchRequest("es_fwfblog_09");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryBuilder queryBuilder =
                //以请求开始时间作为查询字段，进行范围查询
                QueryBuilders.rangeQuery("startTime.keyword")
                        //从XXX时间开始
                        .from("2020-09-07 10:58:00,000")
                        //到XXX时间结束
                        .to("2020-09-07 10:59:00,000")
                        //包含上界
                        .includeLower(true)
                        //包含下界
                        .includeUpper(true);

        searchSourceBuilder.query(queryBuilder);

        searchSourceBuilder.from(0).size(size);

        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(360));

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);

        SearchHits hits = searchResponse.getHits();
        for (SearchHit hit : hits) {
            list.add(hit.getSourceAsMap());
        }

        LocalDateTime endTime = LocalDateTime.now();
        Duration result = Duration.between(startTime, endTime);
        System.out.println("size:" + size + ",耗时:" + result.toMillis() +",数量:" + list.size());
    }

    /**
     * 范围 分页 查询 所有
     * size:200,耗时:67573,数量:1389
     * size:100,耗时:94074,数量:1389
     * size:200,耗时:40551,数量:1389
     * size:200,耗时:27429,数量:1389
     * size:200,耗时:22172,数量:1103
     */
    @Test
    public void testRangeQueryAll() throws IOException {
        List<Map<String, Object>> list = new ArrayList<>(1500);
        LocalDateTime startTime = LocalDateTime.now();

        long total;
        int from = 0;
        int size = 200;

        SearchRequest searchRequest = new SearchRequest("es_fwfblog_09");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder =
                //以请求开始时间作为查询字段，进行范围查询
                QueryBuilders.rangeQuery("startTime.keyword")
                        //从XXX时间开始
                        .from("2020-09-07 10:51:00,000")
                        //到XXX时间结束
                        .to("2020-09-07 10:52:00,000")
                        //包含上界
                        .includeLower(true)
                        //包含下界
                        .includeUpper(true);
        searchSourceBuilder.query(queryBuilder);

        do {
            searchSourceBuilder.from(from).size(size);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            SearchHits hits = searchResponse.getHits();
            for (SearchHit hit : hits) {
                list.add(hit.getSourceAsMap());
            }
            total = hits.getHits().length;
            from = from + size;
        } while (total == size);

        LocalDateTime endTime = LocalDateTime.now();
        Duration result = Duration.between(startTime, endTime);
        System.out.println("size:" + size + ",耗时:" + result.toMillis() +",数量:" + list.size());
    }

    /**
     * scroll 查詢 所有
     * size:200,耗时:47261,数量:1389
     * size:200,耗时:48533,数量:1389
     * size:100,耗时:99765,数量:1389
     * size:200,耗时:53824,数量:1103
     */
    @Test
    public void testScroll() throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        LocalDateTime startTime = LocalDateTime.now();

        int size = 200;

        // 初始化scroll
        // 设定滚动时间间隔
        // 这个时间并不需要长到可以处理所有的数据，仅仅需要足够长来处理前一批次的结果。每个 scroll 请求（包含 scroll 参数）设置了一个新的失效时间。
        Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));

        // 新建索引搜索请求
        SearchRequest searchRequest = new SearchRequest("es_fwfblog_09");
        searchRequest.scroll(scroll);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryBuilder queryBuilder =
                //以请求开始时间作为查询字段，进行范围查询
                QueryBuilders.rangeQuery("startTime.keyword")
                        //从XXX时间开始
                        .from("2020-09-07 10:51:00,000")
                        //到XXX时间结束
                        .to("2020-09-07 10:52:00,000")
                        //包含上界
                        .includeLower(true)
                        //包含下界
                        .includeUpper(true);

        searchSourceBuilder.query(queryBuilder);

        searchSourceBuilder.size(size);

        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(360));

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);

        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit hit : searchHits) {
            list.add(hit.getSourceAsMap());
        }

        //遍历搜索命中的数据，直到没有数据
        String scrollId = searchResponse.getScrollId();

        while ( searchHits.length > 0){
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);

            scrollRequest.scroll(scroll);

            searchResponse = restHighLevelClient.searchScroll(scrollRequest);

            scrollId = searchResponse.getScrollId();

            searchHits = searchResponse.getHits().getHits();
            for (SearchHit hit : searchHits) {
                list.add(hit.getSourceAsMap());
            }
        }
        //清除滚屏
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        //也可以选择setScrollIds()将多个scrollId一起使用
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest);
        boolean succeeded = clearScrollResponse.isSucceeded();
        System.out.println("succeeded:" + succeeded);

        LocalDateTime endTime = LocalDateTime.now();
        Duration result = Duration.between(startTime, endTime);
        System.out.println("size:" + size + ",耗时:" + result.toMillis() +",数量:" + list.size());
    }
}
