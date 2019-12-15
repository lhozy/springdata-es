package com.lhozy.test;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.lhozy.entity.Article;
import com.lhozy.repositories.ArticleRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ElasticsearchTemplate template;

    /**
     * 创建索引并且mappings
     */
    @Test
    public void testCreateIndex(){
        //创建索引并配置映射关系 @Document(indexName = "index_blog",type = "article")
        template.createIndex(Article.class);
        //mapping
        //template.putMapping(Article.class);
    }

    /**
     * 添加文档article
     */
    @Test
    public void add(){
        Article article = new Article();
        article.setId(Long.valueOf(2));
        article.setTitle("阿里云云栖社区为您免费提供2");
        article.setContent("今天来看一下如何配置maven中的镜像2");
        articleRepository.save(article);

    }
    @Test
    public void batch(){
        Article article = new Article();
        for (int i =15;i<30;i++){
            article.setId(Long.valueOf(i));
            article.setTitle("阿里云云栖社区为您免费提供" + i);
            article.setContent("今天来看一下如何配置maven中的镜像" + i);
            articleRepository.save(article);

        }
    }

    /**
     * 删除文档
     */
    @Test
    public void delete(){
        articleRepository.deleteById(Long.valueOf(1));
        //articleRepository.deleteAll();
    }

    /**
     * 更新文档
     */
    @Test
    public void update(){
        Article article = new Article();
        article.setId(Long.valueOf(1));
        article.setTitle("中国社区很漂亮");
        article.setContent("11今天看一下如何配置maven中的镜像");
        articleRepository.save(article);
    }

    /**
     * 查询所有
     */
    @Test
    public void findAll(){
        Iterable<Article> articles = articleRepository.findAll();
        articles.forEach(article -> System.out.println(article));
    }

    /**
     * 根据id查询
     */
    @Test
    public void findById(){
        Optional<Article> optional = articleRepository.findById(Long.valueOf(1));
        Article article = optional.get();
        System.out.println(article);

    }

    /**
     * 自定义查询 findByTitle，只需要按照SpringDateES命名规则定义方法即可，无需实现
     */
    @Test
    public void findByTitle(){
        List<Article> articles = articleRepository.findByTitle("社区");
        System.out.println("条目："+articles.size());
        articles.stream().forEach(article -> System.out.println(article));
    }

    /**
     * 自定义的分词之间默认的是and，所以"提供给我的免费"这个就查不到任何记录，
     * 可以使用NativeSearchQuery创建原生的查询来解决：如测试方法nativeSearchQuery
     */
    @Test
    public void findByTitlePage(){
        Pageable pageable = PageRequest.of(0, 40);
        //自定义的分词之间默认的是and，所以"提供给我的免费"这个就查不到
        List<Article> articles = articleRepository.findByTitle("提供给我的免费",pageable);
        System.out.println("条目："+articles.size());
        articles.stream().forEach(article -> System.out.println(article));
    }
    @Test
    public void nativeSearchQuery(){
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery("提供给我的免费").defaultField("title"))
                .withPageable(PageRequest.of(0,40))
                .build();
        List<Article> articles = template.queryForList(query, Article.class);
        System.out.println("条目："+articles.size());
        articles.stream().forEach(article -> System.out.println(article));

    }

    /**
     * 带分页的自定义查询
     * @throws Exception
     */
    @Test
    public void findByTitleOrContent() throws Exception{
        Pageable pageable = PageRequest.of(0, 20);
        List<Article> articles = articleRepository.findByTitleOrContent("社区","麻烦",pageable);
        System.out.println("条目："+articles.size());
        articles.stream().forEach(article -> System.out.println(article));
    }
    @Test
    public void findByTitleAndContent(){
        Pageable pageable = PageRequest.of(0,40);
        List<Article> articles = articleRepository.findByTitleAndContent("社区", "麻烦", pageable);
        System.out.println("条目："+articles.size());
        articles.stream().forEach(article -> System.out.println(article));
    }
}
