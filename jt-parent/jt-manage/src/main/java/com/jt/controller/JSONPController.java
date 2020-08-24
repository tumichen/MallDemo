package com.jt.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JSONPController {
    /**
     * jsonp返回值结果必须结果特殊搁格式封装
     */
    @RequestMapping("/web/testJSONP")
    public JSONPObject testJSONP(String callback){
        ItemCat item = new ItemCat();
        item.setId(1000l).setName("jsonp测试");
        JSONPObject object = new JSONPObject(callback, item);
        return object;
    }

}
