package com.lagou.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("index")
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView("login");
        return modelAndView;
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request,  String username, String password){
        if(username.equals("admin") && password.equals("admin")){
            //登陆成功
            request.getSession().setAttribute("username",username);
            return "redirect:/resume/list.action";
        }else{
            return "forward:/login/index.action";
        }
    }
}
