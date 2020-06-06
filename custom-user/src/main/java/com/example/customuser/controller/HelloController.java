package com.example.customuser.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date: 2020/6/3 17:38
 */
@RestController
public class HelloController {

    @PostMapping("/hello")
    public String hello(){
        return "hello security";
    }

    @PostMapping("/admin/hello")
    public String adminHello(){
        return "admin hello";
    }

    @PostMapping("/user/hello")
    public String userHello(){
        return "user hello";
    }
}