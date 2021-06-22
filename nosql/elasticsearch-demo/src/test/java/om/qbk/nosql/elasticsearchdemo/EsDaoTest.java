package om.qbk.nosql.elasticsearchdemo;

import com.qbk.nosql.elasticsearchdemo.ElasticSearchDemoApplication;
import com.qbk.nosql.elasticsearchdemo.dao.ArticleDao;
import com.qbk.nosql.elasticsearchdemo.entity.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ElasticsearchRepository　测试
 */
@SpringBootTest(classes = ElasticSearchDemoApplication.class)
public class EsDaoTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ArticleDao articleDao;

    /**
     * 创建索引
     */
    @Test
    public void createIndex2() {
        // 创建索引，会根据Item类的@Document注解信息来创建
        boolean result = elasticsearchTemplate.createIndex(Article.class);
        System.out.println(result);

        // 配置映射，会根据Item类中的@Id、@Field等字段来自动完成映射
        result = elasticsearchTemplate.putMapping(Article.class);
        System.out.println(result);
    }

    //插入
    @Test
    public void testSave(){
        //创建文档
        Article article = new Article();
        article.setId(1);
        article.setTitle("测试elasticsearch");
        article.setContext("20200829测试用");
        //保存文档
        articleDao.save(article);
    }

    //修改
    @Test
    public void testUpdate(){
        //判断数据库中是否有你指定的id的文档,如果没有,就进行保存,如果有,就进行更新
        Article article = new Article();
        article.setId(1);
        article.setTitle("测试elasticsearch02");
        article.setContext("20200829测试用02");
        articleDao.save(article);
    }

    //删除
    @Test
    public void testDelete(){
        //根据主键删除
        articleDao.deleteById(1);
    }

    //批量插入
    @Test
    public void addAll(){
        List<Article> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            //创建文档
            Article article = new Article();
            article.setId(i);
            article.setTitle("20200829测试" + i);
            article.setContext("20200829测试，看看行不行" + i);
            article.setHits(100 + i);
            list.add(article);
        }
        //保存文档
        articleDao.saveAll(list);
    }

    //查询所有
    @Test
    public void testFindAll(){
        Iterable<Article> all = articleDao.findAll();
        for (Article article : all) {
            System.out.println(article);
        }
    }

    //主键查询
    @Test
    public void testFindById(){
        Optional<Article> opt = articleDao.findById(5);
        System.out.println(opt.orElse(null));
    }

    //分页 搜索
    @Test
    public void testFindAllWithPage(){
        //设置分页条件
        //page代表的页码,从0开始
        Pageable pageable = PageRequest.of(1, 3);
        Page<Article> page = articleDao.findAll(pageable);
        for (Article article : page.getContent()) {
            System.out.println(article);
        }
    }

    //排序 搜索
    @Test
    public void testFindAllWithSort(){
        //设置排序条件
        Sort sort = Sort.by(Sort.Order.desc("hits"));
        Iterable<Article> all = articleDao.findAll(sort);
        for (Article article : all) {
            System.out.println(article);
        }
    }

    //分页+排序 搜索
    @Test
    public void testFindAllWithPageAndSort(){
        //设置排序条件
        Sort sort = Sort.by(Sort.Order.desc("hits"));
        //设置分页条件
        //page代表的页码,从0开始
        Pageable pageable =PageRequest.of(1, 3, sort);
        Page<Article>page = articleDao.findAll(pageable);
        for (Article article : page.getContent()) {
            System.out.println(article);
        }
    }

    //根据标题 搜索
    @Test
    public void testFindByTitle(){
        List<Article> articles = articleDao.findByTitle("测试5");
        for (Article article : articles) {
            System.out.println(article);
        }
    }


    //根据标题 搜索
    @Test
    public void testFindByTitleOrContext(){
        List<Article> articles= articleDao.findByTitleOrContext("测试", "看看行不行5");
        for (Article article : articles) {
            System.out.println(article);
        }
    }

    //根据标题 搜索
    @Test
    public void testFindByTitleOrContextWithPage(){
        //设置排序条件
        Sort sort = Sort.by(Sort.Order.desc("hits"));
        //设置分页条件
        //page代表的页码,从0开始
        Pageable pageable =PageRequest.of(1, 3, sort);
        List<Article>articles = articleDao.findByTitleOrContext("测试", "测试",pageable);
        for (Article article : articles) {
            System.out.println(article);
        }
    }
}
