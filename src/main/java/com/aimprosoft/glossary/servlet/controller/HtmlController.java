package com.aimprosoft.glossary.servlet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller which simply handles *.html requests
 */
@Controller
public class HtmlController extends BaseController{

    @RequestMapping("/")
    public String welcome(){
        return "redirect:/index.html";
    }

    @RequestMapping("index.html")
    public String index(){
        return "/index";
    }

    @RequestMapping("login.html")
    public String login(){
        return "/login";
    }

}
