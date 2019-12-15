package com.lhozy.repositories;

import com.lhozy.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ArticleRepository extends ElasticsearchRepository<Article,Long> {
    /**
     * 根据Title查询
     * @param title
     * @return
     */
    List<Article> findByTitle(String title);

    /**
     * 根据Title查询，带分页，分页是从0页开始
     * @param title
     * @param pageable
     * @return
     */
    List<Article> findByTitle(String title,Pageable pageable);

    /**
     * 根据Title或者Content查询，默认是10条
     * @param title
     * @param content
     * @return
     */
    List<Article> findByTitleOrContent(String title,String content);

    /**
     * 根据Title或者Content查询，带分页
     * @param title
     * @param content
     * @param pageable
     * @return
     */
    List<Article> findByTitleOrContent(String title, String content, Pageable pageable);

    /**
     * 根据Title并且Content查询,默认是10条
     * @param title
     * @param content
     * @return
     */
    List<Article> findByTitleAndContent(String title,String content);

    /**
     * 根据Title并且Content查询，带分页
     * @param title
     * @param content
     * @param pageable
     * @return
     */
    List<Article> findByTitleAndContent(String title,String content, Pageable pageable);
}
