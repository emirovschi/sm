package com.emirovschi.sm.lab5.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController
{
    @RequestMapping(value = "/upload")
    public String upload()
    {
        return "index";
    }
    @RequestMapping(value = "/account")
    public String account()
    {
        return "index";
    }
}
