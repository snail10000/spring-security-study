package com.example.formlogin2;

import org.springframework.web.bind.annotation.*;

/**
 * @Date: 2020/5/30 10:48
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello spring security";
    }


    @RequestMapping("/s")
    public String s(){
        return "login success!";
    }
}