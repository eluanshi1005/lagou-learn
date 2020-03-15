package com.lagou.demo.controller;

import com.lagou.demo.service.IDemoService;
import com.lagou.edu.mvcframework.annotations.LagouAutowired;
import com.lagou.edu.mvcframework.annotations.LagouController;
import com.lagou.edu.mvcframework.annotations.LagouRequestMapping;
import com.lagou.edu.mvcframework.annotations.Security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@LagouController
@LagouRequestMapping("/demo")
@Security({"queen"})
public class DemoController {


    @LagouAutowired
    private IDemoService demoService;


    /**
     * URL: /demo/query?name=lisi
     * @param request
     * @param response
     * @param name
     * @return
     */
    @LagouRequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response,String name) throws ServletException, IOException {
        String s = demoService.get(name);

    }

    @LagouRequestMapping("/handler0")
    public void handler0(HttpServletRequest request, HttpServletResponse response,String username) throws ServletException, IOException {
        System.out.println("handler0: 我可以被 【queen】 访问，现在访问我的是 " + username);

    }

    @LagouRequestMapping("/handler1")
    @Security("user1")
    public void handler1(HttpServletRequest request, HttpServletResponse response,String username) throws ServletException, IOException {
        System.out.println("handler1: 我可以被 【queen,user1】 访问，现在访问我的是 " + username);

    }

    @LagouRequestMapping("/handler23")
    @Security({"user2","user3"})
    public void handler23(HttpServletRequest request, HttpServletResponse response,String username) throws ServletException, IOException {
        System.out.println("handler23: 我可以被 【queen,user2,user3】 访问，现在访问我的是 " + username);

    }
}
