package com.aimprosoft.glossary.servlet.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller which simply handles *.html requests
 */
@Controller
public class HtmlController {

    protected Logger _logger = LoggerFactory.getLogger(getClass());

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
