package com.lagou.service.impl;

import com.lagou.pojo.Article;
import com.lagou.repository.ArticleRepository;
import com.lagou.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * YanMei.Li
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Long count() {
        return articleRepository.count();
    }

    @Override
    public Page<Article> list(Integer pageNum, Integer pageSize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Article> articles = articleRepository.findAll(pageable);
        return articles;
    }
}
