package com.lagou.controller;

import com.lagou.pojo.Article;
import com.lagou.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 文章
 * YanMei.Li
 */
@Controller
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    // 页面跳转,方便测试
    @RequestMapping("/")
    public String index() {
        return "redirect:/list";
    }

    // 分页查询
    @RequestMapping("/list")
    public String list(Model model, @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "2") Integer pageSize){
        // 查询总条数
        Long count = articleService.count();
        // 分页数据
        Page<Article> articles = articleService.list(pageNum, pageSize);
        // 数据模型
        model.addAttribute("articles", articles);
        model.addAttribute("count", count);
        // 页面
        return "client/index";
    }


}
