package com.AnnotationPlatform.Core.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class homeController {

    @GetMapping("/")
    public String index() {
        return "Admin/adminHome.html";
    }
}
