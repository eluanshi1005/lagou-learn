package com.lagou.service;

import com.lagou.pojo.Article;
import org.springframework.data.domain.Page;

public interface  ArticleService {
    Page<Article> list(Integer pageNum, Integer pageSize);
    Long count();
}
