package com.lagou.edu.controller;

import com.lagou.edu.dao.ResumeDao;
import com.lagou.edu.pojo.Resume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private ResumeDao resumeDao; // 由于逻辑简单，此处省略 service层，直接调用 dao 层。
    
    @RequestMapping("/list")
    public ModelAndView index(){
        List<Resume> list = resumeDao.findAll();
        ModelAndView modelAndView = new ModelAndView("/resume/list");
        modelAndView.addObject("list",list);
        return modelAndView;
    }

    // 跳转新增 Resume 信息页面
    @RequestMapping("/add")
    public ModelAndView add(){
        List<Resume> list = resumeDao.findAll();
        ModelAndView modelAndView = new ModelAndView("/resume/add");
        modelAndView.addObject("list",list);
        return modelAndView;
    }

    @RequestMapping("/save")
    public String save(Resume resume){
        System.out.println(resume);
        resumeDao.save(resume);
        return "redirect:/resume/list.action";
    }

    @RequestMapping("/update")
    public ModelAndView update(String id){
        Optional<Resume> byId = resumeDao.findById(Long.parseLong(id));
        Resume resume = byId.get();
        ModelAndView modelAndView = new ModelAndView("/resume/update");
        modelAndView.addObject("resume",resume);
        return modelAndView;
    }

    @RequestMapping("/delete")
    public String delete(String id){
        resumeDao.deleteById(Long.parseLong(id));
        return "redirect:/resume/list.action";
    }
}
