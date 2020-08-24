package com.jt.controller;

import com.jt.pojo.Item;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    /**
     * 页面跳转
     */
    @RequestMapping("/page/{moudleName}")
    public String itemAdd(@PathVariable String moudleName) {
        return moudleName;
    }

    @RequestMapping("/saveItem/{title}/{sellPoint}/{price}")
    @ResponseBody
    public Item saveItem(Item item) {
        return item;
    }

    @RequestMapping("/getMsg")
    @ResponseBody
    public String getMsg(){
        return "我是8091...";
    }
}
